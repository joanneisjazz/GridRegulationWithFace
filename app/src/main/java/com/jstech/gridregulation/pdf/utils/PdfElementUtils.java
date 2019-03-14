package com.jstech.gridregulation.pdf.utils;


import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.jstech.gridregulation.pdf.common.PdfConstant;
import com.jstech.gridregulation.utils.TextUtil;

public class PdfElementUtils {

    public static Font pdfFont = null;
    public static BaseFont bfChinese = null;
    public static float pdfFontSize = PdfConstant.FONT_SIZE;
    public static float pdfSpacing = 0.5f * pdfFontSize;

    private static void initPdfFont() throws Exception {
        // 处理中文问题
        bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                BaseFont.NOT_EMBEDDED);
        pdfFont = new Font(bfChinese, pdfFontSize, Font.NORMAL);
//		pdfFont = new Font();
    }

    public static Document initDocument() throws Exception {
        Document document = new Document(PdfConstant.PAGE_SIZE);
        document.setMargins(PdfConstant.PAGE_MARGIN_LEFT,
                PdfConstant.PAGE_MARGIN_RIGHT, PdfConstant.PAGE_MARGIN_TOP,
                PdfConstant.PAGE_MARGIN_BOTTOM);

        initPdfFont();
        return document;
    }

    public static Paragraph getParagraph() {
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(pdfFont);
        paragraph.setSpacingBefore(pdfSpacing);
        paragraph.setSpacingAfter(pdfSpacing);
        return paragraph;
    }

    public static Paragraph getParagraphN() {
        Paragraph paragraph = getParagraph();
        paragraph.add("");
        return paragraph;
    }

    public static Paragraph getParagraphCenter() {
        Paragraph paragraph = getParagraph();
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }

    public static Paragraph getParagraphRight() {
        Paragraph paragraph = getParagraph();
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        return paragraph;
    }

    public static Chunk getText(String str) {
        if (null == str) {
            str = "";
        }
        return new Chunk(str, pdfFont);
    }

    public static Chunk getTextN() {
        Chunk text = getText("    ");
        return text;
    }

    public static Chunk getTextBold(String str) {
        Chunk text = getText(str);
        Font font = new Font(text.getFont());
        font.setStyle(Font.BOLD);
        text.setFont(font);
        return text;
    }

    public static Chunk getTextBold1_5(String str) {
        Chunk text = getTextBold(str);
        Font font = new Font(text.getFont());
        font.setSize(1.5f * pdfFontSize);
        text.setFont(font);
        return text;
    }

    public static PdfPTable getTable(float[] floats) {
        PdfPTable table = new PdfPTable(floats);
        table.setWidthPercentage(100);
        table.setSpacingBefore(pdfSpacing);
        table.setSpacingAfter(pdfSpacing);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        return table;
    }

    public static PdfPTable getTableCenter(float[] floats) {
        PdfPTable table = getTable(floats);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        return table;
    }

    public static PdfPCell getCell() {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    public static PdfPCell getCell(int row, int col) {
        PdfPCell cell = new PdfPCell();
        cell.setRowspan(row);
        cell.setColspan(col);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    public static PdfPCell getCellCenter() {
        PdfPCell cell = getCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    public static PdfPCell getCellCenter(int row, int col) {
        PdfPCell cell = getCell(row, col);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    public static Image getImage(String url) throws Exception {
        Image image = Image.getInstance(url);
        // image.setSpacingBefore(pdfSpacing);
        // image.setSpacingAfter(pdfSpacing);
        return image;
    }

    public static Image getImageCenter(String url) throws Exception {
        Image image = getImage(url);
        image.setAlignment(Image.MIDDLE);
        return image;
    }

    public static Image getImage(float width, float height, String url)
            throws Exception {
        Image image = getImage(url);
        // TODO:不知道为啥，效果不尽人意，凑合用吧
        image.scaleToFit(width, height);
        // image.scaleAbsolute(width, height);
        return image;
    }

    public static Image getImageCenter(float width, float height, String url)
            throws Exception {
        Image image = getImage(width, height, url);
        image.setAlignment(Image.MIDDLE);
        return image;
    }

    public static Image getImageRight(float width, float height, String url)
            throws Exception {
        Image image = getImage(width, height, url);
        image.setAlignment(Image.RIGHT);
        return image;
    }

    public static PdfPTable getTableNoBorder(float[] floats) {
        PdfPTable table = getTable(floats);
        return table;
    }

    public static PdfPTable getTableNoBorderCenter(float[] floats) {
        PdfPTable table = getTableCenter(floats);
        return table;
    }

    public static PdfPCell getCellNoBorder() {
        PdfPCell cell = getCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setSpaceCharRatio(0f);
        return cell;
    }

    public static PdfPCell getCellNoBorderCenter() {
        PdfPCell cell = getCellCenter();
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

}
