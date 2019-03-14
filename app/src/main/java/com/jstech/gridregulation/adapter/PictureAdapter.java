package com.jstech.gridregulation.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseRecyclerAdapter;

import java.util.List;

public class PictureAdapter extends BaseRecyclerAdapter<String> {


    public PictureAdapter(List<String> mDatas, Context mContext, int layoutId) {
        super(mDatas, mContext, layoutId);
    }


    @Override
    protected void bindItemData(ViewHolder viewHolder, String data, final int position) {
        ImageView imageView = viewHolder.getView(R.id.image_view);
        Glide.with(mContext).load(data).into(imageView);

    }

}
