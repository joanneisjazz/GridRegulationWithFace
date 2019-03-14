package com.jstech.gridregulation.pdf.common;

public enum PdfDataType {
	/** 段落 */
	PARAGRAPH
	/** 段落，空行 */
	, PARAGRAPH_N
	/** 段落，居中 */
	, PARAGRAPH_CENTER
	/** 段落，右对齐 */
	, PARAGRAPH_RIGHT
	/** 文本 */
	, TEXT
	/** 文本，空文本块 */
	, TEXT_N
	/** 文本，加粗 */
	, TEXT_BOLD
	/** 文本，加粗，1.5倍 */
	, TEXT_BOLD_1_5
	/** 表格 */
	, TABLE
	/** 单元格 */
	, CELL
	/** 单元格，居中 */
	, CELL_CENTER
	/** 单元格，x行y列 */
	, CELL_X_Y
	/** 单元格，x行y列，居中 */
	, CELL_X_Y_CENTER
	/** 图片，宽w高h */
	, IMAGE_W_H
	/** 图片，宽w高h，居中 */
	, IMAGE_W_H_CENTER
	/** 图片，宽w高h，居右 */
	, IMAGE_W_H_RIGHT
	/** 无边框表格 */
	, TABLE_NO_BORDER
	/** 无边框表格，居中 */
	, TABLE_NO_BORDER_CENTER
	/** 无边框单元格 */
	, CELL_NO_BORDER
	/** 无边框单元格，居中 */
	, CELL_NO_BORDER_CENTER
}
