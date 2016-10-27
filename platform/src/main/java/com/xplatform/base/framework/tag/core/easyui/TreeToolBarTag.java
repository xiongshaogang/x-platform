package com.xplatform.base.framework.tag.core.easyui;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 
 * description :树形的toolbar 操作跟查询
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月17日 下午4:10:50
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月17日 下午4:10:50
 *
 */
public class TreeToolBarTag extends TagSupport {
	protected String url;
	protected String title;
	private String exp;//判断链接是否显示的表达式
	private String funname;//自定义函数名称
	private String icon;//图标
	private String onclick;
	private String width;
	private String height;
	private String operationCode;//按钮的操作Code

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		Tag t = findAncestorWithClass(this, TreeTag.class);
		TreeTag parent = (TreeTag) t;
		//		parent.setFunUrl(url, title, icon, exp,onclick, funname,operationCode,width,height);
		return EVAL_PAGE;
	}

}
