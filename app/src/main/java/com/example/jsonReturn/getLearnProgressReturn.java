package com.example.jsonReturn;

import com.example.entity.LearnBehaviorModel;
import com.example.spt.jaxb.course.Item;

import java.util.List;

/**
 * Created by ysg on 2017/9/7.
 */

public class getLearnProgressReturn {
    String msg;
    boolean success;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    String lastNodeId;//最后一次点击节点主键
    String lastBehaviorId;//最后一次点击行为主键;
    String zhangName;//章名称
    String lastNodeName;//节点名称
    String lastBehaviorName;//行为名称
    String finishingRate;//班级进度
    String lastBehaviorShowNotice ;/**显示须知*/
    String lastBehaviorShowSchedule ;/**显示进度*/
    String lastBehaviorShowEvaluate ;/**显示评价*/
    String lastBehaviorShowAsk;/**显示提问*/
    String lastBehaviorShowShare ;/**显示分享*/
    String lastBehaviorType ;/**学习类型*/
    List<LearnBehaviorModel> courseItemList;

    public String getLastNodeId() {
        return lastNodeId;
    }

    public void setLastNodeId(String lastNodeId) {
        this.lastNodeId = lastNodeId;
    }

    public String getLastBehaviorId() {
        return lastBehaviorId;
    }

    public void setLastBehaviorId(String lastBehaviorId) {
        this.lastBehaviorId = lastBehaviorId;
    }

    public String getZhangName() {
        return zhangName;
    }

    public void setZhangName(String zhangName) {
        this.zhangName = zhangName;
    }

    public String getLastNodeName() {
        return lastNodeName;
    }

    public void setLastNodeName(String lastNodeName) {
        this.lastNodeName = lastNodeName;
    }

    public String getLastBehaviorName() {
        return lastBehaviorName;
    }

    public void setLastBehaviorName(String lastBehaviorName) {
        this.lastBehaviorName = lastBehaviorName;
    }

    public String getFinishingRate() {
        return finishingRate;
    }

    public void setFinishingRate(String finishingRate) {
        this.finishingRate = finishingRate;
    }

    public List<LearnBehaviorModel> getCourseItemList() {
        return courseItemList;
    }

    public void setCourseItemList(List<LearnBehaviorModel> courseItemList) {
        this.courseItemList = courseItemList;
    }

    public String getLastBehaviorShowNotice() {
        return lastBehaviorShowNotice;
    }

    public void setLastBehaviorShowNotice(String lastBehaviorShowNotice) {
        this.lastBehaviorShowNotice = lastBehaviorShowNotice;
    }

    public String getLastBehaviorShowSchedule() {
        return lastBehaviorShowSchedule;
    }

    public void setLastBehaviorShowSchedule(String lastBehaviorShowSchedule) {
        this.lastBehaviorShowSchedule = lastBehaviorShowSchedule;
    }

    public String getLastBehaviorShowEvaluate() {
        return lastBehaviorShowEvaluate;
    }

    public void setLastBehaviorShowEvaluate(String lastBehaviorShowEvaluate) {
        this.lastBehaviorShowEvaluate = lastBehaviorShowEvaluate;
    }

    public String getLastBehaviorShowAsk() {
        return lastBehaviorShowAsk;
    }

    public void setLastBehaviorShowAsk(String lastBehaviorShowAsk) {
        this.lastBehaviorShowAsk = lastBehaviorShowAsk;
    }

    public String getLastBehaviorShowShare() {
        return lastBehaviorShowShare;
    }

    public void setLastBehaviorShowShare(String lastBehaviorShowShare) {
        this.lastBehaviorShowShare = lastBehaviorShowShare;
    }

    public String getLastBehaviorType() {
        return lastBehaviorType;
    }

    public void setLastBehaviorType(String lastBehaviorType) {
        this.lastBehaviorType = lastBehaviorType;
    }
}
