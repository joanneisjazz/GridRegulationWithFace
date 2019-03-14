package com.jstech.gridregulation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.activity.RegulateListAvtivity;
import com.jstech.gridregulation.activity.SearchRegulateObjectActivity;
import com.jstech.gridregulation.activity.SiteActivity;
import com.jstech.gridregulation.activity.TakeFacePicActivity;
import com.jstech.gridregulation.adapter.RegulateObjectAdapter;
import com.jstech.gridregulation.api.GetObjectApi;
import com.jstech.gridregulation.base.BaseFragment;
import com.jstech.gridregulation.utils.LogUtils;
import com.jstech.gridregulation.utils.TextUtil;
import com.jstech.gridregulation.widget.MyPopupWindow;
import com.rxretrofitlibrary.greendao.EnterpriseEntityDao;
import com.rxretrofitlibrary.greendao.RegulateObjectBeanDao;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;
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
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.jstech.gridregulation.fragment.RegulateObjectMapFragment.REQUEST_REFRESH;

/**
 * Created by hesm on 2018/11/21.
 * 现场检查--列表
 */

public class RegulateObjectListFragment extends BaseFragment implements HttpOnNextListener, RegulateObjectAdapter.OnClickListener {

    private PtrClassicFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private RelativeLayout layoutSearch;
    private ImageView ivSearch;
    private ImageView ivCancel;

    private HttpManager manager;
    private GetObjectApi getObjectApi;

    private ArrayList<RegulateObjectBean> mRegulateObjectArrayList = new ArrayList<>();//监管对象list
    private ArrayList<RegulateObjectBean> mData = new ArrayList<>();//
    private String objectId = "";

    private RegulateObjectBeanDao dao;
    private EnterpriseEntityDao enterpriseEntityDao;

    private RegulateObjectAdapter mAdapter;
    private int start = 0;
    private int length = 10;

    private MyPopupWindow newTaskWindow;//是否开启新的检查的窗口
    private String orgId = "";
    private String ueserExtId = "";


    public static RegulateObjectListFragment newInstance() {
        Bundle args = new Bundle();
        RegulateObjectListFragment fragment = new RegulateObjectListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    final public static int REQUEST_REARCH = 1;
    //是否已经加载完毕
    private boolean isPrepared;

    //是否已经加载过一次  已经加载过再次显示fragment不会再加载网络数据
    private boolean isHasLoadOnce;

    @Override
    protected void LazyLoad() {
        if (!isPrepared || !isVisible || isHasLoadOnce) {
            return;
        }
        isHasLoadOnce = true;
        manager.doHttpDeal(getObjectApi);

    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_regulate_object_list;
    }


    @Override
    public void initView(View rootView) {
        isPrepared = true;
        LogUtils.d("list");
        orgId = SharedPreferencesHelper.getInstance(getActivity()).getSharedPreference("orgId", "").toString();
        ueserExtId = SharedPreferencesHelper.getInstance(getActivity()).getSharedPreference("extId", "").toString();

        initWindow();
        findViews(rootView);
        setListener();

        enterpriseEntityDao = MyApplication.instance.getSession().getEnterpriseEntityDao();
        dao = MyApplication.instance.getSession().getRegulateObjectBeanDao();

        mAdapter = new RegulateObjectAdapter(mRegulateObjectArrayList, getActivity(), R.layout.layout_map_bottom_window, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

        manager = new HttpManager(this, (RxAppCompatActivity) getActivity());
        getObjectApi = new GetObjectApi();
        getObjectApi.setId(orgId);
        getObjectApi.setLength(length);
        getObjectApi.setStart(start);
        manager.doHttpDeal(getObjectApi);

    }

    private void findViews(View rootView) {
        ivSearch = rootView.findViewById(R.id.iv_search);
        ivCancel = rootView.findViewById(R.id.iv_cancel);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutSearch = rootView.findViewById(R.id.layout_search);
        ptrFrameLayout = rootView.findViewById(R.id.ptr_layout);
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrFrameLayout.setKeepHeaderWhenRefresh(true);
    }

    private void setListener() {
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
                Intent intent = new Intent(getActivity(), SearchRegulateObjectActivity.class);
                intent.putExtra(ConstantValue.OBJECT_COUNT, mData);
                startActivityForResult(intent, REQUEST_REARCH);
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

        ptrFrameLayout.refreshComplete();
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
                        List<RegulateObjectBean> beans = object.getJSONArray(ConstantValue.RESULT).toJavaList(RegulateObjectBean.class);
                        if (0 == beans.size()) {
                            start = start - length;
                            return null;
                        }
                        if (0 == start) {
                            mData.clear();
                            mRegulateObjectArrayList.clear();
                            dao.deleteAll();
                        }
                        for (RegulateObjectBean r : beans) {
                            r.setUserId(ueserExtId);
                            r.setLegalName(r.getEnterprise().getLegalName());
                            mRegulateObjectArrayList.add(r);
                            mData.add(r);
                            dao.insertOrReplace(r);
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

                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onError(ApiException e, String method) {
        ptrFrameLayout.refreshComplete();
        if (method.equals(getObjectApi.getMethod())) {
            mRegulateObjectArrayList.clear();
            mData.clear();
            mRegulateObjectArrayList.addAll(dao.queryBuilder()
                    .where(RegulateObjectBeanDao
                            .Properties.UserId
                            .eq(ueserExtId))
                    .list());
            mData.addAll(mRegulateObjectArrayList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void check(int postion) {
        final RegulateObjectBean objectBean = mRegulateObjectArrayList.get(postion);
        objectId = objectBean.getId();
        /**
         * 人脸识别验证
         */


        /**
         * 开启新的检查
         */
        if (TextUtil.isEmpty(objectBean.getInspstatus()) || ConstantValue.OBJ_CHECK_STATUS_FINISH.equals(objectBean.getInspstatus())) {
            newTaskWindow.showAtLocation(getLayoutResource(), Gravity.CENTER, 0, 0);
            newTaskWindow.setPassButtonOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newTaskWindow.dismiss();
                    validate(SiteActivity.NEW_TASK, objectBean);
//                    Intent intent = new Intent();
//                    intent.putExtra(ConstantValue.KEY_OBJECT_ID, objectBean.getId());
//                    intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, objectBean);
//                    intent.setClass(getActivity(), CheckTableSelect2Activity.class);
//                    startActivityForResult(intent, ConstantValue.REQUEST_CODE_SITE_CHECK);
//                    getActivity().finish();
                }
            });
            return;
        }
        /**
         * 正在检查中的企业，先判断该企业正在被谁检查
         */
//        if (!ueserExtId.equals(objectBean.getOisuper())) {
//            Toast.makeText(getActivity(), String.format("该企业正在被%s检查，有问题请与%s联系", objectBean.getOisupername(), objectBean.getOisupername()), Toast.LENGTH_LONG).show();
//            return;
//        }

        validate(SiteActivity.CONTINUE_TASK, objectBean);
//        Intent intent = new Intent();
//        intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, objectBean);
//        intent.putExtra(ConstantValue.KEY_OBJECT_ID, objectBean.getId());
//        intent.setClass(getActivity(), RegulateItemListActivity.class);
//        startActivityForResult(intent, ConstantValue.REQUEST_CODE_SITE_CHECK);
//        getActivity().finish();

    }

    /**
     * 人脸验证
     *
     * @param code
     */
    private void validate(String code, RegulateObjectBean objectBean) {
        Intent intent = new Intent(getActivity(), TakeFacePicActivity.class);
        intent.putExtra(ConstantValue.KEY_CHECK_STATUS, String.valueOf(code));
        intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, objectBean);
        startActivityForResult(intent, ConstantValue.REQUEST_CODE_SITE_CHECK);
        getActivity().finish();
    }


    @Override
    public void checkRecord(int postion) {
        Intent intent = new Intent(getActivity(), RegulateListAvtivity.class);
        intent.putExtra(ConstantValue.CODE, "ent");
        intent.putExtra(ConstantValue.KEY_OBJECT_ID, mRegulateObjectArrayList.get(postion).getId());
        startActivity(intent);
    }

    private void initWindow() {
        newTaskWindow = new MyPopupWindow.Builder().setContext(getActivity())
                .setContentView(R.layout.layout_new_task_window)
                .setTitle(getResources().getString(R.string.tip))
                .setPass(getResources().getString(R.string.confrim))
                .setUnpass(getResources().getString(R.string.cancel))
                .builder();
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
                    Intent intent = new Intent(getActivity(), RegulateListAvtivity.class);
                    intent.putExtra(ConstantValue.KEY_OBJECT_ID, objectId);
                    intent.putExtra(ConstantValue.CODE, "ent");
                    intent.putExtra(ConstantValue.KEY_CONTENT, ConstantValue.OBJ_CHECK_STATUS_FINISH);
                    startActivityForResult(intent, REQUEST_REFRESH);
                    break;
                case REQUEST_REFRESH:
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            manager.doHttpDeal(getObjectApi);
        }
    }
}
