package com.example.onlinelearnActivity.courseWare.question;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.adapter.AnswerRecyclerAdapter;
import com.example.adapter.DefaultAdapter;
import com.example.entity.AnswerAdapterModel;
import com.example.entity.AnswerSingle;
import com.example.entity.TopicSingle;
import com.example.jsonReturn.AnswerOneFlagReturn;
import com.example.jsonReturn.SubmitAnswerReturn;
import com.example.jsonReturn.TopicContentFlagReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.BaseActivity;
import com.example.onlinelearnActivity.PopActivity;
import com.example.util.JsonUtil;
import com.example.view.goodview.GoodView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AnswerActivity extends BaseActivity {

    protected String TAG = "AnswerActivity";

    public final String SINGLE_TOPIC_KEY = "SINGLE_TOPIC_KEY";
    public final String REPLY_ONE_ID = "REPLY_ONE_ID";//一级回答主键.
    public final String FLOOR = "FLOOR";//一级回答主键
    public final String ACTION_UP ="0";//点赞
    public final String ACTION_DOWN ="2";//踩
   /* public final String ACTION_DOWN ="2";//踩
    public final String ACTION_CANCLE_DOWN ="3";//取消踩*/

    private String singleTopicId ;//问题主体id
    private String accountName;//用户名
    private String accountId;//用户id

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;//刷新layout
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;//数据列
    @BindView(R.id.answer_bt)
    TextView answer_bt;//回答按钮


    private GoodView mGoodView;//点赞及踩的view

    private List<AnswerAdapterModel> datas;//数据
    private Set<AnswerAdapterModel> dataTemp;//阻止重复数据
    private AnswerRecyclerAdapter adapter;

    private int pageNum =1;//初始化显示页数
    private int pageNumTotal =1;//初始化显示页数
    private final int pageCount =10;//每页数目
    private int forumReplyFirstCount=0;//一级回答个数
    private boolean isLoading =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
    }

    /**
     * 全局信息初始化
     */
    public void initialize(Bundle savedInstanceState) {

        super.initialize(savedInstanceState);
        /**
         * 获取用户信息
         */
        accountId =hdaplication.getStuid();
        accountName =hdaplication.getUsername();
        singleTopicId=(String) getIntent().getExtras().get(SINGLE_TOPIC_KEY);
    }

    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        setActivityLeftTitle("详情");
        setActivityLeftTitleShow(true);
        setToolbar_backShow(true);

    }
    /**
     * 初始化
     */
    protected void initViews(View view){


        mGoodView = new GoodView(this);//点赞动画

        datas =new ArrayList<AnswerAdapterModel>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // 设置adapter
        adapter = new AnswerRecyclerAdapter(this, datas);
        recyclerView.setAdapter(adapter);

    }

    /**
     * 设置listener
     */
    public void setListener(){

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
                load();
            }
        });

        //点赞/踩/评论的点击监听
        adapter.setViewListener(new AnswerRecyclerAdapter.ViewListener() {
            @Override
            public void onclick(View v, AnswerAdapterModel item,int position) {
                switch (v.getId()){
                    case R.id.goodview_ly:
                        //点赞
                        addOrUpdateReplyFirstOpt(item,ACTION_UP,v,position);
                        break;
                    case R.id.badview_ly:
                        //踩
                        addOrUpdateReplyFirstOpt(item,ACTION_DOWN,v,position);
                        break;
                    case R.id.commentview_ly:
                        Intent i = new Intent();
                        i.setClass(AnswerActivity.this, LevelAnswerActivity.class);
                        i.putExtra(REPLY_ONE_ID, datas.get(position).getAnswerSingle().getId());
                        i.putExtra(FLOOR, String.valueOf(position+1));
                        startActivity(i);
                        break;
                }
            }

        });

        adapter.setOnItemClickListener(new DefaultAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {

                if (position !=0){
                    Intent i = new Intent();
                    i.setClass(AnswerActivity.this, LevelAnswerActivity.class);
                    i.putExtra(REPLY_ONE_ID, datas.get(position).getAnswerSingle().getId());
                    i.putExtra(FLOOR, String.valueOf(position+1));
                    startActivity(i);
                }

            }
        });
    }

    /**
     * 逻辑操作
     * @param mContext
     */
    protected void doBusiness(Context mContext) {

        //加载问题
        getQuestionContentThread(singleTopicId);
    }

    /**
     * 获取问题详情Thread
     */
    public void getQuestionContentThread(final String singleTopicId){
        //显示加载框
        showLoading();
        //调用接口方法
        Call<ResponseBody> call = serverApi.getQuestionContent(singleTopicId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求视频信息成功
                    try {
                        //获取第一页回答
                        getQuestionAnswerThread();

                        String result =response.body().string();
                        Log.d(JSON_TAG,"问题详情返回："+result);
                        TopicContentFlagReturn topicContentFlagReturn=JsonUtil.parserString(result,TopicContentFlagReturn.class);

                        if (topicContentFlagReturn.isSuccess()){
                            AnswerAdapterModel answerAdapterModel =new AnswerAdapterModel();
                            TopicSingle topicSingle =topicContentFlagReturn.getForumPostModel();
                            answerAdapterModel.setTopicSingle(topicSingle);
                            adapter.addSingleDate(answerAdapterModel,0);

                            forumReplyFirstCount =Integer.parseInt(topicSingle.getAnswerCount());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    //关闭加载框
                    closeLoading();
                    showToast(R.string.server_error);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //关闭加载框
                closeLoading();
                showToast(R.string.server_error);
            }
        });
    }



    /**
     * 获取单个问题一级回答
     */
    public void getQuestionAnswerThread(){

        //调用接口方法
        Call<ResponseBody> call = serverApi.getForumReplyFirstList(singleTopicId,accountId,String.valueOf(pageNum));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        String result =response.body().string();

                        Log.d(JSON_TAG,"单个问题一级回答返回："+result);

                        AnswerOneFlagReturn answerOneFlagReturn=JsonUtil.parserString(result,AnswerOneFlagReturn.class);

                        if (answerOneFlagReturn.isSuccess()){

                            List<AnswerSingle> answerList =answerOneFlagReturn.getForumReplyFirstModelList();//回答list

                            boolean update =false;
                            AnswerAdapterModel content = datas.get(0);//取出第1个item
                            datas.remove(0);//除去
                            dataTemp =new HashSet<AnswerAdapterModel>(datas);//加入重复检查
                            datas.add(0,content);//重新加入
                            for (AnswerSingle answerSingle:answerList){
                                AnswerAdapterModel answerAdapterModel =new AnswerAdapterModel();
                                answerAdapterModel.setAnswerSingle(answerSingle);
                                //如果能加入该set
                                if (dataTemp.add(answerAdapterModel)){
                                    adapter.addSingleDate(answerAdapterModel);//datas也加入
                                    update=true;
                                }
                            }
                            dataTemp.clear();//清空
                            if (!update&&isLoading){
                                showToast("暂无更多回复");
                            }
                            //设置监听
                            setListener();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //如果是下拉刷新
                if (isLoading){
                    loadFinish();
                }else{
                    closeLoading();
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //请求失败
                if (isLoading){
                    loadFinish();
                }else{
                    closeLoading();
                }
                showToast(R.string.server_error);
            }
        });
    }

    /**
     * 点赞或者取消点赞
     */
    public void addOrUpdateReplyFirstOpt(final AnswerAdapterModel item, final String optType,final View v,final int position){

        //调用接口方法
        Call<ResponseBody> call = serverApi.addOrUpdateReplyFirstOpt(accountId,accountName,item.getAnswerSingle().getId(),optType);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求视频信息成功
                    try {
                        String result =response.body().string();
                        Log.d(JSON_TAG,"点赞或踩返回："+result);

                        JSONObject obj =new JSONObject(result);
                        if (obj.getBoolean("success")){
                            String action =obj.getString("msg");
                            goodViewChangeUi(v,action,item,position);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //服务器请求失败
            }
        });
    }

    /**
     * 判断当前显示是否最后一个
     * @return
     */
    private boolean isLastVisibility(){
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        //这里的findLastVisibleItemPosition总比getItemCount少2,需要-2
       return recyclerView.getLayoutManager().getItemCount() - 2 ==
               ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
    }


    /**
     * 上拉加载操作
     */
    private void load(){
        //当前为加载操作
        isLoading =true;
        //如果当前页<总共页
        if (pageNum<pageNumTotal){
            //加载下一页
            pageNum+=1;
        }
        getQuestionAnswerThread();

    }

    /**
     * 结束上拉加载
     */
    private void loadFinish(){
        refreshLayout.finishLoadmore();
        isLoading =false;
    }

    /**
     * 点击操作
     * @param v
     */
    @OnClick({R.id.answer_bt})
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){

            case R.id.answer_bt:

                Intent intent = new Intent(this, PopActivity.class);
                intent.putExtra("ACTION","ANSWER_ACTION");
                intent.putExtra(SINGLE_TOPIC_KEY,singleTopicId);
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_bottom_in, R.anim.animo_no);
                ActivityCompat.startActivityForResult(this, intent,SUBMIT_ANSWER_SUCCESS, compat.toBundle());
                overridePendingTransition(R.anim.slide_bottom_in, R.anim.animo_no);

                break;
        }
    }

    /**
     * 点赞以后的ui操作
     * @param v  被操作view
     * @param type  操作的类型
     * @param item  被操作的对象
     * @param position  被操作的位置
     */
    private void goodViewChangeUi(View v,String type,AnswerAdapterModel item,int position){
        switch(type){
            case "0":
                //点赞+1
                int uptime =item.getAnswerSingle().getUpTimes();
                item.getAnswerSingle().setUpTimes(uptime+1);

                item.getAnswerSingle().setCurrentOpt(ACTION_UP);
                adapter.notifyItemChanged(position);

                mGoodView.setText("+1");
                mGoodView.show(v.findViewById(R.id.good_bg));


                break;
            case "1":
                //取消赞

                int uptime1 =item.getAnswerSingle().getUpTimes();
                item.getAnswerSingle().setUpTimes(uptime1-1);
                item.getAnswerSingle().setCurrentOpt("");
                adapter.notifyItemChanged(position);
                break;
            case "2":
                //点赞取消踩
                int uptime2 =item.getAnswerSingle().getUpTimes();
                item.getAnswerSingle().setUpTimes(uptime2+1);

                int downtime =item.getAnswerSingle().getDownTimes();
                item.getAnswerSingle().setDownTimes(downtime-1);
                item.getAnswerSingle().setCurrentOpt(ACTION_UP);
                adapter.notifyItemChanged(position);

                mGoodView.setText("+1");
                mGoodView.show(v.findViewById(R.id.good_bg));


                break;
            case "3":
                //踩
                int downtime1 =item.getAnswerSingle().getDownTimes();
                item.getAnswerSingle().setDownTimes(downtime1+1);

                item.getAnswerSingle().setCurrentOpt(ACTION_DOWN);
                adapter.notifyItemChanged(position);

                mGoodView.setText("+1");
                mGoodView.show(v.findViewById(R.id.bad_bg));

                break;

            case "4":
                //取消踩
                int downtime2 =item.getAnswerSingle().getDownTimes();
                item.getAnswerSingle().setDownTimes(downtime2-1);

                item.getAnswerSingle().setCurrentOpt("");
                adapter.notifyItemChanged(position);

                break;

            case "5":
                //踩并取消赞
                int downtime3 =item.getAnswerSingle().getDownTimes();
                item.getAnswerSingle().setDownTimes(downtime3+1);

                int uptime3 =item.getAnswerSingle().getUpTimes();
                item.getAnswerSingle().setUpTimes(uptime3-1);

                item.getAnswerSingle().setCurrentOpt(ACTION_DOWN);
                adapter.notifyItemChanged(position);

                mGoodView.setText("+1");
                mGoodView.show(v.findViewById(R.id.bad_bg));

                break;

        }
    }

    /**
     * 增加回答
     * @param result
     */
    private void addAnswer(String result){

        SubmitAnswerReturn submitAnswerReturn=JsonUtil.parserString(result,SubmitAnswerReturn.class);

        if (forumReplyFirstCount < pageCount || isLastVisibility()) {   //小于20或者当前是最后一个，就直接增加

            AnswerAdapterModel answerAdapterModel = new AnswerAdapterModel();
            answerAdapterModel.setAnswerSingle(submitAnswerReturn.getForumReplyFirstModel());
            adapter.addSingleDate(answerAdapterModel);

        }
        //更改回答个数
        forumReplyFirstCount += 1;
        adapter.getDatas().get(0).getTopicSingle().setAnswerCount(String.valueOf(forumReplyFirstCount));
        adapter.notifyItemChanged(0);
        //更改总页数
        pageNumTotal = submitAnswerReturn.getPageNum();

    }

    /**
     * 提交回答成功回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case SUBMIT_ANSWER_SUCCESS:
                //获取新增问题
                String result = data.getExtras().getString("result");
                //加入list
                addAnswer(result);
                break;
            default:
                break;
        }
    }



}
