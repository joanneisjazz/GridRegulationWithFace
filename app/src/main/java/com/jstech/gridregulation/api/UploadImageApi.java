package com.jstech.gridregulation.api;

import com.jstech.gridregulation.http.HttpPostService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Retrofit;
import rx.Observable;

public class UploadImageApi extends BaseApi {

    /**
     * type：1 上传检查人签字
     * type：2 上传检查对象签字
     * type：3 上传检查项图片
     */
    private String type;

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

    public UploadImageApi() {
        setCache(false);
        setMethod(MyUrl.UPLOAD_IMAGE);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        return retrofit.create(HttpPostService.class).uploadImage(getParts());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
