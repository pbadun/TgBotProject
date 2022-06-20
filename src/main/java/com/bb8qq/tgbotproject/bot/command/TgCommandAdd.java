package com.bb8qq.tgbotproject.bot.command;

import com.bb8qq.tgbotproject.bot.Command;
import com.bb8qq.tgbotproject.bot.TgCommand;
import com.bb8qq.tgbotproject.lib.TaskTurn;
import com.bb8qq.tgbotproject.lib.task.SearchChatsRequest;
import com.bb8qq.tgbotproject.lib.task.SearchChatsResponse;
import com.bb8qq.tgbotproject.service.LastMsgService;
import com.bb8qq.tgbotproject.service.TaskTurnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Command(commands = "/Добавить")
@Slf4j
public class TgCommandAdd extends TgCommand {

    @Autowired
    private LastMsgService lastMsgService;

    @Autowired
    private TaskTurnService taskTurnService;


    @PostConstruct
    public void initCallBack() {
        taskTurnService.addCall(TaskTurn._TASK_RESULT_SEARCH_CHATS, o -> {

            try {
                callSearch((SearchChatsResponse) o);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        });
    }

    private void callSearch(SearchChatsResponse r) throws TelegramApiException {
        switch (r.getTypeResult()) {
            case NO_RESULT:
                sendMessage(r.getChatId(), "Ничего не найденно!");
                break;
            case NO_GROUP:
                sendMessage(r.getChatId(), "Найден " + r.getTitle() + ", не доступно.");
                break;
            case TOTAL_RESULT:
                sendMessage(r.getChatId(), "Всего найденно: " + r.getTotal());
                break;
            case GROUP_ID:
                sendMessage(r.getChatId(), "Группа [" + r.getGroupId() + "]: " + r.getTitle() + " ");
                break;
            case CHANNELS_ID:
                sendMessage(r.getChatId(), "Чат [" + r.getGroupId() + "]: " + r.getTitle() + " ");
                break;
            case ERROR_QUERY:
                sendMessage(r.getChatId(), "Ошибка запроса.");
                break;
        }
    }


    @Override
    public Integer runCommand(Update update, Long chatId, Integer step) throws TelegramApiException {
        switch (step) {
            case 0:
                sendMessage(chatId, "Для добавление нового канала пришлите ссылку или имя канала/группы. ");
                return 1;
            case 1:
                lastMsgService.save(chatId, update.getMessage().getText());
                sendMessage(chatId, "Поиск каналов...");
                taskTurnService.runCall(TaskTurn._TASK_SEARCH_CHATS, new SearchChatsRequest(update.getMessage().getText(), chatId));
                return 2;
            case 2:
                sendMessage(chatId, "Еще разок....");
                taskTurnService.runCall(TaskTurn._TASK_SEARCH_CHATS, new SearchChatsRequest(update.getMessage().getText(), chatId));
                return 2;

        }
        return null;
    }


}
