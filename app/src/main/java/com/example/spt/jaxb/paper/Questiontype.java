package com.example.spt.jaxb.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;


@XStreamAlias("questiontype")
public class Questiontype implements Serializable{
	/**
	 * 题目类型名称
	 */
	String questionTypeName ;
	/**
	 * 题目序号
	 */
	String index ;
	/**
	 * 题目总分
	 */
	String totalscore ;
    @XStreamImplicit(itemFieldName="curQuestion")
	List<CurQuestion> curQuestion;
	
	public String getQuestionTypeName() {
		return questionTypeName;
	}
	public void setQuestionTypeName(String questionTypeName) {
		this.questionTypeName = questionTypeName;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getTotalscore() {
		return totalscore;
	}
	public void setTotalscore(String totalscore) {
		this.totalscore = totalscore;
	}
	public List<CurQuestion> getCurQuestion() {
		return curQuestion;
	}
	public void setCurQuestion(List<CurQuestion> curQuestion) {
		this.curQuestion = curQuestion;
	}
	
	
}
