package com.example.jdbc.Dao.impl;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import com.example.entity.FileInfo;
import com.example.entity.LearnClassEntity;
import com.example.jdbc.Dao.FileDAO;
import com.example.jdbc.DatabaseHelper;
import com.example.jdbc.DatabaseManager;
import com.example.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 數據庫增刪改查的實現類
 *
 */
public class FileDAOImpl implements FileDAO {
	private DatabaseHelper dbHelper = null;
	private String table =dbHelper.TABLE_FILE_MESSAGE;
	public FileDAOImpl(Context context) {
		super();
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * 插入文件信息
	 * @param info
	 */
	public boolean insertFile(FileInfo info) {
		SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();

		long currentMillis =System.currentTimeMillis();//当前时间毫秒;
		String currentTime = DateUtil.getCurrentTime(new Date(currentMillis)
				,DateUtil.GetSimpleDateFormat(DateUtil.FMTSTR_CH));

		ContentValues values = new ContentValues();
		values.put("class_id", info.getClass_id());
		values.put("student_id", info.getStudent_id());
		values.put("behavior_id", info.getBehavior_id());
		values.put("chapter_id", info.getChapter());
		values.put("file_name", info.getFile_name());
		values.put("attach_name", info.getAttach_name());
		values.put("file_type", info.getFile_type());
		values.put("file_code", info.getFile_code());
		values.put("file_size", info.getFile_size());
		values.put("file_finish", info.getFile_finish());
		values.put("savepath", info.getSavepath());
		values.put("create_date", currentTime);
		values.put("url", info.getUrl());
		values.put("file_time",info.getFile_time());
		values.put("progress_status", info.getProgress_status());
        values.put("chapter_name", info.getChapterName());
		values.put("show_schedule", info.getShow_schedule());
		values.put("show_notice", info.getShow_notice());
		values.put("show_evaluate", info.getShow_evaluate());
		values.put("show_ask", info.getShow_ask());

		long i =db.insert(table, null, values);

		DatabaseManager.getInstance(dbHelper).closeDatabase();
		if (i>0)
			return true;
		else
			return false;
	}

	@Override
	public List<FileInfo> queryFile(String table,String[] columns, String selection, String[] selectionArgs,String groupBy,String having,String  orderBy) {

		SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();

		List<FileInfo> list = new ArrayList<FileInfo>();

		Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		while (cursor.moveToNext()) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
			fileInfo.setClass_id(cursor.getString(cursor.getColumnIndex("class_id")));
			fileInfo.setStudent_id(cursor.getString(cursor.getColumnIndex("student_id")));
			fileInfo.setBehavior_id(cursor.getString(cursor.getColumnIndex("behavior_id")));
			fileInfo.setChapter(cursor.getString(cursor.getColumnIndex("chapter_id")));
			fileInfo.setFile_name(cursor.getString(cursor.getColumnIndex("file_name")));
			fileInfo.setAttach_name(cursor.getString(cursor.getColumnIndex("attach_name")));
			fileInfo.setFile_type(cursor.getString(cursor.getColumnIndex("file_type")));
			fileInfo.setFile_code(cursor.getString(cursor.getColumnIndex("file_code")));
			fileInfo.setFile_size(cursor.getInt(cursor.getColumnIndex("file_size")));
			fileInfo.setFile_finish(cursor.getInt(cursor.getColumnIndex("file_finish")));
			fileInfo.setSavepath(cursor.getString(cursor.getColumnIndex("savepath")));
			fileInfo.setCreate_date(cursor.getString(cursor.getColumnIndex("create_date")));
			fileInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
			fileInfo.setFile_time(cursor.getInt(cursor.getColumnIndex("file_time")));
			fileInfo.setProgress_status(cursor.getInt(cursor.getColumnIndex("progress_status")));
            fileInfo.setChapterName(cursor.getString(cursor.getColumnIndex("chapter_name")));
			fileInfo.setShow_schedule(cursor.getString(cursor.getColumnIndex("show_schedule")));
			fileInfo.setShow_evaluate(cursor.getString(cursor.getColumnIndex("show_evaluate")));
			fileInfo.setShow_notice(cursor.getString(cursor.getColumnIndex("show_notice")));
			fileInfo.setShow_ask(cursor.getString(cursor.getColumnIndex("show_ask")));
			list.add(fileInfo);
		}
		cursor.close();
		DatabaseManager.getInstance(dbHelper).closeDatabase();
		return list;

	}

    /**
     * 查找单个用户的的课程id
     * @param student_id
     * @return
     */
    public List<String> queryClassIds(String student_id) {
		List<String> strings = new ArrayList<>();
        SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
        Cursor cursor = db.query(table, new String[] { "class_id" }, " student_id = ?", new String[] { student_id }, "class_id",null,null);
		while (cursor.moveToNext()) {
			strings.add(cursor.getString(cursor.getColumnIndex("class_id")));
		}

        return strings;

    }

	/**
	 * 查询用户单个课程的所有文件
	 * @param class_id
	 * @param student_id
	 * @return
	 */
	public List<FileInfo> querySingleClassFiles(String class_id,String student_id) {

		return queryFile(table,null,"class_id = ? and student_id = ? ", new String[] { class_id,student_id },null,null,null);
	}

	/**
	 * 查询用户单个课程总文件数量
	 * @param class_id	课程id
	 * @param student_id	用户id
	 * @return
	 */
	public int queryFilesSizeALL(String class_id, String student_id) {
		SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
		String sql = "select count(*) from "+table +" where class_id = '"+class_id +"' and student_id = '"+student_id +"'";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		DatabaseManager.getInstance(dbHelper).closeDatabase();
		return count;
	}

	/**
	 * 查询用户单个课程完成文件数量
	 * @param class_id	课程id
	 * @param student_id	用户id
	 * @return
	 */
	public int queryFilesSizeFinish(String class_id,String student_id) {

		SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
		String sql = "select count(*) from "+table +" where class_id = '"+class_id +"' and student_id = '"+student_id +"' and progress_status = 2";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		DatabaseManager.getInstance(dbHelper).closeDatabase();
		return count;
	}

	/**
	 * 查询用户单个课程正在下载的文件数量
	 * @param class_id	课程id
	 * @param student_id	用户id
	 * @return
	 */
	public int queryFilesSizeDownload(String class_id,String student_id) {
		SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
		String sql = "select count(*) from "+table +" where class_id = '"+class_id +"' and student_id = '"+student_id +"'and progress_status = 1";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		DatabaseManager.getInstance(dbHelper).closeDatabase();
		return count;
	}

	/**
	 * 查询用户单个课程下载完成文件总大小
	 * @param class_id	课程id
	 * @param student_id	用户id
	 * @return
	 */
	public long queryFilesSize(String class_id, String student_id) {
		SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
		String sql = "select sum(file_size) from "+table +" where class_id = '"+class_id +"' and student_id = '"+student_id +"'and progress_status = 2";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		long count = cursor.getLong(0);
		cursor.close();
		DatabaseManager.getInstance(dbHelper).closeDatabase();
		return count;
	}

	/**
	 * 查询用户所有课程下载完成文件总大小
	 * @param student_id
	 * @return
	 */
	public long queryFilesTotalSize(String student_id) {
		SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
		String sql = "select sum(file_size) from "+table +" where student_id = '"+student_id +"'and progress_status = 2";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		long count = cursor.getLong(0);
		cursor.close();
		DatabaseManager.getInstance(dbHelper).closeDatabase();
		return count;
	}

	/**
	 * 查询用户某些特定状态的文件
	 * @return
	 */
	public List<FileInfo> queryFilesInStatus(String student_id) {

		return queryFile(table,null,"student_id = ? and progress_status in(0,1,3)", new String[] { student_id },null,null,null);
	}

	@Override
	public List<FileInfo> queryFileWithClassId(String student_id, List<String> class_ids) {

		List<String> parameters = new ArrayList<>();

		for (int i=0; i<class_ids.size(); i++) {
			parameters.add("?");
		}
		return queryFile(table,null," class_id in (" + TextUtils.join(",", parameters) + ")",class_ids.toArray(new String[class_ids.size()]),null,null,null);

	}

	@Override
	public List<FileInfo> queryFileWithBehaviorId(String student_id, List<String> behaviorIds) {
		List<String> parameters = new ArrayList<>();

		for (int i=0; i<behaviorIds.size(); i++) {
			parameters.add("?");
		}
		return queryFile(table,null," behavior_id in (" + TextUtils.join(",", parameters) + ")",behaviorIds.toArray(new String[behaviorIds.size()]),null,null,null);
	}

	/**
	 * 查找该用户的所有文件
	 * @param student_id	用户id
	 * @return
	 */
	public List<FileInfo> queryAllFiles(String student_id) {
		return queryFile(table,null,"student_id = ?", new String[] { student_id },null,null,null);
	}


	/**
	 * 查找该用户的单个文件
	 * @param behaviorId
	 * @return
	 */
	public FileInfo querySingleFile(String student_id,String behaviorId) {

		List<FileInfo> list = queryFile(table,null,"student_id = ? and behavior_id = ?", new String[] { student_id,behaviorId },null,null,null);

		if (list.size()!=0){
			return list.get(0);
		}
		else{
			return null;
		}
	}

	/**
	 * 更新用户单个文件信息
	 * @param info
	 */
	public void updateFile(FileInfo info) {

		SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
		ContentValues values = new ContentValues();

		values.put("attach_name", info.getAttach_name());
		values.put("file_type", info.getFile_type());
		values.put("file_size", info.getFile_size());
		values.put("file_finish", info.getFile_finish());
		values.put("savepath", info.getSavepath());
		values.put("url", info.getUrl());
		values.put("file_time", info.getFile_time());
		values.put("progress_status", info.getProgress_status());
		int result=db.update(table,values,"student_id = ? and behavior_id = ?",new String[]{String.valueOf(info.getStudent_id()), info.getBehavior_id()});

		DatabaseManager.getInstance(dbHelper).closeDatabase();
	}



	@Override
	public void initAll(String coursewareId) {
		SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
		ContentValues values = new ContentValues();

		values.put("progress_status",0);
		int result=db.update(table,values,"class_id = ? and progress_status = 1",new String[]{coursewareId});
		Log.d("XX","更新是否成功"+result);
		DatabaseManager.getInstance(dbHelper).closeDatabase();
	}


	/**
	 * 判断用户单个文件是否存在
	 * @param behaviorId
	 * @param student_id
	 * @return
	 */
	@Override
	public boolean isExists(String behaviorId ,String student_id) {
		SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
		Cursor cursor = db.query(table, null, "behavior_id = ? and student_id = ?", new String[] { behaviorId,student_id},
				null, null, null);
		boolean exists = cursor.moveToNext();

		DatabaseManager.getInstance(dbHelper).closeDatabase();
		cursor.close();
		return exists;
	}

	@Override
	public int queryFilesSize(String coursewareId) {
		SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
		String sql = "select count(*) from "+table +" where class_id = '"+coursewareId +"'";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		DatabaseManager.getInstance(dbHelper).closeDatabase();
		return count;

	}

	@Override
	public boolean deleteFiles(List<FileInfo> fileInfos) {
		SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
		if (null == dbHelper || null == fileInfos || fileInfos.size() <= 0) {
			return false;
		}
		String student_id =fileInfos.get(0).getStudent_id();
		db.beginTransaction();  //开启事务
		try {
			StringBuilder sbStr = new StringBuilder();
			for (int i=0; i<fileInfos.size(); i++) {
				sbStr.append("'")
						.append(fileInfos.get(i).getBehavior_id())
						.append("'")
						.append(",");
			}
			String sql = "delete from "+table+" where student_id = '"+ student_id + "' and behavior_id in (" + sbStr.substring(0, sbStr.length() - 1) + ")";
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
	 * 批量更新用户文件
	 * @param FileInfos
	 * @return
	 */
	public boolean updateFiles(List<FileInfo> FileInfos) {
		SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
		if (null == dbHelper || null == FileInfos || FileInfos.size() <= 0) {
			return false;
		}

		db.beginTransaction();  //开启事务
		try {
			for (FileInfo fileInfo : FileInfos) {
				ContentValues values = new ContentValues();

				values.put("attach_name", fileInfo.getAttach_name());
				values.put("file_type", fileInfo.getFile_type());
				values.put("file_size", fileInfo.getFile_size());
				values.put("file_finish", fileInfo.getFile_finish());
				values.put("savepath", fileInfo.getSavepath());
				values.put("url", fileInfo.getUrl());
				values.put("file_time", fileInfo.getFile_time());
				values.put("progress_status", fileInfo.getProgress_status());

				db.update(table,values,"student_id = ? and behavior_id = ?",new String[]{String.valueOf(fileInfo.getStudent_id()), fileInfo.getBehavior_id()});
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

	@Override
	public boolean insertFiles(List<FileInfo> FileInfos) {
		SQLiteDatabase db = DatabaseManager.getInstance(dbHelper).getReadableDatabase();
		if (null == dbHelper || null == FileInfos || FileInfos.size() <= 0) {
			return false;
		}
		long currentMillis =System.currentTimeMillis();//当前时间毫秒;
		String currentTime = DateUtil.getCurrentTime(new Date(currentMillis)
				,DateUtil.GetSimpleDateFormat(DateUtil.FMTSTR_CH));

		String sql = "INSERT INTO "+ table +"(class_id,behavior_id,file_name,attach_name,file_type," +
				"file_code,file_size,file_finish,savepath,create_date," +
				"url,file_time,progress_status,chapter_id,student_id," +
                "chapter_name,show_schedule,show_notice,show_evaluate,show_ask) " +
				"VALUES (?,?,?,?,?" +
				",?,?,?,?,?" +
				",?,?,?,?,?," +
                "?,?,?,?,?)";
		SQLiteStatement stmt = db.compileStatement(sql);
		db.beginTransaction();  //开启事务
		try {
			for (int i = 0; i < FileInfos.size(); i++) {
				stmt.bindString(1, FileInfos.get(i).getClass_id());
				stmt.bindString(2, FileInfos.get(i).getBehavior_id());
				stmt.bindString(3, FileInfos.get(i).getFile_name());
				stmt.bindString(4, FileInfos.get(i).getAttach_name());
				stmt.bindString(5, FileInfos.get(i).getFile_type());
				stmt.bindString(6, FileInfos.get(i).getFile_code());
				stmt.bindLong(7, FileInfos.get(i).getFile_size());
				stmt.bindLong(8, FileInfos.get(i).getFile_finish());
				stmt.bindString(9, FileInfos.get(i).getSavepath());
				stmt.bindString(10, currentTime);
				stmt.bindString(11, FileInfos.get(i).getUrl());
				stmt.bindLong(12, FileInfos.get(i).getFile_time());
				stmt.bindLong(13, FileInfos.get(i).getProgress_status());
				stmt.bindString(14, FileInfos.get(i).getChapter());
				stmt.bindString(15, FileInfos.get(i).getStudent_id());
                stmt.bindString(16, FileInfos.get(i).getChapterName());
				stmt.bindString(17, FileInfos.get(i).getShow_schedule());
				stmt.bindString(18, FileInfos.get(i).getShow_notice());
				stmt.bindString(19, FileInfos.get(i).getShow_evaluate());
				stmt.bindString(20, FileInfos.get(i).getShow_ask());
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
