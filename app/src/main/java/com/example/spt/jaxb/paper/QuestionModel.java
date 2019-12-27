package com.example.spt.jaxb.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

@XStreamAlias("question")
public class QuestionModel implements Serializable{
	/**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    /**题目主键*/
    @XStreamAsAttribute
    private java.lang.String id;
    /**所属节点主键*/
    @XStreamAsAttribute
    private java.lang.String questionIndex;
    /**题目题型*/
    @XStreamAsAttribute
    private java.lang.String questionType;

    public String getId() {
		return id;
	}
	public String getQuestionIndex() {
		return questionIndex;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public void setQuestionIndex(String questionIndex) {
		this.questionIndex = questionIndex;
	}
	
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	
}
