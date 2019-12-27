package com.example.jsonReturn;

import java.util.ArrayList;
import java.util.List;

import com.example.entity.DLPracticeAnswerModel;
import com.example.entity.ExerciseCard;

public class GotoExerciseReturn {
    private String errorQuestionIdsStr;
    private String allFinishQuestionCount;
    private String batchId;
    private String currentQuestionId;
    private ExerciseCard exerciseCard;
    private String signQuestionIds;//标记主键串
    private String correctIds;//对题主键串
    private String errorIds;//错题主键串
    private Integer questionSum;//题目总数
	public String getErrorQuestionIdsStr() {
		return errorQuestionIdsStr;
	}
	public void setErrorQuestionIdsStr(String errorQuestionIdsStr) {
		this.errorQuestionIdsStr = errorQuestionIdsStr;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getCurrentQuestionId() {
		return currentQuestionId;
	}
	public void setCurrentQuestionId(String currentQuestionId) {
		this.currentQuestionId = currentQuestionId;
	}
	public String getAllFinishQuestionCount() {
		return allFinishQuestionCount;
	}
	public void setAllFinishQuestionCount(String allFinishQuestionCount) {
		this.allFinishQuestionCount = allFinishQuestionCount;
	}
	public ExerciseCard getExerciseCard() {
		return exerciseCard;
	}
	public void setExerciseCard(ExerciseCard exerciseCard) {
		this.exerciseCard = exerciseCard;
	}
	public String getCorrectIds() {
		return correctIds;
	}
	public void setCorrectIds(String correctIds) {
		this.correctIds = correctIds;
	}
	public String getErrorIds() {
		return errorIds;
	}
	public void setErrorIds(String errorIds) {
		this.errorIds = errorIds;
	}
	public String getSignQuestionIds() {
		return signQuestionIds;
	}
	public void setSignQuestionIds(String signQuestionIds) {
		this.signQuestionIds = signQuestionIds;
	}
	public Integer getQuestionSum() {
		return questionSum;
	}
	public void setQuestionSum(Integer questionSum) {
		this.questionSum = questionSum;
	}
	
    
}
