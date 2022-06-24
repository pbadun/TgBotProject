package com.bb8qq.tgbotproject.bot;

import com.bb8qq.tgbotproject.model.TgSession;
import com.bb8qq.tgbotproject.reposetory.TgSessionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

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
        try {
            findAllClass();
        } catch (Exception e) {
            // Пропустить классы с ошибками - они не важны.
            e.printStackTrace();
        }
        //------------------------------------------------------
        for (TgCommand t : tgCommands) {
            t.setTlp(tlp);
        }
    }

    /**
     * Загрузка объектов обработчиков по аннотациям и их параметрам.
     *
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private void findAllClass() throws ClassNotFoundException {
        String lastTgCommand = null;
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));
        final Set<BeanDefinition> classes = provider.findCandidateComponents(TgCommandRepo.class.getPackage().getName());
        for (BeanDefinition bean : classes) {
            Class<?> clazz = Class.forName(bean.getBeanClassName());
            Class cl = Class.forName(clazz.getName());
            if (!cl.isAnnotationPresent(Command.class)) {
                continue;
            }
            Command c = (Command) cl.getAnnotation(Command.class);
            log.warn("Обработчик: " + cl.getSimpleName() + ", комманды: " + c.commands());
            if (!c.isEnd()) {
                tgCommands.add(TgCommand.g(cl.getName()));
            } else {
                lastTgCommand = cl.getName();
            }
        }
        tgCommands.add(TgCommand.g(lastTgCommand));
    }

    @Async("taskExecutorA")
    public void run(Update update) {
        Long chatId;
        String text;
        TgCommand tgCommand = null;
        //-------------------------------
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            text = update.getMessage().getText();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            text = update.getCallbackQuery().getData();
        } else {
            log.error(update.toString());
            return;
        }
        //-------------------------------
        TgSession session = sessionRepo.getFromChatId(chatId);
        if (session == null) {
            session = new TgSession();
            session.setStep(0);
            session.setChatId(chatId);
        }
        Integer step = session.getStep();
        //-------------------------------
        // 1. Ищем среди комманд
        for (TgCommand tc : tgCommands) {
            if (tc.isCommand(text)) {
                tgCommand = tc;
                step = 0;
                break;
            }
        }
        //-------------------------------
        // 2. Еслинет: Ищем последнюю сессию.
        if (tgCommand != null) {
            //log.info("Сессия прерванна. Исполнить команду. " + tgCommand.getClass().getSimpleName());
        } else if (session.getCommand() != null) {
            tgCommand = TgCommand.g(session.getCommand());
        } else if (tgCommand == null) {
            tgCommand = tgCommands.get(tgCommands.size() - 1);
        }
        //-------------------------------
        // 3. Выполняем Команду

        // комманда изменяющая шаг.
        if (TgBaseKey._NEXT.equals(text)) {
            step++;
        }
        try {
            step = tgCommand.runCommand(update, chatId, step);
            if (step != null && step == -1) {
                tgCommand = tgCommands.get(tgCommands.size() - 1);
                step = tgCommand.runCommand(update, chatId, step);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        if (step != null) {
            session.setStep(step);
            session.setCommand(tgCommand.getClass().getName());
            sessionRepo.save(session);
        } else if (session.getId() != null) {
            sessionRepo.delete(session);
        }
    }

}
