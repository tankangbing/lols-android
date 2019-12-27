package com.example.onlinelearnActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.example.adapter.DefaultAdapter;
import com.example.adapter.MyClassRecyclerAdapter;
import com.example.entity.SPAQLearnClassEntity;
import com.example.util.FinalStr;
import com.example.util.NetWork;
import com.example.entity.LearnClassEntity;
import com.example.entity.LearnClassStudentEntity;
import com.example.jdbc.DBManagerToLearnClass;
import com.example.jdbc.DBManagerToLearnClassStudent;
import com.example.jsonReturn.CourseClassReturn;
import com.example.onlinelearn.R;
import com.example.util.JsonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyClassActivity extends BaseActivity {

    protected String TAG = "MyClassActivity";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;//数据列表
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;//刷新layout

    MyClassRecyclerAdapter myClassRecyclerAdapter;
    List<LearnClassEntity> learnClassEntityList;

    private DBManagerToLearnClassStudent dbManagers; //sqlite操作对象
    private DBManagerToLearnClass dbManagers1; //sqlite操作对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myclass);
    }

    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        setActivityLeftTitle("我的课程");
        setActivityLeftTitleShow(true);
        setToolbar_backShow(true);
    }

    /**
     * 初始化
     * @param view
     */
    protected void initViews(View view) {
        learnClassEntityList = new ArrayList<LearnClassEntity>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        myClassRecyclerAdapter = new MyClassRecyclerAdapter(this, learnClassEntityList);
        recyclerView.setAdapter(myClassRecyclerAdapter);

        //设置无任务风格
        refreshLayout.setRefreshHeader(new FalsifyHeader(this));

        refreshLayout.setRefreshFooter(new FalsifyFooter(this));

        refreshLayout.setReboundDuration(500);//回弹动画时长（毫秒）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
            }
        });
    }

    /**
     * 逻辑操作
     * @param mContext
     */
    protected void doBusiness(Context mContext) {
        isExistCourseClass();
        /**
         * 如果是食品安全
         */
        if (FinalStr.SYSTEM_CODE.equals("GZSPAQ")) {
            getSPAQProgress();
        }
    }


    /**
     * 设置监听
     */
    public void setListener(){

        myClassRecyclerAdapter.setOnItemClickListener(new DefaultAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                Intent intent=new Intent();
                intent.setClass(MyClassActivity.this,CourseClassIndexActivity.class);
                intent.putExtra("coursewareId",
                        learnClassEntityList.get(position).getCoursewareId()+"#"+learnClassEntityList.get(position).getId()+"#"+learnClassEntityList.get(position).getClassName());
                hdaplication.setClassicname(learnClassEntityList.get(position).getClassName());
                startActivity(intent);
            }
        });

    }

    /**
     * 判断课程班是否存在数据库
     */
    private void isExistCourseClass()  {

        dbManagers = new DBManagerToLearnClassStudent(this);
        dbManagers1 = new DBManagerToLearnClass(this);

        boolean netStatus= NetWork.isNetworkConnected(this);
        if (netStatus) {     //如果有网络
            showLoading();
            getClassList();

        }else { //否则取数据库

            Map<String, Object> map=new HashMap<String, Object>();
            map.put("student_id",hdaplication.getStuid()+"");
            List<LearnClassStudentEntity> list=dbManagers.queryByMap(map);

            if (list!=null&&list.size()>0) {//数据库有数据
                for (LearnClassStudentEntity entity:list){
                    String id=entity.getClassId();
                    LearnClassEntity learnClassEntity= null;
                    try {
                        learnClassEntity = dbManagers1.queryUserById(id);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    learnClassEntityList.add(learnClassEntity);
                }
                myClassRecyclerAdapter.notifyDataSetChanged();
            }else {
                showToast(R.string.server_error);
            }
        }
    }


    /**
     * 获取课程列表
     */
    public void getClassList(){
        Call<ResponseBody> call = serverApi.getClassList("0","1",hdaplication.getStuid());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求视频信息成功
                    try {
                        String result =response.body().string();
                        Log.d(JSON_TAG,"获取课程列表返回"+result);

                        CourseClassReturn courseClassReturn= JsonUtil.parserString(result, CourseClassReturn.class);
                        learnClassEntityList.addAll(courseClassReturn.getLearnClassEntityList());

                        List<LearnClassStudentEntity> LearnClassStudentList=courseClassReturn.getLearnClassStudentList();
                        if (LearnClassStudentList!=null){
                            for (LearnClassStudentEntity e:LearnClassStudentList){
                                dbManagers.saveEntity(e);
                            }
                        }
                        if (learnClassEntityList!=null){
                            for (LearnClassEntity e:learnClassEntityList){
                                dbManagers1.saveEntity(e);
                            }
                        }
                        myClassRecyclerAdapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    showToast(R.string.server_error);
                }

                closeLoading();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                closeLoading();
                showToast(R.string.server_error);
            }
        });
    }

    /**
     * 获取食品安全课程进度
     */
    private void getSPAQProgress(){

        Call<ResponseBody> call = serverApiSPAQ.getSPAQClassProgress(hdaplication.getStuCode());
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
     * 点击操作
     * @param v
     */
    @OnClick({R.id.toolbar_back})
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.toolbar_back:
                finish();
                break;

        }
    }
}
