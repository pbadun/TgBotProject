package com.bb8qq.tgbotproject.service;

import com.bb8qq.tgbotproject.lib.dto.CannelWorldListDTO;
import com.bb8qq.tgbotproject.lib.dto.ChannelDTO;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface ChannelsService {

    /**
     * Удалить отслеживаемый канал.
     *
     * @param channelId
     */
    void removeChannel(Long channelId);


    /**
     * Донные для редактирования.
     *
     * @param chatId
     * @param channelId
     */
    CannelWorldListDTO editChannel(Long chatId, Long channelId);

    /**
     * Список каналов пользователя.
     *
     * @param chatId
     * @param offset
     * @return
     */
    List<ChannelDTO> listChannel(Long chatId, Integer offset);


    /**
     * Удалить слово из фильтра
     *
     * @param wordId
     */
    void removeWord(Long wordId);

}
