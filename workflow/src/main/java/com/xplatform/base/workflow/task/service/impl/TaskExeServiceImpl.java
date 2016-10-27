package com.xplatform.base.workflow.task.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.core.bpm.model.ProcessCmd;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstanceService;
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;
import com.xplatform.base.workflow.support.msgtemplate.service.MsgTemplateService;
import com.xplatform.base.workflow.task.dao.TaskExeDao;
import com.xplatform.base.workflow.task.entity.TaskExeEntity;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.mybatis.vo.ProcessTask;
import com.xplatform.base.workflow.task.service.TaskActService;
import com.xplatform.base.workflow.task.service.TaskExeService;
import com.xplatform.base.workflow.task.service.TaskMessageService;
import com.xplatform.base.workflow.task.service.TaskOpinionService;
import com.xplatform.base.workflow.threadlocal.TaskThreadService;
/**
 * 
 * description :任务转办代理service实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:30:12
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 下午12:30:12
 *
 */
@Service("taskExeService")
public class TaskExeServiceImpl implements TaskExeService {
	private static final Logger logger = Logger.getLogger(TaskExeServiceImpl.class);
	@Resource
	private TaskExeDao taskExeDao;
	@Resource
	private ProcessInstanceService processInstanceService;
	@Resource
	private BaseService baseService;
	@Resource
	private TaskActService taskActService;
	@Resource
	private TaskOpinionService taskOpinionService;
	@Resource
	private TaskMessageService taskMessageService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private MsgTemplateService msgTemplateService;
	@Resource
	private FlowService flowService;
	
	@Override
	@Action(moduleCode="TaskExeManager",description="任务转办代理新增",detail="任务转办代理${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(TaskExeEntity TaskExe) throws BusinessException {
		String pk="";
		try {
			pk=this.taskExeDao.addTaskExe(TaskExe);
		} catch (Exception e) {
			logger.error("任务转办代理保存失败");
			throw new BusinessException("任务转办代理保存失败");
		}
		logger.info("任务转办代理保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="TaskExeManager",description="任务转办代理删除",detail="任务转办代理${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.taskExeDao.deleteTaskExe(id);
		} catch (Exception e) {
			logger.error("任务转办代理删除失败");
			throw new BusinessException("任务转办代理删除失败");
		}
		logger.info("任务转办代理删除成功");
	}

	@Override
	@Action(moduleCode="TaskExeManager",description="任务转办代理批量删除",detail="任务转办代理${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("任务转办代理批量删除成功");
	}

	@Override
	@Action(moduleCode="TaskExeManager",description="任务转办代理修改",detail="任务转办代理${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(TaskExeEntity TaskExe) throws BusinessException {
		try {
			TaskExeEntity oldEntity = get(TaskExe.getId());
			MyBeanUtils.copyBeanNotNull2Bean(TaskExe, oldEntity);
			this.taskExeDao.updateTaskExe(oldEntity);
		} catch (Exception e) {
			logger.error("任务转办代理更新失败");
			throw new BusinessException("任务转办代理更新失败");
		}
		logger.info("任务转办代理更新成功");
	}

	@Override
	public TaskExeEntity get(String id){
		TaskExeEntity TaskExe=null;
		try {
			TaskExe=this.taskExeDao.getTaskExe(id);
		} catch (Exception e) {
			logger.error("任务转办代理获取失败");
			//throw new BusinessException("任务转办代理获取失败");
		}
		logger.info("任务转办代理获取成功");
		return TaskExe;
	}

	@Override
	public List<TaskExeEntity> queryList() throws BusinessException {
		List<TaskExeEntity> TaskExeList=new ArrayList<TaskExeEntity>();
		try {
			TaskExeList=this.taskExeDao.queryTaskExeList();
		} catch (Exception e) {
			logger.error("任务转办代理获取列表失败");
			throw new BusinessException("任务转办代理获取列表失败");
		}
		logger.info("任务转办代理获取列表成功");
		return TaskExeList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.taskExeDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("任务转办代理获取分页列表失败");
			throw new BusinessException("任务转办代理获取分页列表失败");
		}
		logger.info("任务转办代理获取分页列表成功");
	}
	
	@Override
	public List<TaskExeEntity> getByTaskId(String taskId){
		return this.taskExeDao.findByProperty(TaskExeEntity.class, "taskId", taskId);
	}
	
	@Override
	public void complete(String taskId) {
		List<TaskExeEntity> list = getByTaskId(taskId);
		ProcessCmd cmd = TaskThreadService.getProcessCmd();
		UserEntity sysUser = null;
		if(StringUtil.isNotEmpty(cmd.getCurrentUserId())){
			SysUserService userService=ApplicationContextUtil.getBean("sysUserService");
			sysUser = userService.getUserById(cmd.getCurrentUserId());
		}
		for (TaskExeEntity taskExe : list) {
			if (StringUtil.equals(taskExe.getStatus() ,TaskExeEntity.STATUS_INIT)) {
				taskExe.setExeTime(new Date());
				taskExe.setExeUserId(sysUser.getId());
				taskExe.setExeUserName(sysUser.getName());
				if (taskExe.getAssigneeId().equals(sysUser.getId()))
					taskExe.setStatus(TaskExeEntity.STATUS_COMPLETE);
				else {
					taskExe.setStatus(TaskExeEntity.STATUS_OTHER_COMPLETE);
				}
			}
			try {
				this.update(taskExe);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(TaskExeEntity.class, param, propertyName);
	}
	
	public boolean isAssigneeTask(String taskId) {
		return BeanUtils.isNotEmpty(getByTaskIdStatusInit(taskId));
	}
	
	public TaskExeEntity getByTaskIdStatusInit(String taskId) {
		Map<String,String> param=new HashMap<String,String>();
		param.put("status", TaskExeEntity.STATUS_INIT);
		param.put("taskId", taskId);
		List<TaskExeEntity> list=this.taskExeDao.findByPropertys(TaskExeEntity.class, param);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	//判断是否允许转办
	public boolean isAssigneeTask(TaskEntity taskEnt,DefinitionEntity definition,String userId){
		//1.没有设置转办，则不允许转办
		/*boolean isFirstNode = NodeCache.isFirstNode(taskEnt.getProcessDefinitionId(), taskEnt.getTaskDefinitionKey());
		if (isFirstNode) {
			return false;
		}*/
		//2.没有设置转办，则不允许转办
		boolean rtn = StringUtil.equals(definition.getAllowDivert(), "1");//流程定义设定了转办
		if (!rtn){
			return false;
		}
		//3.任务是候选任务，那么不止一个人审批，不允许转办
		ProcessTask task=taskActService.getByTaskId(taskEnt.getId());
		if(task==null){return false;}
		if(!StringUtil.equals(userId, task.getAssignee())){
			return false;
		}
		//4.别人转给你了，那么你就不能再转发给别人(转发任务不能再次转发)
		boolean isCanAssignee = !isAssigneeTask(taskEnt.getId());
		//askOpinionEntity TaskOpinionEntity = this.TaskOpinionEntityService.getByTaskId(taskEnt.getId());
		//5.上次的审批意见，驳回，驳回到开始节点，代理的不能 转发给别人
		if ((TaskOpinionEntity.STATUS_RECOVER_TOSTART.toString().equals(taskEnt.getDescription()))
				|| (TaskOpinionEntity.STATUS_REJECT_TOSTART.toString().equals(taskEnt.getDescription()))
				|| (TaskOpinionEntity.STATUS_REJECT.toString().equals(taskEnt.getDescription()))
				|| (TaskOpinionEntity.STATUS_DELEGATE.toString().equals(taskEnt.getDescription()))) {
			isCanAssignee = false;
		}
		return isCanAssignee;
	}
	
	public void setTaskExeDao(TaskExeDao taskExeDao) {
		this.taskExeDao = taskExeDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public void cancel(String taskId) {
		// TODO Auto-generated method stub
		TaskExeEntity taskExe = getByTaskIdStatusInit(taskId);
		if (BeanUtils.isNotEmpty(taskExe)) {
			taskExe.setStatus(TaskExeEntity.STATUS_CANCEL);
			try {
				this.update(taskExe);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void saveAssignee(TaskExeEntity taskExe,UserEntity user) throws BusinessException {
		// TODO Auto-generated method stub
		this.save(taskExe);

		Short opinionStatus = TaskOpinionEntity.STATUS_DELEGATE;
		String useType = "";//消息通知模版类型
		try {
			if (taskExe.TYPE_ASSIGNEE.equals(taskExe.getType())) {
				opinionStatus = TaskOpinionEntity.STATUS_AGENT;
				useType = MsgTemplateEntity.USE_TYPE_AGENT;
			} else if (taskExe.TYPE_TRANSMIT.equals(taskExe.getType())) {
				opinionStatus = TaskOpinionEntity.STATUS_DELEGATE;
				this.taskActService.updateTask(taskExe.getTaskId(), taskExe.getAssigneeId().toString(), opinionStatus.toString());
				useType = MsgTemplateEntity.USE_TYPE_DELEGATE;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BusinessException("转办失败");
		}

		TaskOpinionEntity taskOpinion = this.taskOpinionService.getByTaskId(taskExe.getTaskId());
		taskOpinion.setCheckStatus(opinionStatus.intValue());

		if (taskExe.TYPE_TRANSMIT.equals(taskExe.getType())) {
			taskOpinion.setExeUserId(user.getId());
			taskOpinion.setExeUserName(user.getName());
		}
		taskOpinion.setOpinion(taskExe.getDescription());
		taskOpinion.setEndTime(new Date());
		String duration =DateUtils.getTime(DateUtils.getTime(taskOpinion.getCreateTime(), taskOpinion.getEndTime()));
		taskOpinion.setDurTime(duration);
		this.taskOpinionService.update(taskOpinion);

		ProcessInstanceEntity processRun = this.processInstanceService.get(taskExe.getInstId());
		TaskOpinionEntity newOpinion = new TaskOpinionEntity();
		newOpinion.setActInstId(processRun.getActInstId());
		newOpinion.setActDefId(processRun.getActDefId());
		newOpinion.setCheckStatus(TaskOpinionEntity.STATUS_CHECKING.intValue());
		newOpinion.setTaskKey(taskExe.getTaskDefKey());
		newOpinion.setTaskName(taskExe.getTaskName());
		newOpinion.setTaskId(taskExe.getTaskId());
		newOpinion.setExeUserId(taskExe.getAssigneeId());
		newOpinion.setExeUserName(taskExe.getAssigneeName());

		this.taskOpinionService.save(newOpinion);

		try {
			System.out.println("*******************************************转办改造***********************************");
			Map<String,Object> vars = new HashMap<String,Object>();
			vars.put("msgTemplateCode",MsgTemplateEntity.USE_TYPE_DELEGATE);
			vars.put("businessKey", processRun.getBusinessKey());
			vars.put("formCode", processRun.getFormCode());
			vars.put("url", "taskController.do?toStart&taskId="+taskExe.getTaskId());
			vars.put("groupId", processRun.getGroupId());
			sendMessage(taskExe, "msg,IM", useType, taskExe.getDescription(), false,user,vars);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BusinessException("消息发送失败");
		}
	}
	
	private void sendMessage(TaskExeEntity bpmTaskExe, String informType,
			String userType, String opinion, boolean cancel,UserEntity curUser,Map<String,Object> vars) throws BusinessException {
		String taskId = cancel ? null : bpmTaskExe.getTaskId();

		Map msgTempMap = this.msgTemplateService.getTempByFun(userType);

		String receiverId = bpmTaskExe.getAssigneeId();
		UserEntity assignUser = this.sysUserService.getUserById(receiverId);
		List<UserEntity> receiverUserList = new ArrayList<UserEntity>();
		receiverUserList.add(assignUser);
		this.taskMessageService.sendMessage(curUser, receiverUserList,
				informType, msgTempMap, bpmTaskExe.getSubject(), opinion,
				taskId, null,vars);
	}
	
	public ProcessInstanceEntity cancel(TaskExeEntity bpmTaskExe, UserEntity sysUser,String opinion, String informType) throws BusinessException {
		Short opininStatus = TaskOpinionEntity.STATUS_CHECKING;

		try {
			String memo = bpmTaskExe.getDescription() + ",<br/>取消了该任务原因:" + opinion;
			bpmTaskExe.setDescription(memo);
			bpmTaskExe.setExeUserId(bpmTaskExe.getOwnerId());
			bpmTaskExe.setExeUserName(bpmTaskExe.getOwnerName());
			bpmTaskExe.setStatus(TaskExeEntity.STATUS_CANCEL);
			bpmTaskExe.setExeTime(new Date());
			this.update(bpmTaskExe);
			this.taskActService.delDelegateUser(bpmTaskExe.getTaskId());
			this.taskActService.updateTask(bpmTaskExe.getTaskId(), sysUser.getId(), opininStatus.toString());
	
			TaskOpinionEntity taskOpinion = this.taskOpinionService.getByTaskId(bpmTaskExe.getTaskId());
	
			taskOpinion.setCheckStatus(TaskOpinionEntity.STATUS_DELEGATE_CANCEL.intValue());
			taskOpinion.setExeUserId(sysUser.getId());
			taskOpinion.setExeUserName(sysUser.getName());
			taskOpinion.setOpinion(opinion);
			taskOpinion.setEndTime(BeanUtils.isEmpty(taskOpinion.getEndTime()) ? new Date(): taskOpinion.getEndTime());
			String duration =DateUtils.getTime(DateUtils.getTime(taskOpinion.getCreateTime(), taskOpinion.getEndTime()));
			taskOpinion.setDurTime(duration);
			this.taskOpinionService.update(taskOpinion);
	
			ProcessInstanceEntity processRun = this.processInstanceService.get(bpmTaskExe.getInstId());
			TaskOpinionEntity newOpinion = new TaskOpinionEntity();
			newOpinion.setActInstId(processRun.getActInstId());
			newOpinion.setActDefId(processRun.getActDefId());
			newOpinion.setCheckStatus(TaskOpinionEntity.STATUS_CHECKING.intValue());
			newOpinion.setTaskKey(bpmTaskExe.getTaskDefKey());
			newOpinion.setTaskName(bpmTaskExe.getTaskName());
			newOpinion.setTaskId(bpmTaskExe.getTaskId());
			newOpinion.setExeUserId(bpmTaskExe.getOwnerId());
			newOpinion.setExeUserName(bpmTaskExe.getOwnerName());
			this.taskOpinionService.save(newOpinion);
	
			String userType = MsgTemplateEntity.USE_TYPE_CANCLE_DELEGATE;
			if (TaskExeEntity.TYPE_ASSIGNEE.equals(bpmTaskExe.getType()))
				userType = MsgTemplateEntity.USE_TYPE_CANCLE_AGENT;
			else if (TaskExeEntity.TYPE_TRANSMIT.equals(bpmTaskExe.getType())) {
				userType = MsgTemplateEntity.USE_TYPE_CANCLE_DELEGATE;
			}
			//TODO 不知道是什么操作,不过vars为null发消息会有问题,待改造
			sendMessage(bpmTaskExe, informType, userType, opinion, true,sysUser,null);
			return processRun;
		} catch (BusinessException e) {
			// TODO: handle exception
			throw new BusinessException("取消失败");
		}
		
	}

	public List<TaskExeEntity> cancelBat(String ids, String opinion,
			String informType, UserEntity sysUser) throws BusinessException {
		String[] aryId = ids.split(",");
		List<TaskExeEntity> list = new ArrayList<TaskExeEntity>();
		for (int i = 0; i < aryId.length; i++) {
			TaskExeEntity bpmTaskExe = this.get(aryId[i]);
			String taskId = bpmTaskExe.getTaskId();
			UserEntity user=this.sysUserService.getUserById(bpmTaskExe.getOwnerId());
			TaskEntity taskEntity = this.flowService.getTask(taskId);
			if (taskEntity != null) {
				cancel(bpmTaskExe, user, opinion, informType);
				list.add(bpmTaskExe);
			}
		}
		return list;
	}


}
