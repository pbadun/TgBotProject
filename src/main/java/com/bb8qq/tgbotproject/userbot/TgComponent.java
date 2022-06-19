package com.bb8qq.tgbotproject.userbot;

import com.bb8qq.tgbotproject.service.TaskTurnService;
import it.tdlight.client.*;
import it.tdlight.common.Init;
import it.tdlight.common.utils.CantLoadLibrary;
import it.tdlight.jni.TdApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;

@Component
public class TgComponent {

    @Autowired
    private TaskTurnService taskTurn;

    private HashSet<Long> chatsId;

    private static int i = 0;

    //------------------------------------------------
    // Параметры модуля

    @Value("${userbot.token}")
    private String tgToken;

    @Value("${userbot.apiid}")
    private Integer tgApiId;

    @Value("${userbot.session-file}")
    private String tgSessionFile;

    @Value("${userbot.session-downloads}")
    private String sessionDownloads;

    @Value("${userbot.session-data}")
    private String sessionData;

    //------------------------------------------------

    private SimpleTelegramClient client;

    //---------------------------------------------------------------------------------------------------------

    /**
     * Инициализация ЮзерБота
     *
     * @throws CantLoadLibrary
     * @throws InterruptedException
     */
    public void init() throws CantLoadLibrary, InterruptedException {
        chatsId = new HashSet<>();
        Init.start();

        taskTurn.addCall("bot", o -> {
            sendToUserAgent(o.toString());
        });

        var apiToken = new APIToken(tgApiId, tgToken);
        var settings = TDLibSettings.create(apiToken);

        // Настройки хранения данных клиента
        var sessionPath = Paths.get(tgSessionFile);
        settings.setDatabaseDirectoryPath(sessionPath.resolve(sessionData));
        settings.setDownloadedFilesDirectoryPath(sessionPath.resolve(sessionDownloads));
        client = new SimpleTelegramClient(settings);

        // Configure the authentication info
        var authenticationData = AuthenticationData.consoleLogin();

        // Add an example update handler that prints when the bot is started
        client.addUpdateHandler(TdApi.UpdateAuthorizationState.class, this::onUpdateAuthorizationState);

        // Add an example update handler that prints every received message
        client.addUpdateHandler(TdApi.UpdateNewMessage.class, this::onUpdateNewMessage);

        // Add an example command handler that stops the bot
        client.addCommandHandler("stop", new StopCommandHandler());

        // Start the client
        client.start(authenticationData);
    }

    private void sendToUserAgent(String msg) {
        for (Long c : chatsId) {
            //sendMsg(c, msg);
        }
    }


    /**
     * Print new messages received via updateNewMessage
     * Прилетают вообще все сообщения, в т.ч. от самого бота.
     */
    private void onUpdateNewMessage(TdApi.UpdateNewMessage update) {
        chatsId.add(update.message.chatId);
        var messageContent = update.message.content;
        String text;
        if (messageContent instanceof TdApi.MessageText messageText) {
            text = messageText.text.text;
        } else {
            text = String.format("(%s)", messageContent.getClass().getSimpleName());
        }

        //------------------

        client.send(new TdApi.GetChat(update.message.chatId), chatIdResult -> {
            var chat = chatIdResult.get();
            var chatName = chat.title;
            long id = chat.id;
            log("Msg chat [" + id + "][" + chatName + "]: " + text);
            taskTurn.runCall("wea", "UserBot Chat [" + id + "][" + chatName + "]: " + text);
        });

        //------------------

        //log(update.toString());

        if (i < 1) {
            //sendMsg(update.message.chatId, "UserBot: " + text);
            i++;
        } else {
            i = 0;
        }

    }

    private void sendMsg(Long chatId, String msg) {
        TdApi.FormattedText text = new TdApi.FormattedText(msg, null);
        TdApi.InputMessageContent c = new TdApi.InputMessageText(text, false, false);
        TdApi.SendMessage ms = new TdApi.SendMessage(
                chatId,
                0,
                0,
                null,
                null,
                c
        );
        client.send(ms, new GenericResultHandler<TdApi.Message>() {
            @Override
            public void onResult(Result<TdApi.Message> result) {
                log(result.toString());
            }
        });
    }

    /**
     * Close the bot if the /stop command is sent by the administrator
     */
    private class StopCommandHandler implements CommandHandler {

        @Override
        public void onCommand(TdApi.Chat chat, TdApi.MessageSender commandSender, String arguments) {
            // Check if the sender is the admin
            if (isAdmin(commandSender)) {
                // Stop the client
                System.out.println("Received stop command. closing...");
                client.sendClose();
            }
        }
    }

    /**
     * Print the bot status
     */
    private void onUpdateAuthorizationState(TdApi.UpdateAuthorizationState update) {
        var authorizationState = update.authorizationState;
        if (authorizationState instanceof TdApi.AuthorizationStateReady) {
            System.out.println("Logged in");
        } else if (authorizationState instanceof TdApi.AuthorizationStateClosing) {
            System.out.println("Closing...");
        } else if (authorizationState instanceof TdApi.AuthorizationStateClosed) {
            System.out.println("Closed");
        } else if (authorizationState instanceof TdApi.AuthorizationStateLoggingOut) {
            System.out.println("Logging out...");
        }
    }

    /**
     * Check if the command sender is admin
     */
    private boolean isAdmin(TdApi.MessageSender sender) {
        return false;//return sender.equals(ADMIN_ID);
    }


    //---------------------------------------------------------------------------------------------------------

    /**
     * поиск публичных чатов.
     *
     * @param query
     */
    public void searchPublicChats(String query) {
        // Поиск публицных Чатов?
        client.send(new TdApi.SearchPublicChats(query), result -> {
            log(result.toString());
            log(Arrays.toString(result.get().chatIds));
            log("-----------");
            for (long l : result.get().chatIds) {
                chatInfo(l);
            }
            log("-----------");
        });


    }

    /**
     * Поис по номеру телефона.
     *
     * @param phone
     */
    public void searchUserByPhoneNumber(String phone) {
        // Поик по номеру?
        client.send(new TdApi.SearchUserByPhoneNumber(phone), result -> {
            log(result.toString());
        });
    }

    /**
     * Добавиться в группу по ID ?
     *
     * @param chatId
     */
    public void joinChat(long chatId) {
        client.send(new TdApi.JoinChat(chatId), result -> {
            log(result.toString());
        });
    }

    /**
     * Добавиться по ссылке приглашению?
     *
     * @param link
     */
    public void joinChatByInviteLink(String link) {
        client.send(new TdApi.JoinChatByInviteLink(link), result -> {
            //TdApi.Chat chat = result.get();
            log(result.toString());
        });
    }

    /**
     * Информация о чате по ID
     *
     * @param chatId
     */
    public void chatInfo(long chatId) {
        client.send(new TdApi.GetChat(chatId), result -> {
            log(Long.toString(chatId));
            log(result.toString());
        });
    }

    public void getChatHistory(long chatId) {
        client.send(new TdApi.GetChatHistory(chatId, 0, 0, 50, false), result -> {
            Arrays.stream(result.get().messages).forEach(r -> {

            });
        });
    }

    private void log(String msg) {
        System.out.println(msg);
    }

}
