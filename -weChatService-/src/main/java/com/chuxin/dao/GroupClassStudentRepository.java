package com.chuxin.dao;

import com.chuxin.pojo.GroupClassStudent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by wangzhen on 2017/7/13.
 */

public interface GroupClassStudentRepository extends CrudRepository<GroupClassStudent,Long> {
    List<GroupClassStudent> findAllByGroupClassNameAndType(String name,String type);

    List<GroupClassStudent> findAllByGroupClassName(String name);

    GroupClassStudent save(GroupClassStudent groupClassStudent);

    GroupClassStudent findByGroupStudentNameAndGroupClassNameEndingWithAndType(String name, String ends,String type);

    List<GroupClassStudent> findAllByGroupStudentNameAndType(String name,String type);

    void deleteByGroupStudentNameAndGroupClassName(String studentName, String className);



}
