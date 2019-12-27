package com.example.jdbc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.util.FinalStr;


@SuppressLint("NewApi")
public class DatabaseHelper extends SQLiteOpenHelper {

    private final String TAG = "DatabaseHelper";
    // 数据库版本号
    private static final int DATABASE_VERSION = FinalStr.DATABASE_VERSION;
    // 数据库名
    private static final String DATABASE_NAME = "IntentLearnData.db";
    private Context context ;
    // 数据表名
    public static final String TABLE_NAME = "PlayHistoryTb";
    public static final String TABLE_NAME_RSA = "RsaTb";
    public static final String TABLE_NAME_USER = "UserTb";
    public static final String TABLE_LEARN_ACCOUNT = "learn_account";
    public static final String TABLE_THREAD_INFO ="thread_info";//ThreadInfo对象的信息
    public static final String TABLE_FILE_MESSAGE="file_message";//ThreadInfo对象的信息
    public static final String TABLE_LEARN_CLASS="learn_class";//课程班
    public static final String TABLE_LEARN_CLASS_STUDENT="learn_class_student";//课程班学习者
    public static final String TABLE_S_D_PRACTICE="S_D_practice";//练习作答汇总记录
    public static final String TABLE_D_L_PRACTICE_ANSWER_DETAIL="D_L_practice_answer_detail";//练习答题明细
    public static final String TABLE_D_L_PRACTICE_QUESTION_ACTION="D_L_practice_question_action";//练习答题明细
    public static final String TABLE_VIDEO_DETAIL ="video_detail";
    public static final String TABLE_VIDEO_TOTAL ="video_total";
    public static final String TABLE_DOCUMENT_DETAIL ="document_detail";
    public static final String TABLE_DOCUMENT_TOTAL ="document_total";

    // 构造函数，调用父类SQLiteOpenHelper的构造函数
    public DatabaseHelper(Context context, String name, CursorFactory factory,
            int version, DatabaseErrorHandler errorHandler)
    {
        super(context, name,factory, version, errorHandler);
        this.context =context;
    }

    public DatabaseHelper(Context context, String name, CursorFactory factory,
            int version)
    {
        super(context, name, factory, version);
        // SQLiteOpenHelper的构造函数参数：
        // context：上下文环境
        // name：数据库名字
        // factory：游标工厂（可选）
        // version：数据库模型版本号
        this.context =context;
    }

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // 数据库实际被创建是在getWritableDatabase()或getReadableDatabase()方法调用时
        Log.d(TAG, "DatabaseHelper Constructor");
        this.context =context;
        // CursorFactory设置为null,使用系统默认的工厂类
    }

    // 继承SQLiteOpenHelper类,必须要覆写的三个方法：onCreate(),onUpgrade(),onOpen()
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // 调用时间：数据库第一次创建时onCreate()方法会被调用

        // onCreate方法有一个 SQLiteDatabase对象作为参数，根据需要对这个对象填充表和初始化数据
        // 这个方法中主要完成创建数据库后对数据库的操作

        Log.d(TAG, "DatabaseHelper onCreate");

        // 构建创建表的SQL语句（可以从SQLite Expert工具的DDL粘贴过来加进StringBuffer中）
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_NAME + "] (");//视频播放记录表
        sBuffer.append("[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "); //id
        sBuffer.append("[mateid] VARCHAR(36),"); //视频id
        sBuffer.append("[userid] VARCHAR(36),"); //用户id
        sBuffer.append("[time] INTEGER)"); //时间

        // 执行创建表的SQL语句
        db.execSQL(sBuffer.toString());
        
        //创建RSA公钥加密算法表
        sBuffer.delete(0,sBuffer.length());
        sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_NAME_RSA + "] (");
        sBuffer.append("[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "); //id
        sBuffer.append("[appRsaModulus] VARCHAR(300),"); //app模
        sBuffer.append("[sysRsaModulus] VARCHAR(300),"); //v3模
        sBuffer.append("[sysRsaPrivateExponent] VARCHAR(50),"); //v3私钥指数
        sBuffer.append("[appRsaPublicExponent] VARCHAR(50))"); //app公钥指数
        db.execSQL(sBuffer.toString());
     
        //创建纪录用户密码的表
        sBuffer.delete(0,sBuffer.length());
        sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_NAME_USER + "] (");
        sBuffer.append("[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "); //id
        sBuffer.append("[userName] VARCHAR(50),"); //用户名称
        sBuffer.append("[userPsw] VARCHAR(50))"); //用户密码
        db.execSQL(sBuffer.toString());
        
        //创建用户信息表
        sBuffer.delete(0,sBuffer.length());
        sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_LEARN_ACCOUNT + "] (");
        sBuffer.append("[id] VARCHAR NOT NULL PRIMARY KEY, "); //id
        sBuffer.append("[account_type] VARCHAR(2),"); //用户类型
        sBuffer.append("[student_password] VARCHAR(50),"); //登录密码
        sBuffer.append("[student_code] VARCHAR(36),"); //学习者编码
        sBuffer.append("[student_name] VARCHAR(100),"); //学习者名称
        sBuffer.append("[phone_number] VARCHAR(11),"); //手机号码
        sBuffer.append("[certificate_type] VARCHAR(36),"); //证件类型
        sBuffer.append("[certificate_number] VARCHAR(64),"); //证件号
        sBuffer.append("[email] VARCHAR(100),"); //邮箱
        sBuffer.append("[system_code] VARCHAR(36),"); //系统编码
        sBuffer.append("[create_date] datetime,"); //创建日期
        sBuffer.append("[modify_date] datetime,"); //修改日期
        sBuffer.append("[del_flag] VARCHAR(2))"); //删除标记
        db.execSQL(sBuffer.toString());


        //创建文件线程表
        sBuffer.delete(0,sBuffer.length());
        sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_THREAD_INFO + "] (");
        sBuffer.append("[_id] integer primary key autoincrement, "); //id
        sBuffer.append("[thread_id] integer,");
        sBuffer.append("[url] text,");
        sBuffer.append("[start] integer,");
        sBuffer.append("[end] integer,");
        sBuffer.append("[finished] integer)");
        db.execSQL(sBuffer.toString());

        //创建文件信息
        sBuffer.delete(0,sBuffer.length());
        sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_FILE_MESSAGE + "] (");
        sBuffer.append("[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "); //id
        sBuffer.append("[class_id] VARCHAR(36),");
        sBuffer.append("[student_id] VARCHAR(36),");
        sBuffer.append("[behavior_id] VARCHAR(36),");
        sBuffer.append("[chapter_id] VARCHAR(36),");
        sBuffer.append("[file_name] VARCHAR(36),");
        sBuffer.append("[attach_name] VARCHAR(36),");
        sBuffer.append("[file_type] VARCHAR(10),");
        sBuffer.append("[file_code] VARCHAR(10),");
        sBuffer.append("[file_size] INTEGER,");
        sBuffer.append("[file_finish] INTEGER,");
        sBuffer.append("[savepath] VARCHAR(128),");
        sBuffer.append("[create_date] DATETIME,");
        sBuffer.append("[url] VARCHAR(128),");
        sBuffer.append("[file_time] INTEGER,");
        sBuffer.append("[chapter_name] VARCHAR(100),");
        sBuffer.append("[show_schedule] VARCHAR(4),");
        sBuffer.append("[show_notice] VARCHAR(4),");
        sBuffer.append("[show_evaluate] VARCHAR(4),");
        sBuffer.append("[show_ask] VARCHAR(4),");
        sBuffer.append("[progress_status] INTEGER)");
        db.execSQL(sBuffer.toString());

        //创建课程班
        sBuffer.delete(0,sBuffer.length());
        sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_LEARN_CLASS + "] (");
        sBuffer.append("[id] VARCHAR NOT NULL PRIMARY KEY, "); //id
        sBuffer.append("[class_code] VARCHAR(36),"); //课程班编码
        sBuffer.append("[class_name] VARCHAR(100),"); //课程班名称
        sBuffer.append("[class_status] VARCHAR(2),"); //课程班状态
        sBuffer.append("[system_code] VARCHAR(36),"); //系统编码
        sBuffer.append("[system_name] VARCHAR(100),"); //系统名称
        sBuffer.append("[learn_start_time] datetime,"); //开始时间
        sBuffer.append("[learn_end_time] datetime,"); //结束时间
        sBuffer.append("[img_path] TEXT,"); //图片地址
        sBuffer.append("[appointment_start_time] datetime,"); //开始时间
        sBuffer.append("[appointment_end_time] datetime,"); //结束时间
        sBuffer.append("[assessment_index_type] char(1),"); //创建日期
        sBuffer.append("[assessment_index] VARCHAR(100),"); //修改日期
        sBuffer.append("[course_id] VARCHAR(36),"); //邮箱
        sBuffer.append("[courseware_id] VARCHAR(36),"); //系统编码
        sBuffer.append("[teacher_id] VARCHAR(36),"); //创建日期
        sBuffer.append("[teacher_name] VARCHAR(100),"); //修改日期
        sBuffer.append("[teacher_school_name] VARCHAR(100),"); //创建日期
        sBuffer.append("[create_date] datetime,"); //邮箱
        sBuffer.append("[create_username] VARCHAR(36),"); //系统编码
        sBuffer.append("[create_userid] VARCHAR(36),"); //创建日期
        sBuffer.append("[modify_date] datetime,"); //邮箱
        sBuffer.append("[modify_username] VARCHAR(36),"); //系统编码
        sBuffer.append("[modify_userid] VARCHAR(36),"); //创建日期
        sBuffer.append("[del_flag] VARCHAR(2))"); //删除标记
        db.execSQL(sBuffer.toString());

        //创建课程班学习者
        sBuffer.delete(0,sBuffer.length());
        sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_LEARN_CLASS_STUDENT + "] (");
        sBuffer.append("[id] VARCHAR NOT NULL PRIMARY KEY, "); //id
        sBuffer.append("[class_id] VARCHAR(36),"); //用户类型
        sBuffer.append("[student_id] VARCHAR(36),"); //登录密码
        sBuffer.append("[class_name] VARCHAR(100),"); //登录密码
        sBuffer.append("[learn_start_time] datetime,"); //学习者编码
        sBuffer.append("[learn_end_time] datetime,"); //学习者名称
        sBuffer.append("[create_date] datetime,"); //手机号码
        sBuffer.append("[create_username] VARCHAR(36),"); //证件类型
        sBuffer.append("[create_userid] VARCHAR(36),"); //证件号
        sBuffer.append("[modify_date] datetime,"); //邮箱
        sBuffer.append("[modify_username] VARCHAR(36),"); //系统编码
        sBuffer.append("[modify_userid] VARCHAR(36),"); //创建日期
        sBuffer.append("[del_flag] VARCHAR(2))"); //删除标记
        db.execSQL(sBuffer.toString());


        //练习作答汇总记录
        sBuffer.delete(0,sBuffer.length());
        sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_S_D_PRACTICE + "] (");
        sBuffer.append("[id] VARCHAR NOT NULL PRIMARY KEY, "); //id
        sBuffer.append("[account_id] VARCHAR(36),"); //学习者主键
        sBuffer.append("[account_code] VARCHAR(36),"); //学习者编码
        sBuffer.append("[account_name] VARCHAR(100),"); //学习者名称
        sBuffer.append("[class_id] VARCHAR(36),"); //课程班主键
        sBuffer.append("[class_code] VARCHAR(36),"); //课程班编码
        sBuffer.append("[behavior_id] VARCHAR(36),"); //行为主键
        sBuffer.append("[behavior_name] VARCHAR(100),"); //学习行为名称
        sBuffer.append("[practice_times] integer,"); //练习次数
        sBuffer.append("[question_toal_count] integer,"); //题目总数
        sBuffer.append("[question_answer_count] integer,"); //作答过题目总数
        sBuffer.append("[question_error_count] integer,"); //答错过题目总数
        sBuffer.append("[question_correct_count] integer,"); //答对过题目总数
        sBuffer.append("[statistics_date] datetime,"); //统计时间
        sBuffer.append("[sign_question_ids] long,"); //标记主键串
        sBuffer.append("[error_question_ids] long,"); //错题库主键串
        sBuffer.append("[correct_ids] long,"); //最后一次答对主键串
        sBuffer.append("[error_ids] long)"); //最后一次答错主键串
        db.execSQL(sBuffer.toString());


        //练习答题明细
        sBuffer.delete(0,sBuffer.length());
        sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_D_L_PRACTICE_ANSWER_DETAIL + "] (");
        sBuffer.append("[id] VARCHAR NOT NULL PRIMARY KEY, "); //id
        sBuffer.append("[question_id] VARCHAR(36),"); //题目主键
        sBuffer.append("[question_type] VARCHAR(2),"); //题目类型
        sBuffer.append("[answer_status] VARCHAR(2),"); //回答结果
        sBuffer.append("[option_id] VARCHAR(1024),"); //选项主键
        sBuffer.append("[batch_id] VARCHAR(36),"); //选项主键
        sBuffer.append("[behavior_id] VARCHAR(36))"); //批次主键
        db.execSQL(sBuffer.toString());


        //练习答题明细
        sBuffer.delete(0,sBuffer.length());
        sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_D_L_PRACTICE_QUESTION_ACTION + "] (");
        sBuffer.append("[id] VARCHAR NOT NULL PRIMARY KEY, "); //id
        sBuffer.append("[question_id] VARCHAR(36),"); //题目主键
        sBuffer.append("[question_type] VARCHAR(2),"); //题目类型
        sBuffer.append("[class_id] VARCHAR(36),"); //选项主键
        sBuffer.append("[is_marked] VARCHAR(2),"); //回答结果
        sBuffer.append("[account_id] VARCHAR(36),"); //选项主键
        sBuffer.append("[behavior_id] VARCHAR(36))"); //批次主键
        db.execSQL(sBuffer.toString());


        //创建视频明细表
        sBuffer.delete(0,sBuffer.length());
        sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_VIDEO_DETAIL + "] (");
        sBuffer.append("[record_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "); //id
        sBuffer.append("[id] VARCHAR(36),");
        sBuffer.append("[account_id] VARCHAR(36),");
        sBuffer.append("[account_name] VARCHAR(100),");
        sBuffer.append("[class_id] VARCHAR(36),");
        sBuffer.append("[behavior_id] VARCHAR(36),");
        sBuffer.append("[total_time] INTEGER,");
        sBuffer.append("[start_point] INTEGER,");
        sBuffer.append("[end_point] INTEGER,");
        sBuffer.append("[start_time] INTEGER,");
        sBuffer.append("[end_time] INTEGER,");
        sBuffer.append("[is_summary] VARCHAR(2),");
        sBuffer.append("[data_source] VARCHAR(2))");
        db.execSQL(sBuffer.toString());

        //创建视频总汇表
        sBuffer.delete(0,sBuffer.length());
        sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_VIDEO_TOTAL + "] (");
        sBuffer.append("[record_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "); //id
        sBuffer.append("[id] VARCHAR(36),");
        sBuffer.append("[account_id] VARCHAR(36),");
        sBuffer.append("[account_name] VARCHAR(100),");
        sBuffer.append("[class_id] VARCHAR(36),");
        sBuffer.append("[behavior_id] VARCHAR(36),");
        sBuffer.append("[total_time] INTEGER,");
        sBuffer.append("[start_point] INTEGER,");
        sBuffer.append("[end_point] INTEGER,");
        sBuffer.append("[learn_length] INTEGER,");
        sBuffer.append("[start_time] INTEGER ,");
        sBuffer.append("[end_time] INTEGER)");
        db.execSQL(sBuffer.toString());

        //创建文档明细表
        sBuffer.delete(0,sBuffer.length());
        sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_DOCUMENT_DETAIL + "] (");
        sBuffer.append("[record_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "); //id
        sBuffer.append("[id] VARCHAR(36),");
        sBuffer.append("[account_id] VARCHAR(36),");
        sBuffer.append("[account_name] VARCHAR(100),");
        sBuffer.append("[class_id] VARCHAR(36),");
        sBuffer.append("[behavior_id] VARCHAR(36),");
        sBuffer.append("[click_time] INTEGER,");
        sBuffer.append("[total_pages] INTEGER,");
        sBuffer.append("[current_page] INTEGER,");
        sBuffer.append("[is_summary] VARCHAR(2),");
        sBuffer.append("[data_source] VARCHAR(2))");
        db.execSQL(sBuffer.toString());

        //创建文档总汇表
        sBuffer.delete(0,sBuffer.length());
        sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_DOCUMENT_TOTAL + "] (");
        sBuffer.append("[record_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "); //id
        sBuffer.append("[id] VARCHAR(36),");
        sBuffer.append("[account_id] VARCHAR(36),");
        sBuffer.append("[account_name] VARCHAR(100),");
        sBuffer.append("[account_code] VARCHAR(36),");
        sBuffer.append("[class_id] VARCHAR(36),");
        sBuffer.append("[class_code] VARCHAR(36),");
        sBuffer.append("[behavior_id] VARCHAR(36),");
        sBuffer.append("[behavior_name] VARCHAR(36),");
        sBuffer.append("[click_times] INTEGER,");
        sBuffer.append("[total_pages] INTEGER,");
        sBuffer.append("[read_pages] INTEGER,");
        sBuffer.append("[statistics_date] INTEGER)");
        db.execSQL(sBuffer.toString());

        // 即便程序修改重新运行，只要数据库已经创建过，就不会再进入这个onCreate方法

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // 调用时间：如果DATABASE_VERSION值被改为别的数,系统发现现有数据库版本不同,即会调用onUpgrade

        // onUpgrade方法的三个参数，一个 SQLiteDatabase对象，一个旧的版本号和一个新的版本号
        // 这样就可以把一个数据库从旧的模型转变到新的模型
        // 这个方法中主要完成更改数据库版本的操作

        Log.d(TAG, "DatabaseHelper onUpgrade");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RSA); //删除RSA加密表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //删除播放记录表
        // 上述做法简单来说就是，通过检查常量值来决定如何，升级时删除旧表，然后调用onCreate来创建新表
        // 一般在实际项目中是不能这么做的，正确的做法是在更新数据表结构时，还要考虑用户存放于数据库中的数据不丢失

        /**
         * 版本4新增
         */

        try{
            db.execSQL("ALTER TABLE "+TABLE_LEARN_CLASS+" ADD COLUMN 'img_path' TEXT");//增减一项 保存用户数据
        }catch (Exception e){

        }

        clearUserMsg();
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);
        // 每次打开数据库之后首先被执行

        Log.d(TAG, "打开了数据连接");
    }

    /**
     *清空用户信息
     */
    private void clearUserMsg(){
        SharedPreferences sp=context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
