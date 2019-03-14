package com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity;


import com.contrarywind.interfaces.IPickerViewData;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by hesm on 2018/10/14.
 */
@Entity
public class CityEntity implements IPickerViewData {

    /**
     * code : 140000000000
     * pcode : 0
     * sname : 山西
     * jd : 112.549248
     * wd : 37.857014
     * level : 1
     * orders : 4
     * chechked : null
     */
    @Id(autoincrement = true)
    private Long id;
    private String code;
    private String pcode;
    private String sname;
    private String jd;
    private String wd;
    private String level;
    private int orders;
    private String name;


    @Generated(hash = 284986764)
    public CityEntity(Long id, String code, String pcode, String sname, String jd,
                      String wd, String level, int orders, String name) {
        this.id = id;
        this.code = code;
        this.pcode = pcode;
        this.sname = sname;
        this.jd = jd;
        this.wd = wd;
        this.level = level;
        this.orders = orders;
        this.name = name;
    }

    @Generated(hash = 2001321047)
    public CityEntity() {
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
