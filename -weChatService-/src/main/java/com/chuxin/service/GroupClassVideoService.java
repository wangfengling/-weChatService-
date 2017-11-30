package com.chuxin.service;

import com.chuxin.dao.GroupClassStudentRepository;
import com.chuxin.dao.WXOrderRepository;
import com.chuxin.job.DeleteExpireClassTask;
import com.chuxin.job.RemindTask;
import com.chuxin.job.SendWXTemplateMessageTask;
import com.chuxin.pojo.GroupClassStudent;
import com.chuxin.pojo.WXOrder;
import com.chuxin.wxservice.WXMessageService;
import com.chuxin.wxservice.WXUserInfoService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangzhen on 2017/7/12.
 */
@Service
public class GroupClassVideoService {

    @Value("${grouptime}")
    private long groupTimeExpire;

    @Autowired
    private WXMessageService wxMessageService;

    @Autowired
    private WXUserInfoService wxUserInfoService;

    @Autowired
    private GroupClassStudentService groupClassStudentService;

    @Autowired
    private GroupClassStudentRepository groupClassStudentRepository;

    @Autowired
    private VideoService videoService;

    @Autowired
    private WXOrderRepository wxOrderRepository;

    @Autowired
    private WXPayService wxPayService;
    /**
     * Created by wangzhen on 2017/7/12.
     * 开始组班
     */
    @Transactional
    public String groupClass(Map<String,String> map){

        Timer timer = new Timer();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String  className = map.get("openId")+"&"+ map.get("courseId");
        List<String> userlist = new ArrayList<>();
        Date date = new Date();
        map.put("date",simpleDateFormat.format(date));
        groupClassStudentService.save(new GroupClassStudent(className,map.get("openId"),date,map.get("type")));
        String userInfo = wxUserInfoService.getUserInfoByOpenId(map.get("openId"));
        timer.schedule(new SendWXTemplateMessageTask(wxMessageService,timer,map,"success"),2*1000);
        timer.schedule(new DeleteExpireClassTask(map,timer,groupClassStudentService,wxMessageService,wxOrderRepository,wxPayService), groupTimeExpire*1000);
        userlist.add(userInfo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userInfo",userlist);
        jsonObject.put("className",className);
        jsonObject.put("date",date.getTime());
        jsonObject.put("createUser",map.get("openId"));
        return jsonObject.toString();
    }

    /**
     * Created by wangzhen on 2017/7/12.
     * 加入班级
     */
    @Transactional
    public String joinGroupClass(Map<String,String> map){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        GroupClassStudent groupClassStudent = groupClassStudentService.findByGroupStudentNameAndGroupClassNameEndingWithAndType(map.get("openId"),map.get("groupId"),map.get("type"));
        if (groupClassStudent == null){
            groupClassStudentService.save(new GroupClassStudent(map.get("groupId"),map.get("openId"),new Date(),map.get("type")));
        }
        List<GroupClassStudent> groupClassStudents = groupClassStudentService.findAllByGroupClassNameAndType(map.get("groupId"),map.get("type"));
        map.put("date",simpleDateFormat.format(new Date()));
        Timer timer = new Timer();
        timer.schedule(new SendWXTemplateMessageTask(wxMessageService,timer,map,"join"),2*1000);
        //wxMessageService.sendJoinClassMessage(map);
        List<String> userInfoList = new ArrayList<>();
        for (GroupClassStudent str : groupClassStudents){
            if (str.getGroupStudentName().equals(map.get("groupId").split("&")[0])){
                date = str.getDateCreated();
            }
            userInfoList.add(wxUserInfoService.getUserInfoByOpenId(str.getGroupStudentName()));
        }
        if (groupClassStudents.size() >= 3){
            map.put("date",simpleDateFormat.format(date));
            Timer timer1 = new Timer();
            timer1.schedule(new RemindTask(map,timer1,groupClassStudentService,wxMessageService), 4*1000);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userInfo", JSONArray.fromObject(userInfoList));
        jsonObject.put("classname",map.get("groupId"));
        jsonObject.put("courseId",map.get("groupId").split("&")[1]);
        jsonObject.put("data",date.getTime());
        return jsonObject.toString();
    }

    /**
     * Created by wangzhen on 2017/7/13.
     * 判断用户是否拥有看视频的权限
     */
    public String isPermitWatchVideo(String openId,String courseId,String type){
        String user = null;
        Date date = null;
        GroupClassStudent groupClassStudent =groupClassStudentService.findByGroupStudentNameAndGroupClassNameEndingWithAndType(openId,"&"+courseId,type);
        if (groupClassStudent == null){
            if (type.equals("vip")){
                WXOrder wxOrder = wxOrderRepository.findByOpenIdAndCourseIdAndStatus(openId,Integer.parseInt(courseId),WXOrder.Status.YesPay.name());
                if (wxOrder != null){
                    return "single";
                }
            }
            return "1";
        }
        List<String> userInfoList = new ArrayList<>();
        List<GroupClassStudent> groupClassStudentList = groupClassStudentService.findAllByGroupClassNameAndType(groupClassStudent.getGroupClassName(),type);
        for (GroupClassStudent groupClassStudent1 : groupClassStudentList){
            if (groupClassStudent1.getGroupStudentName().equals(groupClassStudent1.getGroupClassName().split("&")[0])){
                date = groupClassStudent1.getDateCreated();
                user = groupClassStudent1.getGroupStudentName();
            }
            userInfoList.add(wxUserInfoService.getUserInfoByOpenId(groupClassStudent1.getGroupStudentName()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userInfo",userInfoList);
        jsonObject.put("className",groupClassStudent.getGroupClassName());
        jsonObject.put("date",date.getTime());
        jsonObject.put("createUser",user);
        return jsonObject.toString();
    }

    /**
     * Created by wangzhen on 2017/7/13.
     * 拿到用户所有的组班的班级
     */
    public String getGroupClassListByOpenId(String openId,String type){
        List<Map<String,Object>> lists = new ArrayList<>();
        List<GroupClassStudent> groupClassStudents = groupClassStudentRepository.findAllByGroupStudentNameAndType(openId,type);
        for(GroupClassStudent groupClassStudent : groupClassStudents){
            Map map = null;
            if (type.equals("free")){
                map = videoService.getDetailsByCourseId(Integer.parseInt(groupClassStudent.getGroupClassName().split("&")[1])).get(0);
            }else {
                map = videoService.getVIPCourseDetailsByCourseGroup(Integer.parseInt(groupClassStudent.getGroupClassName().split("&")[1])).get(0);
            }
            List<GroupClassStudent> groupClassStudents1 = groupClassStudentRepository.findAllByGroupClassName(groupClassStudent.getGroupClassName());
            if (groupClassStudents1.size() >= 3){
                map.put("results","success");
            }else{
                map.put("results","fail");
            }
            map.put("classId",groupClassStudent.getGroupClassName());
            map.remove("content");
            lists.add(map);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("info",lists);
        return jsonObject.toString();
    }

    public String findAllByGroupClassName(String name) {
        String user = null;
        Date date = null;
        List<String> userInfoList = new ArrayList<>();
        List<GroupClassStudent> groupClassStudents = this.groupClassStudentRepository.findAllByGroupClassName(name);
        if (groupClassStudents.size() <= 0){
            return "1";
        }
        for (GroupClassStudent groupClassStudent : groupClassStudents){
            if (groupClassStudent.getGroupStudentName().equals(groupClassStudent.getGroupClassName().split("&")[0])){
                user = groupClassStudent.getGroupStudentName();
                date = groupClassStudent.getDateCreated();
            }
            userInfoList.add(wxUserInfoService.getUserInfoByOpenId(groupClassStudent.getGroupStudentName()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userInfo",userInfoList);
        jsonObject.put("className",name);
        jsonObject.put("date",date.getTime());
        jsonObject.put("createUser",user);
        return jsonObject.toString();
    }

    public WXOrder findVIPOrderInfoByClassName(String className){
        WXOrder wxOrder = wxOrderRepository.findByOpenIdAndCourseIdAndStatus(className.split("&")[0],Integer.parseInt(className.split("&")[1]),WXOrder.Status.YesPay.name());
        return  wxOrder;
    }
}


