package com.jstech.gridregulation.bean;

/**
 * Created by hesm on 2018/11/8.
 */

public class WorkDeskBean {
    private String name;
    private int icon;
    private Class<?> tClass;

    public WorkDeskBean(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public WorkDeskBean(String name, int icon, Class<?> tClass) {
        this.name = name;
        this.icon = icon;
        this.tClass = tClass;
    }

    public Class<?> gettClass() {
        return tClass;
    }

    public void settClass(Class<?> tClass) {
        this.tClass = tClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
