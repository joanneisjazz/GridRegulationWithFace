package com.jstech.gridregulation.api;

import com.jstech.gridregulation.http.HttpPostService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 获取检查表列表
 */
public class GetRegulateRecordApi extends BaseApi {

    String oisuper;
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

    public String getOisuper() {
        return oisuper;
    }

    public void setOisuper(String oisuper) {
        this.oisuper = oisuper;
    }

    public GetRegulateRecordApi() {
        setCache(false);
        setMethod(MyUrl.GET_REGULATE_RECORD);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService service = retrofit.create(HttpPostService.class);
//        return service.enterprise(getId(),"");
        return service.getRegulateRecord(getLength(),getStart(), getOisuper());

    }
}
