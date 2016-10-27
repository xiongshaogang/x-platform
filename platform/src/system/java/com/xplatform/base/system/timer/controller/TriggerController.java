package com.xplatform.base.system.timer.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.Trigger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.system.timer.entity.TimerLogEntity;
import com.xplatform.base.system.timer.entity.TriggerEntity;
import com.xplatform.base.system.timer.service.TimerLogService;
import com.xplatform.base.system.timer.service.TimerService;

/**
 * 
 * description : 定时器任务日志controller
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
@RequestMapping("/triggerController")
public class TriggerController extends BaseController {
	
	@Resource
	private TimerLogService timerLogService;
	
	@Resource
	private TimerService timerService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 定时器任务日志列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "trigger")
	public ModelAndView trigger(HttpServletRequest request ,String jobName) {
		request.setAttribute("jobName", request.getParameter("jobName"));
		return new ModelAndView("platform/system/timer/triggerList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param TimerLog
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @throws BusinessException 
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) throws BusinessException {
		
		String jobName = request.getParameter("jobName");
		List<TriggerEntity> triggerList=new ArrayList<TriggerEntity>();
		List list = this.timerService.getTriggersByJob(jobName);
		if(list!=null && list.size()>0){
			HashMap mapState = this.timerService.getTriggerStatus(list);
			for(int i=0;i<list.size();i++){
				TriggerEntity entity=new TriggerEntity();
				entity.setId(UUIDGenerator.generate());
				Trigger trig=(Trigger)list.get(i);
				entity.setDescription(trig.getDescription());
				entity.setJobName(trig.getJobKey().getName());
				entity.setTrigName(trig.getKey().getName());
				Trigger.TriggerState state=(Trigger.TriggerState)mapState.get(trig.getKey().getName());
				entity.setStatus(state.name());
				triggerList.add(entity);
			}
			dataGrid.setResults(triggerList);
			dataGrid.setTotal(list.size());
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 定时器任务日志删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param TimerLog
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(TimerLogEntity TimerLog, HttpServletRequest request) {
		message = "定时器任务日志删除成功";
		try{
			String name = request.getParameter("name");
			this.timerService.delTrigger(name);
		}catch(Exception e){
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
	@RequestMapping(params = "triggerEdit")
	public ModelAndView triggerEdit( Model model) {
		/*if (StringUtil.isNotEmpty(module.getId())) {
			module = moduleService.get(module.getId());
			model.addAttribute("module", module);
		}*/
		return new ModelAndView("platform/system/timer/triggerEdit");
	}
	
	/**
	 * 新增或修改资源
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param resource
	 * @return
	 */
	@RequestMapping(params = "saveTrigger")
	@ResponseBody
	public AjaxJson saveTrigger(HttpServletRequest request) {
		message = "定时计划添加成功";
		try {
			String trigName =request.getParameter("name");
			String jobName = request.getParameter("jobName");

			String planJson = request.getParameter("planJson");

			boolean rtn = this.timerService.isTriggerExists(trigName);
			if (rtn) {
				message="指定的计划名称已经存在!";
			}else{
				this.timerService.addTrigger(jobName, trigName, planJson);
			}
		} catch (BusinessException e) {
			// TODO: handle exception
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	
	@RequestMapping(params = "doActive")
	@ResponseBody
	public AjaxJson doActive(HttpServletRequest request) {
		message="启用或禁用定时计划作业计划成功";
		try{
			String name =request.getParameter("name");
			this.timerService.updateTriggerRun(name);
		}catch(BusinessException e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	public void setTimerLogService(TimerLogService timerLogService) {
		this.timerLogService = timerLogService;
	}

	public void setTimerService(TimerService timerService) {
		this.timerService = timerService;
	}
	
	
}
