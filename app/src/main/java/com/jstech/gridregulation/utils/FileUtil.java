package com.jstech.gridregulation.utils;

import com.jstech.gridregulation.ConstantValue;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

/**
 * Created by hesm on 2018/11/6.
 */

public class FileUtil {

    public static MultipartBody.Part uploadFile(String path) {
        if (TextUtil.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        if (null == file || !file.exists())
            return null;

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData(ConstantValue.FILE_DATA, file.getName(), requestBody);
    }
}
