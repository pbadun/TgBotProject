package com.bb8qq.tgbotproject.bot.command.func;

import com.bb8qq.tgbotproject.bot.Command;
import com.bb8qq.tgbotproject.bot.TgCommand;
import com.bb8qq.tgbotproject.lib.dto.CannelWorldListDTO;
import com.bb8qq.tgbotproject.lib.dto.ChannelDTO;
import com.bb8qq.tgbotproject.lib.dto.WorldDTO;
import com.bb8qq.tgbotproject.model.TgFilterChat;
import com.bb8qq.tgbotproject.model.TgMyChannels;
import com.bb8qq.tgbotproject.model.TgUser;
import com.bb8qq.tgbotproject.reposetory.TgFilterChatRepo;
import com.bb8qq.tgbotproject.reposetory.TgMyChannelsRepo;
import com.bb8qq.tgbotproject.reposetory.TgUserRepo;
import com.bb8qq.tgbotproject.service.ChannelsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Command(commands = "/Каналы")
public class TgCommandChannel extends TgCommand {

    @Autowired
    private ChannelsService channelsService;

    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        if (update.hasMessage()) {
            switch (step) {
                case 0:
                    sendMessage(chatId, "Каналы.",
                            keyboardMarkup(
                                    new String[]{"/Новый", "Отмена"}
                            ));
                    listMyChannels(update, chatId, 0);
                    return 1;
                case 1:
                    if (update.getMessage().getText().equals("Отмена")) {
                        return -1;
                    }
                    return 1;
            }
        } else if (update.hasCallbackQuery()) {
            log.warn(update.getCallbackQuery().getData());
            updateList(update, chatId);
            return 1;
        }
        return null;
    }

    private void updateList(Update update, Long chatId) throws TelegramApiException {
        String[] dat = update.getCallbackQuery().getData().split(":");
        if (dat.length != 3 && dat.length != 4) {
            return;
        }
        if (dat[0].equals("bt") && dat[2].equals("first")) {
            Integer offset = Integer.parseInt(dat[1]);
            if (offset <= 0) {
                return;
            }
            offset--;
            listMyChannels(update, chatId, offset);
        } else if (dat[0].equals("bt") && dat[2].equals("last")) {
            Integer offset = Integer.parseInt(dat[1]);
            offset++;
            listMyChannels(update, chatId, offset);
        } else if (dat[0].equals("cl") && dat[2].equals("edit")) {
            editChannel(update, chatId, Long.parseLong(dat[1]));
        } else if (dat[0].equals("cl") && dat[2].equals("del")) {
            channelsService.removeChannel(Long.parseLong(dat[1]));
            listMyChannels(update, chatId, Integer.parseInt(dat[3]));
        } else if (dat[0].equals("wb")) {
            listMyChannels(update, chatId, 0);
        } else if (dat[0].equals("wd")) {
            channelsService.removeWord((Long.parseLong(dat[1])));
            editChannel(update, chatId, Long.parseLong(dat[3]));
        }
    }

    /**
     * @param update
     * @param chatId
     * @param channelId
     * @throws TelegramApiException
     */
    private void editChannel(Update update, Long chatId, Long channelId) throws TelegramApiException {
        CannelWorldListDTO c = channelsService.editChannel(chatId, channelId);
        if (c == null) {
            return;
        }
        List<List<InlineKeyboardButton>> kkk = new ArrayList<>();
        List<InlineKeyboardButton> kk = new ArrayList<>();
        InlineKeyboardButton k1 = new InlineKeyboardButton();
        k1.setText(c.getChannelName());
        k1.setUrl("https://t.me" + c.getChannelName().replace('@', '/'));
        kk.add(k1);

        for (WorldDTO w : c.getWorldDTOS()) {
            kk = new ArrayList<>();
            k1 = new InlineKeyboardButton();
            k1.setText(w.getWord());
            k1.setCallbackData("::");
            kk.add(k1);
            k1 = new InlineKeyboardButton();
            k1.setText("Удл.");
            k1.setCallbackData("wd:" + w.getId() + ":del:" + channelId);
            kk.add(k1);
            kkk.add(kk);
        }
        kk = new ArrayList<>();

        k1 = new InlineKeyboardButton();
        k1.setText("Вернуться");
        k1.setCallbackData("wb:-:-");
        kk.add(k1);
        kkk.add(kk);

        InlineKeyboardMarkup im = new InlineKeyboardMarkup();
        im.setKeyboard(kkk);

        EditMessageText em = new EditMessageText();
        em.setChatId(chatId.toString());
        em.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        em.setText("Редактирование фильтра:");
        em.setReplyMarkup(im);
        tlp.execute(em);
    }

    private void listMyChannels(Update update, Long chatId, Integer offset) throws TelegramApiException {
        List<ChannelDTO> channels = channelsService.listChannel(chatId, offset);

        List<List<InlineKeyboardButton>> kkk = new ArrayList<>();
        for (ChannelDTO c : channels) {
            List<InlineKeyboardButton> kk = new ArrayList<>();

            InlineKeyboardButton k1 = new InlineKeyboardButton();
            k1.setText(c.getName());
            k1.setUrl("https://t.me" + c.getName().replace('@', '/'));
            kk.add(k1);
            InlineKeyboardButton k2 = new InlineKeyboardButton();
            k2.setText("Инф.");
            k2.setCallbackData("cl:" + c.getId() + ":edit");
            kk.add(k2);
            InlineKeyboardButton k3 = new InlineKeyboardButton();
            k3.setText("Удл.");
            k3.setCallbackData("cl:" + c.getId() + ":del:" + offset);
            kk.add(k3);

            kkk.add(kk);
        }

        List<InlineKeyboardButton> kk = new ArrayList<>();

        InlineKeyboardButton k1 = new InlineKeyboardButton();
        k1.setText("<<<");
        k1.setCallbackData("bt:" + offset + ":first");
        kk.add(k1);
        InlineKeyboardButton k2 = new InlineKeyboardButton();
        k2.setText(offset.toString());
        k2.setCallbackData("::");
        kk.add(k2);
        InlineKeyboardButton k3 = new InlineKeyboardButton();
        k3.setText(">>>");
        if (channels.size() < 2) {
            k3.setCallbackData("bt:" + offset + ":");
        } else {
            k3.setCallbackData("bt:" + offset + ":last");
        }

        kk.add(k3);
        kkk.add(kk);

        InlineKeyboardMarkup im = new InlineKeyboardMarkup();
        im.setKeyboard(kkk);

        if (update.hasCallbackQuery()) {
            EditMessageText em = new EditMessageText();
            em.setChatId(chatId.toString());
            em.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            em.setText("Список отслеживаемых каналов:");
            em.setReplyMarkup(im);
            tlp.execute(em);
        } else {
            SendMessage em = new SendMessage();
            em.setChatId(chatId.toString());
            em.setText("Список отслеживаемых каналов:");
            em.setReplyMarkup(im);
            tlp.execute(em);
        }
    }

}
