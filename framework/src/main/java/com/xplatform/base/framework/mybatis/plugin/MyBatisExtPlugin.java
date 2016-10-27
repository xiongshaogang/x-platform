/**
 * 2012-4-19
 */
package com.xplatform.base.framework.mybatis.plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.builder.xml.dynamic.ForEachSqlNode;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.log4j.Logger;

import com.xplatform.base.framework.core.util.ReflectHelper;
import com.xplatform.base.framework.mybatis.entity.ExtEntity;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.framework.mybatis.utils.PageSqlUtil;


/**
 * 
 * description :MyBatis3.x 分页插件
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月17日 下午12:51:24
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月17日 下午12:51:24
 *
 */
@Intercepts( { @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class MyBatisExtPlugin implements Interceptor {
	private static final Logger logger = Logger.getLogger(MyBatisExtPlugin.class);
	/**
	 * 数据库方言
	 */
	private static String dialect = "oracle";

	/**
	 * mapper.xml中需要拦截的ID(正则匹配)
	 */
	private static String pageSqlId = ".*ByPage";
	private static String insertSqlId = ".*\\.save[A-Za-z]*";
	private static String updateSqlId = ".*\\.update[A-Za-z]*";

	/**
	 * 
	 * 拦截代理方法
	 * 
	 * @param invocation
	 *            拦截代理参数
	 * @exception Throwable
	 *                抛出异常
	 * @return 返回代理的对象
	 */
	public Object intercept(Invocation invocation) throws Throwable {

		if (invocation.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation
					.getTarget();

			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper
					.getFieldValue(statementHandler, "delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectHelper
					.getFieldValue(delegate, "mappedStatement");

			String queryId = mappedStatement.getId();

			// 分页
			if (queryId.matches(pageSqlId)) {

				this.buildPageQuery(invocation, delegate, mappedStatement);

			}

			if (queryId.contains("save")) {
				if (queryId.matches(insertSqlId)) {
					this.buildInsertExtEntity(delegate);
				}
			}
			// 实体扩展
			if (queryId.matches(insertSqlId)) {

				this.buildInsertExtEntity(delegate);

			}

			// 实体扩展
			if (queryId.matches(updateSqlId)) {

				this.buildUpdateExtEntity(delegate);

			}
		}
		return invocation.proceed();
	}

	/**
	 * 
	 * 构建分页
	 * 
	 * @param invocation
	 * @param delegate
	 * @param mappedStatement
	 * @throws Throwable
	 */
	public void buildPageQuery(Invocation invocation,
			BaseStatementHandler delegate, MappedStatement mappedStatement)
			throws Throwable {

		if (invocation == null || delegate == null || mappedStatement == null) {
			return;
		}

		// 拦截需要分页的SQL
		BoundSql boundSql = delegate.getBoundSql();
		// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
		if (boundSql != null) {
			Object parameterObject = boundSql.getParameterObject();

			if (parameterObject == null) {
				throw new NullPointerException("parameterObject尚未实例化！");
			} else {

				Connection conn = (Connection) invocation.getArgs()[0];

				String sql = boundSql.getSql();
				PreparedStatement preparedStatement=null;
				if("sqlserver".equals(dialect)){
					preparedStatement = conn
					.prepareStatement(new StringBuilder(
							"select count(0) from (").append(sql.toUpperCase().replaceFirst("SELECT", "SELECT TOP 100 PERCENT")).append(
							") tmp_count").toString());
				}else{
					preparedStatement = conn
					.prepareStatement(new StringBuilder(
							"select count(0) from (").append(sql).append(
							") tmp_count").toString());
				}
				this.setParameters(preparedStatement, mappedStatement,
						boundSql, parameterObject);

				ResultSet resultSet = preparedStatement.executeQuery();

				int totalCount = 0;

				if (resultSet.next()) {
					totalCount = resultSet.getInt(1);
				}

				resultSet.close();
				preparedStatement.close();

				Page page = null;

				if (parameterObject instanceof Page) {
					page = (Page) parameterObject;

					page.setTotalCount(totalCount);

					String pageSql = generatePageSql(sql, page);
					// 将分页sql语句反射回BoundSql.
					ReflectHelper.setFieldValue(boundSql, "sql", pageSql);
				}

			}
		}

	}

	/**
	 * 构建扩展的实体信息
	 * 
	 * @param delegate
	 */
	public void buildInsertExtEntity(BaseStatementHandler delegate) {

		if (delegate == null) {
			return;
		}

		// 拦截需要分页的SQL
		BoundSql boundSql = delegate.getBoundSql();
		// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
		if (boundSql != null) {
			Object parameterObject = boundSql.getParameterObject();

			if (parameterObject == null) {
				throw new NullPointerException("parameterObject尚未实例化！");
			} else {

				if (parameterObject instanceof ExtEntity) {
					ExtEntity entity = (ExtEntity) parameterObject;

					/*TSUser currentUser = null;
					try {
						currentUser = ClientUtil.getSessionUserName();
						entity.setCreateTime(new Date());
						entity.setCreateUserId(currentUser.getId());
						entity.setCreateUserName(currentUser.getUserName());
					} catch (RuntimeException e) {
						logger.warn("当前session为空,无法获取用户");
					}*/
				}

			}
		}
	}

	/**
	 * 构建扩展的实体信息
	 * 
	 * @param delegate
	 */
	public void buildUpdateExtEntity(BaseStatementHandler delegate) {

		if (delegate == null) {
			return;
		}

		// 拦截需要分页的SQL
		BoundSql boundSql = delegate.getBoundSql();
		// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
		if (boundSql != null) {
			Object parameterObject = boundSql.getParameterObject();

			if (parameterObject == null) {
				throw new NullPointerException("parameterObject尚未实例化！");
			} else {

				if (parameterObject instanceof ExtEntity) {
					ExtEntity entity = (ExtEntity) parameterObject;
					/*TSUser currentUser = null;
					try {
						currentUser = ResourceUtil.getSessionUserName();
						entity.setUpdateTime(new Date());
						entity.setUpdateUserId(currentUser.getId());
						entity.setUpdateUserName(currentUser.getUserName());
					} catch (RuntimeException e) {
						logger.warn("当前session为空,无法获取用户");
					}*/
					
				}

			}
		}
	}

	/**
	 * 
	 * 包装拦截的对象
	 * 
	 * @param object
	 *            被拦截对象
	 * @return 拦截后的对象
	 */
	public Object plugin(Object object) {
		return Plugin.wrap(object, this);
	}

	/**
	 * 配置插件对应配置参数
	 * 
	 * @param properties
	 *            配置参数集合
	 */
	public void setProperties(Properties properties) {
    	
		if (properties != null) {
			this.dialect=properties.getProperty("database.dialect");
			this.pageSqlId = properties.getProperty("page.regex");
			this.insertSqlId = properties.getProperty("insert.regex");
			this.updateSqlId = properties.getProperty("update.regex");

		}
	}

	/**
	 * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.
	 * DefaultParameterHandler
	 * 
	 * @param preparedStatement
	 *            数据库预编译Statement
	 * @param mappedStatement
	 *            MyBatis3.x映射Statement
	 * @param boundSql
	 *            被拦截Mapping的SQL包装对象
	 * @param parameterObject
	 *            参数对象
	 * @throws SQLException
	 *             SQL异常
	 */
	@SuppressWarnings("unchecked")
	private void setParameters(PreparedStatement preparedStatement,
			MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(
				mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql
				.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration
					.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null
					: configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);

				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry
							.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName
							.startsWith(ForEachSqlNode.ITEM_PREFIX)
							&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value)
									.getValue(
											propertyName.substring(prop
													.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject
								.getValue(propertyName);
					}
					TypeHandler typeHandler = parameterMapping.getTypeHandler();

					if (typeHandler == null) {
						throw new ExecutorException(
								"There was no TypeHandler found for parameter "
										+ propertyName + " of statement "
										+ mappedStatement.getId());
					}

					typeHandler.setParameter(preparedStatement, i + 1, value,
							parameterMapping.getJdbcType());

				}
			}
		}
	}

	/**
	 * 根据数据库方言，生成特定的分页sql
	 * 
	 * @param sql
	 *            原始SQL
	 * @param page
	 *            分页参数对象
	 * @return 编译后的分页sql
	 */
	private String generatePageSql(String sql, Page page) {

		return PageSqlUtil.generatePageSql(dialect, sql, page);
	}

}