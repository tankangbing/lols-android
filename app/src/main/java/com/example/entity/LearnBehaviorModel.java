package com.example.entity;

/**
 * Created by ysg on 2017/10/16.
 */

public class LearnBehaviorModel  {
    /**主键*/
    private java.lang.String id;
    /**课件主键*/
    private java.lang.String coursewareId;
    /**行为主键*/
    private java.lang.String behaviorId;
    /**所属课件架构节点主键*/
    private java.lang.String structureNodeId;
    /**所属课件架构节点名称*/
    private java.lang.String structureNodeName;
    /**一级节点名称*/
    private java.lang.String parentStructureNodeName;
    /**行为类型
     0：视频
     1：文档
     2：网页
     3：练习
     4：测试
     5：考试
     */
    private java.lang.String behaviorType;
    /**行为名称*/
    private java.lang.String behaviorName;
    /**排序号*/
    private java.lang.Integer sortCode;
    /**行为状态 0：显示，1：隐藏，默认0*/
    private java.lang.String behaviorStatus;
    /**行为须知*/
    private java.lang.String behaviorNotice;
    /**资源主键（视频、网页和文档专用）*/
    private java.lang.String resourceId;
    /**是否采用默认计分标准配置*/
    private java.lang.String defaultSoringStandard;
    /**是否采用默认行为约束配置*/
    private java.lang.String defaultBehaviorConstraints;
    /**删除标记:0：未删除,1:删除 默认为0*/
    private java.lang.String delFlag;
    /**显示须知*/
    private java.lang.String showNotice;
    /**显示进度*/
    private java.lang.String showSchedule;
    /**显示评价*/
    private java.lang.String showEvaluate;
    /**显示提问*/
    private java.lang.String showAsk;
    /**显示分享*/
    private java.lang.String showShare;

    /**完成情况   0：未开始 1：进行中 2：已完成*/
    String finishStatus;
    /**完成百分比或分数*/
    String finishPercent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoursewareId() {
        return coursewareId;
    }

    public void setCoursewareId(String coursewareId) {
        this.coursewareId = coursewareId;
    }

    public String getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(String behaviorId) {
        this.behaviorId = behaviorId;
    }

    public String getStructureNodeId() {
        return structureNodeId;
    }

    public void setStructureNodeId(String structureNodeId) {
        this.structureNodeId = structureNodeId;
    }

    public String getStructureNodeName() {
        return structureNodeName;
    }

    public void setStructureNodeName(String structureNodeName) {
        this.structureNodeName = structureNodeName;
    }

    public String getParentStructureNodeName() {
        return parentStructureNodeName;
    }

    public void setParentStructureNodeName(String parentStructureNodeName) {
        this.parentStructureNodeName = parentStructureNodeName;
    }

    public String getBehaviorType() {
        return behaviorType;
    }

    public void setBehaviorType(String behaviorType) {
        this.behaviorType = behaviorType;
    }

    public String getBehaviorName() {
        return behaviorName;
    }

    public void setBehaviorName(String behaviorName) {
        this.behaviorName = behaviorName;
    }

    public Integer getSortCode() {
        return sortCode;
    }

    public void setSortCode(Integer sortCode) {
        this.sortCode = sortCode;
    }

    public String getBehaviorStatus() {
        return behaviorStatus;
    }

    public void setBehaviorStatus(String behaviorStatus) {
        this.behaviorStatus = behaviorStatus;
    }

    public String getBehaviorNotice() {
        return behaviorNotice;
    }

    public void setBehaviorNotice(String behaviorNotice) {
        this.behaviorNotice = behaviorNotice;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getDefaultSoringStandard() {
        return defaultSoringStandard;
    }

    public void setDefaultSoringStandard(String defaultSoringStandard) {
        this.defaultSoringStandard = defaultSoringStandard;
    }

    public String getDefaultBehaviorConstraints() {
        return defaultBehaviorConstraints;
    }

    public void setDefaultBehaviorConstraints(String defaultBehaviorConstraints) {
        this.defaultBehaviorConstraints = defaultBehaviorConstraints;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getShowNotice() {
        return showNotice;
    }

    public void setShowNotice(String showNotice) {
        this.showNotice = showNotice;
    }

    public String getShowSchedule() {
        return showSchedule;
    }

    public void setShowSchedule(String showSchedule) {
        this.showSchedule = showSchedule;
    }

    public String getShowEvaluate() {
        return showEvaluate;
    }

    public void setShowEvaluate(String showEvaluate) {
        this.showEvaluate = showEvaluate;
    }

    public String getShowAsk() {
        return showAsk;
    }

    public void setShowAsk(String showAsk) {
        this.showAsk = showAsk;
    }

    public String getShowShare() {
        return showShare;
    }

    public void setShowShare(String showShare) {
        this.showShare = showShare;
    }

    public String getFinishStatus() {
        return finishStatus;
    }

    public void setFinishStatus(String finishStatus) {
        this.finishStatus = finishStatus;
    }

    public String getFinishPercent() {
        return finishPercent;
    }

    public void setFinishPercent(String finishPercent) {
        this.finishPercent = finishPercent;
    }
}