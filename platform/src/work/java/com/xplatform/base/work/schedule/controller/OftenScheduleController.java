package com.xplatform.base.work.schedule.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
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
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.work.schedule.entity.OftenScheduleEntity;
import com.xplatform.base.work.schedule.service.OftenScheduleService;

@Controller
@RequestMapping("/oftenScheduleController")
public class OftenScheduleController extends BaseController {

	@Resource
	private OftenScheduleService oftenScheduleService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;
	
	/**
	 * 常用事件列表
	 * @author luoheng
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "oftenScheduleList")
	public void oftenScheduleList(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		UserEntity user = client.getUser();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", user.getId());
		List<OftenScheduleEntity> oftenScheduleList = oftenScheduleService.queryList(map);
		
		//拼接前台所接收的事件JSON
		StringBuilder sb = new StringBuilder();
		sb.append("{\"data\":[");
		//sb.append("[");
		for(OftenScheduleEntity oftenSchedule : oftenScheduleList){
			sb.append("{\"eventName\":\""+ oftenSchedule.getTitle() +"\",\"eventColor\":\""+ oftenSchedule.getColorStyle() +"\",\"id\":\"" + 
					oftenSchedule.getId() +"\",\"context\":\""+ oftenSchedule.getContext() +"\",\"success\":true},");
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
	}
	
	/**
	 * 保存、修改常用日程数据
	 * @param request
	 * @param schedule
	 * @return
	 */
	@RequestMapping(params = "saveOrUpdate")
	@ResponseBody
	public void saveOrUpdate(HttpServletRequest request, OftenScheduleEntity oftenSchedule, HttpServletResponse response){
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		UserEntity user = client.getUser();
		oftenSchedule.setUserId(user.getId());
		try {
			//获取前台输入的值
			String id = URLDecoder.decode(request.getParameter("id"), "utf-8");
			String title = URLDecoder.decode(request.getParameter("title"), "utf-8");
			String context = URLDecoder.decode(request.getParameter("context"), "utf-8");
			String colorStyle = URLDecoder.decode(request.getParameter("colorStyle"), "utf-8");
			
			oftenSchedule.setId(id);
			oftenSchedule.setTitle(title);
			oftenSchedule.setColorStyle(colorStyle);
			oftenSchedule.setContext(context);
			if(StringUtil.isNotEmpty(oftenSchedule.getId())){
				//修改日程信息
				oftenScheduleService.update(oftenSchedule);
				message="修改成功";
			}else{
				//保存日程信息
				id = oftenScheduleService.save(oftenSchedule);
				
				message="保存成功";
			}
			StringBuilder sb = new StringBuilder();
			sb.append("{\"id\":\"" + id  + "\"");
			sb.append(",\"title\":\"" + oftenSchedule.getTitle()+"\"");
			sb.append(",\"context\":\""  + oftenSchedule.getContext() + "\"");
			sb.append(",\"colorStyle\":\""  + oftenSchedule.getColorStyle() + "\"");
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
	 * 删除常用日程数据
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request) {
		String ids=request.getParameter("ids");
		try {
			if(StringUtil.isNotEmpty(ids)){
				oftenScheduleService.batchDelete(ids);
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
		OftenScheduleEntity oftenSchedule = oftenScheduleService.getOftenScheduleEntity(id);
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("{\"id\":\"" + oftenSchedule.getId() + "\"");
			sb.append(",\"title\":\"" + oftenSchedule.getTitle()+"\"");
			sb.append(",\"context\":\""  + oftenSchedule.getContext() + "\"");
			sb.append(",\"colorStyle\":\""  + oftenSchedule.getColorStyle() + "\"");
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
	 * 跳转日程编辑页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(params = "oftenScheduleEdit")
	public ModelAndView oftenScheduleEdit(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		if(StringUtil.isNotEmpty(id)){
			OftenScheduleEntity oftenSchedule = oftenScheduleService.getOftenScheduleEntity(id);
			model.addAttribute("oftenSchedule", oftenSchedule);
		}
		return new ModelAndView("platform/work/schedule/oftenScheduleEdit");
	}

	public void setOftenScheduleService(OftenScheduleService oftenScheduleService) {
		this.oftenScheduleService = oftenScheduleService;
	}
}
