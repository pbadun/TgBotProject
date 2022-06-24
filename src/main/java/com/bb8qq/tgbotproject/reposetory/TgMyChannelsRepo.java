package com.bb8qq.tgbotproject.reposetory;

import com.bb8qq.tgbotproject.model.TgMyChannels;
import com.bb8qq.tgbotproject.model.TgTrackingChannel;
import com.bb8qq.tgbotproject.model.TgUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TgMyChannelsRepo extends JpaRepository<TgMyChannels, Long> {

    TgMyChannels getByTgUserAndChannel(TgUser user, TgTrackingChannel channel);

    List<TgMyChannels> findByTgUser(TgUser tgUser, Pageable pageable);

    List<TgMyChannels> findByTgUserOrderByIdDesc(TgUser tgUser, Pageable pageable);

}
