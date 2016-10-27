package com.xplatform.base.workflow.support.msgtemplate.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;

/**
 * 
 * description : 信息模版管理service
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
public interface MsgTemplateService {
	
	/**
	 * 新增信息模版
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param MsgTemplate
	 * @return
	 */
	public String save(MsgTemplateEntity MsgTemplate) throws BusinessException;
	
	/**
	 * 删除信息模版
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除信息模版
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新信息模版
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param MsgTemplate
	 * @return
	 */
	public void update(MsgTemplateEntity MsgTemplate) throws BusinessException;
	
	/**
	 * 查询一条信息模版记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public MsgTemplateEntity get(String id);
	
	/**
	 * 查询信息模版列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<MsgTemplateEntity> queryList();
	
	/**
	 * hibernate信息模版分页列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:34:16
	 * @Decription   
	 * @param cq
	 * @param b
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException;
	
	/**
	 * 判断字段记录是否唯一
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:25:48
	 * @Decription 
	 * @param param
	 * @return
	 */
	public boolean isUnique(Map<String,String> param,String propertyName);
	
	public void updateDefault(String useType);
	/**
	 * 获取使用类型的默认模版
	 * @author xiehs
	 * @createtime 2014年10月6日 下午2:56:18
	 * @Decription
	 *
	 * @param useType
	 * @return
	 */
	public MsgTemplateEntity getDefaultByUseType(String useType);
	
	public Map<String, String> getTempByFun(String useType);
}
