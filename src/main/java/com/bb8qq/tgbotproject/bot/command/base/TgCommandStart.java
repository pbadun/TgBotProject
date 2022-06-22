package com.bb8qq.tgbotproject.bot.command.base;

import com.bb8qq.tgbotproject.bot.Command;
import com.bb8qq.tgbotproject.bot.TgCommand;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Command(commands = "/start")
public class TgCommandStart extends TgCommand {

    String[] msg = {
            "Добро пожаловать!",
            "Я %s бот.",
            "Предоставляю возможность ананимно отслеживать открытые Телеграмм каналы с фильтрацией по ключевым словам.",
            "Для работы необходимо пройти простейшую регистрацию, это необходимо для того что-бы мы могли отправлять" +
                    " интересующий материал именно вам.",
            "Для \"прослушивания\" кналов необходимо добавить их в нашу базу, а так-же задать ключи для фильтра.",
            ""
    };

    @Value("${tg-bot.name}")
    private String botName;

    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        StringBuffer sb = new StringBuffer();
        for (String m : msg) {
            sb.append(m);
            sb.append("\n");
        }
        sendMessage(chatId, String.format(sb.toString(), botName));
        return -1;
    }

}
