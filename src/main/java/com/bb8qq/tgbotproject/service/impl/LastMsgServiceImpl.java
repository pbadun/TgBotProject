package com.bb8qq.tgbotproject.service.impl;

import com.bb8qq.tgbotproject.model.TgLastMsg;
import com.bb8qq.tgbotproject.reposetory.TgLastMsgRepo;
import com.bb8qq.tgbotproject.service.LastMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LastMsgServiceImpl implements LastMsgService {

    @Autowired
    private TgLastMsgRepo lastMsg;

    @Override
    public void save(Long chatId, String msg) {
        TgLastMsg m = lastMsg.getByChatId(chatId);
        if (m == null) {
            m = new TgLastMsg();
        }
        m.setChatId(chatId);
        m.setMsg(msg);
        lastMsg.save(m);
    }

    @Override
    public void remove(Long chatId) {
        TgLastMsg m = lastMsg.getByChatId(chatId);
        lastMsg.delete(m);
    }

    @Override
    public String getLastMsg(Long chatId) {
        TgLastMsg m = lastMsg.getByChatId(chatId);
        if (m != null) {
            return m.getMsg();
        }
        return null;
    }

    @Override
    public String cutLastMsg(Long chatId) {
        String m = getLastMsg(chatId);
        remove(chatId);
        return m;
    }
}
