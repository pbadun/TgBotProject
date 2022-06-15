package com.bb8qq.tgbotproject.bot.command;

import com.bb8qq.tgbotproject.bot.Command;
import com.bb8qq.tgbotproject.bot.TgCommand;
import com.bb8qq.tgbotproject.model.TgSession;
import com.bb8qq.tgbotproject.reposetory.TgSessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Deprecated
@Command(commands = "/session")
public class TgCommandSession extends TgCommand {

    @Autowired
    private TgSessionRepo sessionRepo;

    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        SendMessage s = new SendMessage();
        StringBuffer sb = new StringBuffer();
        List<TgSession> list = sessionRepo.findAll();
        for (TgSession t : list) {
            sb.append(t.toString());
            sb.append("\n");
        }
        if (sb.toString().equals("")) {
            return null;
        }
        s.setChatId(chatId.toString());
        s.setText(sb.toString());
        tlp.execute(s);
        return null;
    }
}
