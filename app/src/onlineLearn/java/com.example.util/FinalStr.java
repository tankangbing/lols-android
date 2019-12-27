package com.example.util;

import android.os.Environment;

public class FinalStr {

    // 学习资源的保存路径
    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/onlineLearn/";
    //apk保存路径
    public static final String DOWNLOAD_APK_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/download/";

    //系统编码
    public static final String SYSTEM_CODE ="ZKZXXX";//ZKZXXX//修复检查更新
    //apk保存的名字
    public static final String APK_NAME ="zxxx-online.apk";
    //应用包id
    public static final String APPLICATION_ID ="com.example.onlinelearn";
    //数据库版本
    public static final int DATABASE_VERSION =4;

    //是否打开我的信息功能
    public final static boolean IS_MY_INFO =false;

    //是否打开我的进度功能
    public final static boolean IS_MY_PROGRESS =false;

    //是否打开我的下载功能
    public final static boolean IS_MY_DOWNLOAD =true;

    //是否打开引导页功能
    public final static boolean IS_GUIDE =false;

	public final static String LOG_TAG = "logtest";

    //在线学习测试机
//    public final static String IP = "219.136.9.94";
//    public final static String HTTP = "http://";
//    public final static String PORT = "8000";
//    public final static String PROJECTNAME = "lols-hnjk";
//    public final static String ACCESSPRYPATH = HTTP+IP+":"+PORT+"/"+PROJECTNAME;

    //食品安全正式机
    public final static String SPAQPATH="https://tl.hnjk.net";
    //在线学习正式机 外网
//    public final static String IP = "tl.hnjk.net/lols-hnjk/"; //ip219.136.9.94
//    public final static String HTTP = "https://"; //http服务
//    public final static String PORT = ""; //端口号
//    public final static String PROJECTNAME = ""; //项目名称
//    public final static String ACCESSPRYPATH = HTTP+IP+":"+PORT+"/"+PROJECTNAME; //访问路径

    public final static String IP = "zk.scutde.net"; //ip219.136.9.94
    public final static String HTTP = "https://"; //http服务
    public final static String PORT = ""; //端口号
    public final static String PROJECTNAME = "lols-zk"; //项目名称
    public final static String ACCESSPRYPATH = HTTP+IP+"/"+PROJECTNAME; //访问路径

    /**
     * 日志
     */
    public final static String TAG_ERR = "err";
    public final static String TAG_DUG = "dug";
    public final static String TAG_INFO = "info";
    public final static String LOGNAME =  "IntentPTLog.log"; //日志名称
    public final static String LOGINSUBMITBUG = "LoginSbmitBug.txt";//登录提交bug
    public final static String LOGPATH =  Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Android/data/com.IntnetLearnPlatform.data/" + "Log/";//日志文件夹

    public static final String FIRST_OPEN = "first_open";
}
