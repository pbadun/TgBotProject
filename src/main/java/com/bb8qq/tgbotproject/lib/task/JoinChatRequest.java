package com.bb8qq.tgbotproject.lib.task;

import lombok.Data;

/**
 * Добавиться в выбранную группу.
 */
@Data
public class JoinChatRequest {

    private Long chatId;

    private Long groupId;

    public JoinChatRequest(Long chatId, Long groupId) {
        this.chatId = chatId;
        this.groupId = groupId;
    }
}
