package com.example.onlinelearnActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.example.Fragment.FCourseWareTest;
import com.example.Fragment.FTask;
import com.example.Fragment.TopicFragment;
import com.example.Fragment.progress.FCourseProgress;
import com.example.adapter.BaseFragmentAdapter;
import com.example.jsonReturn.CourseReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.download.DownloadXzgdActivity;
import com.example.spt.jaxb.course.Manifest;
import com.example.util.FileUtil;
import com.example.util.FinalStr;
import com.example.util.JsonUtil;
import com.example.util.SpUtils;
import com.example.util.XStreamUtil;
import com.example.view.FlycoTabLayout.SlidingTabLayout;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 课程主页页面
 */

public class CourseClassIndexActivity extends BaseActivity{
    protected String TAG = "CourseClassIndexActivity";

    private String accountId ;
    private String classId;
    private String coursewareId;

    @BindView(R.id.tl_2)
    SlidingTabLayout tl_2;//分页
    @BindView(R.id.vp)
    ViewPager vp;//fragment切换页
    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;//加载框

    private String xmlpath;//xml
    //课程Frgment
    private FCourseWareTest fCourseWareTest;
    //总进入Frgment
    private FCourseProgress fCourseProgress;
    //作业Frgment
    private FTask fTask;
    //问答Frgment
    private TopicFragment topicFragment;

    private Manifest manifest;
    //发布时间
    private String publishTime;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_index);
    }

    /**
     * 全局信息初始化
     */
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);

        Intent intent=getIntent();
        String coursewareIds=intent.getStringExtra("coursewareId");
        String [] strings=coursewareIds.split("#");
        if (strings!=null&&strings.length==3){
            hdaplication.setCoursewareId(strings[0]);
            hdaplication.setClassId(strings[1]);
        }
        publishTime=intent.getStringExtra("publishTime");
        if (publishTime==null||publishTime.equals("")){
            publishTime="0000-00-00 00:00:00";
        }
        /**
         * 获取用户信息
         */
        coursewareId =hdaplication.getCoursewareId();
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

        setActivityRight2Btn(R.string.icon_search);
        setActivitRight2BtnShow(true);
        toolbar_right2.setTypeface(iconfont);
        if (!FinalStr.SYSTEM_CODE.equals("GZSPAQ") && !FinalStr.SYSTEM_CODE.equals("ZKZXXX")){
            setActivityRightBtn(R.string.icon_file_down);
            setActivitRightBtnShow(false);//修复去掉下载
        }
        toolbar_right.setTypeface(iconfont);
    }

    /**
     * 初始化
     */
    protected void initViews(View view){

        topicFragment =TopicFragment.newInstance();
        fCourseWareTest=FCourseWareTest.newInstance();
        fCourseProgress=FCourseProgress.newInstance();
        fTask=FTask.newInstance();

    }

    /**
     * 设置监听
     */
    public void setListener(){

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
/*        xmlpath =FinalStr.DOWNLOAD_PATH+"/course_ware";*/
        File pFile=new File(xmlpath);

        //如果不存在,则创建文件夹
        if (!pFile.exists()) {
            pFile.mkdir();
        }

        //判断课件是否存在
        File file =FileUtil.isExist(xmlpath,coursewareId+".xml");
        if(file!=null){
            return true;
        }
        return false;
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
                        Log.d(JSON_TAG,"课程XML返回："+result);

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
                            showToast("无法加载数据,请检查你的网络连接或者联系我们！");
                            closeLoading();//关闭加载
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToast("无法加载数据,请检查你的网络连接或者联系我们！");
                closeLoading();//关闭加载
            }
        });
    }

    /**
     * 解析xml
     */
    private void analyzeXML(){

        File filePath=new File(xmlpath,coursewareId+".xml");

        manifest=new XStreamUtil().str2JavaFormXML(filePath,Manifest.class);
        try {
            if (manifest!=null&&manifest.getPublishTime()!=null&&!manifest.getPublishTime().equals("")&&manifest.getPublishTime().getTime()>=sdf.parse(publishTime).getTime()){

                String showSummarize=manifest.getOrganizations().getOrganization().get(1).getShowSummarize();
                String showCourseware=manifest.getOrganizations().getOrganization().get(1).getShowCourseware();
                String showWork=manifest.getOrganizations().getOrganization().get(1).getShowWork();
                String showCommunity=manifest.getOrganizations().getOrganization().get(1).getShowCommunity();
                String showSchedule =manifest.getOrganizations().getOrganization().get(1).getShowSchedule();
                //绘制导航栏
                drawNav(showSummarize,showCourseware,showWork,showCommunity,showSchedule,manifest);
            }else {
                File file=new File(getFilesDir().getPath()+"/course_ware_Chapter/"+hdaplication.getClassId()+"/");
                if (file.exists()) {
                    File[] files = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        File[] fs = files[i].listFiles();
                        for (int j = 0; j < fs.length; j++) {
                            fs[j].delete();
                        }
                    }
                }
                //下载xml
                downLoadXML();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 绘制导航栏
     * @param showSummarize
     * @param showCourseware
     * @param showWork
     * @param showCommunity
     * @param showSchedule
     */
    private void drawNav(String showSummarize,String showCourseware,String showWork,String showCommunity,String showSchedule,Manifest manifest){

        List<String> fragmentTitles =new ArrayList<>();
        List<Fragment> mFragments = new ArrayList<>();

        if ("Y".equals(showSummarize)) {
           /* fragmentTitles.add("概论");*/
        }
        if ("Y".equals(showCourseware)) {
            fragmentTitles.add("目录");
            mFragments.add(fCourseWareTest);
        }
        if ("Y".equals(showWork)) {
            fragmentTitles.add("作业");
            mFragments.add(fTask);
        }
        if ("Y".equals(showCommunity)) {
            fragmentTitles.add("问答");
            mFragments.add(topicFragment);
        }
        if ("Y".equals(showSchedule)) {
            fragmentTitles.add("进度");
            mFragments.add(fCourseProgress);
        }
        if (mFragments.size()==1){
            tl_2.setIndicatorColor(Color.TRANSPARENT);
        }

        // 为ViewPager设置适配器
        BaseFragmentAdapter adapter =
                new BaseFragmentAdapter(getSupportFragmentManager(), mFragments
                        , (String[])fragmentTitles.toArray(new String[fragmentTitles.size()]));

        vp.setAdapter(adapter);

        //将ViewPager与TableLayout 绑定在一起
        tl_2.setViewPager(vp);

        closeLoading();//关闭加载



    }

    /**
     * 重写base 显示加载框
     */
    public void showLoading(){
        loading.show();
    }

    /**
     * 重写base 关闭加载
     */
    public void closeLoading(){
        loading.hide();
    }


    /**
     * 按钮操作
     * @param v
     */
    @OnClick({R.id.toolbar_right,R.id.toolbar_right2})
    public void click(View v){
        switch (v.getId()){
            case R.id.toolbar_right:

                startActivity(DownloadXzgdActivity.class);
                break;
            case R.id.toolbar_right2:

                startActivity(SearchActivity.class);
                break;
        }
    }


    public Manifest getManifest() {
        return manifest;
    }

    public void setManifest(Manifest manifest) {
        this.manifest = manifest;
    }


    /**
     * 接收提问回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case SUBMIT_TOPIC_SUCCESS:
                String result = data.getExtras().getString("result");
                topicFragment.postQuestionSuccess(result);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
