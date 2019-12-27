package com.example.view.multiSeekbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.example.entity.VideoSpeedSingle;

import java.util.List;


/**
 * Created by TJR on 2017/7/3.
 */

public class MultiColorSeekbar extends SeekBar {

    //背景图片
    MultiColorSeekbarDrawable drawable;

    public MultiColorSeekbar(Context context) {
        super(context);
        init();
    }

    public MultiColorSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiColorSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化seekbar
     */
    private void init(){
        setProgress(0);
        drawable = new MultiColorSeekbarDrawable();
        setProgressDrawable(drawable);
    }

    /**
     * 刷新seekbar数据
     * @param list
     * @param videoTotalTime
     */
    public void refreshData(List<VideoSpeedSingle> list, int videoTotalTime,int lastProgress){
        setProgress(lastProgress);
        setMax(videoTotalTime);//设置最大值
        drawable.refreshBackground(list,videoTotalTime);//刷新背景数据
        refreshDrawableState();//刷新背景图片
    }


}
