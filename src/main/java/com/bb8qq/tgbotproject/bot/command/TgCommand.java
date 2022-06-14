package com.bb8qq.tgbotproject.bot.command;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

/**
 *
 */
public abstract class TgCommand {

    //------------------------------------------------------------------------------------------------------------------
    // Singleton
    public static HashMap<String, TgCommand> map = new HashMap<>();

    public TgCommand() {
        map.put(getClass().getName(), this);
    }

    public static TgCommand g(Class c) {
        return map.get(c.getName());
    }

    public static TgCommand g(String c) {
        return map.get(c);
    }

    //------------------------------------------------------------------------------------------------------------------
    public TelegramLongPollingBot tlp;

    /**
     * Список доступных команд конкретного модуля.
     *
     * @return
     */
    public abstract String commands();

    /**
     * Исполение команды
     *
     * @param update - TG Update
     * @param step   - шаг
     */
    public abstract Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException;

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Это команда?
     *
     * @param msg
     * @return
     */
    public boolean isCommand(String msg) {
        String[] s = commands().split(",");
        for (String ss : s) {
            if (ss.equals(msg)) {
                return true;
            }
        }
        return false;
    }

    public void setTlp(TelegramLongPollingBot tlp) {
        this.tlp = tlp;
    }
}
