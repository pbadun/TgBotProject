package com.bb8qq.tgbotproject.lib.dto;

import com.bb8qq.tgbotproject.lib.MsgType;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class ChatMessages {

    private List<ChatMessagesUser> chatIds;
    private String channelName;
    private String message;
    private MsgType msgType;

    public ChatMessages() {
        this.chatIds = new ArrayList<>();
    }

}
