package com.jstech.gridregulation.http;

import com.jstech.gridregulation.bean.ProfileBean;
import com.jstech.gridregulation.bean.SaveResultBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.IpAndPort;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CaseEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.UserBean;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

public interface HttpPostService {
    //移动端  登录
    @POST
    Observable<String> appLogin(@Url String url, @Body UserBean user);

    @POST(MyUrl.GET_RO_NAME + "{extId}")
    Observable<String> getRoName(@Path("extId") String extId);


    //    @HTTP(method = "POST", path = MyUrl.ENTERPRISE + "{id}" )
    @POST(MyUrl.GET_ENTERPRISE + "{length}/{start}/{id}")
    Observable<String> getEnterprise(@Path("length") int length,
                                     @Path("start") int start,
                                     @Path("id") String id);//获取监管对象

    @POST
    Observable<String> getTable(@Url String url);//获取检查表

    @POST
    Observable<String> getItem(@Url String url);//获取检查项目

    @POST
    Observable<String> getTableAndItem(@Url String url);//获取检查项目

    @POST(MyUrl.ADD_TASK)
    Observable<String> addTask(@Body TaskBean params);//新增检查任务

    @POST(MyUrl.SAVE_ITEMS)
    Observable<String> saveItemResult(@Body List<RegulateResultBean> params);//保存检查项及结果

    @POST(MyUrl.SAVE_REGULATE_RESULT)
    Observable<String> saveResult(@Body SaveResultBean params);//保存检查任务的具体信息

    @POST(MyUrl.DELETE_TASK)
    Observable<String> deteleTask(@Body SaveResultBean params);//删除检查任务

    //    @Multipart
//    @POST(MyUrl.UPLOAD_IMAGE)
//    Observable<String> uploadImage(@Part(ConstantValue.FILE_DATA) RequestBody params);
    @Multipart
//    @POST("http://192.168.0.56:8083/open/file/upload/images")
//    @POST("http://47.104.227.130:8083/open/file/upload/images")
//    @POST("http://218.26.228.85:8083/open/file/upload/images")
//    @POST("http://192.168.0.110:8092/open/file/upload/images")
    @POST(IpAndPort.NOW_IP_UPLOAD + IpAndPort.NOW_PORT_UPLOAD + "open/file/upload/images")
    Observable<String> uploadImage(@Part List<MultipartBody.Part> file);//上传图片

    @Multipart
//    @POST("http://192.168.0.56:8083/open/file/upload/file")
//    @POST("http://47.104.227.130:8083/open/file/upload/file")
//    @POST("http://218.26.228.85:8083/open/file/upload/file")
//    @POST("http://192.168.0.110:8092/open/file/upload/file")
    @POST(IpAndPort.NOW_IP_UPLOAD + IpAndPort.NOW_PORT_UPLOAD + "open/file/upload/file")
    Observable<String> uploadFile(@Part MultipartBody.Part file);//上传文件

    @POST(MyUrl.SAVE_ILLEGAL_CASE)
    Observable<String> saveIllegalCase(@Body CaseEntity params);//违法违规案件上报

    @POST
    Observable<String> getIllegalCase(@Url String url);//获取所有的违法违规案件记录

    //获取检查记录
    @POST(MyUrl.GET_REGULATE_RECORD + "{length}/{start}/{oisuper}")
    Observable<String> getRegulateRecord(@Path("length") int length,
                                         @Path("start") int start,
                                         @Path("oisuper") String oisuper);

    @POST
    Observable<String> getPersonalInfo(@Url String url);//获取个人信息

    @POST
    Observable<String> getItemResult(@Url String url);//获取检查项及结果

    @POST(MyUrl.GET_ENT_REGULATE_RECORD + "{length}/{start}/{entid}")
    Observable<String> getEntReuglateRecord(@Path("length") int length,
                                            @Path("start") int start,
                                            @Path("entid") String entid);//获取企业的检查记录

    @POST(MyUrl.SAVE_USER_INFO)
    Observable<String> saveUserInfo(@Body UserBean info);//保存个人信息

    @POST(MyUrl.GET_CITYS)
    Observable<String> getCity();//获取山西省下面所有的市区县

    //    @POST("http://192.168.0.56:8980/" + MyUrl.UPDATE_APK)
//    @POST("http://47.104.227.130:8083/open/file/upload/file")
//    @POST("http://218.26.228.85:8087/" + MyUrl.UPDATE_APK)
//    @POST("http://192.168.0.110:8092/open/file/upload/file")
    @POST(IpAndPort.NOW_IP_UPLOAD + IpAndPort.NOW_PORT_UPLOAD + MyUrl.UPDATE_APK)
    Observable<String> updateApk();//更新apk


    /**
     * 人脸验证
     *
     * @param file
     * @return
     */
    @Multipart
//    @POST(MyUrl.FACE_VALIDATE)
//    @POST("http://192.168.0.55:8080/grid/api/tentent/compare/face")
    @POST("http://106.12.213.47:8980/js-grid/grid/api/tentent/compare/face")
//    @POST(IpAndPort.NOW_IP + IpAndPort.NOW_PORT_GRID + MyUrl.FACE_VALIDATE)
    Observable<String> faceValidate(@Part MultipartBody.Part file);//上传图片


    /**
     * 更新人脸图片
     *
     * @return
     */
    @POST(MyUrl.UPDATE_PROFILE)
    Observable<String> updateProfile(@Body ProfileBean bean);


}
