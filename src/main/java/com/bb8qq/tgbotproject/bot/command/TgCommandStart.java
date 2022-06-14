package com.bb8qq.tgbotproject.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TgCommandStart extends TgCommand {

    @Override
    public String commands() {
        return "/start,/старт,/начать";
    }

    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        SendMessage s = new SendMessage();
        s.setText("Модуль TgCommandStart, шаг: " + step);
        s.setChatId(chatId.toString());
        tlp.execute(s);
        if (step == 3) {
            return null;
        }
        step++;
        return step;
    }
}
