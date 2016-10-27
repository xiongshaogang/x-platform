package com.xplatform.base.system.sysseting.controller;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.system.sysseting.entity.SysParameterEntity;
import com.xplatform.base.system.sysseting.service.SysParameterService;

/**   
 * @Title: Controller
 * @Description: 系统参数
 * @author onlineGenerator
 * @date 2014-05-19 17:01:31
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/sysParameterController")
public class SysParameterController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SysParameterController.class);

	@Resource
	private SysParameterService sysParameterService;
	
	
	public void setSysParameterService(SysParameterService sysParameterService) {
		this.sysParameterService = sysParameterService;
	}

	
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 系统参数列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "sysParameter")
	public ModelAndView sysParameter(HttpServletRequest request) {
		return new ModelAndView("platform/system/sysseting/sysParameterList");
	}
	
	@RequestMapping(params = "saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(SysParameterEntity sysParameterEntity, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if(StringUtil.isNotEmpty(sysParameterEntity.getId())){
			SysParameterEntity parameter = sysParameterService.getEntity(SysParameterEntity.class, sysParameterEntity.getId());
			if(parameter.getUpdateFlag().equals("N")){
				message = "该参数不能修改";
			}else{
				sysParameterService.update(sysParameterEntity);
				message = "系统参数修改成功";
			}
		}else{
			sysParameterService.save(sysParameterEntity);
			message = "系统参数添加成功";
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(SysParameterEntity sysParameter,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SysParameterEntity.class, dataGrid);
		//查询条件组装器
		//注释by lxt 20150507 start
		//sysParameter.setType("parameterType_user");
		//end
		HqlGenerateUtil.installHql(cq, sysParameter, request.getParameterMap());
		cq.add();
		this.sysParameterService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 批量删除系统参数
	 * 
	 * @return
	 */
	 @RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "系统参数删除成功";
		try{
			for(String id:ids.split(",")){
				sysParameterService.delete(id);
			}
		}catch(Exception e){
			message = "系统参数删除失败";
			ExceptionUtil.printStackTraceAndLogger(e);
		}
		j.setMsg(message);
		return j;
	}
	 
	/**
	 * 系统参数编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "editPage")
	public ModelAndView editPage(String id, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(id)) {
			SysParameterEntity sysParameter = sysParameterService.getEntity(SysParameterEntity.class, id);
			req.setAttribute("sysParameterPage", sysParameter);
		}
		return new ModelAndView("platform/system/sysseting/sysParameterEdit");
	}
}
