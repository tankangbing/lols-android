package com.example.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 单个问题model
 */
public class TopicSingle implements Parcelable {
    String answerCount;
    long askTime;
    String chapterId;
    String chapterName;
    String classId ;
    String coursewareId;
    String hasAttach ;
    String hasReply;
    String id;
    String isEssence;
    String isTop;
    String isUseless;
    String postTitle;
    String questionerId;
    String questionerName;

    //后增
    String postContent;
    String forumPostType;
    String queryCondition;
    String currentAccountId;
    String forumPostOptTime;
    String queryForumPostType;
    String onlyShowMe;
    String queryContent;


    public String getAnswerCount() {
        return (answerCount==null)?"0":answerCount;
    }

    public void setAnswerCount(String answerCount) {
        this.answerCount = answerCount;
    }

    public long getAskTime() {
        return askTime;
    }

    public void setAskTime(long askTime) {
        this.askTime = askTime;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsEssence() {
        return isEssence;
    }

    public void setIsEssence(String isEssence) {
        this.isEssence = isEssence;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getIsUseless() {
        return isUseless;
    }

    public void setIsUseless(String isUseless) {
        this.isUseless = isUseless;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getQuestionerId() {
        return questionerId;
    }

    public void setQuestionerId(String questionerId) {
        this.questionerId = questionerId;
    }

    public String getQuestionerName() {
        return questionerName;
    }

    public void setQuestionerName(String questionerName) {
        this.questionerName = questionerName;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getForumPostType() {
        return forumPostType;
    }

    public void setForumPostType(String forumPostType) {
        this.forumPostType = forumPostType;
    }

    public String getQueryCondition() {
        return queryCondition;
    }

    public void setQueryCondition(String queryCondition) {
        this.queryCondition = queryCondition;
    }

    public String getCurrentAccountId() {
        return currentAccountId;
    }

    public void setCurrentAccountId(String currentAccountId) {
        this.currentAccountId = currentAccountId;
    }

    public String getForumPostOptTime() {
        return forumPostOptTime;
    }

    public void setForumPostOptTime(String forumPostOptTime) {
        this.forumPostOptTime = forumPostOptTime;
    }

    public String getQueryForumPostType() {
        return queryForumPostType;
    }

    public void setQueryForumPostType(String queryForumPostType) {
        this.queryForumPostType = queryForumPostType;
    }

    public String getOnlyShowMe() {
        return onlyShowMe;
    }

    public void setOnlyShowMe(String onlyShowMe) {
        this.onlyShowMe = onlyShowMe;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TopicSingle)) {
            return false;
        }
        TopicSingle b = (TopicSingle)obj;
        if(this.getId() .equals(b.getId())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(answerCount);
        dest.writeLong(askTime);
        dest.writeString(chapterId);
        dest.writeString(chapterName);
        dest.writeString(classId);
        dest.writeString(coursewareId);
        dest.writeString(hasAttach);
        dest.writeString(hasReply);
        dest.writeString(id);
        dest.writeString(isEssence);
        dest.writeString(isTop);
        dest.writeString(isUseless);
        dest.writeString(postTitle);
        dest.writeString(questionerId);
        dest.writeString(questionerName);
    }
    public static final Parcelable.Creator<TopicSingle> CREATOR = new Creator<TopicSingle>() {
        public TopicSingle createFromParcel(Parcel source) {
            TopicSingle topicSingle = new TopicSingle();
            topicSingle.answerCount = source.readString();
            topicSingle.askTime = source.readLong();
            topicSingle.chapterId = source.readString();
            topicSingle.chapterName = source.readString();
            topicSingle.classId = source.readString();
            topicSingle.coursewareId = source.readString();
            topicSingle.hasAttach = source.readString();
            topicSingle.hasReply = source.readString();
            topicSingle.id = source.readString();
            topicSingle.isEssence = source.readString();
            topicSingle.isTop = source.readString();
            topicSingle.isUseless = source.readString();
            topicSingle.postTitle = source.readString();
            topicSingle.questionerId = source.readString();
            topicSingle.questionerName = source.readString();
            return topicSingle;
        }
        public TopicSingle[] newArray(int size) {
            return new TopicSingle[size];
        }
    };

}
