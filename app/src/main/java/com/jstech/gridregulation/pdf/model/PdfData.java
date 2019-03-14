package com.jstech.gridregulation.pdf.model;

import java.util.List;
import java.util.Map;

public class PdfData {

    private String name; // 名字

    private String path; // 路径

    private String titlename; // 网格化执法监督系统

    private String titlenum; // 编号

    private String title1; // 网格化执法监督检查记录

    private String checkdate; // 检查时间

    private String checkpeople; // 检查人

    private String title2; // 企业信息

    private String entname; // 企业名称

    private String entregion; // 所属区域

    private String legalname; // 法人姓名

    private String idnumber; // 身份证号

    private String uncode; // 统一信用代码

    private String basename; // 基地名称

    private String title3; // 执法文书

    private String[] imgs; // 图片路径数组

    private String title4; // 检查内容

    private String tablename; // 检查表名称

    private List<Map<String, Object>> items; // 检查项列表<检查项，检查结果，检查图片路径数组>

    private String question; // 存在问题

    private String opinion; // 整改意见

    private String[] checkimgs; // 检查员签字图片路径数组

    private String[] supimgs; // 监管对象签字图片路径数组

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitlename() {
        if (null == titlename) {
            setTitlename("网格化执法监督系统");
        }
        return titlename;
    }

    public void setTitlename(String titlename) {
        this.titlename = titlename;
    }

    public String getTitlenum() {
        return titlenum;
    }

    public void setTitlenum(String titlenum) {
        this.titlenum = titlenum;
    }

    public String getTitle1() {
        if (null == title1) {
            setTitle1("网格化执法监督检查记录");
        }
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getCheckdate() {
        return checkdate;
    }

    public void setCheckdate(String checkdate) {
        this.checkdate = checkdate;
    }

    public String getCheckpeople() {
        return checkpeople;
    }

    public void setCheckpeople(String checkpeople) {
        this.checkpeople = checkpeople;
    }

    public String getTitle2() {
        if (null == title2) {
            setTitle2("企业信息");
        }
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getEntname() {
        return entname;
    }

    public void setEntname(String entname) {
        this.entname = entname;
    }

    public String getEntregion() {
        return entregion;
    }

    public void setEntregion(String entregion) {
        this.entregion = entregion;
    }

    public String getLegalname() {
        return legalname;
    }

    public void setLegalname(String legalname) {
        this.legalname = legalname;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getUncode() {
        return uncode;
    }

    public void setUncode(String uncode) {
        this.uncode = uncode;
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public String getTitle3() {
        if (null == title3) {
            setTitle3("执法文书");
        }
        return title3;
    }

    public void setTitle3(String title3) {
        this.title3 = title3;
    }

    public String[] getImgs() {
        return imgs;
    }

    public void setImgs(String[] imgs) {
        this.imgs = imgs;
    }

    public String getTitle4() {
        if (null == title4) {
            setTitle4("检查内容");
        }
        return title4;
    }

    public void setTitle4(String title4) {
        this.title4 = title4;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String[] getCheckimgs() {
        return checkimgs;
    }

    public void setCheckimgs(String[] checkimgs) {
        this.checkimgs = checkimgs;
    }

    public String[] getSupimgs() {
        return supimgs;
    }

    public void setSupimgs(String[] supimgs) {
        this.supimgs = supimgs;
    }

}
