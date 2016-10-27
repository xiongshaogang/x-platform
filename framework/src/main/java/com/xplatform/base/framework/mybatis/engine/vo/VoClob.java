package com.xplatform.base.framework.mybatis.engine.vo;

/**
 * 
 * <STRONG>类描述</STRONG> :历史表大字段信息值对象  <p>
 *   
 * @version 1.0 <p>
 * @author jiagq@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : 2012-8-27 下午04:27:04<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   修改时间                     修改内容
 * ---------------         -------------------         -----------------------------------
 * jiagq@huilan.com        2012-8-27 下午04:27:04
 *</pre>
 */
public class VoClob{
	/**
	 * 记录id
	 */
	private String id;
	/**
	 * 类型表数据转换后的xml字符串
	 */
	private String typeTable;
	/**
	 * 扩展表数据转换后的xml字符串
	 */
	private String extTable;
	
	public String getId() {
		return id;
	}

	public VoClob(String id, String typeTable, String extTable) {
		super();
		this.id = id;
		this.typeTable = typeTable;
		this.extTable = extTable;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeTable() {
		return typeTable;
	}

	public void setTypeTable(String typeTable) {
		this.typeTable = typeTable;
	}

	public String getExtTable() {
		return extTable;
	}

	public void setExtTable(String extTable) {
		this.extTable = extTable;
	}

	public VoClob() {
		super();
	}
	
}
