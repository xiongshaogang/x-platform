package com.xplatform.base.platform.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.cache.UcgCache;
import com.xplatform.base.framework.core.cache.manager.UcgCacheManager;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.common.UploadFile;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.common.model.json.ImportFile;
import com.xplatform.base.framework.core.common.model.json.TreeNode;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.common.service.CommonService;
import com.xplatform.base.framework.core.constant.Globals;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.FileUtils;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.MyClassLoader;
import com.xplatform.base.framework.core.util.ReflectHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.TreeMapper;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.framework.core.util.oConvertUtils;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.framework.tag.vo.easyui.Autocomplete;
import com.xplatform.base.framework.tag.vo.easyui.GridFieldJsonModel;
import com.xplatform.base.orgnaization.resouce.mybatis.vo.ResourceVo;
import com.xplatform.base.orgnaization.resouce.service.ResourceService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.poi.excel.ExcelImportUtil;
import com.xplatform.base.poi.excel.entity.ExportParams;
import com.xplatform.base.poi.excel.entity.ImportParams;
import com.xplatform.base.poi.excel.entity.result.ExcelImportResult;
import com.xplatform.base.poi.handler.inter.IExcelDataHandler;
import com.xplatform.base.poi.util.POIPublicUtil;
import com.xplatform.base.system.dict.entity.DictValueEntity;
import com.xplatform.base.system.dict.service.DictValueService;

/**
 * 通用业务处理
 * 
 * @author 张代浩
 * 
 */
@Controller
@RequestMapping("/commonController")
public class CommonController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CommonController.class);
	@Resource
	private CommonService commonService;
	@Resource
	private BaseService baseService;
	@Resource
	private ResourceService resourceService;
	@Resource
	private DictValueService dictValueService;
	@Resource
	private UcgCacheManager ucgCacheManager;

	private String message;

	/**
	 * 通用列表页面跳转
	 */
	@RequestMapping(params = "listTurn")
	public ModelAndView listTurn(HttpServletRequest request) {
		String turn = request.getParameter("turn");// 跳转的目标页面
		return new ModelAndView(turn);
	}

	/**
	 * 附件预览页面打开链接
	 * 
	 * @return
	 */
	@RequestMapping(params = "openViewFile")
	public ModelAndView openViewFile(HttpServletRequest request) {
		String fileid = request.getParameter("fileid");
		String subclassname = oConvertUtils.getString(request.getParameter("subclassname"),
				"com.xplatform.base.system.pojo.base.TSAttachment");
		String contentfield = oConvertUtils.getString(request.getParameter("contentfield"));
		Class fileClass = MyClassLoader.getClassByScn(subclassname);// 附件的实际类
		Object fileobj = commonService.getEntity(fileClass, fileid);
		ReflectHelper reflectHelper = new ReflectHelper(fileobj);
		String extend = oConvertUtils.getString(reflectHelper.getMethodValue("extend"));
		if ("dwg".equals(extend)) {
			String realpath = oConvertUtils.getString(reflectHelper.getMethodValue("realpath"));
			request.setAttribute("realpath", realpath);
			return new ModelAndView("common/upload/dwgView");
		} else if (FileUtils.isPicture(extend)) {
			String realpath = oConvertUtils.getString(reflectHelper.getMethodValue("realpath"));
			request.setAttribute("realpath", realpath);
			request.setAttribute("fileid", fileid);
			request.setAttribute("subclassname", subclassname);
			request.setAttribute("contentfield", contentfield);
			return new ModelAndView("common/upload/imageView");
		} else {
			String swfpath = oConvertUtils.getString(reflectHelper.getMethodValue("swfpath"));
			request.setAttribute("swfpath", swfpath);
			return new ModelAndView("common/upload/swfView");
		}

	}

	/**
	 * 附件预览读取
	 * 
	 * @return
	 */
	@RequestMapping(params = "viewFile")
	public void viewFile(HttpServletRequest request, HttpServletResponse response) {
		String fileid = oConvertUtils.getString(request.getParameter("fileid"));
		String subclassname = oConvertUtils.getString(request.getParameter("subclassname"),
				"com.jeecg.base.pojo.TSAttachment");
		Class fileClass = MyClassLoader.getClassByScn(subclassname);// 附件的实际类
		Object fileobj = commonService.getEntity(fileClass, fileid);
		ReflectHelper reflectHelper = new ReflectHelper(fileobj);
		UploadFile uploadFile = new UploadFile(request, response);
		String contentfield = oConvertUtils.getString(request.getParameter("contentfield"), uploadFile.getByteField());
		byte[] content = (byte[]) reflectHelper.getMethodValue(contentfield);
		String path = oConvertUtils.getString(reflectHelper.getMethodValue("realpath"));
		String extend = oConvertUtils.getString(reflectHelper.getMethodValue("extend"));
		String attachmenttitle = oConvertUtils.getString(reflectHelper.getMethodValue("attachmenttitle"));
		uploadFile.setExtend(extend);
		uploadFile.setTitleField(attachmenttitle);
		uploadFile.setRealPath(path);
		uploadFile.setContent(content);
		//uploadFile.setView(true);
		commonService.viewOrDownloadFile(uploadFile);
	}

	@RequestMapping(params = "importdata")
	public ModelAndView importdata() {
		return new ModelAndView("system/upload");
	}

	/**
	 * 生成XML文件
	 * 
	 * @return
	 */
	@RequestMapping(params = "createxml")
	public void createxml(HttpServletRequest request, HttpServletResponse response) {
		String field = request.getParameter("field");
		String entityname = request.getParameter("entityname");
		ImportFile importFile = new ImportFile(request, response);
		importFile.setField(field);
		importFile.setEntityName(entityname);
		importFile.setFileName(entityname + ".bak");
		importFile.setEntityClass(MyClassLoader.getClassByScn(entityname));
		commonService.createXml(importFile);
	}

	/**
	 * 生成XML文件parserXml
	 * 
	 * @return
	 */
	@RequestMapping(params = "parserXml")
	@ResponseBody
	public AjaxJson parserXml(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson json = new AjaxJson();
		String fileName = null;
		UploadFile uploadFile = new UploadFile(request);
		String ctxPath = request.getSession().getServletContext().getRealPath("");
		File file = new File(ctxPath);
		if (!file.exists()) {
			file.mkdir();// 创建文件根目录
		}
		MultipartHttpServletRequest multipartRequest = uploadFile.getMultipartRequest();
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile mf = entity.getValue();// 获取上传文件对象
			fileName = mf.getOriginalFilename();// 获取文件名
			String savePath = file.getPath() + "/" + fileName;
			File savefile = new File(savePath);
			try {
				FileCopyUtils.copy(mf.getBytes(), savefile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		commonService.parserXml(ctxPath + "/" + fileName);
		json.setSuccess(true);
		return json;
	}

	/**
	 * 自动完成请求返回数据
	 * 
	 * @param request
	 * @param responss
	 */
	@RequestMapping(params = "getAutoList")
	public void getAutoList(HttpServletRequest request, HttpServletResponse response, Autocomplete autocomplete) {
		String jsonp = request.getParameter("jsonpcallback");
		String trem = StringUtil.getEncodePra(request.getParameter("trem"));// 重新解析参数
		autocomplete.setTrem(trem);
		List autoList = commonService.getAutoList(autocomplete);
		String labelFields = autocomplete.getLabelField();
		String[] fieldArr = labelFields.split(",");
		String valueField = autocomplete.getValueField();
		String[] allFieldArr = null;
		if (StringUtil.isNotEmpty(valueField)) {
			allFieldArr = new String[fieldArr.length + 1];
			for (int i = 0; i < fieldArr.length; i++) {
				allFieldArr[i] = fieldArr[i];
			}
			allFieldArr[fieldArr.length] = valueField;
		}

		try {
			String str = TagUtil.getAutoList(autocomplete, autoList);
			str = "(" + str + ")";
			response.setContentType("application/json;charset=UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.getWriter().write(JSONHelper.listtojson(allFieldArr, allFieldArr.length, autoList));
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * 继承于TSAttachment附件公共列表数据
	 */
	@RequestMapping(params = "objfileGrid")
	public void objfileGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String businessKey = oConvertUtils.getString(request.getParameter("businessKey"));
		String subclassname = oConvertUtils.getString(request.getParameter("subclassname"));// 子类类名
		String type = oConvertUtils.getString(request.getParameter("typename"));
		String code = oConvertUtils.getString(request.getParameter("typecode"));
		String filekey = oConvertUtils.getString(request.getParameter("filekey"));
		CriteriaQuery cq = new CriteriaQuery(MyClassLoader.getClassByScn(subclassname), dataGrid);
		cq.eq("businessKey", businessKey);
		if (StringUtil.isNotEmpty(type)) {
			cq.createAlias("TBInfotype", "TBInfotype");
			cq.eq("TBInfotype.typename", type);
		}
		if (StringUtil.isNotEmpty(filekey)) {
			cq.eq("id", filekey);
		}
		if (StringUtil.isNotEmpty(code)) {
			cq.createAlias("TBInfotype", "TBInfotype");
			cq.eq("TBInfotype.typecode", code);
		}
		cq.add();
		this.commonService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	@RequestMapping(params = "checkUnique")
	@ResponseBody
	public AjaxJson checkUnique(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		String propertyName = request.getParameter("name");
		String entityName = request.getParameter("entityName");
		String newValue = request.getParameter("param");
		String oldValue = request.getParameter("oldValue");
		String uniquemsg = request.getParameter("uniquemsg");
		Map<String, String> param = new HashMap<String, String>();
		param.put("newValue", newValue);
		param.put("oldValue", oldValue);
		Class entityClass = null;
		try {
			entityClass = Class.forName(entityName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error(entityName + "转换成Class出错,请检查" + entityName + "是否存在");
			e.printStackTrace();
		}
		boolean flag = baseService.isUnique(entityClass, param, propertyName);
		if (!flag) {
			result.setStatus("n");
			if (StringUtil.isNotEmpty(uniquemsg))
				result.setInfo(uniquemsg);
		} else {
			result.setStatus("y");
			result.setInfo("通过验证");
		}
		return result;
	}

	@RequestMapping(params = "ajaxForOperationCode")
	@ResponseBody
	public AjaxJson ajaxForOperationCode(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		String moduleCode = null;
		List<ResourceVo> resourceVoList = new ArrayList<ResourceVo>();
		String operationCodes = request.getParameter("operationCode");
		String[] operationCodeArray = operationCodes.split(",");
		String filterCode = "";
		HttpSession session = ContextHolderUtils.getSession();
		//拿到当前登录人Client对象
		Client client = ClientManager.getInstance().getClient(session.getId());
		for (String operationCode : operationCodeArray) {
			String filterType = resourceService.queryFilterTypeByCode(operationCode);
			if (StringUtil.isNotEmpty(filterType)) {
				if ("common".equals(filterType)) {
					filterCode += operationCode + ",";
				} else if ("module".equals(filterType)) {
					//如果传入的code中不包含"_"说明取名格式有问题
					if (operationCode.indexOf("_") >= 0) {

						if (ClientUtil.getUserEntity().getUserName().equals("admin")
								|| !Globals.BUTTON_AUTHORITY_CHECK) {
							filterCode += operationCode + ",";
						} else {
							//第一个下划线之前的就是模块编码
							if (StringUtil.isEmpty(moduleCode)) {
								moduleCode = operationCode.split("_")[0];
							}

							//获得对应模块下的有权url资源编码
							if (resourceVoList.size() == 0 && StringUtil.isNotEmpty(moduleCode)) {
								resourceVoList = client.getResources().get(moduleCode);
							}
							for (ResourceVo resourceVo : resourceVoList) {
								String resourceCode = resourceVo.getOptCode();
								if (resourceCode.equals(operationCode)) {
									filterCode += operationCode + ",";
								}
							}
						}
					}
				}
			}

		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("operationCodes", filterCode);
		result.setObj(map);
		return result;
	}

	@RequestMapping(params = "commonSelect")
	public ModelAndView commonSelect(HttpServletRequest request) {
		String hiddenValue = request.getParameter("hiddenValue");
		String displayValue = request.getParameter("displayValue");
		String gridFieldsJson = request.getParameter("gridFieldsJson");
		try {
			displayValue = URLDecoder.decode(displayValue, "utf-8");
			gridFieldsJson = URLDecoder.decode(gridFieldsJson, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String displayId = request.getParameter("displayId");
		String displayName = request.getParameter("displayName");
		String hiddenId = request.getParameter("hiddenId");
		String hiddenName = request.getParameter("hiddenName");
		Boolean multiples = Boolean.parseBoolean(request.getParameter("multiples"));

		Boolean hasTree = Boolean.parseBoolean(request.getParameter("hasTree"));
		Boolean expandAll = Boolean.parseBoolean(request.getParameter("expandAll"));
		String treeUrl = request.getParameter("treeUrl");
		treeUrl = treeUrl.replaceAll("@@", "&");
		String gridTreeFilter = request.getParameter("gridTreeFilter");
		String callback = request.getParameter("callback");
		String idOrName = request.getParameter("idOrName");
		String url = request.getParameter("url");
		url = url.replaceAll("@@", "&");
		//将字段配置项转换为模型类
		List<GridFieldJsonModel> gridFieldJsonList = JSONHelper.toList(gridFieldsJson, GridFieldJsonModel.class);
		for (GridFieldJsonModel model : gridFieldJsonList) {
			ReflectHelper.iteratorStringFieldsFromBlankToNull(model);
		}
		Map<String, String> idNameMap = new HashMap<String, String>();
		if (StringUtil.isNotEmpty(hiddenValue) && StringUtil.isNotEmpty(displayValue)) {
			String[] hiddenArray = hiddenValue.split(",");
			String[] displayArray = displayValue.split(",");
			if (hiddenArray.length == displayArray.length) {
				for (int i = 0; i < hiddenArray.length; i++) {
					idNameMap.put(hiddenArray[i], displayArray[i]);
				}
			}
		}
		request.setAttribute("idNameMap", idNameMap);
		request.setAttribute("displayId", displayId);
		request.setAttribute("displayName", displayName);
		request.setAttribute("hiddenId", hiddenId);
		request.setAttribute("hiddenName", hiddenName);
		request.setAttribute("multiples", multiples);
		request.setAttribute("url", url);
		request.setAttribute("gridFieldJsonList", gridFieldJsonList);
		request.setAttribute("hasTree", hasTree);
		request.setAttribute("expandAll", expandAll);
		request.setAttribute("treeUrl", treeUrl);
		request.setAttribute("gridTreeFilter", gridTreeFilter);
		request.setAttribute("callback", callback);
		request.setAttribute("idOrName", idOrName);
		return new ModelAndView("common/tag/commonSelect");
	}

	@RequestMapping(params = "editorCommonSelect")
	public ModelAndView editorCommonSelect(HttpServletRequest request) {
		String hiddenValue = request.getParameter("hiddenValue");
		String displayValue = request.getParameter("displayValue");
		String gridFieldsJson = request.getParameter("gridFieldsJson");
		try {
			displayValue = URLDecoder.decode(displayValue, "utf-8");
			gridFieldsJson = URLDecoder.decode(gridFieldsJson, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String displayField = request.getParameter("displayField");
		String hiddenField = request.getParameter("hiddenField");
		Boolean multiples = Boolean.parseBoolean(request.getParameter("multiples"));
		String gridId = request.getParameter("gridId");
		String index = request.getParameter("index");

		Boolean hasTree = Boolean.parseBoolean(request.getParameter("hasTree"));
		Boolean expandAll = Boolean.parseBoolean(request.getParameter("expandAll"));
		String treeUrl = request.getParameter("treeUrl");
		treeUrl = treeUrl.replaceAll("@@", "&");
		String gridTreeFilter = request.getParameter("gridTreeFilter");
		String callback = request.getParameter("callback");
		String url = request.getParameter("url");
		url = url.replaceAll("@@", "&");
		//将字段配置项转换为模型类
		List<GridFieldJsonModel> gridFieldJsonList = JSONHelper.toList(gridFieldsJson, GridFieldJsonModel.class);
		for (GridFieldJsonModel model : gridFieldJsonList) {
			ReflectHelper.iteratorStringFieldsFromBlankToNull(model);
		}
		Map<String, String> idNameMap = new HashMap<String, String>();
		if (StringUtil.isNotEmpty(hiddenValue) && StringUtil.isNotEmpty(displayValue)) {
			String[] hiddenArray = hiddenValue.split(",");
			String[] displayArray = displayValue.split(",");
			if (hiddenArray.length == displayArray.length) {
				for (int i = 0; i < hiddenArray.length; i++) {
					idNameMap.put(hiddenArray[i], displayArray[i]);
				}
			}
		}
		request.setAttribute("idNameMap", idNameMap);
		request.setAttribute("displayField", displayField);
		request.setAttribute("hiddenField", hiddenField);
		request.setAttribute("multiples", multiples);
		request.setAttribute("url", url);
		request.setAttribute("gridFieldJsonList", gridFieldJsonList);
		request.setAttribute("hasTree", hasTree);
		request.setAttribute("expandAll", expandAll);
		request.setAttribute("treeUrl", treeUrl);
		request.setAttribute("gridTreeFilter", gridTreeFilter);
		request.setAttribute("gridId", gridId);
		request.setAttribute("index", index);
		request.setAttribute("callback", callback);
		return new ModelAndView("common/tag/editorCommonSelect");
	}

	/**
	 * 数据字典异步树
	 * @author xiehs
	 * @createtime 2014年8月28日 下午6:22:07
	 * @Decription
	 *
	 * @param module
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "getDictValueTree")
	public void getDictValueTree(HttpServletRequest request, HttpServletResponse response) {
		String parentId = "";
		String dictCode = request.getParameter("dictCode");
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			parentId = request.getParameter("id");
		} else {
			parentId = "-1";
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("parentId", parentId);
		param.put("dictTypeCode", dictCode);
		List<DictValueEntity> dictValueList = this.dictValueService.queryListByPorpertys(param);
		//树的转换
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(), "code");
		List<TreeNode> treeList = TreeMapper.buildJsonTree(dictValueList, propertyMapping);
		TagUtil.tree(response, treeList);
	}

	@RequestMapping(params = "commonExport")
	public void commonExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String title = StringUtil.isEmpty(request.getParameter("title"), null);
		short titleHeight = Short.valueOf(StringUtil.isEmpty(request.getParameter("titleHeight"), "0"));
		short titleFontSize = Short.valueOf(StringUtil.isEmpty(request.getParameter("titleFontSize"), "0"));
		String secondTitle = StringUtil.isEmpty(request.getParameter("secondTitle"), null);
		short secondTitleHeight = Short.valueOf(StringUtil.isEmpty(request.getParameter("secondTitleHeight"), "0"));
		short secondTitleFontSize = Short.valueOf(StringUtil.isEmpty(request.getParameter("secondTitleFontSize"), "0"));
		String sheetName = StringUtil.isEmpty(request.getParameter("sheetName"), null);
		String exclusions = StringUtil.isEmpty(request.getParameter("exclusions"), "");
		short color = Short.valueOf(StringUtil.isEmpty(request.getParameter("color"), "0"));
		short secondColor = Short.valueOf(StringUtil.isEmpty(request.getParameter("color"), "0"));
		short headerColor = Short.valueOf(StringUtil.isEmpty(request.getParameter("headerColor"), "0"));
		String dataHanlder = StringUtil.isEmpty(request.getParameter("dataHanlder"), null);
		String needHandlerFields = StringUtil.isEmpty(request.getParameter("needHandlerFields"), "");
		String entityClassStr = StringUtil.isEmpty(request.getParameter("entityClass"), "");
		Class entityClass = Class.forName(entityClassStr);

		String fileName = StringUtil.isEmpty(request.getParameter("fileName"), "");
		String exportServer = StringUtil.isEmpty(request.getParameter("exportServer"), "");

		ExportParams exportParams = new ExportParams();
		exportParams.setTitle(title);
		exportParams.setTitleHeight(titleHeight);
		exportParams.setTitleFontSize(titleFontSize);
		exportParams.setSecondTitle(secondTitle);
		exportParams.setSecondTitleHeight(secondTitleHeight);
		exportParams.setSecondTitleFontSize(secondTitleFontSize);
		exportParams.setSheetName(sheetName);
		exportParams.setExclusions(exclusions.split(","));
		exportParams.setColor(color);
		exportParams.setSecondColor(secondColor);
		exportParams.setHeaderColor(headerColor);

		if (StringUtil.isNotEmpty(dataHanlder)) {
			IExcelDataHandler dataHanlderInstance = (IExcelDataHandler) Class.forName(dataHanlder).newInstance();
			dataHanlderInstance.setNeedHandlerFields(needHandlerFields.split(","));
			exportParams.setDataHanlder(dataHanlderInstance);
		}

		UcgCache exportCache = ucgCacheManager.getExportCacheBean();

		String gridName = request.getParameter("gridName");
		Object cacheObj = exportCache.get(gridName + "_excel_export_" + ClientUtil.getSessionId());
		List<UserEntity> list = new ArrayList<UserEntity>();
		if (BeanUtils.isNotEmpty(cacheObj)) {
			list = (List<UserEntity>) cacheObj;
		}
		HSSFWorkbook workbook = new HSSFWorkbook();
		//		HSSFWorkbook workbook = ExcelExportUtil.exportExcel(exportParams,entityClass, list);
		//		new UserExcelExport().createSheet(workbook, exportParams, UserVo.class, list);
		if (StringUtil.isNotEmpty(exportServer)) {
			Class cls = Class.forName(exportServer);
			Object obj = cls.newInstance();

			Object[] b = new Object[] { workbook, exportParams, entityClass, list };
			Class[] a = { HSSFWorkbook.class, ExportParams.class, Class.class, Collection.class };
			ReflectHelper.invokeMethod(obj, "createSheet", a, b);
		}

		FileUtils.downloadWorkBook(request, response, workbook, fileName);
	}

	@RequestMapping(params = "goImportExcel")
	public ModelAndView goImportExcel(HttpServletRequest request, HttpServletResponse response) {
		Integer titleRows = Integer.valueOf(request.getParameter("titleRows"));
		Integer headRows = Integer.valueOf(request.getParameter("headRows"));
		Integer startRows = Integer.valueOf(request.getParameter("startRows"));
		Integer startCell = Integer.valueOf(request.getParameter("startCell"));
		Integer endCell = Integer.valueOf(request.getParameter("endCell"));
		Integer keyIndex = Integer.valueOf(request.getParameter("keyIndex"));
		Integer sheetNum = Integer.valueOf(request.getParameter("sheetNum"));
		Boolean needSave = Boolean.valueOf(request.getParameter("needSave"));
		String dataHanlder = StringUtil.isEmpty(request.getParameter("dataHanlder"), "");
		String needHandlerFields = StringUtil.isEmpty(request.getParameter("needHandlerFields"), "");
		String templateCode = StringUtil.isEmpty(request.getParameter("templateCode"), "");
		String name = StringUtil.isEmpty(request.getParameter("name"), "");
		String submitUrl = StringUtil.isEmpty(request.getParameter("submitUrl"), "");
		String entityClass = StringUtil.isEmpty(request.getParameter("entityClass"), "");

		ResourceBundle tempBundlePath = ResourceBundle.getBundle("ucg/importTemplate");
		String templatePath = tempBundlePath.getString(templateCode);
		String fileName = templatePath.substring(templatePath.lastIndexOf("/") + 1, templatePath.length());

		request.setAttribute("fileName", fileName);
		request.setAttribute("titleRows", titleRows);
		request.setAttribute("headRows", headRows);
		request.setAttribute("startRows", startRows);
		request.setAttribute("startCell", startCell);
		request.setAttribute("endCell", endCell);
		request.setAttribute("keyIndex", keyIndex);
		request.setAttribute("sheetNum", sheetNum);
		request.setAttribute("needSave", needSave);
		request.setAttribute("dataHanlder", dataHanlder);
		request.setAttribute("needHandlerFields", needHandlerFields);
		request.setAttribute("templateCode", templateCode);
		request.setAttribute("name", name);
		request.setAttribute("submitUrl", submitUrl);
		request.setAttribute("entityClass", entityClass);
		return new ModelAndView("common/poi/excelImport");
	}

	@RequestMapping(params = "downloadTemplate")
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
		try {
			String templateCode = request.getParameter("templateCode");
			ResourceBundle tempBundlePath = ResourceBundle.getBundle("ucg/importTemplate");
			ResourceBundle sysBundlePath = ResourceBundle.getBundle("sysConfig");
			String templatePath = tempBundlePath.getString(templateCode);
			String basePath = sysBundlePath.getString("uploadpath");

			String fileName = templatePath.substring(templatePath.lastIndexOf("/") + 1, templatePath.length());

			String fullPath = basePath + File.separator + templatePath;
			FileUtils.downLoadFile(request, response, fullPath, fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(params = "importExcel")
	public void importExcel(MultipartHttpServletRequest request, HttpServletResponse response) {
		try {
			MultipartFile file = request.getFile("file[]");
			InputStream is = file.getInputStream();
			ImportParams importParams = new ImportParams();

			Integer titleRows = Integer.valueOf(request.getParameter("titleRows"));
			Integer headRows = Integer.valueOf(request.getParameter("headRows"));
			Integer startRows = Integer.valueOf(request.getParameter("startRows"));
			Integer startCell = Integer.valueOf(request.getParameter("startCell"));
			Integer endCell = Integer.valueOf(request.getParameter("endCell"));
			Integer keyIndex = Integer.valueOf(request.getParameter("keyIndex"));
			Integer sheetNum = Integer.valueOf(request.getParameter("sheetNum"));
			Boolean needSave = Boolean.valueOf(request.getParameter("needSave"));
			String dataHanlder = StringUtil.isEmpty(request.getParameter("dataHanlder"), "");
			String needHandlerFields = StringUtil.isEmpty(request.getParameter("needHandlerFields"), "");
			String entityClassStr = StringUtil.isEmpty(request.getParameter("entityClass"), "");
			Class entityClass = Class.forName(entityClassStr);

			if (StringUtil.isNotEmpty(dataHanlder)) {
				IExcelDataHandler dataHanlderInstance = (IExcelDataHandler) Class.forName(dataHanlder).newInstance();
				dataHanlderInstance.setNeedHandlerFields(needHandlerFields.split(","));
				importParams.setDataHanlder(dataHanlderInstance);
			}

			importParams.setTitleRows(titleRows);
			importParams.setHeadRows(headRows);
			importParams.setStartRows(startRows);
			importParams.setStartCell(startCell);
			importParams.setEndCell(endCell);
			importParams.setKeyIndex(keyIndex);
			importParams.setSheetNum(sheetNum);
			importParams.setNeedSave(needSave);

			ExcelImportResult result = ExcelImportUtil.importExcelByIsForResult(is, entityClass, importParams);
			//将数据设置到缓存中
			setExcelImportData(request, result, ClientUtil.getSessionId());
			//构造返回用于展示json对象
			JSONObject main = new JSONObject();
			main.accumulate("correctCount", result.getCorrectCount());
			main.accumulate("errorCount", result.getErrorCount());
			main.accumulate("allCount", result.getAllCount());
			main.accumulate("correctResult", result.getCorrectArrayResult().values());
			main.accumulate("errorResult", result.getErrorArrayResult().values());
			String jsonStr = JSONHelper.toJSONString(main);
			TagUtil.responseWrite(response, jsonStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(params = "downloadError")
	public void downloadError(HttpServletRequest request, HttpServletResponse response) {
		try {
			String tempName = request.getParameter("tempName");
			if (StringUtil.isNotEmpty(tempName)) {
				tempName = "(错误待修改)" + tempName;
			} else {
				tempName = "错误待修改excel.xls";
			}
			ExcelImportResult result = getExcelImportData(request, ClientUtil.getSessionId());
			if (BeanUtils.isNotEmpty(result)) {
				Workbook workbook = result.getWorkbook();
				File file = POIPublicUtil.copyWorkBookToFile(workbook);
				Workbook downloadBook = POIPublicUtil.getWorkBook(new FileInputStream(file));
				Workbook reloadBook = POIPublicUtil.getWorkBook(new FileInputStream(file));
				result.setWorkbook(reloadBook);
				if (file.exists()) {
					file.delete();
				}
				FileUtils.downloadWorkBook(request, response, downloadBook, tempName);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
