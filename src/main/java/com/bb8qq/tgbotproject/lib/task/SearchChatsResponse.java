package com.bb8qq.tgbotproject.lib.task;

import lombok.Data;
import lombok.ToString;

/**
 * Найденные каналы, или группы...
 */
@Data
@ToString
public class SearchChatsResponse {

    public enum TypeResult {
        NO_RESULT, // Ничго не найдено
        ERROR_QUERY, // Ошибка запроса
        TOTAL_RESULT, // Всего найденно
        GROUP_ID, // Гпруппа
        CHANNELS_ID, // Канал
        NO_GROUP, // Приватный чат - пользователь, бот
    }

    private TypeResult typeResult;

    private Long chatId;

    private Long groupId;

    private String title;

    private Integer total;

    /**
     * Ответы с ошибками
     *
     * @param chatId
     * @param typeResult
     */
    public SearchChatsResponse(Long chatId, TypeResult typeResult) {
        this.chatId = chatId;
        this.typeResult = typeResult;
    }

    /**
     * Всего результата
     *
     * @param chatId
     * @param total
     */
    public SearchChatsResponse(Long chatId, Integer total) {
        this.chatId = chatId;
        this.total = total;
        this.typeResult = TypeResult.TOTAL_RESULT;
    }

    /**
     * Найденный канал, группа, чат...
     *
     * @param chatId
     * @param groupId
     * @param title
     * @param typeResult
     */
    public SearchChatsResponse(Long chatId, Long groupId, String title, TypeResult typeResult) {
        this.typeResult = typeResult;
        this.chatId = chatId;
        this.groupId = groupId;
        this.title = title;
    }
}
