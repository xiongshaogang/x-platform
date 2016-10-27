package com.xplatform.base.workflow.node.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.form.entity.FlowFormEntity;
import com.xplatform.base.form.service.FlowFormService;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.workflow.core.bpm.util.BpmUtil;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.service.NodeSetService;
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;
import com.xplatform.base.workflow.support.msgtemplate.service.MsgTemplateService;
import com.xplatform.base.workflow.task.entity.TaskDueEntity;

@Controller
@RequestMapping("/nodeSetController")
public class NodeSetController extends BaseController {

	@Resource
	private DefinitionService definitionService;
	@Resource
	private NodeSetService nodeSetService;
	@Resource
	private FlowService flowService;
	@Resource
	private FlowFormService flowFormService;
	@Resource
	private MsgTemplateService msgTemplateService;
	private AjaxJson result = new AjaxJson();

	private String message;

	/**
	 * 查看节点表单设置
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "nodeForm")
	public ModelAndView nodeForm(HttpServletRequest request, HttpServletResponse response, DefinitionEntity definition)
			throws Exception {
		String defId = definition.getId();
		String isNew = "yes";//默认为新增
		definition = definitionService.get(definition.getId());
		String deployId = definition.getActDeployId();
		List<String> nodeList = new ArrayList<String>();

		Map<String, Map<String, String>> activityList = new HashMap<String, Map<String, String>>();
		String defXml = this.flowService.getDefXmlByDeployId(deployId);
		Map<String, Map<String, String>> activityAllList = BpmUtil.getTranstoActivitys(defXml, nodeList);
		//获取所有的节点（包括全局节点）
		List<NodeSetEntity> listSel = this.nodeSetService.queryByPorperty("defId", definition.getId());
		//获取普通的任务节点
		List<NodeSetEntity> list = new ArrayList<NodeSetEntity>();
		for (NodeSetEntity nodeSetEntity : listSel) {
			if (!nodeSetEntity.getSetType().equals(NodeSetEntity.SetType_GloabalForm)) {
				list.add(nodeSetEntity);
			}
		}
		//获取所有的任务节点
		Map<String, String> taskMap = (Map<String, String>) activityAllList.get("任务节点");
		for (int i = 0; i < list.size(); i++) {
			String nodeId = ((NodeSetEntity) list.get(i)).getNodeId();
			Map<String, String> tempMap = new HashMap<String, String>();
			for (Map.Entry<String, String> entry : taskMap.entrySet()) {
				if (nodeId != null && !nodeId.equals(entry.getKey())) {
					tempMap.put(entry.getKey(), entry.getValue());
				}
			}
			activityList.put(list.get(i).getNodeId(), tempMap);
		}

		NodeSetEntity globalForm = this.nodeSetService.getBySetType1(defId, NodeSetEntity.SetType_GloabalForm);
		NodeSetEntity bpmForm = this.nodeSetService.getBySetType1(defId, NodeSetEntity.SetType_BPMForm);
		//循环判断节点有没有设置普通的节点表单
		for (NodeSetEntity nodeSet : list) {
			if(StringUtil.isNotEmpty(nodeSet.getFormKey())){
				FlowFormEntity bpmFormDef = this.flowFormService.get(nodeSet.getFormKey());
				if (BeanUtils.isNotEmpty(bpmFormDef)) {
					if ("yes".equals(isNew)) {
						isNew = "no";
					}
				}
			}
		}
		//是否设置了全局表单
		if (("yes".equals(isNew)) && ((BeanUtils.isNotEmpty(globalForm)))) {
			isNew = "no";
		}

		return new ModelAndView("workflow/node/nodeForm").addObject("bpmNodeSetList", list)//非全局节点（普通任务节点）
				.addObject("bpmDefinition", definition)//流程定义
				.addObject("globalForm", globalForm)//全局表单
				.addObject("bpmForm", bpmForm)//业务综合表单
				.addObject("activityList", activityList).addObject("isNew", isNew);//新增或者修改
	}

	/**
	 * 保存节点表单设置
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param FlowForm
	 * @return
	 */
	@RequestMapping(params = "saveNodeSet")
	@ResponseBody
	public AjaxJson saveNodeSet(HttpServletRequest request) {

		String defId = request.getParameter("defId");
		DefinitionEntity definition = definitionService.get(defId);

		String[] nodeIds = request.getParameterValues("nodeId");
		//String[] nodeNames = request.getParameterValues("nodeName");
		String[] formTypes = request.getParameterValues("formType");
		String[] aryFormKey = request.getParameterValues("formKey");
		String[] aryFormId = request.getParameterValues("formId");
		String[] formDefNames = request.getParameterValues("formName");
		String[] formUrls = request.getParameterValues("formUrl");
		
		String globalFormType = request.getParameter("globalFormType");
		String formIdGlobal = request.getParameter("formIdGlobal");
		
		String bpmFormType = request.getParameter("bpmFormType");
		String formIdBpm = request.getParameter("formIdBpm");

		Map<String, NodeSetEntity> nodeMap = this.nodeSetService.getMapByDefId(defId);
		try {

			List<NodeSetEntity> nodeList = new ArrayList<NodeSetEntity>();
			for (int i = 0; i < nodeIds.length; i++) {
				String nodeId = nodeIds[i];
				NodeSetEntity nodeSet = new NodeSetEntity();
				if (nodeMap.containsKey(nodeId)) {
					nodeSet = (NodeSetEntity) nodeMap.get(nodeId);
					if (StringUtil.isNotEmpty(nodeSet.getFormKey())) {
						nodeSet.setOldFormKey(nodeSet.getFormKey());
					}
				}

				nodeSet.setNodeId(nodeId);
				nodeSet.setActDefId(definition.getActId());
				nodeSet.setNodeName(request.getParameter("nodeName_" + nodeId));

				Integer formType = Integer.parseInt(formTypes[i]);
				String formUrl = formUrls[i];

				nodeSet.setFormType(formType.toString());

				if (formType == -1) {
					nodeSet.setFormUrl("");
					nodeSet.setFormKey("");
					nodeSet.setFormName("");
				} else if (formType == 0) {
					nodeSet.setFormUrl("");
					if (StringUtil.isNotEmpty(aryFormKey[i])) {
						nodeSet.setFormKey(aryFormKey[i]);
						nodeSet.setFormId(aryFormId[i]);
						nodeSet.setFormName(formDefNames[i]);
					} else {
						nodeSet.setFormKey("");
						nodeSet.setFormName("");
						nodeSet.setFormType("-1");
					}
				} else {
					nodeSet.setFormKey("");
					nodeSet.setFormName("");
					nodeSet.setFormId("");
					nodeSet.setFormUrl(formUrl);
				}

				nodeSet.setDefId(defId);

				String[] jumpType = request.getParameterValues("jumpType_" + nodeId);
				if (jumpType != null)
					nodeSet.setJumpType(StringUtil.asString(jumpType, ","));
				else {
					nodeSet.setJumpType("");
				}
				String isHideOption = request.getParameter("isHideOption_" + nodeId);
				if (StringUtil.isNotEmpty(isHideOption))
					nodeSet.setIsHideOption(NodeSetEntity.HIDE_OPTION);
				else {
					nodeSet.setIsHideOption(NodeSetEntity.NOT_HIDE_OPTION);
				}

				String[] backType = request.getParameterValues("isHidePath_" + nodeId);
				if (backType != null)
					nodeSet.setBackType(StringUtil.asString(backType, ","));
				else {
					nodeSet.setBackType("");
				}

				nodeSet.setSetType(NodeSetEntity.SetType_TaskNode);
				if (nodeSet.getId() != null)
					nodeList.add(nodeSet);
			}
			List<NodeSetEntity> list1 = getGlobalBpm(request, definition);
			List<NodeSetEntity> list2 = getBpmForm(request, definition);
			nodeList.addAll(list1);
			nodeList.addAll(list2);
			if (nodeList != null && nodeList.size() > 0) {
				for (NodeSetEntity nodeSet : nodeList) {
					this.nodeSetService.save(nodeSet);
				}
			}
			if (globalFormType == null || "-1".equals(globalFormType)) {
				if (StringUtil.isNotEmpty(formIdGlobal)) {
					nodeSetService.delete(formIdGlobal);
				}
			}
			if (bpmFormType == null || "-1".equals(bpmFormType)) {
				if (StringUtil.isNotEmpty(formIdBpm)) {
					nodeSetService.delete(formIdBpm);
				}
			}
			message = "保存成功";
		} catch (Exception e) {
			// TODO: handle exception
			message = "保存失败";
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年11月3日 上午11:52:47
	 * @Decription 构造全局表单Entity
	 *
	 * @param request
	 * @param bpmDefinition
	 * @return
	 */
	private List<NodeSetEntity> getGlobalBpm(HttpServletRequest request, DefinitionEntity bpmDefinition) {
		List<NodeSetEntity> list = new ArrayList<NodeSetEntity>();

		String globalFormTypestr = request.getParameter("globalFormType");
		if (StringUtil.isEmpty(globalFormTypestr)) {
			globalFormTypestr = "-1";
		}
		Integer globalFormType = Integer.parseInt(globalFormTypestr);

		if (globalFormType >= 0) {
			String defaultFormKey = request.getParameter("defaultFormKey");
			String defaultFormName = request.getParameter("defaultFormName");
			String defaultFormId = request.getParameter("defaultFormId");
			String formUrlGlobal = request.getParameter("formUrlGlobal");

			NodeSetEntity NodeSetEntity = new NodeSetEntity();
			NodeSetEntity.setDefId(bpmDefinition.getId());
			NodeSetEntity.setActDefId(bpmDefinition.getActId());
			NodeSetEntity.setFormKey(defaultFormKey);
			NodeSetEntity.setFormName(defaultFormName);
			NodeSetEntity.setFormId(defaultFormId);
			NodeSetEntity.setFormUrl(formUrlGlobal);
			NodeSetEntity.setFormType(globalFormType.toString());
			NodeSetEntity.setSetType(NodeSetEntity.SetType_GloabalForm);

			if (StringUtil.equals(globalFormType.toString(), NodeSetEntity.FORM_TYPE_ONLINE)) {
				if (StringUtil.isNotEmpty(defaultFormId)) {
					list.add(NodeSetEntity);
				}

			} else if (StringUtil.isNotEmpty(formUrlGlobal)) {
				NodeSetEntity.setFormKey(null);
				list.add(NodeSetEntity);
			}

		}
		return list;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年11月3日 上午11:52:36
	 * @Decription 构造业务综合表单Entity
	 *
	 * @param request
	 * @param bpmDefinition
	 * @return
	 */
	private List<NodeSetEntity> getBpmForm(HttpServletRequest request, DefinitionEntity bpmDefinition) {
		List<NodeSetEntity> list = new ArrayList<NodeSetEntity>();

		String bpmFormTypeStr = request.getParameter("bpmFormType");
		if (StringUtil.isEmpty(bpmFormTypeStr)) {
			bpmFormTypeStr = "-1";
		}
		Integer bpmFormType = Integer.parseInt(bpmFormTypeStr);

		if (bpmFormType >= 0) {
			String bpmFormKey = request.getParameter("bpmFormKey");
			String bpmFormName = request.getParameter("bpmFormName");
			String bpmFormId = request.getParameter("bpmFormId");
			String bpmFormUrl = request.getParameter("bpmFormUrl");

			NodeSetEntity NodeSetEntity = new NodeSetEntity();
			NodeSetEntity.setDefId(bpmDefinition.getId());
			NodeSetEntity.setActDefId(bpmDefinition.getActId());
			NodeSetEntity.setFormKey(bpmFormKey);
			NodeSetEntity.setFormName(bpmFormName);
			NodeSetEntity.setFormId(bpmFormId);
			NodeSetEntity.setFormUrl(bpmFormUrl);
			NodeSetEntity.setFormType(bpmFormType.toString());
			NodeSetEntity.setSetType(NodeSetEntity.SetType_BPMForm);

			if (StringUtil.equals(bpmFormType.toString(), NodeSetEntity.FORM_TYPE_ONLINE)) {
				if (StringUtil.isNotEmpty(bpmFormId)) {
					list.add(NodeSetEntity);
				}

			} else if (StringUtil.isNotEmpty(bpmFormUrl)) {
				NodeSetEntity.setFormKey(null);
				list.add(NodeSetEntity);
			}
		}
		return list;
	}

	/*private String getHandler(String handler) {
		if ((StringUtil.isEmpty(handler)) || (handler.indexOf(".") == -1)) {
			handler = "";
		}
		return handler;
	}*/

	@RequestMapping(params = "infoType")
	public ModelAndView informType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String actDefId = request.getParameter("actDefId");
		String nodeId = request.getParameter("nodeId");
		String defId = request.getParameter("defId");
		NodeSetEntity bpmNodeSet = this.nodeSetService.getNodeSetByActDefIdNodeId(actDefId, nodeId);

		//获得催办默认模板
		MsgTemplateEntity preMsgTemplate = msgTemplateService
				.getDefaultByUseType(MsgTemplateEntity.USE_TYPE_PRE_NOTICE_USER);
		MsgTemplateEntity lastMsgTemplate = msgTemplateService
				.getDefaultByUseType(MsgTemplateEntity.USE_TYPE_LAST_NOTICE_USER);
		String preInnerContent = null;
		String preMailContent = null;
		String preSmsContent = null;
		String preCommonTitle = null;
		String lastInnerContent = null;
		String lastMailContent = null;
		String lastCommonTitle = null;
		String lastSmsContent = null;
		if (preMsgTemplate != null) {
			preInnerContent = preMsgTemplate.getInnerContent();
			preMailContent = preMsgTemplate.getMailContent();
			preSmsContent = preMsgTemplate.getSmsContent();
			preCommonTitle = preMsgTemplate.getTitle();
		}
		if (lastMsgTemplate != null) {
			lastInnerContent = lastMsgTemplate.getInnerContent();
			lastMailContent = lastMsgTemplate.getMailContent();
			lastCommonTitle = lastMsgTemplate.getTitle();
			lastSmsContent = lastMsgTemplate.getSmsContent();
		}

		if (bpmNodeSet != null) {
			if (StringUtil.isEmpty(bpmNodeSet.getPreInnerContent())
					&& StringUtil.isEmpty(bpmNodeSet.getPreCommonTitle())
					&& StringUtil.isEmpty(bpmNodeSet.getPreSmsContent())
					&& StringUtil.isEmpty(bpmNodeSet.getPreMailContent())
					&& StringUtil.isEmpty(bpmNodeSet.getLastInnerContent())
					&& StringUtil.isEmpty(bpmNodeSet.getLastCommonTitle())
					&& StringUtil.isEmpty(bpmNodeSet.getLastSmsContent())
					&& StringUtil.isEmpty(bpmNodeSet.getLastMailContent())) {
				bpmNodeSet.setPreInnerContent(preInnerContent);
				bpmNodeSet.setPreCommonTitle(preCommonTitle);
				bpmNodeSet.setPreMailContent(preMailContent);
				bpmNodeSet.setPreSmsContent(preSmsContent);

				bpmNodeSet.setLastInnerContent(lastInnerContent);
				bpmNodeSet.setLastCommonTitle(lastCommonTitle);
				bpmNodeSet.setLastMailContent(lastMailContent);
				bpmNodeSet.setLastSmsContent(lastSmsContent);
			}
		}
		request.setAttribute("defId", defId);
		return new ModelAndView("workflow/node/nodeInfoType").addObject("bpmNodeSet", bpmNodeSet);
	}

	@RequestMapping(params = "saveInfoType")
	@ResponseBody
	public AjaxJson saveInformType(HttpServletRequest request, HttpServletResponse response, NodeSetEntity nodeSet)
			throws Exception {
		try {
			message = "节点通知类型设置成功";
			this.nodeSetService.update(nodeSet);
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}

	public void setDefinitionService(DefinitionService definitionService) {
		this.definitionService = definitionService;
	}

	public void setNodeSetService(NodeSetService nodeSetService) {
		this.nodeSetService = nodeSetService;
	}

	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}
}
