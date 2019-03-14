package com.jstech.gridregulation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseRecyclerAdapter;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckItemBean;

import java.util.List;

public class CheckItemSelectAdapter extends BaseRecyclerAdapter<CheckItemBean> {

    SelectInterface selectListener;

    public CheckItemSelectAdapter(List<CheckItemBean> mDatas, Context mContext, int layoutId) {
        super(mDatas, mContext, layoutId);
    }

    public CheckItemSelectAdapter(List<CheckItemBean> mDatas, Context mContext, int layoutId, SelectInterface selectListener) {
        super(mDatas, mContext, layoutId);
        this.selectListener = selectListener;
    }

    @Override
    protected void bindItemData(ViewHolder viewHolder, final CheckItemBean data, final int position) {
        TextView tvContent = viewHolder.getView(R.id.tv_content);
        TextView tvTableName = viewHolder.getView(R.id.tv_content);

        final CheckBox ckbSelect = viewHolder.getView(R.id.ckb_select);
        tvContent.setText(data.getContent());
        tvTableName.setText(data.getTableid());
        ckbSelect.setChecked(data.isSelected());
        ckbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != selectListener) {
                    selectListener.select(position);
                }
            }
        });
    }


    /**
     * 选择检查表的接口
     */
    public interface SelectInterface {
        /**
         * checkbox状态改变触发的事件
         *
         * @param position 检查表的位置
         * @param
         */
        void select(int position);
    }
}
