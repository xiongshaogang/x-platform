package com.xplatform.base.framework.tag.core.easyui;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.vo.easyui.OptTypeDirection;

/**
 * 
 * 类描述：列表弹出窗操作项处理标签
 * 
 * @author 张代浩
 * @date： 日期：2012-12-7 时间：上午10:17:45
 * @version 1.0
 */
public class DataGridOpenOptTag extends TagSupport {
	protected String url;// 弹出页面地址
	protected String width;// 弹出窗口宽度
	protected String height;// 弹出窗口高度
	protected String title;// 链接标题
	private String exp;// 判断链接是否显示的表达式
	private String operationCode;// 按钮的操作Code
	/**
	 * 按钮类型,规定预设的窗口下的按钮或是新建新的tab页 可用值RowSave、RowDetail、OpenBlank、OpenTab RowSave
	 * : 出现默认保存、关闭按钮 RowDetail: 出现关闭按钮,并且页面的:input输入框都变成只读 OpenBlank: 不出现操作栏
	 * OpenTab: 在新tab页里打开(兼容保留)
	 */
	private String openModel = "RowSave";
	private Integer preinstallWidth;// 预设宽度,有2个值:1和2分别代表预设的1栏宽度和2栏宽度
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
		parent.setOpenUrl(url, title, width, height, exp, operationCode, preinstallWidth, icon, exParams, isShowInViewPage);
		icon=null;
		exParams = null;
		return EVAL_PAGE;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setHeight(String height) {
		this.height = height;
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

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	public void setOpenModel(String openModel) {
		this.openModel = openModel;
	}

	public Integer getPreinstallWidth() {
		return preinstallWidth;
	}

	public void setPreinstallWidth(Integer preinstallWidth) {
		this.preinstallWidth = preinstallWidth;
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

	public String getWidth() {
		return width;
	}

	public String getHeight() {
		return height;
	}

	public String getTitle() {
		return title;
	}

	public String getExp() {
		return exp;
	}

	public String getOperationCode() {
		return operationCode;
	}

	public String getOpenModel() {
		return openModel;
	}

	public Boolean getIsShowInViewPage() {
		return isShowInViewPage;
	}

	public void setIsShowInViewPage(Boolean isShowInViewPage) {
		this.isShowInViewPage = isShowInViewPage;
	}

}
