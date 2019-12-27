package com.example.entity;

/**
 * @Title: Entity
 * @Description: 测试试卷作答最新记录
 * @author ZWH
 * @date 2016-08-31 09:41:50
 * @version V1.0
 *
 */
@SuppressWarnings("serial")
public class DLTestEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**学习者主键*/
	private java.lang.String accountId;
	/**学习者名称*/
	private java.lang.String accountName;
	/**课程班主键*/
	private java.lang.String classId;
	/**学习行为主键*/
	private java.lang.String behaviorId;
	/**试卷主键*/
	private java.lang.String paperId;
	/**成绩*/
	private java.lang.Double score;
	/**是否被汇总*/
	private java.lang.String isSummary;
	/**创建日期*/
	private java.sql.Timestamp createDate;
	/**修改日期*/
	private java.sql.Timestamp modifyDate;
	/**删除标记:0：未删除,1:删除 默认为0*/
	private java.lang.String delFlag;

	/**
	 *方法: 取得java.lang.String
	 */
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 */
	public java.lang.String getAccountId(){
		return this.accountId;
	}

	/**
	 *方法: 设置java.lang.String
	 */
	public void setAccountId(java.lang.String accountId){
		this.accountId = accountId;
	}
	/**
	 *方法: 取得java.lang.String
	 */
	public java.lang.String getAccountName(){
		return this.accountName;
	}

	/**
	 *方法: 设置java.lang.String
	 */
	public void setAccountName(java.lang.String accountName){
		this.accountName = accountName;
	}
	/**
	 *方法: 取得java.lang.String
	 */
	public java.lang.String getClassId(){
		return this.classId;
	}

	/**
	 *方法: 设置java.lang.String
	 */
	public void setClassId(java.lang.String classId){
		this.classId = classId;
	}
	/**
	 *方法: 取得java.lang.String
	 */
	public java.lang.String getBehaviorId(){
		return this.behaviorId;
	}

	/**
	 *方法: 设置java.lang.String
	 */
	public void setBehaviorId(java.lang.String behaviorId){
		this.behaviorId = behaviorId;
	}
	/**
	 *方法: 取得java.lang.String
	 */
	public java.lang.String getPaperId(){
		return this.paperId;
	}

	/**
	 *方法: 设置java.lang.String
	 */
	public void setPaperId(java.lang.String paperId){
		this.paperId = paperId;
	}
	/**
	 *方法: 取得java.lang.Double
	 */
	public java.lang.Double getScore(){
		return this.score;
	}

	/**
	 *方法: 设置java.lang.Double
	 */
	public void setScore(java.lang.Double score){
		this.score = score;
	}
	/**
	 *方法: 取得java.lang.String
	 */
	public java.lang.String getIsSummary(){
		return this.isSummary;
	}

	/**
	 *方法: 设置java.lang.String
	 */
	public void setIsSummary(java.lang.String isSummary){
		this.isSummary = isSummary;
	}
	/**
	 *方法: 取得java.sql.Timestamp
	 */
	public java.sql.Timestamp getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.sql.Timestamp
	 */
	public void setCreateDate(java.sql.Timestamp createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.sql.Timestamp
	 */
	public java.sql.Timestamp getModifyDate(){
		return this.modifyDate;
	}

	/**
	 *方法: 设置java.sql.Timestamp
	 */
	public void setModifyDate(java.sql.Timestamp modifyDate){
		this.modifyDate = modifyDate;
	}
	/**
	 *方法: 取得java.lang.String
	 */
	public java.lang.String getDelFlag(){
		return this.delFlag;
	}

	/**
	 *方法: 设置java.lang.String
	 */
	public void setDelFlag(java.lang.String delFlag){
		this.delFlag = delFlag;
	}
}