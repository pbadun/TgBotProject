package com.bb8qq.tgbotproject.service;

public interface LastMsgService {

    /**
     * Сохранить последнее сообщение пользователя в чате.
     *
     * @param chatId
     * @param msg
     */
    void save(Long chatId, String msg);

    /**
     * Удалить сообщения из чата.
     *
     * @param chatId
     */
    void remove(Long chatId);

    /**
     * Получить последнее сообщение
     *
     * @param chatId
     * @return
     */
    String getLastMsg(Long chatId);

    /**
     * Получить сообщение и удалить из базы.
     *
     * @param chatId
     * @return
     */
    String cutLastMsg(Long chatId);

}
