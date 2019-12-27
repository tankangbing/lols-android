package com.example.Fragment.video;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.Fragment.BaseFragment;
import com.example.adapter.VideoProgressAdapter;
import com.example.entity.VideoRecordModel;
import com.example.entity.VideoSpeedSingle;
import com.example.jdbc.Dao.VideoRecordDAO;
import com.example.jdbc.Dao.impl.VideoRecordDAOImpl;
import com.example.jsonReturn.VideoSpeedFlagReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.courseWare.video.VideoActivity;
import com.example.util.ApplicationUtil;
import com.example.util.JsonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProgressFragment extends BaseFragment {
    protected String JSON_TAG = "JSON";
    protected String TAG = "ProgressFragment";
    private VideoRecordDAO mVideoRecordDAO = null;
    private String behaviorId ="";//学习行为Id
    private String classId ="";//课程Id
    private String accountId ="";//用户Id
    private String accountName ="";//用户名


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView; //进度list
    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;//加载
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;//刷新

    private List<Map<String,Object>> mDatas ;//数据
    private VideoProgressAdapter mAdapter;

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this,view);


        /**
         * 获取用户信息
         */
        behaviorId =((ApplicationUtil)getActivity().getApplication()).getBehaviorId();
        classId =((ApplicationUtil)getActivity().getApplication()).getClassId();
        accountId =((ApplicationUtil)getActivity().getApplication()).getStuid();
        accountName =((ApplicationUtil)getActivity().getApplication()).getUsername();
        mVideoRecordDAO =new VideoRecordDAOImpl(getActivity());

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
       /* DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext,
                LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);*/
        mRecyclerView.setLayoutManager(layoutManager);


        //设置无任务风格
        refreshLayout.setRefreshHeader(new FalsifyHeader(getActivity()));
        refreshLayout.setRefreshFooter(new FalsifyFooter(getActivity()));

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
     *  初始化数据
     */
    protected void initData(){

        mDatas = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("videoSpeedTotalReturn",null);
        mDatas.add(map);

        //设置数据
        mAdapter = new VideoProgressAdapter(mContext, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.INVISIBLE);

        //初始化数据
        initVideoTotalSpeed();
    }

    /**
     * 加载layout
     * @return
     */
    protected int getLayoutId() {
        return R.layout.fragment_list_layout;
    }

    @Override
    public void fetchData() {}


    /**
     * 初始化总汇进度
     */
    public void initVideoTotalSpeed(){

        List<VideoRecordModel> videoRecordModels=mVideoRecordDAO.queryTotalRecordSingle(classId,behaviorId,accountId);
        if (videoRecordModels.size()!=0){   //如果数据库有总汇数据

            int totalTime =0;//总时间
            int completeTime  =0;//完成时间
            //绘制
            List<VideoSpeedSingle> totalList =new ArrayList<>();
            VideoSpeedFlagReturn videoSpeedTotalReturn = new VideoSpeedFlagReturn();

            for (VideoRecordModel videoRecordModel:videoRecordModels){  //取出数据库数据
                if (totalTime ==0){ //初始化总时间
                    totalTime =videoRecordModel.getTotalTime();
                }
                VideoSpeedSingle speedSingle =new VideoSpeedSingle();
                speedSingle.setStartPoint(String.valueOf(videoRecordModel.getStartPoint()));
                speedSingle.setEndPoint(String.valueOf(videoRecordModel.getEndPoint()));
                totalList.add(speedSingle);
                completeTime +=videoRecordModel.getEndPoint()-videoRecordModel.getStartPoint();

            }
            videoSpeedTotalReturn.setVideoTotalTime(totalTime); //设置总时间

            //四舍五入
            String percentStr = String.valueOf(new BigDecimal(
                    (double)completeTime/totalTime * 100).setScale(0, BigDecimal.ROUND_HALF_UP));
            //设置总进度
            videoSpeedTotalReturn.setCalculateVideoSectionPercentage(percentStr);
            videoSpeedTotalReturn.setCollectDLVideoSection(totalList);

            //设置数据
            setVideoSpeedTotalSuccess(videoSpeedTotalReturn);
        }else{ //如果数据库没有则去服务器取
            //显示加载框
            showLoading();
            //获取服务器总汇
            showVideoTotalSpeed();
        }
        //显示UI
        mRecyclerView.setVisibility(View.VISIBLE);

    }

    /**
     * 获取服务器的进度
     */
    public void showVideoTotalSpeed(){

        //调用接口方法
        Call<ResponseBody> call = ((VideoActivity)getActivity()).getServerApi()
                .showVideoSpeed(accountId,classId,behaviorId,"0");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //获取视频进度成功
                    try {
                        String result =response.body().string();
                        Log.d(JSON_TAG,"获取总进度："+result);
                        //gson解析
                        VideoSpeedFlagReturn videoFlagReturn = JsonUtil.parserString(result,VideoSpeedFlagReturn.class);
                        int videoTotalTime =videoFlagReturn.getVideoTotalTime();

                        List<VideoSpeedSingle> videoSpeedSingles =videoFlagReturn.getCollectDLVideoSection();
                        //如果服务器获取的总汇不为空
                        if (null!=videoSpeedSingles && videoSpeedSingles.size()!=0){

                            //删除数据库所有内容
                            List<VideoRecordModel> videoRecordModels = mVideoRecordDAO.queryTotalRecordSingle(classId, behaviorId, accountId);
                            if (videoRecordModels.size() != 0) {
                                if (videoSpeedSingles.size() ==1 && "0".equals(videoSpeedSingles.get(0).getStartPoint()) && "0".equals(videoSpeedSingles.get(0).getEndPoint())){
                                }else {
                                    mVideoRecordDAO.deleteTotalRecords(videoRecordModels);
                                }

                            }

                            //插入服务器内容
                            for (VideoSpeedSingle videoSpeedSingle : videoFlagReturn.getCollectDLVideoSection()) {

                                VideoRecordModel totalRecord = new VideoRecordModel(accountId, accountName, classId, behaviorId, videoTotalTime
                                        , Integer.parseInt(videoSpeedSingle.getStartPoint()), Integer.parseInt(videoSpeedSingle.getEndPoint()));
                                mVideoRecordDAO.insertTotalRecord(totalRecord);
                            }

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                initVideoTotalSpeed();//初始化总汇
                closeLoading();//关闭加载

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                initVideoTotalSpeed();//初始化总汇
                closeLoading();//关闭加载
            }
        });

    }

    /**
     * 获取每日进度
     */

    public void showVideoSpeedDaily(){


        //调用接口方法
        Call<ResponseBody> call = ((VideoActivity)getActivity()).getServerApi()
                .showVideoSpeed(accountId,classId,behaviorId,"1");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //获取视频进度成功
                    try {
                        String result =response.body().string();

                        Log.d(JSON_TAG,"获取每日进度："+result);
                        //gson解析
                        VideoSpeedFlagReturn videoFlagReturn = JsonUtil.parserString(result,VideoSpeedFlagReturn.class);

                        setVideoSpeedDailySuccess(videoFlagReturn); //每日进度执行方法


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{ //获取视频进度失败
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //服务器请求失败
            }
        });

    }

    /**
     * 获取进度总汇成功
     * @param videoSpeedTotalReturn 进度总汇return
     */
    private void setVideoSpeedTotalSuccess(VideoSpeedFlagReturn videoSpeedTotalReturn){

        Map<String,Object> map = new HashMap<>();
        map.put("videoSpeedTotalReturn",videoSpeedTotalReturn);

        //更新位置0的数据
        mAdapter.updateSingleDate(map,0);


        //设置进度详情按钮监听并回调到Activity
        mAdapter.setProgressDetailButtonListener(new VideoProgressAdapter.ProgressDetailButtonOnClickListener() {
            @Override
            public void onClick() {
                boolean b = !mAdapter.isOpenDetail();
                if (b){
                    showVideoSpeedDaily();
                    mAdapter.setOpenDetail(true);
                    mAdapter.notifyDataSetChanged();

                }else{
                    mAdapter.setOpenDetail(false);
                    Map<String,Object> map = mAdapter.getDatas().get(0);
                    mAdapter.getDatas().clear();
                    mAdapter.getDatas().add(map);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void arcOnClick() {
                //记录点
                ((VideoActivity)getActivity()).setEndAndPost();
                //更新点
                ((VideoActivity)getActivity()).setVideoStartPoint();
                //更新进度
                initVideoTotalSpeed();
            }
        });
        //设置seekBar监听并回调到Activity
        mAdapter.setOnChangeListener(new VideoProgressAdapter.ProgressSeekbarChangeListener() {
            @Override
            public void onStopTrackingTouch(int position) {
                    ((VideoActivity)getActivity()).seekBarChangePosition(position);
            }
        });
        ((VideoActivity)getActivity()).setOnChangeListener(new VideoActivity.VideoSeekbarChangeListener() {
            @Override
            public void SampleListener(int position) {
                mAdapter.seekBarChangePosition(position);
            }
        });
    }

    /**
     * 获取每日进度成功
     * @param videoSpeedDailyReturn
     */
    public void setVideoSpeedDailySuccess(VideoSpeedFlagReturn videoSpeedDailyReturn){

        //如果有进度详情
        if (0!=videoSpeedDailyReturn.getDateCollectDLVideoSection().size()){
            //加载进度详情界面
            //排序
            Map<String, List<String>> sortMap = new TreeMap<String, List<String>>(
                    new  Comparator<String>(){
                        @Override
                        public int compare(String str1, String str2) {
                            return str1.compareTo(str2);
                        }
                    });
            sortMap.putAll(videoSpeedDailyReturn.getDateCollectDLVideoSection());

            List<Map<String,Object>> list =new ArrayList<>();
            for (String key:sortMap.keySet()){
                Map<String,Object> map = new HashMap<>();

                map.put(mAdapter.VIDEO_SPEED_DATE,key) ;
                map.put(mAdapter.VIDEO_SPEED_LENGTH,sortMap.get(key));
                list.add(map);
            }
            //保留第一行
            Map<String,Object> indexFirst =mAdapter.getDatas().get(0);
            mAdapter.clearDates();
            mAdapter.addSingleDate(indexFirst);
            mAdapter.addDates(list);
        }
        else //如果没有进度详情
            ((VideoActivity)getActivity()).showToast("暂时没有进度详情");

    }



    public void setIsAdMode(boolean flag){
        mAdapter.setAdPlay(flag);
    }

    public void showLoading(){
        loading.show();
    }

    public void closeLoading(){
        loading.hide();
    }


}





