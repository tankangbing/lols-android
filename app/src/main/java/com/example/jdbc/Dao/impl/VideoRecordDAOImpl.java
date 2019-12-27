package com.example.jdbc.Dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.entity.VideoRecordModel;
import com.example.jdbc.Dao.VideoRecordDAO;
import com.example.jdbc.DatabaseHelper;
import com.example.jdbc.DatabaseManager;
import com.example.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TJR on 2017/8/10.
 */
public class VideoRecordDAOImpl implements VideoRecordDAO{
    private DatabaseHelper dbHelper = null;
    private String table_detail =dbHelper.TABLE_VIDEO_DETAIL;
    private String table_total =dbHelper.TABLE_VIDEO_TOTAL;
    public VideoRecordDAOImpl(Context context) {
        super();
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * 插入学习明细
     * @param videoRecordModel
     * @return
     */
    public int insertDetailRecord(VideoRecordModel videoRecordModel) {
        long endTimeMillis =System.currentTimeMillis();//当前时间毫秒;
        long startTimeMillis =endTimeMillis - (videoRecordModel.getEndPoint() -videoRecordModel.getStartPoint())*1000;//减去时间差获取开始时间
      /*  String endTime =DateUtil.getCurrentTime(new Date(endTimeMillis)
                ,DateUtil.GetSimpleDateFormat(DateUtil.FMTSTR_2));
        String startTime =DateUtil.getCurrentTime(new Date(startTimeMillis)
                ,DateUtil.GetSimpleDateFormat(DateUtil.FMTSTR_2));*/


        SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("account_id", videoRecordModel.getAccountId());
        values.put("account_name", videoRecordModel.getAccountName());
        values.put("class_id", videoRecordModel.getClassId());
        values.put("behavior_id", videoRecordModel.getBehaviorId());
        values.put("total_time", videoRecordModel.getTotalTime());
        values.put("start_point", videoRecordModel.getStartPoint());
        values.put("end_point", videoRecordModel.getEndPoint());
        values.put("start_time", startTimeMillis);
        values.put("end_time",endTimeMillis);
        values.put("is_summary","1");
        values.put("data_source","1");

        int i =(int)db.insert(table_detail, null, values);

        DatabaseManager.getInstance(dbHelper).closeDatabase();

        return i;
    }

    /**
     * 插入学习总汇
     * @param videoRecordModel
     * @return
     */
    public int insertTotalRecord(VideoRecordModel videoRecordModel) {

        //当table_total 表不为空时才执行删除
        int size = queryTotalRecordSingle(videoRecordModel.getClassId(),videoRecordModel.getBehaviorId(),videoRecordModel.getAccountId()).size();
        if (size >0)
            deleteTotalRepeatRecord(videoRecordModel.getStartPoint(),videoRecordModel.getEndPoint());

        SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("account_id", videoRecordModel.getAccountId());
        values.put("account_name", videoRecordModel.getAccountName());
        values.put("class_id", videoRecordModel.getClassId());
        values.put("behavior_id", videoRecordModel.getBehaviorId());
        values.put("total_time", videoRecordModel.getTotalTime());
        values.put("start_point", videoRecordModel.getStartPoint());
        values.put("end_point", videoRecordModel.getEndPoint());

        int i =(int)db.insert(table_total, null, values);

        DatabaseManager.getInstance(dbHelper).closeDatabase();

        return i;

    }

    /**
     * 搜索学习记录
     * @param table
     * @param selection
     * @param selectionArgs
     * @param orderBy
     * @return
     */
    public List<VideoRecordModel> queryRecord(String table, String selection, String[] selectionArgs, String orderBy) {
        SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
        List<VideoRecordModel> list = new ArrayList<VideoRecordModel>();

        Cursor cursor = db.query(table, null, selection, selectionArgs, null, null, orderBy);
        while (cursor.moveToNext()) {
            VideoRecordModel videoRecord = new VideoRecordModel();
            videoRecord.setRecord_id(cursor.getInt(cursor.getColumnIndex("record_id")));
            videoRecord.setAccountId(cursor.getString(cursor.getColumnIndex("account_id")));
            videoRecord.setAccountName(cursor.getString(cursor.getColumnIndex("account_name")));
            videoRecord.setClassId(cursor.getString(cursor.getColumnIndex("class_id")));
            videoRecord.setBehaviorId(cursor.getString(cursor.getColumnIndex("behavior_id")));
            videoRecord.setTotalTime(cursor.getInt(cursor.getColumnIndex("total_time")));
            videoRecord.setStartPoint(cursor.getInt(cursor.getColumnIndex("start_point")));
            videoRecord.setEndPoint(cursor.getInt(cursor.getColumnIndex("end_point")));
            videoRecord.setStartTime(cursor.getLong(cursor.getColumnIndex("start_time")));
            videoRecord.setEndTime(cursor.getLong(cursor.getColumnIndex("end_time")));
            if (table.equals(table_detail)) {
                videoRecord.setIsSummary(cursor.getString(cursor.getColumnIndex("is_summary")));
                videoRecord.setDataSource(cursor.getString(cursor.getColumnIndex("data_source")));
            }
            list.add(videoRecord);
        }
        cursor.close();
        DatabaseManager.getInstance(dbHelper).closeDatabase();
        return list;
    }

    /**
     * 查询一个学习行为的明细记录
     * @param classId
     * @param behaviorId
     * @param accountId
     * @return
     */
    public List<VideoRecordModel> queryDetailRecordSingle(String classId, String behaviorId, String accountId) {

        return queryRecord(table_detail,"class_id = ? and behavior_id = ? and account_id = ?",new String[] { classId,behaviorId, accountId},null);

    }

    /**
     * 查询所有学习行为的明细记录
     * @param classId
     * @param accountId
     * @return
     */
    public List<VideoRecordModel> queryDetailRecordAll(String classId, String accountId) {

        return queryRecord(table_detail,"class_id = ? and account_id = ?",new String[] { classId, accountId},null);

    }

    /**
     * 判断该点是否重复再某一段学习记录里面
     * @param position
     * @param classId
     * @param behaviorId
     * @param accountId
     * @param option
     * @return
     */
    public VideoRecordModel isExistInCollection(int position, String classId, String behaviorId, String accountId,String option) {

        List<VideoRecordModel> list =
                queryRecord(table_total,"class_id = ? and behavior_id = ? and account_id = ?",new String[] { classId,behaviorId, accountId},option+" ASC");


        int existPosition =binSearch(list,position);
        if (existPosition !=-1){
            return list.get(existPosition);
        }
        return null;
    }

    /**
     * 查询一个学习行为的总汇记录
     * @param classId
     * @param behaviorId
     * @param accountId
     * @return
     */
    public List<VideoRecordModel> queryTotalRecordSingle(String classId, String behaviorId, String accountId) {

        return queryRecord(table_total,"class_id = ? and behavior_id = ? and account_id = ?",new String[] { classId,behaviorId, accountId},"start_point ASC");
    }

    /**
     * 二分查找普通循环实现
     *
     * @param srcArray
     * @param key
     * @return
     */
    public int binSearch(List<VideoRecordModel> srcArray, int key) {

        for (int i =0;i< srcArray.size() ;i++){
            if (key>= srcArray.get(i).getStartPoint() && key<=srcArray.get(i).getEndPoint()){
                return i;
            }
        }
        // 当low>high时表示查找区间为空，查找失败
        return -1;
    }

    /**
     * 删除总汇重复记录
     * @param start
     * @param end
     */
    public void deleteTotalRepeatRecord(int start, int end) {

        SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();

        int i =db.delete(table_total, "start_point >= ? and end_point <= ?", new String[] { String.valueOf(start) , String.valueOf(end)});
        Log.d("XX","删除是否成功"+i);
        DatabaseManager.getInstance(dbHelper).closeDatabase();
    }

    /**
     * 批量删除记录
     * @param table
     * @param videoRecordModels
     * @return
     */
    public boolean deleteRecords(String table,List<VideoRecordModel> videoRecordModels) {
        SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
        if (null == dbHelper || null == videoRecordModels || videoRecordModels.size() <= 0) {
            return false;
        }

        db.beginTransaction();  //开启事务
        try {
            StringBuilder sbStr = new StringBuilder();
            for (int i=0; i<videoRecordModels.size(); i++) {
                sbStr.append("'")
                        .append(videoRecordModels.get(i).getRecord_id())
                        .append("'")
                        .append(",");
            }
            String sql = "delete from "+table+" where record_id in (" + sbStr.substring(0, sbStr.length() - 1) + ")";
            db.execSQL(sql);
            db.setTransactionSuccessful();  //控制回滚，如果不设置此项自动回滚

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                db.endTransaction();  //事务提交
                DatabaseManager.getInstance(dbHelper).closeDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 批量删除明细记录
     * @param videoRecordModels
     * @return
     */
    public boolean deleteDetailRecords(List<VideoRecordModel> videoRecordModels) {
        return deleteRecords(table_detail,videoRecordModels);
    }

    /**
     * 批量删除总汇记录
     * @param videoRecordModels
     * @return
     */
    public boolean deleteTotalRecords(List<VideoRecordModel> videoRecordModels) {
        return deleteRecords(table_total,videoRecordModels);
    }


    /**
     * 批量插入记录
     * @param videoRecordModels
     * @return
     */
    public boolean insertRecordTotals(List<VideoRecordModel> videoRecordModels) {

        SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
        if (null == dbHelper || null == videoRecordModels || videoRecordModels.size() <= 0) {
            return false;
        }
        String sql = "INSERT INTO "+ table_total +"(account_id,account_name,class_id,behavior_id,total_time,start_point,end_point) VALUES (?,?,?,?,?,?,?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        db.beginTransaction();  //开启事务
        try {
            for (int i = 0; i < videoRecordModels.size(); i++) {
                stmt.bindString(1, videoRecordModels.get(i).getAccountId());
                stmt.bindString(2, videoRecordModels.get(i).getAccountName());
                stmt.bindString(3, videoRecordModels.get(i).getClassId());
                stmt.bindString(4, videoRecordModels.get(i).getBehaviorId());
                stmt.bindLong(5, videoRecordModels.get(i).getTotalTime());
                stmt.bindLong(6, videoRecordModels.get(i).getStartPoint());
                stmt.bindLong(7, videoRecordModels.get(i).getEndPoint());
                stmt.executeInsert();
                stmt.clearBindings();
            }

            db.setTransactionSuccessful();  //控制回滚，如果不设置此项自动回滚

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                db.endTransaction();  //事务提交
                DatabaseManager.getInstance(dbHelper).closeDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
