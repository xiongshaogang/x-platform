package com.xplatform.base.workflow.node.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.common.model.json.TreeNode;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.TreeMapper;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.workflow.core.bpm.model.FlowNode;
import com.xplatform.base.workflow.core.bpm.model.NodeCache;
import com.xplatform.base.workflow.core.bpm.util.BpmUtil;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;
import com.xplatform.base.workflow.node.entity.NodeRuleEntity;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.service.NodeRuleService;
import com.xplatform.base.workflow.node.service.NodeSetService;

/**
 * 
 * description : 节点跳转规则管理controller
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:32:17
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 下午12:32:17
 *
 */
@Controller
@RequestMapping("/nodeRuleController")
public class NodeRuleController extends BaseController {
	
	@Resource
	private NodeRuleService nodeRuleService;
	
	@Resource
	private FlowService flowService;
	
	@Resource
	private DefinitionService definitionService;
	
	public void setDefinitionService(DefinitionService definitionService) {
		this.definitionService = definitionService;
	}

	@Resource
	private NodeSetService nodeSetService;
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 节点跳转规则管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "NodeRule")
	public ModelAndView NodeRule(HttpServletRequest request) {
		String actDefId = request.getParameter("actDefId");
		String defId = request.getParameter("defId");
		String nodeId = request.getParameter("nodeId");
		
		String deployId = request.getParameter("deployId");
		
		String nodeName = request.getParameter("nodeName");

	    FlowNode flowNode = (FlowNode)NodeCache.getByActDefId(actDefId).get(nodeId);

	    Map<String,Object> param = new HashMap<String,Object>();
	    param.put("nodeId", nodeId);
	    param.put("actDefId", actDefId);
	    List<NodeRuleEntity> list = this.nodeRuleService.queryListByNodeIdAndActDefId(param);
	    NodeRuleEntity NodeRule = null;
	    if(list!=null && list.size()>0){
	    	 NodeRule = this.nodeRuleService.queryListByNodeIdAndActDefId(param).get(0);
	    }
	    String defXml = this.flowService.getDefXmlByDeployId(deployId);
	    DefinitionEntity bpmDefinition = this.definitionService.get(defId);

	    List nodeList = new ArrayList();

	    Map activityList = BpmUtil.getTranstoActivitys(defXml, nodeList);

	    NodeSetEntity bpmNodeSet = this.nodeSetService.getNodeSetByActDefIdNodeId(actDefId, nodeId);

	    /*bpmNodeRule.setActDefId(actDefId);
	    bpmNodeRule.setNodeId(nodeId);
*/
	    return new ModelAndView("workflow/node/nodeRule").addObject("activityList", activityList)
	      .addObject("nodeName", nodeName)
	      .addObject("NodeRule", NodeRule)
	      .addObject("NodeRuleList", list)
	      .addObject("deployId", deployId)
	      .addObject("actDefId", actDefId)
	      .addObject("nodeId", nodeId)
	      .addObject("bpmNodeSet", bpmNodeSet)
	      .addObject("defId", defId)
	      .addObject("nextNodes", flowNode.getNextFlowNodes());
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param NodeRule
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(NodeRuleEntity NodeRule,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(NodeRuleEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, NodeRule, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.nodeRuleService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}
	
	
	/**
	 * 规则列表
	 * @author binyong
	 * @createtime 2014年7月31日 下午2:35:48
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "list")
	public void list(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("nodeId", request.getParameter("nodeId"));
		param.put("actDefId", request.getParameter("actDefId"));
		List<NodeRuleEntity> ruleList = this.nodeRuleService.queryListByNodeIdAndActDefId(param);
		//树的转换
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		List<TreeNode> treeList = TreeMapper.buildJsonTree(ruleList,
				propertyMapping);
		TagUtil.tree(response, treeList);
	}
	
	/**
	 * 根据id取得规则数据
	 */
	@RequestMapping(params = "getNodeRule")
	@ResponseBody
	public AjaxJson getNodeRule(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		NodeRuleEntity nodeRule = this.nodeRuleService.get(id);
		Map<String, Object> attributes = new HashMap<String,Object>();
		attributes.put("NodeRule", nodeRule);
		result.setObj(attributes);
		return result;
	}
	

	/**
	 * 节点跳转规则删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param NodeRule
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(NodeRuleEntity NodeRule, HttpServletRequest request) {
		message = "节点跳转规则删除成功";
		try{
			nodeRuleService.delete(NodeRule.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除节点跳转规则
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:27:15
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request) {
		String ids=request.getParameter("ids");
		try {
			nodeRuleService.batchDelete(ids);
			message="删除成功";
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 进入新增或者修改查看页面
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "nodeRuleEdit")
	public ModelAndView NodeRuleEdit(NodeRuleEntity NodeRule,Model model) {
		if (StringUtil.isNotEmpty(NodeRule.getId())) {
			NodeRule = nodeRuleService.get(NodeRule.getId());
			model.addAttribute("NodeRule", NodeRule);
		}
		return new ModelAndView("workflow/organization/NodeRule/NodeRuleEdit");
	}
	
	/**
	 * 新增或修改节点跳转规则
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param NodeRule
	 * @return
	 */
	@RequestMapping(params = "saveNodeRule")
	@ResponseBody
	public AjaxJson saveNodeRule(NodeRuleEntity NodeRule,HttpServletRequest request) {
		String targetNodeName = request.getParameter("targetNodeId").split(",")[1];
		NodeRule.setTargetNodeName(targetNodeName);
		try {
			if (StringUtil.isNotEmpty(NodeRule.getId())) {
				message = "节点跳转规则更新成功";
				nodeRuleService.update(NodeRule);
			} else {
				message = "节点跳转规则新增成功";
				nodeRuleService.save(NodeRule);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		Map<String, Object> attributes = new HashMap<String,Object>();
		attributes.put("NodeRule", NodeRule);
		result.setObj(attributes);
		return result;
	}
	
	public void setNodeRuleService(NodeRuleService nodeRuleService) {
		this.nodeRuleService = nodeRuleService;
	}

	public void setNodeSetService(NodeSetService nodeSetService) {
		this.nodeSetService = nodeSetService;
	}

	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}
	
	
}
