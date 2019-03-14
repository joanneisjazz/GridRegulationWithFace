package com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * 保存在本地的任务表
 */
@Entity
public class TaskBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String time;
    private String userId;
    private String status;
    //    private String entId;
    private String tableId;

    /**
     * 本地保存的图片路径
     */
    private String regulator1SignPathLocal;
    private String regulator2SignPathLocal;
    private String objectSignPath;

    /**
     * 服务器返回的签名路径
     */
    private String regulator1SignPath;
    private String regulator2SignPath;
    private String entsign;//服务器返回的签名路径
    private String supersign;//服务器返回的签名路径


    /**
     * entid : 1
     * entname : 测试数据
     * enttype : 0
     * entregion : 140110001
     * entcredit : B
     * oisuper : 241671aee7cc4ead88b2532b7686183c
     * insptable : 51a83f55ef89426699ca7888cf931f10,5c54ddc9f724434590b954a4cc4dd0d2
     * createBy : 241671aee7cc4ead88b2532b7686183c
     * updateBy : 241671aee7cc4ead88b2532b7686183c
     */

    private String entid;
    private String entname;
    private String enttype;
    private String entregion;
    private String entcredit;
    private String oisuper;
    private String insptable;
    private String createBy;
    private String updateBy;
    private String inspstart;

    private String inspstatus;
    private String inspresult;
    private String inspdoc;
    private String insploc;
    private String inspopinion;

    /**
     * 5个状态 0代表未上传，1代表上传
     * state1:是否已经将新生成的任务提交至服务器
     * state2：检查项是否提交至服务器
     * state2_1：检查项的图片是否提交至服务器
     * state3：检查项结果是否提交至服务器
     * state4：pdf文件是否提交至服务器
     * state5：任务是不是做完了
     * state6：全部的信息是否都已经提交至服务器
     */

    private String state1;
    private String state2;
    private String state2_1;
    private String state3;
    private String state4;
    private String state5;
    private String state6;

    /**
     * 是否删除本次检查任务，0代表未删除，1代表删除
     */
    private String deleteFlag = "0";
    private String deleteUploadFlag;
    private String createDate;
    private String resultstr;


    @Transient
    private String image;
    @Transient
    private UserBean pepole;

    private String createDateLocal;
    private String regulatorName;


    @Generated(hash = 958851268)
    public TaskBean(String id, String time, String userId, String status,
                    String tableId, String regulator1SignPathLocal,
                    String regulator2SignPathLocal, String objectSignPath,
                    String regulator1SignPath, String regulator2SignPath, String entsign,
                    String supersign, String entid, String entname, String enttype,
                    String entregion, String entcredit, String oisuper, String insptable,
                    String createBy, String updateBy, String inspstart, String inspstatus,
                    String inspresult, String inspdoc, String insploc, String inspopinion,
                    String state1, String state2, String state2_1, String state3,
                    String state4, String state5, String state6, String deleteFlag,
                    String deleteUploadFlag, String createDate, String resultstr,
                    String createDateLocal, String regulatorName) {
        this.id = id;
        this.time = time;
        this.userId = userId;
        this.status = status;
        this.tableId = tableId;
        this.regulator1SignPathLocal = regulator1SignPathLocal;
        this.regulator2SignPathLocal = regulator2SignPathLocal;
        this.objectSignPath = objectSignPath;
        this.regulator1SignPath = regulator1SignPath;
        this.regulator2SignPath = regulator2SignPath;
        this.entsign = entsign;
        this.supersign = supersign;
        this.entid = entid;
        this.entname = entname;
        this.enttype = enttype;
        this.entregion = entregion;
        this.entcredit = entcredit;
        this.oisuper = oisuper;
        this.insptable = insptable;
        this.createBy = createBy;
        this.updateBy = updateBy;
        this.inspstart = inspstart;
        this.inspstatus = inspstatus;
        this.inspresult = inspresult;
        this.inspdoc = inspdoc;
        this.insploc = insploc;
        this.inspopinion = inspopinion;
        this.state1 = state1;
        this.state2 = state2;
        this.state2_1 = state2_1;
        this.state3 = state3;
        this.state4 = state4;
        this.state5 = state5;
        this.state6 = state6;
        this.deleteFlag = deleteFlag;
        this.deleteUploadFlag = deleteUploadFlag;
        this.createDate = createDate;
        this.resultstr = resultstr;
        this.createDateLocal = createDateLocal;
        this.regulatorName = regulatorName;
    }

    @Generated(hash = 1443476586)
    public TaskBean() {
    }


    public String getCreateDateLocal() {
        return createDateLocal;
    }

    public void setCreateDateLocal(String createDateLocal) {
        this.createDateLocal = createDateLocal;
    }

    public UserBean getPepole() {
        return pepole;
    }

    public void setPepole(UserBean pepole) {
        this.pepole = pepole;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getDeleteUploadFlag() {
        return deleteUploadFlag;
    }

    public void setDeleteUploadFlag(String deleteUploadFlag) {
        this.deleteUploadFlag = deleteUploadFlag;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegulator1SignPath() {
        return this.regulator1SignPath;
    }

    public void setRegulator1SignPath(String regulator1SignPath) {
        this.regulator1SignPath = regulator1SignPath;
    }

    public String getRegulator2SignPath() {
        return this.regulator2SignPath;
    }

    public void setRegulator2SignPath(String regulator2SignPath) {
        this.regulator2SignPath = regulator2SignPath;
    }

    public String getObjectSignPath() {
        return this.objectSignPath;
    }

    public void setObjectSignPath(String objectSignPath) {
        this.objectSignPath = objectSignPath;
    }

    public String getRegulator1SignPathLocal() {
        return this.regulator1SignPathLocal;
    }

    public void setRegulator1SignPathLocal(String regulator1SignPathLocal) {
        this.regulator1SignPathLocal = regulator1SignPathLocal;
    }

    public String getRegulator2SignPathLocal() {
        return this.regulator2SignPathLocal;
    }

    public void setRegulator2SignPathLocal(String regulator2SignPathLocal) {
        this.regulator2SignPathLocal = regulator2SignPathLocal;
    }

    public String getEntsign() {
        return this.entsign;
    }

    public void setEntsign(String entsign) {
        this.entsign = entsign;
    }

    public String getSupersign() {
        return this.supersign;
    }

    public void setSupersign(String supersign) {
        this.supersign = supersign;
    }


    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public void setEntid(String entid) {
        this.entid = entid;
    }

    public String getEntname() {
        return entname;
    }

    public void setEntname(String entname) {
        this.entname = entname;
    }

    public String getEnttype() {
        return enttype;
    }

    public void setEnttype(String enttype) {
        this.enttype = enttype;
    }

    public String getEntregion() {
        return entregion;
    }

    public void setEntregion(String entregion) {
        this.entregion = entregion;
    }

    public String getEntcredit() {
        return entcredit;
    }

    public void setEntcredit(String entcredit) {
        this.entcredit = entcredit;
    }

    public String getOisuper() {
        return oisuper;
    }

    public void setOisuper(String oisuper) {
        this.oisuper = oisuper;
    }

    public String getInsptable() {
        return insptable;
    }

    public void setInsptable(String insptable) {
        this.insptable = insptable;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getInspstart() {
        return inspstart;
    }

    public void setInspstart(String inspstart) {
        this.inspstart = inspstart;
    }

    public String getEntid() {
        return entid;
    }

    public String getState1() {
        return state1;
    }

    public void setState1(String state1) {
        this.state1 = state1;
    }

    public String getState2() {
        return state2;
    }

    public void setState2(String state2) {
        this.state2 = state2;
    }

    public String getState3() {
        return state3;
    }

    public void setState3(String state3) {
        this.state3 = state3;
    }

    public String getState4() {
        return state4;
    }

    public void setState4(String state4) {
        this.state4 = state4;
    }

    public String getState5() {
        return state5;
    }

    public void setState5(String state5) {
        this.state5 = state5;
    }

    public String getInspstatus() {
        return inspstatus;
    }

    public void setInspstatus(String inspstatus) {
        this.inspstatus = inspstatus;
    }

    public String getInspresult() {
        return inspresult;
    }

    public void setInspresult(String inspresult) {
        this.inspresult = inspresult;
    }

    public String getInspdoc() {
        return inspdoc;
    }

    public void setInspdoc(String inspdoc) {
        this.inspdoc = inspdoc;
    }

    public String getInsploc() {
        return insploc;
    }

    public void setInsploc(String insploc) {
        this.insploc = insploc;
    }

    public String getInspopinion() {
        return inspopinion;
    }

    public void setInspopinion(String inspopinion) {
        this.inspopinion = inspopinion;
    }

    public String getState6() {
        return state6;
    }

    public void setState6(String state6) {
        this.state6 = state6;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRegulatorName() {
        return regulatorName;
    }

    public void setRegulatorName(String regulatorName) {
        this.regulatorName = regulatorName;
    }

    public String getResultstr() {
        return resultstr;
    }

    public void setResultstr(String resultstr) {
        this.resultstr = resultstr;
    }

    public String getState2_1() {
        return this.state2_1;
    }

    public void setState2_1(String state2_1) {
        this.state2_1 = state2_1;
    }


    @Override
    public boolean equals(Object obj) {
        TaskBean taskBean = (TaskBean) obj;
        if (taskBean.getId().equals(this.getId()))
            return true;
        return false;
    }
}
