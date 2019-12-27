package com.example.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.entity.VideoSpeedSingle;
import com.example.jsonReturn.VideoSpeedFlagReturn;
import com.example.onlinelearn.R;
import com.example.util.NetWork;
import com.example.view.multiSeekbar.MultiColorSeekbar;
import com.example.view.circleProgress.ArcProgress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 进度页面加载数据
 */
public class VideoProgressAdapter extends BaseRecyclerAdapter<Map<String,Object>> implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {


    public interface ProgressDetailButtonOnClickListener{
        void onClick();
        void arcOnClick();
    }
    private ProgressDetailButtonOnClickListener onClickListener;

    public interface ProgressSeekbarChangeListener{
        void onStopTrackingTouch(int position);
    }
    private ProgressSeekbarChangeListener onChangeListener;

    //查看进度详情按钮
    private TextView progress_detail_bt;
    //总汇content
    private RelativeLayout total_content;
    //每日content
    private LinearLayout daily_content;
    //百分比seekbar
    private ArcProgress arc_progress;
    //进度总汇片段seekbar
    private MultiColorSeekbar multi_color_sb1;
    //每日进度日期
    private TextView progress_date_tv;
    //seekbar
    private MultiColorSeekbar multi_color_sb;
    //完成百分比
    private int percentage=0;
    //视频总时间
    private int totalTime =0;
    //缓存seekbar的拖拉位置
    private int progressCashe =0;
    //是否打开详情
    private boolean isOpenDetail =false;

    public final String VIDEO_SPEED_DATE ="VideoSpeedDate";
    public final String VIDEO_SPEED_LENGTH ="VideoSpeedLength";

    private boolean isAdPlay =true;//默认一开始是广告播放
    public VideoProgressAdapter(Context context, List<Map<String,Object>> datas ) {
        super(context, R.layout.video_progress_item, datas);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Map<String,Object> item, int position) {

        progress_detail_bt= holder.getView(R.id.progress_detail_bt);
        total_content = holder.getView(R.id.total_progress_content);
        daily_content = holder.getView(R.id.daily_progress_content);
        arc_progress = holder.getView(R.id.arc_progress);
        multi_color_sb1 = holder.getView(R.id.multi_color_sb1);
        progress_date_tv = holder.getView(R.id.progress_date_tv);
        multi_color_sb =holder.getView(R.id.multi_color_sb);
        //===================修复<视频>，进度，点击查看进度详情后，字体没有变成收起进度详情
        Log.d("openDetail","查看/收起进度详情：");
        if(isOpenDetail){
            progress_detail_bt.setText("收起进度详情");
        }else {
            progress_detail_bt.setText("查看进度详情");
        }
        //===================修复<视频>，进度，点击查看进度详情后，字体没有变成收起进度详情
        //如果为第一个item时
        if (position==0){

            if (NetWork.isNetworkConnected(mContext)){ //如果在线,才能查看进度
                progress_detail_bt.setVisibility(View.VISIBLE);
                daily_content.setVisibility(View.VISIBLE);
            }else{
                progress_detail_bt.setVisibility(View.INVISIBLE);
                daily_content.setVisibility(View.INVISIBLE);
            }

            total_content.setVisibility(View.VISIBLE);
            daily_content.setVisibility(View.GONE);


            VideoSpeedFlagReturn videoSpeedTotalReturn = (VideoSpeedFlagReturn) mDatas.get(0)
                    .get("videoSpeedTotalReturn");
            //百分比
            percentage =Integer.parseInt(videoSpeedTotalReturn ==null?"0": videoSpeedTotalReturn.getCalculateVideoSectionPercentage());
            //视频总时间
            totalTime =(videoSpeedTotalReturn ==null?0:videoSpeedTotalReturn.getVideoTotalTime());

            if (videoSpeedTotalReturn!=null){
                //绘制片段seekbar
                multi_color_sb1.refreshData(videoSpeedTotalReturn.getCollectDLVideoSection()
                        ,totalTime,progressCashe);

                if (isAdPlay){//如果是广告播放

                    multi_color_sb1.setEnabled(false);
                    multi_color_sb1.setOnSeekBarChangeListener(null);
                }else{

                    multi_color_sb1.setEnabled(true);
                    multi_color_sb1.setOnSeekBarChangeListener(this);
                }
            }else{
                multi_color_sb1.setEnabled(false);
                multi_color_sb1.setOnSeekBarChangeListener(null);
            }


            //设置百分比
            startAnimator(arc_progress,percentage);
            //设置监听
            progress_detail_bt.setOnClickListener(this);
            arc_progress.setOnClickListener(this);
        }else {


            total_content.setVisibility(View.GONE);
            daily_content.setVisibility(View.VISIBLE);

            List<VideoSpeedSingle> speedSingleList = new ArrayList<>();

            progress_date_tv.setText(mDatas.get(position).get(VIDEO_SPEED_DATE)+"");

            List<String> strlist =(ArrayList)mDatas.get(position).get(VIDEO_SPEED_LENGTH);

            for (String str:strlist){
                String[] point = str.split("-");
                VideoSpeedSingle videoSppedSingel =new VideoSpeedSingle();
                videoSppedSingel.setStartPoint(point[0]);
                videoSppedSingel.setEndPoint(point[1]);
                speedSingleList.add(videoSppedSingel);
            }

            //刷新seekbar背景
            multi_color_sb.refreshData(speedSingleList
                    ,totalTime,0);



        }

    }

    public void seekBarChangePosition(int position) {

        int time = position / 1000;
        Log.d("adpter",position+" ="+totalTime);
        VideoSpeedFlagReturn videoSpeedTotalReturn = (VideoSpeedFlagReturn) mDatas.get(0)
                .get("videoSpeedTotalReturn");
        //百分比
        percentage =Integer.parseInt(videoSpeedTotalReturn ==null?"0": videoSpeedTotalReturn.getCalculateVideoSectionPercentage());
        //视频总时间
        totalTime =(videoSpeedTotalReturn ==null?0:videoSpeedTotalReturn.getVideoTotalTime());

        if (videoSpeedTotalReturn!=null){
            //绘制片段seekbar

            multi_color_sb1.refreshData(videoSpeedTotalReturn.getCollectDLVideoSection()
                    ,totalTime,time);

//            if (isAdPlay){//如果是广告播放
//
//                multi_color_sb1.setEnabled(false);
//                multi_color_sb1.setOnSeekBarChangeListener(null);
//            }else{
//
//                multi_color_sb1.setEnabled(true);
//                multi_color_sb1.setOnSeekBarChangeListener(this);
//            }
        }else{
            multi_color_sb1.setEnabled(false);
            multi_color_sb1.setOnSeekBarChangeListener(null);
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

    public void setProgressDetailButtonListener(ProgressDetailButtonOnClickListener listener){
        this.onClickListener =listener;
    }

    public void setOnChangeListener(ProgressSeekbarChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.progress_detail_bt:
                if (onClickListener !=null){
                    onClickListener.onClick();
                }
                break;
            case R.id.arc_progress:
                if (onClickListener !=null){
                    onClickListener.arcOnClick();
                }
                break;
        }
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        progressCashe =seekBar.getProgress();
        if (onChangeListener!=null){
            onChangeListener.onStopTrackingTouch(seekBar.getProgress());
        }
    }

    public void setAdPlay(boolean adPlay) {
        isAdPlay = adPlay;
        Log.d("XX","setAdPlay"+isAdPlay);
        notifyDataSetChanged();
    }

    public boolean isOpenDetail() {
        return isOpenDetail;
    }

    public void setOpenDetail(boolean openDetail) {
        isOpenDetail = openDetail;
        //===================修复<视频>，进度，点击查看进度详情后，字体没有变成收起进度详情
//        Log.d("openDetail","查看/收起进度详情："+openDetail);
//        if(openDetail){
//            progress_detail_bt.setText("收起进度详情");
//        }else {
//            progress_detail_bt.setText("查看进度详情");
//        }
        //===================修复<视频>，进度，点击查看进度详情后，字体没有变成收起进度详情
    }
}
