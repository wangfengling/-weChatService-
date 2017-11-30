package com.chuxin.controller;

import com.chuxin.service.GroupClassStudentService;
import com.chuxin.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzhen on 2017/7/12.
 */
@Controller
public class VideoController {
    @Autowired
    private VideoService videoService;

    @Autowired
    GroupClassStudentService groupClassStudentService;

    @RequestMapping(value = "/groupVideo/{startId}/{sort}")
    @ResponseBody
    public List<Map<String,Object>> getGroupVideo(@PathVariable int startId,@PathVariable int sort){
        return videoService.getGroupVideoList(startId,sort);
    }

    @RequestMapping(value = "/getLessonperiodListByCourseId/{classId}/{type}")
    @ResponseBody
    public List<Map<String,Object>> getLessonPeriodByCourseId(@PathVariable String classId,@PathVariable String type){
        return videoService.getLessonPeriodByCourseId(classId,type);
    }

    @RequestMapping(value = "/getDetailsByCourseId/{courseId}")
    @ResponseBody
    public List<Map<String, Object>>  getDetailsByCourseId(@PathVariable int courseId){
       return videoService.getDetailsByCourseId(courseId);
    }

    @RequestMapping(value = "/getVIPCourseList/{startId}/{sort}")
    @ResponseBody
    public List<Map<String,Object>> getVIPCourseList(@PathVariable int startId, @PathVariable int sort){
        return videoService.getVIPCourseList(startId,sort);
    }

    @RequestMapping(value = "/getVIPCourseDetails/{courseGroupId}")
    @ResponseBody
    public List<Map<String,Object>> getVIPCourseDetails(@PathVariable int courseGroupId){
        return videoService.getVIPCourseDetailsByCourseGroup(courseGroupId);
    }
}
