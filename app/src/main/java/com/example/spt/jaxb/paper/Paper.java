package com.example.spt.jaxb.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.Serializable;
/**
 * 试卷XML映射Model
 * @author zxh
 *
 */
@XStreamAlias("paper")
public class Paper implements Serializable{

    @XStreamAsAttribute
	String id="";

    @XStreamOmitField
    Questiontypes questiontypes;
	Questions questions;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public Questiontypes getQuestiontypes() {
		return questiontypes;
	}
	public void setQuestiontypes(Questiontypes questiontypes) {
		this.questiontypes = questiontypes;
	}
	public Questions getQuestions() {
		return questions;
	}
	public void setQuestions(Questions questions) {
		this.questions = questions;
	}
	
	
}
