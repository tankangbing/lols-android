package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.entity.AnswerAdapterModel;
import com.example.onlinelearn.R;
import com.example.util.DateUtil;
import com.example.view.shadowviewhelper.ShadowProperty;
import com.example.view.shadowviewhelper.ShadowViewDrawable;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 须知页面的数据加载
 */
public class AnswerRecyclerAdapter extends BaseRecyclerAdapter<AnswerAdapterModel> {

    public interface ViewListener {
        void onclick(View view,AnswerAdapterModel item,int position);
    }
    public final String ACTION_UP ="0";//点赞
    public final String ACTION_DOWN ="2";//踩

    private ViewListener viewListener;
    private TextView chapter_tv;//章节名称
    private TextView topic_title_tv;//问题标题
    private TextView topic_conten_tv;//标题主体
    private TextView questioner_tv;//提问者
    private TextView question_time_tv;//提问时间
    private TextView answer_num_tv;//回答数量
    private TextView username_tv;//回答者
    private TextView floor_num_tv;//楼层
    private TextView answer_time_tv;//回答时间
    private TextView answer_content_tv;//回答主体
    private TextView good_num_tv;//赞数量
    private TextView bad_num_tv;//踩数量
    private TextView comment_tv;//评论数量
    private CircleImageView user_avatar_bg;//用户头像
    private TextView good_bg;//赞icon
    private TextView bad_bg;//踩icon
    private TextView comment_bg;//评论icon
    private LinearLayout l1,l2,l3;

    private RelativeLayout badview_ly,goodview_ly,commentview_ly;

    private Typeface iconfont;
    public AnswerRecyclerAdapter(Context context, List<AnswerAdapterModel> datas) {
        super(context, R.layout.list_answer_item, datas);
        iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
    }

    @Override
    public void convert(BaseRecyclerHolder holder, final AnswerAdapterModel item, final int position) {

        chapter_tv= holder.getView(R.id.chapter_tv);
        topic_title_tv = holder.getView(R.id.topic_title_tv);
        topic_conten_tv = holder.getView(R.id.topic_conten_tv);
        questioner_tv = holder.getView(R.id.questioner_tv);
        question_time_tv = holder.getView(R.id.question_time_tv);
        answer_num_tv = holder.getView(R.id.answer_num_tv);
        username_tv =holder.getView(R.id.username_tv);
        floor_num_tv= holder.getView(R.id.floor_num_tv);
        answer_time_tv = holder.getView(R.id.answer_time_tv);
        answer_content_tv = holder.getView(R.id.answer_content_tv);
        good_num_tv = holder.getView(R.id.good_num_tv);
        bad_num_tv = holder.getView(R.id.bad_num_tv);
        comment_tv = holder.getView(R.id.comment_tv);
        user_avatar_bg = holder.getView(R.id.user_avatar_bg);
        good_bg = holder.getView(R.id.good_bg);
        bad_bg = holder.getView(R.id.bad_bg);
        comment_bg = holder.getView(R.id.comment_bg);

        l1 = holder.getView(R.id.l1);
        l2 = holder.getView(R.id.l2);
        l3 = holder.getView(R.id.l3);

        goodview_ly =holder.getView(R.id.goodview_ly);
        badview_ly =holder.getView(R.id.badview_ly);
        commentview_ly =holder.getView(R.id.commentview_ly);


        comment_bg.setTypeface(iconfont);
        good_bg.setTypeface(iconfont);
        bad_bg.setTypeface(iconfont);
        //如果是第一个item
        if (position==0){

            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.VISIBLE);
            l3.setVisibility(View.GONE);

            chapter_tv.setText(mDatas.get(position).getTopicSingle().getChapterName());
            topic_title_tv.setText(mDatas.get(position).getTopicSingle().getPostTitle());
            topic_conten_tv.setText(mDatas.get(position).getTopicSingle().getPostContent());
            questioner_tv.setText(mDatas.get(position).getTopicSingle().getQuestionerName());
            question_time_tv.setText(DateUtil.timeToDate((Long)(mDatas.get(position).getTopicSingle().getAskTime())));
            answer_num_tv.setText(mDatas.get(position).getTopicSingle().getAnswerCount());


        } else{
            l1.setVisibility(View.GONE);
            l2.setVisibility(View.GONE);
            l3.setVisibility(View.VISIBLE);


            username_tv.setText(mDatas.get(position).getAnswerSingle().getReplierName());
            floor_num_tv.setText(String.valueOf(position+1));
            answer_time_tv.setText(DateUtil.timeToDate((Long)(mDatas.get(position).getAnswerSingle().getReplyTime())));
            answer_content_tv.setText(mDatas.get(position).getAnswerSingle().getReplyContent());
            good_num_tv.setText(String.valueOf(mDatas.get(position).getAnswerSingle().getUpTimes()));
            bad_num_tv.setText(String.valueOf(mDatas.get(position).getAnswerSingle().getDownTimes()));
            comment_tv.setText(mDatas.get(position).getAnswerSingle().getForumReplySecondCount());

            String currentOpt =mDatas.get(position).getAnswerSingle().getCurrentOpt();

            if (ACTION_UP.equals(currentOpt)){
                good_bg.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                bad_bg.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            }else if (ACTION_DOWN.equals(currentOpt)){
                bad_bg.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                good_bg.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            }else{
                good_bg.setTextColor(mContext.getResources().getColor(R.color.color_999999));
                bad_bg.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            }

            //监听点赞
            goodview_ly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (viewListener!=null)
                        viewListener.onclick(v,item,position);
                }
            });
            //监听踩
            badview_ly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (viewListener!=null)
                        viewListener.onclick(v,item,position);
                }
            });
            //监听评论
            commentview_ly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewListener!=null)
                        viewListener.onclick(v,item,position);
                }
            });



        }
    }

    public void setViewListener(ViewListener listener){
        this.viewListener =listener;
    }

}
