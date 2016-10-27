package com.xplatform.base.orgnaization.user.dao.impl;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.BaseDaoImpl;
import com.xplatform.base.orgnaization.user.dao.UserVerifycodeDao;
import com.xplatform.base.orgnaization.user.entity.UserVerifycodeEntity;
import com.xplatform.base.system.dict.dao.DictTypeDao;
import com.xplatform.base.system.dict.entity.DictTypeEntity;
/**
 * 
 * description :字典类型dao实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:23:11
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:23:11
 *
 */
@Repository("userVerifycodeDao")
public class UserVerifycodeDaoImpl extends BaseDaoImpl<UserVerifycodeEntity> implements UserVerifycodeDao {

}
