package com.xplatform.base.framework.tag.core.easyui;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.poi.hssf.util.HSSFColor;

/**
 * description : 列表提供导出所需参数标签
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年11月21日 下午2:35:19
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年11月21日 下午2:35:19
 *
*/

public class DataGridExportParamsTag extends TagSupport {
	/** ExportParams配置的参数 **/
	private String title;//第一行大标题名称
	private short titleHeight = 20;//第一行大标题高度(默认20)
	private short titleFontSize = 24;//第一行大标题大小(默认24)
	private String secondTitle;//第二行标题名称
	private short secondTitleHeight = 8;//第二行标题高度(默认8)
	private short secondTitleFontSize;//第二行标题大小
	private String sheetName;//sheet名称
	private String exclusions;//配置了注解,但依然过滤不进行导入/出的属性,形如(username,age,sex)
	private short color = HSSFColor.WHITE.index;//第一行大标题底色(值类型为short,需查api获得颜色对应,例如:9对应HSSFColor.WHITE.index 默认)
	private short secondColor = HSSFColor.WHITE.index;//第二行标题底色(例如:9对应HSSFColor.WHITE.index 默认)
	private short headerColor = HSSFColor.SKY_BLUE.index;//表头属性行的颜色(例如:40对应HSSFColor.SKY_BLUE.index 默认)

	/** ExcelBaseParams配置的参数 **/
	private String dataHanlder;//数据处理接口,以此为主,replace,format都在这后面
	private String needHandlerFields;//需要用自定义Handler类处理的字段,形如(username,age,sex)

	private String entityClass;//注解配置的实体
	private String fileName = "Excel导出.xls";//导出的文件名
	private String exportServer = "com.xplatform.base.poi.excel.export.ExcelExportServer";//使用的导出方法类(可使用自定义类,完成更灵活的配置)

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		Tag t = findAncestorWithClass(this, DataGridTag.class);
		DataGridTag parent = (DataGridTag) t;
		parent.setExportParam(title, titleHeight, titleFontSize, secondTitle, secondTitleHeight, secondTitleFontSize,
				sheetName, exclusions, color, headerColor, dataHanlder, needHandlerFields, fileName, exportServer,entityClass);
		return EVAL_PAGE;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public short getTitleHeight() {
		return (short) (titleHeight * 50);
	}

	public void setTitleHeight(short titleHeight) {
		this.titleHeight = titleHeight;
	}

	public short getTitleFontSize() {
		return titleFontSize;
	}

	public void setTitleFontSize(short titleFontSize) {
		this.titleFontSize = titleFontSize;
	}

	public String getSecondTitle() {
		return secondTitle;
	}

	public void setSecondTitle(String secondTitle) {
		this.secondTitle = secondTitle;
	}

	public short getSecondTitleHeight() {
		return (short) (secondTitleHeight * 50);
	}

	public void setSecondTitleHeight(short secondTitleHeight) {
		this.secondTitleHeight = secondTitleHeight;
	}

	public short getSecondTitleFontSize() {
		return secondTitleFontSize;
	}

	public void setSecondTitleFontSize(short secondTitleFontSize) {
		this.secondTitleFontSize = secondTitleFontSize;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getExclusions() {
		return exclusions;
	}

	public void setExclusions(String exclusions) {
		this.exclusions = exclusions;
	}

	public short getColor() {
		return color;
	}

	public void setColor(short color) {
		this.color = color;
	}

	public short getSecondColor() {
		return secondColor;
	}

	public void setSecondColor(short secondColor) {
		this.secondColor = secondColor;
	}

	public short getHeaderColor() {
		return headerColor;
	}

	public void setHeaderColor(short headerColor) {
		this.headerColor = headerColor;
	}

	public String getDataHanlder() {
		return dataHanlder;
	}

	public void setDataHanlder(String dataHanlder) {
		this.dataHanlder = dataHanlder;
	}

	public String getNeedHandlerFields() {
		return needHandlerFields;
	}

	public void setNeedHandlerFields(String needHandlerFields) {
		this.needHandlerFields = needHandlerFields;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getExportServer() {
		return exportServer;
	}

	public void setExportServer(String exportServer) {
		this.exportServer = exportServer;
	}

	public String getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(String entityClass) {
		this.entityClass = entityClass;
	}

}
