package com.example.onlinelearnActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entity.LearnAccountEntity;
import com.example.entity.User;
import com.example.jdbc.DBManagerToUser;
import com.example.jsonReturn.GetCaptchaReturn;
import com.example.jsonReturn.LoginReturn;
import com.example.jsonReturn.LoginSpaqReturn;
import com.example.onlinelearn.R;

import com.example.util.CommonUtils;
import com.example.util.FileUtil;

import com.example.util.FinalStr;
import com.example.util.HttpUtil;
import com.example.util.JsonUtil;
import com.example.util.SysUtil;
import com.example.view.captchalib.SwipeCaptchaView;
import com.example.view.captchalib.ValidateSeekBar;
import com.example.view.dialog.AlertDialog;
import com.example.view.dialog.MaterialDialog;
import com.example.view.smoothCheckBox.SmoothCheckBox;



import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * Created by ysg on 2017/8/29.
 */

public class LoginActivity extends BaseActivity {

    private Retrofit retrofit =null;//网络请求框架

    EditText loginName; //用户名
    EditText loginPsw;//密码
    EditText login_spaq_passwd_edit2;//验证码
    SmoothCheckBox rememberPsw; //记住密码按钮
    Button loginBut; //登录按钮
    TextView err_tv;//错误框
    TextView login_hqyzm;//验证码

    TextView version_name;//版本号
    SwipeCaptchaView mSwipeCaptchaView;//拼图
    ValidateSeekBar mSeekBar;//拖拉seekbar
    RelativeLayout shadow;//遮盖层
    RelativeLayout checkbox_group;//记住密码组
    LinearLayout captchalib_layout; //登录验证界面
    GetCaptchaReturn getCaptchaReturn;
    private MaterialDialog loadingDialog =null;//加载view

    private Boolean isRememberPsw = false;//是否记住了密码
    private DBManagerToUser dbManager; //sqlite操作对象
    private User user = new User();
    private String loginType = "RSA";
    private String type = "0";//登录类型，0手动 1自动
    private List<User> listuser;
    private ScaleAnimation scaleAnimation;//缩放动画
    private int error_count =0;//错误次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_spaq);

        toolbar_status.setVisibility(View.GONE);
        SharedPreferences preferences=getSharedPreferences("user",Context.MODE_PRIVATE);
        String stuid=preferences.getString("stuid", "");
        String userXm=preferences.getString("userXm", "");
        String username=preferences.getString("username", "");
        String stucode=preferences.getString("stucode", "");


        if(!stuid.equals("")&&!userXm.equals("")&&!username.equals("")){
            hdaplication.setStuid(stuid);
            hdaplication.setUserXm(userXm);
            hdaplication.setUsername(username);
            hdaplication.setStuCode(stucode);
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this,CourseActivity.class);
            startActivity(intent);
            finish();
        }
        if (!username.equals("")){
            loginName.setText(username);
        }
        updateUi();



    }



    @SuppressLint("HandlerLeak")
    protected void initViews(View view) {
        toolbar.setVisibility(View.GONE);
        login_hqyzm=(TextView)findViewById(R.id.login_hqyzm);
        loginName=(EditText)findViewById(R.id.login_spaq_user_edit);
        loginPsw=(EditText)findViewById(R.id.login_spaq_passwd_edit);
        login_spaq_passwd_edit2=(EditText)findViewById(R.id.login_spaq_passwd_edit2);
        rememberPsw=(SmoothCheckBox)findViewById(R.id.login_spaq_checkBox);
        loginBut=(Button)findViewById(R.id.login_spaq_btn);
        err_tv=(TextView)findViewById(R.id.login_spaq_error_tv);
        /*version_name=(TextView)findViewById(R.id.version_name);
        version_name.setText(CommonUtils.getVersionName(this));*/

        mSwipeCaptchaView=(SwipeCaptchaView)findViewById(R.id.swipeCaptchaView1);
        mSeekBar=(ValidateSeekBar)findViewById(R.id.dragBar1);
        shadow=(RelativeLayout)findViewById(R.id.captchalib);
        checkbox_group=(RelativeLayout)findViewById(R.id.checkbox_group);
        captchalib_layout=(LinearLayout)findViewById(R.id.captchalib_layout);
        //new倒计时对象,总共的时间,每隔多少秒更新一次时间
        final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(120000,1000);
        //给Button设置点击时间,触发倒计时
        login_hqyzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=loginName.getText()+"";
                if (name!=null&&!name.equals("")&&name.length()==11){
                    loginGetCaptcha();
                    myCountDownTimer.start();
                }else {
                    Toast.makeText(LoginActivity.this, "请输入正确帐号再获取验证码", Toast.LENGTH_LONG).show();
                }
            }
        });
        //加载retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(FinalStr.ACCESSPRYPATH +"/")
                .build();


        //设置动画
        scaleAnimation = (ScaleAnimation) AnimationUtils
                .loadAnimation(LoginActivity.this, R.anim.scale_animation);
        //本地图片生成拼图
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.bg_captcha);
        mSwipeCaptchaView.setImageBitmap(bitmap);

        setListener();
    }

    /**
     * 监听器管理
     */
    public  void setListener(){

        //记住密码按钮监听
        rememberPsw.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {

                if(isChecked){
                    isRememberPsw = true;
                }else{
                    isRememberPsw = false;
                }
            }
        });

        //拼图refresh按钮监听
        findViewById(R.id.btnChange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeCaptchaView.createCaptcha();
                mSeekBar.setEnabled(true);
                mSeekBar.setProgress(0);
            }
        });

        //拼图回调
        mSwipeCaptchaView.setOnCaptchaMatchCallback(new SwipeCaptchaView.OnCaptchaMatchCallback() {
            @Override
            public void matchSuccess(SwipeCaptchaView swipeCaptchaView) {
                Toast.makeText(LoginActivity.this, "登录验证成功", Toast.LENGTH_SHORT).show();
                //swipeCaptcha.createCaptcha();
                mSeekBar.setEnabled(false);
                shadow.setVisibility(View.INVISIBLE);
                captchalib_layout.setVisibility(View.INVISIBLE);
                error_count=0;

            }

            @Override
            public void matchFailed(SwipeCaptchaView swipeCaptchaView) {
                Log.d("zxt", "matchFailed() called with: swipeCaptchaView = [" + swipeCaptchaView + "]");
                Toast.makeText(LoginActivity.this, "登录验证失败", Toast.LENGTH_SHORT).show();
                swipeCaptchaView.resetCaptcha();
                mSeekBar.setProgress(0);
                mSeekBar.refreshText();
            }
        });

        //seekbar回调监听
        mSeekBar.setValidateSeekBarCallBack(new ValidateSeekBar.ValidateSeekBarCallBack(){

            @Override
            public void onProgressChangedCallBack(SeekBar seekbar,
                                                  int progress, boolean arg2) {
                mSwipeCaptchaView.setCurrentSwipeValue(progress);

            }

            @Override
            public void onStartTrackingTouchCallBack(SeekBar seekbar) {
                //随便放这里是因为控件
                mSeekBar.setMax(mSwipeCaptchaView.getMaxSwipeValue());

            }

            @Override
            public void onStopTrackingTouchCallBack(SeekBar seekbar) {
                Log.d("zxt", "onStopTrackingTouch() called with: seekBar = [" + seekbar + "]");
                mSwipeCaptchaView.matchCaptcha();

            }


        });

        //加强点击效果
        checkbox_group.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {

                boolean ischeck =rememberPsw.isChecked();
                rememberPsw.setChecked(!ischeck, true);
            }

        });
    }

    @Override
    protected void doBusiness(Context mContext) {

    }

    public void login(View view)  {
        switch (view.getId()) {
            //登录操作
            case R.id.login_spaq_btn:
                user.setUserName(loginName.getText()+"");
                user.setUserPsw(loginPsw.getText()+"");
                err_tv.setText("");
                if(SysUtil.isBlank(user.getUserName()) || SysUtil.isBlank(user.getUserPsw()) || SysUtil.isBlank(login_spaq_passwd_edit2.getText()+"")){
                    createAlert("在线学习帐号密码或者验证码不能\n为空，请输入后再登录！","#ffffff",R.layout.custom_alert_err,R.id.alert_err_txt);
                }else{
                    getResult();//登录
                }
                break;
        }
    }

    public void click(View v){
        if (v.getId()==R.id.login_spaq_xyzc){
            Intent intent= new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(FinalStr.SPAQPATH+"/wap/register?code=1");
            intent.setData(content_url);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 离线登录/在线登录
     */
    public void getResult()  {
        //是否记住了密码
        if(isRememberPsw){ //记住密码
            //查看用户表里是否有数据
            if(null != listuser && listuser.size() > 0){
                //loginType = "MD5";
                user = listuser.get(0);
            }
        }else{
            user.setUserName(loginName.getText()+"");
            user.setUserPsw(loginPsw.getText()+"");
            if(null != listuser && listuser.size() > 0){
                dbManager.deleteUser();
            }
        }
        loginThread();
    }
    LearnAccountEntity learnAccountEntity;
    /**
     * 登录线程
     */
    private void loginThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("mobile", user.getUserName());
                map.put("password", user.getUserPsw());
                map.put("code", login_spaq_passwd_edit2.getText()+"");
                String result = HttpUtil.getResultByPost(FinalStr.SPAQPATH+"/app/user/appLogin",
                        map,LoginActivity.this,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                    sendMessage(0);
                }else{
                    LoginSpaqReturn loginReturn=JsonUtil.parserString(result, LoginSpaqReturn.class);
                    if (loginReturn.getStatus().equals("200")){//校验成功
                        studentCode=loginReturn.getStudentCode();
                        systemCode=loginReturn.getSystemCode();
                        loginSpaq();
                    /*learnAccountEntity=loginReturn.getLearnAccountEntity();
                    if (type.equals("0")){
                    }else if(type.equals("1")){
                        sendMessage(1);
                    }*/
                    }else if (loginReturn.getStatus().equals("300")){//校验失败
                        tsmsg=loginReturn.getMsg();
                        sendMessage(2);
                    }
                    //learnClassEntityList=courseClassReturn.getLearnClassEntityList();
                }
            }
        }).start();

        /*TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        //调用接口方法
        Call<ResponseBody> call = serverApi.login(user.getUserName(),user.getUserPsw(),loginType,
                StringUtils.isNotBlank(TelephonyMgr.getDeviceId())?TelephonyMgr.getDeviceId():"0");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) { //登录成功
                    try {
                        String result =response.body().string();
                        Log.d("XX",result+" result");
                        //gson解析
                        LoginReturn loginReturn= JsonUtil.parserString(result, LoginReturn.class);
                        if(loginReturn!=null){
                            if(!SysUtil.isBlank(loginReturn.getStatus()) &&  "1".equals(loginReturn.getCode())){
                                Bundle bundle=new Bundle();
                                if("200".equals(loginReturn.getStatus())){
                                    LearnAccountEntity learnAccountEntity=loginReturn.getLearnAccountEntity();
                                    loginSuccess(learnAccountEntity);

                                }else if("300".equals(loginReturn.getStatus())){

                                    loginFail(StringUtils.isNotBlank(loginReturn.getMsg()) ?
                                            loginReturn.getMsg(): "验证信息错误,\n请输入正确的信息！");
                                }else{
                                    loginException();
                                }
                            }else{
                                loginException();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                closeLoading();//关闭加载框
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                err_tv.setText("登录异常，请检查你的网\n络连接或者联系我们！");
                closeLoading();//关闭加载框
            }
        });*/
    }

    String studentCode="";
    String systemCode="";
    public void loginSpaq(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("studentCode", studentCode);
                map.put("SYSTEM_CODE", systemCode);
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?loginSpaq",
                        map,LoginActivity.this,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                    sendMessage(0);
                }else{
                    LoginReturn LoginReturn=JsonUtil.parserString(result, LoginReturn.class);
                    //CourseClassReturn courseClassReturn=JsonUtil.parserString(result, CourseClassReturn.class);
                    if (LoginReturn!=null){
                        learnAccountEntity=LoginReturn.getLearnAccountEntity();
                        if (learnAccountEntity!=null){
                            sendMessage(1);
                        }else {
                            tsmsg="无法找到该用户信息，请重新登录";
                            sendMessage(2);
                        }
                    }else {
                        tsmsg="无法找到该用户信息，请重新登录";
                        sendMessage(2);
                    }
                    // sendMessage(1);
                }
            }
        }).start();
    }



    /**
     * 登录成功
     * @param learnAccountEntity
     */
    /*private void loginSuccess(LearnAccountEntity learnAccountEntity)  {

        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,CourseActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("stuid",learnAccountEntity.getId());
        bundle.putInt("type", 1);
        //保存用户信息到缓存
        hdaplication.setStuid(learnAccountEntity.getId());
        hdaplication.setUserXm(learnAccountEntity.getStudentName());
        hdaplication.setUsername(loginName.getText()+"");

        String psw=loginPsw.getText()+"";
        if(!loginType.equals("MD5")){
            try {
                psw= CodeSecurityUtil.getMD5(loginPsw.getText()+"");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //是否需要记住密码
        if(isRememberPsw){
            if(null != listuser && listuser.size() > 0){
                dbManager.updateUser(1, loginName.getText()+"",psw );
            }else{
                user.setId(1);
                user.setUserName(loginName.getText()+"");
                user.setUserPsw(psw);
                dbManager.saveUser(user);
            }
        }
        intent.putExtras(bundle);
        startActivity(intent);

    }*/

    /**
     * 登录失败
     */
    private void loginFail(String msg){
        if(null != listuser && listuser.size() > 0){
            dbManager.deleteUser();
            loginType = "RSA";
        }
        err_tv.setText(msg);
        error_count++;
        if(error_count>=3){
            loginValidate();//登录验证
        }
    }

    /**
     * 登录错误
     */
    private void loginException(){
        err_tv.setText("获取数据异常！");

    }

    /**
     * 登录验证
     */
    private void loginValidate(){
        if(shadow==null){
            int width = captchalib_layout.getMeasuredWidth();  //屏幕宽度
            ViewGroup.LayoutParams lp;
            lp= captchalib_layout.getLayoutParams();
            lp.height=width;
            captchalib_layout.setLayoutParams(lp);  //设置正方形
            //禁止点击下层
            shadow.setOnTouchListener(new View.OnTouchListener(){
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    return true;
                }
            });
        }
        mSeekBar.setProgress(0);
        mSeekBar.setEnabled(true);
        mSwipeCaptchaView.createCaptcha();
        shadow.setVisibility(View.VISIBLE);
        captchalib_layout.startAnimation(scaleAnimation);

    }

    /**
     * 加载框
     */
    public void showLoading(){
        if (loadingDialog==null){
            loadingDialog = new MaterialDialog(this)
                    .setBackgroundResource(0)
                    .setContentView(R.layout.dialog_loading);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        loadingDialog.show();
    }

    /**
     * 关闭加载
     */
    public void closeLoading(){
        loadingDialog.dismiss();
    }

    AlertDialog ad = null;
    private void createAlert(String title, String color, int layout, int id){
        ad=new AlertDialog(LoginActivity.this,title,color,layout,id);
        ad.show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            finish();
        }
        return false;
    }

    public void isAutomaticLogin(){
        try {
            dbManager = new DBManagerToUser(getApplicationContext());
            listuser = dbManager.queryUser();
            if(null != listuser && listuser.size() > 0){
                rememberPsw.setChecked(true);
                user = listuser.get(0);
                loginName.setText("");
                loginName.setText(user.getUserName());
                loginPsw.setText("");
                loginPsw.setText(user.getUserPsw());
                // loginType = "MD5";
                type="1";
            }else{
                rememberPsw.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("登录","登录检验是否记住密码错误");
        }
    }

    @SuppressWarnings("finally")
    public boolean isPopBugDialog(){
        boolean b = false;
        try {
            File file = new File(FinalStr.LOGPATH+ FinalStr.LOGINSUBMITBUG);
            if(!file.exists()) b = false;
            else{
                String str = FileUtil.readFileByChars(file);
                if("true".equals(str)){
                    b = true;
                }else{
                    b = false;
                }
            }
        } catch (Exception e) {
            b = false;
        }finally{
            return b;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 200://提交dug后返回
                isAutomaticLogin();
                break;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        isAutomaticLogin();
    }

    @Override
    protected void onResume() {
        //JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    //获取验证码
    public void getYzm(View v){

    }

    String tsmsg="";
    //获取验证码
    public void loginGetCaptcha(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("mobile", loginName.getText()+"");
                map.put("em", "2");
                String result = HttpUtil.getResultByPost(FinalStr.SPAQPATH+"/mobileCode/loginGetCaptcha",
                        map,LoginActivity.this,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                    sendMessage(0);
                }else{
                    getCaptchaReturn=JsonUtil.parserString(result, GetCaptchaReturn.class);
                    //CourseClassReturn courseClassReturn=JsonUtil.parserString(result, CourseClassReturn.class);
                    if (getCaptchaReturn!=null){
                        tsmsg=getCaptchaReturn.getMsg();
                        sendMessage(3);
                    }
                    // sendMessage(1);
                }
            }
        }).start();
    }

    private Handler handler;//异步刷新组件
    public void updateUi(){
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: //无法加载数据
                        Toast.makeText(LoginActivity.this,"无法加载数据,请检查你的网络连接或者联系我们！",
                                Toast.LENGTH_LONG).show();
                        break;
                    case 1: //加载数据
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this,CourseActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("stuid",learnAccountEntity.getId());
                        bundle.putInt("type", 1);
                        //保存用户信息到缓存
                        hdaplication.setStuid(learnAccountEntity.getId());
                        hdaplication.setStuCode(learnAccountEntity.getStudentCode());
                        hdaplication.setUserXm(learnAccountEntity.getStudentName());
                        hdaplication.setUsername(loginName.getText()+"");
                        SharedPreferences preferences=getSharedPreferences("user",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("stuid", learnAccountEntity.getId());
                        editor.putString("stucode",learnAccountEntity.getStudentCode());
                        editor.putString("userXm", learnAccountEntity.getStudentName());
                        editor.putString("username", loginName.getText()+"");
                        editor.commit();

                        String psw=loginPsw.getText()+"";
                        /*if(!loginType.equals("MD5")){
                            try {
                                psw= CodeSecurityUtil.getMD5(loginPsw.getText()+"");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }*/
                        //是否需要记住密码
                        if(isRememberPsw){
                            if(null != listuser && listuser.size() > 0){
                                dbManager.updateUser(1, loginName.getText()+"",psw );
                            }else{
                                user.setId(1);
                                user.setUserName(loginName.getText()+"");
                                user.setUserPsw(psw);
                                dbManager.saveUser(user);
                            }
                        }
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        break;
                    case 2:
                        Toast.makeText(LoginActivity.this, tsmsg, Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        if (getCaptchaReturn.getCaptcha()!=null&&!getCaptchaReturn.getCaptcha().equals("")){
                            login_spaq_passwd_edit2.setText(getCaptchaReturn.getCaptcha());
                        }
                        Toast.makeText(LoginActivity.this, tsmsg, Toast.LENGTH_LONG).show();
                        break;

                }
                super.handleMessage(msg);
            }
        };
    }

    private void sendMessage(int str){
        Message msg = new Message();
        msg.what = str;
        LoginActivity.this.handler.sendMessage(msg);
    }

    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            login_hqyzm.setClickable(false);
            login_hqyzm.setText(l/1000+"s");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            login_hqyzm.setText("重新获取验证码");
            //设置可点击
            login_hqyzm.setClickable(true);
        }
    }
    /**
     * 重写返回按钮
     */
    public void onBackPressed() {
        //返回home,不销毁
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);

        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(mHomeIntent);

    }
}
