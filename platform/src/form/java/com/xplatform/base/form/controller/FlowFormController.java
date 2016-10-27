package com.xplatform.base.form.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.xplatform.base.form.entity.AppForbiddenEntity;
import com.xplatform.base.form.entity.FlowFormEntity;
import com.xplatform.base.form.service.AppForbiddenService;
import com.xplatform.base.form.service.AppFormApproveUserService;
import com.xplatform.base.form.service.AppFormUserService;
import com.xplatform.base.form.service.FlowFormService;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.FileUtils;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.type.entity.TypeEntity;

/**
 * 
 * description : 流程表单管理controller
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
@RequestMapping("/flowFormController")
public class FlowFormController extends BaseController {
	
	@Resource
	private FlowFormService flowFormService;
	@Resource
	private AppFormUserService appFormUserService;
	@Resource
	private AppFormApproveUserService appFormApproveUserService;
	@Resource 
	private AppForbiddenService appForbiddenService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 流程表单管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "flowForm")
	public ModelAndView FlowForm(HttpServletRequest request) {
		return new ModelAndView("workflow/form/flowFormList");
	}
	
	/**
	 * 审批应用列表页面
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "flowFormApp")
	public ModelAndView flowFormApp(HttpServletRequest request) {
		return new ModelAndView("main/home/form/formAppHome");
	}
	
	@RequestMapping(params = "appForm")
	public ModelAndView appForm(HttpServletRequest request) {

		return new ModelAndView("main/home/form/formHome");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param FlowForm
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "datagrid")
	public void datagrid(FlowFormEntity FlowForm,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(FlowFormEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, FlowForm, request.getParameterMap());
		String flag = request.getParameter("flag");
		try{
		//自定义追加查询条件
			if(StringUtil.isEmpty(flag)){
				if(StringUtil.isNotEmpty(request.getParameter("typeId"))){
					cq.eq("type.id", request.getParameter("typeId"));
				}else{
					cq.eq("type.id", "-1");
				}
			}
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.flowFormService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 流程表单删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param FlowForm
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(FlowFormEntity FlowForm, HttpServletRequest request) {
		message = "流程表单删除成功";
		try{
			String ids = request.getParameter("ids");
			flowFormService.deleteEntityByIds(ids);
		}catch(Exception e){
			message = "流程表单删除失败";
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
	@RequestMapping(params = "flowFormEdit")
	public ModelAndView FlowFormEdit(FlowFormEntity flowForm,Model model) {
		if (StringUtil.isNotEmpty(flowForm.getId())) {
			flowForm = flowFormService.get(flowForm.getId());
			model.addAttribute("flowForm", flowForm);
		}
		return new ModelAndView("workflow/form/flowFormEdit");
	}
	
	/**
	 * 进入新增或者修改查看页面
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "templateEdit")
	public ModelAndView templateEdit(FlowFormEntity flowForm,Model model) {
		/*if (StringUtil.isNotEmpty(flowForm.getId())) {
			flowForm = flowFormService.get(flowForm.getId());
			model.addAttribute("flowForm", flowForm);
		}*/
		return new ModelAndView("workflow/form/template/templateEdit");
	}
	
	/**
	 * 新增或修改流程表单
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param FlowForm
	 * @return
	 */
	@RequestMapping(params = "saveFlowForm")
	@ResponseBody
	public AjaxJson saveFlowForm(FlowFormEntity FlowForm,HttpServletRequest request) {
		try {
			if (StringUtil.isNotEmpty(FlowForm.getId())) {
				message = "流程表单更新成功";
				flowFormService.update(FlowForm);
			} else {
				message = "流程表单新增成功";
				TypeEntity module=new TypeEntity();
				if(StringUtil.isNotEmpty(request.getParameter("typeId"))){
					module.setId(request.getParameter("typeId"));
					//FlowForm.setType(module);
					flowFormService.save(FlowForm);
				}else{
					message = "新增流程表单没有指定模块";
				}
			}
		} catch (BusinessException e) {
			// TODO: handle exception
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "flowFormContent")
	public ModelAndView flowFormContent(FlowFormEntity flowForm,Model model,HttpServletRequest request) {
		if (StringUtil.isNotEmpty(flowForm.getId())) {
			flowForm = flowFormService.get(flowForm.getId());
			String content=FileUtils.readFile(request.getRealPath("/")+"/webpage/"+flowForm.getUrl());
			model.addAttribute("content", content);
			model.addAttribute("flowForm", flowForm);
		}
		return new ModelAndView("workflow/form/flowFormContent");
	}
	
	@RequestMapping(params = "commonFormEdit")
	public ModelAndView commonFormEdit(FlowFormEntity flowForm,Model model,HttpServletRequest request) {
		
		return new ModelAndView("main/home/form/commonFormEdit");
	}
	
	
	public void setFlowFormService(FlowFormService FlowFormService) {
		this.flowFormService = FlowFormService;
	}
	
	/**
	 * 新增或修改流程表单
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param FlowForm
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(params = "saveOrUpdate")
	public ModelAndView saveOrUpdate(FlowFormEntity FlowForm, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//得到json字符串
		String mytest = request.getParameter("jsonStr").toString();
        //String mytest = "{'FlowFormEntity':{'id':'','status':0,'name':'西瓜测试','description':'','parentId':'-1','logo':'fa fa-apple icon','isEdit':1,'version':0},'AppFormField':{'fields':[{'label':'a1','field_type':'text','required':true,'isProveEdit':false,'isShow':true,'isTitle':true,'field_options':{'size':'small'},'cid':'c2'},{'label':'a2','field_type':'paragraph','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{'size':'small'},'cid':'c6'},{'label':'a3','field_type':'checkboxes','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{'options':[{'label':'','checked':false},{'label':'','checked':false}]},'cid':'c10'},{'label':'a4','field_type':'radio','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{'options':[{'label':'','checked':false},{'label':'','checked':false}]},'cid':'c14'},{'label':'a5','field_type':'date','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c20'},{'label':'a6','field_type':'dropdown','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{'options':[{'label':'','checked':false},{'label':'','checked':false}],'include_blank_option':false},'cid':'c24'},{'label':'a7','field_type':'time','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c28'},{'label':'a8','field_type':'number','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c32'},{'label':'a9','field_type':'website','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c36'},{'label':'a10','field_type':'email','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c44'},{'label':'a11','field_type':'price','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c48'},{'label':'a12','field_type':'address','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c52'},{'label':'未定义','field_type':'detail','required':true,'isProveEdit':false,'isShow':false,'isTitle':false,'field_options':{},'cid':'c56'},{'label':'b1','field_type':'text','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{'size':'small'},'cid':'c60','pcid':'c56'},{'label':'b2','field_type':'paragraph','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{'size':'small'},'cid':'c64','pcid':'c56'},{'label':'b3','field_type':'checkboxes','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{'options':[{'label':'','checked':false},{'label':'','checked':false}]},'cid':'c68','pcid':'c56'},{'label':'b4','field_type':'radio','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{'options':[{'label':'','checked':false},{'label':'','checked':false}]},'cid':'c72','pcid':'c56'},{'label':'b5','field_type':'date','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c76','pcid':'c56'},{'label':'b6','field_type':'dropdown','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{'options':[{'label':'','checked':false},{'label':'','checked':false}],'include_blank_option':false},'cid':'c80','pcid':'c56'},{'label':'b7','field_type':'time','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c84','pcid':'c56'},{'label':'b8','field_type':'number','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c88','pcid':'c56'},{'label':'b9','field_type':'website','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c92','pcid':'c56'},{'label':'b10','field_type':'email','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c96','pcid':'c56'},{'label':'b11','field_type':'price','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c100','pcid':'c56'},{'label':'b12','field_type':'address','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c104','pcid':'c56'},{'label':'未定义','field_type':'detail','required':true,'isProveEdit':false,'isShow':false,'isTitle':false,'field_options':{},'cid':'c108'},{'label':'c1','field_type':'text','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{'size':'small'},'cid':'c112','pcid':'c108'},{'label':'c2','field_type':'paragraph','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{'size':'small'},'cid':'c116','pcid':'c108'},{'label':'c3','field_type':'checkboxes','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{'options':[{'label':'','checked':false},{'label':'','checked':false}]},'cid':'c120','pcid':'c108'},{'label':'c4','field_type':'radio','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{'options':[{'label':'','checked':false},{'label':'','checked':false}]},'cid':'c124','pcid':'c108'},{'label':'c5','field_type':'date','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c128','pcid':'c108'},{'label':'c6','field_type':'dropdown','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{'options':[{'label':'','checked':false},{'label':'','checked':false}],'include_blank_option':false},'cid':'c132','pcid':'c108'},{'label':'c7','field_type':'time','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c140','pcid':'c108'},{'label':'c8','field_type':'number','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c144','pcid':'c108'},{'label':'c9','field_type':'website','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c148','pcid':'c108'},{'label':'c10','field_type':'email','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c152','pcid':'c108'},{'label':'c11','field_type':'price','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c156','pcid':'c108'},{'label':'c12','field_type':'address','required':true,'isProveEdit':false,'isShow':true,'isTitle':false,'field_options':{},'cid':'c160','pcid':'c108'}]},'AppFormUser':{'userList':[{'userId':'f898202150650a55015065102b1a0000'},{'userId':'f898202150650a550150698b6e560006'},{'userId':'f898202150caf28e0150cb02aa710007'}]},'AppFormUserData':{'userList':[]},'AppFormApproveUser':{'userList':[{'userId':'f898202150650a55015065102b1a0000','userName':'刘宇健','orderby':1},{'userId':'f898202150650a550150698b6e560006','userName':'隔壁的老王','orderby':2},{'userId':'f898202150caf28e0150cb02aa710007','userName':'离歌笑','orderby':3}]}}";
        //------------------------存储原始json王轶用--------------------------
		String jsonStr = mytest;
		String content=mytest;
		JSONObject jsonStrObject = JSONObject.fromObject(jsonStr);
		jsonStr = jsonStrObject.getString("AppFormField");
		//------------------------存储原始json王轶用--------------------------
		String stringIsDeploy = request.getParameter("isDeploy");
		Integer isDeploy=0;
		if("1".equals(stringIsDeploy)){
			isDeploy=1;
		}
/*		String mytest = "{"
						+"'FlowFormEntity': {'id': '','name': 'test1','description': 'miaoshu666','parentId': '-1','logo': 'iughkhgjhkgj', 'isEdit': 0,'status':1,'version':1},"
						+"'AppFormField': {'fields':[{'label':'测试1','field_type':'checkboxes','required':true,'field_options':{'options':[{'label':'123123','checked':false},{'label':'23534645','checked':false},{'label':'657456745','checked':false}]},'cid':'c6'},{'label':'测试2','field_type':'text','required':true,'field_options':{'size':'small'},'cid':'c10'},{'label':'测试3','field_type':'dropdown','required':true,'field_options':{'options':[{'label':'retyewrt','checked':true},{'label':'sdfsdf','checked':false}],'include_blank_option':true},'cid':'c14'}]},"
						+"'AppFormUser': {'userList': [{'userId': '111111'}, {'userId': '2222222'}]},"
						+"'AppFormUserData': {'userList': [{'userId': '111111'}, {'userId': '2222222'}]},"
						+"'AppFormApproveUser': {'userList': [{'userId': '1111111', 'userName': 'aaaaa', 'orderby': 1}, {'userId': '1111111', 'userName': 'aaaaa', 'orderby': 2}, {'userId': '1111111', 'userName': 'aaaaa', 'orderby': 3}]}"
						+"}";*/
		//String mytest = "{'AppFormField':{'fields':[{'field_type':'text','field_options':{'size':'small'},'label':'姓名','required':true,'cid':'c18'},{'field_type':'detail','field_options':{},'label':'明细密码','required':true,'cid':'c2','fields':[{'field_type':'text','field_options':{'size':'small'},'pcid':'c2','label':'密码','required':true,'cid':'c6'}]},{'field_type':'detail','field_options':{},'label':'明细邮箱','required':true,'cid':'c10','fields':[{'field_type':'text','field_options':{'size':'small'},'pcid':'c10','label':'邮箱','required':true,'cid':'c14'}]}]},'FlowFormEntity':{'id':'','logo':'fa fa-apple icon','parentId':'-1','isEdit':1,'status':0,'description':'','name':'两个明细','version':0}}";
		//老谢的逻辑
		Map<String,Object> param= JSONHelper.parseJSON2Map(mytest);
		Map<String,Object> appFormField=(Map<String, Object>) param.get("AppFormField");
		List<Map<String,Object>> fieldList=(List<Map<String,Object>>)appFormField.get("fields");
		List<Map<String,Object>> varList=new ArrayList<Map<String,Object>>();
		for(Map<String,Object> field:fieldList){
			if(field.get("pcid")!=null){
				varList.add(field);
			}
		}
		for(Map<String,Object> var:varList){
			fieldList.remove(var);
		}
		for(Map<String,Object> var1:varList){
			for(Map<String,Object> field1:fieldList){
				if(StringUtil.equals((String)field1.get("cid"), (String)var1.get("pcid").toString())){
					if(field1.get("fields")==null){
						List<Map<String,Object>> p=new ArrayList<Map<String,Object>>();
						p.add(var1);
						field1.put("fields", p);
					}else{
						List<Map<String,Object>> p=(List<Map<String, Object>>)field1.get("fields");
						p.add(var1);
					}
				}
			}
		}
		
		mytest = JSONHelper.map2json(param);
		
		/*JSONObject jsonObject = JSONObject.fromObject(mytest);
		//List<Map> fields=(List<Map>)jsonObject.get("AppFormField");
		JSONObject mapObject = (JSONObject)jsonObject.get("AppFormField");
		JSONArray jsonArray = JSONArray.fromObject(mapObject.get("fields"));
		for(){
			
		}*/
		JSONObject jsonObject = JSONObject.fromObject(mytest);
		JSONObject apuJsonObject = (JSONObject)jsonObject.get("FlowFormEntity");
		String formId="";
		//if(StringUtil.isEmpty(apuJsonObject.getString("id"))){
		Map<String,String> attr=new HashMap<String,String>();
		if(StringUtil.equals("1", request.getParameter("isDeploy"))){//如果是发布
			//发布方法
			message = "流程表单发布成功";
			formId=flowFormService.updateFlowForm(jsonObject,isDeploy,jsonStr,content);
			JSONObject resultObject = new JSONObject();
			resultObject.element("success", true);
			resultObject.accumulate("msg", message);
			resultObject.accumulate("type", "deploy");
			response.getWriter().print(resultObject);
			
			/*FlowFormEntity flowForm = new FlowFormEntity();
			flowForm.setId(formId);
			flowForm.setName(apuJsonObject.getString("name"));
			flowForm.setDescription(apuJsonObject.getString("description"));*/
			/*if(StringUtil.isNotEmpty(apuJsonObject.optString("parentId"))){
				flowForm.setParentId(apuJsonObject.getString("parentId"));
			}else{
				flowForm.setParentId("-1");
			}
			flowForm.setIsEdit(apuJsonObject.getInt("isEdit"));*/
			//判断不走流程
			JSONObject fFJsonObject = (JSONObject)jsonObject.get("FlowFormEntity");
			if(fFJsonObject.getInt("isStartAssign") == 0){
				JSONObject afauJsonObject = (JSONObject)jsonObject.get("AppFormApproveUser");
				if(afauJsonObject == null){
					return null;
				}else{
					JSONArray jsonArray = JSONArray.fromObject(afauJsonObject.get("userList"));
					if(jsonArray.size() == 0){
						return null;
					}
				}
			}
			attr.put("bpmDefinition.subject", apuJsonObject.getString("name"));
			attr.put("formId", formId);
		}else{
			JSONObject resultObject = new JSONObject();
			if(StringUtil.isEmpty(apuJsonObject.getString("id"))){
				//新增方法
				message = "流程表单新增成功";
				formId = this.flowFormService.saveFlowForm(jsonObject,isDeploy,jsonStr,content);
				resultObject.accumulate("type", "save");
				resultObject.accumulate("formId", formId);
			}else{
				//修改方法
				message = "流程表单更新成功";
				formId=flowFormService.updateFlowForm(jsonObject,isDeploy,jsonStr,content);
				resultObject.accumulate("type", "update");
				resultObject.accumulate("formId", formId);
			}

			resultObject.element("success", true);
			resultObject.accumulate("msg", message);
			response.getWriter().print(resultObject);
			return null;
		}
		return new ModelAndView(new RedirectView("definitionController.do?saveAutoDef"), attr);
	    //return new RedirectView("definitionController.do?saveAutoDef", true, false, false);
	}
	
	@RequestMapping(params = "shareUse")
	public ModelAndView shareUse(FlowFormEntity FlowForm,HttpServletRequest request,HttpServletResponse response){
		
		JSONObject resultObject = new JSONObject();
		resultObject.element("success", true);
		try {
			String formId = request.getParameter("formId");
			
			FlowFormEntity parent=this.flowFormService.get(formId);
			if(parent==null || StringUtil.isEmpty(parent.getContent())){
				resultObject.element("success", false);
				resultObject.accumulate("msg", "分享失败");
				try {
					response.getWriter().print(resultObject);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return null;
			}
			
			List<FlowFormEntity> children=this.flowFormService.queryFlowFormByParentId(formId);
			if(!(children!=null && children.size()>0)){
				children=new ArrayList<FlowFormEntity>();
			}
			children.add(0, parent);
			for(FlowFormEntity flow:children){
				//得到json字符串
				String mytest = flow.getContent();
		        //------------------------存储原始json王轶用--------------------------
				String jsonStr = mytest;
				String content=mytest;
				JSONObject jsonStrObject = JSONObject.fromObject(jsonStr);
				jsonStr = jsonStrObject.getString("AppFormField");
				Map<String,Object> param= JSONHelper.parseJSON2Map(mytest);
				Map<String,Object> appFormField=(Map<String, Object>) param.get("AppFormField");
				List<Map<String,Object>> fieldList=(List<Map<String,Object>>)appFormField.get("fields");
				List<Map<String,Object>> varList=new ArrayList<Map<String,Object>>();
				for(Map<String,Object> field:fieldList){
					if(field.get("pcid")!=null){
						varList.add(field);
					}
				}
				for(Map<String,Object> var:varList){
					fieldList.remove(var);
				}
				for(Map<String,Object> var1:varList){
					for(Map<String,Object> field1:fieldList){
						if(StringUtil.equals((String)field1.get("cid"), (String)var1.get("pcid").toString())){
							if(field1.get("fields")==null){
								List<Map<String,Object>> p=new ArrayList<Map<String,Object>>();
								p.add(var1);
								field1.put("fields", p);
							}else{
								List<Map<String,Object>> p=(List<Map<String, Object>>)field1.get("fields");
								p.add(var1);
							}
						}
					}
				}
				
				mytest = JSONHelper.map2json(param);
				
				JSONObject jsonObject = JSONObject.fromObject(mytest);
				JSONObject apuJsonObject = (JSONObject)jsonObject.get("FlowFormEntity");
				
				
				Map<String,String> attr=new HashMap<String,String>();
				//发布方法
				message = "流程表单发布成功";
				
				jsonObject.accumulate("saveUserId", ClientUtil.getUserId());
				jsonObject.accumulate("saveUserName", ClientUtil.getUsername());
				formId=flowFormService.updateFlowForm(jsonObject,1,jsonStr,content);
				
				resultObject.accumulate("msg", message);
				resultObject.accumulate("type", "deploy");
				response.getWriter().print(resultObject);
				
				//判断不走流程
				JSONObject fFJsonObject = (JSONObject)jsonObject.get("FlowFormEntity");
				if(fFJsonObject.getInt("isStartAssign") == 0){
					JSONObject afauJsonObject = (JSONObject)jsonObject.get("AppFormApproveUser");
					if(afauJsonObject == null){
						return null;
					}else{
						JSONArray jsonArray = JSONArray.fromObject(afauJsonObject.get("userList"));
						if(jsonArray.size() == 0){
							return null;
						}
					}
				}
				attr.put("bpmDefinition.subject", apuJsonObject.getString("name"));
				attr.put("formId", formId);
				//流程定义保存
				return new ModelAndView(new RedirectView("definitionController.do?saveAutoDef"), attr);
			}
			return null;
		} catch (Exception e) {
			// TODO: handle exception
			resultObject.element("success", false);
			resultObject.accumulate("msg", "分享失败");
			resultObject.accumulate("type", "deploy");
			try {
				response.getWriter().print(resultObject);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}
		
	}
	
	
	/**
	 * 根据id获取表单
	 * @author lixt
	 * @createtime 2015年11月06日 下午03:19:16
	 * @param FlowForm
	 * @return
	 */
	@RequestMapping(params = "getFlowForm")
	@ResponseBody
	public AjaxJson getFlowForm(HttpServletRequest request) throws BusinessException{
		String formId = request.getParameter("formId");
		String formCode = request.getParameter("formCode");
		int status = 0;
		result.setObj(this.flowFormService.getFlowForm(formId,formCode, status));
		result.setSuccess(true);
		return result;
	}
	
	@RequestMapping(params = "deleteFlowForm")
	@ResponseBody
	public AjaxJson deleteFlowForm(HttpServletRequest request) throws BusinessException{
		
		String formId = request.getParameter("formId");
		if(StringUtil.isNotEmpty(formId)){
			FlowFormEntity flowFormEntity = this.flowFormService.get(formId);
			if(flowFormEntity != null){
				flowFormEntity.setStatus(2);
				this.flowFormService.update(flowFormEntity);
				result.setSuccess(true);
				result.setMsg("删除成功");
			}else{
				result.setSuccess(false);
				result.setMsg("删除失败，模板不存在");
			}
		}else{
			result.setSuccess(false);
			result.setMsg("删除失败，formId不能为空");
		}
		
		
		
		//result.setObj(this.flowFormService.getFlowForm(formId,formCode, status));
		result.setSuccess(true);
		return result;
	}
	
	/**
	 * 获取表单列表
	 * @author lixt
	 * @createtime 2015年11月06日 下午1:19:16
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "queryMyCreateFlowFormList")
	@ResponseBody
	public AjaxJson queryMyCreateFlowFormList(HttpServletRequest request) throws BusinessException{
		String orgId = ClientUtil.getUserEntity().getOrgIds();
		String userId = ClientUtil.getUserId();
		if(StringUtil.isNotEmpty(userId)){
			result.setObj(this.flowFormService.queryMyCreateFlowFormList(userId,orgId));
			result.setSuccess(true);
		}else{
			result.setObj(null);
			result.setSuccess(false);
			result.setMsg("请登录");
		}
		return result;
	}
	
	/**
	 * 根据formId获取关联表单列表
	 * @author lixt
	 * @createtime 2015年11月17日 下午1:19:16
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "queryRelaFlFoList")
	@ResponseBody
	public AjaxJson queryRelaFlFoList(HttpServletRequest request) throws BusinessException{
		String formId = request.getParameter("formId");
		String formCode = request.getParameter("formCode");
		if(StringUtil.isNotEmpty(formCode)){
			FlowFormEntity flowFormEntity = this.flowFormService.getFlowFormByCode(formCode);
			formId = flowFormEntity.getId();
		}
		if(StringUtil.isNotEmpty(formId)){
			FlowFormEntity flowFormEntity = this.flowFormService.get(formId);
			List<FlowFormEntity> queryRelaFlFoList = this.flowFormService.queryRelaFlFoList(formId,flowFormEntity.getCode());
			
			result.setObj(queryRelaFlFoList);
			result.setSuccess(true);
		}else{
			result.setObj(null);
			result.setSuccess(false);
			result.setMsg("formId为空");
		}
		return result;
	}
	
	/*private List<AppFormApproveUser> deploy(FlowFormEntity flowForm,Map<String,String> attr){
		StringBuffer sb=new StringBuffer();
		String start="<diagram xmlns:bg=\"bpm.graphic\" xmlns:ciied=\"com.ibm.ilog.elixir.diagram\" xmlns:fg=\"flash.geom\">"
						  +"<bg:StartEvent id=\"startEvent1\" height=\"49\" width=\"31\" x=\"350\" y=\"20\">"
						    +"<label>开始</label>"
						    +"<ports>"
						      +"<ciied:Port id=\"port1\" y=\"1\"/>"
						    +"</ports>"
						  +"</bg:StartEvent>";
		sb.append(start);
		List<AppFormApproveUser> userList=appFormApproveUserService.findByProperty("formId", flowForm.getId());
		if(!(userList!=null && userList.size()>0)){
			return null;
		}
		int j=0;
		for(int i=0;i<userList.size();i++){
			j++;
			AppFormApproveUser user=userList.get(i);
			user.setTaskId("task"+j);
			String task="<bg:SequenceFlow id=\"sequenceFlow"+j+"\" endPort=\"port"+(j*2)+"\" startPort=\"port"+(j*2-1)+"\"></bg:SequenceFlow>"
					  +"<bg:Task id=\"task"+j+"\" height=\"50\" user=\"true\" width=\"90\" x=\"321.5\" y=\""+(20+j*80)+"\">"
					  +"<label>"+user.getUserName()+"</label>"
					    +"<ports>"
					      +"<ciied:Port id=\"port"+(j*2)+"\" y=\"0\"/>"
					      +"<ciied:Port id=\"port"+(j*2+1)+"\" y=\"1\"/>"
					      +"</ports>"
					  +"</bg:Task>";
			sb.append(task);
		}
		String end="<bg:SequenceFlow id=\"sequenceFlow"+(j+1)+"\" endPort=\"port"+((j+1)*2)+"\" startPort=\"port"+((j+1)*2-1)+"\"></bg:SequenceFlow>"
					  +"<bg:EndEvent id=\"endEvent1\" height=\"49\" width=\"34\" x=\"349.5\" y=\""+(20+(j+1)*80)+"\">"
					  +"<label>结束1</label>"
					    +"<ports>"
					      +"<ciied:Port id=\"port"+((j+1)*2)+"\" y=\"0\"/>"
					      +"</ports>"
					  +"</bg:EndEvent>"
					+"</diagram>";
		sb.append(end);
		attr.put("bpmDefinition.subject", flowForm.getName());
		attr.put("formId", flowForm.getId());
		attr.put("bpmDefinition.typeId", ConfigConst.WorkflowDefaultTypeId);
		//attr.put("userList", JSONHelper.toJSONString(userList));
		attr.put("bpmDefinition.defXml", sb.toString());
		return userList;
		
	}*/
	
	/**
	 * 跳转到我的模板页面
	 * @author lixt
	 * @createtime 2015年11月06日 下午02:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "flowFormList")
	public ModelAndView flowFormList(HttpServletRequest request) {
		return new ModelAndView("main/home/form/myModules");
	}
	
	/**
	 * 获取所有的cid
	 * @author lixt
	 * @createtime 2015年11月06日 下午02:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "getAllCids")
	@ResponseBody
	public AjaxJson getAllCids(HttpServletRequest request) throws Exception{
		AjaxJson result = new AjaxJson();
		String formCode = request.getParameter("formCode");
		if(StringUtil.isNotEmpty(formCode)){
			result.setObj(this.flowFormService.getCidList(formCode));
			result.setMsg("获取cid成功");
			result.setSuccess(true);
		}else{
			result.setObj(null);
			result.setMsg("获取cid失败，formCode为空");
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 获取所有的模板
	 * @author lixt
	 * @createtime 2015年11月06日 下午02:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "queryAllMyFlowFormList")
	@ResponseBody
	public AjaxJson queryAllFlowFormList(HttpServletRequest request) throws Exception{
		AjaxJson result = new AjaxJson();
		
		String formId = request.getParameter("formId");
		
		if (StringUtil.isNotEmpty(formId)) {
			FlowFormEntity flowForm = this.flowFormService.get(formId);
			String userId = ClientUtil.getUserId();
			if (StringUtil.isNotEmpty(userId)) {
				result.setObj(this.flowFormService.queryMyFlowFormList(userId,flowForm.getCode()));
				result.setMsg("获取模板成功");
				result.setSuccess(true);
			} else {
				result.setMsg("获取模板成功失败");
				result.setSuccess(false);
			}
		}else{
			String userId = ClientUtil.getUserId();
			String orgId = ClientUtil.getUserEntity().getOrgIds();
			if (StringUtil.isNotEmpty(userId)) {
				result.setObj(this.flowFormService.queryMyFlowFormList1(userId,orgId));
				result.setMsg("获取模板成功");
				result.setSuccess(true);
			} else {
				result.setMsg("获取模板成功失败");
				result.setSuccess(false);
			}
		}

		return result;
	}
	
	/**
	 * 获取所有的当前用户所能看到的应用
	 * @author lixt
	 * @createtime 2015年11月06日 下午02:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "queryAPPList")
	@ResponseBody
	public AjaxJson queryAPPList(HttpServletRequest request) throws Exception{
		AjaxJson result = new AjaxJson();
		
		String userId = ClientUtil.getUserId();
		String orgId = ClientUtil.getUserEntity().getOrgIds();
		if(StringUtil.isNotEmpty(userId)){
			result.setObj(this.flowFormService.queryAPPList(userId,orgId));
			result.setMsg("获取应用");
			result.setSuccess(true);
		}else{
			result.setObj(null);
			result.setMsg("获取应用失败");
			result.setSuccess(false);
		}
		return result;
	}
	
	/**
	 * 获取当前用户所能看到的所有的可用禁用的应用
	 * @author lixt
	 * @createtime 2015年11月06日 下午02:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "queryAPPAllList")
	@ResponseBody
	public AjaxJson queryAPPAllList(HttpServletRequest request) throws Exception{
		AjaxJson result = new AjaxJson();

		String userId = ClientUtil.getUserId();
		String orgId = ClientUtil.getUserEntity().getOrgIds();
		if(StringUtil.isNotEmpty(userId)){
			result.setObj(this.flowFormService.queryAPPAllList(userId,orgId));
			result.setMsg("获取应用");
			result.setSuccess(true);
		}else{
			result.setObj(null);
			result.setMsg("获取应用失败");
			result.setSuccess(false);
		}
		return result;
	}
	
	/**
	 * 禁看某个应用
	 * @author lixt
	 * @createtime 2015年11月06日 下午02:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "forbiddenOrStartApp")
	@ResponseBody
	public AjaxJson forbiddenApp(HttpServletRequest request) throws Exception{
		AjaxJson result = new AjaxJson();
		String formId = request.getParameter("formId");
		String formCode = request.getParameter("formCode");
		String userId = ClientUtil.getUserId();
		String orgId = ClientUtil.getUserEntity().getOrgIds();
		String status = "";
		if(StringUtil.isNotEmpty(userId)){
			AppForbiddenEntity appForbidden =  this.appForbiddenService.getAppForbidden(formCode, userId, orgId);
			if(appForbidden == null){
				AppForbiddenEntity appForbiddenEntity =  new AppForbiddenEntity();
				appForbiddenEntity.setOrgId(orgId);
				appForbiddenEntity.setUserId(userId);
				appForbiddenEntity.setFormId(formId);
				appForbiddenEntity.setFormCode(formCode);
				this.appForbiddenService.save(appForbiddenEntity);
				result.setMsg("操作成功");
				result.setSuccess(true);
				result.setStatus(status);
			}else{
				this.appForbiddenService.deleteAppForbidden(formCode, userId, orgId);
				result.setMsg("操作成功");
				result.setSuccess(true);
			}

		}else{
			result.setObj(null);
			result.setMsg("操作失败");
			result.setSuccess(false);
		}
		return result;
	}
	
	/**
	 * 获取当前机构下所有能看到的应用
	 * @author lixt
	 * @createtime 2015年11月06日 下午02:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "queryOrgAppList")
	@ResponseBody
	public AjaxJson queryOrgAppList(HttpServletRequest request) throws Exception{
		AjaxJson result = new AjaxJson();
		
		String orgId = request.getParameter("orgId");
		if(StringUtil.isNotEmpty(orgId)){
			result.setObj(this.flowFormService.queryOrgAppList(orgId));
			result.setMsg("获取应用");
			result.setSuccess(true);
		}else{
			result.setObj(null);
			result.setMsg("orgId不能为空");
			result.setSuccess(false);
		}
		return result;
	}
	
	/**
	 * 我的申请
	 * @author lixt
	 * @createtime 2015年11月06日 下午02:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "myApplication")
	@ResponseBody
	public AjaxJson myApplication(HttpServletRequest request) throws Exception{
		AjaxJson result = new AjaxJson();
		
		String pageStr = request.getParameter("page");
		int page = 1;
		if(StringUtil.isNotEmpty(pageStr)){
			page = Integer.parseInt(pageStr);
		}
		String rowsStr = request.getParameter("rows");
		int rows = 10;
		if(StringUtil.isNotEmpty(rowsStr)){
			rows = Integer.parseInt(rowsStr);
		}
		String userId = ClientUtil.getUserId();
		if(StringUtil.isNotEmpty(userId)){
			result.setObj(this.flowFormService.myApplication(userId,page,rows));
			result.setMsg("获取数据成功");
			result.setSuccess(true);
		}else{
			result.setMsg("获取数据失败");
			result.setSuccess(false);
		}
		return result;
	}
	
	/**
	 * 跳转到我的申请页面
	 * @author lixt
	 * @createtime 2015年11月06日 下午02:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "application")
	public ModelAndView application(HttpServletRequest request) {
		return new ModelAndView("main/home/form/myApplication");
	}
	
	/**
	 * 跳转到代办页面
	 * @author lixt
	 * @createtime 2015年11月06日 下午02:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "commission")
	public ModelAndView commission(HttpServletRequest request) {
		return new ModelAndView("main/home/form/commission");
	}
	
	/**
	 * 跳转到我的app管理列表页
	 * @author lixt
	 * @createtime 2015年11月06日 下午02:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "appList")
	public ModelAndView appList(HttpServletRequest request) {
		return new ModelAndView("main/home/form/myAppList");
	}
	
	
	/**
	 * 我的申请
	 * @author lixt
	 * @createtime 2015年11月06日 下午02:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "getFieldJson")
	@ResponseBody
	public AjaxJson getFieldJson(HttpServletRequest request) throws Exception{
		AjaxJson result = new AjaxJson();
		Map<String,Object> jsonAndUser = new HashMap<String,Object>();
		String formId = request.getParameter("formId");
		if(StringUtil.isNotEmpty(formId)){
			FlowFormEntity flowForm = this.flowFormService.get(formId);
			if(flowForm != null){
				String fieldJson = flowForm.getFieldJsonTemp();
				Map<String,Object> param= JSONHelper.parseJSON2Map(fieldJson);
				//Map<String,Object> appFormField=(Map<String, Object>) param.get("AppFormField");
				List<Map<String,Object>> fieldList=(List<Map<String,Object>>)param.get("fields");
				List<Map<String,Object>> varList=new ArrayList<Map<String,Object>>();
				for(Map<String,Object> field:fieldList){
					if(field.get("pcid") != null){
						varList.add(field);
					}else if("dropdown".equals(field.get("field_type"))){
						varList.add(field);
					}else if("price".equals(field.get("field_type"))){
						varList.add(field);
					}else if("address".equals(field.get("field_type"))){
						varList.add(field);
					}else if("detail".equals(field.get("field_type"))){
						varList.add(field);
					}else if("selectuser".equals(field.get("field_type"))){
						varList.add(field);
					}else if("file".equals(field.get("field_type"))){
						varList.add(field);
					}else{
						if(field.get("isConnectionField") != null){
							field.remove("isConnectionField");
							field.put("isConnectionField", true);
						}else{
							field.put("isConnectionField", true);
						}
						field.remove("isShow");
						field.put("isShow", false);
						field.remove("isTitle");
						field.put("isTitle", false);
					}
				}
				for(Map<String,Object> var:varList){
					fieldList.remove(var);
				}
				JSONObject jsonStrObject = JSONObject.fromObject(param);
				jsonAndUser.put("fieldJson", jsonStrObject);
				List<Map<String,Object>> appFormUserList = this.appFormUserService.queryAppFormList(formId);
				jsonAndUser.put("AppFormUser", appFormUserList);
				result.setObj(jsonAndUser);
				result.setMsg("获取成功");
				result.setSuccess(true);
			}else{
				result.setMsg("formId有误");
				result.setSuccess(false);
			}
		}else{
			result.setMsg("formId不能为空");
			result.setSuccess(false);
		}
		
		return result;
	}
	
	@RequestMapping(params = "appFormDesigner")
	public ModelAndView appFormDesigner(HttpServletRequest request) {
		String formId=request.getParameter("formId");
		request.setAttribute("formId", formId);
		return new ModelAndView("main/home/form/appFormDesigner");
	}
	
	@RequestMapping(params = "getLogo")
	@ResponseBody
	public AjaxJson getLogo( HttpServletRequest request,HttpServletResponse response) throws Exception {
		AjaxJson result = new AjaxJson();
		String logo = request.getParameter("logo");
		if (jodd.util.StringUtil.isNotEmpty(logo)) {
			//FileUtils.outviewImg(request, response, fis);
		        try {
		        	String finalPath = ApplicationContextUtil.getRealPath("/basic/img/logo/"+logo+".png");
		    		File file = new File(finalPath);
		    		if(file != null){
		    			FileUtils.downLoadFile(request, response, file.getAbsolutePath(), file.getName());
		    		}

		        }catch (Exception e) {  
		            e.printStackTrace();  
		        }
		} else {
			String finalPath = ApplicationContextUtil.getRealPath("/basic/img/logo");
			File file = new File(finalPath);
			String test[];
			if(file.list().length > 0){
				test = file.list();
				for (int i = 0; i < test.length; i++) {
					test[i] = test[i].substring(0, test[i].length() - 4);
				}
				result.setObj(test);
			}
			
		}
		return result;
	}
	
	
	/**
	 * 点击某个应用时进入的H5页面
	 * @author xiaqiang
	 * @createtime 2015年11月18日14:37:18
	 * @param request
	 * @return
	 */
//	@RequestMapping(params = "flowFormEnterPage")
//	public ModelAndView flowFormEnterPage(HttpServletRequest request) {
//
//		String code = request.getParameter("code");
//		String tableName = request.getParameter("tableName");
//		String id = request.getParameter("id");
//
//		FlowFormEntity flowForm = flowFormService.findUniqueByProperty("code", code);
//		if (flowForm.getIsEdit().equals(0)) {
//			// 进入列表页
//			String formId = request.getParameter("formId");
//			String userId = ClientUtil.getUserId();
//			if (StringUtil.isNotEmpty(userId)) {
//				Map<String, Object> fieldMap = this.appFormTableService.getFieldData(formId);
//			}
//			return new ModelAndView("main/home/form/fieldData");
//		} else if (flowForm.getIsEdit().equals(1)) {
//			// 进入编辑页
//			return new ModelAndView("main/home/form/commonFormEdit");
//		}
//		
//		request.setAttribute("id", id);
//		return new ModelAndView("main/home/form/commonFormEdit");
//	}
}
