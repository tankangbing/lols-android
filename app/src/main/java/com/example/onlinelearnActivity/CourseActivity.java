package com.example.onlinelearnActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Fragment.FCourseClass;

import com.example.Fragment.FMyIndex;
import com.example.entity.FileInfo;
import com.example.jdbc.Dao.FileDAO;
import com.example.jdbc.Dao.impl.FileDAOImpl;
import com.example.onlinelearn.R;
import com.example.service.DownloadService;
import com.example.util.ApplicationUtil;
import com.example.util.FileUtil;
import com.example.util.FinalStr;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页面
 */
public class CourseActivity extends BaseActivity {
    protected String TAG = "CourseActivity";

    @BindView(R.id.my_class_tab)
    LinearLayout my_class_tab; //课程tab
    @BindView(R.id.my_index_tab)
    LinearLayout my_index_tab; //主页tab
    @BindView(R.id.my_class_tv)
    TextView my_class_tv;//课程tab字符
    @BindView(R.id.my_index_tv)
    TextView my_index_tv;//课程tab图片
    @BindView(R.id.my_class_iv)
    TextView my_class_iv;//主页tab字符
    @BindView(R.id.my_index_iv)
    TextView my_index_iv;//主页tab图片

    //课程fragment
    private FCourseClass fCourseClass=null;
    //我的主页fragment
    private FMyIndex fMyIndex=null;
/*    //接收下载回调
    private DownLoadRecive mRecive;*/
    //退出时间
    private long exitTime = 0;
    //文件对象
    protected FileDAO fileDao = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

    }

    /**
     * 设置toolbar
     */
    protected void setToolbar(){
        super.setToolbar();
        setActivityLeftTitle("我的课程");
        setActivityLeftTitleShow(true);
    }

    /**
     * 初始化
     * @param view
     */
    protected void initViews(View view) {
        fileDao =new FileDAOImpl(this);
      /*  //监听器
        mRecive = new DownLoadRecive();
        IntentFilter intentFilter = new IntentFilter();
        //增加监听内容
        intentFilter.addAction(DownloadService.ACTION_FINISHED);
        registerReceiver(mRecive, intentFilter);*/

        fCourseClass=new FCourseClass();
        fMyIndex=new FMyIndex();

        my_class_iv.setTypeface(iconfont);
        my_index_iv.setTypeface(iconfont);
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();

        if(!fCourseClass.isAdded()){
            ft.add(R.id.course_fl,fCourseClass).commit();
        }else {
            ft.show(fCourseClass).commit();
        }
    }

    @Override
    protected void doBusiness(Context mContext) {
        //检查更新
        isVesionUpdate();
        //开始下载任务
        downloadFile();
    }

    /**
     * 公共按钮操作
     */
    @OnClick({R.id.my_class_tab,R.id.my_index_tab})
    public void onClick(View view) {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        if(fCourseClass!=null){
            if(fCourseClass.isAdded()){
                ft.hide(fCourseClass);
            }
        }

        if(fMyIndex!=null){
            if(fMyIndex.isAdded()){
                ft.hide(fMyIndex);
            }
        }

        switch (view.getId()) {

            case R.id.my_class_tab:

                setActivityLeftTitle("我的课程");
                if(fCourseClass==null){
                    fCourseClass=new FCourseClass();
                }
                if(!fCourseClass.isAdded())
                    ft.add(R.id.course_fl,fCourseClass);
                else{
                    ft.show(fCourseClass);
                }
                my_class_iv.setTextColor(getResources().getColor(R.color.colorPrimary));
                my_class_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                my_index_iv.setTextColor(getResources().getColor(R.color.color_999999));
                my_index_tv.setTextColor(getResources().getColor(R.color.color_999999));
                break;
            case R.id.my_index_tab:

                setActivityLeftTitle("我的主页");
                if(fMyIndex==null){
                    fMyIndex=new FMyIndex();
                }
                if(!fMyIndex.isAdded())
                    ft.add(R.id.course_fl,fMyIndex);
                else{
                    ft.show(fMyIndex);
                }
                my_index_iv.setTextColor(getResources().getColor(R.color.colorPrimary));
                my_index_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                my_class_iv.setTextColor(getResources().getColor(R.color.color_999999));
                my_class_tv.setTextColor(getResources().getColor(R.color.color_999999));

                break;
        }
        ft.commit();
    }

    /**
     * 返回监听
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * [开始下载任务]
     */
    private void downloadFile(){
        List<FileInfo> fileInfos = fileDao.queryFilesInStatus(hdaplication.getStuid());
        for (FileInfo fileInfo :fileInfos){

            if (fileInfo.getProgress_status() ==0 || fileInfo.getProgress_status() ==1){
                fileInfo.setProgress_status(3);//设置文件状态为开始
                fileDao.updateFile(fileInfo);
            }
            //如果没有下载信息
            if ("".equals(fileInfo.getUrl())){
                //获取下载信息
                downloadService.getDownloadMessage(serverApi,hdaplication.getStuid(),fileInfo);
            }else{
                //加入等待队列
                downloadService.prepare(fileInfo);
            }
        }
    }



    /**
     * 退出app
     */
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            hdaplication.exit();
        }
    }

   /* *//**
     * 从DownloadTask中获取广播信息，更新进度条
     *//*
    class DownLoadRecive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //下载完成
            if (DownloadService.ACTION_FINISHED.equals(intent.getAction())){



            }
        }

    }
*/
    /**
     * 下载成功回调
     */
    protected void downloadSuccess(FileInfo fileInfo){
        Log.d(TAG,"下载成功");


        fileInfo.setFile_finish(fileInfo.getFile_size());
        fileInfo.setProgress_status(2);

        File oldFile = FileUtil.isExist(FinalStr.DOWNLOAD_PATH,fileInfo.getAttach_name());
        //更新名字
        if (oldFile !=null){
            fileInfo.setAttach_name(fileInfo.getAttach_name().replaceFirst(".",""));
            File newFile = new File(FinalStr.DOWNLOAD_PATH,fileInfo.getAttach_name());  //重命名去掉点
            oldFile.renameTo(newFile);  //执行重命名
            fileDao.updateFile(fileInfo);//更新数据库
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //退出服务
        Intent intent = new Intent(this, DownloadService.class);
        stopService(intent);
    }

}
