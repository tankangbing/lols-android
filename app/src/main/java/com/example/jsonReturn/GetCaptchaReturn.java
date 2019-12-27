package com.example.jsonReturn;

/**
 * Created by ysg on 2017/9/5.
 */

public class GetCaptchaReturn {
    private String msg;
    private String captcha;
    private boolean result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
