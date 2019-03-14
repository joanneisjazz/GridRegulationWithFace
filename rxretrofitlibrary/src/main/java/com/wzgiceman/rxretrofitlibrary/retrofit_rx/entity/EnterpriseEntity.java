package com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by hesm on 2018/11/7.
 */
@Entity
public class EnterpriseEntity implements Serializable {


    /**
     * id : 10
     * delFlag : 0
     * createBy : dde6f36e295b41f1b17b8a314eba9bef
     * createTime : 2018-09-12 14:58:20
     * validityStartDate : 2018-09-12 00:00:00
     * validityEndDate : 2018-09-12 00:00:00
     * modifyBy : dde6f36e295b41f1b17b8a314eba9bef
     * modifyTime : 2018-09-12 14:58:27
     * name : 山西生产12（田永威测试数据）
     * nature : 4
     * code : 140105000000001
     * businessLicense : null
     * unifiedCode : 91110108752191073D
     * legalName : 测试
     * registeredCapital : 100
     * buildDate : 2018-09-12 00:00:00
     * businessScope : 测试
     * belongedTrade : 1
     * belongedAreaCity : 140100000000
     * belongedAreaCounty : 140110000000
     * belongedAreaTown : 140110001000
     * address : 山西省太原市晋源区罗城镇
     * longitude : 112.5450659808
     * latitude : 37.7548298357
     * briefIntroduction : 测试
     * productIntroduction : 测试
     * enterpriseMainCode : null
     * enterpriseMainCodePic : null
     * status : 0
     * contactPhone : 17839195501
     * approveType : 1;2;3
     * area : null
     * rc3 : null
     * rc4 : null
     * rc5 : null
     * creditStatus : 2
     * validity_if_forver : 0
     * jcsbid : null
     * inspector : null
     * hasInnerInspector : null
     * publishStatus : 1
     * publishTime : 2018-11-06 00:00:00
     * inspectorPhone : null
     * legalCertificate : null
     * changeContent : null
     */

    @Id
    private String id;
    private String name;
    private String nature;
    private String code;
    private String unifiedCode;
    private String legalName;
    private String registeredCapital;


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNature() {
        return this.nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnifiedCode() {
        return this.unifiedCode;
    }

    public void setUnifiedCode(String unifiedCode) {
        this.unifiedCode = unifiedCode;
    }

    public String getLegalName() {
        return this.legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getRegisteredCapital() {
        return this.registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    private static final long serialVersionUID = 1L;


    @Generated(hash = 2068887829)
    public EnterpriseEntity(String id, String name, String nature, String code,
            String unifiedCode, String legalName, String registeredCapital) {
        this.id = id;
        this.name = name;
        this.nature = nature;
        this.code = code;
        this.unifiedCode = unifiedCode;
        this.legalName = legalName;
        this.registeredCapital = registeredCapital;
    }

    @Generated(hash = 413965007)
    public EnterpriseEntity() {
    }

}
