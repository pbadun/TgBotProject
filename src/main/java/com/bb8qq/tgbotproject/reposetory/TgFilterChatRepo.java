package com.bb8qq.tgbotproject.reposetory;

import com.bb8qq.tgbotproject.model.TgFilterChat;
import com.bb8qq.tgbotproject.model.TgMyChannels;
import com.bb8qq.tgbotproject.model.TgTrackingChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TgFilterChatRepo extends JpaRepository<TgFilterChat, Long> {

    @Query(value = "SELECT t FROM TgFilterChat t WHERE t.myChannels=?1 AND t.word=?2")
    TgFilterChat findByAll(TgMyChannels myChannels, String world);

    List<TgFilterChat> findAllByMyChannels(TgMyChannels myChannels);

    List<TgFilterChat> findAllByTrackingChannel(TgTrackingChannel channel);

}
