package com.example.jdbc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.example.entity.LearnClassEntity;
import com.example.entity.LearnClassStudentEntity;
import com.example.entity.User;

import com.example.util.FinalStr;
import com.example.util.SysUtil;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ysg on 2017/8/14.
 */

public class DBManagerToLearnClass {
    private DatabaseHelper helper;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public DBManagerToLearnClass(Context context)
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
    public void saveEntity(LearnClassEntity learnClassEntity)
    {
        Log.d(FinalStr.LOG_TAG, "保存用户信息");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务

        try
        {
            if(null != learnClassEntity){
                Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_LEARN_CLASS +" where id = ?",
                        new String[]{learnClassEntity.getId()});

                ContentValues values = new ContentValues();
                values.put("id", learnClassEntity.getId());
                values.put("class_code", learnClassEntity.getClassCode());
                values.put("class_name", learnClassEntity.getClassName());
                values.put("class_status", learnClassEntity.getClassStatus());
                values.put("system_code", learnClassEntity.getSystemCode());
                values.put("system_name", learnClassEntity.getSystemName());
                String start=sdf.format(learnClassEntity.getLearnStartTime());
                values.put("learn_start_time", start);
                String end=sdf.format(learnClassEntity.getLearnEndTime());
                values.put("learn_end_time", end);
                values.put("appointment_start_time", learnClassEntity.getAppointmentStartTime()+"");
                values.put("appointment_end_time", learnClassEntity.getLearnEndTime()+"");
                values.put("assessment_index_type", learnClassEntity.getAssessmentIndexType());
                values.put("assessment_index", learnClassEntity.getAssessmentIndex());
                values.put("course_id", learnClassEntity.getCourseId());
                values.put("courseware_id", learnClassEntity.getCoursewareId());
                values.put("teacher_id", learnClassEntity.getTeacherId());
                values.put("teacher_name", learnClassEntity.getTeacherName());
                values.put("teacher_school_name", learnClassEntity.getTeacherSchoolName());
                values.put("img_path", learnClassEntity.getClassPhotoPath());
                if (!c.moveToFirst()) {
                    db.insert(DatabaseHelper.TABLE_LEARN_CLASS, null, values);
                    db.setTransactionSuccessful(); // 设置事务成功完成
                }else {//如果数据库已存在则同步服务器
                    db.update(DatabaseHelper.TABLE_LEARN_CLASS, values, "id = ?",
                            new String[] {learnClassEntity.getId()});
                    db.setTransactionSuccessful(); // 设置事务成功完成
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
            db.execSQL("delete from "+DatabaseHelper.TABLE_LEARN_CLASS); //删除
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
    public List<LearnClassEntity> queryUser() throws ParseException {
        Log.d(FinalStr.LOG_TAG, "查询全部课程班信息");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        ArrayList<LearnClassEntity> userList = new ArrayList<LearnClassEntity>(0);
        Cursor c = queryTheCursor();
        while (c.moveToNext())
        {
            LearnClassEntity entity = new LearnClassEntity();
            entity.setId(c.getString(c.getColumnIndex("id")));
            entity.setTeacherName(c.getString(c.getColumnIndex("teacher_name")));
            entity.setClassName(c.getString(c.getColumnIndex("class_name")));
            entity.setClassCode(c.getString(c.getColumnIndex("class_code")));
            entity.setClassStatus(c.getString(c.getColumnIndex("class_status")));
            entity.setCoursewareId(c.getString(c.getColumnIndex("courseware_id")));
            entity.setClassPhotoPath(c.getString(c.getColumnIndex("img_path")));
            String startTime =c.getString(c.getColumnIndex("learn_start_time"));
            Date date = sdf.parse(startTime);
            entity.setLearnStartTime(date);
            String endTime =c.getString(c.getColumnIndex("learn_end_time"));
            Date date2 = sdf.parse(endTime);
            entity.setLearnEndTime(date2);
            userList.add(entity);
        }
        c.close();
        DatabaseManager.getInstance(helper).closeDatabase();
        return userList;
    }

    /**
     * 通过主键查询记录
     *
     * @return List<Rsa>
     */
    public LearnClassEntity queryUserById(String id) throws ParseException {
        Log.d(FinalStr.LOG_TAG, "查询全部用户信息");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        LearnClassEntity learnClassEntity=new LearnClassEntity();
        //Cursor c = queryTheCursor();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_LEARN_CLASS +" where id=?" ,
                new String[]{id});
        if (c.moveToFirst())
        {
            learnClassEntity.setId(c.getString(c.getColumnIndex("id")));
            learnClassEntity.setTeacherName(c.getString(c.getColumnIndex("teacher_name")));
            learnClassEntity.setClassName(c.getString(c.getColumnIndex("class_name")));
            learnClassEntity.setClassCode(c.getString(c.getColumnIndex("class_code")));
            learnClassEntity.setClassStatus(c.getString(c.getColumnIndex("class_status")));
            learnClassEntity.setCoursewareId(c.getString(c.getColumnIndex("courseware_id")));
            learnClassEntity.setClassPhotoPath(c.getString(c.getColumnIndex("img_path")));
            String startTime =c.getString(c.getColumnIndex("learn_start_time"));
            Date date = sdf.parse(startTime);
            learnClassEntity.setLearnStartTime(date);
            String endTime =c.getString(c.getColumnIndex("learn_end_time"));
            Date date2 = sdf.parse(endTime);
            learnClassEntity.setLearnEndTime(date2);
        }
        c.close();
        DatabaseManager.getInstance(helper).closeDatabase();
        return learnClassEntity;
    }

    /**
     * 根据信息查找记录
     * @return
     */
    public List<LearnClassEntity> queryByMap(Map<String,Object> map) throws ParseException {
        Log.d(FinalStr.LOG_TAG, "根据信息查找课程班记录");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        List<LearnClassEntity> list=new ArrayList<LearnClassEntity>();
        StringBuffer sql = new StringBuffer("SELECT * FROM " + DatabaseHelper.TABLE_LEARN_CLASS);
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
            LearnClassEntity entity=new LearnClassEntity();
            entity.setId(c.getString(c.getColumnIndex("id")));
            entity.setTeacherName(c.getString(c.getColumnIndex("teacher_name")));
            entity.setClassName(c.getString(c.getColumnIndex("class_name")));
            entity.setClassCode(c.getString(c.getColumnIndex("class_code")));
            entity.setClassStatus(c.getString(c.getColumnIndex("class_status")));
            entity.setCoursewareId(c.getString(c.getColumnIndex("courseware_id")));
            entity.setClassPhotoPath(c.getString(c.getColumnIndex("img_path")));
            String startTime =c.getString(c.getColumnIndex("learn_start_time"));
            Date date = sdf.parse(startTime);
            entity.setLearnStartTime(date);
            String endTime =c.getString(c.getColumnIndex("learn_end_time"));
            Date date2 = sdf.parse(endTime);
            entity.setLearnEndTime(date2);
            list.add(entity);
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
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_LEARN_CLASS,
                null);
        DatabaseManager.getInstance(helper).closeDatabase();
        return c;
    }

    /**
     * 通过classid查找课程
     * @return
     */
    public List<LearnClassEntity> queryClassByClassId(List<String> classIds)throws ParseException{
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        List<LearnClassEntity> learnClassEntities =new ArrayList<>();
        List<String> parameters = new ArrayList<>();
        StringBuilder sbStr = new StringBuilder();
        for (int i=0; i<classIds.size(); i++) {
            sbStr.append("'")
                    .append(classIds.get(i))
                    .append("'")
                    .append(",");
            parameters.add("?");
        }
        Cursor cursor = db.query(DatabaseHelper.TABLE_LEARN_CLASS, null, " id in (" + TextUtils.join(",", parameters) + ")", classIds.toArray(new String[classIds.size()]), null,null,null);

        while (cursor.moveToNext()) {
            LearnClassEntity learnClassEntity =new LearnClassEntity();
            learnClassEntity.setId(cursor.getString(cursor.getColumnIndex("id")));
            learnClassEntity.setTeacherName(cursor.getString(cursor.getColumnIndex("teacher_name")));
            learnClassEntity.setClassName(cursor.getString(cursor.getColumnIndex("class_name")));
            learnClassEntity.setClassCode(cursor.getString(cursor.getColumnIndex("class_code")));
            learnClassEntity.setClassStatus(cursor.getString(cursor.getColumnIndex("class_status")));
            learnClassEntity.setCoursewareId(cursor.getString(cursor.getColumnIndex("courseware_id")));
            learnClassEntity.setClassPhotoPath(cursor.getString(cursor.getColumnIndex("img_path")));
            String startTime =cursor.getString(cursor.getColumnIndex("learn_start_time"));
            Date date = sdf.parse(startTime);
            learnClassEntity.setLearnStartTime(date);
            String endTime =cursor.getString(cursor.getColumnIndex("learn_end_time"));
            Date date2 = sdf.parse(startTime);
            learnClassEntity.setLearnEndTime(date2);
            learnClassEntities.add(learnClassEntity);
        }
        cursor.close();
        DatabaseManager.getInstance(helper).closeDatabase();
        return learnClassEntities;
    }

    /**
     * 关闭数据连接
     */
    public void closeDB()
    {
        Log.d(FinalStr.LOG_TAG, "关闭数据库连接");
        SQLiteDatabase db = DatabaseManager.getInstance(helper).getReadableDatabase();
        // 释放数据库资源
        DatabaseManager.getInstance(helper).closeDatabase();
        db.close();
    }
}
