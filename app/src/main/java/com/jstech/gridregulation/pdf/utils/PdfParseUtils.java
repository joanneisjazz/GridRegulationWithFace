package com.jstech.gridregulation.pdf.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.jstech.gridregulation.pdf.common.PdfDataType;
import com.jstech.gridregulation.pdf.model.PdfData;
import com.jstech.gridregulation.pdf.model.PdfDataBean;

public class PdfParseUtils {
    public static List<PdfDataBean> parsePdfData(PdfData pdf) {
        PdfDataBeanListUtils listUtils = new PdfDataBeanListUtils();

        listUtils.add(pdf.getTitlename());
        if (isNotBlankStr(pdf.getTitlenum())) {
            listUtils.add("编号：" + pdf.getTitlenum());
        }

        listUtils.add();
        listUtils.add();

        listUtils.addCenterBold1_5(pdf.getTitle1());
        {
            PdfDataBean bean = new PdfDataBean(PdfDataType.PARAGRAPH_CENTER);

            List<PdfDataBean> list = new ArrayList<PdfDataBean>();
            list.add(new PdfDataBean(PdfDataType.TEXT_BOLD, "检查时间："));
            list.add(new PdfDataBean(PdfDataType.TEXT, pdf.getCheckdate()));
            list.add(new PdfDataBean(PdfDataType.TEXT_N));
            list.add(new PdfDataBean(PdfDataType.TEXT_N));
            list.add(new PdfDataBean(PdfDataType.TEXT_BOLD, "检查人："));
            list.add(new PdfDataBean(PdfDataType.TEXT, pdf.getCheckpeople()));

            bean.setData(list);
            listUtils.add(bean);
        }

        listUtils.addCenterBold1_5(pdf.getTitle2());
        {
            PdfDataBean bean = new PdfDataBean(PdfDataType.TABLE);

            List<Object> list = new ArrayList<Object>();
            list.add(new float[]{0.6f, 1f, 0.6f, 1f});
            PdfDataBeanListUtils listUtils1 = new PdfDataBeanListUtils();
            listUtils1.addCellCenterBold("企业名称");
            listUtils1.addCell(1, 3, pdf.getEntname());
            listUtils1.addCellCenterBold("所属区域");
            listUtils1.addCell(1, 3, pdf.getEntregion());
            listUtils1.addCellCenterBold("法人姓名");
            listUtils1.addCellCenter(pdf.getLegalname());
            listUtils1.addCellCenterBold("身份证号");
            listUtils1.addCellCenter(pdf.getIdnumber());
            listUtils1.addCellCenterBold("统一信用代码");
            listUtils1.addCellCenter(pdf.getUncode());
            listUtils1.addCellCenterBold("基地名称");
            listUtils1.addCellCenter(pdf.getBasename());
            list.add(listUtils1.getPdfDataBeanList());

            bean.setData(list);
            listUtils.add(bean);
        }

        if (null != pdf.getImgs() && pdf.getImgs().length > 0) {
            listUtils.addCenterBold1_5(pdf.getTitle3());
            listUtils.addImageSymmetryT(180f, 200f, pdf.getImgs());
        }

        listUtils.addBold(pdf.getTablename());
        for (Map<String, Object> item : pdf.getItems()) {
            listUtils.addTB_T("所属检查项(法律依据)：", (String) item.get("content"));
            listUtils.addTB_T("检查结果：", (String) item.get("result"));
            String[] imgs = (String[]) item.get("imgs");
            if (null != imgs && imgs.length > 0) {
                listUtils.addImageSymmetryT(180f, 200f, imgs);
            }
        }

        listUtils.add();
        listUtils.add();

        if (isNotBlankStr(pdf.getQuestion())) {
            listUtils.addTB_T("存在问题：\n", pdf.getQuestion());
        }
        if (isNotBlankStr(pdf.getOpinion())) {
            listUtils.addTB_T("整改意见：\n", pdf.getOpinion());
        }

        listUtils.add();
        listUtils.add();

        listUtils.addImageSignatureT(50f, 30f, "检查员：", pdf.getCheckimgs(),
                "监管对象：", pdf.getSupimgs());

        return listUtils.getPdfDataBeanList();
    }

    private static boolean isBlankStr(String str) {
        return null == str || "".equals(str.replaceAll(" ", ""));
    }

    private static boolean isNotBlankStr(String str) {
        return !isBlankStr(str);
    }

    public static Document parsePdfDataBeanList(Document document,
                                                List<PdfDataBean> pdfDataBeanList) throws Exception {
        document.open();
        for (PdfDataBean bean : pdfDataBeanList) {
            Object obj = parsePdfDataBean(bean);
            if (obj instanceof Paragraph) {
                document.add((Paragraph) obj);
            } else if (obj instanceof PdfPTable) {
                document.add((PdfPTable) obj);
            } else if (obj instanceof Image) {
                document.add((Image) obj);
            }
        }
        return document;
    }

    private static Object parsePdfDataBean(PdfDataBean bean) throws Exception {
        Object element = null;

        switch (bean.getType()) {
            case PARAGRAPH: {
                Paragraph paragraph = PdfElementUtils.getParagraph();
                element = parseParagraph(paragraph, bean.getData());
            }
            break;
            case PARAGRAPH_N:
                element = PdfElementUtils.getParagraphN();
                break;
            case PARAGRAPH_CENTER: {
                Paragraph paragraph = PdfElementUtils.getParagraphCenter();
                element = parseParagraph(paragraph, bean.getData());
            }
            break;
            case PARAGRAPH_RIGHT: {
                Paragraph paragraph = PdfElementUtils.getParagraphRight();
                element = parseParagraph(paragraph, bean.getData());
            }
            break;
            case TEXT:
                element = PdfElementUtils.getText((String) bean.getData());
                break;
            case TEXT_N:
                element = PdfElementUtils.getTextN();
                break;
            case TEXT_BOLD:
                element = PdfElementUtils.getTextBold((String) bean.getData());
                break;
            case TEXT_BOLD_1_5:
                element = PdfElementUtils.getTextBold1_5((String) bean.getData());
                break;
            case TABLE: {
                @SuppressWarnings("unchecked")
                List<Object> tableInfo = (List<Object>) bean.getData();
                PdfPTable table = PdfElementUtils.getTable((float[]) tableInfo
                        .get(0));
                element = parseTable(table, tableInfo.get(1));
            }
            break;
            case CELL: {
                PdfPCell cell = PdfElementUtils.getCell();
                element = parseCell(cell, bean.getData());
            }
            break;
            case CELL_CENTER: {
                PdfPCell cell = PdfElementUtils.getCellCenter();
                element = parseCell(cell, bean.getData());
            }
            break;
            case CELL_X_Y: {
                @SuppressWarnings("unchecked")
                List<Object> cellInfo = (List<Object>) bean.getData();
                PdfPCell cell = PdfElementUtils.getCell((int) cellInfo.get(0),
                        (int) cellInfo.get(1));
                element = parseCell(cell, cellInfo.get(2));
            }
            break;
            case CELL_X_Y_CENTER: {
                @SuppressWarnings("unchecked")
                List<Object> cellCenterInfo = (List<Object>) bean.getData();
                PdfPCell cell = PdfElementUtils.getCellCenter(
                        (int) cellCenterInfo.get(0), (int) cellCenterInfo.get(1));
                element = parseCell(cell, cellCenterInfo.get(2));
            }
            break;
            case IMAGE_W_H: {
                @SuppressWarnings("unchecked")
                List<Object> imageCenterInfo = (List<Object>) bean.getData();
                element = PdfElementUtils.getImage((float) imageCenterInfo.get(0),
                        (float) imageCenterInfo.get(1),
                        (String) imageCenterInfo.get(2));
            }
            break;
            case IMAGE_W_H_CENTER: {
                @SuppressWarnings("unchecked")
                List<Object> imageCenterInfo = (List<Object>) bean.getData();
                element = PdfElementUtils.getImageCenter(
                        (float) imageCenterInfo.get(0),
                        (float) imageCenterInfo.get(1),
                        (String) imageCenterInfo.get(2));
            }
            break;
            case IMAGE_W_H_RIGHT: {
                @SuppressWarnings("unchecked")
                List<Object> imageCenterInfo = (List<Object>) bean.getData();
                element = PdfElementUtils.getImageRight(
                        (float) imageCenterInfo.get(0),
                        (float) imageCenterInfo.get(1),
                        (String) imageCenterInfo.get(2));
            }
            break;
            case TABLE_NO_BORDER: {
                @SuppressWarnings("unchecked")
                List<Object> tableNoBorderInfo = (List<Object>) bean.getData();
                PdfPTable table = PdfElementUtils
                        .getTableNoBorder((float[]) tableNoBorderInfo.get(0));
                element = parseTable(table, tableNoBorderInfo.get(1));
            }
            break;
            case TABLE_NO_BORDER_CENTER: {
                @SuppressWarnings("unchecked")
                List<Object> tableNoBorderCenterInfo = (List<Object>) bean
                        .getData();
                PdfPTable table = PdfElementUtils
                        .getTableNoBorderCenter((float[]) tableNoBorderCenterInfo
                                .get(0));
                element = parseTable(table, tableNoBorderCenterInfo.get(1));
            }
            break;
            case CELL_NO_BORDER: {
                PdfPCell cell = PdfElementUtils.getCellNoBorder();
                element = parseCell(cell, bean.getData());
            }
            break;
            case CELL_NO_BORDER_CENTER: {
                PdfPCell cell = PdfElementUtils.getCellNoBorderCenter();
                element = parseCell(cell, bean.getData());
            }
            break;
            default:
                break;
        }

        return element;
    }

    private static Paragraph parseParagraph(Paragraph paragraph, Object data)
            throws Exception {
        if (data instanceof PdfDataBean) {
            Object element = parsePdfDataBean((PdfDataBean) data);
            paragraph = parseParagraph(paragraph, element);
            return paragraph;
        }

        if (data instanceof String) {
            paragraph.add((String) data);
            return paragraph;
        }

        if (data instanceof String[]) {
            String[] strs = (String[]) data;
            paragraph.add((Chunk) parsePdfDataBean(new PdfDataBean(
                    PdfDataType.TEXT_BOLD, strs[0])));
            paragraph.add((Chunk) parsePdfDataBean(new PdfDataBean(
                    PdfDataType.TEXT, strs[1])));
            return paragraph;
        }

        if (data instanceof Paragraph) {
            paragraph.add((Paragraph) data);
            return paragraph;
        }

        if (data instanceof Chunk) {
            paragraph.add((Chunk) data);
            return paragraph;
        }

        if (data instanceof com.itextpdf.text.List) {
            paragraph.add((com.itextpdf.text.List) data);
            return paragraph;
        }

        if (data instanceof PdfPTable) {
            paragraph.add((PdfPTable) data);
            return paragraph;
        }

        if (data instanceof Image) {
            paragraph.add((Image) data);
            return paragraph;
        }

        if (data instanceof List) {
            @SuppressWarnings("unchecked")
            List<PdfDataBean> list = (List<PdfDataBean>) data;
            for (int i = 0; i < list.size(); i++) {
                Object element = parsePdfDataBean(list.get(i));
                paragraph = parseParagraph(paragraph, element);
            }
            return paragraph;
        }

        return null;
    }

    private static PdfPTable parseTable(PdfPTable table, Object data)
            throws Exception {
        if (data instanceof PdfDataBean) {
            Object element = parsePdfDataBean((PdfDataBean) data);
            table = parseTable(table, element);
            return table;
        }

        if (data instanceof String) {
            table.addCell((String) data);
            return table;
        }

        if (data instanceof PdfPTable) {
            table.addCell((PdfPTable) data);
            return table;
        }

        if (data instanceof PdfPCell) {
            table.addCell((PdfPCell) data);
            return table;
        }

        if (data instanceof Image) {
            table.addCell((Image) data);
            return table;
        }

        if (data instanceof List) {
            @SuppressWarnings("unchecked")
            List<PdfDataBean> list = (List<PdfDataBean>) data;
            for (int i = 0; i < list.size(); i++) {
                Object element = parsePdfDataBean(list.get(i));
                table = parseTable(table, element);
            }
            return table;
        }

        return null;
    }

    private static PdfPCell parseCell(PdfPCell cell, Object data)
            throws Exception {
        if (data instanceof PdfDataBean) {
            Object element = parsePdfDataBean((PdfDataBean) data);
            cell = parseCell(cell, element);
            return cell;
        }

        if (data instanceof String) {
            cell.addElement(PdfElementUtils.getText((String) data));
            return cell;
        }

        if (data instanceof PdfPTable) {
            cell.addElement((PdfPTable) data);
            return cell;
        }

        if (data instanceof Paragraph) {
            cell.addElement((Paragraph) data);
            return cell;
        }

        if (data instanceof Image) {
            cell.addElement((Image) data);
            return cell;
        }

        if (data instanceof List) {
            @SuppressWarnings("unchecked")
            List<PdfDataBean> list = (List<PdfDataBean>) data;
            for (int i = 0; i < list.size(); i++) {
                Object element = parsePdfDataBean(list.get(i));
                cell = parseCell(cell, element);
            }
            return cell;
        }

        return null;
    }

}
