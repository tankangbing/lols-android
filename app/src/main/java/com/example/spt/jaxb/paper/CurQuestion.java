package com.example.spt.jaxb.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
        * 试卷题型下的题目
        *
        */

@XStreamAlias("curQuestion")
public class CurQuestion implements Serializable{
    /**
     * 题目ID
     */
    String questionId;
    /**
     * 序号
     */
    String index;
    /**
     * 题目在当前试卷中的分数
     */
    String score;
	
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
	
	
}
