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
public class SaveResultApi extends BaseApi {

    SaveResultBean bean;
    boolean is;

    public SaveResultBean getBean() {
        return bean;
    }

    public void setBean(SaveResultBean bean) {
        this.bean = bean;
    }

    public boolean isIs() {
        return is;
    }

    public void setIs(boolean is) {
        this.is = is;
    }

    public SaveResultApi() {
        setCache(false);
        setMethod(MyUrl.SAVE_REGULATE_RESULT);
    }


    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService service = retrofit.create(HttpPostService.class);
        return service.saveResult(getBean());
    }
}
