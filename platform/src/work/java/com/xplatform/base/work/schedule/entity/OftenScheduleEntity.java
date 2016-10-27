package com.xplatform.base.work.schedule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name = "t_work_often_schedule", schema = "")
public class OftenScheduleEntity extends OperationEntity {

	private String title;
	private String context;
	private String userId;
	private String colorStyle;
	
	@Column(name ="title",nullable=true,length=256)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name ="context",nullable=true,length=1000)
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	
	@Column(name ="user_id",nullable=true,length=64)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name ="color_style",nullable=true,length=100)
	public String getColorStyle() {
		return colorStyle;
	}
	public void setColorStyle(String colorStyle) {
		this.colorStyle = colorStyle;
	}
	
}
