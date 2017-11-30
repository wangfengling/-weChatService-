package com.chuxin.serviceImp;

import com.chuxin.dao.VideoDao;
import com.chuxin.pojo.GroupClassStudent;
import com.chuxin.service.GroupClassStudentService;
import com.chuxin.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangzhen on 2017/7/12.
 */
@Service
public class VideoServiceImp implements VideoService {
    @Autowired
    private VideoDao videoDao;

    @Autowired
    private GroupClassStudentService groupClassStudentService;

    @Override
    public List<Map<String, Object>> getGroupVideoList(int startId ,int sort ) {
        List<Map<String,Object>> list = videoDao.doFindGroupVideo(startId,sort);
        return list;
    }

    @Override
    public List<Map<String, Object>> getLessonPeriodByCourseId(String classId,String type) {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        List<GroupClassStudent> groupClassStudentList = groupClassStudentService.findAllByGroupClassNameAndType(classId,type);
        if (groupClassStudentList.size() < 3){
            return list;
        }
        list = videoDao.doFindCourseVideoList(Integer.parseInt(classId.split("&")[1]));
        return list;
    }

    @Override
    public List<Map<String, Object>>  getDetailsByCourseId(int courseId) {
        List<Map<String,Object>> list = videoDao.getDetailsByCourseId(courseId);
        return list;
    }

    @Override
    public List<Map<String, Object>> getVIPCourseList(int startId, int sort) {
        List<Map<String,Object>> list = videoDao.getVIPCourseList(startId,sort);
        return list;
    }

    @Override
    public List<Map<String, Object>> getVIPCourseDetailsByCourseGroup(int courseGroupId) {
        return videoDao.getVIPCourseDetailsByCourseGroupId(courseGroupId);
    }
}
