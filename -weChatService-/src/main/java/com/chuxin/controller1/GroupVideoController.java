package com.chuxin.controller1;

import com.chuxin.service.GroupClassVideoService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangzhen on 2017/7/12.
 */
@Controller
public class GroupVideoController {

    @Autowired
    private GroupClassVideoService groupClassVideoService;

    @RequestMapping(value = "/groupClass",method = RequestMethod.POST)
    @ResponseBody
    public String groupClass(@RequestBody  Map<String,String> data){
        JSONObject params = JSONObject.fromObject(data);
        Map<String,String> map = new HashMap<String, String>();
        map.put("openId",params.getString("openId"));
        map.put("courseId",params.getString("courseId"));
        map.put("courseName",params.getString("courseName"));
        map.put("url",params.getString("url"));
        map.put("type",params.getString("type"));
        String groupName = groupClassVideoService.groupClass(map);
        return groupName;
    }

    @RequestMapping(value = "/joinGroupClass",method = RequestMethod.POST)
    @ResponseBody
    public String joinGroupClass(@RequestBody  String data){
        JSONObject params = JSONObject.fromObject(data);
        Map<String,String> map = new HashMap<String, String>();
        map.put("openId",params.getString("openId"));
        map.put("groupId",params.getString("groupId"));
        map.put("courseName",params.getString("courseName"));
        map.put("url",params.getString("url"));
        map.put("type",params.getString("type"));
        String groupId = groupClassVideoService.joinGroupClass(map);
        return groupId;
    }

    @RequestMapping(value = "/isPermitWatchVideo/{openId}/{courseId}/{type}")
    @ResponseBody
    public String isPermitWatchVideo(@PathVariable String openId,@PathVariable String courseId,@PathVariable String type){
        String info = groupClassVideoService.isPermitWatchVideo(openId,courseId,type);
        return info;
    }


    @RequestMapping(value = "/getGroupClassListByOpenId/{openId}/{type}")
    @ResponseBody
    public String getGroupClassListByOpenId(@PathVariable String openId,@PathVariable String type){
        return groupClassVideoService.getGroupClassListByOpenId(openId,type);
    }

    @RequestMapping(value = "/getAllByGroupClassName/{groupId}")
    @ResponseBody
    public String getAllByGroupClassName(@PathVariable String groupId){
        return groupClassVideoService.findAllByGroupClassName(groupId);
    }
}
