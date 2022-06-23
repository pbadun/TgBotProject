package com.bb8qq.tgbotproject.bot.command.base;

import com.bb8qq.tgbotproject.bot.Command;
import com.bb8qq.tgbotproject.bot.TgCommand;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

//TODO Задел на будущее... В меню - скрыт.
@Deprecated
@Command(commands = "/Донат")
public class TgCommandDonate extends TgCommand {
    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        sendMessage(chatId, "Поддержать разработчи можно перечислив любую сумму в жидкой вовлюте на счет в Баре!");
        return -1;
    }
}
