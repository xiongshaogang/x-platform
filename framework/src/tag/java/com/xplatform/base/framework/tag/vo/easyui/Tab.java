package com.xplatform.base.framework.tag.vo.easyui;

/**
 * TAB模型
 * 
 * @author 张代浩
 * 
 */
public class Tab {
	private String href;//选项卡请求地址
	private boolean iframe;//是否是iframe
	private String id;//选项卡唯一ID
	private String title;//标题
	private String icon = "icon-default";//图标
	private String width;//宽度
	private String height;//高度
	private boolean cache = false;//是否打开缓冲如为TRUE则切换选项卡会再次发送请求
	private String content; //直接书写html内容,支持写一个js方法然后返回内容
	private boolean closable = false;//是否带关闭按钮
	private boolean aysn = true;//是否是异步加载,为true则点击页签才加载,为false为进主页面就加载该页签

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public boolean isIframe() {
		return iframe;
	}

	public void setIframe(boolean iframe) {
		this.iframe = iframe;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public boolean isCache() {
		return cache;
	}

	public void setCache(boolean cache) {
		this.cache = cache;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isClosable() {
		return closable;
	}

	public void setClosable(boolean closable) {
		this.closable = closable;
	}

	public boolean isAysn() {
		return aysn;
	}

	public void setAysn(boolean aysn) {
		this.aysn = aysn;
	}

}
