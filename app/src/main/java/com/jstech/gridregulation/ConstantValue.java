package com.jstech.gridregulation;

import android.Manifest;
import android.os.Environment;

import com.alibaba.fastjson.JSONObject;
import com.arcsoft.face.FaceEngine;

public class ConstantValue {


    public static final String APP_ID = "AneZwRBzm9iXeBdxQBRcPtRXSx3YS6n8qrHsdhzW71E2";
    public static final String SDK_KEY = "23J7UvrwBEeMcVWMCgH9UaPdjaWMAZrgkXTDSkx5ut9F";

    public static final String SD_SRC = Environment.getExternalStorageDirectory() + "/GRID_REGULATE/";

    /**
     * baiduMap 常量
     */
    final public static int ACCURACY_CIRCLE_FILL_COLOR = 0xAAFFFF88;//自定义精度圈填充颜色
    final public static int ACCURACY_CIRCLE_STROKE_COLOR = 0xAAFFFF88;//自定义精度圈边框颜色
    final public static String COOR_TYPE_BD0911 = "bd09ll";//坐标类型bd0911
    final public static int Z_INDEX = 5;//
    final public static String RESULT_QUALIFIED = "2";//合格
    final public static String RESULT_BASIC_QUALIFIED = "1";//基本合格
    final public static String RESULT_UNQUALIFIED = "0";//不合格
    final public static int REQUEST_CODE_SITE_CHECK = 108;//

    final public static String KEY_CHECK_STATUS = "check_status";


    final public static String CODE = "code";
    final public static String RESULT = "result";

    final public static String OBJ_CHECK_STATUS_NEW = "0";//企业的检查状态--检查中
    final public static String OBJ_CHECK_STATUS_CONTINUE = "1";//继续检查
    final public static String OBJ_CHECK_STATUS_FINISH = "2";//继续检查
    final public static String OBJ_CHECK_STATUS_NULL = "2";//未开始检查

    final public static String KEY_CONTENT = "content";//企业的检查状态--检查中

    final public static String KEY_OBJECT_BEAN = "KEY_OBJECT_BEAN";
    final public static String KEY_OBJECT_ID = "KEY_OBJECT_ID";

    final public static String KEY_TABLE_ID = "KEY_TABLE_ID";
    final public static String KEY_TASK_ID = "KEY_TASK_ID";

    final public static String TABLES = "tables";
    final public static String ITEMS = "items";
    final public static String OBJECT_ID = "id";
    final public static String REGULATE_ITEM_SELECT_AGAIN = "again";//重新选择检查内容

    final public static String PATH_SIGN_PICTURE = "GRID_REGULATE/PICTURE";
    final public static String PATH_SIGN_PICTURE_COMPRESS = PATH_SIGN_PICTURE + "/GRID_REGULATE/PICTURE";
    final public static String PATH_SIGN_PICTURE_COMPRESS_WATERMARK = "/GRID_REGULATE/watermark_pictures";
    final public static String PATH_PDF = "/GRID_REGULATE/PDF";

    final public static String CODE_SUCCESS = "200";//成功的返回码

    final public static String OBJECT_COUNT = "count";//成功的返回码

    final public static String NATURE_STORE = "1";//农资门店
    final public static String NATURE_PRODUCTION = "0";//生产主体
    final public static String NATURE_PRODUCTION_CROP = "2";//生产主体-养殖业
    final public static String NATURE_PRODUCTION_BREED = "1";//生产主体-种植业

    final public static String FILE_DATA = "Filedata";//上传图片用的key值
    final public static String SITE_REGULATE_RESULT = "result";//成功的返回码

    final public static String SGIN_PATH = "path";//签名图片
    final public static String DATA = "data";//图片
    final public static String WATER_MARK = "网格化监管" + "\n";
//            + MyApplication.getInstance().getUserBean().getUserName()+"\n";//

    public final static int REQUEST_CODE_REGULATOR_1 = 100;
    public final static int REQUEST_CODE_REGULATOR_2 = 101;
    public final static int REQUEST_CODE_OBJECT = 102;


    final public static String NULL = "无";//

    final public static String KEY_LOCAL = "LOCAL";//
    final public static String KEY_ONLINE = "ONLINE";//
    final public static String KEY_ITEMS = "items";//
    final public static String KEY_ITEM_ID = "item_id";//


    final public static int processMask = FaceEngine.ASF_AGE | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS;

    public final static int ACTION_REQUEST_PERMISSIONS = 0x001;

    /**
     * 所需的所有权限信息
     */
    public final static String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
/*            ,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS*/
    };

    public static boolean isSuccess(JSONObject result) {
        String code = result.getString(ConstantValue.CODE);
        if (code.equals(ConstantValue.CODE_SUCCESS)) {
            return true;
        }
        return false;
    }

}
