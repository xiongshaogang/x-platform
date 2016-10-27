package com.xplatform.base.framework.mybatis.engine.builder;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.engine.SqlBuilder;

/**
 * 
 * <STRONG>抽象的SQL创建类 实现SqlBuilder</STRONG> :  <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Aug 27, 2012 11:28:01 AM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Aug 27, 2012 11:28:01 AM
 *</pre>
 */
public abstract class AbstractSqlBuilder implements SqlBuilder{
	
	protected Logger logger = LoggerFactory.getLogger(AbstractSqlBuilder.class);
	
	/**
	 * "UPDATE "
	 */
	protected static final String UPDATE = "UPDATE ";
	
	/**
	 * "SET "
	 */
	protected static final String SET = " SET ";
	/**
	 * "INSERT INTO "
	 */
	protected static final String INSERT_INTO ="INSERT INTO ";
	
	/**
	 * VALUES
	 */
	protected static final String VALUES= "VALUES";
	/**
	 * “DELETE ”
	 */
	protected static final String DELETE = "DELETE ";
	/**
	 * “ FROM ”
	 */
	protected static final String FROM = " FROM ";
	
	/**
	 * “ WHERE ”
	 */
	protected static final String WHERE = " WHERE ";
	
	/**
	 * “ AND ”
	 */
	
	protected static final String AND = " AND ";
	
	/**
	 * 等号“=”
	 */
	protected static final String EQUAL_SIGN = "=";
	
	/**
	 * 井号“#”
	 */
	protected static final String WELL_SIGN = "#";
	
	/**
	 * 左大括号“{”
	 */
	protected static final String L_BRACKET = "{";
	
	/**
	 * 右大括号“}”
	 */
	protected static final String R_BRACKET = "}";
	
	/**
	 * 拼装的SQL语句
	 */
	protected StringBuilder sql = new StringBuilder();
	
	/**
	 * 表名
	 */
	protected String table;
	
	/**
	 * 参数集合
	 */
	protected Set<String> valueAttributes;
	
	/**
	 * 条件集合
	 */
	protected Set<String> conditionAttributes;
	
	/**
	 * 
	 * <pre>
	 * 检测表名是否为空
	 * </pre>
	 * @return
	 */
	protected boolean checkTableName()throws BusinessRuntimeException{
		if(StringUtil.isEmpty(table)){
			throw new BusinessRuntimeException("表名不存在");
		}
		return true;
	}
	
	/**
	 * 
	 * <pre>
	 * 检测参数集合是否为空
	 * </pre>
	 * @return
	 */
	protected boolean checkValueAttr()throws BusinessRuntimeException{
		if(valueAttributes == null || valueAttributes.size() == 0){
			throw new BusinessRuntimeException("属性值为空");
		}
		return true;
	}
	
	/**
	 * 
	 * <pre>
	 * 检测条件集合是否为空
	 * </pre>
	 * @return
	 */
	protected boolean checkConditionAttr()throws BusinessRuntimeException{
		if(conditionAttributes == null || conditionAttributes.size() == 0){
			throw new BusinessRuntimeException("条件属性为空");
		}
		return true;
	}
	
	/**
	 * <pre>
	 * 执行检查
	 * </pre>
	 * @return
	 */
	protected abstract boolean buildChecked() throws BusinessRuntimeException;
	
	/**
	 * 设置条件集合
	 */
	public SqlBuilder conditionAttributes(Set<String> conditionAttributes) {
		this.conditionAttributes = conditionAttributes;
		return this;
	}

	/**
	 * 设置表名
	 */
	public SqlBuilder table(String table) {
		this.table = table;
		return this;
	}

	/**
	 * 设置参数集合
	 */
	public SqlBuilder valueAttributes(Set<String> valueAttributes) {
		this.valueAttributes = valueAttributes;
		return this;
	}
	
	
}
