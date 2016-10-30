package com.xplatform.base.framework.core.common.dao.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

import com.xplatform.base.framework.core.annotation.JeecgEntityTitle;
import com.xplatform.base.framework.core.common.dao.BaseDao;
import com.xplatform.base.framework.core.common.dao.jdbc.JdbcDao;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.hibernate.qbc.DetachedCriteriaUtil;
import com.xplatform.base.framework.core.common.hibernate.qbc.HqlQuery;
import com.xplatform.base.framework.core.common.hibernate.qbc.PageList;
import com.xplatform.base.framework.core.common.hibernate.qbc.PagerUtil;
import com.xplatform.base.framework.core.common.model.common.DBTable;
import com.xplatform.base.framework.core.common.model.common.UploadFile;
import com.xplatform.base.framework.core.common.model.json.ComboTree;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.model.json.TreeGrid;
import com.xplatform.base.framework.core.extend.template.DataSourceMap;
import com.xplatform.base.framework.core.extend.template.Template;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.ReflectHelper;
import com.xplatform.base.framework.core.util.ToEntityUtil;
import com.xplatform.base.framework.core.util.oConvertUtils;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.framework.tag.vo.datatable.DataTableReturn;
import com.xplatform.base.framework.tag.vo.easyui.ComboTreeModel;
import com.xplatform.base.framework.tag.vo.easyui.TreeGridModel;
/**
 * 
 * 类描述： DAO层泛型基类
 * 
 * 张代浩
 * @date： 日期：2012-12-7 时间：上午10:16:48
 * @param <T>
 * @param <PK>
 * @version 1.0
 */
public abstract class BaseDaoImpl<T> implements BaseDao<T> {
	/**
	 * 初始化Log4j的一个实例
	 */
	private static final Logger logger = Logger.getLogger(BaseDaoImpl.class);
	/** 实体类类型 */
	private Class<T> entityClass;
	
	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		java.lang.reflect.Type type = getClass().getGenericSuperclass();
		java.lang.reflect.Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
		entityClass = (Class<T>) parameterizedType[0];
	}
	/**
	 * 注入一个sessionFactory属性,并注入到父类(HibernateDaoSupport)
	 * **/
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	public Session getSession() {
		// 事务必须是开启的(Required)，否则获取不到
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 获得该类的属性和类型
	 * 
	 * @param entityName
	 *            注解的实体类
	 */
	private <T> void getProperty(Class entityName) {
		ClassMetadata cm = sessionFactory.getClassMetadata(entityName);
		String[] str = cm.getPropertyNames(); // 获得该类所有的属性名称
		for (int i = 0; i < str.length; i++) {
			String property = str[i];
			String type = cm.getPropertyType(property).getName(); // 获得该名称的类型
			com.xplatform.base.framework.core.util.LogUtil.info(property + "---&gt;" + type);
		}
	}

	/**
	 * 获取所有数据表
	 * 
	 * @return
	 */
	public List<DBTable> getAllDbTableName() {
		List<DBTable> resultList = new ArrayList<DBTable>();
		SessionFactory factory = getSession().getSessionFactory();
		Map<String, ClassMetadata> metaMap = factory.getAllClassMetadata();
		for (String key : (Set<String>) metaMap.keySet()) {
			DBTable dbTable = new DBTable();
			AbstractEntityPersister classMetadata = (AbstractEntityPersister) metaMap
					.get(key);
			dbTable.setTableName(classMetadata.getTableName());
			dbTable.setEntityName(classMetadata.getEntityName());
			Class<?> c;
			try {
				c = Class.forName(key);
				JeecgEntityTitle t = c.getAnnotation(JeecgEntityTitle.class);
				dbTable.setTableTitle(t != null ? t.name() : "");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			resultList.add(dbTable);
		}
		return resultList;
	}

	/**
	 * 获取所有数据表
	 * 
	 * @return
	 */
	public Integer getAllDbTableSize() {
		SessionFactory factory = getSession().getSessionFactory();
		Map<String, ClassMetadata> metaMap = factory.getAllClassMetadata();
		return metaMap.size();
	}

	/**
	 * 根据实体名字获取唯一记录
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public T findUniqueByProperty(
			String propertyName, Object value) {
		Assert.hasText(propertyName);
		return (T) createCriteria(
				Restrictions.eq(propertyName, value)).uniqueResult();
	}
	
	/**
	 * 按多属性查找对象列表.
	 */
	public T findUniqueByPropertys(
			Map<String,String> param) {
		
		Criteria criteria = getSession().createCriteria(entityClass);
		for(Entry<String,String> entry:param.entrySet()){
			Criterion criterion = Restrictions.eq(entry.getKey(), entry.getValue());
			criteria.add(criterion);
		}
		return (T) criteria.uniqueResult();
	}

	/**
	 * 按属性查找对象列表.
	 */
	public List<T> findByProperty(
			String propertyName, Object value) {
		Assert.hasText(propertyName);
		return (List<T>) createCriteria(
				Restrictions.eq(propertyName, value)).list();
	}
	
	public <T> List<T> findByProperty(Class entityClass,
			String propertyName, Object value){
		
		Criteria criteria = getSession().createCriteria(entityClass);
		criteria.add(Restrictions.eq(propertyName, value));
		return (List<T>) criteria.list();
	}
	
	/**
	 * 按多属性查找对象列表.
	 */
	public List<T> findByPropertys(Map<String,Object> param) {
		
		Criteria criteria = getSession().createCriteria(entityClass);
		for(Entry<String,Object> entry:param.entrySet()){
			Criterion criterion = Restrictions.eq(entry.getKey(), entry.getValue());
			criteria.add(criterion);
		}
		return (List<T>) criteria.list();
	}
	
	public <T> List<T> findByPropertys(Class entityClass,Map<String,Object> param) {
		
		Criteria criteria = getSession().createCriteria(entityClass);
		for(Entry<String,Object> entry:param.entrySet()){
			Criterion criterion = Restrictions.eq(entry.getKey(), entry.getValue());
			criteria.add(criterion);
		}
		return (List<T>) criteria.list();
	}
	
	public List<T> findByObjectPropertys(
			Map<String,Object> param) {
		
		Criteria criteria = getSession().createCriteria(entityClass);
		for(Entry<String,Object> entry:param.entrySet()){
			Criterion criterion = Restrictions.eq(entry.getKey(), entry.getValue());
			criteria.add(criterion);
		}
		return (List<T>) criteria.list();
	}
	
	public <T> List<T> findByObjectPropertys(Class entityClass,
			Map<String,Object> param) {
		
		Criteria criteria = getSession().createCriteria(entityClass);
		for(Entry<String,Object> entry:param.entrySet()){
			Criterion criterion = Restrictions.eq(entry.getKey(), entry.getValue());
			criteria.add(criterion);
		}
		return (List<T>) criteria.list();
	}
	
	public List<T> findByPropertysisOrder(Map<String,String> param, boolean isAsc,List<String> orderPropertys){
		Criteria criteria = getSession().createCriteria(entityClass);
		for(Entry<String,String> entry:param.entrySet()){
			Criterion criterion = Restrictions.eq(entry.getKey(), entry.getValue());
			criteria.add(criterion);
		}
		if (isAsc) {
			if(orderPropertys!=null && orderPropertys.size()>0){
				for(String orderPorperty:orderPropertys){
					criteria.addOrder(Order.asc(orderPorperty));
				}
			}
		} else {
			if(orderPropertys!=null && orderPropertys.size()>0){
				for(String orderPorperty:orderPropertys){
					criteria.addOrder(Order.desc(orderPorperty));
				}
			}
		}
		
		return (List<T>) criteria.list();
	}
	
	public <T> List<T> findByPropertysisOrder(Class entityClass,Map<String,Object> param, boolean isAsc,List<String> orderPropertys){
		Criteria criteria = getSession().createCriteria(entityClass);
		for(Entry<String,Object> entry:param.entrySet()){
			Criterion criterion = Restrictions.eq(entry.getKey(), entry.getValue());
			criteria.add(criterion);
		}
		if (isAsc) {
			if(orderPropertys!=null && orderPropertys.size()>0){
				for(String orderPorperty:orderPropertys){
					criteria.addOrder(Order.asc(orderPorperty));
				}
			}
		} else {
			if(orderPropertys!=null && orderPropertys.size()>0){
				for(String orderPorperty:orderPropertys){
					criteria.addOrder(Order.desc(orderPorperty));
				}
			}
		}
		
		return (List<T>) criteria.list();
	}
	
	/**
	 * 根据传入的实体持久化对象
	 */
	public <T> Serializable save(T entity) {
		try {
			Serializable id = getSession().save(entity);
			getSession().flush();
			if (logger.isDebugEnabled()) {
				logger.debug("保存实体成功," + entity.getClass().getName());
			}
			return id;
		} catch (RuntimeException e) {
			logger.error("保存实体异常", e);
			throw e;
		}

	}

	/**
	 * 批量保存数据
	 * 
	 * @param <T>
	 * @param entitys
	 *            要持久化的临时实体对象集合
	 */
	public void batchSave(List<T> entitys) {
		for (int i = 0; i < entitys.size(); i++) {
			getSession().save(entitys.get(i));
			if (i % 20 == 0) {
				// 20个对象后才清理缓存，写入数据库
				getSession().flush();
				getSession().clear();
			}
		}
		// 最后清理一下----防止大于20小于40的不保存
		getSession().flush();
		getSession().clear();
	}
	
	/**
	 * 批量保存或修改数据
	 * 
	 * @param <T>
	 * @param entitys
	 *            要持久化/维护的临时实体对象集合
	 */
	public void batchSaveOrUpdate(List<T> entitys) {
		for (int i = 0; i < entitys.size(); i++) {
			getSession().saveOrUpdate(entitys.get(i));
			if (i % 20 == 0) {
				// 20个对象后才清理缓存，写入数据库
				getSession().flush();
				getSession().clear();
			}
		}
		// 最后清理一下----防止大于20小于40的不保存
		getSession().flush();
		getSession().clear();
	}

	/**
	 * 根据传入的实体添加或更新对象
	 * 
	 * @param <T>
	 * 
	 * @param entity
	 */

	public void saveOrUpdate(T entity) {
		try {
			getSession().saveOrUpdate(entity);
			getSession().flush();
			if (logger.isDebugEnabled()) {
				logger.debug("添加或更新成功," + entity.getClass().getName());
			}
		} catch (RuntimeException e) {
			logger.error("添加或更新异常", e);
			throw e;
		}
	}

	/**
	 * 根据传入的实体删除对象
	 */
	public void delete(T entity) {
		try {
			getSession().delete(entity);
			getSession().flush();
			if (logger.isDebugEnabled()) {
				logger.debug("删除成功," + entity.getClass().getName());
			}
		} catch (RuntimeException e) {
			logger.error("删除异常", e);
			throw e;
		}
	}

	/**
	 * 根据主键删除指定的实体
	 * 
	 * @param <T>
	 * @param pojo
	 */
	public void deleteEntityById(Serializable id) {
		delete(get(id));
		getSession().flush();
	}

	/**
	 * 删除全部的实体
	 * 
	 * @param <T>
	 * 
	 * @param entitys
	 */
	public void deleteAllEntitie(Collection<T> entitys) {
		for (Object entity : entitys) {
			getSession().delete(entity);
			getSession().flush();
		}
	}

	/**
	 * 根据Id获取对象。
	 */
	public T get(final Serializable id) {

		return (T) getSession().get(entityClass, id);

	}
	
	public <T> T get(Class className,final Serializable id) {

		return (T) getSession().get(className, id);

	}

	/**
	 * 根据主键获取实体并加锁。
	 * 
	 * @param <T>
	 * @param entityName
	 * @param id
	 * @param lock
	 * @return
	 */
	public T getEntity( Serializable id) {

		T t = (T) getSession().get(entityClass, id);
		if (t != null) {
			getSession().flush();
		}
		return t;
	}

	/**
	 * 更新指定的实体
	 * 
	 * @param <T>
	 * @param pojo
	 */
	public void updateEntitie(T pojo) {
		getSession().update(pojo);
		getSession().flush();
	}
	
	public void merge(T pojo) {
		getSession().merge(pojo);
		getSession().flush();
	}

	/**
	 * 更新指定的实体
	 * 
	 * @param <T>
	 * @param pojo
	 */
	public <T> void updateEntitie(String className, Object id) {
		getSession().update(className, id);
		getSession().flush();
	}

	/**
	 * 根据主键更新实体
	 */
	public void updateEntityById(Serializable id) {
		updateEntitie(get(id));
	}

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public List<T> findByQueryString(final String query) {

		Query queryObject = getSession().createQuery(query);
		List<T> list = queryObject.list();
		if (list.size() > 0) {
			getSession().flush();
		}
		return list;

	}

	/**
	 * 通过hql查询唯一对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public T singleResult(String hql) {
		T t = null;
		Query queryObject = getSession().createQuery(hql);
		List<T> list = queryObject.list();
		if (list.size() == 1) {
			getSession().flush();
			t = list.get(0);
		} else if (list.size() > 0) {
			throw new BusinessRuntimeException("查询结果数:" + list.size() + "大于1");
		}
		return t;
	}

	/**
	 * 通过hql 查询语句查找HashMap对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public Map<Object, Object> getHashMapbyQuery(String hql) {

		Query query = getSession().createQuery(hql);
		List list = query.list();
		Map<Object, Object> map = new HashMap<Object, Object>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object[] tm = (Object[]) iterator.next();
			map.put(tm[0].toString(), tm[1].toString());
		}
		return map;

	}

	/**
	 * 通过sql更新记录
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public int updateBySqlString(final String query) {

		Query querys = getSession().createSQLQuery(query);
		return querys.executeUpdate();
	}

	/**
	 * 通过sql查询语句查找对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public List<T> findListbySql(final String sql) {
		Query querys = getSession().createSQLQuery(sql);
		return querys.list();
	}

	/**
	 * 创建Criteria对象，有排序功能。
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param orderBy
	 * @param isAsc
	 * @param criterions
	 * @return
	 */
	private <T> Criteria createCriteria(boolean isAsc,List<String> orderPorpertys,
			Criterion... criterions) {
		Criteria criteria = createCriteria(criterions);
		if (isAsc) {
			if(orderPorpertys!=null && orderPorpertys.size()>0){
				for(String orderPorperty:orderPorpertys){
					criteria.addOrder(Order.asc(orderPorperty));
				}
			}
		} else {
			if(orderPorpertys!=null && orderPorpertys.size()>0){
				for(String orderPorperty:orderPorpertys){
					criteria.addOrder(Order.desc(orderPorperty));
				}
			}
			
		}
		return criteria;
	}

	/**
	 * 创建Criteria对象带属性比较
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param criterions
	 * @return
	 */
	private Criteria createCriteria(
			Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	public List<T> loadAll() {
		Criteria criteria = createCriteria();
		return criteria.list();
	}

	/**
	 * 创建单一Criteria对象
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param criterions
	 * @return
	 */
	private <T> Criteria createCriteria() {
		Criteria criteria = getSession().createCriteria(entityClass);
		return criteria;
	}

	/**
	 * 根据属性名和属性值查询. 有排序
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param orderBy
	 * @param isAsc
	 * @return
	 */
	public List<T> findByPropertyisOrder(
			String propertyName, Object value, boolean isAsc,List<String> orderPorpertys) {
		Assert.hasText(propertyName);
		return createCriteria(isAsc,orderPorpertys,Restrictions.eq(propertyName, value)).list();
	}

	/**
	 * 根据属性名和属性值 查询 且要求对象唯一.
	 * 
	 * @return 符合条件的唯一对象.
	 */
	public T findUniqueBy(String propertyName,
			Object value) {
		Assert.hasText(propertyName);
		return (T) createCriteria(
				Restrictions.eq(propertyName, value)).uniqueResult();
	}

	/**
	 * 根据查询条件与参数列表创建Query对象
	 * 
	 * @param session
	 *            Hibernate会话
	 * @param hql
	 *            HQL语句
	 * @param objects
	 *            参数列表
	 * @return Query对象
	 */
	public Query createQuery(Session session, String hql, Object... objects) {
		Query query = session.createQuery(hql);
		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				query.setParameter(i, objects[i]);
			}
		}
		return query;
	}

	/**
	 * 批量插入实体
	 * 
	 * @param clas
	 * @param values
	 * @return
	 */
	public <T> int batchInsertsEntitie(List<T> entityList) {
		int num = 0;
		for (int i = 0; i < entityList.size(); i++) {
			save(entityList.get(i));
			num++;
		}
		return num;
	}

	/**
	 * 根据实体名返回全部对象
	 * 
	 * @param <T>
	 * @param hql
	 * @param size
	 * @return
	 */
	/**
	 * 使用占位符的方式填充值 请注意：like对应的值格式："%"+username+"%" Hibernate Query
	 * 
	 * @param hibernateTemplate
	 * @param hql
	 * @param valus
	 *            占位符号?对应的值，顺序必须一一对应 可以为空对象数组，但是不能为null
	 * @return 2008-07-19 add by liuyang
	 */
	public List<T> executeQuery(final String hql, final Object[] values) {
		Query query = getSession().createQuery(hql);
		// query.setCacheable(true);
		for (int i = 0; values != null && i < values.length; i++) {
			query.setParameter(i, values[i]);
		}

		return query.list();

	}

	/**
	 * 根据实体模版查找
	 * 
	 * @param entityName
	 * @param exampleEntity
	 * @return
	 */

	public List findByExample(
			final Object exampleEntity) {
		Assert.notNull(exampleEntity, "Example entity must not be null");
		Criteria executableCriteria = (entityClass != null ? getSession()
				.createCriteria(entityClass) : getSession().createCriteria(
				exampleEntity.getClass()));
		executableCriteria.add(Example.create(exampleEntity));
		return executableCriteria.list();
	}

	// 使用指定的检索标准获取满足标准的记录数
	public Integer getRowCount(DetachedCriteria criteria) {
		return oConvertUtils.getInt(((Criteria) criteria
				.setProjection(Projections.rowCount())).uniqueResult(), 0);
	}

	/**
	 * 调用存储过程
	 */
	public void callableStatementByName(String proc) {
	}

	/**
	 * 查询指定实体的总记录数
	 * 
	 * @param clazz
	 * @return
	 */
	public int getCount() {

		int count = DataAccessUtils.intResult(getSession().createQuery(
				"select count(*) from " + entityClass.getName()).list());
		return count;
	}

	/**
	 * 获取分页记录CriteriaQuery 老方法final int allCounts =
	 * oConvertUtils.getInt(criteria
	 * .setProjection(Projections.rowCount()).uniqueResult(), 0);
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageList(final CriteriaQuery cq, final boolean isOffset) {

		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
				getSession());
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		final int allCounts = ((Long) criteria.setProjection(
				Projections.rowCount()).uniqueResult()).intValue();
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}

		// 判断是否有排序字段
		if (cq.getOrdermap() != null) {
			cq.setOrder(cq.getOrdermap());
		}
		int pageSize = cq.getPageSize();// 每页显示数
		int curPageNO = PagerUtil.getcurPageNo(allCounts, cq.getCurPage(),
				pageSize);// 当前页
		int offset = PagerUtil.getOffset(allCounts, curPageNO, pageSize);
		String toolBar = "";
		if (isOffset) {// 是否分页
			criteria.setFirstResult(offset);
			criteria.setMaxResults(cq.getPageSize());
			if (cq.getIsUseimage() == 1) {
				toolBar = PagerUtil.getBar(cq.getMyAction(), cq.getMyForm(),
						allCounts, curPageNO, pageSize, cq.getMap());
			} else {
				toolBar = PagerUtil.getBar(cq.getMyAction(), allCounts,
						curPageNO, pageSize, cq.getMap());
			}
		} else {
			pageSize = allCounts;
		}
		return new PageList(criteria.list(), toolBar, offset, curPageNO,
				allCounts);
	}

	/**
	 * 返回JQUERY datatables DataTableReturn模型对象
	 */
	public DataTableReturn getDataTableReturn(final CriteriaQuery cq,
			final boolean isOffset) {

		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
				getSession());
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		final int allCounts = ((Long) criteria.setProjection(
				Projections.rowCount()).uniqueResult()).intValue();
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}

		// 判断是否有排序字段
		if (cq.getOrdermap() != null) {
			cq.setOrder(cq.getOrdermap());
		}
		int pageSize = cq.getPageSize();// 每页显示数
		int curPageNO = PagerUtil.getcurPageNo(allCounts, cq.getCurPage(),
				pageSize);// 当前页
		int offset = PagerUtil.getOffset(allCounts, curPageNO, pageSize);
		if (isOffset) {// 是否分页
			criteria.setFirstResult(offset);
			criteria.setMaxResults(cq.getPageSize());
		} else {
			pageSize = allCounts;
		}
		DetachedCriteriaUtil.selectColumn(cq.getDetachedCriteria(), cq
				.getField().split(","), cq.getEntityClass(), false);
		return new DataTableReturn(allCounts, allCounts, cq.getDataTables()
				.getEcho(), criteria.list());
	}

	/**
	 * 返回easyui datagrid DataGridReturn模型对象
	 */
	public DataGridReturn getDataGridReturn(final CriteriaQuery cq,
			final boolean isOffset) {
		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
				getSession());
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		final int allCounts = ((Long) criteria.setProjection(
				Projections.rowCount()).uniqueResult()).intValue();
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (StringUtils.isNotBlank(cq.getDataGrid().getSort())) {
			cq.addOrder(cq.getDataGrid().getSort(), cq.getDataGrid().getOrder());
		}

		// 判断是否有排序字段
		if (!cq.getOrdermap().isEmpty()) {
			cq.setOrder(cq.getOrdermap());
		}
		int pageSize = cq.getPageSize();// 每页显示数
		int curPageNO = PagerUtil.getcurPageNo(allCounts, cq.getCurPage(),
				pageSize);// 当前页
		int offset = PagerUtil.getOffset(allCounts, curPageNO, pageSize);
		if (isOffset) {// 是否分页
			criteria.setFirstResult(offset);
			criteria.setMaxResults(cq.getPageSize());
		} else {
			pageSize = allCounts;
		}
		// DetachedCriteriaUtil.selectColumn(cq.getDetachedCriteria(),
		// cq.getField().split(","), cq.getClass1(), false);
		List list = criteria.list();
		cq.getDataGrid().setResults(list);
		cq.getDataGrid().setTotal(allCounts);
		return new DataGridReturn(allCounts, list);
	}

	/**
	 * 获取分页记录SqlQuery
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageList getPageListBySql(final HqlQuery hqlQuery,
			final boolean isToEntity) {

		Query query = getSession().createSQLQuery(hqlQuery.getQueryString());

		// query.setParameters(hqlQuery.getParam(), (Type[])
		// hqlQuery.getTypes());
		int allCounts = query.list().size();
		int curPageNO = hqlQuery.getCurPage();
		int offset = PagerUtil.getOffset(allCounts, curPageNO,
				hqlQuery.getPageSize());
		query.setFirstResult(offset);
		query.setMaxResults(hqlQuery.getPageSize());
		List list = null;
		if (isToEntity) {
			list = ToEntityUtil.toEntityList(query.list(),
					hqlQuery.getClass1(), hqlQuery.getDataGrid().getField()
							.split(","));
		} else {
			list = query.list();
		}
		return new PageList(hqlQuery, list, offset, curPageNO, allCounts);
	}

	/**
	 * 获取分页记录HqlQuery
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageList getPageList(final HqlQuery hqlQuery,
			final boolean needParameter) {

		Query query = getSession().createQuery(hqlQuery.getQueryString());
		if (needParameter) {
			query.setParameters(hqlQuery.getParam(),
					(Type[]) hqlQuery.getTypes());
		}
		int allCounts = query.list().size();
		int curPageNO = hqlQuery.getCurPage();
		int offset = PagerUtil.getOffset(allCounts, curPageNO,
				hqlQuery.getPageSize());
		String toolBar = PagerUtil.getBar(hqlQuery.getMyaction(), allCounts,
				curPageNO, hqlQuery.getPageSize(), hqlQuery.getMap());
		query.setFirstResult(offset);
		query.setMaxResults(hqlQuery.getPageSize());
		return new PageList(query.list(), toolBar, offset, curPageNO, allCounts);
	}

	/**
	 * 根据CriteriaQuery获取List
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getListByCriteriaQuery(final CriteriaQuery cq, Boolean ispage) {
		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
				getSession());
		// 判断是否有排序字段
		if (cq.getOrdermap() != null) {
			cq.setOrder(cq.getOrdermap());
		}
		if (ispage)
			criteria.setMaxResults(cq.getPageSize());
		return criteria.list();

	}

	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * 使用指定的检索标准检索数据并分页返回数据
	 */
	public List<Map<String, Object>> findForJdbc(String sql, int page, int rows) {
		// 封装分页SQL
		sql = JdbcDao.jeecgCreatePageSql(sql, page, rows);
		return this.jdbcTemplate.queryForList(sql);
	}

	/**
	 * 使用指定的检索标准检索数据并分页返回数据
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public List<T> findObjForJdbc(String sql, int page, int rows) {
		List<T> rsList = new ArrayList<T>();
		// 封装分页SQL
		sql = JdbcDao.jeecgCreatePageSql(sql, page, rows);
		List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);

		T po = null;
		for (Map<String, Object> m : mapList) {
			try {
				po = entityClass.newInstance();
				MyBeanUtils.copyMap2Bean_Nobig(po, m);
				rsList.add(po);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rsList;
	}

	/**
	 * 使用指定的检索标准检索数据并分页返回数据-采用预处理方式
	 * 
	 * @param criteria
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String, Object>> findForJdbcParam(String sql, int page,
			int rows, Object... objs) {
		// 封装分页SQL
		sql = JdbcDao.jeecgCreatePageSql(sql, page, rows);
		return this.jdbcTemplate.queryForList(sql, objs);
	}

	/**
	 * 使用指定的检索标准检索数据并分页返回数据For JDBC
	 */
	public Long getCountForJdbc(String sql) {
		return this.jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 使用指定的检索标准检索数据并分页返回数据For JDBC-采用预处理方式
	 * 
	 */
	public Long getCountForJdbcParam(String sql, Object[] objs) {
		return this.jdbcTemplate.queryForLong(sql, objs);
	}

	public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
		return this.jdbcTemplate.queryForList(sql, objs);
	}

	public Integer executeSql(String sql, List<Object> param) {
		return this.jdbcTemplate.update(sql, param);
	}

	public Integer executeSql(String sql, Object... param) {
		return this.jdbcTemplate.update(sql, param);
	}

	public Integer executeSql(String sql, Map<String, Object> param) {
		return this.namedParameterJdbcTemplate.update(sql, param);
	}

	public Object executeSqlReturnKey(final String sql, Map<String, Object> param) throws BusinessException {
		Object keyValue = null;
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource sqlp = new MapSqlParameterSource(param);
			this.namedParameterJdbcTemplate.update(sql, sqlp, keyHolder);
			if (keyHolder.getKey() != null) {
				keyValue = keyHolder.getKey().longValue();
			}
		} catch (Exception e) {
			throw new BusinessException("保存失败");
		}
		return keyValue;
	}
	public Integer countByJdbc(String sql, Object... param) {
		return this.jdbcTemplate.queryForInt(sql, param);
	}

	public Map<String, Object> findOneForJdbc(String sql, Object... objs) {
		try {
			return this.jdbcTemplate.queryForMap(sql, objs);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public List<T> findHql(String hql, Object... param) {
		Query q = getSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.list();
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年6月21日 上午10:41:13
	 * @Decription 通过hql 查询语句查找唯一对象
	 *
	 * @param hql
	 * @param param
	 * @return
	 */
		
	public T findUniqueByHql(String hql,Object... param){
		List<T> lists=this.findHql(hql, param);
		if(lists.size()>0){
			return lists.get(0);
		}
		return null;
	}
	/**
	 * 执行HQL语句操作更新
	 * 
	 * @param hql
	 * @return
	 */
	public Integer executeHql(String hql) {
		Query q = getSession().createQuery(hql);
		return q.executeUpdate();
	}
	
	public Integer executeHql(String hql, Object... param) {
		Query q = getSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.executeUpdate();
	}

	public List<T> pageList(DetachedCriteria dc, int firstResult,
			int maxResult) {
		Criteria criteria = dc.getExecutableCriteria(getSession());
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResult);
		return criteria.list();
	}

	/**
	 * 离线查询
	 */
	public List<T> findByDetached(DetachedCriteria dc) {
		return dc.getExecutableCriteria(getSession()).list();
	}
	
	public String getRoot(String id) {
		T entity = get(id);
		Method m;
		String rootId = "";
		try {
			String treeIndex = ReflectHelper.getFieldValue(entity, "treeIndex").toString();

			//获得第一次出现分割符的地方,获得根节点Id
			if (treeIndex.indexOf(",") != -1) {
				rootId = treeIndex.substring(0, treeIndex.indexOf(","));
			} else {
				rootId = id;
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rootId;
	}
	
	@SuppressWarnings("unchecked")
	public HttpServletResponse viewOrDownloadFile(UploadFile uploadFile) {
		uploadFile.getResponse().setContentType("UTF-8");
		uploadFile.getResponse().setCharacterEncoding("UTF-8");
		InputStream bis = null;
		BufferedOutputStream bos = null;
		HttpServletResponse response = uploadFile.getResponse();
		HttpServletRequest request = uploadFile.getRequest();
		String ctxPath = request.getSession().getServletContext().getRealPath("/");
		String downLoadPath = "";
		long fileLength = 0;
		if (uploadFile.getRealPath() != null&&uploadFile.getContent() == null) {
			downLoadPath = ctxPath + uploadFile.getRealPath();
			fileLength = new File(downLoadPath).length();
			try {
				bis = new BufferedInputStream(new FileInputStream(downLoadPath));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			if (uploadFile.getContent() != null)
				bis = new ByteArrayInputStream(uploadFile.getContent());
			fileLength = uploadFile.getContent().length;
		}
		try {
			if (!uploadFile.isView() && uploadFile.getExtend() != null) {
				if (uploadFile.getExtend().equals("text")) {
					response.setContentType("text/plain;");
				} else if (uploadFile.getExtend().equals("doc")) {
					response.setContentType("application/msword;");
				} else if (uploadFile.getExtend().equals("xls")) {
					response.setContentType("application/ms-excel;");
				} else if (uploadFile.getExtend().equals("pdf")) {
					response.setContentType("application/pdf;");
				} else if (uploadFile.getExtend().equals("jpg") || uploadFile.getExtend().equals("jpeg")) {
					response.setContentType("image/jpeg;");
				} else {
					response.setContentType("application/x-msdownload;");
				}
				response.setHeader("Content-disposition", "attachment; filename=" + new String((uploadFile.getTitleField() + "." + uploadFile.getExtend()).getBytes("GBK"), "ISO8859-1"));
				response.setHeader("Content-Length", String.valueOf(fileLength));
			}
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}

	public Map<Object, Object> getDataSourceMap(Template template) {
		return DataSourceMap.getDataSourceMap();
	}

	/**
	 * 根据模型生成ComboTree JSON
	 * 
	 * @param all全部对象
	 * @param in已拥有的对象
	 * @param comboTreeModel模型
	 * @return
	 */
	public List<ComboTree> ComboTree(List all, ComboTreeModel comboTreeModel, List in) {
		List<ComboTree> trees = new ArrayList<ComboTree>();
		for (Object obj : all) {
			trees.add(comboTree(obj, comboTreeModel, in, false));
		}
		return trees;

	}

	// 构建ComboTree
	private ComboTree comboTree(Object obj, ComboTreeModel comboTreeModel, List in, boolean recursive) {
		ComboTree tree = new ComboTree();
		Map<String, Object> attributes = new HashMap<String, Object>();
		ReflectHelper reflectHelper = new ReflectHelper(obj);
		String id = oConvertUtils.getString(reflectHelper.getMethodValue(comboTreeModel.getIdField()));
		tree.setId(id);
		tree.setText(oConvertUtils.getString(reflectHelper.getMethodValue(comboTreeModel.getTextField())));
		if (comboTreeModel.getSrcField() != null) {
			attributes.put("href", oConvertUtils.getString(reflectHelper.getMethodValue(comboTreeModel.getSrcField())));
			tree.setAttributes(attributes);
		}
		if (in == null) {
		} else {
			if (in.size() > 0) {
				for (Object inobj : in) {
					ReflectHelper reflectHelper2 = new ReflectHelper(inobj);
					String inId = oConvertUtils.getString(reflectHelper2.getMethodValue(comboTreeModel.getIdField()));
					if (inId == id) {
						tree.setChecked(true);
					}
				}
			}
		}
		List tsFunctions = (List) reflectHelper.getMethodValue(comboTreeModel.getChildField());
		if (tsFunctions != null && tsFunctions.size() > 0) {
			tree.setState("closed");
			tree.setChecked(false);
			/*
			 * if (recursive) {// 递归查询子节点 List<TSFunction> functionList = new ArrayList<TSFunction>(tsFunctions); Collections.sort(functionList, new SetListSort());// 排序 List<ComboTree> children = new ArrayList<ComboTree>(); for (TSFunction f : functionList) { ComboTree t = comboTree(f,comboTreeModel,in, true); children.add(t); } tree.setChildren(children); }
			 */
		}
		return tree;
	}

	public List<TreeGrid> treegrid(List all, TreeGridModel treeGridModel, List<String> attributes) {
		List<TreeGrid> treegrid = new ArrayList<TreeGrid>();
		for (Object obj : all) {
			ReflectHelper reflectHelper = new ReflectHelper(obj);
			TreeGrid tg = new TreeGrid();
			String id = oConvertUtils.getString(reflectHelper.getMethodValue(treeGridModel.getIdField()));
			String src = oConvertUtils.getString(reflectHelper.getMethodValue(treeGridModel.getSrc()));
			String text = oConvertUtils.getString(reflectHelper.getMethodValue(treeGridModel.getTextField()));
			
			//----------------------------------------------------------------
			//update-end--Author:wangyang  Date:20130402 for：添加排序
			//----------------------------------------------------------------
			if(StringUtils.isNotEmpty(treeGridModel.getOrder())){
				String order = oConvertUtils.getString(reflectHelper.getMethodValue(treeGridModel.getOrder()));
				tg.setOrder(order);
			}
			//----------------------------------------------------------------
			//update-end--Author:wangyang  Date:20130402 for：添加排序
			//----------------------------------------------------------------
			tg.setId(id);
			if (treeGridModel.getIcon() != null) {
				String iconpath = TagUtil.fieldNametoValues(treeGridModel.getIcon(), obj).toString();
				if (iconpath != null) {
					tg.setIcon(iconpath);
				} else {
					tg.setCode("");
				}
			}
			if (treeGridModel.getStatus() != null) {
				String status = TagUtil.fieldNametoValues(treeGridModel.getStatus(), obj).toString();
				if (status != null) {
					tg.setStatus(status);
				} else {
					tg.setStatus("");
				}
			}
			if (treeGridModel.getCode() != null) {
				String code = TagUtil.fieldNametoValues(treeGridModel.getCode(), obj).toString();
				if (code != null) {
					tg.setCode(code);
				} else {
					tg.setCode("");
				}
			}
			if (treeGridModel.getIsLeaf() != null) {
				String leaf = TagUtil.fieldNametoValues(treeGridModel.getIsLeaf(), obj).toString();
				if("0".equals(leaf)){// 1 是叶子节点 
					tg.setState("closed");
				}
				if (leaf != null) {
					tg.setIsLeaf(leaf);
				} else {
					tg.setIsLeaf("");
				}
			}
			tg.setSrc(src);
			tg.setText(text);
			if (treeGridModel.getParentId() != null) {
				Object pid = TagUtil.fieldNametoValues(treeGridModel.getParentId(), obj);
				if (pid != null) {
					tg.setParentId(pid.toString());
				} else {
					tg.setParentId("");
				}
			}
			if (treeGridModel.getParentText() != null) {
				Object ptext = TagUtil.fieldNametoValues(treeGridModel.getParentText(), obj);
				if (ptext != null) {
					tg.setParentText(ptext.toString());
				} else {
					tg.setParentText("");
				}

			}
			if (treeGridModel.getResourceIds() != null) {
				String resourceIds = TagUtil.fieldNametoValues(treeGridModel.getResourceIds(), obj).toString();
				tg.setResourceIds(resourceIds);
			}
			/*List childList = (List) reflectHelper.getMethodValue(treeGridModel.getChildList());

			if (childList != null && childList.size() > 0) {
				tg.setState("closed");
			}*/
			
			/*Map<String,String> map = new HashMap<String,String>();
			for(String str : attributes){
				Object pid = TagUtil.fieldNametoValues(str, obj);
				map.put(str, pid.toString());
			}

			tg.setAttributes(map);*/
			treegrid.add(tg);
		}
		return treegrid;
	}
	
	public List<Map<String, Object>> findForNamedJdbcParam(String sql, int page, int rows, Map<String, Object> paramMap) {
		// 封装分页SQL
		sql = JdbcDao.jeecgCreatePageSql(sql, page, rows);
		return this.namedParameterJdbcTemplate.queryForList(sql, paramMap);
	}
	
	public List<Map<String, Object>> findForNamedJdbc(String sql, Map<String, Object> paramMap) {
		return this.namedParameterJdbcTemplate.queryForList(sql, paramMap);
	}
}
