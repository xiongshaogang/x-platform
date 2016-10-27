package com.xplatform.base.system.attachment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.system.type.entity.TypeEntity;

/**
 * description : 附件表
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月2日 下午4:59:51
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiaqiang 2014年7月2日 下午4:59:51
 *
 */
@Entity
@Table(name = "t_sys_attachment")
@SuppressWarnings("serial")
public class AttachEntity extends OperationEntity implements java.io.Serializable {
	private String srcName; // 原始文件名 (比如上传时是21343243243232.jpg)
	private String attachName; // 文件名(带后缀的 比如abc.jpg)
	private String onlyName; // 文件名(不带后缀的 比如abc)
	private Long attachSize; // 大小(byte字节)
	private String attachSizeStr; // 字符化后大小(字符串 如:32MB,1GB)
	private String attachType;// 人为附件分类(如图片image、视频video)
	private String iconType;// 图标分类(如img,ppt,excel...)
	private String attachContentType; // 附件原始类型(如image/png)
	private String attachRemark; // 附件描述
	private String ext; // 后缀名(如.jpg)
	private String relativePath;// 相对路径
	private String absolutePath;// 绝对路径
	private String thumbnailAbPath;// 缩略图绝对路径(如果是图片类型,或者是需要缩略图的类型,才有此值)
	private String thumbnailRePath;// 缩略图相对路径(如果是图片类型,或者是需要缩略图的类型,才有此值)

	private String MD5;// 通过文件内容计算出的MD5码
	private String isFirstUpload;// 是否首次上传
	private String storageType;// 存储位置(0-本地,1-FTP服务器,2-和彩云)
	private String storageId;// 关联表Id(比如关联的和彩云表的主键)

	private TypeEntity typeEntity; // 归属文件夹
	private String businessKey;// 关联的业务主键
	private String businessType;// 关联的业务标识(自己取,比如可以取实体名"DataEntity")
	private String businessExtra;// 同一业务记录下区分字段(比如同一业务id关联7个附件,某次需要取'aType'标识的3条,某次需要取'bType'标识的4条)
	private String otherKey;// 用于本资料记录为从表产生,但是需要关联主表的情况,本字段记录主表ID(或者当做一个查询条件用于多种用途,增加附件灵活性)
	private String otherKeyType;// otherKey的来源类型(一般取实体名比如:DataEntity)

	@Column(name = "srcName", columnDefinition = "varchar(500)")
	public String getSrcName() {
		return srcName;
	}

	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}

	@Column(name = "attachName", columnDefinition = "varchar(500)")
	public String getAttachName() {
		return attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	@Column(name = "attachSize")
	public Long getAttachSize() {
		return attachSize;
	}

	public void setAttachSize(Long attachSize) {
		this.attachSize = attachSize;
	}

	@Column(name = "attachSizeStr", length = 100)
	public String getAttachSizeStr() {
		return attachSizeStr;
	}

	public void setAttachSizeStr(String attachSizeStr) {
		this.attachSizeStr = attachSizeStr;
	}

	@Column(name = "attachType", length = 100)
	public String getAttachType() {
		return attachType;
	}

	public void setAttachType(String attachType) {
		this.attachType = attachType;
	}

	@Column(name = "attachContentType", length = 100)
	public String getAttachContentType() {
		return attachContentType;
	}

	public void setAttachContentType(String attachContentType) {
		this.attachContentType = attachContentType;
	}

	@Column(name = "attachRemark", columnDefinition = "TEXT")
	public String getAttachRemark() {
		return attachRemark;
	}

	public void setAttachRemark(String attachRemark) {
		this.attachRemark = attachRemark;
	}

	@Column(name = "ext", length = 100)
	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	@Column(name = "relativePath", columnDefinition = "varchar(4000)")
	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	@Column(name = "absolutePath", columnDefinition = "varchar(4000)")
	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	@Column(name = "onlyName", columnDefinition = "varchar(500)")
	public String getOnlyName() {
		return onlyName;
	}

	public void setOnlyName(String onlyName) {
		this.onlyName = onlyName;
	}

	@Column(name = "MD5", length = 100)
	public String getMD5() {
		return MD5;
	}

	public void setMD5(String mD5) {
		MD5 = mD5;
	}

	@Column(name = "isFirstUpload", columnDefinition = "char")
	public String getIsFirstUpload() {
		return isFirstUpload;
	}

	public void setIsFirstUpload(String isFirstUpload) {
		this.isFirstUpload = isFirstUpload;
	}

	@Column(name = "thumbnailAbPath", columnDefinition = "varchar(4000)")
	public String getThumbnailAbPath() {
		return thumbnailAbPath;
	}

	public void setThumbnailAbPath(String thumbnailAbPath) {
		this.thumbnailAbPath = thumbnailAbPath;
	}

	@Column(name = "thumbnailRePath", columnDefinition = "varchar(4000)")
	public String getThumbnailRePath() {
		return thumbnailRePath;
	}

	public void setThumbnailRePath(String thumbnailRePath) {
		this.thumbnailRePath = thumbnailRePath;
	}

	@Column(name = "storageType", length = 100)
	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	@Column(name = "storageId", length = 100)
	public String getStorageId() {
		return storageId;
	}

	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}

	@Column(name = "iconType", length = 32)
	public String getIconType() {
		return iconType;
	}

	public void setIconType(String iconType) {
		this.iconType = iconType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeEntity")
	@ForeignKey(name = "null")
	public TypeEntity getTypeEntity() {
		return typeEntity;
	}

	public void setTypeEntity(TypeEntity typeEntity) {
		this.typeEntity = typeEntity;
	}

	@Column(name = "businessKey", length = 32)
	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	@Column(name = "businessType", length = 100)
	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	@Column(name = "businessExtra", length = 100)
	public String getBusinessExtra() {
		return businessExtra;
	}

	public void setBusinessExtra(String businessExtra) {
		this.businessExtra = businessExtra;
	}

	@Column(name = "otherKey", length = 100)
	public String getOtherKey() {
		return otherKey;
	}

	public void setOtherKey(String otherKey) {
		this.otherKey = otherKey;
	}

	@Column(name = "otherKeyType", length = 100)
	public String getOtherKeyType() {
		return otherKeyType;
	}

	public void setOtherKeyType(String otherKeyType) {
		this.otherKeyType = otherKeyType;
	}
}
