package com.jstech.gridregulation.api;

import com.jstech.gridregulation.http.HttpPostService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 获取所有的检查表和检查项
 */
public class GetTableAndItem extends BaseApi {


    public GetTableAndItem() {
        setCache(false);
        setMethod(MyUrl.GET_TABLE_AND_ITEM);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService service = retrofit.create(HttpPostService.class);
        return service.getTableAndItem(MyUrl.GET_TABLE_AND_ITEM);
    }
}
