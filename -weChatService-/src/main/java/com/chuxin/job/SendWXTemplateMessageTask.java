package com.chuxin.job;

import com.chuxin.wxservice.WXMessageService;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SendWXTemplateMessageTask  extends TimerTask {
    private WXMessageService wxMessageService;
    private Timer timer;
    private Map<String,String> map;
    private String type;

    public SendWXTemplateMessageTask(WXMessageService wxMessageService, Timer timer, Map<String, String> map, String type) {
        this.wxMessageService = wxMessageService;
        this.timer = timer;
        this.map = map;
        this.type = type;
    }

    @Override
    public void run() {
        if (type.equals("join")){
            wxMessageService.sendJoinClassMessage(map);
        }
        if (type.equals("success")){
            wxMessageService.sendClassSuccessMessage(map);
        }
        timer.cancel();

    }
}
