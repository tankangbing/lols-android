package com.example.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.entity.FileInfo;
import com.example.entity.LearnClassEntity;
import com.example.onlinelearn.R;

import java.util.List;

/**
 * 须知页面的数据加载
 */
public class MyDownloadRecyclerAdapter extends BaseRecyclerAdapter<LearnClassEntity> {

    private Context context;
    private boolean isEditMode =false;
    public MyDownloadRecyclerAdapter(Context context, List<LearnClassEntity> datas) {
        super(context, R.layout.download_class_item, datas);
        this.context =context;
    }

    @Override
    public void convert(BaseRecyclerHolder holder,final LearnClassEntity item, int position) {

        TextView dlclass_item_title = holder.getView(R.id.dlclass_item_title);
        TextView dlclass_download_num = holder.getView(R.id.dlclass_download_num);
        TextView dlclass_all_num = holder.getView(R.id.dlclass_all_num);
        TextView dlclass_finish_num = holder.getView(R.id.dlclass_finish_num);
        TextView dlclass_item_size = holder.getView(R.id.dlclass_item_size);
        TextView select =holder.getView(R.id.select);
        select.setTypeface(iconfont);
        select.setTextSize(25);


        ImageView class_image = holder.getView(R.id.class_image);

        dlclass_item_title.setText(item.getClassName());
        dlclass_download_num.setText(item.getDownLoadingNum()+"");
        dlclass_finish_num.setText(item.getDownLoadFinishNum()+"");
        dlclass_item_size.setText(item.getDownLoadFileSize());
        dlclass_all_num.setText(item.getDownLoadAllNum()+"");
        Glide.with(context)
                .load(item.getClassPhotoPath())
                .centerCrop()
                .into(class_image);


        if (isEditMode){
            select.setVisibility(View.VISIBLE);
        }else{
            select.setVisibility(View.INVISIBLE);
        }
        if (item.isSelect()){
            select.setText(R.string.icon_select);
            select.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else{
            select.setText(R.string.icon_unselect);
            select.setTextColor(context.getResources().getColor(R.color.color_999999));
        }
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    /**
     * 清空选择
     */
    public void clearSelect(){
        for (LearnClassEntity learnClassEntity:mDatas){
            learnClassEntity.setSelect(false);
        }
        notifyDataSetChanged();
    }
    /**
     * 全选
     */
    public void allSelect(){
        for (LearnClassEntity learnClassEntity:mDatas){
            learnClassEntity.setSelect(true);
        }
        notifyDataSetChanged();
    }

    /**
     * 是否被选中
     */
    public boolean isItemSelected(){

        for ( LearnClassEntity learnClassEntity :mDatas){
            //如果有被选中
            if (learnClassEntity.isSelect()){
                return true;
            }
        }
        return false;
    }

}
