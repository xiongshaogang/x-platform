package com.xplatform.base.framework.tag.vo.easyui;

/**
 * 
 * description :
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月17日 下午4:17:35
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- -------------------  -----------------------------------
 * xiehs           2014年5月17日 下午4:17:35
 * xiaqiang		   2014年6月3日 下午18:17:35
 */
public class TreeUrl {
	private String url;//操作链接地址
	private String title;//按钮名称
	private String icon;//按钮图标
	private String value;//传入参数
	private String width;//弹出窗宽度
	private String height;//弹出窗高度
	private OptTypeDirection buttonType;//按钮类型
	private String message;//询问链接的提示语
	private String exp;//判断链接是否显示的表达式
	private String funname;//js函数名称
	private String callback;//回调函数
	private String preinstallWidth;//预设宽度

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public OptTypeDirection getButtonType() {
		return buttonType;
	}

	public void setButtonType(OptTypeDirection buttonType) {
		this.buttonType = buttonType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

}
