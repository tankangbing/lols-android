package com.example.spt.jaxb.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;


@XStreamAlias("questionsList")
public class ResQuestionTypeRuleModel implements java.io.Serializable {
    /**主键*/
    @XStreamAsAttribute
    private java.lang.String id;
    /**成卷规则主键*/
    @XStreamAsAttribute
    private java.lang.String paperRuleId;
    /**试卷题型*/
    @XStreamAsAttribute
    private java.lang.String paperQuestionType;
    /**题型排序*/
    @XStreamAsAttribute
    private java.lang.Integer sortCode;
    /**总分值*/
    @XStreamAsAttribute
    private Double totalScore;
    /**默认每题分值*/
    @XStreamAsAttribute
    private Double singleQuestionScore;
    /**题目主键集合*/
    @XStreamImplicit(itemFieldName="question")
    private List<QuestionModel> question;

    public java.lang.String getId(){
        return this.id;
    }

    public void setId(java.lang.String id){
        this.id = id;
    }

    public java.lang.String getPaperRuleId(){
        return this.paperRuleId;
    }

    public void setPaperRuleId(java.lang.String paperRuleId){
        this.paperRuleId = paperRuleId;
    }

    public java.lang.String getPaperQuestionType(){
        return this.paperQuestionType;
    }

    public void setPaperQuestionType(java.lang.String paperQuestionType){
        this.paperQuestionType = paperQuestionType;
    }
    public java.lang.Integer getSortCode(){
        return this.sortCode;
    }

    public void setSortCode(java.lang.Integer sortCode){
        this.sortCode = sortCode;
    }

    public Double getTotalScore(){
        return this.totalScore;
    }

    public void setTotalScore(Double totalScore){
        this.totalScore = totalScore;
    }

    public Double getSingleQuestionScore(){
        return this.singleQuestionScore;
    }

    public void setSingleQuestionScore(Double singleQuestionScore){
        this.singleQuestionScore = singleQuestionScore;
    }

    public List<QuestionModel> getQuestion() {
        return question;
    }

    public void setQuestion(List<QuestionModel> question) {
        this.question = question;
    }

}
