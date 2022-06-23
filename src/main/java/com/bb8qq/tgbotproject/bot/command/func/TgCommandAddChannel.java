package com.bb8qq.tgbotproject.bot.command.func;

import com.bb8qq.tgbotproject.bot.Command;
import com.bb8qq.tgbotproject.bot.TgCommand;
import com.bb8qq.tgbotproject.lib.TaskTurn;
import com.bb8qq.tgbotproject.lib.task.JoinChatRequest;
import com.bb8qq.tgbotproject.lib.task.JoinChatResponse;
import com.bb8qq.tgbotproject.lib.task.SearchChatsRequest;
import com.bb8qq.tgbotproject.lib.task.SearchChatsResponse;
import com.bb8qq.tgbotproject.model.TgFilterChat;
import com.bb8qq.tgbotproject.model.TgMyChannels;
import com.bb8qq.tgbotproject.model.TgTrackingChannel;
import com.bb8qq.tgbotproject.model.TgUser;
import com.bb8qq.tgbotproject.reposetory.TgFilterChatRepo;
import com.bb8qq.tgbotproject.reposetory.TgMyChannelsRepo;
import com.bb8qq.tgbotproject.reposetory.TgTrackingChannelRepo;
import com.bb8qq.tgbotproject.reposetory.TgUserRepo;
import com.bb8qq.tgbotproject.service.LastMsgService;
import com.bb8qq.tgbotproject.service.TaskTurnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Command(commands = "/Новый")
@Slf4j
public class TgCommandAddChannel extends TgCommand {

    @Autowired
    private TgTrackingChannelRepo channelRepo;

    @Autowired
    private TgFilterChatRepo filterChatRepo;

    @Autowired
    private TgUserRepo tgUserRepo;

    @Autowired
    private TgMyChannelsRepo myChannelsRepo;

    @Autowired
    private LastMsgService lastMsgService;

    @Autowired
    private TaskTurnService taskTurnService;

    //------------------------------------------------------------------------------------------------------------------
    // Бэк для получения данных из UserBot
    @PostConstruct
    public void initCallBack() {
        taskTurnService.addCall(TaskTurn._TASK_RESULT_SEARCH_CHATS, o -> {
            try {
                callSearch((SearchChatsResponse) o);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });
        taskTurnService.addCall(TaskTurn._TASK_RESULT_JOIN_CHAT, o -> {
            try {
                callJoin(((JoinChatResponse) o));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * подключились к публичному каналу
     *
     * @param j
     * @throws TelegramApiException
     */
    private void callJoin(JoinChatResponse j) throws TelegramApiException {
        sendMessage(j.getChatId(), "Канал добавлен.\nВведите слова-фильтры, через запятую. Или * - для всех сообщений.");
    }

    /**
     * Публичный канал найден.
     *
     * @param r
     * @throws TelegramApiException
     */
    private void callSearch(SearchChatsResponse r) throws TelegramApiException {
        if (r.getGroupId() == null) {
            sendMessage(r.getChatId(), "Ничего не найденно!");
        } else {
            StringBuffer sb = new StringBuffer("Канал [ID: ");
            sb.append(r.getGroupId());
            sb.append("/");
            sb.append(r.getQuery());
            sb.append("] '");
            sb.append(r.getTitle());
            sb.append("'");
            addGroupMessage(r.getGroupId(), sb.toString(), r);
        }
    }

    /**
     * Сообщение с кнопкой.
     *
     * @param groupId
     * @param title
     * @param r
     */
    private void addGroupMessage(Long groupId, String title, SearchChatsResponse r) {
        InlineKeyboardButton k1 = new InlineKeyboardButton();
        k1.setText("Выбрать");
        k1.setCallbackData("add:" + groupId + ":" + r.getQuery());

        InlineKeyboardButton k2 = new InlineKeyboardButton();
        k2.setText("Перейти в канал");
        k2.setUrl("https://t.me/" + r.getQuery());

        InlineKeyboardMarkup im = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> kl = new ArrayList<>();
        kl.add(k1);
        kl.add(k2);
        im.setKeyboard(Collections.singletonList(kl));

        try {
            SendMessage m = new SendMessage();
            m.setChatId(r.getChatId().toString());
            m.setText(title.replace("/@", "/"));
            m.setReplyMarkup(im);
            tlp.execute(m);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        if (update.hasMessage()) {
            switch (step) {
                case 0:
                    sendMessage(chatId, "Для добавление нового канала отправьте ссылку или имя канала");
                    return 1;
                case 1:
                    sendMessage(chatId, "Поиск канала...");
                    taskTurnService.runCall(TaskTurn._TASK_SEARCH_CHATS, new SearchChatsRequest(update.getMessage().getText(), chatId));
                    return 1;
                case 2:
                    return addWord(chatId, update.getMessage().getText());
            }
        } else if (update.hasCallbackQuery()) {
            return addBtn(update, chatId);
        }
        return null;
    }

    /**
     * Обработка кнопки "Выбрать" канал
     *
     * @param update
     * @param chatId
     * @return
     * @throws TelegramApiException
     */
    private Integer addBtn(Update update, Long chatId) throws TelegramApiException {
        String data = update.getCallbackQuery().getData();
        String[] d = data.split(":");
        if (d.length != 3 || !d[0].equals("add")) {
            sendMessage(chatId, "Ошибка имени канала...\nПопробуйде еще раз.");
            return 1;
        }
        TgTrackingChannel ttc = channelRepo.findAllByChannelId(Long.parseLong(d[1]));
        if (ttc == null) {
            ttc = new TgTrackingChannel();
            ttc.setChannelName(d[2]);
            ttc.setChannelId(Long.parseLong(d[1]));
            channelRepo.save(ttc);
            taskTurnService.runCall(TaskTurn._TASK_JOIN_CHAT, new JoinChatRequest(chatId, Long.parseLong(d[1])));
        } else {
            sendMessage(chatId, "Уже в базе.\nВведите слова-фильтры, через запятую. Или * - для всех сообщений.");
        }
        TgUser u = tgUserRepo.findTgUserByChatId(chatId);
        TgMyChannels tm = myChannelsRepo.getByTgUserAndChannel(u, ttc);
        if (tm == null) {
            tm = new TgMyChannels();
            tm.setTgUser(u);
            tm.setChannel(ttc);
            tm.setChannelName(ttc.getChannelName());
            myChannelsRepo.save(tm);
        }
        lastMsgService.save(chatId, data);
        return 2;
    }

    /**
     * Добовление фильтра к выбраннаму каналу.
     *
     * @param chatId
     * @param word
     * @return
     * @throws TelegramApiException
     */
    private Integer addWord(Long chatId, String word) throws TelegramApiException {
        String[] lm = lastMsgService.getLastMsg(chatId).split(":");
        String[] w = word.split(",");

        TgUser u = tgUserRepo.findTgUserByChatId(chatId);
        TgTrackingChannel ttc = channelRepo.findAllByChannelId(Long.parseLong(lm[1]));
        TgMyChannels tmc = myChannelsRepo.getByTgUserAndChannel(u, ttc);

        StringBuffer sb = new StringBuffer();
        for (String s : w) {
            s = s.trim();
            TgFilterChat t = filterChatRepo.findByAll(tmc, s);
            if (s.equals("") || t != null) {
                continue;
            }
            t = new TgFilterChat();
            t.setWord(s);
            t.setMyChannels(tmc);
            t.setTrackingChannel(ttc);
            filterChatRepo.save(t);
            sb.append(s);
            sb.append(", ");
        }
        sendMessage(chatId, String.format("Канал %s с фильтрами '%s' добавлен.", ttc.getChannelName(), sb.toString()));
        return -1;
    }
}
