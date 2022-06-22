package com.bb8qq.tgbotproject.userbot;

import com.bb8qq.tgbotproject.lib.TaskTurn;
import com.bb8qq.tgbotproject.lib.task.JoinChatRequest;
import com.bb8qq.tgbotproject.lib.task.SearchChatsRequest;
import com.bb8qq.tgbotproject.service.LoggerService;
import com.bb8qq.tgbotproject.service.TaskTurnService;
import com.bb8qq.tgbotproject.userbot.func.FuncJoinChat;
import com.bb8qq.tgbotproject.userbot.func.FuncSearchPublicChat;
import it.tdlight.client.APIToken;
import it.tdlight.client.AuthenticationData;
import it.tdlight.client.SimpleTelegramClient;
import it.tdlight.client.TDLibSettings;
import it.tdlight.common.Init;
import it.tdlight.common.utils.CantLoadLibrary;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
@Slf4j
public class UserBotTgComponent {

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

    @Autowired
    private TaskTurnService taskTurn;

    @Autowired
    private LoggerService loggerService;

    private SimpleTelegramClient client;

    //---------------------------------------------------------------------------------------------------------

    /**
     * Инициализация ЮзерБота
     *
     * @throws CantLoadLibrary
     * @throws InterruptedException
     */
    public void init() throws CantLoadLibrary, InterruptedException {
        Init.start();

        var apiToken = new APIToken(tgApiId, tgToken);
        var settings = TDLibSettings.create(apiToken);

        // Настройки хранения данных клиента
        var sessionPath = Paths.get(tgSessionFile);
        settings.setDatabaseDirectoryPath(sessionPath.resolve(sessionData));
        settings.setDownloadedFilesDirectoryPath(sessionPath.resolve(sessionDownloads));
        client = new SimpleTelegramClient(settings);

        // Configure the authentication info
        var authenticationData = AuthenticationData.consoleLogin();
        //-----------------------
        // Функции обработки событии от ТГ клиента.
        // Add an example update handler that prints when the bot is started
        client.addUpdateHandler(TdApi.UpdateAuthorizationState.class, this::onUpdateAuthorizationState);

        // Add an example update handler that prints every received message
        client.addUpdateHandler(TdApi.UpdateNewMessage.class, this::onUpdateNewMessage);

        //-----------------------
        // Start the client
        client.start(authenticationData);

        initFunc();
    }

    /**
     * Функции - запросы к ЮзерБоту...
     */
    private void initFunc() {
        // Поиск группы
        taskTurn.addCall(TaskTurn._TASK_SEARCH_CHATS, o -> {
            new FuncSearchPublicChat(client, o1 -> {
                taskTurn.runCall(TaskTurn._TASK_RESULT_SEARCH_CHATS, o1);
            }).SearchPublicChat((SearchChatsRequest) o);
        });
        // Присоедениться к группе
        taskTurn.addCall(TaskTurn._TASK_JOIN_CHAT, o -> {
            new FuncJoinChat(client, o1 -> {
                taskTurn.runCall(TaskTurn._TASK_RESULT_JOIN_CHAT, o1);
            }).joinChat((JoinChatRequest) o);
        });

    }


    /**
     * Статус бота.
     */
    private void onUpdateAuthorizationState(TdApi.UpdateAuthorizationState update) {
        var authorizationState = update.authorizationState;
        String l = "";
        if (authorizationState instanceof TdApi.AuthorizationStateReady) {
            l = "Logged in";
        } else if (authorizationState instanceof TdApi.AuthorizationStateClosing) {
            l = "Closing...";
        } else if (authorizationState instanceof TdApi.AuthorizationStateClosed) {
            l = "Closed";
        } else if (authorizationState instanceof TdApi.AuthorizationStateLoggingOut) {
            l = "Logging out...";
        }
        log.warn(l);
        loggerService.p(l, "userbot");
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Прилетают вообще все сообщения, в т.ч. от самого бота.
     */
    private void onUpdateNewMessage(TdApi.UpdateNewMessage update) {
        var messageContent = update.message.content;
        String text;
        if (messageContent instanceof TdApi.MessageText messageText) {
            text = messageText.text.text;
        } else {
            text = String.format("(%s)", messageContent.getClass().getSimpleName());
        }
        //log.error(text);
        //log.error(update.toString());
        //------------------
        /*
        client.send(new TdApi.GetChat(update.message.chatId), chatIdResult -> {
            var chat = chatIdResult.get();
            var chatName = chat.title;
            long id = chat.id;
            log("Msg chat [" + id + "][" + chatName + "]: " + text);
            //taskTurn.runCall("wea", "UserBot Chat [" + id + "][" + chatName + "]: " + text);
        });*/

    }

}
