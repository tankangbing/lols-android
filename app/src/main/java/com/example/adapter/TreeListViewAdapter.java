package com.example.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import android.widget.TextView;

import com.example.entity.PDFOutlineElement;
import com.example.onlinelearn.R;
/**
 * 树形目录
 * */

public class TreeListViewAdapter extends ArrayAdapter {


	public TreeListViewAdapter(Context context, int textViewResourceId,
							   List objects) {
		super(context, textViewResourceId, objects);
		mInflater = LayoutInflater.from(context);
		mfilelist = objects;
        iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");

		mIconCollapse = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.node2);
		mIconExpand = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.node);
		mIconVideo=BitmapFactory.decodeResource(
				context.getResources(), R.drawable.video);
		mIconDoc=BitmapFactory.decodeResource(
				context.getResources(), R.drawable.doc);
		mIconHtml=BitmapFactory.decodeResource(
				context.getResources(), R.drawable.node);
		mIconExercise=BitmapFactory.decodeResource(
				context.getResources(), R.drawable.exercise);
		mIconTest=BitmapFactory.decodeResource(
				context.getResources(), R.drawable.test);
		mIconExam=BitmapFactory.decodeResource(
				context.getResources(), R.drawable.node);
		mIconShow=BitmapFactory.decodeResource(
				context.getResources(), R.drawable.show);
		mIconHide=BitmapFactory.decodeResource(
				context.getResources(), R.drawable.hide);
        mIconNew=BitmapFactory.decodeResource(
                context.getResources(), R.drawable.icon_new4);
        curDate= new Date(System.currentTimeMillis());
	}

	private LayoutInflater mInflater;
	private List<PDFOutlineElement> mfilelist;
	private Bitmap mIconCollapse;
	private Bitmap mIconExpand;

	//video--视频  doc--文档  html--网页 exercise--练习  test--测试 exam--考试
	private Bitmap mIconVideo;
	private Bitmap mIconDoc;
	private Bitmap mIconHtml;
	private Bitmap mIconExercise;
	private Bitmap mIconTest;
	private Bitmap mIconExam;

    protected Typeface iconfont;

	private Bitmap mIconShow;
	private Bitmap mIconHide;
    private Bitmap mIconNew;

    SimpleDateFormat CurrentTime= new SimpleDateFormat("yyyy-MM-dd");
    Date curDate;//获取当前时间
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
		ViewHolder holder;

            int level = mfilelist.get(position).getLevel();
            if (level==1){
                convertView = mInflater.inflate(R.layout.course_ware_item, null);
            }else {
                convertView = mInflater.inflate(R.layout.course_ware, null);
            }
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.icon = (TextView) convertView.findViewById(R.id.icon);
			holder.icon2 = (TextView) convertView.findViewById(R.id.icon2);
            holder.icon_new=(ImageView) convertView.findViewById(R.id.icon_new);
			convertView.setTag(holder);
            holder.icon.setTypeface(iconfont);
            holder.icon2.setTypeface(iconfont);
			/*} else {
				holder = (ViewHolder) convertView.getTag();
			}*/

			/*holder.icon.setPadding(100 * (level ), holder.icon
					.getPaddingTop(), 0, holder.icon.getPaddingBottom());*/
			holder.text.setText(mfilelist.get(position).getOutlineTitle());
			if (level==1) {
                if (mfilelist.get(position).getIsNew()!=null&&mfilelist.get(position).getIsNew().equals("0")){
                    if (mfilelist.get(position).getExpirationDate()!=null&&!mfilelist.get(position).getExpirationDate().equals("")){
                        try {
                            Date time=CurrentTime.parse(mfilelist.get(position).getExpirationDate());
                            if (time.getTime()>curDate.getTime()){
                                holder.icon_new.setVisibility(View.VISIBLE);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
				holder.icon.setPadding(110 , holder.icon
						.getPaddingTop(), 0, holder.icon.getPaddingBottom());
				holder.icon.setVisibility(View.INVISIBLE);
			}else if (level==2) {
				holder.icon.setPadding(120 , holder.icon
						.getPaddingTop(), 0, holder.icon.getPaddingBottom());
				//convertView.setBackgroundColor(0xffffffff);
                if (mfilelist.get(position).getBehavior().equals("0")) {//视频
                    holder.icon.setText(R.string.icon_cw_video);
                }else if (mfilelist.get(position).getBehavior().equals("1")) {//文档
                    holder.icon.setText(R.string.icon_cw_doc);
                }else if (mfilelist.get(position).getBehavior().equals("2")) {//网页
                    holder.icon.setText(R.string.icon_cw_doc);
                }else if (mfilelist.get(position).getBehavior().equals("3")) {//练习
                    holder.icon.setText(R.string.icon_cw_practic);
                }else if (mfilelist.get(position).getBehavior().equals("4")) {//测试
                    holder.icon.setText(R.string.icon_cw_test);
                }else if (mfilelist.get(position).getBehavior().equals("4")) {//考试
                    holder.icon.setText(R.string.icon_cw_test);
                }
				/*if (mfilelist.get(position).getBehavior().equals("0")) {//视频
					holder.icon.setImageBitmap(mIconVideo);
				}else if (mfilelist.get(position).getBehavior().equals("1")) {//文档
					holder.icon.setImageBitmap(mIconDoc);
				}else if (mfilelist.get(position).getBehavior().equals("2")) {//网页
					holder.icon.setImageBitmap(mIconHtml);
				}else if (mfilelist.get(position).getBehavior().equals("3")) {//练习
					holder.icon.setImageBitmap(mIconExercise);
				}else if (mfilelist.get(position).getBehavior().equals("4")) {//测试
					holder.icon.setImageBitmap(mIconTest);
				}else if (mfilelist.get(position).getBehavior().equals("4")) {//考试
					holder.icon.setImageBitmap(mIconExam);
				}*/
			}else{
				holder.icon.setPadding(10 , holder.icon
						.getPaddingTop(), 0, holder.icon.getPaddingBottom());
				//convertView.setBackgroundColor(0xffF5F6F8);
                //holder.icon.setText(R.string.icon_cw_doc);
				if (mfilelist.get(position).isMhasChild()
						&& (mfilelist.get(position).isExpanded() == false)) {
					holder.text.setTextColor(0xff5b5b5b);
                    holder.icon2.setText(R.string.icon_more_left);
                    //holder.icon.setTextColor(0xff000000);
					//holder.icon.setImageBitmap(mIconCollapse);
					//holder.icon2.setImageBitmap(mIconHide);
				} else if (mfilelist.get(position).isMhasChild()
						&& (mfilelist.get(position).isExpanded() == true)) {
					holder.text.setTextColor(0xff3193d4);
					//holder.icon.setImageBitmap(mIconExpand);
					//holder.icon2.setImageBitmap(mIconShow);
                    holder.icon2.setText(R.string.icon_more_down);
                    holder.icon2.setTextColor(0xff3193d4);
				} else if (!mfilelist.get(position).isMhasChild()){
                    holder.icon2.setText(R.string.icon_more_left);
                    //holder.icon.setTextColor(0xff000000);
					//holder.icon.setImageBitmap(mIconCollapse);
					//holder.icon2.setImageBitmap(mIconHide);
				}
			}
		//}

		return convertView;
	}

	class ViewHolder {
		TextView text;
        TextView icon;
        TextView icon2;
        ImageView icon_new;
	}


}
