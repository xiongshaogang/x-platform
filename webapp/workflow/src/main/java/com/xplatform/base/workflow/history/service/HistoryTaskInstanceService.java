package com.xplatform.base.workflow.history.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;

import com.xplatform.base.workflow.history.mybatis.vo.ProcessTaskHistory;

/**
 * 
 * description :历史任务扩展service
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月22日 上午11:45:14
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年8月22日 上午11:45:14
 *
 */
public interface HistoryTaskInstanceService {
	/**
	 * 保存历史任务信息
	 * @author xiehs
	 * @createtime 2014年8月22日 上午11:44:07
	 * @Decription
	 *
	 * @param taskHistory
	 */
	public void save(ProcessTaskHistory taskHistory);
	
	/**
	 * 获取历史任务信息
	 * @author xiehs
	 * @createtime 2014年8月22日 上午11:44:27
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public ProcessTaskHistory get(String id);
	
	/**
	 * 获取流程实例和结点的历史任务集合
	 * @author xiehs
	 * @createtime 2014年8月22日 上午11:44:45
	 * @Decription
	 *
	 * @param param
	 * @return
	 */
	public List<HistoricTaskInstanceEntity> getByInstanceIdAndNodeId(Map<String,String> param);
}
