package com.bb8qq.tgbotproject.lib;

/**
 * Интерфейс куллбека задачи.
 */
public interface TaskTurnCallBack<T> {

    /**
     * Вызвать эту функцию.
     *
     * @param o
     */
    void call(T o);

}
