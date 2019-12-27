package com.example.jdbc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.entity.LearnClassEntity;
import com.example.entity.SDPracticeEntity;
import com.example.util.FinalStr;
import com.example.util.SysUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ysg on 2017/8/15.
 */

public class DBManagerToSDPractice {
    private DatabaseHelper helper;

    public DBManagerToSDPractice(Context context)
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
    public void saveEntity(SDPracticeEntity sdPracticeEntity)
    {
        Log.d(FinalStr.LOG_TAG, "保存用户信息");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务

        try
        {
            if(null != sdPracticeEntity){
                Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_S_D_PRACTICE +" where id = ?",
                        new String[]{sdPracticeEntity.getId()});
                ContentValues values = new ContentValues();
                values.put("id", sdPracticeEntity.getId());
                values.put("account_id", sdPracticeEntity.getAccountId());
                values.put("account_code", sdPracticeEntity.getAccountCode());
                values.put("account_name", sdPracticeEntity.getAccountName());
                values.put("class_id", sdPracticeEntity.getClassId());
                values.put("class_code", sdPracticeEntity.getClassCode());
                values.put("behavior_id", sdPracticeEntity.getBehaviorId());
                values.put("behavior_name", sdPracticeEntity.getBehaviorName());
                values.put("question_toal_count", sdPracticeEntity.getQuestionToalCount());
                values.put("question_answer_count", sdPracticeEntity.getQuestionAnswerCount());
                values.put("sign_question_ids", sdPracticeEntity.getSignQuestionIds());
                values.put("error_question_ids", sdPracticeEntity.getErrorIds());
                values.put("correct_ids", sdPracticeEntity.getCorrectIds());
                values.put("error_ids", sdPracticeEntity.getErrorIds());
                if (!c.moveToFirst()) {
                    long a=db.insert(DatabaseHelper.TABLE_S_D_PRACTICE, null, values);
                }else {//如果数据库已存在则同步服务器
                    db.update(DatabaseHelper.TABLE_S_D_PRACTICE, values, "id = ?",
                            new String[] {sdPracticeEntity.getId()});
                }
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
     */
    public void updateUser(int id,String userName,String Psw)
    {
        Log.d(FinalStr.LOG_TAG, "修改用户信息");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("userName", userName);
        cv.put("userPsw", Psw);
        db.update(DatabaseHelper.TABLE_S_D_PRACTICE, cv, "id = ?",
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
            db.execSQL("delete from "+DatabaseHelper.TABLE_S_D_PRACTICE); //删除
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
    public List<SDPracticeEntity> queryUser()
    {
        Log.d(FinalStr.LOG_TAG, "查询全部用户信息");
        ArrayList<SDPracticeEntity> list = new ArrayList<SDPracticeEntity>(0);
        Cursor c = queryTheCursor();
        while (c.moveToNext())
        {
            SDPracticeEntity sdPracticeEntity=new SDPracticeEntity();
            sdPracticeEntity.setId(c.getString(c.getColumnIndex("id")));
            sdPracticeEntity.setQuestionToalCount(c.getInt(c.getColumnIndex("question_toal_count")));
            sdPracticeEntity.setQuestionAnswerCount(c.getInt(c.getColumnIndex("question_answer_count")));
            sdPracticeEntity.setSignQuestionIds(c.getString(c.getColumnIndex("sign_question_ids")));
            sdPracticeEntity.setErrorQuestionIdsStr(c.getString(c.getColumnIndex("error_question_ids")));
            sdPracticeEntity.setCorrectIds(c.getString(c.getColumnIndex("correct_ids")));
            sdPracticeEntity.setErrorIds(c.getString(c.getColumnIndex("error_ids")));
            list.add(sdPracticeEntity);
        }
        c.close();
        return list;
    }

    /**
     * 通过主键查询记录
     *
     * @return List<Rsa>
     */
    public LearnClassEntity queryUserById(String id)
    {
        Log.d(FinalStr.LOG_TAG, "查询全部用户信息");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        LearnClassEntity learnClassEntity=new LearnClassEntity();
        //Cursor c = queryTheCursor();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_S_D_PRACTICE +" where id=?" ,
                new String[]{id});
        if (c.moveToFirst())
        {
            learnClassEntity.setId(c.getString(c.getColumnIndex("id")));
            learnClassEntity.setTeacherName(c.getString(c.getColumnIndex("teacher_name")));
            learnClassEntity.setClassName(c.getString(c.getColumnIndex("class_name")));
            learnClassEntity.setClassCode(c.getString(c.getColumnIndex("class_code")));
            learnClassEntity.setClassStatus(c.getString(c.getColumnIndex("class_status")));
            /*learnClassEntity.setLearnStartTime(c.getString(c.getColumnIndex("learn_start_time")));
            learnClassEntity.setLearnEndTime(c.getString(c.getColumnIndex("learn_end_time")));*/
        }
        c.close();
        DatabaseManager.getInstance(helper).closeDatabase();
        return learnClassEntity;
    }

    /**
     * 根据信息查找记录
     * @return
     */
    public List<SDPracticeEntity> queryByMap(Map<String,Object> map)
    {
        Log.d(FinalStr.LOG_TAG, "DBManager --> queryByMap");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        List<SDPracticeEntity> list=new ArrayList<SDPracticeEntity>();
        StringBuffer sql = new StringBuffer("SELECT * FROM " + DatabaseHelper.TABLE_S_D_PRACTICE);
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
            SDPracticeEntity sdPracticeEntity=new SDPracticeEntity();
            sdPracticeEntity.setId(c.getString(c.getColumnIndex("id")));
            sdPracticeEntity.setQuestionToalCount(c.getInt(c.getColumnIndex("question_toal_count")));
            sdPracticeEntity.setQuestionAnswerCount(c.getInt(c.getColumnIndex("question_answer_count")));
            sdPracticeEntity.setSignQuestionIds(c.getString(c.getColumnIndex("sign_question_ids")));
            sdPracticeEntity.setErrorQuestionIdsStr(c.getString(c.getColumnIndex("error_question_ids")));
            sdPracticeEntity.setCorrectIds(c.getString(c.getColumnIndex("correct_ids")));
            sdPracticeEntity.setErrorIds(c.getString(c.getColumnIndex("error_ids")));
            list.add(sdPracticeEntity);
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
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_S_D_PRACTICE,
                null);
        DatabaseManager.getInstance(helper).closeDatabase();
        return c;
    }

    /**
     * 关闭数据连接
     */
    public void closeDB()
    {
        Log.d(FinalStr.LOG_TAG, "关闭数据库连接");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        // 释放数据库资源
        db.close();
        DatabaseManager.getInstance(helper).closeDatabase();
    }
}
