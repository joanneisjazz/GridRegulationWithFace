package com.jstech.gridregulation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseRecyclerAdapter;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;

import java.util.List;

public class UploadSiteCheckDataAdapter extends BaseRecyclerAdapter<TaskBean> {

    IUplaodable iUplaodable;

    public UploadSiteCheckDataAdapter(List<TaskBean> mDatas, Context mContext, int layoutId) {
        super(mDatas, mContext, layoutId);
    }

    public UploadSiteCheckDataAdapter(List<TaskBean> mDatas, Context mContext, int layoutId, IUplaodable iUplaodable) {
        super(mDatas, mContext, layoutId);
        this.iUplaodable = iUplaodable;
    }

    @Override
    protected void bindItemData(ViewHolder viewHolder, TaskBean data,  int position) {
        final TaskBean taskBean = data;

        TextView tvName = viewHolder.getView(R.id.tv_object);
        TextView tvDate = viewHolder.getView(R.id.tv_date);
        Button button = viewHolder.getView(R.id.button);

        if (null != iUplaodable) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iUplaodable.click(taskBean);
                }
            });
        }
        tvDate.setText(data.getCreateDateLocal());
        tvName.setText(data.getEntname());

    }


    public interface IUplaodable {
        void click(TaskBean taskBean);
    }

}
