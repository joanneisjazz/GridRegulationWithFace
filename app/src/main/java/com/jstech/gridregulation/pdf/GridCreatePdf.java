package com.jstech.gridregulation.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.jstech.gridregulation.pdf.model.PdfData;
import com.jstech.gridregulation.pdf.model.PdfDataBean;
import com.jstech.gridregulation.pdf.utils.PdfElementUtils;
import com.jstech.gridregulation.pdf.utils.PdfParseUtils;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridCreatePdf {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        System.out.println("开始生成数据：" + sdf.format(new Date()));
        PdfData pdfData = initPdfData();
        System.out.println("成功生成数据：" + sdf.format(new Date()));

        System.out.println("生成文档路径：" + createPdfByPdfData(pdfData));
    }

    public static String createPdfByPdfData(PdfData pdf) {
        String pdfPath = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        try {
            System.out.println("开始生成文档：" + sdf.format(new Date()));
            String saveUrl = pdf.getPath() + pdf.getName();
            Document document = PdfElementUtils.initDocument();
            PdfWriter pdfWriter = PdfWriter.getInstance(document,
                    new FileOutputStream(saveUrl));

            List<PdfDataBean> pdfDataBeanList = PdfParseUtils.parsePdfData(pdf);
            PdfParseUtils.parsePdfDataBeanList(document, pdfDataBeanList);

            document.close();
            pdfWriter.close();
            pdfPath = saveUrl;
            System.out.println("成功生成文档：" + sdf.format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成文档失败：" + sdf.format(new Date()));
        }

        return pdfPath;
    }

    private static PdfData initPdfData() {
        PdfData pdf = new PdfData();

        pdf.setName("检查报告-550版本生成.pdf");
        pdf.setPath("/");

        pdf.setTitlenum("0001");

        pdf.setCheckdate("2018-06-04");
        pdf.setCheckpeople("沈涛，马建国");

        pdf.setEntname("山西田康果蔬有限公司");
        pdf.setEntregion("山西省>大同市>阳高县");
        pdf.setLegalname("高永");
        pdf.setIdnumber("14043419703129003");
        pdf.setUncode("180434197031290T");
        pdf.setBasename("山西田康果蔬有限公司");

        // {
        //     String[] imgs = {"D:/image/t/t1.png", "D:/image/t/t2.png"};
        //     pdf.setImgs(imgs);
        // }

        pdf.setTablename("投入品管理          （兽药 饲料）");
        {
            List<Map<String, Object>> items = new ArrayList<>();
            Map<String, Object> item;
            item = new HashMap<>();
            item.put("content",
                    "投入品管理-3.7 饲料选购：外购饮料的养殖场提供所购饮料生产企业的饲料生产许可证复印件、购销合同（或发票、收据等）和检测报告");
            item.put("result", "不合格");
            String[] imgs = {"D:/image/t/t1.png", "D:/image/t/t2.png", "D:/image/t/t2.png"};
            item.put("imgs", imgs);
            items.add(item);
            item = new HashMap<>();
            item.put("content",
                    "投入品管理-3.9 饲料添加剂管理：饲料库房及配料库中饲料添加剂和药物添加剂存放和使用情况。");
            item.put("result", "基本合格");
            imgs = new String[]{};
            item.put("imgs", imgs);
            items.add(item);
            item = new HashMap<>();
            item.put("content",
                    "投入品管理-3.2 引种：从有《种畜禽生产经营许可证》的种畜禽场引进。");
            item.put("result", "合格");
            imgs = new String[]{"D:/image/t/t1.png"};
            item.put("imgs", imgs);
            items.add(item);
            pdf.setItems(items);
        }

        pdf.setQuestion("一、企业生产基地基础设施还不够完善；\n二、企业生产基地生产记录不够完善，没有登记到位；\n三、企业饲料库和配料库中饲料添加剂和药物添加剂存放使用情况做的还不够完善，存在摆放位置随意，使用完后没有妥善保存的问题。");
        pdf.setOpinion("一、加强企业生产基地基础设施建设；\n二、加强企业员工的培训，增加他们的责任意识，做到生产记录每一步都要落实到位；\n三、加强企业库管员的培训，指定库存管理方案，避免出现类似问题。");

        {
            String[] checkimgs = {"D:/image/q/q1.png", "D:/image/q/q2.png"};
            pdf.setCheckimgs(checkimgs);
            String[] supimgs = {"D:/image/q/q3.png"};
            pdf.setSupimgs(supimgs);
        }

        return pdf;
    }
}
