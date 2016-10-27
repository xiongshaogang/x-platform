package com.xplatform.base.framework.tag.core.easyui;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * description : 通用选择字段标签
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年8月4日 上午9:56:41
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年8月4日 上午9:56:41
 *
*/

public class CommonSelectFieldTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private String field; //列关联属性
	private String hidden="true";//是否显示该列
	private String width="1"; //列宽度
	private String title;//列标题
	private String query="false"; //是否作为查询字段
	private String queryMode = "single";//字段查询模式：single单字段查询；group范围查询
	private String queryInputType = "text";//查询框输入类型

	/** 扩展查询时input、select等元素上的data-options属性,用@@分割不同参数,冒号后面的值,比如字符串,符号,布尔值不需要加单引号或双引号
	 * 形如<t:dgCol ... queryExParams="groupSeparator:,@@decimalSeparator:.@@precision:2" queryInputType="Number"/>
	**/
	private String queryExParams;
	private String backField;//标识该列的数据返回到父页面哪个input中(input的name属性去匹配),不填写就表示该列不作为返回字段
	private String dictCode;//数据字典类型code(作为查询框,回显等用途的数据来源)

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		Tag t = findAncestorWithClass(this, CommonSelectTag.class);
		CommonSelectTag parent = (CommonSelectTag) t;
		parent.setColumn(title, field, width, hidden, query, queryMode, queryInputType, queryExParams, backField,
				dictCode);
		return EVAL_PAGE;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getHidden() {
		return hidden;
	}

	public void setHidden(String hidden) {
		this.hidden = hidden;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQueryMode() {
		return queryMode;
	}

	public void setQueryMode(String queryMode) {
		this.queryMode = queryMode;
	}

	public String getQueryInputType() {
		return queryInputType;
	}

	public void setQueryInputType(String queryInputType) {
		this.queryInputType = queryInputType;
	}

	public String getQueryExParams() {
		return queryExParams;
	}

	public void setQueryExParams(String queryExParams) {
		this.queryExParams = queryExParams;
	}

	public String getBackField() {
		return backField;
	}

	public void setBackField(String backField) {
		this.backField = backField;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

}
