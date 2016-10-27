package com.xplatform.base.workflow.core.bpm.model;


public class TaskAmount {
	private Long typeId = Long.valueOf(0L);

	private int read = 0;

	private int total = 0;

	private int notRead = 0;

	public Long getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public int getRead() {
		return this.read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public int getTotal() {
		return this.total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getNotRead() {
		return this.notRead;
	}

	public void setNotRead(int notRead) {
		this.notRead = notRead;
	}
}