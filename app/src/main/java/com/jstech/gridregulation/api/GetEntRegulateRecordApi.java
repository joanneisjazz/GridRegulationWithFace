package com.jstech.gridregulation.api;

import com.jstech.gridregulation.http.HttpPostService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 获取检查项目列表
 */
public class GetEntRegulateRecordApi extends BaseApi {


    private String url = "/grid/api/ta/supervi/get/supervi/insp/list/";
    String param;
    int start;
    int length;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public GetEntRegulateRecordApi() {
        setCache(false);
        setMethod(MyUrl.GET_ENT_REGULATE_RECORD);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService service = retrofit.create(HttpPostService.class);
        return service.getEntReuglateRecord(getLength(), getStart(), getParam());

    }
}
