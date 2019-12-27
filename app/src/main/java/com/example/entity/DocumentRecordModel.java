package com.example.entity;

/**
 * Created by DELL on 2017/8/17.
 */

public class DocumentRecordModel {
    /**
     * 记录主键
     */
    private int record_id;
    /**
     * 主键
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
     * 学习者编码
     */
    private String accountCode;
    /**
     * 课程班主键
     */
    private String classId;
    /**
     * 课程班编码
     */
    private String classCode;
    /**
     * 学习行为主键
     */
    private String behaviorId;
    /**
     * 学习行为名称
     */
    private String behaviorName;
    /**
     * 点击时间
     */
    private long clickTime;
    /**
     * 点击次数
     */
    private int clickTimes;
    /**
     * 总页数
     */
    private int totalPages;
    /**
     * 已阅页数
     */
    private int readPages;
    /**
     * 当前页数
     */
    private int currentPage;
    /**
     * 是否被汇总
     */
    private String isSummary;
    /**
     * 数据来源
     */
    private String dataSource;
    /**
     * 统计时间
     */
    private long statisticsDate;

    public DocumentRecordModel() {
    }

    public DocumentRecordModel(String account_id, String account_name, String class_id, String behavior_id, int total_pages, int current_page) {
        this.accountId = account_id;
        this.accountName = account_name;
        this.classId = class_id;
        this.behaviorId = behavior_id;
        this.totalPages = total_pages;
        this.currentPage = current_page;
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

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(String behaviorId) {
        this.behaviorId = behaviorId;
    }

    public String getBehaviorName() {
        return behaviorName;
    }

    public void setBehaviorName(String behaviorName) {
        this.behaviorName = behaviorName;
    }

    public long getClickTime() {
        return clickTime;
    }

    public void setClickTime(long clickTime) {
        this.clickTime = clickTime;
    }

    public int getClickTimes() {
        return clickTimes;
    }

    public void setClickTimes(int clickTimes) {
        this.clickTimes = clickTimes;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getReadPages() {
        return readPages;
    }

    public void setReadPages(int readPages) {
        this.readPages = readPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
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

    public long getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(long statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    @Override
    public String toString() {
        return "DocumentRecordModel{" +
                "record_id=" + record_id +
                ", id=" + id +
                ", accountId='" + accountId + '\'' +
                ", accountName='" + accountName + '\'' +
                ", accountCode='" + accountCode + '\'' +
                ", classId='" + classId + '\'' +
                ", classCode='" + classCode + '\'' +
                ", behaviorId='" + behaviorId + '\'' +
                ", behaviorName='" + behaviorName + '\'' +
                ", clickTime='" + clickTime + '\'' +
                ", clickTimes=" + clickTimes +
                ", totalPages=" + totalPages +
                ", readPages=" + readPages +
                ", currentPage=" + currentPage +
                ", isSummary='" + isSummary + '\'' +
                ", dataSource='" + dataSource + '\'' +
                ", statisticsDate=" + statisticsDate +
                '}';
    }
}
