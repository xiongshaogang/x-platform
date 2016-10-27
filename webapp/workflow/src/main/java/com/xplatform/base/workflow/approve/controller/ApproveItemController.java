package com.xplatform.base.workflow.approve.controller;
import java.io.IOException;
import java.util.ArrayList;
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
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.workflow.approve.entity.ApproveItemEntity;
import com.xplatform.base.workflow.approve.service.ApproveItemService;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.service.NodeSetService;

/**
 * 
 * description : 审批常用语管理controller
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
@RequestMapping("/approveItemController")
public class ApproveItemController extends BaseController {
	
	@Resource
	private ApproveItemService approveItemService;
	
	@Resource
	private NodeSetService nodeSetService;
	
	@Resource
	private FlowService flowService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 审批常用语管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "approveItem")
	public ModelAndView ApproveItem(HttpServletRequest request) {
		String defId = request.getParameter("defId");
	    String nodeId = request.getParameter("nodeId");
	    String actDefId = request.getParameter("actDefId");

	    String setId = null;

	    Map<String,String> nodeMap = this.flowService.getExecuteNodesMap(actDefId, true);
	    //获取setId
	    if (StringUtil.isNotEmpty(nodeId)) {
	        NodeSetEntity nodeSet = this.nodeSetService.getNodeSetByDefIdNodeId(defId, nodeId);
	        setId = nodeSet.getId();
	    }

	    String isGlobal = "N";

	    ApproveItemEntity defExpItems = this.approveItemService.getFlowApproval(actDefId, ApproveItemEntity.global);
	    String defExp = defExpItems == null ? "" : defExpItems.getContent();
	    isGlobal = defExpItems == null ? "Y" : defExpItems.getIsGlobal();

	    ApproveItemEntity nodeExpItems = this.approveItemService.getTaskApproval(actDefId, nodeId, ApproveItemEntity.notGlobal);
	    String nodeExp = nodeExpItems == null ? "" : nodeExpItems.getContent();
	    if (StringUtil.equals("Y", isGlobal)) {
	      isGlobal = nodeExpItems == null ? "Y" : nodeExpItems.getIsGlobal();
	    }

	    //构造combobox数据
	    List<ComboBox> valueList =new ArrayList<ComboBox>();
	    for(Map.Entry<String,String> entry:nodeMap.entrySet()){
	    	ComboBox combo=new ComboBox();
	    	combo.setId(entry.getKey());
	    	combo.setText(entry.getValue());
	    	valueList.add(combo);
	    }
		String data = JSONHelper.toJSONString(valueList);
		return new ModelAndView("workflow/node/approveItem") 
	    		  .addObject("nodeMap",data)
			      .addObject("nodeId", nodeId)
			      .addObject("actDefId", actDefId)
			      .addObject("defExp", defExp)
			      .addObject("nodeExp", nodeExp)
			      .addObject("setId", setId)
			      .addObject("isGlobal", isGlobal);
	}

	
	/**
	 * 进入新增或者修改查看页面
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "get")
	public void get(HttpServletRequest request,HttpServletResponse response,ApproveItemEntity ApproveItem,Model model) {
	    String nodeId = request.getParameter("nodeId");
	    String actDefId = request.getParameter("actDefId");

	    ApproveItemEntity nodeExpItems = this.approveItemService.getTaskApproval(actDefId, nodeId, ApproveItemEntity.notGlobal);
	    String nodeExp = nodeExpItems == null ? "" : nodeExpItems.getContent();
	    try {
			response.getWriter().write(nodeExp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 新增或修改审批常用语
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param ApproveItem
	 * @return
	 */
	@RequestMapping(params = "saveApproveItem")
	@ResponseBody
	public AjaxJson saveApproveItem(HttpServletRequest request,ApproveItemEntity approveItem) {
		try {
				message = "审批常用语更新成功";
				String isGlobal = request.getParameter("isGlobal");
				if(isGlobal==null){
					approveItem.setIsGlobal("N");
				}
			    String approvalItem = request.getParameter("approvalItem");
			    String actDefId = request.getParameter("actDefId");
			    String nodeId = request.getParameter("nodeId");
			    // String setId = request.getParameter("setId");
			    
			    approveItem.setContent(approvalItem);
			    ApproveItemEntity entity=null;
			    if (StringUtil.equals("Y", isGlobal)){
			    	entity=this.approveItemService.getFlowApproval(actDefId, ApproveItemEntity.global);
			    }else {
			    	entity=this.approveItemService.getTaskApproval(actDefId, nodeId, ApproveItemEntity.notGlobal);
			    }
			    if(entity!=null){
		    		this.approveItemService.delete(entity.getId());
		    	}
			    this.approveItemService.save(approveItem);
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	

	public void setApproveItemService(ApproveItemService approveItemService) {
		this.approveItemService = approveItemService;
	}


	public void setNodeSetService(NodeSetService nodeSetService) {
		this.nodeSetService = nodeSetService;
	}


	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}
	
	
}
