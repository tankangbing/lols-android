package com.example.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * RecyclerView Adapter父类
 */
public abstract class DefaultAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<T> mDatas;
    protected OnItemClickListener mOnItemClickListener;
    protected OnLongItemClickListener mOnLongItemClickListener;

    /**
     * 是否为空
     * @return
     */
    public boolean isEmpty() {

        return mDatas == null || mDatas.size() == 0;
    }

    /**
     * 设置列表中的数据
     * @param datas
     */
    public void setDatas(List<T> datas) {
        if (datas == null) {
            return;
        }
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    /**
     * 获取列表中数据
     * @return
     */
    public List<T> getDatas() {
        return mDatas;
    }

    /**
     * 将单个数据添加到列表中
     * @param t
     */
    public void addSingleDate(T t) {
        if (t == null) {
            return;
        }
        this.mDatas.add(t);
        notifyItemInserted(mDatas.size() - 1);
    }

    /**
     * 插入多个数据
     * @param dates
     * @param position
     */
    public void addDates(List<T> dates, int position) {
        if (dates == null || dates.size() == 0)
            return;
        mDatas.addAll(position, dates);
        notifyItemRangeInserted(position, dates.size());
    }

    /**
     * 移除多个数据
     * @param dates
     * @param position
     */
    public void removeDatas(List<T> dates, int position) {
        if (dates == null || dates.size() == 0)
            return;
        mDatas.removeAll(dates);
        notifyItemRangeRemoved(position, dates.size());
    }

    /**
     * 插入单个数据
     * @param t
     * @param position
     */
    public void addSingleDate(T t, int position) {
        mDatas.add(position, t);
        notifyItemInserted(position);
        // notifyItemRangeChanged(position, mDatas.size());
    }

    /**
     * 移除单个数据
     * @param position
     */
    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
        // notifyItemRangeChanged(position, mDatas.size());
    }

    /**
     * 通过泛型移除单个数据
     * @param t
     */
    public void removeData(T t) {
        int index = mDatas.indexOf(t);
        notifyItemRemoved(index);
        // notifyItemRangeChanged(index, mDatas.size());
    }

    /**
     * 添加一个list
     * @param dates
     * @param isNotify
     */
    public void addDates(List<T> dates, boolean isNotify) {
        if (dates == null || dates.size() == 0) {
            return;
        }
        this.mDatas.addAll(dates);
        if(true){
            notifyDataSetChanged();
        }
    }


    public void addDates(List<T> dates) {
      this.addDates(dates,false);


    }

    public void updateSingleDate(T t, int position){
        mDatas.set(position, t);
       notifyItemChanged(position);
    }

    /**
     * 清空数据
     */
    public void clearDates() {
        if (!isEmpty()) {
            this.mDatas.clear();
        }

    }

    public interface OnItemClickListener<T> {
        void onClick(View view, RecyclerView.ViewHolder holder, T o, int position);

    }

    public interface OnLongItemClickListener<T> {
        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, T o, int position);
    }

    /**
     * 设置点击事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 设置长按点击事件
     *
     * @param onLongItemClickListener
     */
    public void setonLongItemClickListener(OnLongItemClickListener onLongItemClickListener) {
        this.mOnLongItemClickListener = onLongItemClickListener;
    }
}
