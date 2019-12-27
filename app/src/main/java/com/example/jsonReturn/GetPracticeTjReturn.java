package com.example.jsonReturn;

/**
 * Created by ysg on 2017/9/26.
 */

public class GetPracticeTjReturn {
    private String sum;
    private String rightCount;
    private String wrongCount;
    private String answerStatus;
    private String accuracy;
    private boolean success;

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getRightCount() {
        return rightCount;
    }

    public void setRightCount(String rightCount) {
        this.rightCount = rightCount;
    }

    public String getWrongCount() {
        return wrongCount;
    }

    public void setWrongCount(String wrongCount) {
        this.wrongCount = wrongCount;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(String answerStatus) {
        this.answerStatus = answerStatus;
    }
}
