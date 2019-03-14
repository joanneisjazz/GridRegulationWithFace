package com.jstech.gridregulation.api;

import com.jstech.gridregulation.http.HttpPostService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;

import retrofit2.Retrofit;
import rx.Observable;

public class UpdateApkApi extends BaseApi {

    private static final int type = 1;


    public UpdateApkApi() {
        setCache(false);
        setMethod(MyUrl.UPDATE_APK);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        return retrofit.create(HttpPostService.class).updateApk();
    }
}
