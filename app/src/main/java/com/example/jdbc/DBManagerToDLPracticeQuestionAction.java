package com.example.jdbc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.entity.DLPracticeQuestionActionEntity;
import com.example.util.FinalStr;
import com.example.util.SysUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ysg on 2017/8/21.
 */

public class DBManagerToDLPracticeQuestionAction {
    private DatabaseHelper helper;

    public DBManagerToDLPracticeQuestionAction(Context context)
    {
        Log.d(FinalStr.LOG_TAG, "DBManager --> oncreate");
        helper = new DatabaseHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        //db = helper.getWritableDatabase();
    }

    /**
     * save
     */
    public void saveEntity(DLPracticeQuestionActionEntity dlPracticeQuestionActionEntity)
    {
        Log.d(FinalStr.LOG_TAG, "保存用户信息");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务

        try
        {
            if(null != dlPracticeQuestionActionEntity){
                Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_D_L_PRACTICE_QUESTION_ACTION +" where id = ?",
                        new String[]{dlPracticeQuestionActionEntity.getId()});
                ContentValues values = new ContentValues();
                values.put("id", dlPracticeQuestionActionEntity.getId());
                values.put("question_id", dlPracticeQuestionActionEntity.getQuestionId());
                values.put("question_type", dlPracticeQuestionActionEntity.getQuestionType());
                values.put("is_marked", dlPracticeQuestionActionEntity.getIsMarked());
                values.put("account_id", dlPracticeQuestionActionEntity.getAccountId());
                values.put("behavior_id", dlPracticeQuestionActionEntity.getBehaviorId());
                values.put("class_id", dlPracticeQuestionActionEntity.getClassId());
                if (!c.moveToFirst()) {
                    db.insert(DatabaseHelper.TABLE_D_L_PRACTICE_QUESTION_ACTION, null, values);
                    db.setTransactionSuccessful(); // 设置事务成功完成
                }else {//如果数据库已存在则同步服务器
                    db.update(DatabaseHelper.TABLE_D_L_PRACTICE_QUESTION_ACTION, values, "id = ?",
                            new String[] {dlPracticeQuestionActionEntity.getId()});
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
        db.update(DatabaseHelper.TABLE_D_L_PRACTICE_QUESTION_ACTION, cv, "id = ?",
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
            db.execSQL("delete from "+DatabaseHelper.TABLE_D_L_PRACTICE_QUESTION_ACTION); //删除
            db.setTransactionSuccessful(); // 设置事务成功完成
            queryTheCursor();
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
    public List<DLPracticeQuestionActionEntity> queryUser()
    {
        Log.d(FinalStr.LOG_TAG, "查询全部用户信息");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        ArrayList<DLPracticeQuestionActionEntity> list = new ArrayList<DLPracticeQuestionActionEntity>(0);
        Cursor c = queryTheCursor();
        while (c.moveToNext())
        {
            DLPracticeQuestionActionEntity dlPracticeQuestionActionEntity = new DLPracticeQuestionActionEntity();
            dlPracticeQuestionActionEntity.setId(c.getString(c.getColumnIndex("id")));
            dlPracticeQuestionActionEntity.setQuestionId(c.getString(c.getColumnIndex("question_id")));
            dlPracticeQuestionActionEntity.setAccountId(c.getString(c.getColumnIndex("account_id")));
            dlPracticeQuestionActionEntity.setBehaviorId(c.getString(c.getColumnIndex("behavior_id")));
            dlPracticeQuestionActionEntity.setQuestionType(c.getString(c.getColumnIndex("question_type")));
            dlPracticeQuestionActionEntity.setIsMarked(c.getString(c.getColumnIndex("is_marked")));
            dlPracticeQuestionActionEntity.setClassId(c.getString(c.getColumnIndex("class_id")));
            list.add(dlPracticeQuestionActionEntity);
        }
        c.close();
        DatabaseManager.getInstance(helper).closeDatabase();
        return list;
    }

    /**
     * 通过主键查询记录
     *
     * @return List<Rsa>
     */
    public DLPracticeQuestionActionEntity queryUserById(String id)
    {
        Log.d(FinalStr.LOG_TAG, "查询全部用户信息");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        DLPracticeQuestionActionEntity dlPracticeQuestionActionEntity=new DLPracticeQuestionActionEntity();
        //Cursor c = queryTheCursor();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_D_L_PRACTICE_QUESTION_ACTION +" where id=?" ,
                new String[]{id});
        if (c.moveToFirst())
        {
            dlPracticeQuestionActionEntity.setId(c.getString(c.getColumnIndex("id")));
            dlPracticeQuestionActionEntity.setQuestionId(c.getString(c.getColumnIndex("question_id")));
            dlPracticeQuestionActionEntity.setAccountId(c.getString(c.getColumnIndex("account_id")));
            dlPracticeQuestionActionEntity.setBehaviorId(c.getString(c.getColumnIndex("behavior_id")));
            dlPracticeQuestionActionEntity.setQuestionType(c.getString(c.getColumnIndex("question_type")));
            dlPracticeQuestionActionEntity.setIsMarked(c.getString(c.getColumnIndex("is_marked")));
            dlPracticeQuestionActionEntity.setClassId(c.getString(c.getColumnIndex("class_id")));
        }
        c.close();
        DatabaseManager.getInstance(helper).closeDatabase();
        return dlPracticeQuestionActionEntity;
    }

    /**
     * 根据信息查找记录
     * @return
     */
    public List<DLPracticeQuestionActionEntity> queryByMap(Map<String,Object> map)
    {
        Log.d(FinalStr.LOG_TAG, "DBManager --> queryByMap");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        List<DLPracticeQuestionActionEntity> list=new ArrayList<DLPracticeQuestionActionEntity>();
        StringBuffer sql = new StringBuffer("SELECT * FROM " + DatabaseHelper.TABLE_D_L_PRACTICE_QUESTION_ACTION);
        if(!SysUtil.isEmpty(map)){
            sql.append(" where 1 = 1");
            for (String key : map.keySet()) {
                sql.append(" and "+key+" = '"+map.get(key)+"'");
            }
        }
        Cursor c = db.rawQuery(sql.toString(),
                null);
        if (c.moveToFirst())
        {
            DLPracticeQuestionActionEntity dlPracticeQuestionActionEntity=new DLPracticeQuestionActionEntity();
            dlPracticeQuestionActionEntity.setId(c.getString(c.getColumnIndex("id")));
            dlPracticeQuestionActionEntity.setQuestionId(c.getString(c.getColumnIndex("question_id")));
            dlPracticeQuestionActionEntity.setAccountId(c.getString(c.getColumnIndex("account_id")));
            dlPracticeQuestionActionEntity.setBehaviorId(c.getString(c.getColumnIndex("behavior_id")));
            dlPracticeQuestionActionEntity.setQuestionType(c.getString(c.getColumnIndex("question_type")));
            dlPracticeQuestionActionEntity.setIsMarked(c.getString(c.getColumnIndex("is_marked")));
            dlPracticeQuestionActionEntity.setClassId(c.getString(c.getColumnIndex("class_id")));
            list.add(dlPracticeQuestionActionEntity);
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
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_D_L_PRACTICE_QUESTION_ACTION,
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
