package com.xplatform.base.form.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.xplatform.base.form.entity.AppFormField;
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
public interface AppFormFieldService extends BaseService<AppFormField> {

	public void saveAppFormField(JSONObject jsonObject,String code,String mainFlowCode) throws Exception;

	public List<Map<String, Object>> queryAFFList(String formCode) throws BusinessException;
	/**
	 * 根据formCode和tableId获取AppFormField当前正在使用的list表单
	 * @param formCode
	 * @return
	 * @throws BusinessException
	 */
	public List<AppFormField> getAFFListByTableId(String formCode,String tableId)throws BusinessException;
	/**
	 * 根据formCode获取AppFormField当前正在使用的list表单
	 * @param formCode
	 * @return
	 * @throws BusinessException
	 */
	public List<AppFormField> getAFFList(String formCode)throws BusinessException;
	
	
	/**
	 * 根据formCode获取AppFormField所有使用或者未使用的list表单
	 * @param formCode
	 * @return
	 * @throws BusinessException
	 */
	public List<AppFormField> getAllAFFList(String formCode)throws BusinessException;
	
	/**
	 * 根据表名获得所有表字段配置(以key-id,value-entity方式存储)
	 * 
	 * @param tableName
	 * @return
	 */
	public Map<String, AppFormField> queryAppFormFieldMap(String tableName);
	
	/**
	 * 通过tableName获得非关联控件字段
	 * @param tableName
	 * @return
	 */
	public List<AppFormField> queryAppFormFieldBySelf(String tableName);

	/**
	 * 根据表Id查询下属字段
	 * 
	 * @param tableId
	 * @return
	 */
	public List<AppFormField> queryAppFormField(String tableId);
	
	/**
	 * 根据formCode和cid获取表单
	 * 
	 * @param tableName
	 * @return
	 */
	public AppFormField getAppFF(String formCode,String cid);
	
	/**
	 * 根据formCode和panrentId获取表单
	 * 
	 * @param tableName
	 * @return
	 */
	public AppFormField getAppFFByPaId(String formCode, String parentId);
	
	/**
	 * 根据formCode和panrentId获取没过期表单
	 * 
	 * @param tableName
	 * @return
	 */
	public List<AppFormField> getNotIsOverdueAFFList(String formCode,String tableId); 
	
	/**
	 * 查询一张表正在使用的附件fields
	 * @param tableName
	 * @return
	 */
	public List<AppFormField> queryUsedFileAppFormField(String tableName);
	
	/**
	 * 查询一个数据
	 * @param tableName
	 * @return
	 */
	public List<Map<String,Object>>  queryDisableFieldList(String formCode);
	
	/**
	 * 将数据库的field集合转化为物理表的field集合(将非db的移除掉,用于更新或插入使用)
	 * @param fields
	 * @return
	 */
	public List<AppFormField> parseFilterFields(List<AppFormField> fields) ;
	
	/**
	 * 将数据库的field集合转化为物理表的field集合
	 * @param fields
	 * @return
	 */
	public List<AppFormField> parseFilterFields(List<AppFormField> fields, Boolean isRemove) ;
	
	/**
	 * 查询某应用下的标题field(可能为空)
	 * @param formCode
	 * @return
	 */
	public AppFormField queryTitleField(String formCode);
}
