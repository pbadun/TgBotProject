package com.bb8qq.tgbotproject.bot;

import com.bb8qq.tgbotproject.bot.command.TgCommand;
import com.bb8qq.tgbotproject.bot.command.TgCommandEnd;
import com.bb8qq.tgbotproject.bot.command.TgCommandSession;
import com.bb8qq.tgbotproject.bot.command.TgCommandStart;
import com.bb8qq.tgbotproject.model.TgSession;
import com.bb8qq.tgbotproject.reposetory.TgSessionRepo;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TgCommandRepo {

    @Autowired
    private TgSessionRepo sessionRepo;

    private List<TgCommand> tgCommands;

    TelegramLongPollingBot tlp;

    public void onRegistry(TelegramLongPollingBot tlp) {
        this.tlp = tlp;
        this.tgCommands = new ArrayList<>();
        //------------------------------------------------------
        // Все команды бота
        tgCommands.add(TgCommand.g(TgCommandStart.class));
        tgCommands.add(TgCommand.g(TgCommandSession.class));

        //------------------------------------------------------
        // Команда выполняется всегля если до нее дойдет оченедь
        tgCommands.add(TgCommandEnd.g(TgCommandEnd.class));
        //------------------------------------------------------
        for (TgCommand t : tgCommands) {
            t.setTlp(tlp);
        }
    }

    @Async("taskExecutor")
    public void run(Update update) {
        Long chatId;
        String text;
        TgCommand tgCommand = null;
        //-------------------------------
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            text = update.getMessage().getText();
        } else {
            log(update);
            return;
        }
        //-------------------------------
        TgSession session = sessionRepo.getFromChatId(chatId);
        if (session == null) {
            session = new TgSession();
            session.setStep(0);
            session.setChatId(chatId);
        }
        //-------------------------------
        // 1. Ищем среди комманд
        for (TgCommand tc : tgCommands) {
            if (tc.isCommand(text)) {
                tgCommand = tc;
                break;
            }
        }
        //-------------------------------
        // 2. Еслинет: Ищем последнюю сессию.
        if (tgCommand != null) {
            //log.info("Сессия прерванна. Исполнить команду. " + tgCommand.getClass().getSimpleName());
            session.setStep(0);

        } else if (session.getCommand() != null) {
            tgCommand = TgCommand.g(session.getCommand());
        } else if (tgCommand == null) {
            tgCommand = TgCommandEnd.g(TgCommandEnd.class);
        }
        //-------------------------------
        // 3. Выполняем Команду
        Integer step = null;
        try {
            step = tgCommand.runCommand(update, chatId, session.getStep());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        if (step != null) {
            session.setStep(step);
            session.setCommand(tgCommand.getClass().getName());
            sessionRepo.save(session);
            //log("Сессия обнавлена.");
            //log(session);
        } else if (session.getId() != null) {
            sessionRepo.delete(session);
            //log("Сессия удалена.");
        }
    }

    private void log(Object o) {
        log.info(o.toString());
    }

}
