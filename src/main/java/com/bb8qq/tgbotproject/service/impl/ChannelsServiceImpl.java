package com.bb8qq.tgbotproject.service.impl;

import com.bb8qq.tgbotproject.lib.dto.CannelWorldListDTO;
import com.bb8qq.tgbotproject.lib.dto.ChannelDTO;
import com.bb8qq.tgbotproject.lib.dto.WorldDTO;
import com.bb8qq.tgbotproject.model.TgFilterChat;
import com.bb8qq.tgbotproject.model.TgMyChannels;
import com.bb8qq.tgbotproject.model.TgUser;
import com.bb8qq.tgbotproject.reposetory.TgFilterChatRepo;
import com.bb8qq.tgbotproject.reposetory.TgMyChannelsRepo;
import com.bb8qq.tgbotproject.reposetory.TgUserRepo;
import com.bb8qq.tgbotproject.service.ChannelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChannelsServiceImpl implements ChannelsService {

    @Autowired
    private TgUserRepo userRepo;

    @Autowired
    private TgMyChannelsRepo myChannelsRepo;

    @Autowired
    private TgFilterChatRepo filterChatRepo;

    @Override
    public void removeChannel(Long channelId) {
        TgMyChannels tm = myChannelsRepo.getReferenceById(channelId);
        if (tm == null) {
            return;
        }
        List<TgFilterChat> filterChats = filterChatRepo.findAllByMyChannels(tm);
        for (TgFilterChat t : filterChats) {
            filterChatRepo.delete(t);
        }
        myChannelsRepo.delete(tm);
    }

    @Transactional
    public CannelWorldListDTO editChannel(Long chatId, Long channelId) {
        CannelWorldListDTO r = new CannelWorldListDTO();
        TgMyChannels c = myChannelsRepo.getReferenceById(channelId);
        if (c == null) {
            return null;
        }
        r.setChannelName(c.getChannelName());
        List<TgFilterChat> filterChats = filterChatRepo.findAllByMyChannels(c);
        for (TgFilterChat f : filterChats) {
            WorldDTO ww = new WorldDTO();
            ww.setId(f.getId());
            ww.setWord(f.getWord());
            r.getWorldDTOS().add(ww);
        }
        return r;
    }

    @Override
    public List<ChannelDTO> listChannel(Long chatId, Integer offset) {
        List<ChannelDTO> result = new ArrayList<>();
        TgUser u = userRepo.findTgUserByChatId(chatId);
        List<TgMyChannels> channels = myChannelsRepo.findByTgUserOrderByIdDesc(u, PageRequest.of(offset, 4));
        for (TgMyChannels cc : channels) {
            ChannelDTO c = new ChannelDTO();
            c.setId(cc.getId());
            c.setName(cc.getChannelName());
            result.add(c);
        }
        return result;
    }

    @Override
    public void removeWord(Long wordId) {
        TgFilterChat t = filterChatRepo.getReferenceById(wordId);
        if (t != null) {
            filterChatRepo.delete(t);
        }
    }
}
