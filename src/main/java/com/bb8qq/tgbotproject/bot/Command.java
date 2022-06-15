package com.bb8qq.tgbotproject.bot;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Аннотация обработчика команд пользователя от ТГ бота.
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Command {

    /**
     * Команды которые обрабатывает данный класс, через запяную.
     *
     * @return
     */
    String commands();

    /**
     * Это финальный класс обрабатывающий все входящие запросы?
     *
     * @return
     */
    boolean isEnd() default false;

}
