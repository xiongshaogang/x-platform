package com.xplatform.base.system.flowform.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.develop.codeconfig.entity.ConBaseFormEntity;
import com.xplatform.base.develop.codegenerate.database.UcgReadTable;
import com.xplatform.base.develop.codegenerate.pojo.Columnt;
import com.xplatform.base.develop.codegenerate.pojo.DataBaseConst;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.framework.tag.vo.datatable.SortDirection;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.system.flowform.entity.FlowFieldEntity;
import com.xplatform.base.system.flowform.entity.FlowTableEntity;
import com.xplatform.base.system.flowform.service.FlowFieldService;
import com.xplatform.base.system.flowform.service.FlowTableService;


/**   
 * @Title: Controller
 * @Description: 
 * @date 2015-01-04 18:00:41
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/flowTableController")
public class FlowTableController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FlowTableController.class);

	@Resource
	private FlowTableService flowTableService;
	@Resource
	private FlowFieldService flowFieldService;
	
	
	private AjaxJson result = new AjaxJson();
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "flowTable")
	public ModelAndView flowTable(HttpServletRequest request) {
		return new ModelAndView("platform/system/flowform/flowtable/flowTableList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param entity
	 * @throws BusinessException 
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(FlowTableEntity flowTable,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) throws BusinessException {
		CriteriaQuery cq = new CriteriaQuery(FlowTableEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, flowTable, request.getParameterMap());
		try{
		//自定义追加查询条件
		
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.flowTableService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(FlowTableEntity flowTable, HttpServletRequest request) {
		message = "删除成功";
		try{
			flowTableService.delete(flowTable.getId());
		}catch(Exception e){
			e.printStackTrace();
			message = "删除失败";
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除
	 * 
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request){
		String ids=request.getParameter("ids");
		try{
			flowTableService.batchDelete(ids);
			message = "删除成功";
		}catch(Exception e){
			e.printStackTrace();
			message = "删除失败";
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 进入新增或者修改查看页面
	 * @param flowTable
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "flowTableEdit")
	public ModelAndView flowTableEdit(FlowTableEntity flowTable,HttpServletRequest request) {
		if (StringUtil.isNotEmpty(flowTable.getId())) {
			flowTable = flowTableService.get(flowTable.getId());
		}
		return new ModelAndView("platform/system/flowform/flowtable/flowTableEdit").addObject("flowTable", flowTable);
	}
	


	/**
	 * 新增或修改
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(params = "saveFlowTable")
	@ResponseBody
	public AjaxJson saveFlowTable(FlowTableEntity flowTable,HttpServletRequest request) {
		try {
			flowTable.setPkfield("id");
			if (StringUtil.isNotEmpty(flowTable.getId())) {
				message = "更新成功";
				flowTableService.update(flowTable);
			} else {
				message = "新增成功";
				flowTable.setVersionno(1);
				flowTable.setGenerateType("1");
				flowTable.setIsexternal("1");
				flowTable.setIspublished("0");
				flowTableService.save(flowTable);
				saveDefaultColumns(flowTable);
			}
		} catch (Exception e) {
			// TODO: handle exception
			message = "操作失败";
		}
		result.setMsg(message);
		result.setObj(flowTable.getId());
		return result;
	}
	
	/**
	 * 取得该表所有记录
	 */
	@RequestMapping(params = "getMainTable")
	@ResponseBody
	public List<FlowTableEntity> getMainTable(HttpServletRequest request) {
		List<FlowTableEntity> list = this.flowTableService.getMainTableList();
		return list;
		
	}
	
	/**
	 * 增加默认的列
	 * @param FormHead
	 * @throws Exception 
	 */
	public void saveDefaultColumns(FlowTableEntity flowTable) throws Exception{
		FlowFieldEntity flow = new FlowFieldEntity(); 
		flow.setTable(flowTable);
		flow.setFieldName("id");
		flow.setFieldDes("主键");
		flow.setType("string");
		flow.setLength(32);
		flow.setPointLength(0);
		flow.setIsrequired("1");
		flow.setIslist("0");
		flow.setIsquery("0");
		flow.setIsflowvar("0");
		flow.setValueFrom("1");
		flow.setControlType("1");
		this.flowFieldService.save(flow);
		
		flow = new FlowFieldEntity(); 
		flow.setTable(flowTable);
		flow.setFieldName("create_user_id");
		flow.setFieldDes("创建人ID");
		flow.setType("string");
		flow.setLength(32);
		flow.setPointLength(0);
		flow.setIsrequired("0");
		flow.setIslist("0");
		flow.setIsquery("0");
		flow.setIsflowvar("0");
		flow.setValueFrom("1");
		flow.setControlType("1");
		this.flowFieldService.save(flow);
		
		flow = new FlowFieldEntity(); 
		flow.setTable(flowTable);
		flow.setFieldName("create_user_name");
		flow.setFieldDes("创建人name");
		flow.setType("string");
		flow.setLength(32);
		flow.setPointLength(0);
		flow.setIsrequired("0");
		flow.setIslist("0");
		flow.setIsquery("0");
		flow.setIsflowvar("0");
		flow.setValueFrom("1");
		flow.setControlType("1");
		this.flowFieldService.save(flow);
		
		flow = new FlowFieldEntity(); 
		flow.setTable(flowTable);
		flow.setFieldName("create_time");
		flow.setFieldDes("创建时间");
		flow.setType("string");
		flow.setLength(32);
		flow.setPointLength(0);
		flow.setIsrequired("0");
		flow.setIslist("0");
		flow.setIsquery("0");
		flow.setIsflowvar("0");
		flow.setValueFrom("1");
		flow.setControlType("1");
		this.flowFieldService.save(flow);
		
		flow = new FlowFieldEntity(); 
		flow.setTable(flowTable);
		flow.setFieldName("update_user_id");
		flow.setFieldDes("更新人ID");
		flow.setType("string");
		flow.setLength(32);
		flow.setPointLength(0);
		flow.setIsrequired("0");
		flow.setIslist("0");
		flow.setIsquery("0");
		flow.setIsflowvar("0");
		flow.setValueFrom("1");
		flow.setControlType("1");
		this.flowFieldService.save(flow);
		
		flow = new FlowFieldEntity(); 
		flow.setTable(flowTable);
		flow.setFieldName("update_user_name");
		flow.setFieldDes("更新人name");
		flow.setType("string");
		flow.setLength(32);
		flow.setPointLength(0);
		flow.setIsrequired("0");
		flow.setIslist("0");
		flow.setIsquery("0");
		flow.setIsflowvar("0");
		flow.setValueFrom("1");
		flow.setControlType("1");
		this.flowFieldService.save(flow);
		
		
		flow = new FlowFieldEntity(); 
		flow.setTable(flowTable);
		flow.setFieldName("update_time");
		flow.setFieldDes("更新时间");
		flow.setType("string");
		flow.setLength(32);
		flow.setPointLength(0);
		flow.setIsrequired("0");
		flow.setIslist("0");
		flow.setIsquery("0");
		flow.setIsflowvar("0");
		flow.setValueFrom("1");
		flow.setControlType("1");
		this.flowFieldService.save(flow);
	}
	
	/**
	 *发布
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doPublish")
	@ResponseBody
	public AjaxJson doPublish(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String id = request.getParameter("id");
		FlowTableEntity flowTable = flowTableService.getEntity(FlowTableEntity.class,
				id);
		HttpSession session = ContextHolderUtils.getSession();
		Client client=ClientManager.getInstance().getClient(session.getId());
		//同步数据库
		try {
			boolean bl = this.flowTableService.deployDb(flowTable);
			if(bl){
				//
				message = "同步成功";
				flowTable.setVersionno(flowTable.getVersionno()+1);
				flowTable.setIspublished("1");
				flowTable.setPublishedby(client.getUser().getUserName());
				flowTable.setPublishtime(new Date());
				this.flowTableService.update(flowTable);
				j.setMsg(message);
			}else{
				message = "同步失败";		
				j.setMsg(message);
				return j;
			}
		} catch (Exception e) {
			j.setMsg(e.getMessage());
			return j;
		}
		return j;
	}
	
	/**
	 * 取得该表所有记录
	 */
	@RequestMapping(params = "groupTeam")
	public ModelAndView groupTeam(HttpServletRequest request) {
		String id = request.getParameter("id");
		FlowTableEntity flowTable = this.flowTableService.get(id);
		return new ModelAndView("platform/system/flowform/team/flowTeam").addObject("flowTable", flowTable);
	}
	
	@RequestMapping(params = "getData")
	public ModelAndView getData(HttpServletRequest request,
			HttpServletResponse response) {
		String tableId = request.getParameter("tableId");
		List<FlowFieldEntity> flowFieldList = this.flowFieldService.queryListByTableId(tableId);
		StringBuilder sb = new StringBuilder();
		sb.append("<ul class=\"tree\" id=\"formType\" fit=\"false\" border=\"false\">");
		
		for(FlowFieldEntity field : flowFieldList){
			sb.append("<li class=\"formType-li\">");
			sb.append("<div class=\"tree-node\" ondblclick=\"selectField('"+ field.getFieldName() +"','"+ field.getFieldDes()+"')\" style=\"cursor: pointer;\">");
			sb.append("<span class=\"tree-indent\"></span>");
			sb.append("<span class=\"tree-icon awsm-icon-leaf green\"></span>");
			sb.append("<span class=\"tree-title\" id='"+field.getFieldName()+"'>"+field.getFieldName()+"</span></div>");
			sb.append("</li>");
		}
		sb.append("</ul>");
		request.setAttribute("sb", sb);
		return new ModelAndView("develop/formtype/shortcut_west");
	}
	
	
	/**
	 * 保存分组数据
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(params = "saveData")
	@ResponseBody
	public AjaxJson saveData(HttpServletRequest request) {
		message = "保存成功";
		String id = request.getParameter("id");
		String team = request.getParameter("team");
		FlowTableEntity flowTable = this.flowTableService.get(id);
		flowTable.setTeam(team);
		try {
			this.flowTableService.update(flowTable);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			message = "保存失败";
			e.printStackTrace();
		}
		result.setMsg(message);
		return result;
	}
	/**
	 * 取得该表所有记录
	 */
	@RequestMapping(params = "addTableData")
	public ModelAndView addTableData(HttpServletRequest request) {
		return new ModelAndView("platform/system/flowform/team/dbToFlowTable");
	}
	
	@RequestMapping(params = "dbdatagrid")
	public void dbdatagrid(HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		List<String> list = new ArrayList<String>();
		try {
			list = new UcgReadTable().readAllTableNames();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String html = "";
		Collections.sort(list,new StringSort(dataGrid.getOrder()));
		List<String> tables = flowTableService.findByQueryString("select tableName from FlowTableEntity");
		list.removeAll(tables);
		List<String> index = new ArrayList<String>();
		html = getJson(list, list.size());
		try {
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-store");
			PrintWriter writer = response.getWriter();
			writer.println(html);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getJson(List<String> result, Integer size) {
		JSONObject main = new JSONObject();
		JSONArray rows = new JSONArray();
		main.put("total", size);
		for (String m : result) {
			JSONObject item = new JSONObject();
			item.put("id", m);
			rows.add(item);
		}
		main.put("rows", rows);
		return main.toString();
	}
	
	private class StringSort implements Comparator<String>{
			
			private SortDirection sortOrder;
			
			public StringSort(SortDirection sortDirection){
				this.sortOrder = sortDirection;
			}
			public int compare(String prev, String next) {
				return sortOrder.equals(SortDirection.asc)?
						prev.compareTo(next):next.compareTo(prev);
			}
			
	}
	
	/**
	 * 反向生成记录
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(params = "addTable")
	@ResponseBody
	public AjaxJson addTable(HttpServletRequest request) {
		String tableName = request.getParameter("tableName");
		AjaxJson j = new AjaxJson();
		HttpSession session = ContextHolderUtils.getSession();
		Client client=ClientManager.getInstance().getClient(session.getId());
		try{
		List<Columnt> list = new UcgReadTable().readOriginalTableColumn(tableName);
		FlowTableEntity flowTable = new FlowTableEntity();
		flowTable.setTableName(tableName);
		flowTable.setContent(tableName);
		flowTable.setIsMainTable(request.getParameter("isMainTable"));
		flowTable.setMainTable(request.getParameter("mainTable"));
		flowTable.setPkfield("id");
		flowTable.setRelation(request.getParameter("relation"));
		flowTable.setIspublished("1");
		flowTable.setPublishedby(client.getUser().getName());
		flowTable.setPublishtime(new Date());
		flowTable.setGenerateType("0");
		flowTable.setIsexternal("0");
		flowTable.setVersionno(1);
		List<FlowFieldEntity> flowFieldList = new ArrayList<FlowFieldEntity>();		
		
		for (int k = 0; k < list.size(); k++) {
			Columnt columnt = list.get(k);
			String fieldName = columnt.getFieldDbName();
			FlowFieldEntity flowField = new FlowFieldEntity();
			flowField.setFieldName(columnt.getFieldDbName().toLowerCase());
			if (StringUtil.isNotEmpty(columnt.getFiledComment()))
				flowField.setFieldDes(columnt.getFiledComment());
			else
				flowField.setFieldDes(columnt.getFieldName());
			flowField.setIsrequired("Y".equals(columnt.getNullable())?"0":"1");
			flowField.setIslist("0");
			flowField.setIsquery("0");
			flowField.setIsflowvar("0");
			flowField.setValueFrom("1");
			flowField.setControlType("1");
			if ("java.lang.Integer".equalsIgnoreCase(columnt.getFieldType())){
				flowField.setType(DataBaseConst.INT);
			}else if ("java.lang.Long".equalsIgnoreCase(columnt.getFieldType())) {
				flowField.setType(DataBaseConst.INT);
			} else if ("java.util.Date".equalsIgnoreCase(columnt.getFieldType())) {
				flowField.setType(DataBaseConst.DATE);
			} else if ("java.lang.Double".equalsIgnoreCase(columnt.getFieldType())
					||"java.lang.Float".equalsIgnoreCase(columnt.getFieldType())) {
				flowField.setType(DataBaseConst.DOUBLE);
			} else if ("java.math.BigDecimal".equalsIgnoreCase(columnt.getFieldType())) {
				flowField.setType(DataBaseConst.BIGDECIMAL);
			} else if (columnt.getFieldType().contains("blob")) {
				flowField.setType(DataBaseConst.BLOB);
				columnt.setCharmaxLength(null);
			} else {
				flowField.setType(DataBaseConst.STRING);
			}
			if (StringUtil.isNotEmpty(columnt.getCharmaxLength())) {
				if (Long.valueOf(columnt.getCharmaxLength()) >= 3000) {
					flowField.setType(DataBaseConst.TEXT);
					//cgFormField.setShowType(DataBaseConst.TEXTAREA);
					try{//有可能长度超出int的长度
						flowField.setLength(Integer.valueOf(columnt.getCharmaxLength()));
					}catch(Exception e){}
				} else {
					flowField.setLength(Integer.valueOf(columnt
							.getCharmaxLength()));
				}
			} else {
				if (StringUtil.isNotEmpty(columnt.getPrecision())) {
					flowField.setLength(Integer.valueOf(columnt
							.getPrecision()));
				}
				//update-begin--Author:zhangdaihao  Date:20140212 for：[001]oracle下number类型，数据库表导出表单，默认长度为0同步失败
				else{
					if(flowField.getType().equals(DataBaseConst.INT)){
						flowField.setLength(10);
					}
				}
				//update-end--Author:zhangdaihao  Date:20140212 for：[001]oracle下number类型，数据库表导出表单，默认长度为0同步失败
				if (StringUtil.isNotEmpty(columnt.getScale()))
					flowField.setPointLength(Integer.valueOf(columnt
							.getScale()));

			}
			flowFieldList.add(flowField);
		}
		flowTable.setColumns(flowFieldList);
		this.flowTableService.saveTable(flowTable, "");
		}catch(Exception e){
			j.setMsg("操作失败");
		}
		Map<String, String> map = new HashMap<String, String>();
		return j;
	}
}
