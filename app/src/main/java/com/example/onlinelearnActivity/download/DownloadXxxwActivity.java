package com.example.onlinelearnActivity.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adapter.FileDownloadAdapter;
import com.example.entity.FileInfo;
import com.example.jdbc.Dao.FileDAO;
import com.example.jdbc.Dao.ThreadDAO;
import com.example.jdbc.Dao.impl.FileDAOImpl;
import com.example.jdbc.Dao.impl.ThreadDAOImpl;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.BaseActivity;
import com.example.onlinelearnActivity.courseWare.document.DocumentActivity;
import com.example.onlinelearnActivity.courseWare.video.VideoActivity;
import com.example.service.DownloadService;
import com.example.util.FileUtil;

import com.example.util.FinalStr;
import com.example.view.ListViewCompat;
import com.example.view.dialog.MaterialDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;


public class DownloadXxxwActivity extends BaseActivity{

    protected String TAG = "DownloadXxxwActivity";

    public static final String ACTION_ADD_FILE = "ACTION_ADD_FILE";
    public final int STATUS_STOP = 0;//停止状态
    public final int STATUS_START = 1;//开始状态
    public final int STATUS_FINISH = 2;//完成状态
    public final int STATUS_WAIT = 3;//等待状态
    public final int STATUS_ERROR = 4;//错误状态

    private FileDAO fileDao = null;
    private ThreadDAO mThreadDao = null;
    private String classId ="";//课程Id
    private String accountId ="";//用户Id

    private DownLoadRecive mRecive;//接收下载回调
    private List<FileInfo> mFileList;//下载的文件List

    long time = System.currentTimeMillis(); //当前时间

    @BindView(R.id.dl_xxxw_lv)
    ListViewCompat dl_xxxw_lv;//显示下载列表
    @BindView(R.id.dl_xxxw_xzgd)
    TextView dl_xxxw_xzgd;//选择更多bt
    @BindView(R.id.dl_xxxw_qx)
    TextView dl_xxxw_qx;//全选
    @BindView(R.id.dl_xxxw_del)
    TextView dl_xxxw_del;//删除
    @BindView(R.id.dl_xxxw_ll_bj)
    LinearLayout dl_xxxw_ll_bj;//bottom layout

    //下载adapter
    private FileDownloadAdapter adapter =null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_xxxw);

    }

    /**
     * 全局信息初始化
     */
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);

        fileDao =new FileDAOImpl(this);
        mThreadDao = new ThreadDAOImpl(this);

        classId =hdaplication.getClassId();
        accountId =hdaplication.getStuid();

    }

    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        setActivityLeftTitleShow(true);
        setActivityLeftTitle(hdaplication.getClassicname());
        setToolbar_backShow(true);
        setActivitRightBtnShow(true);
        setActivityRightBtn(R.string.down_edit);
        setActivityRightBtnSize(14);
    }

    /**
     * 初始化
     * @param view
     */
    protected void initViews(View view) {

        //下载列表
        mFileList = new ArrayList<FileInfo>();
        adapter =new FileDownloadAdapter(this,mFileList);
        dl_xxxw_lv.setAdapter(adapter);
        dl_xxxw_lv.setDateType(1);
        mRecive = new DownLoadRecive();
        IntentFilter intentFilter = new IntentFilter();
        //增加监听内容
        intentFilter.addAction(DownloadService.ACTION_UPDATE);
        intentFilter.addAction(DownloadService.ACTION_STOP);
        intentFilter.addAction(DownloadService.ACTION_ERROR);
        intentFilter.addAction(ACTION_ADD_FILE);
        registerReceiver(mRecive, intentFilter);

    }

    /**
     * 监听器
     */
    public void setListener(){

        dl_xxxw_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FileInfo currentFile =mFileList.get(position);
                //如果现在处于下载界面
                if (adapter.DOWNLOAD_VIEW == adapter.getStatus()){
                    int currentFileStatus =currentFile.getProgress_status();

                    if (STATUS_START==currentFileStatus||STATUS_WAIT ==currentFileStatus)
                        stopDownload(currentFile);



                    else if(STATUS_STOP==currentFileStatus ||STATUS_ERROR ==currentFileStatus)
                        prepareDownload(currentFile);



                    else if (STATUS_FINISH ==currentFileStatus){    //如果是完成状态-->跳转

                        File file =FileUtil.isExist(FinalStr.DOWNLOAD_PATH,currentFile.getBehavior_id()+currentFile.getFile_type());
                        currentFile.setAttach_name(currentFile.getBehavior_id()+currentFile.getFile_type());

                        if (null!= file) { //如果文件存在的话
                            moveToBehaviorActivity(currentFile);
                        }else{
                            //弹出提示框
                            showMessageDialog("文件存在或者已经被删除");

                            //删除文件列表
                            List<FileInfo> fileInfos =new ArrayList<FileInfo>();
                            fileInfos.add(currentFile);
                            deleteFile(fileInfos,fileInfos);

                        }
                    }
                }
                else {  //处于编辑界面
                    if (currentFile.isSelect())
                        currentFile.setSelect(false);
                    else
                        currentFile.setSelect(true);

                    adapter.notifyDataSetChanged();
                    //设置确定按钮颜色
                    setSureBtAction(adapter.isItemSelected());
                }
            }
        });
    }

    /**
     * 逻辑
     * @param mContext
     */
    protected void doBusiness(Context mContext) {
        //初始化数据
        mFileList.addAll(fileDao.querySingleClassFiles(classId,accountId));
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    /**
     * 从DownloadTask中获取广播信息，更新进度条
     */
    class DownLoadRecive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {  // 更新进度

                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");

                if (mFileList==null){//防止再次进入页面时mFileList为null错误
                    return;
                }
                for (FileInfo fileCurrent :mFileList){
                    if (fileCurrent.getId() ==fileInfo.getId()){
                        fileCurrent.setFile_size(fileInfo.getFile_size());
                        fileCurrent.setFile_finish(fileInfo.getFile_finish());
                        fileCurrent.setSecondDownloadSize(fileCurrent.getSecondDownloadSize()+fileInfo.getSecondDownloadSize());

                        //解决停止后还会更改progressbar的bug
                        if (fileCurrent.getProgress_status()!=0){
                            fileCurrent.setProgress_status(1);
                        }
                        if (fileCurrent.isSelect()){
                            fileCurrent.setSelect(true);
                        }else{
                            fileCurrent.setSelect(false);
                        }
                        Log.d(TAG,fileCurrent.toString());
                    }
                }
                adapter.notifyDataSetChanged();
                //计算每秒下载速度
                upDateDownloadSpeed();

            } else if (DownloadService.ACTION_STOP.equals(intent.getAction())){  //停止完成时

                FileInfo  fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");

                for (FileInfo fileCurrent :mFileList){
                    if (fileCurrent.getId() ==fileInfo.getId()){


                        fileDao.updateFile(fileCurrent);

                        //删除正在下载的任务
                        downloadService.removeDownloading(fileCurrent);

                        break;
                    }
                }
                adapter.notifyDataSetChanged();

            }else if (ACTION_ADD_FILE.equals(intent.getAction())){  //加入文件后

                //搜索该课程下的文件
                List<FileInfo> fileInfoList =fileDao.querySingleClassFiles(classId,accountId);
                //去重
                FileFilter(fileInfoList);

                adapter.notifyDataSetChanged();

            }else if (DownloadService.ACTION_ERROR.equals(intent.getAction())){ //错误状态

                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo_error");
                for (FileInfo fileCurrent :mFileList){
                    if (fileCurrent.getId() ==fileInfo.getId()){
                        //更改为停止状态
                        fileCurrent.setProgress_status(4);
                        //更新数据库
                        fileDao.updateFile(fileCurrent);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }

    }

    /**
     * 重写下载成功回调
     * @param fileInfo
     */
    protected void downloadSuccess(FileInfo fileInfo){

        for (FileInfo fileCurrent :mFileList){
            if(fileCurrent.getId() ==fileInfo.getId()){
                fileCurrent.setFile_finish(fileCurrent.getFile_size());
                fileCurrent.setProgress_status(2);
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 阻止重复数据加入
     * @param fileInfoList
     */
    public void FileFilter(List<FileInfo> fileInfoList){
        //阻止重复数据
        Set<FileInfo> dataTemp =new HashSet<FileInfo>(mFileList);
        for (FileInfo fileInfo:fileInfoList){
            //如果能加入该set
            if (dataTemp.add(fileInfo)){
                mFileList.add(fileInfo);//datas也加入
            }
        }
    }

    /**
     * 准备下载
     * @param currentFile
     */
    private void prepareDownload(FileInfo currentFile){
        //开始下载
        currentFile.setProgress_status(3);//设置文件状态为开始
        adapter.notifyDataSetChanged();

        if (downloadService.isDownloading(currentFile)){//如果正在下载就返回
            return;
        }
        //如果没有下载信息
        if ("".equals(currentFile.getUrl())){
            //获取下载信息
            downloadService.getDownloadMessage(serverApi,hdaplication.getStuid(),currentFile);
        }else{
            //加入等待队列
            downloadService.prepare(currentFile);
        }
    }


    /**
     * 停止下载
     * @param fileInfo
     */
    private void stopDownload(FileInfo fileInfo){
        //设置文件状态为开始
        fileInfo.setProgress_status(0);

        //如果为等待状可以马上更改状态
        if (fileInfo.getProgress_status()==STATUS_WAIT ){
            //更新数据库
            fileDao.updateFile(fileInfo);
            adapter.notifyDataSetChanged();

        }
        downloadService.stop(fileInfo);
    }

    /**
     * 更新正在下载文件的速度
     */
    private void upDateDownloadSpeed() {

        if (System.currentTimeMillis() -time >1000){
            time = System.currentTimeMillis();

            int index =-1;
            for (FileInfo fileInfoTemp :mFileList){ //将正在下载的文件更新下载速度
                if ((index=mFileList.indexOf(fileInfoTemp))!=-1){
                    mFileList.get(index).setSecondDownloadSizeTotal(
                            mFileList.get(index).getSecondDownloadSize());
                    mFileList.get(index).setSecondDownloadSize(0);
                }
            }
        }

    }

    /**
     * 跳转到对应behavior的页面
     * @param currentFile
     */
    private void moveToBehaviorActivity(FileInfo currentFile) {
        Intent intent;
        if (currentFile.getFile_code().equals("0")){    //视频
            intent = new Intent(DownloadXxxwActivity.this, VideoActivity.class);
            intent.putExtra("fileName", currentFile.getFile_name());
            intent.putExtra("chapterId", currentFile.getChapter());
            intent.putExtra("showSchedule", currentFile.getShow_schedule());
            intent.putExtra("showNotice",  currentFile.getShow_notice());
            intent.putExtra("showEvaluate",currentFile.getShow_evaluate());
            intent.putExtra("showAsk", currentFile.getShow_ask());
        }else{  //文档
            intent = new Intent(DownloadXxxwActivity.this, DocumentActivity.class);
            intent.putExtra("fileName", currentFile.getFile_name());
        }
        intent.putExtra("behaviorId", currentFile.getBehavior_id());
        startActivity(intent);
    }

    /**
     * 删除文件
     * @param fileInfos
     */
    private void deleteFile(List<FileInfo> fileInfos,List<FileInfo> deteleList){

        long startTime=System.currentTimeMillis();

        for (FileInfo fileInfo:fileInfos){

            //停止
            downloadService.delete(fileInfo);

            //线程删除
            mThreadDao.deleteThread(fileInfo.getUrl());
        }
        //删除文件表里面的
        if (fileDao.deleteFiles(fileInfos)){
            mFileList.removeAll(deteleList);
            adapter.notifyDataSetChanged();
            //删除磁盘里面的
            for (FileInfo fileInfo :fileInfos){
                File file =FileUtil.isExist(FinalStr.DOWNLOAD_PATH,fileInfo.getAttach_name());
                String fileName =fileInfo.getBehavior_id()+fileInfo.getFile_type();
                File file2 =FileUtil.isExist(FinalStr.DOWNLOAD_PATH,fileName);
                if (file!=null){
                    file.delete();
                }
                if (file2!=null){
                    file2.delete();
                }
            }
        }

        downloadService.start();    //删除完后开始

      /*  List<FileInfo> fileInfos1=fileDao.queryAllFiles();
        List<ThreadInfo> threadInfos =mThreadDao.queryAllThreads();
        for (FileInfo F :fileInfos1){
            Log.d("XX",F.toString());
        }
        for (ThreadInfo f :threadInfos){
            Log.d("XX",f.toString());
        }
        long endTime=System.currentTimeMillis();
        Log.d("XX","删除耗时:"+(endTime-startTime));

        */
    }


    /**
     * 点击监听
     * @param view
     */
    @OnClick({R.id.dl_xxxw_xzgd,R.id.toolbar_right,R.id.dl_xxxw_del,R.id.dl_xxxw_qx})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.dl_xxxw_xzgd:
                //跳转至选择文件列表
                Intent intent =new Intent();
                intent.setClass(DownloadXxxwActivity.this, DownloadXzgdActivity.class);
                startActivity(intent);

                break;
            case R.id.toolbar_right:

                //更改下载页面或者删除页面
                if (adapter.DOWNLOAD_VIEW == adapter.getStatus()){
                    dl_xxxw_xzgd.setVisibility(View.GONE);
                    dl_xxxw_ll_bj.setVisibility(View.VISIBLE);
                    adapter.setStatus(1);
                    setActivityRightBtn("取消");
                }else{
                    dl_xxxw_xzgd.setVisibility(View.VISIBLE);
                    dl_xxxw_ll_bj.setVisibility(View.GONE);
                    adapter.clearSelect();
                    adapter.setStatus(0);
                    setActivityRightBtn("编辑");
                }
                adapter.notifyDataSetChanged();

                break;
            case R.id.dl_xxxw_del:

                //删除已经选择的文件

                List<String> behaviorIds =new ArrayList<>();
                List<FileInfo> deteleList =new ArrayList<>();


                for (FileInfo fileInfo:mFileList){
                    if (fileInfo.isSelect()){
                        deteleList.add(fileInfo);
                        behaviorIds.add(fileInfo.getBehavior_id());
                    }
                }
                List<FileInfo> fileInfos =fileDao.queryFileWithBehaviorId(accountId,behaviorIds);
                deleteFile(fileInfos,deteleList);


                break;
            case R.id.dl_xxxw_qx:

                if ("全选".equals(dl_xxxw_qx.getText().toString())){
                    adapter.allSelect();
                    dl_xxxw_qx.setText("取消全选");

                    //设置确定按钮颜色
                    setSureBtAction(true);
                }else{
                    adapter.clearSelect();
                    dl_xxxw_qx.setText("全选");

                    //设置确定按钮颜色
                    setSureBtAction(false);
                }
                break;

        }
    }


    public void delfile(FileInfo fileInfo){
        List<String> behaviorIds =new ArrayList<>();
        List<FileInfo> deteleList =new ArrayList<>();
        deteleList.add(fileInfo);
        behaviorIds.add(fileInfo.getBehavior_id());
        List<FileInfo> fileInfos =fileDao.queryFileWithBehaviorId(accountId,behaviorIds);
        deleteFile(fileInfos,deteleList);
    }

    /**
     * 设置确定下载按钮颜色及行为
     */
    private void setSureBtAction(boolean flag){
        if (flag){
            dl_xxxw_del.setTextColor(getResources().getColor(R.color.colorPrimary));
            dl_xxxw_del.setClickable(true);
        }else{
            dl_xxxw_del.setTextColor(getResources().getColor(R.color.color_999999));
            dl_xxxw_del.setClickable(false);
        }
    }
    @Override
    protected void onPause() {

        //更新文件
        fileDao.updateFiles(mFileList);
        super.onPause();
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        unregisterReceiver(mRecive);
    }
}
