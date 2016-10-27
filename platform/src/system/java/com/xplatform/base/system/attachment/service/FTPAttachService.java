package com.xplatform.base.system.attachment.service;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.PropertiesUtil;
import com.xplatform.base.system.attachment.entity.AttachEntity;
import com.xplatform.base.system.attachment.entity.FTPAttachEntity;
import com.xplatform.base.system.type.entity.TypeEntity;

/**
 * description : FTP附件Service操作类
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月2日 下午6:04:23
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年7月2日 下午6:04:23
 *
*/

public interface FTPAttachService extends BaseService<FTPAttachEntity> {
}
