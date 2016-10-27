package com.xplatform.base.form.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.form.entity.AppFormField;
import com.xplatform.base.form.entity.AppFormTable;
import com.xplatform.base.form.entity.FlowFormEntity;
import com.xplatform.base.form.service.AppFormFieldService;
import com.xplatform.base.form.service.AppFormTableService;
import com.xplatform.base.form.service.AppFormUserDataService;
import com.xplatform.base.form.service.FlowFormService;
import com.xplatform.base.form.service.FlowInstanceUserService;
import com.xplatform.base.form.service.util.FormGenerateUtils;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.framework.core.util.pdf.HttpUtils;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupService;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.attachment.mybatis.vo.AttachVo;
import com.xplatform.base.system.attachment.service.AttachService;
import com.xplatform.base.system.message.config.service.MessageService;

/**
 * 自定义表请求处理类
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/appFormTableController")
public class AppFormTableController extends BaseController {

	@Resource
	private AppFormTableService appFormTableService;
	@Resource
	private AppFormUserDataService appFormUserDataService;
	@Resource
	private AppFormFieldService appFormFieldService;
	@Resource
	private FlowFormService flowFormService;
	@Resource
	private AttachService attachService;
	@Resource
	private OrgGroupService orgGroupService;
	@Resource
	private FlowInstanceUserService flowInstanceUserService;
	@Resource
	private MessageService messageService;
	@Resource
	private SysUserService sysUserService;

	private AjaxJson result = new AjaxJson();
	private String message;

	/**
	 * 字典类型管理列表页跳转
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "appForm")
	public ModelAndView appForm(HttpServletRequest request) {
		return new ModelAndView("main/home/form/formHome");
	}

	/**
	 * 单表hibernate组装表格数据
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param AppForm
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(AppFormTable appForm, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(AppFormTable.class, dataGrid);
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, appForm, request.getParameterMap());
		cq.add();
		this.appFormTableService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 通用删除方法
	 * 
	 * @createtime 2015年11月16日10:01:55
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "deleteData")
	@ResponseBody
	public AjaxJson deleteData(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		String tableName = request.getParameter("tableName");
		boolean flag = appFormTableService.deleteData(tableName, id);
		if (flag) {
			result.setSuccess(true);
			result.setMsg("删除成功");
		} else {
			result.setSuccess(false);
			result.setMsg("删除失败");
		}
		return result;
	}

	/**
	 * 进入新增或者修改查看页面
	 * 
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "appFormEdit")
	public ModelAndView appFormEdit(AppFormTable appForm, Model model) {
		if (StringUtil.isNotEmpty(appForm.getId())) {
			appForm = appFormTableService.get(appForm.getId());
			model.addAttribute("AppForm", appForm);
		}
		return new ModelAndView("platform/system/dict/AppFormEdit");
	}

	/**
	 * 新增或修改字典类型
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param AppForm
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "saveAppForm")
	@ResponseBody
	public AjaxJson saveAppForm(AppFormTable appForm) throws BusinessException {
		if (StringUtil.isNotEmpty(appForm.getId())) {
			message = "字典类型更新成功";
			try {
				appFormTableService.update(appForm, appForm.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "字典类型新增成功";
			appFormTableService.save(appForm);
		}
		result.setMsg(message);
		return result;
	}

	@RequestMapping(params = "test")
	public void test() throws Exception {

		FlowFormEntity flowForm = new FlowFormEntity();
		flowForm.setCode("test1");
		// this.appFormTableService.doGenerateTable(flowForm);
	}

	// @RequestMapping(params = "goTest")
	// public void goTest(HttpServletRequest request, HttpServletResponse
	// response) {
	// long start = System.currentTimeMillis();
	// String tableName = request.getParameter("tableName");
	//
	// StringWriter stringWriter = new StringWriter();
	// BufferedWriter writer = new BufferedWriter(stringWriter);
	// Map<String, Object> data = new HashMap<String, Object>();
	// String id = request.getParameter("id");
	// // 获取版本号
	// String version =
	// cgFormFieldService.getCgFormVersionByTableName(tableName);
	// // 装载表单配置
	// Map configData = cgFormFieldService.getFtlFormConfig(tableName, version);
	// data = new HashMap(configData);
	// // 如果该表是主表查出关联的附表
	// CgFormHeadEntity head = (CgFormHeadEntity) data.get("head");
	// Map<String, Object> dataForm = new HashMap<String, Object>();
	// if (StringUtils.isNotEmpty(id)) {
	// dataForm = dataBaseService.findOneForJdbc(tableName, id);
	// }
	// Iterator it = dataForm.entrySet().iterator();
	// while (it.hasNext()) {
	// Map.Entry entry = (Map.Entry) it.next();
	// String ok = (String) entry.getKey();
	// Object ov = entry.getValue();
	// data.put(ok, ov);
	// }
	// Map<String, Object> tableData = new HashMap<String, Object>();
	// // 获取主表或单表表单数据
	// // tableData.put(tableName, dataForm);
	// // 获取附表表表单数据
	// // if(StringUtil.isNotEmpty(id)){
	// // if(head.getJformType()==CgAutoListConstant.JFORM_TYPE_MAIN_TALBE){
	// // String subTableStr = head.getSubTableStr();
	// // if(StringUtils.isNotEmpty(subTableStr)){
	// // String [] subTables = subTableStr.split(",");
	// // List<Map<String,Object>> subTableData = new
	// // ArrayList<Map<String,Object>>();
	// // for(String subTable:subTables){
	// // subTableData =
	// // cgFormFieldService.getSubTableData(tableName,subTable,id);
	// // tableData.put(subTable, subTableData);
	// // }
	// // }
	// // }
	// // }
	// // 装载单表/(主表和附表)表单数据
	// data.put("data", tableData);
	// data.put("id", id);
	// // 装载附件信息数据
	// // pushFiles(data,id);
	// String content = FreemarkerHelper.parseTemplate("form.ftl", null);
	// response.setContentType("text/html;charset=utf-8");
	// response.getWriter().print(content);
	// long end = System.currentTimeMillis();
	// logger.debug("自定义表单生成耗时：" + (end - start) + " ms");
	//
	// // return new ModelAndView("main/home/form/test");
	// }

	@RequestMapping(params = "saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(HttpServletRequest request) throws Exception {
		String viewType = request.getParameter("viewType");
		String assignResult = request.getParameter("assignResult");

		AjaxJson j = new AjaxJson();
		Map data = request.getParameterMap();
		if (data != null) {
			data = FormGenerateUtils.mapConvert(data);
			Integer isApp = Integer.parseInt(data.get("isApp").toString());
			String mainTableName = (String) data.get("mainTableName");
			String mainFormTableName =  (String) data.get("mainFormTableName");
			String parentBusinessKey =  (String) data.get("parentBusinessKey");
			String mainId = (String) data.get("id");
			Integer mainTableType = Integer.parseInt(data.get("mainTableType").toString());
			// 如果是关联模板保存
			if (isApp == 0) {
				List<AppFormField> connectionFields = appFormTableService.queryConnectionFields(mainTableName);
				if (connectionFields.size() > 0) {
					// 表明有主模板关联字段,先进行关联字段的更新
					appFormTableService.updateTable(mainFormTableName, parentBusinessKey, data);
				}
			}
			if (BusinessConst.TableType_main.equals(mainTableType)) {
				// 说明是主从保存
				Map<String, List<Map<String, Object>>> mapMore = FormGenerateUtils.mapConvertMore(data, mainTableName);
				if (BusinessConst.ViewType_add.equals(viewType)) {
					// 新增逻辑
					Object key = appFormTableService.insertTableMore(mainTableName, mapMore);
					// 如果候选人数据不为空,则进行候选人表处理
					if (StringUtil.isNotEmpty(assignResult)) {
						flowInstanceUserService.saveOrDeleteFIU(assignResult, key.toString());
					}
					if (key != null) {
						j.setSuccess(true);
						j.setMsg("添加成功");
					} else {
						j.setSuccess(false);
						j.setMsg("添加失败");
					}
				} else {
					// 更新逻辑
					boolean flag = appFormTableService.updateTableMore(mainTableName, mapMore);
					if (flag) {
						j.setSuccess(true);
						j.setMsg("修改成功");
					} else {
						j.setSuccess(false);
						j.setMsg("修改失败");
					}
				}
			} else if (BusinessConst.TableType_single.equals(mainTableType)) {
				// 说明是单表保存
				if (BusinessConst.ViewType_add.equals(viewType)) {
					// 新增逻辑
					Object key = appFormTableService.insertTable(mainTableName, data);
					// 如果候选人数据不为空,则进行候选人表处理
					if (StringUtil.isNotEmpty(assignResult)) {
						flowInstanceUserService.saveOrDeleteFIU(assignResult, key.toString());
					}
					if (key != null) {
						j.setSuccess(true);
						j.setMsg("添加成功");
					} else {
						j.setSuccess(false);
						j.setMsg("添加失败");
					}
				} else {
					// 更新逻辑
					boolean flag = appFormTableService.updateTable(mainTableName, mainId, data);
					if (flag) {
						j.setSuccess(true);
						j.setMsg("添加成功");
					} else {
						j.setSuccess(false);
						j.setMsg("添加失败");
					}
				}
			}
		}
		return j;
	}

	/**
	 * 根据id获取当前表单所产生的数据
	 * 
	 * @author lixt
	 * @createtime 2015年11月06日 下午03:19:16
	 * @param FlowForm
	 * @return
	 */
	@RequestMapping(params = "getFieldData")
	@ResponseBody
	public AjaxJson getFieldData(HttpServletRequest request) throws Exception {
		// String formId = request.getParameter("formId");
		String formCode = request.getParameter("formCode");
		int page = Integer.parseInt(request.getParameter("page"));
		int rows = Integer.parseInt(request.getParameter("rows"));
		String userId = ClientUtil.getUserId();
		if (StringUtil.isNotEmpty(userId)) {
			// AppFormUserData appFormUserData =
			// this.appFormUserDataService.getUserData(formId, userId);
			result.setObj(this.appFormTableService.getFieldData(formCode, userId, page, rows));
			result.setSuccess(true);
		} else {
			result.setObj(null);
			result.setSuccess(false);
			result.setMsg("用户未登陆，请登录");
		}
		return result;
	}

	/**
	 * 根据id获取当前表单所产生的数据
	 * 
	 * @author lixt
	 * @createtime 2015年11月06日 下午03:19:16
	 * @param FlowForm
	 * @return
	 */
	@RequestMapping(params = "getOneFieldData")
	@ResponseBody
	public AjaxJson getOneFieldData(HttpServletRequest request) throws Exception {
		// String formId = request.getParameter("formId");
		String formCode = request.getParameter("formCode");
		String businessKey = request.getParameter("businessKey");
		String userId = ClientUtil.getUserId();
		if (StringUtil.isNotEmpty(userId)) {
			// AppFormUserData appFormUserData =
			// this.appFormUserDataService.getUserData(formId, userId);
			result.setObj(this.appFormTableService.getOneFieldData(formCode, businessKey));
			result.setSuccess(true);
		} else {
			result.setObj(null);
			result.setSuccess(false);
			result.setMsg("用户未登陆，请登录");
		}
		return result;
	}

	@RequestMapping(params = "getTableDataSum")
	@ResponseBody
	public AjaxJson getTableDataSum(HttpServletRequest request) throws Exception {
		// String formId = request.getParameter("formId");
		String formCode = request.getParameter("formCode");
		int pageCount = 0;
		String userId = ClientUtil.getUserId();
		if (StringUtil.isNotEmpty(userId)) {
			List<Map<String, Object>> list = this.appFormTableService.getTableDataSum(formCode, userId);
			int count = ((Long) list.get(0).get("COUNT")).intValue();
			if (count > 0) {
				if (count % ConfigConst.pageRows == 0) {
					pageCount = count / ConfigConst.pageRows;
				} else {
					pageCount = count / ConfigConst.pageRows + 1;
				}
				list.get(0).put("pageCount", pageCount);
				result.setObj(list.get(0));
				result.setSuccess(true);
			} else {
				list.get(0).put("pageCount", 0);
				result.setObj(list.get(0));
				result.setSuccess(true);
			}
		} else {
			result.setObj(null);
			result.setSuccess(false);
			result.setMsg("用户未登陆，请登录");
		}
		return result;
	}

	/**
	 * 
	 * @param appForm
	 * @param model
	 * @return
	 */

	@RequestMapping(params = "fieldData")
	public ModelAndView fieldData(HttpServletRequest request) throws Exception {
		String formId = request.getParameter("formId");
		String formCode = request.getParameter("formCode");
		String pageStr = request.getParameter("page");
		if (StringUtil.isEmpty(pageStr)) {
			pageStr = "1";
		}
		int page = Integer.parseInt(pageStr);

		String rowsStr = request.getParameter("rows");
		if (StringUtil.isEmpty(rowsStr)) {
			rowsStr = "10";
		}
		int rows = Integer.parseInt(rowsStr);
		String userId = ClientUtil.getUserId();
		if (StringUtil.isNotEmpty(userId)) {
			// AppFormUserData appFormUserData =
			// this.appFormUserDataService.getUserData(formId, userId);
			Map<String, Object> fieldMap = this.appFormTableService.getFieldData(formCode, userId, page, rows);
			String fieldData = JSONHelper.map2json(fieldMap);
			request.setAttribute("fieldData", fieldData);
			request.setAttribute("formCode", formCode);
			FlowFormEntity flowFormEntity = this.flowFormService.getFlowFormByCode(formCode);
			if (flowFormEntity != null) {
				request.setAttribute("isFlow", flowFormEntity.getIsFlow());
				request.setAttribute("notifyType", flowFormEntity.getNotifyType());
				request.setAttribute("viewType", flowFormEntity.getViewType());
				request.setAttribute("formId", formId);
			}
		}
		return new ModelAndView("main/home/form/fieldData");
	}

	/**
	 * 跳转通用编辑页方法
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 *
	 */
	@RequestMapping(params = "commonFormEdit2")
	public ModelAndView commonFormEdit2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String businessKey = request.getParameter("businessKey");// 流程跳转传输来的表单主键
		String businessType = request.getParameter("businessType");// 流程跳转传输来的表单主键
		String businessExtra = request.getParameter("businessExtra");// 流程跳转传输来的表单主键
		String viewType = request.getParameter("viewType");// 页面类型
		String formCode = request.getParameter("formCode");// flowForm的code
		String taskId = request.getParameter("taskId");// flowForm的code

		/** 1.获得已上传的附件 */
		String tableTemp = "t_auto_m,t_auto_a,t_auto_b";

		for (String tableName : tableTemp.split(",")) {
			// businessKey=
			businessType = tableName;
			// businessExtra=;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("businessKey", businessKey);
		param.put("businessType", businessType);
		param.put("businessExtra", businessExtra);
		List<AttachVo> dataVoList = attachService.queryUploadAttach(param);

		request.setAttribute("dataVoList", dataVoList);
		return new ModelAndView("main/home/form/commonFormEdit2");
	}

	/**
	 * 跳转通用编辑页方法
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 *
	 */
	@RequestMapping(params = "commonFormEdit")
	public void commonFormEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String businessKey = request.getParameter("businessKey");// 流程跳转传输来的表单主键
		String viewType = request.getParameter("viewType");// 页面类型
		String formCode = request.getParameter("formCode");// flowForm的code
		String taskId = request.getParameter("taskId");// flowForm的code
		String parentBusinessKey = request.getParameter("parentBusinessKey");
		
		//TODO 设置消息为已读 等待验证
		messageService.doReadMessageReceiveBySource(businessKey, formCode);
		/************* 初始化参数区域 *************/
		Map<String, Object> data = new HashMap<String, Object>();// 总数据源
		FlowFormEntity flowForm = flowFormService.queryLastestVersionFlowForm(formCode);//最新版本的FlowForm
		// 主表table数据
		AppFormTable mainTable = appFormTableService.queryMainTable(formCode);
		String buttonType = "";
		Integer notifyType = flowForm.getNotifyType();// 是否需要传阅标识
		Integer isSharefolder = flowForm.getIsSharefolder();// 是否需要发布到云盘
		Integer flowStatus = 0;//1.运行中 2.完成 3.人工终止
		Boolean hasRelationTemplate = ("1".equals(flowForm.getIsLeaf()) && flowForm.getIsApp().equals(1)) ? false : true;//是否有关联模板
		String mainFormTableName = ConfigConst.tablePrefix + flowForm.getMainFormCode();//主模板表名
		List<Map<String, String>> circulateList = new ArrayList<Map<String, String>>();//已传阅人的集合
		if (flowForm.getIsFlow().equals(0)) {
			// 不是流程表单
			if (BusinessConst.ViewType_add.equals(viewType)) {
				businessKey = UUIDGenerator.generate();
				if (notifyType == 1) {
					// 传阅非流程表单的新增页,编辑+显示发送按钮
					buttonType = BusinessConst.ButtonType_saveAndSend;
				}else{
					// 新增页面
					buttonType = BusinessConst.ButtonType_save;
				}
			} else if (BusinessConst.ViewType_update.equals(viewType)) {
				if (notifyType == 1) {
					// 传阅非流程表单,只读+显示已传阅人+无按钮
					circulateList = appFormTableService.queryCirculateReceivers(businessKey);
					viewType = BusinessConst.ViewType_detail; // 改成只读页面
					buttonType = BusinessConst.ButtonType_view;
				} else {
					// 编辑页面
					buttonType = BusinessConst.ButtonType_save;
				}
				if (hasRelationTemplate) {
					List<Map<String, Object>> childrenProcessInstanceList = flowFormService.findForJdbc(
							"select business_key businessKey,def_name defName,formCode formCode from t_flow_instance t where t.parent_id=?", businessKey);
					data.put("childrenProcessInstanceList", childrenProcessInstanceList);
					viewType = BusinessConst.ViewType_detail; // 改成只读页面
					buttonType = BusinessConst.ButtonType_view;
				}
				
			} else if (BusinessConst.ViewType_detail.equals(viewType)) {
				if (notifyType == 1) {
					// 传阅非流程表单,只读+显示已传阅人+无按钮
					circulateList = appFormTableService.queryCirculateReceivers(businessKey);
				}
				viewType = BusinessConst.ViewType_detail; // 改成只读页面
				buttonType = BusinessConst.ButtonType_view;
			} else {
				if (notifyType == 1) {
					// 传阅非流程表单,只读+显示已传阅人+无按钮
					circulateList = appFormTableService.queryCirculateReceivers(businessKey);
					viewType = BusinessConst.ViewType_detail; // 改成只读页面
					buttonType = BusinessConst.ButtonType_view;
				} else {
					// 编辑页面
					viewType = BusinessConst.ViewType_update;
					buttonType = BusinessConst.ButtonType_save;
				}
			}
		} else {
			// 是流程表单
			if (BusinessConst.ViewType_add.equals(viewType)) {
				businessKey = UUIDGenerator.generate();
				data.put("businessName", "测试流程");
				// 流程发起
				List<Map<String, Object>> list = flowFormService.findForJdbc("select * from t_flow_node_set t where t.set_type='2' and t.form_id=?",
						flowForm.getId());
				if (list != null && list.size() > 0) {
					data.put("actDefId", list.get(0).get("act_id"));
				}
				buttonType = BusinessConst.ButtonType_startFlow;
			} else if (BusinessConst.ViewType_nextProcess.equals(viewType)) {
				// 待办页面
				buttonType = BusinessConst.ButtonType_nextProcess;

			} else if (BusinessConst.ViewType_viewProcess.equals(viewType)) {
				// 已办查看(或传阅查看)
				buttonType = BusinessConst.ButtonType_view;
			} else {
				// viewType可能是detail or update 但是都进查看
				// 已办发起人查看(有子流程的可以查出多表单数据)
				viewType = BusinessConst.ViewType_detail;
				buttonType = BusinessConst.ButtonType_view;
				List<Map<String, Object>> childrenProcessInstanceList = flowFormService.findForJdbc(
						"select business_key businessKey,def_name defName,formCode formCode from t_flow_instance t where t.parent_id=?", businessKey);
				data.put("childrenProcessInstanceList", childrenProcessInstanceList);
				// TODO 流程结束标识
				Map<String, Object> flowStatusMap = flowFormService.findOneForJdbc("SELECT status FROM t_flow_instance_history  WHERE business_key=?",
						businessKey);
				if (flowStatusMap != null && flowStatusMap.get("status") != null) {
					flowStatus = Integer.parseInt(flowStatusMap.get("status").toString());
				}
				circulateList = appFormTableService.queryCirculateReceivers(businessKey);
			}
		}

		/************* 获得配置与数据区域 *************/
		Map<String, Object> fieldConfigMap = new HashMap<String, Object>();
		Map<String, Object> formData = new HashMap<String, Object>();
		Map<String, String> subTableNameMap = new HashMap<String, String>();
		String needInitTables = "";

		// json配置map
		JSONObject configJsonMap = JSONHelper.toJSONObject(flowForm.getFieldJson());
		// 处理主表字段配置
		Map<String, Object> mainFieldMap = processFieldsConfig(mainTable.getTableName());
		fieldConfigMap.putAll(mainFieldMap);

		// 处理主表数据
		if (StringUtil.isNotEmpty(businessKey)) {
			// 查询当前模板是否有关联字段
			List<AppFormField> connectionFields = appFormTableService.queryConnectionFields(mainTable.getTableName());
			if (connectionFields.size() > 0) {
				// 主模板主表名
				if(StringUtil.isEmpty(parentBusinessKey)){
					//关联模板进待办时,获得主模板的businessKey
					Map<String, Object> parentBusinessKeyMap = flowFormService.findOneForJdbc("SELECT t.parent_id FROM t_flow_instance t WHERE t.business_key=?",
							businessKey);
					if (parentBusinessKeyMap != null && parentBusinessKeyMap.get("parent_id") != null) {
						parentBusinessKey = parentBusinessKeyMap.get("parent_id").toString();
					}
				}
				Map<String, Object> mainTemplate = this.appFormTableService.querySingleTableData(mainFormTableName, parentBusinessKey);
				formData.put(mainFormTableName, mainTemplate);
			}

			Map<String, Object> mainData = this.appFormTableService.querySingleTableData(mainTable.getTableName(), businessKey);
			if (mainData != null) {
				// 处理主表附件数据
				List<AppFormField> mainFileFields = this.appFormFieldService.queryUsedFileAppFormField(mainTable.getTableName());
				for (AppFormField mainFileField : mainFileFields) {
					// 根据tableName,cid,id去查询相应附件
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("businessKey", businessKey);
					param.put("businessType", mainTable.getTableName());
					param.put("businessExtra", mainFileField.getParentId());
					param.put("sort", "createTime");
					param.put("order", "asc");
					List<AttachVo> dataVoList = attachService.queryUploadAttach(param);

					mainData.put(mainFileField.getCode(), dataVoList);
				}
			} else {
				mainData = new HashMap<String, Object>();
			}
			formData.put(mainTable.getTableName(), mainData);

			// 如果是主从结构
			if (BusinessConst.TableType_main.equals(mainTable.getTableType())) {
				// 获取从表table数据
				for (String subTableName : mainTable.getSubTables().split(",")) {
					AppFormTable subTable = this.appFormTableService.queryAppFormTable(subTableName);

					// 处理从表字段配置
					Map<String, Object> subFieldMap = processFieldsConfig(subTableName);
					fieldConfigMap.putAll(subFieldMap);
					// 处理从表数据
					List<Map<String, Object>> subData = this.appFormTableService.querySubTableData(subTableName, businessKey);
					List<AppFormField> fileFields = this.appFormFieldService.queryUsedFileAppFormField(subTableName);
					for (AppFormField fileField : fileFields) {
						for (Map<String, Object> map : subData) {
							// 根据tableName,cid,id去查询相应附件
							Map<String, Object> param = new HashMap<String, Object>();
							param.put("businessKey", map.get("id").toString());
							param.put("businessType", subTableName);
							param.put("businessExtra", fileField.getParentId());
							param.put("sort", "createTime");
							param.put("order", "asc");

							List<AttachVo> dataVoList = attachService.queryUploadAttach(param);

							map.put(fileField.getCode(), dataVoList);
						}
					}
					formData.put(subTableName, subData);
					if (subData == null || subData.size() == 0) {
						needInitTables += subTableName + ",";
					}
					subTableNameMap.put(subTable.getCId(), subTableName);
				}
				needInitTables = StringUtil.removeDot(needInitTables);
			}
		}

		// 数据与配置相关
		List<Map<String, Object>> disabledFieldsList = appFormFieldService.queryDisableFieldList(formCode);
		data.put("disabledFieldsList", disabledFieldsList);
		data.put("formCode", formCode);
		data.put("mainFormCode", flowForm.getMainFormCode());
		data.put("businessKey", businessKey);
		data.put("parentBusinessKey", parentBusinessKey);
		data.put("viewType", viewType);
		data.put("mainTableName", mainTable.getTableName());
		data.put("mainFormTableName", mainFormTableName);
		data.put("mainTableType", mainTable.getTableType());
		data.put("configJsonMap", configJsonMap);
		data.put("fieldConfigMap", fieldConfigMap);
		data.put("formData", formData);
		data.put("subTableNameMap", subTableNameMap);
		data.put("attachForeRequest", ConfigConst.serverUrl + ConfigConst.attachRequest);
		data.put("attachImgRequest", ConfigConst.serverUrl + ConfigConst.attachImgRequest);
		data.put("isMobile", HttpUtils.isMoblie(request));
		data.put("needInitTables", needInitTables);
		data.put("user_id", ClientUtil.getUserId());
		data.put("circulateList", circulateList);
		
		// 流程相关
		data.put("buttonType", buttonType);
		data.put("isFlow", flowForm.getIsFlow());
		data.put("taskId", taskId);
		data.put("flowStatus", flowStatus);
		data.put("notifyType", notifyType);
		data.put("isSharefolder", isSharefolder);
		data.put("isApp", flowForm.getIsApp());//是否是主模板,还是关联模板
		data.put("isStartAssign", flowForm.getIsStartAssign());

		String template = FreemarkerHelper.parseTemplate("/com/xplatform/base/develop/codegenerate/template/generateform/form.ftl", data);
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(template);
	}

	/**
	 * 跳转通用编辑页方法
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 *
	 */
	@RequestMapping(params = "commonFormEdit1")
	public ModelAndView commonFormEdit1(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String formCode = request.getParameter("formCode");
		String businessKey = request.getParameter("businessKey");
		String viewType = request.getParameter("viewType");

		return new ModelAndView("main/home/form/commonFormEdit");
		//
		// FlowFormEntity flowForm =
		// flowFormService.queryLastestVersionFlowForm(formCode);
		// Map<String, Object> data = new HashMap<String, Object>();
		// Map<String, Object> fieldConfigMap = new HashMap<String, Object>();
		// Map<String, Object> formData = new HashMap<String, Object>();
		// Map<String, String> subTableNameMap = new HashMap<String, String>();
		// // json配置map
		// Map<String, Object> configJsonMap =
		// JSONHelper.toJSONObject(flowForm.getFieldJson());
		//
		// // 主表table数据
		// AppFormTable mainTable =
		// appFormTableService.queryMainTable(formCode);
		// // 处理主表字段配置
		// Map<String, Object> mainFieldMap =
		// processFieldsConfig(mainTable.getTableName());
		// fieldConfigMap.putAll(mainFieldMap);
		//
		// // 处理主表数据
		// Map<String, Object> mainData =
		// this.appFormTableService.querySingleTableData(mainTable.getTableName(),
		// businessKey);
		// formData.put(mainTable.getTableName(), mainData);
		//
		// Map<String, AppFormTable> subTablesMap = new HashMap<String,
		// AppFormTable>();
		// // 如果是主从结构
		// if (BusinessConst.TableType_main.equals(mainTable.getTableType())) {
		// // 获取从表table数据
		// for (String subTableName : mainTable.getSubTables().split(",")) {
		// AppFormTable subTable =
		// this.appFormTableService.queryAppFormTable(subTableName);
		// // 处理从表字段配置
		// Map<String, Object> subFieldMap = processFieldsConfig(subTableName);
		// fieldConfigMap.putAll(subFieldMap);
		// // 处理从表数据
		// List<Map<String, Object>> subData =
		// this.appFormTableService.querySubTableData(subTableName,
		// businessKey);
		// formData.put(subTableName, subData);
		// subTableNameMap.put(subTable.getCId(), subTableName);
		// }
		// }
		//
		// data.put("mainId", businessKey);
		// data.put("viewType", viewType);
		// data.put("mainTableName", mainTable.getTableName());
		// data.put("mainTableType", mainTable.getTableType());
		// data.put("configJsonMap", configJsonMap);
		// data.put("fieldConfigMap", fieldConfigMap);
		// data.put("formData", formData);
		// data.put("subTableNameMap", subTableNameMap);
		//
		// // data.put("mainTable", mainTable);
		// // data.put("subTablesMap", subTablesMap);
		//
		// String template =
		// FreemarkerHelper.parseTemplate("/com/xplatform/base/develop/codegenerate/template/generateform/form.ftl",
		// data);
		// response.setContentType("text/html;charset=utf-8");
		// response.getWriter().print(template);
	}

	/**
	 * 通过tableName找到对应的字段配置并转化为map
	 * 
	 * @param tableName
	 * @return
	 */
	private Map<String, Object> processFieldsConfig(String tableName) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<AppFormField> list = this.appFormFieldService.findByProperty("tableName", tableName);
		for (AppFormField field : list) {
			String parentId = field.getParentId();
			String cId = field.getMyCid();
			String fieldKey = field.getFieldKey();

			// key为空,说明是单字段控件
			map.put(parentId, field);
		}
		return map;
	}
	
	@RequestMapping(params = "goMapView")
	public ModelAndView goMapView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String paramsStr = request.getParameter("params");
		JSONObject params = JSONHelper.toJSONObject(paramsStr);
		request.setAttribute("params", paramsStr);
		return new ModelAndView("main/home/form/mapView");
	}
}
