package com.jstech.gridregulation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseRecyclerAdapter;
import com.jstech.gridregulation.utils.TextUtil;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;

import java.util.List;

public class RegulateAdapter extends BaseRecyclerAdapter<TaskBean> {


    int mType;

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public RegulateAdapter(List<TaskBean> mDatas, Context mContext, int layoutId) {
        super(mDatas, mContext, layoutId);
    }

    public RegulateAdapter(List<TaskBean> mDatas, Context mContext, int layoutId, int type) {
        super(mDatas, mContext, layoutId);
        this.mType = type;
    }


    @Override
    protected void bindItemData(ViewHolder viewHolder, TaskBean data, final int position) {
        TextView tvObject = viewHolder.getView(R.id.tv_object);
        TextView tvOp = viewHolder.getView(R.id.tv_opinoin);
        TextView tvResult = viewHolder.getView(R.id.tv_result);
        TextView tvDate = viewHolder.getView(R.id.tv_date);
        TextView tvItems = viewHolder.getView(R.id.tv_items);

        TextView tvRegulator = viewHolder.getView(R.id.tv_regulator);
        LinearLayout layoutRegulator = viewHolder.getView(R.id.layout_regulator);

        tvResult.setText(TextUtil.getRegulateResult(data.getInspresult()));

        if (TextUtil.isEmpty(data.getResultstr())) {
            if (TextUtil.isEmpty(data.getState5()) || !data.getState5().equals("1")) {
                tvItems.setText("正在检查中");
            } else if (TextUtil.isEmpty(data.getState6()) || !data.getState6().equals("1")) {
                tvItems.setText("未上传");
            }
        } else {
            tvItems.setText(data.getResultstr());
        }

        tvObject.setText(data.getEntname());
        tvOp.setText(data.getInspopinion());

        if (!TextUtil.isEmpty(data.getCreateDate())) {
            tvDate.setText(data.getCreateDate());
        } else {
            tvDate.setText(data.getCreateDateLocal());
        }
        setTextColor(data.getInspresult(), tvResult);

        if (1 != mType) {
            layoutRegulator.setVisibility(View.GONE);
            return;
        }
        layoutRegulator.setVisibility(View.VISIBLE);
        if (null != data.getPepole()) {
            tvRegulator.setText(data.getPepole().getRoName());
        } else {
            tvRegulator.setText(data.getRegulatorName());
        }

    }

    private void setTextColor(Object code, TextView textView) {
        if (null == code) {
            return;
        }
        String strCode = code.toString();
        if (ConstantValue.RESULT_QUALIFIED.equals(strCode)) {
            textView.setTextColor(mContext.getResources().getColor(R.color.check_item_selected_bg));
        } else if (ConstantValue.RESULT_BASIC_QUALIFIED.equals(strCode)) {
            textView.setTextColor(mContext.getResources().getColor(R.color.check_item_selected_bg));
        } else if (ConstantValue.RESULT_UNQUALIFIED.equals(strCode)) {
            textView.setTextColor(mContext.getResources().getColor(R.color.redText));
        }
    }

}
