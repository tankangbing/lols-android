package com.example.entity;

import java.util.Date;

/**
 * Created by ysg on 2017/8/14.
 */

public class LearnClassStudentEntity {
    /**主键*/
    private java.lang.String id;
    /**课程班主键*/
    private java.lang.String classId;
    /**学习者主键*/
    private java.lang.String studentId;
    /**学习者名称*/
    private java.lang.String studentName;
    /**学习开始时间*/
    private java.util.Date learnStartTime;
    /**学习结束时间*/
    private java.util.Date learnEndTime;
    /**创建日期*/
    private java.util.Date createDate;
    /**创建用户名称*/
    private java.lang.String createUsername;
    /**创建用户主键*/
    private java.lang.String createUserid;
    /**修改日期*/
    private java.util.Date modifyDate;
    /**修改用户名称*/
    private java.lang.String modifyUsername;
    /**修改用户主键*/
    private java.lang.String modifyUserid;
    /**删除标记:0：未删除,1:删除 默认为0*/
    private java.lang.String delFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Date getLearnStartTime() {
        return learnStartTime;
    }

    public void setLearnStartTime(Date learnStartTime) {
        this.learnStartTime = learnStartTime;
    }

    public Date getLearnEndTime() {
        return learnEndTime;
    }

    public void setLearnEndTime(Date learnEndTime) {
        this.learnEndTime = learnEndTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUsername() {
        return createUsername;
    }

    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    public String getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyUsername() {
        return modifyUsername;
    }

    public void setModifyUsername(String modifyUsername) {
        this.modifyUsername = modifyUsername;
    }

    public String getModifyUserid() {
        return modifyUserid;
    }

    public void setModifyUserid(String modifyUserid) {
        this.modifyUserid = modifyUserid;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
