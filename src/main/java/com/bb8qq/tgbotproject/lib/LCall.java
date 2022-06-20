package com.bb8qq.tgbotproject.lib;

/**
 * Лямда обратного вызова. Универсальная.
 *
 * @param <T>
 */
public interface LCall<T> {
    void call(T o);
}
