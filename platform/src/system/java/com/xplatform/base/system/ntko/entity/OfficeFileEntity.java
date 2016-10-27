package com.xplatform.base.system.ntko.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;


@Entity
@Table(name="t_sys_office_file",schema="")
@SuppressWarnings("serial")
public class OfficeFileEntity extends OperationEntity implements Serializable{

	private String fileName;
	private String fileSize;
	private String otherData;
	private String fileType;
	private String fileNameDisk;
	private String absoluteFileDisk;
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
	
	@Column(name ="file_type",nullable=true,length=50)
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	@Column(name ="file_name_disk",nullable=true,length=200)
	public String getFileNameDisk() {
		return fileNameDisk;
	}
	public void setFileNameDisk(String fileNameDisk) {
		this.fileNameDisk = fileNameDisk;
	}
	
	@Column(name ="absolute_file_disk",nullable=true,length=500)
	public String getAbsoluteFileDisk() {
		return absoluteFileDisk;
	}
	public void setAbsoluteFileDisk(String absoluteFileDisk) {
		this.absoluteFileDisk = absoluteFileDisk;
	}
	@Column(name ="bus_id",nullable=true,length=32)
	public String getBusId() {
		return busId;
	}
	public void setBusId(String busId) {
		this.busId = busId;
	}
}
