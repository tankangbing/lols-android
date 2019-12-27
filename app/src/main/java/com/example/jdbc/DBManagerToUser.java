package com.example.jdbc;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.entity.User;
import com.example.util.FinalStr;


/**
 * 用户表
 * @author Amao
 *
 */
public class DBManagerToUser {
	private DatabaseHelper helper;
	private SQLiteDatabase db;

	public DBManagerToUser(Context context)
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> oncreate");
		helper = new DatabaseHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}

	/**
	 * save
	 */
	public void saveUser(User user)
	{
		Log.d(FinalStr.LOG_TAG, "保存用户信息");
		// 采用事务处理，确保数据完整性
		db.beginTransaction(); // 开始事务
		try
		{
			if(null != user){
                deleteUser();
				db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME_USER
						+ " VALUES(?,?,?)", new Object[] {"1",user.getUserName(),user.getUserPsw()}); //保持这个表只有一条记录
				db.setTransactionSuccessful(); // 设置事务成功完成
			}
		}
		finally
		{
			db.endTransaction(); // 结束事务
		}
	}

	/**
	 * 修改
	 */
	public void updateUser(int id,String userName,String Psw)
	{
		Log.d(FinalStr.LOG_TAG, "修改用户信息");
		ContentValues cv = new ContentValues();
		cv.put("userName", userName);
		cv.put("userPsw", Psw);
		db.update(DatabaseHelper.TABLE_NAME_USER, cv, "id = ?",
				new String[] {id+""});
	}

	/**
	 * 删除
	 *
	 * @param
	 */
	public void deleteUser()
	{
		Log.d(FinalStr.LOG_TAG, "删除用户信息");
		// 采用事务处理，确保数据完整性
		db.beginTransaction(); // 开始事务
		try
		{
			db.execSQL("delete from "+DatabaseHelper.TABLE_NAME_USER); //删除
			db.setTransactionSuccessful(); // 设置事务成功完成
		}
		finally
		{
			db.endTransaction(); // 结束事务
		}
	}

	/**
	 * 查询全部
	 *
	 * @return List<Rsa>
	 */
	public List<User> queryUser()
	{
		Log.d(FinalStr.LOG_TAG, "查询全部用户信息");
		ArrayList<User> userList = new ArrayList<User>(0);
		Cursor c = queryTheCursor();
		while (c.moveToNext())
		{
			User user = new User();
			user.setId(c.getInt(c.getColumnIndex("id")));
			user.setUserName(c.getString(c.getColumnIndex("userName")));
			user.setUserPsw(c.getString(c.getColumnIndex("userPsw")));
			userList.add(user);
		}
		c.close();
		return userList;
	}

	/**
	 * 查询所有记录
	 *
	 * @return Cursor
	 */
	public Cursor queryTheCursor()
	{
		Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_USER,
				null);
		return c;
	}

	/**
	 * 关闭数据连接
	 */
	public void closeDB()
	{
		Log.d(FinalStr.LOG_TAG, "关闭数据库连接");
		// 释放数据库资源
		db.close();
	}

}
