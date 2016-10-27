package com.xplatform.base.workflow.instance.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;





import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.instance.dao.ProcessInstCptoDao;
import com.xplatform.base.workflow.instance.entity.ProcessInstCptoEntity;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstCptoService;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.service.NodeSetService;
import com.xplatform.base.workflow.node.service.NodeUserService;
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;
import com.xplatform.base.workflow.support.msgtemplate.service.MsgTemplateService;
import com.xplatform.base.workflow.task.service.TaskMessageService;

/**
 * 
 * description :流程结束转发抄送service实现
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
@Service("processInstCptoService")
public class ProcessInstCptoServiceImpl implements ProcessInstCptoService {
	private static final Logger logger = Logger.getLogger(ProcessInstCptoServiceImpl.class);
	@Resource
	private ProcessInstCptoDao processInsCptoDao;
	
	@Resource
	private BaseService baseService;
	@Resource
	private NodeUserService nodeUserService;
	@Resource
	private TaskMessageService taskMessageService;
	@Resource
	private MsgTemplateService msgTemplateService;
	@Override
	@Action(moduleCode="ProcessInsCptoManager",description="流程结束转发抄送新增",detail="流程结束转发抄送${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(ProcessInstCptoEntity ProcessInsCpto) throws BusinessException {
		String pk="";
		try {
			pk=this.processInsCptoDao.addProcessInsCpto(ProcessInsCpto);
		} catch (Exception e) {
			logger.error("流程结束转发抄送保存失败");
			throw new BusinessException("流程结束转发抄送保存失败");
		}
		logger.info("流程结束转发抄送保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="ProcessInsCptoManager",description="流程结束转发抄送删除",detail="流程结束转发抄送${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.processInsCptoDao.deleteProcessInsCpto(id);
		} catch (Exception e) {
			logger.error("流程结束转发抄送删除失败");
			throw new BusinessException("流程结束转发抄送删除失败");
		}
		logger.info("流程结束转发抄送删除成功");
	}

	@Override
	@Action(moduleCode="ProcessInsCptoManager",description="流程结束转发抄送批量删除",detail="流程结束转发抄送${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotEmpty(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("流程结束转发抄送批量删除成功");
	}

	@Override
	@Action(moduleCode="ProcessInsCptoManager",description="流程结束转发抄送修改",detail="流程结束转发抄送${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(ProcessInstCptoEntity ProcessInsCpto) throws BusinessException {
		try {
			ProcessInstCptoEntity oldEntity = get(ProcessInsCpto.getId());
			MyBeanUtils.copyBeanNotNull2Bean(ProcessInsCpto, oldEntity);
			this.processInsCptoDao.updateProcessInsCpto(oldEntity);
		} catch (Exception e) {
			logger.error("流程结束转发抄送更新失败");
			throw new BusinessException("流程结束转发抄送更新失败");
		}
		logger.info("流程结束转发抄送更新成功");
	}

	@Override
	public ProcessInstCptoEntity get(String id){
		ProcessInstCptoEntity ProcessInsCpto=null;
		try {
			ProcessInsCpto=this.processInsCptoDao.getProcessInsCpto(id);
		} catch (Exception e) {
			logger.error("流程结束转发抄送获取失败");
			//throw new BusinessException("流程结束转发抄送获取失败");
		}
		logger.info("流程结束转发抄送获取成功");
		return ProcessInsCpto;
	}

	@Override
	public List<ProcessInstCptoEntity> queryList() throws BusinessException {
		List<ProcessInstCptoEntity> ProcessInsCptoList=new ArrayList<ProcessInstCptoEntity>();
		try {
			ProcessInsCptoList=this.processInsCptoDao.queryProcessInsCptoList();
		} catch (Exception e) {
			logger.error("流程结束转发抄送获取列表失败");
			throw new BusinessException("流程结束转发抄送获取列表失败");
		}
		logger.info("流程结束转发抄送获取列表成功");
		return ProcessInsCptoList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.processInsCptoDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("流程结束转发抄送获取分页列表失败");
			throw new BusinessException("流程结束转发抄送获取分页列表失败");
		}
		logger.info("流程结束转发抄送获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(ProcessInstCptoEntity.class, param, propertyName);
	}
	
	public void handlerCopyTask(ProcessInstanceEntity processRun,Map<String, Object> vars, String preTaskUserId,DefinitionEntity definition,String funcType) throws BusinessException {
		if (BeanUtils.isEmpty(definition))
			return;

		String actDefId = processRun.getActDefId();

		String allowFinishedCc = definition.getAllowFinishedCc();
		if(!StringUtil.isNotEmpty(vars.get("nodeId")) && !StringUtil.equals(allowFinishedCc, "1")){
			return;
		}
		if (BeanUtils.isEmpty(processRun))
			return;	
		String subject = processRun.getTitle();
		
		String startUserId = processRun.getCreateUserId();
		String instanceId = processRun.getActInstId();
		List<TaskExecutor> list = this.nodeUserService.getExecutors(actDefId,instanceId, StringUtil.isNotEmpty(vars.get("nodeId"))?vars.get("nodeId").toString():null, startUserId, vars,funcType);
		List<UserEntity> copyConditionUsers =changeUser(list);

		if (BeanUtils.isEmpty(copyConditionUsers))
			return;

		logger.info("handlerCopyTask:" + actDefId + "," + instanceId + ","+ processRun.getId() + "," + subject);

		addCopyTo(copyConditionUsers, processRun,vars);
		try {
			handlerCopyMessage(actDefId, copyConditionUsers, vars, subject,processRun.getId(), definition.getInformType());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("消息发送失败");
		}
	}

	private void addCopyTo(List<UserEntity> set, ProcessInstanceEntity processRun,Map<String,Object> vars) {
		String instanceId = processRun.getActInstId();
		for (UserEntity user:set) {
			ProcessInstCptoEntity bpmProCopyto = new ProcessInstCptoEntity();
			bpmProCopyto.setActInsId(instanceId);
			bpmProCopyto.setReceiveId(user.getId());
			bpmProCopyto.setReceiveName(user.getName());
			bpmProCopyto.setType(ProcessInstCptoEntity.CPTYPE_SEND);
			bpmProCopyto.setIsRead("0");
			if(StringUtil.isNotEmpty(vars.get("nodeId"))){
				bpmProCopyto.setNodeId(vars.get("nodeId").toString());
			}
			if(StringUtil.isNotEmpty(vars.get("nodeName"))){
				bpmProCopyto.setNodeName(vars.get("nodeName").toString());
			}
			if(StringUtil.isNotEmpty(vars.get("taskId"))){
				bpmProCopyto.setTaskId(vars.get("taskId").toString());
			}
			bpmProCopyto.setInsId(processRun.getId());
			bpmProCopyto.setSubject(processRun.getTitle());
			try {
				this.save(bpmProCopyto);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void handlerCopyMessage(String actDefId,
			List<UserEntity> receiverUserList, Map<String, Object> vars,
			String subject, String runId, String ccMessageType) throws Exception {
		if (BeanUtils.isEmpty(receiverUserList))
			return;
		UserEntity curUser = ClientUtil.getUserEntity();
		Map<String,String> msgTempMap=null;
		NodeSetService nodeSetService=ApplicationContextUtil.getBean("nodeSetService");
		if(!StringUtil.isNotEmpty(vars.get("nodeId"))){//流程结束后抄送
			msgTempMap = this.msgTemplateService.getTempByFun(MsgTemplateEntity.USE_TYPE_COPYTO);
		}else if(StringUtil.isNotEmpty(vars.get("nodeId")) && (vars.get("exeType")!=null) && StringUtil.equals("create", vars.get("exeType").toString())){//节点审批前抄送
			NodeSetEntity nodeSet=nodeSetService.getNodeSetByActDefIdNodeId(actDefId, vars.get("nodeId").toString());
			if(nodeSet!=null){
				msgTempMap=new HashMap<String,String>();
				msgTempMap.put(MsgTemplateEntity.TEMPLATE_TITLE, nodeSet.getPreCommonTitle());
				msgTempMap.put(MsgTemplateEntity.TEMPLATE_TYPE_INNER, nodeSet.getPreInnerContent());
				msgTempMap.put(MsgTemplateEntity.TEMPLATE_TYPE_MAIL, nodeSet.getPreMailContent());
				msgTempMap.put(MsgTemplateEntity.TEMPLATE_TYPE_SMS, nodeSet.getPreSmsContent());
				if(StringUtil.isNotEmpty(nodeSet.getInformType())){
					ccMessageType=nodeSet.getInformType();
				}
			}
			
		}else if(StringUtil.isNotEmpty(vars.get("nodeId")) && (vars.get("exeType")!=null) && StringUtil.equals("complete", vars.get("exeType").toString())){//节点审批前抄送
			NodeSetEntity nodeSet=nodeSetService.getNodeSetByActDefIdNodeId(actDefId, vars.get("nodeId").toString());
			if(nodeSet!=null){
				msgTempMap=new HashMap<String,String>();
				msgTempMap.put(MsgTemplateEntity.TEMPLATE_TITLE, nodeSet.getLastCommonTitle());
				msgTempMap.put(MsgTemplateEntity.TEMPLATE_TYPE_INNER, nodeSet.getLastInnerContent());
				msgTempMap.put(MsgTemplateEntity.TEMPLATE_TYPE_MAIL, nodeSet.getLastMailContent());
				msgTempMap.put(MsgTemplateEntity.TEMPLATE_TYPE_SMS, nodeSet.getLastSmsContent());
				if(StringUtil.isNotEmpty(nodeSet.getInformType())){
					ccMessageType=nodeSet.getInformType();
				}
			}
			
		}
		this.taskMessageService.sendMessage(curUser, receiverUserList,ccMessageType, msgTempMap, subject, "", null, runId,vars);
	}
	
	private List<UserEntity> changeUser(List<TaskExecutor> list){
		SysUserService sysUserService=ApplicationContextUtil.getBean("sysUserService");
		Map<String,TaskExecutor> userMapList=new HashMap<String,TaskExecutor>();
		List<UserEntity> exeList=new ArrayList<UserEntity>();
		if(list!=null && list.size()>0){
			for(TaskExecutor ex:list){
				List<UserEntity> userList=new ArrayList<UserEntity>();
				if("role".equals(ex.getType())){
					userList=sysUserService.getUserListByRoleId(ex.getExecuteId());
				}else if("pos".equals(ex.getType())){
					userList=sysUserService.getUserByCurrentOrgIds(ex.getExecuteId());
				}else if("org".equals(ex.getType())){
					userList=sysUserService.getUserByCurrentOrgIds(ex.getExecuteId());
				}else{
					userMapList.put(ex.getExecuteId(), ex);
				}
				if(userList!=null && userList.size()>0){
					for(UserEntity user:userList){
						userMapList.put(user.getId(), TaskExecutor.getTaskUser(user.getId(), user.getName()));
					}
				}
			}
		}
		for(Map.Entry<String, TaskExecutor> entry:userMapList.entrySet()){
			UserEntity user=new UserEntity();
			user.setId(entry.getValue().getExecuteId());
			user.setName(entry.getValue().getExecutor());
			exeList.add(user);
		}
		return exeList;
	}
	
	@Override
	public List<ProcessInstCptoEntity> findCptoByTaskOrInstId(String id, String type) {
		String hql = "";
		if ("actInstId".equals(type)) {
			hql = "FROM ProcessInstCptoEntity p WHERE p.actInsId=?";
		} else if ("taskId".equals(type)) {
			hql = "FROM ProcessInstCptoEntity p WHERE p.taskId=?";
		}
		return processInsCptoDao.findHql(hql, new Object[] { id });
	}
	
	public void setProcessInsCptoDao(ProcessInstCptoDao processInsCptoDao) {
		this.processInsCptoDao = processInsCptoDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
}
