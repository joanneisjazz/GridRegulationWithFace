package com.jstech.gridregulation.api;

import com.jstech.gridregulation.http.HttpPostService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 获取检查项目列表结果
 */
public class GetItemResultApi extends BaseApi {

    String param;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public GetItemResultApi() {
        setCache(false);
        setMethod(MyUrl.GET_ITEM_RESULT);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService service = retrofit.create(HttpPostService.class);
//        return service.enterprise(getId(),"");
        return service.getItemResult(MyUrl.GET_ITEM_RESULT + getParam());

    }
}
