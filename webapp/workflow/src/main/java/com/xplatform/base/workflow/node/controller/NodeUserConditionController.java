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
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.workflow.node.entity.NodeUserConditionEntity;
import com.xplatform.base.workflow.node.mybatis.vo.NodeUserVo;
import com.xplatform.base.workflow.node.service.NodeUserConditionService;

/**
 * 
 * description : 节点用户条件管理controller
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
@RequestMapping("/nodeUserConditionController")
public class NodeUserConditionController extends BaseController {
	
	@Resource
	private NodeUserConditionService nodeUserConditionService;
	
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 节点用户条件管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "nodeUserCondition")
	public ModelAndView nodeUserCondition(HttpServletRequest request,Model model) {
		String defId = request.getParameter("defId");
		model.addAttribute("defId", defId);
		return new ModelAndView("workflow/node/user/nodeUser");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param NodeUserCondition
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		
		String defId = request.getParameter("defId");
		//List<NodeUserVo> conditonList=this.nodeUserConditionService.queryNodeUserCondtionListByDefId(defId);
		/*List<NodeSetEntity> setList = this.nodeSetService.queryByPorperty("defId",defId);
		List<NodeUserMap> nodeUserMapList = new ArrayList<NodeUserMap>();
		List<NodeUserConditionEntity> bpmUserConditionList;
		
		for (NodeSetEntity nodeSet : setList) {
			bpmUserConditionList = this.nodeUserConditionService.getBySetId(nodeSet.getId());
			NodeUserMap nodeUserMap = new NodeUserMap();
			nodeUserMap.setSetId(nodeSet.getId());
			nodeUserMap.setNodeId(nodeSet.getNodeId());
			nodeUserMap.setNodeName(nodeSet.getNodeName());
			nodeUserMap.setNodeUserConditionList(bpmUserConditionList);
			nodeUserMapList.add(nodeUserMap);
		}*/
		/*if(conditonList!=null){
			for(NodeUserVo user: conditonList){
				if(!StringUtil.isNotEmpty(user.getId())){
					user.setId(UUIDGenerator.generate());
					user.setName("请添加人员信息!!!");
					user.setFlag("false");
				}else{
					user.setFlag("true");
				}
			}
			dataGrid.setTotal(conditonList.size());
		}else{
			dataGrid.setTotal(0);
		}
		dataGrid.setResults(conditonList);*/
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 节点用户条件删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param NodeUserCondition
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(NodeUserConditionEntity NodeUserCondition, HttpServletRequest request) {
		message = "节点用户条件删除成功";
		try{
			nodeUserConditionService.delete(NodeUserCondition.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除节点用户条件
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
			nodeUserConditionService.batchDelete(ids);
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
	@RequestMapping(params = "nodeUserConditionEdit")
	public ModelAndView nodeUserConditionEdit(NodeUserConditionEntity nodeUserCondition,Model model) {
		if (StringUtil.isNotEmpty(nodeUserCondition.getId())) {
			nodeUserCondition = nodeUserConditionService.get(nodeUserCondition.getId());
			model.addAttribute("nodeUserCondition", nodeUserCondition);
		}
		return new ModelAndView("workflow/node/user/nodeUserEdit");
	}
	
	/**
	 * 新增或修改节点用户条件
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param NodeUserCondition
	 * @return
	 */
	@RequestMapping(params = "saveNodeUserCondition")
	@ResponseBody
	public AjaxJson saveNodeUserCondition(NodeUserConditionEntity NodeUserCondition) {
		try {
			if (StringUtil.isNotEmpty(NodeUserCondition.getId())) {
				message = "节点用户条件更新成功";
				nodeUserConditionService.update(NodeUserCondition);
			} else {
				message = "节点用户条件新增成功";
				nodeUserConditionService.save(NodeUserCondition);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	

	public void setNodeUserConditionService(NodeUserConditionService nodeUserConditionService) {
		this.nodeUserConditionService = nodeUserConditionService;
	}
	
	
}
