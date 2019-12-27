package com.example.Fragment;

/**
 * 问答页面
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.adapter.DefaultAdapter;
import com.example.adapter.TopicRecyclerAdapter;
import com.example.entity.TopicSingle;
import com.example.jsonReturn.SubmitTopicReturn;
import com.example.jsonReturn.TopicFlagReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.CourseClassIndexActivity;
import com.example.onlinelearnActivity.PopActivity;
import com.example.onlinelearnActivity.courseWare.question.AnswerActivity;
import com.example.onlinelearnActivity.courseWare.video.VideoActivity;
import com.example.util.ApplicationUtil;
import com.example.util.JsonUtil;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicFragment  extends BaseFragment {

    public final String SINGLE_TOPIC_KEY = "SINGLE_TOPIC_KEY";

    /*static {
        ClassicsFooter.REFRESH_FOOTER_FINISH = "加载完成";
    }*/

    private String accountId ;
    private String classId;
    private String coursewareId;
    private String chapterId ="";

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;//刷新layout
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;//数据列表
    @BindView(R.id.question_fab)
    FloatingActionButton question_fab;
    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;

    List<TopicSingle> datas;//数据
    TopicRecyclerAdapter adapter;

    private int pageNum =1;//初始化显示页数
    private int pageNumTotal =1;//初始化显示页数
    private int pageCount =0;//每页数目
    private int topicCount=0;//问题个数
    private boolean isLoading =false;
    private LinearLayoutManager layoutManager;

    public static TopicFragment newInstance() {
        TopicFragment fragment = new TopicFragment();

        return fragment;
    }

    /**
     * 初始化
     */
    protected void initView(View view) {
        ButterKnife.bind(this, view);

        /**
         * 获取用户信息
         */
        coursewareId =((ApplicationUtil)getActivity().getApplication()).getCoursewareId();
        classId =((ApplicationUtil)getActivity().getApplication()).getClassId();
        accountId =((ApplicationUtil)getActivity().getApplication()).getStuid();
        try{
            chapterId =((VideoActivity)getActivity()).getChapterId();
        }catch (Exception e){
            //抓取非VideoActivity的Exception
        }

        datas =new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TopicRecyclerAdapter(getActivity(), datas);
        recyclerView.setAdapter(adapter);
        /*//获取问题列表
        getQuestionListThread(chapterId);*/

        ClassicsHeader classicsHeader =new ClassicsHeader(getActivity());
        classicsHeader.setSpinnerStyle(SpinnerStyle.Translate);
        //设置是否显示时间
        classicsHeader.setEnableLastTime(false);
        //设置无任务风格
        refreshLayout.setRefreshHeader(classicsHeader);

        refreshLayout.autoRefresh();

    }


    /**
     * 设置监听器
     */
    protected void initListener() {

        /**
         * 问题咧监听
         */
        adapter.setOnItemClickListener(new DefaultAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {

                Intent i = new Intent();
                i.setClass(getActivity(), AnswerActivity.class);
                //传递单个问题
                Bundle mBundle = new Bundle();
                mBundle.putString(SINGLE_TOPIC_KEY, datas.get(position).getId());
                i.putExtras(mBundle);

                startActivity(i);
            }
        });


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                load();
            }
        });
    }



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_topic;
    }

    @Override
    public void fetchData() {

    }

    /**
     * 下拉刷新操作
     */
    private void refresh(){
        datas = new ArrayList<>();
        adapter.setDatas(datas);
        adapter.notifyDataSetChanged();
        pageNum =1;//初始化显示页数
        pageNumTotal =1;//初始化显示页数
        getQuestionListThread(chapterId);
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
        getQuestionListThread(chapterId);

    }

    /**
     * 结束上拉加载
     */
    private void loadFinish(){
        refreshLayout.finishLoadmore();
        isLoading =false;
    }


    /**
     * 获取问题列表
     *
     */
    public void getQuestionListThread(String chapterId){
        if (!isLoading){
            //显示loading
            loading.show();
        }

        Call<ResponseBody> call =null;
        if ("" == chapterId){
            call = ((CourseClassIndexActivity)getActivity()).getServerApi().getForumPostList(accountId,classId,coursewareId,"","0","0",String.valueOf(pageNum));
        }else{
            call = ((VideoActivity)getActivity()).getServerApi().getForumPostList(accountId,classId,coursewareId,chapterId,"0","0",String.valueOf(pageNum));
        }
        //调用接口方法
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String topicListResult =response.body().string();
                        Log.d(JSON_TAG,"问题列表返回："+topicListResult);

                        TopicFlagReturn topicFlagReturn = JsonUtil.parserString(topicListResult,TopicFlagReturn.class);

                        if (topicFlagReturn.isSuccess()){
                            //成功操作
                            List<TopicSingle> topicList =topicFlagReturn.getForumPostModelList();
                            pageNumTotal = topicFlagReturn.getPageNum();
                            pageCount =topicFlagReturn.getPageCount();
                            topicCount =topicFlagReturn.getForumPostCount();

                            boolean update =false;
                            Set<TopicSingle> dataTemp =new HashSet<TopicSingle>(datas);//加入重复检查

                            for (TopicSingle topicSingle:topicList){
                                //如果能加入该set
                                if (dataTemp.add(topicSingle)){
                                    adapter.addSingleDate(topicSingle);//datas也加入
                                    update=true;
                                }
                            }
                            dataTemp.clear();//清空
                            if (!update&&isLoading){

                                adapter.setNoMore(true);
                                adapter.notifyDataSetChanged();
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //如果是下拉刷新
                    if (isLoading){
                        loadFinish();
                    }else{
                       loading.hide();
                        refreshLayout.finishRefresh();
                    }

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //关闭加载
                if (isLoading){
                    loadFinish();
                }else{
                    loading.hide();
                    refreshLayout.finishRefresh();
                }
                ((CourseClassIndexActivity)getActivity()).showToast(R.string.server_error);
            }
        });
    }

    /**
     * 提交问题成功
     * @param result
     */
    public void postQuestionSuccess(String result){
        SubmitTopicReturn submitTopicReturn = JsonUtil.parserString(result,SubmitTopicReturn.class);
        adapter.addSingleDate(submitTopicReturn.getForumPostModelList(),0);
        layoutManager.scrollToPositionWithOffset(0, 0);
//        layoutManager.setStackFromEnd(true);
    }



    @OnClick({R.id.question_fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.question_fab:

                Intent intent = new Intent(getActivity(), PopActivity.class);
                intent.putExtra("ACTION","TOPIC_ACTION");
                if (!("".equals(chapterId))){
                    intent.putExtra("CHAPTER_ID",chapterId);
                }
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(getActivity(), R.anim.slide_bottom_in, R.anim.animo_no);
                ActivityCompat.startActivityForResult(getActivity(), intent, 1,compat.toBundle());
                getActivity().overridePendingTransition(R.anim.slide_bottom_in, R.anim.animo_no);
                break;
        }
    }


}