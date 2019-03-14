package com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

@Entity
public class CheckTableEntity {

    /**
     * id : 51a83f55ef89426699ca7888cf931f10
     * name : 测试1
     * num : 2
     * type : 测试
     */

    @Id(autoincrement = true)
    private Long tableId;
    private String id;
    private String name;
    private int num;
    private String type;

    @Transient
    private boolean isSelected;
    @Transient
    private boolean isExpanded;
    @Transient
    private boolean isAllSected;

    public boolean isAllSected() {
        return isAllSected;
    }

    public void setAllSected(boolean allSected) {
        isAllSected = allSected;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Transient
    private List<CheckItemEntity> checkItemBeans;

    @Generated(hash = 1991423774)
    public CheckTableEntity(Long tableId, String id, String name, int num,
            String type) {
        this.tableId = tableId;
        this.id = id;
        this.name = name;
        this.num = num;
        this.type = type;
    }

    @Generated(hash = 590181715)
    public CheckTableEntity() {
    }


    public List<CheckItemEntity> getCheckItemBeans() {
        return checkItemBeans;
    }

    public void setCheckItemBeans(List<CheckItemEntity> checkItemBeans) {
        this.checkItemBeans = checkItemBeans;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTableId() {
        return this.tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }



}
