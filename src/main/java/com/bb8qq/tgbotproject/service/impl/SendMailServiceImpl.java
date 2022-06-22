package com.bb8qq.tgbotproject.service.impl;

import com.bb8qq.tgbotproject.service.SendMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SendMailServiceImpl implements SendMailService {




    @Override
    public void sendMail(String msg) {
        log.info(msg);
    }

}
