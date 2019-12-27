package com.example.onlinelearnActivity.courseWare.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entity.DLTestEntity;
import com.example.jsonReturn.TextOrExamIndexReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.BaseActivity;
import com.example.util.FinalStr;
import com.example.view.dialog.AlertDialog;
import com.example.util.ApplicationUtil;
import com.example.util.HttpUtil;
import com.example.util.JsonUtil;
import com.example.util.SysUtil;
import com.example.view.GraphView;
import com.example.view.dialog.MaterialDialog;


public class CourseWareTestActivity extends BaseActivity {
    private ApplicationUtil hdaplication;//全局参数对象
    private Handler handler;//异步刷新组件
    MaterialDialog alertDialog;
    LinearLayout cw_test_lookLL, cw_test_Tj, cw_test_zdfsb;
    TextOrExamIndexReturn textOrExamIndexReturn;//请求返回结果
    TextView cw_test_total, cw_test_count, cw_test_tv_zj2, cw_test_tv_zg2, cw_test_tv_pj2;//总分，题目总数，最近得分，最高得分，平均得分
    String msgString = "亲爱的同学，本讲解测试共有0道题，主观题不计分，满分0分，可以重复测试，系统对测试结果不进行考核！";//提示信息
    GraphView cw_test_gv;
    double[] date;
    String titleString = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_ware_test);
        setCache();
        setUi();
        updateUi();
        //getData();
    }

    @Override
    protected void initViews(View view) {
        showLoading();
        cw_test_zdfsb = (LinearLayout) findViewById(R.id.cw_test_zdfsb);
        cw_test_lookLL = (LinearLayout) findViewById(R.id.cw_test_lookLL);
        cw_test_Tj = (LinearLayout) findViewById(R.id.cw_test_Tj);
        cw_test_total = (TextView) findViewById(R.id.cw_test_total);
        cw_test_count = (TextView) findViewById(R.id.cw_test_count);
        cw_test_tv_zj2 = (TextView) findViewById(R.id.cw_test_tv_zj2);
        cw_test_tv_zg2 = (TextView) findViewById(R.id.cw_test_tv_zg2);
        cw_test_tv_pj2 = (TextView) findViewById(R.id.cw_test_tv_pj2);
        cw_test_gv = (GraphView) findViewById(R.id.cw_test_gv);
    }

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        //设置章节名称
        Intent intent = getIntent();
        titleString = (String) intent.getExtras().get("title");
    }

    @Override
    protected void doBusiness(Context mContext) {

    }

    /**
     * 设置toolbar内容
     */
    public void setToolbar() {
        super.setToolbar();
        setActivityLeftTitle(titleString);
        setActivityLeftTitleShow(true);
        setToolbar_backShow(true);
    }

    public void setCache() {
        hdaplication = (ApplicationUtil) getApplication();
        hdaplication.addActivity(this);
    }

    public void setUi() {

    }

    public void updateUi() {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: //无法加载数据
                        Toast.makeText(CourseWareTestActivity.this, "无法加载数据,请检查你的网络连接或者联系我们！",
                                Toast.LENGTH_LONG).show();
                        break;
                    case 1: //加载数据
                        setList();
                        break;
                    case 2://没有获取时，清除页面
                        Toast.makeText(CourseWareTestActivity.this, "没有相关的信息", Toast.LENGTH_LONG).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    public void setList() {
        showLoading();
        if (textOrExamIndexReturn != null) {
            msgString = "亲爱的同学，本讲解测试共有" + textOrExamIndexReturn.getQuestionCount() + "道题，主观题不计分，满分" + textOrExamIndexReturn.getSumScore() + "分，可以重复测试，系统对测试结果不进行考核！";
            cw_test_total.setText("满分" + textOrExamIndexReturn.getSumScore() + "分");
            cw_test_count.setText("共" + textOrExamIndexReturn.getQuestionCount() + "题");
            cw_test_tv_zj2.setText(textOrExamIndexReturn.getLastestScore() + "分");
            cw_test_tv_zg2.setText(textOrExamIndexReturn.getHighestScore() + "分");
            cw_test_tv_pj2.setText(textOrExamIndexReturn.getAverageScore() + "分");
            date = examTj(textOrExamIndexReturn.getPastRecords());
            setQst();
            setZdfsb();
            //cw_test_gv.setDate(date);
            //cw_test_gv.setDate(date);
        }
        closeLoading();
    }

    private void sendMessage(int str) {
        Message msg = new Message();
        msg.what = str;
        CourseWareTestActivity.this.handler.sendMessage(msg);
    }

    //点击事件
    public void click(View v) {
        if (v.getId() == R.id.cw_test_xxxz) {//学习须知
            alertDialog = new MaterialDialog(CourseWareTestActivity.this);
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.setTitle("学习须知");
            alertDialog.setMessage(msgString);
            alertDialog.show();
        } else if (v.getId() == R.id.cw_test_do) {
            alertDialog = new MaterialDialog(CourseWareTestActivity.this);
            alertDialog.setTitle("学习须知");
            alertDialog.setMessage(msgString);
            alertDialog.setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.setPositiveButton("开始测试", new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.setClass(CourseWareTestActivity.this, TestPagerActivity.class);
                    startActivity(intent);
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } else if (v.getId() == R.id.cw_test_lookTJ) {
            cw_test_lookLL.setVisibility(View.GONE);
            cw_test_Tj.setVisibility(View.VISIBLE);
        }
    }

    public void onclick1(View v) {
        String s = (String) v.getTag();
        String[] strings = s.split("##");
        String studentPaperId = "";
        String paperId = "";
        if (strings != null && strings.length == 2) {
            studentPaperId = strings[0];
            paperId = strings[1];
        }
        Intent intent = new Intent();
        intent.setClass(CourseWareTestActivity.this, TestPagerActivity.class);
        intent.putExtra("studentPaperId", studentPaperId);
        intent.putExtra("paperId", paperId);
        startActivity(intent);
    }

    public void setQst() {
        cw_test_gv.setDate(date, Double.parseDouble(textOrExamIndexReturn.getSumScore()));
    }

    public void setZdfsb() {
        List<DLTestEntity> pastRecords = textOrExamIndexReturn.getPastRecords();
        if (pastRecords != null && pastRecords.size() > 0) {
            cw_test_zdfsb.removeAllViews();
            int i = pastRecords.size();
//            for (int j = pastRecords.size() - 1; j >= 0; j--) {
//                DLTestEntity dLTestEntity = pastRecords.get(j);
//                View v = View.inflate(CourseWareTestActivity.this, R.layout.course_ware_test_item, null);
//                v.setTag(dLTestEntity.getId() + "##" + dLTestEntity.getPaperId());
//                TextView tv = (TextView) v.findViewById(R.id.cw_test_item_cs);
//                TextView tv1 = (TextView) v.findViewById(R.id.cw_test_item_sj);
//                TextView tv2 = (TextView) v.findViewById(R.id.cw_test_item_fs);
//                tv.setText(i + "");
//                tv1.setText(dLTestEntity.getCreateDate() + "");
//                tv2.setText(dLTestEntity.getScore() + "");
//                i--;
//                cw_test_zdfsb.addView(v);
//            }
//            int i = 1;
            for (DLTestEntity dLTestEntity:pastRecords){
                View v=View.inflate(CourseWareTestActivity.this,R.layout.course_ware_test_item,null);
                v.setTag(dLTestEntity.getId() + "##" + dLTestEntity.getPaperId());
                TextView tv = (TextView) v.findViewById(R.id.cw_test_item_cs);
                TextView tv1 = (TextView) v.findViewById(R.id.cw_test_item_sj);
                TextView tv2 = (TextView) v.findViewById(R.id.cw_test_item_fs);
                tv.setText(i + "");
                tv1.setText(dLTestEntity.getCreateDate() + "");
                tv2.setText(dLTestEntity.getScore() + "");
                i--;
                cw_test_zdfsb.addView(v);
            }
        }
    }

    public void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>(0);
                map.put("type", "2");
                map.put("coursewareId", hdaplication.getCoursewareId());
                map.put("classId", hdaplication.getClassId());
                map.put("behaviorId", hdaplication.getBehaviorId());
                map.put("accountId", hdaplication.getStuid());
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH + "/appControler.do?textOrExamIndex",
                        map, CourseWareTestActivity.this, null);
                if (SysUtil.isBlank(result) || "NO_NET".equals(result)) {
                    sendMessage(0);
                } else {
                    textOrExamIndexReturn = JsonUtil.parserString(result, TextOrExamIndexReturn.class);
                    if (textOrExamIndexReturn != null && textOrExamIndexReturn.isSuccess()) {
                        sendMessage(1);
                    } else {
                        sendMessage(2);
                    }
                }
            }
        }).start();
    }

    //汇总统计历史答题记录
    public double[] examTj(List<DLTestEntity> pastRecords) {
        double[] date = new double[pastRecords.size() + 1];
        if (pastRecords != null && pastRecords.size() > 0) {
            date[0] = 0.0;
//            for (int i = 0; i < pastRecords.size(); i++) {
//                //double score=pastRecords.get(i).getScore();
//                date[i + 1] = pastRecords.get(i).getScore();
//            }
            for(int i = pastRecords.size();i>0;i--){
                date[i] = pastRecords.get(pastRecords.size() - i).getScore();
            }
        }
        return date;
    }

    public void onResume() {
        getData();
        super.onResume();
    }

}
