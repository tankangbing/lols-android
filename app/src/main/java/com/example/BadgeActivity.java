package com.example;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.onlinelearn.R;
import com.example.view.BadgeView;

/**
 * Created by ysg on 2017/10/24.
 */

public class BadgeActivity extends Activity {

    Drawable exeBtnError, exeBtnCurror, exeBtnNow, exeBorder;//答题卡图标  错题 红色exeBtnError   对题 绿色exeBtnCurror   当前题 蓝色exeBtnNow  未做 透明exeBorder
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);
        exeBtnNow = getResources().getDrawable(R.drawable.exe_btn_now);//当前题 蓝色
        exeBtnNow.setBounds(0, 0, exeBtnNow.getMinimumWidth(), exeBtnNow.getMinimumHeight());
        RelativeLayout rl2=(RelativeLayout) findViewById(R.id.rl2);
        BadgeView badgeView = new BadgeView(this);
        badgeView.setTargetView(findViewById(R.id.r1));
        badgeView.setBadgeGravity(Gravity.RIGHT | Gravity.CENTER);
        badgeView.setBadgeMargin(0, 0, 10, 0);
        badgeView.setText("NEW");

        RelativeLayout rl3=(RelativeLayout) findViewById(R.id.rl3);
       // rl3.setBackground(exeBtnNow);
        BadgeView badgeView2 = new BadgeView(this);
        badgeView2.setTargetView(findViewById(R.id.rl2));
        badgeView2.setBadgeCount(9);
        Button btn=(Button)findViewById(R.id.btn);
        BadgeView badgeView3 = new BadgeView(this);
        badgeView3.setTargetView(rl3);
        badgeView3.setBadgeCount(9);
        btn.setBackground(exeBtnNow);
    }
}