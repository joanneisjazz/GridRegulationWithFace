package com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class RegulateResultBean implements Serializable {


    /**
     * itemid : 1636f6d8ef5348168bb11f2be730e943
     * itemcontent : 测试
     * inspresult : 1
     * inspdesc :
     * inspid : 0e0c872bc1c848eeaf713e9f899f2e0a
     * insptable : 51a83f55ef89426699ca7888cf931f10
     * oisuper : 241671aee7cc4ead88b2532b7686183c
     * insploc : 112.48123,37.760321
     * createBy : 241671aee7cc4ead88b2532b7686183c
     * updateBy : 241671aee7cc4ead88b2532b7686183c
     */
    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long beanId;
    private String id;
    private String itemid;
    private String itemcontent;
    private String inspresult;
    private String inspdesc;
    private String inspid;
    private String insptable;
    private String oisuper;
    private String insploc;
    private String createBy;
    private String updateBy;
    private String inspstatus;
    private String image;//上传后的路径
    private String loacalPath;

    /**
     * 0:只保存在本地，没有提交至服务器
     * 1：检查项已经提交至服务器，检查结果还没提交
     * 2：检查结果已经提交至服务器
     */
    private String status = "";


    private String inspstart;


    @Generated(hash = 1514913300)
    public RegulateResultBean(Long beanId, String id, String itemid, String itemcontent, String inspresult,
            String inspdesc, String inspid, String insptable, String oisuper, String insploc, String createBy,
            String updateBy, String inspstatus, String image, String loacalPath, String status,
            String inspstart) {
        this.beanId = beanId;
        this.id = id;
        this.itemid = itemid;
        this.itemcontent = itemcontent;
        this.inspresult = inspresult;
        this.inspdesc = inspdesc;
        this.inspid = inspid;
        this.insptable = insptable;
        this.oisuper = oisuper;
        this.insploc = insploc;
        this.createBy = createBy;
        this.updateBy = updateBy;
        this.inspstatus = inspstatus;
        this.image = image;
        this.loacalPath = loacalPath;
        this.status = status;
        this.inspstart = inspstart;
    }

    @Generated(hash = 414442485)
    public RegulateResultBean() {
    }


    public String getInspstart() {
        return inspstart;
    }

    public void setInspstart(String inspstart) {
        this.inspstart = inspstart;
    }

    public String getItemcontent() {
        return itemcontent;
    }

    public void setItemcontent(String itemcontent) {
        this.itemcontent = itemcontent;
    }

    public String getInspresult() {
        return inspresult;
    }

    public void setInspresult(String inspresult) {
        this.inspresult = inspresult;
    }

    public String getInspdesc() {
        return inspdesc;
    }

    public void setInspdesc(String inspdesc) {
        this.inspdesc = inspdesc;
    }

    public String getInspid() {
        return inspid;
    }

    public void setInspid(String inspid) {
        this.inspid = inspid;
    }

    public String getInsptable() {
        return insptable;
    }

    public void setInsptable(String insptable) {
        this.insptable = insptable;
    }

    public String getOisuper() {
        return oisuper;
    }

    public void setOisuper(String oisuper) {
        this.oisuper = oisuper;
    }

    public String getInsploc() {
        return insploc;
    }

    public void setInsploc(String insploc) {
        this.insploc = insploc;
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

    public Long getBeanId() {
        return beanId;
    }

    public void setBeanId(Long beanId) {
        this.beanId = beanId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInspstatus() {
        return inspstatus;
    }

    public void setInspstatus(String inspstatus) {
        this.inspstatus = inspstatus;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLoacalPath() {
        return this.loacalPath;
    }

    public void setLoacalPath(String loacalPath) {
        this.loacalPath = loacalPath;
    }
}
