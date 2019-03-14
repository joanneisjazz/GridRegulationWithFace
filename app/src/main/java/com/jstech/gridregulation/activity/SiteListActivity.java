package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.RegulateObjectAdapter;
import com.jstech.gridregulation.api.GetObjectApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.utils.SystemUtil;
import com.jstech.gridregulation.widget.MyPopupWindow;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
//import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.EnterpriseEntityDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;
//import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBeanDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hesm on 2018/11/22.
 */

public class SiteListActivity extends BaseActivity implements HttpOnNextListener, RegulateObjectAdapter.OnClickListener {
    final public static int REQUEST_REARCH = 1;

    private PtrClassicFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private RelativeLayout layoutSearch;
    private ImageView ivSearch;
    private ImageView ivCancel;

    private HttpManager manager;
    private GetObjectApi getObjectApi;

    private ArrayList<RegulateObjectBean> mRegulateObjectArrayList = new ArrayList<>();//监管对象list
    private ArrayList<RegulateObjectBean> mData = new ArrayList<>();//

//    private RegulateObjectBeanDao dao;
//    private EnterpriseEntityDao enterpriseEntityDao;

    private RegulateObjectAdapter mAdapter;
    private int start = 0;
    private int length = 10;

    private MyPopupWindow newTaskWindow;//是否开启新的检查的窗口

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regulate_object_list;
    }

    @Override
    public void initView() {
        setToolBarTitle(getResources().getString(R.string.site_regulate));
        setToolSubBarTitle("地图");
        initWindow();
        ivSearch = findViewById(R.id.iv_search);
        ivCancel = findViewById(R.id.iv_cancel);
        recyclerView = findViewById(R.id.recyclerView);
        layoutSearch = findViewById(R.id.layout_search);
        ptrFrameLayout = findViewById(R.id.ptr_layout);
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
                start += length;
                getObjectApi.setStart(start);
                manager.doHttpDeal(getObjectApi);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                start = 0;
                getObjectApi.setStart(start);
                manager.doHttpDeal(getObjectApi);
            }
        });

        layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SiteListActivity.this, SearchRegulateObjectActivity.class);
                intent.putExtra(ConstantValue.OBJECT_COUNT, mRegulateObjectArrayList.size());
                startActivityForResult(intent, REQUEST_REARCH);
            }
        });


        manager = new HttpManager(this, (RxAppCompatActivity) this);
        getObjectApi = new GetObjectApi();
        getObjectApi.setId(MyApplication.getInstance().getUserBean().getOrgId());
        getObjectApi.setLength(length);
        getObjectApi.setStart(start);

        mAdapter = new RegulateObjectAdapter(mRegulateObjectArrayList, SiteListActivity.this, R.layout.layout_map_bottom_window, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(SiteListActivity.this));
        recyclerView.setAdapter(mAdapter);

//        enterpriseEntityDao = MyApplication.instance.getSession().getEnterpriseEntityDao();
//        dao = MyApplication.instance.getSession().getRegulateObjectBeanDao();
//        mRegulateObjectArrayList.addAll(dao.queryBuilder()
//                .where(RegulateObjectBeanDao.Properties.UserId.eq(MyApplication.getInstance().getUserBean().getUserExtId()))
//                .list());
        mData.addAll(mRegulateObjectArrayList);
        if (mRegulateObjectArrayList.size() != 0) {
            mAdapter.notifyDataSetChanged();
        } else {
            manager.doHttpDeal(getObjectApi);
        }

        getSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SiteListActivity.this, SiteMapActivity.class));
                finish();
            }
        });


        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRegulateObjectArrayList.clear();
                mRegulateObjectArrayList.addAll(mData);
                mAdapter.notifyDataSetChanged();
                ivCancel.setVisibility(View.GONE);
                ivSearch.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onNext(String resulte, String method) {
        JSONObject o = JSON.parseObject(resulte);
        String code = o.getString(ConstantValue.CODE);
        if (!ConstantValue.CODE_SUCCESS.equals(code)) {
            return;
        }

        //保存数据
        Observable.just(o)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Func1<JSONObject, List<RegulateObjectBean>>() {
                    @Override
                    public List<RegulateObjectBean> call(JSONObject object) {
                        //更新数据库
                        List<RegulateObjectBean> beans = object.getJSONArray("result").toJavaList(RegulateObjectBean.class);
                        if (0 == beans.size()) {
                            start = start - length;
                            return null;
                        }
                        if (0 == start) {
                            mData.clear();
                            mRegulateObjectArrayList.clear();
//                            enterpriseEntityDao.deleteAll();
//                            dao.deleteAll();
                        }
                        for (RegulateObjectBean r : beans) {
                            r.setUserId(MyApplication.getInstance().getUserBean().getUserExtId());
                            mRegulateObjectArrayList.add(r);
                            mData.add(r);
//                            enterpriseEntityDao.save(r.getEnterprise());
//                            dao.save(r);
                        }

                        return mRegulateObjectArrayList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<RegulateObjectBean>>() {
                    @Override
                    public void call(List<RegulateObjectBean> o) {

//                        if (mRegulateObjectArrayList.size() == 0) {
//                            Toast.makeText(RegulateObjectListActivity.this, "未获取到企业信息，请重新获取数据", Toast.LENGTH_LONG).show();
//                        }
                        ptrFrameLayout.refreshComplete();
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onError(ApiException e, String method) {
        ptrFrameLayout.refreshComplete();
        mRegulateObjectArrayList.clear();
        mData.clear();
//        mRegulateObjectArrayList.addAll(dao.queryBuilder()
//                .where(RegulateObjectBeanDao
//                        .Properties.UserId
//                        .eq(MyApplication.instance.getUserBean().getUserExtId()))
//                .list());
        mData.addAll(mRegulateObjectArrayList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void check(int postion) {
        final RegulateObjectBean objectBean = mRegulateObjectArrayList.get(postion);
        if (ConstantValue.OBJ_CHECK_STATUS_NEW.equals(objectBean.getInspstatus())) {
            if (!objectBean.getOisuper().equals(MyApplication.getInstance().getUserBean().getUserExtId())) {
                Toast.makeText(SiteListActivity.this, String.format("该企业正在被%s检查，有问题请与%s联系", objectBean.getOisupername(), objectBean.getOisupername()), Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(ConstantValue.KEY_OBJECT_ID, objectBean.getId());
            intent.setClass(SiteListActivity.this, SiteCheckActivity.class);
            startActivityForResult(intent, ConstantValue.REQUEST_CODE_SITE_CHECK);
        } else {
            newTaskWindow.showAtLocation(getLayoutId(), Gravity.CENTER, 0, 0);
            newTaskWindow.setPassButtonOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newTaskWindow.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra(ConstantValue.KEY_OBJECT_ID, objectBean.getId());
                    intent.setClass(SiteListActivity.this, CheckTableSelect2Activity.class);
                    startActivityForResult(intent, ConstantValue.REQUEST_CODE_SITE_CHECK);
                }
            });
        }
    }

    @Override
    public void checkRecord(int postion) {
        Intent intent = new Intent(SiteListActivity.this, RegulateListAvtivity.class);
        intent.putExtra(ConstantValue.CODE, "ent");
        intent.putExtra(ConstantValue.KEY_OBJECT_ID, mRegulateObjectArrayList.get(postion).getId());
        startActivity(intent);
    }

    private void initWindow() {
        newTaskWindow = new MyPopupWindow.Builder().setContext(SiteListActivity.this)
                .setContentView(R.layout.layout_new_task_window)
                .setTitle(getResources().getString(R.string.tip))
                .setPass(getResources().getString(R.string.confrim))
                .setUnpass(getResources().getString(R.string.cancel))
                .setOutSideCancel(true)
                .setUnpassListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newTaskWindow.dismiss();
                    }
                })
                .setFouse(true)
                .setwidth(SystemUtil.getWith(SiteListActivity.this) * 2 / 3)
                .setheight(ViewGroup.LayoutParams.WRAP_CONTENT).builder();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_REARCH:
                    String id = data.getStringExtra(ConstantValue.OBJECT_ID);
                    for (RegulateObjectBean bean : mData) {
                        if (bean.getId().equals(id)) {
                            ivCancel.setVisibility(View.VISIBLE);
                            ivSearch.setVisibility(View.GONE);
                            mRegulateObjectArrayList.clear();
                            mRegulateObjectArrayList.add(bean);
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    break;
                case ConstantValue.REQUEST_CODE_SITE_CHECK:
                    start = 0;
                    getObjectApi.setStart(start);
                    manager.doHttpDeal(getObjectApi);
                    break;
            }
        } else if (resultCode == RESULT_CANCELED && requestCode == ConstantValue.REQUEST_CODE_SITE_CHECK) {
            start = 0;
            getObjectApi.setStart(start);
            manager.doHttpDeal(getObjectApi);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        newTaskWindow.dismiss();
        super.onStop();
    }

    @Override
    protected void onPause() {
        newTaskWindow.dismiss();
        super.onPause();
    }
}

