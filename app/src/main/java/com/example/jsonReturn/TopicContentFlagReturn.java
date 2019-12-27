package com.example.jsonReturn;

import com.example.entity.TopicSingle;

/**
 * Created by TJR on 2017/7/18.
 * 问题主体返回
 */

public class TopicContentFlagReturn {
    TopicSingle forumPostModel;
    boolean success;

    public TopicSingle getForumPostModel() {
        return forumPostModel;
    }

    public void setForumPostModel(TopicSingle forumPostModel) {
        this.forumPostModel = forumPostModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
