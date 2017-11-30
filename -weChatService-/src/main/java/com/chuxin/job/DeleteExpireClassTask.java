package com.chuxin.job;

import com.chuxin.dao.WXOrderRepository;
import com.chuxin.pojo.GroupClassStudent;
import com.chuxin.pojo.WXOrder;
import com.chuxin.service.GroupClassStudentService;
import com.chuxin.service.WXPayService;
import com.chuxin.wxservice.WXMessageService;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangzhen on 2017/8/6.
 */
public class DeleteExpireClassTask extends TimerTask {
    private Timer timer;
    private Map<String,String> map;
    private GroupClassStudentService groupClassStudentService;
    private WXMessageService wxMessageService;
    private WXOrderRepository wxOrderRepository;
    private WXPayService wxPayService;



    public DeleteExpireClassTask(Map<String,String> map,Timer timer,GroupClassStudentService groupClassStudentService,WXMessageService wxMessageService,WXOrderRepository wxOrderRepository,WXPayService wxPayService) {
        this.map = map;
        this.timer = timer;
        this.groupClassStudentService = groupClassStudentService;
        this.wxMessageService = wxMessageService;
        this.wxOrderRepository = wxOrderRepository;
        this.wxPayService = wxPayService;
    }

    @Override
    public void run() {

        List<GroupClassStudent> list = groupClassStudentService.findAllByGroupClassNameAndType(map.get("openId")+"&"+map.get("courseId"),map.get("type"));
        if (list.size() < 3){
            for (GroupClassStudent groupClassStudent : list){
                map.put("openId",groupClassStudent.getGroupStudentName());
                groupClassStudentService.deleteByGroupStudentNameAndGroupClassName(groupClassStudent.getGroupStudentName(),groupClassStudent.getGroupClassName());
                wxMessageService.sendClassFailMessage(map);
                if (map.get("type").equals("vip")){
                    WXOrder wxOrder = wxOrderRepository.findByOpenIdAndCourseIdAndStatus(map.get("openId"),Integer.parseInt(map.get("courseId")),WXOrder.Status.YesPay.name());
                    wxPayService.refund(wxOrder.getOrderId());
                }
            }
        }
        timer.cancel();
    }
}
