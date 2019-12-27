package com.example.onlinelearnActivity.courseWare.question;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.adapter.LevelAnswerRecyclerAdapter;
import com.example.entity.AnswerSingle;
import com.example.jsonReturn.AnswerOneFlagReturn;
import com.example.jsonReturn.SubmitAnswerReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.BaseActivity;
import com.example.onlinelearnActivity.PopActivity;
import com.example.view.dialog.AlertDialog;
import com.example.util.JsonUtil;
import com.example.util.SysUtil;
import com.example.view.dialog.MaterialDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LevelAnswerActivity extends BaseActivity {

    protected String TAG = "LevelAnswerActivity";
    private final String REPLY_ONE_ID = "REPLY_ONE_ID";//一级回答主键
    private final String FLOOR = "FLOOR";//一级回答主键

    private String accountId;//用户id
    private String reply_one_id;//一级回答主键

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;//刷新layout
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;//数据列
    @BindView(R.id.answer_bt)
    TextView answer_bt;//回答按钮
    @BindView(R.id.message_tv)
    TextView message_tv;//抽屉界面

    private MaterialDialog secondReplyDialog =null;//二级回复Dialog

    private List<AnswerSingle> datas;//数据
    private Set<AnswerSingle> dataTemp;//阻止重复数据
    private LevelAnswerRecyclerAdapter adapter;

    private int pageNum =1;
    private int pageNumTotal =1;//初始化显示页数
    private int pageCount =10;//每页数目
    private int forumReplySecondCount=-1;//二级回答个数
    private String floor ="0";
    private boolean isLoading =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
    }
    /**
     * 全局信息初始化
     */
    public void initialize(Bundle savedInstanceState) {

        super.initialize(savedInstanceState);
        /**
         * 获取用户信息
         */
        accountId =hdaplication.getStuid();
        reply_one_id=(String) getIntent().getExtras().get(REPLY_ONE_ID);
        floor=(String) getIntent().getExtras().get(FLOOR);
    }

    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        setActivityLeftTitle(floor+"楼");
        setActivityLeftTitleShow(true);
        setToolbar_backShow(true);
    }


    /**
     * 初始化
     * @param view
     */
    protected void initViews(View view) {


        answer_bt.setText("回复");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // 设置adapter
        datas =new ArrayList<>();
        adapter = new LevelAnswerRecyclerAdapter(this, datas);
        recyclerView.setAdapter(adapter);

    }

    /**
     * 设置监听
     */
    public void setListener() {
        //小评论框
        adapter.setOnClickListener(new LevelAnswerRecyclerAdapter.CommentOnClickListener() {
            @Override
            public void onClick(int position) {

                showSecondReplyDialog(datas.get(position));
            }
        });

        refreshLayout.setReboundDuration(500);//回弹动画时长（毫秒）

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                load();
            }
        });
    }

    /**
     * 逻辑处理
     * @param mContext
     */
    protected void doBusiness(Context mContext) {
        //获取二级回答
        getSecoundReplyList();
    }


    /**
     * 获取二级回答线程
     */
    public void getSecoundReplyList(){
        if (!isLoading){
            //显示加载框
            showLoading();
        }
        //调用接口方法
        Call<ResponseBody> call = serverApi.getSecoundReplyList(reply_one_id,String.valueOf(pageNum));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求视频信息成功
                    try {
                        String result =response.body().string();
                        Log.d(JSON_TAG,"获取二级回答返回："+result);
                        AnswerOneFlagReturn answerOneFlagReturn=JsonUtil.parserString(result,AnswerOneFlagReturn.class);
                        if (answerOneFlagReturn.isSuccess()){

                            boolean update =false;
                            dataTemp =new HashSet<AnswerSingle>(datas);//加入重复检查

                            for (AnswerSingle answerSingle:answerOneFlagReturn.getList()){
                                //如果能加入该set
                                if (dataTemp.add(answerSingle)){
                                    adapter.addSingleDate(answerSingle);//datas也加入
                                    update= true;
                                }
                            }
                            dataTemp.clear();//清空
                            if (!update&&isLoading){
                                showToast("暂无更多回复");
                            }

                            pageCount =answerOneFlagReturn.getPageCount();
                            pageNumTotal =answerOneFlagReturn.getPageNum();
                            forumReplySecondCount =adapter.getItemCount();//获取当前二级回答条数

                        }
                        //如果是下拉刷新
                        if (isLoading){
                            loadFinish();
                        }else{
                            //关闭loading
                            closeLoading();
                            isDatasEmpty();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //服务器请求失败
                if (isLoading){
                    loadFinish();
                }else{
                    //关闭loading
                    closeLoading();
                }
                showToast(R.string.server_error);
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
                        Log.d(JSON_TAG,"新增二级回答返回："+ result);
                        SubmitAnswerReturn submitAnswerReturn=JsonUtil.parserString(result,SubmitAnswerReturn.class);
                        if(submitAnswerReturn.isSuccess()){

                            if (forumReplySecondCount< pageCount ||isLastVisibility()){   //小于20或者当前是最后一个，就直接增加

                                adapter.addSingleDate(submitAnswerReturn.getForumReplySecondModel());
                            }
                            //更改回答个数
                            forumReplySecondCount=adapter.getItemCount();
                            //更改总页数
                            pageNumTotal =submitAnswerReturn.getPageNum();

                            if (secondReplyDialog!=null){
                                //关闭二级回答框
                                closeSecondReplyDialog();
                            }
                            showToast("提交成功");
                            isDatasEmpty();
                        }
                        else{
                            showToast("提交失败");
                        }
                        //关闭loading
                        closeLoading();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //服务器请求失败
                showToast("提交失败");
                //关闭loading
                closeLoading();
            }
        });
    }



    @OnClick({R.id.answer_bt})
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){

            case R.id.answer_bt:

                Intent intent = new Intent(this, PopActivity.class);
                intent.putExtra("ACTION","LEVEL_ANSWER_ACTION");
                intent.putExtra(REPLY_ONE_ID,reply_one_id);
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_bottom_in, R.anim.animo_no);
                ActivityCompat.startActivityForResult(this, intent, SUBMIT_LEVEL_ANSWER_SUCCESS,compat.toBundle());
                overridePendingTransition(R.anim.slide_bottom_in, R.anim.animo_no);

                break;

        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case SUBMIT_LEVEL_ANSWER_SUCCESS:
                String result = data.getExtras().getString("result");
                postLevelAnswerSuccess(result);
                break;
            default:
                break;
        }
    }

    /**
     * 提交二级回答成功
     * @param result
     */
    private void postLevelAnswerSuccess(String result){
        SubmitAnswerReturn submitAnswerReturn=JsonUtil.parserString(result,SubmitAnswerReturn.class);

        if (forumReplySecondCount< pageCount ||isLastVisibility()){   //小于20或者当前是最后一个，就直接增加

            adapter.addSingleDate(submitAnswerReturn.getForumReplySecondModel());
        }
        //更改回答个数
        forumReplySecondCount=adapter.getItemCount();
        //更改总页数
        pageNumTotal =submitAnswerReturn.getPageNum();


        if (secondReplyDialog!=null){
            //关闭二级回答框
            closeSecondReplyDialog();
        }
        isDatasEmpty();

    }

    /**
     * datas是否为空
     */
    private void isDatasEmpty() {
        if (!isLoading &&datas.isEmpty()){  //不是加载操作，且集合为0
            message_tv.setText("暂无更多回答");
            message_tv.setVisibility(View.VISIBLE);
        }else{
            message_tv.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 上拉加载操作
     */
    private void load(){
        //当前为加载操作
        isLoading =true;
        //如果当前页<总共页
        if (pageNum<pageNumTotal){
            //加载下一页
            pageNum+=1;
            getSecoundReplyList();
        }else{
            //更新当页
            getSecoundReplyList();
        }
    }
    /**
     * 结束上拉加载
     */
    private void loadFinish(){
        refreshLayout.finishLoadmore();
        isLoading =false;
    }


    /**
     * 判断当前显示是否最后一个
     * @return
     */
    private boolean isLastVisibility(){
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        //这里的findLastVisibleItemPosition总比getItemCount少2,需要-2
        return recyclerView.getLayoutManager().getItemCount() - 3 ==
                ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
    }


    /**
     * 二级回答框
     */
    private void showSecondReplyDialog(final AnswerSingle answerSingle){

        final View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_secondreply_layout, null);
        final EditText secondReplyContent =(EditText)view.findViewById(R.id.secondreply_tv);
        secondReplyContent.setText("");
        TextView beReplier =(TextView)view.findViewById(R.id.title_tv);
        beReplier.setText("@"+answerSingle.getReplierName());

        secondReplyDialog = new MaterialDialog(this)
                .setCanceledOnTouchOutside(true)
                .setPositiveButton("回复", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        submitSecondAnswerThread(secondReplyContent.getText().toString(),answerSingle.getReplierId());
                    }
                });

        secondReplyDialog.setView(view);
        secondReplyDialog.show();

        secondReplyDialog.getPositiveButton().setClickable(false);
        secondReplyDialog.getPositiveButton().setTextColor(getResources().getColor(R.color.color_999999));
        secondReplyContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (secondReplyDialog !=null){
                    if (!SysUtil.isBlank(secondReplyContent.getText().toString())){
                        secondReplyDialog.getPositiveButton().setClickable(true);
                        secondReplyDialog.getPositiveButton().setTextColor(getResources().getColor(R.color.colorPrimary));
                    }else{
                        secondReplyDialog.getPositiveButton().setClickable(false);
                        secondReplyDialog.getPositiveButton().setTextColor(getResources().getColor(R.color.color_999999));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 关闭二级回答框
     */
    private void closeSecondReplyDialog(){
        secondReplyDialog.dismiss();
        secondReplyDialog =null;
    }




}
