package com.example.entity;

import java.util.List;

public class Answer {
	String content;
	List<Option> option;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<Option> getOption() {
		return option;
	}
	public void setOption(List<Option> option) {
		this.option = option;
	}
}
