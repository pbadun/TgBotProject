package com.bb8qq.tgbotproject.lib.task;

import lombok.Data;
import lombok.ToString;

/**
 * Найденные каналы, или группы...
 * <p>
 * В ответе находится ответ!
 */
@Data
@ToString
public class SearchChatsResponse extends SearchChatsRequest {

    // Группа
    private Long groupId;

    // Заголовок группы
    private String title;

    public SearchChatsResponse(SearchChatsRequest searchChatsRequest) {
        super(searchChatsRequest);
    }

    public SearchChatsResponse(SearchChatsRequest searchChatsRequest, Long groupId, String title) {
        super(searchChatsRequest);
        this.groupId = groupId;
        this.title = title;
    }
}
