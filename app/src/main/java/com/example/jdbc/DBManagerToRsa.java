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
import com.example.entity.Rsa;
import com.example.util.FinalStr;
import com.example.util.SysUtil;


/**
 * Rsa加密算法表操作
 * @author Amao
 *
 */
public class DBManagerToRsa {
	private DatabaseHelper helper;
	private SQLiteDatabase db;

	public DBManagerToRsa(Context context)
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
	public void saveRsa(Rsa rsa)
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> saveRsa");
		// 采用事务处理，确保数据完整性
		db.beginTransaction(); // 开始事务
		try
		{
			if(null != rsa){
				db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME_RSA
						+ " VALUES(?,?,?,?,?)", new Object[] {null,rsa.getAppRsaModulus(),rsa.getSysRsaModulus(),
						rsa.getSysRsaPrivateExponent(),rsa.getAppRsaPublicExponent()}); //保持这个表只有一条记录
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
	public void updateRsa(int id,String appRsaModulus,String sysRsaModulus,String sysRsaPrivateExponent,String appRsaPublicExponent)
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> updateRsa");
		ContentValues cv = new ContentValues();
		cv.put("appRsaModulus", appRsaModulus);
		cv.put("sysRsaModulus", sysRsaModulus);
		cv.put("sysRsaPrivateExponent", sysRsaPrivateExponent);
		cv.put("appRsaPublicExponent", appRsaPublicExponent);
		db.update(DatabaseHelper.TABLE_NAME_RSA, cv, "id = ?",
				new String[] {id+""});
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
	 * 查询全部
	 *
	 * @return List<Rsa>
	 */
	public List<Rsa> queryRsa()
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> query Rsa*");
		ArrayList<Rsa> rsaList = new ArrayList<Rsa>(0);
		Cursor c = queryTheCursor();
		while (c.moveToNext())
		{
			Rsa rsa = new Rsa();
			rsa.setId(c.getInt(c.getColumnIndex("id")));
			rsa.setAppRsaModulus(c.getString(c.getColumnIndex("appRsaModulus")));
			rsa.setSysRsaModulus(c.getString(c.getColumnIndex("sysRsaModulus")));
			rsa.setSysRsaPrivateExponent(c.getString(c.getColumnIndex("sysRsaPrivateExponent")));
			rsa.setAppRsaPublicExponent(c.getString(c.getColumnIndex("appRsaPublicExponent")));
			rsaList.add(rsa);
		}
		c.close();
		return rsaList;
	}

	/**
	 * 查询所有记录
	 *
	 * @return Cursor
	 */
	public Cursor queryTheCursor()
	{
		Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_RSA,
				null);
		return c;
	}

	/**
	 * 根据信息查找
	 * @return
	 */
	public Cursor queryByMap(Map<String,Object> map)
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> queryByMap Rsa");
		StringBuffer sql = new StringBuffer("SELECT * FROM " + DatabaseHelper.TABLE_NAME_RSA);
		if(!SysUtil.isEmpty(map)){
			sql.append(" where 1 = 1");
			for (String key : map.keySet()) {
				sql.append(" and "+key+" = '"+map.get(key)+"'");
			}
		}
		Cursor c = db.rawQuery(sql.toString(),
				null);
		return c;
	}

	/**
	 * 关闭数据连接
	 */
	public void closeDB()
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> closeDB");
		// 释放数据库资源
		db.close();
	}

}
