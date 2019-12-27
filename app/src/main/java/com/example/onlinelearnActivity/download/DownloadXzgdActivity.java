package com.example.onlinelearnActivity.download;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.DownloadAdapter;
import com.example.entity.FileInfo;
import com.example.entity.PDFOutlineElement;
import com.example.jdbc.Dao.FileDAO;
import com.example.jdbc.Dao.impl.FileDAOImpl;
import com.example.jsonReturn.CourseReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.BaseActivity;
import com.example.spt.CourseUtil;
import com.example.spt.jaxb.course.Item;
import com.example.spt.jaxb.course.Manifest;

import com.example.util.FileUtil;
import com.example.util.JsonUtil;
import com.example.util.XStreamUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DownloadXzgdActivity extends BaseActivity {

    protected String TAG = "DownloadXzgdActivity";

    private FileDAO fileDAO = null; //文件dao
    private String coursewareId ="";//课件id
    private String classId ="";
    private String courseWareTitle;//课程名称
    private String accountId;

    @BindView(R.id.dl_xzgd_lv)
    ListView dl_xzgd_lv;//显示xml列表
    @BindView(R.id.dl_xzgd_qd)
    TextView dl_xzgd_qd;//确定按钮
    @BindView(R.id.dl_xzgd_qx)
    TextView dl_xzgd_qx;//全选按钮
    @BindView(R.id.message_tv)
    TextView message_tv; //没有资源时的提示tv

    private ArrayList<PDFOutlineElement> mPdfOutlines = new ArrayList<PDFOutlineElement>();
    private DownloadAdapter downloadAdapter = null;
    private String xmlpath; //保存xml的地址

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_xzgd);
    }

    /**
     * 全局信息初始化
     */
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);

        fileDAO = new FileDAOImpl(this) ;
        coursewareId = hdaplication.getCoursewareId();
        classId =hdaplication.getClassId();
        courseWareTitle =hdaplication.getClassicname();
        accountId = hdaplication.getStuid();
    }

    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        setActivityLeftTitleShow(true);
        setActivityLeftTitle(courseWareTitle);
        setToolbar_backShow(true);
    }

    /**
     * 初始化
     * @param view
     */
    protected void initViews(View view) {

    }

    /**
     * 设置监听
     */
    protected void setListener(){

        //item点击操作
        dl_xzgd_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mPdfOutlines.get(position).getLevel()==2) { //第三层目录

                    //切换子节点选中或者未选中
                    if (mPdfOutlines.get(position).getStatusXz()==0){
                        mPdfOutlines.get(position).setStatusXz(1);

                    }else {
                        mPdfOutlines.get(position).setStatusXz(0);
                    }

                } else if (mPdfOutlines.get(position).getLevel()==1){ //第二层目录

                    //切换父节点及其子节点选中或者未选中
                    if(mPdfOutlines.get(position).getStatusXz()==0){

                        mPdfOutlines.get(position).setStatusXz(1);
                        setChildAction(position,1);
                    }else {
                        mPdfOutlines.get(position).setStatusXz(0);
                        setChildAction(position,0);

                    }
                }
                downloadAdapter.notifyDataSetChanged();
                //设置确定按钮颜色
                setSureBtAction(downloadAdapter.isItemSelected());
            }
        });
    }

    /**
     * 设置子节点的行为
     * @param position
     * @param action 0不选中;1选中
     */
    private void setChildAction(int position ,int action){
        int startIndex =position +1;
        for (int i =startIndex ;i< mPdfOutlines.get(position).getChildSize() +startIndex ;i++){
            if (mPdfOutlines.get(i).getLevel()==2 &&
                    mPdfOutlines.get(i).getCharterId().equals(mPdfOutlines.get(position).getIdentifier())){
                mPdfOutlines.get(i).setStatusXz(action);
            }
        }
    }

    /**
     * 逻辑操作
     * @param mContext
     */
    protected void doBusiness(Context mContext) {

        showLoading();//显示加载

        //判断是否有该xml文件
        if (isExistManifest()){
            //解析xml
            analyzeXML();
        }
        else{
            //下载xml
            downLoadXML();
        }
    }


    /**
     * 是否存在xml文件
     */
    private boolean isExistManifest() {

        xmlpath =getFilesDir().getPath()+"/course_ware";
       /* xmlpath = FinalStr.DOWNLOAD_PATH +"/course_ware";*/
        File pFile=new File(xmlpath);

        //如果不存在,则创建文件夹
        if (!pFile.exists()) {
            pFile.mkdir();
        }

        //判断课件是否存在
        File file = FileUtil.isExist(xmlpath,coursewareId+".xml");
        if(file!=null){
            return true;
        }
        return false;
    }


    /**
     * 解析xml
     */
    private void analyzeXML(){

        File filePath=new File(xmlpath,coursewareId+".xml");

        Manifest manifest=new XStreamUtil().str2JavaFormXML(filePath,Manifest.class);
        initialData(manifest);

        if (mPdfOutlines.size()==0){
            message_tv.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 设置目录数据
     */
    private void initialData( Manifest manifest) {

        courseWareTitle=manifest.getOrganizations().getOrganization().get(0).getTitle();
        List<Item> tItemsOne = CourseUtil.getCourseOrganization(manifest);
        int j=0;
        if (tItemsOne!=null&&tItemsOne.size()>0) {

            for (Item item : tItemsOne) {  //一层结构
                j++;
                PDFOutlineElement pdfOutlineElementOne=new PDFOutlineElement(j+"",item.getTitle(),0,item.getIdentifier(),
                        "",item.getIdentifier(),0,0,null);
                mPdfOutlines.add(pdfOutlineElementOne);

                List<Item> tItemTwo=item.getItem();
                if (tItemTwo!=null&&tItemTwo.size()>0) {

                    boolean isHasFileOne =false; //是否有资源
                    for (int i = 0; i < tItemTwo.size(); i++) {
                        j++;
                        PDFOutlineElement pdfOutlineElementTwo=new PDFOutlineElement(j+"",tItemTwo.get(i).getTitle(),1,tItemTwo.get(i).getIdentifier(),
                                "",pdfOutlineElementOne.getIdentifier(),0,0,pdfOutlineElementOne);
                        List<Item> tItemThree=tItemTwo.get(i).getItem();
                        pdfOutlineElementTwo.setChildSize(tItemThree.size());   //设置3级目录大小
                        mPdfOutlines.add(pdfOutlineElementTwo);


                        boolean isHasFileTwo =false; //二级结构下是否有资源
                        for (Item itemBehavior : tItemThree) {
                            String behavior=(String) itemBehavior.getIdentifierref().subSequence(0, 1); //获取类型
                            if (("1".equals(behavior)||"0".equals(behavior))&& !fileDAO.isExists(itemBehavior.getIdentifier(), accountId)){  //如果有资源的话
                                isHasFileTwo =true;
                                isHasFileOne =true;
                                j++;
                                //获取文件大小
                                int resourceSize =0;
                                if (null!=itemBehavior.getResourceSize() && !("null".equals(itemBehavior.getResourceSize()))){
                                    resourceSize =Integer.parseInt(itemBehavior.getResourceSize());
                                }
                                PDFOutlineElement pdfOutlineElementThree=new PDFOutlineElement(j+"",itemBehavior.getTitle(),2,itemBehavior.getIdentifier(),
                                        behavior,pdfOutlineElementTwo.getIdentifier(),0,resourceSize,pdfOutlineElementTwo);

                                pdfOutlineElementThree.setShowAsk(itemBehavior.getShowAsk());
                                pdfOutlineElementThree.setShowEvaluate(itemBehavior.getShowEvaluate());
                                pdfOutlineElementThree.setShowNotice(itemBehavior.getShowNotice());
                                pdfOutlineElementThree.setShowSchedule(itemBehavior.getShowSchedule());
                                mPdfOutlines.add(pdfOutlineElementThree);
                            }
                        }

                        if (!isHasFileTwo){
                            mPdfOutlines.remove(pdfOutlineElementTwo);
                        }
                    }
                    if (!isHasFileOne){
                        mPdfOutlines.remove(pdfOutlineElementOne);
                    }
                }
            }
        }
        downloadAdapter = new DownloadAdapter(this, R.layout.course_ware, mPdfOutlines);
        dl_xzgd_lv.setAdapter(downloadAdapter);
        closeLoading();//关闭加载
    }

    /**
     * 下载xmL
     */
    private void downLoadXML(){
        //调用接口方法
        Call<ResponseBody> call = serverApi.getShowCatalog(coursewareId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求成功
                    try {
                        String result =response.body().string();
                        Log.d(JSON_TAG,"获取xml返回："+result);

                        CourseReturn courseReturn= JsonUtil.parserString(result, CourseReturn.class);

                        //如果返回成功
                        if(courseReturn.isSuccess()) {

                            String xmlString = courseReturn.getXml();

                            File file = new File(xmlpath, coursewareId+ ".xml");

                            FileOutputStream fos = new FileOutputStream(file);

                            fos.write(xmlString.getBytes());

                            fos.close();

                            //解析xml
                            analyzeXML();
                        }
                        else{
                            showToast(R.string.server_error);
                            closeLoading();//关闭加载
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToast(R.string.server_error);
                closeLoading();//关闭加载
            }
        });
    }

    /**
     * 向数据库插入文件
     */
    private List<FileInfo> insertFile(){

        List<FileInfo> fileInfos =new ArrayList<>();
        for (int i=0;i<mPdfOutlines.size();i++){
            if (mPdfOutlines.get(i).getStatusXz()==1 && mPdfOutlines.get(i).getLevel()==2){

                //生成fileName
                FileInfo fileInfo = new FileInfo();
                fileInfo.setClass_id(classId);
                fileInfo.setStudent_id(accountId);
                fileInfo.setBehavior_id(mPdfOutlines.get(i).getIdentifier());
                fileInfo.setChapter(mPdfOutlines.get(i).getParentElement().getCharterId());
                fileInfo.setPdfOutlineElement(mPdfOutlines.get(i));
                fileInfo.setFile_name(mPdfOutlines.get(i).getOutlineTitle());
                fileInfo.setFile_code(mPdfOutlines.get(i).getBehavior());
                fileInfo.setProgress_status(0);
                fileInfo.setChapterName(mPdfOutlines.get(i).getParentElement().getOutlineTitle());
                fileInfo.setShow_schedule(mPdfOutlines.get(i).getShowSchedule());
                fileInfo.setShow_notice(mPdfOutlines.get(i).getShowNotice());
                fileInfo.setShow_evaluate(mPdfOutlines.get(i).getShowEvaluate());
                fileInfo.setShow_ask(mPdfOutlines.get(i).getShowAsk());
                fileInfos.add(fileInfo);
            }
        }
        //如果插入成功
        if (fileDAO.insertFiles(fileInfos)){
            return fileInfos;
        }
        return null;
    }



    /**
     * 设置确定下载按钮颜色及行为
     */
    private void setSureBtAction(boolean flag){
       if (flag){
           dl_xzgd_qd.setTextColor(getResources().getColor(R.color.colorPrimary));
           dl_xzgd_qd.setClickable(true);
       }else{
           dl_xzgd_qd.setTextColor(getResources().getColor(R.color.color_999999));
           dl_xzgd_qd.setClickable(false);
       }
    }


    @OnClick({R.id.dl_xzgd_qd,R.id.dl_xzgd_qx})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.dl_xzgd_qd:


                // 如果插入文件成功则发送广播
                List<FileInfo> list =insertFile();
                if (list!=null){

                    for (FileInfo fileInfo :list){
                        FileInfo f = fileDAO.querySingleFile(hdaplication.getStuid(),fileInfo.getBehavior_id());
                        f.setProgress_status(3);//设置文件状态为开始

                        //如果没有下载信息
                        if ("".equals(f.getUrl())){
                            //获取下载信息
                            downloadService.getDownloadMessage(serverApi,hdaplication.getStuid(),f);
                        }else{
                            //加入等待队列
                            downloadService.prepare(f);
                        }
                    }

                    Toast.makeText(DownloadXzgdActivity.this, "已加入下载,请到我的下载查看下载进度..", Toast.LENGTH_LONG).show();
                    finish();
                }

                break;

            case R.id.dl_xzgd_qx:

                //改变全选按钮状态
                if ("全选".equals(dl_xzgd_qx.getText().toString())){
                    for (PDFOutlineElement pdfOutlineElement : mPdfOutlines) {
                        pdfOutlineElement.setStatusXz(1);
                    }
                    dl_xzgd_qx.setText("取消全选");

                    //设置确定按钮颜色
                    setSureBtAction(true);
                }else{
                    for (PDFOutlineElement pdfOutlineElement : mPdfOutlines) {
                        pdfOutlineElement.setStatusXz(0);
                    }
                    dl_xzgd_qx.setText("全选");

                    //设置确定按钮颜色
                    setSureBtAction(false);
                }
                downloadAdapter.notifyDataSetChanged();

                break;

        }
    }
}
