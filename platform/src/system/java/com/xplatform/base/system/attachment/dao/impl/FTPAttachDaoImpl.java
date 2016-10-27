package com.xplatform.base.system.attachment.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.BaseDaoImpl;
import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.system.attachment.dao.FTPAttachDao;
import com.xplatform.base.system.attachment.entity.FTPAttachEntity;
import com.xplatform.base.system.dict.dao.DictTypeDao;
import com.xplatform.base.system.dict.entity.DictTypeEntity;

/**
 * 
 * description :FTP dao实现
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
@Repository("ftpAttachDao")
public class FTPAttachDaoImpl extends BaseDaoImpl<FTPAttachEntity> implements FTPAttachDao {
}
