package com.example.jdbc.Dao;

import com.example.entity.ThreadInfo;

import java.util.List;

/**
 * 操作数据库的接口类
 */
public interface ThreadDAO {
	// 插入綫程
	public void insertThread(ThreadInfo info);
	// 刪除綫程
	public void deleteThread(String url);
	// 更新綫程
	public void updateThread(String url, int thread_id, int finished);
	// 查詢綫程
	public List<ThreadInfo> queryThreads(String url);
	// 判斷綫程是否存在
	public boolean isExists(String url, int threadId);
	// 查詢綫程
	public List<ThreadInfo> queryAllThreads();
}
