package com.example.jsonReturn;

import com.example.entity.TaskModel;

/**
 * Created by ysg on 2017/7/7.
 */

public class TaskShowReturn {
    private String taskStudentId;
    private String taskStudentStutas;
    private String accountType;
    private String classId;
    private String coursewareId;
    private String menuCode;
    private TaskModel taskModel;

    public String getTaskStudentId() {
        return taskStudentId;
    }

    public void setTaskStudentId(String taskStudentId) {
        this.taskStudentId = taskStudentId;
    }

    public String getTaskStudentStutas() {
        return taskStudentStutas;
    }

    public void setTaskStudentStutas(String taskStudentStutas) {
        this.taskStudentStutas = taskStudentStutas;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public TaskModel getTaskModel() {
        return taskModel;
    }

    public void setTaskModel(TaskModel taskModel) {
        this.taskModel = taskModel;
    }
}
