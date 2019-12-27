package com.example.jsonReturn;

import java.util.List;

/**
 * Created by DELL on 2017/7/31.
 */

public class ChapterFlagReturn {
    List<ChildList> nodeList;
    boolean success;

    public List<ChildList> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<ChildList> nodeList) {
        this.nodeList = nodeList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class ChildList {

            int sortCode;
            String parentId;
            String parentIds;
            String structureNodeName;
            String structureNodeType;
            String structureNodeStatus;
            int structureNodeLearnHour;
            String coursewareId;
            String structureNodeId;
            List<ChildList> childList;
            String id;

            int Level;
            boolean isExpanded;
            public int getSortCode() {
                return sortCode;
            }

            public void setSortCode(int sortCode) {
                this.sortCode = sortCode;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public String getParentIds() {
                return parentIds;
            }

            public void setParentIds(String parentIds) {
                this.parentIds = parentIds;
            }

            public String getStructureNodeName() {
                return structureNodeName;
            }

            public void setStructureNodeName(String structureNodeName) {
                this.structureNodeName = structureNodeName;
            }

            public String getStructureNodeType() {
                return structureNodeType;
            }

            public void setStructureNodeType(String structureNodeType) {
                this.structureNodeType = structureNodeType;
            }

            public String getStructureNodeStatus() {
                return structureNodeStatus;
            }

            public void setStructureNodeStatus(String structureNodeStatus) {
                this.structureNodeStatus = structureNodeStatus;
            }

            public int getStructureNodeLearnHour() {
                return structureNodeLearnHour;
            }

            public void setStructureNodeLearnHour(int structureNodeLearnHour) {
                this.structureNodeLearnHour = structureNodeLearnHour;
            }

            public String getCoursewareId() {
                return coursewareId;
            }

            public void setCoursewareId(String coursewareId) {
                this.coursewareId = coursewareId;
            }

            public String getStructureNodeId() {
                return structureNodeId;
            }

            public void setStructureNodeId(String structureNodeId) {
                this.structureNodeId = structureNodeId;
            }

            public List<ChildList> getChildList() {
                return childList;
            }

            public void setChildList(List<ChildList> childList) {
                this.childList = childList;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getLevel() {
                return Level;
            }

            public void setLevel(int level) {
                Level = level;
            }

            public boolean isExpanded() {
                return isExpanded;
            }

            public void setExpanded(boolean expanded) {
                isExpanded = expanded;
            }
        }



}
