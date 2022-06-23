package com.bb8qq.tgbotproject.lib.task;

import lombok.Data;

@Data
public class JoinChatResponse extends JoinChatRequest {

    private Boolean ok;

    public JoinChatResponse(JoinChatRequest chatRequest, Boolean ok) {
        super(chatRequest);
        this.ok = ok;
    }
}
