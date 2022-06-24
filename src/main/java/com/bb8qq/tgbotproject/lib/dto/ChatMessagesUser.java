package com.bb8qq.tgbotproject.lib.dto;

import lombok.Data;

@Data
public class ChatMessagesUser {
    private Long chatId;
    private String word;

    public ChatMessagesUser(Long chatId, String word) {
        this.chatId = chatId;
        this.word = word;
    }
}
