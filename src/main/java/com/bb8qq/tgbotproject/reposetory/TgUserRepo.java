package com.bb8qq.tgbotproject.reposetory;

import com.bb8qq.tgbotproject.model.TgUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TgUserRepo extends JpaRepository<TgUser, Long> {

    TgUser findTgUserByChatId(Long chatId);

}
