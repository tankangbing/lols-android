package com.example.adapter;

import android.content.Context;

import com.example.adapter.BaseRecyclerAdapter;
import com.example.adapter.BaseRecyclerHolder;
import com.example.onlinelearn.R;

import java.util.List;

/**
 * 须知页面的数据加载
 */
public class VideoNoticeAdapter extends BaseRecyclerAdapter<String> {

    public VideoNoticeAdapter(Context context, List<String> datas) {
        super(context, R.layout.video_notice_item, datas);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, String item, int position) {
    }
}
