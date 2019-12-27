package com.example.entity;

import java.io.Serializable;
import java.util.List;

public class ExerciseCard implements Serializable{
	/**活动主键*/
	private java.lang.String behaviorId;
	/**题目*/
	List<ResQuestionTypeRuleModel> questionsList;
	/**题目类型*/

	public java.lang.String getBehaviorId() {
		return behaviorId;
	}
	public List<ResQuestionTypeRuleModel> getQuestionsList() {
		return questionsList;
	}
	public void setBehaviorId(java.lang.String behaviorId) {
		this.behaviorId = behaviorId;
	}
	public void setQuestionsList(List<ResQuestionTypeRuleModel> questionsList) {
		this.questionsList = questionsList;
	}
}
