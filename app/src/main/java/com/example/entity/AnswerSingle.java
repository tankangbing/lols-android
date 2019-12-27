package com.example.entity;

public class AnswerSingle {

    String replierId;
    long replyTime;
    String delFlag;
    int upTimes;
    int downTimes;
    String replyContent;
    String replierName;
    String forumReplySecondCount;
    String postId;
    String id;
    String currentOpt;
    /**
     * 二级回答增加
     */
    String replyFirstId;
    String beReplierName;
    String beReplierId;

    public String getReplierId() {
        return replierId;
    }

    public void setReplierId(String replierId) {
        this.replierId = replierId;
    }

    public long getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(long replyTime) {
        this.replyTime = replyTime;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public int getUpTimes() {
        return upTimes;
    }

    public void setUpTimes(int upTimes) {
        this.upTimes = upTimes;
    }

    public int getDownTimes() {
        return downTimes;
    }

    public void setDownTimes(int downTimes) {
        this.downTimes = downTimes;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplierName() {
        return replierName;
    }

    public void setReplierName(String replierName) {
        this.replierName = replierName;
    }

    public String getForumReplySecondCount() {
        return forumReplySecondCount;
    }

    public void setForumReplySecondCount(String forumReplySecondCount) {
        this.forumReplySecondCount = forumReplySecondCount;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrentOpt() {
        return currentOpt;
    }

    public void setCurrentOpt(String currentOpt) {
        this.currentOpt = currentOpt;
    }

    public String getReplyFirstId() {
        return replyFirstId;
    }

    public void setReplyFirstId(String replyFirstId) {
        this.replyFirstId = replyFirstId;
    }

    public String getBeReplierName() {
        return beReplierName;
    }

    public void setBeReplierName(String beReplierName) {
        this.beReplierName = beReplierName;
    }

    public String getBeReplierId() {
        return beReplierId;
    }

    public void setBeReplierId(String beReplierId) {
        this.beReplierId = beReplierId;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AnswerSingle)) {
            return false;
        }
        AnswerSingle b = (AnswerSingle)obj;
        if(this.getId() .equals(b.getId())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
