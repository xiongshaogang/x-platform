package com.xplatform.base.workflow.task.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xplatform.base.form.entity.FlowFormEntity;
import com.xplatform.base.form.service.AppFormTableService;
import com.xplatform.base.form.service.FlowFormService;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.message.config.dao.MessageDao;
import com.xplatform.base.system.message.config.entity.MessageSendEntity;
import com.xplatform.base.system.message.config.service.MessageService;
import com.xplatform.base.system.message.config.util.MsgUtil;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.service.NodeSetService;
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;
import com.xplatform.base.workflow.support.msgtemplate.service.MsgTemplateService;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.service.TaskMessageService;
import com.xplatform.base.workflow.task.service.TaskUserService;
import com.xplatform.base.workflow.util.FlowUtil;

@Service("taskMessageService")
public class TaskMessageServiceImpl implements TaskMessageService {
	@Resource
	private MessageDao messageDao;
	@Resource
	private TaskUserService taskUserService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private MsgTemplateService msgTemplateService;
	@Resource
	private MessageService messageService;
	@Resource
	private OrgGroupService orgGroupService;
	@Resource
	private NodeSetService nodeSetService;
	@Resource
	private FlowFormService flowFormService;
	@Resource
	private AppFormTableService appFormTableService;

	private void pushUser(Map<UserEntity, List<Task>> users, UserEntity user, Task task) {
		if (users.containsKey(user)) {
			users.get(user).add(task);
		} else {
			List<Task> list = new ArrayList<Task>();
			list.add(task);
			users.put(user, list);
		}
	}

	// 获取默认模版内容
	private Map<String, String> getDefaultTemp() throws BusinessException {
		MsgTemplateEntity temp = this.msgTemplateService.getDefaultByUseType(MsgTemplateEntity.USE_TYPE_NOTIFY);
		if (temp == null) {
			throw new BusinessException("模板中未找到内部消息的默认模板或系统模板");
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("inner", temp.getInnerContent());
		map.put("email", temp.getMailContent());
		map.put("sms", temp.getSmsContent());
		map.put(MsgTemplateEntity.TEMPLATE_TITLE, temp.getTitle());
		return map;
	}

	private Map<String, String> getTempByUseType(String useType) throws BusinessException {
		MsgTemplateEntity temp = this.msgTemplateService.getDefaultByUseType(useType);
		if (temp == null) {
			throw new BusinessException("模板中未找到内部消息的默认模板或系统模板");
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("inner", temp.getInnerContent());
		map.put("email", temp.getMailContent());
		map.put("sms", temp.getSmsContent());
		map.put(MsgTemplateEntity.TEMPLATE_TITLE, temp.getTitle());
		return map;
	}

	@Override
	public void notify(List<Task> taskList, String informTypes, String subject, Map<String, String> map, String opinion, Map<String, Object> vars)
			throws BusinessException {
		// TODO Auto-generated method stub
		if (StringUtil.isEmpty(informTypes))
			return;
		Map<UserEntity, List<Task>> users = new HashMap<UserEntity, List<Task>>();
		Set<UserEntity> cUIds;

		for (Task task : taskList) {
			String assignee = task.getAssignee();

			if (FlowUtil.isAssigneeNotEmpty(assignee)) {
				UserEntity user = this.sysUserService.getUserById(assignee);
				pushUser(users, user, task);
			} else {
				cUIds = this.taskUserService.getCandidateUsers(task.getId());
				for (UserEntity user : cUIds) {
					pushUser(users, user, task);
				}
			}
		}

		UserEntity curUser = ClientUtil.getUserEntity();
		String receiverName = curUser.getName();
		for (UserEntity user : users.keySet()) {
			List<Task> tasks = (List<Task>) users.get(user);
			for (Task task : tasks) {
				
					boolean isAgentTask = TaskOpinionEntity.STATUS_AGENT.equals(task.getDescription());
					try {
						if (map == null) {
							if (isAgentTask)
								map = getTempByUseType(MsgTemplateEntity.USE_TYPE_NOTIFYOWNER_AGENT);
							else {
								map = getDefaultTemp();
							}
						}
					} catch (Exception e) {
						throw new BusinessException("模板中未找到内部消息的默认模板或系统模板");
					}

					String titleTemplate = (String) map.get(MsgTemplateEntity.TEMPLATE_TITLE);
					String smsTemplate = (String) map.get(MsgTemplateEntity.TEMPLATE_TYPE_SMS);
					String mailTemplate = (String) map.get(MsgTemplateEntity.TEMPLATE_TYPE_MAIL);
					String innerTemplate = (String) map.get(MsgTemplateEntity.TEMPLATE_TYPE_INNER);
					if (isAgentTask) {
						user = this.sysUserService.getUserById(task.getOwner());
					}

					MessageSendEntity msgSend = new MessageSendEntity();
					msgSend.setReceive(user.getId());
					
					// 邮件内容
					if (informTypes.contains("email")) {
						String url = "";
						if (BeanUtils.isNotEmpty(task.getId())) {
							url = FlowUtil.getUrl(task.getId(), true);
						}
						String title = FlowUtil.replaceTitleTag(titleTemplate, receiverName, "系统消息", subject, opinion, vars);
						String content = FlowUtil.replaceTemplateTag(mailTemplate, receiverName, "系统消息", subject, url, opinion, true, vars);
						msgSend.setTitle(title);
						msgSend.setMailContent(content);
					}

					// 短信内容
					if (informTypes.contains("sms")) {
						String content = FlowUtil.replaceTemplateTag(smsTemplate, receiverName, "系统消息", subject, "", opinion, true, vars);
						msgSend.setSmsContent(content);
					}

					// 站内信内容
					if (informTypes.contains("innerMessage")) {
						String url = "";
						if (BeanUtils.isNotEmpty(task.getId())) {
							url = FlowUtil.getUrl(task.getId(), true);
						}
						if (StringUtil.isNotEmpty(url)) {
							url = url.replace("http://", "");
							url = url.substring(url.indexOf("/"), url.length());
						}

						String title = FlowUtil.replaceTitleTag(titleTemplate, receiverName, "系统消息", subject, opinion, vars);
						String content = FlowUtil.replaceTemplateTag(innerTemplate, receiverName, "系统消息", subject, url, opinion, false, vars);
						msgSend.setTitle(title);
						msgSend.setContent(content);
						msgSend.setSendChannel("IM,msg");
					}
					//Map<String,Object> extraData=new HashMap<String,Object>();
					/*List<Map<String,Object>> extraData=new ArrayList<Map<String,Object>>();
					Map<String,Object> data=new HashMap<String,Object>();
					data.put("name", "项目名称");
					data.put("value", "河南担保项目");
					extraData.add(data);
					data=new HashMap<String,Object>();
					data.put("name", "开始时间");
					data.put("value", "2015-5-28");
					extraData.add(data);
					data=new HashMap<String,Object>();
					data.put("name", "评论意见");
					data.put("value", "我已经完成了所有的待审批任务，准备接受 下一个审批任务");
					extraData.add(data);
					String groupId=(String)vars.get("groupId");
					Map<String,Object> groupUserList=orgGroupService.getUserOrg(groupId);
					Map<String,Object> extra=new HashMap<String,Object>();
					extra.put("groupId", groupId);
					extra.put("currentUserId", user.getId());
					extra.put("title", "流程代办测试消息");
					extra.put("header", "张三的出差需要您的审批");
					extra.put("description", "张三的出差需要您的审批");
					extra.put("content", "张三的出差需要您的审批");
					extra.put("url", "taskController.do?toStart&taskId="+task.getId());
					extra.put("img", "4028810350c618e20150c621c2940002");
					extra.put("createTime", "2016-4-23");
					extra.put("groupUserList", groupUserList.get("OrgGroupMember"));
					extra.put("approveStatus", "1");
					extra.put("footer", "帮帮邦团队 10月15日");
					extra.put("extraData", extraData);*/
					NodeSetEntity bpmNodeSet = this.nodeSetService.getNodeSetByActDefIdNodeId(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
					if(bpmNodeSet==null){
						bpmNodeSet= this.nodeSetService.getGlobalByActDefId(task.getProcessDefinitionId());
					}else{
						//节点表单设置为空，取全局的表单设置
						if (StringUtil.isEmpty(bpmNodeSet.getFormId())&&StringUtil.isEmpty(bpmNodeSet.getFormUrl())) {
							NodeSetEntity globalNodeSet = this.nodeSetService.getGlobalByActDefId(task.getProcessDefinitionId());
							bpmNodeSet.setFormId(globalNodeSet.getFormId());
							bpmNodeSet.setFormType(globalNodeSet.getFormType());
						}
					}
					String fromId=bpmNodeSet.getFormId();
					FlowFormEntity flowForm=flowFormService.get(fromId);
					try {
						Map<String,Object> extra=appFormTableService.getOneFieldData(flowForm.getCode(),(String)vars.get("businessKey"));
						if(extra==null){
							extra=new HashMap<String,Object>();
						}
						String groupId=(String)vars.get("groupId");
						if(StringUtil.isNotEmpty(groupId)){
							extra.put("groupId", groupId);
							Map<String,Object> groupUserList=orgGroupService.getUserOrg(groupId);
							extra.put("groupUserList", groupUserList.get("OrgGroupMember"));
						}
						extra.put("currentUserId", user.getId());
						extra.put("createTime", DateUtils.formatTime(task.getCreateTime()));
						extra.put("approveStatus", "1");//审批中
						extra.put("taskId", task.getId());//审批中
						extra.put("url", "taskController.do?toStart&taskId="+task.getId());
						msgSend.setSourceType("workflow");
						msgSend.setSourceId((String)vars.get("businessKey"));
						msgSend.setExtra(JSONHelper.toJSONString(extra));
						MsgUtil.SendMulMeassage(msgSend);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		}

	}

	@Override
	public void sendMessage(String receiveUserIds, Map<String, Object> vars) throws BusinessException {
		List<UserEntity> receiverUserList = sysUserService.getUserByIds(receiveUserIds);
		Object obj = vars.get("msgTemplateCode");
		Map<String, String> msgTempMap = null;
		if (obj != null) {
			msgTempMap = this.msgTemplateService.getTempByFun(obj.toString());
		}
		sendMessage(null, receiverUserList, "msg,IM", msgTempMap, null, null, null, null, vars);
	}
			
			
	@Override
	public void sendMessage(UserEntity sendUser, List<UserEntity> receiverUserList, String informTypes, Map<String, String> msgTempMap, String subject,
			String opinion, String taskId, String runId, Map<String, Object> vars) throws BusinessException {
		// TODO Auto-generated method stub
		if (StringUtil.isEmpty(informTypes))
			return;
		if (BeanUtils.isEmpty(sendUser)) {
			sendUser = new UserEntity();
			sendUser.setId("0");
			sendUser.setName("系统消息");
		}
		if (BeanUtils.isEmpty(receiverUserList))
			return;
		String id = null;
		boolean isTask = true;
		String messageType = "";
		if (BeanUtils.isNotEmpty(taskId)) {
			id = taskId;
			isTask = true;
			messageType = "task";
		} else {
			id = runId;
			isTask = false;
			messageType = "processInstance";
		}
		
		String titleTemplate = "";
		String smsTemplate = "";
		String mailTemplate = "";
		String innerTemplate = "";
		if (BeanUtils.isNotEmpty(msgTempMap)) {
			titleTemplate = (String) msgTempMap.get(MsgTemplateEntity.TEMPLATE_TITLE);
			smsTemplate = (String) msgTempMap.get(MsgTemplateEntity.TEMPLATE_TYPE_SMS);
			mailTemplate = (String) msgTempMap.get(MsgTemplateEntity.TEMPLATE_TYPE_MAIL);
			innerTemplate = (String) msgTempMap.get(MsgTemplateEntity.TEMPLATE_TYPE_INNER);
		} else {
			titleTemplate = "流程相关消息";
			innerTemplate = "流程相关消息";
		}
		
		smsTemplate = StringUtil.jsonUnescape(smsTemplate);
		mailTemplate = StringUtil.jsonUnescape(mailTemplate);
		int userSize = receiverUserList.size();

		for (int i = 0; i < userSize; i++) {
			UserEntity receiverUser = sysUserService.getUserById(receiverUserList.get(i).getId());

			Object messageSendObj = vars.get("MessageSendEntity");
			MessageSendEntity msgSend = messageSendObj == null ? new MessageSendEntity() : (MessageSendEntity) messageSendObj;
			String title="";
			msgSend.setReceive(receiverUser.getId());// 设置接收人
			msgSend.setSendChannel(informTypes);
			// 修改邮件内容
			if (informTypes.contains("email")) {

				String url = "";
				if (BeanUtils.isNotEmpty(id)) {
					url = FlowUtil.getUrl(id, true);
				}
				title = FlowUtil.replaceTitleTag(titleTemplate, receiverUser.getName(), sendUser.getName(), subject, opinion, vars);
				String content = FlowUtil.replaceTemplateTag(mailTemplate, receiverUser.getName(), sendUser.getName(), subject, url, opinion, isTask, vars);
				msgSend.setTitle(title);
				msgSend.setMailContent(content);
			}

			// 修改短信内容
			if (informTypes.contains("sms")) {
				String content = FlowUtil.replaceTemplateTag(smsTemplate, receiverUser.getName(), sendUser.getName(), subject, "", opinion, isTask, vars);
				msgSend.setSmsContent(content);
			}

			// 修改站内信内容
			if (informTypes.contains("msg")) {
				String url = "";
				if (BeanUtils.isNotEmpty(id)) {
					url = FlowUtil.getUrl(id, true);
				}
				if (StringUtil.isNotEmpty(url)) {
					url = url.replace("http://", "");
					url = url.substring(url.indexOf("/"), url.length());
				}

				title = FlowUtil.replaceTitleTag(titleTemplate, receiverUser.getName(), sendUser.getName(), subject, opinion, vars);
				String content = FlowUtil.replaceTemplateTag(innerTemplate, receiverUser.getName(), sendUser.getName(), subject, url, opinion, isTask, vars);
				msgSend.setTitle(title);
				msgSend.setContent(content);
			}

			// 修改IM内容
			if (informTypes.contains("IM")) {
				String businessKey = vars.get("businessKey").toString();
				String formCode = vars.get("formCode").toString();
				String sourceType = vars.get("sourceType") == null ? BusinessConst.SourceType_CODE_workflow : vars.get("sourceType").toString();
				String sourceBusinessType = vars.get("sourceBusinessType") == null ? null : vars.get("sourceBusinessType").toString();
				String groupId = vars.get("groupId") == null ? null : vars.get("groupId").toString();
				String createTime = vars.get("createTime") == null ? DateUtils.formatTime(new Date()) : DateUtils.formatTime((Date) vars.get("createTime"));
				String url = vars.get("url") == null ? "appFormTableController.do?commonFormEdit&viewType=viewProcess&formCode=" + formCode + "&businessKey="
						+ businessKey : vars.get("url").toString();

				msgSend.setSendChannel(informTypes);
				msgSend.setReceive(receiverUser.getId());
				msgSend.setSourceType(sourceType);
				msgSend.setSourceBusinessType(sourceBusinessType);
				msgSend.setSourceId(businessKey);
				Map<String, Object> extra = null;
				try {
					if (StringUtil.isNotEmpty(formCode) && StringUtil.isNotEmpty(businessKey)) {
						extra = appFormTableService.getOneFieldData(formCode, businessKey);
					}
					if (extra == null) {
						extra = new HashMap<String, Object>();
					}
					if (StringUtil.isNotEmpty(groupId)) {
						extra.put("groupId", groupId);
						Map<String, Object> groupUserList = orgGroupService.getUserOrg(groupId);
						extra.put("groupUserList", groupUserList.get("OrgGroupMember"));
					}
					// extra.put("currentUserId", receiverUser.getId());
					extra.put("content", title);
					extra.put("createTime", createTime);// 发送时间为当前时间
					extra.put("url", url);// 发送时间为当前时间
					// extra.put("approveStatus", "1");//审批中
					// extra.put("taskId", task.getId());//审批中
					msgSend.setExtra(JSONHelper.toJSONString(extra));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			MsgUtil.SendMulMeassage(msgSend);
		}
	}

}
