package com.example.util;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import com.example.Handler.UIHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import okhttp3.ResponseBody;

public class FileUtil {

    private static final String TAG ="FileUtil";
    private static final int REFRESH_PROGRESS = 100; //进度返回

    //进度回调
    private static UIHandler uiHandler;

    /**
     * 判断文件是否存在
     * @param path
     * @param fileName
     * @return
     */
    public static File isExist(String path, String fileName){
        final File file = new File(path, fileName);

        if (file.exists()){
            Log.d(TAG,"打开本地文件");
            return file;
        }
        return null;
    }

    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     */
    public static String readFileByChars(File file) {
        StringBuffer rtstr = new StringBuffer();
        if(null == file) return "";
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                rtstr.append(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            return rtstr.toString();
        }
    }



    /**
     * Retrofit 网络框架写入文件
     * @param responseBody
     * @return
     */
    public static File writeResponseBodyToDisk(ResponseBody responseBody , String path, String fileName) {

        Log.d(TAG,path);

        InputStream is = null;
        byte[] buf = new byte[2048];
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
                if (uiHandler !=null){

                    Bundle bundle =new Bundle();
                    bundle.putInt("progress",progress);

                    sendMessage(REFRESH_PROGRESS,bundle);
                }
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
     * 从URL地址中获取文件名，即/后面的字符
     * @param url
     * @return
     */
    public static String getfileName(String url) {

        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 格式化 Int
     * @param resourceSize
     * @return
     */
    public static String computeFileSize(int resourceSize){
        DecimalFormat decimalFormat = new DecimalFormat("#.##"); // 小数格式化
        String sizeStr="";
        double size=0;
        if (resourceSize>=1024){
            size=resourceSize/1024;
            if (size>1024){
                size=size/1024;
                if (size>1024){
                    size=size/1024;
                    sizeStr=decimalFormat.format(size)+" G";
                }else {
                    sizeStr=decimalFormat.format(size)+" M";
                }
            }else {
                sizeStr=decimalFormat.format(size)+" KB";
            }
        }else {
            sizeStr=decimalFormat.format(size)+" B";
        }
        return sizeStr;
    }


    /**
     * 格式化 Long
     * @param resourceSize
     * @return
     */
    public static String computeFileSize(long resourceSize){
        DecimalFormat decimalFormat = new DecimalFormat("#.##"); // 小数格式化
        String sizeStr="";
        double size=0;
        if (resourceSize>=1024){
            size=resourceSize/1024;
            if (size>1024){
                size=size/1024;
                if (size>1024){
                    size=size/1024;
                    sizeStr=decimalFormat.format(size)+" G";
                }else {
                    sizeStr=decimalFormat.format(size)+" M";
                }
            }else {
                sizeStr=decimalFormat.format(size)+" KB";
            }
        }else {
            sizeStr=decimalFormat.format(size)+" B";
        }
        return sizeStr;
    }

    public static void setUiHandler(UIHandler Handler) {
        uiHandler = Handler;
    }

    /**
     * 发送消息
     * @param flag
     * @param b
     */
    public static void sendMessage(int flag, Bundle b){
        Message msg = new Message();
        msg.what = flag;
        if(null != b){
            msg.setData(b);
        }
        uiHandler.sendMessage(msg);
    }

    /**
     * 清空handler
     */
    public static void clearHandler(){
        uiHandler =null;
    }
}
