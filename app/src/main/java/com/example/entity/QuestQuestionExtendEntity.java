package com.example.entity;

/**
 * @Title: Entity
 * @Description: 题目扩展表
 * @author zhouwenhai
 * @date 2017-02-22 13:40:23
 * @version V1.0   
 *
 */
public class QuestQuestionExtendEntity implements java.io.Serializable {
	/**主键*/
	private String id;
	/**题目主键*/
	private String questionId;
	/**正确答案*/
	private String correctAnswer;
	/**答案附件名称*/
	private String answerAttachName;
	/**答案附件路径*/
	private String answerAttachPath;
	/**解析*/
	private String analysis;
	/**解析附件名称*/
	private String analysisAttachName;
	/**解析附件路径*/
	private String analysisAttachPath;
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
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */
	
	public String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  主键
	 */
	public void setId(String id){
		this.id = id;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  正确答案
	 */
	public String getCorrectAnswer(){
		return this.correctAnswer;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  正确答案
	 */
	public void setCorrectAnswer(String correctAnswer){
		this.correctAnswer = correctAnswer;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  答案附件路径
	 */
	public String getAnswerAttachPath(){
		return this.answerAttachPath;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  答案附件路径
	 */
	public void setAnswerAttachPath(String answerAttachPath){
		this.answerAttachPath = answerAttachPath;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  解析
	 */
	public String getAnalysis(){
		return this.analysis;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  解析
	 */
	public void setAnalysis(String analysis){
		this.analysis = analysis;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  解析附件路径
	 */
	public String getAnalysisAttachPath(){
		return this.analysisAttachPath;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  解析附件路径
	 */
	public void setAnalysisAttachPath(String analysisAttachPath){
		this.analysisAttachPath = analysisAttachPath;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  修改日期
	 */
	public java.util.Date getModifyDate(){
		return this.modifyDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  修改日期
	 */
	public void setModifyDate(java.util.Date modifyDate){
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
	public String getAnswerAttachName() {
		return answerAttachName;
	}
	public String getAnalysisAttachName() {
		return analysisAttachName;
	}

	public void setAnswerAttachName(String answerAttachName) {
		this.answerAttachName = answerAttachName;
	}

	public void setAnalysisAttachName(String analysisAttachName) {
		this.analysisAttachName = analysisAttachName;
	}
	
}
