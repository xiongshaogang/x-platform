/**
 * Copyright (c) 2014
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.xplatform.base.poi.excel.entity.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * description :
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年11月25日 下午6:21:21
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年11月25日 下午6:21:21 
 *
*/
public class ExcelImportVo implements Serializable{
	 /** 
	  * serialVersionUID
	  */
	private static final long serialVersionUID = -4430561133489617438L;
	private String errorMsg; //验证提示语句
	private Boolean validResult = true; //验证是否通过
	private Map<String, Object> oldValueMap = new HashMap<String, Object>();//存放转换前(经过replace,dataHandler后)的某些字段的值,key为字段名称,value为各类型旧值

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Boolean getValidResult() {
		return validResult;
	}

	public void setValidResult(Boolean validResult) {
		this.validResult = validResult;
	}

	public Map<String, Object> getOldValueMap() {
		return oldValueMap;
	}

	public void setOldValueMap(Map<String, Object> oldValueMap) {
		this.oldValueMap = oldValueMap;
	}
}
