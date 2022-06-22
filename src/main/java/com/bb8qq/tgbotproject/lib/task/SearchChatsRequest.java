package com.bb8qq.tgbotproject.lib.task;

import lombok.Data;

/**
 * Запрос на посиск групп, каналов,...
 */
@Data
public class SearchChatsRequest {

    // Че ищем
    private String query;

    // Кому ответить
    private Long chatId;

    public SearchChatsRequest(String query, Long chatId) {
        this.query = query;
        this.chatId = chatId;
    }

    public SearchChatsRequest(SearchChatsRequest searchChatsRequest) {
        this.chatId = searchChatsRequest.getChatId();
        this.query = searchChatsRequest.getQuery();
    }
}
