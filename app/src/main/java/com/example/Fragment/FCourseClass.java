package com.example.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adapter.DefaultAdapter;
import com.example.adapter.MyClassRecyclerAdapter;
import com.example.entity.SPAQLearnClassEntity;
import com.example.service.ApiService;
import com.example.service.ApiServiceSPAQ;
import com.example.util.NetWork;
import com.example.entity.LearnClassEntity;
import com.example.entity.LearnClassStudentEntity;
import com.example.jdbc.DBManagerToLearnClass;
import com.example.jdbc.DBManagerToLearnClassStudent;
import com.example.jsonReturn.CourseClassReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.CourseClassIndexActivity;
import com.example.util.ApplicationUtil;
import com.example.util.FinalStr;
import com.example.util.HttpUtil;
import com.example.util.JsonUtil;
import com.example.util.SpUtils;
import com.example.util.SysUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wang.avi.AVLoadingIndicatorView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by ysg on 2017/8/2.
 */

public class FCourseClass extends Fragment {
    protected String JSON_TAG = "JSON";
    private ApplicationUtil hdaplication;//全局参数对象
    private Handler handler;//异步刷新组件
    List<LearnClassEntity> learnClassEntityList=new ArrayList<LearnClassEntity>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DecimalFormat df   = new DecimalFormat("######0.00");
    boolean netStatus=false;
    private DBManagerToLearnClassStudent dbManagers; //sqlite操作对象
    private DBManagerToLearnClass dbManagers1; //sqlite操作对象
    private AVLoadingIndicatorView loading;//加载
    private SmartRefreshLayout refreshLayout;//刷新
    //控件
    RecyclerView recyclerView;
    MyClassRecyclerAdapter myClassRecyclerAdapter;

    //网络请求框架
    protected Retrofit retrofit =null,  retrofit2 =null;
    private ApiServiceSPAQ serverApi;
    private ApiService serverApi2;

    private static OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS).build();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_layout,container,false);


        //加载retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(FinalStr.SPAQPATH +"/")
                .build();
        retrofit2 = new Retrofit.Builder()
                .client(client)
                .baseUrl(FinalStr.ACCESSPRYPATH +"/")
                .build();
        //生成接口文件
        serverApi = retrofit.create(ApiServiceSPAQ.class);
        serverApi2 = retrofit2.create(ApiService.class);

        hdaplication = (ApplicationUtil)getActivity().getApplication();
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        loading =(AVLoadingIndicatorView)view.findViewById(R.id.loading);
        refreshLayout =(SmartRefreshLayout)view.findViewById(R.id.refreshLayout);

        ClassicsHeader classicsHeader =new ClassicsHeader(getActivity());
        classicsHeader.setSpinnerStyle(SpinnerStyle.Translate);
        //设置是否显示时间
        classicsHeader.setEnableLastTime(false);
        //设置无任务风格
        refreshLayout.setRefreshHeader(classicsHeader);

        refreshLayout.setRefreshFooter(new FalsifyFooter(getActivity()));
        refreshLayout.autoRefresh();
        refreshLayout.setReboundDuration(500);//回弹动画时长（毫秒）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                try {
                    isCourseClass();
                } catch (ParseException e) {
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

        updateUi();
        //判断该用户课程班列表本地是否存在
      /*  try {
            isCourseClass();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        return view;
    }

    /**
     * 判断课程班是否存在数据库
     */
    private void isCourseClass() throws ParseException {

        dbManagers = new DBManagerToLearnClassStudent(getActivity());
        dbManagers1 = new DBManagerToLearnClass(getActivity());
        //判断是否连接网络
        netStatus= NetWork.isNetworkConnected(getActivity());

        if (netStatus) {
            getData();
        }else {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("student_id",hdaplication.getStuid()+"");
            List<LearnClassStudentEntity> list=dbManagers.queryByMap(map);
            if (list!=null&&list.size()>0) {//数据库有数据
                for (LearnClassStudentEntity entity:list){
                    String id=entity.getClassId();
                    LearnClassEntity learnClassEntity=dbManagers1.queryUserById(id);
                    learnClassEntityList.add(learnClassEntity);
                }
                sendMessage(1);
            }else {
                sendMessage(0);
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void updateUi(){

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: //无法加载数据
                        Toast.makeText(getActivity(),"无法加载数据,请检查你的网络连接或者联系我们！",
                                Toast.LENGTH_LONG).show();
                        loading.hide();
                        break;

                    case 1: //加载数据

                        setList();
                        /**
                         * 如果是食品安全
                         */
                        if (FinalStr.SYSTEM_CODE.equals("GZSPAQ")) {
                            getSPAQProgress();
                        }
                        refreshLayout.finishRefresh();
                        loading.hide();

                        break;
                    case 2:
                        Toast.makeText(getActivity(), "没有相关的未学课程信息", Toast.LENGTH_LONG).show();
                        loading.hide();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }
    private void sendMessage(int str){
        Message msg = new Message();
        msg.what = str;
        FCourseClass.this.handler.sendMessage(msg);
    }

    public void setList(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        myClassRecyclerAdapter = new MyClassRecyclerAdapter(getActivity(), learnClassEntityList);
        recyclerView.setAdapter(myClassRecyclerAdapter);

        myClassRecyclerAdapter.setOnItemClickListener(new DefaultAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),CourseClassIndexActivity.class);
                intent.putExtra("coursewareId",
                        learnClassEntityList.get(position).getCoursewareId()+"#"+learnClassEntityList.get(position).getId()+"#"+learnClassEntityList.get(position).getClassName());
                hdaplication.setClassicname(learnClassEntityList.get(position).getClassName());
                String date ="" ;
                if (learnClassEntityList.get(position).getPublishTime() !=null){
                    date=sdf.format(learnClassEntityList.get(position).getPublishTime());
                }

                intent.putExtra("publishTime",date);
                //进入一个课程时清空问题缓存
                clearQuestionCashe();
                startActivity(intent);


            }
        });

    }
    public void getData(){
        loading.show();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Map<String,Object> map = new HashMap<String,Object>(0);
//                map.put("classType", "0");
//                map.put("page", "1");
//                map.put("accountId", hdaplication.getStuid());
//                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?courseClassList",
//                        map,getActivity(),null);
//                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
//                    sendMessage(0);
//                }else{
//                    CourseClassReturn courseClassReturn= JsonUtil.parserString(result, CourseClassReturn.class);
//                    learnClassEntityList=courseClassReturn.getLearnClassEntityList();
//                    List<LearnClassStudentEntity> LearnClassStudentList=courseClassReturn.getLearnClassStudentList();
//                    if (LearnClassStudentList!=null){
//                        for (LearnClassStudentEntity e:LearnClassStudentList){
////                            dbManagers.saveEntity(e);
//                        }
//                    }
//                    if (learnClassEntityList!=null){
//                        for (LearnClassEntity e:learnClassEntityList){
////                            dbManagers1.saveEntity(e);
//                        }
//                    }
//                    sendMessage(1);
//                }
//            }
//        }).start();

        Call<ResponseBody> call = serverApi2.courseClassList("0","1", hdaplication.getStuid());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求成功
                    try {
                        String result =response.body().string();

                        Log.d(JSON_TAG,"获取课程列表:"+result);

                        CourseClassReturn courseClassReturn= JsonUtil.parserString(result, CourseClassReturn.class);
                        learnClassEntityList=courseClassReturn.getLearnClassEntityList();
                        List<LearnClassStudentEntity> LearnClassStudentList=courseClassReturn.getLearnClassStudentList();
                        if (LearnClassStudentList!=null){
                            for (LearnClassStudentEntity e:LearnClassStudentList){
//                            dbManagers.saveEntity(e);
                            }
                        }
                        if (learnClassEntityList!=null){
                            for (LearnClassEntity e:learnClassEntityList){
//                            dbManagers1.saveEntity(e);
                            }
                        }
                        sendMessage(1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{

                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("XX","CUOWU");
            }
        });
    }

    /**
     * 获取食品安全课程进度
     */
    private void getSPAQProgress(){

        Call<ResponseBody> call = serverApi.getSPAQClassProgress(hdaplication.getStuCode());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求成功
                    try {
                        String result =response.body().string();

                        Log.d(JSON_TAG,"获取食品安全课程进度:"+result);

                        JSONObject jsonObject = new JSONObject(result);
                        Iterator<String> keys = jsonObject.keys();
                        List<SPAQLearnClassEntity> list =new ArrayList<SPAQLearnClassEntity>();

                        while(keys.hasNext()) {
                            String key =keys.next();
                            Log.d(JSON_TAG,key);
                            Log.d(JSON_TAG,jsonObject.getJSONObject(key).toString());
                            String obj = jsonObject.getJSONObject(key).toString();
                            SPAQLearnClassEntity learnClassEntity = JsonUtil.parserString(obj,SPAQLearnClassEntity.class);
                            learnClassEntity.setClassCode(key);
                            list.add(learnClassEntity);

                        }
                        //设置时长
                        myClassRecyclerAdapter.setLearnClassEntities(list);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{

                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * 清除缓存内容
     */
    private void clearQuestionCashe() {
        SpUtils.putString(getActivity(),SpUtils.QUESTION_TITLE,"");
        SpUtils.putString(getActivity(),SpUtils.QUESTION_CONTENT,"");
        SpUtils.putString(getActivity(),SpUtils.QUESTION_CHAPTER_NAME,"");
        SpUtils.putString(getActivity(),SpUtils.QUESTION_CHAPTER_ID,"");
    }

}
