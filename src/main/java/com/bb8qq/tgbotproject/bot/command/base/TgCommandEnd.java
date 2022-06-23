package com.bb8qq.tgbotproject.bot.command.base;

import com.bb8qq.tgbotproject.bot.Command;
import com.bb8qq.tgbotproject.bot.TgCommand;
import com.bb8qq.tgbotproject.model.TgUser;
import com.bb8qq.tgbotproject.reposetory.TgUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Command(commands = "", isEnd = true)
public class TgCommandEnd extends TgCommand {

    @Autowired
    private TgUserRepo tgUserRepo;

    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        TgUser u = tgUserRepo.findTgUserByChatId(chatId);
        if (u == null) {
            sendMessage(chatId,
                    "Для использования бота необходимо пройти регистрацию.",
                    keyboardMarkup(new String[]{"/Регистрация"})
            );
        } else {
            String msg;
            if (step != null && step == -1) {
                msg = String.format("Выберите комманду.");
            } else {
                msg = String.format("Комманда не распознанна!");
            }
            sendMessage(chatId,
                    msg,
                    keyboardMarkup(
                            new String[]{"/Каналы", "/Предложения"}
                            //new String[]{ "/Настройки", "/Справка"}
                            //new String[]{"/Донат",}
                    )
            );
        }
        return null;
    }

}
