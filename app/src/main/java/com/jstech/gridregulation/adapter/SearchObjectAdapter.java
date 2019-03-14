package com.jstech.gridregulation.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseRecyclerAdapter;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;

import java.util.List;

/**
 * Created by hesm on 2018/9/24.
 */

public class SearchObjectAdapter extends BaseRecyclerAdapter<RegulateObjectBean> {
    public SearchObjectAdapter(List<RegulateObjectBean> mDatas, Context mContext, int layoutId) {
        super(mDatas, mContext, layoutId);
    }

    @Override
    protected void bindItemData(ViewHolder viewHolder, RegulateObjectBean data, int position) {

        TextView textView = viewHolder.getView(R.id.tv_object_name);
        textView.setText(data.getName());
        ImageView ivMarker = viewHolder.getView(R.id.iv_marker);
        if (ConstantValue.NATURE_STORE.equals(data.getNature())) {
            ivMarker.setBackground(mContext.getResources().getDrawable(R.mipmap.ic_farm_capital_store_marker));
        } else {
            ivMarker.setBackground(mContext.getResources().getDrawable(R.mipmap.ic_operating_entity_marker));
        }

    }
}
