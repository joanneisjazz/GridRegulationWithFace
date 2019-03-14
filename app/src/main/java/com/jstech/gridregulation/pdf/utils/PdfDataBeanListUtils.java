package com.jstech.gridregulation.pdf.utils;


import com.jstech.gridregulation.pdf.common.PdfDataType;
import com.jstech.gridregulation.pdf.model.PdfDataBean;

import java.util.ArrayList;
import java.util.List;

public class PdfDataBeanListUtils {

	private List<PdfDataBean> pdfDataBeanList = new ArrayList<PdfDataBean>();

	public List<PdfDataBean> getPdfDataBeanList() {
		return pdfDataBeanList;
	}

	public void setPdfDataBeanList(List<PdfDataBean> pdfDataBeanList) {
		this.pdfDataBeanList = pdfDataBeanList;
	}

	private PdfDataBean getPdbP() {
		return new PdfDataBean(PdfDataType.PARAGRAPH);
	}

	private PdfDataBean getPdbPC() {
		PdfDataBean bean = new PdfDataBean(PdfDataType.PARAGRAPH_CENTER);
		return bean;
	}

	private PdfDataBean getPdbPR() {
		PdfDataBean bean = new PdfDataBean(PdfDataType.PARAGRAPH_RIGHT);
		return bean;
	}

	private PdfDataBean getPdbT(String text) {
		return new PdfDataBean(PdfDataType.TEXT, text);
	}

	private PdfDataBean getPdbTB(String text) {
		return new PdfDataBean(PdfDataType.TEXT_BOLD, text);
	}

	private PdfDataBean getPdbTB1_5(String text) {
		return new PdfDataBean(PdfDataType.TEXT_BOLD_1_5, text);
	}

	private PdfDataBean getPdbC() {
		return new PdfDataBean(PdfDataType.CELL);
	}

	private PdfDataBean getPdbCC() {
		return new PdfDataBean(PdfDataType.CELL_CENTER);
	}

	private PdfDataBean getPdbCxy(List<Object> list) {
		return new PdfDataBean(PdfDataType.CELL_X_Y, list);
	}

	private PdfDataBean getPdbCxyC(List<Object> list) {
		return new PdfDataBean(PdfDataType.CELL_X_Y_CENTER, list);
	}

	private PdfDataBean getPdbIwh(List<Object> list) {
		return new PdfDataBean(PdfDataType.IMAGE_W_H, list);
	}

	private PdfDataBean getPdbIwhC(List<Object> list) {
		return new PdfDataBean(PdfDataType.IMAGE_W_H_CENTER, list);
	}

	private PdfDataBean getPdbIwhR(List<Object> list) {
		return new PdfDataBean(PdfDataType.IMAGE_W_H_RIGHT, list);
	}

	private PdfDataBean getPdbTaNb() {
		return new PdfDataBean(PdfDataType.TABLE_NO_BORDER);
	}

	private PdfDataBean getPdbTaNbC() {
		return new PdfDataBean(PdfDataType.TABLE_NO_BORDER_CENTER);
	}

	private PdfDataBean getPdbCNb() {
		return new PdfDataBean(PdfDataType.CELL_NO_BORDER);
	}

	private PdfDataBean getPdbCNbC() {
		return new PdfDataBean(PdfDataType.CELL_NO_BORDER_CENTER);
	}

	/** 添加空白段落 */
	public PdfDataBeanListUtils add() {
		PdfDataBean bean = new PdfDataBean(PdfDataType.PARAGRAPH_N);
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加PdfDataBean */
	public PdfDataBeanListUtils add(PdfDataBean bean) {
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加普通文本 */
	public PdfDataBeanListUtils add(String text) {
		PdfDataBean bean = getPdbP();
		bean.setData(getPdbT(text));
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加居中普通文本 */
	public PdfDataBeanListUtils addCenter(String text) {
		PdfDataBean bean = getPdbPC();
		bean.setData(getPdbT(text));
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加加粗文本 */
	public PdfDataBeanListUtils addBold(String text) {
		PdfDataBean bean = getPdbP();
		bean.setData(getPdbTB(text));
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加居中加粗文本 */
	public PdfDataBeanListUtils addCenterBold(String text) {
		PdfDataBean bean = getPdbP();
		bean.setData(getPdbTB(text));
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加居中加粗1.5倍文本 */
	public PdfDataBeanListUtils addCenterBold1_5(String text) {
		PdfDataBean bean = getPdbPC();
		bean.setData(getPdbTB1_5(text));
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加前边加粗后边普通拼接文本 */
	public PdfDataBeanListUtils addTB_T(String textBold, String text) {
		PdfDataBean bean = getPdbP();
		List<PdfDataBean> list = new ArrayList<PdfDataBean>();
		list.add(getPdbTB(textBold));
		list.add(getPdbT(text));
		bean.setData(list);
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加普通文本Cell */
	public PdfDataBeanListUtils addCell(String str) {
		PdfDataBean bean = getPdbC();
		PdfDataBean bean1 = getPdbP();
		bean1.setData(getPdbT(str));
		bean.setData(bean1);
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加居中普通文本Cell */
	public PdfDataBeanListUtils addCellCenter(String str) {
		PdfDataBean bean = getPdbCC();
		PdfDataBean bean1 = getPdbPC();
		bean1.setData(getPdbT(str));
		bean.setData(bean1);
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加加粗文本Cell */
	public PdfDataBeanListUtils addCellBold(String str) {
		PdfDataBean bean = getPdbC();
		PdfDataBean bean1 = getPdbP();
		bean1.setData(getPdbTB(str));
		bean.setData(bean1);
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加居中加粗文本Cell */
	public PdfDataBeanListUtils addCellCenterBold(String str) {
		PdfDataBean bean = getPdbCC();
		PdfDataBean bean1 = getPdbPC();
		bean1.setData(getPdbTB(str));
		bean.setData(bean1);
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加普通文本Cell_x_y */
	public PdfDataBeanListUtils addCell(int x, int y, String str) {
		List<Object> list = new ArrayList<Object>();
		list.add(x);
		list.add(y);
		PdfDataBean bean1 = getPdbP();
		bean1.setData(getPdbT(str));
		list.add(bean1);
		PdfDataBean bean = getPdbCxy(list);
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加居中普通文本Cell_x_y */
	public PdfDataBeanListUtils addCellCenter(int x, int y, String str) {
		List<Object> list = new ArrayList<Object>();
		list.add(x);
		list.add(y);
		PdfDataBean bean1 = getPdbPC();
		bean1.setData(getPdbT(str));
		list.add(bean1);
		PdfDataBean bean = getPdbCxyC(list);
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加加粗文本Cell_x_y */
	public PdfDataBeanListUtils addCellBold(int x, int y, String str) {
		List<Object> list = new ArrayList<Object>();
		list.add(x);
		list.add(y);
		PdfDataBean bean1 = getPdbP();
		bean1.setData(getPdbTB(str));
		list.add(bean1);
		PdfDataBean bean = getPdbCxy(list);
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加居中加粗文本Cell_x_y */
	public PdfDataBeanListUtils addCellCenterBold(int x, int y, String str) {
		List<Object> list = new ArrayList<Object>();
		list.add(x);
		list.add(y);
		PdfDataBean bean1 = getPdbPC();
		bean1.setData(getPdbTB(str));
		list.add(bean1);
		PdfDataBean bean = getPdbCxyC(list);
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加多张左右对称居中限定宽高的图片，以表格的方式 */
	public PdfDataBeanListUtils addImageSymmetryT(float width, float height,
			String[] urls) {
		PdfDataBean bean = getPdbTaNbC();
		List<Object> list = new ArrayList<Object>();
		list.add(new float[] { 1f, 1f });
		List<PdfDataBean> list1 = new ArrayList<PdfDataBean>();
		for (int i = 0; i < urls.length; i++) {
			PdfDataBean bean1 = getPdbCNbC();
			List<Object> list2 = new ArrayList<Object>();
			list2.add(width);
			list2.add(height);
			list2.add(urls[i]);
			bean1.setData(getPdbIwhC(list2));
			list1.add(bean1);
		}
		if (1 == (urls.length % 2)) {
			PdfDataBean bean2 = getPdbCNbC();
			bean2.setData("");
			list1.add(bean2);
		}
		list.add(list1);
		bean.setData(list);
		this.pdfDataBeanList.add(bean);
		return this;
	}

	/** 添加签名，多张左右对称两边对齐限定宽高的图片，以表格的方式 */
	public PdfDataBeanListUtils addImageSignatureT(float width, float height,
			String str1, String[] urls1, String str2, String[] urls2) {
		PdfDataBean bean = getPdbTaNb();
		List<Object> list = new ArrayList<Object>();
		list.add(new float[] { 1f, 1f });
		List<PdfDataBean> list1 = new ArrayList<PdfDataBean>();
		PdfDataBean bean1 = getPdbCNb();
		PdfDataBean bean2 = getPdbP();
		bean2.setData(getPdbTB(str1));
		bean1.setData(bean2);
		list1.add(bean1);
		PdfDataBean bean3 = getPdbCNb();
		PdfDataBean bean4 = getPdbPR();
		bean4.setData(getPdbTB(str2));
		bean3.setData(bean4);
		list1.add(bean3);
		{
			PdfDataBean bean5 = getPdbCNb();
			PdfDataBean bean6 = getPdbTaNb();
			List<Object> list2 = new ArrayList<Object>();
			float[] f6 = new float[] { 1f, 1f, 1f };
			list2.add(f6);
			List<PdfDataBean> list3 = new ArrayList<PdfDataBean>();
			for (String url1 : urls1) {
				PdfDataBean bean7 = getPdbCNb();
				List<Object> listI = new ArrayList<Object>();
				listI.add(width);
				listI.add(height);
				listI.add(url1);
				bean7.setData(getPdbIwh(listI));
				list3.add(bean7);
			}
			int j = f6.length - list3.size() % f6.length;
			for (int i = 0; i < j; i++) {
				PdfDataBean bean7 = getPdbCNb();
				bean7.setData("");
				list3.add(bean7);
			}
			list2.add(list3);
			bean6.setData(list2);
			bean5.setData(bean6);
			list1.add(bean5);
		}
		{
			PdfDataBean bean5 = getPdbCNb();
			PdfDataBean bean6 = getPdbTaNb();
			List<Object> list2 = new ArrayList<Object>();
			float[] f6 = new float[] { 1f, 1f, 1f };
			list2.add(f6);
			List<PdfDataBean> list3 = new ArrayList<PdfDataBean>();
			int x = urls2.length / f6.length;
			int y = f6.length - urls2.length % f6.length;
			for (String url2 : urls2) {
				PdfDataBean bean7 = getPdbCNb();
				List<Object> listI = new ArrayList<Object>();
				listI.add(width);
				listI.add(height);
				listI.add(url2);
				bean7.setData(getPdbIwhR(listI));
				if (x == list3.size() / f6.length) {
					for (int i = 0; i < y; i++) {
						PdfDataBean bean7i = getPdbCNb();
						bean7i.setData("");
						list3.add(bean7i);
					}
					x = 0;
				}
				list3.add(bean7);
			}
			list2.add(list3);
			bean6.setData(list2);
			bean5.setData(bean6);
			list1.add(bean5);
		}
		list.add(list1);
		bean.setData(list);
		this.pdfDataBeanList.add(bean);
		return this;
	}

}
