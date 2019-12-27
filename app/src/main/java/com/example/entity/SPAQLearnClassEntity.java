package com.example.entity;

/**
 * 食品安全课程进度
 */

public class SPAQLearnClassEntity {
    //课程编码
    private String classCode ;
    //课程时长
    private String courseLearnHour;
    //已观看时长
    private String studyhour;
    //老师名字
    private String teachername;
    //课程名字
    private String courseName;

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getCourseLearnHour() {
        return courseLearnHour;
    }

    public void setCourseLearnHour(String courseLearnHour) {
        this.courseLearnHour = courseLearnHour;
    }

    public String getStudyHour() {
        return studyhour;
    }

    public void setStudyHour(String studyHour) {
        this.studyhour = studyHour;
    }

    public String getTeacherName() {
        return teachername;
    }

    public void setTeacherName(String teacherName) {
        this.teachername = teacherName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
