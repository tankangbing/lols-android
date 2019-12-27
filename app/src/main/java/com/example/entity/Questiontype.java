package com.example.entity;

import com.example.spt.jaxb.paper.CurQuestion;

import java.util.List;

public class Questiontype implements java.io.Serializable{
	/**
	 * ��Ŀ��������
	 */
	String questionTypeName ;
	/**
	 * ��Ŀ���
	 */
	String index ;
	/**
	 * ��Ŀ�ܷ�
	 */
	String totalscore ;
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
