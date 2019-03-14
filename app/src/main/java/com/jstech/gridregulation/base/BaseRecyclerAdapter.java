package com.jstech.gridregulation.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suncc on 2016/8/16.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {
    protected List<T> mDatas = new ArrayList<>();
    protected Context mContext;
    AdapterView.OnItemClickListener mItemClickListener;
    protected int layoutId;
    protected LayoutInflater inflater;

    public BaseRecyclerAdapter(List<T> mDatas, Context mContext, int layoutId) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        this.layoutId = layoutId;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T item = getItem(position);
        bindItemData(holder, item, position);
        int pos=holder.getLayoutPosition();
        setupOnItemClick(holder, pos);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    protected abstract void bindItemData(ViewHolder viewHolder, T data, int position);

    protected void setupOnItemClick(final ViewHolder viewHolder, final int position) {
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(null, viewHolder.itemView, position, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public T getItem(int position) {
        position = Math.max(0, position);
        return mDatas.get(position);
    }

    public List<T> getDataSource() {
        return mDatas;
    }

    public void addData(List<T> newItems) {
        if (newItems != null) {
            mDatas.addAll(newItems);
            notifyDataSetChanged();
        }
    }

    public void deleteItem(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void updateListViewData(List<T> lists) {
        mDatas.clear();
        if (lists != null) {
            mDatas.addAll(lists);
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;


        public ViewHolder(View itemView) {
            super(itemView);
            mConvertView = itemView;
            mViews = new SparseArray<>();
        }


        /**
         * 通过viewId获取控件
         *
         * @param viewId
         * @return
         */
        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }
    }


}