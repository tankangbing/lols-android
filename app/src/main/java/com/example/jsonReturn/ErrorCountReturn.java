package com.example.jsonReturn;

public class ErrorCountReturn {
    private String count;
    private String correctCount;
    private String errorCount;
    private String errorQuestionIdsStr;
    private String correctIds;
    private String errorIds;
    private boolean success;
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getCorrectCount() {
		return correctCount;
	}
	public void setCorrectCount(String correctCount) {
		this.correctCount = correctCount;
	}
	public String getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(String errorCount) {
		this.errorCount = errorCount;
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
