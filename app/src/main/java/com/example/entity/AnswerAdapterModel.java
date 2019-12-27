package com.example.entity;

/**
 * AnswerRecyclerAdapter数据的类型
 */
public class AnswerAdapterModel {
    TopicSingle topicSingle;
    AnswerSingle answerSingle;

    public TopicSingle getTopicSingle() {
        return topicSingle;
    }

    public void setTopicSingle(TopicSingle topicSingle) {
        this.topicSingle = topicSingle;
    }

    public AnswerSingle getAnswerSingle() {
        return answerSingle;
    }

    public void setAnswerSingle(AnswerSingle answerSingle) {
        this.answerSingle = answerSingle;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AnswerAdapterModel)) {
            return false;
        }
        AnswerAdapterModel b = (AnswerAdapterModel)obj;
        if(this.answerSingle.getId() .equals(b.answerSingle.getId())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
