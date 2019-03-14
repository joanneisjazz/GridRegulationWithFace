package com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity;

import java.io.Serializable;

/**
 * 违法违规案件上传
 */
public class CaseEntity implements Serializable {

    /**
     * reporter :
     * reportername : 上报人
     * reportertel : 12345678901
     * filetype :
     * fileaddr :
     * content : 一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十
     * location : 112.48123,37.760321
     * hapDate : 选择日期或当前日期，null也可以不过不推荐
     * createBy : 241671aee7cc4ead88b2532b7686183c
     * remarks :
     * rc1 :
     * rc2 :
     * rc3 :
     * rc4 :
     * rc5 :
     */

    private String reporter;//
    private String reportername;
    private String reportertel;
    private String filetype;
    private String fileaddr;
    private String content;
    private String location;
    private String hapDate;
    private String createBy;
    private String remarks;
    private String rc1;
    private String rc2;
    private String rc3;
    private String rc4;
    private String rc5;
    private String contacts;//联系人
    private String subject;//事件发生主体
    private String haploccode;//发生地点代码
    private String haplocaddr;//发生地点
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHaploccode() {
        return haploccode;
    }

    public void setHaploccode(String haploccode) {
        this.haploccode = haploccode;
    }

    public String getHaplocaddr() {
        return haplocaddr;
    }

    public void setHaplocaddr(String haplocaddr) {
        this.haplocaddr = haplocaddr;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getReportername() {
        return reportername;
    }

    public void setReportername(String reportername) {
        this.reportername = reportername;
    }

    public String getReportertel() {
        return reportertel;
    }

    public void setReportertel(String reportertel) {
        this.reportertel = reportertel;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFileaddr() {
        return fileaddr;
    }

    public void setFileaddr(String fileaddr) {
        this.fileaddr = fileaddr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHapDate() {
        return hapDate;
    }

    public void setHapDate(String hapDate) {
        this.hapDate = hapDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRc1() {
        return rc1;
    }

    public void setRc1(String rc1) {
        this.rc1 = rc1;
    }

    public String getRc2() {
        return rc2;
    }

    public void setRc2(String rc2) {
        this.rc2 = rc2;
    }

    public String getRc3() {
        return rc3;
    }

    public void setRc3(String rc3) {
        this.rc3 = rc3;
    }

    public String getRc4() {
        return rc4;
    }

    public void setRc4(String rc4) {
        this.rc4 = rc4;
    }

    public String getRc5() {
        return rc5;
    }

    public void setRc5(String rc5) {
        this.rc5 = rc5;
    }
}
