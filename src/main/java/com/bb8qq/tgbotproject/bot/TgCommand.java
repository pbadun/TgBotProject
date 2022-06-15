package com.bb8qq.tgbotproject.bot;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

/**
 *
 */
@Slf4j
public abstract class TgCommand {

    //------------------------------------------------------------------------------------------------------------------
    // Singleton
    public static HashMap<String, TgCommand> map = new HashMap<>();

    public TgCommand() {
        map.put(getClass().getName(), this);
        // Читаем параметры из аннотаций.
        try {
            Class cl = Class.forName(this.getClass().getName());
            if (!cl.isAnnotationPresent(Command.class)) {
                throw new Exception("");
            }
            Command c = (Command) cl.getAnnotation(Command.class);
            commands = c.commands();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("");
        }
    }

    public static TgCommand g(Class c) {
        return map.get(c.getName());
    }

    public static TgCommand g(String c) {
        return map.get(c);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Для отправки сообщений.
    public TelegramLongPollingBot tlp;
    // Исполняемы комманды
    private final String commands;

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
        String[] s = commands.split(",");
        for (String ss : s) {
            if (ss.trim().equals(msg)) {
                return true;
            }
        }
        return false;
    }

    public void setTlp(TelegramLongPollingBot tlp) {
        this.tlp = tlp;
    }
}
