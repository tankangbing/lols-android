package com.example.entity;

import java.util.Date;

/**
 * Created by ysg on 2017/8/14.
 */

public class LearnClassEntity {
    /**主键*/
    private java.lang.String id;
    /**课程班代码*/
    private java.lang.String classCode;
    /**课程班名称*/
    private java.lang.String className;
    /**0：未开班
     1：开课中*/
    private java.lang.String classStatus;
    /**开班系统编码*/
    private java.lang.String systemCode;
    /**开班系统名称*/
    private java.lang.String systemName;
    /**学习开始时间*/
    private java.util.Date learnStartTime;
    /**学习结束时间*/
    private java.util.Date learnEndTime;
    /**预约学习开始时间*/
    private java.util.Date appointmentStartTime;
    /**预约学习结束时间*/
    private java.util.Date appointmentEndTime;
    /**课程考核指标类型0:各章节统一考核指标
     1:章节个性化考核指标。*/
    private java.lang.String assessmentIndexType;
    /**考核指标video:80%;test:50*/
    private java.lang.String assessmentIndex;
    /**课程主键*/
    private java.lang.String courseId;
    /**课件主键*/
    private java.lang.String coursewareId;
    /**授课老师主键*/
    private java.lang.String teacherId;
    /**授课老师名称*/
    private java.lang.String teacherName;
    /**授课老师所在学校名称*/
    private java.lang.String teacherSchoolName;
    /**发布时间*/
    private java.util.Date publishTime;
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
    /**系统所属组织主键*/
    private java.lang.String systemOrgId;
    /**所属系统主键*/
    private java.lang.String belongSystemId;
    /**课程百分比*/
    private String passPercentStr;
    /**课程图片*/
    private String classPhotoPath;
    /**
     * 已经下载文件的大小
     */
    private String DownLoadFileSize;
    /**
     * 正在下载数量
     */
    private int DownLoadingNum;
    /**
     * 完成的数量
     */
    private int DownLoadFinishNum;
    /**
     * 所有文件数量
     */
    private int DownLoadAllNum;
    /**
     * 是否被选中
     */
    private boolean isSelect;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassStatus() {
        return classStatus;
    }

    public void setClassStatus(String classStatus) {
        this.classStatus = classStatus;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
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

    public Date getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public void setAppointmentStartTime(Date appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public Date getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public void setAppointmentEndTime(Date appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public String getAssessmentIndexType() {
        return assessmentIndexType;
    }

    public void setAssessmentIndexType(String assessmentIndexType) {
        this.assessmentIndexType = assessmentIndexType;
    }

    public String getAssessmentIndex() {
        return assessmentIndex;
    }

    public void setAssessmentIndex(String assessmentIndex) {
        this.assessmentIndex = assessmentIndex;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCoursewareId() {
        return coursewareId;
    }

    public void setCoursewareId(String coursewareId) {
        this.coursewareId = coursewareId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherSchoolName() {
        return teacherSchoolName;
    }

    public void setTeacherSchoolName(String teacherSchoolName) {
        this.teacherSchoolName = teacherSchoolName;
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

    public String getSystemOrgId() {
        return systemOrgId;
    }

    public void setSystemOrgId(String systemOrgId) {
        this.systemOrgId = systemOrgId;
    }

    public String getBelongSystemId() {
        return belongSystemId;
    }

    public void setBelongSystemId(String belongSystemId) {
        this.belongSystemId = belongSystemId;
    }

    public String getDownLoadFileSize() {
        return DownLoadFileSize;
    }

    public void setDownLoadFileSize(String downLoadFileSize) {
        DownLoadFileSize = downLoadFileSize;
    }

    public int getDownLoadingNum() {
        return DownLoadingNum;
    }

    public void setDownLoadingNum(int downLoadingNum) {
        DownLoadingNum = downLoadingNum;
    }

    public int getDownLoadFinishNum() {
        return DownLoadFinishNum;
    }

    public void setDownLoadFinishNum(int downLoadFinishNum) {
        DownLoadFinishNum = downLoadFinishNum;
    }

    public String getPassPercentStr() {
        return passPercentStr;
    }

    public void setPassPercentStr(String passPercentStr) {
        this.passPercentStr = passPercentStr;
    }

    public String getClassPhotoPath() {
        return classPhotoPath;
    }

    public void setClassPhotoPath(String classPhotoPath) {
        this.classPhotoPath = classPhotoPath;
    }

    public int getDownLoadAllNum() {
        return DownLoadAllNum;
    }

    public void setDownLoadAllNum(int downLoadAllNum) {
        DownLoadAllNum = downLoadAllNum;
    }
    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
