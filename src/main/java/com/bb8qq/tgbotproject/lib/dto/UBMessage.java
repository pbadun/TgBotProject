package com.bb8qq.tgbotproject.lib.dto;

import com.bb8qq.tgbotproject.lib.MsgType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UBMessage {

    private MsgType msgType;

    private Long channelId;

    private String msg;

}
