package com.bb8qq.tgbotproject.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Точка "входа" для бота.
 */
@Component
public class TgBot extends TelegramLongPollingBot {

    @Value("${tg-bot.name}")
    private String tgBotName;
    @Value("${tg-bot.key}")
    private String tgBotKey;

    @Override
    public String getBotToken() {
        return tgBotKey;
    }

    @Override
    public String getBotUsername() {
        return tgBotName;
    }

    //------------------------------------------------------------------------------------------------------------------
    @Autowired
    private TgCommandRepo tgCommandRepo;
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void onRegister() {
        super.onRegister();
        tgCommandRepo.onRegistry(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        tgCommandRepo.run(update);
    }

}
