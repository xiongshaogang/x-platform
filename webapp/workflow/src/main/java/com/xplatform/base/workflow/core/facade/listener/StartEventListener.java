package com.xplatform.base.workflow.core.facade.listener;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;














import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import com.xplatform.base.form.entity.AppFormApproveUser;
import com.xplatform.base.form.entity.FlowInstanceUserEntity;
import com.xplatform.base.form.service.FlowInstanceUserService;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.orggroup.entity.OrgGroupEntity;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.workflow.core.bpm.util.BpmConst;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstanceService;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;
import com.xplatform.base.workflow.node.service.NodeSetService;
import com.xplatform.base.workflow.node.service.NodeUserService;
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;
import com.xplatform.base.workflow.task.service.TaskMessageService;
import com.xplatform.base.workflow.threadlocal.TaskThreadService;

public class StartEventListener extends BaseNodeEventListener {
	 /** 
	  * serialVersionUID
	  */
	private static final long serialVersionUID = 1L;
	private ProcessInstanceService processInstanceService = ApplicationContextUtil.getBean("processInstanceService");
	private OrgGroupService orgGroupservice = ApplicationContextUtil.getBean("orgGroupService");
	private NodeUserService nodeUserService = ApplicationContextUtil.getBean("nodeUserService");
	private NodeSetService nodeSetService = ApplicationContextUtil.getBean("nodeSetService");
	private FlowInstanceUserService flowInstanceUserService = ApplicationContextUtil.getBean("flowInstanceUserService");
	private TaskMessageService taskMessageService = ApplicationContextUtil.getBean("taskMessageService");
	private SysUserService sysUserService = ApplicationContextUtil.getBean("sysUserService");

	protected void execute(DelegateExecution execution, String actDefId,String nodeId) {
		
		ProcessInstanceEntity processInstan=TaskThreadService.getProcessCmd().getProcessRun();
		processInstan.setActInstId(execution.getProcessInstanceId());
		Map<String,Object> vars = execution.getVariables();
		String startUserId = (String) vars.get("startUser");
		try {
			//创建群
			
			Set<String> set=new HashSet<String>();
			
			Integer isStartAssign=TaskThreadService.getProcessCmd().getVariables().get("isStartAssign")==null?0:Integer.parseInt(TaskThreadService.getProcessCmd().getVariables().get("isStartAssign").toString());
            if(isStartAssign==1){
            	List<FlowInstanceUserEntity> list=flowInstanceUserService.queryFIUListByBus(TaskThreadService.getProcessCmd().getBusinessKey());
            	if(list!=null && list.size()>0){
            		for(FlowInstanceUserEntity user:list){
            			if(StringUtil.equals("user", user.getType())){
    						if(!set.contains(user.getUserId())){
    							set.add(user.getUserId());
    						}
    					}else if(StringUtil.equals("role", user.getType())){
    						List<UserEntity> userList=this.sysUserService.getUserListByRoleId(user.getUserId());
    						if(userList!=null && userList.size()>0){
    							for(UserEntity userR:userList){
    								set.add(userR.getId());
    							}
    						}
    					}
            			
            			
            		}
            	}
            }else{
    			List<NodeSetEntity> nodeList=nodeSetService.getTaskNodeListByActDefId(actDefId);
    			for(NodeSetEntity nodeSet:nodeList){
    				List<TaskExecutor> users= nodeUserService.getExecutors(actDefId, execution.getProcessInstanceId(), nodeSet.getNodeId(),startUserId, vars,NodeUserEntity.FUNC_NODE_USER);
    				for(TaskExecutor user:users){
    					if(StringUtil.equals("user", user.getType())){
    						if(!set.contains(user.getExecuteId())){
    							set.add(user.getExecuteId());
    						}
    					}else if(StringUtil.equals("role", user.getType())){
    						List<UserEntity> userList=this.sysUserService.getUserListByRoleId(user.getExecuteId());
    						if(userList!=null && userList.size()>0){
    							for(UserEntity userR:userList){
    								set.add(userR.getId());
    							}
    						}
    					}
    				}
    			}
            }
			
			AjaxJson result = null;
			try {
				OrgGroupEntity orgGroup =new OrgGroupEntity();
				orgGroup.setOwner(startUserId);//群主id
				orgGroup.setAllowinvites(0);
				orgGroup.setName(TaskThreadService.getProcessCmd().getSubject());
				orgGroup.setMaxusers(1000);
				String userIds=null;
				if(set!=null && set.size()>0){
					userIds=StringUtil.toString(set);
				}
				if(StringUtil.isNotEmpty(userIds) && !StringUtil.equals(startUserId, userIds)){
					result = this.orgGroupservice.saveAndProcessHX(orgGroup, null, userIds,0);
				}else {
					result = this.orgGroupservice.saveAndProcessHX(orgGroup,0);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			OrgGroupEntity orgGroupResult=(OrgGroupEntity)result.getObj();
			//创建流程实例扩展
			TaskThreadService.getProcessCmd().getVariables().put("groupId", orgGroupResult.getId());
			vars.put("groupId",orgGroupResult.getId());
			processInstan.setGroupId(orgGroupResult.getId());
			processInstanceService.save(processInstan);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handExtSubProcess(execution);
		
		try {
			System.out.println("*******************************************流程启动发给发起人***********************************");
			vars.put("msgTemplateCode",MsgTemplateEntity.USE_TYPE_STARTFLOW);
			vars.put("sourceType",BusinessConst.SourceType_CODE_flowNotice);
			
//			vars.put("businessKey",);
//			vars.put("formCode",);
			
			taskMessageService.sendMessage(startUserId, vars);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void handExtSubProcess(DelegateExecution execution) {
		ExecutionEntity ent = (ExecutionEntity) execution;

		if (execution.getVariable("innerPassVars") == null)
			return;
	}
	

	protected String getScriptType() {
		return BpmConst.StartScript;
	}
}