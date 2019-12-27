package com.example.onlinelearnActivity.courseWare.document;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.util.NetWork;
import com.example.entity.DocumentRecordModel;
import com.example.entity.FileInfo;
import com.example.jdbc.Dao.DocumentDAO;
import com.example.jdbc.Dao.FileDAO;
import com.example.jdbc.Dao.impl.DocumentDaoImpl;
import com.example.jdbc.Dao.impl.FileDAOImpl;
import com.example.jsonReturn.DocumentRecordReturn;
import com.example.jsonReturn.RideoFlagReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.BaseActivity;
import com.example.util.FileUtil;
import com.example.util.FinalStr;
import com.example.util.JsonUtil;
import com.example.view.dialog.MaterialDialog;
import com.example.view.numberProgress.NumberProgressBar;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;


import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 文档activity
 */
public class DocumentActivity extends BaseActivity implements OnPageChangeListener ,OnLoadCompleteListener {

    private String TAG = "DocumentActivity";

    private final int LOAD_FILE_SUCCESS =1; //加载成功返回
    private final int LOAD_FILE_FAIL =2; //加载失败返回
    private final int REFRESH_PROGRESS =100;  //进度条

    private FileDAO fileDao = null;
    private DocumentDAO documentDAO = null;

    @BindView(R.id.pdfView)
    PDFView pdfView; //加载pdf的view
    @BindView(R.id.page_tv)
    TextView page_tv;//页数tv

    private MaterialDialog downLoadDialog; //无文档框 下载框

    private NumberProgressBar progressBar ;//downloaddialog中的进度条
    private TextView message_tv ;//downloaddialog中的进度条

    private String behaviorId ="";//学习行为Id
    private String classId ="";//课程Id
    private String accountId ="";//用户Id
    private String accountName ="";//用户名
    private String coursewareId ="";//课件id
    private RideoFlagReturn rideoReturn;//视频&文档信息返回

    private int pageNumber = 0;//当前页数
    private int spacing = 10;//pdfview中每页的间隔

    private int positions   =0;//进度条位置
    private int positionMax =100;//进度条最大值

    private File writeResult =null;//写入SD卡后的文件

    private int delayTime =2000;//延迟多少秒关闭dalog
    private String fileName ;   //文件名称
    private String chapterName ; //章节名称
    private FileInfo fileInfo;  //当前的文件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
    }

    /**
     * 全局信息初始化
     */
    public void initialize(Bundle savedInstanceState) {

        super.initialize(savedInstanceState);

        if (null != getIntent().getStringExtra("fileName")){
            fileName =getIntent().getStringExtra("fileName");
            chapterName =getIntent().getStringExtra("chapterName");
        }
        /**
         * 获取用户信息
         */
        coursewareId =hdaplication.getCoursewareId();
        if (null != getIntent().getStringExtra("behaviorId")){
            behaviorId =getIntent().getStringExtra("behaviorId");
        }else{
            behaviorId =hdaplication.getBehaviorId();
        }
        classId =hdaplication.getClassId();
        accountId =hdaplication.getStuid();
        accountName =hdaplication.getUsername();

        fileDao =new FileDAOImpl(this);
        documentDAO = new DocumentDaoImpl(this);
    }

    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        setActivityLeftTitle(fileName);
        setActivityLeftTitleShow(true);
        setToolbar_backShow(true);
    }

    /**
     * 初始化
     */
    public void initViews(View view){

        //设置pdfview背景
        pdfView.setBackgroundColor(Color.WHITE);

    }

    /**
     * 设置监听
     */
    public void setListener(){

        FileUtil.setUiHandler(handler);
    }

    /**
     * 业务操作
     * @param mContext
     */
    protected void doBusiness(Context mContext) {

        if (!isExistLocalDocument()){ //如果没有本地视频
            if (!NetWork.isNetworkConnected(this)){  //如果没有网络
                showMessageDialog("没有离线该文档且当前不在网络状态，请检查网络连接");
                return;
            }
            //新增一条文件记录
            insertFile();

            //下载文件
            showDocumentThread();
        }
    }


    /**
     * 是否有本地文档
     */
    private boolean isExistLocalDocument(){

        fileInfo = fileDao.querySingleFile(accountId,behaviorId);
        //如果数据库里有文件信息 && 已经下载完成
        if (null !=fileInfo && 2 ==fileInfo.getProgress_status()){  //完成状态
            File file =FileUtil.isExist(FinalStr.DOWNLOAD_PATH,fileInfo.getAttach_name());

            if (null!= file){ //如果文件存在的话

                display(file,pageNumber);
                return true;
            }
        }
        return false;
    }

    /**
     * 新增文件
     */
    private void insertFile(){

        //生成fileName
        FileInfo fileInfo = new FileInfo();
        fileInfo.setClass_id(classId);
        fileInfo.setStudent_id(accountId);
        fileInfo.setBehavior_id(behaviorId);
        fileInfo.setFile_name(fileName);
        fileInfo.setFile_code("1");
        fileInfo.setProgress_status(0);
        fileInfo.setChapterName(chapterName);

        //新增一条文件记录
        fileDao.insertFile(fileInfo);
    }

    /**
     * 更新文件信息
     */
    private void updateFile(){
        String webPath =rideoReturn.getResourceWebPath();
        String fileUrl ="";
        if (webPath.contains("http")){
            fileUrl =webPath;
        }else{
            fileUrl =rideoReturn.getResourceIPAddress()+webPath;
        }
        int index =webPath.lastIndexOf("/");
        String savePath = FinalStr.DOWNLOAD_PATH +webPath.substring(0,index+1);

        fileInfo = fileDao.querySingleFile(accountId,behaviorId);
        //更改后缀
        fileUrl =fileUrl.replace(".swf",".pdf");
        String fileType =".pdf";
        fileInfo.setUrl(fileUrl);
        fileInfo.setSavepath(savePath);
        fileInfo.setFile_type(fileType);
        fileInfo.setAttach_name(fileInfo.getBehavior_id()+fileType);
        fileInfo.setFile_time(Integer.parseInt(rideoReturn.getVideoTotalTime()));

    }

    /**
     * 获取文档信息线程
     */
    private void showDocumentThread(){
        //调用接口方法
        Call<ResponseBody> call = serverApi.getFileMessage(accountId,classId,behaviorId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result =response.body().string();
                        Log.d(JSON_TAG,"获取文档信息："+result);

                        //gson解析
                        rideoReturn = JsonUtil.parserString(result,RideoFlagReturn.class);
                        if (rideoReturn.isSuccess()){
                            //更新文件信息
                            updateFile();
                            //下载文件
                            downloadDocument();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //获取文件信息失败操作
            }
        });
    }

    /**
     * 下载文档线程
     */
    private void downloadDocument() {

        //调用下载文档接口
        Call<ResponseBody> call = serverApi.downloadFile(fileInfo.getUrl());
        // 异步下载文件.
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //成功操作
                    //显示下载框
                    showDownLoadDialog();
                    fileInfo.setFile_size((int) response.body().contentLength());
                    //开启线程写入sd卡
                    loadFile(response.body(), FinalStr.DOWNLOAD_PATH,fileInfo.getAttach_name());

                }else{
                    //失败操作
                    showMessageDialog("该课程暂无文档");
                    pdfView.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //失败操作
                showMessageDialog("该课程暂无文档");
                pdfView.setVisibility(View.INVISIBLE);
            }
        });
    }


    /**
     * 将文件写入SD卡
     * @param responseBody
     * @param path
     * @param fileName
     */
    private void loadFile(final ResponseBody responseBody , final String path, final String fileName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeResult = FileUtil.writeResponseBodyToDisk(responseBody, path, fileName);
                if (writeResult!=null){
                    sendMessage(LOAD_FILE_SUCCESS,null);
                }else{
                    sendMessage(LOAD_FILE_FAIL,null);
                }
            }
        }).start();
    }



    /**
     * 显示pdf
     * @param file
     * @param pageNumber
     */
    private void display(File file, int pageNumber) {


        pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .spacing(spacing) // in dp
                .load();

    }

    /**
     * 页面滑动监听
     * @param page
     * @param pageCount
     */
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        page_tv.setText((page + 1)+"/"+pageCount);
        page_tv.setVisibility(View.VISIBLE);

    }

    /**
     * 滑动结束后监听
     */
    public void onPageLoadFinish() {
        page_tv.setVisibility(View.INVISIBLE);
    }

    /**
     * pdf加载完的监听
     * @param nbPages the number of pages in this PDF file
     */
    public void loadComplete(int nbPages) {
        //插入记录
        insertRecord();
    }

    /**
     * 插入记录
     */
    private void insertRecord(){
        DocumentRecordModel documentRecordModel = new DocumentRecordModel(accountId,accountName,classId,behaviorId,pdfView.getPageCount(),pageNumber);
        documentDAO.insertDetailRecord(documentRecordModel);
    }

    /**
     * 获取明细
     */
    private void getDocumentRecord(){
        //获取该用户该行为的明细
        List<DocumentRecordModel> documentRecordModels =documentDAO.queryDetailRecordSingle(classId,behaviorId,accountId);

        List<DocumentRecordModel> temp =null;
        int postTime =0; //提交次数
        int itemCount =100;//每100条提交一次
        if (documentRecordModels.size()/itemCount ==0){ //如果条数不足100
            temp = documentRecordModels.subList(0,documentRecordModels.size());
            offLineDLFile(0,temp,"Y");

        }else {  //是100条的倍数
            postTime =documentRecordModels.size()/itemCount;
            for (int i=0;i<postTime;i++){
                temp =documentRecordModels.subList(i*itemCount,(i+1)*itemCount);


                if (i == postTime-1 && documentRecordModels.size()%itemCount==0){   //如果当前为最后一次且没有余数
                    offLineDLFile(i,temp,"Y");
                    break;
                }
                offLineDLFile(i,temp,"N");
            }
            if (documentRecordModels.size()%itemCount!=0){   //还有数据
                temp =documentRecordModels.subList(postTime*itemCount,documentRecordModels.size());
                offLineDLFile(postTime,temp,"Y");
            }

        }

    }

    /**
     * 提交明细
     * @param num
     * @param temp
     * @param isLast
     */
    private void offLineDLFile(final int num, final List<DocumentRecordModel> temp,final String isLast){

        //调用接口方法
        Call<ResponseBody> call = serverApi.offLineDLFile(JsonUtil.BuildJson(temp),isLast,accountId,classId,behaviorId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求成功

                    try {
                        String result =response.body().string();
                        Log.d(JSON_TAG,"获取提交文档明细返回:"+result);
                        //gson解析
                        DocumentRecordReturn documentRecordReturn = JsonUtil.parserString(result,DocumentRecordReturn.class);
                        if (documentRecordReturn.isSuccess()){
                            //删除明细表里面的数据
                            documentDAO.deleteDetailRecords(temp);

                            List<DocumentRecordModel> documentRecordModels= documentDAO.queryDetailRecordSingle(classId,behaviorId,accountId);
                            Log.d(TAG,"明细表剩余条数:"+documentRecordModels.size()+"");

                        }
                        //如果是最后一次返回
                        if ("Y".equals(isLast)){
                            //获取现有的behavior数据
                            List<DocumentRecordModel> documentRecordTotals= documentDAO.queryTotalRecordSingle(classId,behaviorId,accountId);
                            //批量插入服务器的汇总并判断是否成功
                            if (documentDAO.insertTotalRecords(documentRecordReturn.getsDFileEntity())){    //先插入后删除防止插入失败
                                //删除总汇表对应的behavior数据
                                documentDAO.deleteTotalRecords(documentRecordTotals);
                                Log.d(TAG,"总汇表剩余条数:"+documentDAO.queryTotalRecordSingle(classId,behaviorId,accountId).size()+"");

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
    @Override
    protected void onPause() {
        super.onPause();
        //获取文件记录
        getDocumentRecord();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        FileUtil.clearHandler();
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
     * 请求回调
     */
    @Override
    protected void handler(Message msg) { //我们可以处理数据消息了
        switch (msg.what) {

            case LOAD_FILE_SUCCESS:
                fileInfo.setProgress_status(2);
                fileInfo.setFile_finish(fileInfo.getFile_size());
                fileDao.updateFile(fileInfo);//更新数据库
                display(writeResult, pageNumber);

                closeDownLoadDialog();
                break;

            case  LOAD_FILE_FAIL:

                message_tv.setText("下载失败");
                //延迟关闭
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        closeDownLoadDialog();
                    }
                }, delayTime);
                break;

            case REFRESH_PROGRESS :
                //更新下载进度

                progressBar.setProgress((int)msg.getData().getInt("progress"));

                break;
        }
    }


    /**
     * 显示下载框
     */
    private void showDownLoadDialog(){

        if (downLoadDialog ==null){
            View view =  LayoutInflater.from(this)
                    .inflate(R.layout.dialog_progress_layout, null);
            progressBar = (NumberProgressBar) view.findViewById(R.id.number_bar);
            message_tv = (TextView) view.findViewById(R.id.message_tv);
            downLoadDialog = new MaterialDialog(this)
                    .setCanceledOnTouchOutside(false)
                    .setContentView(view);
        }
        downLoadDialog.show();
    }

    /**
     * 关闭下载框
     */
    private void closeDownLoadDialog(){
        downLoadDialog.dismiss();
    }



}



