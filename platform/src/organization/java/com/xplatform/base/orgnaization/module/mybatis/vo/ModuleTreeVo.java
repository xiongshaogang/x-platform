package com.xplatform.base.orgnaization.module.mybatis.vo;

import com.xplatform.base.framework.mybatis.entity.IdEntity;




public class ModuleTreeVo extends IdEntity {
	 /** 
	  * serialVersionUID
	  */
	private static final long serialVersionUID = 1L;
	private String name;
	private String code;
	private String iconCls;
	private String url;
	private String checked;
	private String parentId;
	private String isLeaf;
	private int level;
	private String isIframe;
	private String phoneShow;
	private int orderby;
	private String subsystem;
	private String subSystemName;
	private String resourceIds; //资源操作权限   treegrid权限设置使用
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getIsIframe() {
		return isIframe;
	}
	public void setIsIframe(String isIframe) {
		this.isIframe = isIframe;
	}
	public int getOrderby() {
		return orderby;
	}
	public void setOrderby(int orderby) {
		this.orderby = orderby;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSubsystem() {
		return subsystem;
	}
	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}
	public String getResourceIds() {
		return resourceIds;
	}
	public void setResourceIds(String resourceIds) {
		this.resourceIds = resourceIds;
	}
	public String getSubSystemName() {
		return subSystemName;
	}
	public void setSubSystemName(String subSystemName) {
		this.subSystemName = subSystemName;
	}
	public String getPhoneShow() {
		return phoneShow;
	}
	public void setPhoneShow(String phoneShow) {
		this.phoneShow = phoneShow;
	}
	
}
