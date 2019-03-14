package com.jstech.gridregulation.api;

import com.jstech.gridregulation.bean.SaveResultBean;
import com.jstech.gridregulation.http.HttpPostService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 更新检查任务检查结果或删除检查任务
 */
public class DeteleTaskApi extends BaseApi {

    SaveResultBean bean;

    public SaveResultBean getBean() {
        return bean;
    }

    public void setBean(SaveResultBean bean) {
        this.bean = bean;
    }


    public DeteleTaskApi() {
        setCache(false);
        setMethod(MyUrl.DELETE_TASK);
    }


    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService service = retrofit.create(HttpPostService.class);
        return service.deteleTask(getBean());
    }
}
