package com.xplatform.base.workflow.instance.controller;

import java.util.Arrays;
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
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.instance.entity.ProcessInstCptoEntity;
import com.xplatform.base.workflow.instance.entity.ProcessInstHistoryEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstCptoService;
import com.xplatform.base.workflow.instance.service.ProcessInstHistoryService;
import com.xplatform.base.workflow.instance.service.ProcessInstanceService;

/**
 * 
 * description : 流程实例管理controller
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
@RequestMapping("/processInsCptoController")
public class ProcessInstCptoController extends BaseController {
	
	@Resource
	private ProcessInstCptoService processInsCptoService;
	@Resource
	private ProcessInstanceService processInstanceService;
	@Resource
	private ProcessInstHistoryService processInstHistoryService;
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 流程实例管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "ProcessInsCpto")
	public ModelAndView ProcessInsCpto(HttpServletRequest request) {
		return new ModelAndView("platform/organization/ProcessInsCpto/ProcessInsCptoList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param ProcessInsCpto
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(ProcessInstCptoEntity ProcessInsCpto,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ProcessInstCptoEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, ProcessInsCpto, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.processInsCptoService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年12月20日 下午6:26:47
	 * @Decription 抄送人列表数据
	 *
	 * @param ProcessInsCpto
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "cptoDatagrid")
	public void cptoDatagrid(ProcessInstCptoEntity ProcessInsCpto, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		List<ProcessInstCptoEntity> list = this.processInsCptoService.findCptoByTaskOrInstId(id, type);
		dataGrid.setResults(list);
		dataGrid.setTotal(list.size());
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 流程实例删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param ProcessInsCpto
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(ProcessInstCptoEntity ProcessInsCpto, HttpServletRequest request) {
		message = "流程实例删除成功";
		try{
			processInsCptoService.delete(ProcessInsCpto.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除流程实例
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
			processInsCptoService.batchDelete(ids);
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
	@RequestMapping(params = "ProcessInsCptoEdit")
	public ModelAndView ProcessInsCptoEdit(ProcessInstCptoEntity ProcessInsCpto,Model model) {
		if (StringUtil.isNotEmpty(ProcessInsCpto.getId())) {
			ProcessInsCpto = processInsCptoService.get(ProcessInsCpto.getId());
			model.addAttribute("ProcessInsCpto", ProcessInsCpto);
		}
		return new ModelAndView("platform/organization/ProcessInsCpto/ProcessInsCptoEdit");
	}
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年11月4日 下午2:21:30
	 * @Decription "转发"按钮进入页面
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "divertTask")
	public ModelAndView divertTask(HttpServletRequest request) {
		String actInstId = request.getParameter("actInstId");
		request.setAttribute("actInstId", actInstId);
		return new ModelAndView("workflow/task/divertTask");
	}
	
	/**
	 * 新增或修改流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param ProcessInsCpto
	 * @return
	 */
	@RequestMapping(params = "finishDivert")
	@ResponseBody
	public AjaxJson finishDivert(HttpServletRequest request) {
		message = "流程转发成功";
		try {
			/*if (StringUtil.isNotEmpty(ProcessInsCpto.getId())) {
				message = "流程实例更新成功";
				processInsCptoService.update(ProcessInsCpto);
			} else {
				message = "流程实例新增成功";
				processInsCptoService.save(ProcessInsCpto);
			}*/
			
			String actInstId = request.getParameter("actInstId");
		    String users = request.getParameter("empIds");
		    String informType = StringUtil.asString(request.getParameterValues("informType"), ",");
		    String opinion = request.getParameter("opinion");
		    
	        String[] userArray = users.split(",");
	        List<String> userList = Arrays.asList(userArray);
	        ProcessInstHistoryEntity historyInst = processInstHistoryService.getByActInstanceId(actInstId);
            UserEntity currUser = ClientUtil.getUserEntity();
            this.processInstanceService.executeDivertProcess(historyInst, userList, currUser, informType, opinion);
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	public void setProcessInsCptoService(ProcessInstCptoService processInsCptoService) {
		this.processInsCptoService = processInsCptoService;
	}
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年12月20日 下午6:22:02
	 * @Decription 进入抄送人列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "showCptoList")
	public ModelAndView showCptoList(HttpServletRequest request) {
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		request.setAttribute("id", id);
		request.setAttribute("type", type);
		return new ModelAndView("workflow/task/showCptoList");
	}
}
