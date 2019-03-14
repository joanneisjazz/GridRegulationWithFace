package com.jstech.gridregulation.utils;

import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.api.AddTaskApi;
import com.jstech.gridregulation.api.DeteleTaskApi;
import com.jstech.gridregulation.api.SaveItemResultApi;
import com.jstech.gridregulation.api.SaveResultApi;
import com.jstech.gridregulation.api.UploadFileApi;
import com.jstech.gridregulation.api.UploadImageApi;
import com.jstech.gridregulation.bean.SaveResultBean;
import com.rxretrofitlibrary.greendao.DaoSession;
import com.rxretrofitlibrary.greendao.FileEntityDao;
import com.rxretrofitlibrary.greendao.RegulateObjectBeanDao;
import com.rxretrofitlibrary.greendao.RegulateResultBeanDao;
import com.rxretrofitlibrary.greendao.TaskBeanDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.FileEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;

import java.util.List;

public class TaskUploadUtil {

    HttpManager manager;
    DeteleTaskApi deteleTaskApi;
    AddTaskApi addTaskApi;
    SaveItemResultApi saveItemResultApi;
    UploadImageApi uploadImageApi;
    UploadFileApi uploadFileApi;
    SaveResultApi saveResultApi;

    TaskBeanDao taskBeanDao;
    FileEntityDao fileEntityDao;
    RegulateResultBeanDao regulateResultBeanDao;
    RegulateObjectBeanDao regulateObjectBeanDao;

    List<TaskBean> taskBeans;
    List<FileEntity> fileEntities;
    TaskBean nowTask;

    public TaskUploadUtil(HttpManager manager) {
        this.manager = manager;
        DaoSession session = MyApplication.getInstance().getSession();
        taskBeanDao = session.getTaskBeanDao();
        fileEntityDao = session.getFileEntityDao();
        regulateResultBeanDao = session.getRegulateResultBeanDao();
        regulateObjectBeanDao = session.getRegulateObjectBeanDao();
    }


    public void uploadLocalData() {
        //取出本地所有未上传的检查任务
        taskBeans = taskBeanDao.queryBuilder().where(
                TaskBeanDao.Properties.State5.eq("1"),
                TaskBeanDao.Properties.State6.notEq("1")
        ).list();
        if (taskBeans == null || taskBeans.isEmpty()) {
            return;
        }
        //取出第一条上传
        nowTask = taskBeans.get(0);
        uploadTask(nowTask);
    }

    public void uploadTask(TaskBean bean) {
        if (null == bean) {
            return;
        }

        if ("1".equals(bean.getDeleteFlag())) {
            deleteTask(bean);
            return;
        }
        addTask(bean, true);
    }

    public void addTask(TaskBean bean, boolean isWithPDF) {
        if ("0".equals(bean.getState1())) {
            //如果需要上传检查任务，调用上传检查任务的接口
            addTaskApi.setParams(nowTask);
            manager.doHttpDeal(addTaskApi);
            return;
        }
        uploadItemPics(bean, isWithPDF);
    }

    public void uploadItemPics(TaskBean taskBean, boolean isWithPDF) {
        if ("1".equals(taskBean.getState3())) {
            uploadPDF(taskBean);
            return;
        }

        fileEntities = fileEntityDao.queryBuilder().where(
                FileEntityDao.Properties.TaskId.eq(taskBean.getId()),
                FileEntityDao.Properties.Type.eq("item_img")
        ).list();

        if (null != fileEntities && fileEntities.size() > 0) {
            uploadImageApi.setType("4");
            uploadImageApi.setParts(PictureSelectorUtil.imagesToMultipartBody2(fileEntities));
            manager.doHttpDeal(uploadImageApi);
            return;
        }
        uploadItems(taskBean, isWithPDF);
    }

    /**
     * 上传检查项的结果
     *
     * @param
     */
    public void uploadItems(TaskBean bean, boolean isWithPDF) {
        //先判断检查项的结果是否已经上传过了，没有上传才需要调用上传检查项的接口
        if ("0".equals(bean.getState3()) || "0".equals(bean.getState2())) {
            List<RegulateResultBean> list = regulateResultBeanDao.queryBuilder().where(
                    RegulateResultBeanDao.Properties.Inspid.eq(bean.getId())
            ).list();
            saveItemResultApi.setParams(list);
            manager.doHttpDeal(saveItemResultApi);
            return;
        }
        if (isWithPDF) {
            uploadPDF(bean);
        }
    }


    /**
     * 上传pdf文档
     *
     * @param bean
     */
    public void uploadPDF(TaskBean bean) {
        if ("0".equals(bean.getState4())) {
            FileEntity pdf = fileEntityDao.queryBuilder().where(
                    FileEntityDao.Properties.TaskId.eq(bean.getId()),
                    FileEntityDao.Properties.Type.eq("pdf")
            ).unique();
            if (null != pdf) {
                uploadFileApi.setPart(FileUtil.uploadFile(pdf.getLocalPath()));
                manager.doHttpDeal(uploadFileApi);
            } else {
                uploadSuperSign(bean);
            }
            return;
        }
        uploadSuperSign(bean);

    }

    /**
     * 上传检查人签字
     */
    public void uploadSuperSign(TaskBean taskBean) {
        List<FileEntity> regulateSign = fileEntityDao.queryBuilder().where(
                FileEntityDao.Properties.TaskId.eq(taskBean.getId()),
                FileEntityDao.Properties.Type.eq("reg_sign")
        ).list();
        if (null != regulateSign && regulateSign.size() > 0) {
            uploadImageApi.setParts(PictureSelectorUtil.imagesToMultipartBody2(regulateSign));
            uploadImageApi.setType("1");
            manager.doHttpDeal(uploadImageApi);
            return;
        }
        uploadEntSign(taskBean);
    }

    /**
     * 上传检查对象签字
     */
    public void uploadEntSign(TaskBean taskBean) {

        List<FileEntity> regulateSign = fileEntityDao.queryBuilder().where(
                FileEntityDao.Properties.TaskId.eq(taskBean.getId()),
                FileEntityDao.Properties.Type.eq("obj_sign")
        ).list();
        if (null != regulateSign && regulateSign.size() > 0) {
            uploadImageApi.setParts(PictureSelectorUtil.imagesToMultipartBody2(regulateSign));
            uploadImageApi.setType("2");
            manager.doHttpDeal(uploadImageApi);
            return;
        }
        uploadAll(taskBean);
    }

    /**
     * 上传所有的信息
     *
     * @param
     * @param taskBean
     */
    public void uploadAll(TaskBean taskBean) {
        SaveResultBean saveResultBean = new SaveResultBean();
//        saveResultBean.setImgs(taskBean.getImage());
        saveResultBean.setInsp(taskBean);
        saveResultApi.setBean(saveResultBean);
        manager.doHttpDeal(saveResultApi);
    }


    /**
     * 删除检查任务
     */
    public void deleteTask(TaskBean taskBean) {
        SaveResultBean saveResultBean = new SaveResultBean();
        saveResultBean.setInsp(taskBean);
        deteleTaskApi.setBean(saveResultBean);
        taskBeanDao.delete(taskBean);
        regulateResultBeanDao.deleteInTx(regulateResultBeanDao.queryBuilder().where(
                RegulateResultBeanDao.Properties.Inspid.eq(taskBean.getId())
        ).list());
        manager.doHttpDeal(deteleTaskApi);
    }


    public void setDeteleTaskApi(DeteleTaskApi deteleTaskApi) {
        this.deteleTaskApi = deteleTaskApi;
    }

    public void setAddTaskApi(AddTaskApi addTaskApi) {
        this.addTaskApi = addTaskApi;
    }

    public void setSaveItemResultApi(SaveItemResultApi saveItemResultApi) {
        this.saveItemResultApi = saveItemResultApi;
    }

    public void setUploadImageApi(UploadImageApi uploadImageApi) {
        this.uploadImageApi = uploadImageApi;
    }

    public void setUploadFileApi(UploadFileApi uploadFileApi) {
        this.uploadFileApi = uploadFileApi;
    }

    public void setSaveResultApi(SaveResultApi saveResultApi) {
        this.saveResultApi = saveResultApi;
    }


}
