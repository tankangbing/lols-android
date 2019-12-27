package com.example.view.videoView;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.courseWare.video.VideoActivity;
import com.example.util.CommonUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import moe.codeest.enviews.ENDownloadView;
import moe.codeest.enviews.ENPlayView;
import tv.danmaku.ijk.media.player.IMediaPlayer;


public class LandLayoutVideo extends StandardGSYVideoPlayer {

    protected String TAG = "LandLayoutVideo";

    private Context context;
    //广告时间框
    private TextView mADTextView;
    //错误框
    private TextView mErrorTextView;
    //图片广告框
    private ImageView mADImageView;
    //图片广告layout
    private RelativeLayout mAdLayout;
    //图片广告url
    private String url ="";

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public LandLayoutVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        this.context =context;
        initviews();
    }

    public LandLayoutVideo(Context context) {
        super(context);
        this.context =context;
        initviews();
    }

    public LandLayoutVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context =context;
        initviews();
    }
    private void initviews() {
        setShrinkImageRes(R.drawable.video_shrinks);
        setEnlargeImageRes(R.drawable.video_enlarges);

        mADTextView =(TextView)findViewById(R.id.ad_tv);
        mErrorTextView =(TextView)findViewById(R.id.error_tv);
        mADImageView =(ImageView)findViewById(R.id.ad_iv);
        mAdLayout =(RelativeLayout) findViewById(R.id.ad_layout);
        setViewShowState(mFullscreenButton, INVISIBLE);
    }


    //这个必须配置最上面的构造才能生效
    @Override
    public int getLayoutId() {
        if (mIfCurrentIsFullscreen) {
            return R.layout.sample_video_land;
        }
        return R.layout.sample_video_normal;
    }

    /**
     * 重载updateStartImage
     */
    protected void updateStartImage() {
        if (mIfCurrentIsFullscreen) {
            if(mStartButton instanceof ImageView) {
                ImageView imageView = (ImageView) mStartButton;
                if (mCurrentState == CURRENT_STATE_PLAYING) {
                    imageView.setImageResource(R.drawable.video_click_pause_selector);
                } else if (mCurrentState == CURRENT_STATE_ERROR) {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                } else {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                }
            }
        } else {
            if (mStartButton instanceof ENPlayView) {
                ENPlayView enPlayView = (ENPlayView) mStartButton;
                enPlayView.setDuration(500);
                if (mCurrentState == CURRENT_STATE_PLAYING) {
                    enPlayView.play();
                } else if (mCurrentState == CURRENT_STATE_ERROR) {
                    enPlayView.pause();
                } else {
                    enPlayView.pause();
                }
            } else if (mStartButton instanceof ImageView) {
                ImageView imageView = (ImageView) mStartButton;
                if (mCurrentState == CURRENT_STATE_PLAYING) {
                    imageView.setImageResource(com.shuyu.gsyvideoplayer.R.drawable.video_click_pause_selector);
                } else if (mCurrentState == CURRENT_STATE_ERROR) {
                    imageView.setImageResource(com.shuyu.gsyvideoplayer.R.drawable.video_click_error_selector);
                } else {
                    imageView.setImageResource(com.shuyu.gsyvideoplayer.R.drawable.video_click_play_selector);
                }
            }
        }
    }

    /**
     * 重载
     * changeUiToNormal
     */
    protected void changeUiToNormal(){
        Log.d(TAG,"changeUiToNormal" );
        setViewShowState(mErrorTextView, INVISIBLE);
        if (VideoActivity.isAdPlay){
            //关闭滑动
            this.setIsTouchWiget(false);
            this.setIsTouchWigetFull(false);

            //显示广告框
            setViewShowState(mADTextView, VISIBLE);

            setViewShowState(mTopContainer, INVISIBLE);
            setViewShowState(mBottomContainer, INVISIBLE);
            setViewShowState(mStartButton, INVISIBLE);
            setViewShowState(mLoadingProgressBar, INVISIBLE);
            setViewShowState(mThumbImageViewLayout, INVISIBLE);
            setViewShowState(mBottomProgressBar, INVISIBLE);
            setViewShowState(mLockScreen, (mIfCurrentIsFullscreen && mNeedLockFull) ? VISIBLE : GONE);

            updateStartImage();

        }else {
            //开启滑动
            this.setIsTouchWiget(true);
            this.setIsTouchWigetFull(true);

            setViewShowState(mADTextView, INVISIBLE);
            setViewShowState(mFullscreenButton, VISIBLE);
            super.changeUiToNormal();
        }
    }

    /**
     * 重载
     * changeUiToPreparingShow
     */
    protected void changeUiToPreparingShow(){

        Log.d(TAG,"changeUiToPreparingShow");

        if (VideoActivity.isAdPlay){

            setViewShowState(mLoadingProgressBar, VISIBLE);
            setViewShowState(mFullscreenButton, VISIBLE);

        }else{
            setViewShowState(mFullscreenButton, VISIBLE);
            super.changeUiToPreparingShow();
        }


    }

    /**
     * 重载
     * changeUiToPlayingShow
     */
    protected void changeUiToPlayingShow(){

        Log.d(TAG,"changeUiToPlayingShow");

        if (VideoActivity.isAdPlay){
            setViewShowState(mLoadingProgressBar, INVISIBLE);
            setViewShowState(mFullscreenButton, VISIBLE);

        }else{
            setViewShowState(mFullscreenButton, VISIBLE);
            super.changeUiToPlayingShow();

        }

    }

    /**
     * 重载
     * changeUiToPauseShow
     */
    protected  void changeUiToPauseShow(){

        Log.d(TAG,"changeUiToPauseShow");

        if (VideoActivity.isAdPlay){

        }else{

            setViewShowState(mFullscreenButton, VISIBLE);

            super.changeUiToPauseShow();
        }


    }

    /**
     * 重载
     * changeUiToError
     */
    protected  void changeUiToError(){

        Log.d(TAG,"changeUiToError");

        super.changeUiToError();
        if (isIfCurrentIsFullscreen()){
            setViewShowState(mErrorTextView, VISIBLE);
        }

    }

    /**
     * 重载
     * changeUiToCompleteShow
     */
    protected  void changeUiToCompleteShow(){

        Log.d(TAG,"changeUiToCompleteShow");

        if (VideoActivity.isAdPlay){

        }else{
            setViewShowState(mFullscreenButton, VISIBLE);
            super.changeUiToCompleteShow();
        }

    }

    /**
     * 重载
     * changeUiToPlayingBufferingShow
     */
    protected  void changeUiToPlayingBufferingShow(){

        Log.d(TAG,"changeUiToPlayingBufferingShow");

        if (VideoActivity.isAdPlay){

            setViewShowState(mFullscreenButton, VISIBLE);
            setViewShowState(mLoadingProgressBar, VISIBLE);


        }else{
            setViewShowState(mFullscreenButton, VISIBLE);
            super.changeUiToPlayingBufferingShow();
        }


    }

    /**
     * 重载
     * changeUiToPrepareingClear
     */
    protected void changeUiToPrepareingClear() {

        Log.d(TAG,"changeUiToPrepareingClear");

        setViewShowState(mFullscreenButton, INVISIBLE);
        super.changeUiToPrepareingClear();
    }

    /**
     * 重载
     * changeUiToPlayingClear
     */
    protected void changeUiToPlayingClear() {
        Log.d(TAG,"changeUiToPlayingClear");

        setViewShowState(mFullscreenButton, INVISIBLE);
        super.changeUiToPlayingClear();
    }

    /**
     * 重载
     * changeUiToPauseClear
     */
    protected void changeUiToPauseClear() {
        Log.d(TAG,"changeUiToPauseClear");

        setViewShowState(mFullscreenButton, INVISIBLE);
        super.changeUiToPauseClear();
    }

    /**
     * 重载
     * changeUiToPlayingBufferingClear
     */
    protected void changeUiToPlayingBufferingClear() {
        Log.d(TAG,"changeUiToPlayingBufferingClear");

        setViewShowState(mFullscreenButton, INVISIBLE);
        super.changeUiToPlayingBufferingClear();
    }

    /**
     * 重载
     * changeUiToClear
     */
    protected void changeUiToClear() {
        Log.d(TAG,"changeUiToClear");

        setViewShowState(mFullscreenButton, INVISIBLE);
        super.changeUiToClear();
    }

    /**
     * 重载
     * changeUiToCompleteClear
     */
    protected void changeUiToCompleteClear() {
        Log.d(TAG,"changeUiToCompleteClear");

        setViewShowState(mFullscreenButton, INVISIBLE);
        super.changeUiToCompleteClear();
    }

    /**
     * 重载
     * hideAllWidget
     */
    public void hideAllWidget(){
        if (VideoActivity.isAdPlay){
            setViewShowState(mFullscreenButton, INVISIBLE);
        }else{
            setViewShowState(mFullscreenButton, INVISIBLE);
            super.hideAllWidget();
        }
    }

    /**
     * 重载双击停止/开始
     * 不需要这功能
     */
    protected void touchDoubleUp() {

    }

    /**
     * 设置广告时间
     * @param adTime
     */
    public void setADTime(int adTime){
        mADTextView.setText("广告 "+adTime);
    }

    /**
     * 设置广告显示/隐藏
     * @param flag
     */
    public void showAdTextView(boolean flag){
        if (flag){
            mADTextView.setVisibility(VISIBLE);
        }else {
            mADTextView.setVisibility(INVISIBLE);
        }
    }

    /**
     * 重写缓冲方法
     * @param what
     * @param extra
     */
    public void onInfo(int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            mBackUpPlayingBufferState = mCurrentState;
            //避免在onPrepared之前就进入了buffering，导致一只loading
            if (mHadPlay && mCurrentState != CURRENT_STATE_PREPAREING && mCurrentState > 0)
                setStateAndUi(CURRENT_STATE_PLAYING_BUFFERING_START);

        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            if (mBackUpPlayingBufferState != -1) {

                //如果当前状态下为停止，则返回
                if (mCurrentState == CURRENT_STATE_PAUSE)
                    return;
                if (mHadPlay && mCurrentState != CURRENT_STATE_PREPAREING && mCurrentState > 0)
                    setStateAndUi(mBackUpPlayingBufferState);

                mBackUpPlayingBufferState = -1;
            }
        } else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
            mRotate = extra;
            if (mTextureView != null)
                mTextureView.setRotation(mRotate);
        }
    }

    /**
     * 重载点击
     * @param v
     */
    public void onClick(View v) {

        int i = v.getId();
        //限制错误状态下的点击
        if (i == com.shuyu.gsyvideoplayer.R.id.surface_container && mCurrentState == CURRENT_STATE_ERROR){

        }else{
            super.onClick(v);
        }

    }

    /**
     * 获取当前进度条位置
     * @return
     */
    public int getCurrentPosition(){
        int position = 0;

        try {
            position = (int) GSYVideoManager.instance().getMediaPlayer().getCurrentPosition();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return position;
        }

        return position;
    }

    /***
     * 重写拖动进度条监听的
     * onStopTrackingTouch 方法
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        int end =getCurrentPosition();
        int time =0;
        if (GSYVideoManager.instance().getMediaPlayer() != null && mHadPlay) {
            try {
                time = seekBar.getProgress() * getDuration() / 100;
                GSYVideoManager.instance().getMediaPlayer().seekTo(time);
            } catch (Exception e) {
                Debuger.printfWarning(e.toString());
            }
        }
        if (mVideoAllCallBack != null && isCurrentMediaListener()) {
            if (isIfCurrentIsFullscreen()) {
                Debuger.printfLog("onClickSeekbarFullscreen");
                mVideoAllCallBack.onClickSeekbarFullscreen(mOriginUrl, mTitle, this ,end,time);
            } else {
                Debuger.printfLog("onClickSeekbar");
                mVideoAllCallBack.onClickSeekbar(mOriginUrl, mTitle, this ,end,time);
            }
        }
    }

    /**
     * 设置进度条能否拖动
     * @param flag
     */
    public void setSeekbarCanDrag(boolean flag){
        mProgressBar.setEnabled(flag);
    }

    /**
     * 用于进度页面下的seekbar改变进度使用
     * @param mCurrentPosition
     */
    public void setCurrentProgress(int mCurrentPosition) {

        int end =getCurrentPosition();
        int time =0;
        if (GSYVideoManager.instance().getMediaPlayer() != null && mHadPlay) {
            try {
                time = mCurrentPosition*1000;
                GSYVideoManager.instance().getMediaPlayer().seekTo(time);
            } catch (Exception e) {
                Debuger.printfWarning(e.toString());
            }
        }
        if (mVideoAllCallBack != null && isCurrentMediaListener()) {
            if (isIfCurrentIsFullscreen()) {
                Debuger.printfLog("onClickSeekbarFullscreen");
                mVideoAllCallBack.onClickSeekbarFullscreen(mOriginUrl, mTitle, this ,end,time);
            } else {
                Debuger.printfLog("onClickSeekbar");
                mVideoAllCallBack.onClickSeekbar(mOriginUrl, mTitle, this ,end,time);
            }
        }

    }

    /**
     * 设置图片广告
     */
    public void setADImage(String url){
        this.url =url;
    }

    /**
     * 显示图片广告
     */
    public void showAdImage(){
        if (!url.equals("")){
            try{
                CommonUtils.loadIntoUseFitWidth(context,url,0,mADImageView,"");
            }catch(IllegalArgumentException e){
                //java.lang.IllegalArgumentException: You cannot start a load for a destroyed activity
            }

        }
        setViewShowState(mAdLayout, VISIBLE);

    }

    /**
     * 隐藏图片广告
     */
    public void hideAdImage(){

        setViewShowState(mAdLayout, INVISIBLE);
    }

  /*  *//**
     * 加载广告
     * @param context
     * @param imageUrl
     * @param errorImageId
     * @param imageView
     *//*
    public void loadIntoUseFitWidth(Context context, final String imageUrl, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .placeholder(errorImageId)
                .error(errorImageId)
                .into(imageView);
    }*/

}
