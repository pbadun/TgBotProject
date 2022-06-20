package com.bb8qq.tgbotproject.service.impl;

import com.bb8qq.tgbotproject.model.TgLogger;
import com.bb8qq.tgbotproject.reposetory.TgLoggerRepo;
import com.bb8qq.tgbotproject.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoggerServiceImpl implements LoggerService {

    @Autowired
    private TgLoggerRepo loggerRepo;

    @Override
    public void p(String msg) {
        p(msg, "");
    }

    @Override
    public void p(String msg, String tag) {
        TgLogger t = new TgLogger();
        t.setMsg(msg);
        t.setTag(tag);
        t.setTime(System.currentTimeMillis());
        loggerRepo.save(t);
    }
}
