package com.jstech.gridregulation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseRecyclerAdapter;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckItemBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;

import java.util.List;

public class CheckResultAdapter extends BaseRecyclerAdapter<RegulateResultBean> {
    MethodInterface listener;

    public CheckResultAdapter(List<RegulateResultBean> mDatas, Context mContext, int layoutId) {
        super(mDatas, mContext, layoutId);
    }

    public CheckResultAdapter(List<RegulateResultBean> mDatas, Context mContext, int layoutId, MethodInterface listener) {
        super(mDatas, mContext, layoutId);
        this.listener = listener;
    }

    @Override
    protected void bindItemData(final ViewHolder viewHolder, final RegulateResultBean data, final int position) {

        TextView tvContent = viewHolder.getView(R.id.tv_content);
        TextView tvCheckMethod = viewHolder.getView(R.id.tv_method);
        TextView tvTitle = viewHolder.getView(R.id.tv_title);
        final TextView tvQualified = viewHolder.getView(R.id.tv_qualified);
        final TextView tvBasicQualified = viewHolder.getView(R.id.tv_basic_qualified);
        final TextView tvUnqualified = viewHolder.getView(R.id.tv_unqualified);
        final TextView tvUnqualifiedReason = viewHolder.getView(R.id.tv_unqualified_reason);

        tvContent.setText(data.getItemcontent());

        //检查方法
        tvCheckMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.showMethod(data.getId());
                }
            }
        });
        String result = data.getInspresult();

        if (ConstantValue.RESULT_BASIC_QUALIFIED.equals(result)) {
            setSelectStyle(tvBasicQualified, tvQualified, tvUnqualified);
            tvUnqualifiedReason.setVisibility(View.GONE);
        } else if (ConstantValue.RESULT_QUALIFIED.equals(result)) {
            setSelectStyle(tvQualified, tvBasicQualified, tvUnqualified);
            tvUnqualifiedReason.setVisibility(View.GONE);
        } else if (ConstantValue.RESULT_UNQUALIFIED.equals(result)) {
            setSelectStyle(tvUnqualified, tvBasicQualified, tvQualified);
            tvUnqualifiedReason.setVisibility(View.VISIBLE);
        } else {
            setUnSelectStyle(tvQualified, tvBasicQualified, tvUnqualified);
            tvUnqualifiedReason.setVisibility(View.GONE);
        }

        //检查结果--合格
        tvQualified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setSelectStyle(tvQualified, tvBasicQualified, tvUnqualified);
                tvUnqualifiedReason.setVisibility(View.GONE);
                listener.selectResult(ConstantValue.RESULT_QUALIFIED, data, viewHolder);

            }
        });

        //检查结果--基本合格
        tvBasicQualified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setSelectStyle(tvBasicQualified, tvQualified, tvUnqualified);
                tvUnqualifiedReason.setVisibility(View.GONE);
                listener.selectResult(ConstantValue.RESULT_BASIC_QUALIFIED, data, viewHolder);

            }
        });

        //检查结果--不合格
        tvUnqualified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setInspresult(ConstantValue.RESULT_UNQUALIFIED);
//                setSelectStyle(tvUnqualified, tvQualified, tvBasicQualified);
//                tvUnqualifiedReason.setVisibility(View.VISIBLE);
                listener.selectResult(ConstantValue.RESULT_UNQUALIFIED, data, viewHolder);
            }
        });
    }

    public interface MethodInterface {
        void showMethod(String id);

        void selectResult(String result, RegulateResultBean data, ViewHolder viewHolder);
    }

    /**
     * 设置选中和未选中的样式
     */
    private void setSelectStyle(TextView tvSeleted, TextView tvUnSeleted1, TextView tvSeleted2) {
        tvSeleted.setBackground(mContext.getResources().getDrawable(R.drawable.bg_check_item_result_selected));
        tvUnSeleted1.setBackground(mContext.getResources().getDrawable(R.drawable.bg_check_item_result_unselect));
        tvSeleted2.setBackground(mContext.getResources().getDrawable(R.drawable.bg_check_item_result_unselect));
    }

    /**
     * 设置未选中的样式
     *
     * @param tvSeleted
     * @param tvUnSeleted1
     * @param tvSeleted2
     */
    private void setUnSelectStyle(TextView tvSeleted, TextView tvUnSeleted1, TextView tvSeleted2) {
        tvSeleted.setBackground(mContext.getResources().getDrawable(R.drawable.bg_check_item_result_unselect));
        tvUnSeleted1.setBackground(mContext.getResources().getDrawable(R.drawable.bg_check_item_result_unselect));
        tvSeleted2.setBackground(mContext.getResources().getDrawable(R.drawable.bg_check_item_result_unselect));
    }
}
