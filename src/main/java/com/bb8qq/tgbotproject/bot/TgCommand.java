package com.bb8qq.tgbotproject.bot;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
@Slf4j
public abstract class TgCommand {

    //------------------------------------------------------------------------------------------------------------------
    // Для отправки сообщений.
    public TelegramLongPollingBot tlp;
    // Исполняемы комманды
    private final String commands;

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
    // Abstract functions TGBot

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

    //------------------------------------------------------------------------------------------------------------------
    // Частоиспользуемые функции.

    /**
     * Клавиатурка из массива.
     *
     * @param keyRow
     * @return
     */
    public ReplyKeyboardMarkup keyboardMarkup(String[]... keyRow) {
        ReplyKeyboardMarkup rk = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (int i = 0; i < keyRow.length; i++) {
            KeyboardRow key = new KeyboardRow();
            for (String k : keyRow[i]) {
                key.add(new KeyboardButton(k));
            }
            keyboardRows.add(key);
        }
        rk.setKeyboard(keyboardRows);
        rk.setSelective(true);
        rk.setResizeKeyboard(true);
        rk.setOneTimeKeyboard(false);
        return rk;
    }

    /**
     * Кнопка Продолжить. Меняет шаг (step++).
     *
     * @return
     */
    public void further(Long chatId, String msg) throws TelegramApiException {
        sendMessage(chatId, msg, keyboardMarkup(new String[]{TgBaseKey._NEXT}));
    }

    /**
     * Отправка сообщения в чат
     *
     * @param chatId
     * @param msg
     * @throws TelegramApiException
     */
    public void sendMessage(Long chatId, String msg) throws TelegramApiException {
        SendMessage m = new SendMessage();
        m.setChatId(chatId.toString());
        m.setText(msg);
        tlp.execute(m);
    }

    /**
     * Отправка сообщения с клавиатурай в чат.
     *
     * @param chatId
     * @param msg
     * @param replyKeyboard
     * @throws TelegramApiException
     */
    public void sendMessage(Long chatId, String msg, ReplyKeyboard replyKeyboard) throws TelegramApiException {
        SendMessage m = new SendMessage();
        m.setChatId(chatId.toString());
        m.setText(msg);
        m.setReplyMarkup(replyKeyboard);
        tlp.execute(m);
    }


}
