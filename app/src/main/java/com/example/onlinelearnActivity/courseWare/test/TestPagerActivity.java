package com.example.onlinelearnActivity.courseWare.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entity.ExerciseCard;
import com.example.entity.Option;
import com.example.entity.Paper;
import com.example.entity.Question;
import com.example.entity.Questiontype;
import com.example.entity.Questiontypes;
import com.example.jsonReturn.BuildPaperReturn;
import com.example.jsonReturn.CheckQuestionReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.BaseActivity;
import com.example.onlinelearnActivity.alert.HintPracticActivity;
import com.example.onlinelearnActivity.courseWare.practic.PracticePagerActivity;
import com.example.spt.jaxb.paper.CurQuestion;
import com.example.spt.jaxb.paper.QuestionModel;
import com.example.spt.jaxb.paper.ResQuestionTypeRuleModel;
import com.example.util.ApplicationUtil;
import com.example.util.FinalStr;
import com.example.util.HttpUtil;
import com.example.util.JsonUtil;
import com.example.util.SysUtil;
import com.example.view.NoScrollListView;
import com.example.view.dialog.AlertDialog;
import com.example.view.dialog.MaterialDialog;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.AdapterView.OnItemClickListener;
import java.util.Map;

public class TestPagerActivity extends BaseActivity {
    private ApplicationUtil hdaplication;//全局参数对象
    ViewPager readerViewPager;
    MaterialDialog alertDialog;
    RadioGroup rGroup;
    private Handler handler;///异步刷新组件
    private static final String YOUR_PREF_FILE_NAME = null;
    Map<String,Question> questionMap=new HashMap<String, Question>();
    Map<String,String> daAn=new HashMap<String,String>();
    Map<String,Integer> daAnJl=new HashMap<String,Integer>();
    Map<String,String> rightOptionMap;
    Questiontypes questiontypes;
    TableLayout test_ll;
    int questionIndex=1;
    int index=0;
    String studentPaperId;//作答记录主键
    String todo="do";//测试类型  do 测试  view 查看
    private String questionForm="0";//��Ŀ��ʽ
    int questionType=0;//��Ŀ���� 1��ѡ 2��ѡ 3�ж� 5 7���  8���ʽ���
    RadioGroup test_rg_option;//��ѡ �жϵİ�ť����
    LinearLayout test_ll_option;//ѡ���
    //String optionId;//ѡ������ ѡ��
    //Button btn;//�ύ��ť
    int allQuestionCount=0;
    List<CheckBox> checkBoxList;//��ѡ��ť����
    String exerciseStatus;//答题卡状态
    String jsonstr="";
    ExerciseCard exerciseCard;
    TableLayout pe_ll;
    Button lastButton;
    TextView test_retrun;
    String imageStr;//题型图片
    String isright ="";
    String rightCount ="";
    String signString="";
   // Question question;
    //TextView tv;
//    TextView pq_nr;
    Map<String,String> correctIdsAndErrorIds = new HashMap<String,String>();
    Map<String,String> signQuestionMap = new HashMap<String,String>();
    Map<String,String> errorQuestionMap = new HashMap<String,String>();
    RelativeLayout test_ll_title;
    LinearLayout test_sl_ll;
    Drawable drawable_zq,drawable_cw,ui_icon_chose0,ui_icon_chose1;
    NoScrollListView lv_xx;
    TextView test_sd_tx;
    //ѡ������
    List <String> xxTitle=new ArrayList<String>();
    //ѡ��Id
    List <String> xxId=new ArrayList<String>();
    Map<String, String> xxMap=new HashMap<String, String>();
    Map<String, String> xxBj=new HashMap<String, String>();
    private ScrollView gv;
    private SlidingDrawer sd;
    Drawable exeBtnError,exeBtnCurror,exeBtnNow,exeBorder ,shape,shape2,exeBtnFin;
    Boolean sdStatus=false;//��¼���⿨�Ƿ��״̬����ʼֵΪfalse
    Html.ImageGetter imageGetter;
    CustomImageSpan imageSpan;
    Map<String ,Object> buildPaperReturnMap=new HashMap<String ,Object>();
    private List<View> views = new ArrayList<View>();
    String paperId="";
    //Paper paper=new Paper();
    double objectiveScore;
    Dialog dialog;
    List<String > questionIds;
    MyAdapterPager myAdapterPager;
    View recoverView ;
    View view1 ;
    View view2;
    int preItem=0;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_pager);
        setCache();
        /*gv = (ScrollView) findViewById(R.id.test_sv);
        gv.setOnTouchListener(this);*/
       // setUi();
        updateUi();
    }

    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        test_retrun=findViewById(R.id.test_retrun);
        test_retrun.setTypeface(iconfont);
        setToobbarShow(false);
    }

    @Override
    protected void initViews(View view) {
        showLoading();
        test_ll=(TableLayout)findViewById(R.id.test_ll);
        //判断是否第一次启动该程序
        SharedPreferences setting = getSharedPreferences(YOUR_PREF_FILE_NAME, 0);
        Boolean user_first = setting.getBoolean("FIRST",true);
        if(user_first){//第一次
            setting.edit().putBoolean("FIRST", false).commit();
            //弹出手指滑动切换题目的提示
            Intent intent1 = new Intent();
            intent1.setClass(TestPagerActivity.this,HintPracticActivity.class);
            startActivity(intent1);
        }
        //tv=(TextView)findViewById(R.id.test_btn_sign);
        test_ll_title=(RelativeLayout)findViewById(R.id.test_ll_title);
        test_sl_ll=(LinearLayout)findViewById(R.id.test_sl_ll);
        test_sd_tx=(TextView)findViewById(R.id.test_sd_tx);
        //答题卡
        sd = (SlidingDrawer) findViewById(R.id.sliding);
        //int[] mTouchableIds={R.id.test_btn_sign};
        //答题卡打开时执行
        sd.setOnDrawerOpenListener(new OnDrawerOpenListener() {
            public void onDrawerOpened(){
                sdStatus=true;
                test_sl_ll.setBackgroundColor(0x509D9D9D);
                //tv.setFocusable(false);
                //tv.setClickable(false);
            }});
        //答题卡关闭时执行
        sd.setOnDrawerCloseListener(new OnDrawerCloseListener() {
            public void onDrawerClosed() {
                sdStatus=false;
                test_sl_ll.setBackgroundColor(0x99FFF);
                //tv.setFocusable(true);
                //tv.setClickable(true);
                //tv.requestFocus();
            }
        });
        lastButton=new Button(getApplicationContext());
        checkBoxList=new ArrayList<CheckBox>();
        Intent intent = getIntent();
        if (intent.getStringExtra("studentPaperId")!=null&&!intent.getStringExtra("studentPaperId").equals("")){
            studentPaperId = intent.getStringExtra("studentPaperId");
            paperId = intent.getStringExtra("paperId");
            todo="view";
        }
        //初始化各种图片背景
        shape=getResources().getDrawable(R.drawable.shape);//蓝色弧边按钮
        shape.setBounds(0, 0, shape.getMinimumWidth(),shape.getMinimumHeight());  //此为必须写的
        shape2=getResources().getDrawable(R.drawable.shape2);//灰色弧边按钮
        shape2.setBounds(0, 0, shape2.getMinimumWidth(),shape2.getMinimumHeight());  //此为必须写的
        //初始化各种图片背景
        drawable_zq = getResources().getDrawable(R.drawable.u20);//选项答对图片
        drawable_zq.setBounds(0, 0, 54, 54);  //此为必须写的
        drawable_cw = getResources().getDrawable(R.drawable.u21);//选项答错图片
        drawable_cw.setBounds(0, 0,  54, 54);  //此为必须写的
        ui_icon_chose0 = getResources().getDrawable(R.drawable.u19);//选项默认图片
        ui_icon_chose0.setBounds(0, 0,  54, 54);  //此为必须写的
        ui_icon_chose1 = getResources().getDrawable(R.drawable.u17);//选项选中图片
        ui_icon_chose1.setBounds(0, 0,  54, 54);  //此为必须写的
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

        imageGetter = new Html.ImageGetter() {

            public Drawable getDrawable(String source) {
                Drawable drawable=null;
                int rId=Integer.parseInt(source);
                drawable=getResources().getDrawable(rId);
                drawable.setBounds(0, 0, 50,50);
                return drawable;
            };
        };
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

        public LoadImage(TextView mTextView) {
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
            //resource
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

//                mDrawable.setBounds(0, 0, (int) (bitmap.getWidth()*bl), 100);
                mDrawable.setBounds(0, 0, (int) (bitmap.getWidth()*1.5), (int)(bitmap.getHeight() * 1.5));
//修复图片过大时，显示不完整,改为1.5
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

    @Override
    protected void doBusiness(Context mContext) {
        getData();
    }

    public void setCache() {
        hdaplication = (ApplicationUtil) getApplication();
        hdaplication.addActivity(this);
    }

    public void setUi(){
        showLoading();
        test_ll=(TableLayout)findViewById(R.id.test_ll);
        //判断是否第一次启动该程序
        SharedPreferences setting = getSharedPreferences(YOUR_PREF_FILE_NAME, 0);
        Boolean user_first = setting.getBoolean("FIRST",true);
        if(user_first){//第一次
            setting.edit().putBoolean("FIRST", false).commit();
            //弹出手指滑动切换题目的提示
            Intent intent1 = new Intent();
            intent1.setClass(TestPagerActivity.this,HintPracticActivity.class);
            startActivity(intent1);
        }
        //tv=(TextView)findViewById(R.id.test_btn_sign);
        test_ll_title=(RelativeLayout)findViewById(R.id.test_ll_title);
        test_sl_ll=(LinearLayout)findViewById(R.id.test_sl_ll);
        test_sd_tx=(TextView)findViewById(R.id.test_sd_tx);
        //答题卡
        sd = (SlidingDrawer) findViewById(R.id.sliding);
        //int[] mTouchableIds={R.id.test_btn_sign};
        //答题卡打开时执行
        sd.setOnDrawerOpenListener(new OnDrawerOpenListener() {
            public void onDrawerOpened(){
                sdStatus=true;
                test_sl_ll.setBackgroundColor(0x509D9D9D);
                //tv.setFocusable(false);
                //tv.setClickable(false);
            }});
        //答题卡关闭时执行
        sd.setOnDrawerCloseListener(new OnDrawerCloseListener() {
            public void onDrawerClosed() {
                sdStatus=false;
                test_sl_ll.setBackgroundColor(0x99FFF);
                //tv.setFocusable(true);
                //tv.setClickable(true);
                //tv.requestFocus();
            }
        });
        lastButton=new Button(getApplicationContext());
        checkBoxList=new ArrayList<CheckBox>();
        Intent intent = getIntent();
        if (intent.getStringExtra("studentPaperId")!=null&&!intent.getStringExtra("studentPaperId").equals("")){
            studentPaperId = intent.getStringExtra("studentPaperId");
            paperId = intent.getStringExtra("paperId");
            todo="view";
        }
        //初始化各种图片背景
        shape=getResources().getDrawable(R.drawable.shape);//蓝色弧边按钮
        shape.setBounds(0, 0, shape.getMinimumWidth(),shape.getMinimumHeight());  //此为必须写的
        shape2=getResources().getDrawable(R.drawable.shape2);//灰色弧边按钮
        shape2.setBounds(0, 0, shape2.getMinimumWidth(),shape2.getMinimumHeight());  //此为必须写的
        drawable_zq = getResources().getDrawable(R.drawable.ui_icon_chose_current);//选项答对图片
        drawable_zq.setBounds(0, 0, drawable_zq.getMinimumWidth(),drawable_zq.getMinimumHeight());  //此为必须写的
        drawable_cw = getResources().getDrawable(R.drawable.ui_icon_chose_error);//选项答错图片
        drawable_cw.setBounds(0, 0, drawable_cw.getMinimumWidth(),drawable_cw.getMinimumHeight());  //此为必须写的
        ui_icon_chose0 = getResources().getDrawable(R.drawable.ui_icon_chose0);//选项默认图片
        ui_icon_chose0.setBounds(0, 0, ui_icon_chose0.getMinimumWidth(),ui_icon_chose0.getMinimumHeight());  //此为必须写的
        //初始化答题卡按钮颜色
        exeBtnError = getResources().getDrawable(R.drawable.exe_btn_error1);//错题 红色
        exeBtnError.setBounds(0, 0, exeBtnError.getMinimumWidth(),exeBtnError.getMinimumHeight());
        exeBtnCurror = getResources().getDrawable(R.drawable.exe_btn_curror1);//对题 绿色
        exeBtnCurror.setBounds(0, 0, exeBtnCurror.getMinimumWidth(),exeBtnCurror.getMinimumHeight());
        exeBtnNow = getResources().getDrawable(R.drawable.exe_btn_now);//当前题 蓝色
        exeBtnNow.setBounds(0, 0, exeBtnNow.getMinimumWidth(),exeBtnNow.getMinimumHeight());
        exeBorder = getResources().getDrawable(R.drawable.btn_border);//未做 透明
        exeBorder.setBounds(0, 0, exeBorder.getMinimumWidth(),exeBorder.getMinimumHeight());


        imageGetter = new Html.ImageGetter() {

            public Drawable getDrawable(String source) {
                Drawable drawable=null;
                int rId=Integer.parseInt(source);
                drawable=getResources().getDrawable(rId);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            };
        };
    }


    public void updateUi(){
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: //无法加载数据
                        Toast.makeText(TestPagerActivity.this,"无法加载数据,请检查你的网络连接或者联系我们！",
                                Toast.LENGTH_LONG).show();
                        break;
                    case 1: //加载数据
                        setList();
                        break;
                    case 2://没有获取时，清除页面
                        // removeview();
                        Toast.makeText(TestPagerActivity.this, "该章节试卷不支持查看！", Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    case 3:
                        //设置答题卡
                        setExerciseCordList();
                        break;
                    case 4:
                        //设置答题卡按钮变化
                        showAlertFh();
                        break;
                    case 5:
                        //设置标记
                        //setSign();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }


    private void sendMessage(int str){
        Message msg = new Message();
        msg.what = str;
        TestPagerActivity.this.handler.sendMessage(msg);
    }

    public void showAlertFh(){
        closeLoading();
        alertDialog=new MaterialDialog(TestPagerActivity.this);
        alertDialog.setTitle("提示");
        alertDialog.setMessage("该测试你获得"+objectiveScore+"分");
        alertDialog.setNegativeButton("查看测试",new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("重新测试",new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showLoading();
                studentPaperId="";
                daAn.clear();
                daAnJl.clear();
                todo="do";
                getData();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

   /* *//**
     * 加载框
     *//*
    private void showLoading(){
        if (dialog==null){
            dialog=new android.app.AlertDialog.Builder(TestPagerActivity.this).create();
            dialog.show();
            dialog.setContentView(R.layout.loading_process_dialog_anim);
        }else {
            dialog.show();
        }
    }

    *//**
     * 关闭加载
     *//*
    private void closeLoading(){
        dialog.dismiss();
    }
*/
    public void setList() {
        recoverView=View.inflate(TestPagerActivity.this, R.layout.test_pager_item, null);
        view1=View.inflate(TestPagerActivity.this, R.layout.test_pager_item, null);
        view2=View.inflate(TestPagerActivity.this, R.layout.test_pager_item, null);
        readerViewPager=(ViewPager)findViewById(R.id.readerViewPager);
        index=0;
        setView(recoverView,0);
        // View recoverView =View.inflate(PracticePagerActivity.this, R.layout.progress_item, null);
        if (questionMap.size()>1){
            setView(view1,1);
        }
        if (questionMap.size()>2){
            setView(view2,2);
        }
        views.clear();
        views.add(recoverView);
        views.add(view1);
        views.add(view2);
        myAdapterPager=new MyAdapterPager();
        readerViewPager.setAdapter(myAdapterPager);
        preItem=0;
        final int in=index+1;
        test_sd_tx.setText("全部："+in+"/"+questionMap.size());
        readerViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setExerciseCorlor2(preItem);
                if(position>preItem){
                    //下一题
                    if (position-preItem==1){
                        int i=position%3;
                        index=position;
                        if (index<questionMap.size()-1){
                            if (i==0){
                                setView(view1,index+1);
                                setView(recoverView,index);
//                                if (index>=0){
//                                }
//                                if (index<questionMap.size()-2){
////                                    setView(view2,index+2);
//                                }
                            }else if (i==1){
                                setView(view2,index+1);
                                setView(view1,index);
//                                if (index>=0){
//                                    setView(view1,index);
//                                }
//                                if (index<questionMap.size()-2){
////                                    setView(recoverView,index+2);
//                                }
                            }else if (i==2){
                                setView(recoverView,index+1);
                                setView(view2,index);
//                                if (index>=0){
//                                }
//                                if (index<questionMap.size()-2){
//                                    setView(view1,index+2);
//                                }
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
//                                if (index>1){
//                                    setView(view1,index-2);
//                                }
//                                if (index<questionMap.size()){
//                                    setView(recoverView,index);
//                                }
                            }else if (i==1){
                                setView(recoverView,index-1);
                                setView(view1,index);
//                                if (index>1){
//                                    setView(view2,index-2);
//                                }
//                                if (index<questionMap.size()){
//                                }
                            }else if (i==2){
                                setView(view1,index-1);
                                setView(view2,index);
//                                if (index>1){
//                                    setView(recoverView,index-2);
//                                }
//                                if (index<questionMap.size()){
//                                }
                            }
                        }
                        views.clear();
                        views.add(recoverView);
                        views.add(view1);
                        views.add(view2);
                    }
                    preItem = position;
                }
                lastButton = (Button) findViewById(index);
                lastButton.setBackground(exeBtnNow);
                final int in=index+1;
                test_sd_tx.setText("全部："+in+"/"+questionMap.size());
                return;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    //获取测试题目数据
    public void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("behaviorType", "2");
                map.put("coursewareId", hdaplication.getCoursewareId());
                map.put("classId", hdaplication.getClassId());
                map.put("behaviorId", hdaplication.getBehaviorId());
                map.put("accountId", hdaplication.getStuid());
                map.put("studentPaperId", studentPaperId);
                map.put("paperId", paperId);
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?buildPaper",
                        map,TestPagerActivity.this,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                    sendMessage(0);
                }else{
                    BuildPaperReturn buildPaperReturn=JsonUtil.parserString(result, BuildPaperReturn.class);
                    if (buildPaperReturn!=null&& buildPaperReturn.isHasPaper()) {
                        Paper paper=buildPaperReturn.getPaper();
                        buildPaperReturnMap.put("paperId",paper.getId());
                        buildPaperReturnMap.put("studentPaperId",buildPaperReturn.getStudentPaperId());
                        questiontypes=paper.getQuestiontypes();
                        rightOptionMap=getRightOption(paper);
                        List<Question> question=paper.getQuestions().getQuestion();
                        questionMap.clear();//修复测试题、点提交、点重新测试，本来题目是10的然后回随机变动
//                        Log.d("TesPagerActivity","questionMap:"+questionMap.size());
                        if (question!=null) {
                            for (Question item : question) {
                                questionMap.put(item.getId(), item);
//                                Log.d("TesPagerActivity","questionMap.size:"+questionMap.size()+"=="+item.getId()+"=="+item);
                            }
                        }
                        setAnswer(buildPaperReturn.getObjectiveAnswerJson());
                        sendMessage(3);
                        sendMessage(1);
                    }else {
                        sendMessage(2);
                    }
                }
            }
        }).start();
    }

    //提交题目
    public void getData2(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("behaviorType", "2");
                map.put("coursewareId", hdaplication.getCoursewareId());
                map.put("classId", hdaplication.getClassId());
                map.put("behaviorId", hdaplication.getBehaviorId());
                map.put("accountId", hdaplication.getStuid());
                map.put("paperId",  buildPaperReturnMap.get("paperId"));
                map.put("standard", "60");
                map.put("studentPaperId", buildPaperReturnMap.get("studentPaperId"));
                map.put("returnJSON",jsonstr);
                map.put("accountName", hdaplication.getUsername());
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?checkQuestion",
                        map,TestPagerActivity.this,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                    sendMessage(0);
                }else{
                    CheckQuestionReturn checkQuestionReturn=JsonUtil.parserString(result, CheckQuestionReturn.class);
                    if (checkQuestionReturn!=null) {
                        rightOptionMap=checkQuestionReturn.getRightOptionMap();
                        objectiveScore=checkQuestionReturn.getObjectiveScore();
                        /*Paper paper=checkQuestionReturn.getPaper();
                        questiontypes=paper.getQuestiontypes();
                        List<Question> question=paper.getQuestions().getQuestion();
                        if (question!=null) {
                            for (Question item : question) {
                                questionMap.put(item.getId(), item);
                            }
                        }*/
                        todo="view";
                        index=0;
                        sendMessage(4);
                        sendMessage(3);
                        sendMessage(1);
                    }else {
                        sendMessage(2);
                    }
                }
            }
        }).start();
    }

    //答题卡数据
    public void setExerciseCordList(){
        try {
            if(questiontypes!=null){
                WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                int width = wm.getDefaultDisplay().getWidth();
                int widths=width/36;
                test_ll.removeAllViews();
                List<Questiontype> questiontype=questiontypes.getQuestiontype();
                questionIds=new ArrayList<String>();
                int xuhao=0;
                TextView tView1=new TextView(getApplicationContext());
                String aString ="&nbsp&nbsp&nbsp&nbsp<img src=\""+R.drawable.u40+"\"  />:当前题 &nbsp&nbsp&nbsp <img src=\""+R.drawable.u39+"\"  />:已答  &nbsp&nbsp&nbsp<img src=\""+R.drawable.u31+"\"  />:答对  &nbsp&nbsp&nbsp<img src=\""+R.drawable.u32+"\"  />:答错   ";
                tView1.setText(Html.fromHtml(aString, imageGetter, null));
                tView1.setTextColor(0xff000000);
                test_ll.addView(tView1);
                if(questiontype!=null&&questiontype.size()>0){
                    for(int i=0;i<questiontype.size();i++){//������Ŀ����
                        List<CurQuestion> curQuestion=questiontype.get(i).getCurQuestion();
                        if(curQuestion!=null&&curQuestion.size()>0){
                            //�ж���Ŀ����
                            String type="1";
                            System.out.print(questiontype.get(i).getQuestionTypeName());
                            if (questiontype.get(i).getQuestionTypeName().equals("单选题")){
                                type="1";
                            }else if (questiontype.get(i).getQuestionTypeName().equals("多选题")){
                                type="2";
                            }else if (questiontype.get(i).getQuestionTypeName().equals("判断题")){
                                type="3";
                            }else if (questiontype.get(i).getQuestionTypeName().equals("名词解释题")){
                                type="8";
                            }else if (questiontype.get(i).getQuestionTypeName().equals("简答题")){
                                type="7";
                            }else if (questiontype.get(i).getQuestionTypeName().equals("论述题")){
                                type="5";
                            }
                            //��ʾ��Ŀ����
                            TextView tView=new TextView(getApplicationContext());
                            tView.setText(questiontype.get(i).getQuestionTypeName());
                            tView.setTextColor(0xff000000);
                            tView.setPadding(10, 25, 10, 10);
                            test_ll.addView(tView);

                            int size = curQuestion.size(); // ���Button�ĸ���
                            TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT); // ÿ�е�ˮƽLinearLayout
                            TableRow llayout = null;
                            for(int j=0;j<size;j++){//����ÿ�������е�������Ŀ
                                /*if (xuhao==1) {
                                    questionId=curQuestion.get(j).getQuestionId();
                                }*/
                                //questionIdMap.put(xuhao+"",curQuestion.get(j).getQuestionId());
                                TableRow.LayoutParams itemParams = new TableRow.LayoutParams(widths*5 ,widths*5);
                                String result="";
                                itemParams.setMargins(widths/2, widths/4, widths/2, widths/4);
                                if(j%6==0){
                                    llayout=new TableRow(getApplicationContext());
                                }
                                String idString=type+""+curQuestion.get(j).getIndex();
                                //String idString=questionList.get(j).getQuestionType()+""+questionList.get(j).getQuestionIndex();
                                int id=Integer.parseInt(idString);
                                Button childBtn = (Button)LayoutInflater.from(this).inflate(R.layout.exercise_button, null);
                                //=============修复答题卡作图题的下标序号都是1============================
//                                childBtn.setText(curQuestion.get(j).getIndex());
                                int index = j + 1;
                                childBtn.setText(index+"");
                                //=============修复答题卡作图题的下标序号都是1============================
                                childBtn.setTag(curQuestion.get(j).getQuestionId()+"#"+curQuestion.get(j).getIndex()+"#"+xuhao);
                                questionIds.add(curQuestion.get(j).getQuestionId());
                                //childBtn.setId(id);
                                childBtn.setId(xuhao);
                                childBtn.setLayoutParams(itemParams);
                                if (todo.equals("view")){
                                    if (daAn.get(curQuestion.get(j).getQuestionId())!=null){
                                        //4028818f54943d7801549442511f02b7=1_4028818f54943d7801549442511f02b7_4028818f54943d7801549442515b02ba&,
                                        if (rightOptionMap.get(curQuestion.get(j).getQuestionId())!=null){
                                            String right=rightOptionMap.get(curQuestion.get(j).getQuestionId());
                                            String [] strings=right.split("_");
                                            if (strings!=null&&strings.length==3){
                                                String [] s=strings[2].split("&");
                                                String [] dA=daAn.get(curQuestion.get(j).getQuestionId()).split("_");
                                                String [] daString=dA[1].split("&");
                                                if (strings[0].equals("2")){//多选题
                                                    if (daString.length-s.length==0){
                                                        boolean sult=false;
                                                        b1:for (int a=0;a<daString.length;a++){
                                                            sult=false;
                                                            for(int b=0;b<s.length;b++){
                                                                if (daString[a].equals(s[b])){
                                                                    sult=true;
                                                                }
                                                            }
                                                            if (!sult){
                                                                correctIdsAndErrorIds.put(curQuestion.get(j).getQuestionId(),"错误");
                                                                childBtn.setBackground(exeBtnError);
                                                                break b1;
                                                            }
                                                        }
                                                        if (sult){
                                                            correctIdsAndErrorIds.put(curQuestion.get(j).getQuestionId(),"正确");
                                                            childBtn.setBackground(exeBtnCurror);
                                                        }
                                                    }else {
                                                        correctIdsAndErrorIds.put(curQuestion.get(j).getQuestionId(),"错误");
                                                        childBtn.setBackground(exeBtnError);
                                                    }
                                                }else {//单选题或判断题
                                                    if (daString[0].equals(s[0])){
                                                        correctIdsAndErrorIds.put(curQuestion.get(j).getQuestionId(),"正确");
                                                        childBtn.setBackground(exeBtnCurror);
                                                    }else {
                                                        correctIdsAndErrorIds.put(curQuestion.get(j).getQuestionId(),"错误");
                                                        childBtn.setBackground(exeBtnError);
                                                    }
                                                }
                                            }else {
                                                correctIdsAndErrorIds.put(curQuestion.get(j).getQuestionId(),"错误");
                                                childBtn.setBackground(exeBtnError);
                                            }
                                        }else {
                                            correctIdsAndErrorIds.put(curQuestion.get(j).getQuestionId(),"错误");
                                            childBtn.setBackground(exeBtnError);
                                        }
                                    }else {
                                        correctIdsAndErrorIds.put(curQuestion.get(j).getQuestionId(),"错误");
                                        childBtn.setBackground(exeBtnError);
                                    }
                                }
                                if(xuhao==0){
                                    childBtn.setBackground(exeBtnNow);
                                }
                                llayout.addView(childBtn);
                                llayout.setLayoutParams(layoutParams);
                                if (j%6==5) {
                                    test_ll.addView(llayout);
                                }else {
                                    if (j==curQuestion.size()-1) {
                                        for (int a = j%6; a < 6; a++) {
                                            TextView tv=new TextView(getApplicationContext());
                                            llayout.addView(tv);
                                        }
                                        test_ll.addView(llayout);
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


    public void click(View v){
        if(v.getId()==R.id.test_tj){//提交
            if (!todo.equals("view")){
                String questionId=questionIds.get(index);
                Question question=questionMap.get(questionId);
                if (question.getType().equals("2")){
                    updateDaAn();
                }
                if (daAn.size()==0){
                    alertDialog=new MaterialDialog(TestPagerActivity.this);
                    alertDialog.setTitle("提示");
                    alertDialog.setMessage("您一道题也没有作答，不能提交");
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                }else {
                    alertDialog=new MaterialDialog(TestPagerActivity.this);
                    alertDialog.setTitle("提示");
                    alertDialog.setMessage("您已答了"+daAn.size()+"题，共"+questionMap.size()+"题，请问是否提交？");
                    alertDialog.setNegativeButton("取消",new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setPositiveButton("提交",new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            showLoading();
                            jsonstr="[";
                            for (String key : daAn.keySet()) {//遍历单选题
                                String item="{\"questionId\":\""+key+"\",\"optionId\":\""+daAn.get(key)+"\"},";
                                jsonstr+=item;
                            }
                            jsonstr=jsonstr.substring(0,jsonstr.length()-1)+"]";
                            jsonstr=jsonstr.replace("&","#");//修复多项选择题没分数;原因是在拼接多项选择题时
                            getData2();
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        }/*else if (v.getId()==R.id.test_btn_sign) {//标记
			//getData4();
		}*/else if (v.getId()==R.id.p_btn_exerciseCard) {//答题卡⿨
        }else if(v.getId()==R.id.test_retrun) {
            this.finish();
        }else {//点击答题卡题目
            sd.close();
            setExerciseCorlor2(index);
            String questionId=questionIds.get(index);
            Question question=questionMap.get(questionId);
            if (question.getType().equals("2")&&!todo.equals("view")){
                updateDaAn();
            }
            String exe = (String) v.getTag();
            String[] strings = exe.split("#");
            if (strings != null && strings.length == 3) {
                index = Integer.parseInt(strings[2]);
            }
            int i=index%3;
            if (i==0){
                setView(recoverView,index);
                if (index>0){
                    setView(view2,index-1);
                }
                if (index<questionMap.size()-1){
                    setView(view1,index+1);
                }
            }else if (i==1){
                setView(view1,index);
                if (index>0){
                    setView(recoverView,index-1);
                }
                if (index<questionMap.size()-1){
                    setView(view2,index+1);
                }
            }else if (i==2){
                setView(view2,index);
                if (index>0){
                    setView(view1,index-1);
                }
                if (index<questionMap.size()-1){
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

    //设置答题卡颜色标记
    public void setExerciseCorlor2(int i) {
        String qId=questionIds.get(i);
        lastButton = (Button) findViewById(index);
        if(todo.equals("view")){
            if(correctIdsAndErrorIds!=null&&correctIdsAndErrorIds.containsKey(qId)){
                if (correctIdsAndErrorIds.get(qId).equals("错误")) {
                    lastButton.setBackground(exeBtnError);
                }else {
                    lastButton.setBackground(exeBtnCurror);
                }
            }else {
                lastButton.setBackground(exeBtnError);
            }
        }else {
            if (daAn.get(qId)!=null&&!daAn.get(qId).equals("")){
                lastButton.setBackground(exeBtnFin);
            }else {
                lastButton.setBackground(exeBorder);
            }
        }

    }

    //解析答案
    public void setAnswer(String json){
        //{"questionId":"ff808081586c57350158a8fd1efc00ac","optionId":"ff808081586c57350158a8fd1efc00ac_ff808081586c57350158a8fd1f0300ae"}
        //{"questionId":"ff808081586c57350158a8fd1efc00ac","optionId":"ff808081586c57350158a8fd1efc00ac_ff808081586c57350158a8fd1f0300ae&ff808081586c57350158a8fd1f0300ddd"}
        if (json!=null&&!json.equals("")){
            String []s=json.split("\\}");
            if (s!=null&s.length>0){
                for (int i=0;i<s.length;i++){
                    String[]s1=s[i].split("\",\"");
                    if (s1!=null&&s1.length==2){
                        String []options=s1[1].split(":");
                        if(options!=null&&options.length==2){
                            options[1]=options[1].substring(1,options[1].length()-1);
                            String []s2=options[1].split("_");
                            daAn.put(s2[0],options[1]);
                        }
                    }
                }
            }
        }
    }

    public Map<String,String>  getRightOption(Paper paper){
        Map<String,String> rightOptionMap=new HashMap<String,String>();
        List<Question> questionList = new ArrayList<Question>();//试卷题目
        questionList = paper.getQuestions().getQuestion();
        if(questionList!=null){//正确选项map
            for(Question question : questionList){//遍历试卷题目
                if(Integer.parseInt(question.getType())<4){
                    String rightOptionStr = question.getType() +"_"+ question.getId()+"_";//正确选项集合
                    if(question.getType().equals("3")){//判断题
                        rightOptionStr += question.getAnswer().getContent()+"&";
                    }else if(question.getType().equals("1") || question.getType().equals("2")){//选择题
                        for(Option option : question.getOption()){
                            if(option.getOptionIsCorrent().equals("0")){//正确选项
                                rightOptionStr += option.getId() + "&";
                            }
                        }
                    }
                    rightOptionMap.put(question.getId(), rightOptionStr);
                }
            }
        }
        return rightOptionMap;
    }

    public void updateDaAn(){
        /*if (question.getType().equals("2")){
            //{"questionId":"ff808081571cbe720157403fd3090988","optionId":"ff808081571cbe720157403fd3090988_ff808081571cbe720157403fd316098a#ff808081571cbe720157403fd31f098c"}
            String str="";
            //String str2=question.getType()+"#"+questionId+"";
            String str2=questionId+"_";
            SparseBooleanArray booleanArray = lv_xx.getCheckedItemPositions();
            boolean b=false;
            for (int j = 0; j < booleanArray.size(); j++) {
                int key = booleanArray.keyAt(j);
                if (booleanArray.get(key)) {
                    b=true;
                    str2=str2+str+xxId.get(key);
                } else {
                }
                str="#";
            }
            if (b){
                daAn.put(questionId,str2);
            }else {
                daAn.put(questionId,"");
            }
        }*/
    }

    //适配器，显示题目选项内容
    private class MyAdapterPager extends PagerAdapter {

        @Override
        public int getCount() {
            return questionMap.size();
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

            //container.removeView(views.get(position%3));
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

    }

    public View setView(View v,int z){
        final String questionId = questionIds.get(z);
        final Question question1=questionMap.get(questionId);
        final TextView pq_nr1=(TextView)v.findViewById(R.id.pq_nr);
        final List<String> xxTitle1 = new ArrayList<String>();
        //选项Id
        final List<String> xxId1 = new ArrayList<String>();
        final Map<String, String> xxMap1 = new HashMap<String, String>();
        final Map<String, String> xxBj1 = new HashMap<String, String>();

        //选项自定义listview
        final NoScrollListView lv_xx1=(NoScrollListView)v.findViewById(R.id.cw_test_lv_xx);

        if(question1!=null){
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
                        v = View.inflate(TestPagerActivity.this, R.layout.test_item, null);

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
                        tv_seekname.setText(Html.fromHtml(" "+ string, new Html.ImageGetter() {
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
//                    tv_seekname.setText("  " + Html.fromHtml(string));
                    if (string2 != null && string2.equals("0")) {
                        tv_seekname.setCompoundDrawables(drawable_zq, null, null, null);
                    } else if (string2 != null && string2.equals("1")) {
                        tv_seekname.setCompoundDrawables(drawable_cw, null, null, null);
                    } else {
                        tv_seekname.setCompoundDrawables(ui_icon_chose0, null, null, null);
                    }
                    if (xxBj1.get(i + "") != null) {
                        if (string2 == null) {
                            tv_seekname.setCompoundDrawables(ui_icon_chose1, null, null, null);
                        }
                        v.setBackgroundColor(0xfffec200);
                    } else {
                        v.setBackgroundColor(0xfffff);
                    }

                    return v;
                }
            };

//            SpannableString spannableString = new SpannableString("a "+getQuestionIndex(question1.getId())+"."+Html.fromHtml(question1.getContent()));
            String ids=question1.getType()+getQuestionIndex(question1.getId());
            int idInt=Integer.parseInt(ids);
            /*lastButton=(Button)findViewById(index);
            lastButton.setBackground(exeBtnNow);*/
            String tx="题型";
            //int sum=0;
            /*xxTitle1.clear();
            xxMap1.clear();
            xxBj1.clear();
            xxId1.clear();*/
            if(null != question1.getOption() && question1.getOption().size() > 0){//�ж����ͣ�����ѡ��
                if(question1.getType().equals("1")){
//                    imageSpan = new CustomImageSpan(this, R.drawable.icon_chose, 2);

                    imageStr="<img src='"+R.drawable.icon_chose+"'  />";
                    tx="单选题";
                    lv_xx1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//������ѡģʽ

                    for(int i=0;i<question1.getOption().size();i++){
                        xxTitle1.add(question1.getOption().get(i).getContent());
                        xxId1.add(question1.getOption().get(i).getId());
                    }
                }else if(question1.getType().equals("2")){
                    //btn.setText("�ύ");
//                    imageSpan = new CustomImageSpan(this, R.drawable.icon_chose2, 2);
                    imageStr="<img src='"+R.drawable.icon_chose2+"'  />";
                    tx="多选题";
                    lv_xx1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);//������ѡģʽ
                    for(int i=0;i<question1.getOption().size();i++){
                        xxTitle1.add(question1.getOption().get(i).getContent());
                        xxId1.add(question1.getOption().get(i).getId());
                    }
                }else if(question1.getType().equals("4")){
                    //btn.setText("��ʾ�ο���");
//                    imageSpan = new CustomImageSpan(this, R.drawable.icon_chose7_2, 2);
                    imageStr="<img src='"+R.drawable.icon_chose7_2+"'  />";
                    tx="填空题";
                }
            }else{
                if(question1.getType().equals("3")){
//                    imageSpan = new CustomImageSpan(this, R.drawable.icon_chose3, 2);
                    imageStr="<img src='"+R.drawable.icon_chose3+"'  />";
                    tx="判断题";
                    lv_xx1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//开启单选模式
                    xxTitle1.add(" 正确");
                    xxId1.add("0");
                    xxTitle1.add(" 错误");
                    xxId1.add("1");
                }else{
                    //btn.setText("��ʾ�ο���");
                    if(question1.getType().equals("5")){
//                        imageSpan = new CustomImageSpan(this, R.drawable.icon_chose6, 2);
                        imageStr="<img src='"+R.drawable.icon_chose6+"'  />";
                        tx="论述题";
                    }else if(question1.getType().equals("7")){
//                        imageSpan = new CustomImageSpan(this, R.drawable.icon_chose5, 2);
                        imageStr="<img src='"+R.drawable.icon_chose5+"'  />";
                        tx="简答题";
                    }else if(question1.getType().equals("8")){
//                        imageSpan = new CustomImageSpan(this, R.drawable.icon_chose4, 2);
                        imageStr="<img src='"+R.drawable.icon_chose4+"'  />";
                        tx="名词解析题";
                    }
                }
            }
//            spannableString.setSpan(imageSpan, 0,1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            Spanned spanned = Html.fromHtml(imageStr +getQuestionIndex(question1.getId())+ "." +question1.getContent(), new Html.ImageGetter() {
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
                                R.drawable.u19);
                        d.addLevel(0, 0, empty);
                        d.setBounds(0, 0, 50, 50);
                        new LoadImage(pq_nr1).execute(source, d);
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
            pq_nr1.setText(spanned);

            //String [] ss=(lastButton.getTag()+"").split("#");
            //index=Integer.parseInt(ss[2]);
            if(todo.equals("view")){
                if(question1.getType().equals("1")){
                    String [] daString=new String[]{};
                    if (daAn.get(question1.getId())!=null&&!daAn.get(question1.getId()).equals("")){
                        daString=daAn.get(question1.getId()).split("_");
                        //daString=daString[1].split("&");
                    }
                    for (int i=0;i<xxId1.size();i++) {
                        if (question1.getOption().get(i).getOptionIsCorrent().equals("0")) {
                            xxMap1.put(i+"", 0+"");
                        }
                        if (daString!=null&daString.length>1){
                            if (xxId1.get(i).equals(daString[1])) {
                                xxBj1.put(i+"", i+"");
                            }
                        }
                        if(correctIdsAndErrorIds.get(question1.getId()).equals("错误")){
                            if (daString!=null&daString.length>1){
                                if (xxId1.get(i).equals(daString[1])) {
                                    xxMap1.put(i+"", 1+"");
                                }
                            }
                        }
                    }
                }else if(question1.getType().equals("2")){
                    //{"questionId":"402881b951f2059a0151f224215e03e3","optionId":"402881b951f2059a0151f224215e03e3_402881b951f2059a0151f22421e203e6#402881b951f2059a0151f224220a03e7#402881b951f2059a0151f224223203e8"}
                    String [] daString=new String[]{};
                    if (daAn.get(question1.getId())!=null&&!daAn.get(question1.getId()).equals("")){
                        daString=daAn.get(question1.getId()).split("_");
                        daString=daString[1].split("&");
                    }
                    if(correctIdsAndErrorIds.get(question1.getId()).equals("错误")){
                        for (int i=0;i<xxId1.size();i++) {
                            if (daString!=null&&daString.length>=1){
                                for(int a=0;a<daString.length;a++){
                                    if(xxId1.get(i).equals(daString[a])){
                                        //xxTitle.set(i, xxTitle.get(i)+"#1");
                                        xxMap1.put(i+"", 1+"");
                                        xxBj1.put(i+"", i+"");
                                    }
                                }
                            }
                            if(question1.getOption().get(i).getOptionIsCorrent().equals("0")){
                                xxMap1.put(i+"", 0+"");
                            }
                        }
                    }else {
                        for (int i=0;i<xxId1.size();i++) {
                            if (daString!=null&daString.length>1){
                                for(int a=0;a<daString.length;a++){//修复选择了正确答案提交后，没有显示出全 选择的选项
                                    if(xxId1.get(i).equals(daString[a])){
                                    xxBj1.put(i+"", i+"");
                                }
                                }
                            }
                            if(question1.getOption().get(i).getOptionIsCorrent().equals("0")){
                                xxMap1.put(i+"", 0+"");
                            }
                        }
                    }
                }else if(question1.getType().equals("3")){
                    String [] daString=new String[]{};
                    if (daAn.get(question1.getId())!=null&&!daAn.get(question1.getId()).equals("")){
                        daString=daAn.get(question1.getId()).split("_");
                        //daString=daString[1].split("&");
                    }
                    for (int i=0;i<xxId1.size();i++) {
                        if(question1.getAnswer().getContent().equals(i+"")){
                            xxMap1.put(i+"", 0+"");
                        }
                        if(correctIdsAndErrorIds.get(question1.getId()).equals("错误")){
                            if (daString!=null&&daString.length>1){
                                if(xxId1.get(i).equals(daString[1])){
                                    xxMap1.put(i+"", 1+"");
                                    xxBj1.put(i+"", i+"");
                                }
                            }
                        }
                    }
                }

                LinearLayout llyout=(LinearLayout)v.findViewById(R.id.test_ll_status);
                TextView test_tv_jx = (TextView) v.findViewById(R.id.test_tv_jx);
                TextView test_tv_zsd = (TextView) v.findViewById(R.id.test_tv_zsd);
                //设置解析与相关知识点
                String explain = "略";
                if (question1.getExplain() != null&&!question1.getExplain().equals("")) {
                    explain = question1.getExplain().getContent();
                }
                test_tv_jx.setText(explain);
                String zsd = "";
                if (question1.getCoursenodes() != null && question1.getCoursenodes().size() > 0) {
                    for (int i = 0; i < question1.getCoursenodes().size(); i++) {
                        zsd += " " + question1.getCoursenodes().get(i).getNodename();
                    }
                }
                test_tv_zsd.setText(zsd);
                llyout.setVisibility(View.VISIBLE);
            }else {
                //test_tj.setVisibility(View.VISIBLE);
                String [] daString=new String[]{};
                if (daAn.get(question1.getId())!=null&&!daAn.get(question1.getId()).equals("")){
                    daString=daAn.get(question1.getId()).split("_");
                    daString=daString[1].split("&");
                }
                if (daString!=null&&daString.length>0) {
                    for (int i=0;i<xxId1.size();i++) {
                        for(int a=0;a<daString.length;a++){
                            if(xxId1.get(i).equals(daString[a])){
                                xxBj1.put(i+"", i+"");
                            }
                        }
                    }
                }
            }
            lv_xx1.setAdapter(baseAdapter);
            //点击选项
            lv_xx1.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (todo.equals("do")){
                        if (question1.getType().equals("1")||question1.getType().equals("3")) {
                            if (daAnJl.get(question1.getId())!=null&&!daAnJl.get(question1.getId()).equals(i)){
                                View v=lv_xx1.getChildAt(daAnJl.get(question1.getId()));
                                TextView tv_seekname = (TextView) v.findViewById(R.id.tvb);
                                tv_seekname.setCompoundDrawables(ui_icon_chose0, null, null, null);
                                v.setBackgroundColor(0xfffff);
                            }
                            view.setBackgroundColor(0xfffec200);
                            TextView tv_seekname = (TextView) view.findViewById(R.id.tvb);
                            tv_seekname.setCompoundDrawables(ui_icon_chose1, null, null, null);
                            String optionId=xxId1.get(i);
                            if(optionId!=null&&!optionId.equals("")){
                                //optionId=question.getType()+"#"+questionId+"&"+optionId;
                                optionId= questionId +"_"+optionId;
                                daAn.put(question1.getId(),optionId);
                                daAnJl.put(question1.getId(),i);
                            }
                            if (index<questionMap.size()-1){
                               // readerViewPager.setCurrentItem(index+1);
                            }
                        }else if (question1.getType().equals("2")) {
                            SparseBooleanArray booleanArray = lv_xx1.getCheckedItemPositions();
                            String optionIds = daAn.get(question1.getId());
                            if (optionIds == null || optionIds.equals("") || optionIds.indexOf(xxId1.get(i)) == -1) {
                                view.setBackgroundColor(0xfffec200);
                                TextView tv_seekname = (TextView) view.findViewById(R.id.tvb);
                                tv_seekname.setCompoundDrawables(ui_icon_chose1, null, null, null);
//                                String optionIds = daAn.get(question1.getId());
                                if (optionIds == null || optionIds.equals("")){
                                    optionIds = questionId + "_" + xxId1.get(i) + "&";
                                }else{
                                    optionIds = optionIds + xxId1.get(i) + "&";
                                }
                                daAn.put(question1.getId(),optionIds);
                            } else {
                                view.setBackgroundColor(0xfffff);
                                TextView tv_seekname = (TextView) view.findViewById(R.id.tvb);
                                tv_seekname.setCompoundDrawables(ui_icon_chose0, null, null, null);
                                optionIds = optionIds.replace(xxId1.get(i) + "&","");
                                daAn.put(question1.getId(),optionIds);
                            }
                        }
                    }
                }
            });
            lv_xx1.setOnFlingListener(new NoScrollListView.OnFlingListener() {

                @Override
                public void onLeftFling() {
                }

                @Override
                public void onRightFling() {
                }
            });
            closeLoading();
        }else {
            sendMessage(2);
        }
        return v;
    }

    public String getQuestionIndex(String questionid) {
        String index="1";
        List<Questiontype> questiontype=questiontypes.getQuestiontype();
        if (questiontype != null) {
            for (Questiontype q : questiontype) {
                if (q.getCurQuestion() != null) {//修复<测试>，“阴影与透视”，第二章测试，点击【开始测试】按钮，闪退  非空判断
                    for (CurQuestion m : q.getCurQuestion()) {
                        if (m.getQuestionId().equals(questionid)) {
                            index = m.getIndex();
                        }
                    }
                }
            }
        }
        return index;
    }
}
