package com.xplatform.base.workflow.task.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.orgnaization.user.entity.UserEntity;

public interface TaskMessageService {
	public void notify(List<Task> taskList, String informTypes, String subject,Map<String, String> map, String opinion,Map<String,Object> vars) throws BusinessException;
	
	public void sendMessage(String receiveUserIds, Map<String, Object> vars)  throws BusinessException;

	public void sendMessage(UserEntity sendUser, List<UserEntity> receiverUserList,
			String informTypes, Map<String, String> msgTempMap, String subject,
			String opinion, String taskId, String runId,Map<String,Object> vars) throws BusinessException;
}
