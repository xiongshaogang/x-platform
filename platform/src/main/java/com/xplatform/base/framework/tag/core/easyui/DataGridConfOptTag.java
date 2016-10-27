package com.xplatform.base.framework.tag.core.easyui;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 
 * 类描述：列表操作项处理标签
 * 
 * 张代浩
 * @date： 日期：2012-12-7 时间：上午10:17:45
 * @version 1.0
 */
public class DataGridConfOptTag extends TagSupport {
	protected String url;
	protected String title;
	private String message;//询问链接的提示语
	private String exp;//判断链接是否显示的表达式
	private String operationCode;//按钮的操作Code
	private String icon = "glyphicon glyphicon-ok icon-color";//按钮图标
	private String exParams;//操作按钮除以上主要参数之外额外的参数,json格式
	private Boolean isShowInViewPage=false;//是否在只读页面显示该按钮

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		Tag t = findAncestorWithClass(this, DataGridTag.class);
		DataGridTag parent = (DataGridTag) t;
		parent.setConfUrl(url, title, message, exp, operationCode, icon, exParams,isShowInViewPage);
		return EVAL_PAGE;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getExParams() {
		return exParams;
	}

	public void setExParams(String exParams) {
		this.exParams = exParams;
	}

	public String getUrl() {
		return url;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public String getExp() {
		return exp;
	}

	public String getOperationCode() {
		return operationCode;
	}

	public Boolean getIsShowInViewPage() {
		return isShowInViewPage;
	}

	public void setIsShowInViewPage(Boolean isShowInViewPage) {
		this.isShowInViewPage = isShowInViewPage;
	}

}
