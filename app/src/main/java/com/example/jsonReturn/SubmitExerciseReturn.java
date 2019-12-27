package com.example.jsonReturn;

import java.util.List;

public class SubmitExerciseReturn {
    private String msg;
    private List<String> answerConditionList;
    private String allFinishQuestionCount;
    private String signCount;//标记
    private String signQuestionIds;
    private String errorQuestionIdsStr;
    private String correctIds;
    private String errorIds;
    private String rightCount;//对题
    private String wrongCount;//错题
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<String> getAnswerConditionList() {
		return answerConditionList;
	}
	public void setAnswerConditionList(List<String> answerConditionList) {
		this.answerConditionList = answerConditionList;
	}

    public String getAllFinishQuestionCount() {
        return allFinishQuestionCount;
    }

    public void setAllFinishQuestionCount(String allFinishQuestionCount) {
        this.allFinishQuestionCount = allFinishQuestionCount;
    }

    public String getSignCount() {
        return signCount;
    }

    public void setSignCount(String signCount) {
        this.signCount = signCount;
    }

    public String getSignQuestionIds() {
        return signQuestionIds;
    }

    public void setSignQuestionIds(String signQuestionIds) {
        this.signQuestionIds = signQuestionIds;
    }

    public String getErrorQuestionIdsStr() {
        return errorQuestionIdsStr;
    }

    public void setErrorQuestionIdsStr(String errorQuestionIdsStr) {
        this.errorQuestionIdsStr = errorQuestionIdsStr;
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
}
