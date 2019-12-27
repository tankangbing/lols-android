package com.example.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.ViewGroup;
import android.view.WindowManager;


import java.lang.reflect.Method;

/**
 * 兼容21 19 以及19 以下版本的 浸入式状态栏
 * 兼容虚拟键的显示
 */

public class BarCompat {


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, int statusColor)
    {

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (statusColor != INVALID_VAL)
            {
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        }*/

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            int color = COLOR_DEFAULT;
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL)
            {
                color = statusColor;
            }
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
        }*/
        //如果版本大于19,浸入式操作
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
        /*setNotFullScreen(activity);*/
    }


    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取虚拟键高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context){

        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }


    /**
     * 是否有虚拟键
     * @param context
     * @return
     */
    private static boolean checkDeviceHasNavigationBar(Context context) {
        //Android 5.0以下没有虚拟按键
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    /**
     * 设置全屏下虚拟键
     * @param activity
     *//*
    public static void setFullScreen(Activity activity){
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        p.setMargins(0, 0, 0, 0);
        contentView.setLayoutParams(p);
    }*/

    /**
     * 设置非全屏下虚拟键
     * @param activity
     */
    public static void setNotFullScreen(Activity activity){
        if (checkDeviceHasNavigationBar(activity)){
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
            p.setMargins(0, 0, 0, getNavigationBarHeight(activity));
            contentView.setLayoutParams(p);
        }
    }
}
