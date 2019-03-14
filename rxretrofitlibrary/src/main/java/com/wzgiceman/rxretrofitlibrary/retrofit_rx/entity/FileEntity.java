package com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class FileEntity {

    @Id
    public Long id;
    public String taskId;
    public String type;
    public String localPath;
    public Long beanId;

    @Generated(hash = 731398337)
    public FileEntity(Long id, String taskId, String type, String localPath,
            Long beanId) {
        this.id = id;
        this.taskId = taskId;
        this.type = type;
        this.localPath = localPath;
        this.beanId = beanId;
    }

    @Generated(hash = 1879603201)
    public FileEntity() {
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocalPath() {
        return this.localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBeanId() {
        return this.beanId;
    }

    public void setBeanId(Long beanId) {
        this.beanId = beanId;
    }
}
