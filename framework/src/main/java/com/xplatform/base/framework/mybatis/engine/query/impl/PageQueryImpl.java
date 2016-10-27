package com.xplatform.base.framework.mybatis.engine.query.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.engine.query.AbstractValueQuery;
import com.xplatform.base.framework.mybatis.engine.query.PageQuery;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.framework.mybatis.utils.PageSqlUtil;

/**
 * 
 * <STRONG>表单列表查询实现类</STRONG> :分页查询 <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Sep 14, 2012 1:54:52 PM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Sep 14, 2012 1:54:52 PM
 *</pre>
 */
public class PageQueryImpl extends AbstractValueQuery<PageQuery, Map<String, String>> implements
		PageQuery {

	private static final long serialVersionUID = 1L;
	
	protected Logger logger = LoggerFactory.getLogger(PageQueryImpl.class);
	
	/**
	 * 分页查询参数
	 */
	Page<Map<String, String>> page;
	
	
	/**
	 * 设置查询参数
	 */
	public void page(Page<Map<String, String>> page) {
		if(page == null){
			throw new BusinessRuntimeException("属性值为空");
		}
		this.page=page;
		genderSql();
		if(StringUtil.isNotEmpty(page.getOrder())){
			buildOrderBy();
		}
		
		logger.debug("page form query sql :" + sql.toString());
	//	System.out.println(sql.toString());
	}

	@Override
	protected void build() {

	}

	/**
	 * 
	 * <pre>
	 * 生成排序SQL
	 * </pre>
	 */
	protected void buildOrderBy() {
		
		StringBuffer orderBySql = new StringBuffer();
		if(page != null && page.getOrderBy() != null){
			orderBySql.append(" SELECT ").append(" * FROM(").append(sql).append(")aa ORDER BY TOP_ORDER ASC, ").append(page.getOrderBy())
			.append(" ").append(page.getOrder());
			sql= orderBySql;
		}
	//	System.out.println("********"+sql.toString());
	}

	@Override
	public long count() {
		int sum = 0;
		
		return sum;
	}

	@Override
	public List<Map<String, String>> list() {

		/*ApplicationContext context = SpringContextHolder.getApplicationContext();
		DynamicFormService dynamicFormService = context.getBean(DynamicFormService.class);
		
		// 执行SQL
		this.queryMap.put("DYNAMIC_SQL", sql.toString());
//		this.queryMap.put("PAGE_FORM_KEY", "sdf");
//		this.queryMap.put("DYNAMIC_SQL", "SELECT MAIN.ID ID FROM EPS_ENTITY MAIN,EPS_ENTITY_PAGE PAGE WHERE 1=1  AND MAIN.KEY=PAGE.ENTITY_KEY AND PAGE.FORM_KEY=#{PAGE_FORM_KEY}");
		List<Map<String, String>> result = dynamicFormService.QueryDynamicForm(this.queryMap);*/
		return null;
	}

	@Override
	public Page<Map<String, String>> listPage() {
		
		this.page.setTotalCount(this.count());
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("application.properties");
		Properties props =null;
		String dbType  ="oracle";
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
			if(StringUtil.isNotEmpty(props.getProperty("database.dialect"))){
				dbType = props.getProperty("database.dialect");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String querySql = PageSqlUtil.generatePageSql(dbType, sql.toString(),
				page);
		// this.page.setResult(this.jswfDao.listPage(query));
		// 执行querySql 得到 结果List
		this.sql = new StringBuffer(querySql);
		List<Map<String, String>> result = list();
		this.page.setResult(result);
		return this.page;
	}

}
