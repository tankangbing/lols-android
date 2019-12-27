package com.example.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.entity.PlayHistory;
import com.example.util.FinalStr;
import com.example.util.SysUtil;

/**
 * 播放记录表操作
 * @author Amao
 *
 */
public class DBManagerToPlayHistory {
	private DatabaseHelper helper;

	public DBManagerToPlayHistory(Context context)
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> oncreate");
		helper = new DatabaseHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
	}

	/**
	 * 批量添加播放历史记录
	 *
	 * @param playhistoryList
	 */
	public void add(List<PlayHistory> playhistoryList)
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> add");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
		// 采用事务处理，确保数据完整性
		db.beginTransaction(); // 开始事务
		try
		{
			for (PlayHistory phl : playhistoryList)
			{
				db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
						+ " VALUES(null, ?, ?, ?)", new Object[] { phl.getMateid(),
						phl.getUserid(), phl.getTime() });
				// 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
				// 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
				// 使用占位符有效区分了这种情况
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		}
		finally
		{
			db.endTransaction(); // 结束事务
            DatabaseManager.getInstance(helper).closeDatabase();
		}
	}

	/**
	 * 单条播放记录
	 *
	 * @param plh
	 */
	public void savePlayHis(PlayHistory plh)
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> savePlayHis");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
		// 采用事务处理，确保数据完整性
		db.beginTransaction(); // 开始事务
		try
		{
			if(null != plh){
				db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
						+ " VALUES(null, ?, ?, ?)", new Object[] { plh.getMateid(),
						plh.getUserid(), plh.getTime() });
				db.setTransactionSuccessful(); // 设置事务成功完成
			}
		}
		finally
		{
			db.endTransaction(); // 结束事务
            DatabaseManager.getInstance(helper).closeDatabase();
		}
	}

	/**
	 * 修改
	 *
	 * @param  plh
	 */
	public void updatePlayHis(PlayHistory plh)
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> updatePlayHis");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("time", plh.getTime());
		db.update(DatabaseHelper.TABLE_NAME, cv, "mateid = ? and userid = ?",
				new String[] { plh.getMateid(),plh.getUserid() });
        DatabaseManager.getInstance(helper).closeDatabase();
	}

	/**
	 * 删除
	 *
	 * @param plh
	 */
	public void deleteOldPerson(PlayHistory plh)
	{
		//todo
	}

	/**
	 * 查询
	 *
	 * @return List<PlayHistory>
	 */
	public List<PlayHistory> query()
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> query");
		ArrayList<PlayHistory> plhList = new ArrayList<PlayHistory>(0);
		Cursor c = queryTheCursor();
		while (c.moveToNext())
		{
			PlayHistory plh = new PlayHistory();
			plh.setId(c.getInt(c.getColumnIndex("id"))); //id
			plh.setMateid(c.getString(c.getColumnIndex("mateid"))); //材料id
			plh.setUserid(c.getString(c.getColumnIndex("userid"))); //用户id
			plh.setTime(c.getInt(c.getColumnIndex("time"))); //时间
			plhList.add(plh);
		}
		c.close();
		return plhList;
	}

	/**
	 * 查询所有记录
	 *
	 * @return Cursor
	 */
	public Cursor queryTheCursor()
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> selete *");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME,
				null);
        DatabaseManager.getInstance(helper).closeDatabase();
		return c;
	}

	/**
	 * 根据信息查找播放记录
	 * @return
	 */
	public Cursor queryByMap(Map<String,Object> map)
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> queryByMap");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
		StringBuffer sql = new StringBuffer("SELECT * FROM " + DatabaseHelper.TABLE_NAME);
		if(!SysUtil.isEmpty(map)){
			sql.append(" where 1 = 1");
			for (String key : map.keySet()) {
				sql.append(" and "+key+" = '"+map.get(key)+"'");
			}
		}
		Cursor c = db.rawQuery(sql.toString(),
				null);
        DatabaseManager.getInstance(helper).closeDatabase();
		return c;
	}

	/**
	 * 关闭数据连接
	 */
	public void closeDB()
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> closeDB");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
		// 释放数据库资源
		db.close();
        DatabaseManager.getInstance(helper).closeDatabase();
	}

}
