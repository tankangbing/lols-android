package com.example.onlinelearnActivity.task;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.QuestQuestionModel;
import com.example.entity.StudentTaskModel;
import com.example.entity.TaskModel;
import com.example.entity.TaskSaveModel;
import com.example.jsonReturn.GetFjReturn;
import com.example.jsonReturn.TaskShowReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.BaseActivity;
import com.example.test.HQCodec;
import com.example.util.ApplicationUtil;

import com.example.util.FinalStr;
import com.example.util.HttpUtil;
import com.example.util.JsonUtil;
import com.example.util.SysUtil;
import com.example.view.NoScrollListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.view.dialog.AlertDialog;
import com.example.view.dialog.MaterialDialog;


/**
 * Created by ysg on 2017/7/7.
 */

public class TaskShowActivity extends BaseActivity implements View.OnTouchListener{
    private ApplicationUtil hdaplication;//全局参数对象
    private Handler handler;//异步刷新组件
    android.app.AlertDialog ad;//弹框提示
    TextView tshow_alert_title,tshow_alert_startdate,tshow_alert_tjdate,tshow_alert_yjdate,tshow_alert_total,tshow_alert_count;//弹框提示的各种信息
    Button tshow_alert_Cancel,tshow_alert_Sure;//弹框提示的取消与确定
    private TaskModel taskModel;
    Map<String, String> xxBj=new HashMap<String, String>();
    Map<String, String> xxMap=new HashMap<String, String>();
    private List<QuestQuestionModel> questQuestionList;
    int index=1;
  /*  AlertDialog alertDialog;*/
    //提示框
    protected MaterialDialog alertDialog;
    List <String> xxTitle=new ArrayList<String>();
    List <String> xxId=new ArrayList<String>();
    Map<String,String> daAn=new HashMap<String,String>();//答案
    Map<String,String> daAnfj=new HashMap<String,String>();//答案附件
    Map<String,Integer> daAnJl=new HashMap<String,Integer>();
    Drawable drawable_zq,drawable_cw,ui_icon_chose0,ui_icon_chose1;
    String taskId="";
    String optionId="";
    String objectJson="";//客观题json
    String subjectJson="";//主观题json
    Boolean sdStatus=false;//答题卡状态
    Html.ImageGetter imageGetter;
    QuestQuestionModel questQuestionModel;
    LinearLayout tshow_result;
    EditText tshow_et;
    CustomImageSpan imageSpan;
    TextView tshow_nr;
    TextView tshow_tj;
    NoScrollListView lv_xx;
    ScrollView gv;
    TableLayout tshow_ll;
    TextView tshow_tjfj;//添加附件按钮
    TextView tshow_xzfj;//下载附件按钮
    Button lastButton;
    Drawable exeBtnError,exeBtnCurror,exeBtnNow,exeBorder ,shape,shape2,exeBtnFin;
    private SlidingDrawer sd;
    String questionId;
    String taskStudentId;
    String tjType="0";//提交类型：0保存 1提交
    String todo="do";//作业类型  do 作答  view 查看
    String name="";//附件名称
    String filePath="";//附件路径
    private long reference;
    Map<String ,TaskSaveModel> map=new HashMap<String ,TaskSaveModel>();
    JSONObject jsonObj = new JSONObject();
    List<TaskSaveModel> list=new ArrayList<TaskSaveModel>();
    private final static int backCode = 0x11;

    Dialog dialog;
    List<EditText> tshowEtList=new ArrayList<EditText>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_show);
        setCache();
        setUi();
        updateUi();
        getData();
    }

    @Override
    protected void initViews(View view) {
        gv = (ScrollView) findViewById(R.id.tshow_sv);
        gv.setOnTouchListener(this);
        Intent intent=getIntent();
        taskId=intent.getStringExtra("taskId");
        tshow_nr=(TextView)findViewById(R.id.tshow_nr);
        tshow_result=(LinearLayout)findViewById(R.id.tshow_result);
        tshow_tj=(TextView)findViewById(R.id.tshow_tj);
        tshow_tjfj=(TextView) findViewById(R.id.tshow_tjfj);
        tshow_xzfj=(TextView)findViewById(R.id.tshow_xzfj);

        //选项自定义listview
        lv_xx=(NoScrollListView)findViewById(R.id.tshow_lv_xx);
        //初始化各种图片背景
        drawable_zq = getResources().getDrawable(R.drawable.u20);//选项答对图片
        drawable_zq.setBounds(0, 0, 54, 54);  //此为必须写的
        drawable_cw = getResources().getDrawable(R.drawable.u21);//选项答错图片
        drawable_cw.setBounds(0, 0,  54, 54);  //此为必须写的
        ui_icon_chose0 = getResources().getDrawable(R.drawable.u19);//选项默认图片
        ui_icon_chose0.setBounds(0, 0,  54, 54);  //此为必须写的
        ui_icon_chose1 = getResources().getDrawable(R.drawable.u17);//选项选中图片
        ui_icon_chose1.setBounds(0, 0,  54, 54);  //此为必须写的
        tshow_ll=(TableLayout)findViewById(R.id.tshow_ll);
        lv_xx.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (todo.equals("do")){
                    if (questQuestionModel.getQuestionType().equals("01")||questQuestionModel.getQuestionType().equals("03")) {
                        optionId=xxId.get(i);
                        if (daAnJl.get(questQuestionModel.getId())!=null&&!daAnJl.get(questQuestionModel.getId()).equals(i)){
                            View v=lv_xx.getChildAt(daAnJl.get(questQuestionModel.getId()));
                            TextView tv_seekname = (TextView) v.findViewById(R.id.tvb);
                            tv_seekname.setCompoundDrawables(ui_icon_chose0, null, null, null);
                            v.setBackgroundColor(0xfffff);
                        }
                        view.setBackgroundColor(0xfffec200);
                        TextView tv_seekname = (TextView) view.findViewById(R.id.tvb);
                        tv_seekname.setCompoundDrawables(ui_icon_chose1, null, null, null);
                        optionId=xxId.get(i);
                        if(optionId!=null&&!optionId.equals("")){
                            //optionId=questQuestionModel.getQuestionType()+"#"+questQuestionModel.getId()+"&"+optionId;
                            daAn.put(questQuestionModel.getId(),optionId);
                            daAnJl.put(questQuestionModel.getId(),i);
                        }
                    }else if (questQuestionModel.getQuestionType().equals("02")) {
                        SparseBooleanArray booleanArray = lv_xx.getCheckedItemPositions();
                        if (booleanArray.get(i)) {
                            view.setBackgroundColor(0xfffec200);
                            TextView tv_seekname = (TextView) view.findViewById(R.id.tvb);
                            tv_seekname.setCompoundDrawables(ui_icon_chose1, null, null, null);
                        } else {
                            view.setBackgroundColor(0xfffff);
                            TextView tv_seekname = (TextView) view.findViewById(R.id.tvb);
                            tv_seekname.setCompoundDrawables(ui_icon_chose0, null, null, null);
                        }
                    }
                }
            }
        });
        lv_xx.setOnFlingListener(new NoScrollListView.OnFlingListener(){

            @Override
            public void onLeftFling() {
                if(questQuestionModel!=null){
                    //setExerciseCorlor2(questionId);
                    //questionId=getExerciseReturn.getNextQuestion().getId();
                    //getData2();
                }else {
                    Toast.makeText(TaskShowActivity.this, "当前题是第一道题", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRightFling() {
                if(questQuestionModel!=null){
                    //setExerciseCorlor2(questionId);
                    //questionId=getExerciseReturn.getLastQuestion().getId();
                    //getData2();
                }else {
                    Toast.makeText(TaskShowActivity.this, "当前题是最后一道题", Toast.LENGTH_SHORT).show();
                }
            }


        });
        imageGetter = new Html.ImageGetter() {

            public Drawable getDrawable(String source) {
                Drawable drawable=null;
                int rId=Integer.parseInt(source);
                drawable=getResources().getDrawable(rId);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            };
        };

        //答题卡
        sd = (SlidingDrawer) findViewById(R.id.sliding);
        //答题卡打开时执行
        sd.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            public void onDrawerOpened(){
                sdStatus=true;
                /*test_sl_ll.setBackgroundColor(0x509D9D9D);
                tv.setFocusable(false);
                tv.setClickable(false);*/
            }});
        //答题卡关闭时执行
        sd.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            public void onDrawerClosed() {
                sdStatus=false;
                /*test_sl_ll.setBackgroundColor(0x99FFF);
                tv.setFocusable(true);
                tv.setClickable(true);
                tv.requestFocus();*/
            }
        });
    }

    protected void initialize(View view) {
        //初始化答题卡按钮颜色
        exeBtnError = getResources().getDrawable(R.drawable.u32);//错题 红色
        exeBtnError.setBounds(0, 0, exeBtnError.getMinimumWidth(), exeBtnError.getMinimumHeight());
        exeBtnCurror = getResources().getDrawable(R.drawable.u31);//对题 绿色
        exeBtnCurror.setBounds(0, 0, exeBtnCurror.getMinimumWidth(), exeBtnCurror.getMinimumHeight());
        exeBtnNow = getResources().getDrawable(R.drawable.u40);//当前题 蓝色
        exeBtnNow.setBounds(0, 0, exeBtnNow.getMinimumWidth(), exeBtnNow.getMinimumHeight());
        exeBorder = getResources().getDrawable(R.drawable.u30);//未做 透明
        exeBorder.setBounds(0, 0, exeBtnNow.getMinimumWidth(), exeBtnNow.getMinimumHeight());;
        exeBtnFin = getResources().getDrawable(R.drawable.u39);//已做 篮圈
        exeBtnFin.setBounds(0, 0, exeBtnNow.getMinimumWidth(), exeBtnNow.getMinimumHeight());;
    }
    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        setToobbarShow(false);
    }
    @Override
    protected void doBusiness(Context mContext) {

    }

    public void setCache() {
        hdaplication = (ApplicationUtil) getApplication();
        hdaplication.addActivity(this);
    }

    public void setUi(){
        showLoading();//显示加载框
        gv = (ScrollView) findViewById(R.id.tshow_sv);
        gv.setOnTouchListener(this);
        Intent intent=getIntent();
        taskId=intent.getStringExtra("taskId");
        tshow_nr=(TextView)findViewById(R.id.tshow_nr);
        tshow_result=(LinearLayout)findViewById(R.id.tshow_result);
        tshow_tj=(TextView)findViewById(R.id.tshow_tj);
        tshow_tjfj=(TextView) findViewById(R.id.tshow_tjfj);
        tshow_xzfj=(TextView)findViewById(R.id.tshow_xzfj);
        //选项自定义listview
        drawable_zq = getResources().getDrawable(R.drawable.u20);//选项答对图片
        drawable_zq.setBounds(0, 0, 54, 54);  //此为必须写的
        drawable_cw = getResources().getDrawable(R.drawable.u21);//选项答错图片
        drawable_cw.setBounds(0, 0,  54, 54);  //此为必须写的
        ui_icon_chose0 = getResources().getDrawable(R.drawable.u19);//选项默认图片
        ui_icon_chose0.setBounds(0, 0,  54, 54);  //此为必须写的
        ui_icon_chose1 = getResources().getDrawable(R.drawable.u17);//选项选中图片
        ui_icon_chose1.setBounds(0, 0,  54, 54);  //此为必须写的

        tshow_ll=(TableLayout)findViewById(R.id.tshow_ll);
        lv_xx.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (todo.equals("do")){
                    if (questQuestionModel.getQuestionType().equals("01")||questQuestionModel.getQuestionType().equals("03")) {
                        optionId=xxId.get(i);
                        if (daAnJl.get(questQuestionModel.getId())!=null&&!daAnJl.get(questQuestionModel.getId()).equals(i)){
                            View v=lv_xx.getChildAt(daAnJl.get(questQuestionModel.getId()));
                            v.setBackgroundColor(0xfffff);
                            TextView tv_seekname = (TextView) v.findViewById(R.id.tvb);
                            tv_seekname.setCompoundDrawables(ui_icon_chose0, null, null, null);
                        }
                        view.setBackgroundColor(0xfffec200);
                        TextView tv_seekname = (TextView) view.findViewById(R.id.tvb);
                        tv_seekname.setCompoundDrawables(ui_icon_chose1, null, null, null);
                        optionId=xxId.get(i);
                        if(optionId!=null&&!optionId.equals("")){
                            //optionId=questQuestionModel.getQuestionType()+"#"+questQuestionModel.getId()+"&"+optionId;
                            daAn.put(questQuestionModel.getId(),optionId);
                            daAnJl.put(questQuestionModel.getId(),i);
                        }
                    }else if (questQuestionModel.getQuestionType().equals("02")) {
                        SparseBooleanArray booleanArray = lv_xx.getCheckedItemPositions();
                        if (booleanArray.get(i)) {
                            view.setBackgroundColor(0xfffec200);
                            TextView tv_seekname = (TextView) view.findViewById(R.id.tvb);
                            tv_seekname.setCompoundDrawables(ui_icon_chose1, null, null, null);
                        } else {
                            view.setBackgroundColor(0xfffff);
                            TextView tv_seekname = (TextView) view.findViewById(R.id.tvb);
                            tv_seekname.setCompoundDrawables(ui_icon_chose0, null, null, null);
                        }
                    }
                }
            }
        });
        lv_xx.setOnFlingListener(new NoScrollListView.OnFlingListener(){

            @Override
            public void onLeftFling() {
                if(questQuestionModel!=null){
                    //setExerciseCorlor2(questionId);
                    //questionId=getExerciseReturn.getNextQuestion().getId();
                    //getData2();
                }else {
                    Toast.makeText(TaskShowActivity.this, "当前题是第一道题", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRightFling() {
                if(questQuestionModel!=null){
                    //setExerciseCorlor2(questionId);
                    //questionId=getExerciseReturn.getLastQuestion().getId();
                    //getData2();
                }else {
                    Toast.makeText(TaskShowActivity.this, "当前题是最后一道题", Toast.LENGTH_SHORT).show();
                }
            }


        });
        imageGetter = new Html.ImageGetter() {

            public Drawable getDrawable(String source) {
                Drawable drawable=null;
                int rId=Integer.parseInt(source);
                drawable=getResources().getDrawable(rId);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            };
        };

        //答题卡
        sd = (SlidingDrawer) findViewById(R.id.sliding);
        //答题卡打开时执行
        sd.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            public void onDrawerOpened(){
                sdStatus=true;
                /*test_sl_ll.setBackgroundColor(0x509D9D9D);
                tv.setFocusable(false);
                tv.setClickable(false);*/
            }});
        //答题卡关闭时执行
        sd.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            public void onDrawerClosed() {
                sdStatus=false;
                /*test_sl_ll.setBackgroundColor(0x99FFF);
                tv.setFocusable(true);
                tv.setClickable(true);
                tv.requestFocus();*/
            }
        });

        //初始化答题卡按钮颜色
        exeBtnError = getResources().getDrawable(R.drawable.u32);//错题 红色
        exeBtnError.setBounds(0, 0, exeBtnError.getMinimumWidth(), exeBtnError.getMinimumHeight());
        exeBtnCurror = getResources().getDrawable(R.drawable.u31);//对题 绿色
        exeBtnCurror.setBounds(0, 0, exeBtnCurror.getMinimumWidth(), exeBtnCurror.getMinimumHeight());
        exeBtnNow = getResources().getDrawable(R.drawable.u40);//当前题 蓝色
        exeBtnNow.setBounds(0, 0, exeBtnNow.getMinimumWidth(), exeBtnNow.getMinimumHeight());
        exeBorder = getResources().getDrawable(R.drawable.u30);//未做 透明
        exeBorder.setBounds(0, 0, exeBtnNow.getMinimumWidth(), exeBtnNow.getMinimumHeight());;
        exeBtnFin = getResources().getDrawable(R.drawable.u39);//已做 篮圈
        exeBtnFin.setBounds(0, 0, exeBtnNow.getMinimumWidth(), exeBtnNow.getMinimumHeight());;
    }

    private void sendMessage(int str){
        Message msg = new Message();
        msg.what = str;
        TaskShowActivity.this.handler.sendMessage(msg);
    }

    public void updateUi(){
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: //无法加载数据
                        Toast.makeText(TaskShowActivity.this,"无法加载数据,请检查你的网络连接或者联系我们！",
                                Toast.LENGTH_LONG).show();
                        break;
                    case 1: //加载数据
                        setList();
                        break;
                    case 2://没有获取时，清除页面
                        Toast.makeText(TaskShowActivity.this, "没有相关的信息", Toast.LENGTH_LONG).show();
                        break;
                    case 3://设置答题卡
                        setExerciseCordList();
                        break;
                    case 4://显示附件
                        showFj();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    public void click(View v){
        if (v.getId()==R.id.tshow_retrun){
            if (todo.equals("do")){
                showAlertBC();
            }else {
                finish();
            }
        }else if (v.getId()==R.id.tshow_tj){
            saveDaAn();
            alertDialog= new MaterialDialog(this);
            alertDialog .setCanceledOnTouchOutside(false);
            alertDialog.setTitle("提示");
            alertDialog.setMessage("提交作业后不能再做修改，请问是否提交？");
            alertDialog.setNegativeButton("取消",new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.setPositiveButton("提交",new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    saveDaAn();
                    objectJson="[";
                    subjectJson="[";
                    for (QuestQuestionModel model:taskModel.getQuestQuestionList()){
                        String result="";
                        if (daAn.get(model.getId())!=null&&!daAn.get(model.getId()).equals("")){
                            result=daAn.get(model.getId());
                        }
                        if (model.getQuestionType().equals("01")||model.getQuestionType().equals("02")||model.getQuestionType().equals("03")){
                            objectJson=objectJson+"{\"questionId\":\""+model.getId()+"\",\"questionType\":\""+model.getQuestionType()+"\",\"optionId\":\""+result+"\"},";
                        }else {
                            subjectJson=subjectJson+"{\"questionId\":\""+model.getId()+"\",\"questionType\":\""+model.getQuestionType()+"\",\"answer\":\""+result+"\"},";
                        }
                    }
                    if (objectJson.length()>1){
                        objectJson=objectJson.substring(0,objectJson.length()-1)+"]";
                    }else {
                        objectJson=objectJson+"]";
                    }
                    if (subjectJson.length()>1){
                        subjectJson=subjectJson.substring(0,subjectJson.length()-1)+"]";
                    }else {
                        subjectJson=subjectJson+"]";
                    }
                    tjType="1";
                    //jsonObj.put("fj",map);
                    jsonObj.putAll(map);
                    alertDialog.dismiss();
                    saveOrSumbitTask();
                    returnAlert();
                }
            });
            alertDialog.show();
        }else if(v.getId()==R.id.tshow_tjfj){//添加附件按钮
            alertDialog=new MaterialDialog(TaskShowActivity.this);
            alertDialog.setTitle("提示");
            alertDialog.setMessage("请使用电脑上传附件");
            alertDialog .setCanceledOnTouchOutside(true);
            alertDialog.show();
            /*Intent intent = new Intent();
            intent.setClass(TaskShowActivity.this, ChoseFileToolActivity.class);
            startActivityForResult(intent, backCode);*/
        }else if(v.getId()==R.id.tshow_xzfj){
            String n=(String) v.getTag();
            String [] nString= n.split("##");
            //先判断附件是否存在，存在直接打开，不存在则下载
            File file=new File("");
            if (nString!=null&&nString.length==2){
                filePath=nString[0];
                name=nString[1];
                file=new File("/storage/emulated/0/Download/"+name);
            }
            /*if(file.exists()){
                Intent intent1 = new Intent();
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setAction(Intent.ACTION_VIEW);
                String type= getMIMEType(file);
                //设置intent的data和Type属性。
                intent1.setDataAndType(Uri.fromFile(file), type);
                startActivity(intent1);
            }else {*/
                alertDialog=new MaterialDialog(TaskShowActivity.this);
                alertDialog.setTitle("提示");
                alertDialog.setMessage("是否下载附件？");
                alertDialog.setNegativeButton("取消",new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setPositiveButton("下载",new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFj();
                        /*try{
                            //创建下载任务,downloadUrl就是下载链接
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://news.qq.com/newsgn/rss_newsgn.xml"));
                            //指定下载路径和下载文件名
                            request.setDestinationInExternalPublicDir("/download/", "rss_newsgn.xml");
                            //获取下载管理器
                            DownloadManager downloadManager= (DownloadManager) getApplication().getSystemService(Context.DOWNLOAD_SERVICE);
                            //将下载任务加入下载队列，否则不会进行下载
                            reference=downloadManager.enqueue(request);

                            //创建一个下载的广播,下载完成之后
                            IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                            BroadcastReceiver myReceiver = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    long referenceTo = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                                    if (referenceTo == reference) {
                                        Toast.makeText(getApplicationContext(), "文件下载完成", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                    }
                                }
                            };
                            //注册广播
                            registerReceiver(myReceiver, intentFilter);
                        }catch (Exception e){
                            e.printStackTrace();
                        }*/
                    }
                });
            alertDialog.show();
            //}
        }else{
            sd.close();
            saveDaAn();
            setExerciseCorlor2(questQuestionModel.getId());
            String exe=(String) v.getTag();
            String [] strings=exe.split("#");
            if(strings!=null&&strings.length==3){
                questionId=strings[0];
                index=Integer.parseInt(strings[1]);
                //index=Integer.parseInt(strings[2]);
                showTask(index);
            }
        }
    }

    //显示附件
    public void showFj(){
        alertDialog.dismiss();
        File file=new File("/storage/emulated/0/Download/"+name);
        Intent intent1 = new Intent();
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.setAction(Intent.ACTION_VIEW);
        String type= getMIMEType(file);
        //设置intent的data和Type属性。
        intent1.setDataAndType(Uri.fromFile(file), type);
        startActivity(intent1);
    }

    /**
     * 根据文件后缀名匹配MIMEType
     * @param file
     * @return
     */
    public static String getMIMEType(File file) {
        String type ="*/*";
        String name = file.getName();
        int index = name.lastIndexOf('.');
        if (index < 0) {
            return type;
        }
        String end = name.substring(index,name.length()).toLowerCase();
        if (TextUtils.isEmpty(end)) return type;

        for (int i = 0;i < MIME_MapTable.length;i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private static final String[][] MIME_MapTable={
            {".3gp",    "video/3gpp"},
            {".apk",    "application/vnd.android.package-archive"},
            {".asf",    "video/x-ms-asf"},
            {".avi",    "video/x-msvideo"},
            {".bin",    "application/octet-stream"},
            {".bmp",    "image/bmp"},
            {".c",  "text/plain"},
            {".class",  "application/octet-stream"},
            {".conf",   "text/plain"},
            {".cpp",    "text/plain"},
            {".doc",    "application/msword"},
            {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls",    "application/vnd.ms-excel"},
            {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe",    "application/octet-stream"},
            {".gif",    "image/gif"},
            {".gtar",   "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h",  "text/plain"},
            {".htm",    "text/html"},
            {".html",   "text/html"},
            {".jar",    "application/java-archive"},
            {".java",   "text/plain"},
            {".jpeg",   "image/jpeg"},
            {".jpg",    "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log",    "text/plain"},
            {".m3u",    "audio/x-mpegurl"},
            {".m4a",    "audio/mp4a-latm"},
            {".m4b",    "audio/mp4a-latm"},
            {".m4p",    "audio/mp4a-latm"},
            {".m4u",    "video/vnd.mpegurl"},
            {".m4v",    "video/x-m4v"},
            {".mov",    "video/quicktime"},
            {".mp2",    "audio/x-mpeg"},
            {".mp3",    "audio/x-mpeg"},
            {".mp4",    "video/mp4"},
            {".mpc",    "application/vnd.mpohun.certificate"},
            {".mpe",    "video/mpeg"},
            {".mpeg",   "video/mpeg"},
            {".mpg",    "video/mpeg"},
            {".mpg4",   "video/mp4"},
            {".mpga",   "audio/mpeg"},
            {".msg",    "application/vnd.ms-outlook"},
            {".ogg",    "audio/ogg"},
            {".pdf",    "application/pdf"},
            {".png",    "image/png"},
            {".pps",    "application/vnd.ms-powerpoint"},
            {".ppt",    "application/vnd.ms-powerpoint"},
            {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop",   "text/plain"},
            {".rc", "text/plain"},
            {".rmvb",   "audio/x-pn-realaudio"},
            {".rtf",    "application/rtf"},
            {".sh", "text/plain"},
            {".tar",    "application/x-tar"},
            {".tgz",    "application/x-compressed"},
            {".txt",    "text/plain"},
            {".wav",    "audio/x-wav"},
            {".wma",    "audio/x-ms-wma"},
            {".wmv",    "audio/x-ms-wmv"},
            {".wps",    "application/vnd.ms-works"},
            {".xml",    "text/plain"},
            {".z",  "application/x-compress"},
            {".zip",    "application/x-zip-compressed"},
            {"",        "*/*"}
    };

    /**
     * 根据路径生成一个Bitmap
     * @param path
     * @param w 指定宽
     * @param h 指定高
     * @return
     */
    public static Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        int inSampleSize = 1;
        if (height > w || width > h) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)h);
            } else {
                inSampleSize = Math.round((float)width / (float)w);
            }
        }
        opts.inSampleSize = inSampleSize;
        opts.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, opts);
    }

    public void saveDaAn(){
        if (questQuestionModel.getQuestionType().equals("02")){
            String str="";
            String str2="";//questQuestionModel.getQuestionType()+"#"+questQuestionModel.getId()+"";
            SparseBooleanArray booleanArray = lv_xx.getCheckedItemPositions();
            for (int j = 0; j < booleanArray.size(); j++) {
                int key = booleanArray.keyAt(j);
                if (booleanArray.get(key)) {
                    str2=str2+str+xxId.get(key);
                    str=",";
                } else {
                }
            }
            daAn.put(questQuestionModel.getId(),str2);
        }else if(questQuestionModel.getQuestionType().equals("04")){
            if (tshowEtList!=null&&tshowEtList.size()>0){
                String str="";
                for (EditText et:tshowEtList){
                    String s=et.getText()+"";
                    s=et.getTag()+"_"+s+"&";
                    str+=s;
                }
                daAn.put(questQuestionModel.getId(),str);
            }
        }else if (!questQuestionModel.getQuestionType().equals("01")&&!questQuestionModel.getQuestionType().equals("03")){
            String str=tshow_et.getText()+"";
            if (str!=null&&!str.equals("")){
                //String str2=questQuestionModel.getQuestionType()+"#"+questQuestionModel.getId()+"#"+str;
                daAn.put(questQuestionModel.getId(),str);
            }
        }
    }

    public void setList(){
        if (todo.equals("view")){
            tshow_tj.setVisibility(View.GONE);
        }

        showTask(index);
    }

    public void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("coursewareId", hdaplication.getCoursewareId());
                map.put("classId", hdaplication.getClassId());
                map.put("accountId", hdaplication.getStuid());
                map.put("operationType", "0");
                map.put("taskId", taskId);
                map.put("userName", hdaplication.getUsername());
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?doTask",
                        map,TaskShowActivity.this,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                    sendMessage(0);
                }else{
                    TaskShowReturn taskShowReturn= JsonUtil.parserString(result, TaskShowReturn.class);
                    if (taskShowReturn!=null){
                        taskModel=taskShowReturn.getTaskModel();
                        taskStudentId=taskShowReturn.getTaskStudentId();
                        if (taskShowReturn.getTaskStudentStutas().equals("0")){
                            todo="do";
                        }else {
                            todo="view";
                        }
                        JSONObject jsonObj = new JSONObject();
                        setAnswer(taskModel.getQuestQuestionList());
                        sendMessage(3);
                        sendMessage(1);
                    }
                }
            }
        }).start();
    }

    public void getFj(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("fileName", name);
                map.put("filePath", filePath);
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?getFj",
                        map,TaskShowActivity.this,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                    sendMessage(0);
                }else{
                    GetFjReturn getFjReturn= JsonUtil.parserString(result, GetFjReturn.class);
                    if (getFjReturn!=null){
                        File f = new File("/storage/emulated/0/Download/"+name);
                        //File f = new File("D:/android studio/"+"answer"+java.io.File.separator+taskSaveModel.getTitle()+"."+taskSaveModel.getHr());
                        byte[] in =  HQCodec.hexDecode(getFjReturn.getFileStr());
                        FileOutputStream fos;
                        try {
                            fos= new FileOutputStream(f);
                            fos.write(in, 0, in.length);
                            fos.flush();
                            sendMessage(4);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    public void saveOrSumbitTask(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("accountId", hdaplication.getStuid());
                map.put("userName", hdaplication.getUsername());
                map.put("taskStudentId",  taskStudentId);
                map.put("taskId", taskId);
                map.put("subjectJson", subjectJson);
                map.put("objectJson", objectJson);
                map.put("type", tjType);
                map.put("uuid", "");
                map.put("fj", jsonObj);
                //map.put("fileName", fileName);
                //map.put("fileStr", fileStr);
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?saveOrSumbitTask",
                        map,TaskShowActivity.this,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                    sendMessage(0);
                }else{
                    /*sendMessage(3);
                    sendMessage(1);*/
                    /*TaskShowReturn taskShowReturn= JsonUtil.parserString(result, TaskShowReturn.class);
                    if (taskShowReturn!=null){
                        taskModel=taskShowReturn.getTaskModel();
                        sendMessage(3);
                        sendMessage(1);
                    }*/
                }
            }
        }).start();
    }

    public void saveOrSumbitTaskJson(){

    }

    //答题卡数据
    public void setExerciseCordList(){
        try {
            if(taskModel!=null){
                WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                int width = wm.getDefaultDisplay().getWidth();
                int widths=width/36;
                tshow_ll.removeAllViews();
                List<QuestQuestionModel> questQuestionList=taskModel.getQuestQuestionList();
                TextView tView1=new TextView(getApplicationContext());
                String aString ="&nbsp&nbsp&nbsp&nbsp<img src=\""+R.drawable.u44+"\"  />:当前题 &nbsp&nbsp&nbsp <img src=\""+R.drawable.u43+"\"  />:答对  &nbsp&nbsp&nbsp<img src=\""+R.drawable.u42+"\"  />:答错   ";
                tView1.setText(Html.fromHtml(aString, imageGetter, null));
                tView1.setTextColor(0xff000000);
                tshow_ll.addView(tView1);
                String type="";
                String typeName="";
                int xuhao=0;
                TableLayout.LayoutParams layoutParams=new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                if(questQuestionList!=null&&questQuestionList.size()>0){
                    TableRow llayout = null;
                    for(int i=0;i<questQuestionList.size();i++){
                        QuestQuestionModel questQuestionModel=questQuestionList.get(i);
                        if (!questQuestionModel.getQuestionType().equals(type)){
                            if (xuhao%6!=0){
                                for (int a = xuhao%6; a < 6; a++) {
                                    TextView tv=new TextView(getApplicationContext());
                                    llayout.addView(tv);
                                }
                                tshow_ll.addView(llayout);
                            }
                            xuhao=0;
                            type=questQuestionModel.getQuestionType();
                            if (type.equals("01")){
                                typeName="单选题";
                            }else if (type.equals("02")){
                                typeName="多选题";
                            }else if (type.equals("03")){
                                typeName="判断题";
                            }else if (type.equals("04")){
                                typeName="填空题";
                            }else if (type.equals("05")){
                                typeName="简答题";
                            }else if (type.equals("06")){
                                typeName="论述题";
                            }else if (type.equals("07")){
                                typeName="材料题";
                            }else if (type.equals("08")){
                                typeName="名词解释题";
                            }
                            TextView tView=new TextView(getApplicationContext());
                            tView.setText(typeName);
                            tView.setTextColor(0xff000000);
                            tView.setPadding(10, 25, 10, 10);
                            tshow_ll.addView(tView);
                            layoutParams=new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        }
                        TableRow.LayoutParams itemParams = new TableRow.LayoutParams(widths*5 ,widths*5);
                        String result="";
                        itemParams.setMargins(widths/2, widths/4, widths/2, widths/4);
                        if(xuhao%6==0){
                            llayout=new TableRow(getApplicationContext());
                        }
                        Button childBtn = (Button)LayoutInflater.from(this).inflate(R.layout.exercise_button, null);
                        childBtn.setText((i+1)+"");
                        childBtn.setTag(questQuestionModel.getId()+"#"+(i+1)+"#"+xuhao);
                        childBtn.setId((i+1));
                        childBtn.setLayoutParams(itemParams);
                        //判断答题卡颜色
                        if (daAn.get(questQuestionModel.getId())!=null&&!daAn.get(questQuestionModel.getId()).equals("")){//已经作答
                            childBtn.setBackground(exeBtnCurror);
                        }
                        llayout.addView(childBtn);
                        llayout.setLayoutParams(layoutParams);
                        if (xuhao%6==5) {
                            tshow_ll.addView(llayout);
                        }
                        xuhao++;
                        if (i==questQuestionList.size()-1){
                            if (xuhao%6!=0){
                                for (int a = xuhao%6; a < 6; a++) {
                                    TextView tv=new TextView(getApplicationContext());
                                    llayout.addView(tv);
                                }
                                tshow_ll.addView(llayout);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(2);
        }
    }

    public void showTask(int ind){
        showLoading();//显示加载框
        tshow_tjfj.setText("添加附件");
        tshow_xzfj.setVisibility(View.GONE);
        TextView tshow_sd_tx=(TextView)findViewById(R.id.tshow_sd_tx);
        tshow_sd_tx.setText("全部"+ind+"/"+taskModel.getQuestQuestionList().size());
        tshow_result.removeAllViews();
        xxTitle.clear();
        xxMap.clear();
        xxBj.clear();
        xxId.clear();
        questQuestionModel=taskModel.getQuestQuestionList().get(ind-1);
        questionId=questQuestionModel.getId();
        lastButton=(Button)findViewById(ind);
        lastButton.setBackground(exeBtnNow);
        if(null != questQuestionModel.getQuestQuestionOptionList() &&questQuestionModel.getQuestQuestionOptionList().size()>0){
            tshow_tjfj.setVisibility(View.GONE);
            tshow_nr.setText(questQuestionModel.getQuestionContent());
            if(questQuestionModel.getQuestionType().equals("01")){
                imageSpan = new CustomImageSpan(this, R.drawable.icon_chose, 2);

                lv_xx.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//设置单选模式

                for(int i=0;i<questQuestionModel.getQuestQuestionOptionList().size();i++){
                    xxTitle.add(questQuestionModel.getQuestQuestionOptionList().get(i).getOptionContent());
                    xxId.add(questQuestionModel.getQuestQuestionOptionList().get(i).getId());
                }
            }else if(questQuestionModel.getQuestionType().equals("02")){
                imageSpan = new CustomImageSpan(this, R.drawable.icon_chose2, 2);
                lv_xx.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);//设置多选模式
                for(int i=0;i<questQuestionModel.getQuestQuestionOptionList().size();i++){
                    xxTitle.add(questQuestionModel.getQuestQuestionOptionList().get(i).getOptionContent());
                    xxId.add(questQuestionModel.getQuestQuestionOptionList().get(i).getId());
                }
            }else if(questQuestionModel.getQuestionType().equals("03")){
                imageSpan = new CustomImageSpan(this, R.drawable.icon_chose3, 2);
                lv_xx.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//设置单选模式

                for(int i=0;i<questQuestionModel.getQuestQuestionOptionList().size();i++){
                    xxTitle.add(questQuestionModel.getQuestQuestionOptionList().get(i).getOptionContent());
                    xxId.add(questQuestionModel.getQuestQuestionOptionList().get(i).getId());
                }
            }else if(questQuestionModel.getQuestionType().equals("04")){
                imageSpan = new CustomImageSpan(this, R.drawable.icon_chose7_2, 2);
                tshowEtList=new ArrayList<EditText>();
                for(int i=0;i<questQuestionModel.getQuestQuestionOptionList().size();i++){
                    LinearLayout layout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.task_show_da_item, null);
                    tshow_result.addView(layout);
                    tshow_et=(EditText)layout.findViewById(R.id.tshow_et);
                    tshow_et.setTag(questQuestionModel.getQuestQuestionOptionList().get(i).getId());
                    tshowEtList.add(tshow_et);
                }
            }

            if (questQuestionModel.getQuestionType().equals("04")){
                String [] daString=new String[]{};
                if (daAn.get(questQuestionModel.getId())!=null&&!daAn.equals("")){
                    daString=daAn.get(questQuestionModel.getId()).split("&");
                    if (daString!=null&&daString.length>0) {
                        for (int i=0;i<tshowEtList.size();i++) {
                            String tag=tshowEtList.get(i).getTag()+"";
                            for(int a=0;a<daString.length;a++){
                                if(daString[a].indexOf(tag)!=-1){
                                    String []da=daString[a].split("_");
                                    if (da!=null&&da.length==2){
                                        tshowEtList.get(i).setText(da[1]);
                                    }
                                }
                            }
                        }
                    }
                }
            }else {
                String [] daString1=new String[]{};
                if (daAn.get(questQuestionModel.getId())!=null&&!daAn.equals("")){
                    daString1=daAn.get(questQuestionModel.getId()).split(",");
                    //daString=daString[2].split("&");
                }
                if (daString1!=null&&daString1.length>0) {
                    for (int i=0;i<xxId.size();i++) {
                        for(int a=0;a<daString1.length;a++){
                            if(xxId.get(i).equals(daString1[a])){
                                xxBj.put(i+"", i+"");
                                daAnJl.put(questQuestionModel.getId(),i);
                            }
                        }
                    }
                }
            }
        }else{
            if(questQuestionModel.getQuestionType().equals("03")){
                tshow_tjfj.setVisibility(View.GONE);
                imageSpan = new CustomImageSpan(this, R.drawable.icon_chose3, 2);
                lv_xx.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//开启单选模式
                xxTitle.add("正确");
                xxId.add("0");
                xxTitle.add("错误");
                xxId.add("1");

                String [] daString=new String[]{};
                if (daAn.get(questQuestionModel.getId())!=null&&!daAn.equals("")){
                    daString=daAn.get(questQuestionModel.getId()).split(",");
                    //daString=daString[2].split("&");
                }
                if (daString!=null&&daString.length>0) {
                    for (int i=0;i<xxId.size();i++) {
                        for(int a=0;a<daString.length;a++){
                            if(xxId.get(i).equals(daString[a])){
                                xxBj.put(i+"", i+"");
                            }
                        }
                    }
                }
            }else{
                if (questQuestionModel.getAttachName()!=null&&!questQuestionModel.getAttachName().equals("")&&questQuestionModel.getAttachPath()!=null&&!questQuestionModel.getAttachPath().equals("")){
                    tshow_xzfj.setText(questQuestionModel.getAttachName());
                    tshow_xzfj.setTag(questQuestionModel.getAttachPath()+"##"+questQuestionModel.getAttachName());
                    tshow_xzfj.setVisibility(View.VISIBLE);
                }
                tshow_tjfj.setVisibility(View.VISIBLE);
                if(questQuestionModel.getQuestionType().equals("04")){
                    imageSpan = new CustomImageSpan(this, R.drawable.icon_chose7_2, 2);
                }else if(questQuestionModel.getQuestionType().equals("05")){
                    imageSpan = new CustomImageSpan(this, R.drawable.icon_chose5, 2);
                }else if(questQuestionModel.getQuestionType().equals("06")){
                    imageSpan = new CustomImageSpan(this, R.drawable.icon_chose6, 2);
                }else if(questQuestionModel.getQuestionType().equals("07")){
                    imageSpan = new CustomImageSpan(this, R.drawable.icon_chose10, 2);
                }else if(questQuestionModel.getQuestionType().equals("08")){
                    imageSpan = new CustomImageSpan(this, R.drawable.icon_chose4, 2);
                }else if(questQuestionModel.getQuestionType().equals("09")){
                    imageSpan = new CustomImageSpan(this, R.drawable.icon_chose8, 2);
                }else if(questQuestionModel.getQuestionType().equals("10")){
                    imageSpan = new CustomImageSpan(this, R.drawable.icon_chose9, 2);
                }

                LinearLayout layout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.task_show_da_item, null);
                tshow_result.addView(layout);
                tshow_et=(EditText)layout.findViewById(R.id.tshow_et);
                /*String [] daString=new String[]{};
                if (daAn.get(questionId)!=null&&!daAn.equals("")){
                    daString=daAn.get(questionId).split("#");
                }*/
                if (daAn.get(questQuestionModel.getId())!=null&&!daAn.get(questQuestionModel.getId()).equals("")) {
                    tshow_et.setText(daAn.get(questQuestionModel.getId())+"");
                }
                if (daAnfj.get(questQuestionModel.getId())!=null&&!daAnfj.get(questQuestionModel.getId()).equals("")){
                    tshow_tjfj.setText(daAnfj.get(questQuestionModel.getId()));
                }
                if (todo.equals("view")){
                    tshow_et.setFocusable(false);
                }
            }
        }
        SpannableString spannableString = new SpannableString("a "+index+"."+Html.fromHtml(questQuestionModel.getQuestionContent()));
        spannableString.setSpan(imageSpan, 0,1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tshow_nr.setText(spannableString);

        lv_xx.setAdapter(new MyAdapter());
        closeLoading();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //设置题目选项内容
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            //return lists.size();
            return xxTitle.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v;
            if (view==null){
                v=View.inflate(TaskShowActivity.this,R.layout.test_item,null);

            }else {
                v=view;
            }
            TextView tv_seekname=(TextView)v.findViewById(R.id.tvb);
            String string=xxTitle.get(i);
            String string2=xxMap.get(i+"");
            tv_seekname.setText("  "+Html.fromHtml(string));
            if (string2!=null&&string2.equals("0")) {
                tv_seekname.setCompoundDrawables(drawable_zq, null, null, null);
            }else if (string2!=null&&string2.equals("1")){
                tv_seekname.setCompoundDrawables(drawable_cw, null, null, null);
            }else {
                tv_seekname.setCompoundDrawables(ui_icon_chose0, null, null, null);
            }
            if (xxBj.get(i+"")!=null) {
                lv_xx.setItemChecked(i,true);
                if (string2 ==null) {
                    tv_seekname.setCompoundDrawables(ui_icon_chose1, null, null, null);
                }
                v.setBackgroundColor(0xfffec200);
            }else {
                v.setBackgroundColor(0xfffff);
            }

            return v;
        }
    }



    /**
     * 自定义imageSpan实现图片与文字的居中对齐
     */
    class CustomImageSpan extends ImageSpan {

        //自定义对齐方式--与文字中间线对齐
        private int ALIGN_FONTCENTER = 2;

        public CustomImageSpan(Context context, int resourceId) {
            super(context, resourceId);
        }

        public CustomImageSpan(Context context, int resourceId, int verticalAlignment) {
            super(context, resourceId, verticalAlignment);
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
                         Paint paint) {

            //draw 方法是重写的ImageSpan父类 DynamicDrawableSpan中的方法，在DynamicDrawableSpan类中，虽有getCachedDrawable()，
            // 但是私有的，不能被调用，所以调用ImageSpan中的getrawable()方法，该方法中 会根据传入的drawable ID ，获取该id对应的
            // drawable的流对象，并最终获取drawable对象
            Drawable drawable = getDrawable(); //调用imageSpan中的方法获取drawable对象
            canvas.save();

            //获取画笔的文字绘制时的具体测量数据
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();

            //系统原有方法，默认是Bottom模式)
            int transY = bottom - drawable.getBounds().bottom;
            if (mVerticalAlignment == ALIGN_BASELINE) {
                transY -= fm.descent;
            } else if (mVerticalAlignment == ALIGN_FONTCENTER) {    //此处加入判断， 如果是自定义的居中对齐
                //与文字的中间线对齐（这种方式不论是否设置行间距都能保障文字的中间线和图片的中间线是对齐的）
                // y+ascent得到文字内容的顶部坐标，y+descent得到文字的底部坐标，（顶部坐标+底部坐标）/2=文字内容中间线坐标
                transY = ((y + fm.descent) + (y + fm.ascent)) / 2 - drawable.getBounds().bottom / 2;
            }
            canvas.translate(x, transY);
            drawable.draw(canvas);
            canvas.restore();
        }
    }


    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    //触摸事件，左右滑动切换题目
    public boolean onTouch(View arg0, MotionEvent event) {


        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            //判断答题卡是否打开状态
            if (sdStatus) {//打开状态
                //sd.close();
                sdStatus=false;
            }else {
                x2 = event.getX();
                y2 = event.getY();
                if(x2-x1>150){//右滑动
                    if(index!=1){
                        setExerciseCorlor2(questionId);
                        saveDaAn();
                        index--;
                        questionId=taskModel.getQuestQuestionList().get(index-1).getId();
                        showTask(index);
                    }else {
                        Toast.makeText(TaskShowActivity.this, "当前题是第一题！", Toast.LENGTH_SHORT).show();
                    }
                }else if(x1-x2>150){//左滑动
                    if(index!=taskModel.getQuestQuestionList().size()){
                        saveDaAn();
                        setExerciseCorlor2(questionId);
                        index++;
                        questionId=taskModel.getQuestQuestionList().get(index-1).getId();
                        showTask(index);
                    }else {
                        Toast.makeText(TaskShowActivity.this, "当前题是最后一道题！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    //设置答题卡颜色标记
    public void setExerciseCorlor2(String qId) {
            if (daAn.get(qId)!=null&&!daAn.get(qId).equals("")){
                lastButton.setBackground(exeBtnCurror);
            }else {
                lastButton.setBackground(exeBorder);
            }
    }

    //解析答案
    public void setAnswer(List<QuestQuestionModel> questQuestionList){
        if (questQuestionList!=null&&questQuestionList.size()>0){
            for (QuestQuestionModel model:questQuestionList){
                if (model.getQuestionType().equals("01")||model.getQuestionType().equals("02")||model.getQuestionType().equals("03")){
                    if (model.getAccountSelectedOption()!=null&&!model.getAccountSelectedOption().equals("")){
                        daAn.put(model.getId(),model.getAccountSelectedOption());
                    }
                }else if(model.getQuestionType().equals("04")){
                    if (model.getAccountAnswer()!=null&&!model.getAccountAnswer().equals("")){
                        /*String str="";
                        for (StudentTaskModel m:model.getList()){
                            String s=m.getOptionId()+"_"+m.getAnswer();
                            str+=s+"&";
                        }*/
                        daAn.put(model.getId(),model.getAccountAnswer());
                    }
                }else{
                    if (model.getAccountAnswer()!=null&&!model.getAccountAnswer().equals("")){
                        daAn.put(model.getId(),model.getAccountAnswer());
                    }
                    //判断是否有附件
                    if(model.getAccountAttachName()!=null&&!model.getAccountAttachName().equals("")&&!model.getAccountAttachName().equals("null.null")){
                        daAnfj.put(model.getId(),model.getAccountAttachName());
                    }
                }
            }
        }
    }

    //返回退出app
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (todo.equals("do")){
                showAlertBC();
            }else {
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void returnAlert(){
        alertDialog=new MaterialDialog(TaskShowActivity.this);
        alertDialog.setTitle("提交成功");
        alertDialog.setMessage("是否返回作业首页");
        alertDialog.setNegativeButton("取消",new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                tshow_tj.setVisibility(View.GONE);
                todo="view";
                alertDialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("确定",new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog.show();
    }

    public void showAlertBC(){
        alertDialog=new MaterialDialog(TaskShowActivity.this);
        alertDialog.setTitle("提示");
        alertDialog.setMessage("是否保存作业");
        alertDialog.setNegativeButton("不保存",new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alertDialog.dismiss();
                finish();
            }
        } );
        alertDialog.setPositiveButton("保存",new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                saveDaAn();
                objectJson="[";
                subjectJson="[";
                for (QuestQuestionModel model:taskModel.getQuestQuestionList()){
                    String result="";
                    if (daAn.get(model.getId())!=null&&!daAn.get(model.getId()).equals("")){
                        result=daAn.get(model.getId());
                    }
                    if (model.getQuestionType().equals("01")||model.getQuestionType().equals("02")||model.getQuestionType().equals("03")){
                        objectJson=objectJson+"{\"questionId\":\""+model.getId()+"\",\"questionType\":\""+model.getQuestionType()+"\",\"optionId\":\""+result+"\"},";
                    }else {
                        subjectJson=subjectJson+"{\"questionId\":\""+model.getId()+"\",\"questionType\":\""+model.getQuestionType()+"\",\"answer\":\""+result+"\"},";
                    }
                }
                if (objectJson.length()>1){
                    objectJson=objectJson.substring(0,objectJson.length()-1)+"]";
                }else {
                    objectJson=objectJson+"]";
                }
                if (subjectJson.length()>1){
                    subjectJson=subjectJson.substring(0,subjectJson.length()-1)+"]";
                }else {
                    subjectJson=subjectJson+"]";
                }
                tjType="0";

                //jsonObj.put("fj",map);
                jsonObj.putAll(map);
                //jsonObj.writeJSONString();
                alertDialog.dismiss();
                saveOrSumbitTask();
                finish();
            }
        });
        alertDialog.show();
    }

    //选择附件返回事件
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == backCode){
            if(resultCode == ChoseFileToolActivity.resultCode){
                Bundle bundle = data.getExtras();
                String str = bundle.getString("current_path");
                String fileName = bundle.getString("fileName");
                String []name=fileName.split("\\.");
                if (name!=null&&name.length==2){
                    tshow_tjfj.setText(fileName);
                    if(str != null){
                        FileInputStream is = null;
                        try {
                            is = new FileInputStream(str);
                            byte[] in = new byte[is.available()];
                            is.read(in);
                            String fileStr = HQCodec.hexEncode(in);
                            TaskSaveModel taskSaveModel=new TaskSaveModel();
                            taskSaveModel.setQuestionId(questionId);
                            taskSaveModel.setJsonStr(fileStr);
                            taskSaveModel.setTitle(name[0]);
                            taskSaveModel.setHr(name[1]);
                            map.put(questionId,taskSaveModel);
                            list.add(taskSaveModel);
                            daAnfj.put(questionId,fileName);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else {

                }
            }
        }
    }
}
