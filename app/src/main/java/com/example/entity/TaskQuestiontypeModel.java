package com.example.entity;

import java.util.List;

/**   
 * @Description: 作业题型配置
 *
 */
public class TaskQuestiontypeModel implements java.io.Serializable {
	/**主键*/
	private String id;
	/**作业主键*/
	private String taskId;
	/**题型
            01:单选题
            02:多选题
            03:判断题
            04:填空题
            05:论述题
            06:材料题
            07:简答题
            08:名词解释题
            09:作图题
            10:大作业*/
	private String questionType;
	/**题型描述*/
	private String questionTypeDescribe;
	/**题型排序号*/
	private Integer questionTypeIndex;
	/**题目数量*/
	private Integer questionNumber;
	/**题型总分*/
	private Integer questionTypeScore;
	/**创建日期*/
	private java.util.Date createDate;
	/**创建用户名称*/
	private String createUsername;
	/**创建用户主键*/
	private String createUserid;
	/**修改日期*/
	private java.util.Date modifyDate;
	/**修改用户名称*/
	private String modifyUsername;
	/**修改用户主键*/
	private String modifyUserid;
	/**删除标记:0：未删除,1:删除 默认为0*/
	private String delFlag;
	/**作业题型题目集合*/
	private List<TaskQuestionTypeQuestionModel> taskQuestionTypeQuestionList;
	/**作业题型题目JSON字符串*/
	private String questionJsonStr;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */
	
	public String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 */
	public void setId(String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 */
	public String getTaskId(){
		return this.taskId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  作业主键
	 */
	public void setTaskId(String taskId){
		this.taskId = taskId;
	}
	/**
	 *方法: 取得java.lang.String
            01:单选题
            02:多选题
            03:判断题
            04:填空题
            05:论述题
            06:材料题
            07:简答题
            08:名词解释题
            09:作图题
            10:大作业
	 */
	public String getQuestionType(){
		return this.questionType;
	}

	/**
	 *方法: 设置java.lang.String
            01:单选题
            02:多选题
            03:判断题
            04:填空题
            05:论述题
            06:材料题
            07:简答题
            08:名词解释题
            09:作图题
            10:大作业
	 */
	public void setQuestionType(String questionType){
		this.questionType = questionType;
	}
	/**
	 *方法: 取得java.lang.String
	 */
	public String getQuestionTypeDescribe(){
		return this.questionTypeDescribe;
	}

	/**
	 *方法: 设置java.lang.String
	 */
	public void setQuestionTypeDescribe(String questionTypeDescribe){
		this.questionTypeDescribe = questionTypeDescribe;
	}
	/**
	 *方法: 取得java.lang.Integer
	 */
	public Integer getQuestionTypeIndex(){
		return this.questionTypeIndex;
	}

	/**
	 *方法: 设置java.lang.Integer
	 */
	public void setQuestionTypeIndex(Integer questionTypeIndex){
		this.questionTypeIndex = questionTypeIndex;
	}
	/**
	 *方法: 取得java.lang.Integer
	 */
	public Integer getQuestionNumber(){
		return this.questionNumber;
	}

	/**
	 *方法: 设置java.lang.Integer
	 */
	public void setQuestionNumber(Integer questionNumber){
		this.questionNumber = questionNumber;
	}
	/**
	 *方法: 取得java.lang.Integer
	 */
	public Integer getQuestionTypeScore(){
		return this.questionTypeScore;
	}

	/**
	 *方法: 设置java.lang.Integer
	 */
	public void setQuestionTypeScore(Integer questionTypeScore){
		this.questionTypeScore = questionTypeScore;
	}
	/**
	 *方法: 取得java.util.Date
	 */
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 */
	public String getCreateUsername(){
		return this.createUsername;
	}

	/**
	 *方法: 设置java.lang.String
	 */
	public void setCreateUsername(String createUsername){
		this.createUsername = createUsername;
	}
	/**
	 *方法: 取得java.lang.String
	 */
	public String getCreateUserid(){
		return this.createUserid;
	}

	/**
	 *方法: 设置java.lang.String
	 */
	public void setCreateUserid(String createUserid){
		this.createUserid = createUserid;
	}
	/**
	 *方法: 取得java.util.Date
	 */
	public java.util.Date getModifyDate(){
		return this.modifyDate;
	}

	/**
	 *方法: 设置java.util.Date
	 */
	public void setModifyDate(java.util.Date modifyDate){
		this.modifyDate = modifyDate;
	}
	/**
	 *方法: 取得java.lang.String
	 */
	public String getModifyUsername(){
		return this.modifyUsername;
	}

	/**
	 *方法: 设置java.lang.String
	 */
	public void setModifyUsername(String modifyUsername){
		this.modifyUsername = modifyUsername;
	}
	/**
	 *方法: 取得java.lang.String
	 */
	public String getModifyUserid(){
		return this.modifyUserid;
	}

	/**
	 *方法: 设置java.lang.String
	 */
	public void setModifyUserid(String modifyUserid){
		this.modifyUserid = modifyUserid;
	}
	/**
	 *方法: 取得java.lang.String
	 */
	public String getDelFlag(){
		return this.delFlag;
	}

	/**
	 *方法: 设置java.lang.String
	 */
	public void setDelFlag(String delFlag){
		this.delFlag = delFlag;
	}
	public List<TaskQuestionTypeQuestionModel> getTaskQuestionTypeQuestionList() {
		return taskQuestionTypeQuestionList;
	}

	public void setTaskQuestionTypeQuestionList(
			List<TaskQuestionTypeQuestionModel> taskQuestionTypeQuestionList) {
		this.taskQuestionTypeQuestionList = taskQuestionTypeQuestionList;
	}
	public String getQuestionJsonStr() {
		return questionJsonStr;
	}

	public void setQuestionJsonStr(String questionJsonStr) {
		this.questionJsonStr = questionJsonStr;
	}

}
