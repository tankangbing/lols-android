package com.example.jsonReturn;

import com.example.entity.Paper;

public class BuildPaperReturn {
	private Paper paper;
	private String adoptOrNotthrough;
	private String studentPaperId;
	private String paperScore;
	private String canCountdown;
	private String paperType;
	private String fullMarkType;
	private String canRedo;
	private String redoConstraint;
	private Integer redoTimes;
	private String displayCorrectAnswer;
	private String displayIsPass;
	private String displayCurrentScore;
	private String displayEffectiveScore;
	private String effectiveScoreType;
	private String standard;
	private String displayEffectivePaper;
	private Integer countdownTime;
    private String objectiveAnswerJson;
    private boolean hasPaper;

	public Paper getPaper() {
		return paper;
	}

	public void setPaper(Paper paper) {
		this.paper = paper;
	}

	public String getAdoptOrNotthrough() {
		return adoptOrNotthrough;
	}

	public void setAdoptOrNotthrough(String adoptOrNotthrough) {
		this.adoptOrNotthrough = adoptOrNotthrough;
	}

	public String getStudentPaperId() {
		return studentPaperId;
	}

	public void setStudentPaperId(String studentPaperId) {
		this.studentPaperId = studentPaperId;
	}

	public String getPaperScore() {
		return paperScore;
	}

	public void setPaperScore(String paperScore) {
		this.paperScore = paperScore;
	}

	public String getCanCountdown() {
		return canCountdown;
	}

	public void setCanCountdown(String canCountdown) {
		this.canCountdown = canCountdown;
	}

	public String getPaperType() {
		return paperType;
	}

	public void setPaperType(String paperType) {
		this.paperType = paperType;
	}

	public String getFullMarkType() {
		return fullMarkType;
	}

	public void setFullMarkType(String fullMarkType) {
		this.fullMarkType = fullMarkType;
	}

	public String getCanRedo() {
		return canRedo;
	}

	public void setCanRedo(String canRedo) {
		this.canRedo = canRedo;
	}

	public String getRedoConstraint() {
		return redoConstraint;
	}

	public void setRedoConstraint(String redoConstraint) {
		this.redoConstraint = redoConstraint;
	}

	public Integer getRedoTimes() {
		return redoTimes;
	}

	public void setRedoTimes(Integer redoTimes) {
		this.redoTimes = redoTimes;
	}

	public String getDisplayCorrectAnswer() {
		return displayCorrectAnswer;
	}

	public void setDisplayCorrectAnswer(String displayCorrectAnswer) {
		this.displayCorrectAnswer = displayCorrectAnswer;
	}

	public String getDisplayIsPass() {
		return displayIsPass;
	}

	public void setDisplayIsPass(String displayIsPass) {
		this.displayIsPass = displayIsPass;
	}

	public String getDisplayCurrentScore() {
		return displayCurrentScore;
	}

	public void setDisplayCurrentScore(String displayCurrentScore) {
		this.displayCurrentScore = displayCurrentScore;
	}

	public String getDisplayEffectiveScore() {
		return displayEffectiveScore;
	}

	public void setDisplayEffectiveScore(String displayEffectiveScore) {
		this.displayEffectiveScore = displayEffectiveScore;
	}

	public String getEffectiveScoreType() {
		return effectiveScoreType;
	}

	public void setEffectiveScoreType(String effectiveScoreType) {
		this.effectiveScoreType = effectiveScoreType;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getDisplayEffectivePaper() {
		return displayEffectivePaper;
	}

	public void setDisplayEffectivePaper(String displayEffectivePaper) {
		this.displayEffectivePaper = displayEffectivePaper;
	}

	public Integer getCountdownTime() {
		return countdownTime;
	}

	public void setCountdownTime(Integer countdownTime) {
		this.countdownTime = countdownTime;
	}

    public String getObjectiveAnswerJson() {
        return objectiveAnswerJson;
    }

    public void setObjectiveAnswerJson(String objectiveAnswerJson) {
        this.objectiveAnswerJson = objectiveAnswerJson;
    }

    public boolean isHasPaper() {
        return hasPaper;
    }

    public void setHasPaper(boolean hasPaper) {
        this.hasPaper = hasPaper;
    }
}
