package com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id : a2c62bab5c3a4521a3cdb99d560ef473
     * loginname : grid
     * password : 83OXOu9if72y9go1F1vAIVFcOQWNsH1EIyMh5yvHoLE=
     * salt : Ugrk4LRAZTwOo5dLcJ8sJw==
     * username : 网格化监管员
     * isOnline : true
     * disabled : false
     * email : limingkai@163.com
     * loginAt : 1541752908
     * loginIp : null
     * loginCount : 727
     * customMenu : null
     * loginTheme : palette.4.css
     * mobile : 15795643355
     * orgId : 140000000000001
     * orgName : 山西省农产品质量安全监管局
     * orgType : JG
     * userExtId : 241671aee7cc4ead88b2532b7686183c
     * loginSidebar : false
     * loginBoxed : false
     * loginScroll : false
     * loginPjax : true
     * unitid : root
     * unit : null
     * roles : [{"id":"9f9c59e494304c9598b5914731ef689b","name":"网格化监管员","code":"ROLE_GRID_CHECKER","aliasName":null,"disabled":false,"unitid":"","note":"各级网格化监管员","unit":null,"menus":null,"users":null,"opBy":"75fd9a9f72b14c989622d35c71ad8480","opAt":1537694810,"delFlag":false}]
     * units : null
     * menus : null
     * firstMenus : null
     * secondMenus : null
     * customMenus : null
     * expTime : 2592000
     * opBy : c8eea9fc63b54f5995e567b08007a4ad
     * opAt : 1538026655
     * delFlag : false
     */

    @Id(autoincrement = true)
    private Long beanId;

    private String id;

    private String password;
    private String roName;

    private String loginname;
    private String salt;
    private String username;
    private String email;
    private int loginCount;
    private String loginTheme;
    private String mobile;
    private String orgId;
    private String orgName;
    private String orgType;
    private String userExtId;
    private boolean loginSidebar;
    private boolean loginBoxed;
    private boolean loginScroll;
    private boolean loginPjax;
    private String unitid;

    private int expTime;
    private String opBy;
    private int opAt;
    private String loginTime;


    @Generated(hash = 1461321634)
    public UserBean(Long beanId, String id, String password, String roName, String loginname, String salt, String username, String email, int loginCount, String loginTheme, String mobile, String orgId, String orgName, String orgType, String userExtId, boolean loginSidebar,
            boolean loginBoxed, boolean loginScroll, boolean loginPjax, String unitid, int expTime, String opBy, int opAt, String loginTime) {
        this.beanId = beanId;
        this.id = id;
        this.password = password;
        this.roName = roName;
        this.loginname = loginname;
        this.salt = salt;
        this.username = username;
        this.email = email;
        this.loginCount = loginCount;
        this.loginTheme = loginTheme;
        this.mobile = mobile;
        this.orgId = orgId;
        this.orgName = orgName;
        this.orgType = orgType;
        this.userExtId = userExtId;
        this.loginSidebar = loginSidebar;
        this.loginBoxed = loginBoxed;
        this.loginScroll = loginScroll;
        this.loginPjax = loginPjax;
        this.unitid = unitid;
        this.expTime = expTime;
        this.opBy = opBy;
        this.opAt = opAt;
        this.loginTime = loginTime;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
    }


    public String getRoName() {
        return roName;
    }

    public void setRoName(String roName) {
        this.roName = roName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public String getLoginTheme() {
        return loginTheme;
    }

    public void setLoginTheme(String loginTheme) {
        this.loginTheme = loginTheme;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getUserExtId() {
        return userExtId;
    }

    public void setUserExtId(String userExtId) {
        this.userExtId = userExtId;
    }

    public boolean isLoginSidebar() {
        return loginSidebar;
    }

    public void setLoginSidebar(boolean loginSidebar) {
        this.loginSidebar = loginSidebar;
    }

    public boolean isLoginBoxed() {
        return loginBoxed;
    }

    public void setLoginBoxed(boolean loginBoxed) {
        this.loginBoxed = loginBoxed;
    }

    public boolean isLoginScroll() {
        return loginScroll;
    }

    public void setLoginScroll(boolean loginScroll) {
        this.loginScroll = loginScroll;
    }

    public boolean isLoginPjax() {
        return loginPjax;
    }

    public void setLoginPjax(boolean loginPjax) {
        this.loginPjax = loginPjax;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public int getExpTime() {
        return expTime;
    }

    public void setExpTime(int expTime) {
        this.expTime = expTime;
    }

    public String getOpBy() {
        return opBy;
    }

    public void setOpBy(String opBy) {
        this.opBy = opBy;
    }

    public int getOpAt() {
        return opAt;
    }

    public void setOpAt(int opAt) {
        this.opAt = opAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getLoginSidebar() {
        return this.loginSidebar;
    }

    public boolean getLoginBoxed() {
        return this.loginBoxed;
    }

    public boolean getLoginScroll() {
        return this.loginScroll;
    }

    public boolean getLoginPjax() {
        return this.loginPjax;
    }

    public String getLoginTime() {
        return this.loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public Long getBeanId() {
        return this.beanId;
    }

    public void setBeanId(Long beanId) {
        this.beanId = beanId;
    }
}
