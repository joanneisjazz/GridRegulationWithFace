package com.jstech.gridregulation.api;

import com.jstech.gridregulation.http.HttpPostService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 保存检查项的结果
 */
public class SaveItemResultApi extends BaseApi {

    private List<RegulateResultBean> params;


    public SaveItemResultApi() {
        setCache(false);
        setMethod(MyUrl.SAVE_ITEMS);
    }

    public SaveItemResultApi(List<RegulateResultBean>  params) {
        this.params = params;
        setCache(false);
        setMethod(MyUrl.SAVE_ITEMS);
    }

    public List<RegulateResultBean>  getParams() {
        return params;
    }

    public void setParams(List<RegulateResultBean>  params) {
        this.params = params;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService service = retrofit.create(HttpPostService.class);
        return service.saveItemResult(getParams());
    }
}
