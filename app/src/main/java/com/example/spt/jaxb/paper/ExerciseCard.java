package com.example.spt.jaxb.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;

@XStreamAlias("exerciseCard")
public class ExerciseCard implements Serializable{
	/**活动主键*/
    @XStreamAsAttribute
	private String behaviorId;
	/**题目*/
    @XStreamImplicit(itemFieldName="questionsList")
	List<ResQuestionTypeRuleModel> questionsList;
	public String getBehaviorId() {
		return behaviorId;
	}
	public List<ResQuestionTypeRuleModel> getQuestionsList() {
		return questionsList;
	}
	public void setBehaviorId(String behaviorId) {
		this.behaviorId = behaviorId;
	}
	public void setQuestionsList(List<ResQuestionTypeRuleModel> questionsList) {
		this.questionsList = questionsList;
	}
	
	
}
