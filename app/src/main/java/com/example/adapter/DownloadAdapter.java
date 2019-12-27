package com.example.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.entity.PDFOutlineElement;
import com.example.onlinelearn.R;
import com.example.util.FileUtil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by ysg on 2017/8/3.
 */

public class DownloadAdapter extends ArrayAdapter {


    public DownloadAdapter(Context context, int textViewResourceId, List objects) {
        super(context, textViewResourceId, objects);

        mInflater = LayoutInflater.from(context);
        mfilelist = objects;
        iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
    }

    private LayoutInflater mInflater;
    private List<PDFOutlineElement> mfilelist;
    private Typeface iconfont;

    public int getCount() {
        return mfilelist.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder =null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.download_xzgd_ware, null);

            holder.item_layout =(RelativeLayout) convertView.findViewById(R.id.item_layout);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.icon = (TextView) convertView.findViewById(R.id.icon);
            holder.icon2 = (TextView) convertView.findViewById(R.id.icon2);
            holder.size = (TextView) convertView.findViewById(R.id.size);
            holder.icon.setTypeface(iconfont);
            holder.icon2.setTypeface(iconfont);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        int level = mfilelist.get(position).getLevel();
        holder.text.setText(mfilelist.get(position).getOutlineTitle());

        if (level==1) {//第二层目录
            holder.item_layout.setBackgroundColor(getContext().getResources().getColor(R.color.color_f2f4f7));
            holder.icon.setPadding(110 , holder.icon
                    .getPaddingTop(), 0, holder.icon.getPaddingBottom());
            holder.icon.setVisibility(View.INVISIBLE);
            holder.icon.setText("");
            holder.icon2.setTextSize(25);

            if (mfilelist.get(position).getStatusXz()==0){

                holder.icon2.setText(R.string.icon_unselect);
                holder.icon2.setTextColor(getContext().getResources().getColor(R.color.color_999999));
            }else {

                holder.icon2.setText(R.string.icon_select);
                holder.icon2.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            }
            holder.text.setTextColor(getContext().getResources().getColor(R.color.color_999999));
            holder.text.setTextSize(14);
            holder.size.setVisibility(View.GONE);

        }else if (level==2) {

            holder.item_layout.setBackgroundColor(getContext().getResources().getColor(R.color.color_white));
            holder.icon.setPadding(120 , holder.icon
                    .getPaddingTop(), 0, holder.icon.getPaddingBottom());
            holder.icon.setVisibility(View.VISIBLE);
            if (mfilelist.get(position).getBehavior().equals("0")) {//视频
                holder.icon.setText(R.string.icon_cw_video);
            }else if (mfilelist.get(position).getBehavior().equals("1")) {//文档
                holder.icon.setText(R.string.icon_cw_doc);
            }else if (mfilelist.get(position).getBehavior().equals("2")) {//网页
                holder.icon.setText(R.string.icon_cw_video);
            }else if (mfilelist.get(position).getBehavior().equals("3")) {//练习
                holder.icon.setText(R.string.icon_cw_practic);
            }else if (mfilelist.get(position).getBehavior().equals("4")) {//测试
                holder.icon.setText(R.string.icon_cw_test);
            }else if (mfilelist.get(position).getBehavior().equals("4")) {//考试
                holder.icon.setText(R.string.icon_cw_test);
            }
            holder.icon2.setTextSize(25);
            if (mfilelist.get(position).getStatusXz()==0){

                holder.icon2.setText(R.string.icon_unselect);
                holder.icon2.setTextColor(getContext().getResources().getColor(R.color.color_999999));
            }else {

                holder.icon2.setText(R.string.icon_select);
                holder.icon2.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            }
            holder.text.setTextColor(getContext().getResources().getColor(R.color.color_333333));
            holder.text.setTextSize(16);
            holder.size.setVisibility(View.VISIBLE);

            int resourceSize=mfilelist.get(position).getResourceSize();
            String resourceSizeStr= FileUtil.computeFileSize(resourceSize);
            holder.size.setText(resourceSizeStr);

        }else{
            holder.item_layout.setBackgroundColor(getContext().getResources().getColor(R.color.color_white));
            holder.icon.setPadding(10 , holder.icon
                    .getPaddingTop(), 0, holder.icon.getPaddingBottom());
            holder.icon.setVisibility(View.INVISIBLE);
            holder.icon.setText("");
            holder.icon2.setText(R.string.icon_more_down);
            holder.icon2.setTextColor(0xff3193d4);
            holder.text.setTextColor(0xff3193d4);
            holder.text.setTextSize(16);
            holder.size.setVisibility(View.GONE);

        }
        return convertView;
    }

    /**
     * 是否被选中
     */
    public boolean isItemSelected(){

        for ( PDFOutlineElement pdfOutlineElement :mfilelist){
            //如果有被选中
            if (1 ==pdfOutlineElement.getStatusXz()){
                return true;
            }
        }
        return false;
    }

    class ViewHolder {
        TextView text;
        TextView icon;
        TextView icon2;
        TextView size;
        RelativeLayout item_layout;
    }

}
