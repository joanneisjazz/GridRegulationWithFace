package com.jstech.gridregulation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.IllegalCaseAdapter;
import com.jstech.gridregulation.adapter.TestAdapter;
import com.jstech.gridregulation.api.GetCaseApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CaseEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TestActivity extends BaseActivity implements HttpOnNextListener{

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    TestAdapter mAdapter;
    List<CaseEntity> mList;

    HttpManager manager;
    GetCaseApi getCaseApi;

    int lenth = 10;
    int start = 0;
    String by;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_illegal_case_list;
    }

    @Override
    public void initView() {
        setToolBarTitle("违法案件列表");
        setToolSubBarTitle("");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        mAdapter = new TestAdapter(mList, this, R.layout.item_test);
        recyclerView.setAdapter(mAdapter);

        manager = new HttpManager(this, this);
        getCaseApi = new GetCaseApi();
        getCaseApi.setBy(by);
        getCaseApi.setLength(lenth);
        getCaseApi.setStart(start);
        manager.doHttpDeal(getCaseApi);

        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    @Override
    public void onNext(String resulte, String method) {
        JSONObject o = JSON.parseObject(resulte);
        if (!ConstantValue.isSuccess(o)) {
            return;
        }
        mList.clear();
        mList.addAll(o.getJSONArray(ConstantValue.RESULT).toJavaList(CaseEntity.class));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(ApiException e, String method) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }
}
