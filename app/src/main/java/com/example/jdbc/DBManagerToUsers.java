package com.example.jdbc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.entity.LearnAccountEntity;
import com.example.entity.User;
import com.example.util.FinalStr;
import com.example.util.SysUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBManagerToUsers {
	private DatabaseHelper helper;

	public DBManagerToUsers(Context context)
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> oncreate");
		helper = new DatabaseHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
	}

	/**
	 * save
	 */
	public void saveUser(User user)
	{
		Log.d(FinalStr.LOG_TAG, "保存用户信息");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
		// 采用事务处理，确保数据完整性
		db.beginTransaction(); // 开始事务

		try
		{
			if(null != user){
				Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_LEARN_ACCOUNT +" where id = ?",
						new String[]{user.getUserId()});
				if (!c.moveToFirst()) {
	        			/*ContentValues cv = new ContentValues();
	        	        cv.put("userName", user.getUserName());
	        	        cv.put("userPsw", user.getUserPsw());
	        	        cv.put("classId", user.getClassId());
	        	        cv.put("userId", user.getUserId());
	        	        db.update(DatabaseHelper.TABLE_NAME_USERS, cv, " id = ?",
	        	                new String[] {user.getUserId()});
					}else {*/
					db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_LEARN_ACCOUNT
							+ " VALUES(?,?,?,?,?,?)", new Object[] {user.getUserId(),user.getUserId(),user.getUserName(),user.getUserPsw(),user.getClassIds(),user.getUserXm()});
					db.setTransactionSuccessful(); // 设置事务成功完成
				}else {//如果数据库已存在则同步服务器
					ContentValues cv = new ContentValues();
					cv.put("userName", user.getUserName());
					cv.put("userPsw", user.getUserPsw());
					cv.put("userId", user.getUserId());
					cv.put("classId", user.getClassIds());
					cv.put("userXm", user.getUserXm());
					db.update(DatabaseHelper.TABLE_LEARN_ACCOUNT, cv, "id = ?",
							new String[] {user.getUserId()});
				}
				c.close();
			}
		}
		finally
		{
			db.endTransaction(); // 结束事务

			DatabaseManager.getInstance(helper).closeDatabase();
		}
	}

    /**
     * save
     */
    public void saveUser(LearnAccountEntity learnAccountEntity)
    {
        Log.d(FinalStr.LOG_TAG, "保存用户信息");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务

        try
        {
            if(null != learnAccountEntity){
                Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_LEARN_ACCOUNT +" where id = ?",
                        new String[]{learnAccountEntity.getId()});
                if (!c.moveToFirst()) {
	        			/*ContentValues cv = new ContentValues();
	        	        cv.put("userName", user.getUserName());
	        	        cv.put("userPsw", user.getUserPsw());
	        	        cv.put("classId", user.getClassId());
	        	        cv.put("userId", user.getUserId());
	        	        db.update(DatabaseHelper.TABLE_NAME_USERS, cv, " id = ?",
	        	                new String[] {user.getUserId()});
					}else {*/
                    db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_LEARN_ACCOUNT
                            + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[] {learnAccountEntity.getId(),learnAccountEntity.getAccountType(),learnAccountEntity.getStudentPassword(),
                            learnAccountEntity.getStudentCode(),learnAccountEntity.getStudentName(),learnAccountEntity.getPhoneNumber(),learnAccountEntity.getCertificateType(),learnAccountEntity.getCertificateNumber(),
                            learnAccountEntity.getEmail(),learnAccountEntity.getSystemCode(),learnAccountEntity.getCreateDate(),learnAccountEntity.getModifyDate(),learnAccountEntity.getDelFlag()});
                    db.setTransactionSuccessful(); // 设置事务成功完成
                }else {//如果数据库已存在则同步服务器
                    ContentValues cv = new ContentValues();
                    cv.put("account_type",learnAccountEntity.getAccountType());
                    cv.put("student_password", learnAccountEntity.getStudentPassword());
                    cv.put("student_code", learnAccountEntity.getStudentCode());
                    cv.put("student_name", learnAccountEntity.getStudentName());
                    cv.put("phone_number", learnAccountEntity.getPhoneNumber());
                    cv.put("certificate_type",learnAccountEntity.getCertificateType());
                    cv.put("certificate_number", learnAccountEntity.getCertificateNumber());
                    cv.put("email", learnAccountEntity.getEmail());
                    cv.put("student_name", learnAccountEntity.getStudentName());
                    db.update(DatabaseHelper.TABLE_LEARN_ACCOUNT, cv, "id = ?",
                            new String[] {learnAccountEntity.getId()});
                }
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
	 */
	public void updateUser(int id,String userName,String Psw)
	{
		Log.d(FinalStr.LOG_TAG, "修改用户信息");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("userName", userName);
		cv.put("userPsw", Psw);
		db.update(DatabaseHelper.TABLE_LEARN_ACCOUNT, cv, "id = ?",
				new String[] {id+""});
        DatabaseManager.getInstance(helper).closeDatabase();
	}

	/**
	 * 删除
	 *
	 */
	public void deleteUser()
	{
		Log.d(FinalStr.LOG_TAG, "删除用户信息");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
		// 采用事务处理，确保数据完整性
		db.beginTransaction(); // 开始事务
		try
		{
			db.execSQL("delete from "+DatabaseHelper.TABLE_LEARN_ACCOUNT); //删除
			db.setTransactionSuccessful(); // 设置事务成功完成
		}
		finally
		{
			db.endTransaction(); // 结束事务
            DatabaseManager.getInstance(helper).closeDatabase();
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
	 * 通过主键查询记录
	 *
	 * @return List<Rsa>
	 */
	public User queryUserById(String id)
	{
		Log.d(FinalStr.LOG_TAG, "查询全部用户信息");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
		User user = new User();

		//Cursor c = queryTheCursor();
		Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_LEARN_ACCOUNT +" where id=?" ,
				new String[]{id});
		if (c.moveToFirst())
		{
			user.setUserId(c.getString(c.getColumnIndex("userId")));
			user.setUserName(c.getString(c.getColumnIndex("userName")));
			user.setUserPsw(c.getString(c.getColumnIndex("userPsw")));
			user.setClassIds(c.getString(c.getColumnIndex("classId")));
			user.setUserXm(c.getString(c.getColumnIndex("userXm")));
		}
		c.close();
        DatabaseManager.getInstance(helper).closeDatabase();
		return user;
	}

	/**
	 * 根据信息查找用户记录
	 * @return
	 */
	public LearnAccountEntity queryByMap(Map<String,Object> map)
	{
		Log.d(FinalStr.LOG_TAG, "DBManager --> queryByMap");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
		StringBuffer sql = new StringBuffer("SELECT * FROM " + DatabaseHelper.TABLE_LEARN_ACCOUNT);
		if(!SysUtil.isEmpty(map)){
			sql.append(" where 1 = 1");
			for (String key : map.keySet()) {
				sql.append(" and "+key+" = '"+map.get(key)+"'");
			}
		}
		Cursor c = db.rawQuery(sql.toString(),
				null);
        LearnAccountEntity entity=null;
        if (c.moveToFirst())
        {
            entity=new LearnAccountEntity();
            entity.setId(c.getString(c.getColumnIndex("id")));
            entity.setStudentName(c.getString(c.getColumnIndex("student_name")));
        }
        DatabaseManager.getInstance(helper).closeDatabase();
		return entity;
	}


	/**
	 * 查询所有记录
	 *
	 * @return Cursor
	 */
	public Cursor queryTheCursor()
	{
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_LEARN_ACCOUNT,
				null);
        DatabaseManager.getInstance(helper).closeDatabase();
		return c;
	}

	/**
	 * 关闭数据连接
	 */
	public void closeDB()
	{
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
		Log.d(FinalStr.LOG_TAG, "关闭数据库连接");
		// 释放数据库资源
		db.close();
        DatabaseManager.getInstance(helper).closeDatabase();
	}
}
