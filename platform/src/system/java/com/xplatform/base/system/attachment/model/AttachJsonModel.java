/**
 * Copyright (c) 2014
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.xplatform.base.system.attachment.model;

/**
 * description : 后台返回给jquery file upload组件的files的json数据
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月2日 下午2:11:06
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiaqiang 2014年7月2日 下午2:11:06
 *
 */
public class AttachJsonModel {
	private String id;// 上传后的附件表id
	private String name; // 附件名称(带后缀名)
	private Long size; // 附件大小(byte字节为单位)
	private String sizeStr; // 字符化后大小(字符串 如:32MB,1GB)
	private String ext; // 文件后缀名
	private String iconType; // 图标类型
	private String downloadUrl; // 用于下载的url
	private String thumbnailUrl; // 显示缩略图url(需要系统设置了上传时再存一份缩略图)
	private String deleteUrl; // 用于删除请求的url
	private String deleteType; // 删除的类型(作用未知)
	private String info; // 上传时发生错误的文件,返回的错误提示

	// 业务关联字段(参照AttachEntity)
	private String businessKey;
	private String businessType;
	private String businessExtra;
	private String otherKey;
	private String otherKeyType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	public String getDeleteType() {
		return deleteType;
	}

	public void setDeleteType(String deleteType) {
		this.deleteType = deleteType;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getIconType() {
		return iconType;
	}

	public void setIconType(String iconType) {
		this.iconType = iconType;
	}

	public String getSizeStr() {
		return sizeStr;
	}

	public void setSizeStr(String sizeStr) {
		this.sizeStr = sizeStr;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getBusinessExtra() {
		return businessExtra;
	}

	public void setBusinessExtra(String businessExtra) {
		this.businessExtra = businessExtra;
	}

	public String getOtherKey() {
		return otherKey;
	}

	public void setOtherKey(String otherKey) {
		this.otherKey = otherKey;
	}

	public String getOtherKeyType() {
		return otherKeyType;
	}

	public void setOtherKeyType(String otherKeyType) {
		this.otherKeyType = otherKeyType;
	}

}
