package com.jstech.gridregulation.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

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
import com.jstech.gridregulation.api.GetObjectApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.utils.LogUtils;
import com.jstech.gridregulation.utils.SystemUtil;
import com.jstech.gridregulation.widget.MapBottomWindow;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.ArrayList;

import butterknife.BindView;

//import com.jstech.gridregulation.db.RegulateObjectBeanDao;

@SuppressLint("Registered")
public class SiteInspectionObjectMapActivity extends BaseActivity implements
        SensorEventListener, BaiduMap.OnMarkerClickListener, MapBottomWindow.TaskInterface, HttpOnNextListener {

    @BindView(R.id.mapview)
    MapView mMapView;
    BaiduMap mBaiduMap;

    public static MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

    LocationClient mLocClient;
    MyLocationListener myLocationListener = new MyLocationListener();
    private SensorManager sensorManager;
    private double lastX = 0.0;
    //112.787601,38.068948 测试的
    private double mCurrentLat = 37.760252;
    private double mCurrentLon = 112.48668;
    private int mCurrentDirection = 0;
    private float mCurrentAccracy;

    boolean isFisrtLoc = true;
    private MyLocationData mLocData;

    ArrayList<RegulateObjectBean> mRegulateObjectArrayList = new ArrayList<>();//监管对象list

    MapBottomWindow window;
    HttpManager manager;
    GetObjectApi getObjectApi;

    boolean isSaveData = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_site_inspection_object_map;
    }

    @Override
    public void initView() {
        setMap();
        initWindow();


        MyLocationData locData = new MyLocationData.Builder().accuracy(mCurrentAccracy).direction(mCurrentDirection)
                .latitude(mCurrentLat).longitude(mCurrentLon).build();
        mBaiduMap.setMyLocationData(locData);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, null));
        mMapView.setLogoPosition(LogoPosition.logoPostionCenterBottom);
        manager = new HttpManager(this, this);
        getObjectApi = new GetObjectApi();
        getObjectApi.setId("140100");
        manager.doHttpDeal(getObjectApi);
    }

    private void setMap() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器服务
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        //开启定位监听
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setScanSpan(100);
        option.setCoorType(ConstantValue.COOR_TYPE_BD0911);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mBaiduMap.setOnMarkerClickListener(this);
    }

    private void initWindow() {
        window = new MapBottomWindow.Builder().setContext(SiteInspectionObjectMapActivity.this)
                .setOutSideCancel(true)
                .setFouse(true)
                .setwidth(SystemUtil.getWith(this))
                .setheight(SystemUtil.getHeight(this) * 2 / 7)
                .setListener(this)
                .builder();
    }

    //测试的监管对象
    private void addOverLay() {
//        LatLng llA = new LatLng(38.071902, 112.79026);//112.79026,38.071902
//        LatLng llB = new LatLng(38.067244, 112.792416);//112.792416,38.067244
//        LatLng llC = new LatLng(38.07338, 112.76712);//112.76712,38.07338
//        LatLng llD = new LatLng(38.065255, 112.781636);//112.781636,38.065255

        int markerIcon = -1;
        for (RegulateObjectBean o : mRegulateObjectArrayList) {
            LatLng ll = new LatLng(o.getLatitude(), o.getLongitude());
            if ("0".equals(o.getNature())) {//生产经营主体
                markerIcon = R.mipmap.ic_operating_entity_marker;
            } else {//农资门店
                markerIcon = R.mipmap.ic_farm_capital_store_marker;
            }
            MarkerOptions markerOption = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(markerIcon)).
                    zIndex(ConstantValue.Z_INDEX).animateType(MarkerOptions.MarkerAnimateType.drop);
//            o.setMarker((Marker) (mBaiduMap.addOverlay(markerOption)));

        }
    }

    @Override
    protected void onDestroy() {
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        //为系统的方向传感器注册监听器
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        window.dismiss();
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
        String staus;
        for (RegulateObjectBean o : mRegulateObjectArrayList) {
//            if (marker == o.getMarker()) {
//                window.setObj(o);
//                window.getTvAddress().setText("详细地址：" + o.getAddress());
//                window.getTvDetails().setText("检查次数：" + o.getInspcount() + " | " + "检查合格率：" + o.getPassrate());
//                window.getTvDistance().setText("300m");
//                window.getTvObjectName().setText(o.getName());
//                window.getTvTel().setText("联系电话：" + o.getContactPhone());
//                if (o.getInspstatus() == ConstantValue.OBJ_CHECK_STATUS_NEW) {
//                    staus = "开启新的检查";
//                } else {
//                    staus = "继续检查";
//                }
//                window.getBtnCheck().setText(staus);
//
//                window.showAtLocation(getLayoutId(), Gravity.BOTTOM, 0, 0);
////                Toast.makeText(SiteInspectionObjectMapActivity.this, o.getAddress(), Toast.LENGTH_LONG).show();
//            }
        }
        return true;
    }


    /**
     * 跳转的方法
     * 如果是开启新的检查，跳转到选择检查表的页面
     * 如果是继续检查，跳转到判定检查结果的页面
     */
    @Override
    public void task(RegulateObjectBean objectBean) {
        Intent intent = new Intent();
        if (objectBean.getInspstatus() == ConstantValue.OBJ_CHECK_STATUS_NEW) {
            intent.putExtra(ConstantValue.KEY_OBJECT_ID, objectBean.getId());
            intent.setClass(this, CheckTableSelectActivity.class);
        } else {
            intent.setClass(this, CheckItemSelectActivity.class);
        }
        startActivity(intent);
    }

    @Override
    public void onNext(String resulte, String method) {
        LogUtils.d(resulte);
        LogUtils.d(method);
        JSONObject o = JSON.parseObject(resulte);
        String code = o.getString(ConstantValue.CODE);
        if (method.equals(MyUrl.GET_ENTERPRISE)) {
            if ("200".equals(code)) {
                mRegulateObjectArrayList = (ArrayList<RegulateObjectBean>) o.getJSONArray(ConstantValue.RESULT).toJavaList(RegulateObjectBean.class);
                addOverLay();
                LogUtils.d(mRegulateObjectArrayList.size() + "");
            }
        }

        //开一个新线程保存数据到本地
        new Thread(new Runnable() {
            @Override
            public void run() {
                isSaveData = true;
                MyApplication app = (MyApplication) getApplication();
//                RegulateObjectBeanDao dao = app.getSession().getRegulateObjectBeanDao();
//                dao.insertOrReplaceInTx(mRegulateObjectArrayList);
                isSaveData = false;
            }
        }).start();
    }

    @Override
    public void onError(ApiException e, String method) {

        LogUtils.d(e.getMessage());
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
//            mCurrentLat = bdLocation.getLatitude();
//            mCurrentLon = bdLocation.getLongitude();
            mCurrentAccracy = bdLocation.getRadius();
            mLocData = new MyLocationData.Builder().
                    accuracy(mCurrentAccracy).
                    direction(mCurrentDirection).
                    latitude(mCurrentLat).
                    longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(mLocData);
            if (isFisrtLoc) {//如果是第一次定位
                isFisrtLoc = false;
//                LatLng ll = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                LatLng ll = new LatLng(mCurrentLat, mCurrentLon);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }


}
