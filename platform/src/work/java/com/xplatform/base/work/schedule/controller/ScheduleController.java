package com.xplatform.base.work.schedule.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.mybatis.dao.SysUserMybatisDao;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.work.schedule.entity.OftenScheduleEntity;
import com.xplatform.base.work.schedule.entity.ScheduleEntity;
import com.xplatform.base.work.schedule.mybatis.dao.ReminderMybatisDao;
import com.xplatform.base.work.schedule.service.OftenScheduleService;
import com.xplatform.base.work.schedule.service.ScheduleService;

/**
 * 日程管理
 * @version 1.0
 * @author luoheng
 *
 */
@Controller
@RequestMapping("/scheduleController")
public class ScheduleController extends BaseController {
	
	@Resource
	private ScheduleService scheduleService;
	
	@Resource
	private ReminderMybatisDao reminderMybatisDao;
	
	@Resource
	private OftenScheduleService oftenScheduleService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 跳转日程管理主界面
	 * @author luoheng
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "scheduleManagement")
	public ModelAndView ScheduleManagement(HttpServletRequest request) {
		return new ModelAndView("platform/work/schedule/scheduleManagement");
	}
	
	/**
	 * 保存、修改日程数据
	 * @param request
	 * @param schedule
	 * @return
	 */
	@RequestMapping(params = "saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(HttpServletRequest request){
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		UserEntity user = client.getUser();
		ScheduleEntity schedule = new ScheduleEntity();
		schedule.setUserId(user.getId());
		
		try {
			
			//获取前台输入的值
			String id = URLDecoder.decode(request.getParameter("id"), "utf-8");
			String title = URLDecoder.decode(request.getParameter("title"), "utf-8");
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String context = URLDecoder.decode(request.getParameter("context"), "utf-8");
			String className = URLDecoder.decode(request.getParameter("className"), "utf-8");
			String scheduleType = "";
			String scheduleTypes[] = request.getParameterValues("scheduleType");
			if(scheduleTypes != null){
				for(int i = 0; i < scheduleTypes.length; i++){
					scheduleType += scheduleTypes[i] + ",";
				}
				scheduleType = scheduleType.substring(0, scheduleType.length() - 1);
			}else{
				scheduleType = URLDecoder.decode(request.getParameter("scheduleType"), "utf-8");
			}
			String allDay = "true";
			if(request.getParameter("allDay") != null){
				allDay = URLDecoder.decode(request.getParameter("allDay"), "utf-8");
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			//设置对象值并保存数据
			schedule.setId(id);
			schedule.setTitle(title);
			schedule.setStartDate(format.parse(startDate));
			schedule.setEndDate(format.parse(endDate));
			schedule.setContext(context);
			schedule.setClassName(className);
			schedule.setAllDay(allDay);
			schedule.setScheduleType(scheduleType);
			schedule.setState("0");
			
			if(StringUtil.isNotEmpty(schedule.getId())){
				//修改日程信息
				scheduleService.update(schedule);
				
				message="修改成功";
			}else{
				//保存日程信息
				scheduleService.save(schedule);
				message="保存成功";
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 删除日程数据
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request) {
		String ids=request.getParameter("ids");
		try {
			if(StringUtil.isNotEmpty(ids)){
				scheduleService.batchDelete(ids);
			}
			message="删除成功";
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
			result.setMsg(message);
		}
		return result;
	}
	
	/**
	 * 根据ID查询日程信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "findById")
	public void findById(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id");
		ScheduleEntity schedule = scheduleService.getScheduleEntity(id);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("{\"id\":\"" + schedule.getId() + "\"");
			sb.append(",\"title\":\"\\n" + schedule.getTitle()+"\\n");
			sb.append("\"");
			sb.append(",\"startDate\":\"" + format.format(schedule.getStartDate()) + "\"" );
			sb.append(",\"endDate\":\""  + format.format(schedule.getEndDate()) + "\"");
			sb.append(",\"context\":\""  + schedule.getContext() + "\"");
			sb.append(",\"className\":\""  + schedule.getClassName() + "\"");
			sb.append(",\"scheduleType\":\""  + schedule.getScheduleType() + "\"");
			sb.append(",\"allDay\":\""  + schedule.getAllDay() + "\"");
			sb.append("}");
			//往前台传输
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			response.getWriter().write(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(params = "scheduleMainTabs")
	public void scheduleMainTabs(HttpServletRequest request, HttpServletResponse response){
		try {
			//查出日程信息
			HttpSession session = ContextHolderUtils.getSession();
			Client client = ClientManager.getInstance().getClient(session.getId());
			UserEntity user = client.getUser();
			
			List<ScheduleEntity> scheduleList = scheduleService.queryList(user.getId());
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			
			//今天
			StringBuilder today = new StringBuilder();
			today.append("\"today\":[");
			
			//明天
			StringBuilder tomorrow = new StringBuilder();
			tomorrow.append("\"tomorrow\":[");
			
			//后天
			StringBuilder dayAfterTomorrow = new StringBuilder();
			dayAfterTomorrow.append("\"dayAfterTomorrow\":[");
			
			//后3天
			StringBuilder dayAfterThr = new StringBuilder();
			dayAfterThr.append("\"dayAfterThr\":[");
			
			//后4天
			StringBuilder dayAfterFor = new StringBuilder();
			dayAfterFor.append("\"dayAfterFor\":[");
			
			for(ScheduleEntity schedule : scheduleList){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
				SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
				String startTime = "";
				String endTime = "";
				if(schedule.getAllDay().equals("false")){
					startTime = formatTime.format(schedule.getStartDate());
					endTime = formatTime.format(schedule.getEndDate());
					
				}
				
				Date date = new Date(System.currentTimeMillis());
				String currentDate = format.format(date);
				String startDate = format.format(schedule.getStartDate());
				String endDate = format.format(schedule.getEndDate());
				//今天
				if(format.parse(startDate).getTime() <= format.parse(currentDate).getTime() && format.parse(currentDate).getTime() <= format.parse(endDate).getTime()){
					today.append("{\"id\":\"" + schedule.getId() + "\"");
					today.append(",\"title\":\"" + schedule.getTitle()+"\"");
					today.append(",\"dateTime\":\"" + currentDate + "\"" );
					today.append(",\"startTime\":\"" + startTime + "\"" );
					today.append(",\"endTime\":\""  + endTime + "\"");
					today.append(",\"allDay\":\""  + schedule.getAllDay() + "\"");
					today.append("},");
				}
				
				Date  tomorrowDate = StringUtil.getNextNDay(date, 1, "after");
				String strtomorrowDate = format.format(tomorrowDate);
				//明天
				if(format.parse(startDate).getTime() <= format.parse(strtomorrowDate).getTime() && format.parse(strtomorrowDate).getTime() <= format.parse(endDate).getTime()){
					tomorrow.append("{\"id\":\"" + schedule.getId() + "\"");
					tomorrow.append(",\"title\":\"" + schedule.getTitle()+"\"");
					tomorrow.append(",\"dateTime\":\"" + strtomorrowDate + "\"" );
					tomorrow.append(",\"startTime\":\"" + startTime + "\"" );
					tomorrow.append(",\"endTime\":\""  + endTime + "\"");
					tomorrow.append(",\"allDay\":\""  + schedule.getAllDay() + "\"");
					tomorrow.append("},");
				}
				
				//后天
				Date  dayAfterTomorrowDate = StringUtil.getNextNDay(date, 2, "after");
				String strdayAfterTomorrowDate = format.format(dayAfterTomorrowDate);
				if(format.parse(startDate).getTime() <= format.parse(strdayAfterTomorrowDate).getTime() && format.parse(strdayAfterTomorrowDate).getTime() <= format.parse(endDate).getTime()){
					dayAfterTomorrow.append("{\"id\":\"" + schedule.getId() + "\"");
					dayAfterTomorrow.append(",\"title\":\"" + schedule.getTitle()+"\"");
					dayAfterTomorrow.append(",\"dateTime\":\"" + strdayAfterTomorrowDate + "\"" );
					dayAfterTomorrow.append(",\"startTime\":\"" + startTime + "\"" );
					dayAfterTomorrow.append(",\"endTime\":\""  + endTime + "\"");
					dayAfterTomorrow.append(",\"allDay\":\""  + schedule.getAllDay() + "\"");
					dayAfterTomorrow.append("},");
				}
				
				//后3天
				Date  dayAfterThrDate = StringUtil.getNextNDay(date, 3, "after");
				String strdayAfterThrDate = format.format(dayAfterThrDate);
				if(format.parse(startDate).getTime() <= format.parse(strdayAfterThrDate).getTime() && format.parse(strdayAfterThrDate).getTime() <= format.parse(endDate).getTime()){
					dayAfterThr.append("{\"id\":\"" + schedule.getId() + "\"");
					dayAfterThr.append(",\"title\":\"" + schedule.getTitle()+"\"");
					dayAfterThr.append(",\"dateTime\":\"" + strdayAfterThrDate + "\"" );
					dayAfterThr.append(",\"startTime\":\"" + startTime + "\"" );
					dayAfterThr.append(",\"endTime\":\""  + endTime + "\"");
					dayAfterThr.append(",\"allDay\":\""  + schedule.getAllDay() + "\"");
					dayAfterThr.append("},");
				}
				
				//后4天
				Date dayAfterForDate = StringUtil.getNextNDay(date, 4, "after");
				String strdayAfterForDate = format.format(dayAfterForDate);
				if(format.parse(startDate).getTime() <= format.parse(strdayAfterForDate).getTime() && format.parse(strdayAfterForDate).getTime() <= format.parse(endDate).getTime()){
					dayAfterFor.append("{\"id\":\"" + schedule.getId() + "\"");
					dayAfterFor.append(",\"title\":\"" + schedule.getTitle()+"\"");
					dayAfterFor.append(",\"dateTime\":\"" + strdayAfterForDate + "\"" );
					dayAfterFor.append(",\"startTime\":\"" + startTime + "\"" );
					dayAfterFor.append(",\"endTime\":\""  + endTime + "\"");
					dayAfterFor.append(",\"allDay\":\""  + schedule.getAllDay() + "\"");
					dayAfterFor.append("},");
				}
			}
			if(today.indexOf(",") != -1)
				today.setLength(today.length()-1);
			today.append("],");
			
			if(tomorrow.indexOf(",") != -1)
				tomorrow.setLength(tomorrow.length()-1);
			tomorrow.append("],");
			
			if(dayAfterTomorrow.indexOf(",") != -1)
				dayAfterTomorrow.setLength(dayAfterTomorrow.length()-1);
			dayAfterTomorrow.append("],");
			
			if(dayAfterThr.indexOf(",") != -1)
				dayAfterThr.setLength(dayAfterThr.length()-1);
			dayAfterThr.append("],");
			
			if(dayAfterFor.indexOf(",") != -1)
				dayAfterFor.setLength(dayAfterFor.length()-1);
			dayAfterFor.append("]");
			
			sb.append(today.toString());
			sb.append(tomorrow.toString());
			sb.append(dayAfterTomorrow.toString());
			sb.append(dayAfterThr.toString());
			sb.append(dayAfterFor.toString());
			sb.append("}");
			
			//往前台传输
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			response.getWriter().write(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 查询日程事件
	 * @author luoheng
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "calendarEvents")
	public void calendarEvents(HttpServletRequest request, HttpServletResponse response){
		//查出日程信息
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		UserEntity user = client.getUser();
		
		List<ScheduleEntity> scheduleList = scheduleService.queryList(user.getId());
		
		//拼接前台所接收的事件JSON
		StringBuilder sb = new StringBuilder();
		sb.append("{\"eventinfo\":[");
		for(ScheduleEntity schedule : scheduleList){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(schedule.getAllDay().equals("true")){
				format = new SimpleDateFormat("yyyy-MM-dd");
			}
			sb.append("{\"id\":\"" + schedule.getId() + "\"");
			sb.append(",\"title\":\"" + schedule.getTitle()+"\"");
			sb.append(",\"start\":\"" + format.format(schedule.getStartDate()) + "\"" );
			sb.append(",\"end\":\""  + format.format(schedule.getEndDate()) + "\"");
			sb.append(",\"className\":\""  + schedule.getClassName() + "\"");
			sb.append(",\"allDay\":\""  + schedule.getAllDay() + "\"");
			sb.append("},");
		}
		if(sb.indexOf(",") != -1)
			sb.setLength(sb.length()-1);
		sb.append("]}");
		
		//往前台传输
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		try {
            response.getWriter().write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

		//return null;
	}
	
	/**
	 * 当日程时间发生改变时更新数据
	 * @author luoheng
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "eventResize")
	@ResponseBody
	public AjaxJson eventResize(HttpServletRequest request){
		String id = request.getParameter("id");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		try {
			if(StringUtil.isNotEmpty(id)){
				ScheduleEntity schedule = scheduleService.getScheduleEntity(id);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				schedule.setStartDate(format.parse(startDate));
				schedule.setEndDate(format.parse(endDate));
				scheduleService.update(schedule);
			}
			message="修改成功";
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
			result.setMsg(message);
		}
		return result;
	}
	
	/**
	 * 保存拖拽过来的常用事件
	 * @author luoheng
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "drop")
	@ResponseBody
	public AjaxJson drop(HttpServletRequest request){
		String id = request.getParameter("id");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String allDay = request.getParameter("allDay");
		try {
			if(StringUtil.isNotEmpty(id)){
				OftenScheduleEntity oftenSchedule = oftenScheduleService.getOftenScheduleEntity(id);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				ScheduleEntity schedule = new ScheduleEntity();
				schedule.setTitle(oftenSchedule.getTitle());
				schedule.setContext(oftenSchedule.getContext());
				schedule.setClassName(oftenSchedule.getColorStyle());
				schedule.setStartDate(format.parse(startDate));
				schedule.setUserId(oftenSchedule.getUserId());
				schedule.setEndDate(format.parse(endDate));
				schedule.setAllDay(allDay);
				scheduleService.save(schedule);
			}
			message="保存成功";
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
			result.setMsg(message);
		}
		return result;
	}
	
	public String getAfterDate(Date d){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  Date date = null;
		  Calendar calendar = Calendar.getInstance();  
		  calendar.setTime(d);  
		  calendar.add(Calendar.DAY_OF_MONTH,1);  
		  date = calendar.getTime();  
		  //format.format(date);
		  return format.format(date);
	}
	
	/**
	 * 跳转日程编辑页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(params = "scheduleEdit")
	public ModelAndView scheduleEdit(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		ScheduleEntity schedule = null;
		if(StringUtil.isNotEmpty(id)){
			schedule = scheduleService.getScheduleEntity(id);
		}else{
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				if(StringUtil.isNotEmpty(startDate) && StringUtil.isNotEmpty(endDate)){
					Date dateS = format.parse(startDate);
					Date dateE = format.parse(endDate);
					Timestamp timestampS = new Timestamp(dateS.getTime());
					Timestamp timestampE = new Timestamp(dateE.getTime());
					schedule = new ScheduleEntity();
					schedule.setStartDate(timestampS);
					schedule.setEndDate(timestampE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		model.addAttribute("schedule", schedule);
		return new ModelAndView("platform/work/schedule/scheduleEdit");
	}
	
	
	/**
	 * 跳转日程提醒列表页面
	 * @return
	 */
	@RequestMapping(params = "dateReminder")
	public ModelAndView reminder(HttpServletRequest request) {
		return new ModelAndView("platform/work/schedule/dateReminderList");
	}
	
	/**
	 * 查询日程提醒数据
	 * @return
	 */
	@RequestMapping(params = "dateReminderDatagrid")
	public void dateReminderDatagrid(HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		Map<String, Object> param = new HashMap<String, Object>();
		UserEntity user = ClientUtil.getUserEntity();
		param.put("userId", user.getId());
		param.put("messageType", "schedule");
		List<Map<String, Object>> list = this.reminderMybatisDao.queryDateReminderList(param);
		dataGrid.setResults(list);
		dataGrid.setTotal(list.size());
		TagUtil.datagrid(response, dataGrid);
	}

	public void setScheduleService(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	public void setOftenScheduleService(OftenScheduleService oftenScheduleService) {
		this.oftenScheduleService = oftenScheduleService;
	}

	public void setReminderMybatisDao(ReminderMybatisDao reminderMybatisDao) {
		this.reminderMybatisDao = reminderMybatisDao;
	}
	
}
