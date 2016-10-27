package com.xplatform.base.system.timer.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
@Entity
@Table(name = "t_sys_timer_log", schema = "")
public class TimerLogEntity extends OperationEntity{
	private String jobName; //定时器名称
	private String triggerName; //计划任务名称
	private String content; //内容
	private Integer state;//状态
	private Date endTime;//结束时间
	private Long runTime;//执行时间
	
	@Column(name ="job_name",nullable=true,length=100)
	public String getJobName() {
		return jobName;
	}
	
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	@Column(name ="trigger_name",nullable=true,length=100)
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	
	@Column(name ="content",nullable=true,length=1000)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name ="state",nullable=true,length=100)
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	@Column(name ="end_time",nullable=true)
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name ="run_time",nullable=true)
	public Long getRunTime() {
		return runTime;
	}
	public void setRunTime(Long runTime) {
		this.runTime = runTime;
	}
}
