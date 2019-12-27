package com.example.onlinelearnActivity.courseWare.practic;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinelearnActivity.BaseActivity;
import com.example.util.NetWork;
import com.example.entity.PracticLearnBehavior;
import com.example.entity.SDPracticeEntity;
import com.example.jdbc.DBManagerToSDPractice;
import com.example.jsonReturn.CourseWCLXReturn;
import com.example.jsonReturn.UpdatePaperReturn;
import com.example.onlinelearn.R;
import com.example.spt.jaxb.paper.ExerciseCard;
import com.example.spt.jaxb.paper.Paper;
import com.example.util.ApplicationUtil;

import com.example.util.FinalStr;
import com.example.util.HttpUtil;
import com.example.util.JsonUtil;
import com.example.util.SysUtil;
import com.example.util.XStreamUtil;
import com.example.view.BadgeView;
import com.example.view.dialog.AlertDialog;
import com.example.view.dialog.MaterialDialog;


import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;

/**
 * Created by ysg on 2017/8/24.
 */

public class CourseWareChapterTestActivity extends BaseActivity {
    private ApplicationUtil hdaplication;//全局参数对象
    private Handler handler;//异步刷新组件
    RelativeLayout cwlx_rl_wz,cwlx_rl_ct,cwlx_rl_bj;
    CourseWCLXReturn courseWCLXReturn;
    TextView cw_chapter_count,cwc_lx_wz,cwc_lx_ct,cwc_lx_bj;
    Button cw_chaptert_do;
    //网络状态
    Boolean netStatus=false;
    private DBManagerToSDPractice dbSDPractice; //sqlite操作对象
    String titleString="";
    String msgString="亲爱的同学，本讲解练习共有0道题，主观题不能答题，可查看答案，非主观题可重复答题！";//提示信息
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_ware_chapter);
        //setUi();//初始化ui
        setCache();//将当前activity添加到全局参数对象

    }
    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        setActivityLeftTitle(titleString);
        setActivityLeftTitleShow(true);
        setToolbar_backShow(true);
    }
    @Override
    protected void initViews(View view) {
        showLoading();
        //判断是否有网络
        netStatus= NetWork.isNetworkConnected(CourseWareChapterTestActivity.this);
        dbSDPractice = new DBManagerToSDPractice(getApplicationContext());
        cw_chaptert_do=(Button)findViewById(R.id.cw_chaptert_do);
        cw_chapter_count=(TextView)findViewById(R.id.cw_chapter_count);
        cwc_lx_wz=(TextView)findViewById(R.id.cwc_lx_wz);
        cwc_lx_ct=(TextView)findViewById(R.id.cwc_lx_ct);
        cwc_lx_bj=(TextView)findViewById(R.id.cwc_lx_bj);
        cwlx_rl_wz=(RelativeLayout)findViewById(R.id.cwlx_rl_wz);
        cwlx_rl_ct=(RelativeLayout)findViewById(R.id.cwlx_rl_ct);
        cwlx_rl_bj=(RelativeLayout)findViewById(R.id.cwlx_rl_bj);
    }

    /**
     * 全局信息初始化
     */
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);

        updateUi();//更新ui
        hdaplication = (ApplicationUtil)getApplication();
        hdaplication.setStatus("0");
        //设置章节名称
        Intent intent=getIntent();
        titleString=(String) intent.getExtras().get("title");

    }

    @Override
    protected void doBusiness(Context mContext) {
        isPaperAndExerciseCard();//判断练习资源与答题卡资源是否存在
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

    public void isPaperAndExerciseCard(){
        File pFile=new File(getFilesDir().getPath()+"/course_ware_Chapter");//判断文件夹是否存在，不存在则新建
        if (!pFile.exists()) {
            pFile.mkdir();
        }
        pFile=new File(getFilesDir().getPath()+"/course_ware_Chapter/"+hdaplication.getClassId());//判断文件夹是否存在，不存在则新建
        if (!pFile.exists()) {
            pFile.mkdir();
        }
        File file=new File(getFilesDir().getPath()+"/course_ware_Chapter/"+hdaplication.getClassId()+"/"+hdaplication.getBehaviorId());//判断文件夹是否存在，不存在则新建
        if (!file.exists()) {
            file.mkdir();
        }
        String pathString=getFilesDir().getPath();
        boolean status=true;
        //判断试卷与答题卡是否存在 不存在则修改status为false
        File paperfile=new File(getFilesDir().getPath()+"/course_ware_Chapter/"+hdaplication.getClassId()+"/"+hdaplication.getBehaviorId(),
                "paper.xml");
        if (!paperfile.exists()){
            status=false;
        }
        File exefile=new File(getFilesDir().getPath()+"/course_ware_Chapter/"+hdaplication.getClassId()+"/"+hdaplication.getBehaviorId(),
                "exerciseCard.xml");
        if (!exefile.exists()){
            status=false;
        }
        //判断是否有网络
        netStatus= NetWork.isNetworkConnected(CourseWareChapterTestActivity.this);
        if (netStatus){
            if(status){
                getData();
            }else {
                updatePaper();
            }
        }else {
            if(status){
                sendMessage(1);
            }else {
                closeLoading();
                Toast.makeText(CourseWareChapterTestActivity.this, "请检查网络连接状态", Toast.LENGTH_LONG).show();
            }
        }
    }

    MaterialDialog alertDialog;
    //开始练习按钮点击事件
    public void click(View v) {
        if (v.getId()==R.id.cw_chapter_xxxz){
            alertDialog=new MaterialDialog(CourseWareChapterTestActivity.this);
            alertDialog.setTitle("学习须知");
            alertDialog.setMessage(msgString);
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        }else {
            //判断是否有网络
            netStatus= NetWork.isNetworkConnected(CourseWareChapterTestActivity.this);
            if (!netStatus){
                if (v.getId()==R.id.cwlx_rl_wz||v.getId()==R.id.cwlx_rl_ct){
                    Toast.makeText(CourseWareChapterTestActivity.this, "离线状态下，该功能不开放", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            String aa=v.getTag()+"";
            String [] tagStrings=aa.split("#");
            if (tagStrings.length>=2) {
                if (!tagStrings[2].equals("0")) {
                    if (tagStrings[1].equals("2")) {
                        Intent intent =new Intent();
                        intent.setClass(CourseWareChapterTestActivity.this,CourseWareLxCtActivity.class);
                        intent.putExtra("count", tagStrings[2]);
                        startActivity(intent);
                    }else {
                       /* Intent intent =new Intent();*/
                       /* intent.setClass(CourseWareChapterTestActivity.this,PracticePagerActivity.class);*/
                        Bundle mBundle = new Bundle();
                        //mBundle.putSerializable("papers",paper);
                        mBundle.putString("batchId", v.getTag()+"");
                       /* intent.putExtras(mBundle);
                        intent.putExtra("batchId", v.getTag()+"");

                        startActivity(intent);*/
                       startActivity(PracticePagerActivity.class,mBundle);
                    }
                }else {
                    Toast.makeText(CourseWareChapterTestActivity.this, "没有相关的信息", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(CourseWareChapterTestActivity.this, "没有相关的信息", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void setCache() {
        hdaplication = (ApplicationUtil) getApplication();
        hdaplication.addActivity(this);
    }

    /*
    * 更新ui
    * */
    public void updateUi(){
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: //无法加载数据
                        closeLoading();
                        Toast.makeText(CourseWareChapterTestActivity.this,"无法加载数据,请检查你的网络连接或者联系我们！",
                                Toast.LENGTH_LONG).show();
                        break;
                    case 1: //加载数据
                        setList();
                        //closeAlert();
                        break;
                    case 2:
                        //closeAlert();
                        closeLoading();
                        Toast.makeText(CourseWareChapterTestActivity.this, "没有相关的信息", Toast.LENGTH_LONG).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    private void sendMessage(int str){
        Message msg = new Message();
        msg.what = str;
        CourseWareChapterTestActivity.this.handler.sendMessage(msg);
    }

    private  Paper paper;
    public void setList(){
        try {
            showLoading();
            //设置悬浮题目数量
            int questionSum=0;
            String questionSunIds="";
            int questionWz=0;
            String questionWzIds="";
            int wrongCount=0;
            String questionWrongIds="";
            int signCount=0;
            int AllFinishQuestionCount=0;
            //解析xml
            File paperfile=new File(getFilesDir().getPath()+"/course_ware_Chapter/"+hdaplication.getClassId()+"/"+hdaplication.getBehaviorId(),
                    "paper.xml");
            if (paperfile.exists()){
                paper=new XStreamUtil().str2JavaFormXML(paperfile,Paper.class);
                hdaplication.setPapers(paper);
                questionSum=paper.getQuestions().getQuestion().size();

            }

            msgString="亲爱的同学，本讲解练习共有"+questionSum+"道题，主观题不能答题，可查看答案，非主观题可重复答题！";
            //解析xml
            File exerciseCardfile=new File(getFilesDir().getPath()+"/course_ware_Chapter/"+hdaplication.getClassId()+"/"+hdaplication.getBehaviorId(),
                    "exerciseCard.xml");
            if (exerciseCardfile.exists()){
                ExerciseCard exerciseCard=new XStreamUtil().str2JavaFormXML(exerciseCardfile,ExerciseCard.class);
                hdaplication.setExerciseCard(exerciseCard);
            }
            String batchId="";
            if(courseWCLXReturn!=null&&hdaplication.getStatus().equals("0")){
                AllFinishQuestionCount=Integer.parseInt(courseWCLXReturn.getAllFinishQuestionCount());
                questionWz=questionSum-AllFinishQuestionCount;//未做
                wrongCount=Integer.parseInt(courseWCLXReturn.getWrongCount());//错题
                signCount=Integer.parseInt(courseWCLXReturn.getSignCount());//标记
                PracticLearnBehavior practicLearnBehavior=new PracticLearnBehavior();
                practicLearnBehavior.setCorrectIds(courseWCLXReturn.getCorrectIds());
                practicLearnBehavior.setErrorIds(courseWCLXReturn.getErrorIds());
                practicLearnBehavior.setErrorQuestionIdsStr(courseWCLXReturn.getErrorQuestionIdsStr());
                practicLearnBehavior.setSignQuestionIds(courseWCLXReturn.getSignQuestionIds());
                practicLearnBehavior.setQuestionAnswerCount(AllFinishQuestionCount);
                practicLearnBehavior.setQuestionSignCount(signCount);
                hdaplication.setPracticLearnBehavior(practicLearnBehavior);
                batchId=courseWCLXReturn.getBatchId();
            }else if(hdaplication.getStatus().equals("1")){
                PracticLearnBehavior practicLearnBehavior=hdaplication.getPracticLearnBehavior();
                if (practicLearnBehavior!=null){
                    AllFinishQuestionCount=practicLearnBehavior.getQuestionAnswerCount();
                    if (netStatus){
                        questionWz=questionSum-AllFinishQuestionCount;//未做
                        wrongCount=practicLearnBehavior.getQuestionErrorCount();
                    }
                    signCount=practicLearnBehavior.getQuestionSignCount();
                }
            }else {
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("account_id",hdaplication.getStuid()+"");
                map.put("class_id",hdaplication.getClassId()+"");
                map.put("behavior_id",hdaplication.getBehaviorId()+"");
                List<SDPracticeEntity> list=dbSDPractice.queryByMap(map);
                if (list!=null&&list.size()>0){
                    SDPracticeEntity entity=list.get(0);
                    PracticLearnBehavior practicLearnBehavior=new PracticLearnBehavior();
                    practicLearnBehavior.setCorrectIds(entity.getCorrectIds());
                    practicLearnBehavior.setErrorIds(entity.getErrorIds());
                    practicLearnBehavior.setErrorQuestionIdsStr(entity.getErrorQuestionIdsStr());
                    practicLearnBehavior.setSignQuestionIds(entity.getSignQuestionIds());
                    String signStr=entity.getSignQuestionIds();
                    if (signStr!=null&&!signStr.equals("")){
                        String []str=signStr.split("&");
                        if (str!=null){
                            signCount=str.length;
                        }
                    }
                    practicLearnBehavior.setQuestionSignCount(signCount);
                    AllFinishQuestionCount=entity.getQuestionAnswerCount();
                    practicLearnBehavior.setQuestionAnswerCount(AllFinishQuestionCount);
                    hdaplication.setPracticLearnBehavior(practicLearnBehavior);
                }
            }
            cwc_lx_wz.setText(questionWz+"");
            cwc_lx_ct.setText(wrongCount+"");
            cwc_lx_bj.setText(signCount+"");
            cw_chapter_count.setText(AllFinishQuestionCount+"/"+questionSum);
            cw_chaptert_do.setTag("do#0#"+questionSum+"#"+batchId);
            cwlx_rl_wz.setTag("do#1#"+questionWz+"#"+batchId);
            cwlx_rl_ct.setTag("do#2#"+wrongCount+"#"+batchId);
            cwlx_rl_bj.setTag("do#3#"+signCount+"#"+batchId);
            closeLoading();
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(2);
        }
    }

    //从服务器获取练习题目数据
    public void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("coursewareId", hdaplication.getCoursewareId());
                map.put("classId", hdaplication.getClassId());
                map.put("behaviorId", hdaplication.getBehaviorId());
                map.put("accountId", hdaplication.getStuid());
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?buildExercise",
                        map,CourseWareChapterTestActivity.this,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                    sendMessage(0);
                }else{
                    courseWCLXReturn = JsonUtil.parserString(result, CourseWCLXReturn.class);
                    if(courseWCLXReturn!=null&&courseWCLXReturn.isSuccess()){
                        //hdaplication.setBatchId(courseWCLXReturn.getBatchId());
                        //updatePaper();
                        sendMessage(1);
                        //汇总入库
                        SDPracticeEntity sdPracticeEntity=new SDPracticeEntity();
                        sdPracticeEntity.setId(hdaplication.getStuid()+hdaplication.getBehaviorId()+hdaplication.getClassId());
                        sdPracticeEntity.setAccountId(hdaplication.getStuid());
                        sdPracticeEntity.setBehaviorId(hdaplication.getBehaviorId());
                        sdPracticeEntity.setClassId(hdaplication.getClassId());
                        sdPracticeEntity.setQuestionAnswerCount(Integer.parseInt(courseWCLXReturn.getAllFinishQuestionCount()));
                        sdPracticeEntity.setSignQuestionIds(courseWCLXReturn.getSignQuestionIds());
                        sdPracticeEntity.setErrorQuestionIdsStr(courseWCLXReturn.getErrorQuestionIdsStr());
                        sdPracticeEntity.setErrorIds(courseWCLXReturn.getErrorIds());
                        sdPracticeEntity.setCorrectIds(courseWCLXReturn.getCorrectIds());
                        dbSDPractice.saveEntity(sdPracticeEntity);
                    }else {
                        sendMessage(0);
                    }
                }
            }
        }).start();
    }

    //更新题目
    public void updatePaper(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("coursewareId", hdaplication.getCoursewareId());
                map.put("behaviorId", hdaplication.getBehaviorId());
                map.put("dataType", "xml");
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?updatePaper",
                        map,CourseWareChapterTestActivity.this,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                    sendMessage(0);
                }else{
                    UpdatePaperReturn updatePaperReturn = JsonUtil.parserString(result, UpdatePaperReturn.class);
                    if(updatePaperReturn!=null){
                        String paperXml=updatePaperReturn.getPaperXml();
                        String exerciseCardXml=updatePaperReturn.getExerciseCardXml();
                        try {
                            //保存题目xml
                            File paperfile=new File(getFilesDir().getPath()+"/course_ware_Chapter/"+hdaplication.getClassId()+"/"+hdaplication.getBehaviorId(),
                                    "paper.xml");
                            FileOutputStream fos=new FileOutputStream(paperfile);
                            fos.write(paperXml.toString().getBytes());
                            fos.close();

                            //保存答题卡xml
                            File exefile=new File(getFilesDir().getPath()+"/course_ware_Chapter/"+hdaplication.getClassId()+"/"+hdaplication.getBehaviorId(),
                                    "exerciseCard.xml");
                            FileOutputStream fos1=new FileOutputStream(exefile);
                            fos1.write(exerciseCardXml.toString().getBytes());
                            fos1.close();
                            getData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).start();
    }

    //设置悬浮数字
    private void setBadgeView(ImageView v, int questionWz,BadgeView badgeView) {
        badgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        badgeView.setBackground(12, Color.parseColor("#ef6620"));
        badgeView.setTargetView(v);
        badgeView.setBadgeCount(questionWz);
    }

    /*
    * 加载页面时进来
    * */
    public void onResume() {
        if(hdaplication.getStatus().equals("0")){
            //isPaperAndExerciseCard();//判断练习资源与答题卡资源是否存在
        }else {
            sendMessage(1);
        }
        super.onResume();
    }

    public void clickReturn(View v){
        finish();
    }


}
