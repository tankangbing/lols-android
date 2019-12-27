package com.example.jsonReturn;

import com.example.entity.TopicSingle;

/**
 * 提交问题返回
 */

public class SubmitTopicReturn {
    TopicSingle forumPostModelList;
    boolean success;

    public TopicSingle getForumPostModelList() {
        return forumPostModelList;
    }

    public void setForumPostModelList(TopicSingle forumPostModelList) {
        this.forumPostModelList = forumPostModelList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
