package com.xplatform.base.workflow.agent.controller;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.xplatform.base.workflow.agent.entity.AgentFlowEntity;
import com.xplatform.base.workflow.agent.entity.AgentSettingEntity;
import com.xplatform.base.workflow.agent.service.AgentSettingService;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;

/**
 * 
 * description : 流程代理设置管理controller
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
@RequestMapping("/agentSettingController")
public class AgentSettingController extends BaseController {
	
	@Resource
	private AgentSettingService agentSettingService;
	
	@Resource
	private DefinitionService definitionService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 流程代理设置管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "agentSetting")
	public ModelAndView agentSetting(HttpServletRequest request) {
		return new ModelAndView("workflow/agent/agentSettingList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param AgentSetting
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(AgentSettingEntity AgentSetting,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(AgentSettingEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, AgentSetting, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.agentSettingService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 流程代理设置删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param AgentSetting
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(AgentSettingEntity AgentSetting, HttpServletRequest request) {
		message = "流程代理设置删除成功";
		try{
			agentSettingService.delete(AgentSetting.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除流程代理设置
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
			agentSettingService.batchDelete(ids);
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
	@RequestMapping(params = "agentSettingEdit")
	public ModelAndView agentSettingEdit(AgentSettingEntity agentSetting,Model model) {
		if (StringUtil.isNotEmpty(agentSetting.getId())) {
			agentSetting = agentSettingService.get(agentSetting.getId());
			model.addAttribute("agentSetting", agentSetting);
		}
		List<AgentFlowEntity> list = this.agentSettingService.queryFlowList(agentSetting);
		String flowId ="";
		String flowName = "";
		for(AgentFlowEntity agent : list){
			flowId += agent.getFlowId()+",";
			flowName += agent.getFlowName()+",";
		}
		return new ModelAndView("workflow/agent/agentSettingEdit").addObject("flowId", flowId.substring(0, flowId.lastIndexOf(",") == -1 ? 0 : flowId.lastIndexOf(",") )).addObject("flowName", flowName.substring(0, flowName.lastIndexOf(",") == -1 ? 0 : flowName.lastIndexOf(",") ));
	}
	
	/**
	 * 新增或修改流程代理设置
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param AgentSetting
	 * @return
	 */
	@RequestMapping(params = "saveAgentSetting")
	@ResponseBody
	public AjaxJson saveAgentSetting(AgentSettingEntity AgentSetting,HttpServletRequest request) {
		try {
			if (StringUtil.isNotEmpty(AgentSetting.getId())) {
				message = "流程代理设置更新成功";
				agentSettingService.update(AgentSetting);
			} else {
				message = "流程代理设置新增成功";
				agentSettingService.save(AgentSetting);
			}
			this.agentSettingService.deleteAgentFlowEntity(AgentSetting);
			String flowId = request.getParameter("flowId");
			if(StringUtils.isNotEmpty(flowId)){
				 String[] flowIds = flowId.split(",");
				 for(int i=0;i<flowIds.length;i++){
					 DefinitionEntity Definition = this.definitionService.get(flowIds[i]);
					 AgentFlowEntity agentFlowEntity = new AgentFlowEntity();
					 agentFlowEntity.setFlowId(flowIds[i]);
					 agentFlowEntity.setFlowCode(Definition.getCode());
					 agentFlowEntity.setFlowName(Definition.getName());
					 agentFlowEntity.setFlowVersion(Definition.getVersion());
					 agentFlowEntity.setSetting(AgentSetting);
					 agentSettingService.saveFlow(agentFlowEntity);
				 }	
			}
			
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	

	public void setAgentSettingService(AgentSettingService agentSettingService) {
		this.agentSettingService = agentSettingService;
	}

	public void setDefinitionService(DefinitionService definitionService) {
		this.definitionService = definitionService;
	}
	
	
}
