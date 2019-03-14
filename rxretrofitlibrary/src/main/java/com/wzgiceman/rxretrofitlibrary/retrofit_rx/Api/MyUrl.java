package com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api;

/**
 * 存放网络请求的url地址
 */
public class MyUrl {

    //grid/api/ta/supervi/get/enterprise/list/
    public final static String SUPERVI = "grid/api/ta/supervi/";//现场检查的接口
    public final static String SUPERVI_GET = SUPERVI + "get/";
    public final static String GET_ENTERPRISE = SUPERVI_GET + "enterprise/list/";//获取监管对象的信息
    public final static String GET_TABLE = SUPERVI_GET + "norm/table/list/";//获取检查表
    public final static String GET_ITEM = SUPERVI_GET + "norm/item/list/";//获取检查项目
    public final static String GET_TABLE_AND_ITEM = SUPERVI_GET + "norm/ti/list";//获取所有的检查表和检查项
    public final static String SUPERVI_SAVE = SUPERVI + "save/";
    public final static String ADD_TASK = SUPERVI_SAVE + "supervi/insp";//增加检查任务
    public final static String SAVE_ITEMS = SUPERVI_SAVE + "supervi/items";//保存检查项及结果
    public final static String SAVE_REGULATE_RESULT = SUPERVI + "update/supervi/insp/true";//保存整个检查任务的结果
    public final static String DELETE_TASK = SUPERVI + "update/supervi/insp/false";//删除检查任务
    public final static String UPLOAD_IMAGE = "open/file/upload/image";//上传图片
    public final static String SAVE_ILLEGAL_CASE = "grid/api/ta/illegal/case/save/illegal/case";//
    public final static String GET_ILLEGAL_CASE = "grid/api/ta/illegal/case/get/illegal/case/list/";//
    public final static String GET_REGULATE_RECORD = SUPERVI_GET + "supervi/insp/list/";//
    public final static String GET_RO_NAME = "grid/api/xiao/user/get/user/infor/";//
//    public final static String GET_RO_NAME = "grid/api/xiao/user/get/people/info/";//

    public final static String GET_ITEM_RESULT = "grid/api/ta/supervi/get/supervi/item/list/";//

    public final static String GET_ENT_REGULATE_RECORD = "grid/api/ta/supervi/get/supervi/insp/list/byentid/";//
    public final static String SAVE_USER_INFO = "grid/api/xiao/user/update/user/infor";

    public final static String GET_CITYS = "grid/api/xiao/user/get/city/";//

    public final static String UPLOAD_FILE = "open/file/upload/file";//上传文件

    public final static String UPDATE_APK = "ucenter/api/ta/app/version/get/latest/app/2";//下载apk

    public final static String FACE_VALIDATE = "grid/api/ta/illegal/case/compare/face";//上传图片

    public final static String UPDATE_PROFILE = "/grid/api/xiao/user/update/people/image";//更新头像
    /**
     * 测试和打包的时候需要修改的访问地址
     */
    public final static String IP_47 = "http://47.104.227.130:";//阿里云的ip
    public final static String IP_218 = "http://218.26.228.85:";//政务云的ip
    public final static String IP_110 = "http://192.168.0.110:";//田永威的ip
    public final static String IP_56 = "http://192.168.0.56:";//56服务器的ip
    public final static String IP_106 = "http://106.12.213.47:";//百度云服务器的ip

    /**
     * 阿里云
     */
    public final static String PORT_47_GRID = "8680/";//网格化监管的端口号
    public final static String PORT_47_UPDATE = "8083/";//版本更新的端口号
    public final static String PORT_47_UPLOAD = "8083/";//文件上传的端口号
    public final static String PORT_47_LOGIN = "8082/";//登录的端口号

    /**
     * 政务云
     */
    public final static String PORT_218_GRID = "8089/";
    public final static String PORT_218_UPDATE = "8087/";
    public final static String PORT_218_UPLOAD = "8083/";
    public final static String PORT_218_LOGIN = "8082/";

    /**
     * 田永威的端口
     */
    public final static String PORT_110 = "8090/";//田永威的ip

    /**
     * 56的端口
     */
    public final static String PORT_56_GRID = "8680/";
    public final static String PORT_56_UPDATE = "8980/";
    public final static String PORT_56_UPLOAD = "8083/";
    public final static String PORT_56_LOGIN = "8082/";

    /**
     * 百度云的端口
     */
    public final static String PORT_106_GRID = "8980/js-grid/";
    public final static String PORT_106_UPDATE = "8087/";
    public final static String PORT_106_UPLOAD = "8083/";
    public final static String PORT_106_LOGIN = "8082/";


}
