package com.bb8qq.tgbotproject.service;

public interface LoggerService {

    /**
     * Ананимная запись в журнале
     *
     * @param msg
     */
    void p(String msg);

    /**
     * Запись с тегом.
     *
     * @param msg
     * @param tag
     */
    void p(String msg, String tag);

}
