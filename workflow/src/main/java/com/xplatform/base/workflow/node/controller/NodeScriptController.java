package com.xplatform.base.workflow.node.controller;
import java.util.List;

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
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.workflow.node.entity.NodeScriptEntity;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.service.NodeScriptService;
import com.xplatform.base.workflow.node.service.NodeSetService;

/**
 * 
 * description : 节点时间脚本管理controller
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
@RequestMapping("/nodeScriptController")
public class NodeScriptController extends BaseController {
	
	@Resource
	private NodeScriptService nodeScriptService;
	
	@Resource
	private NodeSetService nodeSetService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 节点时间脚本管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "nodeScript")
	public ModelAndView NodeScript(HttpServletRequest request) {
		String actDefId = request.getParameter("actDefId");
		String nodeId = request.getParameter("nodeId");
		String type = request.getParameter("type");
		List<NodeScriptEntity> list = this.nodeScriptService.queryListByActDefIdAndNodeId(actDefId,nodeId);
		NodeScriptEntity pre = null;
		NodeScriptEntity rear = null;
		NodeScriptEntity allot = null;
		for(NodeScriptEntity nse : list){
			if("pre".equals(nse.getType())){
				pre = nse;
			}else if("rear".equals(nse.getType())){
				rear = nse;
			}else if("allot".equals(nse.getType())){
				allot = nse;
			}else{
				pre = nse;
			}
		}
		return new ModelAndView("workflow/node/nodeScript").addObject("pre", pre).addObject("rear", rear).addObject("allot", allot);
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param NodeScript
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(NodeScriptEntity NodeScript,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(NodeScriptEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, NodeScript, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.nodeScriptService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 节点时间脚本删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param NodeScript
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(NodeScriptEntity NodeScript, HttpServletRequest request) {
		message = "节点时间脚本删除成功";
		try{
			nodeScriptService.delete(NodeScript.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除节点时间脚本
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
			nodeScriptService.batchDelete(ids);
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
	@RequestMapping(params = "nodeScriptEdit")
	public ModelAndView NodeScriptEdit(NodeScriptEntity NodeScript,Model model) {
		if (StringUtil.isNotEmpty(NodeScript.getId())) {
			NodeScript = nodeScriptService.get(NodeScript.getId());
			model.addAttribute("NodeScript", NodeScript);
		}
		return new ModelAndView("platform/organization/NodeScript/NodeScriptEdit");
	}
	
	/**
	 * 新增或修改节点时间脚本
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param NodeScript
	 * @return
	 */
	@RequestMapping(params = "saveNodeScript")
	@ResponseBody
	public AjaxJson saveNodeScript(HttpServletRequest request) {
		String actDefId = request.getParameter("actDefId");
		String nodeId = request.getParameter("nodeId");
		String type = request.getParameter("type");
		nodeScriptService.deleteByActDefIdAndNodeId(nodeId,actDefId);
		
		try {
			if("userTask".equals(type) || "multiUserTask".equals(type)){
				//前置
				NodeScriptEntity pre = new NodeScriptEntity();
				pre.setActDefId(actDefId);
				pre.setNodeId(nodeId);
				pre.setScript(request.getParameter("preScript"));
				pre.setType("pre");
				pre.setDescription(request.getParameter("preDescription"));
				nodeScriptService.save(pre);
				//后置
				NodeScriptEntity rear = new NodeScriptEntity();
				rear.setActDefId(actDefId);
				rear.setNodeId(nodeId);
				rear.setScript(request.getParameter("rearScript"));
				rear.setType("rear");
				rear.setDescription(request.getParameter("rearDescription"));
				nodeScriptService.save(rear);
				//分配
				NodeScriptEntity allot = new NodeScriptEntity();
				allot.setActDefId(actDefId);
				allot.setNodeId(nodeId);
				allot.setScript(request.getParameter("allotScript"));
				allot.setType("allot");
				allot.setDescription(request.getParameter("allotDescription"));
				nodeScriptService.save(allot);
			}else if("startEvent".equals(type)){ 
				//开始脚本
				NodeScriptEntity pre = new NodeScriptEntity();
				pre.setActDefId(actDefId);
				pre.setNodeId(nodeId);
				pre.setScript(request.getParameter("preScript"));
				pre.setType("start");
				pre.setDescription(request.getParameter("preDescription"));
				nodeScriptService.save(pre);
			}else if("endEvent".equals(type)){
				//结束脚本
				NodeScriptEntity pre = new NodeScriptEntity();
				pre.setActDefId(actDefId);
				pre.setNodeId(nodeId);
				pre.setScript(request.getParameter("preScript"));
				pre.setType("end");
				pre.setDescription(request.getParameter("preDescription"));
				nodeScriptService.save(pre);
			}else if("script".equals(type)){
				//脚本节点
				NodeScriptEntity pre = new NodeScriptEntity();
				pre.setActDefId(actDefId);
				pre.setNodeId(nodeId);
				pre.setScript(request.getParameter("preScript"));
				pre.setType("script");
				pre.setDescription(request.getParameter("preDescription"));
				nodeScriptService.save(pre);
			}
		} catch (BusinessException e) {
			// TODO: handle exception
			message=e.getMessage();
			result.setMsg(message);
			return result;
		}
		
		message = "保存成功";
		result.setMsg(message);
		return result;
	}
	

	public void setNodeScriptService(NodeScriptService nodeScriptService) {
		this.nodeScriptService = nodeScriptService;
	}

	public void setNodeSetService(NodeSetService nodeSetService) {
		this.nodeSetService = nodeSetService;
	}
	
	
}
