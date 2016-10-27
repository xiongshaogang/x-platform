package com.xplatform.base.framework.tag.core.easyui;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 
 * description :树自定义函数操作项处理标签
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月17日 下午4:19:12
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月17日 下午4:19:12
 *
 */
public class TreeFunOptTag extends TagSupport {
	private String title;//按钮标题
	private String url; //请求的url
	private String exp;//url中的表达式
	private String funname;//js函数名称
	private String icon; //按钮样式
	/** 按钮类型,对应OptTypeDirection枚举值 
	 *	TreeCustom 代表用户自定义功能按钮,非预设类型按钮
	 **/
	private String buttonType = "TreeCustom";
	private String operationCode;//按钮的操作Code,控制权限
	/** 有弹出页的才会用到 **/
	private String width; //宽度
	private String height; //高度
	private String preinstallWidth;//预设宽度
	private String callback;//回调函数
	private String message;//确认框中的提示语句

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		Tag t = findAncestorWithClass(this, TreeTag.class);
		TreeTag parent = (TreeTag) t;
		parent.setFunUrl(title, url, exp, funname, icon, buttonType,
				operationCode, width, height, preinstallWidth, callback,
				message);
		return EVAL_PAGE;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getFunname() {
		return funname;
	}

	public void setFunname(String funname) {
		this.funname = funname;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

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

	public String getPreinstallWidth() {
		return preinstallWidth;
	}

	public void setPreinstallWidth(String preinstallWidth) {
		this.preinstallWidth = preinstallWidth;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getButtonType() {
		return buttonType;
	}

	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}

}
