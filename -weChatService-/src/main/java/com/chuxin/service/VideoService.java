package com.chuxin.service;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzhen on 2017/7/12.
 */
public interface VideoService {
    List<Map<String ,Object>> getGroupVideoList(int startId, int sort);

    List<Map<String,Object>> getLessonPeriodByCourseId(String classId,String type);

    List<Map<String, Object>>  getDetailsByCourseId(int courseId);

    List<Map<String,Object>> getVIPCourseList(int startId,int sort);

    List<Map<String,Object>> getVIPCourseDetailsByCourseGroup(int courseGroupId);
}
