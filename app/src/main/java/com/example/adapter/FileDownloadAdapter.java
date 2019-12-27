package com.example.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.entity.FileInfo;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.download.DownloadXxxwActivity;
import com.example.util.FileUtil;
import com.example.view.RoundProgressBar;
import com.example.view.SlideView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by DELL on 2017/8/7.
 */

public class FileDownloadAdapter extends BaseAdapter implements
        SlideView.OnSlideListener {
    public final int DOWNLOAD_VIEW =0;
    public final int EDIT_VIEW =1;

    private DownloadXxxwActivity mContext = null;
    private List<FileInfo> mFileList = null;
    private LayoutInflater layoutInflater;

    private int status =DOWNLOAD_VIEW; //下载页面
    private Typeface iconfont;
    @Override
    public int getCount() {
        return mFileList.size();
    }

    public FileDownloadAdapter(DownloadXxxwActivity mContext, List<FileInfo> mFileList) {
        super();
        this.mContext = mContext;
        this.mFileList = mFileList;
        layoutInflater = LayoutInflater.from(mContext);
        iconfont = Typeface.createFromAsset(mContext.getAssets(), "iconfont.ttf");
    }
    @Override
    public Object getItem(int position) {
        return mFileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SlideView slideView = (SlideView) convertView;
        Holder holder;
        if (slideView == null) {
            View itemView = layoutInflater.inflate(R.layout.download_xxxw_item,
                    null);

            slideView = new SlideView(mContext);
            slideView.setContentView(itemView);

            holder = new Holder(slideView);
            holder.select_iv.setTypeface(iconfont);
            holder.file_icon.setTypeface(iconfont);
            slideView.setOnSlideListener(this);
            slideView.setTag(holder);
        } else {
            holder = (Holder) slideView.getTag();
        }

/*        Holder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.download_xxxw_item, null);
            holder = new Holder();
            holder.file_name_tv =(TextView) convertView.findViewById(R.id.file_name_tv);
            holder.file_chapter_tv =(TextView) convertView.findViewById(R.id.file_chapter_tv);
            holder.file_size_tv =(TextView) convertView.findViewById(R.id.file_size_tv);
            holder.down_speed_tv =(TextView) convertView.findViewById(R.id.down_speed_tv);
            holder.download_pb =(RoundProgressBar) convertView.findViewById(R.id.download_pb);
            holder.deleteHolder = (ViewGroup) convertView.findViewById(R.id.holder);
            holder.download_pb.setMax(100);
            holder.select_iv =(TextView) convertView.findViewById(R.id.select_iv);
            holder.file_icon =(TextView)convertView.findViewById(R.id.file_icon);
            holder.select_iv.setTypeface(iconfont);
            holder.file_icon.setTypeface(iconfont);
            convertView.setTag(holder);

        } else {
            holder = (Holder)convertView.getTag();
        }*/

        holder.file_name_tv.setText(mFileList.get(position).getFile_name());
        holder.file_chapter_tv.setText(mFileList.get(position).getChapterName());
        holder.select_iv.setTextSize(25);
        if (status ==0){
            mFileList.get(position).slideView = slideView;
            mFileList.get(position).slideView.shrink();
            holder.deleteHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击删除
                    mContext.delfile(mFileList.get(position));
                }
            });
            holder.select_iv.setVisibility(View.INVISIBLE);
            holder.download_pb.setVisibility(View.VISIBLE);

            if (0==mFileList.get(position).getProgress_status()){

                holder.download_pb.setStatus(0);
                holder.file_size_tv.setText("");
                holder.down_speed_tv.setText("");

            }else if (1==mFileList.get(position).getProgress_status()){

                holder.download_pb.setStatus(1);
                holder.file_size_tv.setText(FileUtil.computeFileSize(mFileList.get(position).getFile_finish())+" / "+FileUtil.computeFileSize(mFileList.get(position).getFile_size()));
                holder.file_size_tv.setTextColor(mContext.getResources().getColor(R.color.color_999999));
                holder.down_speed_tv.setText(FileUtil.computeFileSize(mFileList.get(position).getSecondDownloadSizeTotal())+"/S");

            }else if (2==mFileList.get(position).getProgress_status()){

                holder.download_pb.setStatus(2);
                holder.file_size_tv.setText(FileUtil.computeFileSize(mFileList.get(position).getFile_size()));
                holder.down_speed_tv.setText("");

            }else if (3==mFileList.get(position).getProgress_status()){

                holder.download_pb.setStatus(1);
                holder.file_size_tv.setText("等待下载");
                holder.file_size_tv.setTextColor(mContext.getResources().getColor(R.color.color_999999));
                holder.down_speed_tv.setText("");
            }else if (4==mFileList.get(position).getProgress_status()){

                holder.download_pb.setStatus(0);
                holder.file_size_tv.setText("解析下载地址错误");
                holder.file_size_tv.setTextColor(mContext.getResources().getColor(R.color.color_fd2557));
                holder.down_speed_tv.setText("");
            }
        }else{
            mFileList.get(position).slideView = null;
            holder.select_iv.setVisibility(View.VISIBLE);
            holder.download_pb.setVisibility(View.INVISIBLE);
            if (mFileList.get(position).isSelect()){
                holder.select_iv.setText(R.string.icon_select);
                holder.select_iv.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            }else {
                holder.select_iv.setText(R.string.icon_unselect);
                holder.select_iv.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            }
        }

        holder.download_pb.setProgress((int)((float)mFileList.get(position).getFile_finish() * 100
                /(float) mFileList.get(position).getFile_size()));

        if ("0".equals(mFileList.get(position).getFile_code())){
            holder.file_icon.setText(R.string.icon_cw_video);
        }else{
            holder.file_icon.setText(R.string.icon_cw_doc);
        }

        return slideView;
    }

    private SlideView mLastSlideViewWithStatusOn;
    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null
                && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }


    private static class Holder {
        public TextView file_name_tv = null;
        public TextView file_size_tv = null;
        public TextView down_speed_tv = null;
        public TextView file_chapter_tv =null;
        public TextView file_icon =null;
        public RoundProgressBar download_pb = null;
        public TextView select_iv =null;
        public ViewGroup deleteHolder =null;
        Holder(View view) {
            file_name_tv =(TextView) view.findViewById(R.id.file_name_tv);
            file_chapter_tv =(TextView) view.findViewById(R.id.file_chapter_tv);
            file_size_tv =(TextView) view.findViewById(R.id.file_size_tv);
            down_speed_tv =(TextView) view.findViewById(R.id.down_speed_tv);
            download_pb =(RoundProgressBar) view.findViewById(R.id.download_pb);
            download_pb.setMax(100);
            select_iv =(TextView) view.findViewById(R.id.select_iv);
            file_icon =(TextView)view.findViewById(R.id.file_icon);
            deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
        }
    }

    /**
     * 设置显示什么
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    /**
     * 清空选择
     */
    public void clearSelect(){
        for (FileInfo fileInfo:mFileList){
            fileInfo.setSelect(false);
        }
        notifyDataSetChanged();
    }
    /**
     * 全选
     */
    public void allSelect(){
        for (FileInfo fileInfo:mFileList){
            fileInfo.setSelect(true);
        }
        notifyDataSetChanged();
    }

    /**
     * 是否被选中
     */
    public boolean isItemSelected(){

        for ( FileInfo fileInfo :mFileList){
            //如果有被选中
            if (fileInfo.isSelect()){
                return true;
            }
        }
        return false;
    }


}
