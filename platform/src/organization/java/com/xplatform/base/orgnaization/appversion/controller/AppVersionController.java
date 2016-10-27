package com.xplatform.base.orgnaization.appversion.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.FTPUtil;
import com.xplatform.base.framework.core.util.FileUtils;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.appversion.dao.AppVersionDao;
import com.xplatform.base.orgnaization.appversion.entity.AppVersionEntity;
import com.xplatform.base.orgnaization.appversion.service.AppVersionService;
import com.xplatform.base.system.attachment.entity.AttachEntity;
import com.xplatform.base.system.attachment.entity.FTPAttachEntity;
import com.xplatform.base.system.attachment.service.AttachService;

@Controller
@RequestMapping("/appVersonController")
public class AppVersionController extends BaseController{
	
	@Resource
	private AppVersionDao appVersonDao;
	@Resource
	private AppVersionService appVersonService;
	@Resource
	private AttachService attachService;
	
    private String message;
    
	/**
	 * 跳转轮播图片页面
	 * @author lxt
	 * @createtime 2015年7月13日 下午2:32:45
	 * @Decription
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "listApp")
	public ModelAndView listPic( AppVersionEntity app,HttpServletRequest request) throws Exception{
				
		AppVersionEntity appVersonEntity = new AppVersionEntity();
		appVersonEntity.setId(UUIDGenerator.generate());
		
		request.setAttribute("version", appVersonEntity);
		
		return new ModelAndView("platform/organization/appverson/appVersonList");
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
	public void datagrid(AppVersionEntity app,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(AppVersionEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, app, request.getParameterMap());
		
		cq.add();
		
		DataGridReturn dgReturn=this.appVersonService.getDataGridReturn(cq, true);
		
		TagUtil.datagrid(response, dataGrid);
	}
	
	@RequestMapping(params = "editPage")
	public ModelAndView editPage(AppVersionEntity appVersonEntity, HttpServletRequest req) throws Exception {
		AppVersionEntity appVerson = null;

		if (StringUtil.isNotEmpty(appVersonEntity.getId())) {
			appVerson = this.appVersonDao.getEntity(appVersonEntity.getId());
		}else{
			appVerson = new AppVersionEntity();
			appVerson.setId(UUIDGenerator.generate());
		}
		
		List<AppVersionEntity> appVersonEntities = this.appVersonService.queryAppVersonList();
		if (!appVersonEntities.isEmpty()) {
			req.setAttribute("oversion", appVersonEntities.get(0));
		}
		req.setAttribute("version", appVerson);
		return new ModelAndView("platform/organization/appverson/appVersonEdit");
	}
	
	
	@RequestMapping(params = "saveApp")
	@ResponseBody
	public AjaxJson saveApp(AppVersionEntity appVersonEntity,HttpServletRequest request) throws Exception {
		String message = "上传成功";
		AjaxJson result = new AjaxJson();
		
		this.appVersonService.saveAppVersion(appVersonEntity);
		result.setMsg(message);
		return result;
		
	}
	
	@RequestMapping(params = "getNewApp")
	@ResponseBody
	public AjaxJson getNewApp(HttpServletRequest request) throws Exception {
		AjaxJson result = new AjaxJson();
		String versionNumber = request.getParameter("versionNumber");
		String typeStr = request.getParameter("type");
		int type;
		if(StringUtil.isEmpty(versionNumber)){
			versionNumber = "1.0";
		}
		if(StringUtil.isEmpty(typeStr)){
			type = 0;
		}else{
			type = Integer.parseInt(typeStr);
		}
		List<AppVersionEntity> AppVersionEntityList =  this.appVersonService.getNewApp(versionNumber,type);
		if(AppVersionEntityList.size() >0){
			result.setObj(AppVersionEntityList.get(0));
			result.setMsg("获取app成功");
			result.setSuccess(true);
		}else{
			result.setMsg("暂无最新app");
			result.setSuccess(true);
		}
		
		return result;
		
	}
	
	@RequestMapping(params = "downloadAppFile")
	public void downloadAppFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String typeStr = request.getParameter("type");
		int type;
		if(StringUtil.isEmpty(typeStr)){
			type = 0;
		}else{
			type = Integer.parseInt(typeStr);
		}
		AppVersionEntity appVersionEntity = this.appVersonService.getLatestApp(type);
		if (appVersionEntity != null) {
			String attachmentId = appVersionEntity.getAttachMentId();
			AttachEntity attachEntity = attachService.get(attachmentId);
			if (attachEntity == null) {
				return;
			}
			try {
				if (StringUtil.isNotEmpty(attachEntity.getStorageType())) {
					if ("0".equals(attachEntity.getStorageType())) {
						FileUtils.downLoadFile(request, response, attachEntity.getAbsolutePath(), attachEntity.getAttachName());
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
