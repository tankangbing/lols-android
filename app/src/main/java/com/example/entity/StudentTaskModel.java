package com.example.entity;

/**
 * Created by ysg on 2017/8/24.
 */

public class StudentTaskModel {
    /**题目主键*/
    private String questionId;
    private String childrenQuestionId;
    private String optionId;
    private String answer;
    private String questionType;

    public String getQuestionId() {
        return questionId;
    }
    public String getChildrenQuestionId() {
        return childrenQuestionId;
    }
    public String getOptionId() {
        return optionId;
    }
    public String getAnswer() {
        return answer;
    }
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
    public void setChildrenQuestionId(String childrenQuestionId) {
        this.childrenQuestionId = childrenQuestionId;
    }
    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getQuestionType() {
        return questionType;
    }
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
}
