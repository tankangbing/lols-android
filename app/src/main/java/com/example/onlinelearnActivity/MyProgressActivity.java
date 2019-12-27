package com.example.onlinelearnActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.onlinelearn.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ysg on 2017/10/31.
 */

public class MyProgressActivity extends BaseActivity {

    @BindView(R.id.my_progress_date)
    TextView my_progress_date;//课程有效期
    @BindView(R.id.my_progress_xxrwzsc)
    TextView my_progress_xxrwzsc;//学习任务总时长
    @BindView(R.id.my_progress_ygksc)
    TextView my_progress_ygksc;//已观看时长
    @BindView(R.id.my_progress_wcrwzsc)
    TextView my_progress_wcrwzsc;//完成任务总时长
    @BindView(R.id.my_progress_xsrdzsc)
    TextView my_progress_xsrdzsc;//学时认定总时长
    @BindView(R.id.my_progress_mszsc)
    TextView my_progress_mszsc;//面授总时长
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprogress);
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void doBusiness(Context mContext) {
        //加载
        showLoading();
        //获取数据
        getSPAQMyProgress();
    }

    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        setActivityLeftTitle("我的进度");
        setActivityLeftTitleShow(true);
        setToolbar_backShow(true);
    }

    /**
     * 获取食品安全个人进度
     */
    private void getSPAQMyProgress(){
        Call<ResponseBody> call = serverApiSPAQ.getSPAQMyProgress(hdaplication.getStuCode());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求成功
                    try {
                        String result =response.body().string();

                        Log.d(JSON_TAG,"获取食品安全个人进度:"+result);

                        JSONObject jsonObject =new JSONObject(result);
                        if (jsonObject.getBoolean("result")){

                            my_progress_date.setText("有效期 " + jsonObject.getString("starttime")
                                    +" 至 "+jsonObject.getString("endtime"));
                            my_progress_xxrwzsc.setText(jsonObject.getString("AllLearnHour"));
                            my_progress_wcrwzsc.setText(new BigDecimal(
                                    jsonObject.getString("hadStudyAndConfirmXueShi")).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                            my_progress_ygksc.setText(new BigDecimal(
                                    jsonObject.getString("hadStudyClassHours")).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                            my_progress_xsrdzsc.setText(new BigDecimal(
                                    jsonObject.getString("confirmXueShi")).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                            my_progress_mszsc.setText(new BigDecimal(
                                    jsonObject.getString("kaoQinXueShi")).setScale(2,BigDecimal.ROUND_HALF_UP).toString());

                            closeLoading();
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                showToast(R.string.server_error);
                closeLoading();

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showToast(R.string.server_error);
                closeLoading();
            }
        });
    }
}
