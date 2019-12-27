package com.example.jsonReturn;

/**
 * Created by TJR on 2017/7/5.
 * 视频返回
 */

public class RideoFlagReturn {
    /**
     * 返回是否成功
     */
    private boolean success;
    /**
     * 上一次观看的时间
     */
    private int lastEntPoint;
    /**
     * 视频总时间
     */
    private String videoTotalTime;
    /**
     * 存放视频的IP
     */
    private String resourceIPAddress;
    /**
     * 存放视频相对路径
     */
    private String resourceWebPath;
    /**
     * 视频名字
     */
    private String resourceName;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getLastEntPoint() {
        return lastEntPoint;
    }

    public void setLastEntPoint(int lastEntPoint) {
        this.lastEntPoint = lastEntPoint;
    }

    public String getVideoTotalTime() {
        return videoTotalTime ==null? "0":videoTotalTime;
    }

    public void setVideoTotalTime(String videoTotalTime) {
        this.videoTotalTime = videoTotalTime;
    }

    public String getResourceIPAddress() {
        return resourceIPAddress;
    }

    public void setResourceIPAddress(String resourceIPAddress) {
        this.resourceIPAddress = resourceIPAddress;
    }

    public String getResourceWebPath() {
        return resourceWebPath;
    }

    public void setResourceWebPath(String resourceWebPath) {
        this.resourceWebPath = resourceWebPath;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
