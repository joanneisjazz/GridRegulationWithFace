package com.jstech.gridregulation.utils;

import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.api.AddTaskApi;
import com.jstech.gridregulation.api.SaveItemResultApi;
import com.jstech.gridregulation.api.SaveResultApi;
import com.jstech.gridregulation.api.UploadFileApi;
import com.jstech.gridregulation.api.UploadImageApi;
import com.jstech.gridregulation.bean.SaveResultBean;
import com.rxretrofitlibrary.greendao.FileEntityDao;
import com.rxretrofitlibrary.greendao.RegulateResultBeanDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.FileEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;

public class TaskUtil {

    public static AddTaskApi addTaskApi = new AddTaskApi();
    public static SaveItemResultApi saveItemResultApi = new SaveItemResultApi();
    public static UploadFileApi uploadFileApi = new UploadFileApi();
    public static UploadImageApi uploadImageApi = new UploadImageApi();
    public static SaveResultApi saveResultApi = new SaveResultApi();

    public static void uploadData(TaskBean taskBean, HttpManager manager) {
        uploadTask(taskBean, manager);
    }

    public static void uploadTask(TaskBean bean, HttpManager manager) {
        if ("0".equals(bean.getState1())) {
            //如果需要上传检查任务，调用上传检查任务的接口
            addTaskApi.setParams(bean);
            manager.doHttpDeal(addTaskApi);
            return;
        }
        //如果不需要上传检查任务，判断是否需要上传检查项
        uploadItems(bean, manager);
    }

    /**
     * 上传检查项的结果
     *
     * @param
     */
    public static void uploadItems(TaskBean bean, HttpManager manager) {
        //先判断检查项的结果是否已经上传过了，没有上传才需要调用上传检查项的接口
        if ("0".equals(bean.getState3()) || "0".equals(bean.getState2())) {
            RegulateResultBeanDao regulateResultBeanDao = MyApplication.getInstance().getSession().getRegulateResultBeanDao();
            List<RegulateResultBean> list = regulateResultBeanDao.queryBuilder().where(
                    RegulateResultBeanDao.Properties.Inspid.eq(bean.getId())
            ).list();
            saveItemResultApi.setParams(list);
            manager.doHttpDeal(saveItemResultApi);
            return;
        }
        uploadPDF(bean, manager);
    }


    /**
     * 上传pdf文档
     *
     * @param bean
     */
    public static void uploadPDF(TaskBean bean, HttpManager manager) {
        if ("0".equals(bean.getState4())) {
            FileEntityDao fileEntityDao = MyApplication.getInstance().getSession().getFileEntityDao();
            FileEntity pdf = fileEntityDao.queryBuilder().where(
                    FileEntityDao.Properties.TaskId.eq(bean.getId()),
                    FileEntityDao.Properties.Type.eq("pdf")
            ).unique();
            if (null != pdf) {
                uploadFileApi.setPart(FileUtil.uploadFile(pdf.getLocalPath()));
                manager.doHttpDeal(uploadFileApi);
            }
            return;
        }
        uploadSuperSign(bean, manager);
    }

    /**
     * 上传检查人签字
     */
    public static void uploadSuperSign(TaskBean taskBean, HttpManager manager) {
        if (TextUtil.isEmpty(taskBean.getRegulator1SignPath()) || TextUtil.isEmpty(taskBean.getRegulator2SignPath())) {
            List<MultipartBody.Part> list = new ArrayList<>();
            if (!TextUtil.isEmpty(taskBean.getRegulator1SignPathLocal())) {
                list.add(PictureSelectorUtil.imagesToMultipartBody3(taskBean.getRegulator1SignPathLocal()));
            }
            if (!TextUtil.isEmpty(taskBean.getRegulator2SignPathLocal())) {
                list.add(PictureSelectorUtil.imagesToMultipartBody3(taskBean.getRegulator2SignPathLocal()));

            }
            uploadImageApi.setParts(list);
            uploadImageApi.setType("1");
            manager.doHttpDeal(uploadImageApi);
            return;
        }
        uploadEntSign(taskBean, manager);
    }

    /**
     * 上传检查对象签字
     */
    public static void uploadEntSign(TaskBean taskBean, HttpManager manager) {
        if (TextUtil.isEmpty(taskBean.getEntsign())) {
            List<MultipartBody.Part> list = new ArrayList<>();
            if (!TextUtil.isEmpty(taskBean.getObjectSignPath())) {
                list.add(PictureSelectorUtil.imagesToMultipartBody3(taskBean.getRegulator1SignPathLocal()));
            }
            uploadImageApi.setParts(list);
            uploadImageApi.setType("2");
            manager.doHttpDeal(uploadImageApi);
            return;
        }
        uploadImages(taskBean, manager);
    }

    /**
     * 上传检查图片
     *
     * @param
     */
    public static void uploadImages(TaskBean taskBean, HttpManager manager) {
        if ("0".equals(taskBean.getState5())) {
            FileEntityDao fileEntityDao = MyApplication.getInstance().getSession().getFileEntityDao();
            List<FileEntity> files = fileEntityDao.queryBuilder().where(
                    FileEntityDao.Properties.TaskId.eq(taskBean.getId()),
                    FileEntityDao.Properties.Type.eq("img")
            ).list();
            if (null != files && files.size() > 0) {
                uploadImageApi.setType("3");
                uploadImageApi.setParts(PictureSelectorUtil.imagesToMultipartBody2(files));
                manager.doHttpDeal(uploadImageApi);
                return;
            }
        }
        uploadAll(taskBean, manager);
    }

    /**
     * 上传所有的信息
     *
     * @param
     * @param taskBean
     */
    public static void uploadAll(TaskBean taskBean, HttpManager manager) {
        SaveResultBean saveResultBean = new SaveResultBean();
        saveResultBean.setImgs(taskBean.getImage());
        saveResultBean.setInsp(taskBean);
        saveResultApi.setBean(saveResultBean);
        manager.doHttpDeal(saveResultApi);
    }
}
