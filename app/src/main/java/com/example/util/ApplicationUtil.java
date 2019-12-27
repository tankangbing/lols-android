package com.example.util;


import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.view.View;
import android.view.WindowManager;

import com.example.entity.PracticLearnBehavior;
import com.example.entity.Rsa;
import com.example.jdbc.DBManagerToRsa;
import com.example.spt.jaxb.paper.ExerciseCard;
import com.example.spt.jaxb.paper.Paper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
/*import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;


public class ApplicationUtil extends MultiDexApplication {

    //设置加载框
    static {//static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
           /*     layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色*/
                return new FalsifyHeader(context);//
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }
   /* private RefWatcher refWatcher;*/
    private String userid; //用户id
    private String stuCode; //学生编码
    private String userPsw;//缓存用户密码（加密）
    private String username; //用户名称
    private String stuid;
    private String userEmail;//email
    private String userMobile;//电话
    private String userXm;//中文名称
    private String courseId;//课程id
    private String mateid;//视频id
    private String classicname;//课程名称
    private String behaviorId ;//练习所有题目
    private String classId ;//课程班主键
    private String coursewareId ;//课件主键
    private List<Activity> activityList = new LinkedList<Activity>();
    private DBManagerToRsa dbManager; //sqlite操作对象
    private Rsa rsa = new Rsa();//rsa 加密算法

    private Paper papers =new Paper();//练习所有题目
    private PracticLearnBehavior practicLearnBehavior;//练习学习记录

    private ExerciseCard exerciseCard=new ExerciseCard();
    String status="0";//加载状态：0打开加载 1返回加载




    @Override
    public void onCreate() {
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        super.onCreate();

        //调用jpush
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        //初始化Rsa加密算法
        initRsa();

      /*  refWatcher = LeakCanary.install(this);*/

    }

    /**
     * 多包错误需增加
     * @param base
     */
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     *  初始化Rsa加密算法
     */
    private void initRsa(){
        try {
            dbManager = new DBManagerToRsa(getApplicationContext());
            List<Rsa> listRsa = dbManager.queryRsa();
            if(null != listRsa && listRsa.size() > 0){
                rsa = listRsa.get(0);
            }else{ //从V3平台获取Rsa加密算法信息
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String,Object> map = new HashMap<String,Object>(0);
                       /* //获取手机唯一标识
                        TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
                        map.put("mobileKey",StringUtils.isNotBlank(TelephonyMgr.getDeviceId())?TelephonyMgr.getDeviceId():"0");*/

                        String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/edu3/app/urlto/getRsaKey.html",
                                map,getApplicationContext(),null);
                        Map<String, Object> maps = StringToJson.parseJSON2Map(result);
                        rsa.setId(1);
                        rsa.setAppRsaModulus(maps.get("appRsaModulus")+"");
                        rsa.setSysRsaModulus(maps.get("sysRsaModulus")+"");
                        rsa.setAppRsaPublicExponent(maps.get("appRsaPublicExponent")+"");
                        rsa.setSysRsaPrivateExponent(maps.get("sysRsaPrivateExponent")+"");
                        dbManager.saveRsa(rsa);
                        dbManager.closeDB();
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 /*   *//**
     * 内存泄漏检测
     * @param context
     * @return
     *//*
    public static RefWatcher getRefWatcher(Context context) {
        ApplicationUtil application = (ApplicationUtil) context.getApplicationContext();
        return application.refWatcher;
    }*/


    public void addActivity(Activity activity){
        if(!activityList.contains(activity)){
            activityList.add(activity);
        }
    }

    //关闭所有活动的activity
    public void exit() {
        try {
            for (Activity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            onLowMemory();
            System.exit(0);
        }
    }

    /**
     * 关闭所有活动界面
     */
    public void deAllActivity() {
        try {
            for (Activity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    public void deThisActivity(Activity a){
        if(null != activityList && activityList.size() > 0){
            List<Activity> rmlist = new ArrayList<Activity>();
            for(Activity ac :activityList){
                if(a.getClass().getSimpleName().equals(ac.getClass().getSimpleName())){
                    rmlist.add(ac);
                }
            }
            activityList.removeAll(rmlist);
            rmlist = null;
        }
    }



    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
    public WindowManager.LayoutParams getWindowParams() {
        return windowParams;
    }


    public Rsa getRsa() {
        return rsa;
    }

    public void setRsa(Rsa rsa) {
        this.rsa = rsa;
    }

    public String getUserPsw() {
        return userPsw;
    }

    public void setUserPsw(String userPsw) {
        this.userPsw = userPsw;
    }

    public String getStuid() {
        return stuid;
    }

    public void setStuid(String stuid) {
        this.stuid = stuid;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserXm() {
        return userXm;
    }

    public void setUserXm(String userXm) {
        this.userXm = userXm;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getMateid() {
        return mateid;
    }

    public void setMateid(String mateid) {
        this.mateid = mateid;
    }

    public String getClassicname() {
        return classicname;
    }

    public void setClassicname(String classicname) {
        this.classicname = classicname;
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }


    public void setPapers(Paper papers) {
        this.papers = papers;
    }

    public Paper getPapers() {
        return papers;
    }

    public String getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(String behaviorId) {
        this.behaviorId = behaviorId;
    }


    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getCoursewareId() {
        return coursewareId;
    }

    public void setCoursewareId(String coursewareId) {
        this.coursewareId = coursewareId;
    }

    public PracticLearnBehavior getPracticLearnBehavior() {
        return practicLearnBehavior;
    }

    public void setPracticLearnBehavior(PracticLearnBehavior practicLearnBehavior) {
        this.practicLearnBehavior = practicLearnBehavior;
    }

    public ExerciseCard getExerciseCard() {
        return exerciseCard;
    }

    public void setExerciseCard(ExerciseCard exerciseCard) {
        this.exerciseCard = exerciseCard;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStuCode() {
        return stuCode;
    }

    public void setStuCode(String stuCode) {
        this.stuCode = stuCode;
    }
}
