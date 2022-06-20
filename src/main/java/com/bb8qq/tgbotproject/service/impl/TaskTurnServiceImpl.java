package com.bb8qq.tgbotproject.service.impl;

import com.bb8qq.tgbotproject.lib.TaskTurnCallBack;
import com.bb8qq.tgbotproject.service.TaskTurnService;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class TaskTurnServiceImpl<T> implements TaskTurnService {

    private HashMap<String, TaskTurnCallBack> task;

    public TaskTurnServiceImpl() {
        this.task = new HashMap<>();
    }

    public void addCall(String key, TaskTurnCallBack callBack) {
        task.put(key, callBack);
    }

    public void delCall(String key) {
        TaskTurnCallBack<String> s = task.get(key);
        if (s != null) {
            task.remove(key);
        }
    }

    @Override
    public void runCall(String key, Object msg) {
        TaskTurnCallBack<T> s = task.get(key);
        if (s != null) {
            s.call((T) msg);
        }
    }

}
