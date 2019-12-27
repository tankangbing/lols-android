package com.example.entity;

/**
 * Created by ysg on 2017/8/21.
 */

public class DLPracticeQuestionActionEntity {
    /**主键*/
    private java.lang.String id;
    /**课程班主键*/
    private java.lang.String classId;
    /**学习行为主键*/
    private java.lang.String behaviorId;
    /**学习者主键*/
    private java.lang.String accountId;
    /**题目主键*/
    private java.lang.String questionId;
    /**题目类型*/
    private java.lang.String questionType;
    /**是否标记*/
    private java.lang.String isMarked;

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

    public String getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(String behaviorId) {
        this.behaviorId = behaviorId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getIsMarked() {
        return isMarked;
    }

    public void setIsMarked(String isMarked) {
        this.isMarked = isMarked;
    }
}
