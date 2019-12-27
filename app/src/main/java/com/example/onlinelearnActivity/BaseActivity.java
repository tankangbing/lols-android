package com.example.onlinelearnActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Handler.IHandler;
import com.example.Handler.UIHandler;
import com.example.entity.FileInfo;
import com.example.onlinelearn.R;
import com.example.service.ApiService;
import com.example.service.ApiServiceSPAQ;
import com.example.service.DownloadService;
import com.example.util.ApplicationUtil;
import com.example.util.BarCompat;
import com.example.util.CommonUtils;
import com.example.util.DownLoadApkTool;
import com.example.util.FinalStr;
import com.example.util.SpUtils;
import com.example.util.StringToJson;
import com.example.view.dialog.MaterialDialog;
import com.example.view.numberProgress.NumberProgressBar;
/*import com.squareup.leakcanary.RefWatcher;*/


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 公共父类，所有activity继承
 */
public abstract class BaseActivity extends AppCompatActivity{

    protected String TAG = "BaseActivity";
    protected String JSON_TAG = "JSON";

    @BindView(R.id.toolbar)
    RelativeLayout toolbar; //toolbar
    @BindView(R.id.layout_center)
    LinearLayout layout_center; //加载的布局
    @BindView(R.id.toolbar_back)
    TextView toolbar_back; //返回按钮
    @BindView(R.id.toolbar_left_tv)
    TextView toolbar_left_tv; //左边text
    @BindView(R.id.toolbar_right)
    TextView toolbar_right; //右边的btn
    @BindView(R.id.toolbar_right2)
    TextView toolbar_right2; //右边第二个btn
    @BindView(R.id.toolbar_status)
    TextView toolbar_status; //沉浸式text
    //全局参数对象
    protected ApplicationUtil hdaplication;
    //上下文对象
    protected Context mContext = this;
    //事件处理器
    protected UIHandler handler = new UIHandler(Looper.getMainLooper());
    //网络请求框架
    protected Retrofit retrofit =null;
    protected Retrofit retrofitSPAQ =null;
    //网络请求接口
    protected ApiService serverApi;
    protected ApiServiceSPAQ serverApiSPAQ;
    private static OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS).build();

    //加载框
    protected MaterialDialog loadingDialog;
    //网络提示框
    protected MaterialDialog messageDialog;
    //版本更新框
    protected MaterialDialog versionUpdateDialog;
    //字符图片类型
    protected Typeface iconfont;
    //是否获取权限
    protected static boolean isGetPermissions =true;
    protected final int SUBMIT_TOPIC_SUCCESS =1;
    protected final int SUBMIT_ANSWER_SUCCESS =2;
    protected final int SUBMIT_LEVEL_ANSWER_SUCCESS =3;

    /**
     * [需要进行检测的权限数组]
     */
    protected String[] mNeedPermissions = {

            // 你需要申请的权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
//            Manifest.permission.READ_PHONE_STATE
    };

    private static final int PERMISSION_REQUEST_CODE = 0;

    //下载服务
    public static DownloadService downloadService =null;
    private DownLoadRecive mRecive;//接收下载回调
    private IntentFilter intentFilter;
    private DownLoadApkTool downLoadApkTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(savedInstanceState);
        setContentView(R.layout.activity_base);
        checkPermissions(mNeedPermissions);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        if (layout_center == null && R.layout.activity_base == layoutResID) {
            super.setContentView(R.layout.activity_base);

            layout_center =(LinearLayout)findViewById(R.id.layout_center);
            layout_center.removeAllViews();

        } else if (layoutResID != R.layout.activity_base) {
            View contentView = LayoutInflater.from(this).inflate(layoutResID, null);
            layout_center.addView(contentView, new ViewGroup.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            ButterKnife.bind(this);

            setToolbar();
            initViews(contentView);
            setListener();
            setHandler();
            doBusiness(this);
        }
    }

    /**
     * [设置回调]
     */
    private void setHandler() {
        handler.setHandler(new IHandler() {
            public void handleMessage(Message msg) {
                handler(msg);//有消息就提交给子类实现的方法
            }
        });
    }


    private MaterialDialog downLoadDialog;
    private NumberProgressBar progressBar;
    private TextView message_tv;
    /**
     * [处理消息]
     */
    protected void handler(Message msg){
        if (downLoadDialog ==null){
            View view =  LayoutInflater.from(this)
                    .inflate(R.layout.dialog_progress_layout, null);
            progressBar = (NumberProgressBar) view.findViewById(R.id.number_bar);
            message_tv = (TextView) view.findViewById(R.id.message_tv);
            downLoadDialog = new MaterialDialog(this)
                    .setCanceledOnTouchOutside(false)
                    .setContentView(view);
        }
        switch (msg.what) {

            case DownLoadApkTool.PREPARE_FINISH:


                downLoadDialog.show();

                break;

            case DownLoadApkTool.DOWNLOAD_SUCCESS:

                downLoadDialog.dismiss();
                break;
            case DownLoadApkTool.DOWNLOAD_FAIL:

                message_tv.setText((String)msg.getData().getString("msg"));
                showToast((String)msg.getData().getString("msg"));

//                new Handler().postDelayed(new Runnable(){
//                    public void run() {
//                         downLoadDialog.dismiss();
//                    }
//                }, 2000);

                break;
            case DownLoadApkTool.DOWNLOADING:
                progressBar.setProgress((int)msg.getData().getInt("progress"));
                break;
        }
    }

    /**
     * [全局信息初始化]
     */
    protected void initialize(Bundle savedInstanceState){
        hdaplication = (ApplicationUtil) getApplication();
        hdaplication.addActivity(this);

        //加载retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(FinalStr.ACCESSPRYPATH +"/")
                .client(client)
                .build();

        retrofitSPAQ =  new Retrofit.Builder()
                .baseUrl(FinalStr.SPAQPATH +"/")
                .client(client)
                .build();
        //生成接口文件
        serverApi = retrofit.create(ApiService.class);
        serverApiSPAQ =retrofitSPAQ.create(ApiServiceSPAQ.class);

        downLoadApkTool = new DownLoadApkTool(handler,serverApi,this);

        iconfont = Typeface.createFromAsset(getAssets(), "iconfont.ttf");

        Intent intent = new Intent(this, DownloadService.class);
        //绑定服务
        if (!CommonUtils.isServiceRunning(this,"com.example.service.DownloadService")){
            Log.d(TAG,"初始化服务");

            startService(intent);
            bindService(intent, conn, Service.BIND_AUTO_CREATE);
        }else{
            Log.d(TAG,"绑定服务");
            bindService(intent, conn, Service.BIND_AUTO_CREATE);
        }


        mRecive = new DownLoadRecive();
        intentFilter = new IntentFilter();
        //增加监听内容
        intentFilter.addAction(DownloadService.ACTION_UPDATE);
        intentFilter.addAction(DownloadService.ACTION_FINISHED);
        registerReceiver(mRecive, intentFilter);

    }

    /**
     * [设置toolbar]
     */
    protected void setToolbar(){

        toolbar_back.setTypeface(iconfont);


        //版本低于21时
        if (Build.VERSION.SDK_INT >=19)
        {
            toolbar_status.setHeight(BarCompat.getStatusBarHeight(this));
            toolbar_status.setVisibility(View.VISIBLE);
            getWindow().getDecorView().setFitsSystemWindows(true);
            //透明状态栏 @顶部
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏 @底部    这一句不要加，目的是防止沉浸式状态栏和部分底部自带虚拟按键的手机（比如华为）发生冲突，注释掉就好了
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }/*else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            getWindow().getDecorView().setFitsSystemWindows(true);
            //透明状态栏 @顶部
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏 @底部    这一句不要加，目的是防止沉浸式状态栏和部分底部自带虚拟按键的手机（比如华为）发生冲突，注释掉就好了
        }*/
    }

    /**
     * [初始化控件]
     *
     * @param view
     */
    protected abstract void initViews(final View view);

    /**
     * [绑定控件]
     *
     * @param resId
     *
     * @return
     */
    protected    <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }

    /**
     * [设置监听]
     */
    protected void setListener(){

    }

    /**
     * [业务操作]
     *
     * @param mContext
     */
    protected abstract void doBusiness(Context mContext);

    /**
     * [页面跳转]
     *
     * @param clz
     */
    protected void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this,clz));
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    protected void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    protected void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * [显示加载框]
     */
    public void showLoading(){
        if (loadingDialog ==null){
            loadingDialog = new MaterialDialog(this)
                    .setCanceledOnTouchOutside(false)
                    .setBackgroundResource(0)
                    .setContentView(R.layout.dialog_loading);

        }
        loadingDialog.show();

    }

    /**
     * [关闭加载]
     */
    public void closeLoading(){
        loadingDialog.dismiss();
    }

    /**
     * [简化Toast]
     * @param msg
     */
    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    public void showToast(@StringRes int textId) {
        Toast.makeText(this,textId,Toast.LENGTH_SHORT).show();
    }



    /**
     *  [设置返回按钮显示]
     */
    public void setToolbar_backShow(boolean flag) {
        if (flag)
            toolbar_back.setVisibility(View.VISIBLE);
        else
            toolbar_back.setVisibility(View.INVISIBLE);
    }
    /**
     *  [设置返回按钮显示]
     */
    public void setToolbar_back(String text) {
        toolbar_back.setText(text);
    }
    public void setToolbar_back(@StringRes int textId) {
        toolbar_back.setText(textId);
    }
    /**
     *  [设置左边title]
     * @param text
     */
    public void setActivityLeftTitle(String text) {
        toolbar_left_tv.setText(text);
    }
    public void setActivityLeftTitle(@StringRes int textId) {
        toolbar_left_tv.setText(textId);
    }
    public void setActivityLeftTitleShow(boolean flag) {
        if (flag)
            toolbar_left_tv.setVisibility(View.VISIBLE);
        else
            toolbar_left_tv.setVisibility(View.INVISIBLE);
    }

    public void setToobbarShow(boolean flag){
        if (flag)
            toolbar.setVisibility(View.VISIBLE);
        else
            toolbar.setVisibility(View.GONE);
    }
    /**
     *  [设置右边title]
     * @param text
     */
    public void setActivityRightBtn(String text) {
        toolbar_right.setText(text);
    }
    public void setActivityRightBtn(@StringRes int textId) {
        toolbar_right.setText(textId);
    }
    public void setActivityRightBtnSize(int size) {
        toolbar_right.setTextSize(size);
    }
    public void setActivitRightBtnShow(boolean flag) {
        if (flag)
            toolbar_right.setVisibility(View.VISIBLE);
        else
            toolbar_right.setVisibility(View.INVISIBLE);
    }

    /**
     *  [设置右边第二个title]
     * @param text
     */
    public void setActivityRight2Btn(String text) {
        toolbar_right2.setText(text);
    }
    public void setActivityRight2Btn(@StringRes int textId) {
        toolbar_right2.setText(textId);
    }
    public void setActivityRight2BtnSize(int size) {
        toolbar_right2.setTextSize(size);
    }
    public void setActivitRight2BtnShow(boolean flag) {
        if (flag)
            toolbar_right2.setVisibility(View.VISIBLE);
        else
            toolbar_right2.setVisibility(View.INVISIBLE);
    }

    /**
     * [检查权限]
     * @param permissions
     */
    protected void checkPermissions(String... permissions) {
        List<String> needRequestPermissionList = findDeniedPermissions(permissions);
        if (null != needRequestPermissionList
                && needRequestPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissionList.toArray(
                            new String[needRequestPermissionList.size()]),
                    PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * [获取权限集中需要申请权限的列表]
     *
     * @param permissions
     * @return
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                needRequestPermissionList.add(perm);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 检测是否所有的权限都已经授权
     *
     * @param grantResults
     * @return
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示提示信息
     *
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("你需要开启权限才能使用");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 显示提示框
     */
    public void showMessageDialog(String message){
        if (messageDialog ==null){
            messageDialog = new MaterialDialog(this)
                    .setCanceledOnTouchOutside(false)
                    .setTitle("提示")
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            messageDialog.dismiss();
                        }
                    });
        }
        messageDialog.setMessage(message);
        messageDialog.show();
    }

    /**
     * [发送消息]
     * @param flag
     * @param b
     */
    public void sendMessage(int flag,Bundle b){
        Message msg = new Message();
        msg.what = flag;
        if(null != b){
            msg.setData(b);
        }
        handler.sendMessage(msg);
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    /**
     *关闭软键盘
     */
    protected void closeInput(){

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    }


    /**
     * [关闭更新框]
     */
    protected void closeUpdateDialog(){
        versionUpdateDialog.dismiss();
    }


    /**
     * [跳转更新]
     */
    public void downloadApk(String url) {
        closeUpdateDialog();
        DownLoadApkTool downLoadApkTool =new DownLoadApkTool(handler,serverApi,this);
        downLoadApkTool.onDownloadPrepare(url, FinalStr.DOWNLOAD_APK_PATH, FinalStr.APK_NAME);

    }

    /**
     * [ServiceConnection与服务的连接]
     */
    protected  ServiceConnection conn = new ServiceConnection() {
        /**
         * 与服务器端交互的接口方法 绑定服务的时候被回调，在这个方法获取绑定Service传递过来的IBinder对象，
         * 通过这个IBinder对象，实现宿主和Service的交互。
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "绑定成功调用：onServiceConnected");
            // 获取Binder
            DownloadService.LocalBinder binder = (DownloadService.LocalBinder) service;
            downloadService = binder.getService();
        }
        /**
         * 当取消绑定的时候被回调。但正常情况下是不被调用的，它的调用时机是当Service服务被意外销毁时，
         * 例如内存的资源不足时这个方法才被自动调用。
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            downloadService = null;
        }
    };

    /**
     * 广播接收
     */
    protected class DownLoadRecive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {  // 更新进度

                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
                Log.d(TAG,fileInfo.toString());
            }
            else if (DownloadService.ACTION_FINISHED.equals(intent.getAction())){   //下载完成

                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
                //删除正在下载的任务
                downloadService.removeDownloading(fileInfo);
                //并且输出下载完成框
                Toast.makeText(BaseActivity.this, fileInfo.getFile_name() + "下载完毕", Toast.LENGTH_SHORT).show();
                //下载成功回调
                downloadSuccess(fileInfo);

            }
        }

    }

    /**
     * 下载成功回调
     */
    protected void downloadSuccess(FileInfo fileInfo){}


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        //删除当前页面
        hdaplication.deThisActivity(this);
        unregisterReceiver(mRecive);
        unbindService(conn);
       /* RefWatcher refWatcher = ApplicationUtil.getRefWatcher(this);
        refWatcher.watch(this);*/

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG,"onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"onSaveInstanceState");
    }
    /**
     * [返回键操作]
     */
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    /**
     * [获取权限回调]
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!verifyPermissions(grantResults)) {
                showMissingPermissionDialog();
                isGetPermissions =false;
            }
            else{
                isGetPermissions =true;
            }
        }
    }

    /**
     * [公共按钮操作]
     */
    @OnClick({R.id.toolbar_back})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.toolbar_back:
                finish();
                break;
        }
    }



    public ApiService getServerApi() {
        return serverApi;
    }

   /* *//**
     * 屏幕旋转时调用此方法
     *//*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            BarCompat.setNotFullScreen(this);
        }
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            BarCompat.setFullScreen(this);
        }
    }*/

    /**
     * [是否有更新版本]
     */
    protected void isVesionUpdate(){
        //写入当前版本
        SpUtils.putString(this,SpUtils.OLD_VERSION,CommonUtils.getVersionName(this));
        SpUtils.putString(this,SpUtils.NEW_VERSION,CommonUtils.getVersionName(this));

        //调用接口方法
        Call<ResponseBody> call = serverApi.search_version(CommonUtils.getVersionName(this), FinalStr.SYSTEM_CODE);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        String result =response.body().string();
                        Log.d(JSON_TAG,"版本更新返回："+result);

                        Map<String, Object> maps = StringToJson.parseJSON2Map(result);
                        if ((boolean)maps.get("success")) {

                            if(maps.containsKey("isHaveVersion") && "Y".equals(maps.get("isHaveVersion").toString())){

                                //写入最新版本
                                SpUtils.putString(BaseActivity.this,SpUtils.NEW_VERSION,(String) maps.get("version"));
                                SpUtils.putString(BaseActivity.this,SpUtils.URL,(String) maps.get("url"));
                                SpUtils.putString(BaseActivity.this,SpUtils.MSG,(String) maps.get("content"));
                                SpUtils.putString(BaseActivity.this,SpUtils.RANK,(String) maps.get("rank"));

                                String msg =maps.get("content").toString();
                                //弹框
                                showUpdateDialog(String.valueOf(maps.get("url")),maps.get("rank").toString(),msg);

                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    /**
     * [显示更新框]
     */
    protected void showUpdateDialog(final String url,String rank,String msg){


        View view =  LayoutInflater.from(this)
                .inflate(R.layout.version_dialog, null);

        LinearLayout L1 = (LinearLayout) view.findViewById(R.id.L1);
        switch (rank){
            case "0":
                L1.setVisibility(View.GONE);
                break;
        }
        TextView update = (TextView) view.findViewById(R.id.alertmsgSure);
        TextView cancle = (TextView) view.findViewById(R.id.alertmsgCancel);
        TextView old_tv = (TextView) view.findViewById(R.id.old_tv);
        TextView new_tv = (TextView) view.findViewById(R.id.new_tv);
        TextView exitcontent = (TextView) view.findViewById(R.id.exitcontent);
        exitcontent.setText(msg);
        old_tv.setText(SpUtils.getString(this,SpUtils.OLD_VERSION));
        new_tv.setText(SpUtils.getString(this,SpUtils.NEW_VERSION));
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadApk(url);
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeUpdateDialog();
            }
        });
        versionUpdateDialog = new MaterialDialog(this)
                .setCanceledOnTouchOutside(false)
                .setContentView(view);

        try{
            versionUpdateDialog.show();
        }catch (Exception e){
            //跳过登录页面的时候会报错
        }

    }

}
