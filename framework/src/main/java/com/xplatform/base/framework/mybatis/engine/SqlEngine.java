package com.xplatform.base.framework.mybatis.engine;

import java.util.Set;

/**
 * 
 * <STRONG>创建SQL的引擎</STRONG> :  <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Aug 28, 2012 9:18:28 AM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Aug 28, 2012 9:18:28 AM
 *</pre>
 */
public interface SqlEngine {
	
	/**
	 * 
	 * <pre>
	 * 通过类名创建 SqlBuilder 
	 * </pre>
	 * @param <T> 
	 * @param cls  SqlBuilder实现类
	 * @return
	 */
	<T extends SqlBuilder> T createSqlBuilder(Class<T> cls);
	
	/**
	 * 
	 * <pre>
	 * 通过类名创建 SqlBuilder 并给表名、参数列表、条件列表 赋值
	 * </pre>
	 * @param <T>
	 * @param cls
	 * @param table 表名
	 * @param valueAttributes 参数列表
	 * @param conditionAttributes 条件列表
	 * @return
	 */
	<T extends SqlBuilder> T createSqlBuilder(Class<T> cls,String table, Set<String> valueAttributes, Set<String> conditionAttributes);
	
	<T extends Query> T createQuery(Class<T> cls);
	
}