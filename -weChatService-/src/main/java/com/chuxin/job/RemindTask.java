package com.chuxin.job;

import com.chuxin.pojo.GroupClassStudent;
import com.chuxin.service.GroupClassStudentService;
import com.chuxin.wxservice.WXMessageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangzhen on 2017/8/6.
 */
public class RemindTask extends TimerTask {
    @Autowired
    private WXMessageService wxMessageService;
    private Timer timer;
    private Map<String,String> map;
    @Autowired
    private GroupClassStudentService groupClassStudentService;

    public RemindTask(Map<String,String> map, Timer timer,GroupClassStudentService groupClassStudentService,WXMessageService wxMessageService){
        this.map = map;
        this.timer = timer;
        this.groupClassStudentService = groupClassStudentService;
        this.wxMessageService = wxMessageService;
    }
    public void run() {
        List<GroupClassStudent> list = groupClassStudentService.findAllByGroupClassNameAndType(map.get("groupId"),map.get("type"));
        map.put("success","组建班级成功!");
        for (GroupClassStudent groupClassStudent : list){
            map.put("openId",groupClassStudent.getGroupStudentName());
            wxMessageService.sendTemplateMessage(map);
        }
        timer.cancel();
    }
}
