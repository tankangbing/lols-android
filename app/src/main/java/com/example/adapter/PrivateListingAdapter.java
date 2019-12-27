package com.example.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlinelearn.R;
import com.example.entity.MessageBean;
import com.example.view.SlideView;

import java.util.ArrayList;
import java.util.List;

public class PrivateListingAdapter extends BaseAdapter implements
        SlideView.OnSlideListener {
	private static final String TAG = "SlideAdapter";

	private Context mContext;
	private LayoutInflater mInflater;

	private List<MessageBean> mMessageItems = new ArrayList<MessageBean>();
	private SlideView mLastSlideViewWithStatusOn;

	public PrivateListingAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setmMessageItems(List<MessageBean> mMessageItems) {
		this.mMessageItems = mMessageItems;
	}

	@Override
	public int getCount() {
		if (mMessageItems == null) {
			mMessageItems = new ArrayList<MessageBean>();
		}
		return mMessageItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mMessageItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

        if(position == 0){
            ViewHolder holder;
            SlideView slideView = (SlideView) convertView;
            if (slideView == null) {
                View itemView = mInflater.inflate(R.layout.course_ware_lx_ct_item,
                        null);

                slideView = new SlideView(mContext);
                slideView.setContentView(itemView);

                holder = new ViewHolder(slideView);
                slideView.setOnSlideListener(this);
                slideView.setTag(holder);
            } else {
                holder = (ViewHolder) slideView.getTag();
            }
            MessageBean item = mMessageItems.get(position);
            item.slideView = slideView;
            item.slideView.shrink();

           // holder.ct_iv.setImageResource(item.ct_iv);
            holder.ct_tvtitle.setText(item.ct_tvtitle);
            holder.ct_tv.setText(item.ct_tv);
            holder.deleteHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMessageItems.remove(position);
                    notifyDataSetChanged();
                }
            });

            return slideView;
        }else{
            View itemView = mInflater.inflate(R.layout.course_ware_lx_ct_item,
                        null);

            ViewHolder holder = new ViewHolder(itemView);

            MessageBean item = mMessageItems.get(position);

           // holder.ct_iv.setImageResource(item.ct_iv);
            holder.ct_tvtitle.setText(item.ct_tvtitle);
            holder.ct_tv.setText(item.ct_tv);

            return itemView;
        }
	}

	private static class ViewHolder {
		public ImageView ct_iv;
		public TextView ct_tvtitle;
		public TextView ct_tv;
		public ViewGroup deleteHolder;

		ViewHolder(View view) {
            ct_iv = (ImageView) view.findViewById(R.id.ct_iv);
            ct_tvtitle = (TextView) view.findViewById(R.id.ct_tvtitle);
            ct_tv = (TextView) view.findViewById(R.id.ct_tv);
			deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
		}
	}

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
}
