package com.example.entity;

/**
 * Created by ysg on 2017/8/10.
 */

public class PracticLearnBehavior {
    private String signQuestionIds;//标记题目主键串
    private String errorQuestionIdsStr;//所有批次错题主键串
    private String correctIds;//对题主键串
    private String errorIds;//错题主键串

    /**答过题目总数*/
    private java.lang.Integer questionAnswerCount;
    /**答错过的题目总数*/
    private java.lang.Integer questionErrorCount;
    /**错题总数*/
    private java.lang.Integer lastErrorCount;
    /**标记题目总数*/
    private java.lang.Integer questionSignCount;

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

    public Integer getQuestionSignCount() {
        return questionSignCount;
    }

    public void setQuestionSignCount(Integer questionSignCount) {
        this.questionSignCount = questionSignCount;
    }

    public Integer getLastErrorCount() {
        return lastErrorCount;
    }

    public void setLastErrorCount(Integer lastErrorCount) {
        this.lastErrorCount = lastErrorCount;
    }
}
