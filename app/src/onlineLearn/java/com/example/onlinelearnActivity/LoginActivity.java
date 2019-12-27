package com.example.onlinelearnActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.util.NavigationBarCompat;
import com.example.util.NetWork;
import com.example.entity.LearnAccountEntity;
import com.example.entity.User;
import com.example.jdbc.DBManagerToUser;
import com.example.jdbc.DBManagerToUsers;
import com.example.jsonReturn.LoginReturn;
import com.example.onlinelearn.R;
import com.example.util.CodeSecurityUtil;
import com.example.util.CommonUtils;
import com.example.util.FinalStr;
import com.example.util.JsonUtil;
import com.example.util.SpUtils;
import com.example.util.StringToJson;
import com.example.util.SysUtil;
import com.example.view.captchalib.SwipeCaptchaView;
import com.example.view.captchalib.ValidateSeekBar;
import com.example.view.captchalib.ValidateSeekBar.ValidateSeekBarCallBack;
import com.example.view.smoothCheckBox.SmoothCheckBox;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 登录页面
 * */

public class LoginActivity extends BaseActivity {
    private String TAG = "LoginActivity";
    @BindView(R.id.tv)
    TextView tv; //app名字
    @BindView(R.id.login_user_edit)
    EditText loginName; //用户名
    @BindView(R.id.login_passwd_edit)
    EditText loginPsw;//密码
    @BindView(R.id.checkBox)
    SmoothCheckBox rememberPsw; //记住密码按钮
    @BindView(R.id.login_login_btn)
    Button loginBut; //登录按钮
    @BindView(R.id.fuwu)
    TextView fuwu; //服务
    @BindView(R.id.error_tv)
    TextView err_tv;//错误框
    @BindView(R.id.swipeCaptchaView1)
    SwipeCaptchaView mSwipeCaptchaView;//拼图
    @BindView(R.id.dragBar1)
    ValidateSeekBar mSeekBar;//拖拉seekbar
    @BindView(R.id.captchalib)
    RelativeLayout shadow;//遮盖层
    @BindView(R.id.checkbox_group)
    RelativeLayout checkbox_group;//记住密码组
    @BindView(R.id.captchalib_layout)
    LinearLayout captchalib_layout; //登录验证界面
    @BindView(R.id.icon_user)
    TextView icon_user;//记住密码组
    @BindView(R.id.icon_lock)
    TextView icon_lock; //登录验证界面

    private Boolean isRememberPsw = false;//是否记住了密码
    private DBManagerToUser dbManager; //sqlite操作对象
    private DBManagerToUsers dbManagers; //sqlite操作对象
    private User user = new User();
    private String loginType = "RSA";
    private List<User> listuser;
    private Boolean netStatus=true;
    private ScaleAnimation scaleAnimation;//缩放动画
    private int error_count =0;//错误次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        NavigationBarCompat.assistActivity(findViewById(android.R.id.content));
    }

    /**
     * 初始化
     * @param view
     */
    public void initViews(View view) {

        //设置app名字
        tv.setText(CommonUtils.getAppName(this));

        dbManagers = new DBManagerToUsers(getApplicationContext());

        //设置动画
        scaleAnimation = (ScaleAnimation) AnimationUtils
                .loadAnimation(LoginActivity.this, R.anim.scale_animation);
        //本地图片生成拼图
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.bg_captcha);
        mSwipeCaptchaView.setImageBitmap(bitmap);

        icon_user.setTypeface(iconfont);
        icon_lock.setTypeface(iconfont);
        //网络图片生成拼图
		/* Glide.with(this)
	            .load("http://www.investide.cn/data/edata/image/20151201/20151201180507_281.jpg")
	            .asBitmap()
	            .into(new SimpleTarget<Bitmap>() {
	                @Override
	                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
	                	Log.d("lklk", resource.toString());
	                    mSwipeCaptchaView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.captcha_bg));
	                    mSwipeCaptchaView.createCaptcha();
	                }
	            });*/

    }

    /**
     * 监听器管理
     */
    public void setListener(){

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
        findViewById(R.id.btnChange).setOnClickListener(new OnClickListener() {
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

                Toast.makeText(LoginActivity.this, "登录验证失败", Toast.LENGTH_SHORT).show();
                swipeCaptchaView.resetCaptcha();
                mSeekBar.setProgress(0);
                mSeekBar.refreshText();
            }
        });

        //seekbar回调监听
        mSeekBar.setValidateSeekBarCallBack(new ValidateSeekBarCallBack(){

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

                mSwipeCaptchaView.matchCaptcha();

            }


        });

        //加强点击效果
        checkbox_group.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {

                boolean ischeck =rememberPsw.isChecked();
                rememberPsw.setChecked(!ischeck, true);
            }

        });

        //取消loginType
        loginPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginType="";
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loginName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginType="";
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //监测软键盘的完成动作
        loginPsw.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE){

                    closeInput();//关闭软件盘
                    login();//登录操作
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 业务逻辑
     * @param mContext
     */
    public void doBusiness(Context mContext) {
       /* isFileExistInSD();  //同步磁盘文件及数据库的信息*/
        //是否有版本更新
        isVesionUpdate();
    }



    @OnClick({R.id.login_login_btn,R.id.fuwu})
    public void onClick(View view)  {
        Intent intent = null;
        switch (view.getId()) {
            //登录操作
            case R.id.login_login_btn:
                login();
                break;
//            //登录操作
            case R.id.fuwu:
                intent = new Intent(LoginActivity.this,FuWuActivity.class);
                startActivity(intent);
                break;
        }

    }
    /**
     * 登录操作
     */
    public void login(){
        //如果没有权限
        if (!isGetPermissions){
            checkPermissions(mNeedPermissions);
            return;
        }
        if(!loginType.equals("MD5")){
            user.setUserName(loginName.getText()+"");
            user.setUserPsw(loginPsw.getText()+"");
        }
        err_tv.setText("");
        if(SysUtil.isBlank(user.getUserName()) || SysUtil.isBlank(user.getUserPsw())){
            err_tv.setText("在线学习帐号或者密码不能\n为空，请输入后再登录！");
        }else{
            getResult();//登录
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
                user = listuser.get(0);
            }
        }else{
            user.setUserName(loginName.getText()+"");
            user.setUserPsw(loginPsw.getText()+"");
            if(null != listuser && listuser.size() > 0){
                dbManager.deleteUser();
            }
        }
        //点击登录 判断是否联网
        netStatus= NetWork.isNetworkConnected(LoginActivity.this);
        if (netStatus) {
            loginThread();
        }else {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("student_code", user.getUserName());
            map.put("student_password", user.getUserPsw());
            LearnAccountEntity entity=dbManagers.queryByMap(map);
            if (entity!=null) {//数据库有数据，则不用登录
                loginSuccess(entity);
            }else {
                err_tv.setText("登录错误，请重新输入或联网登录");

            }
        }
    }

    /**
     * 登录线程
     */
    private void loginThread(){
        showLoading();//显示加载框
        //调用接口方法
        Call<ResponseBody> call = serverApi.login(user.getUserName(),user.getUserPsw(),loginType);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) { //登录成功
                    try {
                        String result =response.body().string();
                        Log.d(JSON_TAG,"登录成功返回："+result);
                        //gson解析
                        LoginReturn loginReturn= JsonUtil.parserString(result, LoginReturn.class);
                        if(loginReturn!=null){
                            if (loginReturn.isSuccess()){
                                LearnAccountEntity learnAccountEntity=loginReturn.getLearnAccountEntity();
                                loginSuccess(learnAccountEntity);
                            }else{
                                loginFail(StringUtils.isNotBlank(loginReturn.getMsg()) ?
                                        loginReturn.getMsg(): "登录失败,\n请重试！");
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
        });
    }

    /**
     * 登录成功
     * @param learnAccountEntity
     */
    private void loginSuccess(LearnAccountEntity learnAccountEntity)  {


        Bundle bundle=new Bundle();
        bundle.putString("stuid",learnAccountEntity.getId());
        bundle.putInt("type", 1);
        //保存用户信息到缓存
        hdaplication.setStuid(learnAccountEntity.getId());
        hdaplication.setUserXm(learnAccountEntity.getStudentName());
        hdaplication.setUsername(loginName.getText()+"");

        //判断是否联网，联网则同步数据
        if (netStatus) {
            //添加记录到用户信息表
            User users=new User();
            users.setUserId(learnAccountEntity.getId());
            users.setUserName(user.getUserName());
            users.setUserXm(learnAccountEntity.getStudentName());
            users.setUserPsw(learnAccountEntity.getStudentPassword());

            dbManagers.saveUser(learnAccountEntity);
        }
        String psw=user.getUserPsw();
        if(!loginType.equals("MD5")){
            try {
                psw=CodeSecurityUtil.getMD5(loginPsw.getText()+"");
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
        startActivity(CourseActivity.class,bundle);

    }

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
            shadow.setOnTouchListener(new OnTouchListener(){
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
                loginPsw.setText("******");
                loginType = "MD5";
            }else{
                rememberPsw.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("登录","登录检验是否记住密码错误");
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

        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    /**
     * 本地文件列表与磁盘列表对应
     */
    private void isFileExistInSD() {
      /*  List<FileInfo> fileInfos = fileDao.queryAllFiles();
        List<FileInfo> deleteList = new ArrayList<>();
        for (FileInfo fileInfo : fileInfos) {
            //如果硬盘上不存在该文件
            if (fileInfo.getFile_finish()!=0 &&FileUtil.isExist(FinalStr.DOWNLOAD_PATH, fileInfo.getAttach_name()) == null) {
                deleteList.add(fileInfo);
            }
        }
        fileDao.deleteFiles(deleteList);

        List <FileInfo> F =fileDao.queryAllFiles();
        List <ThreadInfo> T = threadDAO.queryAllThreads();*/
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
