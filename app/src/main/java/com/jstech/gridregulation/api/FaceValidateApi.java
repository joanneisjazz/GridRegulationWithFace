package com.jstech.gridregulation.api;

import com.jstech.gridregulation.http.HttpPostService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;

import okhttp3.MultipartBody;
import retrofit2.Retrofit;
import rx.Observable;

public class FaceValidateApi extends BaseApi {

    private String type;

    MultipartBody.Part part;

    public MultipartBody.Part getPart() {
        return part;
    }

    public void setPart(MultipartBody.Part part) {
        this.part = part;
    }


    public FaceValidateApi() {
        setCache(false);
        setMethod(MyUrl.FACE_VALIDATE);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        return retrofit.create(HttpPostService.class).faceValidate(getPart());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
