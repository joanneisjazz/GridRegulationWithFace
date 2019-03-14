package com.jstech.gridregulation.listener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

/**
 * Created by hesm on 2018/11/8.
 */

public class MyLocationListener implements BDLocationListener {

    double lat;
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        //获取定位结果
        lat = bdLocation.getLatitude();
        if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {

        } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {

        } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {


        } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {

        } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {

        } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {

        }


    }
}
