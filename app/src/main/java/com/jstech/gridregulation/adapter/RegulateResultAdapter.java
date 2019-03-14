package com.jstech.gridregulation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.activity.PictureAvtivity;
import com.jstech.gridregulation.base.BaseRecyclerAdapter;
import com.jstech.gridregulation.utils.TextUtil;
import com.rxretrofitlibrary.greendao.FileEntityDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.FileEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegulateResultAdapter extends BaseRecyclerAdapter<RegulateResultBean> {


    public RegulateResultAdapter(List<RegulateResultBean> mDatas, Context mContext, int layoutId) {
        super(mDatas, mContext, layoutId);
    }


    @Override
    protected void bindItemData(ViewHolder viewHolder, RegulateResultBean data, int position) {

        TextView tvContent = viewHolder.getView(R.id.tv_content);
        TextView tvReason = viewHolder.getView(R.id.tv_unqualified_reason);
        TextView tvResult = viewHolder.getView(R.id.tv_result);
        RecyclerView recyclerView = viewHolder.getView(R.id.recyclerview_pictures);


        tvContent.setText(data.getItemcontent());
        tvResult.setText(TextUtil.getRegulateResult(data.getInspresult()));
        if (ConstantValue.RESULT_UNQUALIFIED.equals(data.getInspresult())) {
            tvReason.setVisibility(View.VISIBLE);
            if (TextUtil.isEmpty(data.getInspdesc())) {
                tvReason.setText(mContext.getResources().getString(R.string.unqualified_reason) + "无");
            } else {
                tvReason.setText(mContext.getResources().getString(R.string.unqualified_reason) + data.getInspdesc());
            }
        } else {
            tvReason.setVisibility(View.GONE);
        }
        setTextColor(data.getInspresult(), tvResult);

        String image = data.getImage();
        final List<String> strings = new ArrayList<>();
        if (TextUtil.isEmpty(image)) {
            FileEntityDao fileEntityDao = MyApplication.getInstance().getSession().getFileEntityDao();
            List<FileEntity> fileEntities = fileEntityDao.queryBuilder().where(
                    FileEntityDao.Properties.TaskId.eq(data.getInspid()),
                    FileEntityDao.Properties.BeanId.eq(data.getBeanId())
            ).list();
            if (null == fileEntities || fileEntities.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
            } else {
                for (FileEntity fileEntity : fileEntities) {
                    strings.add(fileEntity.getLocalPath());
                }
            }
        } else {
            strings.addAll(Arrays.asList(image.split(",")));
        }
        PictureAdapter pictureAdapter = new PictureAdapter(strings, mContext, R.layout.item_picture);
        recyclerView.setAdapter(pictureAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        pictureAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, PictureAvtivity.class);
                intent.putExtra(ConstantValue.SGIN_PATH, strings.get(position));
                mContext.startActivity(intent);
            }
        });

    }

    private void setTextColor(Object code, TextView textView) {
        if (null == code) {
            return;
        }
        String strCode = code.toString();
        if (ConstantValue.RESULT_QUALIFIED.equals(strCode) || ConstantValue.RESULT_BASIC_QUALIFIED.equals(strCode)) {
            textView.setTextColor(mContext.getResources().getColor(R.color.check_item_selected_bg));
        } else {
            textView.setTextColor(mContext.getResources().getColor(R.color.redText));
        }
    }
}
