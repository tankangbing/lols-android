package com.example.jsonReturn;

import com.example.entity.TopicSingle;

import java.util.List;

/**
 * Created by TJR on 2017/7/18.
 * 问题List返回
 */

public class TopicFlagReturn {
    /*TopicReturn attributes;
    String msg;
    boolean success;

    public TopicReturn getAttributes() {
        return attributes;
    }

    public void setAttributes(TopicReturn attributes) {
        this.attributes = attributes;
    }

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

    public static class TopicReturn {*/
    int forumPostCount;
    int pageCount;
    int pageNum;
    boolean success;
    List<TopicSingle> forumPostModelList;

    public int getForumPostCount() {
        return forumPostCount;
    }

    public void setForumPostCount(int forumPostCount) {
        this.forumPostCount = forumPostCount;
    }

    public List<TopicSingle> getForumPostModelList() {
        return forumPostModelList;
    }

    public void setForumPostModelList(List<TopicSingle> forumPostModelList) {
        this.forumPostModelList = forumPostModelList;
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

    public boolean isSuccess() {
        return success;
    }

        public void setSuccess(boolean success) {
            this.success = success;
        }
   // }
}
