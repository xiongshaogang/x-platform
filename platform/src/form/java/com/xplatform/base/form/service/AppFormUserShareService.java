package com.xplatform.base.form.service;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.xplatform.base.form.entity.AppFormUserShareEntity;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.orgnaization.user.vo.UserTypeVo;
import com.xplatform.base.platform.common.utils.ClientUtil;

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
public interface AppFormUserShareService extends BaseService<AppFormUserShareEntity>{
	/**
	 * 获取别人分享的模版
	 * @param userId
	 * @return
	 */
	public List<Map<String,Object>> queryShareFormList(String userId);
	
	public void updateUsers(String formId,String userId,List<UserTypeVo> users) throws BusinessException;
	
	public List<AppFormUserShareEntity> queryShareUser(String userId,String formId);
}
