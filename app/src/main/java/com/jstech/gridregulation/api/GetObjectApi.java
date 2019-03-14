package com.jstech.gridregulation.api;

import com.jstech.gridregulation.http.HttpPostService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 根据所属地区获取企业列表
 */
public class GetObjectApi extends BaseApi {

    String id;
    int length;
    int start;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public GetObjectApi() {
        setCache(false);
        setMethod(MyUrl.GET_ENTERPRISE);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService service = retrofit.create(HttpPostService.class);
//        return service.enterprise(getId(),"");
        return service.getEnterprise(getLength(), getStart(), getId());

    }
}
