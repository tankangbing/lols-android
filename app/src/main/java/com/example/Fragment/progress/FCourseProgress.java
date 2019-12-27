package com.example.Fragment.progress;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Fragment.BaseFragment;
import com.example.entity.LearnBehaviorModel;
import com.example.jsonReturn.getLearnProgressReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.courseWare.document.DocumentActivity;
import com.example.onlinelearnActivity.courseWare.practic.CourseWareChapterTestActivity;
import com.example.onlinelearnActivity.courseWare.test.CourseWareTestActivity;
import com.example.onlinelearnActivity.courseWare.test.TestActivity;
import com.example.onlinelearnActivity.courseWare.test.TestPagerActivity;
import com.example.onlinelearnActivity.courseWare.video.VideoActivity;
import com.example.spt.jaxb.course.Item;
import com.example.util.ApplicationUtil;
import com.example.util.FinalStr;
import com.example.util.HttpUtil;
import com.example.util.JsonUtil;
import com.example.util.SysUtil;
import com.example.view.circleProgress.ArcProgress;
import com.example.view.dialog.MaterialDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ysg on 2017/9/7.
 */

public class FCourseProgress extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.arc_progress)
    ArcProgress arc_progress;
    @BindView(R.id.cp_protext)
    TextView cp_protext;
    @BindView(R.id.cp_tb)
    TableLayout cp_tb;
    @BindView(R.id.cp_rg)
    RadioGroup cp_rg;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;//刷新
    @BindView(R.id.cp_lin)
    LinearLayout cp_lin;
    private ApplicationUtil hdaplication;//全局参数对象
    private Handler handler;//异步刷新组件
    getLearnProgressReturn attributes;
    //完成百分比
    private int percentage=0;
    String nodeId="";
    protected Typeface iconfont;
    MaterialDialog materialDialog;
    FragmentActivity activity ;
    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
        hdaplication = (ApplicationUtil)getActivity().getApplication();
        activity = getActivity();
        setUi();
        updateUi();

        iconfont = Typeface.createFromAsset(getActivity().getAssets(), "iconfont.ttf");
        cp_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.cp_rb_all){
                    setCourseProgress(attributes.getCourseItemList(),"00");
                }else if (i==R.id.cp_rb_ywc){
                    setCourseProgress(attributes.getCourseItemList(),"02");
                }else {
                    setCourseProgress(attributes.getCourseItemList(),"01");
                }
            }
        });
    }

    public void setUi(){
        ClassicsHeader classicsHeader =new ClassicsHeader(getActivity());
        classicsHeader.setSpinnerStyle(SpinnerStyle.Translate);
        //设置是否显示时间
        classicsHeader.setEnableLastTime(false);
        //设置无任务风格
        refreshLayout.setRefreshHeader(classicsHeader);

        refreshLayout.setRefreshFooter(new FalsifyFooter(getActivity()));

        refreshLayout.setReboundDuration(500);//回弹动画时长（毫秒）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                try {
                    getDate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
            }
        });
        refreshLayout.autoRefresh();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.course_progress;
    }

    @Override
    public void fetchData() {

    }

    public static FCourseProgress newInstance() {
        FCourseProgress fragment = new FCourseProgress();

        return fragment;
    }

    public void updateUi(){
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: //无法加载数据
                        if(activity!=null){//修复多点奔溃
                            Toast.makeText(activity,"无法加载数据,请检查你的网络连接或者联系我们！",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1: //加载数据
                        setList();
                        refreshLayout.finishRefresh();
                        break;
                    case 2:
                        if(activity!=null){//修复多点奔溃
                            Toast.makeText(activity,"没有相关的未学课程信息",
                                    Toast.LENGTH_SHORT).show();
                        }break;
                }
                super.handleMessage(msg);
            }
        };
    }

    private void sendMessage(int str){
        Message msg = new Message();
        msg.what = str;
        FCourseProgress.this.handler.sendMessage(msg);
    }

    //获取数据
    public void getDate(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("coursewareId", hdaplication.getCoursewareId());
                map.put("accountId", hdaplication.getStuid());
                map.put("classId", hdaplication.getClassId());
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?getLearnProgress",
                        map,getActivity(),null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                    //sendMessage(0);
                }else{
                    attributes= JsonUtil.parserString(result, getLearnProgressReturn.class);
                    if (attributes!=null&&attributes.isSuccess()){
                        sendMessage(1);
                    }else {
                        sendMessage(2);
                    }
                }
            }
        }).start();
    }

    public void setList(){
        if (attributes!=null){
            cp_lin.setVisibility(View.VISIBLE);
            if (attributes.getFinishingRate()!=null&&!attributes.getFinishingRate().equals("")){
                percentage =(int)(Double.parseDouble(
                        attributes.getFinishingRate()));
            }
            if (attributes.getLastBehaviorName()!=null&&!attributes.getLastBehaviorName().equals("")){
                cp_protext.setText("上次学习到"+attributes.getLastBehaviorName()+"继续学习>>");
                cp_protext.setVisibility(View.VISIBLE);
                cp_protext.setOnClickListener(this);
            }

            startAnimator(arc_progress,percentage);
            setCourseProgress(attributes.getCourseItemList(),"00");
        }
    }

    public void setCourseProgress(List<LearnBehaviorModel> courseItemList, String type) {//type:00 全部  02已完成   01未完成
        try {
            if (courseItemList!=null&&courseItemList.size()>0) {
                Context con = getActivity();
                if (con != null) {
                    //获取屏幕高度和宽度
                    WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                    int width = wm.getDefaultDisplay().getWidth();
                    int widths = width / 4;
                    TableRow.LayoutParams itemParams = new TableRow.LayoutParams(widths, widths);
                    cp_tb.removeAllViews();
                    TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT); // 每行的水平LinearLayout
                    TableRow rowLayout = null;
                    int index = 0;
                    int index2 = 0;
                    if (type.equals("00")) {
                        for (LearnBehaviorModel item : courseItemList) {//遍历章节点

                            if (item.getFinishStatus() != null) {
                                if (index % 4 == 0) {
                                    rowLayout = new TableRow(getActivity());
                                    rowLayout.setLayoutParams(layoutParams);
                                    rowLayout.setBackgroundColor(Color.WHITE);
                                }
                                String behavior = (String) item.getBehaviorType();
                                View view = View.inflate(getActivity(), R.layout.progress_item, null);
                                view.setLayoutParams(itemParams);
                                TextView iv = (TextView) view.findViewById(R.id.progress_item_iv);
                                iv.setTypeface(iconfont);
                                TextView tv = (TextView) view.findViewById(R.id.progress_item_tv);
                                String str = "0";
                                if (behavior.equals("0")) {//视频
                                    str = item.getFinishPercent() + "%";
                                    iv.setText(R.string.icon_cw_video);
                                    if (item.getFinishStatus().equals("2")) {//通过
                                        iv.setTextColor(getResources().getColor(R.color.text_green));
                                    } else if (item.getFinishStatus().equals("1")) {//进行中
                                        iv.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    } else {//未完成
                                        iv.setTextColor(getResources().getColor(R.color.text_gray));
                                    }
                                } else if (behavior.equals("1")) {//文档
                                    iv.setText(R.string.icon_cw_doc);
                                    if (item.getFinishStatus().equals("2")) {//通过
                                        iv.setTextColor(getResources().getColor(R.color.text_green));
                                        str = "已查看";
                                    } else {
                                        iv.setTextColor(getResources().getColor(R.color.text_gray));
                                        str = "未查看";
                                    }
                                } else if (behavior.equals("3")) {//练习
                                    iv.setText(R.string.icon_cw_practic);
                                    str = item.getFinishPercent() + "%";
                                    if (item.getFinishStatus().equals("2")) {//通过
                                        iv.setTextColor(getResources().getColor(R.color.text_green));
                                    } else if (item.getFinishStatus().equals("1")) {
                                        iv.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    } else {
                                        iv.setTextColor(getResources().getColor(R.color.text_gray));
                                    }
                                } else if (behavior.equals("4")) {//测试
                                    iv.setText(R.string.icon_cw_test);
                                    str = item.getFinishPercent() + "分";
                                    if (item.getFinishStatus().equals("2")) {//通过
                                        iv.setTextColor(getResources().getColor(R.color.text_green));
                                    } else if (item.getFinishStatus().equals("1")) {
                                        iv.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    } else {
                                        iv.setTextColor(getResources().getColor(R.color.text_gray));
                                    }
                                }
                                view.setId(index);
                                tv.setText(str);
                                view.setOnClickListener(this);
                                rowLayout.addView(view);
                                if (index % 4 == 3) {
                                    cp_tb.addView(rowLayout);
                                    rowLayout = null;
                                }
                                index++;
                            }
                        }
                        if (rowLayout != null) {
                            cp_tb.addView(rowLayout);
                        }
                    } else if (type.equals("02")) {//已完成
                        for (LearnBehaviorModel item : courseItemList) {//遍历章节点
                            /*courseIndex++;
                            nodeIndex=0;
                            behaviorIndex++;*/
                            if (item.getFinishStatus() != null && item.getFinishStatus().equals("2")) {
                                if (index2 % 4 == 0) {
                                    rowLayout = new TableRow(getActivity());
                                    rowLayout.setLayoutParams(layoutParams);
                                    rowLayout.setBackgroundColor(Color.WHITE);
                                }
                                String behavior = (String) item.getBehaviorType();
                                View view = View.inflate(getActivity(), R.layout.progress_item, null);
                                view.setLayoutParams(itemParams);
                                TextView iv = (TextView) view.findViewById(R.id.progress_item_iv);
                                TextView tv = (TextView) view.findViewById(R.id.progress_item_tv);
                                iv.setTypeface(iconfont);
                                iv.setTextColor(getResources().getColor(R.color.text_green));
                                String str = "0";
                                if (behavior.equals("0")) {//视频
                                    iv.setText(R.string.icon_cw_video);
                                    str = item.getFinishPercent() + "%";
                                } else if (behavior.equals("1")) {//文档
                                    iv.setText(R.string.icon_cw_doc);
                                    str = "已查看";
                                } else if (behavior.equals("3")) {//练习
                                    iv.setText(R.string.icon_cw_practic);
                                    str = item.getFinishPercent() + "%";
                                } else if (behavior.equals("4")) {//测试
                                    iv.setText(R.string.icon_cw_test);
                                    str = item.getFinishPercent() + "分";
                                }
                                tv.setText(str);
                                view.setId(index);
                                view.setOnClickListener(this);
                                rowLayout.addView(view);
                                if (index2 % 4 == 3) {
                                    cp_tb.addView(rowLayout);
                                    rowLayout = null;
                                }
                                index2++;
                            }
                            index++;
                        }
                        if (rowLayout != null) {
                            cp_tb.addView(rowLayout);
                        }
                    } else if (type.equals("01")) {//未完成
                        for (LearnBehaviorModel item : courseItemList) {//遍历章节点
                            //behaviorIndex++;
                            if (item.getFinishStatus() != null && !item.getFinishStatus().equals("2")) {
                                if (index2 % 4 == 0) {
                                    rowLayout = new TableRow(getActivity());
                                    rowLayout.setBackgroundColor(Color.WHITE);
                                    rowLayout.setLayoutParams(layoutParams);
                                }
                                String behavior = (String) item.getBehaviorType();
                                View view = View.inflate(getActivity(), R.layout.progress_item, null);
                                view.setLayoutParams(itemParams);
                                TextView iv = (TextView) view.findViewById(R.id.progress_item_iv);
                                TextView tv = (TextView) view.findViewById(R.id.progress_item_tv);
                                iv.setTypeface(iconfont);
                                String str = "0";
                                if (behavior.equals("0")) {//视频
                                    str = item.getFinishPercent() + "%";
                                    iv.setText(R.string.icon_cw_video);
                                    if (item.getFinishStatus().equals("1")) {
                                        iv.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    } else {
                                        iv.setTextColor(getResources().getColor(R.color.text_gray));
                                    }
                                } else if (behavior.equals("1")) {//文档
                                    iv.setText(R.string.icon_cw_doc);
                                    str = "未查看";
                                    iv.setTextColor(getResources().getColor(R.color.text_gray));
                                } else if (behavior.equals("3")) {//练习
                                    iv.setText(R.string.icon_cw_practic);
                                    str = item.getFinishPercent() + "%";
                                    if (item.getFinishStatus().equals("1")) {
                                        iv.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    } else {
                                        iv.setTextColor(getResources().getColor(R.color.text_gray));
                                    }
                                } else if (behavior.equals("4")) {//测试
                                    iv.setText(R.string.icon_cw_test);
                                    str = item.getFinishPercent() + "分";
                                    if (item.getFinishStatus().equals("1")) {
                                        iv.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    } else {
                                        iv.setTextColor(getResources().getColor(R.color.text_gray));
                                    }
                                }
                                tv.setText(str);
                                view.setId(index);
                                view.setOnClickListener(this);
                                rowLayout.addView(view);
                                if (index2 % 4 == 3) {
                                    cp_tb.addView(rowLayout);
                                    rowLayout = null;
                                }
                                index2++;
                            }
                            index++;
                        }
                        if (rowLayout != null) {
                            cp_tb.addView(rowLayout);
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            sendMessage(2);
        }
    }

    /**
     * 百分比动画
     * @param arc_progress
     * @param progress
     */
    public void startAnimator(ArcProgress arc_progress, int progress){
        ObjectAnimator anim = ObjectAnimator.ofInt(arc_progress, "progress", 0, progress);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(500);
        anim.start();
    }

    Intent intent ;
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.cp_protext){
            hdaplication.setBehaviorId(attributes.getLastBehaviorId());
            String behavior=attributes.getLastBehaviorType();
            if (behavior.equals("0")) { //视频
                Intent intent = new Intent();
                intent.setClass(getActivity(), VideoActivity.class);
                intent.putExtra("chapterId", attributes.getLastNodeId());
                intent.putExtra("showSchedule", attributes.getLastBehaviorShowSchedule());
                intent.putExtra("showNotice", attributes.getLastBehaviorShowNotice());
                intent.putExtra("showEvaluate",attributes.getLastBehaviorShowEvaluate());
                intent.putExtra("showAsk", attributes.getLastBehaviorShowAsk());
                startActivity(intent);
            } else if (behavior.equals("1")) {//文档
                Intent intent = new Intent();
                intent.setClass(getActivity(), DocumentActivity.class);
                String name = attributes.getLastNodeName() + "-"+attributes.getLastBehaviorName();
                intent.putExtra("file_name",name);
                startActivity(intent);
            } else if (behavior.equals("2")) {//网页
                Toast.makeText(getActivity(),"app不支持查看网页，请使用电脑打开！",
                        Toast.LENGTH_LONG).show();
            } else if (behavior.equals("3")) {//练习
                Intent intent = new Intent();
                intent.setClass(getActivity(), CourseWareChapterTestActivity.class);
                intent.putExtra("title", attributes.getLastBehaviorName());
                startActivity(intent);
            } else if (behavior.equals("4")) {//测试
                Intent intent = new Intent();
                intent.setClass(getActivity(), CourseWareTestActivity.class);
                intent.putExtra("title",attributes.getLastBehaviorName());
                startActivity(intent);
            }
        }else {
            int id=view.getId();
            LearnBehaviorModel item=attributes.getCourseItemList().get(id);
            final String behavior=item.getBehaviorType();//getIdentifierref().substring(0,1);
            hdaplication.setBehaviorId(item.getBehaviorId());
            //nodeId=attributes.getCourseItemList().get(courseIndex-1).getItem().get(nodeIndex-1).getIdentifier();
            nodeId=item.getStructureNodeId();
            intent= new Intent();
            String msg="";
            if (behavior.equals("0")) { //视频
                intent.setClass(getActivity(), VideoActivity.class);
                intent.putExtra("chapterId", item.getStructureNodeId());
                intent.putExtra("showSchedule", item.getShowSchedule());
                intent.putExtra("showNotice", item.getShowNotice());
                intent.putExtra("showEvaluate",item.getShowEvaluate());
                intent.putExtra("showAsk", item.getShowAsk());
                msg="观看了"+item.getFinishPercent()+"%";
            } else if (behavior.equals("1")) {//文档
                intent.setClass(getActivity(), DocumentActivity.class);
                String name = item.getStructureNodeName() + "-"+item.getBehaviorName();
                intent.putExtra("file_name",name);
                if (item.getFinishStatus().equals("2")){
                    msg="已阅读";
                }else {
                    msg="未阅读";
                }
            } else if (behavior.equals("2")) {//网页
            } else if (behavior.equals("3")) {//练习
                intent.setClass(getActivity(), CourseWareChapterTestActivity.class);
                intent.putExtra("title", item.getBehaviorName());
                msg="已做了"+item.getFinishPercent()+"%";
            } else if (behavior.equals("4")) {//测试
                intent.setClass(getActivity(), CourseWareTestActivity.class);
                intent.putExtra("title", item.getBehaviorName());
                msg="有效分为"+item.getFinishPercent();
            }
            materialDialog=new MaterialDialog(getActivity());
            LinearLayout layout=(LinearLayout) View.inflate(getActivity(),R.layout.progress_dialog,null);
            TextView dialog_behavior_tv=(TextView)layout.findViewById(R.id.dialog_behavior_tv);
            dialog_behavior_tv.setText(item.getBehaviorName());
            TextView dialog_progress_tv=(TextView)layout.findViewById(R.id.dialog_progress_tv);
            dialog_progress_tv.setText(msg);
            TextView dialog_node_tv=(TextView)layout.findViewById(R.id.dialog_node_tv);
            dialog_node_tv.setText(item.getStructureNodeName());
            TextView dialog_parent_tv=(TextView)layout.findViewById(R.id.dialog_parent_tv);
            dialog_parent_tv.setText(item.getParentStructureNodeName());
            materialDialog.setContentView(layout);
            materialDialog.setTitle("提示");
            materialDialog.setNegativeButton("取消",new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    materialDialog.dismiss();
                }
            });
            materialDialog.setPositiveButton("开始学习",new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if(behavior.equals("2")){
                        Toast.makeText(getActivity(),"app不支持查看网页，请使用电脑打开！",
                                Toast.LENGTH_LONG).show();
                    }else {
                        startActivity(intent);
                    }
                    materialDialog.dismiss();
                }
            });
            materialDialog.show();
        }
    }


    public void setClickRecord(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("accountId", hdaplication.getStuid());
                map.put("classId", hdaplication.getClassId());
                map.put("behaviorId", hdaplication.getBehaviorId());
                map.put("nodeId", nodeId);
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?setClickRecord",
                        map,getActivity(),null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                }else{
                }
            }
        }).start();
    }

}
