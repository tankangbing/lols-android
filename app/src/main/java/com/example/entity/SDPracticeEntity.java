package com.example.entity;

import java.util.Date;

/**
 * Created by ysg on 2017/8/15.
 */

public class SDPracticeEntity {
    /**主键*/
    private java.lang.String id;
    /**学习者主键*/
    private java.lang.String accountId;
    /**学习者编码*/
    private java.lang.String accountCode;
    /**学习者名称*/
    private java.lang.String accountName;
    /**课程班主键*/
    private java.lang.String classId;
    /**课程班编码*/
    private java.lang.String classCode;
    /**学习行为主键*/
    private java.lang.String behaviorId;
    /**学习行为名称*/
    private java.lang.String behaviorName;
    /**练习次数*/
    private java.lang.Integer practiceTimes;
    /**题目总数*/
    private java.lang.Integer questionToalCount;
    /**答过题目总数*/
    private java.lang.Integer questionAnswerCount;
    /**答错过的题目总数*/
    private java.lang.Integer questionErrorCount;
    /**答对过的题目总数*/
    private java.lang.Integer questionCorrectCount;
    /**统计时间*/
    private java.util.Date statisticsDate;

    private String signQuestionIds;
    private String errorQuestionIdsStr;
    private String correctIds;
    private String errorIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(String behaviorId) {
        this.behaviorId = behaviorId;
    }

    public String getBehaviorName() {
        return behaviorName;
    }

    public void setBehaviorName(String behaviorName) {
        this.behaviorName = behaviorName;
    }

    public Integer getPracticeTimes() {
        return practiceTimes;
    }

    public void setPracticeTimes(Integer practiceTimes) {
        this.practiceTimes = practiceTimes;
    }

    public Integer getQuestionToalCount() {
        return questionToalCount;
    }

    public void setQuestionToalCount(Integer questionToalCount) {
        this.questionToalCount = questionToalCount;
    }

    public Integer getQuestionAnswerCount() {
        return questionAnswerCount;
    }

    public void setQuestionAnswerCount(Integer questionAnswerCount) {
        this.questionAnswerCount = questionAnswerCount;
    }

    public Integer getQuestionErrorCount() {
        return questionErrorCount;
    }

    public void setQuestionErrorCount(Integer questionErrorCount) {
        this.questionErrorCount = questionErrorCount;
    }

    public Integer getQuestionCorrectCount() {
        return questionCorrectCount;
    }

    public void setQuestionCorrectCount(Integer questionCorrectCount) {
        this.questionCorrectCount = questionCorrectCount;
    }

    public Date getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Date statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public String getSignQuestionIds() {
        return signQuestionIds;
    }

    public void setSignQuestionIds(String signQuestionIds) {
        this.signQuestionIds = signQuestionIds;
    }

    public String getErrorQuestionIdsStr() {
        return errorQuestionIdsStr;
    }

    public void setErrorQuestionIdsStr(String errorQuestionIdsStr) {
        this.errorQuestionIdsStr = errorQuestionIdsStr;
    }

    public String getCorrectIds() {
        return correctIds;
    }

    public void setCorrectIds(String correctIds) {
        this.correctIds = correctIds;
    }

    public String getErrorIds() {
        return errorIds;
    }

    public void setErrorIds(String errorIds) {
        this.errorIds = errorIds;
    }
}
