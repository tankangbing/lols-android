package com.example.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jsonReturn.ChapterFlagReturn;
import com.example.onlinelearn.R;

import java.util.List;

/**
 * 树形目录
 * */

public class TreeListViewAnswerAdapter extends ArrayAdapter {

	private LayoutInflater mInflater;
	private List<ChapterFlagReturn.ChildList> list;
	private Bitmap mIconShow;
	private Bitmap mIconHide;
	private Typeface iconfont;
	public TreeListViewAnswerAdapter(Context context, int textViewResourceId, List list) {
		super(context, textViewResourceId, list);
		mInflater = LayoutInflater.from(context);
		this.list = list;
		mIconShow=BitmapFactory.decodeResource(
				context.getResources(), R.drawable.show);
		mIconHide=BitmapFactory.decodeResource(
				context.getResources(), R.drawable.hide);
		iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
	}


	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

			convertView = mInflater.inflate(R.layout.course_ware, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.icon = (TextView) convertView.findViewById(R.id.icon);
			holder.icon2 = (TextView) convertView.findViewById(R.id.icon2);
			holder.icon2.setTypeface(iconfont);
			int level = list.get(position).getLevel();
			holder.icon.setVisibility(View.INVISIBLE);
			holder.text.setText(list.get(position).getStructureNodeName());
			if (level==1) {
				holder.icon.setPadding(110 , holder.icon.getPaddingTop(), 0, holder.icon.getPaddingBottom());

			}else{
				holder.icon.setPadding(10 , holder.icon
						.getPaddingTop(), 0, holder.icon.getPaddingBottom());

				if (list.get(position).isExpanded() == false) {
					holder.text.setTextColor(0xff5b5b5b);
					holder.icon2.setText(R.string.icon_more_left);
				} else if (list.get(position).isExpanded() == true) {
					holder.text.setTextColor(0xff3193d4);
					holder.icon2.setText(R.string.icon_more_down);
				}
			}

		return convertView;
	}

	class ViewHolder {
		TextView text;
		TextView icon;
		TextView icon2;
	}


}
