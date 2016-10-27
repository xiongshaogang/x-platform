package com.xplatform.base.framework.mybatis.engine.query;

import java.util.List;

import com.xplatform.base.framework.mybatis.engine.Query;
import com.xplatform.base.framework.mybatis.engine.vo.FormViewField;
import com.xplatform.base.framework.mybatis.engine.vo.VoForm;

/**
 * 
 * <STRONG>字段查询</STRONG> :  <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Sep 14, 2012 1:49:25 PM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Sep 14, 2012 1:49:25 PM
 *</pre>
 */
public interface FieldQuery<T extends Query<T, U> , U> extends Query<T, U> {
	
	/**
	 * 
	 * <pre>
	 * 设置查询字段条件
	 * </pre>
	 * @param formList
	 * @param sumFieldList
	 * @return
	 */
	FieldQuery<T,U> fields(List<VoForm> formList,List<FormViewField> sumFieldList);
	
}
