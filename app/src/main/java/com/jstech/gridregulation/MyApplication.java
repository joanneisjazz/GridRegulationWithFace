package com.jstech.gridregulation;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Glide;
import com.jstech.gridregulation.utils.RequestQueueUtil;
import com.rxretrofitlibrary.greendao.DaoMaster;
import com.rxretrofitlibrary.greendao.DaoSession;
import com.tencent.bugly.crashreport.CrashReport;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.MySQLiteOpenHelper;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.RxRetrofitApp;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.UserBean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class MyApplication extends Application {
    //服务器 url
    private UserBean userBean;
    public static MyApplication instance;
    private boolean isFirstGetTables = true;
    private DaoSession session;
    private String latitude;
    private String longtitude;


    private RequestQueue requestQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        ApplicationLike tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();
//        // 初始化TinkerPatch SDK, 更多配置可参照API章节中的,初始化SDK
//        if(null!=tinkerApplicationLike){
//            TinkerPatch.init(tinkerApplicationLike)
//                    .reflectPatchLibrary()
////                .setPatchResultCallback(ResultCallBack)
//                    .setPatchRollbackOnScreenOff(true)
//                    .setPatchRestartOnSrceenOff(true)
//                    .setFetchPatchIntervalByHours(10);
//
//            // 每隔3个小时(通过setFetchPatchIntervalByHours设置)去访问后台时候有更新,通过handler实现轮训的效果
//            TinkerPatch.with().fetchPatchUpdateAndPollWithInterval();
//        }

        RxRetrofitApp.init(this, BuildConfig.DEBUG);
        SDKInitializer.initialize(getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        requestQueue = RequestQueueUtil.getRequestQueue(this);
        initDb();
        initCrash();

    }

    private void initDb() {
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, "grid_db", null);
        DaoMaster master = new DaoMaster(helper.getWritableDb());
        session = master.newSession();
        MyApplication.getInstance().setSession(session);
//        initCitys();
    }

    private void initCrash() {
        // 获取当前包名
        String packageName = this.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        // 初始化Bugly
        CrashReport.initCrashReport(this, "e4454cd543", true);

    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    public void setSession(DaoSession session) {
        this.session = session;
    }

    public boolean isFirstGetTables() {
        return isFirstGetTables;
    }

    public void setFirstGetTables(boolean firstGetTables) {
        isFirstGetTables = firstGetTables;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public DaoSession getSession() {
        return session;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }
}
