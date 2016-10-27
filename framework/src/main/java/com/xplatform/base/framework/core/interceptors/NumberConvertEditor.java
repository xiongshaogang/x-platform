package com.xplatform.base.framework.core.interceptors;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.util.StringUtils;

/**
 * Boolean转Integer的转换类,例如前段传&isRead=true,但是实体或属性需要接收为Integer类型的参数
 * 
 * @author xiaqiang
 */
public class NumberConvertEditor extends PropertyEditorSupport {
	public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.hasText(text)) {
			if ("false".equals(text)) {
				setValue(0);
			} else if ("true".equals(text)) {
				setValue(1);
			} else {
				throw new IllegalArgumentException("Could not parse boolean(String type) to Integer");
			}
		} else {
			setValue(null);
		}
	}
}
