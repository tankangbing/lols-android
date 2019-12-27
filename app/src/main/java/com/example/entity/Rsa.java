package com.example.entity;

/**
 * RSA公钥加密算法实体类
 * @author Amao
 *
 */
public class Rsa {

	private int id; //id
	private String appRsaModulus; //app rsa模
	private String sysRsaModulus; //v3 rsa模
	private String sysRsaPrivateExponent; //app 私钥指数
	private String appRsaPublicExponent; //v3 私钥指数

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAppRsaModulus() {
		return appRsaModulus;
	}
	public void setAppRsaModulus(String appRsaModulus) {
		this.appRsaModulus = appRsaModulus;
	}
	public String getSysRsaModulus() {
		return sysRsaModulus;
	}
	public void setSysRsaModulus(String sysRsaModulus) {
		this.sysRsaModulus = sysRsaModulus;
	}
	public String getSysRsaPrivateExponent() {
		return sysRsaPrivateExponent;
	}
	public void setSysRsaPrivateExponent(String sysRsaPrivateExponent) {
		this.sysRsaPrivateExponent = sysRsaPrivateExponent;
	}
	public String getAppRsaPublicExponent() {
		return appRsaPublicExponent;
	}
	public void setAppRsaPublicExponent(String appRsaPublicExponent) {
		this.appRsaPublicExponent = appRsaPublicExponent;
	}



}
