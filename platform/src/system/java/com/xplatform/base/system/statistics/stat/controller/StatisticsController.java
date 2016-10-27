package com.xplatform.base.system.statistics.stat.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang.StringUtils;
import org.apache.fop.svg.PDFTranscoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.common.model.json.Highchart;
import com.xplatform.base.framework.core.common.model.json.PieChart;
import com.xplatform.base.framework.core.common.model.json.PieDataVo;
import com.xplatform.base.framework.core.common.model.json.TreeNode;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.MybatisTreeMapper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.TreeMapper;
import com.xplatform.base.framework.mybatis.engine.condition.CriterionBuilder;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.system.statistics.datasource.service.DataSourceService;
import com.xplatform.base.system.statistics.field.entity.FieldEntity;
import com.xplatform.base.system.statistics.field.service.FieldService;
import com.xplatform.base.system.statistics.stat.entity.StatisticsEntity;
import com.xplatform.base.system.statistics.stat.service.StatisticsService;
import com.xplatform.base.system.sysseting.entity.SysParameterEntity;
import com.xplatform.base.system.sysseting.service.SysParameterService;
import com.xplatform.base.system.type.entity.TypeEntity;
import com.xplatform.base.system.type.mybatis.vo.TypeTreeVo;
import com.xplatform.base.system.type.service.TypeService;

/**
 * @Title: Controller
 * @Description: 分类统计
 * @author onlineGenerator
 * @date 2014-07-03 10:42:37
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/statisticsController")
public class StatisticsController extends BaseController {

	@Resource
	private StatisticsService statisticsService;

	@Resource
	private DataSourceService dataSourceService;

	@Resource
	private FieldService fieldService;

	@Resource
	private TypeService typeService;
	
	@Resource
	private SysParameterService sysParameterService;

	@Resource
	private AuthorityService authorityService;

	private AjaxJson result = new AjaxJson();
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 分类统计列表 页面跳转
	 * 
	 * @date 2014-07-03 10:42:37
	 * @return
	 */
	@RequestMapping(params = "statistics")
	public ModelAndView statistics(HttpServletRequest request) {
		return new ModelAndView(
				"platform/system/statistics/stat/statisticsList");
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
	public void datagrid(HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		/*
		 * CriteriaQuery cq = new CriteriaQuery(StatisticsEntity.class,
		 * dataGrid); //查询条件组装器 HqlGenerateUtil.installHql(cq, statistics,
		 * request.getParameterMap()); try{ //自定义追加查询条件 if
		 * (StringUtil.isNotEmpty(request.getParameter("type_id"))) {
		 * cq.eq("type.id", request.getParameter("type_id")); } else {
		 * cq.eq("type.id", "-1"); } }catch (Exception e) { throw new
		 * BusinessException(e.getMessage()); } cq.add();
		 * this.statisticsService.getDataGridReturn(cq, true);
		 * TagUtil.datagrid(response, dataGrid);
		 */

		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(request.getParameter("type_id"))) {
			params.put("typeid", request.getParameter("type_id"));
		} else {
			params.put("typeid", "-1");
		}
		List<Map<String, Object>> list = this.statisticsService
				.queryStatisticsList(params);
		dataGrid.setResults(list);
		dataGrid.setTotal(list.size());
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除分类统计
	 * 
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(StatisticsEntity statistics,
			HttpServletRequest request) {
		message = "分类统计删除成功";
		try {
			statisticsService.delete(statistics.getId());
		} catch (Exception e) {
			e.printStackTrace();
			message = "分类统计删除失败";
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 批量删除分类统计
	 * 
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		try {
			statisticsService.batchDelete(ids);
			message = "分类统计删除成功";
		} catch (Exception e) {
			e.printStackTrace();
			message = "分类统计删除失败";
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 进入新增或者修改查看页面
	 * 
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "statisticsEdit")
	public ModelAndView statisticsEdit(StatisticsEntity statistics, Model model) {
		if (StringUtil.isNotEmpty(statistics.getId())) {
			statistics = statisticsService.get(statistics.getId());
			model.addAttribute("statistics", statistics);
		}
		return new ModelAndView(
				"platform/system/statistics/stat/statisticsEdit");
	}

	/**
	 * 新增或修改分类统计
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "saveStatistics")
	@ResponseBody
	public AjaxJson saveStatistics(StatisticsEntity statistics,
			HttpServletRequest request) {
		try {
			if (StringUtil.isNotEmpty(statistics.getId())) {
				message = "分类统计更新成功";
				statisticsService.update(statistics);
			} else {
				message = "分类统计新增成功";
				if (StringUtil.isNotEmpty(statistics.getType().getId())) {
					statisticsService.save(statistics);
				} else {
					message = "新增分类统计没有指定树记录";
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			message = "分类统计操作失败";
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 分类统计数据图形显示
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "statisticsView")
	public ModelAndView statisticsView(HttpServletRequest request) {
		String datasourceId = request.getParameter("datasourceId");
		request.setAttribute("datasourceId", datasourceId);
		//DataSourceEntity dse = this.dataSourceService.get(datasourceId); //modify by lxt
		SysParameterEntity dse = this.sysParameterService.getEntity(SysParameterEntity.class, datasourceId);

		if ("sql".equals(dse.getType())) {//sql类型统计
			List<FieldEntity> list = fieldService
					.queryByDatasourceIdList(datasourceId);
			request.setAttribute("list", list);
			return new ModelAndView(
					"platform/system/statistics/stat/statisticsSqlView");
		}else{//报表类型统计
			request.setAttribute("dse", dse);
			return new ModelAndView(
					"platform/system/statistics/stat/statisticsReportView");
		}
	}

	/**
	 * 分类统计数据表格显示
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "datagridView")
	@ResponseBody
	public void datagridView(HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		String datasourceId = request.getParameter("datasourceId");
		//DataSourceEntity ce = this.dataSourceService.get(datasourceId); modify by lxt
		SysParameterEntity ce = this.sysParameterService.getEntity(SysParameterEntity.class, datasourceId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sql", ce.getValue());

		CriterionBuilder builder = new CriterionBuilder();
		builder.buildQueryCondition(request, "t");
		String conditionSql = dataSourceService.buildRelations(builder);
		params.put("parameter", builder.getValueMap());
		params.put("condition", conditionSql);

		List<Map<String, Object>> list = null;
		if (StringUtils.equals("sql", ce.getType())) {
			list = this.statisticsService.queryDataList(params);
		}
		dataGrid.setResults(list);
		dataGrid.setTotal(list.size());
		dataGrid.setExParam(params);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 分类统计数据图形显示
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "getData")
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request,
			HttpServletResponse response) {
		String reportType = request.getParameter("reportType");
		List<Highchart> list = new ArrayList<Highchart>();
		List<Map<String, Object>> lt = new ArrayList<Map<String, Object>>();
		List<String> xAxis = new ArrayList<String>(); // X轴记录集合
		List<String> yAxis = new ArrayList<String>(); // Y轴记录集合
		String X = "";// X轴字段名
		Highchart hc = new Highchart();
		Map<String, Object> map;
		String datasourceId = request.getParameter("datasourceId");
		//DataSourceEntity ce = this.dataSourceService.get(datasourceId); modify by lxt
		SysParameterEntity ce = this.sysParameterService.getEntity(SysParameterEntity.class, datasourceId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sql", ce.getValue());
		List<Map<String, Object>> data = null;
		if (StringUtils.equals("sql", ce.getType())) {
			data = this.statisticsService.queryDataList(params);
		}
		List<FieldEntity> fieldList = this.fieldService
				.queryByDatasourceIdList(datasourceId);
		for (FieldEntity field : fieldList) {
			for (Map<String, Object> obj : data) {
				if (StringUtils.equals("Y", field.getIsx())) {
					X = field.getName();
					xAxis.add(obj.get(field.getName()).toString());
				}
			}
			if (reportType.equals("pie")) {
				if (StringUtils.equals(field.getName(),
						request.getParameter("yAxis"))) {
					yAxis.add(field.getName() + ":" + field.getShowName());
				}
			} else {
				if (StringUtils.equals("Y", field.getIsy())) {
					yAxis.add(field.getName() + ":" + field.getShowName());
				}
			}
		}
		for (String yfiled : yAxis) {
			String fieldName = yfiled.split(":")[0];
			String fieldShowName = yfiled.split(":")[1];
			hc = new Highchart();
			hc.setType(reportType);
			hc.setName(fieldShowName);
			List y = new ArrayList();
			if (reportType.equals("pie")) {
				for (Map<String, Object> obj : data) {
					map = new HashMap<String, Object>();
					map.put("name", obj.get(X));
					map.put("y", obj.get(fieldName));
					lt.add(map);
				}
				hc.setData(lt);
			} else {
				for (Map<String, Object> obj : data) {
					y.add(obj.get(fieldName));
				}
				hc.setData(y);
			}
			list.add(hc);
		}
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("name", ce.getDescription());
		json.put("xAxis", xAxis);
		json.put("data", list);
		return json;
	}
	
	
	/**
	 * 分类统计数据图形显示
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "getDataECharts")
	@ResponseBody
	public Map<String, Object> getDataECharts(HttpServletRequest request,
			HttpServletResponse response) {
		String reportType = request.getParameter("reportType");
		List<Highchart> list = new ArrayList<Highchart>();
		List<Map<String, Object>> lt = new ArrayList<Map<String, Object>>();
		List<String> xAxis = new ArrayList<String>(); // X轴记录集合
		List<String> yAxis = new ArrayList<String>(); // Y轴记录集合
		String X = "";// X轴字段名
		Highchart hc = new Highchart();
		PieChart pc = new PieChart();
		List<PieDataVo> poList = new ArrayList<PieDataVo>();
		String datasourceId = request.getParameter("datasourceId");
		//DataSourceEntity ce = this.dataSourceService.get(datasourceId); modify by lxt
		SysParameterEntity ce = this.sysParameterService.getEntity(SysParameterEntity.class, datasourceId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sql", ce.getValue());
		List<Map<String, Object>> data = null;
		
		params.put("parameter", request.getParameter("parameter"));
		params.put("condition", request.getParameter("condition"));
		
		if (StringUtils.equals("sql", ce.getType())) {
			data = this.statisticsService.queryDataList(params);
		}
		List<FieldEntity> fieldList = this.fieldService
				.queryByDatasourceIdList(datasourceId);
		for (FieldEntity field : fieldList) {
			for (Map<String, Object> obj : data) {
				if (StringUtils.equals("Y", field.getIsx())) {
					X = field.getName();
					xAxis.add(obj.get(field.getName()).toString());
				}
			}
			if (reportType.equals("pie")) {
				if (StringUtils.equals(field.getName(),
						request.getParameter("yAxis"))) {
					yAxis.add(field.getName() + ":" + field.getShowName());
				}
			} else {
				if (StringUtils.equals("Y", field.getIsy())) {
					yAxis.add(field.getName() + ":" + field.getShowName());
				}
			}
		}
		
		List<String> dataType = new ArrayList<String>();
		for (String yfiled : yAxis) {
			String fieldName = yfiled.split(":")[0];
			String fieldShowName = yfiled.split(":")[1];
			dataType.add(fieldShowName);
			hc = new Highchart();
			hc.setType(reportType);
			hc.setName(fieldShowName);
			List y = new ArrayList();
			if (reportType.equals("pie")) {
				for (Map<String, Object> obj : data) {
					PieDataVo po = new PieDataVo();
					po.setName(obj.get(X) == null ? "" : obj.get(X).toString());
					po.setValue(obj.get(fieldName) == null ? 0 : Integer.valueOf(obj.get(fieldName).toString()));
					poList.add(po);
				}
			} else {
				for (Map<String, Object> obj : data) {
					y.add(obj.get(fieldName));
				}
				hc.setData(y);
			}
			list.add(hc);
		}
		Map<String, Object> json = new HashMap<String, Object>();
		List<PieChart> pie = new ArrayList<PieChart>();
		if(reportType.equals("pie")){
			json.put("name", dataType.get(0));
			pc.setName(dataType.get(0));
			pc.setType("pie");
			pc.setRadius("50%");
			//pc.setCenter("['50%', '60%']");
			pc.setData(poList);
			json.put("data", pc);
			json.put("xAxis", xAxis);
		}else{
			json.put("name", ce.getName());
			json.put("xAxis", xAxis);
			json.put("data", list);
			json.put("dataType", dataType);
		}
		return json;
	}

	/**
	 * 报表打印
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(params = "export")
	public void export(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String type = request.getParameter("type");
		String svg = request.getParameter("svg");
		String filename = request.getParameter("filename");

		filename = filename == null ? "chart" : filename;
		ServletOutputStream out = response.getOutputStream();
		try {
			if (null != type && null != svg) {
				svg = svg.replaceAll(":rect", "rect");
				String ext = "";
				Transcoder t = null;
				if (type.equals("image/png")) {
					ext = "png";
					t = new PNGTranscoder();
				} else if (type.equals("image/jpeg")) {
					ext = "jpg";
					t = new JPEGTranscoder();
				} else if (type.equals("application/pdf")) {
					ext = "pdf";
					t = (Transcoder) new PDFTranscoder();
				} else if (type.equals("image/svg+xml"))
					ext = "svg";
				response.addHeader(
						"Content-Disposition",
						"attachment; filename="
								+ new String(filename.getBytes("GBK"),
										"ISO-8859-1") + "." + ext);
				response.addHeader("Content-Type", type);

				if (null != t) {
					TranscoderInput input = new TranscoderInput(
							new StringReader(svg));
					TranscoderOutput output = new TranscoderOutput(out);

					try {
						t.transcode(input, output);
					} catch (TranscoderException e) {
						out.print("Problem transcoding stream. See the web logs for more details.");
						e.printStackTrace();
					}
				} else if (ext.equals("svg")) {
					// out.print(svg);
					OutputStreamWriter writer = new OutputStreamWriter(out,
							"UTF-8");
					writer.append(svg);
					writer.close();
				} else
					out.print("Invalid type: " + type);
			} else {
				response.addHeader("Content-Type", "text/html");
				out.println("Usage:\n\tParameter [svg]: The DOM Element to be converted."
						+ "\n\tParameter [type]: The destination MIME type for the elment to be transcoded.");
			}
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	/**
	 * 统计显示 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "result")
	public ModelAndView result(HttpServletRequest request) {
		return new ModelAndView(
				"platform/system/statistics/stat/statisticsResultList");
	}

	/**
	 * 统计显示 分类树
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "tree")
	public void tree(TypeTreeVo type, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		UserEntity user = client.getUser();
		String id = "";
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			id = request.getParameter("id");
		} else {
			id = "-1";
		}
		String sysType = request.getParameter("sysType");
		Map<String, String> map = new HashMap<String, String>();
		map.put("parentId", id);
		map.put("userId", user.getId());
		map.put("sysType", sysType);
		// map.put("userTypeId", user.getUserTypeId());
		List<TreeNode> treeList = new ArrayList<TreeNode>();
		List<TypeTreeVo> typeList = typeService
				.queryTypeRoleTreeBySysTypeTree(map);

		for (TypeTreeVo t : typeList) {
			if (StringUtils.equals("1", t.getIsLeaf())) {
				// List<StatisticsEntity> stat =
				// this.statisticsService.findByPropertys(t.getId(),params);//查询自己创建的统计
				List<StatisticsEntity> stat = this.statisticsService
						.queryListByPorperty("type.id", t.getId());
				if (stat.size() > 0) {
					t.setIsLeaf("0");
				}
			}
		}
		// List<StatisticsEntity> stat =
		// this.statisticsService.findByPropertys(id,params);
		List<StatisticsEntity> stat = this.statisticsService
				.queryListByPorperty("type.id", id);
		if (stat.size() > 0) {
			for (StatisticsEntity s : stat) {
				TypeTreeVo t = new TypeTreeVo();
				t.setId(s.getDatasourceId());
				t.setName(s.getName());
				t.setLevel(typeService.getType(id).getLevel() + 1);
				t.setIsLeaf("1");
				t.setParentId(id);
				t.setIconCls("awsm-icon-refresh");
				t.setSysType("stat");
				t.setShowType(s.getShowType());
				typeList.add(t);
			}
		}

		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		propertyMapping.put(TreeMapper.PropertyType.ICONCLS.getValue(),
				"iconCls");
		propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(),
				"code,name,sysType,showType");
		treeList = MybatisTreeMapper.buildJsonTree(typeList, propertyMapping);

		TagUtil.tree(response, treeList);
	}

	/**
	 * 取得该表所有记录
	 */
	@RequestMapping(params = "getyAxis")
	@ResponseBody
	public List<FieldEntity> getyAxis(HttpServletRequest request) {
		String datasourceId = request.getParameter("datasourceId");
		List<FieldEntity> list = fieldService
				.queryByDatasourceIdList(datasourceId);
		List<FieldEntity> yAxis = new ArrayList<FieldEntity>();
		for (FieldEntity field : list) {
			if (field.getIsy().equals("Y")) {
				yAxis.add(field);
			}
		}
		return yAxis;

	}

	@RequestMapping(params = "report")
	public ModelAndView report(HttpServletRequest request) {
		request.setAttribute("xmlData", "<?xml version='1.0' encoding='UTF-8'?><data></data>");
		/* return new ModelAndView("reportJsp/showReport"); */
		return new ModelAndView("web/xmlTest");
	}
	
	@RequestMapping(params = "statisticsViewSample")
	public ModelAndView statisticsViewSample(HttpServletRequest request) {
		return new ModelAndView("platform/system/statistics/stat/statisticsViewSample");
	}

}
