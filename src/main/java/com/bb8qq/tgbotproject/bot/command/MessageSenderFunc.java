package com.bb8qq.tgbotproject.bot.command;


import com.bb8qq.tgbotproject.bot.Command;
import com.bb8qq.tgbotproject.bot.TgCommand;
import com.bb8qq.tgbotproject.lib.MsgType;
import com.bb8qq.tgbotproject.lib.TaskTurn;
import com.bb8qq.tgbotproject.lib.dto.ChatMessages;
import com.bb8qq.tgbotproject.lib.dto.ChatMessagesUser;
import com.bb8qq.tgbotproject.service.TaskTurnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Command(commands = "/sender_message_func")
public class MessageSenderFunc extends TgCommand {

    @Autowired
    private TaskTurnService taskTurnService;

    @PostConstruct
    public void initFunc() {
        taskTurnService.addCall(TaskTurn._TASK_USER_BOT, o -> {
            if (o instanceof ChatMessages chatMessages) {
                sendAllMessage(chatMessages);
            }
        });
    }

    private void sendAllMessage(ChatMessages chatMessages) {
        for (ChatMessagesUser cm : chatMessages.getChatIds()) {
            SendMessage sm = new SendMessage();
            sm.setText(message(cm, chatMessages.getMsgType(), chatMessages.getChannelName(), chatMessages.getMessage()));
            sm.setChatId(cm.getChatId().toString());
            try {
                tlp.execute(sm);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(25);
            } catch (Exception e) {

            }
        }
    }

    private String message(ChatMessagesUser msg, MsgType msgType, String channel, String ms) {
        StringBuffer sb = new StringBuffer();
        sb.append("Новое ");
        switch (msgType) {
            case MESSAGE:
                sb.append("сообщение");
                break;
            case PHOTO:
                sb.append("фото");
                break;
            case VIDEO:
                sb.append("видео");
                break;
            case POLL:
                sb.append("-опрос-");
                break;
            case ANIMATION:
                sb.append("-анимация-");
                break;
            default:
                sb.append("---");
                break;
        }
        sb.append(" на канале [");
        sb.append(channel);
        sb.append("] по фильтру '");
        sb.append(msg.getWord());
        sb.append("]\n");
        sb.append(ms);
        return sb.toString();
    }

    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        return null;
    }


}
