package com.jstech.gridregulation.api;

import com.jstech.gridregulation.http.HttpPostService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.IpAndPort;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.UserBean;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by hesm on 2018/11/6.
 */

public class LoginApi extends BaseApi {


    //    private String url = "http://192.168.0.56:8082/index/appLogin";
//    private String url = "http://106.12.213.47:8082/index/appLogin";
    //    private String url = "http://218.26.228.85:8082/index/appLogin";
//    private String url = "http://47.104.227.130:8082/index/appLogin";
    private String url = IpAndPort.NOW_IP + IpAndPort.NOW_PORT_LOGIN + "index/appLogin";

    UserBean userBean;

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public LoginApi() {
        setCache(false);
        setMethod("index/appLogin");
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        return retrofit.create(HttpPostService.class).appLogin(getUrl(), getUserBean());
    }
}
