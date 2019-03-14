package com.jstech.gridregulation.adapter;

import android.content.Context;
import android.widget.TextView;

import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseRecyclerAdapter;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CaseEntity;

import java.util.List;

public class IllegalCaseAdapter extends BaseRecyclerAdapter<CaseEntity> {


    public IllegalCaseAdapter(List<CaseEntity> mDatas, Context mContext, int layoutId) {
        super(mDatas, mContext, layoutId);
    }


    @Override
    protected void bindItemData(ViewHolder viewHolder, CaseEntity data, final int position) {
        TextView tvDetails = viewHolder.getView(R.id.tv_details);
        TextView tvObject = viewHolder.getView(R.id.tv_object);
        TextView tvReporterName = viewHolder.getView(R.id.tv_reporter_name);

        TextView tvDate = viewHolder.getView(R.id.tv_date);
        TextView tvSite = viewHolder.getView(R.id.tv_site);

        tvDetails.setText(data.getContent());
        tvObject.setText(data.getSubject());
        tvReporterName.setText(data.getContacts());
        if (null != data.getHapDate() && data.getHapDate().length() > 10)
            tvDate.setText(data.getHapDate().substring(0, 10));
        tvSite.setText(data.getHaplocaddr());
        tvReporterName.setText(data.getReportername());
    }

}
