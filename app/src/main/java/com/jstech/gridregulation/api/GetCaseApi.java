package com.jstech.gridregulation.api;

import com.jstech.gridregulation.http.HttpPostService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 获取检查表列表
 */
public class GetCaseApi extends BaseApi {

    int length;
    int start;
    String by;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public GetCaseApi() {
        setCache(false);
        setMethod(MyUrl.GET_ILLEGAL_CASE);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService service = retrofit.create(HttpPostService.class);
//        return service.enterprise(getId(),"");
        return service.getIllegalCase(MyUrl.GET_ILLEGAL_CASE + getLength() + "/" + getStart() + "/" + getBy());

    }
}
