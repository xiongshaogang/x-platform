package com.xplatform.base.form.service;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.xplatform.base.form.entity.AppFormUser;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.orgnaization.user.vo.UserTypeVo;

/**
 * 
 * description : 字典类型管理service
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:34:52
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:34:52
 *
 */
public interface AppFormUserService extends BaseService<AppFormUser>{

	public AjaxJson saveOrDeleteAppForm(String formId,JSONObject jsonObject) throws BusinessException;
	/**
	 * 根据formid来获取数据列表
	 * @param formId
	 * @return
	 * @throws BusinessException
	 */
	public List<AppFormUser> getAppFormList(String formId) throws BusinessException;
	
	/**
	 * 根据formid来获取数据列表
	 * @param formId
	 * @return
	 * @throws BusinessException
	 */
	public AppFormUser getAppFormUser(String formId,String userId) throws BusinessException;
	
	/**
	 * 根据formid来获取数据列表以及用户名称，头像
	 * @param formId
	 * @return
	 * @throws BusinessException
	 */
	public List<Map<String,Object>> queryAppFormList(String formId) throws BusinessException;
	
}
