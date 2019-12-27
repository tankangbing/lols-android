package com.example.jsonReturn;

import com.example.entity.Paper;

import java.util.Map;

/**
 * Created by ysg on 2017/6/30.
 */

public class CheckQuestionReturn {
    private Paper paper;
    private double objectiveScore;
    Map<String,String> rightOptionMap;

    public double getObjectiveScore() {
        return objectiveScore;
    }

    public void setObjectiveScore(double objectiveScore) {
        this.objectiveScore = objectiveScore;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public Map<String, String> getRightOptionMap() {
        return rightOptionMap;
    }

    public void setRightOptionMap(Map<String, String> rightOptionMap) {
        this.rightOptionMap = rightOptionMap;
    }
}
