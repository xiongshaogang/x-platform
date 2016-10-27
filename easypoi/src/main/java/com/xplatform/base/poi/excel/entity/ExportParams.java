package com.xplatform.base.poi.excel.entity;

import org.apache.poi.hssf.util.HSSFColor;

/**
 * Excel 导出参数
 * 
 * @author jueyue
 * @version 1.0 2013年8月24日
 */
public class ExportParams extends ExcelBaseParams {

	public ExportParams() {

	}

	public ExportParams(String title, String sheetName) {
		this.title = title;
		this.sheetName = sheetName;
	}

	public ExportParams(String title, String secondTitle, String sheetName) {
		this.title = title;
		this.secondTitle = secondTitle;
		this.sheetName = sheetName;
	}

	/**
	 * 第一行大标题名称
	 */
	private String title;
	/**
	 * 第一行大标题高度
	 */
	private short titleHeight = 20;
	/**
	 * 第一行大标题大小
	 */
	private short titleFontSize = 24;
	/**
	 * 第二行标题名称
	 */
	private String secondTitle;
	/**
	 * 第二行标题高度
	 */
	private short secondTitleHeight = 8;
	/**
	 * 第二行标题大小
	 */
	private short secondTitleFontSize ;
	/**
	 * sheet名称
	 */
	private String sheetName;
	/**
	 * 配置了注解,但依然过滤不进行导入/出的属性
	 */
	private String[] exclusions;
	/**
	 * 第一行大标题底色(例如:HSSFColor.WHITE.index 默认)
	 */
	private short color = HSSFColor.WHITE.index;
	/**
	 * 第二行标题底色(例如:HSSFColor.WHITE.index 默认)
	 */
	private short secondColor = HSSFColor.WHITE.index;
	/**
	 * 表头属性行的颜色(例如:HSSFColor.SKY_BLUE.index 默认)
	 */
	private short headerColor = HSSFColor.SKY_BLUE.index;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public short getColor() {
		return color;
	}

	public void setColor(short color) {
		this.color = color;
	}

	public String[] getExclusions() {
		return exclusions;
	}

	public void setExclusions(String[] exclusions) {
		this.exclusions = exclusions;
	}

	public String getSecondTitle() {
		return secondTitle;
	}

	public void setSecondTitle(String secondTitle) {
		this.secondTitle = secondTitle;
	}

	public short getHeaderColor() {
		return headerColor;
	}

	public void setHeaderColor(short headerColor) {
		this.headerColor = headerColor;
	}

	public short getTitleHeight() {
		return (short) (titleHeight * 50);
	}

	public void setTitleHeight(short titleHeight) {
		this.titleHeight = titleHeight;
	}

	public short getSecondTitleHeight() {
		return (short) (secondTitleHeight * 50);
	}

	public void setSecondTitleHeight(short secondTitleHeight) {
		this.secondTitleHeight = secondTitleHeight;
	}

	public short getTitleFontSize() {
		return titleFontSize;
	}

	public void setTitleFontSize(short titleFontSize) {
		this.titleFontSize = titleFontSize;
	}

	public short getSecondTitleFontSize() {
		return secondTitleFontSize;
	}

	public void setSecondTitleFontSize(short secondTitleFontSize) {
		this.secondTitleFontSize = secondTitleFontSize;
	}

	public short getSecondColor() {
		return secondColor;
	}

	public void setSecondColor(short secondColor) {
		this.secondColor = secondColor;
	}
	
}
