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
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.ComboBox;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.orgnaization.service.OrgnaizationService;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.definition.service.DefinitionService;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;
import com.xplatform.base.workflow.node.mybatis.vo.NodeUserVo;
import com.xplatform.base.workflow.node.service.NodeSetService;
import com.xplatform.base.workflow.node.service.NodeUserService;

/**
 * 
 * description : 节点用户管理controller
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
@RequestMapping("/nodeUserController")
public class NodeUserController extends BaseController {

	@Resource
	private NodeUserService nodeUserService;

	@Resource
	private DefinitionService definitionService;

	@Resource
	private FlowService flowService;

	@Resource
	private NodeSetService nodeSetService;

	@Resource
	private OrgnaizationService orgnaizationService;

	private AjaxJson result = new AjaxJson();

	private String message;

	/**
	 * 节点用户管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "nodeUser")
	public ModelAndView nodeUser(HttpServletRequest request, Model model) {
		String defId = request.getParameter("defId");
		model.addAttribute("defId", defId);
		return new ModelAndView("workflow/node/user/nodeUserList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param job
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(NodeUserEntity nodeUser, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		String defId = request.getParameter("defId");
		String funcType = request.getParameter("funcType");
		String nodeId = request.getParameter("nodeId");
		List<NodeUserVo> nodeUserList = new ArrayList<NodeUserVo>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("defId", defId);
		map.put("funcType", funcType);
		map.put("nodeId", nodeId);
		if ("nodeUser".equals(funcType)) {
			nodeUserList = this.nodeUserService.queryNodeUserListByDefIdAndType(map);
			for (NodeUserVo user : nodeUserList) {
				if (!StringUtil.isNotEmpty(user.getId())) {
					user.setId(UUIDGenerator.generate());
					user.setAssignTypeName("请添加人员信息!!!");
					user.setFlag("false");
				} else {
					user.setFlag("true");
				}
			}
		} else {
			nodeUserList = this.nodeUserService.queryNodeUserListByCondition(map);
		}

		dataGrid.setTotal(nodeUserList.size());
		dataGrid.setResults(nodeUserList);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 节点用户删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param NodeUser
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(NodeUserEntity NodeUser, HttpServletRequest request) {
		message = "节点用户删除成功";
		try {
			nodeUserService.delete(NodeUser.getId());
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 批量删除节点用户
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:27:15
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		try {
			nodeUserService.batchDelete(ids);
			message = "删除成功";
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
	@RequestMapping(params = "nodeUserEdit")
	public ModelAndView nodeUserEdit(HttpServletRequest request, NodeUserEntity nodeUser, Model model) {
		String gridId = request.getParameter("gridId");
		if (StringUtil.isNotEmpty(nodeUser.getId())) {
			nodeUser = nodeUserService.get(nodeUser.getId());
		}
		
		List<NodeSetEntity> taskList = this.nodeSetService.getTaskNodeList(nodeUser.getDefId());
		if (taskList != null && taskList.size() > 0) {
			//构造combobox数据
			List<ComboBox> nodeList = new ArrayList<ComboBox>();
			for (NodeSetEntity nodeSet : taskList) {
				ComboBox combo = new ComboBox();
				combo.setId(nodeSet.getNodeId());
				combo.setText(nodeSet.getNodeName());
				nodeList.add(combo);
			}
			String nodeData = JSONHelper.toJSONString(nodeList);
			model.addAttribute("nodeData", nodeData);
		}
		model.addAttribute("nodeUser", nodeUser);
		request.setAttribute("gridId", gridId);
		return new ModelAndView("workflow/node/user/nodeUserEdit");
	}

	/**
	 * 新增或修改工作流通用选择用户
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param NodeUser
	 * @return
	 */
	@RequestMapping(params = "saveNodeUser")
	@ResponseBody
	public AjaxJson saveNodeUser(HttpServletRequest request, NodeUserEntity nodeUser) {
		if (StringUtil.equals(nodeUser.getAssignType(), "user")) {//用户
			nodeUser.setAssignIds(request.getParameter("userIds"));
			nodeUser.setAssignNames(request.getParameter("userNames"));
			nodeUser.setAssignTypeName("用户");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "role")) {//角色
			nodeUser.setAssignIds(request.getParameter("roleUserIds"));
			nodeUser.setAssignNames(request.getParameter("roleUserNames"));
			nodeUser.setAssignTypeName("角色");
		}else if (StringUtil.equals(nodeUser.getAssignType(), "org")) {//组织机构
			String deptIds = StringUtil.asString(request.getParameterValues("orgUserIds"), ",");
			nodeUser.setAssignIds(deptIds);
			String deptNames = orgnaizationService.queryOrgNameByIds(deptIds, ",");
			nodeUser.setAssignNames(deptNames);
			nodeUser.setAssignTypeName("组织机构");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "startUser")) {//部门
			nodeUser.setAssignTypeName("流程发起人");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "startUserDirectLeader")) {//流程发起人
			nodeUser.setAssignTypeName("流程发起人直接领导");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "startUserBranchLeader")) {//流程发起人
			nodeUser.setAssignTypeName("流程发起人分管领导");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "startUserRoleSame")) {//流程发起人直接领导
			nodeUser.setAssignTypeName("流程发起人相同的角色");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "startUserJobSame")) {//流程发起人相同的角色
			nodeUser.setAssignTypeName("流程发起人相同的岗位");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "startUserJobCharge")) {//流程发起人相同的岗位
			nodeUser.setAssignTypeName("流程发起人相同的岗位负责人");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "startUserDeptSame")) {//流程发起人相同的岗位负责人
			nodeUser.setAssignTypeName("流程发起人相同的部门");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "startUserDeptCharge")) {//流程发起人相同的部门
			nodeUser.setAssignTypeName("流程发起人相同的部门负责人");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "taskExecutor")) {//节点任务执行人
			NodeSetEntity nodeSet = this.nodeSetService.getNodeSetByDefIdNodeId(nodeUser.getDefId(),
					request.getParameter("taskExecutor"));
			if (nodeSet != null) {
				nodeUser.setAssignNames(nodeSet.getNodeName());
			}
			nodeUser.setAssignIds(request.getParameter("taskExecutor"));
			nodeUser.setAssignTypeName("节点任务执行人");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "taskExecutorDirectLeader")) {//节点任务执行人直接领导
			NodeSetEntity nodeSet = this.nodeSetService.getNodeSetByDefIdNodeId(nodeUser.getDefId(),
					request.getParameter("taskExecutorDirectLeader"));
			if (nodeSet != null) {
				nodeUser.setAssignNames(nodeSet.getNodeName());
			}
			nodeUser.setAssignIds(request.getParameter("taskExecutorDirectLeader"));
			nodeUser.setAssignTypeName("节点任务执行人直接领导");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "taskExecutorBranchLeader")) {//节点任务执行人直接领导
			NodeSetEntity nodeSet = this.nodeSetService.getNodeSetByDefIdNodeId(nodeUser.getDefId(),
					request.getParameter("taskExecutorBranchLeader"));
			if (nodeSet != null) {
				nodeUser.setAssignNames(nodeSet.getNodeName());
			}
			nodeUser.setAssignIds(request.getParameter("taskExecutorBranchLeader"));
			nodeUser.setAssignTypeName("节点任务执行人分管领导");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "taskExecutorRoleSame")) {//节点任务执行人相同的角色
			NodeSetEntity nodeSet = this.nodeSetService.getNodeSetByDefIdNodeId(nodeUser.getDefId(),
					request.getParameter("taskExecutorRoleSame"));
			if (nodeSet != null) {
				nodeUser.setAssignNames(nodeSet.getNodeName());
			}
			nodeUser.setAssignIds(request.getParameter("taskExecutorRoleSame"));
			nodeUser.setAssignTypeName("节点任务执行人相同的角色");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "taskExecutorJobSame")) {//节点任务执行人相同的岗位
			NodeSetEntity nodeSet = this.nodeSetService.getNodeSetByDefIdNodeId(nodeUser.getDefId(),
					request.getParameter("taskExecutorJobSame"));
			if (nodeSet != null) {
				nodeUser.setAssignNames(nodeSet.getNodeName());
			}
			nodeUser.setAssignIds(request.getParameter("taskExecutorJobSame"));
			nodeUser.setAssignTypeName("节点任务执行人相同的岗位");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "taskExecutorJobCharge")) {//节点任务执行人相同的岗位负责人
			NodeSetEntity nodeSet = this.nodeSetService.getNodeSetByDefIdNodeId(nodeUser.getDefId(),
					request.getParameter("taskExecutorJobCharge"));
			if (nodeSet != null) {
				nodeUser.setAssignNames(nodeSet.getNodeName());
			}
			nodeUser.setAssignIds(request.getParameter("taskExecutorJobCharge"));
			nodeUser.setAssignTypeName("节点任务执行人相同的岗位负责人");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "taskExecutorDeptSame")) {//节点任务执行人相同的部门
			NodeSetEntity nodeSet = this.nodeSetService.getNodeSetByDefIdNodeId(nodeUser.getDefId(),
					request.getParameter("taskExecutorDeptSame"));
			if (nodeSet != null) {
				nodeUser.setAssignNames(nodeSet.getNodeName());
			}
			nodeUser.setAssignIds(request.getParameter("taskExecutorDeptSame"));
			nodeUser.setAssignTypeName("节点任务执行人相同的部门");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "taskExecutorDeptCharge")) {//节点任务执行人相同的部门负责人
			NodeSetEntity nodeSet = this.nodeSetService.getNodeSetByDefIdNodeId(nodeUser.getDefId(),
					request.getParameter("taskExecutorDeptCharge"));
			if (nodeSet != null) {
				nodeUser.setAssignNames(nodeSet.getNodeName());
			}
			nodeUser.setAssignIds(request.getParameter("taskExecutorDeptCharge"));
			nodeUser.setAssignTypeName("节点任务执行人相同的部门负责人");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "taskCandidates")) {//节点任务候选人
			NodeSetEntity nodeSet = this.nodeSetService.getNodeSetByDefIdNodeId(nodeUser.getDefId(),
					request.getParameter("taskExecutorJobSame"));
			if (nodeSet != null) {
				nodeUser.setAssignNames(nodeSet.getNodeName());
			}
			nodeUser.setAssignIds(request.getParameter("taskExecutorJobSame"));
			nodeUser.setAssignTypeName("节点任务候选人");
		} else if (StringUtil.equals(nodeUser.getAssignType(), "manualExecutor")) {
			nodeUser.setAssignIds(request.getParameter("manualExecutor"));
			nodeUser.setAssignNames("自定义脚本执行人");
			nodeUser.setAssignTypeName("自定义脚本执行人");
		}

		if (StringUtil.equals("and", nodeUser.getCountType())) {
			nodeUser.setCountTypeName("与");
		} else if (StringUtil.equals("or", nodeUser.getCountType())) {
			nodeUser.setCountTypeName("或");
		} else if (StringUtil.equals("not", nodeUser.getCountType())) {
			nodeUser.setCountTypeName("非");
		}
		try {
			if (StringUtil.isNotEmpty(nodeUser.getId())) {
				message = "选择用户更新成功";
				nodeUserService.update(nodeUser);
			} else {
				message = "选择用户新增成功";
				nodeUserService.save(nodeUser);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}

	public void setNodeUserService(NodeUserService nodeUserService) {
		this.nodeUserService = nodeUserService;
	}

	public void setDefinitionService(DefinitionService definitionService) {
		this.definitionService = definitionService;
	}

	public void setNodeSetService(NodeSetService nodeSetService) {
		this.nodeSetService = nodeSetService;
	}

}
