package com.xplatform.base.form.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.xplatform.base.form.entity.AppFormApproveUser;
import com.xplatform.base.form.entity.AppFormField;
import com.xplatform.base.form.entity.AppFormUser;
import com.xplatform.base.form.entity.AppFormUserData;
import com.xplatform.base.form.entity.FlowFormEntity;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.BaseService;

/**
 * 
 * description : 流程表单管理service
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:34:52
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiehs 2014年5月24日 上午11:34:52
 *
 */
public interface FlowFormService extends BaseService<FlowFormEntity> {

	/**
	 * 保存表单
	 * 
	 * @author lixt
	 * @param flowForm
	 * @throws BusinessException
	 */
	public String saveFlowForm(JSONObject jSONObject, Integer isDeploy, String jsonStr,String content) throws BusinessException;

	/**
	 * 修改表单
	 * 
	 * @author lixt
	 * @param flowForm
	 * @throws BusinessException
	 */
	public String updateFlowForm(JSONObject jSONObject, Integer isDeploy, String jsonStr,String content) throws Exception;

	/**
	 * 查询表单
	 * 
	 * @author lixt
	 * @param flowForm
	 * @throws BusinessException
	 */
	public Map<Object, Object> getFlowForm(String fromId, String formCode, int status) throws BusinessException;

	/**
	 * 根据code查询表单（最高版本，不包含被删除的）
	 * 
	 * @author lixt
	 * @param flowForm
	 * @throws BusinessException
	 */
	public FlowFormEntity getFlowFormByCode(String code) throws BusinessException;
	
	/**
	 * 根据code查询表单（最高版本，包含被删除的）
	 * 
	 * @author lixt
	 * @param flowForm
	 * @throws BusinessException
	 */
	public FlowFormEntity getFlowFormByCodeAll(String code) throws BusinessException;

	/**
	 * 查询用户的表单李列表
	 * 
	 * @author lixt
	 * @param flowForm
	 * @throws BusinessException
	 */
	public List<Map<String, Object>> queryFlowFormList(String userId) throws BusinessException;

	/**
	 * 查询当前用户所能看到的所有应用
	 * 
	 * @author lixt
	 * @param flowForm
	 * @throws BusinessException
	 */
	public List<Map<String, Object>> queryAPPList(String userId,String orgId) throws BusinessException;
	
	/**
	 * 查询当前机构所有能看到的应用
	 * 
	 * @author lixt
	 * @param flowForm
	 * @throws BusinessException
	 */
	public List<Map<String, Object>> queryOrgAppList(String orgId) throws BusinessException;
	
	
	/**
	 * 查询当前用户所能看到的可用禁用的所有应用
	 * 
	 * @author lixt
	 * @param flowForm
	 * @throws BusinessException
	 */
	public List<Map<String, Object>> queryAPPAllList(String userId,String orgId) throws BusinessException;
	
	/**
	 * 查询当前用户所能看到的所有应用
	 * 
	 * @author lixt
	 * @param flowForm
	 * @throws BusinessException
	 */
	public List<Map<String, Object>> myApplication(String userId,int page,int rows) throws Exception;

	public List<Map<String, Object>> querySingle(String table, String field, Map params, int page, int rows) throws BusinessException;

	/**
	 * 根据用户的parentId查询关联的模板
	 * 
	 * @author lixt
	 * @param flowForm
	 * @throws BusinessException
	 */
	public List<FlowFormEntity> queryRelaFlFoList(String formId,String formCode) throws BusinessException;

	/**
	 * 根据用户的parentId查询关联的模板
	 * 
	 * @author lixt
	 * @param flowForm
	 * @throws BusinessException
	 */
	public String getCidList(String formId) throws BusinessException;

	/**
	 * 查询所有已发布并且最高版本的模板
	 * 
	 * @author lixt
	 * @param flowForm
	 * @throws BusinessException
	 */
	public List<FlowFormEntity> queryMyFlowFormList(String userId,String code) throws BusinessException;
	
	/**
	 * 查询所有已发布并且最高版本的模板
	 * 
	 * @author lixt
	 * @param flowForm
	 * @throws BusinessException
	 */
	public List<FlowFormEntity> queryMyFlowFormList1(String userId,String orgId) throws BusinessException;

	/**
	 * 查询最新版本的Flowform记录
	 * 
	 * @param formCode
	 * @return
	 */
	public FlowFormEntity queryLastestVersionFlowForm(String formCode);
	
	/**
	 * 查询我发布的模板
	 * 
	 * @param formCode
	 * @return
	 */
	public List<Map<String,Object>> queryMyCreateFlowFormList(String userId,String orgId) throws BusinessException;
	
	public List<FlowFormEntity> queryFlowFormByParentId(String parentId);
}
