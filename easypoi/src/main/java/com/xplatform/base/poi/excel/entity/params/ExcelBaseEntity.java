package com.xplatform.base.poi.excel.entity.params;

import java.lang.reflect.Method;
import java.util.List;
/**
 * Excel 导入导出基础对象类
 * @author JueYue
 * @date 2014年6月20日 下午2:26:09
 */
public class ExcelBaseEntity {
	/**
	 * 对应实体的属性(字段)
	 */
	private String field;
	/**
	 * 对应name
	 */
	private String name;
	/**
	 * 对应type(1 是文本 2 是图片,3是函数 默认是文本)
	 */
	private int type;
	/**
	 * 数据库格式(字符串转时间的格式,用SimpleDateFormat从String转成Date)
	 */
	private String databaseFormat;
	/**
	 * 导出日期格式
	 */
	private String format;
	/**
	 * 导出替换键值(格式:男_nan,女_nv)
	 */
	private String[] replace;
	/**
	 * set/get方法
	 */
	private Method method;
	
	/**
	 * 如果有methods就执行methods,没methods就执行method(未来获取真实数据值时)
	 */
	private List<Method> methods;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDatabaseFormat() {
		return databaseFormat;
	}

	public void setDatabaseFormat(String databaseFormat) {
		this.databaseFormat = databaseFormat;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String[] getReplace() {
		return replace;
	}

	public void setReplace(String[] replace) {
		this.replace = replace;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

}
