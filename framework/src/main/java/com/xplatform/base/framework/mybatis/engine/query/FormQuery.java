package com.xplatform.base.framework.mybatis.engine.query;

import java.util.Map;

/**
 * 
 * <STRONG>单个表单内容查询</STRONG> :  <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Sep 14, 2012 1:50:00 PM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Sep 14, 2012 1:50:00 PM
 *</pre>
 */
public interface FormQuery extends FieldQuery<FormQuery, Map<String, String>> {
	
	/**
	 * 
	 * <pre>
	 * 设置查询ID
	 * </pre>
	 * @param id
	 * @return
	 */
	FormQuery id(String id,String pageKey);
	
}