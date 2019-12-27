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
 * Created by TJR on 2017/7/11.
 */

public interface ApiService {

    /**
     * 版本校验
     * @param version
     * @param systemCode
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?search-version")
    Call<ResponseBody> search_version(@Field("version") String version,
                                      @Field("systemCode") String systemCode);

    /**
     * 登录
     * @param loginId
     * @param password
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?login")
    Call<ResponseBody> login(@Field("loginId") String loginId,
                             @Field("password") String password,
                             @Field("type") String type);

    /**
     * 获取课程list
     * @param loginId
     * @param password
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?courseClassList")
    Call<ResponseBody> getClassList(@Field("classType") String loginId,
                                    @Field("page") String password,
                                    @Field("accountId") String type);


    /**
     * 下载xml
     * @param coursewareId
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?getShowCatalog")
    Call<ResponseBody> getShowCatalog(@Field("coursewareId") String coursewareId);


    /**
     * 获取视频/文档信息
     * @param accountId
     * @param classId
     * @param behaviorId
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?showVideo")
    Call<ResponseBody> getFileMessage(@Field("accountId") String accountId,
                                      @Field("classId") String classId,
                                      @Field("behaviorId") String behaviorId);

    /**
     * 获取广告
     * @param behaviorId
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?getAdvertisementPath")
    Call<ResponseBody> getAdvertisementPath(@Field("behaviorId") String behaviorId,
                                            @Field("classId") String classId);

    /**
     * 获取学习进度
     * @param accountId
     * @param classId
     * @param behaviorId
     * @param operationStatus
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?showVideoSpeed")
    Call<ResponseBody> showVideoSpeed(@Field("accountId") String accountId,
                                      @Field("classId") String classId,
                                      @Field("behaviorId") String behaviorId,
                                      @Field("operationStatus")String operationStatus);

    /**
     * 提交学习行为
     * @param accountId
     * @param classId
     * @param behaviorId
     * @param startPoint
     * @param endPoint
     * @param accountName
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?saveLearningTraces")
    Call<ResponseBody> saveLearningTraces(@Field("accountId") String accountId,
                                          @Field("classId") String classId,
                                          @Field("behaviorId") String behaviorId,
                                          @Field("startPoint")int startPoint,
                                          @Field("endPoint")int endPoint,
                                          @Field("accountName") String accountName);

    /**
     * 下载文件
     * @param fileUrl
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    /**
     * 提交文档学习行为
     * @param accountId
     * @param classId
     * @param behaviorId
     * @param accountName
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?dLFile")
    Call<ResponseBody> postDocumentLearningTraces(@Field("accountId") String accountId,
                                                  @Field("classId") String classId,
                                                  @Field("behaviorId") String behaviorId,
                                                  @Field("accountName") String accountName);

    /**
     * 获取问题list
     * @param accountId
     * @param classId
     * @param coursewareId
     * @param forumPostType
     * @param queryCondition
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?getForumPostList")
    Call<ResponseBody> getForumPostList(@Field("accountId") String accountId,
                                        @Field("classId") String classId,
                                        @Field("coursewareId") String coursewareId,
                                        @Field("chapterId")String chapterId,
                                        @Field("forumPostType") String forumPostType,
                                        @Field("queryCondition") String queryCondition,
                                        @Field("page") String page);

    /**
     * 新增问题
     * @param accountId    用户id
     * @param postContent  上传问题的主体
     * @param postJSONString   包括 帖子标题，班级主键，课件主键，章节点主键，是否有附件，是否回答，删除标记位
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?addForumPost")
    Call<ResponseBody>submitTopic(@Field("accountId") String accountId,
                                  @Field("postContent") String postContent,
                                  @Field("postJSONString") String postJSONString,
                                  @Field("getModel")String getModel);

    /**
     * 获取章节点
     * @param coursewareId
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?getChapter")
    Call<ResponseBody>getChapter(@Field("coursewareId") String coursewareId);


    /**
     * 获取问题主体
     * @param postId
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?questionPage")
    Call<ResponseBody>getQuestionContent(@Field("postId") String postId);


    /**
     * 新增一级回答
     * @param accountId
     * @param accountName
     * @param postId
     * @param replyContent
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?submitAnswer")
    Call<ResponseBody>submitAnswer(@Field("accountId") String accountId,
                                   @Field("accountName") String accountName,
                                   @Field("postId") String postId,
                                   @Field("replyContent") String replyContent,
                                   @Field("getModel")String getModel,
                                   @Field("getPageCount")String getPageCount);

    /**
     * 获取一级回答
     * @param postId
     * @param page
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?getForumReplyFirstList")
    Call<ResponseBody>getForumReplyFirstList(@Field("postId") String postId,
                                             @Field("accountId") String accountId,
                                             @Field("page") String page);


    /**
     * 点赞或者取消点赞
     * @param accountId
     * @param accountName
     * @param replyFirstId  一级回复的id
     * @param optType   类型
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?addOrUpdateReplyFirstOpt")
    Call<ResponseBody>addOrUpdateReplyFirstOpt(@Field("accountId") String accountId,
                                               @Field("accountName") String accountName,
                                               @Field("replyFirstId") String replyFirstId,
                                               @Field("optType") String optType);


    /**
     * 获取二级回答
     * @param replyFirstId
     * @param getSecoundReplyTimes
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?getSecoundReplyList")
    Call<ResponseBody>getSecoundReplyList(@Field("replyFirstId") String replyFirstId,
                                          @Field("getSecoundReplyTimes") String getSecoundReplyTimes);


    /**
     *新增二级回答
     * @param replyFirstId
     * @param secondReplyContent
     * @param beReplierId
     * @param accountId
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?submitSecondReply")
    Call<ResponseBody>submitSecondReply(@Field("replyFirstId") String replyFirstId,
                                        @Field("secondReplyContent") String secondReplyContent,
                                        @Field("beReplierId") String beReplierId,
                                        @Field("accountId") String accountId,
                                        @Field("getPageCount")String getPageCount);


    /**
     * 批量提交学习明细
     * @param dataJsonString
     * @param isLast
     * @param accountId
     * @param classId
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?offLineSaveLearningTraces")
    Call<ResponseBody>offLineSaveLearningTraces(@Field("dataJsonString") String dataJsonString,
                                                @Field("isLast") String isLast,
                                                @Field("accountId") String accountId,
                                                @Field("classId") String classId,
                                                @Field("behaviorId") String behaviorId);

    /**
     * 批量提交学习明细
     * @param dataJsonString
     * @param isLast
     * @param accountId
     * @param classId
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?offLineDLFile")
    Call<ResponseBody>offLineDLFile(@Field("dataJsonString") String dataJsonString,
                                        @Field("isLast") String isLast,
                                        @Field("accountId") String accountId,
                                        @Field("classId") String classId,
                                        @Field("behaviorId") String behaviorId);

    /**
     * 获取启动页广告
     * @return
     */
    @POST("appControler.do?getStartPagePicturePath")
    Call<ResponseBody>getStartPagePicturePath();


    /**
     * 获取课程列表
     * @return
     */
    @FormUrlEncoded
    @POST("appControler.do?courseClassList")
    Call<ResponseBody>courseClassList(@Field("classType") String classType,
                                      @Field("page") String page,
                                      @Field("accountId") String accountId);

}
