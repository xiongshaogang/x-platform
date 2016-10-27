package com.xplatform.base.workflow.task.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.task.entity.TaskReadEntity;
import com.xplatform.base.workflow.task.mybatis.vo.TaskUser;

/**
 * 
 * description :任务执行人
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年9月23日 下午2:08:05
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年9月23日 下午2:08:05
 *
 */
public interface TaskUserService {
	
	/**
	 * 查询任务候选人
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public Set<UserEntity> getCandidateUsers(String taskId) throws BusinessException;
	
	/**
	 * hibernate查询任务候选执行人
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:34:16
	 * @Decription   
	 * @param cq
	 * @param b
	 */
	public Set<TaskExecutor> getCandidateExecutors(String taskId) throws BusinessException;
	
	/**
	 * 任务候选人保存
	 * @author xiehs
	 * @createtime 2014年11月4日 下午3:44:56
	 * @Decription
	 *
	 * @param taskUser
	 */
	public void saveTaskUser(TaskUser taskUser);
	
	
}
