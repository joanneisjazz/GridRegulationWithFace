package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.RegulateAdapter;
import com.jstech.gridregulation.api.GetEntRegulateRecordApi;
import com.jstech.gridregulation.api.GetRegulateRecordApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.utils.TextUtil;
import com.rxretrofitlibrary.greendao.DaoSession;
import com.rxretrofitlibrary.greendao.TaskBeanDao;
import com.rxretrofitlibrary.greendao.UserBeanDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.UserBean;
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

/**
 * Created by hesm on 2018/10/21.
 * 现场检查的记录
 */

public class RegulateListAvtivity extends BaseActivity implements HttpOnNextListener, AdapterView.OnItemClickListener {
    private PtrClassicFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private TextView tvHint;

    private RegulateAdapter mAdapter;

    private HttpManager manager;
    private GetRegulateRecordApi getRegulateRecordApi;
    private GetEntRegulateRecordApi getEntRegulateRecordApi;

    private List<TaskBean> mData;
    private String type = "";
    private String entId = "";
    private String userExtId = "";
    private int start = 0;
    private int length = 10;

    private DaoSession session;
    private TaskBeanDao taskBeanDao;
    private UserBeanDao userBeanDao;
    private UserBean userBean;
    private String roName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_illegal_case_list;
    }

    @Override
    public void initView() {
        setToolBarTitle(getResources().getString(R.string.regulate_records));
        setToolSubBarTitle("");

        session = MyApplication.getInstance().getSession();
        taskBeanDao = session.getTaskBeanDao();
//        userBeanDao = session.getUserBeanDao();
//        userBean = userBeanDao.queryBuilder().where(
//                UserBeanDao.Properties.Id.eq(SharedPreferencesHelper.getInstance(RegulateListAvtivity.this).getSharedPreference("id", ""))
//        ).unique();
//        if (null != userBean) {
//            roName = userBean.getRoName();
//        }

        type = getIntent().getStringExtra(ConstantValue.CODE);
        if (null == type) {
            type = "user";
        }
        entId = getIntent().getStringExtra(ConstantValue.KEY_OBJECT_ID);
        userExtId = SharedPreferencesHelper.getInstance(this).getUserExtId(this);

        tvHint = findViewById(R.id.tv_hint);
        tvHint.setText("无检查记录");
        recyclerView = findViewById(R.id.recyclerview);
        ptrFrameLayout = findViewById(R.id.fragment_ptr_home_ptr_frame);

        mData = new ArrayList<>();
        mAdapter = new RegulateAdapter(mData, this, R.layout.item_regulate);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        manager = new HttpManager(this, this);
        getEntRegulateRecordApi = new GetEntRegulateRecordApi();
        getRegulateRecordApi = new GetRegulateRecordApi();

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
                if ("ent".equals(type)) {
                    //查看企业的检查记录
                    getEntRegulateRecordApi.setStart(start);
                    manager.doHttpDeal(getEntRegulateRecordApi);
                } else {
                    //查看检察人员的检查记录
                    getRegulateRecordApi.setStart(start);
                    manager.doHttpDeal(getRegulateRecordApi);
                }

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                tvHint.setVisibility(View.GONE);
                start = 0;
                if ("ent".equals(type)) {
                    //查看企业的检查记录
                    getEntRegulateRecordApi.setStart(start);
                    manager.doHttpDeal(getEntRegulateRecordApi);
                } else {
                    //查看检察人员的检查记录
                    getRegulateRecordApi.setStart(start);
                    manager.doHttpDeal(getRegulateRecordApi);
                }

            }
        });


        if ("ent".equals(type)) {
            //查看企业的检查记录
            getEntRegulateRecordApi.setParam(entId);
            getEntRegulateRecordApi.setLength(length);
            getEntRegulateRecordApi.setStart(start);
            manager.doHttpDeal(getEntRegulateRecordApi);
            mAdapter.setmType(1);
        } else {
            //查看检察人员的检查记录
            getRegulateRecordApi.setLength(length);
            getRegulateRecordApi.setStart(start);
            getRegulateRecordApi.setOisuper(SharedPreferencesHelper.getInstance(this).getSharedPreference("extId", "").toString());
            manager.doHttpDeal(getRegulateRecordApi);
            mAdapter.setmType(2);
        }

    }

    @Override
    public void onNext(String resulte, String method) {
        ptrFrameLayout.refreshComplete();
        JSONObject o = JSON.parseObject(resulte);
        if (!ConstantValue.isSuccess(o)) {
            return;
        }
        if (start == 0) {
            mData.clear();
        }
        JSONArray jsonArray = o.getJSONArray("result");
        if (null == jsonArray || jsonArray.size() <= 0) {
            start = start - length;
        } else {
            List<TaskBean> list = jsonArray.toJavaList(TaskBean.class);
            if (null != list && list.size() > 0) {
                updateDao(list);
            }
        }
        updateAdapter();
    }

    @Override
    public void onError(ApiException e, String method) {
        ptrFrameLayout.refreshComplete();

        //有网络的时候就用服务器上面的数据，没有的话才用本地的
        mData.clear();
        updateAdapter();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!TextUtil.isEmpty(mData.get(i).getState5()) && mData.get(i).getState5().equals("1")) {
            Intent intent = new Intent(this, RegulateDetailActivity.class);
            intent.putExtra(ConstantValue.CODE, type);
            intent.putExtra(ConstantValue.RESULT, mData.get(i));
            startActivity(intent);
        } else {
            Toast.makeText(RegulateListAvtivity.this, "正在检查中", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        String code = getIntent().getStringExtra(ConstantValue.KEY_CONTENT);
        if (!TextUtil.isEmpty(code) || ConstantValue.OBJ_CHECK_STATUS_FINISH.equals(code)) {
            Intent intent = new Intent(RegulateListAvtivity.this, SiteActivity.class);
            startActivity(intent);
        }
        super.onBackPressed();
    }


    /**
     * 从服务器获取到最新数据之后，更新本地数据库里的任务信息
     *
     * @param onlineTasks
     */
    private void updateDao(List<TaskBean> onlineTasks) {
        List<TaskBean> localTasks;
        if (type.equals("ent")) {
            localTasks = taskBeanDao.queryBuilder().where(
                    TaskBeanDao.Properties.Entid.eq(entId),
                    TaskBeanDao.Properties.DeleteFlag.notEq("1")
            ).list();
        } else {
            localTasks = taskBeanDao.queryBuilder().where(
                    TaskBeanDao.Properties.Oisuper.eq(userExtId),
                    TaskBeanDao.Properties.DeleteFlag.notEq("1")
            ).list();
        }

        if (null == localTasks || localTasks.isEmpty()) {
            taskBeanDao.insertInTx(onlineTasks);
            return;
        }
        int size = onlineTasks.size();
        for (int i = 0; i < size; i++) {
            TaskBean now = onlineTasks.get(i);
            int index = localTasks.indexOf(now);
            if (-1 == index) {
                //如果不在本地数据库里，就插入这条数据
                now.setState6("1");
                now.setState5("1");
//                now.setRegulatorName(roName);
                taskBeanDao.insert(now);
            } else {
                localTasks.get(index).setResultstr(now.getResultstr());
//                localTasks.get(index).setRegulatorName(roName);
                taskBeanDao.update(localTasks.get(index));
            }
        }
    }

    private void updateAdapter() {
        mData.clear();
        if (type.equals("ent")) {
            mData.addAll(taskBeanDao.queryBuilder().where(
                    TaskBeanDao.Properties.Entid.eq(entId),
                    TaskBeanDao.Properties.DeleteFlag.notEq("1")
            ).list());
        } else {
            mData.addAll(taskBeanDao.queryBuilder().where(
                    TaskBeanDao.Properties.Oisuper.eq(userExtId),
                    TaskBeanDao.Properties.DeleteFlag.notEq("1")
            ).list());
        }
        if (mData.size() > 0) {
            tvHint.setVisibility(View.GONE);
        } else {
            tvHint.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

}
