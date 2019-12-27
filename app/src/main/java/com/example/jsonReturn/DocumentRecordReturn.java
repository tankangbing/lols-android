package com.example.jsonReturn;

import com.example.entity.DocumentRecordModel;

import java.util.List;

/**
 * 文档总汇返回
 */

public class DocumentRecordReturn {

    private List<DocumentRecordModel> sDFileEntity;
    private boolean success;

    public List<DocumentRecordModel> getsDFileEntity() {
        return sDFileEntity;
    }

    public void setsDFileEntity(List<DocumentRecordModel> sDFileEntity) {
        this.sDFileEntity = sDFileEntity;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
