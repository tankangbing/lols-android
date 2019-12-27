package com.example.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.entity.TopicSingle;
import com.example.onlinelearn.R;
import com.example.util.DateUtil;
import com.example.view.shadowviewhelper.ShadowProperty;
import com.example.view.shadowviewhelper.ShadowViewDrawable;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.List;
import java.util.Map;

/**
 * 问答页面数据绑定
 */
public class TopicRecyclerAdapter extends BaseRecyclerAdapter<TopicSingle> {

    private Context context;
    private TextView chapter_tv;//章节名称
    private TextView topic_title_tv;//问题标题
    private TextView question_time_tv;//提问时间
    private TextView answer_num_tv;//回答数量
    private TextView topic_content_tv;//问题内容

    private boolean isNoMore =false;

    public TopicRecyclerAdapter(Context context, List<TopicSingle> datas) {
        super(context, R.layout.list_topic_item, datas);
        this.context =context;
    }

    @Override
    public void convert(BaseRecyclerHolder holder,TopicSingle item, int position) {
        chapter_tv= holder.getView(R.id.chapter_tv);
        topic_title_tv = holder.getView(R.id.topic_title_tv);
        question_time_tv = holder.getView(R.id.question_time_tv);
        answer_num_tv = holder.getView(R.id.answer_num_tv);
        topic_content_tv =holder.getView(R.id.topic_content_tv);
        chapter_tv.setText(item.getChapterName());
        topic_title_tv.setText(item.getPostTitle());
        question_time_tv.setText(DateUtil.timeToDate(item.getAskTime()));
        answer_num_tv.setText(item.getAnswerCount());
        topic_content_tv.setText(item.getPostContent());



        if (isNoMore && position == mDatas.size()-1){
            ClassicsFooter.REFRESH_FOOTER_FINISH = "暂无更多";
        }else{
            ClassicsFooter.REFRESH_FOOTER_FINISH = "加载完成";

        }
    }

    /**
     * 设置没有更多回复
     */
    public void setNoMore(boolean flag){
        isNoMore =flag;
    }
}
