package com.example.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.example.entity.LearnClassEntity;
import com.example.entity.SPAQLearnClassEntity;
import com.example.onlinelearn.R;
import com.example.util.CommonUtils;
import com.example.util.DateUtil;
import com.example.util.FinalStr;
import com.thoughtworks.xstream.mapper.Mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 须知页面的数据加载
 */
public class MyClassRecyclerAdapter extends BaseRecyclerAdapter<LearnClassEntity> {

    private Context context;
    private List<SPAQLearnClassEntity> learnClassEntities =null ;
    public MyClassRecyclerAdapter(Context context, List<LearnClassEntity> datas) {
        super(context, R.layout.course_class_item, datas);
        this.context =context;
    }

    @Override
    public void convert(BaseRecyclerHolder holder,final LearnClassEntity item, int position) {


        TextView class_item_title = holder.getView(R.id.class_item_title);
        TextView class_item_teacher = holder.getView(R.id.class_item_teacher);
        TextView class_item_date = holder.getView(R.id.class_item_date);
        TextView class_item_text = holder.getView(R.id.class_item_text);
        ProgressBar class_item_firstBar=holder.getView(R.id.class_item_firstBar);
        ImageView class_image = holder.getView(R.id.class_image);
        TextView class_down_tv =holder.getView(R.id.class_down_btn);
        RelativeLayout progress_layout =holder.getView(R.id.zxxx_progress);
        TextView class_kcsc = holder.getView(R.id.class_kcsc);//课程时长
        TextView class_ygksc = holder.getView(R.id.class_ygksc);//已观看时长
        if (FinalStr.SYSTEM_CODE.equals("GZSPAQ") || FinalStr.SYSTEM_CODE.equals("ZKZXXX")){
            //可下载按钮隐藏
            class_down_tv.setVisibility(View.GONE);
            //progress进度layout隐藏
            progress_layout.setVisibility(View.GONE);
        }else {
            class_kcsc.setVisibility(View.GONE);
            class_ygksc.setVisibility(View.GONE);
        }

        //设置在线学习的进度
        if (item.getPassPercentStr()==null||item.getPassPercentStr().equals("")){
            item.setPassPercentStr("0");
        }
        int percent = (int)Double.parseDouble(item.getPassPercentStr());
        class_item_text.setText(percent+"%");
        class_item_firstBar.setProgress(percent);

        //设置span进度
        if (null !=learnClassEntities){
            for (SPAQLearnClassEntity learnClassEntity :learnClassEntities){
                if (learnClassEntity.getClassCode().equals(item.getClassCode())){
                    class_kcsc.setText("课程时长: "+new BigDecimal(learnClassEntity.getStudyHour()).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    class_ygksc.setText("已观看时长: "+new BigDecimal(learnClassEntity.getCourseLearnHour()).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                }
            }
        }

        String sign =item.getModifyDate() ==null? "":item.getModifyDate().toString();
        Glide.with(context)
                .load(item.getClassPhotoPath())
                .signature(new StringSignature(sign))
                .centerCrop()
                .into(class_image);


        class_item_date.setText(DateUtil.GetSimpleDateFormat(DateUtil.FMTSTR_3).format(item.getLearnStartTime())
                +"至"+DateUtil.GetSimpleDateFormat(DateUtil.FMTSTR_3).format(item.getLearnEndTime()));

        class_item_title.setText(item.getClassName());
        if (item.getTeacherName()!=null && item.getTeacherName().equals("")){
            class_item_teacher.setText("主讲："+item.getTeacherName().equals("")+"教授");
        }else {
            class_item_teacher.setText("主讲：");
        }


    }

    public void setLearnClassEntities(List<SPAQLearnClassEntity> learnClassEntities) {
        this.learnClassEntities = learnClassEntities;
        notifyDataSetChanged();
    }
}
