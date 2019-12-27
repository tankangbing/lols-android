package com.example.entity;

import java.sql.Timestamp;
/**
 * Created by TJR on 2017/8/10.
 * 视频学习明细数据库model -- 表TABLE_VIDEO_DETAIL
 */
public class VideoRecordModel {
    /**
     * 记录主键
     */
    private int record_id;
    /**
     * 服务器主键
     */
    private String id;
    /**
     * 学习者主键
     */
    private String accountId;
    /**
     * 学习者名称
     */
    private String accountName;
    /**
     * 课程班主键
     */
    private String classId;
    /**
     * 学习行为主键
     */
    private String behaviorId;
    /**
     * 视频总长
     */
    private int totalTime;
    /**
     * 视频开始位置
     */
    private int startPoint;
    /**
     * 视频结束位置
     */
    private int endPoint;
    /**
     * 开始学习时间
     */
    private long startTime;
    /**
     * 结束学习时间
     */
    private long endTime;
    /**
     * 是否被总汇
     */
    private String isSummary;
    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 长度
     */
    private int learnLength ;

    public VideoRecordModel(){

    }

    public VideoRecordModel(String account_id, String account_name, String class_id, String behavior_id, int total_time, int start_point, int end_point) {
        this.accountId = account_id;
        this.accountName = account_name;
        this.classId = class_id;
        this.behaviorId = behavior_id;
        this.totalTime = total_time;
        this.startPoint = start_point;
        this.endPoint = end_point;
    }

    public int getRecord_id() {
        return record_id;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(String behaviorId) {
        this.behaviorId = behaviorId;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
    }

    public int getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(int endPoint) {
        this.endPoint = endPoint;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getIsSummary() {
        return isSummary;
    }

    public void setIsSummary(String isSummary) {
        this.isSummary = isSummary;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public int getLearnLength() {
        return learnLength;
    }

    public void setLearnLength(int learnLength) {
        this.learnLength = learnLength;
    }

    @Override
    public String toString() {
        return "VideoRecordModel{" +
                "record_id=" + record_id +
                ", accountId='" + accountId + '\'' +
                ", accountName='" + accountName + '\'' +
                ", classId='" + classId + '\'' +
                ", behaviorId='" + behaviorId + '\'' +
                ", totalTime=" + totalTime +
                ", startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", isSummary='" + isSummary + '\'' +
                ", dataSource='" + dataSource + '\'' +
                '}';
    }
}
