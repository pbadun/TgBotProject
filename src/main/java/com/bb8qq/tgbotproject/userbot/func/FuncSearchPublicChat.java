package com.bb8qq.tgbotproject.userbot.func;

import com.bb8qq.tgbotproject.lib.LCall;
import com.bb8qq.tgbotproject.lib.task.SearchChatsRequest;
import com.bb8qq.tgbotproject.lib.task.SearchChatsResponse;
import com.bb8qq.tgbotproject.userbot.UserBotFunc;
import it.tdlight.client.SimpleTelegramClient;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FuncSearchPublicChat extends UserBotFunc {

    private LCall<SearchChatsResponse> call;

    public FuncSearchPublicChat(SimpleTelegramClient client, LCall call) {
        super(client);
        this.call = call;
    }

    public void SearchPublicChat(SearchChatsRequest q) {
        String query = q.getQuery();
        String[] u = query.split("/");
        if (u.length != 1) {
            query = u[u.length - 1];
        }
        if (query.indexOf("@") == -1) {
            query = "@" + query;
        }
        q.setQuery(query);
        client.send(new TdApi.SearchPublicChat(query), o -> {
            if (o.isError()) {
                call.call(new SearchChatsResponse(q));
            } else {
                call.call(new SearchChatsResponse(q, o.get().id, o.get().title));
            }
        });
    }

}
