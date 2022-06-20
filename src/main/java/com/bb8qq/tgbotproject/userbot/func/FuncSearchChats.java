package com.bb8qq.tgbotproject.userbot.func;

import com.bb8qq.tgbotproject.lib.LCall;
import com.bb8qq.tgbotproject.lib.task.SearchChatsRequest;
import com.bb8qq.tgbotproject.lib.task.SearchChatsResponse;
import com.bb8qq.tgbotproject.userbot.UserBotFunc;
import it.tdlight.client.Result;
import it.tdlight.client.SimpleTelegramClient;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;

/**
 * Функция ЮзерБота
 * Поиск канало, групп и т.п.
 */
@Slf4j
public class FuncSearchChats extends UserBotFunc {

    private LCall<SearchChatsResponse> call;
    private Long chatId;

    public FuncSearchChats(SimpleTelegramClient client, LCall call) {
        super(client);
        this.call = call;
    }

    /**
     * поиск публичных чатов.
     *
     * @param query
     */
    public void searchPublicChats(SearchChatsRequest query) {
        this.chatId = query.getChatId();
        client.send(new TdApi.SearchPublicChats(query.getQuery()), result -> {
            try {
                chatInfo(result.get().chatIds);
            } catch (Exception e) {
                call.call(new SearchChatsResponse(chatId, SearchChatsResponse.TypeResult.ERROR_QUERY));
                log.error(result.toString());
            }
        });
    }

    /**
     * Обработка списка
     *
     * @param chatIds
     */
    private void chatInfo(long[] chatIds) {
        if (chatIds.length == 0) {
            // Ничего нет
            call.call(new SearchChatsResponse(chatId, SearchChatsResponse.TypeResult.NO_RESULT));
        } else {
            // Всего найденно
            call.call(new SearchChatsResponse(chatId, chatIds.length));
        }
        // Перебираем все чаты, получая инфу
        for (long l : chatIds) {
            client.send(new TdApi.GetChat(l), result -> chatInfoResult(result));
        }
    }

    /**
     * Обработка результата поиска детальной информации по ID
     *
     * @param result
     */
    private void chatInfoResult(Result<TdApi.Chat> result) {
        try {
            TdApi.Chat c = result.get();
            if (c.type instanceof TdApi.ChatTypeSupergroup) {
                // Группы, чаты
                if (((TdApi.ChatTypeSupergroup) c.type).isChannel) {
                    call.call(new SearchChatsResponse(chatId, c.id, c.title, SearchChatsResponse.TypeResult.GROUP_ID));
                } else {
                    call.call(new SearchChatsResponse(chatId, c.id, c.title, SearchChatsResponse.TypeResult.CHANNELS_ID));
                }
            } else if (c.type instanceof TdApi.ChatTypeBasicGroup) {
                log.warn(result.toString());
            } else if (c.type instanceof TdApi.ChatTypePrivate) {
                // Приватный чат с пользователем.
                call.call(new SearchChatsResponse(chatId, c.id, c.title, SearchChatsResponse.TypeResult.NO_GROUP));
            } else if (c.type instanceof TdApi.ChatTypeSecret) {
                log.warn(result.toString());
            }
        } catch (Exception e) {
            log.error(result.toString());
        }
    }

}
