package com.example.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerAdapter<T> extends DefaultAdapter<T> {

    protected Context mContext;
    protected final int mItemLayoutId;
    protected Typeface iconfont;
    public BaseRecyclerAdapter(Context context, int itemLayoutId) {
        mContext = context;
        mItemLayoutId = itemLayoutId;
        mDatas = new ArrayList<>();
        try{
            iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        }catch (Exception e){
            Log.d("error","context为null错误");
        }

    }

    public BaseRecyclerAdapter(Context context, int itemLayoutId, List<T> datas) {
        mContext = context;
        mItemLayoutId = itemLayoutId;
        mDatas = datas;
        try{
            iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        }catch (Exception e){
            Log.d("error","context为null错误");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        BaseRecyclerHolder holder = new BaseRecyclerHolder(LayoutInflater.from
                (mContext).inflate(mItemLayoutId, parent, false));
        setListener(parent, holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        BaseRecyclerHolder baseHolder = (BaseRecyclerHolder) holder;

        convert(baseHolder, (T) mDatas.get(position), position);
    }

    /**
     * @param holder   自定义的ViewHolder对象，可以getView获取控件
     * @param item     对应的数据
     * @param position
     */
    public abstract void convert(BaseRecyclerHolder holder, T item, int position);

    @Override
    public int getItemCount() {
        return isEmpty() ? 0 : mDatas.size();
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    public void setClickListener(BaseRecyclerHolder holder, int id, View.OnClickListener
            onClickListener) {
        holder.getView(id).setOnClickListener(onClickListener);
    }

    /**
     * 设置监听器
     * @param parent
     * @param viewHolder
     * @param viewType
     */
    protected void setListener(final ViewGroup parent, final BaseRecyclerHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    //这个方法是获取在holder里面真正的位置，而不是对应list的位置
                    int position = viewHolder.getAdapterPosition();
                    T t = mDatas.get(position);
                    mOnItemClickListener.onClick(v, viewHolder, t, position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnLongItemClickListener.onItemLongClick(v, viewHolder, mDatas.get
                            (position), position);
                }
                return false;
            }
        });

    }


}

