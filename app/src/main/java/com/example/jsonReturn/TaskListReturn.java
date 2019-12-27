package com.example.jsonReturn;

import com.example.entity.TaskModel;

import java.util.List;

/**
 * Created by ysg on 2017/7/6.
 */

public class TaskListReturn {
    private String accountType;
    private List<TaskModel> taskList;
    private String menuCode;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public List<TaskModel> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskModel> taskList) {
        this.taskList = taskList;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }
}
