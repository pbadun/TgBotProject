package com.bb8qq.tgbotproject.bot.command;

import com.bb8qq.tgbotproject.bot.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Command(commands = "/start,/старт,/начать")
public class TgCommandStart extends TgCommand {

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
