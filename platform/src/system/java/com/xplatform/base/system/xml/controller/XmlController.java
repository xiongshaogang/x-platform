package com.xplatform.base.system.xml.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.xml.XMLHelper;
import com.xplatform.base.system.xml.entity.XmlEntity;
import com.xplatform.base.system.xml.service.XmlService;

@Controller
@RequestMapping("/xmlController")
public class XmlController {

	@Resource
	private XmlService xmlService;

	private AjaxJson result = new AjaxJson();
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	@RequestMapping(params = "list")
	public ModelAndView report(HttpServletRequest request) {
		List<XmlEntity>  list = xmlService.queryList();
		request.setAttribute("xmlData", list.get(0).getXmldata());
		return new ModelAndView("web/xmlTest");
	}
	
	/**
	 * 新增或修改分类统计
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "saveXml")
	@ResponseBody
	public AjaxJson saveXml(HttpServletRequest request) {
		String xmldata = request.getParameter("xmlData");
		XmlEntity xml = new XmlEntity();
		xml.setXmldata(xmldata);
		this.xmlService.save(xml);
		message = "保存成功";
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "compare")
	public ModelAndView compare(HttpServletRequest request) {
		List<XmlEntity>  list = xmlService.queryList();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String newXml = list.get(3).getXmldata();
		String oldXml = list.get(1).getXmldata();
		// 参数 newXml oldXml
		resultMap = XMLHelper.compareXmlDiff(XMLHelper.parseDocument(newXml), XMLHelper.parseDocument(oldXml),
				null, null, true, true, null);
		request.setAttribute("request", resultMap);
		return new ModelAndView("web/xmlCompare");
	}
	
}
