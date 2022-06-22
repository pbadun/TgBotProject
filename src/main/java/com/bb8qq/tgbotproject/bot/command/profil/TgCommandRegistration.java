package com.bb8qq.tgbotproject.bot.command.profil;

import com.bb8qq.tgbotproject.bot.Command;
import com.bb8qq.tgbotproject.bot.TgCommand;
import com.bb8qq.tgbotproject.model.TgUser;
import com.bb8qq.tgbotproject.reposetory.TgUserRepo;
import com.bb8qq.tgbotproject.service.LastMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Регистрация пользователей.
 */
@Command(commands = "/Регистрация")
public class TgCommandRegistration extends TgCommand {

    @Autowired
    private TgUserRepo tgUserRepo;

    @Autowired
    private LastMsgService lastMsg;

    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        switch (step) {
            case 0:
                sendMessage(chatId, "Как к вам обращаться ?",
                        keyboardMarkup(new String[]{"Ананим"})
                );
                return 1;
            case 1:
                lastMsg.save(chatId, update.getMessage().getText());
                further(chatId, String.format("Мы будет обращаться к вам как к \"%s\", согласны?\n" +
                        "Если нет - введите другое имя.", update.getMessage().getText()));
                return 1;
            case 2:
                String name = lastMsg.cutLastMsg(chatId);
                TgUser u = tgUserRepo.findTgUserByChatId(chatId);
                if (u == null) {
                    u = new TgUser();
                }
                u.setChatId(chatId);
                u.setAct(true);
                u.setDel(false);
                u.setUname(name);
                tgUserRepo.save(u);
                sendMessage(chatId,
                        String.format("Поздровляем %s! Вы успешно прошли регистрацию и теперь можете воспользоваться сервисом.", name)
                );
                return -1;
        }
        return null;
    }
}
