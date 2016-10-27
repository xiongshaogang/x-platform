package com.xplatform.base.framework.tag.core.easyui;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.poi.hssf.util.HSSFColor;

/**
 * description : 列表提供excel导入所需参数标签
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

public class DataGridImportParamsTag extends TagSupport {

	/** ImportParams配置的参数 **/
	private Integer titleRows = 0;//标题行数,默认0
	private Integer headRows = 1;//表头属性行数,默认1
	private Integer startRows = 0;//字段真正值和列标题之间的距离 默认0(暂未使用)
	private Integer startCell = 0;//需导入数据的开始列
	private Integer endCell = 0;//需导入数据的结束列
	private Integer keyIndex = -1;//主键设置,如何这个cell没有值,就跳过 或者认为这个是list的下面的值
	private Integer sheetNum = 1;//上传表格需要读取的sheet数量,默认为1
	private boolean needSave = false;//是否需要保存上传的Excel,默认为false

	/** ExcelBaseParams配置的参数 **/
	private String dataHanlder;//数据处理接口,以此为主,replace,format都在这后面
	private String needHandlerFields;//需要用自定义Handler类处理的字段,形如(username,age,sex)

	private String templateCode;//下载模板code
	private String name;//导入结果集名称(用于设、取ehcache缓存中数据)
	private String submitUrl;//自行处理结果集的请求url(比如拿到结果集保存);
	private String entityClass;//注解配置的实体

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		Tag t = findAncestorWithClass(this, DataGridTag.class);
		DataGridTag parent = (DataGridTag) t;
		parent.setImportParam(titleRows, headRows, startRows, startCell, endCell, keyIndex, sheetNum, needSave,
				dataHanlder, needHandlerFields, templateCode, name, submitUrl, entityClass);
		return EVAL_PAGE;
	}

	public Integer getTitleRows() {
		return titleRows;
	}

	public void setTitleRows(Integer titleRows) {
		this.titleRows = titleRows;
	}

	public Integer getHeadRows() {
		return headRows;
	}

	public void setHeadRows(Integer headRows) {
		this.headRows = headRows;
	}

	public Integer getStartRows() {
		return startRows;
	}

	public void setStartRows(Integer startRows) {
		this.startRows = startRows;
	}

	public Integer getStartCell() {
		return startCell;
	}

	public void setStartCell(Integer startCell) {
		this.startCell = startCell;
	}

	public Integer getEndCell() {
		return endCell;
	}

	public void setEndCell(Integer endCell) {
		this.endCell = endCell;
	}

	public Integer getKeyIndex() {
		return keyIndex;
	}

	public void setKeyIndex(Integer keyIndex) {
		this.keyIndex = keyIndex;
	}

	public Integer getSheetNum() {
		return sheetNum;
	}

	public void setSheetNum(Integer sheetNum) {
		this.sheetNum = sheetNum;
	}

	public boolean isNeedSave() {
		return needSave;
	}

	public void setNeedSave(boolean needSave) {
		this.needSave = needSave;
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

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubmitUrl() {
		return submitUrl;
	}

	public void setSubmitUrl(String submitUrl) {
		this.submitUrl = submitUrl;
	}

	public String getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(String entityClass) {
		this.entityClass = entityClass;
	}

}
