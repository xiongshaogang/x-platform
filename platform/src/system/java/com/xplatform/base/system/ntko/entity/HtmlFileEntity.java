package com.xplatform.base.system.ntko.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name="t_sys_html_file",schema="")
@SuppressWarnings("serial")
public class HtmlFileEntity extends OperationEntity implements Serializable{
	private String fileName;
	private String fileSize;
	private String otherData;
	private String fileNameDisk;
	private String absoluteFileDir;
	private String busId;//业务主键
	
	@Column(name ="file_name",nullable=true,length=50)
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name ="file_size",nullable=true,length=50)
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
	@Column(name ="other_data",nullable=true,length=50)
	public String getOtherData() {
		return otherData;
	}
	public void setOtherData(String otherData) {
		this.otherData = otherData;
	}
	
	
	@Column(name ="file_name_disk",nullable=true,length=200)
	public String getFileNameDisk() {
		return fileNameDisk;
	}
	public void setFileNameDisk(String fileNameDisk) {
		this.fileNameDisk = fileNameDisk;
	}
	
	@Column(name ="absolute_file_dir",nullable=true,length=500)
	public String getAbsoluteFileDir() {
		return absoluteFileDir;
	}
	public void setAbsoluteFileDir(String absoluteFileDir) {
		this.absoluteFileDir = absoluteFileDir;
	}
	@Column(name ="bus_id",nullable=true,length=32)
	public String getBusId() {
		return busId;
	}
	public void setBusId(String busId) {
		this.busId = busId;
	}

}
