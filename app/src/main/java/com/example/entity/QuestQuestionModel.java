package com.example.entity;

import java.util.List;

/**   
 * @Title: Entity
 * @Description: 题目表
 * @author zhouwenhai 
 * @date 2017-02-22 13:39:54
 * @version V1.0   
 *
 */
public class QuestQuestionModel implements java.io.Serializable {
	/**主键*/
	private String id;
	/**模式0:简单模式，1：高级模式，默认0*/
	private String questionModel;
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
	/**题干内容*/
	private String questionContent;
	/**附件名称*/
	private String attachName;
	/**附件路径*/
	private String attachPath;
	/**上级题目主键*/
	private String parentId;
	/**开班系统编码*/
	private String systemCode;
	/**开班系统名称*/
	private String systemName;
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
	/**题目选项表*/
	private List<QuestQuestionOptionEntity> questQuestionOptionList;
	/**题目扩展表*/
	private QuestQuestionExtendEntity questQuestionExtend;
	/**用户选择选项*/
	private String accountSelectedOption;
	/**用户回答答案*/
	private String accountAnswer; 
	/**学生主观题作答情况主键*/
	private String taskStudentSubjectId;
	/**学生主观题作答分数*/
	private Integer studentScore;
    /**附件名称*/
    private java.lang.String accountAttachName;
    /**附件答案路径*/
    private java.lang.String accountAttachPath;

    private List<StudentTaskModel> list;
    public String getAccountAttachName() {
        return accountAttachName;
    }

    public void setAccountAttachName(String accountAttachName) {
        this.accountAttachName = accountAttachName;
    }

    public String getAccountAttachPath() {
        return accountAttachPath;
    }

    public void setAccountAttachPath(String accountAttachPath) {
        this.accountAttachPath = accountAttachPath;
    }

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
	 *@return: java.lang.String  模式0:简单模式，1：高级模式，默认0
	 */
	public String getQuestionModel(){
		return this.questionModel;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  模式0:简单模式，1：高级模式，默认0
	 */
	public void setQuestionModel(String questionModel){
		this.questionModel = questionModel;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  题型
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
	 *@param: java.lang.String  题型
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
	 *@return: java.lang.String  题干内容
	 */
	public String getQuestionContent(){
		return this.questionContent;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  题干内容
	 */
	public void setQuestionContent(String questionContent){
		this.questionContent = questionContent;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  附件路径
	 */
	public String getAttachPath(){
		return this.attachPath;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  附件路径
	 */
	public void setAttachPath(String attachPath){
		this.attachPath = attachPath;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  上级题目主键
	 */
	public String getParentId(){
		return this.parentId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  上级题目主键
	 */
	public void setParentId(String parentId){
		this.parentId = parentId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  开班系统编码
	 */
	public String getSystemCode(){
		return this.systemCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  开班系统编码
	 */
	public void setSystemCode(String systemCode){
		this.systemCode = systemCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  开班系统名称
	 */
	public String getSystemName(){
		return this.systemName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  开班系统名称
	 */
	public void setSystemName(String systemName){
		this.systemName = systemName;
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
	public List<QuestQuestionOptionEntity> getQuestQuestionOptionList() {
		return questQuestionOptionList;
	}

	public void setQuestQuestionOptionList(
			List<QuestQuestionOptionEntity> questQuestionOptionList) {
		this.questQuestionOptionList = questQuestionOptionList;
	}
	public QuestQuestionExtendEntity getQuestQuestionExtend() {
		return questQuestionExtend;
	}

	public void setQuestQuestionExtend(QuestQuestionExtendEntity questQuestionExtend) {
		this.questQuestionExtend = questQuestionExtend;
	}
	public String getAccountSelectedOption() {
		return accountSelectedOption;
	}
	public String getAccountAnswer() {
		return accountAnswer;
	}

	public void setAccountSelectedOption(String accountSelectedOption) {
		this.accountSelectedOption = accountSelectedOption;
	}

	public void setAccountAnswer(String accountAnswer) {
		this.accountAnswer = accountAnswer;
	}
	public String getTaskStudentSubjectId() {
		return taskStudentSubjectId;
	}

	public void setTaskStudentSubjectId(String taskStudentSubjectId) {
		this.taskStudentSubjectId = taskStudentSubjectId;
	}
	public Integer getStudentScore() {
		return studentScore;
	}

	public void setStudentScore(Integer studentScore) {
		this.studentScore = studentScore;
	}
	public String getAttachName() {
		return attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

    public List<StudentTaskModel> getList() {
        return list;
    }

    public void setList(List<StudentTaskModel> list) {
        this.list = list;
    }
}
