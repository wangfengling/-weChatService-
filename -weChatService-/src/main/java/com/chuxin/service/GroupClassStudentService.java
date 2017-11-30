package com.chuxin.service;

import com.chuxin.pojo.GroupClassStudent;

import java.util.List;

/**
 * Created by wangzhen on 2017/7/14.
 */
public interface GroupClassStudentService {

    GroupClassStudent save(GroupClassStudent groupClassStudent);

    List<GroupClassStudent> findAllByGroupClassNameAndType(String name,String type);

    GroupClassStudent findByGroupStudentNameAndGroupClassNameEndingWithAndType(String name, String ends,String type);

    void deleteByGroupStudentNameAndGroupClassName(String studentName, String className);
}
