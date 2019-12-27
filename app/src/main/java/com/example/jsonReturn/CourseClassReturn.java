package com.example.jsonReturn;

import com.example.entity.LearnClassEntity;
import com.example.entity.LearnClassModel;
import com.example.entity.LearnClassStudentEntity;

import java.util.List;

/**
 * Created by ysg on 2017/7/25.
 */

public class CourseClassReturn {
    private List<LearnClassEntity> learnClassEntityList;
    private List<LearnClassStudentEntity> LearnClassStudentList;
    private String xml;


    public List<LearnClassEntity> getLearnClassEntityList() {
        return learnClassEntityList;
    }

    public void setLearnClassEntityList(List<LearnClassEntity> learnClassEntityList) {
        this.learnClassEntityList = learnClassEntityList;
    }

    public List<LearnClassStudentEntity> getLearnClassStudentList() {
        return LearnClassStudentList;
    }

    public void setLearnClassStudentList(List<LearnClassStudentEntity> learnClassStudentList) {
        LearnClassStudentList = learnClassStudentList;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
