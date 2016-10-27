package com.xplatform.base.workflow.task.mybatis.vo;

public class TaskVars {
	protected Long id;
	protected String name;
	protected String type;
	protected int revision;
	protected String processInstanceId;
	protected String executionId;
	protected String taskId;
	protected Long longValue;
	protected Double doubleValue;
	protected String textValue;
	protected String textValue2;
	protected String byteArrayValueId;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRevision() {
		return this.revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public String getProcessInstanceId() {
		return this.processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getExecutionId() {
		return this.executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Long getLongValue() {
		return this.longValue;
	}

	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}

	public Double getDoubleValue() {
		return this.doubleValue;
	}

	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public String getTextValue() {
		return this.textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	public String getTextValue2() {
		return this.textValue2;
	}

	public void setTextValue2(String textValue2) {
		this.textValue2 = textValue2;
	}

	public String getByteArrayValueId() {
		return this.byteArrayValueId;
	}

	public void setByteArrayValueId(String byteArrayValueId) {
		this.byteArrayValueId = byteArrayValueId;
	}
}