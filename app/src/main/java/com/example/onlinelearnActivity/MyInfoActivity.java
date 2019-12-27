package com.example.onlinelearnActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.onlinelearn.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ysg on 2017/10/31.
 */

public class MyInfoActivity extends BaseActivity {

    @BindView(R.id.my_info_name)
    TextView my_info_name;//姓名
    @BindView(R.id.my_info_sex)
    TextView my_info_sex;//性别
    @BindView(R.id.my_info_type)
    TextView my_info_type;//证件类型
    @BindView(R.id.my_info_number)
    TextView my_info_number;//证件号码
    @BindView(R.id.my_info_phone)
    TextView my_info_phone;//手机号码
    @BindView(R.id.my_info_company)
    TextView my_info_company;//单位名称
    @BindView(R.id.my_info_post)
    TextView my_info_post;//岗位
    @BindView(R.id.my_info_layout)
    LinearLayout my_info_layout;//岗位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void doBusiness(Context mContext) {
        //加载
        showLoading();
        //获取数据
        getSPAQMyInfo();

    }
    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        setActivityLeftTitle("我的信息");
        setActivityLeftTitleShow(true);
        setToolbar_backShow(true);
    }

    /**
     * 获取食品安全个人信息
     */
    private void getSPAQMyInfo(){
        Call<ResponseBody> call = serverApiSPAQ.getSPAQMyInfo(hdaplication.getStuCode());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) { //请求成功
                    try {
                        String result =response.body().string();

                        Log.d(JSON_TAG,"获取食品安全个人信息:"+result);
                        JSONObject jsonObject =new JSONObject(result);
                        my_info_name.setText(jsonObject.getString("name"));
                        my_info_sex.setText(setUserSex(jsonObject.getInt("sex")));
                        my_info_type.setText(setPapersType(jsonObject.getInt("papersType")));
                        my_info_number.setText(jsonObject.getString("papersCode").equals("null")? "-":jsonObject.getString("papersCode"));
                        my_info_phone.setText(jsonObject.getString("mobile").equals("null")? "-":jsonObject.getString("mobile"));
                        my_info_company.setText(jsonObject.getString("companyName").equals("null")? "-":jsonObject.getString("companyName"));
                        my_info_post.setText(jsonObject.getString("stationName").equals("null")? "-":jsonObject.getString("stationName"));

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast(R.string.server_error);
                        closeLoading();
                    }
                    my_info_layout.setVisibility(View.VISIBLE);
                }else{
                    showToast(R.string.server_error);
                }
                closeLoading();

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                closeLoading();
                showToast(R.string.server_error);
            }
        });
    }

    /**
     * 设置用户性别
     */
    private String setUserSex(int code){
        String sex = "-";
        switch (code){
            case 1:
                sex ="男";
                break;
            case 0:
                sex ="女";
                break;
        }
        return sex;
    }

    /**
     * 设置用户证件类型
     */
    private String setPapersType(int code){

        String type = "-";
        switch (code){
            case 1:
                type ="身份证";
                break;
            case 2:
                type ="港澳台身份证";
                break;
            case 3:
                type ="护照";
                break;
            case 4:
                type ="军人证";
                break;
            case 5:
                type ="回乡证";
                break;
            case 6:
                type ="其他";
                break;
        }
        return type;
    }

}
