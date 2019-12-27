package com.example.jsonReturn;

import com.example.entity.DLTestEntity;

import java.util.List;

public class TextOrExamIndexReturn {
	private String questionCount;//题目总数
	private String sumScore;//总分
	private String lastestScore;//最近分数
	private String highestScore;//最高分数
	private String averageScore;//平均分
	private Integer pastRecordsSize;//历史作答总数
	private List<DLTestEntity> pastRecords;//历史作答记录
    private boolean success;


	public String getQuestionCount() {
		return questionCount;
	}
	public void setQuestionCount(String questionCount) {
		this.questionCount = questionCount;
	}
	public String getSumScore() {
		return sumScore;
	}
	public void setSumScore(String sumScore) {
		this.sumScore = sumScore;
	}
	public String getLastestScore() {
		return lastestScore;
	}
	public void setLastestScore(String lastestScore) {
		this.lastestScore = lastestScore;
	}
	public String getHighestScore() {
		return highestScore;
	}
	public void setHighestScore(String highestScore) {
		this.highestScore = highestScore;
	}
	public String getAverageScore() {
		return averageScore;
	}
	public void setAverageScore(String averageScore) {
		this.averageScore = averageScore;
	}
	public Integer getPastRecordsSize() {
		return pastRecordsSize;
	}
	public void setPastRecordsSize(Integer pastRecordsSize) {
		this.pastRecordsSize = pastRecordsSize;
	}
	public List<DLTestEntity> getPastRecords() {
		return pastRecords;
	}
	public void setPastRecords(List<DLTestEntity> pastRecords) {
		this.pastRecords = pastRecords;
	}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
