package com.xplatform.base.workflow.definition.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.form.entity.AppFormApproveUser;
import com.xplatform.base.form.entity.FlowFormEntity;
import com.xplatform.base.form.service.AppFormApproveUserService;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.common.model.json.ResultMessage;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.PinyinUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.orgnaization.service.OrgnaizationService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.system.type.entity.TypeEntity;
import com.xplatform.base.workflow.approve.entity.ApproveItemEntity;
import com.xplatform.base.workflow.approve.service.ApproveItemService;
import com.xplatform.base.workflow.core.bpm.graph.ShapeMeta;
import com.xplatform.base.workflow.core.bpm.model.BpmNode;
import com.xplatform.base.workflow.core.bpm.model.FlowNode;
import com.xplatform.base.workflow.core.bpm.model.NodeCache;
import com.xplatform.base.workflow.core.bpm.util.BpmUtil;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;
import com.xplatform.base.workflow.history.mybatis.vo.ProcessTaskHistory;
import com.xplatform.base.workflow.history.service.HistoryTaskInstanceService;
import com.xplatform.base.workflow.instance.entity.ProcessInstHistoryEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstHistoryService;
import com.xplatform.base.workflow.node.entity.NodeRuleEntity;
import com.xplatform.base.workflow.node.entity.NodeScriptEntity;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.entity.NodeUserConditionEntity;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;
import com.xplatform.base.workflow.node.model.NodeUserMap;
import com.xplatform.base.workflow.node.service.NodeRuleService;
import com.xplatform.base.workflow.node.service.NodeScriptService;
import com.xplatform.base.workflow.node.service.NodeSetService;
import com.xplatform.base.workflow.node.service.NodeUserConditionService;
import com.xplatform.base.workflow.node.service.NodeUserService;
import com.xplatform.base.workflow.task.entity.TaskDueEntity;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.service.TaskDueService;
import com.xplatform.base.workflow.task.service.TaskOpinionService;

/**
 * 报表demo控制器
 * 
 * @author xiehs
 * 
 */
@Controller
@RequestMapping("/definitionController")
public class DefinitionController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DefinitionController.class);

	@Resource
	private DefinitionService definitionService;
	@Resource
	private FlowService flowService;
	@Resource
	private NodeSetService nodeSetService;
	@Resource
	private NodeUserConditionService nodeUserConditionService;
	@Resource
	private NodeScriptService nodeScriptService;
	@Resource
	private ApproveItemService approveItemService;
	@Resource
	private NodeRuleService nodeRuleService;
	@Resource
	private TaskDueService taskDueService;
	@Resource
	private NodeUserService nodeUserService;
	@Resource
	private TaskOpinionService taskOpinionService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private OrgnaizationService orgnaizationService;
	@Resource
	private HistoryTaskInstanceService historyTaskInstanceService;
	@Resource
	private AppFormApproveUserService appFormApproveUserService;
	@Resource
	private ProcessInstHistoryService processInstHistoryService;

	private AjaxJson result = new AjaxJson();

	private String message;

	/**
	 * 流程定义管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "definition")
	public ModelAndView definition(HttpServletRequest request, Model model) {
		String isMain = request.getParameter("isMain");
		if (StringUtils.equals("0", isMain)) {
			model.addAttribute("isMain", isMain);
			DefinitionEntity definition = this.definitionService.get(request.getParameter("id"));
			model.addAttribute("code", definition.getCode());
			return new ModelAndView("workflow/definition/definitionVersionList");
		} else {
			return new ModelAndView("workflow/definition/definitionList");
		}

	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param Definition
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(DefinitionEntity definition, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DefinitionEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, definition, request.getParameterMap());
		try {
			//自定义追加查询条件
			if (!StringUtils.equals("0", definition.getIsMain())) {//非历史版本，加入类型
				cq.eq("isMain", "1");
				if (StringUtil.isNotEmpty(request.getParameter("typeId"))) {
					cq.eq("type.id", request.getParameter("typeId"));
				} else {
					cq.eq("type.id", "-1");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.definitionService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * @Decription 流程在线设计，获取分类的所有流程,外部调用子流程的时候使用
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getFlowListByTypeId")
	public ModelAndView getFlowListByTypeId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String typeId = request.getParameter("typeId");//类型id
		String word = request.getParameter("word");//版本
		List<DefinitionEntity> list = this.definitionService.queryList();
		/*if (StringUtil.isEmpty(word))
			list = this.bpmDefinitionService.getPublishedByTypeId(id);
		else {
			list = this.bpmDefinitionService.getAllPublished("%" + word + "%");
		}*/
		StringBuffer msg = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Result>");
		for (DefinitionEntity bpmDefinition : list) {
			msg.append("<item name=\"" + bpmDefinition.getName() + "\" key=\"" + bpmDefinition.getCode() + "\" type=\""
					+ bpmDefinition.getType().getId() + "\"></item>");
		}
		msg.append("</Result>");
		PrintWriter out = response.getWriter();
		out.println(msg.toString());
		return null;
	}

	/**
	 * @Decription 流程在线设计，通过标题获取code
	 * @author xiehs
	 * @createtime 2014年7月3日 下午3:05:30
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getFlowKey")
	public ModelAndView getFlowKey(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String subject = request.getParameter("subject");
		if (StringUtil.isEmpty(subject)) {
			return null;
		}
		//获取汉字的首字母
		String pingyin = PinyinUtil.getPinYinHeadChar(subject);
		String key = pingyin;
		int count = 0;
		Map<String, String> param = new HashMap<String, String>();
		param.put("newValue", key);
		//循环保证key是唯一的
		do {
			key = pingyin + (count == 0 ? "" : Integer.valueOf(count));
			count++;
			param.put("newValue", key);
		} while (!this.definitionService.isUnique(param, "code"));
		//传递到客户端
		PrintWriter out = response.getWriter();
		out.println(key);
		return null;
	}

	/**
	 * 流程定义删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param Definition
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(DefinitionEntity Definition, HttpServletRequest request) {
		message = "流程定义删除成功";
		String isOnlyVersion = request.getParameter("isOnlyVersion");
		boolean onlyVersion = "true".equals(isOnlyVersion) ? true : false;
		try {
			definitionService.delete(Definition.getId(), onlyVersion);
		} catch (Exception e) {
			message = "流程定义删除失败";
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 批量删除流程定义
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:27:15
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request) {
		String isOnlyVersion = request.getParameter("isOnlyVersion");
		boolean onlyVersion = "true".equals(isOnlyVersion) ? true : false;
		String ids = request.getParameter("ids");
		if (StringUtil.isNotEmpty(ids)) {
			try {
				definitionService.batchDelete(ids, onlyVersion);
				message = "删除成功";
			} catch (Exception e) {
				// TODO: handle exception
				message = "删除失败";
			}
		} else {
			message = "删除失败,没有选定流程";
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 流程激活或者冻结
	 * @author xiehs
	 * @createtime 2014年9月24日 下午12:58:16
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doActive")
	@ResponseBody
	public AjaxJson doActive(String id) {
		DefinitionEntity definition = this.definitionService.get(id);
		if (definition == null) {
			result.setMsg("操作失败");
			return result;
		}
		String active = definition.getStatus();
		try {
			if (StringUtil.isNotEmpty(active) && active.equals("Y")) {
				definition.setStatus("N");
				this.flowService.stopProcessDefinition(definition.getActId());
				message = "流程冻结成功";
			} else if (StringUtil.isNotEmpty(active) && active.equals("N")) {
				definition.setStatus("Y");
				this.flowService.activeProcessDefinition(definition.getActId());
				message = "流程激活成功";
			}
			this.definitionService.update(definition);
		} catch (BusinessException e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 新增或修改进入流程设计器
	 * 
	 * @return
	 */
	@RequestMapping(params = "online")
	public ModelAndView online(HttpServletRequest request) {
		//String sb="<folder id='0' label='全部'><folder id='10000000080354' label='谢海水'><folder id='10000000090009' label='slkjf'></folder></folder><folder id='10000000060001' label='测试分类'></folder></folder>";
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		UserEntity userInfo = client.getUser();
		String sb = definitionService.getTypeListByKey("workflow", userInfo.getId());
		request.setAttribute("xmlRecord", sb);
		request.setAttribute("uId", userInfo.getId());

		String defId = request.getParameter("id");
		request.setAttribute("defId", defId);

		return new ModelAndView("workflow/definition/onlineDesigner");
	}

	@RequestMapping(params = "flexGet")
	public void flexGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String defId = request.getParameter("defId");
		DefinitionEntity bpmDefinition = this.definitionService.get(defId);
		if (bpmDefinition == null) {
			return;
		}
		StringBuffer msg = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Result>");
		msg.append("<defId>" + bpmDefinition.getId() + "</defId>");
		msg.append("<defXml>" + bpmDefinition.getDefXml() + "</defXml>");
		if (bpmDefinition.getType() != null) {
			TypeEntity proType = bpmDefinition.getType();
			msg.append("<typeName>" + proType.getName() + "</typeName>");
			msg.append("<typeId>" + proType.getId() + "</typeId>");
		}
		msg.append("<subject>" + bpmDefinition.getName() + "</subject>");
		msg.append("<defKey>" + bpmDefinition.getCode() + "</defKey>");
		msg.append("<descp>" + bpmDefinition.getDescription() + "</descp>");
		msg.append("<versionNo>" + bpmDefinition.getVersion() + "</versionNo>");
		msg.append("</Result>");

		PrintWriter out = response.getWriter();
		out.println(msg.toString());
	}

	@RequestMapping(params = "saveDef")
	public ModelAndView saveDef(HttpServletRequest request, HttpServletResponse response, DefinitionEntity definition) {
		String defKey = request.getParameter("bpmDefinition.defKey").replaceAll("\r|\n", "");
		String defName = request.getParameter("bpmDefinition.subject");
		String defXml = request.getParameter("bpmDefinition.defXml");
		String id = request.getParameter("bpmDefinition.defId");

		TypeEntity type = new TypeEntity();
		type.setId(request.getParameter("bpmDefinition.typeId"));
		if (StringUtil.isNotEmpty(id)) {
			definition = this.definitionService.get(id);
		}
		definition.setId(id);
		definition.setCode(defKey);
		definition.setName(defName);
		definition.setDescription(request.getParameter("bpmDefinition.descp"));
		definition.setDefXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + defXml);
		definition.setTaskNameRule("{流程标题:title}-{发起人:startUser}-{发起时间:startTime}");
		definition.setType(type);
		//判断是不是可发布,一边这个方法里面是在修改流程版本的时候为可发布，不修改版本只是修改流程节点的时候isDeploy为false
		Boolean isDeploy = Boolean.valueOf(Boolean.parseBoolean(request.getParameter("deploy")));
		try {
			//转换成为activiti能识别的xml
			String actFlowDefXml = BpmUtil.transform(defKey, defName, defXml);
			this.definitionService.saveOrUpdate(definition, isDeploy.booleanValue(), actFlowDefXml);
			response.getWriter().print("success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ResultMessage resultMessage = new ResultMessage(0, "流程定义保存或发布失败:\r\n");
			try {
				response.getWriter().print(resultMessage);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发布流程
	 * @author xiehs
	 * @createtime 2014年7月4日 上午10:08:47
	 * @Decription
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "published")
	public void published(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
		DefinitionEntity definition = this.definitionService.get(id);
		String actDefXml = BpmUtil.transform(definition.getCode(), definition.getName(), definition.getDefXml());//将自己定义的xml转换成activiti的xml
		this.definitionService.deploy(definition, actDefXml);

	}
	
	
	
	@RequestMapping(params = "saveAutoDef")
	@ResponseBody
	public AjaxJson saveAutoDef(HttpServletRequest request, HttpServletResponse response, DefinitionEntity definition) {
		String defName = request.getParameter("bpmDefinition.subject");
		String id = request.getParameter("bpmDefinition.defId");
		Map<String,Object> dep=this.definitionService.getDeployXml(request.getParameter("formId"),null);
		String defXml= (String)dep.get("defXml");
		List<AppFormApproveUser> userList=(List<AppFormApproveUser>)dep.get("userList");
		TypeEntity type = new TypeEntity();
		type.setId(ConfigConst.WorkflowDefaultTypeId);
		if (StringUtil.isNotEmpty(id)) {
			definition = this.definitionService.get(id);
		}
		//将中文转换成拼音code，但是得保证唯一
		String pingyin = PinyinUtil.getPinYinHeadChar(defName);
		String defKey = pingyin;
		int count = 0;
		Map<String, String> param = new HashMap<String, String>();
		param.put("newValue", defKey);
		do {
			defKey = pingyin + (count == 0 ? "" : Integer.valueOf(count));
			count++;
			param.put("newValue", defKey);
		} while (!this.definitionService.isUnique(param, "code"));
		//设置参数
		definition.setId(id);
		definition.setCode(defKey);
		definition.setName(defName);
		definition.setDescription(request.getParameter("bpmDefinition.descp"));
		definition.setDefXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + defXml);
		definition.setTaskNameRule("{流程标题:title}-{发起人:startUser}-{发起时间:startTime}");
		definition.setType(type);
		definition.setInformStart("innerMessage");
		definition.setInformType("innerMessage");
		//判断是不是可发布,一边这个方法里面是在修改流程版本的时候为可发布，不修改版本只是修改流程节点的时候isDeploy为false
		Boolean isDeploy = Boolean.valueOf(true);
		try {
			String actFlowDefXml = BpmUtil.transform(defKey, defName, defXml);
			//第一步：保存流程定义
			String pk=this.definitionService.saveOrUpdate(definition, isDeploy.booleanValue(), actFlowDefXml);//如果是新增，那么会返回主键
			//第二步：加入节点审批人
			
			List<NodeSetEntity> nodeList=this.nodeSetService.getTaskNodeList(pk);
			for(NodeSetEntity nodeSet:nodeList){//设置审批人
				NodeUserEntity nodeUser=new NodeUserEntity();
				nodeUser.setNodeId(nodeSet.getNodeId());
				nodeUser.setDefId(pk);
				for(AppFormApproveUser user:userList){
					if(StringUtil.equals(nodeSet.getNodeId(), user.getTaskId())){
						nodeUser.setAssignType(user.getType());
						nodeUser.setFuncType("nodeUser");
						nodeUser.setAssignIds(user.getUserId());
						nodeUser.setAssignNames(user.getUserName());
						nodeUser.setAssignTypeName("用户");
						this.nodeUserService.save(nodeUser);
						break;
					}
				}
			}
			//第三步：加入流程审批全局表单
			NodeSetEntity nodeSet = new NodeSetEntity();
			nodeSet.setDefId(pk);
			nodeSet.setActDefId(definition.getActId());
			nodeSet.setFormKey(defKey);
			nodeSet.setFormName(defName);
			nodeSet.setFormId(request.getParameter("formId"));
			nodeSet.setFormType("0");
			nodeSet.setSetType(NodeSetEntity.SetType_GloabalForm);
			this.nodeSetService.save(nodeSet);
			result.setMsg("发布成功");
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("发布失败");
		}
		return result;
	}

	/**
	 * 进入流程设置页面
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "flowSetting")
	public ModelAndView flowSetting(HttpServletRequest request, DefinitionEntity definition, Model model) {
		//流程定义的详细信息
		/*if (StringUtil.isNotEmpty(definition.getId())) {
			definition = definitionService.get(definition.getId());
			model.addAttribute("definition", definition);
		}*/
		//类型id
		//model.addAttribute("typeId", request.getParameter("typeId"));
		model.addAttribute("defId", definition.getId());
		return new ModelAndView("workflow/definition/flowSetting");
	}

	/**
	 * 进入流程设置页面
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "detail")
	public ModelAndView detail(DefinitionEntity definition, Model model) {
		if (StringUtil.isNotEmpty(definition.getId())) {
			definition = definitionService.get(definition.getId());
			model.addAttribute("definition", definition);
		}
		return new ModelAndView("workflow/definition/detail");
	}

	/**
	 * 查看流程定义的activiti的xml
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "bpmnXml")
	public ModelAndView bpmnXml(HttpServletRequest request, Model model, HttpServletResponse response,
			DefinitionEntity definition) throws Exception {
		definition = definitionService.get(definition.getId());
		if (definition.getActDeployId() != null) {
			String bpmnXml = this.flowService.getDefXmlByDeployId(definition.getActDeployId().toString());
			if (bpmnXml.startsWith("<?xml version=\"1.0\" encoding=\"utf-8\"?>")) {
				bpmnXml = bpmnXml.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
			}
			if (bpmnXml.startsWith("<?xml version=\"1.0\" encoding=\"GBK\"?>")) {
				bpmnXml = bpmnXml.replace("<?xml version=\"1.0\" encoding=\"GBK\"?>", "");
			}
			bpmnXml = bpmnXml.trim();
			model.addAttribute("activitiXml", bpmnXml);
		}
		return new ModelAndView("workflow/definition/bpmnXml");
	}

	/**
	 * 进入流程节点设置
	 * @author xiehs
	 * @createtime 2014年7月9日 下午3:13:57
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "nodeSet")
	public ModelAndView nodeSet(HttpServletRequest request, Model model, HttpServletResponse response,
			DefinitionEntity definition) throws Exception {
		definition = definitionService.get(definition.getId());
		if (definition.getActDeployId() != null) {
			String defXml = this.flowService.getDefXmlByDeployId(definition.getActDeployId().toString());
			model.addAttribute("defXml", defXml);
			ShapeMeta shapeMeta = BpmUtil.transGraph(defXml);
			model.addAttribute("shapeMeta", shapeMeta);
			model.addAttribute("definition", definition);
		}
		return new ModelAndView("workflow/node/nodeSet");
	}

	/**
	 * 节点概要
	 * @author xiehs
	 * @createtime 2014年7月10日 下午3:54:04
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "nodeSummary")
	public ModelAndView nodeSummary(HttpServletRequest request, HttpServletResponse response,
			DefinitionEntity definition) throws Exception {
		definition = definitionService.get(definition.getId());
		List<NodeSetEntity> nodeSetList = this.nodeSetService.queryByPorperty("defId", definition.getId());
		NodeSetEntity globalNodeSet = this.nodeSetService.getBySetType(definition.getId(),
				NodeSetEntity.SetType_GloabalForm);
		for (NodeSetEntity nodeSet : nodeSetList) {
			if (nodeSet.getNodeId() == null) {
				nodeSetList.remove(nodeSet);
				break;
			}
		}

		String actDefId = definition.getActId();
		FlowNode startFlowNode = NodeCache.getStartNode(actDefId);
		List endFlowNodeList = NodeCache.getEndNode(actDefId);

		Map startScriptMap = new HashMap();
		Map endScriptMap = new HashMap();
		Map preScriptMap = new HashMap();
		Map afterScriptMap = new HashMap();

		Map assignScriptMap = new HashMap();
		Map nodeRulesMap = new HashMap();

		Map bpmFormMap = new HashMap();
		Map buttonMap = new HashMap();
		Map nodeButtonMap = new HashMap();
		Map taskReminderMap = new HashMap();
		Map mobileSetMap = new HashMap();
		Map nodeUserMap = new HashMap();
		Map formMap = new HashMap();
		Map taskApprovalItemsMap = new HashMap();
		Map globalApprovalMap = new HashMap();

		getNodeScriptMap(nodeSetList, actDefId, startScriptMap, endScriptMap, preScriptMap, afterScriptMap,
				assignScriptMap);

		geTaskApprovalItemsMap(nodeSetList, actDefId, taskApprovalItemsMap, globalApprovalMap);
		getNodeRulesMap(nodeSetList, actDefId, nodeRulesMap);

		getNodeSetMap(nodeSetList, bpmFormMap, mobileSetMap);

		getTaskReminderMap(nodeSetList, actDefId, taskReminderMap);

		if (checkForm(globalNodeSet).booleanValue()) {
			formMap.put("global", Boolean.valueOf(true));
		}
		/*getNodeUserMap(nodeSetList, actDefId, nodeUserMap);


		getNodeScriptMap(nodeSetList, actDefId, startScriptMap, endScriptMap,
				preScriptMap, afterScriptMap, assignScriptMap);

		getNodeRulesMap(nodeSetList, actDefId, nodeRulesMap);

		getNodeButtonMap(nodeSetList, defId, buttonMap, nodeButtonMap);

		getTaskReminderMap(nodeSetList, actDefId, taskReminderMap);

		getNodeSetMap(nodeSetList, bpmFormMap, mobileSetMap);

		if (checkForm(globalNodeSet).booleanValue()){
			formMap.put("global", Boolean.valueOf(true));
		}*/
		return new ModelAndView("workflow/node/nodeSummary").addObject("bpmDefinition", definition)
				.addObject("deployId", definition.getActDeployId()).addObject("defId", definition.getId())
				.addObject("actDefId", actDefId).addObject("nodeSetList", nodeSetList)
				.addObject("startScriptMap", startScriptMap).addObject("endScriptMap", endScriptMap)
				.addObject("preScriptMap", preScriptMap).addObject("afterScriptMap", afterScriptMap)
				.addObject("assignScriptMap", assignScriptMap).addObject("nodeRulesMap", nodeRulesMap)
				.addObject("nodeUserMap", nodeUserMap).addObject("bpmFormMap", bpmFormMap)
				.addObject("formMap", formMap).addObject("buttonMap", buttonMap)
				.addObject("nodeButtonMap", nodeButtonMap).addObject("taskReminderMap", taskReminderMap)
				.addObject("taskApprovalItemsMap", taskApprovalItemsMap)
				.addObject("globalApprovalMap", globalApprovalMap).addObject("mobileSetMap", mobileSetMap)
				.addObject("startFlowNode", startFlowNode).addObject("endFlowNodeList", endFlowNodeList);
	}

	private void getTaskReminderMap(List<NodeSetEntity> nodeSetList, String actDefId,
			Map<String, Boolean> taskReminderMap) {
		List<TaskDueEntity> taskReminderList = this.taskDueService.getByActDefId(actDefId);
		for (NodeSetEntity nodeSet : nodeSetList)
			for (TaskDueEntity taskReminder : taskReminderList)
				if (nodeSet.getNodeId().equals(taskReminder.getNodeId()))
					taskReminderMap.put(nodeSet.getId(), Boolean.valueOf(true));
	}

	private void getNodeSetMap(List<NodeSetEntity> nodeSetList, Map<String, Boolean> bpmFormMap,
			Map<String, Boolean> mobileSetMap) {
		for (NodeSetEntity nodeSet : nodeSetList) {
			if ("1".equals(nodeSet.getIsAllowMobile()))
				mobileSetMap.put(nodeSet.getId(), Boolean.valueOf(true));
			if (checkForm(nodeSet).booleanValue())
				bpmFormMap.put(nodeSet.getId(), Boolean.valueOf(true));
		}
	}

	private Boolean checkForm(NodeSetEntity bpmNodeSet) {
		if (bpmNodeSet == null || StringUtils.isEmpty(bpmNodeSet.getId()))
			return Boolean.valueOf(false);
		if ("0".equals(bpmNodeSet.getFormType())) {
			if (StringUtils.isNotEmpty(bpmNodeSet.getFormKey()))
				return Boolean.valueOf(true);
		} else if ("1".equals(bpmNodeSet.getFormType())) {
			if (StringUtil.isNotEmpty(bpmNodeSet.getFormUrl()))
				return Boolean.valueOf(true);
		}
		return Boolean.valueOf(false);
	}

	private void getNodeRulesMap(List<NodeSetEntity> nodeSetList, String actDefId, Map<String, Boolean> nodeRulesMap) {
		List<NodeRuleEntity> nodeRuleList = this.nodeRuleService.getByActDefId(actDefId);
		for (NodeSetEntity nodeSet : nodeSetList)
			for (NodeRuleEntity bpmNodeRule : nodeRuleList)
				if (nodeSet.getNodeId().equals(bpmNodeRule.getNodeId()))
					nodeRulesMap.put(nodeSet.getId(), Boolean.valueOf(true));
	}

	private void geTaskApprovalItemsMap(List<NodeSetEntity> nodeSetList, String actDefId,
			Map<String, Boolean> taskApprovalItemsMap, Map<String, Boolean> globalApprovalMap) {
		List<ApproveItemEntity> taskApprovalItemsList = this.approveItemService.getByActDefId(actDefId);
		for (ApproveItemEntity taskApprovalItems : taskApprovalItemsList) {
			if ("Y".equals(taskApprovalItems.getIsGlobal())) {
				globalApprovalMap.put("global", Boolean.valueOf(true));
			}
		}
		for (NodeSetEntity nodeSet : nodeSetList)
			for (ApproveItemEntity taskApprovalItems : taskApprovalItemsList) {
				if ((!StringUtils.isNotEmpty(taskApprovalItems.getNodeId()))
						|| (!nodeSet.getNodeId().equals(taskApprovalItems.getNodeId())))
					continue;
				taskApprovalItemsMap.put(nodeSet.getId(), Boolean.valueOf(true));
			}
	}

	private void getNodeScriptMap(List<NodeSetEntity> nodeSetList, String actDefId,
			Map<String, Boolean> startScriptMap, Map<String, Boolean> endScriptMap, Map<String, Boolean> preScriptMap,
			Map<String, Boolean> afterScriptMap, Map<String, Boolean> assignScriptMap) {
		List<NodeScriptEntity> NodeScriptList = this.nodeScriptService.getByNodeScriptId(actDefId);

		for (NodeScriptEntity NodeScript : NodeScriptList) {
			if (StringUtil.isNotEmpty(NodeScript.getNodeId()) && StringUtil.isNotEmpty(NodeScript.getScript())) {
				if ("start".equals(NodeScript.getType())) {
					startScriptMap.put(NodeScript.getNodeId(), Boolean.valueOf(true));
				} else if ("end".equals(NodeScript.getType())) {
					endScriptMap.put(NodeScript.getNodeId(), Boolean.valueOf(true));
				}
			}
		}
		for (NodeSetEntity nodeSet : nodeSetList)
			for (NodeScriptEntity NodeScript : NodeScriptList) {
				if (StringUtils.isEmpty(NodeScript.getNodeId()) || StringUtils.isEmpty(NodeScript.getScript()))
					continue;
				if (nodeSet.getNodeId().equals(NodeScript.getNodeId()))
					if ("pre".equals(NodeScript.getType())) {
						preScriptMap.put(nodeSet.getId(), Boolean.valueOf(true));
					} else if ("rear".equals(NodeScript.getType())) {
						afterScriptMap.put(nodeSet.getId(), Boolean.valueOf(true));
					} else if ("allot".equals(NodeScript.getType())) {
						assignScriptMap.put(nodeSet.getId(), Boolean.valueOf(true));
					}
			}
	}

	/*private void getNodeUserMap(List<NodeSetEntity> nodeSetList, String actDefId,
			Map<Long, Boolean> nodeUserMap) {
		List<NodeUserConditionEntity> bpmUserConditionList = this.bpmUserConditionService
				.getByActDefId(actDefId);
		for (BpmNodeSet nodeSet : nodeSetList) {
			for (BpmUserCondition bpmUserCondition : bpmUserConditionList)
				if (nodeSet.getNodeId().equals(bpmUserCondition.getNodeid()))
					nodeUserMap.put(nodeSet.getSetId(), Boolean.valueOf(true));
		}
	}*/

	/**
	 * 流程节点用户设置
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "nodeUser")
	public ModelAndView nodeUser(HttpServletRequest request, Model model, HttpServletResponse response,
			DefinitionEntity definition) throws Exception {
		definition = definitionService.get(definition.getId());
		List<NodeSetEntity> nodeSetList = this.nodeSetService.queryByPorperty("defId", definition.getId());
		String nodeId = request.getParameter("nodeId");
		List nodeUserMapList = new ArrayList();
		List bpmUserConditionList;
		if (StringUtil.isNotEmpty(nodeId)) {
			/*BpmNodeSet nodeSet = this.bpmNodeSetService.getByDefIdNodeId(defId,nodeId);
			bpmUserConditionList = this.bpmUserConditionService
					.getBySetId(nodeSet.getSetId());
			NodeUserMap nodeUserMap = new NodeUserMap();
			nodeUserMap.setSetId(nodeSet.getSetId());
			nodeUserMap.setNodeId(nodeSet.getNodeId());
			nodeUserMap.setNodeName(nodeSet.getNodeName());
			nodeUserMap.setBpmUserConditionList(bpmUserConditionList);
			nodeUserMapList.add(nodeUserMap);
			modelView.addObject("nodeId", nodeId);*/
		} else {
			for (NodeSetEntity nodeSet : nodeSetList) {
				/*bpmUserConditionList = this.bpmUserConditionService.getBySetId(nodeSet.getSetId());
				NodeUserMap nodeUserMap = new NodeUserMap();
				nodeUserMap.setSetId(nodeSet.getSetId());
				nodeUserMap.setNodeId(nodeSet.getNodeId());
				nodeUserMap.setNodeName(nodeSet.getNodeName());
				nodeUserMap.setBpmUserConditionList(bpmUserConditionList);
				nodeUserMapList.add(nodeUserMap);*/
			}
		}
		model.addAttribute("nodeSetList", nodeSetList);
		model.addAttribute("bpmDefinition", definition);
		model.addAttribute("nodeUserMapList", nodeUserMapList);
		model.addAttribute("defId", definition.getId());
		return new ModelAndView("workflow/node/nodeUser");
	}

	@RequestMapping(params = "setCondition")
	public ModelAndView setCondition(HttpServletRequest request, HttpServletResponse response, Model model)
			throws Exception {
		String deployId = request.getParameter("deployId");
		String nodeId = request.getParameter("nodeId");
		String defId = request.getParameter("defId");
		//获取activiti流程定义
		ProcessDefinitionEntity proDefEntity = this.flowService.getProcessDefinitionByDeployId(deployId);
		//获取当前节点
		ActivityImpl curActivity = proDefEntity.findActivity(nodeId);
		//节点类型
		String curNodeType = (String) curActivity.getProperty("type");
		//是否网关节点
		Boolean ifInclusiveGateway = Boolean.valueOf(curNodeType.equals("inclusiveGateway"));

		if (ifInclusiveGateway.booleanValue()) {//包容 分支
			String curNodeName = curActivity.getId();
			DefinitionEntity bpmDefinition = this.definitionService.get(defId);
			Object val = bpmDefinition.getCanChoicePathNodeMap().get(curNodeName);
			if (val != null) {
				model.addAttribute("canChoicePathGateway", curNodeName).addAttribute("selectCanChoicePathNodeId", val);
			}
		}

		List<BpmNode> incomeNodes = new ArrayList<BpmNode>();
		List<BpmNode> outcomeNodes = new ArrayList<BpmNode>();

		List<PvmTransition> inTrans = curActivity.getIncomingTransitions();
		String nodeName;
		for (PvmTransition tran : inTrans) {
			ActivityImpl act = (ActivityImpl) tran.getSource();
			String id = act.getId();
			nodeName = (String) act.getProperty("name");
			String nodeType = (String) act.getProperty("type");
			Boolean isMultiple = Boolean.valueOf(act.getProperty("multiInstance") != null);

			incomeNodes.add(new BpmNode(id, nodeName, nodeType, isMultiple));
		}

		String xml = this.flowService.getDefXmlByDeployId(deployId);
		Map conditionMap = BpmUtil.getDecisionConditions(xml, nodeId);

		List<PvmTransition> outTrans = curActivity.getOutgoingTransitions();
		for (PvmTransition tran : outTrans) {
			ActivityImpl act = (ActivityImpl) tran.getDestination();
			String id = act.getId();
			nodeName = (String) act.getProperty("name");
			String nodeType = (String) act.getProperty("type");
			Boolean isMultiple = Boolean.valueOf(act.getProperty("multiInstance") != null);

			BpmNode bpmNode = new BpmNode(id, nodeName, nodeType, isMultiple);
			String condition = (String) conditionMap.get(id);
			if (condition != null) {
				bpmNode.setCondition(condition);
			}
			outcomeNodes.add(bpmNode);
		}

		return new ModelAndView("workflow/node/nodeCondition").addObject("defId", defId)
				.addObject("ifInclusiveGateway", ifInclusiveGateway).addObject("nodeId", nodeId)
				.addObject("deployId", deployId).addObject("incomeNodes", incomeNodes)
				.addObject("outcomeNodes", outcomeNodes);
	}

	@RequestMapping(params = "saveCondition")
	@ResponseBody
	public AjaxJson saveCondition(HttpServletRequest request, HttpServletResponse response) throws IOException {
		message = "保存成功";
		try {
			String nodeId = request.getParameter("nodeId");
			String defId = request.getParameter("defId");
			String tasks = request.getParameter("tasks");
			String conditions = request.getParameter("conditions");
			String[] aryTasks = tasks.split("#split#");
			String canChoicePathNodeId = request.getParameter("canChoicePathNodeId");
			conditions = " " + conditions + " ";
			String[] aryCondition = conditions.split("#split#");
			Map<String, String> map = new HashMap<String, String>();

			for (int i = 0; i < aryTasks.length; i++) {
				String condition = aryCondition[i].trim();
				map.put(aryTasks[i], condition);
			}
			this.flowService.saveCondition(defId, nodeId, map, canChoicePathNodeId);
		} catch (Exception e) {
			// TODO: handle exception
			message = "保存条件失败";
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 流程节点用户设置
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "nodeUserSet")
	public ModelAndView nodeUserSet(HttpServletRequest request, HttpServletResponse response, Model model)
			throws Exception {
		String defId = request.getParameter("defId");
		String nodeId = request.getParameter("nodeId");
		DefinitionEntity bpmDefinition = this.definitionService.get(defId);
		List<NodeSetEntity> setList = this.nodeSetService.queryByPorperty("defId", defId);
		List<NodeUserMap> nodeUserMapList = new ArrayList<NodeUserMap>();
		List<NodeUserConditionEntity> bpmUserConditionList;
		if (StringUtil.isNotEmpty(nodeId)) {
			NodeSetEntity nodeSet = this.nodeSetService.getNodeSetByDefIdNodeId(defId, nodeId);
			bpmUserConditionList = this.nodeUserConditionService.getBySetId(nodeSet.getId());
			NodeUserMap nodeUserMap = new NodeUserMap();
			nodeUserMap.setSetId(nodeSet.getId());
			nodeUserMap.setNodeId(nodeSet.getNodeId());
			nodeUserMap.setNodeName(nodeSet.getNodeName());
			nodeUserMap.setNodeUserConditionList(bpmUserConditionList);
			nodeUserMapList.add(nodeUserMap);
			model.addAttribute("nodeId", nodeId);
		} else {
			for (NodeSetEntity nodeSet : setList) {
				bpmUserConditionList = this.nodeUserConditionService.getBySetId(nodeSet.getId());
				NodeUserMap nodeUserMap = new NodeUserMap();
				nodeUserMap.setSetId(nodeSet.getId());
				nodeUserMap.setNodeId(nodeSet.getNodeId());
				nodeUserMap.setNodeName(nodeSet.getNodeName());
				nodeUserMap.setNodeUserConditionList(bpmUserConditionList);
				nodeUserMapList.add(nodeUserMap);
			}
		}
		return new ModelAndView("workflow/node/nodeUserSet").addObject("nodeSetList", setList)
				.addObject("bpmDefinition", bpmDefinition).addObject("nodeUserMapList", nodeUserMapList)
				.addObject("defId", defId);
	}

	@RequestMapping(params = "otherParam")
	public ModelAndView otherParam(HttpServletRequest request, Model model) {
		DefinitionEntity definition = this.definitionService.get(request.getParameter("id"));
		model.addAttribute("definition", definition);
		model.addAttribute("defId", request.getParameter("id"));
		return new ModelAndView("workflow/definition/otherParam");
	}

	@RequestMapping(params = "saveOtherParam")
	@ResponseBody
	public AjaxJson saveOtherParam(HttpServletRequest request, DefinitionEntity definition) throws IOException {
		message = "保存成功";
		try {
			this.definitionService.update(definition);
			;
		} catch (Exception e) {
			// TODO: handle exception
			message = "保存失败";
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月6日 下午7:16:45
	 * @Decription 审批页测试方法
	 *
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(params = "taskApprove")
	public ModelAndView taskApprove(HttpServletRequest request, Model model) {
		model.addAttribute("aaa", "sssss");
		return new ModelAndView("workflow/task/taskApprove");
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月6日 下午7:16:53
	 * @Decription 进入加签弹出页
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "showAddSign")
	public ModelAndView showAddSignWindow(HttpServletRequest request) {
		request.setAttribute("taskId", request.getParameter("taskId"));
		return new ModelAndView("workflow/task/showAddSign");
	}

	@RequestMapping(params = "showTaskOpinions")
	public ModelAndView showTaskOpinions(HttpServletRequest request) {
		
		String actInstanceId = request.getParameter("actInstId");
		String taskId = request.getParameter("taskId");
		String businessKey = request.getParameter("businessKey");
		if(StringUtil.isNotEmpty(taskId)){
			ProcessTaskHistory taskHistory = this.historyTaskInstanceService.get(taskId);
			actInstanceId = taskHistory.getProcessInstanceId();
		}else if(StringUtil.isNotEmpty(businessKey)){
			ProcessInstHistoryEntity taskHistory = this.processInstHistoryService.getByBusinessKey(businessKey);
			if(taskHistory==null)return null;
			actInstanceId = taskHistory.getActInstId();
		}
		List<TaskOpinionEntity> taskOpinionList = taskOpinionService.getByActInstId(actInstanceId);
		//为意见表查询实时的用户头像,填充到临时字段
		for (TaskOpinionEntity entity : taskOpinionList) {
			String userId = entity.getExeUserId();
			UserEntity emp = sysUserService.getUserById(userId);
			if(emp!=null){
				entity.setPortrait80(emp.getPortrait());
			}
		}
		/*Collections.sort(taskOpinionList, new Comparator<TaskOpinionEntity>() {
			public int compare(TaskOpinionEntity arg0, TaskOpinionEntity arg1) {
				return arg0.getUpdateTime().getTime() > arg1.getUpdateTime().getTime() ? 1 : -1;
			}
		});*/
		Map<String, List<TaskOpinionEntity>> map = new HashMap<String, List<TaskOpinionEntity>>();
		//将意见按中文日期每日分组
		for (TaskOpinionEntity entity : taskOpinionList) {
			String dateStr = DateUtils.formatDateWZTime(entity.getCreateTime());
			if (map.containsKey(dateStr)) {
				List<TaskOpinionEntity> list = map.get(dateStr);
				list.add(entity);
				map.put(dateStr, list);
			} else {
				List<TaskOpinionEntity> list = new ArrayList<TaskOpinionEntity>();
				list.add(entity);
				map.put(dateStr, list);
			}
		}
		Map.Entry[] entrys = definitionService.getTimeSortedHashtableByKey(map);
		JSONObject data = definitionService.getTimeLineJSONData(entrys);
		request.setAttribute("data", data);
		return new ModelAndView("workflow/task/showTaskOpinions");
	}
}
