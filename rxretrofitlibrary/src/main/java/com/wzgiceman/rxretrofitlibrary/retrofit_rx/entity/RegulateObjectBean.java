package com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;

/**
 * 监管对象的实体类
 */
@Entity
public class RegulateObjectBean implements Serializable {

    /**
     * id : 2
     * name : 测试数据
     * nature : 1
     * code : 140105002000001
     * unifiedCode : 91110108752191073D
     * belongedTrade : 1
     * address : 山西省太原市晋源区罗城镇
     * longitude : 112.48639
     * latitude : 37.760263
     * contactPhone : 17839195501
     * inspcount : 2
     * entcredit :
     * passrate :
     * entregion : 140110001
     */


    @Id
    private String id;
    private String name;
    private String nature;
    private String code;
    private String unifiedCode;
    private String belongedTrade;
    private String address;
    private double longitude;
    private double latitude;
    private String contactPhone;
    private String inspcount;
    private String entcredit;
    private String passrate;
    private String entregion;
    private String oisuper;
    private String oisupername;
    private String inspstatus;
    private String userId;
    private String belongedarea;
    private String legalName;

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getInspstatus() {
        return inspstatus;
    }

    public void setInspstatus(String inspstatus) {
        this.inspstatus = inspstatus;
    }

    public String getIsSearched() {
        return isSearched;
    }

    public void setIsSearched(String isSearched) {
        this.isSearched = isSearched;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String isSearched;


    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnifiedCode() {
        return unifiedCode;
    }

    public void setUnifiedCode(String unifiedCode) {
        this.unifiedCode = unifiedCode;
    }

    public String getBelongedTrade() {
        return belongedTrade;
    }

    public void setBelongedTrade(String belongedTrade) {
        this.belongedTrade = belongedTrade;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getInspcount() {
        return inspcount;
    }

    public void setInspcount(String inspcount) {
        this.inspcount = inspcount;
    }

    public String getEntcredit() {
        return entcredit;
    }

    public void setEntcredit(String entcredit) {
        this.entcredit = entcredit;
    }

    public String getPassrate() {
        return passrate;
    }

    public void setPassrate(String passrate) {
        this.passrate = passrate;
    }

    public String getEntregion() {
        return entregion;
    }

    public void setEntregion(String entregion) {
        this.entregion = entregion;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getOisuper() {
        return this.oisuper;
    }

    public void setOisuper(String oisuper) {
        this.oisuper = oisuper;
    }

    public String getOisupername() {
        return this.oisupername;
    }

    public void setOisupername(String oisupername) {
        this.oisupername = oisupername;
    }

    public String getBelongedarea() {
        return belongedarea;
    }

    public void setBelongedarea(String belongedarea) {
        this.belongedarea = belongedarea;
    }

    @Transient
    private EnterpriseEntity enterprise;


    public EnterpriseEntity getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(EnterpriseEntity enterprise) {
        this.enterprise = enterprise;
    }

    private static final long serialVersionUID = 1L;

    @Generated(hash = 559987075)
    public RegulateObjectBean(String id, String name, String nature, String code,
            String unifiedCode, String belongedTrade, String address,
            double longitude, double latitude, String contactPhone,
            String inspcount, String entcredit, String passrate, String entregion,
            String oisuper, String oisupername, String inspstatus, String userId,
            String belongedarea, String legalName, String isSearched) {
        this.id = id;
        this.name = name;
        this.nature = nature;
        this.code = code;
        this.unifiedCode = unifiedCode;
        this.belongedTrade = belongedTrade;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.contactPhone = contactPhone;
        this.inspcount = inspcount;
        this.entcredit = entcredit;
        this.passrate = passrate;
        this.entregion = entregion;
        this.oisuper = oisuper;
        this.oisupername = oisupername;
        this.inspstatus = inspstatus;
        this.userId = userId;
        this.belongedarea = belongedarea;
        this.legalName = legalName;
        this.isSearched = isSearched;
    }

    @Generated(hash = 1562055225)
    public RegulateObjectBean() {
    }


}
