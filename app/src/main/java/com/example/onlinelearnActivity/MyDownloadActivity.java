package com.example.onlinelearnActivity;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adapter.DefaultAdapter;
import com.example.adapter.MyDownloadRecyclerAdapter;
import com.example.entity.FileInfo;
import com.example.entity.LearnClassEntity;
import com.example.jdbc.DBManagerToLearnClass;
import com.example.jdbc.Dao.FileDAO;
import com.example.jdbc.Dao.ThreadDAO;
import com.example.jdbc.Dao.impl.FileDAOImpl;
import com.example.jdbc.Dao.impl.ThreadDAOImpl;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.download.DownloadXxxwActivity;
import com.example.util.CommonUtils;
import com.example.util.FileUtil;
import com.example.util.FinalStr;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的下载页面
 */
public class MyDownloadActivity extends BaseActivity {

    private FileDAO fileDAO = null; //文件dao
    private ThreadDAO threadDAO = null; //线程dao
    private DBManagerToLearnClass dbManagerToLearnClass =null;
    private String student_id;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView; //已下载文件所属课程list
    @BindView(R.id.size_tv)
    TextView size_tv; //所剩容量
    @BindView(R.id.size_pb)
    ProgressBar size_pb; //容量进度条
    @BindView(R.id.size_layout)
    RelativeLayout size_layout; //容量layout
    @BindView(R.id.menu_layout)
    LinearLayout menu_layout; //菜单layout
    @BindView(R.id.start_all)
    LinearLayout start_all; //全部开始
    @BindView(R.id.stop_all)
    LinearLayout stop_all; //全部停止
    @BindView(R.id.delete_all)
    LinearLayout delete_all; //全部删除
    @BindView(R.id.icon_start_all)
    TextView icon_start_all; //全部开始icon
    @BindView(R.id.icon_stop_all)
    TextView icon_stop_all; //全部停止icon
    @BindView(R.id.icon_delete_all)
    TextView icon_delete_all; //全部删除icon
    @BindView(R.id. bottom_menu_layout)
    LinearLayout bottom_menu_layout; //底部layout
    @BindView(R.id. select_all_bt)
    TextView select_all_bt; //全选按钮
    @BindView(R.id. detele_bt)
    TextView detele_bt; //删除按钮

    private  String sdSizeText;//磁盘总容量Str
    private  Long sdSize ;//磁盘总容量
    private boolean isEditMode =false;//是否是编辑模式


    private MyDownloadRecyclerAdapter myDownloadRecyclerAdapter;
    private List<LearnClassEntity> learnClassEntityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_download);
    }

    /**
     * 全局信息初始化
     */
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        student_id = hdaplication.getStuid();
        fileDAO = new FileDAOImpl(this);
        threadDAO =new ThreadDAOImpl(this);
        dbManagerToLearnClass = new DBManagerToLearnClass(this);

    }

    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        setActivityLeftTitleShow(true);
        setActivityLeftTitle("我的下载");
        setToolbar_backShow(true);
        setActivityRightBtn(R.string.icon_file_more);
        setActivitRightBtnShow(true);
        toolbar_right.setTypeface(iconfont);
    }

    /**
     * 初始化
     * @param view
     */
    protected void initViews(View view) {

        learnClassEntityList =new ArrayList<>();
        myDownloadRecyclerAdapter =new MyDownloadRecyclerAdapter(this,learnClassEntityList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(myDownloadRecyclerAdapter);
        icon_start_all.setTypeface(iconfont);
        icon_stop_all.setTypeface(iconfont);
        icon_delete_all.setTypeface(iconfont);
    }

    /**
     * 监听器管理
     */
    public void setListener(){
        //item点击监听
        myDownloadRecyclerAdapter.setOnItemClickListener(new DefaultAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                if (isEditMode){
                    if (learnClassEntityList.get(position).isSelect()){
                        learnClassEntityList.get(position).setSelect(false);
                    }else{
                        learnClassEntityList.get(position).setSelect(true);

                    }
                    myDownloadRecyclerAdapter.notifyDataSetChanged();
                    //设置确定按钮颜色
                    setSureBtAction(myDownloadRecyclerAdapter.isItemSelected());
                }else{
                    //设置当前选中课程
                    hdaplication.setCoursewareId(learnClassEntityList.get(position).getCoursewareId());
                    hdaplication.setClassId(learnClassEntityList.get(position).getId());
                    hdaplication.setClassicname(learnClassEntityList.get(position).getClassName());

                    startActivity(DownloadXxxwActivity.class);
                }

            }
        });
    }

    /**
     * 逻辑
     * @param mContext
     */
    protected void doBusiness(Context mContext) {

        //查询该用户已下载文件所属课程
        List<String> classids =fileDAO.queryClassIds(student_id);

        //查询课程内容
        List<LearnClassEntity> learnClassEntities = null;
        try {
            learnClassEntities = dbManagerToLearnClass.queryClassByClassId(classids);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //课程列表初始化
        myDownloadRecyclerAdapter.addDates(learnClassEntities);


        //是否存在SD卡,如果存在则计算sd卡剩余容量
        if (CommonUtils.isExistSD()){
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());

            long blockSize;//区块大小
            long availableBlocks;//可用区块个数
            /**
             *  getBlockSize,getBlockCount,getAvailableBlocks方法只有在4.3之前使用
             *  所以判断当前版本是否是4.3以上。
             *  Build.VERSION.SDK_INT 当前使用的安卓版本;Build.VERSION_CODES.JELLY_BEAN_MR2代表安卓4.3
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                blockSize=stat.getBlockSizeLong();
                availableBlocks=stat.getAvailableBlocksLong();
            }else {
                blockSize=stat.getBlockSize();
                availableBlocks=stat.getAvailableBlocks();
            }
            //sd卡剩余容量
            sdSize =blockSize*availableBlocks ;

        }else{
            //隐藏容量显示
            size_layout.setVisibility(View.INVISIBLE);
        }


    }

    /**
     * 更新磁盘容量信息
     */
    private void updateBlockSize() {
        Long allFileSize =fileDAO.queryFilesTotalSize(student_id);
        sdSizeText = FileUtil.computeFileSize(sdSize);
        String allFileSizeText = FileUtil.computeFileSize(allFileSize);
        double a =(double)allFileSize/sdSize;
        size_pb.setMax(1000000);
        int percent = (int)(a*1000000);
        size_tv.setText(allFileSizeText+" / "+sdSizeText);
        size_pb.setProgress(percent);
    }

    /**
     * 更新下载过程信息
     */
    private void updateDownloadMessage() {
        List<LearnClassEntity> learnClassEntitys =new ArrayList<>();
        for (LearnClassEntity learnClassEntity :learnClassEntityList){
            int downloading = fileDAO.queryFilesSizeDownload(learnClassEntity.getId(),student_id);
            int downLoadFinish =fileDAO.queryFilesSizeFinish(learnClassEntity.getId(),student_id);
            int downLoadAll = fileDAO.queryFilesSizeALL(learnClassEntity.getId(),student_id);
            long totalFileSize =fileDAO.queryFilesSize(learnClassEntity.getId(),student_id);
            //如果总文件为0,则删除文件夹
            if (downLoadAll ==0){
                learnClassEntitys.add(learnClassEntity);
            }
            String totalFileSizeStr = FileUtil.computeFileSize(totalFileSize);
            learnClassEntity.setDownLoadFinishNum(downLoadFinish);
            learnClassEntity.setDownLoadAllNum(downLoadAll);
            learnClassEntity.setDownLoadingNum(downloading);
            learnClassEntity.setDownLoadFileSize(totalFileSizeStr);

        }

        learnClassEntityList.removeAll(learnClassEntitys);

        myDownloadRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        //更新数据
        if (learnClassEntityList!=null){
            updateDownloadMessage();
            updateBlockSize();
        }
    }

    @OnClick({R.id.toolbar_right,R.id.start_all,R.id.stop_all,R.id.delete_all,R.id.select_all_bt,R.id.detele_bt})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            //工具栏右侧按钮操作
            case R.id.toolbar_right:
                //编辑模式
                if (isEditMode){
                    isEditMode =false;
                    myDownloadRecyclerAdapter.setEditMode(isEditMode);
                    myDownloadRecyclerAdapter.notifyDataSetChanged();
                    setActivityRightBtn(R.string.icon_file_more);
                    bottom_menu_layout.setVisibility(View.GONE);
                }else{  //非编辑模式

                    if (View.VISIBLE ==menu_layout.getVisibility()){
                        menu_layout.setVisibility(View.INVISIBLE);
                    }else{
                        menu_layout.setVisibility(View.VISIBLE);
                    }

                }
                break;

            //全部删除按钮
            case R.id.delete_all:

                isEditMode =true;
                myDownloadRecyclerAdapter.setEditMode(isEditMode);
                myDownloadRecyclerAdapter.notifyDataSetChanged();
                setActivityRightBtn("取消");
                menu_layout.setVisibility(View.GONE);
                bottom_menu_layout.setVisibility(View.VISIBLE);

                break;

            //全选
            case R.id.select_all_bt:
                if ("全选".equals(select_all_bt.getText().toString())){
                    myDownloadRecyclerAdapter.allSelect();
                    select_all_bt.setText("取消全选");

                    //设置确定按钮颜色
                    setSureBtAction(true);
                }else{
                    myDownloadRecyclerAdapter.clearSelect();
                    select_all_bt.setText("全选");

                    //设置确定按钮颜色
                    setSureBtAction(false);
                }
                break;

            //删除
            case R.id.detele_bt:

                List<String> classIds =new ArrayList<>();
                List<LearnClassEntity> deteleList =new ArrayList<>();
                //删除选中
                for (LearnClassEntity entity :learnClassEntityList){
                    if (entity.isSelect()){
                        classIds.add(entity.getId());
                        deteleList.add(entity);
                    }
                }
                List<FileInfo> fileInfos =fileDAO.queryFileWithClassId(student_id,classIds);
                deleteFile(fileInfos,deteleList);
                break;

            //全部开始按钮
            case R.id.start_all:

                downloadService.startAll(serverApi,student_id);
                menu_layout.setVisibility(View.GONE);
                break;

            //全部停止按钮
            case R.id.stop_all:

                downloadService.stopAll(student_id);
                menu_layout.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 删除文件
     * @param fileInfos
     */
    private void deleteFile(List<FileInfo> fileInfos,List<LearnClassEntity> deteleList){

        long startTime=System.currentTimeMillis();
        for (FileInfo fileInfo:fileInfos){
            //停止
            downloadService.delete(fileInfo);

            //线程删除
            threadDAO.deleteThread(fileInfo.getUrl());
        }
        //删除文件表里面的
        if (fileDAO.deleteFiles(fileInfos)){
            //ui刷新
            learnClassEntityList.removeAll(deteleList);
            myDownloadRecyclerAdapter.notifyDataSetChanged();
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
        updateBlockSize();//更新信息
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
     * 设置确定下载按钮颜色及行为
     */
    private void setSureBtAction(boolean flag){
        if (flag){
            detele_bt.setTextColor(getResources().getColor(R.color.colorPrimary));
            detele_bt.setClickable(true);
        }else{
            detele_bt.setTextColor(getResources().getColor(R.color.color_999999));
            detele_bt.setClickable(false);
        }
    }

}
