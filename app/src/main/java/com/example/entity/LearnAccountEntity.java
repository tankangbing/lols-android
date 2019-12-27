package com.example.entity;

import java.util.Date;

/**
 * Created by ysg on 2017/8/14.
 */

public class LearnAccountEntity {
    /**主键*/
    private java.lang.String id;
    /**0：学习者 1：指导者*/
    private java.lang.String accountType;
    /**登录密码*/
    private java.lang.String studentPassword;
    /**学习者编码*/
    private java.lang.String studentCode;
    /**学习者名称*/
    private java.lang.String studentName;
    /**学习者手机号*/
    private java.lang.String phoneNumber;
    /**学习者证件类型*/
    private java.lang.String certificateType;
    /**学习者证件号*/
    private java.lang.String certificateNumber;
    /**学习者邮箱*/
    private java.lang.String email;
    /**创建日期*/
    private java.util.Date createDate;
    /**修改日期*/
    private java.util.Date modifyDate;
    /**删除标记:0：未删除,1:删除 默认为0*/
    private java.lang.String delFlag;
    /**系统编码*/
    private java.lang.String systemCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    public void setStudentPassword(String studentPassword) {
        this.studentPassword = studentPassword;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
}
