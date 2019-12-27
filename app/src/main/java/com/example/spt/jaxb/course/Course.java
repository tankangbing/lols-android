package com.example.spt.jaxb.course;

/**
 * 
 * @author zxh
 * 璧勬簮鍖匰rcom鏍囧噯XML鏄犲皠Model
 */
public class Course {
	/**
	 * 璇句欢璁″垎鏍囧噯
	 */
	Scoringstandard scoringstandard;
	/**
	 * 璇句欢琛屼负绾︽潫
	 */
	Constraint Constraint;
	/**
	 * 瀛︿範琛屼负鏍囧噯銆佸涔犺涓虹害鏉熴�佸墠缃涔犺涓�
	 */
	Learningbehaviors learningbehaviors;
	
	/**
	 * 棰滆壊
	 */
	String backgroundColor;
	/**
	 * 鍥剧墖
	 */
	ThemeImg themeImg;
	
	
	
	public Scoringstandard getScoringstandard() {
		return scoringstandard;
	}
	public void setScoringstandard(Scoringstandard scoringstandard) {
		this.scoringstandard = scoringstandard;
	}
	public Constraint getConstraint() {
		return Constraint;
	}
	public void setConstraint(Constraint constraint) {
		Constraint = constraint;
	}
	public Learningbehaviors getLearningbehaviors() {
		return learningbehaviors;
	}
	public void setLearningbehaviors(Learningbehaviors learningbehaviors) {
		this.learningbehaviors = learningbehaviors;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public ThemeImg getThemeImg() {
		return themeImg;
	}
	public void setThemeImg(ThemeImg themeImg) {
		this.themeImg = themeImg;
	}
	
	
	
	
	
}