package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.SearchObjectAdapter;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.utils.KeyboardUtils;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;
//import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBeanDao;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hesm on 2018/9/24.
 */

public class SearchRegulateObjectActivity extends BaseActivity implements View.OnClickListener {

    Toolbar toolbar;
    EditText edtSearch;
    RecyclerView recyclerView;

    TextView tvCrop, tvBreed, tvStore;
    ImageView ivBack;
    LinearLayout linearLayout;

    List<RegulateObjectBean> data = new ArrayList<>();
    ArrayList<RegulateObjectBean> list = new ArrayList<>();

    SearchObjectAdapter mAdapter;

//    RegulateObjectBeanDao regulateObjectBeanDao;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_regulate_object;
    }

    @Override
    public void initView() {
        findViews();
//        regulateObjectBeanDao = MyApplication.getInstance().getSession().getRegulateObjectBeanDao();
//        data.addAll(regulateObjectBeanDao.queryBuilder().where(
//                RegulateObjectBeanDao.Properties.UserId.eq(MyApplication.getInstance().getUserBean().getUserExtId())).list());
        data = (List<RegulateObjectBean>) getIntent().getSerializableExtra(ConstantValue.OBJECT_COUNT);
        list.addAll(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchObjectAdapter(data, this, R.layout.item_searh_object);
        recyclerView.setAdapter(mAdapter);
        setlistener();
    }


    private void findViews() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        linearLayout = findViewById(R.id.layout_search);
        tvCrop = findViewById(R.id.tv_crop);
        tvStore = findViewById(R.id.tv_store);
        tvBreed = findViewById(R.id.tv_breed);
        edtSearch = findViewById(R.id.edt_search);
        recyclerView = findViewById(R.id.recyclerview);
        ivBack = findViewById(R.id.iv_back);

    }

    /**
     * 设置输入框的监听，模糊搜索符合条件的企业
     */
    private void setlistener() {

        tvBreed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtils.hideKeyboard(SearchRegulateObjectActivity.this);
                tvBreed.setTextColor(getResources().getColor(R.color.check_item_selected_bg));
                tvCrop.setTextColor(getResources().getColor(R.color.black));
                tvStore.setTextColor(getResources().getColor(R.color.black));
                getObjectsByType(ConstantValue.NATURE_PRODUCTION, ConstantValue.NATURE_PRODUCTION_BREED);

            }
        });
        tvCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtils.hideKeyboard(SearchRegulateObjectActivity.this);
                tvCrop.setTextColor(getResources().getColor(R.color.check_item_selected_bg));
                tvBreed.setTextColor(getResources().getColor(R.color.black));
                tvStore.setTextColor(getResources().getColor(R.color.black));
                getObjectsByType(ConstantValue.NATURE_PRODUCTION, ConstantValue.NATURE_PRODUCTION_CROP);
            }
        });
        tvStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtils.hideKeyboard(SearchRegulateObjectActivity.this);
                tvStore.setTextColor(getResources().getColor(R.color.check_item_selected_bg));
                tvCrop.setTextColor(getResources().getColor(R.color.black));
                tvBreed.setTextColor(getResources().getColor(R.color.black));
                getObjectsByType(ConstantValue.NATURE_STORE, "");
            }
        });
        ivBack.setOnClickListener(this);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Observable.just(charSequence.toString())
                        .observeOn(Schedulers.newThread())
                        .map(new Func1<String, List<RegulateObjectBean>>() {

                            @Override
                            public List<RegulateObjectBean> call(String s) {
                                data.clear();
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getName().contains(s)) {
                                        data.add(list.get(i));
                                    }
                                }
                                return data;
                            }
//                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<RegulateObjectBean>>() {
                            @Override
                            public void call(List<RegulateObjectBean> o) {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra(ConstantValue.OBJECT_ID, data.get(i).getId());
                intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, data.get(i));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }


    private void getObjectsByType(String type, String blongedTrade) {
        data.clear();
        if ("".equals(blongedTrade)) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getNature().equals(type)) {
                    data.add(list.get(i));
                }
            }
//            data.addAll(regulateObjectBeanDao.queryBuilder().where(
//                    RegulateObjectBeanDao.Properties.Nature.eq(type)
//            ).list());
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getNature().equals(type) && list.get(i).getBelongedTrade().equals(blongedTrade)) {
                    data.add(list.get(i));
                }
            }
//            data.addAll(regulateObjectBeanDao.queryBuilder().where(
//                    RegulateObjectBeanDao.Properties.Nature.eq(type), RegulateObjectBeanDao.Properties.BelongedTrade.eq(blongedTrade)
//            ).list());
        }

        mAdapter.notifyDataSetChanged();
    }
}
