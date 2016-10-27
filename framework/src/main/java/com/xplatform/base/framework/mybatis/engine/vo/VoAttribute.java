package com.xplatform.base.framework.mybatis.engine.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 
 * <STRONG>类描述</STRONG> : 主实体表、类型表、或扩展表对应的表单属性值对象 <p>
 *   
 * @version 1.0 <p>
 * @author jiagq@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : 2012-8-27 下午04:30:34<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   修改时间                     修改内容
 * ---------------         -------------------         -----------------------------------
 * jiagq@huilan.com        2012-8-27 下午04:30:34
 *</pre>
 */
public class VoAttribute{
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * 表别名
	 */
	private String alias;
	
	/**
	 * 增加、修改和删除时的属性名：值map
	 */
	private  Map<String,String> attributeMap;
	
	/**
	 * 查询的属性名
	 */
	private List<FormViewField> fieldList = new ArrayList();
	public VoAttribute() {
		super();
	}
	public VoAttribute(String tableName) {
		super();
		this.tableName = tableName;
	}
	public VoAttribute(String tableName, Map<String, String> attributeMap) {
		super();
		this.tableName = tableName;
		this.attributeMap = attributeMap;
	}
	
	public VoAttribute(String tableName, String alias,
			Map<String, String> attributeMap) {
		super();
		this.tableName = tableName;
		this.alias = alias;
		this.attributeMap = attributeMap;
	}
	
	
	public VoAttribute(String tableName, String alias,
			List<FormViewField> fieldList) {
		super();
		this.tableName = tableName;
		this.alias = alias;
		this.fieldList = fieldList;
	}
	public VoAttribute(String tableName, String alias,
			Map<String, String> attributeMap, List<FormViewField> fieldList) {
		super();
		this.tableName = tableName;
		this.alias = alias;
		this.attributeMap = attributeMap;
		this.fieldList = fieldList;
	}
	
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}
	public void setAttributeMap(Map<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	}
	public List<FormViewField> getFieldList() {
		return fieldList;
	}
	public void setFieldList(List<FormViewField> fieldList) {
		this.fieldList = fieldList;
	}
	
}
