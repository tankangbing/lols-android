package com.example.jsonReturn;

import com.example.entity.LearnAccountEntity;

/**
 * 登录返回
 */
public class LoginReturn {
    private String status;
    private boolean success;
    private String msg;
    private LearnAccountEntity learnAccountEntity;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LearnAccountEntity getLearnAccountEntity() {
        return learnAccountEntity;
    }

    public void setLearnAccountEntity(LearnAccountEntity learnAccountEntity) {
        this.learnAccountEntity = learnAccountEntity;
    }
}
