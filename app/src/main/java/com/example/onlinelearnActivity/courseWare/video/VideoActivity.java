package com.example.onlinelearnActivity.courseWare.video;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.Fragment.TopicFragment;
import com.example.Fragment.video.NoticeFragment;
import com.example.Fragment.video.ProgressFragment;
import com.example.adapter.BaseFragmentAdapter;
import com.example.jdbc.Dao.FileDAO;
import com.example.jdbc.Dao.VideoRecordDAO;
import com.example.jdbc.Dao.impl.FileDAOImpl;
import com.example.jdbc.Dao.impl.VideoRecordDAOImpl;
import com.example.util.NetWork;
import com.example.entity.FileInfo;
import com.example.entity.VideoRecordModel;
import com.example.jsonReturn.AdvertisementPathReturn;
import com.example.jsonReturn.RideoFlagReturn;
import com.example.jsonReturn.VideoRecordReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.BaseActivity;
import com.example.util.FileUtil;
import com.example.util.FinalStr;
import com.example.util.JsonUtil;
import com.example.util.SpUtils;
import com.example.view.FlycoTabLayout.SlidingTabLayout;
import com.example.view.dialog.MaterialDialog;
import com.example.view.videoView.LandLayoutVideo;
import com.example.view.videoView.SampleListener;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 视频页面
 *
 * 关于学习记录:
 * 每次打开视频取本地的总汇，退出视频提交明细获取服务器的统计，并且统计到现有的总汇中
 */
public class VideoActivity extends BaseActivity implements View.OnClickListener{

    protected String TAG = "VideoActivity";
    private final int GET_VIDEO_CORVER =0;//获取网络视频封面
    private final int AD_PLAY_CALLBACK =1;//广告播放回调

    @BindView(R.id.video_tl)
    SlidingTabLayout video_tl;//tabbars
    @BindView(R.id.video_vp)
    ViewPager video_vp;//fragment切换页
    @BindView(R.id.video_player)
    LandLayoutVideo detailPlayer;//播放view
    @BindView(R.id.appBar)
    AppBarLayout appBarLayout;//appbar

    private String coursewareId ="";//课件id
    private String chapterId =""; //章节Id
    private String behaviorId ="";//学习行为Id
    private String classId ="";//课程Id
    private String accountId ="";//用户Id
    private String accountName ="";//用户名
    private String showSchedule ;//是否显示进度
    private String showNotice;//是否显示需知
    private String showEvaluate;//是否显示评价
    private String showAsk;//是否显示问答
    private String titleName;//标题名字

    protected FileDAO fileDao = null;
    protected VideoRecordDAO mVideoRecordDAO = null;

    private List<Fragment> mFragments = new ArrayList<>();
    private NoticeFragment noticeFragment;//需知fragment
    private ProgressFragment progressFragment ;//进度fragment
    private TopicFragment questionFragment;//问答fragment
    private NoticeFragment commentFragment;//评价fragment

    private AppBarLayout.LayoutParams lp;//appbar类型的属性

    private String urls = null; //视频url,不能为空
    private String fileName; //文件名字
    private boolean isPlay;//是否正在播放
    private boolean isPause;//是否暂停
    private OrientationUtils orientationUtils;//旋转辅助工具
    private Bitmap videoCover;//视频封面

    private RideoFlagReturn rideoReturn;//视频信息返回
    private int videoLastEndPoint =0;//上一次视频结束位置
    private int errorTimeRecord =0;//错误状态下的开始时间

    private Timer getCurrentPointTimer =null ;//获取当前位置定时器
    private TimerTask getCurrentPointTask;

    private Timer adPlayTimer =null;//广告计时定时器
    private TimerTask adPlayTask =null;

    private int videoStartPoint =0;//学习行为开始位置
    private int videoEndPonit=0;//学习行为结束位置
    private int currentPoint =0;//视频当前位置

    public static boolean isAdPlay =true;//现在是否在播放广告，默认是
    private int adLength;//广告总长度
    private String adPhoto="";
    private boolean isAdPhotoPlay =false;

    private boolean isHide =false;//是否视频被遮盖
    private boolean isInit =true;//是否第一次进入onresume方法

    private MaterialDialog mobileConnectDialog;
    private int videoTotalTime =0;//视频总时长
    private boolean firstOpen =true;
    private boolean isStop2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

    }

    /**
     * 全局信息初始化
     */
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);

        //下载页面跳转过来的
        if (null != getIntent().getStringExtra("behaviorId")){
            behaviorId =getIntent().getStringExtra("behaviorId");
            hdaplication.setBehaviorId(behaviorId);
        }else{//
            behaviorId =hdaplication.getBehaviorId();
        }
        coursewareId =hdaplication.getCoursewareId();
        classId =hdaplication.getClassId();
        accountId =hdaplication.getStuid();
        accountName =hdaplication.getUserXm();
        chapterId =(String) getIntent().getExtras().get("chapterId");
        showSchedule =(String) getIntent().getExtras().get("showSchedule");
        showNotice =(String) getIntent().getExtras().get("showNotice");
        showEvaluate =(String) getIntent().getExtras().get("showEvaluate");
        showAsk =(String) getIntent().getExtras().get("showAsk");
        titleName =(String)getIntent().getExtras().get("fileName");
        Log.d(TAG,coursewareId+" "+behaviorId+" "+classId+" "+accountId+" "+accountName+" "+chapterId);
        Log.d(TAG,showSchedule+" "+showNotice+" "+showEvaluate+" "+showAsk+" ");

    }

    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        setActivityLeftTitle(titleName);
        setActivityLeftTitleShow(true);
        setToolbar_backShow(true);
    }
    /**
     * 初始化
     */
    public void initViews(final View view){

        fileDao =new FileDAOImpl(this);
        mVideoRecordDAO =new VideoRecordDAOImpl(this);

        isAdPlay =true;

        //获取视频view的layout属性
        lp =( AppBarLayout.LayoutParams)detailPlayer.getLayoutParams();

        //设置全屏拉伸
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);


        //渲染viewpager
        setupViewPager();

        /**
         * 降低视频倍数
         */
        VideoOptionModel videoOptionModel =
                new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 50);
        //解决帧数问题
        VideoOptionModel videoOptionModel2 =
                new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        List<VideoOptionModel> list = new ArrayList<>();
        list.add(videoOptionModel);
        list.add(videoOptionModel2);
        GSYVideoManager.instance().setOptionModelList(list);


        //设置视频不能隐藏
        setVideoViewCanHide(false);
    }

    @Override
    protected void doBusiness(Context mContext) {

        //如果没有本地视频
        if (!isExistLocalVideo()){
            if (!NetWork.isNetworkConnected(this)){  //如果没有网络
                showMessageDialog("没有离线该视频且当前不在网络状态，请检查网络连接");
                return;
            }
            else if (!NetWork.isWifiConnected(this)){   //如果不是wifi
                //弹框
                if (!showMobileConnectDialog()){    //如果不用移动网络播放
                    return;
                }
            }
            //获取在线播放地址
            showCourseThead();
        }else{
            //播放广告
            showAdThead();
        }

//        if(FinalStr.SYSTEM_CODE == "ZXXX"){

//        }

    }

    /**
     * 是否有本地视频
     */
    private boolean isExistLocalVideo(){
        FileInfo fileInfo =fileDao.querySingleFile(accountId,behaviorId);
        //如果数据库里有文件信息 && 已经下载完成
        if (null !=fileInfo && 2 ==fileInfo.getProgress_status()){  //完成状态
            File file = FileUtil.isExist(FinalStr.DOWNLOAD_PATH,fileInfo.getAttach_name());

            if (null!= file){ //如果文件存在的话
                urls = file.getAbsolutePath();
                fileName =titleName;
                videoTotalTime =fileInfo.getFile_time();
                return true;

            }
        }
        return false;
    }

    /**
     * 设置ViewPager需要加载的Fragment
     */
    private void setupViewPager() {

        List<String> fragmentTitles =new ArrayList<>();
        mFragments = new ArrayList<>();


        if ("Y".equals(showNotice)){
            noticeFragment =new NoticeFragment();
            mFragments.add(noticeFragment);
            fragmentTitles.add("  需知  ");
        }
        if ("Y".equals(showSchedule)){
            progressFragment =new ProgressFragment();
            mFragments.add(progressFragment);
            fragmentTitles.add("  进度  ");
        }
        if ("Y".equals(showAsk)){
            questionFragment =new TopicFragment();
            mFragments.add(questionFragment);
            fragmentTitles.add("  问答  ");
        }
        if ("Y".equals(showEvaluate)){
            commentFragment =new NoticeFragment();
            mFragments.add(commentFragment);
            fragmentTitles.add("  评价  ");
        }
        if (mFragments.size()==1){
            video_tl.setIndicatorColor(Color.TRANSPARENT);
        }
        // 为ViewPager设置适配器
        BaseFragmentAdapter adapter =
                new BaseFragmentAdapter(getSupportFragmentManager(), mFragments
                        , (String[])fragmentTitles.toArray(new String[fragmentTitles.size()]));

        video_vp.setAdapter(adapter);
        //将ViewPager与TableLayout 绑定在一起
        video_tl.setViewPager(video_vp);

    }

    /**
     * 加载视频
     * @param url 视频url
     * @param resourceName 视频名称
     */
    private void loadRideo(final String url, String resourceName){


        //开启一个线程获取网络视频
        new Thread(new Runnable() {
            @Override
            public void run() {
                //增加封面
                videoCover =getNetVideoBitmap(url);
                if (videoCover !=null){
                    sendMessage(GET_VIDEO_CORVER,null);
                }

            }
        }).start();

        //重置视频界面
        resolveNormalVideoUI(resourceName);

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, detailPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        detailPlayer.setADImage(adPhoto);

        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setUrl("https://cr.scutde.net/t29courses/shzdjjx/video/v010100.mp4")
                .setCacheWithPlay(false)
                .setVideoTitle(resourceName)
                .setStandardVideoAllCallBack(sampleListener)
                .build(detailPlayer);

        if (!isAdPlay){
            //设置开始位置
            getStartPosition();
            detailPlayer.setSeekOnStart(videoLastEndPoint*1000);
        }
        //如果是在全屏状态下setup
        if (detailPlayer.isIfCurrentIsFullscreen()){
            orientationUtils.setIsLand(1);
        }

        //设置不能拖动
        detailPlayer.setSeekbarCanDrag(false);


        //开始播放视频
        detailPlayer.startPlayLogic();

    }

    /**
     * 增加视频封面
     */
    private void addVideoCover(){
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(videoCover);
        detailPlayer.setThumbImageView(imageView);
    }

    /**
     *  服务器返回url，通过url去获取视频的第一帧
     *  Android 原生给我们提供了一个MediaMetadataRetriever类
     *  提供了获取url视频第一帧的方法,返回Bitmap对象
     *
     *  @param videoUrl
     *  @return
     */
    public Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    /**
     * 重置视频界面
     */
    private void resolveNormalVideoUI(String resourceName) {
        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        detailPlayer.getTitleTextView().setText(resourceName);
        detailPlayer.getBackButton().setVisibility(View.GONE);
    }


    /**
     * 重写全屏时的返回键
     */
    @Override
    public void onBackPressed() {

        //如果video全屏则退出
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        finish();


    }

    /**
     * 重写 onConfigurationChanged
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
                if (!detailPlayer.isIfCurrentIsFullscreen()) {
                    detailPlayer.startWindowFullscreen(VideoActivity.this, true, true);
                }
            } else {
                //新版本isIfCurrentIsFullscreen的标志位内部提前设置了，所以不会和手动点击冲突
                if (detailPlayer.isIfCurrentIsFullscreen()) {
                    StandardGSYVideoPlayer.backFromWindowFull(this);
                }
                if (orientationUtils != null) {
                    orientationUtils.setEnable(true);
                }
            }
        }
    }

    /**
     * 视频操作listener
     */
    private SampleListener sampleListener =new SampleListener(){
        //加载成功
        public void onPrepared(String url, Object... objects) {
            super.onPrepared(url,objects);
            Log.d(TAG,"----加载成功----");
            isInit =false;
            //打开外部旋转
            orientationUtils.setEnable(true);
            isPlay = true;
            isStop2 = false;
            //如果当前是后台状态且刚刚视频缓冲完毕
            if (isHide){
                detailPlayer.onVideoPause();
                return;
            }
            //如果是广告的话
            if (isAdPlay){
                adLength = detailPlayer.getDuration();
                //开启广告计时
                startAdPlayTimer();
                return;
            }

            //开启获取当前位置的定时器
            startGetCurrentPointTimer();

            //progressFragment下的seekbar可以使用
            progressFragment.setIsAdMode(false);

            if (firstOpen ==true){
                //记录start位置
                videoStartPoint =videoLastEndPoint*1000;
                Log.d(TAG,"onPrepared:--videoStartPoint--"+ videoStartPoint +" --videoEndPoint--" + videoEndPonit);
                firstOpen =false;
            }


            detailPlayer.setSeekbarCanDrag(true);


        }

        //点击了开始按键播放
        public void onClickStartIcon(String url, Object... objects) {
            super.onClickStartIcon(url,objects);
            detailPlayer = (LandLayoutVideo) objects[1];
            Log.d(TAG,"----点击了开始按键播放----");

            //视频状态-->播放：不可隐藏
            setVideoViewCanHide(false);
            isStop2 = false;
        }

        //点击了错误状态下的开始按键
        public void onClickStartError(String url, Object... objects) {
            super.onClickStartError(url,objects);
            Log.d(TAG,"----点击了错误状态下的开始按键----");

            detailPlayer.setSeekOnStart(errorTimeRecord);

            errorTimeRecord=0;
            //视频状态-->播放：不可隐藏
            setVideoViewCanHide(false);
            isStop2 = false;

        }

        //点击了播放状态下的开始按键--->停止
        public void onClickStop(String url, Object... objects) {
            super.onClickStop(url,objects);
            Log.d(TAG,"----点击了播放状态下停止----");

            //视频状态-->停止：隐藏
            setVideoViewCanHide(true);

            setEndAndPost();

            if (FinalStr.SYSTEM_CODE.equals("GZSPAQ")){
                detailPlayer.showAdImage();
                isAdPhotoPlay =true;
            }
            isStop2 = true;
        }

        //点击了全屏播放状态下的开始按键--->停止
        public void onClickStopFullscreen(String url, Object... objects) {
            super.onClickStopFullscreen(url,objects);
            Log.d(TAG,"----点击了全屏播放状态下停止----");

            setEndAndPost();
            if (FinalStr.SYSTEM_CODE.equals("GZSPAQ")){
                detailPlayer.showAdImage();
                isAdPhotoPlay =true;
            }
            isStop2 = true;
        }

        //点击了暂停状态下的开始按键--->播放
        public void onClickResume(String url, Object... objects) {
            super.onClickResume(url,objects);
            Log.d(TAG,"----点击了暂停状态下播放----");

            //视频状态-->播放：不可隐藏
            setVideoViewCanHide(false);

            //记录start位置
            videoStartPoint =currentPoint;
            detailPlayer.hideAdImage();
            isAdPhotoPlay =false;

            isStop2 = false;
        }

        //点击了全屏暂停状态下的开始按键--->播放
        public void onClickResumeFullscreen(String url, Object... objects) {
            super.onClickResumeFullscreen(url,objects);
            Log.d(TAG,"----点击了全屏暂停状态下播放----");

            //记录start位置
            videoStartPoint =currentPoint;
            detailPlayer.hideAdImage();
            isAdPhotoPlay =false;
            isStop2 = false;
        }

        //点击了空白弹出seekbar
        public void onClickSeekbar(String url, Object... objects) {
            super.onClickSeekbar(url,objects);
            Log.d(TAG,"----点击了空白弹出seekbar----" );

            //记录end位置
            videoEndPonit = Integer.parseInt(String.valueOf(objects[2]));
            Log.d(TAG,"点击了空白弹出seekbar--videoStartPoint--"+ videoStartPoint +" --videoEndPoint--" + videoEndPonit);
            if (!isStop2){
                //传入明细
                insertDetailRecord();
            }

            videoStartPoint = Integer.parseInt(String.valueOf(objects[3]));
            Log.d(TAG, "init_startpoint" + videoStartPoint);

        }

        //点击了全屏的seekbar
        public void onClickSeekbarFullscreen(String url, Object... objects) {
            super.onClickSeekbarFullscreen(url,objects);
            Log.d(TAG,"----点击了全屏的seekbar----");

            //记录end位置
            videoEndPonit = Integer.parseInt(String.valueOf(objects[2]));
            Log.d(TAG,"onClickSeekbarFullscreen--videoStartPoint--"+ videoStartPoint +" --videoEndPoint--" + videoEndPonit);
            if (!isStop2){
                //传入明细
                insertDetailRecord();
            }


            videoStartPoint = Integer.parseInt(String.valueOf(objects[3]));
            Log.d(TAG, "init_startpoint" + videoStartPoint);
        }


        //播放完了
        public void onAutoComplete(String url, Object... objects) {
            super.onAutoComplete(url,objects);
            Log.d(TAG,"----播放完了----");

            if (!detailPlayer.isIfCurrentIsFullscreen()){
                //视频状态-->停止：隐藏
                setVideoViewCanHide(true);
            }

            detailPlayer.setSeekbarCanDrag(false);

            //记录end位置
            videoEndPonit = videoTotalTime *1000;
            Log.d(TAG,"播放完了--videoStartPoint--"+ videoStartPoint +" --videoEndPoint--" + videoEndPonit);
            //传入明细
            insertDetailRecord();

            //停止定时器
            stopGetCurrentPointTimer();

            isPlay =false;
            videoStartPoint=0;
            videoEndPonit =0;
            currentPoint =0;

        }

        @Override
        public void onEnterFullscreen(String url, Object... objects) {
            super.onEnterFullscreen(url,objects);
            Log.d(TAG,"----进入全屏----");

            detailPlayer = (LandLayoutVideo) objects[1];
            detailPlayer.setADImage(adPhoto);

            if (isAdPhotoPlay ==true){
                detailPlayer.showAdImage();
            }else{
                detailPlayer.hideAdImage();
            }
            if(isPlay){
                detailPlayer.setSeekbarCanDrag(true);
            }else{
                detailPlayer.setSeekbarCanDrag(false);
            }
        }

        @Override
        public void onQuitFullscreen(String url, Object... objects) {
            super.onQuitFullscreen(url,objects);
            Log.d(TAG,"----退出全屏----");
            detailPlayer = (LandLayoutVideo) objects[1];
            detailPlayer.setADImage(adPhoto);

            if (isAdPhotoPlay ==true){
                detailPlayer.showAdImage();
            }else{
                detailPlayer.hideAdImage();
            }
            if(isPlay){
                detailPlayer.setSeekbarCanDrag(true);
            }else{
                detailPlayer.setSeekbarCanDrag(false);
            }

            if (orientationUtils != null) {
                orientationUtils.backToProtVideo();
            }
        }

        //触摸调整进度
        public void onTouchScreenSeekPosition(String url, Object... objects) {
            super.onTouchScreenSeekPosition(url,objects);
            Log.d(TAG,"----触摸调整进度----");

            if (!isStop2){
                setEndAndPost();
            }


            //记录start(拖拉会出现负数)
            videoStartPoint =detailPlayer.getCurrentPosition()< 0 ? 0:detailPlayer.getCurrentPosition();

        }

        @Override
        public void onPlayError(String url, Object... objects) {
            super.onPlayError(url,objects);
            //视频加载错误
            Log.d(TAG,"----视频加载错误----");
            if (errorTimeRecord==0){
                errorTimeRecord =detailPlayer.getCurrentPosition();
            }
            Log.d(TAG,errorTimeRecord+"");
            if (isAdPlay){
                stopAdPlayTimer();//停止广告计时
                //播放学习视频
                loadRideo(urls,fileName);
                isAdPlay =false;
                //隐藏广告时间
                detailPlayer.showAdTextView(false);
            }
            setEndAndPost();
        }
    };

    /**
     * //改变进度
     * @param position
     */
    public void seekBarChangePosition(int position) {

        detailPlayer.setCurrentProgress(position);
    }

    /**
     * 设置监听/回调
     */
    public void setListener(){

        //播放器全屏按钮监听
        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null !=orientationUtils){
                    //直接横屏
                    orientationUtils.resolveByClick();
                    //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                    detailPlayer.startWindowFullscreen(VideoActivity.this, true, true);
                }
            }
        });


        //锁屏按钮监听
        detailPlayer.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });


        //viewpager切换监听
        video_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                switch(position) {
                    case 0:
                        break;
                    case 1:
                        //获取视频进度总汇
                        progressFragment.initVideoTotalSpeed();
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

    }



    @OnClick({R.id.toolbar_back})
    public void onClick(View view) {
        switch (view.getId()) {
            //返回键操作
            case R.id.toolbar_back:
                finish();
                break;
        }
    }

    /**
     * 获取视频Thread
     */

    public void showCourseThead(){
        //调用接口方法
        Call<ResponseBody> call = serverApi.getFileMessage(accountId,classId,behaviorId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求视频信息成功
                    try {
                        String result =response.body().string();
                        Log.d(JSON_TAG,"获取视频信息："+result);
                        //gson解析
                        rideoReturn = JsonUtil.parserString(result,RideoFlagReturn.class);
                        if (rideoReturn.getResourceWebPath().contains("http")){

                            urls = rideoReturn.getResourceWebPath();
                        }else{

                            urls =rideoReturn.getResourceIPAddress() + rideoReturn.getResourceWebPath();
                        }
                        fileName =rideoReturn.getResourceName();
                        videoTotalTime =Integer.parseInt(rideoReturn.getVideoTotalTime());

                        //播放广告
                        showAdThead();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{ //请求视频信息失败

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //服务器请求失败
            }
        });
    }

    /**
     * 获取视频Thread
     */

    public void showAdThead(){
        //调用接口方法
        Call<ResponseBody> call = serverApi.getAdvertisementPath(behaviorId,classId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求视频信息成功
                    try {
                        String result  =response.body().string();
                        Log.d(JSON_TAG,"获取广告信息："+result);
                        final AdvertisementPathReturn adPathReturn = JsonUtil.parserString(result,AdvertisementPathReturn.class);
                        if(adPathReturn.isHasAd()){

                            adPhoto =adPathReturn.getPhotoAdPath();
                            detailPlayer.setADImage(adPhoto);
                            detailPlayer.showAdImage();
                            //延迟5秒关闭
                            new Handler().postDelayed(new Runnable(){
                                public void run() {
                                    detailPlayer.hideAdImage();
                                    //加载广告
                                    loadRideo(adPathReturn.getVideoAdPath(),"");
                                }
                            }, 5000);
                        }else{
                            isAdPlay =false;
                            loadRideo(urls,fileName);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //服务器请求失败
                isAdPlay =false;
                loadRideo(urls,fileName);
            }
        });
    }


    /**
     * 将学习明细传入数据库
     */
    public void insertDetailRecord(){

        //转为秒数
        int finalVideoStartPoint =videoStartPoint/1000;
        int finalVideoEndPonit =videoEndPonit/1000;

        Log.d(TAG,"------CUNRRENT------："+finalVideoStartPoint+"   "+finalVideoEndPonit+" ");

        //如果end位置=0 ||开始位置<0 ||结束位置<0  则舍弃数据
        if (finalVideoEndPonit==0||finalVideoStartPoint<0||finalVideoEndPonit<0
                ||finalVideoEndPonit<finalVideoStartPoint||finalVideoStartPoint==finalVideoEndPonit){

            return;
        }
        //如果大于视频总长，则等于视频总长
        if (finalVideoEndPonit>videoTotalTime){
            finalVideoEndPonit =videoTotalTime;
        }

        //插入数据
        VideoRecordModel videoRecord = new VideoRecordModel(accountId,accountName, classId, behaviorId,videoTotalTime,finalVideoStartPoint,finalVideoEndPonit);

        //如果插入成功则统计
        if (mVideoRecordDAO.insertDetailRecord(videoRecord)>0){
            //统计
            countRecord(videoRecord.getStartPoint(),videoRecord.getEndPoint());
        }

    }

    /**
     * 统计该明细
     * @param start
     * @param end
     */
    private void countRecord(int start,int end){

        //判断是否存在于某集合中
        VideoRecordModel startRecord = mVideoRecordDAO.isExistInCollection(start,classId,behaviorId,accountId,null);
        VideoRecordModel endRecord = mVideoRecordDAO.isExistInCollection(end,classId,behaviorId,accountId,null);

        //最终要纳入总汇的开始点及结束点
        int totalStart;
        int totalEnd;
        if (startRecord !=null && endRecord!=null){ //两个点都存在于集合当中

            totalStart =startRecord.getStartPoint();
            totalEnd =endRecord.getEndPoint();

        }
        else if (startRecord !=null && endRecord ==null){ //开始点存在集合中，结束点不存在

            totalStart =startRecord.getStartPoint();
            totalEnd =end;

        }else if (startRecord ==null && endRecord !=null){  //开始点不存在集合中，结束点存在

            totalStart =start;
            totalEnd =endRecord.getEndPoint();

        }else{  //如果两个点都不存在于集合当中

            totalStart =start;
            totalEnd =end;

        }
        //插入数据
        VideoRecordModel totalRecord = new VideoRecordModel(accountId,accountName, classId, behaviorId, videoTotalTime, totalStart, totalEnd);
        mVideoRecordDAO.insertTotalRecord(totalRecord);
        List<VideoRecordModel> list =mVideoRecordDAO.queryTotalRecordSingle(classId,behaviorId,accountId);

    }


    @Override
    protected void handler(Message msg) {
        switch (msg.what) {

            //获取封面回调
            case GET_VIDEO_CORVER:
                //设置封面
                addVideoCover();
                break;

            case AD_PLAY_CALLBACK:
                detailPlayer.getCurrentPosition();
                Log.d(TAG,"广告总长度："+ adLength +" 现播放长度:"+detailPlayer.getCurrentPositionWhenPlaying()+" ");
                if (detailPlayer.getCurrentPositionWhenPlaying()!=0)
                    detailPlayer.setADTime((adLength -detailPlayer.getCurrentPositionWhenPlaying())/1000);

                //广告播放完毕
                if (adLength -detailPlayer.getCurrentPositionWhenPlaying()<=0
                        || (adLength -detailPlayer.getCurrentPositionWhenPlaying())/1000 ==0){
                    isAdPlay =false;
                    detailPlayer.setADTime(0);
                    //停止计时
                    stopAdPlayTimer();
                    detailPlayer.onCompletion();
                    //隐藏广告时间
                    detailPlayer.showAdTextView(false);
                    //播放学习视频
                    loadRideo(urls,fileName);
                }
                break;

        }
    }


    /**
     * 开启获取当前位置的定时器
     */
    private void startGetCurrentPointTimer(){
        //如果定时器为空
        if (getCurrentPointTimer==null){

            getCurrentPointTimer =new Timer();
            getCurrentPointTask = new TimerTask() {
                @Override
                public void run() {

                    //更新当前位置

                    if (detailPlayer.getCurrentPosition()<0){
                        return;
                    }
                    currentPoint = detailPlayer.getCurrentPosition();
                    if (onChangeVieoListener!=null){
                        onChangeVieoListener.SampleListener(currentPoint);
                    }
                    Log.d(TAG, "currentPoint:" + currentPoint+" currentStatus"+detailPlayer.getCurrentState());

                }
            };
            //每秒执行
            getCurrentPointTimer.schedule(getCurrentPointTask, 0, 1000);
        }

    }

    /**
     * 停止获取当前位置的定时器
     */
    private void stopGetCurrentPointTimer(){

        if (getCurrentPointTimer != null) {
            getCurrentPointTimer.cancel();
            getCurrentPointTimer = null;
        }
        if (getCurrentPointTask != null) {
            getCurrentPointTask.cancel();
            getCurrentPointTask = null;
        }
    }


    /**
     * 开始广告计时的定时器
     */
    private void startAdPlayTimer(){

        //如果定时器为空
        if (adPlayTimer ==null){

            adPlayTimer =new Timer();
            adPlayTask = new TimerTask() {
                @Override
                public void run() {

                    sendMessage(AD_PLAY_CALLBACK,null);
                }
            };

            adPlayTimer.schedule(adPlayTask,0,1000);
        }
    }

    /**
     * 停止广告计时的定时器
     */
    private void stopAdPlayTimer(){
        if (adPlayTimer != null) {
            adPlayTimer.cancel();
            adPlayTimer = null;
        }
        if (adPlayTask != null) {
            adPlayTask.cancel();
            adPlayTask = null;
        }
    }
    /**
     * 设置视频能否隐藏
     * @param flag
     */
    private void setVideoViewCanHide(boolean flag){
        if (!detailPlayer.isIfCurrentIsFullscreen()){   //不是全屏状态下才能执行
            if (flag){
                //可以隐藏
                lp.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                        |AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                detailPlayer.setLayoutParams(lp);
            }else{
                //不可隐藏
                lp.setScrollFlags(0);
                detailPlayer.setLayoutParams(lp);
            }
        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        isHide =true;
        detailPlayer.onVideoPause();
        if (isAdPlay){
            stopAdPlayTimer();
        }
        isPause = true;
        if(isStop2)
            return;
        Log.d(TAG,"onPause--videoStartPoint--"+ videoStartPoint +" --videoEndPoint--" + videoEndPonit);

        setEndAndPost();
    }

    @Override
    protected void onResume() {
        super.onResume();

        isPause = false;
        //如果当前在播放广告就重新播放
        if (isAdPlay&&!isInit){
            startAdPlayTimer();
            detailPlayer.onVideoResume();
        }

        isHide =false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        GSYVideoPlayer.releaseAllVideos();

        if (orientationUtils != null)
            orientationUtils.releaseListener();

        stopGetCurrentPointTimer();

        stopAdPlayTimer();
        //上传学习行为
        getVideoRecord();

    }


    public void setEndAndPost(){
        //记录end位置
        videoEndPonit = currentPoint;
        Log.d(TAG,"记录end位置--videoStartPoint--"+ videoStartPoint +" --videoEndPoint--" + videoEndPonit);
        //传入明细
        insertDetailRecord();
    }

    /**
     * 设置当前的开始点
     * 用于刷新进度
     */
    public void setVideoStartPoint(){
        videoStartPoint = videoEndPonit;
    }


    /**
     * 显示提示移动网络Dialog
     */
    private boolean showMobileConnectDialog(){
        final boolean[] isSure = {false};
        if (mobileConnectDialog ==null){
            mobileConnectDialog = new MaterialDialog(VideoActivity.this)
                    .setCanceledOnTouchOutside(false)
                    .setTitle("提示")
                    .setMessage("你没有连接wifi，在线播放将会消耗流量，是否在线播放")
                    .setPositiveButton("下载", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mobileConnectDialog.dismiss();
                            isSure[0] =true;
                        }
                    })
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mobileConnectDialog.dismiss();
                            isSure[0] =false;
                        }
                    });
        }
        mobileConnectDialog.show();
        return isSure[0];
    }


    int postTime =0; //提交次数
    private void getVideoRecord(){
        //获取数据库所有明细
        List<VideoRecordModel> videoRecordModels =mVideoRecordDAO.queryDetailRecordSingle(classId,behaviorId,accountId);
        List<VideoRecordModel> temp =null;

        int itemCount =100;
        if (videoRecordModels.size()/itemCount ==0){ //如果条数不足100
            temp = videoRecordModels.subList(0,videoRecordModels.size());
            offLineSaveLearningTraces(0,temp,"Y");

        }else {  //是100条的倍数
            postTime =videoRecordModels.size()/itemCount;
            for (int i=0;i<postTime;i++){
                temp =videoRecordModels.subList(i*itemCount,(i+1)*itemCount);


                if (i == postTime-1 && videoRecordModels.size()%itemCount==0){   //如果当前为最后一次且没有余数
                    offLineSaveLearningTraces(i,temp,"Y");
                    break;
                }
                offLineSaveLearningTraces(i,temp,"N");
            }
            if (videoRecordModels.size()%itemCount!=0){   //还有数据
                temp =videoRecordModels.subList(postTime*itemCount,videoRecordModels.size());
                offLineSaveLearningTraces(postTime,temp,"Y");
            }

        }

    }

    /**
     * 提交学习行为
     * @param num
     * @param temp
     * @param isLast
     */
    private void offLineSaveLearningTraces(final int num, final List<VideoRecordModel> temp, final String isLast){
        //如果并没有学习行为的话,则不执行提交
        Log.d(JSON_TAG,"获取提交视频明细:"+"temp:"+JsonUtil.BuildJson(temp)+"========isLast:"+isLast+"=====accountId:"+accountId+"======classId:"+classId+"=====behaviorId:"+behaviorId);
        if (temp.size()==0){
            return;
        }
//        Log.d(JSON_TAG,JsonUtil.BuildJson(temp));
        //调用接口方法
        Call<ResponseBody> call = serverApi.offLineSaveLearningTraces(JsonUtil.BuildJson(temp),isLast,accountId,classId,behaviorId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求成功
                    try {
                        String result =response.body().string();

                        Log.d(JSON_TAG,"获取提交视频明细返回:"+result);
                        //gson解析
                        VideoRecordReturn videoRecordReturn = JsonUtil.parserString(result,VideoRecordReturn.class);

                        if (videoRecordReturn.isSuccess()){
                            //删除明细表里面的数据
                            mVideoRecordDAO.deleteDetailRecords(temp);
                            List<VideoRecordModel> videoRecordModels= mVideoRecordDAO.queryDetailRecordSingle(classId,behaviorId,accountId);
                            Log.d(TAG,"明细表剩余条数:"+videoRecordModels.size()+"");

                            //如果是最后一次返回
                            if ("Y".equals(isLast)){
                                for(VideoRecordModel videoRecordModel:videoRecordReturn.getsDVideoSectionEntity()){

//                                    countRecord(videoRecordModel.getStartPoint(),videoRecordModel.getEndPoint());
                                }

                                /*//获取现有的behavior数据
                                List<VideoRecordModel> videoRecordTotals= mVideoRecordDAO.queryTotalRecordSingle(classId,behaviorId,accountId);
                                //批量插入服务器的汇总并判断是否成功
                                if (videoRecordReturn.getObj().size()!=0 &&mVideoRecordDAO.insertRecordTotals(videoRecordReturn.getObj())){    //如果服务器返回的总汇不为空，就先插入后删除防止插入失败
                                    //删除总汇表对应的behavior数据
                                    mVideoRecordDAO.deleteTotalRecords(videoRecordTotals);
                                    Log.d("AA","总汇表剩余条数:"+mVideoRecordDAO.queryTotalRecordSingle(classId,behaviorId,accountId).size()+"");
                                    for (VideoRecordModel videoRecordModel:mVideoRecordDAO.queryTotalRecordSingle(classId,behaviorId,accountId)){
                                        Log.d("AA",videoRecordModel.toString());
                                    }
                                }*/
                            }
                        }
                    } catch (IOException e) {
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
     * 获取开始时间
     */
    private void getStartPosition(){
        List<VideoRecordModel> videoRecordModels = mVideoRecordDAO.queryTotalRecordSingle(classId,behaviorId,accountId);
        if (videoRecordModels.isEmpty()){
            videoLastEndPoint=0;
        }else{
            if (videoRecordModels.get(0).getStartPoint()!=0 ||videoRecordModels.get(0).getEndPoint()==videoRecordModels.get(0).getTotalTime()){   //如果第一个元素的开始点!=0
                videoLastEndPoint=0;
            }else{
                videoLastEndPoint=videoRecordModels.get(0).getEndPoint();
            }
        }
    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ;
        outState.putString("behaviorId",behaviorId);
        outState.putString("coursewareId",coursewareId);
        outState.putString("classId",classId);
        outState.putString("accountId",accountId);
        outState.putString("accountName",accountName);
        outState.putString("chapterId",chapterId);


    }

    public String getChapterId() {
        return chapterId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case SUBMIT_TOPIC_SUCCESS:
                String result = data.getExtras().getString("result");
                questionFragment.postQuestionSuccess(result);
                break;
            default:
                break;
        }
    }
    //进度接口回调
    public interface VideoSeekbarChangeListener{
        void SampleListener(int position);
    }
    private VideoSeekbarChangeListener onChangeVieoListener;
    public void setOnChangeListener(VideoSeekbarChangeListener onChangeListener) {
        this.onChangeVieoListener = onChangeListener;
    }
}



