package com.xplatform.base.workflow.instance.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.instance.entity.ProcessInstHistoryEntity;

/**
 * 
 * description : 流程实例管理service
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
public interface ProcessInstHistoryService {
	
	/**
	 * 新增流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param ProcessInsHistory
	 * @return
	 */
	public String save(ProcessInstHistoryEntity ProcessInsHistory) throws BusinessException;
	
	/**
	 * 删除流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param ProcessInsHistory
	 * @return
	 */
	public void update(ProcessInstHistoryEntity ProcessInsHistory) throws BusinessException;
	
	/**
	 * 查询一条流程实例记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public ProcessInstHistoryEntity get(String id);
	
	/**
	 * 查询流程实例列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<ProcessInstHistoryEntity> queryList() throws BusinessException;
	
	/**
	 * hibernate流程实例分页列表
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
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年11月4日 上午11:19:22
	 * @Decription 通过流程实例id查找历史实例记录
	 *
	 * @param actInstId
	 * @return
	 */
	public ProcessInstHistoryEntity getByActInstanceId(String actInstId); 
	
	public ProcessInstHistoryEntity getByBusinessKey(String businessKey);
}
