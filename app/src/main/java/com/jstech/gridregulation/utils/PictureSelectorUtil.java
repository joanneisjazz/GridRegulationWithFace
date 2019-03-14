package com.jstech.gridregulation.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.FileEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PictureSelectorUtil {

    public static void initPictureSelector(Activity fragment, int max,
                                           int min, int span,
                                           List<LocalMedia> selectList) {
        PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(max)// 最大图片选择数量
                .minSelectNum(min)// 最小选择数量
                .imageSpanCount(span)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .isCamera(true)// 是否显示拍照按钮
                .videoMaxSecond(0)
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .rotateEnabled(true)
                .selectionMedia(selectList)
                .previewVideo(false)
//                .compressSavePath(Environment.getExternalStorageDirectory() + ConstantValue.PATH_SIGN_PICTURE_COMPRESS)
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }


    public static List<MultipartBody.Part> imagesToMultipartBody(List<LocalMedia> selectList) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (LocalMedia localMedia : selectList) {
            String path = localMedia.getCompressPath();
            File file = new File(path);
            if (null != file) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/JPEG"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData(ConstantValue.FILE_DATA, file.getName(), requestBody);
                partList.add(part);
            }
        }
        return partList;
    }

    public static List<MultipartBody.Part> imagesToMultipartBody2(List<FileEntity> selectList) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (FileEntity localMedia : selectList) {
            String path = localMedia.getLocalPath();
            File file = new File(path);
            if (null != file) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/JPEG"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData(ConstantValue.FILE_DATA, file.getName(), requestBody);
                partList.add(part);
            }
        }
        return partList;
    }


    public static List<MultipartBody.Part> imagesToMultipartBody4(List<FileEntity> selectList) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (FileEntity localMedia : selectList) {
            String path = localMedia.getLocalPath();
            File file = new File(path);
            if (null != file) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData(ConstantValue.FILE_DATA, file.getName() + ".jpg", requestBody);
                partList.add(part);
            }
        }
        return partList;
    }

    public static MultipartBody.Part imagesToMultipartBody3(String path) {
        File file = new File(path);
        MultipartBody.Part part = null;
        if (null != file) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            part = MultipartBody.Part.createFormData(ConstantValue.FILE_DATA, file.getName() + ".jpg", requestBody);
        }

        return part;
    }


    /**
     * 在单张图片的右下方加水印
     *
     * @param markText 水印文字
     * @return
     */
    public static Bitmap OnePictureCreateWatermark(Context context, String path, String markText) {

        Bitmap bitmap = LocakImgToBitmap(path);//BitmapFactory.decodeResource(context.getResources(), resources);
        // 获取图片的宽高
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        // 创建一个和图片一样大的背景图
        Bitmap bmp = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmp);
        // 画背景图
        canvas.drawBitmap(bitmap, 0, 0, null);
        //-------------开始绘制文字-------------------------------

        if (!TextUtils.isEmpty(markText)) {
            int screenWidth = getScreenWidth(context);
            float textSize = dp2px(context, 16) * bitmapWidth / screenWidth;
            // 创建画笔
            TextPaint mPaint = new TextPaint();
            // 文字矩阵区域
            Rect textBounds = new Rect();

            // 水印的字体大小
            mPaint.setTextSize(textSize);
            // 文字阴影
            mPaint.setShadowLayer(0, 0f, 0f, Color.YELLOW);
            // 抗锯齿
            mPaint.setAntiAlias(true);
            // 水印的区域
            mPaint.getTextBounds(markText, 0, markText.length(), textBounds);
            // 水印的颜色
            mPaint.setColor(Color.RED);
            StaticLayout layout = new StaticLayout(markText, 0, markText.length(), mPaint, (int) (bitmapWidth - textSize),
                    Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.5F, true);
            // 文字开始的坐标
            float textX = bitmapWidth - textBounds.width() / 2;
            float textY = bitmapHeight - textBounds.height() - dp2px(context, 50);//图片的中间
            // 画文字
            canvas.translate(textX, textY);
            layout.draw(canvas);

        }

        //保存所有元素
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return bmp;
    }

    private static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * dip转pix
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static Bitmap LocakImgToBitmap(String path) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(path);
            bitmap = BitmapFactory.decodeStream(fis);
            return bitmap;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static void PictureListCreateWatermark(Context context, List<LocalMedia> seletedList, String text) {
        for (LocalMedia media : seletedList) {
            Bitmap bitmap = null;
            try {
                bitmap = ImageUtil.drawTextToLeftBottom(context, LocakImgToBitmap(media.getCompressPath()), text, 8, R.color.check_item_selected_bg, 5, 5);
//            Bitmap bitmap = OnePictureCreateWatermark(context, media.getCompressPath(), text);
                String path = SaveBitmapToLocal(context, bitmap, media.getCompressPath());
                media.setCompressPath(path);
            } catch (Exception e) {
            } finally {
                if (null != bitmap) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }
        }
    }

    public static String OnePictureListCreateWatermark(Context context, Bitmap bit, String text, String path) {
        String newpath = "";
        Bitmap bitmap = null;
        bitmap = ImageUtil.drawTextToLeftBottom(context, bit, text, 8, R.color.check_item_selected_bg, 5, 5);
        newpath = SaveBitmapToLocal(context, bitmap, path);
        if (null != bitmap)
            bitmap.recycle();
        bitmap = null;
        return newpath;


    }

    public static String SaveBitmapToLocal(Context context, Bitmap btImage, String path) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
        {      // 获取SDCard指定目录下
            String sdCardDir = Environment.getExternalStorageDirectory() + ConstantValue.PATH_SIGN_PICTURE_COMPRESS_WATERMARK;
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {
                dirFile.mkdirs();
                Log.d("hesm", sdCardDir + "创建成功");//如果不存在，那就建立这个文件夹
            }
            //文件夹有啦，就可以保存图片啦
            String[] strings = path.split("/");
            LogUtils.d("path ===" + strings[strings.length - 1]);
            File file = new File(sdCardDir, strings[strings.length - 1]);// 在SDcard的目录下创建图片文,以当前时间为其命名
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                btImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
                Log.d("hesm", "_________保存到___" + file.getPath());
            } catch (FileNotFoundException e) {
                Log.d("hesm", "发生错误---" + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    out.flush();
                    out.close();
                } catch (IOException io) {

                }
                if (null != btImage)
                    btImage.recycle();
                btImage = null;
            }
            return file.getPath();
        }
        return null;
    }

    public static String SaveBitmapToLocal(Bitmap btImage, String sdCardDir, String path) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
        {      // 获取SDCard指定目录下
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {
                dirFile.mkdirs();
                Log.d("hesm", sdCardDir + "创建成功");//如果不存在，那就建立这个文件夹
            }
            //文件夹有啦，就可以保存图片啦

            File file = new File(sdCardDir, path);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                btImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
                Log.d("hesm", "_________保存到___" + file.getPath());
            } catch (FileNotFoundException e) {
                Log.d("hesm", "发生错误---" + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    out.flush();
                    out.close();
                } catch (IOException io) {

                }
                if (null != btImage)
                    btImage.recycle();
                btImage = null;
            }
            return file.getPath();
        }
        return null;
    }

}