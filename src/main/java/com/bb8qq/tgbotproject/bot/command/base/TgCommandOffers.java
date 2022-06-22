package com.bb8qq.tgbotproject.bot.command.base;

import com.bb8qq.tgbotproject.bot.Command;
import com.bb8qq.tgbotproject.bot.TgCommand;
import com.bb8qq.tgbotproject.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Command(commands = "/Предложения")
public class TgCommandOffers extends TgCommand {

    @Autowired
    private SendMailService sendMailService;

    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        switch (step) {
            case 0:
                sendMessage(chatId, "Тут вы можете отправить пожелания или предложения по развитию ТГ Бота.");
                return 1;
            case 1:
                StringBuffer sb = new StringBuffer();
                sb.append("ChatId: ");
                sb.append(chatId.toString());
                sb.append("\n\n");
                sb.append(update.getMessage().getText());
                sendMailService.sendMail(sb.toString());
                sendMessage(chatId, "Спасибо за ваше сообщение.");
        }
        return -1;
    }


}
