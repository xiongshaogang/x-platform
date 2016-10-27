package com.xplatform.base.system.timer.controller;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
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
import com.xplatform.base.system.timer.entity.TimerLogEntity;
import com.xplatform.base.system.timer.service.TimerLogService;

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
@RequestMapping("/timerLogController")
public class TimerLogController extends BaseController {
	
	@Resource
	private TimerLogService timerLogService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 定时器任务日志列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "timerLog")
	public ModelAndView timerLog(HttpServletRequest request) {
		request.setAttribute("jobName", request.getParameter("jobName"));
		request.setAttribute("triggerName", request.getParameter("triggerName"));
		return new ModelAndView("platform/system/timer/timerLogList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param TimerLog
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(TimerLogEntity TimerLog,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String jobName=request.getParameter("jobName");
		String triggerName=request.getParameter("triggerName");
		if(!(StringUtil.isEmpty(jobName) && StringUtil.isEmpty(triggerName))){
			CriteriaQuery cq = new CriteriaQuery(TimerLogEntity.class, dataGrid);
			//查询条件组装器
			HqlGenerateUtil.installHql(cq, TimerLog, request.getParameterMap());
			cq.add();
			this.timerLogService.getDataGridReturn(cq, true);
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
			timerLogService.delete(TimerLog.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除定时器任务日志
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
			timerLogService.batchDelete(ids);
			message="删除成功";
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	
	
	public void setTimerLogService(TimerLogService timerLogService) {
		this.timerLogService = timerLogService;
	}
	
	
}
