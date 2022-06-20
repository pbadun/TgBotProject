package com.bb8qq.tgbotproject.reposetory;

import com.bb8qq.tgbotproject.model.TgTrackingChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TgTrackingChatRepo extends JpaRepository<TgTrackingChat, Long> {

}
