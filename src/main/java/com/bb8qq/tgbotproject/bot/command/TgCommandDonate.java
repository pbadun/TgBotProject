package com.bb8qq.tgbotproject.bot.command;

import com.bb8qq.tgbotproject.bot.Command;
import com.bb8qq.tgbotproject.bot.TgCommand;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Command(commands = "/Донат")
public class TgCommandDonate extends TgCommand {
    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        return null;
    }
}
