package com.xplatform.base.workflow.node.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.workflow.node.entity.NodeMessageEntity;
import com.xplatform.base.workflow.node.service.NodeMessageService;
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;
import com.xplatform.base.workflow.support.msgtemplate.service.MsgTemplateService;

@Controller
@RequestMapping("/nodeMessageController")
public class NodeMessageController extends BaseController{
	
	@Resource
	private NodeMessageService nodeMessageService;
	
	@Resource
	private MsgTemplateService msgTemplateService;
	
    private AjaxJson result = new AjaxJson();
	
	private String message;
	
	
	@RequestMapping(params = "nodeMessage")
	public ModelAndView NodeMessage(HttpServletRequest request) {
		String actDefId = request.getParameter("actDefId");
		String nodeId = request.getParameter("nodeId");
		Map<String,String> params = new HashMap<String,String>();
		params.put("nodeId", nodeId);
		params.put("actDefId", actDefId);
		List<NodeMessageEntity> list = nodeMessageService.getNodeMessageList(params);
		NodeMessageEntity email = null;
		NodeMessageEntity message = null;
		NodeMessageEntity sms = null;
		for(NodeMessageEntity nme : list){
			if("email".equals(nme.getMessageType())){
				email = nme;
			}else if("innerMessage".equals(nme.getMessageType())){
				message = nme;
			}else if("sms".equals(nme.getMessageType())){
				sms = nme;
			}
		}
		return new ModelAndView("workflow/node/nodeMessage").addObject("email",email).addObject("message",message).addObject("sms", sms);
	}
	
	@RequestMapping(params = "saveNodeMessage")
	@ResponseBody
	public AjaxJson saveNodeMessage(HttpServletRequest request) {
		String actDefId = request.getParameter("actDefId");
		String nodeId = request.getParameter("nodeId");
		Map<String,String> params = new HashMap<String,String>();
		params.put("nodeId", nodeId);
		params.put("actDefId", actDefId);
		params.put("messageType", "email");
	    NodeMessageEntity email = nodeMessageService.getEntity(params);
		if(email == null){
			NodeMessageEntity n = new NodeMessageEntity();
			n.setNodeId(nodeId);
			n.setActDefId(actDefId);
			n.setMessageType("email");
			n.setSubject(request.getParameter("emailSubject"));
			n.setTemplate(request.getParameter("emailTemplate"));
			n.setIsSend(request.getParameter("emailSend"));
			this.nodeMessageService.save(n);
		}else{
			email.setNodeId(nodeId);
			email.setActDefId(actDefId);
			email.setMessageType("email");
			email.setSubject(request.getParameter("emailSubject"));
			email.setTemplate(request.getParameter("emailTemplate"));
			email.setIsSend(request.getParameter("emailSend"));
			this.nodeMessageService.update(email);
		}
		params.put("messageType", "innerMessage");
	    NodeMessageEntity innerMessage = nodeMessageService.getEntity(params);
	    if(innerMessage == null){
			NodeMessageEntity n = new NodeMessageEntity();
			n.setNodeId(nodeId);
			n.setActDefId(actDefId);
			n.setMessageType("innerMessage");
			n.setSubject(request.getParameter("messageSubject"));
			n.setTemplate(request.getParameter("messageTemplate"));
			n.setIsSend(request.getParameter("messageSend"));
			this.nodeMessageService.save(n);
		}else{
			innerMessage.setNodeId(nodeId);
			innerMessage.setActDefId(actDefId);
			innerMessage.setMessageType("innerMessage");
			innerMessage.setSubject(request.getParameter("messageSubject"));
			innerMessage.setTemplate(request.getParameter("messageTemplate"));
			innerMessage.setIsSend(request.getParameter("messageSend"));
			this.nodeMessageService.update(innerMessage);
		}
	    params.put("messageType", "sms");
	    NodeMessageEntity sms = nodeMessageService.getEntity(params);
	    if(sms == null){
			NodeMessageEntity n = new NodeMessageEntity();
			n.setNodeId(nodeId);
			n.setActDefId(actDefId);
			n.setMessageType("sms");
			n.setSubject(request.getParameter("smsSubject"));
			n.setTemplate(request.getParameter("smsTemplate"));
			n.setIsSend(request.getParameter("smsSend"));
			this.nodeMessageService.save(n);
		}else{
			sms.setNodeId(nodeId);
			sms.setActDefId(actDefId);
			sms.setMessageType("sms");
			sms.setSubject(request.getParameter("smsSubject"));
			sms.setTemplate(request.getParameter("smsTemplate"));
			sms.setIsSend(request.getParameter("smsSend"));
			this.nodeMessageService.update(sms);
		}
		message = "保存成功";
		result.setMsg(message);
		return result;
	}
	
	
	/**
	 * 取得消息模板记录
	 */
	@RequestMapping(params = "getTemplate")
	@ResponseBody
	public List<MsgTemplateEntity> getTemplate(HttpServletRequest request) {
		List<MsgTemplateEntity> list = msgTemplateService.queryList();
		return list;
	}
	
	/**
	 * 取得消息模板内容
	 */
	@RequestMapping(params = "getContent")
	@ResponseBody
	public String getContent(HttpServletRequest request) {
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		if("email".equals(type)){
			return this.msgTemplateService.get(id).getMailContent();	
		}else if("msg".equals(type)){
			return this.msgTemplateService.get(id).getInnerContent();
		}else if("sms".equals(type)){
			return this.msgTemplateService.get(id).getSmsContent();
		}
		return null;
	}
	
	public void setNodeMessageService(NodeMessageService nodeMessageService) {
		this.nodeMessageService = nodeMessageService;
	}

	public void setMsgTemplateService(MsgTemplateService msgTemplateService) {
		this.msgTemplateService = msgTemplateService;
	}
	
}
