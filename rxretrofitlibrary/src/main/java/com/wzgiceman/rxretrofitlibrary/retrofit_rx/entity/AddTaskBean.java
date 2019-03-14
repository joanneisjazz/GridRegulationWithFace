package com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 增加检查任务接口里上传的信息
 */
@Entity
public class AddTaskBean {

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
    @Id
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

    public String getInspstart() {
        return inspstart;
    }

    public void setInspstart(String inspstart) {
        this.inspstart = inspstart;
    }

    public AddTaskBean() {
    }

    @Generated(hash = 2071753277)
    public AddTaskBean(String entid, String entname, String enttype,
            String entregion, String entcredit, String oisuper, String insptable,
            String createBy, String updateBy, String inspstart) {
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
}
