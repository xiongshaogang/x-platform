package com.xplatform.base.framework.mybatis.engine.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.mybatis.engine.Query;
import com.xplatform.base.framework.mybatis.engine.vo.FormViewField;
import com.xplatform.base.framework.mybatis.engine.vo.VoForm;
import com.xplatform.base.framework.mybatis.entity.Page;

/**
 * 
 * <STRONG>抽象查询</STRONG> :  <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Sep 14, 2012 1:44:51 PM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Sep 14, 2012 1:44:51 PM
 *</pre>
 */
public abstract class AbstractQuery<T extends Query<T, U>, U> implements
		Query<T, U>, Serializable {

	private static final long serialVersionUID = 1L;

	public static final String SORTORDER_ASC = "asc";

	public static final String SORTORDER_DESC = "desc";

	private static enum ResultType {
		LIST, LIST_PAGE, SINGLE_RESULT, COUNT
	}

	/**
	 * 最终执行的SQL
	 */
	protected StringBuffer sql = new StringBuffer();

	/**
	 * 表名SQL集合
	 */
	protected List<String> tableNameSqlList = new ArrayList();

	/**
	 * 初始化条件SQL集合
	 */
	protected List<String> initConditionSqlList = new ArrayList();
	
	/**
	 * 查询条件的VOFORM集合
	 */
	protected List<VoForm> formList = new ArrayList();
	
	/**
	 * 查询公共字段
	 */
	protected List<FormViewField> sumFieldList = new ArrayList();

	/**
	 * 分页查询前端语句
	 */
	protected String limitBefore;

	/**
	 * 分页查询后端语句
	 */
	protected String limitAfter;
	
	protected String orderBy;
	protected ResultType resultType;
	protected QueryProperty orderProperty;
	protected int firstResult;
	protected int maxResults;

	/**
	 * 查询总数
	 */
	protected long count;

	/**
	 * 分页查询条件参数
	 */
	protected Page<U> page;

	protected Object parameter;
	protected T query;

	/**
	 * 
	 * <pre>
	 * 生成语句方法
	 * </pre>
	 */
	protected abstract void build();

	public T orderBy(QueryProperty property) {
		this.orderProperty = property;
		return query;
	}

	public T asc() {
		return direction(Direction.ASCENDING);
	}

	public T desc() {
		return direction(Direction.DESCENDING);
	}

	@SuppressWarnings("unchecked")
	public T direction(Direction direction) {
		if (orderProperty == null) {
			throw new BusinessRuntimeException(
					"You should call any of the orderBy methods first before specifying a direction");
		}
		addOrder(orderProperty.getName(), direction.getName());
		orderProperty = null;
		return (T) this;
	}

	protected void addOrder(String column, String sortOrder) {
		if (orderBy == null) {
			orderBy = "";
		} else {
			orderBy = orderBy + ", ";
		}
		orderBy = orderBy + column + " " + sortOrder;
	}

	protected void checkQueryOk() {
		if (orderProperty != null) {
			throw new BusinessRuntimeException(
					"Invalid query: call asc() or desc() after using orderByXX()");
		}
	}


//	protected Query names(List<VoForm> formList) {
//		this.formList = formList;
//		buildNames();
//		return this;
//	}

	public long count() {
		return 0;
	}

	public List<U> list() {
		return null;
	}

	public Page<U> listPage(Page<U> page) {

		this.page = page;

		this.page.setTotalCount(this.count());

		/*
		 * this.limitBefore = "select * from (select tmp_tb.*,ROWNUM row_id from
		 * ("; this.limitAfter = ") tmp_tb where ROWNUM<="+page.getLast()+")
		 * tmp_rs where row_id>="+page.getFirst();
		 */

		this.limitAfter = " limit " + (page.getFirst() - 1) + ","
				+ page.getPageSize();

		// this.page.setResult(this.jswfDao.listPage(query));

		return this.page;
	}

	public Page<U> listPage() {
		return null;
	}

	public U singleResult() {
		return null;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public String getLimitAfter() {
		return limitAfter;
	}

	public void setLimitAfter(String limitAfter) {
		this.limitAfter = limitAfter;
	}

	public String getLimitBefore() {
		return limitBefore;
	}

	public void setLimitBefore(String limitBefore) {
		this.limitBefore = limitBefore;
	}

}
