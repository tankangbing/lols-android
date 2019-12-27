package com.example.entity;

/**
 * Created by ysg on 2017/8/16.
 */

public class DLPracticeAnswerDetailEntity {

    /**主键*/
    private java.lang.String id;
    /**题目主键*/
    private java.lang.String questionId;
    /**选项主键*/
    private java.lang.String optionId;
    /**学习行为主键*/
    private java.lang.String behaviorId;
    /**批次主键*/
    private java.lang.String batchId;
    /**题目类型*/
    private java.lang.String questionType;
    /**作答结果*/
    private java.lang.String answerStatus;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(String behaviorId) {
        this.behaviorId = behaviorId;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(String answerStatus) {
        this.answerStatus = answerStatus;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
}
