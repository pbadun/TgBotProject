package com.bb8qq.tgbotproject.reposetory;

import com.bb8qq.tgbotproject.model.TgFilterChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TgFilterChatRepo extends JpaRepository<TgFilterChat, Long> {
}
