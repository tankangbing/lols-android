package com.example.jdbc.Dao;

import com.example.entity.DocumentRecordModel;
import java.util.List;

/**
 * 文档文档数据层实现的接口类
 */
public interface DocumentDAO {
	// 插入单个文档明细
	public void insertDetailRecord(DocumentRecordModel documentRecordModel);

	// 批量插入文档总汇
	public boolean insertTotalRecords(List<DocumentRecordModel> documentRecordModels);

	// 批量刪除记录
	public boolean deleteRecords(String table,List<DocumentRecordModel> documentRecordModels);

	//批量删除明细记录
	public boolean deleteDetailRecords(List<DocumentRecordModel> documentRecordModels);

	//批量删除总汇记录
	public boolean deleteTotalRecords(List<DocumentRecordModel> documentRecordModels);

	//查询记录
	public List<DocumentRecordModel> queryRecord(String table, String selection, String[] selectionArgs);

	//查询单个用户 单个行为的明细
	public List<DocumentRecordModel> queryDetailRecordSingle(String classId,String behaviorId,String accountId);

	//查询单个用户 单个行为的总汇
	public List<DocumentRecordModel> queryTotalRecordSingle(String classId,String behaviorId,String accountId);


}
