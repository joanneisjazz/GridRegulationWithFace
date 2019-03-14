package com.jstech.gridregulation.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.utils.LogUtils;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.IpAndPort;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.UserBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hesm on 2018/11/4.
 * 后台定位
 */

public class GPSService extends Service {
    //定位点信息
    public double longtitude;
    public double latitude;
    //    private static final String url = MyUrl.NOW_IP + MyUrl.NOW_PORT_GRID + "/grid/api/xiao/user/update/people/location";
//        private static final String url = "http://218.26.228.85:8089/grid/api/xiao/user/update/people/location";
//    private static final String url = "http://192.168.0.56:8680/grid/api/xiao/user/update/people/location";
//    private static final String url = "http://106.12.213.47:8980/js-grid/grid/api/xiao/user/update/people/location";
//    private static final String url = "http://192.168.1.100:8080/grid/api/xiao/user/update/people/location";
//    private static final String url = "http://47.104.227.130:8089/grid/api/xiao/user/update/people/location";
    private static final String url = IpAndPort.NOW_IP + IpAndPort.NOW_PORT_GRID + "grid/api/xiao/user/update/people/location";
    private String extId = "";

    private LocationClient mLocationClient = null;//定位客户端
    public MyLocationListener mMyLocationListener = new MyLocationListener();
    ;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private boolean isStop = false;

    private RequestQueue requestQueue;
    public static GPSService instance;
    public UserBean bean;


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
        extId = SharedPreferencesHelper.getInstance(GPSService.this).getSharedPreference("extId", "").toString();
        requestQueue = MyApplication.getInstance().getRequestQueue();

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(mMyLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setScanSpan(100);
        option.setCoorType(ConstantValue.COOR_TYPE_BD0911);
        mLocationClient.setLocOption(option);
        bean = MyApplication.getInstance().getUserBean();
        initLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 触发定时器
        if (!isStop) {
            LogUtils.i("定时器启动");
            startTimer();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        super.onDestroy();
        // 停止定时器
        if (isStop) {
            LogUtils.i("定时器服务停止");
            stopTimer();
        }
    }

    /**
     * 定时器 每隔一段时间执行一次
     */
    private void startTimer() {
        isStop = true;//定时器启动后，修改标识，关闭定时器的开关
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {

                @Override
                public void run() {
                    do {
                        try {
                            mLocationClient.start();
//                            Thread.sleep(1000 * 6 * 60);//3秒后再次执行
                            Thread.sleep(1000 * 3 * 60);//3分钟后再次执行

                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } while (isStop);

                }
            };
        }

        if (mTimer != null && mTimerTask != null) {
            mTimer.schedule(mTimerTask, 0);//执行定时器中的任务
        }
    }

    /**
     * 停止定时器，初始化定时器开关
     */
    private void stopTimer() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        isStop = false;//重新打开定时器开关
//        LogUtils.d("isStop=" + isStop);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 定位客户端参数设定，更多参数设置，查看百度官方文档
     *
     * @return
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);

        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);

    }

    /**
     * 定位监听器
     *
     * @author User
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }

            LogUtils.d("locationType == " + location.getLocType());
            LogUtils.d("nettype == " + location.getNetworkLocationType());
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            latitude = location.getLatitude();
            longtitude = location.getLongitude();
            LogUtils.d("latitude == " + latitude);
            LogUtils.d("longtitude == " + longtitude);

            MyApplication.instance.setLatitude(String.valueOf(latitude));
            MyApplication.instance.setLongtitude(String.valueOf(longtitude));

            uploadGPS();
            if (mLocationClient.isStarted()) {
                mLocationClient.stop();
            }

        }

    }


    /**
     * 每隔5分钟上报一次检察人员的经纬度信息
     */
    private void uploadGPS() {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                LogUtils.d("成功====" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.d("失败====" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                BigDecimal bd1 = new BigDecimal(longtitude);
                BigDecimal bd2 = new BigDecimal(latitude);

                Map<String, String> map = new HashMap<String, String>();
                map.put("longitude", longtitude + "");
                map.put("latitude", latitude + "");
                map.put("id", extId);
                return map;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                5 * 1000,//链接超时时间
                0,//重新尝试连接次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(request);

    }

}

