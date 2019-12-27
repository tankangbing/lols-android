package com.example.jdbc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.entity.DLPracticeAnswerDetailEntity;
import com.example.util.FinalStr;
import com.example.util.SysUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ysg on 2017/8/15.
 */

public class DBManagerToDLPracticeAnswerDetail {
    private DatabaseHelper helper= null;
    //private SQLiteDatabase db;

    public DBManagerToDLPracticeAnswerDetail(Context context)
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
    public void saveEntity(DLPracticeAnswerDetailEntity dlPracticeAnswerDetailEntity)
    {
        Log.d(FinalStr.LOG_TAG, "保存用户信息");
        // 采用事务处理，确保数据完整性

        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        db.beginTransaction(); // 开始事务
        try
        {
            if(null != dlPracticeAnswerDetailEntity){
                Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_D_L_PRACTICE_ANSWER_DETAIL +" where id = ?",
                        new String[]{dlPracticeAnswerDetailEntity.getId()});
                ContentValues values = new ContentValues();
                values.put("id", dlPracticeAnswerDetailEntity.getId());
                values.put("question_id", dlPracticeAnswerDetailEntity.getQuestionId());
                values.put("question_type", dlPracticeAnswerDetailEntity.getQuestionType());
                values.put("answer_status", dlPracticeAnswerDetailEntity.getAnswerStatus());
                values.put("option_id", dlPracticeAnswerDetailEntity.getOptionId());
                values.put("behavior_id", dlPracticeAnswerDetailEntity.getBehaviorId());
                values.put("batch_id", dlPracticeAnswerDetailEntity.getBatchId());
                if (!c.moveToFirst()) {
                    db.insert(DatabaseHelper.TABLE_D_L_PRACTICE_ANSWER_DETAIL, null, values);
                    db.setTransactionSuccessful(); // 设置事务成功完成
                }else {//如果数据库已存在则同步服务器
                    db.update(DatabaseHelper.TABLE_D_L_PRACTICE_ANSWER_DETAIL, values, "id = ?",
                            new String[] {dlPracticeAnswerDetailEntity.getId()});
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
     * 删除
     *
     */
    public void deleteUser()
    {
        Log.d(FinalStr.LOG_TAG, "删除表内数据");

        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try
        {
            db.execSQL("delete from "+DatabaseHelper.TABLE_D_L_PRACTICE_ANSWER_DETAIL); //删除
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
    public List<DLPracticeAnswerDetailEntity> queryUser()
    {
        Log.d(FinalStr.LOG_TAG, "查询全部用户信息");
        ArrayList<DLPracticeAnswerDetailEntity> list = new ArrayList<DLPracticeAnswerDetailEntity>(0);
        Cursor c = queryTheCursor();
        while (c.moveToNext())
        {
            DLPracticeAnswerDetailEntity dlPracticeAnswerDetailEntity = new DLPracticeAnswerDetailEntity();
            dlPracticeAnswerDetailEntity.setId(c.getString(c.getColumnIndex("id")));
            dlPracticeAnswerDetailEntity.setQuestionId(c.getString(c.getColumnIndex("question_id")));
            dlPracticeAnswerDetailEntity.setOptionId(c.getString(c.getColumnIndex("option_id")));
            dlPracticeAnswerDetailEntity.setBatchId(c.getString(c.getColumnIndex("batch_id")));
            dlPracticeAnswerDetailEntity.setBehaviorId(c.getString(c.getColumnIndex("behavior_id")));
            dlPracticeAnswerDetailEntity.setQuestionType(c.getString(c.getColumnIndex("question_type")));
            dlPracticeAnswerDetailEntity.setAnswerStatus(c.getString(c.getColumnIndex("answer_status")));
            list.add(dlPracticeAnswerDetailEntity);
        }
        c.close();
        return list;
    }

    /**
     * 通过主键查询记录
     *
     * @return List<Rsa>
     */
    public DLPracticeAnswerDetailEntity queryUserById(String id)
    {
        Log.d(FinalStr.LOG_TAG, "查询全部用户信息");
        DLPracticeAnswerDetailEntity dlPracticeAnswerDetailEntity=new DLPracticeAnswerDetailEntity();
        //Cursor c = queryTheCursor();
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_D_L_PRACTICE_ANSWER_DETAIL +" where id=?" ,
                new String[]{id});
        if (c.moveToFirst())
        {
            dlPracticeAnswerDetailEntity.setId(c.getString(c.getColumnIndex("id")));
            dlPracticeAnswerDetailEntity.setQuestionId(c.getString(c.getColumnIndex("question_id")));
            dlPracticeAnswerDetailEntity.setOptionId(c.getString(c.getColumnIndex("option_id")));
            dlPracticeAnswerDetailEntity.setBehaviorId(c.getString(c.getColumnIndex("behavior_id")));
            dlPracticeAnswerDetailEntity.setBatchId(c.getString(c.getColumnIndex("batch_id")));
            dlPracticeAnswerDetailEntity.setQuestionType(c.getString(c.getColumnIndex("question_type")));
            dlPracticeAnswerDetailEntity.setAnswerStatus(c.getString(c.getColumnIndex("answer_status")));
        }
        c.close();
        DatabaseManager.getInstance(helper).closeDatabase();
        return dlPracticeAnswerDetailEntity;
    }

    /**
     * 根据信息查找记录
     * @return
     */
    public List<DLPracticeAnswerDetailEntity> queryByMap(Map<String,Object> map)
    {
        Log.d(FinalStr.LOG_TAG, "DBManager --> queryByMap");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        List<DLPracticeAnswerDetailEntity> list=new ArrayList<DLPracticeAnswerDetailEntity>();
        StringBuffer sql = new StringBuffer("SELECT * FROM " + DatabaseHelper.TABLE_D_L_PRACTICE_ANSWER_DETAIL);
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
            DLPracticeAnswerDetailEntity dlPracticeAnswerDetailEntity=new DLPracticeAnswerDetailEntity();
            dlPracticeAnswerDetailEntity.setId(c.getString(c.getColumnIndex("id")));
            dlPracticeAnswerDetailEntity.setQuestionId(c.getString(c.getColumnIndex("question_id")));
            dlPracticeAnswerDetailEntity.setOptionId(c.getString(c.getColumnIndex("option_id")));
            dlPracticeAnswerDetailEntity.setBehaviorId(c.getString(c.getColumnIndex("behavior_id")));
            dlPracticeAnswerDetailEntity.setBatchId(c.getString(c.getColumnIndex("batch_id")));
            dlPracticeAnswerDetailEntity.setQuestionType(c.getString(c.getColumnIndex("question_type")));
            dlPracticeAnswerDetailEntity.setAnswerStatus(c.getString(c.getColumnIndex("answer_status")));
            list.add(dlPracticeAnswerDetailEntity);
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
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_D_L_PRACTICE_ANSWER_DETAIL,
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
