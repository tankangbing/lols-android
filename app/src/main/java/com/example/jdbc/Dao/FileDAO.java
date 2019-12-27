package com.example.jdbc.Dao;

import com.example.entity.FileInfo;
import com.example.entity.ThreadInfo;

import java.util.List;

/**
 * 操作数据库的接口类
 */
public interface FileDAO {
	// 插入单个文件
	public boolean insertFile(FileInfo info);

	// 查询文件
	public List<FileInfo> queryFile(String table,String[] columns, String selection, String[] selectionArgs,String groupBy,String having,String  orderBy);

    //查询用户的课程id
    public List<String> queryClassIds(String student_id);

	// 查询用户单个课程的所有文件
	public List<FileInfo> querySingleClassFiles(String class_id,String student_id);

	// 查询用户单个课程总文件数量
	public int queryFilesSizeALL(String class_id,String student_id);

	// 查询用户单个课程完成文件数量
	public int queryFilesSizeFinish(String class_id,String student_id);

	// 查询用户单个课程正在下载的文件数量
	public int queryFilesSizeDownload(String class_id,String student_id);

	// 查询用户单个课程下载完成文件总大小
	public long queryFilesSize(String class_id, String student_id);

	// 查询用户所有课程下载完成文件总大小
	public long queryFilesTotalSize(String student_id);

	// 查询用户某些特定状态的文件
	public List<FileInfo> queryFilesInStatus(String student_id);

	//根据classId查询用户文件
	public List<FileInfo> queryFileWithClassId(String student_id, List<String> class_id);

	//根据behaviorId查询用户文件
	public List<FileInfo> queryFileWithBehaviorId(String student_id, List<String> behaviorIds);

    //查找该用户的所有文件
    public List<FileInfo> queryAllFiles(String student_id);

	//查找该用户的单个文件
	public FileInfo querySingleFile(String student_id,String behaviorId);

	//更新用户单个文件信息
	public void updateFile(FileInfo info);

	//初始化全部
	public void initAll(String coursewareId);

	//判断用户单个是否存在
	public boolean isExists(String behaviorId,String student_id);

	//统计单个课程下载列中文件数量
	public int queryFilesSize(String coursewareId);

	//批量刪除文件
	public boolean deleteFiles(List<FileInfo> FileInfos);

	// 批量更新用户文件
	public boolean updateFiles(List<FileInfo> FileInfos);

	// 批量插入文件
	public boolean insertFiles(List<FileInfo> FileInfos);



}
