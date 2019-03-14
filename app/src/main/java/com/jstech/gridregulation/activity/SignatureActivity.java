package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.api.UploadImageApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.utils.ImageUtil;
import com.jstech.gridregulation.utils.LogUtils;
import com.jstech.gridregulation.utils.PictureSelectorUtil;
import com.jstech.gridregulation.utils.TextUtil;
import com.rxretrofitlibrary.greendao.FileEntityDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.FileEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 手写签字
 * 检查人签字（2个）
 * 被检企业负责人签字（1个）
 */
public class SignatureActivity extends BaseActivity implements View.OnClickListener, HttpOnNextListener {

    private SignaturePad signaturePad;
    private LinearLayout layoutRoot;
    private Button btnSave;
    private Button btnClear;
//    private ImageView ivSign;

    public int signType;//

    private String strPictureName = "";
    //    private String strPath = "";
    private String taskId = "";
    private String localPath = "";
    private String onlinePath = "";

//    TaskBeanDao taskBeanDao;
//    TaskBean taskBean;

    private HttpManager manager;
    private UploadImageApi uploadImageApi;

    private boolean isSign = false;

    FileEntity entity;
    FileEntityDao fileEntityDao;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_signature;
    }

    @Override
    public void initView() {
        setToolBarTitle("签字");
        setToolSubBarTitle("");
        layoutRoot = findViewById(R.id.layout_root);
        signaturePad = findViewById(R.id.sign_pad);
        btnSave = findViewById(R.id.btn_save);
        btnClear = findViewById(R.id.btn_clear);
//        ivSign = findViewById(R.id.iv_sign);
        fileEntityDao = MyApplication.getInstance().getSession().getFileEntityDao();
        getData();
        setListener();
        uploadImageApi = new UploadImageApi();
        manager = new HttpManager(this, this);

    }

    private void getData() {
        signType = getIntent().getIntExtra(ConstantValue.CODE, 0);
        localPath = getIntent().getStringExtra(ConstantValue.KEY_LOCAL);
        onlinePath = getIntent().getStringExtra(ConstantValue.KEY_ONLINE);
        taskId = getIntent().getStringExtra(ConstantValue.KEY_TASK_ID);
//        strPath = getIntent().getStringExtra(ConstantValue.SGIN_PATH);
//        if (!TextUtils.isEmpty(localPath)) {
//            ivSign.setVisibility(View.VISIBLE);
//            signaturePad.setVisibility(View.GONE);
//            Glide.with(this).load(localPath).into(ivSign);
//        }
    }

    private void setListener() {
        btnClear.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                isSign = true;
                LogUtils.d("start sign");
            }

            @Override
            public void onSigned() {
                LogUtils.d("on signing");
            }

            @Override
            public void onClear() {
                isSign = false;
                LogUtils.d("clear sign");
                Toast.makeText(SignatureActivity.this, "清除当前签名，请重新签名", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear:
                isSign = false;
//                if (ivSign != null && ivSign.getVisibility() == View.VISIBLE) {//要删掉原来的图片，这个还没做
//
//                    ivSign.setVisibility(View.GONE);
//                    layoutRoot.removeView(ivSign);
//                    Glide.get(this).clearMemory();
//                    signaturePad.setVisibility(View.VISIBLE);
//                }
                signaturePad.clear();
                localPath = "";
                onlinePath = "";
                break;
            case R.id.btn_save:
//                if (ivSign != null && ivSign.getVisibility() == View.VISIBLE) {
//                    Intent intent = new Intent();
//                    intent.putExtra(ConstantValue.KEY_LOCAL, localPath);
//                    intent.putExtra(ConstantValue.KEY_ONLINE, onlinePath);
//                    SignatureActivity.this.setResult(RESULT_OK, intent);
//                    SignatureActivity.this.finish();
//                    return;
//                }
                if (!isSign) {
                    Toast.makeText(SignatureActivity.this, "请先签名", Toast.LENGTH_LONG).show();
                    return;
                }
                showPD();
                Observable.just(signaturePad.getSignatureBitmap())
                        .observeOn(Schedulers.newThread())
                        .map(new Func1<Bitmap, String>() {
                            @Override
                            public String call(Bitmap bitmap) {
                                Bitmap signBitmap = signaturePad.getSignatureBitmap();
                                Bitmap compressBitmap = null;
                                String path = "";
                                String str = "";
                                try {
                                    /**
                                     *  先将bitmap压缩一下
                                     *  绘制水印
                                     *  最后保存
                                     */
                                    String username = SharedPreferencesHelper.getInstance(SignatureActivity.this).getSharedPreference("userName", "").toString();
                                    String type = "";
                                    if (signType == ConstantValue.REQUEST_CODE_REGULATOR_1) {
                                        strPictureName = "regulator_1_" + username + System.currentTimeMillis();
                                        type = "reg_sign";
                                    } else if (signType == ConstantValue.REQUEST_CODE_REGULATOR_2) {
                                        strPictureName = "regulator_2_" + username + System.currentTimeMillis();
                                        type = "reg_sign";
                                    } else {
                                        strPictureName = "object_" + username + System.currentTimeMillis();
                                        type = "obj_sign";
                                    }
                                    compressBitmap = ImageUtil.compressScaleBitmap(SignatureActivity.this, signBitmap);
                                    str = PictureSelectorUtil.OnePictureListCreateWatermark(SignatureActivity.this,
                                            compressBitmap, ConstantValue.WATER_MARK + TextUtil.date(),
                                            strPictureName);
//                                    path = addJpgSignatureToGallery(ImageUtil.compressScaleBitmap(SignatureActivity.this, signBitmap));
//                                    str = PictureSelectorUtil.OnePictureListCreateWatermark(SignatureActivity.this, path, ConstantValue.WATER_MARK + TextUtil.date());

                                    entity = new FileEntity();
                                    entity.setTaskId(taskId);
                                    entity.setLocalPath(str);
                                    entity.setType(type);
                                    fileEntityDao.save(entity);

                                    File file = new File(str);
                                    if (null != file) {
                                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
                                        MultipartBody.Part part = MultipartBody.Part.createFormData(ConstantValue.FILE_DATA, file.getName() + ".jpg", requestBody);
                                        List<MultipartBody.Part> partList = new ArrayList<>();
                                        partList.add(part);
                                        uploadImageApi.setParts(partList);
//                                    uploadImageApi.setPart(part);

                                    } else {
                                        Toast.makeText(SignatureActivity.this, "图片为空", Toast.LENGTH_SHORT);
                                    }
                                } catch (Exception e) {

                                } finally {
                                    if (null != signBitmap)
                                        signBitmap.recycle();
                                    signBitmap = null;
                                    return str;

                                }

                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                LogUtils.d("保存成功");
                            }

                            @Override
                            public void onError(Throwable e) {
                                dissmisPD();
                                Toast.makeText(SignatureActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onNext(String s) {
                                localPath = s;
                                manager.doHttpDeal(uploadImageApi);
                            }
                        });


                break;
            default:
                break;
        }
    }

    public String addJpgSignatureToGallery(Bitmap signature) {
        File photo = null;
        try {
            String username = SharedPreferencesHelper.getInstance(this).getSharedPreference("userName", "").toString();
            if (signType == ConstantValue.REQUEST_CODE_REGULATOR_1) {
                strPictureName = "regulator_1_" + username + System.currentTimeMillis();
            } else if (signType == ConstantValue.REQUEST_CODE_REGULATOR_2) {
                strPictureName = "regulator_2_" + username + System.currentTimeMillis();
            } else {
                strPictureName = "object_" + username + System.currentTimeMillis();
            }
            photo = new File(getAlbumStorageDir(ConstantValue.PATH_SIGN_PICTURE), strPictureName);
            LogUtils.d("photo path = " + photo.getAbsolutePath());
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo.getAbsolutePath();
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
//        File file = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), albumName);
        File file = new File(Environment.getExternalStorageDirectory(), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = null;
        Canvas canvas = null;
        OutputStream stream = null;
        try {
            newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
            canvas = new Canvas(newBitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            stream = new FileOutputStream(photo);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        } catch (Exception e) {
        } finally {
            if (null != newBitmap)
                newBitmap.recycle();
            newBitmap = null;
            canvas = null;
            stream.close();
        }
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        SignatureActivity.this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onNext(String resulte, String method) {

        JSONObject o = JSON.parseObject(resulte);
        if (!ConstantValue.isSuccess(o)) {
            return;
        }
        if (null != entity)
            fileEntityDao.delete(entity);
        onlinePath = o.getJSONArray("data").getString(0);
        dissmisPD();
        back();
        SignatureActivity.this.finish();
    }

    @Override
    public void onError(ApiException e, String method) {
        dissmisPD();
        back();
    }

    private void back() {
        Intent intent = new Intent();
        intent.putExtra(ConstantValue.KEY_LOCAL, localPath);
        intent.putExtra(ConstantValue.KEY_ONLINE, onlinePath);
        SignatureActivity.this.setResult(RESULT_OK, intent);
        SignatureActivity.this.finish();
    }


}
