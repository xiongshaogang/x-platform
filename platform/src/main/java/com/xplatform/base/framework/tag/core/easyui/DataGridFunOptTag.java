package com.xplatform.base.framework.tag.core.easyui;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.xplatform.base.framework.core.util.StringUtil;

/**
 * 
 * 类描述：列表自定义函数操作项处理标签
 * 
 * @author 张代浩
 * @date： 日期：2012-12-7 时间：上午10:17:45
 * @version 1.0
 */
public class DataGridFunOptTag extends TagSupport {
	protected String title;
	private String exp;// 判断链接是否显示的表达式
	private String funname;// 自定义函数名称
	private String operationCode;// 按钮的操作Code
	private String icon;// 按钮图标
	private String exParams;// 操作按钮除以上主要参数之外额外的参数,json格式
	private Boolean isShowInViewPage = false;// 是否在只读页面显示该按钮

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		Tag t = findAncestorWithClass(this, DataGridTag.class);
		DataGridTag parent = (DataGridTag) t;
		if (StringUtil.isEmpty(icon)) {
			if (StringUtil.isNotEmpty(exParams)) {
				if (exParams.indexOf("optFlag:'add'") > -1) {
				} else if (exParams.indexOf("optFlag:'update'") > -1) {
					icon = "glyphicon glyphicon-pencil icon-color";
				} else if (exParams.indexOf("optFlag:'detail'") > -1) {
					icon = "glyphicon glyphicon-search icon-color";
				} else if (exParams.indexOf("optFlag:'delete'") > -1) {
					icon = "glyphicon glyphicon-remove icon-color";
				}
			} else {
				icon = "glyphicon glyphicon-pencil icon-color";
			}
		}
		parent.setFunUrl(title, exp, funname, operationCode, icon, exParams, isShowInViewPage);
		return EVAL_PAGE;
	}

	public void setFunname(String funname) {
		this.funname = funname;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getTitle() {
		return title;
	}

	public String getExp() {
		return exp;
	}

	public String getFunname() {
		return funname;
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
