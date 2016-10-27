package com.xplatform.base.orgnaization.user.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.ComboBox;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.orgnaization.user.entity.UserVerifycodeEntity;
import com.xplatform.base.system.dict.entity.DictTypeEntity;
import com.xplatform.base.system.dict.entity.DictValueEntity;

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
public interface UserVerifycodeService extends BaseService<UserVerifycodeEntity>{
	public void sendVerifyCode(UserVerifycodeEntity verifycode) ;
	
	public boolean compareVerifyCode(String phone,String verifyCode,String moduleFlag);
}
