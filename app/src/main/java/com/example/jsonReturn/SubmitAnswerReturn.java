package com.example.jsonReturn;

import com.example.entity.AnswerSingle;
import com.example.entity.TopicSingle;

/**
 * 提交问题返回
 */

public class SubmitAnswerReturn {
    int pageCount;
    AnswerSingle forumReplyFirstModel;
    AnswerSingle forumReplySecondModel;
    boolean success;
    int pageNum;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public AnswerSingle getForumReplyFirstModel() {
        return forumReplyFirstModel;
    }

    public void setForumReplyFirstModel(AnswerSingle forumReplyFirstModel) {
        this.forumReplyFirstModel = forumReplyFirstModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public AnswerSingle getForumReplySecondModel() {
        return forumReplySecondModel;
    }

    public void setForumReplySecondModel(AnswerSingle forumReplySecondModel) {
        this.forumReplySecondModel = forumReplySecondModel;
    }
}
