package com.chuxin.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wangzhen on 2017/7/13.
 */
@Entity
public class GroupClassStudent {
    @Id
    @SequenceGenerator(name="GroupclassStudentId_generator", sequenceName="GroupclassStudentId_sequence", initialValue = 1)
    @GeneratedValue(generator = "GroupclassStudentId_generator")
    private Long id;

    @Column(nullable = true)
    private String groupClassName;

    @Column(nullable = true)
    private String groupStudentName;

    @Column(nullable = true)
    private Date dateCreated;

    @Column(nullable = true)
    private String type;

    public GroupClassStudent() {
    }

    public GroupClassStudent(String groupClassName, String groupStudentName, Date date,String type) {
        this.groupClassName = groupClassName;
        this.groupStudentName = groupStudentName;
        this.dateCreated = date;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupClassName() {
        return groupClassName;
    }

    public void setGroupClassName(String groupClassName) {
        this.groupClassName = groupClassName;
    }

    public String getGroupStudentName() {
        return groupStudentName;
    }

    public void setGroupStudentName(String groupStudentName) {
        this.groupStudentName = groupStudentName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
