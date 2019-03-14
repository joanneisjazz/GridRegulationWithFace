package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.WorkDeskAdapter;
import com.jstech.gridregulation.api.GetCityApi;
import com.jstech.gridregulation.api.GetRoNameApi;
import com.jstech.gridregulation.api.GetTableAndItem;
import com.jstech.gridregulation.api.UpdateApkApi;
import com.jstech.gridregulation.bean.WorkDeskBean;
import com.jstech.gridregulation.service.GPSService;
import com.jstech.gridregulation.update.EventMessage;
import com.jstech.gridregulation.update.UpdateAppUtils;
import com.jstech.gridregulation.utils.AppManager;
import com.jstech.gridregulation.utils.TextUtil;
import com.jstech.gridregulation.utils.ToastUtil;
import com.jstech.gridregulation.widget.MyGridLayoutManager;
import com.jstech.gridregulation.widget.MyPopupWindow;
import com.rxretrofitlibrary.greendao.CheckItemEntityDao;
import com.rxretrofitlibrary.greendao.CheckTableEntityDao;
import com.rxretrofitlibrary.greendao.CityEntityDao;
import com.rxretrofitlibrary.greendao.DaoSession;
import com.rxretrofitlibrary.greendao.TaskBeanDao;
import com.rxretrofitlibrary.greendao.UserBeanDao;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckItemEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckTableEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CityEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.UserBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

/**
 * Created by hesm on 2018/11/8.
 */

public class WorkDeskActivity extends RxAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HttpOnNextListener {

    boolean IsDownLoad = false;
    String downUrl;
    int serviceVersionCode;


    private RecyclerView recyclerView;
    private RelativeLayout layoutPersonalInfo;
    private LinearLayout layoutNotifications;
    private ImageView ivRefresh;


    private WorkDeskAdapter workDeskAdapter;
    private List<WorkDeskBean> mDatas;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View headView;
    private ImageView ivHead;
    private TextView tvName;
    private TextView tvOrg;
    private TextView tvBack;

    private MyPopupWindow popupWindow;

    private UserBean userBean;
    private String launcher = "";
    private String orgId = "";
//    private GetPersonalInfoApi getPersonalInfoApi;

    private GetCityApi getCityApi;
    private GetTableAndItem getTableAndItem;
    private UpdateApkApi updateApkApi;
    private GetRoNameApi getRoNameApi;
    private HttpManager manager;


    private TaskBeanDao taskBeanDao;
    private CityEntityDao cityEntityDao;
    private CheckItemEntityDao itemDao;
    private CheckTableEntityDao tableDao;
    private UserBeanDao userBeanDao;

    private TourGuide mTourGuide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_desk);
        active();
        EventBus.getDefault().register(this);

        userBean = (UserBean) getIntent().getSerializableExtra(ConstantValue.KEY_CONTENT);
        launcher = getIntent().getStringExtra(ConstantValue.KEY_OBJECT_ID);
        findViews();
        initData();
        setUserInfo();

        workDeskAdapter = new WorkDeskAdapter(mDatas, this, R.layout.item_work_desk);
        MyGridLayoutManager myGridLayoutManager = new MyGridLayoutManager(this, 3);
        myGridLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(myGridLayoutManager);
        recyclerView.setAdapter(workDeskAdapter);

        workDeskAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(WorkDeskActivity.this, mDatas.get(i).gettClass());
                startActivity(intent);
            }
        });

        layoutPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(WorkDeskActivity.this, PersonalInfoActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);

        layoutNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WorkDeskActivity.this, NotificationActivity.class));
            }
        });

        popupWindow = new MyPopupWindow.Builder().setContext(this).
                setContentView(R.layout.layout_back_tip).setTitle(getResources().getString(R.string.tip))
                .setPass(getString(R.string.confrim))
                .setUnpass(getString(R.string.cancel))
                .builder();
        tvBack = popupWindow.getContentFrameLayout().findViewById(R.id.tv_content);
        tvBack.setText("您确定要退出吗？");

        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTourGuide.cleanUp();
                startActivity(new Intent(WorkDeskActivity.this, UploadSiteCheckDataActivity.class));
//                uploadData(false);
            }
        });
        updateApkApi = new UpdateApkApi();
        getRoNameApi = new GetRoNameApi();

        manager = new HttpManager(this, this);
//        getPersonalInfoApi = new GetPersonalInfoApi();
//        getPersonalInfoApi.setId(SharedPreferencesHelper.getInstance(this).getSharedPreference("id", "").toString());
//        if ("1".equals(launcher)) {
//            manager.doHttpDeal(getPersonalInfoApi);
//        }

//        uploadData(true);

//        getRoNameApi.setExtId(SharedPreferencesHelper.getInstance(WorkDeskActivity.this).getSharedPreference("extId", "").toString());
//        manager.doHttpDeal(getRoNameApi);

        Animation animation = new TranslateAnimation(0f, 0f, 200f, 0f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setInterpolator(new BounceInterpolator());
        mTourGuide = TourGuide.init(this).with(TourGuide.Technique.CLICK);
        mTourGuide.setOverlay(new Overlay());
        mTourGuide.setPointer(new Pointer());
        mTourGuide.setToolTip(new ToolTip()
                .setTitle("请上传现场检查任务")
                .setDescription("点击该处上传现场检查任务")
                .setShadow(true)
                .setEnterAnimation(animation)
                .setGravity(Gravity.BOTTOM | Gravity.LEFT));

        checkOffLineData();

    }

    private void findViews() {
        ivRefresh = findViewById(R.id.iv_refresh);
        layoutNotifications = findViewById(R.id.layout_notification);
        recyclerView = findViewById(R.id.recyclerView);
        layoutPersonalInfo = findViewById(R.id.layout_personal_info);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headView = navigationView.inflateHeaderView(R.layout.nav_header_work_desk);
        ivHead = headView.findViewById(R.id.imageView);
        tvName = headView.findViewById(R.id.name);
        tvOrg = headView.findViewById(R.id.tv_org);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.nav_clear:
                Toast.makeText(WorkDeskActivity.this, "正在清除缓存", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_back:
                tvBack.setText("您确定要退出登录吗？");
                popupWindow.getPassButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                        Intent gpsService = new Intent(WorkDeskActivity.this, GPSService.class);
                        stopService(gpsService);

                        SharedPreferencesHelper.getInstance(WorkDeskActivity.this).put("expTime", 0l);
                        startActivity(new Intent(WorkDeskActivity.this, LoginActivity.class));
                        AppManager.getAppManager().finishAllActivity();
                    }
                });
                popupWindow.showAtLocation(R.layout.activity_work_desk, Gravity.CENTER, 0, 0);

                break;
            case R.id.nav_help:
//                Toast.makeText(WorkDeskActivity.this, "已经是最新版本", Toast.LENGTH_LONG).show();
                manager.doHttpDeal(updateApkApi);
                break;

            case R.id.nav_update:
                manager.doHttpDeal(getCityApi);
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UpdateAppUtils.REQUEST_PERMISSION_SDCARD_SETTING) {
            getData();
        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    userBean = (UserBean) data.getSerializableExtra(ConstantValue.KEY_CONTENT);
                    setUserInfo();
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            tvBack.setText("您确定要退出吗？");
            popupWindow.getPassButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    AppManager.getAppManager().finishAllActivity();
                    System.exit(0);
                }
            });
            popupWindow.showAtLocation(R.layout.activity_work_desk, Gravity.CENTER, 0, 0);
        }
        return false;
    }

    @Override
    protected void onStop() {
        popupWindow.dismiss();
        super.onStop();
    }

    private void setUserInfo() {
        userBean = userBeanDao.queryBuilder().where(
                UserBeanDao.Properties.Id.eq(SharedPreferencesHelper.getInstance(this).getSharedPreference("id", "").toString())
        ).unique();
        if (null != userBean) {
            tvOrg.setText(userBean.getOrgName());
            tvName.setText(userBean.getUsername());
        }
    }

    private void initData() {
        orgId = SharedPreferencesHelper.getInstance(this).getSharedPreference("orgId", "").toString();

        mDatas = new ArrayList<>();
        WorkDeskBean workDeskBean = new WorkDeskBean("现场检查", R.mipmap.wk_ic_site, SiteActivity.class);
        WorkDeskBean workDeskBean2 = new WorkDeskBean("案件上报", R.mipmap.wk_ic_case, CaseUploadActivity.class);
        WorkDeskBean workDeskBean3 = new WorkDeskBean("现场检查记录", R.mipmap.wk_ic_site_record, RegulateListAvtivity.class);
        WorkDeskBean workDeskBean4 = new WorkDeskBean("案件上报记录", R.mipmap.wk_ic_case_record, IlleagelCaseListActivity.class);
        mDatas.add(workDeskBean);
        mDatas.add(workDeskBean2);
        mDatas.add(workDeskBean3);
        mDatas.add(workDeskBean4);
        //orgId 中7-12位 不是6个0，就是乡镇监管员
        //如果是县级以及以上的监管员，可以看到领导驾驶舱
        if (!TextUtil.isEmpty(orgId) && orgId.length() > 12) {
            orgId = orgId.substring(6, 12);
            if ("000000".equals(orgId)) {
                WorkDeskBean workDeskBean5 = new WorkDeskBean("领导驾驶舱", R.mipmap.wk_ic_lead, CreditActivity.class);
                mDatas.add(workDeskBean5);
            }
        }

        DaoSession session = MyApplication.getInstance().getSession();
        cityEntityDao = session.getCityEntityDao();
        tableDao = session.getCheckTableEntityDao();
        itemDao = session.getCheckItemEntityDao();
        userBeanDao = session.getUserBeanDao();
        taskBeanDao = session.getTaskBeanDao();

        getCityApi = new GetCityApi();
        getTableAndItem = new GetTableAndItem();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onNext(String resulte, String method) {
        JSONObject o = JSON.parseObject(resulte);
        if (!ConstantValue.isSuccess(o)) {
            return;
        }
//        if (method.equals(getPersonalInfoApi.getMethod())) {
//            userBean = o.getObject(ConstantValue.RESULT, UserBean.class);
//            setUserInfo();
//        } else
/*        if (method.equals(getRoNameApi.getMethod())) {
            UserBean userBean = userBeanDao.queryBuilder().where(
                    UserBeanDao.Properties.UserExtId.eq()
            ).unique();
            userBean.setRoName();
            userBeanDao.update();

            uploadData(true);
        } else*/
        if (method.equals(getCityApi.getMethod())) {
            cityEntityDao.deleteAll();
            cityEntityDao.saveInTx(JSON.parseObject(resulte).getJSONArray(ConstantValue.RESULT).toJavaList(CityEntity.class));
            manager.doHttpDeal(getTableAndItem);
        } else if (method.equals(getTableAndItem.getMethod())) {
            JSONObject jsonObject = o.getJSONObject(ConstantValue.RESULT);
            JSONArray arrayTables = jsonObject.getJSONArray(ConstantValue.TABLES);
            tableDao.deleteAll();
            tableDao.saveInTx(arrayTables.toJavaList(CheckTableEntity.class));
            JSONArray arrayItems = jsonObject.getJSONArray(ConstantValue.ITEMS);
            itemDao.deleteAll();
            itemDao.saveInTx(arrayItems.toJavaList(CheckItemEntity.class));

        } else if (updateApkApi.getMethod().equals(method)) {

            try {

                org.json.JSONObject jsonObject = new org.json.JSONObject(resulte);

                if ("200".equals(jsonObject.getString("code"))) {

                    org.json.JSONObject jsonObject2 = jsonObject.getJSONObject("result");

                    serviceVersionCode = Integer.parseInt(jsonObject2.getString("code"));

                    downUrl = jsonObject2.getString("path");

                    if (IsDownLoad) {
                        Toast.makeText(WorkDeskActivity.this, "正在更新，请稍后...", Toast.LENGTH_LONG).show();
                        return;
                    }
                    //升级app
                    getData();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(ApiException e, String method) {
//        if (method.equals(getPersonalInfoApi.getMethod())) {
//            Toast.makeText(WorkDeskActivity.this, "获取个人信息失败", Toast.LENGTH_LONG).show();
//        } else
        if (method.equals(getTableAndItem.getMethod()) || method.equals(getCityApi.getMethod())) {
            Toast.makeText(WorkDeskActivity.this, "更新信息失败", Toast.LENGTH_LONG).show();
        } else if (method.equals(updateApkApi.getMethod())) {
            Toast.makeText(WorkDeskActivity.this, "下载失败", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(WorkDeskActivity.this, "上传失败，请重新上传", Toast.LENGTH_LONG).show();
        }
    }


    private void getData() {
        String info = "优化部分内容，修改部分bug";  //更新说明
        int Forced = 1;// 1：强制更新   0：不是
        if (serviceVersionCode != 0 && !TextUtils.isEmpty(downUrl)) {
            UpdateAppUtils.UpdateApp(WorkDeskActivity.this, null, serviceVersionCode, info,
                    downUrl, Forced == 1 ? true : false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExitappEvent(EventMessage messageEvent) {
        if (messageEvent.getMessageType() == EventMessage.Exitapp) {

        } else if (messageEvent.getMessageType() == EventMessage.CheckApp) {
            IsDownLoad = messageEvent.isDownLoading();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    private void active() {

        Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                FaceEngine faceEngine = new FaceEngine();
                int activeCode = faceEngine.active(WorkDeskActivity.this, ConstantValue.APP_ID, ConstantValue.SDK_KEY);
                subscriber.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {
                            ToastUtil.toast(WorkDeskActivity.this, "人脸识别激活成功");
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                        } else {
                            ToastUtil.toast(WorkDeskActivity.this, "人脸识别激活失败");
                        }
                    }
                });

    }


    private void checkOffLineData() {
        List<TaskBean> datas = taskBeanDao.queryBuilder().where(
                TaskBeanDao.Properties.State5.eq("1"),
                TaskBeanDao.Properties.State6.notEq("1")
        ).list();

        if (null != datas && datas.size() > 0) {
            mTourGuide.playOn(ivRefresh);
        }
    }

}
