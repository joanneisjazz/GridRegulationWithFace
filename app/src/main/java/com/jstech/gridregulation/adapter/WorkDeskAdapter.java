package com.jstech.gridregulation.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseRecyclerAdapter;
import com.jstech.gridregulation.bean.ObjectMarkerBean;
import com.jstech.gridregulation.bean.WorkDeskBean;

import java.util.List;

public class WorkDeskAdapter extends BaseRecyclerAdapter<WorkDeskBean> {


    public WorkDeskAdapter(List<WorkDeskBean> mDatas, Context mContext, int layoutId) {
        super(mDatas, mContext, layoutId);
    }


    @Override
    protected void bindItemData(ViewHolder viewHolder, WorkDeskBean data, int position) {
        TextView tvName = viewHolder.getView(R.id.tv_name);
        ImageView imageView = viewHolder.getView(R.id.image_view);

        tvName.setText(data.getName());
        imageView.setBackground(mContext.getResources().getDrawable(data.getIcon()));
    }

}
