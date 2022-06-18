package com.bb8qq.tgbotproject.reposetory;

import com.bb8qq.tgbotproject.model.TgSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TgSessionRepo extends JpaRepository<TgSession, Long> {

    @Query(value = "select * from tg_session where chat_id = ?1 limit 1", nativeQuery = true)
    TgSession getFromChatId(Long chatId);

}
