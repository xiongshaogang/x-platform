package com.xplatform.base.form.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xplatform.base.form.entity.FlowInstanceUserEntity;
import com.xplatform.base.form.service.FlowInstanceUserService;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.StringUtil;

@Controller
@RequestMapping("/flowInstanceUserController")
public class FlowInstanceUserController extends BaseController {

	@Resource
	private FlowInstanceUserService flowInstanceUserService;
	
	@RequestMapping(params = "saveOrDeleteFIU")
	@ResponseBody
	public AjaxJson saveOrDeleteFIU(FlowInstanceUserEntity flowInstanceUser, HttpServletRequest request) throws BusinessException{
		AjaxJson result = new AjaxJson();
		
//		String userIds = request.getParameter("userIds");
//		String userNames = request.getParameter("userNames");
//		String businessKey  = request.getParameter("businessKey");
//		//String taskNodeId   = request.getParameter("taskNodeId");
//		
//		if(StringUtil.isNotEmpty(userIds) && StringUtil.isNotEmpty(userNames) && StringUtil.isNotEmpty(businessKey)){
//			result = flowInstanceUserService.saveOrDeleteFIU(userIds,userNames,businessKey);
//		}else{
//			result.setMsg("参数有误");
//			result.setSuccess(false);
//		}
		
		return result;
	}

	
	@RequestMapping(params = "queryFIUListByBus")
	@ResponseBody
	public AjaxJson queryFIUListByBus(HttpServletRequest request) throws BusinessException{
		AjaxJson result = new AjaxJson();
		
		String businessKey  = request.getParameter("businessKey");
		if(StringUtil.isNotEmpty(businessKey)){
			result.setObj(this.flowInstanceUserService.queryFIUListByBus(businessKey));
			result.setMsg("查询成功");
			result.setSuccess(true);
		}else{
			result.setMsg("参数有误");
			result.setSuccess(false);
		}
		
		return result;
	}
	
	
	
}
