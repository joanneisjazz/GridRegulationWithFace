package com.jstech.gridregulation.bean;

import java.io.Serializable;

/**
 * Created by hesm on 2018/10/22.
 * 获取现场检查信息的时候，里面包含的检察人员的信息
 */

public class UserInfoBean implements Serializable {

    private String loginname;
    private String username;
    private String email;

    private String mobile;
    private String orgName;
    /**
     * id : 241671aee7cc4ead88b2532b7686183c
     * roName : 郭淳
     * superviseId : 140000000000001
     * superviseName : null
     * roGender : 女
     * roIdcert : 422156478921546211
     * roUnit : 检测站
     * roTel : 15795643355
     * roEducation : 大专
     * roTitle :
     * roJob : 1
     * ifInspector : 0
     * roScore : 0
     * roValidityPeriod : 2015-09-15至2018-09-15
     * createBy : 超级管理员
     * createDate : 2018-09-05 10:33:35
     * remarks :
     * delFlag : 0
     * rc1 : null
     * rc2 : null
     * rc3 : null
     * rc4 : null
     * rc5 : null
     * supervise : null
     * loginname : null
     * password : null
     * userid : null
     */

    private String id;
    private String roName;
    private String superviseId;
    private Object superviseName;
    private String roGender;
    private String roIdcert;
    private String roUnit;
    private String roTel;
    private String roEducation;
    private String roTitle;
    private String roJob;
    private String ifInspector;
    private int roScore;
    private String roValidityPeriod;
    private String createBy;
    private String createDate;
    private String remarks;
    private String delFlag;
    private Object rc1;
    private Object rc2;
    private Object rc3;
    private Object rc4;
    private Object rc5;
    private Object supervise;

    private Object password;
    private Object userid;

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoName() {
        return roName;
    }

    public void setRoName(String roName) {
        this.roName = roName;
    }

    public String getSuperviseId() {
        return superviseId;
    }

    public void setSuperviseId(String superviseId) {
        this.superviseId = superviseId;
    }

    public Object getSuperviseName() {
        return superviseName;
    }

    public void setSuperviseName(Object superviseName) {
        this.superviseName = superviseName;
    }

    public String getRoGender() {
        return roGender;
    }

    public void setRoGender(String roGender) {
        this.roGender = roGender;
    }

    public String getRoIdcert() {
        return roIdcert;
    }

    public void setRoIdcert(String roIdcert) {
        this.roIdcert = roIdcert;
    }

    public String getRoUnit() {
        return roUnit;
    }

    public void setRoUnit(String roUnit) {
        this.roUnit = roUnit;
    }

    public String getRoTel() {
        return roTel;
    }

    public void setRoTel(String roTel) {
        this.roTel = roTel;
    }

    public String getRoEducation() {
        return roEducation;
    }

    public void setRoEducation(String roEducation) {
        this.roEducation = roEducation;
    }

    public String getRoTitle() {
        return roTitle;
    }

    public void setRoTitle(String roTitle) {
        this.roTitle = roTitle;
    }

    public String getRoJob() {
        return roJob;
    }

    public void setRoJob(String roJob) {
        this.roJob = roJob;
    }

    public String getIfInspector() {
        return ifInspector;
    }

    public void setIfInspector(String ifInspector) {
        this.ifInspector = ifInspector;
    }

    public int getRoScore() {
        return roScore;
    }

    public void setRoScore(int roScore) {
        this.roScore = roScore;
    }

    public String getRoValidityPeriod() {
        return roValidityPeriod;
    }

    public void setRoValidityPeriod(String roValidityPeriod) {
        this.roValidityPeriod = roValidityPeriod;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
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

    public Object getSupervise() {
        return supervise;
    }

    public void setSupervise(Object supervise) {
        this.supervise = supervise;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public Object getUserid() {
        return userid;
    }

    public void setUserid(Object userid) {
        this.userid = userid;
    }
}
