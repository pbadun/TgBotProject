package com.bb8qq.tgbotproject.service.impl;

import com.bb8qq.tgbotproject.lib.TaskTurnCallBack;
import com.bb8qq.tgbotproject.service.TaskTurnService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class TaskTurnServiceImpl implements TaskTurnService {

    private HashMap<String, TaskTurnCallBack> task;

    public TaskTurnServiceImpl() {
        this.task = new HashMap<>();
    }

    /**
     * @param key
     * @param callBack
     */
    public void addCall(String key, TaskTurnCallBack callBack) {
        task.put(key, callBack);
    }

    /**
     * Удалить бэк.
     *
     * @param key
     */
    public void delCall(String key) {
        TaskTurnCallBack<String> s = task.get(key);
        if (s != null) {
            task.remove(key);
        }
    }

    /**
     * Вызов бэка.
     *
     * @param key
     * @param msg
     */
    public void runCall(String key, String msg) {
        TaskTurnCallBack<String> s = task.get(key);
        if (s != null) {
            s.call(msg);
        }
    }

}
