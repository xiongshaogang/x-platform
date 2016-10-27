package com.xplatform.base.framework.tag.core.easyui;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.vo.easyui.OptTypeDirection;

/**
 *  @author 张代浩
 * 类描述：列表工具条标签
 */
public class DataGridToolBarTag extends TagSupport {
	private String id="";//按钮id
	protected String url;
	protected String title;
	private String exp;//判断链接是否显示的表达式
	private String funname;//自定义函数名称
	private String icon;//图标
	private String onclick;
	private String width;
	private String height;
	private String operationCode;//按钮的操作Code
	/** 操作栏按钮的操作类型,实质上是为构造不同的js方法
	 *  以下几种取值GridAdd,GridUpdate,GridDetail,GridDelMul,GridCustom
	 */
	private String buttonType;
	private String callback;//回调函数
	private String rowId="id";//行主键field
	private String message;//提示语句
	private Integer preinstallWidth;//预设宽度,有2个值:1和2分别代表预设的1栏宽度和2栏宽度
	private String exParams;//操作按钮除以上主要参数之外额外的参数,json格式

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		Tag t = findAncestorWithClass(this, DataGridTag.class);
		DataGridTag parent = (DataGridTag) t;
		OptTypeDirection optTypeDirection = null;
		if (StringUtil.isEmpty(icon)) {
			if ("deleteALLSelect".equals(funname) || "GridDelMul".equals(buttonType)) {
				icon = "glyphicon glyphicon-trash icon-color";
			} else if ("add".equals(funname) || "GridAdd".equals(buttonType)) {
				icon = "glyphicon glyphicon-plus icon-color";
			} else if ("update".equals(funname) || "GridUpdate".equals(buttonType)) {
				icon = "glyphicon glyphicon-pencil icon-color";
			} else if ("detail".equals(funname) || "GridDetail".equals(buttonType)) {
				icon = "glyphicon glyphicon-search icon-color";
			}
		}

		if ("deleteALLSelect".equals(funname) || "GridDelMul".equals(buttonType)) {
			optTypeDirection = OptTypeDirection.GridDelMul;
		} else if ("add".equals(funname) || "GridAdd".equals(buttonType)) {
			optTypeDirection = OptTypeDirection.GridAdd;
		} else if ("update".equals(funname) || "GridUpdate".equals(buttonType)) {
			optTypeDirection = OptTypeDirection.GridUpdate;
		} else if ("detail".equals(funname) || "GridDetail".equals(buttonType)) {
			optTypeDirection = OptTypeDirection.GridDetail;
		} else if ("GridExcelExport".equals(buttonType)) {
			optTypeDirection = OptTypeDirection.GridExcelExport;
		} else if ("GridExcelImport".equals(buttonType)) {
			optTypeDirection = OptTypeDirection.GridExcelImport;
		} else {
			optTypeDirection = OptTypeDirection.GridCustom;
		}
		parent.setToolbar(url, title, icon, exp, onclick, funname, operationCode, width, height, callback, optTypeDirection, message, preinstallWidth,
				exParams, id, rowId);
		icon = null;
		return EVAL_PAGE;
	}

	public void setFunname(String funname) {
		this.funname = funname;
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

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
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

	public String getButtonType() {
		return buttonType;
	}

	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
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

	public Integer getPreinstallWidth() {
		return preinstallWidth;
	}

	public void setPreinstallWidth(Integer preinstallWidth) {
		this.preinstallWidth = preinstallWidth;
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

	public String getExp() {
		return exp;
	}

	public String getFunname() {
		return funname;
	}

	public String getIcon() {
		return icon;
	}

	public String getOnclick() {
		return onclick;
	}

	public String getOperationCode() {
		return operationCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

}
