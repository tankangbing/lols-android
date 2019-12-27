package com.example.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.TaskListViewAdapter;
import com.example.entity.TaskModel;
import com.example.jsonReturn.TaskListReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.task.TaskShowActivity;
import com.example.util.ApplicationUtil;
import com.example.util.FinalStr;
import com.example.util.HttpUtil;
import com.example.util.JsonUtil;
import com.example.util.SysUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ysg on 2017/7/17.
 */

public class FTask extends Fragment {
    private ApplicationUtil hdaplication;//全局参数对象
    private Handler handler;//异步刷新组件
    ListView task_list;
    List<TaskModel> taskList;
    LinearLayout task_zy;

    private SmartRefreshLayout refreshLayout;//刷新
    Date curDate;//获取当前时间
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    TaskListViewAdapter taskViewAdapter;
    /* 定义适配器 */
    private MyAdapter adapter;
   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);
        setCache();
        setUi();
        updateUi();
        getData();
    }*/
    /* 这个方法适合初始化数据 */
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       hdaplication = (ApplicationUtil)getActivity().getApplication();
       adapter=new MyAdapter();
       curDate= new Date(System.currentTimeMillis());
       updateUi();
       //getData();
   }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task,container,false);
        refreshLayout=(SmartRefreshLayout)view.findViewById(R.id.refreshLayout);


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
                    getData();
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

        task_list=(ListView)view.findViewById(R.id.task_list);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public void setUi(){
        //task_list=(ListView)getfindViewById(R.id.task_list);
        //task_zy=(LinearLayout)findViewById(R.id.task_zy);
    }

    private void sendMessage(int str){
        Message msg = new Message();
        msg.what = str;
        FTask.this.handler.sendMessage(msg);
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
                        setList();
                        refreshLayout.finishRefresh();
                        break;
                    case 2://没有获取时，清除页面
                        refreshLayout.finishRefresh();
                        //Toast.makeText(getActivity(), "没有相关的作业", Toast.LENGTH_LONG).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    public void setList(){
        /*if (taskList!=null&&taskList.size()>0){
            for(int i=0;i<taskList.size();i++){
                View v = LayoutInflater.from(this).inflate(R.layout.task_item, null);
                v.setTag(taskList.get(i).getId());
                TextView task_item_title=(TextView)v.findViewById(R.id.task_item_title);
                TextView task_item_date=(TextView)v.findViewById(R.id.task_item_date);
                TextView task_item_tv3=(TextView)v.findViewById(R.id.task_item_tv3);
                TextView task_item_msclick =(TextView)v.findViewById(R.id.task_item_msclick);
                task_item_ms=(TextView)v.findViewById(R.id.task_item_ms);
                task_item_msclick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        task_item_ms.setVisibility(View.VISIBLE);
                    }
                });
                task_item_title.setText(taskList.get(i).getTaskName());
                task_item_date.setText("作业日期："+taskList.get(i).getStartDate()+" 至 "+taskList.get(i).getSubmitEndDate());
                task_item_tv3.setText(taskList.get(i).getTaskStatus());
                task_zy.addView(v);
            }
        }*/
        /*taskViewAdapter = new TaskListViewAdapter(getActivity(), R.id.task_list,
                taskList);
        task_list.setAdapter(taskViewAdapter);*/

        if (taskList!=null&&taskList.size()>0){
            adapter.bindData(taskList);
            task_list.setAdapter(adapter);
        }else {
            sendMessage(2);
        }

        /*task_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent=new Intent();
                intent.setClass(TaskActivity.this,TaskShowActivity.class);
                startActivity(intent);
            }
        });*/
    }

    public void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("coursewareId", hdaplication.getCoursewareId());
                map.put("classId", hdaplication.getClassId());
                map.put("accountId", hdaplication.getStuid());
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?taskList",
                        map,getActivity(),null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                    sendMessage(0);
                }else{
                    TaskListReturn taskListReturn= JsonUtil.parserString(result, TaskListReturn.class);
                    if (taskListReturn!=null){
                        if (taskListReturn.getTaskList()!=null&&taskListReturn.getTaskList().size()>0){
                            taskList=taskListReturn.getTaskList();
                            sendMessage(1);
                        }else {
                            sendMessage(2);
                        }
                    }
                }
            }
        }).start();
    }

   /* public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent();
        intent.setClass(getActivity(),TaskShowActivity.class);
        intent.putExtra("taskId",view.getTag()+"");
        startActivity(intent);
    }*/

    public void click(View v){
        if (v.getId()==R.id.task_item_click){
            Intent intent=new Intent();
            intent.setClass(getActivity(),TaskShowActivity.class);
            intent.putExtra("taskId",v.getTag()+"");
            startActivity(intent);
        }else if (v.getId()==R.id.task_item_msclick){

        }
    }

    //适配器，显示题目选项内容
    class MyAdapter extends BaseAdapter {

        /* 数据集合 */
        private List<TaskModel>  list;

        @Override
        public int getCount() {
            //return lists.size();
            return list.size();
        }

        /**
         * 绑定数据
         *
         * @param list
         */
        public void bindData(List<TaskModel> list) {
            this.list = list;
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
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v;
            if (view==null){
                v=View.inflate(getActivity(),R.layout.task_item,null);

            }else {
                v=view;
            }
            v.setTag(list.get(i).getId());
            TextView task_item_title=(TextView)v.findViewById(R.id.task_item_title);
            TextView task_item_date=(TextView)v.findViewById(R.id.task_item_date);
            TextView task_item_tv3=(TextView)v.findViewById(R.id.task_item_tv3);
            TextView task_item_ms=(TextView)v.findViewById(R.id.task_item_ms);
            TextView task_item_fbz=(TextView)v.findViewById(R.id.task_item_fbz);
            TextView task_item_df=(TextView)v.findViewById(R.id.task_item_df);
            task_item_ms.setText(list.get(i).getTaskDescribe());
            LinearLayout task_item_click=(LinearLayout)v.findViewById(R.id.task_item_click);
            if (list.get(i).getPublisherName()!=null){
                task_item_fbz.setText(list.get(i).getPublisherName());
            }
            if (list.get(i).getStudentScore()!=null&&!list.get(i).getStudentScore().equals("null")){
                task_item_df.setText(list.get(i).getStudentScore()+"");
            }
            task_item_click.setTag(i);
            task_item_click.setOnClickListener(new View.OnClickListener() {     //目录点击事件

                @Override
                public void onClick(View v) {
                    int z= (int) v.getTag();
                    if (list.get(z).getSubmitEndDate().getTime()>curDate.getTime()){
                        Intent intent=new Intent();
                        intent.setClass(getActivity(),TaskShowActivity.class);
                        intent.putExtra("taskId",list.get(z).getId());
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(),"该时间已经过了作答时间，不允许作答",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            LinearLayout task_item_msclick=(LinearLayout)v.findViewById(R.id.task_item_msclick);
            task_item_msclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView task_item_ms=(TextView)view.findViewById(R.id.task_item_ms);
                    if (task_item_ms.getVisibility()==View.GONE){
                        task_item_ms.setVisibility(View.VISIBLE);
                    }else {
                        task_item_ms.setVisibility(View.GONE);
                    }
                }
            });
            String status="";
            if (list.get(i).getTaskStatus().equals("0")){
                status="已保存";
            }else if (list.get(i).getTaskStatus().equals("1")){
                status="已发布";
            }
            task_item_title.setText(list.get(i).getTaskName());
            task_item_date.setText("作业日期："+sdf.format(list.get(i).getStartDate())+" 至 "+sdf.format(list.get(i).getSubmitEndDate()));
            task_item_tv3.setText(status);
            return v;
        }

    }

    public static FTask newInstance() {
        FTask fragment = new FTask();

        return fragment;
    }

}
