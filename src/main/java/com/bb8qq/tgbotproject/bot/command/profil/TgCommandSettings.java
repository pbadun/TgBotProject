package com.bb8qq.tgbotproject.bot.command.profil;

import com.bb8qq.tgbotproject.bot.Command;
import com.bb8qq.tgbotproject.bot.TgCommand;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

//TODO Функционал на будущее !
@Command(commands = "/Настройки")
@Slf4j
public class TgCommandSettings extends TgCommand {
    private String[] messages = {
            "Не верная команда.",
            "Настройка оповещение о новых сообщениях",
            "Преостановить прослушивание ",
            "Удалить профиль",
            ""
    };

    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        if (update.hasMessage()) {
            switch (step) {
                case 0:
                    sendMessage(chatId, "Настройки бота",
                            keyboardMarkup(
                                    new String[]{"Оповещение", "Преостановить"}, // "Возобновить"
                                    new String[]{"Удалить профиль", "На главную"}
                            )
                    );
                    return 1;
                case 1:
                    return commands(chatId, update.getMessage().getText());
                case 2: //Оповещение
                case 3: //Преостановить / Возобновить
                case 4: //Удалить профиль
                    sendMessage(chatId, "Ok");
                    return 0;
                default:
                    return 0;
            }
        } else if (update.hasCallbackQuery()) {
            String dat = update.getCallbackQuery().getData();
            log.warn(dat);
            sendMessage(chatId, "Комманда:" + dat);
            return 0;
        }
        return null;
    }

    /**
     * Внутренние команды модуля.
     *
     * @param chatId
     * @param msg
     * @return
     * @throws TelegramApiException
     */
    private Integer commands(Long chatId, String msg) throws TelegramApiException {
        if (msg.equals("Оповещение")) {
            sendMessage(chatId, messages[1]);
            return 2;
        } else if (msg.equals("Преостановить") || msg.equals("Возобновить")) {
            sendMessage(chatId, messages[2]);
            return 3;
        } else if (msg.equals("Удалить профиль")) {
            sendMessage(chatId, messages[3]);
            return 4;
        } else if (msg.equals("На главную")) {
            return -1;
        }
        sendMessage(chatId, messages[0]);
        return 0;
    }
}
