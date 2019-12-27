package com.example.entity;

import com.example.spt.jaxb.course.*;
import com.example.spt.jaxb.course.Item;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by ysg on 2018/1/26.
 */

public class SearchItem {
    /**
     * 课件节点名称
     */
    String title;
    /**
     * 课件架构节点ID
     */
    String identifier;
    /**
     * 父节点名称
     */
    String nodeTitle;
    /**
     * 父节点ID
     */
    String nodeId;
    /**
     * 课件类型
     */
    String type;

    /**
     * 是否显示进度页
     */
    String showSchedule ;
    /**
     * 是否显示提示页
     */
    String showNotice;
    /**
     * 是否显示评价页
     */
    String showEvaluate;
    /**
     * 是否显示问答页
     */
    String showAsk;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getNodeTitle() {
        return nodeTitle;
    }

    public void setNodeTitle(String nodeTitle) {
        this.nodeTitle = nodeTitle;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShowSchedule() {
        return showSchedule;
    }

    public void setShowSchedule(String showSchedule) {
        this.showSchedule = showSchedule;
    }

    public String getShowNotice() {
        return showNotice;
    }

    public void setShowNotice(String showNotice) {
        this.showNotice = showNotice;
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
}
