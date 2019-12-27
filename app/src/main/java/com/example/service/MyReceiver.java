package com.example.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


/**
 * 注册广播监听网络状态
 * @author Amao
 *
 */
@SuppressLint("ShowToast")
public class MyReceiver extends BroadcastReceiver {

    private final static int NET_Y = 1;//有网络
    private final static int NET_N = 0;//无网络

    public MyReceiver(){};

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getNetworkState(context)) {
            case 0:
                Toast.makeText(context, "请检查你的网络连接！", 1000).show();
                break;
        }
    }


    /**
     * 获取当前网络状态
     * @param context
     * @return
     */
    public static int getNetworkState(Context context){
        if (context!=null){
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeInfo = connManager.getActiveNetworkInfo();
            if(null != activeInfo){
                return NET_Y;
            }else{
                return NET_N;
            }
        }else {
            return NET_N;
        }
    }
}  
