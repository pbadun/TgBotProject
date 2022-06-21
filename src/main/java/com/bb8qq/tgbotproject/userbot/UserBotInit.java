package com.bb8qq.tgbotproject.userbot;

import it.tdlight.common.utils.CantLoadLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserBotInit {

    @Autowired
    private UserBotTgComponent tgComponent;

    //после того, как приложение полностью запущено
    @EventListener({ApplicationReadyEvent.class})
    public void init() {
        try {
            tgComponent.init();
        } catch (CantLoadLibrary e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
