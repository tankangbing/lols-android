package com.example.onlinelearnActivity.courseWare.practic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.entity.DLPracticeAnswerDetailEntity;
import com.example.entity.DLPracticeQuestionActionEntity;
import com.example.entity.PracticLearnBehavior;
import com.example.entity.SDPracticeEntity;
import com.example.jdbc.DBManagerToDLPracticeAnswerDetail;
import com.example.jdbc.DBManagerToDLPracticeQuestionAction;
import com.example.jdbc.DBManagerToSDPractice;
import com.example.jsonReturn.GetPracticeTjReturn;
import com.example.jsonReturn.SignQuestionReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.BaseActivity;
import com.example.onlinelearnActivity.alert.HintPracticActivity;
import com.example.spt.jaxb.paper.ExerciseCard;
import com.example.spt.jaxb.paper.Option;
import com.example.spt.jaxb.paper.Paper;
import com.example.spt.jaxb.paper.Question;
import com.example.spt.jaxb.paper.QuestionModel;
import com.example.spt.jaxb.paper.ResQuestionTypeRuleModel;
import com.example.util.ApplicationUtil;
import com.example.util.FinalStr;
import com.example.util.HttpUtil;
import com.example.util.JsonUtil;
import com.example.util.NetWork;
import com.example.util.PracticUtil;
import com.example.util.SysUtil;
import com.example.view.BadgeView;
import com.example.view.MBadgeView;
import com.example.view.NoScrollListView;
import com.example.view.NoScrollListView.OnFlingListener;
import com.example.view.WrappingSlidingDrawer;
import com.google.common.util.concurrent.FutureCallback;
import com.shuyu.gsyvideoplayer.utils.FileUtils;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 练习页面
 */

@SuppressLint("NewApi")
public class PracticePagerActivity extends BaseActivity implements  OnCheckedChangeListener {
    private static final String YOUR_PREF_FILE_NAME = null;

    //界面控件
    RadioGroup rGroup;//
    LinearLayout p_sl_ll;
    TextView tv;//标记
    TextView pq_nr;//题目内容
    Button lastButton;//答题卡当前题目按钮
    TextView p_sd_tx;// 题型：当前第几题/题目总数
    MyAdapterPager myAdapterPager;//viewPager适配器
    ViewPager readerViewPager;//题目切换动画 三个view复用 0：recoverView  1：view1   2：view2
    View recoverView ;
    View view1 ;
    View view2;
    private WrappingSlidingDrawer sd;//答题卡容器
    TableLayout pe_ll;//答题卡
    TextView p_retrun;
    //各种图片背景
    Drawable drawable_zq, drawable_cw, ui_icon_chose0;//选项答对图片drawable_zq   选项答错图片drawable_cw   选项默认图片ui_icon_chose0
    Drawable exeBtnError, exeBtnCurror, exeBtnNow, exeBorder;//答题卡图标  错题 红色exeBtnError   对题 绿色exeBtnCurror   当前题 蓝色exeBtnNow  未做 透明exeBorder

    private String questionForm = "0";//题目形式   ： 所有 未做 错题 标记  默认所有
    private Handler handler;//异步刷新组件
    private ApplicationUtil hdaplication;//全局参数对象
    private String questionId;//题目主键
    int questionType = 0;//题目类型 1单选 2多选 3判断 5 7简答  8名词解析
    String optionId;//选项主键 选中
    String todo;//批次主键 答题模式 或 背题模式
    int allQuestionCount = 0; //题目总数
    List<CheckBox> checkBoxList;//多选按钮集合
    String exerciseStatus;//练习状态 继续或重做
    GetPracticeTjReturn getPracticeTjReturn; //返回题目统计实体
    String signString = "";//标记提示
    Map<String, Object> correctIdsAndErrorIds = new HashMap<String, Object>();//错题与对题map  <题目id，正确或错误>
    Map<String, Object> signQuestionMap = new HashMap<String, Object>();//标记map  <题目id，标记>
    Boolean sdStatus = false;//记录答题卡是否打开状态，初始值为false
    Html.ImageGetter imageGetter,imageGetter1;
    CustomImageSpan imageSpan;//题型图片
    String imageStr;//题型图片
    List<Question> questions;//题目集合
    ExerciseCard exerciseCard;//答题卡
    String questionIndex = "1";//题目序号
    Map<String, String> daAn = new HashMap<String, String>();
    Map<String, String> daAnJl = new HashMap<String, String>();
    int index = 0;//题目索引  从0开始
    String batchId = "";//批次id
    //网络状态
    Boolean netStatus = false;
    String jsonStr = "";//答案json
    String bjJsonStr = "";//标记json
    private String errorQuestionIdsStr;//所有批次错题主键串
    private DBManagerToDLPracticeAnswerDetail dbManagers; //sqlite操作对象
    private DBManagerToSDPractice dbManagers1; //sqlite操作对象dbManagers1
    private DBManagerToDLPracticeQuestionAction dbManagerToDLPracticeQuestionAction; //sqlite操作对象dbManagers1
    int preItem=0;//当前题目编码 viewPager跳转使用
    private List<View> views = new ArrayList<View>();//viewPager的三个view集合
    private Paper paper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_pager);
    }

    @Override
    protected void initViews(View view) {
        setUi();//初始化ui
    }

    @Override
    protected void doBusiness(Context mContext) {
        setCache();//将当前activity添加到全局参数对象
        updateUi();//更新ui
        update();//更新题目（错题、对题、标记题）
        //初始化viewPager
        updatePager();
    }
    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        p_retrun=(TextView)findViewById(R.id.p_retrun);
        iconfont = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
        p_retrun.setTypeface(iconfont);
        setToobbarShow(false);
    }
    private void updatePager() {
        //初始化三个view
        recoverView=View.inflate(PracticePagerActivity.this, R.layout.practice_item, null);
        view1=View.inflate(PracticePagerActivity.this, R.layout.practice_item, null);
        view2=View.inflate(PracticePagerActivity.this, R.layout.practice_item, null);
        setView(recoverView,0);
        if (questions.size()>1){
            setView(view1,1);
        }
        if (questions.size()>2){
            setView(view2,2);
        }
        views.add(recoverView);
        setSign(index);
        views.add(view1);
        views.add(view2);
        sendMessage(3);
        myAdapterPager=new MyAdapterPager();
        readerViewPager.setAdapter(myAdapterPager);
        setXuhao(index+1);
        readerViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                questionId = questions.get(position).getId();
                //设置翻页前题目答题卡按钮颜色 preItem翻页前题目序号
                setExerciseCorlor(preItem,false);
                //设置翻页后题目答题卡按钮颜色 position翻页后题目序号
                setExerciseCorlor(position,true);
                if(position>preItem){
                    //下一题
                    if (position-preItem==1){
                        int i=position%3;//判断翻页后题目显示在第几个view 从0开始算起
                        index=position;//翻页后的当前题目序号
                        //预加载翻页后的下一道题
                        if (index<questions.size()-1){
                            if (i==0){
                                setView(view1,index+1);
                            }else if (i==1){
                                setView(view2,index+1);
                            }else if (i==2){
                                setView(recoverView,index+1);
                            }
                        }
                        views.clear();
                        views.add(recoverView);
                        views.add(view1);
                        views.add(view2);
                    }
                    preItem = position;
                }
                if(position<preItem){
                    //上一题
                    if (preItem-position==1){
                        index=position;
                        int i=position%3;
                        if (index>0){
                            if (i==0){
                                setView(view2,index-1);
                                setView(recoverView,index);
                            }else if (i==1){
                                setView(recoverView,index-1);
                                setView(view1,index);
                            }else if (i==2){
                                setView(view1,index-1);
                                setView(view2,index);
                            }
                        }
                        views.clear();
                        views.add(recoverView);
                        views.add(view1);
                        views.add(view2);
                    }
                    preItem = position;
                }
                //判断标记
                setSign(index);
                setXuhao(index+1);
                return;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /*
    * 设置标记
    * */
    private void setSign(int i) {
        if (signQuestionMap.get(questions.get(i).getId()) != null && signQuestionMap.get(questions.get(i).getId()).equals("标记")) {
            tv.setTextColor(0xFFFFCC00);
        } else {
            tv.setTextColor(0xFF999999);
        }
    }

    /*
    * 获取题目序号
    * */
    public String getQuestionIndex(String questionid) {
        String index="1";
        if (exerciseCard != null) {
            for (ResQuestionTypeRuleModel q : exerciseCard.getQuestionsList()) {
                if (q != null) {
                    for (QuestionModel m : q.getQuestion()) {
                        if (m.getId().equals(questionid)) {
                            index = m.getQuestionIndex();
                        }
                    }
                }
            }
        }
        return index;
    }

    //将当前activity添加到全局参数对象
    public void setCache() {
        hdaplication = (ApplicationUtil) getApplication();
        hdaplication.addActivity(this);
    }

    /*
    * 更新题目（错题、对题）
    * */
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
        questions = PracticUtil.getQuestionList(hdaplication.getPapers(), questionIds, questionForm);//通过全部题目、主键串、题目形式获取题目集合
        questionId = questions.get(0).getId();
        exerciseCard = PracticUtil.getExerciseCard(hdaplication.getExerciseCard(), questionIds, questionForm);//通过全部答题卡、主键串、题目形式获取答题卡
        /*for (int z=0;z<exerciseCard.getQuestionsList().size();z++){
            if (exerciseCard.getQuestionsList().get(z).getQuestion().size()!=0){
                questionId = exerciseCard.getQuestionsList().get(z).getQuestion().get(0).getId();//获取第一题
                questionIndex = exerciseCard.getQuestionsList().get(z).getQuestion().get(0).getQuestionIndex();
                break;
            }
        }*/
        allQuestionCount = questions.size();//题目总数
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

    /*
    * 设置Ui初始化
    * */
    @SuppressWarnings("deprecation")
    public void setUi() {
        showLoading();
        //判断是否有网络
        netStatus = NetWork.isNetworkConnected(PracticePagerActivity.this);
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
            intent1.setClass(PracticePagerActivity.this, HintPracticActivity.class);
            startActivity(intent1);
        }

        //初始化
        tv = (TextView) findViewById(R.id.p_btn_sign);
        tv.setTypeface(iconfont);
        tv.setText(R.string.icon_sign);
        p_sl_ll = (LinearLayout) findViewById(R.id.p_sl_ll);
        p_sd_tx=(TextView)findViewById(R.id.p_sd_tx);
        readerViewPager=(ViewPager)findViewById(R.id.readerViewPager);
        //答题卡
        sd = (WrappingSlidingDrawer) findViewById(R.id.sliding);
        sd.setTouchableIds(new int[]{R.id.p_btn_sign});
        //答题卡打开时执行
        sd.setOnDrawerOpenListener(new OnDrawerOpenListener() {
            public void onDrawerOpened() {
                sdStatus = true;
                p_sl_ll.setBackgroundColor(0x809D9D9D);
                /*tv.setFocusable(false);
                tv.setClickable(false);*/
            }
        });
        //答题卡关闭时执行
        sd.setOnDrawerCloseListener(new OnDrawerCloseListener() {
            public void onDrawerClosed() {
                sdStatus = false;
                p_sl_ll.setBackgroundColor(0x99FFF);
                /*tv.setFocusable(true);
                tv.setClickable(true);
                tv.requestFocus();*/
            }
        });
        //p_sd_tx.setOnClickListener(onLeftClickListener);
        sd.setTouchableIds(new int[]{R.id.p_btn_sign});
        sd.setListener(new WrappingSlidingDrawer.ClickListener() {
            @Override
            public void onClick(int id) {
                switch (id){
                    case R.id.p_btn_sign:
                        Log.d("XX",id+"p_sd_tx");
                        DLPracticeQuestionActionEntity dlPracticeQuestionActionEntity = new DLPracticeQuestionActionEntity();
                        dlPracticeQuestionActionEntity.setAccountId(hdaplication.getStuid());
                        dlPracticeQuestionActionEntity.setBehaviorId(hdaplication.getBehaviorId());
                        dlPracticeQuestionActionEntity.setQuestionId(questions.get(index).getId());
                        dlPracticeQuestionActionEntity.setClassId(hdaplication.getClassId());
                        dlPracticeQuestionActionEntity.setQuestionType(questionType + "");
                        dlPracticeQuestionActionEntity.setId("bj" + questions.get(index).getId() + hdaplication.getStuid());
                        if (signQuestionMap.get(questions.get(index).getId()) != null && !signQuestionMap.get(questions.get(index).getId()).equals("")) {
                            signQuestionMap.remove(questions.get(index).getId());
                            dlPracticeQuestionActionEntity.setIsMarked("1");
                            signString = "取消标记成功";
                        } else {
                            signQuestionMap.put(questions.get(index).getId(), "标记");
                            dlPracticeQuestionActionEntity.setIsMarked("0");
                            signString = "标记成功";
                        }
                        dbManagerToDLPracticeQuestionAction.saveEntity(dlPracticeQuestionActionEntity);
                        setSignImg();
                        break;
                    case R.id.p_btn_exerciseCard:
                        Log.d("XX",id+"p_btn_exerciseCard");
                        break;
                }

            }
        });

        lastButton = new Button(getApplicationContext());
        pe_ll = (TableLayout) findViewById(R.id.pe_ll);
        checkBoxList = new ArrayList<CheckBox>();
        rGroup = (RadioGroup) findViewById(R.id.practice_rg);
        rGroup.setOnCheckedChangeListener((OnCheckedChangeListener) this);

        //通过意图，获取答题方式todo和题目形式questionForm
        Intent intent = getIntent();
        String string = intent.getStringExtra("batchId");
        //paper = (Paper) intent.getSerializableExtra("papers");
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
        drawable_zq = getResources().getDrawable(R.drawable.u20);//选项答对图片
        drawable_zq.setBounds(0, 0, 54, 54);  //此为必须写的
        drawable_cw = getResources().getDrawable(R.drawable.u21);//选项答错图片
        drawable_cw.setBounds(0, 0,  54, 54);  //此为必须写的
        ui_icon_chose0 = getResources().getDrawable(R.drawable.u19);//选项默认图片
        ui_icon_chose0.setBounds(0, 0,  54, 54);  //此为必须写的
        //初始化答题卡按钮颜色
        exeBtnError = getResources().getDrawable(R.drawable.u32);//错题 红色
        exeBtnError.setBounds(0, 0, exeBtnError.getMinimumWidth(), exeBtnError.getMinimumHeight());
        exeBtnCurror = getResources().getDrawable(R.drawable.u31);//对题 绿色
        exeBtnCurror.setBounds(0, 0, exeBtnCurror.getMinimumWidth(), exeBtnCurror.getMinimumHeight());
        exeBtnNow = getResources().getDrawable(R.drawable.u40);//当前题 蓝色
        exeBtnNow.setBounds(0, 0, exeBtnNow.getMinimumWidth(), exeBtnNow.getMinimumHeight());
        exeBorder = getResources().getDrawable(R.drawable.u30);//未做 透明
        exeBorder.setBounds(0, 0, exeBtnNow.getMinimumWidth(), exeBtnNow.getMinimumHeight());

        imageGetter = new Html.ImageGetter() {

            public Drawable getDrawable(String source) {
                Drawable drawable = null;
                int rId = Integer.parseInt(source);
                drawable = getResources().getDrawable(rId);
                drawable.setBounds(0, 0, 50,50);
                return drawable;
            };
        };

//        //加载网络图片
//        imageGetter1 = new Html.ImageGetter() {
//            @Override
//            public Drawable getDrawable(String source) {//参数为image的src属性
//                LevelListDrawable d = new LevelListDrawable();
//                Drawable empty = getResources().getDrawable(
//                        R.drawable.u19);
//                d.addLevel(0, 0, empty);
//                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//                new LoadImage().execute(source, d);
//                return d;
//            }
//        };
    }

    /**
     * 异步下载图片类
     *
     * @author Ruffian
     * @date 2018年3月23日
     *
     */
    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;
        private TextView mTextView;

        public LoadImage(TextView mTextView ) {
            this.mTextView = mTextView;
        }


        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = "";
            if(((String) params[0]).contains("resource")){//修复兼容加载本地图片
                source ="http://zk.scutde.net/"+(String) params[0];
            }else {
                source ="https://zk.scutde.net/"+(String) params[0];
            }
            mDrawable = (LevelListDrawable) params[1];
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 图片下载完成后执行
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                /**
                 * 适配图片大小 <br/>
                 * 默认大小：bitmap.getWidth(), bitmap.getHeight()<br/>
                 * 适配屏幕：getDrawableAdapter
                 */
                double bl = 100/bitmap.getHeight();

//                mDrawable = getDrawableAdapter(mContext, mDrawable,bitmap.getWidth(), bitmap.getHeight());
//修复图片过大时，显示不完整
                mDrawable.setBounds(0, 0, (int) (bitmap.getWidth()*1.5), (int)(bitmap.getHeight() * 1.5));

//                 mDrawable.setBounds(0, 0, bitmap.getWidth(),bitmap.getHeight());

                mDrawable.setLevel(1);

                /**
                 * 图片下载完成之后重新赋值textView<br/>
                 * mtvActNewsContent:我项目中使用的textView
                 *
                 */
                mTextView.invalidate();
                CharSequence t = mTextView.getText();
                mTextView.setText(t);
            }
        }

        /**
         * 加载网络图片,适配大小
         *
         * @param context
         * @param drawable
         * @param oldWidth
         * @param oldHeight
         * @return
         * @author Ruffian
         * @date 2016年1月15日
         */
        public LevelListDrawable getDrawableAdapter(Context context,
                                                    LevelListDrawable drawable, int oldWidth, int oldHeight) {
            LevelListDrawable newDrawable = drawable;
//            long newHeight = 0;// 未知数
//            int newWidth = PhoneUtils.getScreenWidth(context);// 默认屏幕宽
//            newHeight = (newWidth * oldHeight) / oldWidth;
            // LogUtils.w("oldWidth:" + oldWidth + "oldHeight:" +
            // oldHeight);
            // LogUtils.w("newHeight:" + newHeight + "newWidth:" +
            // newWidth);
//            newDrawable.setBounds(0, 0, newWidth, (int) newHeight);
            return newDrawable;
        }
    }


    private void sendMessage(int str) {
        Message msg = new Message();
        msg.what = str;
        PracticePagerActivity.this.handler.sendMessage(msg);
    }

    /*
     * 更新ui
     */
    public void updateUi() {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: //无法加载数据
                        closeLoading();
                        Toast.makeText(PracticePagerActivity.this, "无法加载数据,请检查你的网络连接或者联系我们！",
                                Toast.LENGTH_LONG).show();
                        break;
                    case 1: //加载答题统计数据
                        int i=index%3;
                        if (i==0){
                            setPracticeTj(recoverView);
                        }else if (i==1){
                            setPracticeTj(view1);
                        }else if (i==2){
                            setPracticeTj(view2);
                        }
                        break;
                    case 2://没有获取时，清除页面
                        closeLoading();
                        Toast.makeText(PracticePagerActivity.this, "没有相关的信息", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        //设置答题卡
                        setExerciseCordList();
                        break;
                    case 4:
                        //设置答题卡按钮变化
                        //setExerciseCorlor();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }


    /*
    * 获取答题统计
    * */
    public void getData2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>(0);
                map.put("coursewareId", hdaplication.getCoursewareId());
                map.put("classId", hdaplication.getClassId());
                map.put("behaviorId", hdaplication.getBehaviorId());
                map.put("accountId", hdaplication.getStuid());
                map.put("questionId", questionId);
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH + "/appControler.do?getPracticeTj",
                        map, PracticePagerActivity.this, null);
                if (SysUtil.isBlank(result) || "NO_NET".equals(result)) {
                    sendMessage(0);
                } else {
                    getPracticeTjReturn= JsonUtil.parserString(result, GetPracticeTjReturn.class);
                    if (getPracticeTjReturn.isSuccess()){
                        sendMessage(1);
                    }
                }
            }
        }).start();
    }

    /*
    *
    * 提交作答题目
    * */
    public void getData3(final int i) {//提交
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
                        map, PracticePagerActivity.this, null);
                if (SysUtil.isBlank(result) || "NO_NET".equals(result)) {
                    sendMessage(0);
                } else {
                    if (i==1){
                        getData2();
                    }
                    dbManagers.deleteUser();
                }
            }
        }).start();
    }

    /*
    * 提交标记题目
    * */
    public void getData4() {//标记
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>(0);
                map.put("accountId", hdaplication.getStuid());
                map.put("bjJsonStr", bjJsonStr);//标记json
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH + "/appControler.do?signQuestion",
                        map, PracticePagerActivity.this, null);
                if (SysUtil.isBlank(result) || "NO_NET".equals(result)) {
                    sendMessage(0);
                } else {
                    SignQuestionReturn signQuestionReturn= JsonUtil.parserString(result, SignQuestionReturn.class);
                    if(signQuestionReturn!=null&&signQuestionReturn.isSuccess()){
                        dbManagerToDLPracticeQuestionAction.deleteUser();
                    }
                }
            }
        }).start();
    }

    /*
    * 切换答题模式/背题模式
    * */
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        exerciseStatus = "continue";
        if (id == R.id.btn_do) {
            todo = "do";
        } else if (id == R.id.btn_recite) {
            todo = "view";
        }
        int i=index%3;
        if (i==0){
            setView(recoverView,index);
            if (index<questions.size()-1){
                setView(view1,index+1);
            }
            if (index>0){
                setView(view2,index-1);
            }
        }else if (i==1){
            if (index<questions.size()-1){
                setView(view2,index+1);
            }
            if (index>0){
                setView(recoverView,index-1);
            }
            setView(view1,index);
        }else if (i==2){
            if (index<questions.size()-1){
                setView(recoverView,index+1);
            }
            if (index>0){
                setView(view1,index-1);
            }
            setView(view2,index);
        }
        views.clear();
        views.add(recoverView);
        views.add(view1);
        views.add(view2);
        readerViewPager.setCurrentItem(index);
    }

    /*
    * 点击事件click
    * */
    public void click(View v) {
        if (v.getId() == R.id.p_btn_sign) {//标记
            DLPracticeQuestionActionEntity dlPracticeQuestionActionEntity = new DLPracticeQuestionActionEntity();
            dlPracticeQuestionActionEntity.setAccountId(hdaplication.getStuid());
            dlPracticeQuestionActionEntity.setBehaviorId(hdaplication.getBehaviorId());
            dlPracticeQuestionActionEntity.setQuestionId(questions.get(index).getId());
            dlPracticeQuestionActionEntity.setClassId(hdaplication.getClassId());
            dlPracticeQuestionActionEntity.setQuestionType(questionType + "");
            dlPracticeQuestionActionEntity.setId("bj" + questions.get(index).getId() + hdaplication.getStuid());
            if (signQuestionMap.get(questions.get(index).getId()) != null && !signQuestionMap.get(questions.get(index).getId()).equals("")) {
                signQuestionMap.remove(questions.get(index).getId());
                dlPracticeQuestionActionEntity.setIsMarked("1");
                signString = "取消标记成功";
            } else {
                signQuestionMap.put(questions.get(index).getId(), "标记");
                dlPracticeQuestionActionEntity.setIsMarked("0");
                signString = "标记成功";
            }
            dbManagerToDLPracticeQuestionAction.saveEntity(dlPracticeQuestionActionEntity);
            setSignImg();
        } else if (v.getId() == R.id.p_retrun) {//返回按钮
            tj(0);
            hdaplication.setStatus("1");
            this.finish();
        } else {//选择答题卡
            sd.close();
            setExerciseCorlor2(index);
            String exe = (String) v.getTag();
            String[] strings = exe.split("#");
            if (strings != null && strings.length == 3) {
                //questionId = strings[0];
                index = Integer.parseInt(strings[2]);
            }
            int i=index%3;
            if (i==0){
                 setView(recoverView,index);
                if (index>0){
                     setView(view2,index-1);
                }
                if (index<questions.size()-1){
                      setView(view1,index+1);
                }
            }else if (i==1){
                 setView(view1,index);
                if (index>0){
                    setView(recoverView,index-1);
                }
                if (index<questions.size()-1){
                     setView(view2,index+1);
                }
            }else if (i==2){
                setView(view2,index);
                if (index>0){
                    setView(view1,index-1);
                }
                if (index<questions.size()-1){
                     setView(recoverView,index+1);
                }
            }
            views.clear();
            views.add(recoverView);
            views.add(view1);
            views.add(view2);
            readerViewPager.setCurrentItem(index,false);
        }
    }

    /*
     *设置答题卡
     *
     **/
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
                String aString = "&nbsp&nbsp&nbsp&nbsp<img src=\"" + R.drawable.u40 + "\"  />:当前题 &nbsp&nbsp&nbsp <img src=\"" + R.drawable.u31 + "\"  />:答对  &nbsp&nbsp&nbsp<img src=\"" + R.drawable.u32 + "\"  />:答错   ";
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
                                TableRow.LayoutParams itemParams = new TableRow.LayoutParams(widths * 5, widths * 5);
                                String result = "";
                                itemParams.setMargins(widths / 2, widths / 4, widths / 2, widths / 4);
                                if (j % 6 == 0) {
                                    llayout = new TableRow(getApplicationContext());
                                }
                                String idString = questionList.get(j).getQuestionType() + "" + questionList.get(j).getQuestionIndex();
                                //String idString = questionList.get(j).getQuestionType() + "" + xuhao;
                                int id = Integer.parseInt(idString);
                                RelativeLayout layout=new RelativeLayout(this);
                                layout.setLayoutParams(itemParams);
                                Button childBtn = (Button) LayoutInflater.from(this).inflate(R.layout.exercise_button, null);
                                childBtn.setText(questionList.get(j).getQuestionIndex());
                                childBtn.setTag(questionList.get(j).getId() + "#" + questionsList.get(i).getPaperQuestionType() + "#" + xuhao);
                                //if (todo!="view") {
                                result = (String) correctIdsAndErrorIds.get(questionList.get(j).getId());
                                if (result != null && result.equals("正确")) {
                                    childBtn.setBackground(exeBtnCurror);
                                } else if (result != null && result.equals("错误")) {
                                    childBtn.setBackground(exeBtnError);
                                }
                                //}
                                layout.setId(id);
//                                childBtn.setLayoutParams(itemParams);
                                if(xuhao==0){
                                    childBtn.setBackground(exeBtnNow);
                                }
                                layout.addView(childBtn);
                                llayout.addView(layout);
                                //判断是否是标记题目
                                String signString = (String) signQuestionMap.get(questionList.get(j).getId());
                                if (signString != null && signString.equals("标记")) {
                                    MBadgeView badge = new MBadgeView(this, layout);
                                    String idS = id + "000001";
                                    id = Integer.parseInt(idS);
                                    badge.setId(id);
                                    badge.toggle(true);
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
                                xuhao++;
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

    /*
    * 设置当前答题按钮
    * i 题目索引
    * isNow 是否当前题目
    * */
    public void setExerciseCorlor(int i,boolean isNow) {
        Question question=questions.get(i);
        String questionIndex=getQuestionIndex(question.getId());
        String ids = question.getType() + questionIndex;
        int idInt = Integer.parseInt(ids);
        RelativeLayout layout=(RelativeLayout)findViewById(idInt);
        lastButton = (Button) layout.findViewById(R.id.item_btn);
        if(isNow){
            lastButton.setBackground(exeBtnNow);
        }else {
            if (correctIdsAndErrorIds != null && correctIdsAndErrorIds.containsKey(question.getId())) {
                if (correctIdsAndErrorIds.get(question.getId()).equals("错误")) {
                    lastButton.setBackground(exeBtnError);
                } else {
                    lastButton.setBackground(exeBtnCurror);
                }
            } else {
                lastButton.setBackground(exeBorder);
            }
        }
    }

    /*
    * 设置答题卡相应按钮
    * */
    public void setExerciseCorlor2(int i) {
        Question question=questions.get(i);
        String questionIndex=getQuestionIndex(question.getId());
        String ids = question.getType() + questionIndex;
        int idInt = Integer.parseInt(ids);
        RelativeLayout layout=(RelativeLayout)findViewById(idInt);
        lastButton = (Button) layout.findViewById(R.id.item_btn);
        if (correctIdsAndErrorIds != null && correctIdsAndErrorIds.containsKey(question.getId())) {
            if (correctIdsAndErrorIds.get(question.getId()).equals("错误")) {
                lastButton.setBackground(exeBtnNow);
            } else {
                lastButton.setBackground(exeBtnCurror);
            }
        } else {
            lastButton.setBackground(exeBorder);
        }
    }

    /*
    * 标记成功并添加答题卡标记图片
    * */
    public void setSignImg() {
        if (signString.equals("标记成功")) {
            tv.setTextColor(0xFFFFCC00);
            setSignImg(index, true);
        } else if (signString.equals("取消标记成功")) {
            tv.setTextColor(0xFF999999);
            setSignImg(index, false);
        }
        Toast.makeText(PracticePagerActivity.this, signString, Toast.LENGTH_SHORT).show();
    }

    public void setXuhao(int xuhao) {
        if(questionForm.equals("0")) {
            p_sd_tx.setText("全部：" + xuhao + "/" + allQuestionCount);
        } else if (questionForm.equals("1")) {
            p_sd_tx.setText("未做: " + allQuestionCount);
        } else if (questionForm.equals("2") || questionForm.equals("5") || questionForm.equals("6")) {
            p_sd_tx.setText("错题: " + xuhao + "/" + allQuestionCount);
        } else if (questionForm.equals("3")) {
            p_sd_tx.setText("标记: " + xuhao + "/" + allQuestionCount);
        }
    }


    /**
     * 自定义imageSpan实现图片与文字的居中对齐
     */
    class CustomImageSpan extends ImageSpan {

        //自定义对齐方式--与文字中间线对齐
        private int ALIGN_FONTCENTER = 2;

        public CustomImageSpan(Drawable b, int verticalAlignment) {
            super(b, verticalAlignment);

        }

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
//            if (mVerticalAlignment == ALIGN_BASELINE) {
//                transY -= fm.descent;
//            } else if (mVerticalAlignment == ALIGN_FONTCENTER) {    //此处加入判断， 如果是自定义的居中对齐
                //与文字的中间线对齐（这种方式不论是否设置行间距都能保障文字的中间线和图片的中间线是对齐的）
                // y+ascent得到文字内容的顶部坐标，y+descent得到文字的底部坐标，（顶部坐标+底部坐标）/2=文字内容中间线坐标
                transY = ((y + fm.descent) + (y + fm.ascent)) / 2 - drawable.getBounds().bottom / 2;
//            }
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

    /*
    * 设置标记
    * */
    public void setSignImg(int i, boolean b) {
        Question question=questions.get(i);
        String questionIndex=getQuestionIndex(question.getId());
        String ids = question.getType() + questionIndex;
        int id1 = Integer.parseInt(ids);
        RelativeLayout layout = (RelativeLayout) findViewById(id1);

        String idString = id1 + "000001";
        int id = Integer.parseInt(idString);
        MBadgeView myBadgeView = (MBadgeView) findViewById(id);
        if (myBadgeView == null) {
            myBadgeView = new MBadgeView(this, layout);
            myBadgeView.setId(id);
        }
        if (b) {
            myBadgeView.toggle();
        } else {
            myBadgeView.toggle(true);
        }
    }

    /*
    * 判断题目对错
    * */
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
                    daAnJl.put(question.getId(), o.getOptionIsCorrent());
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
                    daAnJl.put(question.getId(), "0");
                    dlPracticeAnswerDetailEntity.setAnswerStatus("0");
                    submitSure = true;
                } else {
                    correctIdsAndErrorIds.put(question.getId(), "错误");
                    daAnJl.put(question.getId(), "1");
                    dlPracticeAnswerDetailEntity.setAnswerStatus("1");
                    submitSure = false;
                }
            } else {
                correctIdsAndErrorIds.put(question.getId(), "错误");
                daAnJl.put(question.getId(), "1");
                dlPracticeAnswerDetailEntity.setAnswerStatus("1");
                submitSure = false;
            }
        } else if (question.getType().equals("3")) {
            if (question.getAnswer().getContent().equals(optionid)) {
                correctIdsAndErrorIds.put(question.getId(), "正确");
                daAnJl.put(question.getId(), "0");
                dlPracticeAnswerDetailEntity.setAnswerStatus("0");
                submitSure = true;
            } else {
                correctIdsAndErrorIds.put(question.getId(), "错误");
                daAnJl.put(question.getId(), "1");
                dlPracticeAnswerDetailEntity.setAnswerStatus("1");
                submitSure = false;
            }
        } else {
            correctIdsAndErrorIds.put(question.getId(), "正确");
            daAnJl.put(question.getId(), "0");
            dlPracticeAnswerDetailEntity.setAnswerStatus("0");
            submitSure = true;
        }
        if (!submitSure) {
            if (errorQuestionIdsStr.indexOf(question.getId()) == -1) {
                errorQuestionIdsStr += "#" + question.getId();
            }
        }
        dbManagers.saveEntity(dlPracticeAnswerDetailEntity);
        return submitSure;
    }

    /*
    * 退出前提交题目至服务器 并本地保存至数据库
    * */
    public void tj(int i) {
        netStatus = NetWork.isNetworkConnected(PracticePagerActivity.this);
        PracticLearnBehavior practicLearnBehavior = new PracticLearnBehavior();
        if (netStatus && !daAnJl.isEmpty()) {
            List<DLPracticeAnswerDetailEntity> list = dbManagers.queryUser();
            ObjectMapper mapper = new ObjectMapper();
            try {
                jsonStr = mapper.writeValueAsString(list);
                getData3(i);
            } catch (Exception e) {
            }
        }else {
            getData2();
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
            tj(0);
            hdaplication.setStatus("1");
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

/*

    Dialog dialog;
    */
/**
     * 加载框
     *//*

    private void showLoading(){
        if (dialog==null){
            dialog=new android.app.AlertDialog.Builder(PracticePagerActivity.this).create();
            dialog.show();
            dialog.setContentView(R.layout.loading_process_dialog_anim);
        }else {
            dialog.show();
        }
    }

    */
/**
     * 关闭加载
     *//*

    private void closeLoading(){
        dialog.dismiss();
    }
*/



    //viewPager适配器
    private class MyAdapterPager extends PagerAdapter {

        @Override
        public int getCount() {
            return questions.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            View v = views.get(position%3);
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null) {
                parent.removeView(v);
            }
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object)
        {

            /*container.removeView(views.get(position%3));*/
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

    }

    /*
    * 通过题目序号获取题目重绘view
    * */
    public View setView(View v,int z){
       // View v=View.inflate(PracticePagerActivity.this, R.layout.practice_item, null);
        final Question question=questions.get(z);
        final TextView p_tv_jx = (TextView) v.findViewById(R.id.p_tv_jx);//解析
        final TextView p_tv_zsd = (TextView) v.findViewById(R.id.p_tv_zsd);//相关知识点
        final LinearLayout llyout1=(LinearLayout)v.findViewById(R.id.p_ll_status);//答题状态
        llyout1.setVisibility(View.GONE);
        final TextView pitem_tj=(TextView)v.findViewById(R.id.pitem_tj);//查看题目统计
        pitem_tj.setVisibility(View.VISIBLE);
        pitem_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tj(1);
                pitem_tj.setVisibility(View.GONE);
            }
        });
        final LinearLayout p_ll_da=(LinearLayout)v.findViewById(R.id.p_ll_da);//答案
        final LinearLayout p_ll_tj1=(LinearLayout)v.findViewById(R.id.p_ll_tj);//答题统计
        p_ll_tj1.setVisibility(View.GONE);
        final TextView pq_nr = (TextView) v.findViewById(R.id.pq_nr);
        Button btn1=(Button)v.findViewById(R.id.p_btn_tj);//提交按钮
        final NoScrollListView lv_xx1=(NoScrollListView)v.findViewById(R.id.lv_xx);

        final List<String> xxTitle1 = new ArrayList<String>();

        final List<String> xxId1 = new ArrayList<String>();//选项Id
        final Map<String, String> xxMap1 = new HashMap<String, String>();//选项对错  <选项序号, 0正确/1错误>
        final Map<String, String> xxBj1 = new HashMap<String, String>();//选中背景
        if (question != null) {
            //适配器 选项
            final BaseAdapter baseAdapter= new BaseAdapter() {
                @Override
                public int getCount() {
                    return xxId1.size();
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
                        v = View.inflate(PracticePagerActivity.this, R.layout.test_item, null);

                    } else {
                        v = view;
                    }

                    final TextView tv_seekname = (TextView) v.findViewById(R.id.tvb);
                    String string = xxTitle1.get(i);
                    String string2 = xxMap1.get(i + "");
                    String optionId= xxId1.get(i);
                    String tag =  (String)tv_seekname.getTag();
                    if(tag==null || tag.equals("") || !tag.equals(optionId)){
                        int hei = 40;
                        String [] s1= string.split(" ");
                        for(String s:s1){
                            if(s.contains("height=")){
                                String [] s2= s.split("=");
                                if(s2.length==2){
                                    hei = Integer.parseInt(s2[1]) ;
                                }
                            }
                        }
                        final int height = hei;
                        tv_seekname.setText(Html.fromHtml(" "+string, new Html.ImageGetter() {
                            @Override
                            public Drawable getDrawable(String source) {
                                LevelListDrawable d = new LevelListDrawable();
                                Drawable empty = getResources().getDrawable(
                                        R.drawable.jiazai);
                                d.addLevel(0, 0, empty);
                                d.setBounds(0, 0, (int)(height*2.5),(int)(height*2.5));
                                new LoadImage(tv_seekname).execute(source, d);
                                return d;
                            }
                        }, null));
                    }
                    tv_seekname.setTag(optionId);
//                    tv_seekname.setText(string+"");
                    if (string2 != null && string2.equals("0")) {
                        tv_seekname.setCompoundDrawables(drawable_zq, null, null, null);
                    } else if (string2 != null && string2.equals("1")) {
                        tv_seekname.setCompoundDrawables(drawable_cw, null, null, null);
                    } else {
                        tv_seekname.setCompoundDrawables(ui_icon_chose0, null, null, null);
                    }
                    if (xxBj1.get(i + "") != null) {
                        v.setBackgroundColor(0xfffec200);
                    } else {
                        v.setBackgroundColor(0xfffff);
                    }

                    return v;
                }
            };
            if (null != question.getOption() && question.getOption().size() > 0) {//判断题型，设置选项
                if (question.getType().equals("1")) {
                    //调用自定义的imageSpan,实现文字与图片的横向居中对齐
//                    imageSpan = new CustomImageSpan(this, R.drawable.icon_chose, 2);
                    imageStr="<img src='"+R.drawable.icon_chose+"'  />";

                    lv_xx1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//开启单选模式

                    for (int i = 0; i < question.getOption().size(); i++) {
                        xxTitle1.add(question.getOption().get(i).getContent());
                        xxId1.add(question.getOption().get(i).getId());
                    }
                } else if (question.getType().equals("2")) {
                    btn1.setText("提交");
                    //aString ="<img src=\""+R.drawable.icon_chose2+"\" style='float:left;' /> "+" "+questionIndex+"."+question.getContent();
//                    imageSpan = new CustomImageSpan(this, R.drawable.icon_chose2, 2);
                    imageStr="<img src='"+R.drawable.icon_chose2+"'  />";
                    lv_xx1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);//开启多选模式
                    for (int i = 0; i < question.getOption().size(); i++) {
                        xxTitle1.add(question.getOption().get(i).getContent());
                        xxId1.add(question.getOption().get(i).getId());
                    }
                } else if (question.getType().equals("4")) {
                    btn1.setText("显示参考答案");
                    BadgeView badgeView=new BadgeView(this);
                    badgeView.setTargetView(btn1);
                    badgeView.setBadgeCount(11);
                    //aString ="<img src=\""+R.drawable.icon_chose2+"\" style='float:left;' /> "+" "+questionIndex+"."+question.getContent();
//                    imageSpan = new CustomImageSpan(this, R.drawable.icon_chose7_2, 2);
                    imageStr="<img src='"+R.drawable.icon_chose7_2+"'  />";
                }
            } else {
                if (question.getType().equals("3")) {
                    //aString ="<img src=\""+R.drawable.icon_chose3+"\" style='float:left;' /> "+questionIndex+"."+question.getContent();
//                    imageSpan = new CustomImageSpan(this, R.drawable.icon_chose3, 2);
                    imageStr="<img src='"+R.drawable.icon_chose3+"'  />";
                    lv_xx1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//开启单选模式
                    xxTitle1.add(" 正确");
                    xxId1.add("0");
                    xxTitle1.add(" 错误");
                    xxId1.add("1");
                } else {
                    pitem_tj.setVisibility(View.GONE);
                    btn1.setText("显示参考答案");
                    if (question.getType().equals("5")) {
                        //aString ="<img src=\""+R.drawable.icon_chose6+"\" style='float:left;' /> "+questionIndex+"."+question.getContent();
//                        imageSpan = new CustomImageSpan(this, R.drawable.icon_chose6, 2);
                        imageStr="<img src='"+R.drawable.icon_chose6+"'  />";
                    } else if (question.getType().equals("7")) {
                        //aString ="<img src=\""+R.drawable.icon_chose5+"\" style='float:left;' /> "+questionIndex+"."+question.getContent();
//                        imageSpan = new CustomImageSpan(this, R.drawable.icon_chose5, 2);
                        imageStr="<img src='"+R.drawable.icon_chose5+"'  />";
                    } else if (question.getType().equals("8")) {
                        //aString ="<img src=\""+R.drawable.icon_chose4+"\" style='float:left;' /> "+questionIndex+"."+question.getContent();
//                        imageSpan = new CustomImageSpan(this, R.drawable.icon_chose4, 2);
                        imageStr="<img src='"+R.drawable.icon_chose4+"'  />";
                    }
                }
            }
//            spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            pq_nr.setText(spannableString);
            //https://zk.scutde.net/85ba6cbe-6dc4-4e1b-a3d7-88b614577c4c.files/image001.gif
//            pq_nr.setText(Html.fromHtml("<img src='https://zk.scutde.net/85ba6cbe-6dc4-4e1b-a3d7-88b614577c4c.files/image001.gif'  />111",imageGetter1 , null));
            Spanned spanned = Html.fromHtml(imageStr +getQuestionIndex(question.getId())+ "." +question.getContent(), new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    Drawable drawable = null;
                    try {

                        int rId = Integer.parseInt(source);
                        drawable = getResources().getDrawable(rId);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
                        return drawable;
                    } catch (Exception e) {
                        LevelListDrawable d = new LevelListDrawable();
                        Drawable empty = getResources().getDrawable(
                                R.drawable.jiazai);
                        d.addLevel(0, 0, empty);
                        d.setBounds(0, 0, 50, 50);
                        new LoadImage(pq_nr).execute(source, d);
                        return d;
                    }
                }
            }, null);
            if (spanned instanceof SpannableStringBuilder) {
                ImageSpan[] imageSpans = spanned.getSpans(0, spanned.length(), ImageSpan.class);
                for (ImageSpan imageSpan : imageSpans) {
                    int start = spanned.getSpanStart(imageSpan);
                    int end = spanned.getSpanEnd(imageSpan);
                    Drawable d = imageSpan.getDrawable();
                    CustomImageSpan newImageSpan = new CustomImageSpan(d, ImageSpan.ALIGN_BASELINE);
                    ((SpannableStringBuilder) spanned).setSpan(newImageSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    ((SpannableStringBuilder) spanned).removeSpan(imageSpan);
                }
            }
            pq_nr.setText(spanned);

            if (todo.equals("view")) {
                btn1.setVisibility(View.GONE);
            } else {
                if (question.getType().equals("1") || question.getType().equals("3")) {
                    btn1.setVisibility(View.GONE);
                } else {
                    btn1.setVisibility(View.VISIBLE);
                    btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean sure = false;
                            if (question.getType().equals("2")) {
                                String str = "";
                                optionId = "";
                                SparseBooleanArray booleanArray = lv_xx1.getCheckedItemPositions();
                                for (int j = 0; j < booleanArray.size(); j++) {
                                    int key = booleanArray.keyAt(j);
                                    //放入SparseBooleanArray，未必选中
                                    if (booleanArray.get(key)) {
                                        //这样mAdapter.getItem(key)就是选中的项
                                        optionId += str + xxId1.get(key);
                                        str = "&";
                                    } else {
                                        //这里是用户刚开始选中，后取消选中的项
                                    }
                                }
                                if (!optionId.equals("")) {
                                    //optionId=question.getType()+"#"+questionId+"&"+optionId;
                                    daAn.put(question.getId(), optionId);//getData3();
                                    sure = submitExerciseQuestion(optionId, question);
                                }else {
                                    Toast.makeText(PracticePagerActivity.this,"请选择选项后再提交",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                optionId = question.getType();
                                daAn.put(question.getId(), optionId);
                                sure = submitExerciseQuestion(optionId, question);

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
                                p_tv_zsd.setText(zsd);
                                llyout1.setVisibility(View.VISIBLE);
                            }
                            if (question.getType().equals("2")){
                                setExerciseCorlor2(index);
                                if (index < questions.size() - 1 && sure) {
//                                    index++;
                                    //Question question2 = questions.get(index);
                                    //questionId = question2.getId();
                                    //getQuestion(questionId);
                                    //getQuestionIndex(questionId);
//======================修改选择了正确答案点击提交，标记出选项正确的答案，页面跳转下一题===========================
                                    for (int i = 0; i < xxTitle1.size(); i++) {
                                        if (question.getOption().get(i).getOptionIsCorrent().equals("0")) {
                                            xxBj1.put(i + "", i + "");
                                            xxMap1.put(i + "", 0 + "");
                                        }
                                    }
                                    baseAdapter.notifyDataSetChanged();
                                    if (index<questions.size()-2){
                                        int i=(index+1)%3;
                                        if (i==0){
                                            setView(view1,index+2);
                                        }else if (i==1){
                                            setView(view2,index+2);
                                        }else if (i==2){
                                            setView(recoverView,index+2);
                                        }
                                    }
                                    String string2 = xxMap1.get(1 + "");
                                    views.clear();
                                    views.add(recoverView);
                                    views.add(view1);
                                    views.add(view2);
                                    readerViewPager.setCurrentItem(index+1);

//======================修改选择了正确答案点击提交，页面跳转下一题===========================
                                }else{//修复<练习>-错题集-我要练习，多选题，选择了正确答案点击提交，正确答案没有显示出来（只有最后一题是多选题的情况下，才会出现该种情况，包括未做题目，错题集，标记题这三个模块）
//                                    if (daAnJl.get(question.getId()).equals("1")) {
                                        for (int i = 0; i < xxTitle1.size(); i++) {
                                            if (daAn.get(question.getId()) != null&&!daAn.get(question.getId()).equals("")) {
                                                if (daAn.get(question.getId()).indexOf(xxId1.get(i)) != -1) {
                                                    xxMap1.put(i + "", 1 + "");
                                                    xxBj1.put(i + "", i + "");
                                                }
                                            }
                                            if (question.getOption().get(i).getOptionIsCorrent().equals("0")) {
                                                xxMap1.put(i + "", 0 + "");
                                            }
//                                        }
                                    }
                                }
                            }
                            baseAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            if (daAnJl.get(question.getId()) != null || todo.equals("view")) {

                if (question.getType().equals("1")) {
                    for (int i = 0; i < xxTitle1.size(); i++) {
                        if (question.getOption().get(i).getOptionIsCorrent().equals("0")) {
                            //xxTitle.set(i, xxTitle.get(i)+"#0");
                            xxMap1.put(i + "", 0 + "");
                        }
                        if (!todo.equals("view")) {
                            if (daAnJl.get(question.getId()).equals("1")) {
                                if (xxId1.get(i).equals(daAn.get(question.getId()))) {
                                    //xxTitle.set(i, xxTitle.get(i)+"#1");
                                    xxMap1.put(i + "", 1 + "");
                                    xxBj1.put(i + "", i + "");
                                }
                            }
                        }
                    }
                } else if (question.getType().equals("2")) {
                    if (!todo.equals("view")) {
                        if (daAnJl.get(question.getId()).equals("1")) {
                            for (int i = 0; i < xxTitle1.size(); i++) {
                                if (daAn.get(question.getId()) != null) {
                                    if (daAn.get(question.getId()).indexOf(xxId1.get(i)) != -1) {
                                        xxMap1.put(i + "", 1 + "");
                                        xxBj1.put(i + "", i + "");
                                    }
                                }
                                if (question.getOption().get(i).getOptionIsCorrent().equals("0")) {
                                    xxMap1.put(i + "", 0 + "");
                                }
                            }
                        }else {//////////////////////修多选题答案正确标记出选项正确的答案//////////////////////////
                            for (int i = 0; i < xxTitle1.size(); i++) {
                                if (question.getOption().get(i).getOptionIsCorrent().equals("0")) {
                                    xxBj1.put(i + "", i + "");
                                    xxMap1.put(i + "", 0 + "");
                                }
                            }//////////////////////多选题答案正确标记出选项正确的答案//////////////////////////
                        }

                    } else {
                        for (int i = 0; i < xxTitle1.size(); i++) {
                            if (question.getOption().get(i).getOptionIsCorrent().equals("0")) {
                                xxMap1.put(i + "", 0 + "");
                            }
                        }
                    }
                } else if (question.getType().equals("3")) {
                    for (int i = 0; i < xxId1.size(); i++) {
                        if (question.getAnswer().getContent().equals(i + "")) {
                            xxMap1.put(i + "", 0 + "");
                        }
                        if (!todo.equals("view")) {
                            if (daAnJl.get(question.getId()).equals("1")) {
                                if (xxId1.get(i).equals(daAn.get(question.getId()))) {
                                    xxMap1.put(i + "", 1 + "");
                                    xxBj1.put(i + "", i + "");
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
                //设置解析与相关知识点
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
                p_tv_zsd.setText(zsd);
                llyout1.setVisibility(View.VISIBLE);

            }
            lv_xx1.setAdapter(baseAdapter);
            lv_xx1.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    if (daAn.get(question.getId()) == null && !todo.equals("view")) {
                        if (question.getType().equals("1") || question.getType().equals("3")) {
                            arg1.setBackgroundColor(0xfffec200);
                            String optionId = xxId1.get(arg2);
                            if (optionId != null && !optionId.equals("")) {
                                //optionId=getExerciseReturn.getCurrentQuesiton().getType()+"#"+questionId+"&"+optionId;
                                daAn.put(question.getId(), optionId);
                                boolean sure = submitExerciseQuestion(optionId, question);
                                setExerciseCorlor2(index);
                                if (index < questions.size() - 1 && sure) {//正确 进入下一题
                                    if (question.getType().equals("1")){
                                        for (int i = 0; i < xxTitle1.size(); i++) {
                                            if (question.getOption().get(i).getOptionIsCorrent().equals("0")) {
                                                xxBj1.put(i + "", i + "");
                                                xxMap1.put(i + "", 0 + "");
                                            }
                                        }
                                    }else if (question.getType().equals("3")){
                                        for (int i = 0; i < xxId1.size(); i++) {
                                            if (question.getAnswer().getContent().equals(i + "")) {
                                                xxMap1.put(i + "", 0 + "");
                                                xxBj1.put(i + "", i + "");
                                            }
                                        }
                                    }
                                    baseAdapter.notifyDataSetChanged();
//                                    lv_xx1.setAdapter(baseAdapter);
                                    if (index<questions.size()-2){
                                        int i=(index+1)%3;
                                        if (i==0){
                                             setView(view1,index+2);
                                        }else if (i==1){
                                            setView(view2,index+2);
                                        }else if (i==2){
                                            setView(recoverView,index+2);
                                        }
                                    }
                                    views.clear();
                                    views.add(recoverView);
                                    views.add(view1);
                                    views.add(view2);
                                    readerViewPager.setCurrentItem(index+1);
                                }else {//错误，留在本题并显示答案
                                    if (question.getType().equals("1")) {
                                        for (int i = 0; i < xxTitle1.size(); i++) {
                                            if (question.getOption().get(i).getOptionIsCorrent().equals("0")) {
                                                //xxTitle.set(i, xxTitle.get(i)+"#0");
                                                xxMap1.put(i + "", 0 + "");
                                            }
                                            if (!todo.equals("view")) {
                                                if (daAnJl.get(question.getId()).equals("1")) {
                                                    if (xxId1.get(i).equals(daAn.get(question.getId()))) {
                                                        //xxTitle.set(i, xxTitle.get(i)+"#1");
                                                        xxMap1.put(i + "", 1 + "");
                                                        xxBj1.put(i + "", i + "");
                                                    }
                                                }
                                            }
                                        }
                                    }  else if (question.getType().equals("3")) {
                                        for (int i = 0; i < xxId1.size(); i++) {
                                            if (question.getAnswer().getContent().equals(i + "")) {
                                                xxMap1.put(i + "", 0 + "");
                                            }
                                            if (!todo.equals("view")) {
                                                if (daAnJl.get(question.getId()).equals("1")) {
                                                    if (xxId1.get(i).equals(daAn.get(question.getId()))) {
                                                        xxMap1.put(i + "", 1 + "");
                                                        xxBj1.put(i + "", i + "");
                                                    }
                                                }
                                            }
                                        }

                                    }

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
                                    llyout1.setVisibility(View.VISIBLE);
                                    baseAdapter.notifyDataSetChanged();
//                                    lv_xx1.setAdapter(baseAdapter);
                                }
                            }
                        } else if (question.getType().equals("2")) {
                            SparseBooleanArray booleanArray = lv_xx1.getCheckedItemPositions();
                            if (booleanArray.get(arg2)) {
                                arg1.setBackgroundColor(0xfffec200);
                            } else {
                                arg1.setBackgroundColor(0xfffff);
                            }
                        }
                    }
                }
            });
            lv_xx1.setOnFlingListener(new OnFlingListener() {

                @Override
                public void onLeftFling() {
                }

                @Override
                public void onRightFling() {
            }


        });
            closeLoading();
            //}
        } else {
            sendMessage(2);
        }
        return v;
    }


    /*
    * 加载题目统计数据
    * */
    public void setPracticeTj(View v){
        String dtcs="0",zql="0%",cw="0",zq="0",zjyc="";
        LinearLayout p_ll_tj=(LinearLayout) v.findViewById(R.id.p_ll_tj);
        p_ll_tj.setVisibility(View.VISIBLE);
        //需要显示的控件 正确率p_tv_zql 答题次数p_tv_dtcs  错误p_tv_cw  正确p_tv_zq 最后一次p_tv_zjyc
        TextView p_tv_zql=(TextView)v.findViewById(R.id.p_tv_zql);
        TextView p_tv_dtcs=(TextView)v.findViewById(R.id.p_tv_dtcs);
        TextView p_tv_cw=(TextView)v.findViewById(R.id.p_tv_cw);
        TextView p_tv_zq=(TextView)v.findViewById(R.id.p_tv_zq);
        TextView p_tv_zjyc=(TextView)v.findViewById(R.id.p_tv_zjyc);
        if (getPracticeTjReturn.getSum()!=null&&!getPracticeTjReturn.getSum().equals("")){
            dtcs=getPracticeTjReturn.getSum();
        }
        if (getPracticeTjReturn.getRightCount()!=null&&!getPracticeTjReturn.getRightCount().equals("")){
            zq=getPracticeTjReturn.getRightCount();
        }
        if (getPracticeTjReturn.getWrongCount()!=null&&!getPracticeTjReturn.getWrongCount().equals("")){
            cw=getPracticeTjReturn.getWrongCount();
        }
        if (getPracticeTjReturn.getAccuracy()!=null&&!getPracticeTjReturn.getAccuracy().equals("")){
            zql=getPracticeTjReturn.getAccuracy()+"%";
        }
        if (getPracticeTjReturn.getAnswerStatus()!=null&&!getPracticeTjReturn.getAnswerStatus().equals("")){
            zjyc=getPracticeTjReturn.getAnswerStatus();
        }
        p_tv_zql.setText(zql);
        p_tv_dtcs.setText(dtcs);
        p_tv_cw.setText(cw);
        p_tv_zq.setText(zq);
        p_tv_zjyc.setText(zjyc);

    }
}