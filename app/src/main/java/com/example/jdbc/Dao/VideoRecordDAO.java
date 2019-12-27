package com.example.jdbc.Dao;

import com.example.entity.VideoRecordModel;

import java.util.List;

/**
 * 视频学习行为Dao
 */
public interface VideoRecordDAO {

	//插入单条学习明细记录
	public int insertDetailRecord(VideoRecordModel videoRecordModel);

	//插入单条总汇记录
	public int insertTotalRecord (VideoRecordModel videoRecordModel);

	//查询学习记录
	public List<VideoRecordModel> queryRecord(String table,String selection,String[] selectionArgs,String orderBy);

	//查询用户一个学习行为的明细记录
	public List<VideoRecordModel> queryDetailRecordSingle(String classId, String behaviorId, String accountId);

	//查询用户所有学习行为的明细记录
	public List<VideoRecordModel> queryDetailRecordAll(String classId, String accountId);

	//查询用户一个学习行为的总汇记录
	public List<VideoRecordModel> queryTotalRecordSingle(String classId, String behaviorId, String accountId);

	//判断该点是否重复再某一段学习记录里面
    public VideoRecordModel isExistInCollection(int position,String classId,String behaviorId,String accountId,String option);

	//删除总汇重复记录
	public void deleteTotalRepeatRecord(int start,int end);

	//批量刪除记录
	public boolean deleteRecords(String table,List<VideoRecordModel> videoRecordModels);

	//批量删除明细记录
	public boolean deleteDetailRecords(List<VideoRecordModel> videoRecordModels);

	//批量删除总汇记录
	public boolean deleteTotalRecords(List<VideoRecordModel> videoRecordModels);

	//批量插入总汇记录
	public boolean insertRecordTotals(List<VideoRecordModel> videoRecordModels);
}