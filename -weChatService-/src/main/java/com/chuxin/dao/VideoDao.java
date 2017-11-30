package com.chuxin.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzhen on 2017/7/12.
 */
public interface VideoDao {
     List<Map<String,Object>> doFindGroupVideo(int startId, int sort);

     List<Map<String,Object>> doFindCourseVideoList(int courseId);

     List<Map<String,Object>> getDetailsByCourseId(int courseId);

     List<Map<String,Object>> getVIPCourseList(int startId, int sort);

     List<Map<String,Object>> getVIPCourseDetailsByCourseGroupId(int courseGroupId);
}
