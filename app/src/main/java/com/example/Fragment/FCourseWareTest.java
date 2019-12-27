package com.example.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.TreeListViewAdapter;
import com.example.entity.LearningBehavior;
import com.example.entity.PDFOutlineElement;
import com.example.jsonReturn.getClickRecordReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.CourseClassIndexActivity;
import com.example.onlinelearnActivity.SearchActivity;
import com.example.onlinelearnActivity.courseWare.document.DocumentActivity;
import com.example.onlinelearnActivity.courseWare.practic.CourseWareChapterTestActivity;
import com.example.onlinelearnActivity.courseWare.test.CourseWareTestActivity;
import com.example.onlinelearnActivity.courseWare.video.VideoActivity;
import com.example.spt.CourseUtil;
import com.example.spt.jaxb.course.Item;
import com.example.spt.jaxb.course.Manifest;
import com.example.util.ApplicationUtil;
import com.example.util.FinalStr;
import com.example.util.HttpUtil;
import com.example.util.JsonUtil;
import com.example.util.SysUtil;
import com.example.view.dialog.AlertDialog;


import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ysg on 2017/9/6.
 */

public class FCourseWareTest extends Fragment {
    private ArrayList<PDFOutlineElement> mPdfOutlinesCount = new ArrayList<PDFOutlineElement>();
    private ArrayList<PDFOutlineElement> mPdfOutlines = new ArrayList<PDFOutlineElement>();
    private TreeListViewAdapter treeViewAdapter = null;
    private ApplicationUtil hdaplication;//全局参数对象
    private Handler handler;//异步刷新组件
    private Manifest manifest;
    //List<Item> tItems=new ArrayList<Item>();
    //String courseWareTitle="";
    String filePath="";
    private Map<String, List<LearningBehavior>> LearningBehaviorMap;
    XmlPullParser parser;
    private String nodeId="";
    private String behaviorId="";
    private String parentId="";
    private int index=0;
    FragmentActivity fragmentActivity;
    ListView fcw_lv;
    LinearLayout fcw_xxjl_ll;//学习记录提示框
    TextView fcw_xxjl_text;//学习记录文本框
    ImageView fcw_xxjl_close;
    final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(10000,1000);
    int jzcount=0;
    private ArrayList<PDFOutlineElement> mPdfOutlinesCountTwo = new ArrayList<PDFOutlineElement>();;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_ware,container,false);

        jzcount++;
        hdaplication = (ApplicationUtil)getActivity().getApplication();
        fragmentActivity=getActivity();
        manifest=((CourseClassIndexActivity)getActivity()).getManifest();
        fcw_lv=(ListView)view.findViewById(R.id.fcw_lv);
        fcw_xxjl_ll=(LinearLayout)view.findViewById(R.id.fcw_xxjl_ll);
        fcw_xxjl_text=(TextView)view.findViewById(R.id.fcw_xxjl_text);
        fcw_xxjl_close=(ImageView)view.findViewById(R.id.fcw_xxjl_close);//new倒计时对象,总共的时间,每隔多少秒更新一次时间
        setListener();
        updateUi();
        getClickRecord();
        return view;

    }

    public static FCourseWareTest newInstance() {
        FCourseWareTest fragment = new FCourseWareTest();

        return fragment;
    }

    private void getinitialData() {
        mPdfOutlinesCount = new ArrayList<PDFOutlineElement>();
        mPdfOutlines = new ArrayList<PDFOutlineElement>();
        List<Item> tItems = CourseUtil.getCourseOrganization(manifest);
        int j=1;
        if (tItems!=null&&tItems.size()>0) {
            for (Item item : tItems) {
                j++;
                if (parentId==null||(item.getIdentifier().equals(parentId)&&jzcount==1)){
                    PDFOutlineElement pdfOutlineElement=new PDFOutlineElement(j+"",item.getTitle(),false,true,"0",0, true,"","","",null,"","");
                    mPdfOutlinesCount.add(pdfOutlineElement);
                    mPdfOutlines.add(pdfOutlineElement);
                    int pId=j;
                    List<Item> tItemChild=item.getItem();
                    if (tItemChild!=null&&tItemChild.size()>0) {
                        for (int i = 0; i < tItemChild.size(); i++) {

                            List<Item> tItemChildBehavior=tItemChild.get(i).getItem();
                            String isvisible = tItemChild.get(i).getIsvisible();
                            if(tItemChildBehavior!=null && "true".equals(isvisible)){
                                j++;
                                PDFOutlineElement pdfOutlineElements=new PDFOutlineElement(j+"",tItemChild.get(i).getTitle(),true,true,pId+"",1, true,tItemChild.get(i).getIdentifier(),"","",pdfOutlineElement,tItemChild.get(i).getExpirationDate(),tItemChild.get(i).getIsNew());
                                mPdfOutlinesCount.add(pdfOutlineElements);
                                mPdfOutlines.add(pdfOutlineElements);
                                int pIds=j;
                                for (Item itemBehavior : tItemChildBehavior) {
                                    String behavior=(String) itemBehavior.getIdentifierref().subSequence(0, 1);
                                    j++;
                                    PDFOutlineElement pdfOutlineElementBehavior=new PDFOutlineElement(j+"",itemBehavior.getTitle(),true,false,pIds+"",2, false,itemBehavior.getIdentifier(),behavior,tItemChild.get(i).getIdentifier(),pdfOutlineElements,"","");
                                    pdfOutlineElementBehavior.setShowAsk(itemBehavior.getShowAsk());
                                    pdfOutlineElementBehavior.setShowEvaluate(itemBehavior.getShowEvaluate());
                                    pdfOutlineElementBehavior.setShowNotice(itemBehavior.getShowNotice());
                                    pdfOutlineElementBehavior.setShowSchedule(itemBehavior.getShowSchedule());
                                    mPdfOutlinesCount.add(pdfOutlineElementBehavior);
                                    mPdfOutlines.add(pdfOutlineElementBehavior);
                                    if (itemBehavior.getIdentifier().equals(behaviorId)||behaviorId==null){
                                        index=mPdfOutlinesCount.size()-1;
                                        fcw_xxjl_text.setText("上次学习到："+itemBehavior.getTitle());
                                        fcw_xxjl_ll.setVisibility(View.VISIBLE);
                                        myCountDownTimer.start();
                                        fcw_xxjl_text.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                fcw_xxjl_ll.setVisibility(View.GONE);
                                                learningTwo(index);//修复点收起章节，点击上次学习闪退
                                            }
                                        });
                                    }
                                }
                            }

                        }
                    }
                }else {
                    PDFOutlineElement pdfOutlineElement=new PDFOutlineElement(j+"",item.getTitle(),false,true,"0",0, true,"","","",null,"","");
                    mPdfOutlinesCount.add(pdfOutlineElement);
                    mPdfOutlines.add(pdfOutlineElement);
                    int pId=j;
                    List<Item> tItemChild=item.getItem();
                    if (tItemChild!=null&&tItemChild.size()>0) {
                        for (int i = 0; i < tItemChild.size(); i++) {

                            List<Item> tItemChildBehavior=tItemChild.get(i).getItem();
                            String isvisible = tItemChild.get(i).getIsvisible();
                            if(tItemChildBehavior!=null && "true".equals(isvisible)){
                                j++;
                                PDFOutlineElement pdfOutlineElements=new PDFOutlineElement(j+"",tItemChild.get(i).getTitle(),true,true,pId+"",1, true,tItemChild.get(i).getIdentifier(),"","",pdfOutlineElement,tItemChild.get(i).getExpirationDate(),tItemChild.get(i).getIsNew());
                                mPdfOutlinesCount.add(pdfOutlineElements);
                                mPdfOutlines.add(pdfOutlineElements);
                                int pIds=j;
                                for (Item itemBehavior : tItemChildBehavior) {
                                    String behavior=(String) itemBehavior.getIdentifierref().subSequence(0, 1);
                                    j++;
                                    PDFOutlineElement pdfOutlineElementBehavior=new PDFOutlineElement(j+"",itemBehavior.getTitle(),true,false,pIds+"",2, false,itemBehavior.getIdentifier(),behavior,tItemChild.get(i).getIdentifier(),pdfOutlineElements,"","");
                                    pdfOutlineElementBehavior.setShowAsk(itemBehavior.getShowAsk());
                                    pdfOutlineElementBehavior.setShowEvaluate(itemBehavior.getShowEvaluate());
                                    pdfOutlineElementBehavior.setShowNotice(itemBehavior.getShowNotice());
                                    pdfOutlineElementBehavior.setShowSchedule(itemBehavior.getShowSchedule());
                                    mPdfOutlinesCount.add(pdfOutlineElementBehavior);
                                    mPdfOutlines.add(pdfOutlineElementBehavior);
                                }
                            }

                        }
                    }
                }
            }
        }else {
            Toast.makeText(getActivity(), "没有相关的未学课程信息", Toast.LENGTH_LONG).show();
        }
        mPdfOutlinesCountTwo.clear();
        mPdfOutlinesCountTwo.addAll(mPdfOutlinesCount);
        treeViewAdapter = new TreeListViewAdapter(getActivity(), R.layout.course_ware,
                mPdfOutlinesCount);
        fcw_lv.setAdapter(treeViewAdapter);
    }

    public void updateUi(){
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: //无法加载数据
                        Toast.makeText(getActivity(),"无法加载数据,请检查你的网络连接或者联系我们！",
                                Toast.LENGTH_LONG).show();
                        break;
                    case 1: //加载数据

                        getinitialData();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "没有相关的未学课程信息", Toast.LENGTH_LONG).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }
    private void sendMessage(int str){
        Message msg = new Message();
        msg.what = str;
        FCourseWareTest.this.handler.sendMessage(msg);
    }

    public void getClickRecord(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("accountId", hdaplication.getStuid());
                map.put("classId", hdaplication.getClassId());
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?getClickRecord",
                        map,fragmentActivity,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                    //sendMessage(0);
                }else{
                    getClickRecordReturn getClickRecordReturn= JsonUtil.parserString(result, getClickRecordReturn.class);
                    if (getClickRecordReturn!=null){
                        nodeId=getClickRecordReturn.getNodeId();
                        behaviorId=getClickRecordReturn.getBehaviorId();
                        parentId=getClickRecordReturn.getParentId();
                    }
                }
                sendMessage(1);
            }
        }).start();
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
                        map,fragmentActivity,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                }else{
                }
            }
        }).start();
    }

    private long exitTime = 0;

    /**
     * 设置监听
     */
    private void setListener(){
        fcw_xxjl_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fcw_xxjl_ll.setVisibility(View.GONE);
            }
        });
        fcw_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*if (mPdfOutlinesCount.get(position).getLevel()==1) { //第二层目录  不可点击缩放
                    return;
                }*/
                if (!mPdfOutlinesCount.get(position).isMhasChild()) {//第三层目录 没有子目录
                    learning(position);
                    return;
                }
                if (mPdfOutlinesCount.get(position).isExpanded()) {//当前点击目录为展开状态，点击即修改为收缩状态
                    mPdfOutlinesCount.get(position).setExpanded(false);
                    PDFOutlineElement pdfOutlineElement=mPdfOutlinesCount.get(position);
                    ArrayList<PDFOutlineElement> temp=new ArrayList<PDFOutlineElement>();
//                    mPdfOutlinesCountTwo.clear();
//                    mPdfOutlinesCountTwo.addAll(mPdfOutlinesCount);
                    for (int i = position+1; i < FCourseWareTest.this.mPdfOutlinesCount.size(); i++) {
                        if (pdfOutlineElement.getLevel()>= FCourseWareTest.this.mPdfOutlinesCount.get(i).getLevel()) {
                            break;
                        }
                        temp.add(FCourseWareTest.this.mPdfOutlinesCount.get(i));
                    }

                    FCourseWareTest.this.mPdfOutlinesCount.removeAll(temp);

                    treeViewAdapter.notifyDataSetChanged();

                } else {//当前点击目录为收缩状态，点击即修改为展开状态
                    mPdfOutlinesCount.get(position).setExpanded(true);
                    int level = mPdfOutlinesCount.get(position).getLevel();
                    int nextLevel = level + 1;

                    int j=0;
                    for (PDFOutlineElement pdfOutlineElement : mPdfOutlines) {
                        if (pdfOutlineElement.getParent().equals(mPdfOutlinesCount.get(position).getId())) {
                            pdfOutlineElement.setLevel(nextLevel);
                            j++;
                            mPdfOutlinesCount.add(position+j, pdfOutlineElement);
                            if (pdfOutlineElement.isExpanded()){
                                for (PDFOutlineElement pdfOutlineElement1 : mPdfOutlines) {
                                    if (pdfOutlineElement1.getParent().equals(pdfOutlineElement.getId())) {
                                        pdfOutlineElement1.setLevel(nextLevel+1);
                                        pdfOutlineElement1.setExpanded(false);
                                        j++;
                                        mPdfOutlinesCount.add(position+j, pdfOutlineElement1);
                                    }
                                }
                            }
                        }
                    }
//                    mPdfOutlinesCountTwo.clear();
//                    mPdfOutlinesCountTwo.addAll(mPdfOutlinesCount);
                    treeViewAdapter.notifyDataSetChanged();
                }

                view.setBackgroundColor(0xFFffffff);
            }
        });
    }

    public void learning(int position){
        String behavior = mPdfOutlinesCount.get(position).getBehavior();
        if (behavior != null && !behavior.equals("")) {
            hdaplication.setBehaviorId(mPdfOutlinesCount.get(position).getIdentifier());
            nodeId=mPdfOutlinesCount.get(position).getParentElement().getIdentifier();
            //提交当前学习行为与父节点id
            setClickRecord();
            //判断是什么学习行为
            if (behavior.equals("0")) { //视频
                Intent intent = new Intent();
                intent.setClass(getActivity(), VideoActivity.class);
                intent.putExtra("fileName", mPdfOutlinesCount.get(position).getOutlineTitle());
                intent.putExtra("chapterId", mPdfOutlinesCount.get(position).getCharterId());
                intent.putExtra("showSchedule", mPdfOutlinesCount.get(position).getShowSchedule());
                intent.putExtra("showNotice", mPdfOutlinesCount.get(position).getShowNotice());
                intent.putExtra("showEvaluate", mPdfOutlinesCount.get(position).getShowEvaluate());
                intent.putExtra("showAsk", mPdfOutlinesCount.get(position).getShowAsk());
                startActivity(intent);
            } else if (behavior.equals("1")) {//文档
                Intent intent = new Intent();
                intent.setClass(getActivity(), DocumentActivity.class);
                String chapterName = mPdfOutlinesCount.get(position).getParentElement().getOutlineTitle();
                String fileName =mPdfOutlinesCount.get(position).getOutlineTitle();
                intent.putExtra("fileName",fileName);
                intent.putExtra("chapterName",chapterName);
                startActivity(intent);
            } else if (behavior.equals("2")) {//网页
                Toast.makeText(getActivity(),"app不支持查看网页，请使用电脑打开！",
                        Toast.LENGTH_LONG).show();
            } else if (behavior.equals("3")) {//练习
                Intent intent = new Intent();
                intent.setClass(getActivity(), CourseWareChapterTestActivity.class);
                intent.putExtra("title", mPdfOutlinesCount.get(position).getOutlineTitle());
                startActivity(intent);
            } else if (behavior.equals("4")) {//测试
                Intent intent = new Intent();
                intent.setClass(getActivity(), CourseWareTestActivity.class);
                intent.putExtra("title", mPdfOutlinesCount.get(position).getOutlineTitle());
                startActivity(intent);
            } else if (behavior.equals("5")) {//考试

            }
        }
    }
    public void learningTwo(int position){
        String behavior = mPdfOutlinesCountTwo.get(position).getBehavior();
        if (behavior != null && !behavior.equals("")) {
            hdaplication.setBehaviorId(mPdfOutlinesCountTwo.get(position).getIdentifier());
            nodeId=mPdfOutlinesCountTwo.get(position).getParentElement().getIdentifier();
            //提交当前学习行为与父节点id
            setClickRecord();
            //判断是什么学习行为
            if (behavior.equals("0")) { //视频
                Intent intent = new Intent();
                intent.setClass(getActivity(), VideoActivity.class);
                intent.putExtra("fileName", mPdfOutlinesCountTwo.get(position).getOutlineTitle());
                intent.putExtra("chapterId", mPdfOutlinesCountTwo.get(position).getCharterId());
                intent.putExtra("showSchedule", mPdfOutlinesCountTwo.get(position).getShowSchedule());
                intent.putExtra("showNotice", mPdfOutlinesCountTwo.get(position).getShowNotice());
                intent.putExtra("showEvaluate", mPdfOutlinesCountTwo.get(position).getShowEvaluate());
                intent.putExtra("showAsk", mPdfOutlinesCountTwo.get(position).getShowAsk());
                startActivity(intent);
            } else if (behavior.equals("1")) {//文档
                Intent intent = new Intent();
                intent.setClass(getActivity(), DocumentActivity.class);
                String chapterName = mPdfOutlinesCountTwo.get(position).getParentElement().getOutlineTitle();
                String fileName =mPdfOutlinesCountTwo.get(position).getOutlineTitle();
                intent.putExtra("fileName",fileName);
                intent.putExtra("chapterName",chapterName);
                startActivity(intent);
            } else if (behavior.equals("2")) {//网页
                Toast.makeText(getActivity(),"app不支持查看网页，请使用电脑打开！",
                        Toast.LENGTH_LONG).show();
            } else if (behavior.equals("3")) {//练习
                Intent intent = new Intent();
                intent.setClass(getActivity(), CourseWareChapterTestActivity.class);
                intent.putExtra("title", mPdfOutlinesCountTwo.get(position).getOutlineTitle());
                startActivity(intent);
            } else if (behavior.equals("4")) {//测试
                Intent intent = new Intent();
                intent.setClass(getActivity(), CourseWareTestActivity.class);
                intent.putExtra("title", mPdfOutlinesCountTwo.get(position).getOutlineTitle());
                startActivity(intent);
            } else if (behavior.equals("5")) {//考试

            }
        }
    }

    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            fcw_xxjl_ll.setVisibility(View.GONE);
        }
    }
}
