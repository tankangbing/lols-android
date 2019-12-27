package com.example.jsonReturn;

import com.example.entity.DocumentRecordModel;
import com.example.entity.VideoRecordModel;

import java.util.List;

/**
 * Created by TJR on 2017/8/21.
 */

public class VideoRecordReturn {

    private List<VideoRecordModel> sDVideoSectionEntity;
    private boolean success;

    public List<VideoRecordModel> getsDVideoSectionEntity() {
        return sDVideoSectionEntity;
    }

    public void setsDVideoSectionEntity(List<VideoRecordModel> sDVideoSectionEntity) {
        this.sDVideoSectionEntity = sDVideoSectionEntity;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
