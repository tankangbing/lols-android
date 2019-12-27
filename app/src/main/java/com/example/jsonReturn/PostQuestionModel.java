package com.example.jsonReturn;

/**
 * 提交问提的model
 */
public class PostQuestionModel {

    //标题
    String postTitle;
    //班级id
    String classId;
    //课件id
    String coursewareId;
    //行为id
    String chapterId;
    //是否有附件
    String hasAttach;
    //是否回答
    String hasReply;
    //删除标记位
    String delFlag;

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getCoursewareId() {
        return coursewareId;
    }

    public void setCoursewareId(String coursewareId) {
        this.coursewareId = coursewareId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getHasAttach() {
        return hasAttach;
    }

    public void setHasAttach(String hasAttach) {
        this.hasAttach = hasAttach;
    }

    public String getHasReply() {
        return hasReply;
    }

    public void setHasReply(String hasReply) {
        this.hasReply = hasReply;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
