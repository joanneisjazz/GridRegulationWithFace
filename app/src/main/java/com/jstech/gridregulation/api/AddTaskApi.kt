package com.jstech.gridregulation.api

import com.jstech.gridregulation.http.HttpPostService
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean

import retrofit2.Retrofit
import rx.Observable

/**
 * 增加检查任务的接口
 */
class AddTaskApi : BaseApi() {

    var params: TaskBean? = null

    init {
        isCache = false
        method = MyUrl.ADD_TASK
    }

    override fun getObservable(retrofit: Retrofit): Observable<*> {
        val service = retrofit.create(HttpPostService::class.java)
        return service.addTask(params)
    }
}
