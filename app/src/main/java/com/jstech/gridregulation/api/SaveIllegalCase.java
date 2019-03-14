package com.jstech.gridregulation.api;

import com.jstech.gridregulation.http.HttpPostService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CaseEntity;

import retrofit2.Retrofit;
import rx.Observable;

public class SaveIllegalCase extends BaseApi {

    CaseEntity mCaseEntity;

    public CaseEntity getmCaseEntity() {
        return mCaseEntity;
    }

    public void setmCaseEntity(CaseEntity mCaseEntity) {
        this.mCaseEntity = mCaseEntity;
    }

    public SaveIllegalCase() {
        setCache(false);
        setMethod(MyUrl.SAVE_ILLEGAL_CASE);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService service = retrofit.create(HttpPostService.class);
        return service.saveIllegalCase(getmCaseEntity());
    }
}
