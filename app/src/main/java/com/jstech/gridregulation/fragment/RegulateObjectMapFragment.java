package com.jstech.gridregulation.fragment;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.activity.CheckTableSelect2Activity;
import com.jstech.gridregulation.activity.RegulateListAvtivity;
import com.jstech.gridregulation.activity.SearchRegulateObjectActivity;
import com.jstech.gridregulation.activity.SiteActivity;
import com.jstech.gridregulation.activity.SiteCheckActivity;
import com.jstech.gridregulation.activity.TakeFacePicActivity;
import com.jstech.gridregulation.adapter.CheckingEntAdapter;
import com.jstech.gridregulation.api.GetObjectApi;
import com.jstech.gridregulation.base.BaseFragment;
import com.jstech.gridregulation.bean.ObjectMarkerBean;
import com.jstech.gridregulation.utils.LogUtils;
import com.jstech.gridregulation.utils.SystemUtil;
import com.jstech.gridregulation.utils.TextUtil;
import com.jstech.gridregulation.widget.MapBottomWindow;
import com.jstech.gridregulation.widget.MyPopupWindow;
import com.jstech.gridregulation.widget.RecyclerPopupWindow;
import com.rxretrofitlibrary.greendao.RegulateObjectBeanDao;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by hesm on 2018/11/21.
 * 现场检查--地图
 */

public class RegulateObjectMapFragment extends BaseFragment implements
        SensorEventListener, BaiduMap.OnMarkerClickListener, MapBottomWindow.TaskInterface, HttpOnNextListener {

    public static RegulateObjectMapFragment newInstance() {
        Bundle args = new Bundle();
        RegulateObjectMapFragment fragment = new RegulateObjectMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    final public static int REQUEST_REARCH = 1;
    final public static int REQUEST_REFRESH = 190;

    @Override
    protected void LazyLoad() {

    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_site_regulate_map;
    }

    private RelativeLayout layoutSearch;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private String ueserExtId = "";
    private String objectId = "";

    public static MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

    LocationClient mLocClient;
    RegulateObjectMapFragment.MyLocationListener myLocationListener = new RegulateObjectMapFragment.MyLocationListener();
    private SensorManager sensorManager;
    private double lastX = 0.0;
    //112.787601,38.068948 测试的
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private int mCurrentDirection = 0;
    private float mCurrentAccracy;

    private boolean isFisrtLoc = true;
    private MyLocationData mLocData;

    private ArrayList<ObjectMarkerBean> markerArrayList = new ArrayList<>();//监管对象有marker的list
    private ArrayList<RegulateObjectBean> mRegulateObjectArrayList = new ArrayList<>();//监管对象list
    private ArrayList<ObjectMarkerBean> checking = new ArrayList<>();//正在检查中的企业

    private MapBottomWindow window;
    private MyPopupWindow newTaskWindow;//是否开启新的检查的窗口
    private RecyclerPopupWindow checkingEntWindow;//正在检查中的企业

    private HttpManager manager;
    private GetObjectApi getObjectApi;

    private RegulateObjectBeanDao dao;

    //正在检查中的企业
    private CheckingEntAdapter checkingEntAdapter;

    private int start = 0;
    private int length = 1000;

    @Override
    public void initView(View rootView) {
        LogUtils.d("map");
        layoutSearch = rootView.findViewById(R.id.layout_search);
        mMapView = rootView.findViewById(R.id.mapview);
        ueserExtId = SharedPreferencesHelper.getInstance(getActivity()).getSharedPreference("extId", "").toString();
        setMap();
        initWindow();

        dao = MyApplication.getInstance().getSession().getRegulateObjectBeanDao();

        manager = new HttpManager(this, (RxAppCompatActivity) getActivity());
        getObjectApi = new GetObjectApi();
        getObjectApi.setId(SharedPreferencesHelper.getInstance(getActivity()).getSharedPreference("orgId", "").toString());
        getObjectApi.setLength(length);
        getObjectApi.setStart(start);
//        manager.doHttpDeal(getObjectApi);

        layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchRegulateObjectActivity.class);
                intent.putExtra(ConstantValue.OBJECT_COUNT, mRegulateObjectArrayList);
//                intent.putExtra(ConstantValue.OBJECT_COUNT, mRegulateObjectArrayList.size());
                startActivityForResult(intent, REQUEST_REARCH);
            }
        });

    }

    private void setMap() {
        sensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);//获取传感器服务
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomBy(1);
        mBaiduMap.animateMapStatus(mapStatusUpdate);
        //开启定位监听
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setScanSpan(100);
        option.setCoorType(ConstantValue.COOR_TYPE_BD0911);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mBaiduMap.setOnMarkerClickListener(this);
        MyLocationData locData = new MyLocationData.Builder().accuracy(mCurrentAccracy).direction(mCurrentDirection)
                .latitude(mCurrentLat).longitude(mCurrentLon).build();
        mBaiduMap.setMyLocationData(locData);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, null));
        mMapView.setLogoPosition(LogoPosition.logoPostionCenterBottom);
    }

    private void initWindow() {
        window = new MapBottomWindow.Builder().setContext(getActivity())
                .setListener(this)
                .builder();
        window.setOnDismissListener(onDismissListener);
        newTaskWindow = new MyPopupWindow.Builder().setContext(getActivity())
                .setContentView(R.layout.layout_new_task_window)
                .setTitle(getResources().getString(R.string.tip))
                .setPass(getResources().getString(R.string.confrim))
                .setUnpass(getResources().getString(R.string.cancel)).builder();

        checkingEntWindow = new RecyclerPopupWindow.Builder().setContext(getActivity())
                .setheight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setwidth(SystemUtil.getWith(getActivity()) * 2 / 3)
                .setTitle("正在进行中的任务").builder();
    }

    private void addOverLay() {
        int markerIcon = -1;
        markerArrayList.clear();
        mBaiduMap.clear();
        for (RegulateObjectBean o : mRegulateObjectArrayList) {
            ObjectMarkerBean markerBean = new ObjectMarkerBean();
            markerBean.setBean(o);
            LatLng ll = new LatLng(o.getLatitude(), o.getLongitude());
            if (ConstantValue.NATURE_PRODUCTION.equals(o.getNature())) {//生产经营主体
                markerIcon = R.mipmap.ic_operating_entity_marker;
            } else {//农资门店
                markerIcon = R.mipmap.ic_farm_capital_store_marker;
            }
            MarkerOptions markerOption = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(markerIcon)).
                    zIndex(ConstantValue.Z_INDEX).animateType(MarkerOptions.MarkerAnimateType.none);
            markerBean.setMarker((Marker) (mBaiduMap.addOverlay(markerOption)));
//            markerOption o.setMarker((Marker) (mBaiduMap.addOverlay(markerOption)));
            markerArrayList.add(markerBean);

        }
        MapStatus.Builder builder = new MapStatus.Builder();
        LatLng center = new LatLng(mCurrentLat, mCurrentLon);
        builder.target(center).zoom(4);

    }


    private void showInCheckObject() {
        checking.clear();
        for (ObjectMarkerBean regulateObjectBean : markerArrayList) {
            if (!TextUtil.isEmpty(regulateObjectBean.getBean().getInspstatus()) && !regulateObjectBean.getBean().getInspstatus().equals(ConstantValue.OBJ_CHECK_STATUS_FINISH)) {
                checking.add(regulateObjectBean);
            }
        }
        /**
         * 没有未检查的企业
         */
        if (null == checking || checking.size() == 0) {
            return;
        }
        /**
         * 如果只有一家未检查的企业
         */
        if (null != checking && checking.size() == 1) {
            showWindow(checking.get(0).getBean());
            for (ObjectMarkerBean regulateObjectBean : markerArrayList) {
                if (regulateObjectBean != checking.get(0)) {
                    regulateObjectBean.getMarker().remove();
                }
            }
            return;
        }
        checkingEntAdapter = new CheckingEntAdapter(checking, getActivity(), R.layout.item_checking_ent);
        checkingEntWindow.getRecyclerView().setAdapter(checkingEntAdapter);
        checkingEntWindow.showAtLocation(getLayoutResource(), Gravity.CENTER, 0, 0);
        checkingEntAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RegulateObjectBean objectBean = checking.get(i).getBean();
                if (!objectBean.getOisuper().equals(ueserExtId)) {
                    Toast.makeText(getActivity(), String.format("该企业正在被%s检查，有问题请与%s联系", objectBean.getOisupername(), objectBean.getOisupername()), Toast.LENGTH_LONG).show();
                    return;
                }

                validate(SiteActivity.CONTINUE_TASK,objectBean);
//                objectId = objectBean.getId();
//                Intent intent = new Intent();
//                intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, objectBean);
//                intent.putExtra(ConstantValue.KEY_OBJECT_ID, objectBean.getId());
//                intent.setClass(getActivity(), SiteCheckActivity.class);
//                startActivityForResult(intent, ConstantValue.REQUEST_CODE_SITE_CHECK);
//                checkingEntWindow.dismiss();
            }
        });

    }


    @Override
    public void onDestroy() {
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        //为系统的方向传感器注册监听器
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        window.dismiss();
        newTaskWindow.dismiss();
        checkingEntWindow.dismiss();
        super.onPause();
    }

    //传感器
    @Override
    public void onSensorChanged(SensorEvent event) {

        double x = event.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) < 1.0) {
            mCurrentDirection = (int) x;
            mLocData = new MyLocationData.Builder().accuracy(mCurrentAccracy).direction(mCurrentDirection)
                    .longitude(mCurrentLon).latitude(mCurrentLat).build();
            mBaiduMap.setMyLocationData(mLocData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (ObjectMarkerBean o : markerArrayList) {
            if (marker == o.getMarker()) {
                showWindow(o.getBean());
            } else {
                o.getMarker().remove();
            }
        }
        return true;
    }

    /**
     * 显示企业信息的弹框
     *
     * @param o
     */

    private void showWindow(final RegulateObjectBean o) {
        String staus;
        window.setObj(o);
        window.getTvAddress().setText(o.getAddress());
        if (TextUtil.isEmpty(o.getInspcount())) {
            window.getTvCount().setText("无");
        } else {
            window.getTvCount().setText(o.getInspcount());
        }

        if (TextUtil.isEmpty(o.getPassrate())) {
            window.getTvRate().setText("无");
        } else {
            window.getTvRate().setText(o.getPassrate());
        }

        if (TextUtil.isEmpty(o.getEntcredit())) {
            window.getTvPoint().setText("无");
        } else {
            window.getTvPoint().setText(o.getEntcredit());
        }

        window.getTvObjectName().setText(o.getName());
        window.getTvTel().setText(o.getContactPhone());

        if (null == o.getInspstatus() ||
                !o.getInspstatus().equals(ConstantValue.OBJ_CHECK_STATUS_NEW)) {
            staus = "开启新的检查";
            window.getLayoutRegulator().setVisibility(View.GONE);
            window.getBtnCheck().setBackground(getResources().getDrawable(R.drawable.bg_btn_check_finish));
        } else {
            window.getLayoutRegulator().setVisibility(View.VISIBLE);
            window.getTvRegulator().setText(o.getOisupername());
            window.getBtnCheck().setBackground(getResources().getDrawable(R.drawable.bg_btn_checking));
            staus = "继续检查";
        }
        window.getBtnCheck().setText(staus);
        window.getContentview().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        window.showAtLocation(getLayoutResource(), Gravity.BOTTOM, 0, 0);

        window.getLayoutCount().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegulateListAvtivity.class);
                intent.putExtra(ConstantValue.CODE, "ent");
                intent.putExtra(ConstantValue.KEY_OBJECT_ID, o.getId());
                startActivity(intent);
            }
        });
    }


    /**
     * 跳转的方法
     * 如果是开启新的检查，跳转到选择检查表的页面
     * 如果是继续检查，跳转到判定检查结果的页面
     */
    @Override
    public void task(final RegulateObjectBean objectBean) {

        window.dismiss();
        /**
         * 开启新的检查任务
         */
        if (TextUtil.isEmpty(objectBean.getInspstatus()) || ConstantValue.OBJ_CHECK_STATUS_FINISH.equals(objectBean.getInspstatus())) {
            newTaskWindow.showAtLocation(getLayoutResource(), Gravity.CENTER, 0, 0);
            newTaskWindow.setPassButtonOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    validate(SiteActivity.NEW_TASK,objectBean);
//                    Intent intent = new Intent();
//                    objectId = objectBean.getId();
//                    intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, objectBean);
//                    intent.putExtra(ConstantValue.KEY_OBJECT_ID, objectBean.getId());
//                    intent.setClass(getActivity(), CheckTableSelect2Activity.class);
//                    startActivityForResult(intent, ConstantValue.REQUEST_CODE_SITE_CHECK);
                }
            });
        } else {
            if (!objectBean.getOisuper().equals(ueserExtId)) {
                Toast.makeText(getActivity(), String.format("该企业正在被%s检查，有问题请与%s联系", objectBean.getOisupername(), objectBean.getOisupername()), Toast.LENGTH_LONG).show();
                return;
            }
            validate(SiteActivity.CONTINUE_TASK,objectBean);
//            Intent intent = new Intent();
//            objectId = objectBean.getId();
//            intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, objectBean);
//            intent.putExtra(ConstantValue.KEY_OBJECT_ID, objectBean.getId());
//            intent.setClass(getActivity(), SiteCheckActivity.class);
//            startActivityForResult(intent, ConstantValue.REQUEST_CODE_SITE_CHECK);
        }
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
                        List<RegulateObjectBean> beans = object.getJSONArray(ConstantValue.RESULT).toJavaList(RegulateObjectBean.class);
                        if (0 == beans.size()) {
                            mRegulateObjectArrayList.addAll(dao.queryBuilder().where(
                                    RegulateObjectBeanDao.Properties.UserId.eq(ueserExtId)
                            ).list());
                            return null;
                        }
                        mRegulateObjectArrayList.clear();
                        dao.deleteAll();
                        for (RegulateObjectBean r : beans) {
                            r.setUserId(ueserExtId);
                            r.setLegalName(r.getEnterprise().getLegalName());
                            mRegulateObjectArrayList.add(r);
                            dao.insertOrReplace(r);
                        }

                        return mRegulateObjectArrayList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<RegulateObjectBean>>() {
                    @Override
                    public void call(List<RegulateObjectBean> o) {
                        addOverLay();
                        showInCheckObject();
                    }
                });


    }

    @Override
    public void onError(ApiException e, String method) {

        LogUtils.d(e.getMessage());
        if (method.equals(getObjectApi.getMethod())) {
            mRegulateObjectArrayList.clear();
            mRegulateObjectArrayList.addAll(dao.queryBuilder()
                    .where(RegulateObjectBeanDao
                            .Properties.UserId
                            .eq(ueserExtId))
                    .list());
        }
        addOverLay();
        showInCheckObject();
    }

    /**
     * 定位监听Listener
     */

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (null == mBaiduMap && null == bdLocation) {
                return;
            }
            // TODO: 2018/11/5 这个定位实际发布的时候要改一下
            mCurrentLat = bdLocation.getLatitude();
            mCurrentLon = bdLocation.getLongitude();
            mCurrentAccracy = bdLocation.getRadius();
            mLocData = new MyLocationData.Builder().
                    accuracy(mCurrentAccracy).
                    direction(mCurrentDirection).
                    latitude(mCurrentLat).
                    longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(mLocData);
            if (isFisrtLoc) {//如果是第一次定位
                isFisrtLoc = false;
//                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                LatLng ll = new LatLng(mCurrentLat, mCurrentLon);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(14f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_REARCH:
                    String id = data.getStringExtra(ConstantValue.OBJECT_ID);
                    for (ObjectMarkerBean bean : markerArrayList) {
                        if (bean.getBean().getId().equals(id)) {
                            showWindow(bean.getBean());
                        } else {
                            bean.getMarker().remove();
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
                    manager.doHttpDeal(getObjectApi);
                    break;
            }
        } else if (resultCode == RESULT_CANCELED && requestCode == ConstantValue.REQUEST_CODE_SITE_CHECK) {
            manager.doHttpDeal(getObjectApi);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    PopupWindow.OnDismissListener onDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            addOverLay();
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            manager.doHttpDeal(getObjectApi);
        }
    }

    public void refresh() {
        manager.doHttpDeal(getObjectApi);
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

}
