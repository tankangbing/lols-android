package com.example.entity;


public class Paper implements java.io.Serializable{
	String id;
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
