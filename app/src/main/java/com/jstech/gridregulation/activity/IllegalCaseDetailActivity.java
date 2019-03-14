package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.PictureAdapter;
import com.jstech.gridregulation.base.BaseActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CaseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class IllegalCaseDetailActivity extends BaseActivity {

    TextView tvObject;
    TextView tvReporterName;
    TextView tvReporterPhone;
    TextView tvHapDate;
    TextView tvHapSite;
    TextView tvDetails;

    RecyclerView rvPictures;

    PictureAdapter mAdapter;
    CaseEntity mCaseEntity;

    List<String> mList;


    private void findViews() {
        tvObject = findViewById(R.id.tv_object);
        tvReporterName = findViewById(R.id.tv_reporter_name);
        tvReporterPhone = findViewById(R.id.tv_phone);
        tvHapDate = findViewById(R.id.tv_date);
        tvHapSite = findViewById(R.id.tv_happen_site);
        tvDetails = findViewById(R.id.tv_details);
        rvPictures = findViewById(R.id.recyclerview_pictures);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_case_detail;
    }

    @Override
    public void initView() {
        setToolBarTitle("详情");
        setToolSubBarTitle("");
        findViews();
        mList = new ArrayList<>();
        mCaseEntity = (CaseEntity) getIntent().getSerializableExtra(ConstantValue.ITEMS);
        if (null == mCaseEntity) {
        } else {
            String[] path = mCaseEntity.getFileaddr().split(",");
            if (null != path && path.length > 0) {
                mList.addAll(Arrays.asList(path));
            }
            tvObject.setText(mCaseEntity.getSubject());
            tvReporterPhone.setText(mCaseEntity.getReportertel());
            tvReporterName.setText(mCaseEntity.getContacts());
            if (null != mCaseEntity.getHapDate() && mCaseEntity.getHapDate().length() > 10)
                tvHapDate.setText(mCaseEntity.getHapDate().substring(0, 10));
            tvHapSite.setText(mCaseEntity.getHaplocaddr());
            tvDetails.setText(mCaseEntity.getContent());
        }
        mAdapter = new PictureAdapter(mList, this, R.layout.item_picture);
        rvPictures.setLayoutManager(new GridLayoutManager(this, 4));
        rvPictures.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(IllegalCaseDetailActivity.this, PictureAvtivity.class);
                intent.putExtra(ConstantValue.SGIN_PATH, mList.get(position));
                startActivity(intent);
            }
        });
    }
}
