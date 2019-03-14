package com.jstech.gridregulation.utils;

import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.pdf.model.PdfData;
import com.jstech.gridregulation.pdf.model.PdfDataBean;
import com.jstech.gridregulation.pdf.utils.PdfElementUtils;
import com.jstech.gridregulation.pdf.utils.PdfParseUtils;
import com.luck.picture.lib.entity.LocalMedia;
import com.rxretrofitlibrary.greendao.CheckTableEntityDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckTableEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hesm on 2018/11/6.
 */

public class PDFUtil {
    public static PdfData initPdfData(String supName, RegulateObjectBean object, TaskBean taskBean, List<RegulateResultBean> list,
                                      List<LocalMedia> checkImgs,
                                      String opinion) {
        PdfData pdf = new PdfData();

        pdf.setName(taskBean.getId() + ".pdf");//
        String sdCardDir = Environment.getExternalStorageDirectory() + ConstantValue.PATH_PDF;
        File dirFile = new File(sdCardDir);  //目录转化成文件夹
        if (!dirFile.exists()) {
            dirFile.mkdirs();
            Log.d("hesm", sdCardDir + "创建成功");//如果不存在，那就建立这个文件夹
        }
        pdf.setPath(sdCardDir + "/");


        pdf.setTitlenum("");
        pdf.setCheckdate(TextUtil.date2());
        pdf.setCheckpeople(supName);
//        RegulateObjectBean bean = MyApplication.getInstance().getSession().
//                getRegulateObjectBeanDao().
//                queryBuilder().
//                where(RegulateObjectBeanDao.Properties.Id.eq(taskBean.getEntId())).unique();
//        EnterpriseEntity enterpriseEntity = MyApplication.getInstance().getSession().getEnterpriseEntityDao().
//                queryBuilder().
//                where(EnterpriseEntityDao.Properties.Id.eq(object.getId())).unique();
        pdf.setEntname(object.getName());
        pdf.setEntregion(object.getBelongedarea());
        pdf.setLegalname(object.getLegalName());
        pdf.setUncode(object.getUnifiedCode());

        // TODO: 2018/11/8 基地名称
        pdf.setBasename(object.getName() + "基地");
        // TODO: 2018/11/6 待处理？？
        pdf.setIdnumber("");

        //本地的图片路径
//        String[] checkImage = new String[checkImgs.size()];
//        for (int i = 0; i < checkImgs.size(); i++) {
//            checkImage[i] = checkImgs.get(i).getCompressPath();
//        }
//        pdf.setImgs(checkImage);

        CheckTableEntity entity = MyApplication.getInstance().getSession().getCheckTableEntityDao().
                queryBuilder().where(CheckTableEntityDao.Properties.Id.eq(taskBean.getInsptable()))
                .unique();
        if (null == entity) {
            pdf.setTablename("");
        } else {
            pdf.setTablename(entity.getName());
        }

        //检查项的结果
        List<Map<String, Object>> items = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> item;
            item = new HashMap<>();

            item.put("content", list.get(i).getItemcontent());
            item.put("result", TextUtil.getRegulateResult(list.get(i).getInspresult()));

            String imgsStr = list.get(i).getLoacalPath();
            String[] imgs = null;
            if (!TextUtil.isEmpty(imgsStr)) {
                imgs = imgsStr.split(",");
            }
            item.put("imgs", imgs);

            items.add(item);
        }
        pdf.setItems(items);
        pdf.setOpinion(opinion);
        if (TextUtil.isEmpty(taskBean.getRegulator1SignPathLocal())) {
            String[] checkimgs = {taskBean.getRegulator2SignPathLocal()};
            pdf.setCheckimgs(checkimgs);
        } else {
            if (TextUtil.isEmpty(taskBean.getRegulator2SignPathLocal())) {
                String[] checkimgs = {taskBean.getRegulator1SignPathLocal()};
                pdf.setCheckimgs(checkimgs);
            } else {
                String[] checkimgs = {taskBean.getRegulator1SignPathLocal(), taskBean.getRegulator2SignPathLocal()};
                pdf.setCheckimgs(checkimgs);
            }
        }

        String[] supimgs = {taskBean.getObjectSignPath()};
        pdf.setSupimgs(supimgs);

        return pdf;
    }


    public static String createPdfByPdfData(PdfData pdf) {
        String pdfPath = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        try {
            LogUtils.d("开始生成文档：" + sdf.format(new Date()));
            String saveUrl = pdf.getPath() + pdf.getName();
            Document document = PdfElementUtils.initDocument();
            PdfWriter pdfWriter = PdfWriter.getInstance(document,
                    new FileOutputStream(saveUrl));

            List<PdfDataBean> pdfDataBeanList = PdfParseUtils.parsePdfData(pdf);
            PdfParseUtils.parsePdfDataBeanList(document, pdfDataBeanList);

            document.close();
            pdfWriter.close();
            pdfPath = saveUrl;
            LogUtils.d("成功生成文档：" + sdf.format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("生成文档失败：" + sdf.format(new Date()));
            LogUtils.d("失败原因：" + e.getMessage());
            return "";
        }

        return pdfPath;
    }


}
