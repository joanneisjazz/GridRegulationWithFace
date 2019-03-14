package com.jstech.gridregulation.api;

import com.jstech.gridregulation.http.HttpPostService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Retrofit;
import rx.Observable;

public class UploadFileApi extends BaseApi {


    MultipartBody.Part part;

    public MultipartBody.Part getPart() {
        return part;
    }

    public void setPart(MultipartBody.Part part) {
        this.part = part;
    }

    List<MultipartBody.Part> parts;

    public List<MultipartBody.Part> getParts() {
        return parts;
    }

    public void setParts(List<MultipartBody.Part> parts) {
        this.parts = parts;
    }

    public UploadFileApi() {
        setCache(false);
        setMethod(MyUrl.UPLOAD_FILE);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        return retrofit.create(HttpPostService.class).uploadFile(getPart());
    }
}
