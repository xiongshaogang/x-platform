package com.xplatform.base.framework.mybatis.engine.query;

import java.util.Map;

import com.xplatform.base.framework.mybatis.entity.Page;


/**
 * 
 * <STRONG>表单列表查询</STRONG> :  <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Sep 14, 2012 1:51:00 PM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Sep 14, 2012 1:51:00 PM
 *</pre>
 */
public interface PageQuery extends ValueQuery<PageQuery, Map<String, String>> {
	
	/**
	 * 
	 * <pre>
	 * 设置查询分页对象
	 * </pre>
	 * @param page
	 */
	void page(Page<Map<String, String>> page);
}
