package com.xplatform.base.system.attachment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * description : 附件表
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月2日 下午4:59:51
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年7月2日 下午4:59:51
 *
*/
@Entity
@Table(name = "t_sys_ftp_attachment")
@SuppressWarnings("serial")
public class FTPAttachEntity extends OperationEntity implements java.io.Serializable {
	private String remoteFilePath; //FTP服务器用于下载的路径
	private String remotePath; //FTP服务器存储该文件的文件夹
	private String fileName; //FTP服务器上的文件名
	private String remoteThumbnailFilePath;//FTP服务器缩略图下载路径(如果是图片类型,或者是需要缩略图的类型,才有此值)
	private String remoteThumbnailPath;//FTP服务器存储该缩略图的文件夹(如果是图片类型,或者是需要缩略图的类型,才有此值)

	@Column(name = "remoteFilePath", columnDefinition = "varchar(4000)")
	public String getRemoteFilePath() {
		return remoteFilePath;
	}
	
	public void setRemoteFilePath(String remoteFilePath) {
		this.remoteFilePath = remoteFilePath;
	}

	@Column(name = "remotePath", columnDefinition = "varchar(4000)")
	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	@Column(name = "fileName", columnDefinition = "varchar(500)")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "remoteThumbnailFilePath", columnDefinition = "varchar(4000)")
	public String getRemoteThumbnailFilePath() {
		return remoteThumbnailFilePath;
	}

	public void setRemoteThumbnailFilePath(String remoteThumbnailFilePath) {
		this.remoteThumbnailFilePath = remoteThumbnailFilePath;
	}
	
	@Column(name = "remoteThumbnailPath", columnDefinition = "varchar(4000)")
	public String getRemoteThumbnailPath() {
		return remoteThumbnailPath;
	}
	
	public void setRemoteThumbnailPath(String remoteThumbnailPath) {
		this.remoteThumbnailPath = remoteThumbnailPath;
	}

}
