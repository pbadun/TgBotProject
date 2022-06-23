package com.bb8qq.tgbotproject.reposetory;

import com.bb8qq.tgbotproject.model.TgTrackingChannel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Отслеживаемые каналы.
 */
public interface TgTrackingChannelRepo extends JpaRepository<TgTrackingChannel, Long> {

    TgTrackingChannel findAllByChannelId(Long channelId);

}
