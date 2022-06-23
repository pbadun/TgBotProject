package com.bb8qq.tgbotproject.bot.command.base;

import com.bb8qq.tgbotproject.bot.Command;
import com.bb8qq.tgbotproject.bot.TgCommand;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

//TODO Функционал на будущее !
@Command(commands = "/Справка")
public class TgCommandHelp extends TgCommand {
    private String[] messages = {
            "Тут по идее должна быть справка о возможностях Телеграмм бота...",
            "Но, Мне лень писать!"
    };

    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        StringBuffer sb = new StringBuffer();
        for (String m : messages) {
            sb.append(m);
            sb.append("\n");
        }
        sendMessage(chatId, sb.toString());
        return -1;
    }
}
