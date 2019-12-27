package com.example.jdbc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.entity.LearnClassStudentEntity;
import com.example.entity.User;

import com.example.util.FinalStr;
import com.example.util.SysUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ysg on 2017/8/14.
 */

public class DBManagerToLearnClassStudent {
    private DatabaseHelper helper;

    public DBManagerToLearnClassStudent(Context context)
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
    public void saveEntity(LearnClassStudentEntity learnClassStudentEntity)
    {
        Log.d(FinalStr.LOG_TAG, "保存用户信息");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务

        try
        {
            if(null != learnClassStudentEntity){
                Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_LEARN_CLASS_STUDENT +" where id = ?",
                        new String[]{learnClassStudentEntity.getId()});
                ContentValues values = new ContentValues();
                values.put("id", learnClassStudentEntity.getId());
                values.put("class_id", learnClassStudentEntity.getClassId());
                values.put("student_id", learnClassStudentEntity.getStudentId());
                //values.put("learn_start_time", learnClassStudentEntity.getLearnStartTime()+"");
                //values.put("learn_end_time", learnClassStudentEntity.getLearnEndTime()+"");
                if (!c.moveToFirst()) {
                    long a=db.insert(DatabaseHelper.TABLE_LEARN_CLASS_STUDENT, null, values);
                    db.setTransactionSuccessful(); // 设置事务成功完成
                }else {//如果数据库已存在则同步服务器
                    db.update(DatabaseHelper.TABLE_LEARN_CLASS_STUDENT, values, "id = ?",
                            new String[] {learnClassStudentEntity.getId()});
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
        db.update(DatabaseHelper.TABLE_LEARN_CLASS_STUDENT, cv, "id = ?",
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
            db.execSQL("delete from "+DatabaseHelper.TABLE_LEARN_CLASS_STUDENT); //删除
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
    public List<LearnClassStudentEntity> queryUser()
    {
        Log.d(FinalStr.LOG_TAG, "查询全部用户信息");
        ArrayList<LearnClassStudentEntity> list = new ArrayList<LearnClassStudentEntity>(0);
        Cursor c = queryTheCursor();
        while (c.moveToNext())
        {
            LearnClassStudentEntity learnClassStudentEntity=new LearnClassStudentEntity();
            learnClassStudentEntity.setId(c.getString(c.getColumnIndex("id")));
            learnClassStudentEntity.setClassId(c.getString(c.getColumnIndex("class_id")));
            learnClassStudentEntity.setStudentName(c.getString(c.getColumnIndex("student_id")));
            list.add(learnClassStudentEntity);
        }
        c.close();
        return list;
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
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_LEARN_CLASS_STUDENT +" where id=?" ,
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
     * 根据信息查找记录
     * @return
     */
    public List<LearnClassStudentEntity> queryByMap(Map<String,Object> map)
    {
        Log.d(FinalStr.LOG_TAG, "DBManager --> queryByMap");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        List<LearnClassStudentEntity> list=new ArrayList<LearnClassStudentEntity>();
        StringBuffer sql = new StringBuffer("SELECT * FROM " + DatabaseHelper.TABLE_LEARN_CLASS_STUDENT);
        if(!SysUtil.isEmpty(map)){
            sql.append(" where 1 = 1");
            for (String key : map.keySet()) {
                sql.append(" and "+key+" = '"+map.get(key)+"'");
            }
        }
        Cursor c = db.rawQuery(sql.toString(),
                null);
        Log.e("count",c.getCount()+"");
        while (c.moveToNext())
        {
            LearnClassStudentEntity learnClassStudentEntity=new LearnClassStudentEntity();
            learnClassStudentEntity.setId(c.getString(c.getColumnIndex("id")));
            learnClassStudentEntity.setClassId(c.getString(c.getColumnIndex("class_id")));
            learnClassStudentEntity.setStudentName(c.getString(c.getColumnIndex("student_id")));
            list.add(learnClassStudentEntity);
        }
        DatabaseManager.getInstance(helper).closeDatabase();
        return list;
    }


    /**
     * 查询所有记录
     *
     * @return Cursor
     */
    public Cursor queryTheCursor()
    {
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_LEARN_CLASS_STUDENT,
                null);
        Log.d(FinalStr.LOG_TAG, c.getCount()+"");

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
