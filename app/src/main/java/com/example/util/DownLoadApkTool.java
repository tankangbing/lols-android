package com.example.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.example.Handler.UIHandler;
import com.example.service.ApiService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownLoadApkTool {

    public static final int PREPARE_FINISH =0;
    public static final int DOWNLOAD_SUCCESS=1;
    public static final int DOWNLOAD_FAIL=2;
    public static final int DOWNLOADING=3;

    private final String TAG ="DownLoadTool";
    private UIHandler handler ;
    private ApiService serverApi;
    private Context context;

    public DownLoadApkTool(UIHandler handler, ApiService serverApi, Context context) {

        this.handler =handler;
        this.serverApi =serverApi;
        this.context =context;

    }

    /**
     * 下载app线程
     */
    public void onDownloadPrepare(String url,final String path ,final String FileName) {

        //调用下载文档接口
        Call<ResponseBody> call = serverApi.downloadFile(url);
        // 异步下载文件.
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //成功操作

                    sendMessage(PREPARE_FINISH,null);
                    //显示下载框
                    //开启线程写入sd卡
                    loadFile(response.body(),path,FileName);

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //失败操作
                Bundle bundle =new Bundle();
                bundle.putString("msg","无法获取下载资源.");
                sendMessage(DOWNLOAD_FAIL,bundle);
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
                File writeResult = writeResponseBodyToDisk(responseBody, path, fileName);
                if (writeResult!=null){
                    sendMessage(DOWNLOAD_SUCCESS,null);
                    installAPK(context,writeResult);
                }else{
                    Bundle bundle =new Bundle();
                    bundle.putString("msg","下载过程失败.");
                    sendMessage(DOWNLOAD_FAIL,bundle);
                }
            }
        }).start();
    }

    /**
     * Retrofit 网络框架写入文件
     * @param responseBody
     * @return
     */
    private File writeResponseBodyToDisk(ResponseBody responseBody , String path, String fileName) {


        InputStream is = null;
        byte[] buf = new byte[4096];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = responseBody.byteStream();
            long total = responseBody.contentLength();
            File filePath = new File(path);
            //如果文件夹不存在，则创建
            if (!filePath.exists()){
                filePath.mkdirs();
            }
            File mFile = new File(path, fileName);
            if (!mFile.exists()){
                boolean mkdir = mFile.createNewFile();
            }
            fos = new FileOutputStream(mFile);
            long sum = 0;

            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
                int progress = (int) (sum * 1.0f / total * 100);
                Bundle bundle =new Bundle();
                bundle.putInt("progress",progress);
                sendMessage(DOWNLOADING,bundle);

                Log.d(TAG, "progress=" + progress);
            }
            fos.flush();

            Log.d(TAG, "文件下载成功");
            return mFile;

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "文件下载失败"+ e.toString());

        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
            }
        }
        return null;
    }


    /**
     * 安装app
     * @param file
     *//*
    private void installAPK(File file){

        if(!file.exists()){
            return;
        }
        Intent intent=new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        intent.setDataAndType(Uri.parse("file://"+file.toString()), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
*/
    /**
     * 安装app 7.0版本
     * @param file
     */
    public static void installAPK(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "com.example.onlinelearn.downapk.fileprovider"即是在清单文件中配置的authorities

            data = FileProvider.getUriForFile(context, FinalStr.APPLICATION_ID +".downapk.fileprovider", file);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        context.startActivity(intent);
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
}
