package com.example.entity;

/**
 * Created by TJR on 2017/7/5.
 * 视频片段model
 */

public class VideoSpeedSingle {
    //片段开始时间
    private String startPoint;
    //片段结束时间
    private String endPoint;

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
}
