package com.jstech.gridregulation.pdf.model;


import com.jstech.gridregulation.pdf.common.PdfDataType;

public class PdfDataBean {

	private PdfDataType type;
	private Object data;

	public PdfDataBean(PdfDataType type) {
		super();
		this.type = type;
	}

	public PdfDataBean(PdfDataType type, Object data) {
		super();
		this.type = type;
		this.data = data;
	}

	public PdfDataType getType() {
		return type;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
