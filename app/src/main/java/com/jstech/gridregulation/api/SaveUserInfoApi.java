package com.jstech.gridregulation.api;

import com.jstech.gridregulation.http.HttpPostService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.UserBean;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by hesm on 2018/11/2.
 * 保存用户的信息
 */

public class SaveUserInfoApi extends BaseApi {

    public SaveUserInfoApi() {
        setCache(false);
        setMethod(MyUrl.SAVE_USER_INFO);
    }

    private UserBean userInfoBean;

    public UserBean getUserInfoBean() {
        return userInfoBean;
    }

    public void setUserInfoBean(UserBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        return retrofit.create(HttpPostService.class).saveUserInfo(getUserInfoBean());
    }
}
