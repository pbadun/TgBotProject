package com.bb8qq.tgbotproject.lib;

/**
 * Задачи которые передает через шину.
 */
public interface TaskTurn {

    // Найденные каналы и группы.
    String _TASK_RESULT_SEARCH_CHATS = "TASK_SEARCH_CHATS_BACK";

    // Поиск каналов, групп..
    String _TASK_SEARCH_CHATS = "TASK_SEARCH_CHATS";

    // Добавить бота в чат
    String _TASK_JOIN_CHAT = "TASK_JOIN_CHAT";

    // Результат бодавления с группу
    String _TASK_RESULT_JOIN_CHAT = "TASK_RESULT_JOIN_CHAT";

}
