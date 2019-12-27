package com.example.jsonReturn;


import com.example.entity.AnswerSingle;

import java.util.List;

/**
 * 一级回答返回
 */
public class AnswerOneFlagReturn {

    boolean success;
    int forumReplyFirstCount;
    List<AnswerSingle> forumReplyFirstModelList;
    List<AnswerSingle> list;
    int pageCount;
    int pageNum;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getForumReplyFirstCount() {
        return forumReplyFirstCount;
    }

    public void setForumReplyFirstCount(int forumReplyFirstCount) {
        this.forumReplyFirstCount = forumReplyFirstCount;
    }

    public List<AnswerSingle> getForumReplyFirstModelList() {
        return forumReplyFirstModelList;
    }

    public void setForumReplyFirstModelList(List<AnswerSingle> forumReplyFirstModelList) {
        this.forumReplyFirstModelList = forumReplyFirstModelList;
    }

    public List<AnswerSingle> getList() {
        return list;
    }

    public void setList(List<AnswerSingle> list) {
        this.list = list;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
