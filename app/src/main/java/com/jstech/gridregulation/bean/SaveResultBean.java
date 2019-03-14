package com.jstech.gridregulation.bean;

import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;

public class SaveResultBean {

    private String imgs;
    private TaskBean insp;

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public TaskBean getInsp() {
        return insp;
    }

    public void setInsp(TaskBean insp) {
        this.insp = insp;
    }
}
