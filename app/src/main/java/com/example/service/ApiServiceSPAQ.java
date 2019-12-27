package com.example.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 食品安全接口
 */

public interface ApiServiceSPAQ {


    /**
     * 获取食品安全课程进度
     * @return
     */
    @FormUrlEncoded
    @POST("/app/project/findcurriculum")
    Call<ResponseBody> getSPAQClassProgress(@Field("studeyCode") String studeyCode);

    /**
     * 获取食品安全个人进度
     * @return
     */
    @FormUrlEncoded
    @POST("/app/project/findproject")
    Call<ResponseBody>getSPAQMyProgress(@Field("studeyCode") String studeyCode);

    /**
     * 获取食品安全信息
     * @return
     */
    @FormUrlEncoded
    @POST("/app/project/findinformation")
    Call<ResponseBody>getSPAQMyInfo(@Field("studeyCode") String studeyCode);
}
