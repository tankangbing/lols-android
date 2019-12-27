package com.example.onlinelearnActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adapter.TreeListViewAnswerAdapter;
import com.example.jsonReturn.ChapterFlagReturn;
import com.example.jsonReturn.PostQuestionModel;
import com.example.jsonReturn.SubmitAnswerReturn;
import com.example.jsonReturn.SubmitTopicReturn;
import com.example.onlinelearn.R;
import com.example.util.CommonUtils;
import com.example.util.JsonUtil;
import com.example.util.SpUtils;
import com.example.util.SysUtil;
import com.example.view.dialog.MaterialDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 弹出页面
 */
public class PopActivity extends BaseActivity
{
    protected String TAG = "PopActivity";
    private String accountId ;
    private String classId;
    private String coursewareId;
    private String singleTopicId ;//问题主体id
    private String accountName;//用户名
    private String reply_one_id;

    @BindView(R.id.chapter_btn)
    TextView chapter_btn;//章节点按钮
    @BindView(R.id.more_btn)
    TextView more_btn;//章节点按钮
    @BindView(R.id.topic_title_tv)
    EditText topic_title_tv;//问题title
    @BindView(R.id.topic_title_content)
    EditText topic_title_content;//问题内容
    @BindView(R.id.topic_title_layout)
    LinearLayout topic_title_layout;//回答标题
    @BindView(R.id.chapter_layout)
    LinearLayout chapter_layout;//回答标题

    private TreeListViewAnswerAdapter adapter;
    private String chapterId =null;
    private String chapterName =null;
    private String title ;
    private String content ;
    private String chapterIdCashe =null;//缓存的chapterId
    private String action ;
    private Intent intent;
    private ChapterFlagReturn chapterFlagReturn;//章节点返回
    private List<ChapterFlagReturn.ChildList> parentLists;
    private MaterialDialog chapterDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);
        Log.d("xxxx","aaddd");
    }

    /**
     * 全局信息初始化
     */
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);


        /**
         * 获取用户信息
         */
        intent =getIntent();
        coursewareId =hdaplication.getCoursewareId();
        classId =hdaplication.getClassId();
        accountId =hdaplication.getStuid();
        accountName =hdaplication.getUsername();
        action =intent.getStringExtra("ACTION");
    }

    /**
     * 设置toolbar内容
     * */
    public void setToolbar(){
        super.setToolbar();
        //设置状态栏为白色
        toolbar_status.setTextColor(Color.WHITE);
        setActivityLeftTitleShow(true);

        //如果为提问页面
        if ("TOPIC_ACTION".equals(action)){
            setActivityLeftTitle("提问");
            chapterId =(String) intent.getExtras().get("CHAPTER_ID");
        } else if ("ANSWER_ACTION".equals(action)) {    //回答页面
            setActivityLeftTitle("回答");
            singleTopicId=(String) intent.getExtras().get("SINGLE_TOPIC_KEY");
        }else if ("LEVEL_ANSWER_ACTION".equals(action)){    //回复页面
            setActivityLeftTitle("回复");
            reply_one_id=(String) intent.getExtras().get("REPLY_ONE_ID");

        }
        setToolbar_backShow(true);
        setToolbar_back(R.string.icon_close);
        setActivitRightBtnShow(true);
        setActivityRightBtn("提交");
        //修改颜色
        toolbar_back.setTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar_right.setTextColor(getResources().getColor(R.color.color_e3e3e3));
        toolbar_left_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setBackgroundColor(Color.WHITE);
    }

    /**
     * 初始化
     * @param view
     */
    protected void initViews(View view) {
        more_btn.setTypeface(iconfont);
        toolbar_right.setClickable(false);
        //如果是回答页面或者二级回答页面
        if ("ANSWER_ACTION".equals(action) || "LEVEL_ANSWER_ACTION".equals(action)) {
            topic_title_layout.setVisibility(View.GONE);
        }else if("TOPIC_ACTION".equals(action)){    //如果是提问页面

            //chapter为空
            if (null == chapterId){
                chapter_layout.setVisibility(View.VISIBLE);
            }
            //之前是否有缓存信息
            if (!("".equals(SpUtils.getString(this,SpUtils.QUESTION_TITLE))) || !("".equals(SpUtils.getString(this,SpUtils.QUESTION_CONTENT)))
                    || !("".equals(SpUtils.getString(this,SpUtils.QUESTION_CHAPTER_NAME)))){//缓存信息不为空

                //设置缓存信息
                title =SpUtils.getString(this,SpUtils.QUESTION_TITLE);
                content =SpUtils.getString(this,SpUtils.QUESTION_CONTENT);
                chapterName =SpUtils.getString(this,SpUtils.QUESTION_CHAPTER_NAME);
                chapterIdCashe =SpUtils.getString(this,SpUtils.QUESTION_CHAPTER_ID);

            }
        }
    }

    /**
     * 监听
     */
    protected void setListener(){
        topic_title_tv.addTextChangedListener(textWatcher);
        topic_title_content.addTextChangedListener(textWatcher);
        chapter_btn.addTextChangedListener(textWatcher);

        /**
         * 写在这里是因为要在设置监听之后再更改内容达到监听效果
         */
        topic_title_tv.setText(title);
        topic_title_content.setText(content);
        chapter_btn.setText(chapterName);
    }

    /**
     * 逻辑操作
     * @param mContext
     */
    protected void doBusiness(Context mContext) {

        //一秒后打开软键盘
        new Handler().postDelayed(new Runnable(){
            public void run() {

                InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 1000);

        //获取章节点
        if("TOPIC_ACTION".equals(action) && null == chapterId){
            getChapterThread();
        }


    }

    /**
     * 点击回调
     * @param view
     */
    @OnClick({R.id.toolbar_right,R.id.toolbar_back,R.id.chapter_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            //返回键操作
            case R.id.toolbar_right:

                closeInput();
                //获取textview内内容
                title = topic_title_tv.getText().toString();
                content = topic_title_content.getText().toString();
                content = CommonUtils.replaceLineBlanks(content);//去掉换行符
                if (content.length() >1000){  //去除超出段落
                    content = content.substring(0,1000);
                }
                if ("TOPIC_ACTION".equals(action)){
                    submitQuestion(content);
                } else if ("ANSWER_ACTION".equals(action)) {
                    submitAnswerThread(content);
                }else if ("LEVEL_ANSWER_ACTION".equals(action)) {
                    submitSecondAnswerThread(content,"");
                }
                break;

            case R.id.toolbar_back:
                finish();
                setQuestionCashe();
                break;

            case R.id.chapter_layout:
                showChapterDialog();
                closeInput();
                break;
        }
    }

    /**
     * 文本输入监听器
     */
    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //如果章节点layout是显示的
            if (chapter_layout.getVisibility() ==View.VISIBLE){
                //如果title content charpter 不为空
                if ( !(SysUtil.isBlank(topic_title_content.getText().toString()) || SysUtil.isBlank(topic_title_tv.getText().toString()))
                        &&( !SysUtil.isBlank(chapterId) || !SysUtil.isBlank(chapterIdCashe))){
                    //可以提交
                    toolbar_right.setClickable(true);
                    toolbar_right.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else{
                    //不可提交
                    toolbar_right.setClickable(false);
                    toolbar_right.setTextColor(getResources().getColor(R.color.color_e3e3e3));
                }
            }else{

                //如果 标题layout是显示的
                if (topic_title_layout.getVisibility()==View.VISIBLE){

                    //如果title content 不为空
                    if (!(SysUtil.isBlank(topic_title_content.getText().toString()) || SysUtil.isBlank(topic_title_tv.getText().toString()))){
                        //可以提交
                        toolbar_right.setClickable(true);
                        toolbar_right.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }else {
                        //不可提交
                        toolbar_right.setClickable(false);
                        toolbar_right.setTextColor(getResources().getColor(R.color.color_e3e3e3));
                    }
                }else{
                    //如果 content 不为空
                    if (!SysUtil.isBlank(topic_title_content.getText().toString())){
                        //可以提交
                        toolbar_right.setClickable(true);
                        toolbar_right.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }else{
                        //不可提交
                        toolbar_right.setClickable(false);
                        toolbar_right.setTextColor(getResources().getColor(R.color.color_e3e3e3));
                    }
                }

            }

        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    /* *
    * 提交问题
    */
    private void submitQuestion(String content){

        PostQuestionModel postQuestionModel =new PostQuestionModel();
        postQuestionModel.setPostTitle(title);
        postQuestionModel.setClassId(classId);
        postQuestionModel.setCoursewareId(coursewareId);
        //总课程
        if (null == chapterId){
            postQuestionModel.setChapterId(chapterIdCashe);
        }else{  //单个节点
            postQuestionModel.setChapterId(chapterId);
        }
        postQuestionModel.setHasAttach("N");
        postQuestionModel.setHasReply("N");
        postQuestionModel.setDelFlag("0");
        List<PostQuestionModel> list =new ArrayList<>();
        list.add(postQuestionModel);
        postQuestionThread(content, JsonUtil.BuildJson(list));

    }

    /**
     * 新增问题
     */
    public void postQuestionThread(String content, String property){
        //打开加载框
        showLoading();
        //调用接口方法
        Call<ResponseBody> call = serverApi.submitTopic(accountId,content,property,"Y");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result =response.body().string();
                        Log.d(JSON_TAG,"提交问题的返回:"+result);
                        SubmitTopicReturn submitTopicReturn = JsonUtil.parserString(result,SubmitTopicReturn.class);
                        if (submitTopicReturn.isSuccess()){

                            Bundle bundle = new Bundle();
                            bundle.putString("result",result);
                            intent.putExtras(bundle);
                            setResult(SUBMIT_TOPIC_SUCCESS, intent);
                            //提交成功则清空缓存信息
                            clearQuestionCashe();
                            //退出Activity
                            finish();
                        }else{
                            showToast("提交失败");
                        }
                        closeLoading();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showToast("提交失败");
                closeLoading();
            }
        });
    }


    /**
     * 新增回答线
     */
    public void submitAnswerThread(String replyContent){
        //显示加载框
        showLoading();
        //调用接口方法
        Call<ResponseBody> call = serverApi.submitAnswer(accountId,accountName,singleTopicId,replyContent,"Y","Y");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求视频信息成功
                    try {
                        String result =response.body().string();
                        Log.d(JSON_TAG,"提交回答的返回:"+result);
                        SubmitAnswerReturn submitAnswerReturn=JsonUtil.parserString(result,SubmitAnswerReturn.class);
                        if(submitAnswerReturn.isSuccess()){
                            Bundle bundle = new Bundle();
                            bundle.putString("result",result);
                            intent.putExtras(bundle);
                            setResult(SUBMIT_ANSWER_SUCCESS, intent);
                            finish();
                        }else {
                            showToast("提交失败");
                        }
                        closeLoading();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToast("提交失败");
                closeLoading();
            }
        });
    }

    /**
     * 新增回答线程
     */
    public void submitSecondAnswerThread(String secondReplyContent,String beReplierId){
        //显示加载框
        showLoading();
        //调用接口方法
        Call<ResponseBody> call = serverApi.submitSecondReply(reply_one_id,secondReplyContent,beReplierId,accountId,"Y");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求视频信息成功
                    try {
                        String result =response.body().string();
                        Log.d(JSON_TAG,"提交二级回答的返回:"+result);
                        SubmitAnswerReturn submitAnswerReturn=JsonUtil.parserString(result,SubmitAnswerReturn.class);
                        if(submitAnswerReturn.isSuccess()){

                            Bundle bundle = new Bundle();
                            bundle.putString("result",result);
                            intent.putExtras(bundle);
                            setResult(SUBMIT_LEVEL_ANSWER_SUCCESS, intent);
                            finish();

                        }
                        else{
                            showToast("提交失败");
                        }
                        closeLoading();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showToast("提交失败");
                closeLoading();
            }
        });
    }

    /**
     * 获取章节点
     */
    private void getChapterThread(){

        //调用接口方法
        Call<ResponseBody> call = serverApi.getChapter(coursewareId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        String result =response.body().string();

                        chapterFlagReturn = JsonUtil.parserString(result,ChapterFlagReturn.class);
                        Log.d(JSON_TAG,"获取章节点的返回:"+result);
                        initTreeList();


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
     * 初始化章节点List数据
     */
    private void initTreeList(){

        parentLists =chapterFlagReturn.getNodeList();
        for (int i=0;i<parentLists.size();i++) {
            parentLists.get(i).setLevel(0);
            parentLists.get(i).setExpanded(false);
            List<ChapterFlagReturn.ChildList> childLists=parentLists.get(i).getChildList();
            if (null!=childLists){
                for(ChapterFlagReturn.ChildList child:childLists){
                    child.setLevel(1);
                }
            }
            adapter = new TreeListViewAnswerAdapter(this, R.layout.course_ware,
                    parentLists);
        }

    }

    /**
     * 显示章节点框
     */
    private void showChapterDialog(){
        View view = LayoutInflater.from(PopActivity.this).inflate(R.layout.dialog_listview, null);
        ListView listView =(ListView) view.findViewById(R.id.dialog_lv);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parentLists.get(position).getLevel()==0
                        &&parentLists.get(position).getChildList()!=null){
                    if (!parentLists.get(position).isExpanded()){
                        parentLists.addAll(position+1,parentLists.get(position).getChildList());
                        parentLists.get(position).setExpanded(true);
                    }
                    else{
                        parentLists.removeAll(parentLists.get(position).getChildList());
                        parentLists.get(position).setExpanded(false);
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    chapterName =parentLists.get(position).getStructureNodeName();
                    chapterIdCashe =parentLists.get(position).getStructureNodeId();
                    chapter_btn.setText(chapterName);

                    closeChapterDialog();
                }
            }
        });
        chapterDialog = new MaterialDialog(this)
                .setCanceledOnTouchOutside(true)
                .setContentView(view);
        chapterDialog.show();
    }

    /**
     * 关闭章节点框
     */
    private void closeChapterDialog(){
        chapterDialog.dismiss();
    }

    /**
     * 清除缓存内容
     */
    private void clearQuestionCashe() {

        SpUtils.putString(this,SpUtils.QUESTION_TITLE,"");
        SpUtils.putString(this,SpUtils.QUESTION_CONTENT,"");
        SpUtils.putString(this,SpUtils.QUESTION_CHAPTER_NAME,"");
        SpUtils.putString(this,SpUtils.QUESTION_CHAPTER_ID,"");
    }

    /**
     * 记录缓存内容
     */
    private void setQuestionCashe() {
        SpUtils.putString(this,SpUtils.QUESTION_TITLE,topic_title_tv.getText().toString());
        SpUtils.putString(this,SpUtils.QUESTION_CONTENT,topic_title_content.getText().toString());
        SpUtils.putString(this,SpUtils.QUESTION_CHAPTER_NAME,chapter_btn.getText().toString());
        SpUtils.putString(this,SpUtils.QUESTION_CHAPTER_ID,chapterIdCashe);

    }

    /**
     * 返回按钮操作
     */
    public void onBackPressed() {
        //清空
        setQuestionCashe();
        finish();

    }
    /**
     * 退出该activity
     */
    public void finish(){
        closeInput();
        super.finish();
        overridePendingTransition(0,R.anim.slide_bottom_out);
    }


}
