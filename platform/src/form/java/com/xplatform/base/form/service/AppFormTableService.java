package com.xplatform.base.form.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.form.entity.AppFormField;
import com.xplatform.base.form.entity.AppFormTable;
import com.xplatform.base.form.entity.FlowFormEntity;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.service.BaseService;

/**
 * 
 * description : 字典类型管理service
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:34:52
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiehs 2014年5月24日 上午11:34:52
 *
 */
public interface AppFormTableService extends BaseService<AppFormTable> {
	/**
	 * 创建物理表
	 * 
	 * @throws Exception
	 */
	public void generatePhysicalTable(AppFormTable table, Integer tableType) throws Exception;

	/**
	 * 更新已存在的物理表
	 * 
	 * @param table
	 * @throws Exception
	 */
	public void updatePhysicalTable(AppFormTable table) throws Exception;

	/**
	 * 根据主键策略获取实际插入的主键值
	 * 
	 * @param tableName
	 *            表单名称
	 * @return
	 */

	public Object getPkValue(String tableName) throws BusinessException;

	/**
	 * 单表表单添加
	 * 
	 * @param tableName
	 *            表名
	 * @param data
	 *            添加的数据map
	 * @throws Exception
	 */
	public Object insertTable(String tableName, Map<String, Object> data) throws Exception;

	/**
	 * 主从表表单添加
	 * 
	 * @param mainTableName
	 * @param mapMore
	 * @return
	 * @throws Exception
	 */
	public Object insertTableMore(String mainTableName, Map<String, List<Map<String, Object>>> mapMore) throws Exception;

	/**
	 * 单表表单更新
	 * 
	 * @param tableName
	 * @param id
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public boolean updateTable(String tableName, Object id, Map<String, Object> data) throws Exception;

	/**
	 * 主从表表单更新
	 * 
	 * @param mapMore
	 * @param mainTableName
	 * @return
	 * @throws BusinessException
	 */
	public boolean updateTableMore(String mainTableName, Map<String, List<Map<String, Object>>> mapMore) throws Exception;

	/**
	 * 通过formId获取对象
	 * 
	 * @param tableName
	 * @param id
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public List<AppFormTable> getAppFormTable(String formId) throws Exception;

	public AppFormTable queryAppFormTable(String tableName) throws BusinessException;

	/**
	 * 通过主表名获取所有字表对象
	 * 
	 * @param mainTableName
	 * @return
	 * @throws Exception
	 */
	public List<AppFormTable> queryAppFormTableList(String mainTableName) throws BusinessException;

	/**
	 * 通过主表名获取上次正在使用的子表对象
	 * 
	 * @param mainTableName
	 * @return
	 * @throws Exception
	 */
	public List<AppFormTable> getIsUseAFTList(String tableName) throws BusinessException;

	/**
	 * 通过formId获取数据
	 * 
	 * @param mainTableName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getFieldData(String formCode, String userId,int page, int rows) throws Exception;
	
	/**
	 * 通过formCode获取数据
	 * 
	 * @param businessKey
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOneFieldData(String formCode, String businessKey) throws Exception;

	public List<Map<String, Object>> querySingle(String table, String field, Map params, int page, int rows) throws BusinessException;

	public List<Map<String, Object>> querySingle(String table, String field, Map params) throws BusinessException;
	/**
	 * 数据通用删除方法(主从结构会一起删除)
	 * 
	 * @param table
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteData(String table, Object id) throws Exception;

	/**
	 * 查询数据表的某条记录(不传Id查全表记录)
	 * 
	 * @param tableName
	 *            表名
	 * @param id
	 *            表数据id
	 */
	public Map<String, Object> querySingleTableData(String tableName, String id);

	/**
	 * 查询单个从表记录
	 * 
	 * @param tableName
	 * @param mainId
	 * @return
	 */
	public List<Map<String, Object>> querySubTableData(String tableName, String mainId);

	/**
	 * 根据formCode查询主表记录
	 * 
	 * @param formCode
	 * @return
	 */
	public AppFormTable queryMainTable(String formCode);
	

	/**
	 * 通过formCode和cid判断明细是否存在
	 * 
	 * @param mainTableName
	 * @return
	 * @throws Exception
	 */
	public AppFormTable getAppFTByCid(String formCode,String cid) throws BusinessException;
	
	
	/**
	 * 通过formCode拼接获取数据总数
	 * 
	 * @param mainTableName
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getTableDataSum(String formCode,String userId) throws BusinessException;
	
	/**
	 * 获得一个关联模板的主表关联字段
	 * @param tableName
	 * @return
	 */
	public List<AppFormField> queryConnectionFields(String tableName);
	
	/**
	 * 查询已传阅人的集合
	 * @param businessKey
	 * @return
	 */
	public List<Map<String, String>> queryCirculateReceivers(String businessKey);
	
	/**
	 * 通过formCode查询table
	 * @param formCode
	 * @return
	 */
	public AppFormTable queryAppFormTableByFormCode(String formCode);
}
