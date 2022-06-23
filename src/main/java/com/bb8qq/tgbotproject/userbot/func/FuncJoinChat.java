package com.bb8qq.tgbotproject.userbot.func;

import com.bb8qq.tgbotproject.lib.LCall;
import com.bb8qq.tgbotproject.lib.task.JoinChatRequest;
import com.bb8qq.tgbotproject.lib.task.JoinChatResponse;
import com.bb8qq.tgbotproject.userbot.UserBotFunc;
import it.tdlight.client.SimpleTelegramClient;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FuncJoinChat extends UserBotFunc {
    private LCall<JoinChatResponse> call;

    public FuncJoinChat(SimpleTelegramClient client, LCall call) {
        super(client);
        this.call = call;
    }

    /**
     * Добавиться в группу по ID ?
     *
     * @param
     */
    public void joinChat(JoinChatRequest joinChatRequest) {
        long chatId = joinChatRequest.getGroupId();
        client.send(new TdApi.JoinChat(chatId), result -> {
            if (!result.isError()) {
                call.call(new JoinChatResponse(joinChatRequest, true));
            } else {
                call.call(new JoinChatResponse(joinChatRequest, false));
            }
        });
    }

}
