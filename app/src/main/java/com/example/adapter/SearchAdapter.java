package com.example.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.entity.FileInfo;
import com.example.entity.SearchItem;
import com.example.spt.jaxb.course.Item;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.download.DownloadXxxwActivity;

import java.util.List;

/**
 * Created by ysg on 2018/1/24.
 */

public class SearchAdapter extends BaseAdapter {


    private Context mContext = null;
    private List<SearchItem> mItemList = null;
    private Typeface iconfont;
    private LayoutInflater layoutInflater;
    private String content;
    private boolean isShow;

    public SearchAdapter(Context mContext, List<SearchItem> itemList ,String content, boolean isShow) {
        super();
        this.mContext = mContext;
        this.mItemList = itemList;
        this.content = content;
        this.isShow = isShow;
        layoutInflater = LayoutInflater.from(mContext);
        iconfont = Typeface.createFromAsset(mContext.getAssets(), "iconfont.ttf");
    }
    @Override
    public int getCount() {
        if(!isShow){
            return 9;
        }else{
            isShow = true;
            return mItemList.size()+3;
        }
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    @Override
    public Object getItem(int i) {
        if(i==0){
            return null;
        }else if(i > mItemList.size()){
            return null;
        }else{
            return mItemList.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(i==0){
            View itemView = layoutInflater.inflate(R.layout.activity_search_item_title,
                    null);
            return itemView;
        }else if((isShow && i == mItemList.size()+1)|| (!isShow && i == 7)){
            View itemView = new View(mContext);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 50); // 每行的水平LinearLayout
            itemView.setLayoutParams(layoutParams);
            itemView.setBackgroundColor(0xffe3e3e3);
            return itemView;
        }else if(!isShow && i == 6){
            View itemView = layoutInflater.inflate(R.layout.activity_search_showall,
                    null);
            TextView search_showall_icon =(TextView) itemView.findViewById(R.id.search_showall_icon);
            search_showall_icon.setTypeface(iconfont);
            return itemView;
        }else if((isShow && i == mItemList.size()+2)|| (!isShow &&  i == 8)){
            View itemView = layoutInflater.inflate(R.layout.activity_search_item,
                    null);
            TextView search_item_behavoir =(TextView) itemView.findViewById(R.id.search_item_behavoir);
            String text = "搜一搜 <font color='#FF0000'>"+content+"</font>";
            search_item_behavoir.setText(Html.fromHtml(text));
            TextView search_item_icon =(TextView) itemView.findViewById(R.id.search_item_icon);
            TextView search_item_ej =(TextView) itemView.findViewById(R.id.search_item_ej);
            search_item_ej.setText("问答等");
            search_item_icon.setTypeface(iconfont);
            search_item_icon.setText(R.string.icon_wd);
            return itemView;
        }else{
            View itemView = layoutInflater.inflate(R.layout.activity_search_item,
                    null);
            TextView search_item_behavoir =(TextView) itemView.findViewById(R.id.search_item_behavoir);
            String title = mItemList.get(i-1).getTitle();
            if(!content.isEmpty()&&title.contains(content)){//==加非空判断修复搜索为空的bug
                String [] item = title.split(content);
                title = item[0];
                for (int x = 1 ; x < item.length;x++){
                    title += "<font color='#FF0000'>"+content+"</font>" + item[x];//修复<搜索>，“英语（二）”课程，输入1，点击搜索，列表没有显示完整搜索结果的名称将1改为x
                }
                if (item.length == 1) {
                    title += "<font color='#FF0000'>"+content+"</font>";
                }
            }
            search_item_behavoir.setText(Html.fromHtml(title));
            TextView search_item_icon =(TextView) itemView.findViewById(R.id.search_item_icon);
            TextView search_item_ej =(TextView) itemView.findViewById(R.id.search_item_ej);
            String nodeTitle = mItemList.get(i-1).getNodeTitle();
            if(!content.isEmpty()&&nodeTitle.contains(content)){//==加非空判断修复搜索为空的bug
                String [] item = nodeTitle.split(content);
                if(item!=null&&item.length>0){
                    nodeTitle = item[0];
                    for (int x = 1 ; x < item.length;x++){
                        nodeTitle += "<font color='#FF0000'>"+content+"</font>" + item[x];//修复<搜索>，“英语（二）”课程，输入1，点击搜索，列表没有显示完整搜索结果的名称将1改为x
                    }
                }else{
                    nodeTitle = "<font color='#FF0000'>"+content+"</font>";
                }
            }
            search_item_ej.setText(Html.fromHtml(nodeTitle));
            search_item_icon.setTypeface(iconfont);
            if (mItemList.get(i-1).getType().equals("0")) {//视频
                search_item_icon.setText(R.string.icon_cw_video);
            }else if (mItemList.get(i-1).getType().equals("1")) {//文档
                search_item_icon.setText(R.string.icon_cw_doc);
            }else if (mItemList.get(i-1).getType().equals("2")) {//网页
                search_item_icon.setText(R.string.icon_cw_video);
            }else if (mItemList.get(i-1).getType().equals("3")) {//练习
                search_item_icon.setText(R.string.icon_cw_practic);
            }else if (mItemList.get(i-1).getType().equals("4")) {//测试
                search_item_icon.setText(R.string.icon_cw_test);
            }else if (mItemList.get(i-1).getType().equals("4")) {//考试
                search_item_icon.setText(R.string.icon_cw_practic);
            }
            return itemView;
        }
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
