package com.xplatform.base.form.dao.impl;

import org.springframework.stereotype.Repository;

import com.xplatform.base.form.dao.AppFormUserDataDao;
import com.xplatform.base.form.entity.AppFormUserData;
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
@Repository("appFormUserDataDao")
public class AppFormUserDataDaoImpl extends BaseDaoImpl<AppFormUserData> implements AppFormUserDataDao {

}
