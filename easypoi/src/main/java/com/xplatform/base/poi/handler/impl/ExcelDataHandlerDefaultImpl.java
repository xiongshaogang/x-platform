package com.xplatform.base.poi.handler.impl;

import com.xplatform.base.poi.handler.inter.IExcelDataHandler;

/**
 * 数据处理默认实现,返回空
 * 
 * @author JueYue
 * @date 2014年6月20日 上午12:11:52
 */
public abstract class ExcelDataHandlerDefaultImpl implements IExcelDataHandler {
	/**
	 * 需要处理的字段
	 */
	private String[] needHandlerFields;

	@Override
	public Object exportHandler(Object obj, String name, Object value) {
		return value;
	}

	@Override
	public Object importHandler(Object obj, String name, Object value) {
		return value;
	}

	@Override
	public String[] getNeedHandlerFields() {
		return needHandlerFields;
	}

	@Override
	public void setNeedHandlerFields(String[] needHandlerFields) {
		this.needHandlerFields = needHandlerFields;
	}

}
