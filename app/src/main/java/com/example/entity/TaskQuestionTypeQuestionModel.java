package com.example.entity;

/**
 * @Description: 作业题型题目
 *
 */
public class TaskQuestionTypeQuestionModel implements java.io.Serializable {
	/**主键*/
	private String id;
	/**作业题型配置主键*/
	private String taskQuestiontypeId;
	/**题目主键*/
	private String questionId;
	/**题目排序号*/
	private Integer questionIndex;
	/**题目分值*/
	private Integer questionScore;
	/**题目状态0:启用，1：停用*/
	private String questionStatus;
	/**创建日期*/
	private java.sql.Timestamp createDate;
	/**创建用户名称*/
	private String createUsername;
	/**创建用户主键*/
	private String createUserid;
	/**修改日期*/
	private java.sql.Timestamp modifyDate;
	/**修改用户名称*/
	private String modifyUsername;
	/**修改用户主键*/
	private String modifyUserid;
	/**删除标记:0：未删除,1:删除 默认为0*/
	private String delFlag;
	/**题目*/
	QuestQuestionModel questQuestion;
	
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
	public String getTaskQuestiontypeId(){
		return this.taskQuestiontypeId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  作业题型配置主键
	 */
	public void setTaskQuestiontypeId(String taskQuestiontypeId){
		this.taskQuestiontypeId = taskQuestiontypeId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  题目主键
	 */
	public String getQuestionId(){
		return this.questionId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  题目主键
	 */
	public void setQuestionId(String questionId){
		this.questionId = questionId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  题目排序号
	 */
	public Integer getQuestionIndex(){
		return this.questionIndex;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  题目排序号
	 */
	public void setQuestionIndex(Integer questionIndex){
		this.questionIndex = questionIndex;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  题目分值
	 */
	public Integer getQuestionScore(){
		return this.questionScore;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  题目分值
	 */
	public void setQuestionScore(Integer questionScore){
		this.questionScore = questionScore;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  题目状态0:启用，1：停用
	 */
	public String getQuestionStatus(){
		return this.questionStatus;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  题目状态0:启用，1：停用
	 */
	public void setQuestionStatus(String questionStatus){
		this.questionStatus = questionStatus;
	}
	/**
	 *方法: 取得java.sql.Timestamp
	 *@return: java.sql.Timestamp  创建日期
	 */
	public java.sql.Timestamp getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.sql.Timestamp
	 *@param: java.sql.Timestamp  创建日期
	 */
	public void setCreateDate(java.sql.Timestamp createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建用户名称
	 */
	public String getCreateUsername(){
		return this.createUsername;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建用户名称
	 */
	public void setCreateUsername(String createUsername){
		this.createUsername = createUsername;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建用户主键
	 */
	public String getCreateUserid(){
		return this.createUserid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建用户主键
	 */
	public void setCreateUserid(String createUserid){
		this.createUserid = createUserid;
	}
	/**
	 *方法: 取得java.sql.Timestamp
	 *@return: java.sql.Timestamp  修改日期
	 */
	public java.sql.Timestamp getModifyDate(){
		return this.modifyDate;
	}

	/**
	 *方法: 设置java.sql.Timestamp
	 *@param: java.sql.Timestamp  修改日期
	 */
	public void setModifyDate(java.sql.Timestamp modifyDate){
		this.modifyDate = modifyDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  修改用户名称
	 */
	public String getModifyUsername(){
		return this.modifyUsername;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  修改用户名称
	 */
	public void setModifyUsername(String modifyUsername){
		this.modifyUsername = modifyUsername;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  修改用户主键
	 */
	public String getModifyUserid(){
		return this.modifyUserid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  修改用户主键
	 */
	public void setModifyUserid(String modifyUserid){
		this.modifyUserid = modifyUserid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  删除标记:0：未删除,1:删除 默认为0
	 */
	public String getDelFlag(){
		return this.delFlag;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  删除标记:0：未删除,1:删除 默认为0
	 */
	public void setDelFlag(String delFlag){
		this.delFlag = delFlag;
	}
	public QuestQuestionModel getQuestQuestion() {
		return questQuestion;
	}

	public void setQuestQuestion(QuestQuestionModel questQuestion) {
		this.questQuestion = questQuestion;
	}
	
}
