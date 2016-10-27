package com.xplatform.base.system.timer.entity;

public class TriggerEntity {
	private String trigName;
	private String status;
	private String jobName;
	private String description;
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTrigName() {
		return trigName;
	}
	public void setTrigName(String trigName) {
		this.trigName = trigName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
