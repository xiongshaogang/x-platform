package com.xplatform.base.work.concernpage.controller;

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
@RequestMapping("/concernPageController")
public class ConcernPageController extends BaseController {

	@RequestMapping(params = "concernPage")
	public ModelAndView concernPage(HttpServletRequest request) {
		return new ModelAndView("main/userConcerned");
	}
}
