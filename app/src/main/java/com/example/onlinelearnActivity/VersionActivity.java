package com.example.onlinelearnActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.adapter.DefaultAdapter;
import com.example.adapter.FunctionRecyclerAdapter;
import com.example.onlinelearn.R;
import com.example.util.SpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class VersionActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;//数据列表
    @BindView(R.id.login_login_btn)
    Button login_login_btn;

    List<String> functionNames ;
    FunctionRecyclerAdapter functionRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versition);
    }

    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        setActivityLeftTitle("设置");
        setActivityLeftTitleShow(true);
        setToolbar_backShow(true);
    }

    @Override
    protected void initViews(View view) {

        functionNames =new ArrayList<>();
        functionNames.add("版本更新");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        functionRecyclerAdapter = new FunctionRecyclerAdapter(this, functionNames);
        recyclerView.setAdapter(functionRecyclerAdapter);


    }

    public void setListener(){
        functionRecyclerAdapter.setOnItemClickListener(new DefaultAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                if (functionNames.get(position).equals("版本更新")){

                    String oldversion= SpUtils.getString(VersionActivity.this,SpUtils.OLD_VERSION);
                    String newversion= SpUtils.getString(VersionActivity.this,SpUtils.NEW_VERSION);
                    if (oldversion.equals(newversion)){
                        showToast("当前版本:"+oldversion+" 暂无版本更新");
                    }else{
                        showUpdateDialog(SpUtils.getString(VersionActivity.this,SpUtils.URL)
                                ,SpUtils.getString(VersionActivity.this,SpUtils.RANK)
                                ,SpUtils.getString(VersionActivity.this,SpUtils.MSG));
                    }
                }
            }
        });

    }
    @Override
    protected void doBusiness(Context mContext) {
        //判断是否有版本更新
    }

    /**
     * 点击操作
     * @param v
     */
    @OnClick({R.id.login_login_btn,R.id.toolbar_back})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_login_btn:
                SharedPreferences preferences=getSharedPreferences("user",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("stuid", "");
                editor.putString("userXm", "");
                editor.putString("username", "");
                editor.commit();
                //停止下载任务
                downloadService.stopAll(hdaplication.getStuid());
                //清除所以activity
                hdaplication.deAllActivity();


                startActivity(LoginActivity.class);
                finish();
                break;
            case R.id.toolbar_back:
                finish();
                break;

        }
    }
}
