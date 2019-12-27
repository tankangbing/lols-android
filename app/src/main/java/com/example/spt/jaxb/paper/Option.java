package com.example.spt.jaxb.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("option")
public class Option implements Serializable{
	/**
	 * 题目选项ID
	 */
	String id;
	/**
	 * 题目选项内容
	 */
	String content;
	/**
	 * 题目选项序号
	 */
	String index;
	/**
	 * 是否正确
	 * */
	String optionIsCorrent;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getOptionIsCorrent() {
		return optionIsCorrent;
	}
	public void setOptionIsCorrent(String optionIsCorrent) {
		this.optionIsCorrent = optionIsCorrent;
	}
	
	

}
