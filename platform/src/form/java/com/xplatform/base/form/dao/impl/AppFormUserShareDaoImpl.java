package com.xplatform.base.form.dao.impl;

import org.springframework.stereotype.Repository;

import com.xplatform.base.form.dao.AppFormUserDao;
import com.xplatform.base.form.dao.AppFormUserShareDao;
import com.xplatform.base.form.entity.AppFormUser;
import com.xplatform.base.form.entity.AppFormUserShareEntity;
import com.xplatform.base.framework.core.common.dao.impl.BaseDaoImpl;
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
@Repository("appFormUserShareDao")
public class AppFormUserShareDaoImpl extends BaseDaoImpl<AppFormUserShareEntity> implements AppFormUserShareDao {

}
