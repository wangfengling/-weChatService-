package com.chuxin.serviceImp;

import com.chuxin.dao.GroupClassStudentRepository;
import com.chuxin.pojo.GroupClassStudent;
import com.chuxin.service.GroupClassStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wangzhen on 2017/7/14.
 */
@Transactional
@Service
public class GroupClassStudentImpService implements GroupClassStudentService {
    @Autowired
    private GroupClassStudentRepository groupClassStudentRepository;


    @Override
    public GroupClassStudent save(GroupClassStudent groupClassStudent) {
        GroupClassStudent groupClassStudent1 = groupClassStudentRepository.save(groupClassStudent);
        return groupClassStudent1;
    }

    @Override
    public List<GroupClassStudent> findAllByGroupClassNameAndType(String name,String type) {
        List<GroupClassStudent> groupClassStudents = this.groupClassStudentRepository.findAllByGroupClassNameAndType(name,type);
        return groupClassStudents;
    }

    @Override
    public GroupClassStudent findByGroupStudentNameAndGroupClassNameEndingWithAndType(String name, String ends,String type) {
        GroupClassStudent groupClassStudents = this.groupClassStudentRepository.findByGroupStudentNameAndGroupClassNameEndingWithAndType(name,ends,type);
        return groupClassStudents;
    }

    @Override
    public void deleteByGroupStudentNameAndGroupClassName(String studentName, String className) {
        this.groupClassStudentRepository.deleteByGroupStudentNameAndGroupClassName(studentName,className);
    }


}
