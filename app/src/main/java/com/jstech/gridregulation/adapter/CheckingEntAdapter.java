package com.jstech.gridregulation.adapter;

import android.content.Context;
import android.widget.TextView;

import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseRecyclerAdapter;
import com.jstech.gridregulation.bean.ObjectMarkerBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CaseEntity;

import java.util.List;

public class CheckingEntAdapter extends BaseRecyclerAdapter<ObjectMarkerBean> {


    public CheckingEntAdapter(List<ObjectMarkerBean> mDatas, Context mContext, int layoutId) {
        super(mDatas, mContext, layoutId);
    }


    @Override
    protected void bindItemData(ViewHolder viewHolder, ObjectMarkerBean data, final int position) {
        TextView tvName = viewHolder.getView(R.id.tv_ent_name);
        tvName.setText(data.getBean().getName());
    }

}
