package com.example.jsonReturn;

import java.util.List;

public class CourseWCLXReturn {
	private List<String> learnPracBatchTimeList;
    private String rightCount;//对题
    private String finishQuestionCount;//做过的题目
    private String wrongCount;//错题
    private String learnPracBatchListSize;
    private String allFinishQuestionCount;
    private String questionSum;//所有题目
    private String batchId;//批次
    private String signCount;//标记
    private String paperXml;//试卷xml
    private String signQuestionIds;
    private String errorQuestionIdsStr;
    private String correctIds;
    private String errorIds;
    private boolean success;
	public List<String> getLearnPracBatchTimeList() {
		return learnPracBatchTimeList;
	}
	public void setLearnPracBatchTimeList(List<String> learnPracBatchTimeList) {
		this.learnPracBatchTimeList = learnPracBatchTimeList;
	}
	public String getRightCount() {
		return rightCount;
	}
	public void setRightCount(String rightCount) {
		this.rightCount = rightCount;
	}
	public String getFinishQuestionCount() {
		return finishQuestionCount;
	}
	public void setFinishQuestionCount(String finishQuestionCount) {
		this.finishQuestionCount = finishQuestionCount;
	}
	public String getWrongCount() {
		return wrongCount;
	}
	public void setWrongCount(String wrongCount) {
		this.wrongCount = wrongCount;
	}
	public String getLearnPracBatchListSize() {
		return learnPracBatchListSize;
	}
	public void setLearnPracBatchListSize(String learnPracBatchListSize) {
		this.learnPracBatchListSize = learnPracBatchListSize;
	}
	public String getAllFinishQuestionCount() {
		return allFinishQuestionCount;
	}
	public void setAllFinishQuestionCount(String allFinishQuestionCount) {
		this.allFinishQuestionCount = allFinishQuestionCount;
	}
	public String getQuestionSum() {
		return questionSum;
	}
	public void setQuestionSum(String questionSum) {
		this.questionSum = questionSum;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getSignCount() {
		return signCount;
	}
	public void setSignCount(String signCount) {
		this.signCount = signCount;
	}
	public String getPaperXml() {
		return paperXml;
	}
	public void setPaperXml(String paperXml) {
		this.paperXml = paperXml;
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
