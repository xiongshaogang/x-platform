package com.xplatform.base.framework.core.common.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.dao.BaseDao;
import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.hibernate.qbc.HqlQuery;
import com.xplatform.base.framework.core.common.hibernate.qbc.PageList;
import com.xplatform.base.framework.core.common.model.common.DBTable;
import com.xplatform.base.framework.core.common.model.common.UploadFile;
import com.xplatform.base.framework.core.common.model.json.ComboTree;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.model.json.TreeGrid;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.tag.vo.datatable.DataTableReturn;
import com.xplatform.base.framework.tag.vo.easyui.Autocomplete;
import com.xplatform.base.framework.tag.vo.easyui.ComboTreeModel;
import com.xplatform.base.framework.tag.vo.easyui.TreeGridModel;
@Service("baseService")
public class BaseServiceImpl<T> implements BaseService<T>{

	@Resource
	private ICommonDao commonDao;
	
	private BaseDao<T> baseDao;
	public void setBaseDao(BaseDao<T> baseDao) {
		this.baseDao = baseDao;
	}
	
	public <T> boolean isUnique(Class<T> entityClass,Map<String, String> param,String propertyName){
		// TODO Auto-generated method stub
		String newValue = param.get("newValue");
		String oldValue = param.get("oldValue");
		if (newValue == null || StringUtil.equals(newValue, oldValue)) {//修改同一条记录
			return true;
		}
		List<T> list= this.commonDao.findByProperty(entityClass, propertyName, newValue);
		if(list!=null &&list.size()>0){//数据库记录不唯一，返回false
			return false;
		}else{
			return true;
		}
	}
	
	private static final Logger logger = Logger.getLogger(CommonServiceImpl.class);

	/**
	 * 获取所有数据库表
	 * 
	 * @return
	 */
	public List<DBTable> getAllDbTableName() {
		return baseDao.getAllDbTableName();
	}

	public Integer getAllDbTableSize() {
		return baseDao.getAllDbTableSize();
	}

	public Serializable save(T entity) throws BusinessException {
		Serializable pk = "";
		try {
			pk = baseDao.save(entity);
		} catch (Exception e) {
			logger.error("保存失败");
			throw new BusinessException("保存失败");
		}
		logger.info("保存成功");
		return pk;
	}
	
	public void saveOrUpdate(T entity) {
		baseDao.saveOrUpdate(entity);

	}

	
	public void delete(T entity) {
		baseDao.delete(entity);

	}

	/**
	 * 删除实体集合
	 * 
	 * @param <T>
	 * @param entities
	 */
	public void deleteAllEntitie(Collection<T> entities) {
		baseDao.deleteAllEntitie(entities);
	}

	/**
	 * 根据实体名获取对象
	 */
	public T get(Serializable id) {
		return (T) baseDao.get(id);
	}
	
	@Override
	public List<T> getIds(String ids) {
		List<T> list = new ArrayList<T>();
		if (StringUtil.isNotEmpty(ids)) {
			Type genType = getClass().getGenericSuperclass();
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
			Class entityClass = (Class) params[0];
			String hql = "FROM " + entityClass.getName() + " WHERE id in(:ids)";
			Query q = getSession().createQuery(hql);
			q.setParameterList("ids", ids.split(","));
			list = q.list();
		}
		return list;
	}
	/**
	 * 根据实体名返回全部对象
	 * 
	 * @param <T>
	 * @param hql
	 * @param size
	 * @return
	 */
	public List<T> getList() {
		return baseDao.loadAll();
	}

	/**
	 * 根据实体名获取对象
	 */
	public T getEntity(Serializable id) {
		return (T) baseDao.getEntity(id);
	}

	/**
	 * 根据实体名称和字段名称和字段值获取唯一记录
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public T findUniqueByProperty(
			String propertyName, Object value) {
		return (T) baseDao.findUniqueByProperty( propertyName, value);
	}

	/**
	 * 按属性查找对象列表.
	 */
	public List<T> findByProperty(
			String propertyName, Object value) {

		return baseDao.findByProperty(propertyName, value);
	}
	
	/**
	 * 按多属性查找对象列表.
	 */
	public List<T> findByPropertys(Map<String, Object> param) {
		return baseDao.findByPropertys(param);
	}

	/**
	 * 加载全部实体
	 * 
	 * @param <T>
	 * @param entityClass
	 * @return
	 */
	public List<T> loadAll() {
		return baseDao.loadAll();
	}

	public T singleResult(String hql) {
		return (T) baseDao.singleResult(hql);
	}

	/**
	 * 删除实体主键ID删除对象
	 * 
	 * @param <T>
	 * @param entities
	 */
	public void deleteEntityByIds(Serializable ids) {
		if (ids != null && StringUtil.isNotEmpty(ids.toString())) {
			String[] idArray = StringUtil.split(ids.toString(), ",");
			for (String id : idArray) {
				baseDao.deleteEntityById(id);
			}
		}
	}
	
	@Override
	public void deleteByIds(String ids) throws BusinessException {
		try {
			if (StringUtil.isNotEmpty(ids)) {
				Type genType = getClass().getGenericSuperclass();
				Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
				Class entityClass = (Class) params[0];
				String hql = "DELETE FROM " + entityClass.getName() + " WHERE id in(:ids)";
				Query q = getSession().createQuery(hql);
				q.setParameterList("ids", ids.split(","));
				q.executeUpdate();
			}
		} catch (Exception e) {
			ExceptionUtil.throwBusinessException("删除失败");
		}
	}

	/**
	 * 更新指定的实体
	 * 
	 * @param <T>
	 * @param pojo
	 * @throws Exception 
	 */
	public void update(T pojo, Serializable id) throws Exception {
		T oldEntity = get(id);
		MyBeanUtils.copyBeanNotNull2Bean(pojo, oldEntity);
		baseDao.merge(oldEntity);
	}
	
	/**
	 * 更新实体
	 * 
	 * @param <T>
	 * @param pojo
	 */
	@Override
	public void update(T pojo) throws BusinessException{
		try {
			baseDao.updateEntitie(pojo);
			logger.info("更新成功");
		} catch (Exception e) {
			logger.error("更新失败");
		}
	}

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public List<T> findByQueryString(String hql) {
		return baseDao.findByQueryString(hql);
	}

	/**
	 * 根据sql更新
	 * 
	 * @param query
	 * @return
	 */
	public int updateBySqlString(String sql) {
		return baseDao.updateBySqlString(sql);
	}

	/**
	 * 根据sql查找List
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public List<T> findListbySql(String query) {
		return baseDao.findListbySql(query);
	}

	/**
	 * 通过属性称获取实体带排序
	 * 
	 * @param <T>
	 * @param clas
	 * @return
	 */
	public List<T> findByPropertyisOrder(
			String propertyName, Object value, boolean isAsc,List<String> orderPropertyes) {
		return baseDao.findByPropertyisOrder(propertyName,
				value, isAsc,orderPropertyes);
	}

	/**
	 * 
	 * cq方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageList(final CriteriaQuery cq, final boolean isOffset) {
		return baseDao.getPageList(cq, isOffset);
	}

	/**
	 * 返回DataTableReturn模型
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public DataTableReturn getDataTableReturn(final CriteriaQuery cq,
			final boolean isOffset) {
		return baseDao.getDataTableReturn(cq, isOffset);
	}

	/**
	 * 返回easyui datagrid模型
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public DataGridReturn getDataGridReturn(final CriteriaQuery cq,
			final boolean isOffset) {
		return baseDao.getDataGridReturn(cq, isOffset);
	}

	/**
	 * 
	 * hqlQuery方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageList(final HqlQuery hqlQuery,
			final boolean needParameter) {
		return baseDao.getPageList(hqlQuery, needParameter);
	}

	/**
	 * 
	 * sqlQuery方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageListBySql(final HqlQuery hqlQuery,
			final boolean isToEntity) {
		return baseDao.getPageListBySql(hqlQuery, isToEntity);
	}

	public Session getSession()

	{
		return baseDao.getSession();
	}

	public List findByExample(
			final Object exampleEntity) {
		return baseDao.findByExample(exampleEntity);
	}

	/**
	 * 通过cq获取全部实体
	 * 
	 * @param <T>
	 * @param cq
	 * @return
	 */
	public List<T> getListByCriteriaQuery(final CriteriaQuery cq,
			Boolean ispage) {
		return baseDao.getListByCriteriaQuery(cq, ispage);
	}

	/**
	 * 文件上传
	 * 
	 * @param request
	 */
	/*public T uploadFile(UploadFile uploadFile) {
		return (T) baseDao.uploadFile(uploadFile);
	}*/

	public HttpServletResponse viewOrDownloadFile(UploadFile uploadFile)

	{
		return baseDao.viewOrDownloadFile(uploadFile);
	}

	/**
	 * 生成XML文件
	 * 
	 * @param fileName
	 *            XML全路径
	 * @return
	 */
	/*public HttpServletResponse createXml(ImportFile importFile) {
		return baseDao.createXml(importFile);
	}*/

	/**
	 * 解析XML文件
	 * 
	 * @param fileName
	 *            XML全路径
	 */
	/*public void parserXml(String fileName) {
		baseDao.parserXml(fileName);
	}*/


	/**
	 * 根据模型生成JSON
	 * 
	 * @param all
	 *            全部对象
	 * @param in
	 *            已拥有的对象
	 * @param comboBox
	 *            模型
	 * @return
	 */
	public List<ComboTree> ComboTree(List all, ComboTreeModel comboTreeModel,List in) {
		return baseDao.ComboTree(all, comboTreeModel, in);
	}

	/**
	 * 构建树形数据表
	 */
	public List<TreeGrid> treegrid(List all, TreeGridModel treeGridModel, List<String> attributes) {
		return baseDao.treegrid(all, treeGridModel,attributes);
	}

	/**
	 * 获取自动完成列表
	 * 
	 * @param <T>
	 * @return
	 */
	public List<T> getAutoList(Autocomplete autocomplete) {
		StringBuffer sb = new StringBuffer("");
		for (String searchField : autocomplete.getSearchField().split(",")) {
			sb.append("  or " + searchField + " like '%" + autocomplete.getTrem() + "%' ");
		}
		String hql = "from " + autocomplete.getEntityName() + " where 1!=1 " + sb.toString();
		return baseDao.getSession().createQuery(hql).setFirstResult(autocomplete.getCurPage() - 1)
				.setMaxResults(autocomplete.getMaxRows()).list();
	}

	
	public Integer executeSql(String sql, List<Object> param) {
		return baseDao.executeSql(sql, param);
	}

	
	public Integer executeSql(String sql, Object... param) {
		return baseDao.executeSql(sql, param);
	}

	
	public Integer executeSql(String sql, Map<String, Object> param) {
		return baseDao.executeSql(sql, param);
	}
	
	public Object executeSqlReturnKey(String sql, Map<String, Object> param) throws BusinessException {
		return baseDao.executeSqlReturnKey(sql, param);
	}
	
	public List<Map<String, Object>> findForJdbc(String sql, int page, int rows) {
		return baseDao.findForJdbc(sql, page, rows);
	}

	
	public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
		return baseDao.findForJdbc(sql, objs);
	}

	
	public List<Map<String, Object>> findForJdbcParam(String sql, int page,
			int rows, Object... objs) {
		return baseDao.findForJdbcParam(sql, page, rows, objs);
	}

	
	public List<T> findObjForJdbc(String sql, int page, int rows) {
		return baseDao.findObjForJdbc(sql, page, rows);
	}

	
	public Map<String, Object> findOneForJdbc(String sql, Object... objs) {
		return baseDao.findOneForJdbc(sql, objs);
	}

	
	public Long getCountForJdbc(String sql) {
		return baseDao.getCountForJdbc(sql);
	}

	
	public Long getCountForJdbcParam(String sql, Object[] objs) {
		return baseDao.getCountForJdbc(sql);
	}

	
	public void batchSave(List<T> entitys) {
		this.baseDao.batchSave(entitys);
	}

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public List<T> findHql(String hql, Object... param) {
		return this.baseDao.findHql(hql, param);
	}

	public List<T> pageList(DetachedCriteria dc, int firstResult,
			int maxResult) {
		return this.baseDao.pageList(dc, firstResult, maxResult);
	}

	public List<T> findByDetached(DetachedCriteria dc) {
		return this.baseDao.findByDetached(dc);
	}
	
	public Integer executeHql(String hql) {
		return baseDao.executeHql(hql);
	}
	
	public List<Map<String, Object>> findForNamedJdbcParam(String sql, int page, int rows, Map<String, Object> paramMap){
		return baseDao.findForNamedJdbcParam(sql, page, rows, paramMap);
	}

	public List<Map<String, Object>> findForNamedJdbc(String sql, Map<String, Object> paramMap) {
		return baseDao.findForNamedJdbc(sql, paramMap);
	}
}
