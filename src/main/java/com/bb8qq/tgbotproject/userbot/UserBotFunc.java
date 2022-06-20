package com.bb8qq.tgbotproject.userbot;

import it.tdlight.client.SimpleTelegramClient;

public abstract class UserBotFunc {

    public SimpleTelegramClient client;

    public UserBotFunc(SimpleTelegramClient client) {
        this.client = client;
    }

}
