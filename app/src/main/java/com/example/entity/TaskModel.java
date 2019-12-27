package com.example.entity;

import java.util.Date;
import java.util.List;

public class TaskModel implements java.io.Serializable {
    /**主键*/
    private java.lang.String id;
    /**课程班主键*/
    private java.lang.String classId;
    /**作业名称*/
    private java.lang.String taskName;
    /**开始日期*/
    private java.util.Date startDate;
    /**学生提交截止日期*/
    private java.util.Date submitEndDate;
    /**阅卷截止日期*/
    private java.util.Date markingEndDate;

    /**开始日期*/
    private java.lang.String startDateString;
    /**学生提交截止日期*/
    private java.lang.String submitEndDateString;
    /**阅卷截止日期*/
    private java.lang.String markingEndDateString;


    /**总分*/
    private java.lang.Integer totalScore;
    /**作业描述*/
    private java.lang.String taskDescribe;
    /**作业状态0：已保存，1：已发布*/
    private java.lang.String taskStatus;
    /**登录用户类型*/
    private java.lang.String userType;
    /**课程班人数*/
    private java.lang.Integer classStudentNumber;
    /**作业提交人数*/
    private java.lang.Integer submitNumber;
    /**作业批改人数*/
    private java.lang.Integer markedNumber;
    /**作业题型集合*/
    private List<TaskQuestiontypeModel> taskQuestiontypeList;
    /**作业学生集合*/
    private List taskStudentList;
    /**题目集合*/
    private List<QuestQuestionModel> questQuestionList;
    /**创建用户主键*/
    private java.lang.String createUserid;
    /**发布时间*/
    private java.util.Date publishTime;
    /**发布人主键*/
    private java.lang.String publisherId;
    /**发布人名称*/
    private java.lang.String publisherName;
    /**学生等分*/
    private java.lang.Integer studentScore;
    /**当前用户主键*/
    private java.lang.String currentAccountId;

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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getSubmitEndDate() {
        return submitEndDate;
    }

    public void setSubmitEndDate(Date submitEndDate) {
        this.submitEndDate = submitEndDate;
    }

    public Date getMarkingEndDate() {
        return markingEndDate;
    }

    public void setMarkingEndDate(Date markingEndDate) {
        this.markingEndDate = markingEndDate;
    }

    public String getStartDateString() {
        return startDateString;
    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    public String getSubmitEndDateString() {
        return submitEndDateString;
    }

    public void setSubmitEndDateString(String submitEndDateString) {
        this.submitEndDateString = submitEndDateString;
    }

    public String getMarkingEndDateString() {
        return markingEndDateString;
    }

    public void setMarkingEndDateString(String markingEndDateString) {
        this.markingEndDateString = markingEndDateString;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public String getTaskDescribe() {
        return taskDescribe;
    }

    public void setTaskDescribe(String taskDescribe) {
        this.taskDescribe = taskDescribe;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getClassStudentNumber() {
        return classStudentNumber;
    }

    public void setClassStudentNumber(Integer classStudentNumber) {
        this.classStudentNumber = classStudentNumber;
    }

    public Integer getSubmitNumber() {
        return submitNumber;
    }

    public void setSubmitNumber(Integer submitNumber) {
        this.submitNumber = submitNumber;
    }

    public Integer getMarkedNumber() {
        return markedNumber;
    }

    public void setMarkedNumber(Integer markedNumber) {
        this.markedNumber = markedNumber;
    }

    public List<TaskQuestiontypeModel> getTaskQuestiontypeList() {
        return taskQuestiontypeList;
    }

    public void setTaskQuestiontypeList(List<TaskQuestiontypeModel> taskQuestiontypeList) {
        this.taskQuestiontypeList = taskQuestiontypeList;
    }

    public List getTaskStudentList() {
        return taskStudentList;
    }

    public void setTaskStudentList(List taskStudentList) {
        this.taskStudentList = taskStudentList;
    }

    public List<QuestQuestionModel> getQuestQuestionList() {
        return questQuestionList;
    }

    public void setQuestQuestionList(List<QuestQuestionModel> questQuestionList) {
        this.questQuestionList = questQuestionList;
    }

    public String getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public Integer getStudentScore() {
        return studentScore;
    }

    public void setStudentScore(Integer studentScore) {
        this.studentScore = studentScore;
    }

    public String getCurrentAccountId() {
        return currentAccountId;
    }

    public void setCurrentAccountId(String currentAccountId) {
        this.currentAccountId = currentAccountId;
    }
}
