package com.xplatform.base.document.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/documentController")
public class DocumentControler {
	/**
	 * 流程代理设置管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "document")
	public ModelAndView document(HttpServletRequest request) {
		return new ModelAndView("workflow/document/documentEdit");
	}
}
