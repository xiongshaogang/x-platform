package com.xplatform.base.system.log.aop;

import java.util.Map;

import com.xplatform.base.system.log.entity.OperLogEntity;

class LogHolder {
	OperLogEntity operLogEntity;
	boolean needParse = false;
	Map<String, Object> parseDataModel;


	public OperLogEntity getOperLogEntity() {
		return operLogEntity;
	}

	public void setOperLogEntity(OperLogEntity operLogEntity) {
		this.operLogEntity = operLogEntity;
	}

	public boolean isNeedParse() {
		return this.needParse;
	}

	public void setNeedParse(boolean needParse) {
		this.needParse = needParse;
	}

	public Map<String, Object> getParseDataModel() {
		return this.parseDataModel;
	}

	public void setParseDataModel(Map<String, Object> parseDataModel) {
		this.parseDataModel = parseDataModel;
	}
}