package com.example.entity;

import org.codehaus.jackson.annotate.JsonProperty;

public class QuestionModel {
	/**题目主键*/
	private java.lang.String id;
	/**所属节点主键*/
	@JsonProperty("questionIndex")
	private java.lang.String questionIndex;

	/**题目题型*/
	private java.lang.String questionType;
	public java.lang.String getId() {
		return id;
	}
	public java.lang.String getQuestionIndex() {
		return questionIndex;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}

	public void setQuestionIndex(java.lang.String questionIndex) {
		this.questionIndex = questionIndex;
	}
	public java.lang.String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(java.lang.String questionType) {
		this.questionType = questionType;
	}

}
