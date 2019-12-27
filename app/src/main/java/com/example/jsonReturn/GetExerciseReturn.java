package com.example.jsonReturn;

import java.util.List;

import com.example.entity.DLPracticeAnswerModel;
import com.example.entity.Question;
import com.example.entity.QuestionModel;

public class GetExerciseReturn {
    private String accuracy;
    private String answerStatus;
    private String rightCount;
    private String wrongCount;
    private String index;
    private String errorQuestionIdsStr;
    private String sum;
    private QuestionModel lastQuestion;
    private QuestionModel nextQuestion;
    private Question currentQuesiton;
    private List<DLPracticeAnswerModel> learnPracAnsRecordList;
    private List<DLPracticeAnswerModel> learnPracAnsRecordEntityList;
	
    public String getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}
	public String getAnswerStatus() {
		return answerStatus;
	}
	public void setAnswerStatus(String answerStatus) {
		this.answerStatus = answerStatus;
	}
	public String getRightCount() {
		return rightCount;
	}
	public void setRightCount(String rightCount) {
		this.rightCount = rightCount;
	}
	public String getWrongCount() {
		return wrongCount;
	}
	public void setWrongCount(String wrongCount) {
		this.wrongCount = wrongCount;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getErrorQuestionIdsStr() {
		return errorQuestionIdsStr;
	}
	public void setErrorQuestionIdsStr(String errorQuestionIdsStr) {
		this.errorQuestionIdsStr = errorQuestionIdsStr;
	}
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	public QuestionModel getLastQuestion() {
		return lastQuestion;
	}
	public void setLastQuestion(QuestionModel lastQuestion) {
		this.lastQuestion = lastQuestion;
	}
	public QuestionModel getNextQuestion() {
		return nextQuestion;
	}
	public void setNextQuestion(QuestionModel nextQuestion) {
		this.nextQuestion = nextQuestion;
	}
	public Question getCurrentQuesiton() {
		return currentQuesiton;
	}
	public void setCurrentQuesiton(Question currentQuesiton) {
		this.currentQuesiton = currentQuesiton;
	}
	public List<DLPracticeAnswerModel> getLearnPracAnsRecordList() {
		return learnPracAnsRecordList;
	}
	public void setLearnPracAnsRecordList(
			List<DLPracticeAnswerModel> learnPracAnsRecordList) {
		this.learnPracAnsRecordList = learnPracAnsRecordList;
	}
	public List<DLPracticeAnswerModel> getLearnPracAnsRecordEntityList() {
		return learnPracAnsRecordEntityList;
	}
	public void setLearnPracAnsRecordEntityList(
			List<DLPracticeAnswerModel> learnPracAnsRecordEntityList) {
		this.learnPracAnsRecordEntityList = learnPracAnsRecordEntityList;
	}
    
    
}
