package com.bb8qq.tgbotproject.bot.command;

import com.bb8qq.tgbotproject.bot.Command;
import com.bb8qq.tgbotproject.bot.TgCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Command(commands = "", isEnd = true)
public class TgCommandEnd extends TgCommand {

    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        SendMessage m = new SendMessage();
        m.setChatId(chatId.toString());
        m.setText("Привет! Нажми /start /session для запуска бота!");
        m.setReplyMarkup(null);
        tlp.execute(m);
        return null;
    }

}
