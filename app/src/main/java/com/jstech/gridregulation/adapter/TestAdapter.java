package com.jstech.gridregulation.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseRecyclerAdapter;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CaseEntity;

import java.util.Arrays;
import java.util.List;

public class TestAdapter extends BaseRecyclerAdapter<CaseEntity> {


    public TestAdapter(List<CaseEntity> mDatas, Context mContext, int layoutId) {
        super(mDatas, mContext, layoutId);
    }


    @Override
    protected void bindItemData(ViewHolder viewHolder, CaseEntity data, final int position) {
        RecyclerView recyclerView = viewHolder.getView(R.id.recyclerview);
        List<String> strings = Arrays.asList(data.getFileaddr().split(","));
        PictureAdapter adapter = new PictureAdapter(strings, mContext, R.layout.item_picture);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView.setAdapter(adapter);
    }

}
