package com.xplatform.base.system.timer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.JobDetail;
import org.quartz.impl.JobDetailImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.system.timer.entity.JobEntity;
import com.xplatform.base.system.timer.service.TimerService;

@Controller
@RequestMapping("/timerController")
public class TimerController {
	@Resource
	private TimerService timerService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 资源管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "timer")
	public ModelAndView resource(HttpServletRequest request) {
		return new ModelAndView("platform/system/timer/timerList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param resource
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @throws BusinessException 
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) throws BusinessException {
		List<JobDetail> list = null;
		list = this.timerService.getJobList();
		if(list!=null && list.size()>0){
			List jobList=new ArrayList();
			for(JobDetail jobDetail:list){
				JobDetailImpl de=(JobDetailImpl)jobDetail;
				JobEntity job=new JobEntity();
				job.setId(UUIDGenerator.generate());
				job.setDescription(jobDetail.getDescription());
				job.setClassName(jobDetail.getJobClass());
				job.setName(de.getName());
				jobList.add(job);
			}
			dataGrid.setResults(jobList);
			dataGrid.setTotal(list.size());
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 资源删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param resource
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete( HttpServletRequest request) {
		message = "定时计划删除成功";
		try{
			String jobName = request.getParameter("jobName");
			this.timerService.delJob(jobName);
		}catch(BusinessException e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "executeJob")
	@ResponseBody
	public AjaxJson executeJob(HttpServletRequest request) {
		message = "定时计划删除成功";
		try{
			String jobName = request.getParameter("jobName");
			this.timerService.executeJob(jobName);
		}catch(BusinessException e){
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
	@RequestMapping(params = "timerEdit")
	public ModelAndView timerEdit(Model model) {
		/*if (StringUtil.isNotEmpty(resource.getId())) {
			resource = resourceService.get(resource.getId());
			model.addAttribute("resource", resource);
		}*/
		return new ModelAndView("platform/system/timer/timerEdit");
	}
	
	/**
	 * 新增或修改资源
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param resource
	 * @return
	 */
	@RequestMapping(params = "saveTimer")
	@ResponseBody
	public AjaxJson saveTimer(HttpServletRequest request) {
		message = "定时计划添加成功";
		try {
				String className = request.getParameter("className");
				String jobName =request.getParameter("name");
				String parameterJson = request.getParameter("parameterJson");
				String description = request.getParameter("description");
				boolean isExist = this.timerService.isJobExists(jobName);
				if (isExist) {
					message = "任务名称已经存在，添加失败!";
				} else {
					this.timerService.addJob(jobName, className,parameterJson, description);
				}
		} catch (BusinessException e) {
			// TODO: handle exception
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "validClass")
	@ResponseBody
	public AjaxJson validClass(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String className = request.getParameter("className");
		message = "类验证通过";
		try {
			Class.forName(className);
		} catch (ClassNotFoundException e) {
			message = "类验证失败";
		}
		result.setMsg(message);
		return result;
	}

	public void setTimerService(TimerService timerService) {
		this.timerService = timerService;
	}

}
