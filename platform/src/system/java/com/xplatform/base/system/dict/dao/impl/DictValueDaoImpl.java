package com.xplatform.base.system.dict.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.BaseDaoImpl;
import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.dict.dao.DictValueDao;
import com.xplatform.base.system.dict.entity.DictTypeEntity;
import com.xplatform.base.system.dict.entity.DictValueEntity;
/**
 * 
 * description :模块dao实现
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
@Repository("dictValueDao")
public class DictValueDaoImpl extends BaseDaoImpl<DictValueEntity> implements DictValueDao {

}
