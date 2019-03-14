package com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public class CheckItemEntity {


    /**
     * id : 1636f6d8ef5348168bb11f2be730e943
     * content : 测试
     * method : 测试
     * iskey : 0
     * tableid : 51a83f55ef89426699ca7888cf931f10
     */

    @Id
    private Long itemId;
    private String id;
    private String content;
    private String method;
    private String iskey;
    private String tableid;

    @Transient
    private boolean isSelected;

    @Generated(hash = 574564590)
    public CheckItemEntity(Long itemId, String id, String content, String method,
            String iskey, String tableid) {
        this.itemId = itemId;
        this.id = id;
        this.content = content;
        this.method = method;
        this.iskey = iskey;
        this.tableid = tableid;
    }

    @Generated(hash = 2142493737)
    public CheckItemEntity() {
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIskey() {
        return iskey;
    }

    public void setIskey(String iskey) {
        this.iskey = iskey;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public Long getItemId() {
        return this.itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
