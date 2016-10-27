/**
 * Copyright (c) 2014
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.xplatform.base.system.attachment.mybatis.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * description : 资料列表/缩略图 视图VO
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月3日 上午11:37:53
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiaqiang 2014年7月3日 上午11:37:53
 *
 */
public class AttachVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3971263815110864285L;
	private String id;// 注意,分类目录 和 附件 此字段分别是存自己的Id
	private String name; // 文件名(如果是分类目录就是分类目录名,如果是附件就是附件名,通过mybatis融合2类查询)

	private String businessKey;// 关联的业务主键
	private String businessType;// 关联的业务标识(自己取,比如可以取实体名"DataEntity")
	private String businessExtra;// 同一业务记录下区分字段(比如同一业务id关联7个附件,某次需要取'aType'标识的3条,某次需要取'bType'标识的4条)
	private String otherKey;// 用于本资料记录为从表产生,但是需要关联主表的情况,本字段记录主表ID(或者当做一个查询条件用于多种用途,增加附件灵活性)
	private String otherKeyType;// otherKey的来源类型(一般取实体名比如:DataEntity)
	private Integer fileFlag;// 如果是0表示分类目录,1表示附件
	private Integer isPublic;// 是否公共区(文件夹记录有该值)
	private String typeName; // 分类目录名称(只有附件有该值)
	private String code;// 目录code(文件夹记录有该值)
	private String orgId;// 文件夹归属机构

	private Long attachSize;// 字节大小
	private String attachSizeStr; // 字符化后大小(字符串 如:32MB,1GB)
	private String attachType;// 人为附件分类(如word,excel,ppt...)
	private String iconType;// 图标分类(如img,ppt,excel...)
	private String attachContentType;// 附件原始类型(如image/png)
	private String storageType;// 存储位置(0-本地,1-FTP服务器,2-和彩云)
	private String ext;// 后缀名
	private String remark; // 附件描述
	private String relativePath;// 相对路径
	private String absolutePath;// 绝对路径
	private String thumbnailAbPath;// 缩略图绝对路径
	private String thumbnailRePath;// 缩略图相对路径
	private String parentTypeId;// 父分类目录Id(只有文件夹类型有该值,以免每次请求还要去查询该值)
	private Date createTime;// 创建时间
	private Date updateTime;// 修改时间
	private String createUserName;// 创建人名称
	private String createUserId;// 创建人Id
	private String updateUserName;// 上次修改人Id
	private String updateUserId;// 上次修改人Id
	// (0为无权限,1为有权限,权限标识均为该取值)
	private String downloadAuthority;// 下载权限
	private String uploadAuthority;// 上传权限
	private String renameAuthority;// 重命名权限
	private String deleteAuthority;// 删除权限
	private String moveAuthority;// 移动权限
	private String createFolderAuthority;// 创建文件夹权限
	private String folderAuthority;// 文件夹授权权限

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getAttachSizeStr() {
		return attachSizeStr;
	}

	public void setAttachSizeStr(String attachSizeStr) {
		this.attachSizeStr = attachSizeStr;
	}

	public String getAttachType() {
		return attachType;
	}

	public void setAttachType(String attachType) {
		this.attachType = attachType;
	}

	public String getAttachContentType() {
		return attachContentType;
	}

	public void setAttachContentType(String attachContentType) {
		this.attachContentType = attachContentType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getFileFlag() {
		return fileFlag;
	}

	public void setFileFlag(Integer fileFlag) {
		this.fileFlag = fileFlag;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentTypeId() {
		return parentTypeId;
	}

	public void setParentTypeId(String parentTypeId) {
		this.parentTypeId = parentTypeId;
	}

	public String getThumbnailAbPath() {
		return thumbnailAbPath;
	}

	public void setThumbnailAbPath(String thumbnailAbPath) {
		this.thumbnailAbPath = thumbnailAbPath;
	}

	public String getThumbnailRePath() {
		return thumbnailRePath;
	}

	public void setThumbnailRePath(String thumbnailRePath) {
		this.thumbnailRePath = thumbnailRePath;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
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

	public Long getAttachSize() {
		return attachSize;
	}

	public void setAttachSize(Long attachSize) {
		this.attachSize = attachSize;
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

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getIconType() {
		return iconType;
	}

	public void setIconType(String iconType) {
		this.iconType = iconType;
	}

	public String getDownloadAuthority() {
		return downloadAuthority;
	}

	public void setDownloadAuthority(String downloadAuthority) {
		this.downloadAuthority = downloadAuthority;
	}

	public String getUploadAuthority() {
		return uploadAuthority;
	}

	public void setUploadAuthority(String uploadAuthority) {
		this.uploadAuthority = uploadAuthority;
	}

	public String getRenameAuthority() {
		return renameAuthority;
	}

	public void setRenameAuthority(String renameAuthority) {
		this.renameAuthority = renameAuthority;
	}

	public String getDeleteAuthority() {
		return deleteAuthority;
	}

	public void setDeleteAuthority(String deleteAuthority) {
		this.deleteAuthority = deleteAuthority;
	}

	public String getMoveAuthority() {
		return moveAuthority;
	}

	public void setMoveAuthority(String moveAuthority) {
		this.moveAuthority = moveAuthority;
	}

	public String getCreateFolderAuthority() {
		return createFolderAuthority;
	}

	public void setCreateFolderAuthority(String createFolderAuthority) {
		this.createFolderAuthority = createFolderAuthority;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	public String getFolderAuthority() {
		return folderAuthority;
	}

	public void setFolderAuthority(String folderAuthority) {
		this.folderAuthority = folderAuthority;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}
