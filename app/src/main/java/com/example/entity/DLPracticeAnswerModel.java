package com.example.entity;

public class DLPracticeAnswerModel {
	/**主键*/
	private java.lang.String id;
	/**课程班主键*/
	private java.lang.String classId;
	/**学习行为主键*/
	private java.lang.String behaviorId;
	/**学习者主键*/
	private java.lang.String accountId;
	/**练习批次主键*/
	private java.lang.String pracBatchId;
	/**题目主键*/
	private java.lang.String questionId;
	/**题目类型*/
	private java.lang.String questionType;
	/**答题时间*/
	private java.util.Date answerTime;
	/**答题状态*/
	private java.lang.String answerStatus;
	/**当前答题状态连续次数*/
	private java.lang.Integer currentStatusContinueCount;
	/**最高连续正确次数*/
	private java.lang.Integer highestContinueCorrectCount;
	/**最高连续错误次数*/
	private java.lang.Integer highestContinueErrorCount;
	/**子题目主键*/
	private java.lang.String subQuestionId;
	/**选项主键*/
	private java.lang.String optionId;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */

	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  主键
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  课程班主键
	 */
	public java.lang.String getClassId(){
		return this.classId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  课程班主键
	 */
	public void setClassId(java.lang.String classId){
		this.classId = classId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  学习行为主键
	 */
	public java.lang.String getBehaviorId(){
		return this.behaviorId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  学习行为主键
	 */
	public void setBehaviorId(java.lang.String behaviorId){
		this.behaviorId = behaviorId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  学习者主键
	 */
	public java.lang.String getAccountId(){
		return this.accountId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  学习者主键
	 */
	public void setAccountId(java.lang.String accountId){
		this.accountId = accountId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  练习批次主键
	 */
	public java.lang.String getPracBatchId(){
		return this.pracBatchId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  练习批次主键
	 */
	public void setPracBatchId(java.lang.String pracBatchId){
		this.pracBatchId = pracBatchId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  题目主键
	 */
	public java.lang.String getQuestionId(){
		return this.questionId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  题目主键
	 */
	public void setQuestionId(java.lang.String questionId){
		this.questionId = questionId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  题目类型
	 */
	public java.lang.String getQuestionType(){
		return this.questionType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  题目类型
	 */
	public void setQuestionType(java.lang.String questionType){
		this.questionType = questionType;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  答题时间
	 */
	public java.util.Date getAnswerTime(){
		return this.answerTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  答题时间
	 */
	public void setAnswerTime(java.util.Date answerTime){
		this.answerTime = answerTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  答题状态
	 */
	public java.lang.String getAnswerStatus(){
		return this.answerStatus;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  答题状态
	 */
	public void setAnswerStatus(java.lang.String answerStatus){
		this.answerStatus = answerStatus;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  当前答题状态连续次数
	 */
	public java.lang.Integer getCurrentStatusContinueCount(){
		return this.currentStatusContinueCount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  当前答题状态连续次数
	 */
	public void setCurrentStatusContinueCount(java.lang.Integer currentStatusContinueCount){
		this.currentStatusContinueCount = currentStatusContinueCount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  最高连续正确次数
	 */
	public java.lang.Integer getHighestContinueCorrectCount(){
		return this.highestContinueCorrectCount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  最高连续正确次数
	 */
	public void setHighestContinueCorrectCount(java.lang.Integer highestContinueCorrectCount){
		this.highestContinueCorrectCount = highestContinueCorrectCount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  最高连续错误次数
	 */
	public java.lang.Integer getHighestContinueErrorCount(){
		return this.highestContinueErrorCount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  最高连续错误次数
	 */
	public void setHighestContinueErrorCount(java.lang.Integer highestContinueErrorCount){
		this.highestContinueErrorCount = highestContinueErrorCount;
	}

	public java.lang.String getSubQuestionId() {
		return subQuestionId;
	}

	public java.lang.String getOptionId() {
		return optionId;
	}

	public void setSubQuestionId(java.lang.String subQuestionId) {
		this.subQuestionId = subQuestionId;
	}

	public void setOptionId(java.lang.String optionId) {
		this.optionId = optionId;
	}
}
