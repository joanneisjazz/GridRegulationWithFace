package com.jstech.gridregulation.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.activity.RegulateListAvtivity;
import com.jstech.gridregulation.activity.SiteActivity;
import com.jstech.gridregulation.base.BaseRecyclerAdapter;
import com.jstech.gridregulation.bean.ObjectMarkerBean;
import com.jstech.gridregulation.utils.TextUtil;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;

import java.util.List;

public class RegulateObjectAdapter extends BaseRecyclerAdapter<RegulateObjectBean> {
    OnClickListener listener;

    public RegulateObjectAdapter(List<RegulateObjectBean> mDatas, Context mContext, int layoutId, OnClickListener listener) {
        super(mDatas, mContext, layoutId);
        this.listener = listener;
    }

    public RegulateObjectAdapter(List<RegulateObjectBean> mDatas, Context mContext, int layoutId) {
        super(mDatas, mContext, layoutId);
    }


    @Override
    protected void bindItemData(ViewHolder viewHolder, RegulateObjectBean data, int position) {

        final int pos = position;

        TextView tvAddress = viewHolder.getView(R.id.tv_address);
        TextView tvPoint = viewHolder.getView(R.id.tv_point);
        TextView tvCount = viewHolder.getView(R.id.tv_count);
        TextView tvRate = viewHolder.getView(R.id.tv_rate);
        LinearLayout layoutRegulator = viewHolder.getView(R.id.layout_regulator);
        TextView tvTel = viewHolder.getView(R.id.tv_tel);
        TextView tvRegulator = viewHolder.getView(R.id.tv_regulator);
        TextView tvObjectName = viewHolder.getView(R.id.tv_object_name);
        Button btnCheck = viewHolder.getView(R.id.btn_check);
        LinearLayout layoutCount = viewHolder.getView(R.id.layout_count);
        TextView tvCountText = viewHolder.getView(R.id.tv_count_text);


        tvCountText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvCountText.getPaint().setAntiAlias(true);
        tvCount.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvCount.getPaint().setAntiAlias(true);

        tvObjectName.setText(data.getName());
        tvAddress.setText(data.getAddress());
        tvCount.setText(data.getInspcount());
        tvPoint.setText(data.getEntcredit());
        tvRate.setText(data.getPassrate());
        tvTel.setText(data.getContactPhone());

        if (TextUtil.isEmpty(data.getInspstatus()) || data.getInspstatus().equals(ConstantValue.OBJ_CHECK_STATUS_FINISH)) {
            btnCheck.setText("开启新的检查");
            btnCheck.setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_check_finish));
            layoutRegulator.setVisibility(View.GONE);
        } else {
            btnCheck.setText("继续检查");
            btnCheck.setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_checking));
            layoutRegulator.setVisibility(View.VISIBLE);
            tvRegulator.setText(data.getOisupername());
        }


        if (null != listener) {
            btnCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.check(pos);
                }
            });
            layoutCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.checkRecord(pos);
                }
            });
        }

    }


    public interface OnClickListener {
        void check(int postion);

        void checkRecord(int postion);
    }

}
