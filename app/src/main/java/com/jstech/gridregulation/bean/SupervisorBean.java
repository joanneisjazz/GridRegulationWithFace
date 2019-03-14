package com.jstech.gridregulation.bean;

public class SupervisorBean {
    public String image;
    //头像审核状态，0-未上传，1-已上传，2-不通过，3-通过")
    public String imageStatus;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }
}
