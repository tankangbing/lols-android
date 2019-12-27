package com.example.onlinelearnActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.onlinelearn.R;
import com.example.util.BarCompat;
import com.example.util.FileUtil;
import com.example.util.FinalStr;
import com.example.util.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends BaseActivity {
    private final String TAG ="SplashActivity";
    private final int LOAD_FILE_SUCCESS =1; //加载成功返回
    private final int LOAD_FILE_FAIL =2; //加载失败返回


    private ImageView iv_splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 判断是否是第一次开启应用
        boolean isFirstOpen = SpUtils.getBoolean(this, FinalStr.FIRST_OPEN);

        //第一次启动进入功能引导页
        if (!isFirstOpen && FinalStr.IS_GUIDE) {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 否则正常显示启动屏
        setContentView(R.layout.activity_splash);


    }

    private void enterHomeActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 下载图片
     */
    private void downloadImage(String url) {

        //调用下载文档接口
        Call<ResponseBody> call = serverApi.downloadFile(url);
        // 异步下载文件.
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    //开启线程写入sd卡
                    loadFile(response.body(), FinalStr.DOWNLOAD_PATH,"AA.jpg");

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //两秒跳至登录页
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        enterHomeActivity();
                    }
                }, 2000);
            }
        });
    }

    /**
     * 将文件写入SD卡
     * @param responseBody
     * @param path
     * @param fileName
     */
    private void loadFile(final ResponseBody responseBody , final String path, final String fileName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                File writeResult = FileUtil.writeResponseBodyToDisk(responseBody, path, fileName);
                if (writeResult!=null){
                    sendMessage(LOAD_FILE_SUCCESS,null);
                }else{
                    sendMessage(LOAD_FILE_FAIL,null);
                }
            }
        }).start();
    }

    protected void handler(Message msg) { //我们可以处理数据消息了
        switch (msg.what) {

            case LOAD_FILE_SUCCESS:

                break;

            case LOAD_FILE_FAIL:


                break;
        }
    }

    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        //隐藏toolbar
        toolbar.setVisibility(View.GONE);
        BarCompat.compat(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected void initViews(View view) {
        iv_splash =(ImageView)findViewById(R.id.iv_splash);
    }

    @Override
    protected void doBusiness(Context mContext) {

        //调用接口方法
        Call<ResponseBody> call = serverApi.getStartPagePicturePath();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求视频信息成功

                    try {
                        String result =response.body().string();
                        Log.d(TAG,result+" result");
                        JSONObject obj =new JSONObject(result);
                        if (obj.getBoolean("success")){
                            String path =obj.getString("startPagePicturePath");
                            Log.d(TAG,path+" result");
                            Glide.with(SplashActivity.this)
                                    .load(path)
                                    .centerCrop()
                                    .into(iv_splash);
                          /*  downloadImage(path);*/
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e){
                        //  java.lang.IllegalArgumentException: You cannot start a load for a destroyed activity
                    }
                }
                //两秒跳至登录页
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        enterHomeActivity();
                    }
                }, 3000);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //两秒跳至登录页
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        enterHomeActivity();
                    }
                }, 2000);
            }
        });
    }
}
