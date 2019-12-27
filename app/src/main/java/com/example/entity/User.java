package com.example.entity;

/**
 * 用户
 * @author Amao
 *
 */
public class User {

	private int id; //id
	private String userName; //登录帐号
	private String userPsw; //用户密码  --md5
	private String userXm; //用户姓名
	private String classIds; //课程班id串
	private String userId; //学生id

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPsw() {
		return userPsw;
	}
	public void setUserPsw(String userPsw) {
		this.userPsw = userPsw;
	}
	public String getClassIds() {
		return classIds;
	}
	public void setClassIds(String classIds) {
		this.classIds = classIds;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserXm() {
		return userXm;
	}
	public void setUserXm(String userXm) {
		this.userXm = userXm;
	}




}
