package com.jstech.gridregulation.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point
import android.hardware.Camera
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.Toast
import com.arcsoft.face.*
import com.jstech.gridregulation.ConstantValue
import com.jstech.gridregulation.R
import com.jstech.gridregulation.base.BaseActivity
import com.jstech.gridregulation.camera.*
import com.jstech.gridregulation.utils.LogUtils
import com.jstech.gridregulation.utils.PermissionUtil
import com.jstech.gridregulation.utils.PictureSelectorUtil
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_take_face_pic.*
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

class UploadPhotoActivity : BaseActivity(), HttpOnNextListener, ViewTreeObserver.OnGlobalLayoutListener {


    private lateinit var httpManager: HttpManager;
    private var cameraHelper: CameraHelper? = null
    private lateinit var drawHelper: DrawHelper
    private lateinit var previewSize: Camera.Size

    private val cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT
    private lateinit var faceEngine: FaceEngine
    private var afCode = -1
    private val userExtId: String = SharedPreferencesHelper.instance.getUserExtId(this@UploadPhotoActivity)

    override fun getLayoutId(): Int {
        return R.layout.activity_take_face_pic
    }

    override fun initView() {

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            var params = window.attributes
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            window.attributes = params
        }

        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT ->
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            Configuration.ORIENTATION_LANDSCAPE ->
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
        }
        texture_preview.viewTreeObserver.addOnGlobalLayoutListener { this@UploadPhotoActivity.onGlobalLayout() }


        button.visibility = View.VISIBLE
        button.setOnClickListener {

        }
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onNext(resulte: String?, method: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(e: ApiException?, method: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGlobalLayout() {
        texture_preview.getViewTreeObserver().removeOnGlobalLayoutListener(this)
        if (!PermissionUtil.checkPermissions(this@UploadPhotoActivity.applicationContext, ConstantValue.NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, ConstantValue.NEEDED_PERMISSIONS, ConstantValue.ACTION_REQUEST_PERMISSIONS)
        } else {
            initEngine()
            initCamera()
        }
    }

    private fun initEngine() {
        faceEngine = FaceEngine()
        afCode = faceEngine.init(this.applicationContext, FaceEngine.ASF_DETECT_MODE_VIDEO, ConfigUtil.getFtOrient(this),
                16, 20, FaceEngine.ASF_FACE_DETECT or FaceEngine.ASF_AGE or FaceEngine.ASF_FACE3DANGLE or FaceEngine.ASF_GENDER or FaceEngine.ASF_LIVENESS)
        val versionInfo = VersionInfo()
        faceEngine.getVersion(versionInfo)
        if (afCode != ErrorInfo.MOK) {
            Toast.makeText(this, "初始化失败", Toast.LENGTH_SHORT).show()
        }
    }

    private fun unInitEngine() {
        if (afCode == 0) {
            afCode = faceEngine.unInit()
            LogUtils.d("unInitEngine: $afCode")
        }
    }

    override fun onDestroy() {
        cameraHelper?.release()
        cameraHelper = null
        unInitEngine()
        super.onDestroy()
    }

    private fun initCamera() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val cameraListener = object : CameraListener {
            override fun onCameraOpened(camera: Camera, cameraId: Int, displayOrientation: Int, isMirror: Boolean) {
                LogUtils.d("onCameraOpened: $cameraId  $displayOrientation $isMirror")
                previewSize = camera.parameters.previewSize
                drawHelper = DrawHelper(previewSize.width, previewSize.height, texture_preview.getWidth(), texture_preview.getHeight(), displayOrientation, cameraId, isMirror)
            }


            override fun onPreview(nv21: ByteArray, camera: Camera) {

                if (face_rect_view != null) {
                    face_rect_view.clearFaceInfo()
                }
                val faceInfoList = ArrayList<FaceInfo>()
                var code = faceEngine.detectFaces(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList)
                if (code == ErrorInfo.MOK && faceInfoList.size > 0) {
                    code = faceEngine.process(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList, ConstantValue.processMask)
                    if (code != ErrorInfo.MOK) {
                        return
                    }
                } else {
                    return
                }

                val ageInfoList = ArrayList<AgeInfo>()
                val genderInfoList = ArrayList<GenderInfo>()
                val face3DAngleList = ArrayList<Face3DAngle>()
                val faceLivenessInfoList = ArrayList<LivenessInfo>()
                val ageCode = faceEngine.getAge(ageInfoList)
                val genderCode = faceEngine.getGender(genderInfoList)
                val face3DAngleCode = faceEngine.getFace3DAngle(face3DAngleList)
                val livenessCode = faceEngine.getLiveness(faceLivenessInfoList)

                //有其中一个的错误码不为0，return
                if (ageCode or genderCode or face3DAngleCode or livenessCode != ErrorInfo.MOK) {
                    return
                }
                if (face_rect_view != null && drawHelper != null) {
                    val drawInfoList = ArrayList<DrawInfo>()
                    for (i in faceInfoList.indices) {
                        drawInfoList.add(DrawInfo(faceInfoList[i].rect, genderInfoList[i].gender, ageInfoList[i].age, faceLivenessInfoList[i].liveness, null))
                    }
                    drawHelper.draw(face_rect_view, drawInfoList)
                    button.visibility = View.VISIBLE
                }
            }

            override fun onCameraClosed() {
                LogUtils.d("onCameraClosed: ")
            }

            override fun onCameraError(e: Exception) {
                LogUtils.d("onCameraError: " + e.message)
            }

            override fun onCameraConfigurationChanged(cameraID: Int, displayOrientation: Int) {
                if (drawHelper != null) {
                    drawHelper.setCameraDisplayOrientation(displayOrientation)
                }
                LogUtils.d("onCameraConfigurationChanged: $cameraID  $displayOrientation")
            }
        }
        cameraHelper = CameraHelper.Builder()
                .previewViewSize(Point(texture_preview.getMeasuredWidth(), texture_preview.getMeasuredHeight()))
                .rotation(windowManager.defaultDisplay.rotation)
                .specificCameraId(cameraID ?: Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(texture_preview)
                .cameraListener(cameraListener)
                .addPictureCallback(callback)
                .build()
        cameraHelper?.init()
    }

    internal var resource: Bitmap? = null
    internal var bitmap: Bitmap? = null
    internal var callback: Camera.PictureCallback = Camera.PictureCallback { bytes, camera ->
        camera.startPreview()
        Observable.create(Observable.OnSubscribe<String> { subscriber ->
            try {
                resource = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                if (resource == null) {
                    Toast.makeText(this@UploadPhotoActivity, "拍照失败", Toast.LENGTH_SHORT).show()
                    return@OnSubscribe
                }
                val matrix = Matrix()
                matrix.postRotate(270f)
                bitmap = Bitmap.createBitmap(resource!!, 0, 0, resource!!.width, resource!!.height, matrix, true)
                val path = PictureSelectorUtil.SaveBitmapToLocal(bitmap, ConstantValue.SD_SRC + "face", "$userExtId.jpg")
                Log.d("hesm", path)

                subscriber.onNext(path)

            } catch (e: Exception) {
                Log.d("hesm", "失败：" + e.message)

            } finally {
                if (null != resource)
                    resource!!.recycle()
                if (null != bitmap)
                    bitmap!!.recycle()
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onNext(s: String) {
                        Log.d("hesm", "next：$s")

                        if (null != resource)
                            resource!!.recycle()
                        if (null != bitmap)
                            bitmap!!.recycle()

                        val intent = Intent(this@UploadPhotoActivity, FaceValidateActivity::class.java)
                        intent.putExtra("url", s)
                        startActivity(intent)
                        finish()
                    }

                    override fun onError(e: Throwable) {
                        if (null != resource)
                            resource!!.recycle()
                        if (null != bitmap)
                            bitmap!!.recycle()
                    }

                    override fun onCompleted() {
                        if (null != resource)
                            resource!!.recycle()
                        if (null != bitmap)
                            bitmap!!.recycle()
                    }
                })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ConstantValue.ACTION_REQUEST_PERMISSIONS) {
            var isAllGranted = true
            for (grantResult in grantResults) {
                isAllGranted = isAllGranted and (grantResult == PackageManager.PERMISSION_GRANTED)
            }
            if (isAllGranted) {
                initEngine()
                initCamera()
                if (cameraHelper != null) {
                    cameraHelper?.start()
                }
            } else {
                Toast.makeText(this@UploadPhotoActivity, R.string.permission_denied, Toast.LENGTH_SHORT).show()
            }
        }
    }

}