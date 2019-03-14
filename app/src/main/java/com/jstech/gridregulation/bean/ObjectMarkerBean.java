package com.jstech.gridregulation.bean;

import com.baidu.mapapi.map.Marker;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;

public class ObjectMarkerBean {

    private Marker marker;
    private RegulateObjectBean bean;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public RegulateObjectBean getBean() {
        return bean;
    }

    public void setBean(RegulateObjectBean bean) {
        this.bean = bean;
    }
}
