package com.jstech.gridregulation.activity;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseActivity;

public class PictureAvtivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_picture;
    }

    @Override
    public void initView() {
        setToolBarTitle("图片");
        setToolSubBarTitle("");
        String path = getIntent().getStringExtra(ConstantValue.SGIN_PATH);
        ImageView imageView = findViewById(R.id.image_view);
        Glide.with(this).load(path).into(imageView);
    }
}
