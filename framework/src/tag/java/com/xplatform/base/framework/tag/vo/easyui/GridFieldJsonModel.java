/**
 * Copyright (c) 2014
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.xplatform.base.framework.tag.vo.easyui;

/**
 * description : 用于通用选择自定义标签传递数据的模型类
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月31日 上午11:34:52
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年7月31日 上午11:34:52 
 *
*/
public class GridFieldJsonModel {
	private String field;
	private String hidden = "true";
	private String width = "1";
	private String title;
	private String query = "false";
	private String queryMode = "single";
	private String queryInputType = "text";
	private String queryExParams;
	private String backField;
	private String dictCode;
	private String halign;
	private String align;

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

	public String getHalign() {
		return halign;
	}

	public void setHalign(String halign) {
		this.halign = halign;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

}
