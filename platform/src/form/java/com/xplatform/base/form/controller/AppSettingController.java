package com.xplatform.base.form.controller;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;

/**
 * 
 * description : 字典类型管理controller
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
@RequestMapping("/appSettingController")
public class AppSettingController extends BaseController {
	
	
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 字典类型管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "appSetting")
	public ModelAndView appSetting(HttpServletRequest request) {
		return new ModelAndView("main/home/setting/appSettingHome");
	}

	

	
	
}
