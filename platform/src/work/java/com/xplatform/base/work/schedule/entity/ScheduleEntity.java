package com.xplatform.base.work.schedule.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name = "t_work_schedule", schema = "")
public class ScheduleEntity extends OperationEntity {

	private String title;
	private Date startDate;
	private Date endDate;
	private String context;
	private String userId;
	private String className;
	private String allDay;
	private String scheduleType;
	private String state;
	
	@Column(name ="title",nullable=true,length=256)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name ="start_date",nullable=true)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column(name ="end_date",nullable=true)
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	
	@Column(name ="class_name",nullable=true,length=100)
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	@Column(name ="allDay",nullable=true,length=64)
	public String getAllDay() {
		return allDay;
	}
	public void setAllDay(String allDay) {
		this.allDay = allDay;
	}
	
	@Column(name ="scheduleType",nullable=true,length=64)
	public String getScheduleType() {
		return scheduleType;
	}
	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}
	
	@Column(name ="state",nullable=true,length=64)
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
