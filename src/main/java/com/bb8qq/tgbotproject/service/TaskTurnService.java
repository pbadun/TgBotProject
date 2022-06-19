package com.bb8qq.tgbotproject.service;

import com.bb8qq.tgbotproject.lib.TaskTurnCallBack;

public interface TaskTurnService {

    void addCall(String key, TaskTurnCallBack callBack);

    void delCall(String key);

    void runCall(String key, String msg);
}
