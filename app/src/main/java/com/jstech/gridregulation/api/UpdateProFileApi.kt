package com.jstech.gridregulation.api

import com.jstech.gridregulation.bean.ProfileBean
import com.jstech.gridregulation.http.HttpPostService
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl
import retrofit2.Retrofit
import rx.Observable

class UpdateProFileApi : BaseApi() {


    var profileBean: ProfileBean? = null

    init {
        cache = false
        method = MyUrl.UPDATE_PROFILE
    }

    override fun getObservable(retrofit: Retrofit): Observable<*> {
        val service = retrofit.create(HttpPostService::class.java)
        return service.updateProfile(profileBean)
    }
}