package com.jstech.gridregulation.bean;

import java.io.Serializable;

/**
 * Created by hesm on 2018/10/22.
 */

public class RegulateResult2Bean implements Serializable{


    /**
     * id : fab8d7ee5be94e8d9e51883941864146
     * entid : 6
     * entname : 山西太原店3（田永威测试数据）
     * enttype : 1
     * entregion : 0000
     * entcredit :
     * inspyear : 2018
     * oisuper : 241671aee7cc4ead88b2532b7686183c
     * inspstatus : 2
     * inspcount : 2
     * inspstart : 2018-10-21 14:37:57
     * inspend : 2018-10-21 14:42:15
     * inspresult : null
     * inspdoc : null
     * insploc : null
     * insptable : 51a83f55ef89426699ca7888cf931f10
     * inspopinion :
     * supersign : http://192.168.0.56:8083/upload/image/20181021/688dma83c0hs2pqfiegb8c6ml5.jpg
     * entsign : http://192.168.0.56:8083/upload/image/20181021/6i3hv5snmijk4o6q426gd1vj3e.jpg
     * image : http://192.168.0.56:8083/upload/image/20181021/5vnt8t6vmqhi0qvcmpcsd48olv.jpg,http://192.168.0.56:8083/upload/image/20181021/obvtbnkqaoikpohseoifj4f3t3.jpg,http://192.168.0.56:8083/upload/image/20181021/23gpih8h9mhkiq37m6qv02r7ad.jpg
     * rc1 : null
     * rc2 : null
     * rc3 : null
     * rc4 : null
     * rc5 : null
     * pepole : null
     * table : null
     * enterprise : null
     * createBy : 241671aee7cc4ead88b2532b7686183c
     * createDate : 2018-10-21 14:37:57
     * updateBy : 241671aee7cc4ead88b2532b7686183c
     * updateDate : 2018-10-21 14:42:15
     * remarks : null
     * delFlag : 0
     */

    private String id;
    private String entid;
    private String entname;
    private int enttype;
    private String entregion;
    private String entcredit;
    private String inspyear;
    private String oisuper;
    private String inspstatus;
    private int inspcount;
    private String inspstart;
    private String inspend;
    private Object inspresult;
    private String inspdoc;
    private Object insploc;
    private String insptable;
    private String inspopinion;
    private String supersign;
    private String entsign;
    private String image;
    private Object rc1;
    private Object rc2;
    private Object rc3;
    private Object rc4;
    private Object rc5;
    private Object table;
    private Object enterprise;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
    private Object remarks;
    private String delFlag;

    private UserInfoBean pepole;

    public void setPepole(UserInfoBean pepole) {
        this.pepole = pepole;
    }

    public UserInfoBean getPepole() {
        return pepole;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntid() {
        return entid;
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

    public int getEnttype() {
        return enttype;
    }

    public void setEnttype(int enttype) {
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

    public String getInspyear() {
        return inspyear;
    }

    public void setInspyear(String inspyear) {
        this.inspyear = inspyear;
    }

    public String getOisuper() {
        return oisuper;
    }

    public void setOisuper(String oisuper) {
        this.oisuper = oisuper;
    }

    public String getInspstatus() {
        return inspstatus;
    }

    public void setInspstatus(String inspstatus) {
        this.inspstatus = inspstatus;
    }

    public int getInspcount() {
        return inspcount;
    }

    public void setInspcount(int inspcount) {
        this.inspcount = inspcount;
    }

    public String getInspstart() {
        return inspstart;
    }

    public void setInspstart(String inspstart) {
        this.inspstart = inspstart;
    }

    public String getInspend() {
        return inspend;
    }

    public void setInspend(String inspend) {
        this.inspend = inspend;
    }

    public Object getInspresult() {
        return inspresult;
    }

    public void setInspresult(Object inspresult) {
        this.inspresult = inspresult;
    }

    public String getInspdoc() {
        return inspdoc;
    }

    public void setInspdoc(String inspdoc) {
        this.inspdoc = inspdoc;
    }

    public Object getInsploc() {
        return insploc;
    }

    public void setInsploc(Object insploc) {
        this.insploc = insploc;
    }

    public String getInsptable() {
        return insptable;
    }

    public void setInsptable(String insptable) {
        this.insptable = insptable;
    }

    public String getInspopinion() {
        return inspopinion;
    }

    public void setInspopinion(String inspopinion) {
        this.inspopinion = inspopinion;
    }

    public String getSupersign() {
        return supersign;
    }

    public void setSupersign(String supersign) {
        this.supersign = supersign;
    }

    public String getEntsign() {
        return entsign;
    }

    public void setEntsign(String entsign) {
        this.entsign = entsign;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Object getRc1() {
        return rc1;
    }

    public void setRc1(Object rc1) {
        this.rc1 = rc1;
    }

    public Object getRc2() {
        return rc2;
    }

    public void setRc2(Object rc2) {
        this.rc2 = rc2;
    }

    public Object getRc3() {
        return rc3;
    }

    public void setRc3(Object rc3) {
        this.rc3 = rc3;
    }

    public Object getRc4() {
        return rc4;
    }

    public void setRc4(Object rc4) {
        this.rc4 = rc4;
    }

    public Object getRc5() {
        return rc5;
    }

    public void setRc5(Object rc5) {
        this.rc5 = rc5;
    }

    public Object getTable() {
        return table;
    }

    public void setTable(Object table) {
        this.table = table;
    }

    public Object getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Object enterprise) {
        this.enterprise = enterprise;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
