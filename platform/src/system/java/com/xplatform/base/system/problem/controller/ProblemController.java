package com.xplatform.base.system.problem.controller;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.platform.common.controller.CommonController;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.problem.entity.ProblemEntity;
import com.xplatform.base.system.problem.service.ProblemService;

/**
 * 
 * description : 问题反馈
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年10月20日 下午3:17:03
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * hexj        2014年10月20日 下午3:17:03
 *
 */

@Controller
@RequestMapping("/problemController")
public class ProblemController extends CommonController {
	
	private String message;
	
	@Resource
	private ProblemService problemService;
	
	@RequestMapping(params = "problem")
	public ModelAndView Problem(HttpServletRequest request){
		return new ModelAndView("platform/system/problem/problemList");
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "datagrid")
	public void datagrid(ProblemEntity problem, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid){
		CriteriaQuery cq = new CriteriaQuery(ProblemEntity.class, dataGrid);
		HqlGenerateUtil.installHql(cq, problem, request.getParameterMap());
		cq.add();
		problemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	@RequestMapping(params = "editPage")
	public ModelAndView editPage(ProblemEntity problem, Model model) {
		problem.setId(UUIDGenerator.generate());
		model.addAttribute("email", ClientUtil.getUserEntity().getEmail());
		model.addAttribute("problem", problem);
		model.addAttribute("problem", problem);
		return new ModelAndView("platform/system/problem/problemEdit");
	}
	
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(ProblemEntity problem, HttpServletRequest request) {
		String optFlag = request.getParameter("optFlag");
		AjaxJson result = new AjaxJson();
		try {
			problem.setProblemState("0");
			problemService.save(problem);
			message = "问题反馈成功,感谢您的支持！";
		} catch (BusinessException e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "detail")
	public ModelAndView detail(ProblemEntity problem, Model model) throws BusinessException{
		if(StringUtil.isNotEmpty(problem.getId())){
			problem = problemService.get(problem.getId());
			if("0".equals(problem.getProblemState())){
				problemService.updateState(problem.getId(), "1");
			}
			model.addAttribute("problem", problem);
		}
		return new ModelAndView("platform/system/problem/problemEdit");
	}


	@RequestMapping(params = "resolveProblemPage")
	public ModelAndView resolveProblemPage(ProblemEntity problem, Model model){
		problem = problemService.get(problem.getId());
		model.addAttribute("problem", problem);
		return new ModelAndView("platform/system/problem/resolveProblem");
	}
	
	@RequestMapping(params = "resolveProblem")
	@ResponseBody
	public AjaxJson resolveProblem(ProblemEntity problem, HttpServletRequest request){
		AjaxJson result = new AjaxJson();
		try {
			String resolveFlag = request.getParameter("resolveFlag");
			if("Y".equals(resolveFlag)){
				problem.setProblemState("2");
			}else{
				problem.setProblemState("1");
			}
			problem.setUpdateUserId(ClientUtil.getUserId());
			problem.setUpdateUserName(ClientUtil.getUsername());
			problemService.saveResolveProblem(problem);
			message = "问题解决方案保存成功！";
		} catch (BusinessException e) {
			message = "问题解决方案保存失败，请稍后再试！";
		}
		result.setMsg(message);
		return result;
	}
	
	
	
	
	public ProblemService getProblemService() {
		return problemService;
	}

	public void setProblemService(ProblemService problemService) {
		this.problemService = problemService;
	}

}
