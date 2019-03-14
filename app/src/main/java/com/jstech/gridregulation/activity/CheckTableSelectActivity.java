package com.jstech.gridregulation.activity;

import android.view.View;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.RegulateItemAdpter;
import com.jstech.gridregulation.api.GetItemApi;
import com.jstech.gridregulation.api.GetTableApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.utils.LogUtils;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckItemBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckItemEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckTableBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckTableEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 选择检查表
 */
public class CheckTableSelectActivity extends BaseActivity implements View.OnClickListener, HttpOnNextListener {

    @BindView(R.id.listview)
    ExpandableListView listView;

    ArrayList<CheckTableEntity> mCheckTableBeanList = new ArrayList<>();
    ArrayList<CheckItemBean> selectedItemList = new ArrayList<>();

    RegulateItemAdpter mAdpter;
    HttpManager manager;
    GetTableApi getTableApi;
    GetItemApi getItemApi;

    long objectId;
    MyApplication app = (MyApplication) getApplication();

    int nowPostion = -1;//本次点击的检查表的位置
    int oldPostion = -1;//上一次点击的检查表的位置

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_table_select;
    }

    @Override
    public void initView() {
        setToolSubBarTitle("");
        objectId = getIntent().getLongExtra(ConstantValue.KEY_OBJECT_ID, 0);
        mAdpter = new RegulateItemAdpter(this, mCheckTableBeanList);
        listView.setAdapter(mAdpter);
        setListViewClick();
        manager = new HttpManager(this, this);
        getItemApi = new GetItemApi();
        getTableApi = new GetTableApi();
        manager.doHttpDeal(getTableApi);


    }


    String tableId = "";


    private void setListViewClick() {
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                oldPostion = nowPostion;
                nowPostion = groupPosition;
                CheckTableEntity bean = mCheckTableBeanList.get(nowPostion);

                //判断是否选中，如果已选中，则取消选中
                if (bean.isSelected()) {
                    bean.setSelected(false);
                    listView.collapseGroup(nowPostion);
                    nowPostion = -1;
                    selectedItemList.clear();
                    mAdpter.notifyDataSetChanged();
                    return true;
                }
                //如果未选中
                //判断上一次是否有选中的检查表，如果有的话要将上次选中的表取消掉
                selectedItemList.clear();
                if (oldPostion != -1) {
                    mCheckTableBeanList.get(oldPostion).setSelected(false);
                    listView.collapseGroup(oldPostion);
                }
                if (null == bean.getCheckItemBeans()) {
                    //如果检查表下面没有检查项，要去请求接口获取数据
                    getItemApi.setParam(mCheckTableBeanList.get(nowPostion).getId());
                    bean.setSelected(true);
                    manager.doHttpDeal(getItemApi);
                    listView.setSelectedGroup(nowPostion);
                } else {
                    bean.setSelected(true);
                    listView.setSelectedGroup(nowPostion);
                    listView.expandGroup(nowPostion);
//                    selectedItemList.addAll(bean.getCheckItemBeans());
                }
                mAdpter.notifyDataSetChanged();
                return true;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                CheckTableEntity checkTableBean = mCheckTableBeanList.get(i);
                CheckItemEntity checkItemBean = checkTableBean.getCheckItemBeans().get(i1);
                if (checkItemBean.getIsSelected()) {
                    checkItemBean.setIsSelected(false);
                    mAdpter.notifyDataSetChanged();
                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }


    @Override
    public void onNext(String resulte, String method) {
        LogUtils.d(resulte);
        LogUtils.d(method);
        JSONObject o = JSON.parseObject(resulte);
        String code = o.getString(ConstantValue.CODE);
        if (!code.equals(ConstantValue.CODE_SUCCESS)) {
            return;
        }
        //一次性获取所有的数据，然后保存到本地
        Observable.just(o.getJSONObject("")).observeOn(Schedulers.newThread())
                .map(new Func1<JSONObject, Object>() {
                    @Override
                    public Object call(JSONObject jsonObject) {
                        //转成adapter需要的实体类列表
                        List<CheckTableEntity> entityList = new ArrayList<>();
                        for (CheckTableEntity entity:entityList){

                        }
                        return null;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {

                    }
                });
        if (method.equals(getTableApi.getMethod())) {
            mCheckTableBeanList.clear();
            mCheckTableBeanList.addAll((ArrayList<CheckTableEntity>) o.getJSONArray(ConstantValue.RESULT).toJavaList(CheckTableEntity.class));
            mAdpter.flashData(mCheckTableBeanList);
            //将数据保存到本地

            LogUtils.d(mCheckTableBeanList.size() + "");

        } else if (method.equals(getItemApi.getMethod())) {
            mCheckTableBeanList.get(nowPostion).setCheckItemBeans(new ArrayList<CheckItemEntity>());
            mCheckTableBeanList.get(nowPostion).getCheckItemBeans().addAll(o.getJSONArray(ConstantValue.RESULT).toJavaList(CheckItemEntity.class));
            selectedItemList.addAll(o.getJSONArray(ConstantValue.RESULT).toJavaList(CheckItemBean.class));
            mAdpter.notifyDataSetChanged();
            listView.expandGroup(nowPostion);

        }
    }

    @Override
    public void onError(ApiException e, String method) {

    }

}
