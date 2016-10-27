package com.xplatform.base.framework.mybatis.engine.query.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.mybatis.engine.query.AbstractFieldQuery;
import com.xplatform.base.framework.mybatis.engine.query.FormQuery;

/**
 * 
 * <STRONG>单个表单内容查询实现类</STRONG> :
 * <p>
 * 
 * @version 1.0
 *          <p>
 * @author mengfx@huilan.com
 *         <p>
 * 
 * <STRONG>创建时间</STRONG> : Sep 14, 2012 1:56:12 PM
 * <p>
 * <STRONG>修改历史</STRONG> :
 * <p>
 * 
 * <pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Sep 14, 2012 1:56:12 PM
 * </pre>
 */
public class FormQueryImpl extends
		AbstractFieldQuery<FormQuery, Map<String, String>> implements FormQuery {

	private static final long serialVersionUID = 1L;

	protected Logger logger = LoggerFactory.getLogger(FormQueryImpl.class);


	/**
	 * 查询ID
	 */
	private String id;
	
	private String pageKey;
	
	private List<String> clobSqlList = new ArrayList();

	/**
	 * 设置查询ID
	 */
	public FormQueryImpl id(String id,String pageKey) {

		if (id == null) {
			throw new BusinessRuntimeException("属性值为空");
		}

		this.id = id;
		this.pageKey = pageKey;
		build();
		return this;
	}

	/**
	 * 生成查询SQL
	 */
	public void build() {
		
		sql.append("SELECT ").append(this.fieldsSqlList.get(0))
				.append(" FROM ").append(this.tableNameSqlList.get(0)).append(
						this.initConditionSqlList.get(0)).append(
						" AND MAIN.ID='").append(id).append("' AND PAGE.PAGE_KEY= '").append(pageKey).append("'");
		
		
		logger.debug("single form query sql :" + sql.toString());
		
		
		for(String field :this.clobFieldSqlList){
			StringBuffer tempSql = new StringBuffer();
			tempSql.append("SELECT ").append(field)
			.append(" FROM ").append(this.tableNameSqlList.get(0)).append(
					this.initConditionSqlList.get(0)).append(
					" AND MAIN.ID='").append(id).append("' AND PAGE.PAGE_KEY= '").append(pageKey).append("'");
			this.clobSqlList.add(tempSql.toString());
			
		}
	}

	@Override
	public Map<String, String> singleResult() {

		Map<String, String> result = new HashMap<String, String>();
		/*ApplicationContext context = SpringContextHolder.getApplicationContext();
		DynamicFormService dynamicFormService = context.getBean(DynamicFormService.class);
		// 执行SQL
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("DYNAMIC_SQL", sql.toString());
		List<Map<String, String>> resultList = dynamicFormService
				.QueryDynamicForm(valueMap);
		if (resultList != null && resultList.size() > 0) {
			result =  resultList.get(0);
		}
		
		if(this.clobSqlList!= null && this.clobSqlList.size()!=0){
			int i = 0;
			for(String clobSql: clobSqlList){
				valueMap = new HashMap<String, String>();
				valueMap.put("DYNAMIC_SQL", clobSql);
				String clobContent = dynamicFormService.QueryDynamicFormClob(valueMap);
				result.put(clobFieldName.get(i), clobContent);
				i ++;
			}
		}*/
		
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPageKey() {
		return pageKey;
	}

	public void setPageKey(String pageKey) {
		this.pageKey = pageKey;
	}
	
	

}
