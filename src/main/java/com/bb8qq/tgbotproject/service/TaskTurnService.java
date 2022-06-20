package com.bb8qq.tgbotproject.service;

import com.bb8qq.tgbotproject.lib.TaskTurnCallBack;

public interface TaskTurnService<T> {

    /**
     * Добавить обработчик вызовов
     *
     * @param key
     * @param callBack
     */
    void addCall(String key, TaskTurnCallBack callBack);

    /**
     * Удалить обработчик
     *
     * @param key
     */
    void delCall(String key);

    /**
     * Передать данные обработчику
     *
     * @param key
     * @param msg
     */
    void runCall(String key, T msg);

}
