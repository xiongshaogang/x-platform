package com.xplatform.base.framework.mybatis.engine;

import java.util.Set;

import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;


/**
 * 
 * <STRONG>创建SQL 接口</STRONG> :  <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Aug 27, 2012 11:43:04 AM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Aug 27, 2012 11:43:04 AM
 *</pre>
 */
public interface SqlBuilder{
	
	/**
	 * 
	 * <pre>
	 * 生成SQL
	 * </pre>
	 * @return
	 * @throws SqlBudilderException
	 */
	String build() throws BusinessRuntimeException;
	
	/**
	 * 设置表名
	 */
	SqlBuilder table(String table);
	
	/**
	 * 设置参数属性
	 */
	SqlBuilder valueAttributes(Set<String> valueAttributes);
	
	/**
	 * 设置条件属性
	 */
	SqlBuilder conditionAttributes(Set<String> conditionAttributes);
	
}
