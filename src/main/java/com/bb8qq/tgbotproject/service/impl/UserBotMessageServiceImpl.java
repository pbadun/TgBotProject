package com.bb8qq.tgbotproject.service.impl;

import com.bb8qq.tgbotproject.lib.TaskTurn;
import com.bb8qq.tgbotproject.lib.dto.ChatMessages;
import com.bb8qq.tgbotproject.lib.dto.ChatMessagesUser;
import com.bb8qq.tgbotproject.lib.dto.UBMessage;
import com.bb8qq.tgbotproject.model.TgFilterChat;
import com.bb8qq.tgbotproject.model.TgTrackingChannel;
import com.bb8qq.tgbotproject.reposetory.TgFilterChatRepo;
import com.bb8qq.tgbotproject.reposetory.TgTrackingChannelRepo;
import com.bb8qq.tgbotproject.reposetory.TgUserRepo;
import com.bb8qq.tgbotproject.service.TaskTurnService;
import com.bb8qq.tgbotproject.service.UserBotMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
public class UserBotMessageServiceImpl implements UserBotMessageService {

    @Autowired
    private TaskTurnService taskTurn;

    @Autowired
    private TgTrackingChannelRepo trackingChannelRepo;

    @Autowired
    private TgFilterChatRepo filterChatRepo;

    @Override
    @Async("taskExecutorB")
    @Transactional
    public void sendMessage(UBMessage message) {
        ChatMessages cm = new ChatMessages();
        TgTrackingChannel channel = trackingChannelRepo.findAllByChannelId(message.getChannelId());
        if (channel == null) {
            return;
        }
        cm.setChannelName(channel.getChannelName());
        cm.setMessage(message.getMsg());
        cm.setMsgType(message.getMsgType());

        List<TgFilterChat> filter = filterChatRepo.findAllByTrackingChannel(channel);
        if (filter.size() == 0) {
            return;
        }
        for (TgFilterChat f : filter) {
            if (!message.getMsg().toUpperCase().contains(f.getWord().toUpperCase()) || f.getWord().equals("*")) {
                continue;
            }
            Long chId = f.getMyChannels().getTgUser().getChatId();
            cm.getChatIds().add(new ChatMessagesUser(chId, f.getWord()));
        }
        if (cm.getChatIds().size() == 0) {
            return;
        }
        taskTurn.runCall(TaskTurn._TASK_USER_BOT, cm);
    }


}
