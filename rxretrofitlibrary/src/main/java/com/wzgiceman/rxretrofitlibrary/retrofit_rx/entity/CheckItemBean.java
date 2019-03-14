package com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity;

import java.io.Serializable;

public class CheckItemBean implements Serializable{



    private boolean isSelected;
    private String result;
    private String reason;
    /**
     * id : 1636f6d8ef5348168bb11f2be730e943
     * content : 测试
     * method : 测试
     * iskey : 0
     * tableid : 51a83f55ef89426699ca7888cf931f10
     */

    private String id;
    private String content;
    private String method;
    private String iskey;
    private String tableid;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
}
