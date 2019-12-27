package com.example.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.entity.AnswerSingle;
import com.example.onlinelearn.R;
import com.example.util.DateUtil;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 须知页面的数据加载
 */
public class LevelAnswerRecyclerAdapter extends BaseRecyclerAdapter<AnswerSingle> {

    private Typeface iconfont;
    private CommentOnClickListener listener ;
    public interface CommentOnClickListener {
        void onClick(int position);
    }

    private TextView username_tv;//回答者
    private TextView answer_time_tv;//回答时间
    private TextView answer_content_tv;//回答主体
    private CircleImageView user_avatar_bg;//用户头像
    private TextView comment_bg;//评论icon
    private TextView bereplyer_tv;//被回答者


    public LevelAnswerRecyclerAdapter(Context context, List<AnswerSingle> datas) {
        super(context, R.layout.list_levelanswer_item, datas);
        iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
    }

    @Override
    public void convert(BaseRecyclerHolder holder, AnswerSingle item, final int position) {

        username_tv =holder.getView(R.id.username_tv);
        answer_time_tv = holder.getView(R.id.answer_time_tv);
        answer_content_tv = holder.getView(R.id.answer_content_tv);
        user_avatar_bg = holder.getView(R.id.user_avatar_bg);
        comment_bg = holder.getView(R.id.comment_bg);
        bereplyer_tv = holder.getView(R.id.bereplyer_tv);
        comment_bg.setTypeface(iconfont);

        username_tv.setText(item.getReplierName());
        answer_time_tv.setText(DateUtil.timeToDate((Long)(item.getReplyTime())));
        answer_content_tv.setText(item.getReplyContent());
        comment_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null)
                    listener.onClick(position);
            }
        });
        if (null!=item.getBeReplierId()){
            bereplyer_tv.setText("@"+item.getBeReplierName());
            bereplyer_tv.setVisibility(View.VISIBLE);
        }
        else {
            bereplyer_tv.setVisibility(View.GONE);
        }


    }

    public void setOnClickListener(CommentOnClickListener listener){
        this.listener =listener;
    }

}
