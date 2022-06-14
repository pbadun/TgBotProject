package com.bb8qq.tgbotproject.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TgCommandEnd extends TgCommand {

    @Override
    public String commands() {
        return "*";
    }

    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        SendMessage m = new SendMessage();
        m.setChatId(chatId.toString());
        m.setText("Привет! Нажми /start /session для запуска бота!");
        m.setReplyMarkup(null);
        tlp.execute(m);
        return 0;
    }

}
