package com.xplatform.base.system.areas.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.system.areas.entity.AreasEntity;
import com.xplatform.base.system.areas.service.AreasService;

@Controller
@RequestMapping("/areasController")
public class AreasController {

	@Resource
	private AreasService areasService;

	/**
	 * 查询各级区域数据
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryAreas")
	@ResponseBody
	public AjaxJson queryAreas(HttpServletRequest request) throws Exception {
		AjaxJson result = new AjaxJson();
		String parentId = request.getParameter("parentId");
		if (StringUtil.isEmpty(parentId)) {
			parentId = "0";// 若传入的parentId为空,则是请求省份的数据
		}
		List<AreasEntity> areas = areasService.queryAreasByParent(parentId);
		result.setObj(areas);
		return result;
	}
}
