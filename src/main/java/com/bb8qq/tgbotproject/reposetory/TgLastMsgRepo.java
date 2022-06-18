package com.bb8qq.tgbotproject.reposetory;

import com.bb8qq.tgbotproject.model.TgLastMsg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TgLastMsgRepo extends JpaRepository<TgLastMsg, Long> {

    TgLastMsg getByChatId(Long chatId);

}
