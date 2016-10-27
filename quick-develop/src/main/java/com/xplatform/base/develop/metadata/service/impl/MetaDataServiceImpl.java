package com.xplatform.base.develop.metadata.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Service;

import com.sun.star.uno.RuntimeException;
import com.xplatform.base.develop.codegenerate.service.config.DbTableHandleI;
import com.xplatform.base.develop.codegenerate.service.impl.config.util.DbTableProcess;
import com.xplatform.base.develop.codegenerate.service.impl.config.util.DbTableUtil;
import com.xplatform.base.develop.codegenerate.util.PublicUtil;
import com.xplatform.base.develop.metadata.dao.MetaDataDao;
import com.xplatform.base.develop.metadata.entity.MetaDataEntity;
import com.xplatform.base.develop.metadata.entity.MetaDataFieldEntity;
import com.xplatform.base.develop.metadata.service.MetaDataService;
import com.xplatform.base.framework.core.annotation.Ehcache;
import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.service.impl.CommonServiceImpl;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.oConvertUtils;

@Service("metaDataService")
public class MetaDataServiceImpl extends CommonServiceImpl implements MetaDataService {
	private static final Logger logger = Logger.getLogger(MetaDataServiceImpl.class);
	// 同步方式：普通同步
	private static final String SYN_NORMAL = "normal";
	// 同步方式：强制同步
	private static final String SYN_FORCE = "force";

	@Resource
	private MetaDataDao metaDataDao;

	public void setmetaDataDao(MetaDataDao metaDataDao) {
		this.metaDataDao = metaDataDao;
	}

	@Override
	public DataGridReturn getDataGridReturn(CriteriaQuery cq, boolean b) {
		return metaDataDao.getDataGridReturn(cq, b);
	}

	@Override
	public MetaDataEntity getEntity(Class<MetaDataEntity> class1, String id) {
		// TODO Auto-generated method stub
		return metaDataDao.getEntity(class1, id);
	}

	@Override
	@Action(description = "保存或更新实体数据", detail = "保存或更新MetaDataEntity实体数据", execOrder = ActionExecOrder.AFTER, moduleCode = "formHeadManager")
	public void saveOrUpdate(MetaDataEntity formHead) {
		// TODO Auto-generated method stub
		metaDataDao.saveOrUpdate(formHead);
	}

	@Override
	@Action(description = "保存元数据", detail = "元数据表${tableName}保存成功", execOrder = ActionExecOrder.AFTER, moduleCode = "formHeadManager")
	public void save(MetaDataEntity formHead) {
		// TODO Auto-generated method stub
		metaDataDao.save(formHead);
	}

	@Override
	@Action(description = "更新元数据", detail = "元数据表${tableName}更新成功", execOrder = ActionExecOrder.AFTER, moduleCode = "formHeadManager")
	public void update(MetaDataEntity formHead) {
		// TODO Auto-generated method stub
		metaDataDao.updateEntitie(formHead);
	}

	@Override
	@Action(description = "元数据管理数据同步", detail = "<#if synMethod == 'normal'>普通同步</#if><#if synMethod == 'force'>强制同步</#if>表${MetaDataService.getTableName(id)}成功", execOrder = ActionExecOrder.AFTER, moduleCode = "formHeadManager")
	public boolean dbSynch(MetaDataEntity MetaData, String synMethod) throws BusinessException {
		try {
			if (SYN_NORMAL.equals(synMethod)) {
				// 普通方式同步
				// step.1判断表是否存在
				if (judgeTableIsExit(MetaData.getTableName())) {
					// 更新表操作
					DbTableProcess dbTableProcess = new DbTableProcess(getSession());
					List<String> updateTable = dbTableProcess.updateTable(MetaData, getSession());
					for (String sql : updateTable) {
						if (StringUtils.isNotEmpty(sql)) {
							metaDataDao.executeSql(sql);
						}
					}
				} else {
					// 不存在的情况下，创建新表
					try {
						DbTableProcess.createOrDropTable(MetaData, getSession());
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						throw new BusinessException("同步失败:创建新表出错");
					}
				}
				MetaData.setIsDbsynch("Y");
				this.saveOrUpdate(MetaData);
			} else if (SYN_FORCE.equals(synMethod)) {
				// 强制方式同步
				try {
					try {
						String sql = getTableUtil().dropTableSQL(MetaData.getTableName());
						this.executeSql(sql);
					} catch (Exception e) {
						// 部分数据库在没有表而执行删表语句时会报错
						logger.error(e.getMessage());
					}
					DbTableProcess.createOrDropTable(MetaData, getSession());
					MetaData.setIsDbsynch("Y");
					this.saveOrUpdate(MetaData);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new BusinessException("同步失败:创建新表出错");
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("同步失败:数据库不支持本次修改,如果不需要保留数据,请尝试强制同步");
		}
		return true;
	}

	@Override
	public boolean appendSubTableStr4Main(MetaDataEntity entity) {
		// TODO Auto-generated method stub
		// step.1 获取本表的名称
		String thisSubTable = entity.getTableName();
		List<MetaDataFieldEntity> columns = entity.getColumns();
		// step.2 扫描字段配置，循环处理填有主表以及主表字段的条目
		for (MetaDataFieldEntity fieldE : columns) {
			String mainT = fieldE.getMainTable();
			String mainF = fieldE.getMainField();
			if (!StringUtil.isEmpty(mainT) && !StringUtil.isEmpty(mainF)) {
				MetaDataEntity mainE = this.getMetaDataByTableName(mainT);
				if (mainE == null) {
					continue;
				}
				// step.4 追加处理主表的附表串
				String subTableStr = String.valueOf(mainE.getSubTableStr() == null ? "" : mainE.getSubTableStr());
				// step.5 判断是否已经存在于附表串
				if (!subTableStr.contains(thisSubTable)) {
					// step.6 追加到附表串
					if (!StringUtil.isEmpty(subTableStr)) {
						subTableStr += "," + thisSubTable;
					} else {
						subTableStr += thisSubTable;
					}
					mainE.setSubTableStr(subTableStr);
					logger.info("--主表" + mainE.getTableName() + "的附表串：" + mainE.getSubTableStr());
				}
				// step.7 更新主表的表配置
				this.updateTable(mainE, "sign");
			}
		}
		return true;
	}

	/**
	 * 根据tableName 获取表单配置 根据版本号缓存
	 * 
	 * @param tableName
	 * @param version
	 * @return
	 */
	@Ehcache
	public MetaDataEntity getMetaDataByTableName(String tableName, String version) {
		StringBuilder hql = new StringBuilder("");
		hql.append("from MetaDataEntity f");
		hql.append(" where f.tableName=? ");
		List<MetaDataEntity> list = this.findHql(hql.toString(), tableName);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public MetaDataEntity getMetaDataByTableName(String tableName) {
		StringBuilder hql = new StringBuilder("");
		hql.append("from MetaDataEntity f");
		hql.append(" where f.tableName=? ");
		List<MetaDataEntity> list = this.findHql(hql.toString(), tableName);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public boolean judgeTableIsExit(String tableName) {
		Connection conn = null;
		ResultSet rs = null;
		try {
			String[] types = { "TABLE" };
			conn = SessionFactoryUtils.getDataSource(getSession().getSessionFactory()).getConnection();
			DatabaseMetaData dbMetaData = conn.getMetaData();
			rs = dbMetaData.getTables(null, null, DbTableUtil.getDataType(getSession()).equals("ORACLE") ? tableName.toUpperCase() : tableName, types);
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new RuntimeException();
		} finally {// 关闭连接
			try {
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取数据操作接口
	 * 
	 * @return
	 */
	private DbTableHandleI getTableUtil() {
		return DbTableUtil.getTableHandle(getSession());
	}

	public synchronized void updateTable(MetaDataEntity t, String sign) {
		MetaDataFieldEntity column;
		boolean databaseFieldIsChange = false;
		for (int i = 0; i < t.getColumns().size(); i++) {
			column = t.getColumns().get(i);
			if (oConvertUtils.isEmpty(column.getFieldName())) {
				continue;
			}
			column.setTable(t);
			// 设置checkbox的值
			PublicUtil.judgeCheckboxValue(column, "isNull,isShow,isShowList,isQuery,isKey");
			if (oConvertUtils.isEmpty(column.getId())) {
				databaseFieldIsChange = true;
				this.save(column);
			} else {
				MetaDataFieldEntity c = this.getEntity(MetaDataFieldEntity.class, column.getId());
				if (!databaseFieldIsChange && databaseFieldIsChange(c, column)) {
					databaseFieldIsChange = true;
				}
				try {
					MyBeanUtils.copyBeanNotNull2Bean(column, c);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e);
				}
				this.saveOrUpdate(c);
			}
		}
		t.setIsDbsynch(databaseFieldIsChange ? "N" : t.getIsDbsynch());

		// 表单配置修改，版本号未升级
		Integer newVerion = t.getJformVersion() + 1;
		t.setJformVersion(newVerion);
		this.saveOrUpdate(t);
	}

	/**
	 * 判断数据库字段是否修改了
	 * 
	 * @param oldColumn
	 * @param newColumn
	 * @return
	 */
	private boolean databaseFieldIsChange(MetaDataFieldEntity oldColumn, MetaDataFieldEntity newColumn) {
		if (!PublicUtil.compareValue(oldColumn.getFieldName(), newColumn.getFieldName())
				|| !PublicUtil.compareValue(oldColumn.getContent(), newColumn.getContent())
				|| !PublicUtil.compareValue(oldColumn.getLength(), newColumn.getLength())
				|| !PublicUtil.compareValue(oldColumn.getPointLength(), newColumn.getPointLength())
				|| !PublicUtil.compareValue(oldColumn.getType(), newColumn.getType()) || !PublicUtil.compareValue(oldColumn.getIsNull(), newColumn.getIsNull())
				|| !PublicUtil.compareValue(oldColumn.getOrderNum(), newColumn.getOrderNum())
				|| !PublicUtil.compareValue(oldColumn.getIsKey(), newColumn.getIsKey())
				|| !PublicUtil.compareValue(oldColumn.getMainTable(), newColumn.getMainTable())
				|| !PublicUtil.compareValue(oldColumn.getMainField(), newColumn.getMainField())
				|| !PublicUtil.compareValue(oldColumn.getFieldDefault(), newColumn.getFieldDefault())) {
			return true;
		}
		return false;
	}

	/**
	 * 重载创建表
	 * 
	 * @param MetaData
	 * @param a
	 */
	@Override
	public void saveTable(MetaDataEntity MetaData, String string) {
		MetaData.setId((String) this.getSession().save(MetaData));
		MetaDataFieldEntity column;
		for (int i = 0; i < MetaData.getColumns().size(); i++) {
			column = MetaData.getColumns().get(i);
			PublicUtil.judgeCheckboxValue(column, "isNull,isShow,isShowList,isQuery,isKey");
			column.setTable(MetaData);
			this.save(column);
		}

	}

	@Override
	@Action(description = "删除元数据", detail = "删除元数据表${MetaDataService.getTableName(id)}成功", execOrder = ActionExecOrder.AFTER, moduleCode = "formHeadManager")
	public void deleteMetaData(MetaDataEntity MetaData) {
		if (judgeTableIsExit(MetaData.getTableName())) {
			String sql = getTableUtil().dropTableSQL(MetaData.getTableName());
			this.executeSql(sql);
		}
		this.delete(MetaData);
	}

	@Override
	public boolean removeSubTableStr4Main(MetaDataEntity entity) {
		// TODO Auto-generated method stub
		if (entity == null) {
			return false;
		}
		// step.1 获取本表的名称
		String thisSubTable = entity.getTableName();
		List<MetaDataFieldEntity> columns = entity.getColumns();
		// step.2 扫描字段配置，循环处理填有主表以及主表字段的条目
		for (MetaDataFieldEntity fieldE : columns) {
			String mainT = fieldE.getMainTable();
			String mainF = fieldE.getMainField();
			if (!StringUtil.isEmpty(mainT) && !StringUtil.isEmpty(mainF)) {
				MetaDataEntity mainE = this.getMetaDataByTableName(mainT);
				if (mainE == null) {
					continue;
				}
				// step.4 追加处理主表的附表串
				String subTableStr = String.valueOf(mainE.getSubTableStr() == null ? "" : mainE.getSubTableStr());
				// step.5 判断是否已经存在于附表串
				if (subTableStr.contains(thisSubTable)) {
					// step.6 剔除主表的附表串
					if (subTableStr.contains(thisSubTable + ",")) {
						subTableStr = subTableStr.replace(thisSubTable + ",", "");
					} else if (subTableStr.contains("," + thisSubTable)) {
						subTableStr = subTableStr.replace("," + thisSubTable, "");
					} else {
						subTableStr = subTableStr.replace(thisSubTable, "");
					}
					mainE.setSubTableStr(subTableStr);
					logger.info("--主表" + mainE.getTableName() + "的附表串：" + mainE.getSubTableStr());
					// step.7 更新主表的表配置,不用更新,因为hibernate是读取的缓存,所以和那边操作的对象是一个的
					// this.updateTable(mainE, null);
				}
			}
		}
		return true;
	}

	@Override
	public List<MetaDataEntity> getMetaDataList() {
		// TODO Auto-generated method stub
		String hql = "FROM MetaDataEntity";
		return metaDataDao.findByQueryString(hql);
	}

	@Override
	public String getTableName(String id) {
		return metaDataDao.get(MetaDataEntity.class, id).getTableName();
	}
}
