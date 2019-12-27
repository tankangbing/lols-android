package com.example.entity;

import java.util.List;

public class Questions implements java.io.Serializable{
	List<Question> question;

	public List<Question> getQuestion() {
		return question;
	}

	public void setQuestion(List<Question> question) {
		this.question = question;
	}
}
