/**
 * Copyright (c) 2014
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.xplatform.base.framework.tag.vo.easyui;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * description :需要用到树属性的标签封装父类
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
public class TreeModel extends TagSupport {
	protected String url; //请求url
	protected boolean animate = true; //展开是否有动画效果
	protected boolean checkbox = false; //是否显示复选框
	protected boolean cascadeCheck = false; //是否级联选择
	protected boolean onlyLeafCheck = false;//是否只能子节点被复选
	protected boolean dnd = false; //是否允许拖拽
	protected boolean lines = false; //是否显示树形虚线

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isAnimate() {
		return animate;
	}

	public void setAnimate(boolean animate) {
		this.animate = animate;
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}

	public boolean isCascadeCheck() {
		return cascadeCheck;
	}

	public void setCascadeCheck(boolean cascadeCheck) {
		this.cascadeCheck = cascadeCheck;
	}

	public boolean isOnlyLeafCheck() {
		return onlyLeafCheck;
	}

	public void setOnlyLeafCheck(boolean onlyLeafCheck) {
		this.onlyLeafCheck = onlyLeafCheck;
	}

	public boolean isDnd() {
		return dnd;
	}

	public void setDnd(boolean dnd) {
		this.dnd = dnd;
	}

	public boolean isLines() {
		return lines;
	}

	public void setLines(boolean lines) {
		this.lines = lines;
	}

}
