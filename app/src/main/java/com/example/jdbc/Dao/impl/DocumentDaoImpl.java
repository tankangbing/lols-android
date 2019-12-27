package com.example.jdbc.Dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.entity.DocumentRecordModel;
import com.example.entity.FileInfo;
import com.example.entity.VideoRecordModel;
import com.example.jdbc.Dao.DocumentDAO;
import com.example.jdbc.DatabaseHelper;
import com.example.jdbc.DatabaseManager;
import com.example.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文档数据层实现
 */

public class DocumentDaoImpl implements DocumentDAO {

    private DatabaseHelper dbHelper = null;
    private String table_detail =dbHelper.TABLE_DOCUMENT_DETAIL;
    private String table_total =dbHelper.TABLE_DOCUMENT_TOTAL;

    public DocumentDaoImpl(Context context) {
        super();
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * 插入单个文档明细
     * @param documentRecordModel
     */
    public void insertDetailRecord(DocumentRecordModel documentRecordModel) {

        long currentTimeMillis =System.currentTimeMillis();//当前时间毫秒;

        SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("account_id", documentRecordModel.getAccountId());
        values.put("account_name", documentRecordModel.getAccountName());
        values.put("class_id", documentRecordModel.getClassId());
        values.put("behavior_id", documentRecordModel.getBehaviorId());
        values.put("click_time", currentTimeMillis);
        values.put("total_pages", documentRecordModel.getTotalPages());
        values.put("current_page", documentRecordModel.getCurrentPage());
        values.put("is_summary", "1");
        values.put("data_source","1");

        db.insert(table_detail, null, values);
        DatabaseManager.getInstance(dbHelper).closeDatabase();
    }


    /**
     * 批量插入文档总汇
     * @param documentRecordModels
     * @return
     */
    public boolean insertTotalRecords(List<DocumentRecordModel> documentRecordModels) {
        SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
        if (null == dbHelper || null == documentRecordModels || documentRecordModels.size() <= 0) {
            return false;
        }
        String sql = "INSERT INTO "+ table_total +"(account_id,account_name,account_code,class_id,class_code,behavior_id,behavior_name,click_times,total_pages,read_pages,statistics_date)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        db.beginTransaction();  //开启事务
        try {
            for (int i = 0; i < documentRecordModels.size(); i++) {
                stmt.bindString(1, documentRecordModels.get(i).getAccountId());
                stmt.bindString(2, documentRecordModels.get(i).getAccountName());
                stmt.bindString(3, documentRecordModels.get(i).getAccountCode());
                stmt.bindString(4, documentRecordModels.get(i).getClassId());
                stmt.bindString(5, documentRecordModels.get(i).getClassCode());
                stmt.bindString(6, documentRecordModels.get(i).getBehaviorId());
                stmt.bindString(7, documentRecordModels.get(i).getBehaviorName());
                stmt.bindLong(8, documentRecordModels.get(i).getClickTime());
                stmt.bindLong(9, documentRecordModels.get(i).getTotalPages());
                stmt.bindLong(10, documentRecordModels.get(i).getReadPages());
                stmt.bindLong(11, documentRecordModels.get(i).getStatisticsDate());
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

    /**
     * 批量删除文档记录
     * @param table
     * @param documentRecordModels
     * @return
     */
    public boolean deleteRecords(String table, List<DocumentRecordModel> documentRecordModels) {
        SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
        if (null == dbHelper || null == documentRecordModels || documentRecordModels.size() <= 0) {
            return false;
        }

        db.beginTransaction();  //开启事务
        try {
            StringBuilder sbStr = new StringBuilder();
            for (int i=0; i<documentRecordModels.size(); i++) {
                sbStr.append("'")
                        .append(documentRecordModels.get(i).getRecord_id())
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
     * @param documentRecordModels
     * @return
     */
    public boolean deleteDetailRecords(List<DocumentRecordModel> documentRecordModels) {
        return deleteRecords(table_detail,documentRecordModels);
    }

    /**
     * 批量删除总汇记录
     * @param documentRecordModels
     * @return
     */
    public boolean deleteTotalRecords(List<DocumentRecordModel> documentRecordModels) {
        return deleteRecords(table_total,documentRecordModels);
    }


    /**
     * 查询记录
     * @param table
     * @param selection
     * @param selectionArgs
     * @return
     */
    public List<DocumentRecordModel> queryRecord(String table, String selection, String[] selectionArgs) {
        SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
        List<DocumentRecordModel> list = new ArrayList<DocumentRecordModel>();

        Cursor cursor = db.query(table, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            DocumentRecordModel documentRecordModel = new DocumentRecordModel();
            documentRecordModel.setRecord_id(cursor.getInt(cursor.getColumnIndex("record_id")));
            documentRecordModel.setAccountId(cursor.getString(cursor.getColumnIndex("account_id")));
            documentRecordModel.setAccountName(cursor.getString(cursor.getColumnIndex("account_name")));
            documentRecordModel.setClassId(cursor.getString(cursor.getColumnIndex("class_id")));
            documentRecordModel.setBehaviorId(cursor.getString(cursor.getColumnIndex("behavior_id")));
            documentRecordModel.setTotalPages(cursor.getInt(cursor.getColumnIndex("total_pages")));
            if (table_detail.equals(table)){
                documentRecordModel.setClickTime(cursor.getLong(cursor.getColumnIndex("click_time")));
                documentRecordModel.setCurrentPage(cursor.getInt(cursor.getColumnIndex("current_page")));
                documentRecordModel.setIsSummary(cursor.getString(cursor.getColumnIndex("is_summary")));
                documentRecordModel.setDataSource(cursor.getString(cursor.getColumnIndex("data_source")));
            }else{
                documentRecordModel.setAccountCode(cursor.getString(cursor.getColumnIndex("account_code")));
                documentRecordModel.setBehaviorName(cursor.getString(cursor.getColumnIndex("behavior_name")));
                documentRecordModel.setClickTimes(cursor.getInt(cursor.getColumnIndex("click_times")));
                documentRecordModel.setReadPages(cursor.getInt(cursor.getColumnIndex("read_pages")));
                documentRecordModel.setStatisticsDate(cursor.getLong(cursor.getColumnIndex("statistics_date")));
            }
            list.add(documentRecordModel);
        }
        cursor.close();
        DatabaseManager.getInstance(dbHelper).closeDatabase();
        return list;
    }

    /**
     * 查询单个用户 单个行为的明细
     * @param classId
     * @param behaviorId
     * @param accountId
     * @return
     */
    public List<DocumentRecordModel> queryDetailRecordSingle(String classId, String behaviorId, String accountId) {

        return queryRecord(table_detail,"class_id = ? and behavior_id = ? and account_id = ?",new String[] { classId,behaviorId, accountId});
    }

    /**
     * 查询单个用户 单个行为的总汇
     * @param classId
     * @param behaviorId
     * @param accountId
     * @return
     */
    public List<DocumentRecordModel> queryTotalRecordSingle(String classId, String behaviorId, String accountId) {
        return queryRecord(table_total,"class_id = ? and behavior_id = ? and account_id = ?",new String[] { classId,behaviorId, accountId});
    }

}
