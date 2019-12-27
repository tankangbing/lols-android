package com.example.jsonReturn;

import com.example.entity.VideoSpeedSingle;

import java.util.List;
import java.util.Map;

/**
 * Created by TJR on 2017/7/6.
 * 进度返回
 */

public class VideoSpeedFlagReturn {

    private boolean success;
    /**
     * 进度片段List
     */
    private List<VideoSpeedSingle> collectDLVideoSection =null;
    /**
     * 视频总时间
     */
    private int videoTotalTime;
    /**
     * 进度百分比
     */
    private String calculateVideoSectionPercentage;
    /**
     * 每日进程片段
     */
    private Map<String,List<String>> dateCollectDLVideoSection;

    public List<VideoSpeedSingle> getCollectDLVideoSection() {
        return collectDLVideoSection;
    }

    public void setCollectDLVideoSection(List<VideoSpeedSingle> collectDLVideoSection) {
        this.collectDLVideoSection = collectDLVideoSection;
    }

    public int getVideoTotalTime() {
        return videoTotalTime;
    }

    public void setVideoTotalTime(int videoTotalTime) {
        this.videoTotalTime = videoTotalTime;
    }

    public String getCalculateVideoSectionPercentage() {
        return calculateVideoSectionPercentage;
    }

    public void setCalculateVideoSectionPercentage(String calculateVideoSectionPercentage) {
        this.calculateVideoSectionPercentage = calculateVideoSectionPercentage;
    }
    public Map<String, List<String>> getDateCollectDLVideoSection() {
        return dateCollectDLVideoSection;
    }

    public void setDateCollectDLVideoSection(Map<String, List<String>> dateCollectDLVideoSection) {
        this.dateCollectDLVideoSection = dateCollectDLVideoSection;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
