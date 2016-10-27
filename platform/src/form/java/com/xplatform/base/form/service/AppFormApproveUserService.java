package com.xplatform.base.form.service;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.xplatform.base.form.entity.AppFormApproveUser;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.BaseService;

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
public interface AppFormApproveUserService extends BaseService<AppFormApproveUser>{

	public AjaxJson saveOrDeleteAFAU(String formId,JSONObject jsonObject) throws BusinessException;
	/**
	 * 根据forId获取数据列表
	 * @param formId
	 * @return
	 * @throws BusinessException
	 */
	public List<AppFormApproveUser> getAFAUList(String formId) throws BusinessException;
	/**
	 * 根据forId获取数据列表，用户头像，名称
	 * @param formId
	 * @return
	 * @throws BusinessException
	 */
	public List<Map<String,Object>> queryAFAUList(String formId) throws BusinessException;
	
}
