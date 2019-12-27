package com.example.onlinelearnActivity.courseWare.practic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.view.MyBadgeView;
import com.example.util.NetWork;
import com.example.entity.DLPracticeAnswerDetailEntity;
import com.example.entity.DLPracticeQuestionActionEntity;
import com.example.entity.PracticLearnBehavior;
import com.example.entity.SDPracticeEntity;
import com.example.jdbc.DBManagerToDLPracticeAnswerDetail;
import com.example.jdbc.DBManagerToDLPracticeQuestionAction;
import com.example.jdbc.DBManagerToSDPractice;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.alert.HintPracticActivity;
import com.example.spt.jaxb.paper.ExerciseCard;
import com.example.spt.jaxb.paper.Option;
import com.example.spt.jaxb.paper.Question;
import com.example.spt.jaxb.paper.QuestionModel;
import com.example.spt.jaxb.paper.ResQuestionTypeRuleModel;
import com.example.util.ApplicationUtil;
import com.example.util.FinalStr;
import com.example.util.HttpUtil;
import com.example.util.PracticUtil;
import com.example.util.SysUtil;
import com.example.view.NoScrollListView;
import com.example.view.NoScrollListView.OnFlingListener;
/*import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;*/

import org.codehaus.jackson.map.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 练习页面
 */

@SuppressLint("NewApi")
public class PTestActivity extends Activity implements OnTouchListener, OnCheckedChangeListener {
    private static final String YOUR_PREF_FILE_NAME = null;
    RadioGroup rGroup;
    private String questionForm = "0";//题目形式   ： 所有   未做 错题 标记
    private Handler handler;//异步刷新组件
    private ApplicationUtil hdaplication;//全局参数对象
    private String questionId;//题目主键
    int questionType = 0;//题目类型 1单选 2多选 3判断 5 7简答  8名词解析
    RadioGroup p_rg_option;//单选 判断的按钮集合
    LinearLayout p_ll_option;//选项集合
    String optionId;//选项主键 选中
    Button btn;//提交按钮
    String todo;//批次主键 答题模式 或 背题模式
    int allQuestionCount = 0;
    List<CheckBox> checkBoxList;//多选按钮集合
    LinearLayout llyout, p_ll_tj, p_ll_da;
    String exerciseStatus;//练习状态 继续或重做
    TableLayout pe_ll;
    Button lastButton;
    String isright = "";
    String rightCount = "";
    String signString = "";
    TextView tv;
    TextView pq_nr;
    Map<String, Object> correctIdsAndErrorIds = new HashMap<String, Object>();
    Map<String, Object> signQuestionMap = new HashMap<String, Object>();
    Map<String, Object> errorQuestionMap = new HashMap<String, Object>();
    RelativeLayout p_ll_title;
    LinearLayout p_sl_ll;
    Drawable drawable_zq, drawable_cw, ui_icon_chose0;
    NoScrollListView lv_xx;
    //选项内容
    List<String> xxTitle = new ArrayList<String>();
    //选项Id
    List<String> xxId = new ArrayList<String>();
    Map<String, String> xxMap = new HashMap<String, String>();
    Map<String, String> xxBj = new HashMap<String, String>();
    private ScrollView gv;
    private SlidingDrawer sd;
    Drawable exeBtnError, exeBtnCurror, exeBtnNow, exeBorder, shape, shape2;
    Boolean sdStatus = false;//记录答题卡是否打开状态，初始值为false
    Html.ImageGetter imageGetter;
    CustomImageSpan imageSpan;
    List<Question> questions;
    ExerciseCard exerciseCard;
    Question question;
    String questionIndex = "1";
    Map<String, String> daAn = new HashMap<String, String>();
    Map<String, String> daAnJl = new HashMap<String, String>();
    int index = 0;
    private WindowManager mWindowManager = null;
    private WindowManager.LayoutParams wmParams = null;
    // 用于显示右下角浮动图标
    private ImageView img_Float;
    String batchId = "";
    RelativeLayout rl_yd;
    //ImageButton p_btn_sign;
    //网络状态
    Boolean netStatus = false;

    String jsonStr = "";//答案json
    String bjJsonStr = "";//标记json
    private String errorQuestionIdsStr;//所有批次错题主键串
    private DBManagerToDLPracticeAnswerDetail dbManagers; //sqlite操作对象
    private DBManagerToSDPractice dbManagers1; //sqlite操作对象dbManagers1
    private DBManagerToDLPracticeQuestionAction dbManagerToDLPracticeQuestionAction; //sqlite操作对象dbManagers1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice);
        setCache();
        //mdrawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        setUi();
        gv = (ScrollView) findViewById(R.id.p_sv);
        gv.setOnTouchListener(this);
        updateUi();
        getData();
        update();
        getQuestion(questionId);
        sendMessage(3);
        sendMessage(1);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
    }

    public void getQuestion(String questionid) {
        int indexQ = 0;
        if (questions != null) {
            for (Question q : questions) {
                if (q.getId().equals(questionid)) {
                    question = q;
                    index = indexQ;
                }
                indexQ++;
            }
        }
    }

    public void getQuestionIndex(String questionid) {
        if (exerciseCard != null) {
            for (ResQuestionTypeRuleModel q : exerciseCard.getQuestionsList()) {
                if (q != null) {
                    for (QuestionModel m : q.getQuestion()) {
                        if (m.getId().equals(questionid)) {
                            questionIndex = m.getQuestionIndex();
                        }
                    }
                }
            }
        }
    }

    public void setCache() {
        hdaplication = (ApplicationUtil) getApplication();
        hdaplication.addActivity(this);
    }

    public void update() {
        PracticLearnBehavior practicLearnBehavior = hdaplication.getPracticLearnBehavior();
        if (practicLearnBehavior != null) {
            String errorIds = practicLearnBehavior.getErrorIds();
            if (errorIds != null && !errorIds.equals("")) {
                String[] errorStr = errorIds.split("#");
                if (errorStr != null) {
                    for (String error : errorStr) {
                        if (!error.equals("")) {
                            correctIdsAndErrorIds.put(error, "错误");
                        }
                    }
                }
            }
            String correctIds = practicLearnBehavior.getCorrectIds();
            if (correctIds != null && !correctIds.equals("")) {
                String[] correctStr = correctIds.split("#");
                if (correctStr != null) {
                    for (String correct : correctStr) {
                        if (!correct.equals("")) {
                            correctIdsAndErrorIds.put(correct, "正确");
                        }
                    }
                }
            }
            String signIds = practicLearnBehavior.getSignQuestionIds();
            if (signIds != null && !signIds.equals("")) {
                String[] signStr = signIds.split("#");
                if (signStr != null) {
                    for (String sign : signStr) {
                        if (!signStr.equals("")) {
                            signQuestionMap.put(sign, "标记");
                        }
                    }
                }
            }
            errorQuestionIdsStr = practicLearnBehavior.getErrorQuestionIdsStr();
        }
        //获取题目主键串
        String questionIds = getQuestionIdsByTodo(questionForm);
      /*  questions = PracticUtil.getQuestionList(hdaplication.getPapers(), questionIds, questionForm);*/
        exerciseCard = PracticUtil.getExerciseCard(hdaplication.getExerciseCard(), questionIds, questionForm);
        for (int z=0;z<exerciseCard.getQuestionsList().size();z++){
            if (exerciseCard.getQuestionsList().get(z).getQuestion().size()!=0){
                questionId = exerciseCard.getQuestionsList().get(z).getQuestion().get(0).getId();
                questionIndex = exerciseCard.getQuestionsList().get(z).getQuestion().get(0).getQuestionIndex();
                break;
            }
        }
        allQuestionCount = questions.size();
    }

    public String getQuestionIdsByTodo(String todo) {
        String questionIds = "";
        switch (todo) {
            case "0":
                break;
            case "1":
                questionIds = hdaplication.getPracticLearnBehavior().getCorrectIds() + "#" + hdaplication.getPracticLearnBehavior().getErrorIds();
                break;
            case "2":
                questionIds = hdaplication.getPracticLearnBehavior().getErrorQuestionIdsStr();
                break;
            case "3":
                questionIds = hdaplication.getPracticLearnBehavior().getSignQuestionIds();
                break;
            case "5":
                questionIds = hdaplication.getPracticLearnBehavior().getErrorQuestionIdsStr()+"-"+hdaplication.getPracticLearnBehavior().getCorrectIds();
                break;
            case "6":
                questionIds = hdaplication.getPracticLearnBehavior().getErrorIds();
                break;
        }
        return questionIds;
    }

    @SuppressWarnings("deprecation")
    public void setUi() {
        showLoading();
        //判断是否有网络
        netStatus = NetWork.isNetworkConnected(PTestActivity.this);
        dbManagers = new DBManagerToDLPracticeAnswerDetail(getApplicationContext());
        dbManagers1 = new DBManagerToSDPractice(getApplicationContext());
        dbManagerToDLPracticeQuestionAction = new DBManagerToDLPracticeQuestionAction(getApplicationContext());
        //判断是否第一次启动该程序
        SharedPreferences setting = getSharedPreferences(YOUR_PREF_FILE_NAME, 0);
        Boolean user_first = setting.getBoolean("FIRST", true);
        if (user_first) {//第一次
            setting.edit().putBoolean("FIRST", false).commit();
            //弹出手指滑动切换题目的提示
            Intent intent1 = new Intent();
            intent1.setClass(PTestActivity.this, HintPracticActivity.class);
            startActivity(intent1);
        }
        tv = (TextView) findViewById(R.id.p_btn_sign);
        p_ll_title = (RelativeLayout) findViewById(R.id.p_ll_title);
        p_sl_ll = (LinearLayout) findViewById(R.id.p_sl_ll);

        //答题卡
        sd = (SlidingDrawer) findViewById(R.id.sliding);
        int[] mTouchableIds = {R.id.p_btn_sign};
        //答题卡打开时执行
        sd.setOnDrawerOpenListener(new OnDrawerOpenListener() {
            public void onDrawerOpened() {
                sdStatus = true;
                p_sl_ll.setBackgroundColor(0x509D9D9D);
                tv.setFocusable(false);
                tv.setClickable(false);
            }
        });
        //答题卡关闭时执行
        sd.setOnDrawerCloseListener(new OnDrawerCloseListener() {
            public void onDrawerClosed() {
                sdStatus = false;
                p_sl_ll.setBackgroundColor(0x99FFF);
                tv.setFocusable(true);
                tv.setClickable(true);
                tv.requestFocus();
            }
        });
        //选项自定义listview
        lv_xx = (NoScrollListView) findViewById(R.id.lv_xx);
        //点击选项
        lv_xx.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (daAn.get(questionId) == null && !todo.equals("view")) {
                    if (question.getType().equals("1") || question.getType().equals("3")) {
                        arg1.setBackgroundColor(0xfffec200);
                        optionId = xxId.get(arg2);
                        if (optionId != null && !optionId.equals("")) {
                            //optionId=getExerciseReturn.getCurrentQuesiton().getType()+"#"+questionId+"&"+optionId;
                            daAn.put(questionId, optionId);
                            boolean sure = submitExerciseQuestion(optionId, question);
                            setExerciseCorlor2(questionId);
                            if (index < questions.size() - 1 && sure) {
                                index++;
                                Question question2 = questions.get(index);
                                questionId = question2.getId();
                                getQuestion(questionId);
                                getQuestionIndex(questionId);
                            }
                            setList();
                        }
                    } else if (question.getType().equals("2")) {
                        SparseBooleanArray booleanArray = lv_xx.getCheckedItemPositions();
                        if (booleanArray.get(arg2)) {
                            arg1.setBackgroundColor(0xfffec200);
                        } else {
                            arg1.setBackgroundColor(0xfffff);
                        }
                    }
                }
            }
        });
        lv_xx.setOnFlingListener(new OnFlingListener() {

            @Override
            public void onLeftFling() {
                if (index < questions.size() - 1) {
                    index++;
                    Question question2 = questions.get(index);
                    setExerciseCorlor2(questionId);
                    questionId = question2.getId();
                    getQuestion(questionId);
                    getQuestionIndex(questionId);
                    setList();
                } else {
                    Toast.makeText(PTestActivity.this, "当前题是最后一道题！", Toast.LENGTH_SHORT).show();
                }
                /*if(getExerciseReturn.getNextQuestion()!=null){
                    setExerciseCorlor2(questionId);
                    questionId=getExerciseReturn.getNextQuestion().getId();
                    getData2();
                }else {
                    Toast.makeText(PTestActivity.this, "当前题是最后一道题！", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onRightFling() {
                if (index > 0) {
                    index--;
                    Question question2 = questions.get(index);
                    setExerciseCorlor2(questionId);
                    questionId = question2.getId();
                    getQuestion(questionId);
                    getQuestionIndex(questionId);
                    setList();
                } else {
                    Toast.makeText(PTestActivity.this, "当前题是第一题！", Toast.LENGTH_SHORT).show();
                }

                /*if(getExerciseReturn.getLastQuestion()!=null){
                    setExerciseCorlor2(questionId);
                    questionId=getExerciseReturn.getLastQuestion().getId();
                    getData2();
                }else {
                    Toast.makeText(PTestActivity.this, "当前题是第一题！", Toast.LENGTH_SHORT).show();
                }*/
            }


        });
        lastButton = new Button(getApplicationContext());
        pe_ll = (TableLayout) findViewById(R.id.pe_ll);
        p_ll_option = (LinearLayout) findViewById(R.id.p_ll_option);
        checkBoxList = new ArrayList<CheckBox>();
        rGroup = (RadioGroup) findViewById(R.id.practice_rg);
        rGroup.setOnCheckedChangeListener((OnCheckedChangeListener) this);
        btn = (Button) findViewById(R.id.p_btn_tj);
        llyout = (LinearLayout) findViewById(R.id.p_ll_status);
        p_ll_tj = (LinearLayout) findViewById(R.id.p_ll_tj);
        p_ll_da = (LinearLayout) findViewById(R.id.p_ll_da);

        //通过意图，获取答题方式todo和题目形式questionForm
        Intent intent = getIntent();
        String string = intent.getStringExtra("batchId");
        if (string != null && !string.equals("null") && !string.equals("")) {
            String[] strlist = string.split("#");
            for (int i = 0; i < strlist.length; i++) {
                if (i == 0) {
                    todo = strlist[i];
                } else if (i == 1) {
                    questionForm = strlist[i];
                } else if (i == 3) {
                    batchId = strlist[i];
                }
            }
        }
        if (batchId.equals("")) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
            batchId = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        }
        //初始化各种图片背景
        shape = getResources().getDrawable(R.drawable.shape);//蓝色弧边按钮
        shape.setBounds(0, 0, shape.getMinimumWidth(), shape.getMinimumHeight());  //此为必须写的
        shape2 = getResources().getDrawable(R.drawable.shape2);//灰色弧边按钮
        shape2.setBounds(0, 0, shape2.getMinimumWidth(), shape2.getMinimumHeight());  //此为必须写的
        drawable_zq = getResources().getDrawable(R.drawable.ui_icon_chose_current);//选项答对图片
        drawable_zq.setBounds(0, 0, drawable_zq.getMinimumWidth(), drawable_zq.getMinimumHeight());  //此为必须写的
        drawable_cw = getResources().getDrawable(R.drawable.ui_icon_chose_error);//选项答错图片
        drawable_cw.setBounds(0, 0, drawable_cw.getMinimumWidth(), drawable_cw.getMinimumHeight());  //此为必须写的
        ui_icon_chose0 = getResources().getDrawable(R.drawable.ui_icon_chose0);//选项默认图片
        ui_icon_chose0.setBounds(0, 0, ui_icon_chose0.getMinimumWidth(), ui_icon_chose0.getMinimumHeight());  //此为必须写的
        //初始化答题卡按钮颜色
        exeBtnError = getResources().getDrawable(R.drawable.exe_btn_error1);//错题 红色
        exeBtnError.setBounds(0, 0, exeBtnError.getMinimumWidth(), exeBtnError.getMinimumHeight());
        exeBtnCurror = getResources().getDrawable(R.drawable.exe_btn_curror1);//对题 绿色
        exeBtnCurror.setBounds(0, 0, exeBtnCurror.getMinimumWidth(), exeBtnCurror.getMinimumHeight());
        exeBtnNow = getResources().getDrawable(R.drawable.exe_btn_now);//当前题 蓝色
        exeBtnNow.setBounds(0, 0, exeBtnNow.getMinimumWidth(), exeBtnNow.getMinimumHeight());
        exeBorder = getResources().getDrawable(R.drawable.btn_border);//未做 透明
        exeBorder.setBounds(0, 0, exeBorder.getMinimumWidth(), exeBorder.getMinimumHeight());

        imageGetter = new Html.ImageGetter() {

            public Drawable getDrawable(String source) {
                Drawable drawable = null;
                int rId = Integer.parseInt(source);
                drawable = getResources().getDrawable(rId);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            }

            ;
        };

    }

    public void updateUi() {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: //无法加载数据
                        closeLoading();
                        Toast.makeText(PTestActivity.this, "无法加载数据,请检查你的网络连接或者联系我们！",
                                Toast.LENGTH_LONG).show();
                        break;
                    case 1: //加载数据
                        setList();
                        break;
                    case 2://没有获取时，清除页面
                        removeview();
                        closeLoading();
                        Toast.makeText(PTestActivity.this, "没有相关的信息", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        //设置答题卡
                        setExerciseCordList();
                        break;
                    case 4:
                        //设置答题卡按钮变化
                        setExerciseCorlor();
                        break;
                    case 5:
                        //设置标记
                        setSign();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public void setList() {
        try {
            showLoading();
            //设置答题卡旁边的当前题型：总数/当前题
            TextView p_sd_tx = (TextView) findViewById(R.id.p_sd_tx);
            //setTitletext();
            llyout.setVisibility(View.GONE);
            p_ll_tj.setVisibility(View.GONE);
            p_ll_da.setVisibility(View.GONE);
            p_rg_option = new RadioGroup(getApplicationContext());
            p_ll_option.removeAllViews();
            //pq_tx=(TextView)findViewById(R.id.pq_tx);
            //pq_th=(TextView)findViewById(R.id.pq_th);
            pq_nr = (TextView) findViewById(R.id.pq_nr);
            if (question != null) {
                //判断标记
                if (signQuestionMap.get(question.getId()) != null && signQuestionMap.get(question.getId()).equals("标记")) {
                    tv.setText("★");
                } else {
                    tv.setText("☆");
                }
                btn.setBackground(shape);
                btn.setEnabled(true);
                //pq_th.setText(questionIndex+".");//设置题目序号
                String aString = "";
                SpannableString spannableString = new SpannableString("a " + questionIndex + "." + Html.fromHtml(question.getContent()));


                String ids = question.getType() + questionIndex;
                int idInt = Integer.parseInt(ids);
                lastButton = (Button) findViewById(idInt);
                lastButton.setBackground(exeBtnNow);
                String tx = "题型";
                int sum = 0;
                xxTitle.clear();
                xxMap.clear();
                xxBj.clear();
                xxId.clear();
                if (null != question.getOption() && question.getOption().size() > 0) {//判断题型，设置选项
                    if (question.getType().equals("1")) {
                        //aString ="<div style='width:20%;float:left;'><img src=\""+R.drawable.icon_chose+"\"  /></div><div style='float:left;'> "+questionIndex+"."+question.getContent()+"</div>";
                        //调用自定义的imageSpan,实现文字与图片的横向居中对齐
                        imageSpan = new CustomImageSpan(this, R.drawable.icon_chose, 2);

                        tx = "单选题";
                        lv_xx.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//开启单选模式
                        //LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); // 每行的水平LinearLayout

                        for (int i = 0; i < question.getOption().size(); i++) {
                            xxTitle.add(question.getOption().get(i).getContent());
                            xxId.add(question.getOption().get(i).getId());
                        }
                        //p_ll_option.addView(p_rg_option);
                    } else if (question.getType().equals("2")) {
                        btn.setText("提交");
                        //aString ="<img src=\""+R.drawable.icon_chose2+"\" style='float:left;' /> "+" "+questionIndex+"."+question.getContent();
                        imageSpan = new CustomImageSpan(this, R.drawable.icon_chose2, 2);
                        tx = "多选题";
                        lv_xx.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);//开启多选模式
                        for (int i = 0; i < question.getOption().size(); i++) {
                            xxTitle.add(question.getOption().get(i).getContent());
                            xxId.add(question.getOption().get(i).getId());
                        }
                    } else if (question.getType().equals("4")) {
                        btn.setText("显示参考答案");
                        //aString ="<img src=\""+R.drawable.icon_chose2+"\" style='float:left;' /> "+" "+questionIndex+"."+question.getContent();
                        imageSpan = new CustomImageSpan(this, R.drawable.icon_chose7_2, 2);
                        tx = "填空题";
                    }
                } else {
                    if (question.getType().equals("3")) {
                        //aString ="<img src=\""+R.drawable.icon_chose3+"\" style='float:left;' /> "+questionIndex+"."+question.getContent();
                        imageSpan = new CustomImageSpan(this, R.drawable.icon_chose3, 2);
                        tx = "判断题";
                        lv_xx.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//开启单选模式
                        xxTitle.add("正确");
                        xxId.add("0");
                        xxTitle.add("错误");
                        xxId.add("1");
                    } else {
                        btn.setText("显示参考答案");
                        if (question.getType().equals("5")) {
                            //aString ="<img src=\""+R.drawable.icon_chose6+"\" style='float:left;' /> "+questionIndex+"."+question.getContent();
                            imageSpan = new CustomImageSpan(this, R.drawable.icon_chose6, 2);
                            tx = "论述题";
                        } else if (question.getType().equals("7")) {
                            //aString ="<img src=\""+R.drawable.icon_chose5+"\" style='float:left;' /> "+questionIndex+"."+question.getContent();
                            imageSpan = new CustomImageSpan(this, R.drawable.icon_chose5, 2);
                            tx = "简答题";
                        } else if (question.getType().equals("8")) {
                            //aString ="<img src=\""+R.drawable.icon_chose4+"\" style='float:left;' /> "+questionIndex+"."+question.getContent();
                            imageSpan = new CustomImageSpan(this, R.drawable.icon_chose4, 2);
                            tx = "名词解析题";
                        }
                    }
                }
                spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                pq_nr.setText(spannableString);

                //pq_nr.setText(Html.fromHtml(aString, imageGetter, null));//设置题目内容
                String[] ss = (lastButton.getTag() + "").split("#");
                int in = index + 1;
                if (questionForm.equals("0")) {
                    p_sd_tx.setText("全部：" + in + "/" + allQuestionCount);
                } else if (questionForm.equals("1")) {
                    p_sd_tx.setText("未做: " + allQuestionCount);
                } else if (questionForm.equals("2") || questionForm.equals("5") || questionForm.equals("6")) {
                    p_sd_tx.setText("错题: " + in + "/" + allQuestionCount);
                } else if (questionForm.equals("3")) {
                    p_sd_tx.setText("标记: " + in + "/" + allQuestionCount);
                }
                //pq_tx.setText(tx);
                if (todo.equals("view")) {
                    btn.setVisibility(View.GONE);
                } else {
                    if (question.getType().equals("1") || question.getType().equals("3")) {
                        btn.setVisibility(View.GONE);
                    } else {
                        btn.setVisibility(View.VISIBLE);
                    }
                }

                if (daAnJl.get(questionId) != null || todo.equals("view")) {

                    if (question.getType().equals("1")) {
                        for (int i = 0; i < xxTitle.size(); i++) {
                            if (question.getOption().get(i).getOptionIsCorrent().equals("0")) {
                                //xxTitle.set(i, xxTitle.get(i)+"#0");
                                xxMap.put(i + "", 0 + "");
                            }
                            if (!todo.equals("view")) {
                                if (daAnJl.get(questionId).equals("1")) {
                                    if (xxId.get(i).equals(daAn.get(questionId))) {
                                        //xxTitle.set(i, xxTitle.get(i)+"#1");
                                        xxMap.put(i + "", 1 + "");
                                        xxBj.put(i + "", i + "");
                                    }
                                }
                            }
                        }
                    } else if (question.getType().equals("2")) {
                        if (!todo.equals("view")) {
                            if (daAnJl.get(questionId).equals("1")) {
                                for (int i = 0; i < xxTitle.size(); i++) {
                                    if (daAn.get(questionId) != null) {
                                        if (daAn.get(questionId).indexOf(xxId.get(i)) != -1) {
                                            xxMap.put(i + "", 1 + "");
                                            xxBj.put(i + "", i + "");
                                        }
                                    }
                                    if (question.getOption().get(i).getOptionIsCorrent().equals("0")) {
                                        xxMap.put(i + "", 0 + "");
                                    }
                                }
                            }

                        } else {
                            for (int i = 0; i < xxTitle.size(); i++) {
                                if (question.getOption().get(i).getOptionIsCorrent().equals("0")) {
                                    xxMap.put(i + "", 0 + "");
                                }
                            }
                        }
                    } else if (question.getType().equals("3")) {
                        for (int i = 0; i < xxId.size(); i++) {
                            if (question.getAnswer().getContent().equals(i + "")) {
                                xxMap.put(i + "", 0 + "");
                            }
                            if (!todo.equals("view")) {
                                if (daAnJl.get(questionId).equals("1")) {
                                    if (xxId.get(i).equals(daAn.get(questionId))) {
                                        xxMap.put(i + "", 1 + "");
                                        xxBj.put(i + "", i + "");
                                    }
                                }
                            }
                        }

                    } else if (question.getType().equals("4")) {
                        p_ll_da.setVisibility(View.VISIBLE);
                        TextView p_tv_da = (TextView) findViewById(R.id.p_tv_da);
                        List<Option> options = question.getOption();
                        String textDa = "";
                        String textDh = "";
                        if (options != null && options.size() > 0) {
                            for (int i = 0; i < options.size(); i++) {
                                textDa = textDa + textDh + options.get(i).getContent();
                                textDh = "、";
                            }
                        }
                        p_tv_da.setText(textDa);
                    }
                    btn.setBackground(shape2);
                    btn.setEnabled(false);
                    //DLPracticeAnswerModel learnPracAnsRecordEntity=learnPracAnsRecordEntityList.get(0);
                    TextView p_tv_jx = (TextView) findViewById(R.id.p_tv_jx);

                    String explain = "略";
                    if (question.getExplain() != null) {
                        explain = question.getExplain().getContent();
                    }
                    p_tv_jx.setText(explain);
                    String zsd = "";
                    if (question.getCoursenodes() != null && question.getCoursenodes().size() > 0) {
                        for (int i = 0; i < question.getCoursenodes().size(); i++) {
                            zsd += " " + question.getCoursenodes().get(i).getNodename();
                        }
                    }
                    TextView p_tv_zsd = (TextView) findViewById(R.id.p_tv_zsd);
                    p_tv_zsd.setText(zsd);
                    //题型大于三，即简答题等不用作答的题目
                    /*if (Integer.parseInt(question.getType())<=3) {
                        TextView p_tv_zql=(TextView)findViewById(R.id.p_tv_zql);
                        TextView p_tv_dtcs=(TextView)findViewById(R.id.p_tv_dtcs);
                        TextView p_tv_zq=(TextView)findViewById(R.id.p_tv_zq);
                        TextView p_tv_cw=(TextView)findViewById(R.id.p_tv_cw);
                        TextView p_tv_zjyc=(TextView)findViewById(R.id.p_tv_zjyc);
                        p_tv_zql.setText(getExerciseReturn.getAccuracy()+"%");
                        p_tv_dtcs.setText(getExerciseReturn.getSum());
                        p_tv_zq.setText(getExerciseReturn.getRightCount());
                        p_tv_cw.setText(getExerciseReturn.getWrongCount());
                        p_tv_zjyc.setText(getExerciseReturn.getAnswerStatus());
                        p_ll_tj.setVisibility(View.VISIBLE);
                    }*/
                    llyout.setVisibility(View.VISIBLE);
                    //learnPracAnsRecordList=getExerciseReturn.getLearnPracAnsRecordList();
                }

                lv_xx.setAdapter(new MyAdapter());

                closeLoading();
                //}
            } else {
                sendMessage(2);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(2);
        }
    }

    public void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
               /* Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("exerciseStatus", exerciseStatus);
                map.put("todo", todo);
                map.put("batchId", hdaplication.getBatchId());
                map.put("type", "0");
                map.put("coursewareId", hdaplication.getCoursewareId());
                map.put("classId", hdaplication.getClassId());
                map.put("behaviorId", hdaplication.getBehaviorId());
                map.put("accountId", hdaplication.getStuid());
                map.put("questionForm", questionForm);
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?gotoExercise",
                        map,PTestActivity.this,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                    sendMessage(0);
                }else{
                    GotoExerciseReturn gotoExerciseReturn=JsonUtil.parserString(result, GotoExerciseReturn.class);
                    if(gotoExerciseReturn!=null){
                        questionId=gotoExerciseReturn.getCurrentQuestionId();
                        hdaplication.setBatchId(gotoExerciseReturn.getBatchId());
                        exerciseCard=gotoExerciseReturn.getExerciseCard();
                        allQuestionCount=gotoExerciseReturn.getQuestionSum();
                        String correctIds=gotoExerciseReturn.getCorrectIds();
                        int count=0;
                        if (correctIds!=null&&!correctIds.equals("")) {
                            String[] correctStrings=correctIds.split("#");
                            if(correctStrings!=null){
                                count=count+correctStrings.length;
                                for(int i=0;i<correctStrings.length;i++){
                                    correctIdsAndErrorIds.put(correctStrings[i], "正确");
                                }
                            }
                        }
                        String errorIds=gotoExerciseReturn.getErrorIds();
                        if (errorIds!=null&&!errorIds.equals("")) {
                            String[] errorStrings=errorIds.split("#");
                            if(errorStrings!=null){
                                count=count+errorStrings.length;
                                for(int i=0;i<errorStrings.length;i++){
                                    correctIdsAndErrorIds.put(errorStrings[i], "错误");
                                }
                            }
                        }
                        //unfinishedQuestionCount=allQuestionCount-count;
                        String signQuestionIds=gotoExerciseReturn.getSignQuestionIds();
                        if (signQuestionIds!=null&&!signQuestionIds.equals("")) {
                            String[] signStrings=signQuestionIds.split("#");
                            if(signStrings!=null){
                                for(int i=0;i<signStrings.length;i++){
                                    signQuestionMap.put(signStrings[i], "标记");
                                }
                            }
                        }
                        String errorQuestionIds=gotoExerciseReturn.getErrorQuestionIdsStr();
                        if (errorQuestionIds!=null&&!errorQuestionIds.equals("")) {
                            String[] errorStrings=errorQuestionIds.split("#");
                            if(errorStrings!=null){
                                for(int i=0;i<errorStrings.length;i++){
                                    if (errorQuestionMap.get(errorStrings[i])==null) {
                                        errorQuestionMap.put(errorStrings[i], "错题");
                                    }
                                }
                            }
                        }
                        getData2();
                        sendMessage(3);
                    }else{
                        sendMessage(0);
                    }
                }*/
            }
        }).start();
    }

    public void getData2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("todo", todo);
                map.put("batchId", hdaplication.getBatchId());
                map.put("type", "0");
                map.put("coursewareId", hdaplication.getCoursewareId());
                map.put("classId", hdaplication.getClassId());
                map.put("behaviorId", hdaplication.getBehaviorId());
                map.put("accountId", hdaplication.getStuid());
                map.put("questionId", questionId);
                map.put("questionForm", questionForm);
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?getExerciseQuestion",
                        map,PTestActivity.this,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                    sendMessage(0);
                }else{
                    getExerciseReturn=JsonUtil.parserString(result, GetExerciseReturn.class);
                    if(getExerciseReturn!=null){
                        sendMessage(1);
                    }else{
                        sendMessage(0);
                    }
                }*/
            }
        }).start();
    }

    public void getData3() {//提交
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>(0);
                map.put("jsonStr", jsonStr);//答案json
                map.put("type", "0");
                map.put("coursewareId", hdaplication.getCoursewareId());
                map.put("classId", hdaplication.getClassId());
                map.put("behaviorId", hdaplication.getBehaviorId());
                map.put("accountId", hdaplication.getStuid());
                map.put("questionType", "1");
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH + "/appControler.do?submitExerciseQuestion",
                        map, PTestActivity.this, null);
                if (SysUtil.isBlank(result) || "NO_NET".equals(result)) {
                    sendMessage(0);
                } else {
                    dbManagers.deleteUser();
                }
            }
        }).start();
    }

    public void getData4() {//标记
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>(0);
                map.put("accountId", hdaplication.getStuid());
                map.put("bjJsonStr", bjJsonStr);//标记json
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH + "/appControler.do?signQuestion",
                        map, PTestActivity.this, null);
                if (SysUtil.isBlank(result) || "NO_NET".equals(result)) {
                    sendMessage(0);
                } else {
                    /*signString="修改标记失败";
                    if(result.equals("\"标记成功\"")){
                        signQuestionMap.put(questionId, "标记");
                        signString="标记成功";
                    }else if(result.equals("\"取消标记成功\"")){
                        signQuestionMap.remove(questionId);
                        signString="取消标记成功";
                    }*/
                    dbManagerToDLPracticeQuestionAction.deleteUser();
                }
            }
        }).start();
    }

    private void sendMessage(int str) {
        Message msg = new Message();
        msg.what = str;
        PTestActivity.this.handler.sendMessage(msg);
    }

    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        exerciseStatus = "continue";
        if (id == R.id.btn_do) {
            todo = "do";
        } else if (id == R.id.btn_recite) {
            todo = "view";
        }
        setList();
    }

    public void click(View v) {
        if (v.getId() == R.id.p_btn_tj) {//提交
            boolean sure = false;
            if (question.getType().equals("2")) {
                String str = "";
                optionId = "";
                SparseBooleanArray booleanArray = lv_xx.getCheckedItemPositions();
                for (int j = 0; j < booleanArray.size(); j++) {
                    int key = booleanArray.keyAt(j);
                    //放入SparseBooleanArray，未必选中
                    if (booleanArray.get(key)) {
                        //这样mAdapter.getItem(key)就是选中的项
                        //optionId=optionId+str+xxId.get(key);
                        optionId += str + xxId.get(key);
                        str = "&";
                    } else {
                        //这里是用户刚开始选中，后取消选中的项
                    }
                }
                if (!optionId.equals("")) {
                    //optionId=question.getType()+"#"+questionId+"&"+optionId;
                    daAn.put(questionId, optionId);//getData3();
                    sure = submitExerciseQuestion(optionId, question);
                }
            } else {
                optionId = question.getType();
                daAn.put(questionId, optionId);
                sure = submitExerciseQuestion(optionId, question);
            }
            if (question.getType().equals("2")){
                setExerciseCorlor2(questionId);
                if (index < questions.size() - 1 && sure) {
                    index++;
                    Question question2 = questions.get(index);
                    questionId = question2.getId();
                    getQuestion(questionId);
                    getQuestionIndex(questionId);
                }
            }
            setList();
        } else if (v.getId() == R.id.p_btn_sign) {//标记
            DLPracticeQuestionActionEntity dlPracticeQuestionActionEntity = new DLPracticeQuestionActionEntity();
            dlPracticeQuestionActionEntity.setAccountId(hdaplication.getStuid());
            dlPracticeQuestionActionEntity.setBehaviorId(hdaplication.getBehaviorId());
            dlPracticeQuestionActionEntity.setQuestionId(questionId);
            dlPracticeQuestionActionEntity.setClassId(hdaplication.getClassId());
            dlPracticeQuestionActionEntity.setQuestionType(questionType + "");
            dlPracticeQuestionActionEntity.setId("bj" + questionId + hdaplication.getStuid());
            if (signQuestionMap.get(questionId) != null && !signQuestionMap.get(questionId).equals("")) {
                signQuestionMap.remove(questionId);
                dlPracticeQuestionActionEntity.setIsMarked("1");
                signString = "取消标记成功";
            } else {
                signQuestionMap.put(questionId, "标记");
                dlPracticeQuestionActionEntity.setIsMarked("0");
                signString = "标记成功";
            }
            dbManagerToDLPracticeQuestionAction.saveEntity(dlPracticeQuestionActionEntity);
            setSign();
        } else if (v.getId() == R.id.p_btn_exerciseCard) {//答题卡
            //mdrawerLayout.openDrawer(Gravity.START);
        } else if (v.getId() == R.id.p_retrun) {
            tj();
            this.finish();
        } else {
            sd.close();
            setExerciseCorlor2(questionId);
            String exe = (String) v.getTag();
            String[] strings = exe.split("#");
            if (strings != null && strings.length == 3) {
                questionId = strings[0];
                questionIndex = strings[2];
            }
            //mdrawerLayout.closeDrawers();
            getQuestion(questionId);
            setList();
        }
    }

    //设置答题卡
    public void setExerciseCordList() {
        try {
            if (exerciseCard != null) {
                //获取屏幕高度和宽度
                WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                int width = wm.getDefaultDisplay().getWidth();
                int widths = width / 36;
                pe_ll.removeAllViews();
                List<ResQuestionTypeRuleModel> questionsList = exerciseCard.getQuestionsList();
                int xuhao = 0;
                TextView tView1 = new TextView(getApplicationContext());
                String aString = "&nbsp&nbsp&nbsp&nbsp<img src=\"" + R.drawable.exe_btn_now_2 + "\"  />:当前题 &nbsp&nbsp&nbsp <img src=\"" + R.drawable.exe_btn_curror1_2 + "\"  />:答对  &nbsp&nbsp&nbsp<img src=\"" + R.drawable.exe_btn_error1_2 + "\"  />:答错   ";
                tView1.setText(Html.fromHtml(aString, imageGetter, null));
                tView1.setTextColor(0xff000000);
                pe_ll.addView(tView1);
                if (questionsList != null && questionsList.size() > 0) {
                    for (int i = 0; i < questionsList.size(); i++) {//遍历题目题型
                        List<QuestionModel> questionList = questionsList.get(i).getQuestion();
                        if (questionList != null && questionList.size() > 0) {
                            //显示题目题型
                            TextView tView = new TextView(getApplicationContext());
                            tView.setText(questionsList.get(i).getPaperQuestionType());
                            tView.setTextColor(0xff000000);
                            tView.setPadding(10, 25, 10, 10);
                            pe_ll.addView(tView);

                            int size = questionList.size(); // 添加Button的个数
                            TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT); // 每行的水平LinearLayout
                            TableRow llayout = null;
                            for (int j = 0; j < size; j++) {//遍历每种题型中的所有题目
                                xuhao++;
                                TableRow.LayoutParams itemParams = new TableRow.LayoutParams(widths * 5, widths * 5);
                                String result = "";
                                itemParams.setMargins(widths / 2, widths / 4, widths / 2, widths / 4);
                                if (j % 6 == 0) {
                                    llayout = new TableRow(getApplicationContext());
                                }
                                String idString = questionList.get(j).getQuestionType() + "" + questionList.get(j).getQuestionIndex();
                                int id = Integer.parseInt(idString);
                                Button childBtn = (Button) LayoutInflater.from(this).inflate(R.layout.exercise_button, null);
                                childBtn.setText(questionList.get(j).getQuestionIndex());
                                childBtn.setTag(questionList.get(j).getId() + "#" + questionsList.get(i).getPaperQuestionType() + "#" + questionList.get(j).getQuestionIndex());
                                //if (todo!="view") {
                                result = (String) correctIdsAndErrorIds.get(questionList.get(j).getId());

                                if ("402881b951f2059a0151f223f77402e4".equals(questionList.get(j).getId())) {
                                    System.out.println((String) correctIdsAndErrorIds.get(questionList.get(j).getId()));
                                }

                                if (result != null && result.equals("正确")) {
                                    childBtn.setBackground(exeBtnCurror);
                                } else if (result != null && result.equals("错误")) {
                                    childBtn.setBackground(exeBtnError);
                                }
                                //}
                                childBtn.setId(id);
                                childBtn.setLayoutParams(itemParams);
                                llayout.addView(childBtn);
                                //判断是否是标记题目
                                String signString = (String) signQuestionMap.get(questionList.get(j).getId());
                                if (signString != null && signString.equals("标记")) {
                                    //childBtn.setBackground(exeBtnCurror);
                                    MyBadgeView badge = new MyBadgeView(this, childBtn);
                                    String idS = id + "000001";
                                    id = Integer.parseInt(idS);
                                    badge.setId(id);
                                    badge.show();
                                }
                                llayout.setLayoutParams(layoutParams);
                                if (j % 6 == 5) {
                                    pe_ll.addView(llayout);
                                } else {
                                    if (j == questionList.size() - 1) {
                                        for (int a = j % 6; a < 6; a++) {
                                            TextView tv = new TextView(getApplicationContext());
                                            llayout.addView(tv);
                                        }
                                        pe_ll.addView(llayout);
                                    }
                                }
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

    //设置悬浮题目数量
    public void setPaint(ImageView v, String text) {
        Bitmap orign = BitmapFactory.decodeResource(getResources(), R.drawable.icon_star_30_2);
        Drawable background = getResources().getDrawable(R.drawable.icon_star1);
        int textsize = 20;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(textsize);


        /**/
        int bgWidth = background.getIntrinsicWidth();
        int bgHeight = background.getIntrinsicHeight();

        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);

        int textHeight = textBounds.height();
        int textWidth = (int) paint.measureText(text, 0, text.length());
        int circleHeight = (int) (bgHeight * 0.71);
        /**/

        Canvas canvas = new Canvas();

        Bitmap bitmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        background.setBounds(-10, -10, bgWidth - 10, bgHeight - 10);
        background.draw(canvas);

        int x = (bgWidth - textWidth) / 2;
        int y = (circleHeight + 1) - (circleHeight - textHeight) / 2;
        canvas.drawText(text, x, y, paint);

        Bitmap bitmap2 = orign.copy(Bitmap.Config.ARGB_8888, true);
        canvas.setBitmap(bitmap2);
        canvas.drawBitmap(bitmap, 0, 0, null);

        v.setImageBitmap(bitmap2);
    }

    public void setExerciseCorlor() {
        if (isright.equals("true")) {
            lastButton.setBackground(exeBtnCurror);
        } else {
            lastButton.setBackground(exeBtnError);
        }
    }

    public void setExerciseCorlor2(String qId) {
        if (todo.equals("view")) {
            lastButton.setBackground(exeBorder);
        } else {
            if (correctIdsAndErrorIds != null && correctIdsAndErrorIds.containsKey(questionId)) {
                if (correctIdsAndErrorIds.get(questionId).equals("错误")) {
                    lastButton.setBackground(exeBtnError);
                } else {
                    lastButton.setBackground(exeBtnCurror);
                }
            } else {
                lastButton.setBackground(exeBorder);
            }
        }
    }

    //标记按钮改变文字
    public void setSign() {
        if (signString.equals("标记成功")) {
            tv.setText("★");
            setSignImg(lastButton, true);
        } else if (signString.equals("取消标记成功")) {
            tv.setText("☆");
            setSignImg(lastButton, false);
        }
        Toast.makeText(PTestActivity.this, signString, Toast.LENGTH_SHORT).show();
        //setTitletext();
    }

    //清除内容
    public void removeview() {
        p_ll_option.removeAllViews();
        //pq_tx.setText("");
        //pq_th.setText("");
        pq_nr.setText("");
    }

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    //触摸事件，左右滑动切换题目
    @Override
    public boolean onTouch(View arg0, MotionEvent event) {


        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            //判断答题卡是否打开状态
            if (sdStatus) {//打开状态
                sd.close();
                sdStatus = false;
            } else {
                x2 = event.getX();
                y2 = event.getY();
                if (x2 - x1 > 150) {//右滑动
                    if (index > 0) {
                        index--;
                        Question question2 = questions.get(index);
                        setExerciseCorlor2(questionId);
                        questionId = question2.getId();
                        getQuestion(questionId);
                        getQuestionIndex(questionId);
                        setList();
                    } else {
                        Toast.makeText(PTestActivity.this, "当前题是第一题！", Toast.LENGTH_SHORT).show();
                    }
                } else if (x1 - x2 > 150) {//左滑动
                    if (index < questions.size() - 1) {
                        index++;
                        Question question2 = questions.get(index);
                        setExerciseCorlor2(questionId);
                        questionId = question2.getId();
                        getQuestion(questionId);
                        getQuestionIndex(questionId);
                        setList();
                    } else {
                        Toast.makeText(PTestActivity.this, "当前题是最后一道题！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
   /* public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("PTest Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }*/


    //适配器，显示题目选项内容
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
            if (view == null) {
                v = View.inflate(PTestActivity.this, R.layout.test_item, null);

            } else {
                v = view;
            }

            TextView tv_seekname = (TextView) v.findViewById(R.id.tvb);
            String string = xxTitle.get(i);
            String string2 = xxMap.get(i + "");
            tv_seekname.setText("  " + Html.fromHtml(string));
            if (string2 != null && string2.equals("0")) {
                tv_seekname.setCompoundDrawables(drawable_zq, null, null, null);
            } else if (string2 != null && string2.equals("1")) {
                tv_seekname.setCompoundDrawables(drawable_cw, null, null, null);
            } else {
                tv_seekname.setCompoundDrawables(ui_icon_chose0, null, null, null);
            }
            if (xxBj.get(i + "") != null) {
                v.setBackgroundColor(0xfffec200);
            } else {
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

        /**
         * 重写getSize方法，只有重写该方法后，才能保证不论是图片大于文字还是文字大于图片，都能实现中间对齐
         */
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            Drawable d = getDrawable();
            Rect rect = d.getBounds();
            if (fm != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;
                int drHeight = rect.bottom - rect.top;

                int top = drHeight / 2 - fontHeight / 4;
                int bottom = drHeight / 2 + fontHeight / 4;

                fm.ascent = -bottom;
                fm.top = -bottom;
                fm.bottom = top;
                fm.descent = top;
            }
            return rect.right;
        }
    }

    public void setSignImg(View v, boolean b) {
        int id = v.getId();
        String idString = id + "000001";
        id = Integer.parseInt(idString);
        MyBadgeView myBadgeView = (MyBadgeView) findViewById(id);
        if (myBadgeView == null) {
            myBadgeView = new MyBadgeView(this, v);
            myBadgeView.setId(id);
        }
        if (b) {
            myBadgeView.show();
        } else {
            myBadgeView.hide();
        }
    }

    public boolean submitExerciseQuestion(String optionid, Question question) {
        DLPracticeAnswerDetailEntity dlPracticeAnswerDetailEntity = new DLPracticeAnswerDetailEntity();
        dlPracticeAnswerDetailEntity.setQuestionId(question.getId());
        dlPracticeAnswerDetailEntity.setQuestionType(question.getType());
        dlPracticeAnswerDetailEntity.setOptionId(optionid);
        dlPracticeAnswerDetailEntity.setBehaviorId(hdaplication.getBehaviorId());
        dlPracticeAnswerDetailEntity.setBatchId(batchId);
        dlPracticeAnswerDetailEntity.setId(question.getId() + batchId);
        boolean submitSure = false;
        List<Option> option = question.getOption();
        if (question.getType().equals("1")) {//单选题
            for (Option o : option) {
                if (o.getId().equals(optionid)) {
                    if (o.getOptionIsCorrent().equals("0")) {
                        correctIdsAndErrorIds.put(question.getId(), "正确");
                        dlPracticeAnswerDetailEntity.setAnswerStatus("0");
                        submitSure = true;
                    } else {
                        correctIdsAndErrorIds.put(question.getId(), "错误");
                        dlPracticeAnswerDetailEntity.setAnswerStatus("1");
                        submitSure = false;
                    }
                    daAnJl.put(questionId, o.getOptionIsCorrent());
                }
            }
        } else if (question.getType().equals("2")) {//多选题
            String[] optionStr = optionid.split("&");
            List<String> optionCorrentStr = new ArrayList<String>();
            for (Option o : option) {
                if (o.getOptionIsCorrent().equals("0")) {
                    optionCorrentStr.add(o.getId());
                }
            }
            if (optionCorrentStr != null && optionCorrentStr.size() == optionStr.length) {
                boolean sure = true;
                for (int i = 0; i < optionCorrentStr.size(); i++) {
                    if (optionid.indexOf(optionCorrentStr.get(i)) == -1) {
                        sure = false;
                    }
                }
                if (sure) {
                    correctIdsAndErrorIds.put(question.getId(), "正确");
                    daAnJl.put(questionId, "0");
                    dlPracticeAnswerDetailEntity.setAnswerStatus("0");
                    submitSure = true;
                } else {
                    correctIdsAndErrorIds.put(question.getId(), "错误");
                    daAnJl.put(questionId, "1");
                    dlPracticeAnswerDetailEntity.setAnswerStatus("1");
                    submitSure = false;
                }
            } else {
                correctIdsAndErrorIds.put(question.getId(), "错误");
                daAnJl.put(questionId, "1");
                dlPracticeAnswerDetailEntity.setAnswerStatus("1");
                submitSure = false;
            }
        } else if (question.getType().equals("3")) {
            if (question.getAnswer().getContent().equals(optionid)) {
                correctIdsAndErrorIds.put(question.getId(), "正确");
                daAnJl.put(questionId, "0");
                dlPracticeAnswerDetailEntity.setAnswerStatus("0");
                submitSure = true;
            } else {
                correctIdsAndErrorIds.put(question.getId(), "错误");
                daAnJl.put(questionId, "1");
                dlPracticeAnswerDetailEntity.setAnswerStatus("1");
                submitSure = false;
            }
        } else {
            correctIdsAndErrorIds.put(question.getId(), "正确");
            daAnJl.put(questionId, "0");
            dlPracticeAnswerDetailEntity.setAnswerStatus("0");
            submitSure = true;
        }
        if (!submitSure) {
            if (errorQuestionIdsStr.indexOf(questionId) == -1) {
                errorQuestionIdsStr += "#" + questionId;
            }
        }
        dbManagers.saveEntity(dlPracticeAnswerDetailEntity);
        return submitSure;
    }

    public void tj() {
        netStatus = NetWork.isNetworkConnected(PTestActivity.this);
        PracticLearnBehavior practicLearnBehavior = new PracticLearnBehavior();
        if (netStatus && !daAnJl.isEmpty()) {
            List<DLPracticeAnswerDetailEntity> list = dbManagers.queryUser();
            ObjectMapper mapper = new ObjectMapper();
            try {
                jsonStr = mapper.writeValueAsString(list);
                getData3();
            } catch (Exception e) {
            }
        }
        if (netStatus) {
            List<DLPracticeQuestionActionEntity> list = dbManagerToDLPracticeQuestionAction.queryUser();
            if (list != null && list.size() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    bjJsonStr = mapper.writeValueAsString(list);
                    getData4();
                } catch (Exception e) {
                }
            }
        }
        String errorIds = "";
        String correctIds = "";
        String signIds = "";
        String str1 = "";
        String str2 = "";
        String str3 = "";
        //遍历map中的键
        int answerCount = 0;
        int errorCount = 0;
        int lastErrorCount = 0;
        int signCount = 0;
        for (String key : correctIdsAndErrorIds.keySet()) {
            if (correctIdsAndErrorIds.get(key).equals("正确")) {
                correctIds += str1 + key;
                str1 = "#";
                answerCount++;
            }
            if (correctIdsAndErrorIds.get(key).equals("错误")) {
                errorIds += str2 + key;
                str2 = "#";
                answerCount++;
                lastErrorCount++;
            }
        }
        for (String key : signQuestionMap.keySet()) {
            if (signQuestionMap.get(key).equals("标记")) {
                signIds += str3 + key;
                str3 = "#";
                signCount++;
            }
        }
        if (errorQuestionIdsStr != null) {
            String[] error = errorQuestionIdsStr.split("#");
            if (error != null && error.length > 0) {
                for (int z = 0; z < error.length; z++) {
                    if (error[z] != null && !error[z].equals("")) {
                        errorCount++;
                    }
                }
            }
        }
        practicLearnBehavior.setSignQuestionIds(signIds);
        practicLearnBehavior.setErrorIds(errorIds);
        practicLearnBehavior.setCorrectIds(correctIds);
        practicLearnBehavior.setErrorQuestionIdsStr(errorQuestionIdsStr);
        practicLearnBehavior.setQuestionAnswerCount(answerCount);
        practicLearnBehavior.setQuestionErrorCount(errorCount);
        practicLearnBehavior.setQuestionSignCount(signCount);
        practicLearnBehavior.setLastErrorCount(lastErrorCount);
        hdaplication.setPracticLearnBehavior(practicLearnBehavior);
        //汇总入库
        SDPracticeEntity sdPracticeEntity = new SDPracticeEntity();
        sdPracticeEntity.setId(hdaplication.getStuid() + hdaplication.getBehaviorId() + hdaplication.getClassId());
        sdPracticeEntity.setAccountId(hdaplication.getStuid());
        sdPracticeEntity.setBehaviorId(hdaplication.getBehaviorId());
        sdPracticeEntity.setClassId(hdaplication.getClassId());
        sdPracticeEntity.setQuestionAnswerCount(answerCount);
        sdPracticeEntity.setSignQuestionIds(signIds);
        sdPracticeEntity.setErrorQuestionIdsStr(errorQuestionIdsStr);
        sdPracticeEntity.setErrorIds(errorIds);
        sdPracticeEntity.setCorrectIds(correctIds);
        dbManagers1.saveEntity(sdPracticeEntity);

    }

    //返回退出练习页
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            tj();
            hdaplication.setStatus("1");
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    Dialog dialog;
    /**
     * 加载框
     */
    private void showLoading(){
        if (dialog==null){
            dialog=new android.app.AlertDialog.Builder(PTestActivity.this).create();
            dialog.show();
            dialog.setContentView(R.layout.loading_process_dialog_anim);
        }else {
            dialog.show();
        }
    }

    /**
     * 关闭加载
     */
    private void closeLoading(){
        dialog.dismiss();
    }
}