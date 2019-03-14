package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.IllegalCaseAdapter;
import com.jstech.gridregulation.api.GetCaseApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.bean.RegulateResult2Bean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CaseEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class IlleagelCaseListActivity extends BaseActivity implements HttpOnNextListener {

    private TextView tvHint;
    private RecyclerView recyclerView;
    private PtrClassicFrameLayout ptrFrameLayout;

    IllegalCaseAdapter mAdapter;
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
        tvHint = findViewById(R.id.tv_hint);
        recyclerView = findViewById(R.id.recyclerview);
        ptrFrameLayout = findViewById(R.id.fragment_ptr_home_ptr_frame);
        tvHint.setText("无违法案件上报记录");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        mAdapter = new IllegalCaseAdapter(mList, this, R.layout.item_illegal_case);
        recyclerView.setAdapter(mAdapter);

        manager = new HttpManager(this, this);
        getCaseApi = new GetCaseApi();
        by = SharedPreferencesHelper.getInstance(this).getSharedPreference("extId", "").toString();
        getCaseApi.setBy(by);
        getCaseApi.setLength(lenth);
        getCaseApi.setStart(start);
        manager.doHttpDeal(getCaseApi);

        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(IlleagelCaseListActivity.this, IllegalCaseDetailActivity.class);
                intent.putExtra(ConstantValue.ITEMS, mList.get(position));
                startActivity(intent);
            }
        });

        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrFrameLayout.setKeepHeaderWhenRefresh(true);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return PtrDefaultHandler2.checkContentCanBePulledUp(frame, content, footer);
            }


            //加载更多
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                start += lenth;
                getCaseApi.setStart(start);
                manager.doHttpDeal(getCaseApi);
                tvHint.setVisibility(View.GONE);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                tvHint.setVisibility(View.GONE);
                start = 0;
                getCaseApi.setStart(start);
                manager.doHttpDeal(getCaseApi);
            }
        });


    }

    @Override
    public void onNext(String resulte, String method) {
        ptrFrameLayout.refreshComplete();
        JSONObject o = JSON.parseObject(resulte);
        if (!ConstantValue.isSuccess(o)) {
            return;
        }
        if (start == 0) {
            mList.clear();
        }
        JSONArray jsonArray = o.getJSONArray("result");
        if (null == jsonArray || jsonArray.size() <= 0) {
            start = start - lenth;
        } else {
            mList.addAll(o.getJSONArray("result").toJavaList(CaseEntity.class));
            mAdapter.notifyDataSetChanged();
        }

        if (mList.size() > 0) {
            tvHint.setVisibility(View.GONE);
        } else {
            tvHint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(ApiException e, String method) {
        ptrFrameLayout.refreshComplete();
        if (mList.size() >= 0) {
            tvHint.setVisibility(View.GONE);
        } else {
            tvHint.setVisibility(View.VISIBLE);
        }
    }
}
