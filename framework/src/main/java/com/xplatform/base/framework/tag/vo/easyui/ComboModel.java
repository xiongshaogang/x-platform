/**
 * Copyright (c) 2014
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.xplatform.base.framework.tag.vo.easyui;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * description :需要用到combo属性的标签封装父类
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年6月5日 下午3:03:44
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年6月5日 下午3:03:44 
 *
*/
public class ComboModel extends TagSupport {
	protected String id;// ID
	protected String name;//控件名称
	protected String textname;//若表单也需要提交combo的text,则指定本值(相当于表单会同时提交id和text的值,分别由name,textname指定提交参数名)
	protected String width = "192";//宽度
	protected String height = "28";//高度
	protected String panelWidth = "192";//下拉后宽度
	protected String panelHeight;//下拉后高度
	protected boolean editable = false;//定义是否可以直接到文本域中键入文本
	protected boolean multiple = false;//是否多选
	protected String value;
	protected String data; //直接设置json数据
	protected String url;//远程请求url,获取数据
	protected boolean fit = false;//是否适应父容器宽度
	protected boolean disabled = false;//定义是否禁用字段 

	protected String datatype;//验证类型
	protected String sucmsg;//验证成功提示
	protected String nullmsg;//为空提示
	protected String errormsg;//验证错误提示
	protected String ajaxurl;//远程验证url
	protected String uniquemsg;//远程验证出现错误时的提示
	protected String entityName;//远程验证的实体名(带包路径,如com.xplatform.base.system.type.entity.TypeEntity)
	protected String oldValue;//远程验证-处理更新时旧值保留的问题
	protected String ignore;//验证框架中,设置ignore="ignore"则不输入不进行验证

	protected String onShowPanel;//当下拉面板显示的时候触发
	protected String onHidePanel;//当下拉面板隐藏的时候触发
	protected String onChange;//当字段值改变的时候触发

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getPanelWidth() {
		return panelWidth;
	}

	public void setPanelWidth(String panelWidth) {
		this.panelWidth = panelWidth;
	}

	public String getPanelHeight() {
		return panelHeight;
	}

	public void setPanelHeight(String panelHeight) {
		this.panelHeight = panelHeight;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getSucmsg() {
		return sucmsg;
	}

	public void setSucmsg(String sucmsg) {
		this.sucmsg = sucmsg;
	}

	public String getNullmsg() {
		return nullmsg;
	}

	public void setNullmsg(String nullmsg) {
		this.nullmsg = nullmsg;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getAjaxurl() {
		return ajaxurl;
	}

	public void setAjaxurl(String ajaxurl) {
		this.ajaxurl = ajaxurl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOnShowPanel() {
		return onShowPanel;
	}

	public void setOnShowPanel(String onShowPanel) {
		this.onShowPanel = onShowPanel;
	}

	public String getOnHidePanel() {
		return onHidePanel;
	}

	public void setOnHidePanel(String onHidePanel) {
		this.onHidePanel = onHidePanel;
	}

	public String getOnChange() {
		return onChange;
	}

	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}

	public boolean isFit() {
		return fit;
	}

	public void setFit(boolean fit) {
		this.fit = fit;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getUniquemsg() {
		return uniquemsg;
	}

	public void setUniquemsg(String uniquemsg) {
		this.uniquemsg = uniquemsg;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getIgnore() {
		return ignore;
	}

	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}

	public String getTextname() {
		return textname;
	}

	public void setTextname(String textname) {
		this.textname = textname;
	}

}
