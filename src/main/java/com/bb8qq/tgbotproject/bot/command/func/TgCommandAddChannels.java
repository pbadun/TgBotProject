package com.bb8qq.tgbotproject.bot.command.func;

import com.bb8qq.tgbotproject.bot.Command;
import com.bb8qq.tgbotproject.bot.TgCommand;
import com.bb8qq.tgbotproject.lib.TaskTurn;
import com.bb8qq.tgbotproject.lib.task.JoinChatRequest;
import com.bb8qq.tgbotproject.lib.task.SearchChatsRequest;
import com.bb8qq.tgbotproject.lib.task.SearchChatsResponse;
import com.bb8qq.tgbotproject.service.LastMsgService;
import com.bb8qq.tgbotproject.service.TaskTurnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Command(commands = "/Добавить")
@Slf4j
public class TgCommandAddChannels extends TgCommand {

    @Autowired
    private LastMsgService lastMsgService;

    @Autowired
    private TaskTurnService taskTurnService;

    @PostConstruct
    public void initCallBack() {
        taskTurnService.addCall(TaskTurn._TASK_RESULT_SEARCH_CHATS, o -> {
            try {
                callSearch((SearchChatsResponse) o);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });
    }

    private void callSearch(SearchChatsResponse r) throws TelegramApiException {
        if (r.getChatId() == null) {
            sendMessage(r.getChatId(), "Ничего не найденно!");
        } else {
            addGroupMessage(r.getGroupId(), "Чат [ID: " + r.getGroupId() + "]: '" + r.getTitle() + "'", r);
        }
    }

    private void addGroupMessage(Long groupId, String title, SearchChatsResponse r) {
        InlineKeyboardButton k = new InlineKeyboardButton();
        k.setText("Добавить");
        k.setCallbackData("add:" + groupId);
        InlineKeyboardMarkup im = new InlineKeyboardMarkup();
        im.setKeyboard(Collections.singletonList(Collections.singletonList(k)));
        try {
            SendMessage m = new SendMessage();
            m.setChatId(r.getChatId().toString());
            m.setText(title);
            m.setReplyMarkup(im);
            tlp.execute(m);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        taskTurnService.runCall(TaskTurn._TASK_JOIN_CHAT, new JoinChatRequest(r.getChatId(), groupId));
    }


    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        if (update.hasMessage()) {
            //forward(update, chatId);

            switch (step) {
                case 0:
                    sendMessage(chatId, "Для добавление нового канала отправьте ссылку или имя канала ");
                    return 1;
                case 1:
                    lastMsgService.save(chatId, update.getMessage().getText());
                    sendMessage(chatId, "Поиск каналов...");
                    taskTurnService.runCall(TaskTurn._TASK_SEARCH_CHATS, new SearchChatsRequest(update.getMessage().getText(), chatId));
                    return 2;
                case 2:
                    sendMessage(chatId, "Еще разок....");
                    taskTurnService.runCall(TaskTurn._TASK_SEARCH_CHATS, new SearchChatsRequest(update.getMessage().getText(), chatId));
                    return 2;

            }

        } else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            log.warn(data);
            return step;
        }
        return null;
    }

    private void forward(Update update, Long chatId) {
        Message message = update.getMessage();
        //log.error(message.toString());
        //update.getMessage().getMessageId();
        try {
            ForwardMessage mm = new ForwardMessage();
            mm.setChatId(chatId.toString());
            //mm.setReplyToMessageId(message.getMessageId());
            //mm.setText("lol");
            mm.setFromChatId(chatId.toString());
            mm.setMessageId(message.getMessageId());
            tlp.execute(mm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
