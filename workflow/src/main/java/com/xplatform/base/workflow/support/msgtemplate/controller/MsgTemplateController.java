package com.xplatform.base.workflow.support.msgtemplate.controller;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;
import com.xplatform.base.workflow.support.msgtemplate.service.MsgTemplateService;

/**
 * 
 * description : 信息模版管理controller
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
@RequestMapping("/msgTemplateController")
public class MsgTemplateController extends BaseController {
	
	@Resource
	private MsgTemplateService msgTemplateService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 信息模版管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "msgTemplate")
	public ModelAndView MsgTemplate(HttpServletRequest request) {
		return new ModelAndView("workflow/support/msgTemplate/msgTemplateList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param MsgTemplate
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(MsgTemplateEntity MsgTemplate,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(MsgTemplateEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, MsgTemplate, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.msgTemplateService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 信息模版删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param MsgTemplate
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(MsgTemplateEntity MsgTemplate, HttpServletRequest request) {
		message = "信息模版删除成功";
		try{
			msgTemplateService.delete(MsgTemplate.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除信息模版
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
			msgTemplateService.batchDelete(ids);
			message="删除成功";
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 进入新增或者修改查看页面
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "msgTemplateEdit")
	public ModelAndView MsgTemplateEdit(MsgTemplateEntity msgTemplate,Model model) {
		if (StringUtil.isNotEmpty(msgTemplate.getId())) {
			msgTemplate = msgTemplateService.get(msgTemplate.getId());
			model.addAttribute("msgTemplate", msgTemplate);
		}
		return new ModelAndView("workflow/support/msgTemplate/msgTemplateEdit");
	}
	
	/**
	 * 新增或修改信息模版
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param MsgTemplate
	 * @return
	 */
	@RequestMapping(params = "saveMsgTemplate")
	@ResponseBody
	public AjaxJson saveMsgTemplate(MsgTemplateEntity msgTemplate) {
		try {
			if(StringUtil.equals(msgTemplate.getIsDefault(), "Y")){
				msgTemplateService.updateDefault(msgTemplate.getUseType());
			}
			if (StringUtil.isNotEmpty(msgTemplate.getId())) {
				message = "信息模版更新成功";
				msgTemplateService.update(msgTemplate);
			} else {
				message = "信息模版新增成功";
				msgTemplateService.save(msgTemplate);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	

	public void setMsgTemplateService(MsgTemplateService msgTemplateService) {
		this.msgTemplateService = msgTemplateService;
	}
	
	
}
