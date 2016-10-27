package com.xplatform.base.framework.tag.core.easyui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.xplatform.base.framework.core.common.model.json.ComboBox;
import com.xplatform.base.framework.core.common.service.CommonService;
import com.xplatform.base.framework.core.constant.Globals;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.vo.easyui.ColumnValue;
import com.xplatform.base.framework.tag.vo.easyui.DataGridColumn;
import com.xplatform.base.framework.tag.vo.easyui.DataGridUrl;
import com.xplatform.base.framework.tag.vo.easyui.InputComponentType;
import com.xplatform.base.framework.tag.vo.easyui.OptTypeDirection;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.dict.service.DictTypeService;
/**
 * 
 * 类描述：DATAGRID标签处理类
 * 
 @author xiehs
 * @date： 日期：2012-12-7 时间：上午10:17:45
 * @version 1.0
 */
@SuppressWarnings({"serial","rawtypes","unchecked","static-access"})
public class DataGridTag extends TagSupport {
	/** 原生属性 **/
	private String resizeHandle;//调整列的位置，可用的值有：'left','right','both'。在使用'right'的时候用户可以通过拖动右侧边缘的列标题调整列，等等。
	private Boolean autoRowHeight;//定义设置行的高度，根据该行的内容。设置为false可以提高负载性能。
	private Boolean striped;//是否显示斑马线效果。
	private String method;//该方法类型请求远程数据。
	private Boolean nowrap;//如果为true，则在同一行中显示数据。设置为true可以提高加载性能。
	private String loadMsg;//在从远程站点加载数据的时候显示提示消息。
	private Boolean rownumbers=true;//如果为true，则显示一个行号列。
	private Boolean singleSelect;//如果为true，则只允许选择一行。
	private Boolean checkOnSelect;//如果为true,当用户点击行的时候该复选框就会被选中或取消选中;如果为false,当用户仅在点击该复选框的时候才会呗选中或取消。
	private Boolean selectOnCheck;//如果为true,单击复选框将永远选择行;如果为false，选择行将不选中复选框。
	private Boolean remoteSort;//定义从服务器对数据进行排序。
	private Boolean showHeader;//定义是否显示行头。
	private Boolean showFooter;//定义是否显示行脚。
	private Integer scrollbarSize;//滚动条的宽度(当滚动条是垂直的时候)或高度(当滚动条是水平的时候)。
	private Boolean border=true;//列表是否需要边框
	private Boolean animate;//定义在节点展开或折叠的时候是否显示动画效果。
	
	protected List<ColumnValue> columnValueList = new ArrayList<ColumnValue>();// 值替换集合
	protected List<ColumnValue> columnStyleList = new ArrayList<ColumnValue>();// 颜色替换集合
	protected List<DataGridUrl> urlList = new ArrayList<DataGridUrl>();// 列表操作显示
	protected List<DataGridUrl> toolBarList = new ArrayList<DataGridUrl>();// 工具条列表
	protected List<DataGridColumn> columnList = new ArrayList<DataGridColumn>();// 所有列表字段
	protected List<DataGridColumn> queryColumnList = new ArrayList<DataGridColumn>();// 列表中需要查询的字段
	
	public Map<String, Object> map;// 封装查询条件
	protected static Map<String,String> syscode = new HashMap<String,String>();//json转换中的系统保留字
	protected Map<String,String> cascadeMap=new HashMap<String,String>() ;//级联使用的map,key为组名,value为字段的字符串拼接(例如"fieldA,fieldB,fieldC")
	
	private Boolean showButtonDiv=true;//定制是否显示列表上方操作按钮Div
	private Boolean showOptColumn=true;//定制是否有操作栏按钮(没有的话,用于去除操作栏)
	private Boolean showButtonDivResult=true;//最终是否显示列表上方操作按钮Div
	private Boolean hasOptButtonResult=false;//最终是否有操作栏按钮(没有的话,用于去除操作栏)
	protected boolean hasQueryColum = false;// 列表中是否有查询字段
	FreemarkerHelper viewEngine = new FreemarkerHelper();
	
	protected String fields = "";// 显示字段
	protected String searchFields = "";// 区间查询的支持
	protected String name;// 表格标示
	protected String title;// 表格标示
	protected String idField="id";// 主键字段
	protected boolean treegrid = false;// 是否是树形列表
	
	
	/** datagrid分页相关 **/
	public int allCount;
	public int curPageNo;
	public int pageSize = 10;
	public boolean pagination = true;// 是否显示分页
	private Integer pageNumber;//在设置分页属性的时候初始化页码。
	private String sortName;//定义的列进行排序
	private String sortOrder = "asc";//定义列的排序顺序，只能是"递增"或"降序".
	private boolean showRefresh = true;// 定义是否显示刷新按钮
	private boolean showText = true;// 定义是否显示刷新按钮
	private boolean showPageList = true;// 定义是否显示页面列表
	private String pagePosition;//定义分页工具栏的位置。可用的值有：'top','bottom','both'。
	
	private String actionUrl;// 分页提交路径
	private String width;
	private String height;
	private boolean editable=false;//表格是否可编辑
	private boolean defaultOpt=false;//可编辑表格是否显示默认的操作
	private boolean checkbox = true;// 是否显示复选框
	
	private boolean openFirstNode = false;//是不是展开第一个节点
	private boolean fit = true;// 是否允许表格自动缩放，以适应父容器
	private boolean fitColumns = true;// 当为true时，自动展开/合同列的大小，以适应的宽度，防止横向滚动.
	
	private String style = "easyui";// 列表样式easyui,datatables
	private String onLoadSuccess;// 数据加载完成调用方法
	private String onClick;// 单击事件调用方法
	private String onDblClickRow;// 双击行事件调用方法
	private String queryMode = "group";//查询模式
	private String entityName;//对应的实体对象
	private String rowStyler;//rowStyler函数
	private String extendParams;//扩展参数,easyui有的,但是jeecg没有的参数进行扩展
	private boolean autoLoadData=true; // 列表是否自动加载数据

	private String allOptButtonTypes = "Deff,Del,Fun,OpenWin,Confirm,OpenTab";//所有的操作栏按钮type字符集合
	private boolean statistics=false;//是否是统计功能使用的datagrid(查询框的name会变化)
	private String view;//datagrid视图类型
	
	/**** 分组视图相关 ****/
	private String groupField;//datagrid分组字段
	private String groupFormatter;//datagrid分组函数,返回分组栏上的内容,范例groupFormatter="test(value,rows)" 自己去定义test方法 返回一段字符串
	//private boolean frozenColumn=false; // 是否是冰冻列    默认不是
	
	private String flag;//页面中有多个datagrid时,需要用jquery统一取到其中所有的或某几个datagrid,可以用此属性
	
	/**** 列表导入/导出相关 ****/
	protected String exportParams;//导出所需参数,可以直接设置,也可以通过t:dgExportParams设置(url参数形式,形如:&title=人员列表&titleHeight=5)
	protected String importParams;//导入所需参数,可以直接设置,也可以通过t:dgImportParams设置(url参数形式,形如:&title=人员列表&titleHeight=5)
	protected String submitUrl;//导入后自定义处理请求
	static{
		syscode.put("class", "clazz");
	}
	
	
	public void setOnLoadSuccess(String onLoadSuccess) {
		this.onLoadSuccess = onLoadSuccess;
	}

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	public void setShowText(boolean showText) {
		this.showText = showText;
	}

	public void setPagination(boolean pagination) {
		this.pagination = pagination;
	}

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTreegrid(boolean treegrid) {
		this.treegrid = treegrid;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setIdField(String idField) {
		this.idField = idField;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFit(boolean fit) {
		this.fit = fit;
	}

	public void setShowPageList(boolean showPageList) {
		this.showPageList = showPageList;
	}

	public void setShowRefresh(boolean showRefresh) {
		this.showRefresh = showRefresh;
	}

	/**
	 * 设置级联分组Map
	 */
	public void setCascadeGroup(String groupName, Integer cascadeIndex, String fieldName) {
		if (cascadeMap.containsKey(groupName)) {//如果map中存在,则拼接字段到value
			String oldValue = cascadeMap.get(groupName);
			oldValue += "," + fieldName;
			cascadeMap.put(groupName, oldValue);
		} else { //如果map中不存在则增加对应key并设置value
			cascadeMap.put(groupName, fieldName);
		}
	}
	
	/**
	 * 设置询问操作URL
	 */
	public void setConfUrl(String url, String title, String message,
			String exp, String operationCode, String icon, String exParams,Boolean isShowInViewPage) {
		DataGridUrl dataGridUrl = new DataGridUrl();
		dataGridUrl.setTitle(title);
		dataGridUrl.setUrl(url);
		dataGridUrl.setType(OptTypeDirection.Confirm);
		dataGridUrl.setMessage(message);
		dataGridUrl.setExp(exp);
		dataGridUrl.setIcon(icon);
		dataGridUrl.setExParams(exParams);
		dataGridUrl.setIsShowInViewPage(isShowInViewPage);
		installOperationCode(dataGridUrl, operationCode, urlList,1);
	}

	/**
	 * 设置删除操作URL
	 */
	public void setDelUrl(String url, String title, String message, String exp,
			String funname, String operationCode, String callback, String icon,
			String exParams,Boolean isShowInViewPage) {
		DataGridUrl dataGridUrl = new DataGridUrl();
		dataGridUrl.setTitle(title);
		dataGridUrl.setUrl(url);
		dataGridUrl.setType(OptTypeDirection.Del);
		dataGridUrl.setMessage(message);
		dataGridUrl.setExp(exp);
		dataGridUrl.setFunname(funname);
		dataGridUrl.setCallback(callback);
		dataGridUrl.setIcon(icon);
		dataGridUrl.setExParams(exParams);
		dataGridUrl.setIsShowInViewPage(isShowInViewPage);
		installOperationCode(dataGridUrl, operationCode, urlList,1);
	}

	/**
	 * 设置默认操作URL
	 */
	public void setDefUrl(String url, String title, String exp,
			String operationCode, String icon, String exParams,Boolean isShowInViewPage) {
		DataGridUrl dataGridUrl = new DataGridUrl();
		dataGridUrl.setTitle(title);
		dataGridUrl.setUrl(url);
		dataGridUrl.setType(OptTypeDirection.Deff);
		dataGridUrl.setExp(exp);
		dataGridUrl.setIcon(icon);
		dataGridUrl.setExParams(exParams);
		dataGridUrl.setIsShowInViewPage(isShowInViewPage);
		installOperationCode(dataGridUrl, operationCode, urlList,1);

	}
	/**
	 * 设置工具条
	 * @param height2 
	 * @param width2 
	 */
	public void setToolbar(String url, String title, String icon, String exp,
			String onclick, String funname, String operationCode,
			String width2, String height2, String callback,
			OptTypeDirection optTypeDirection, String message,
			Integer preinstallWidth, String exParams,String id,String rowId) {
		DataGridUrl dataGridUrl = new DataGridUrl();
		dataGridUrl.setTitle(title);
		dataGridUrl.setUrl(url);
		dataGridUrl.setType(optTypeDirection);
		dataGridUrl.setIcon(icon);
		dataGridUrl.setOnclick(onclick);
		dataGridUrl.setExp(exp);
		dataGridUrl.setFunname(funname);
		dataGridUrl.setWidth(String.valueOf(width2));
		dataGridUrl.setHeight(String.valueOf(height2));
		dataGridUrl.setCallback(callback);
		dataGridUrl.setMessage(message);
		dataGridUrl.setPreinstallWidth(preinstallWidth);
		dataGridUrl.setExParams(exParams);
		dataGridUrl.setId(id);
		dataGridUrl.setRowId(rowId);
		installOperationCode(dataGridUrl, operationCode, toolBarList,2);
		
	}

	/**
	 * 设置自定义函数操作URL
	 */
	public void setFunUrl(String title, String exp, String funname,
			String operationCode, String icon, String exParams,Boolean isShowInViewPage) {
		DataGridUrl dataGridUrl = new DataGridUrl();
		dataGridUrl.setTitle(title);
		dataGridUrl.setType(OptTypeDirection.Fun);
		dataGridUrl.setExp(exp);
		dataGridUrl.setFunname(funname);
		dataGridUrl.setIcon(icon);
		dataGridUrl.setExParams(exParams);
		dataGridUrl.setIsShowInViewPage(isShowInViewPage);
		installOperationCode(dataGridUrl, operationCode, urlList,1);

	}

	/**
	 * 设置自定义函数操作URL
	 */
	public void setOpenUrl(String url, String title, String width,
			String height, String exp, String operationCode,
			Integer preinstallWidth, String icon, String exParams,Boolean isShowInViewPage) {
		DataGridUrl dataGridUrl = new DataGridUrl();
		dataGridUrl.setTitle(title);
		dataGridUrl.setUrl(url);
		dataGridUrl.setWidth(width);
		dataGridUrl.setHeight(height);
		dataGridUrl.setType(OptTypeDirection.OpenWin);
		dataGridUrl.setExp(exp);
		dataGridUrl.setPreinstallWidth(preinstallWidth);
		dataGridUrl.setIcon(icon);
		dataGridUrl.setExParams(exParams);
		dataGridUrl.setIsShowInViewPage(isShowInViewPage);
		installOperationCode(dataGridUrl, operationCode, urlList,1);

	}

	/**
	 * 设置提供导出所需参数标签
	 */
	public void setExportParam(String title, short titleHeight, short titleFontSize, String secondTitle,
			short secondTitleHeight, short secondTitleFontSize, String sheetName, String exclusions, short color,
			short headerColor, String dataHanlder, String needHandlerFields, String fileName, String exportServer,
			String entityClass) {
		title = StringUtil.isEmpty(title, "");
		secondTitle = StringUtil.isEmpty(secondTitle, "");
		sheetName = StringUtil.isEmpty(sheetName, "");
		exclusions = StringUtil.isEmpty(exclusions, "");
		dataHanlder = StringUtil.isEmpty(dataHanlder, "");
		needHandlerFields = StringUtil.isEmpty(needHandlerFields, "");
		entityClass = StringUtil.isEmpty(entityClass, "");

		StringBuffer sb = new StringBuffer();
		sb.append("&title=").append(title);
		sb.append("&titleHeight=").append(titleHeight);
		sb.append("&titleFontSize=").append(titleFontSize);
		sb.append("&secondTitle=").append(secondTitle);
		sb.append("&secondTitleHeight=").append(secondTitleHeight);
		sb.append("&secondTitleFontSize=").append(secondTitleFontSize);
		sb.append("&sheetName=").append(sheetName);
		sb.append("&exclusions=").append(exclusions);
		sb.append("&color=").append(color);
		sb.append("&headerColor=").append(headerColor);
		sb.append("&dataHanlder=").append(dataHanlder);
		sb.append("&needHandlerFields=").append(needHandlerFields);
		sb.append("&fileName=").append(fileName);
		sb.append("&exportServer=").append(exportServer);
		sb.append("&entityClass=").append(entityClass);
		exportParams = sb.toString();
	}
	
	/**
	 * 设置提供导入所需参数标签
	 */
	public void setImportParam(Integer titleRows, Integer headRows, Integer startRows, Integer startCell,
			Integer endCell, Integer keyIndex, Integer sheetNum, boolean needSave, String dataHanlder,
			String needHandlerFields, String templateCode, String name, String submitUrl, String entityClass) {
		templateCode = StringUtil.isEmpty(templateCode, "");
		name = StringUtil.isEmpty(name, "");
		submitUrl = StringUtil.isEmpty(submitUrl, "");
		dataHanlder = StringUtil.isEmpty(dataHanlder, "");
		needHandlerFields = StringUtil.isEmpty(needHandlerFields, "");
		entityClass = StringUtil.isEmpty(entityClass, "");

		String resultName = StringUtil.isNotEmpty(name) ? name : this.name;
		String parseSubmitUrl = "";
		String oldSubmitUrl = submitUrl + "&name=" + resultName;
		try {
			parseSubmitUrl = URLEncoder.encode(oldSubmitUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.submitUrl = oldSubmitUrl;
		StringBuffer sb = new StringBuffer();
		sb.append("&titleRows=").append(titleRows);
		sb.append("&headRows=").append(headRows);
		sb.append("&startRows=").append(startRows);
		sb.append("&startCell=").append(startCell);
		sb.append("&endCell=").append(endCell);
		sb.append("&keyIndex=").append(keyIndex);
		sb.append("&sheetNum=").append(sheetNum);
		sb.append("&needSave=").append(needSave);
		sb.append("&dataHanlder=").append(dataHanlder);
		sb.append("&needHandlerFields=").append(needHandlerFields);
		sb.append("&templateCode=").append(templateCode);
		sb.append("&needHandlerFields=").append(needHandlerFields);
		sb.append("&name=").append(resultName);
		sb.append("&submitUrl=").append(parseSubmitUrl);
		sb.append("&entityClass=").append(entityClass);
		importParams = sb.toString();
	}
	
	/**
	 * 
	 * <b>Summary: </b> setColumn(设置字段)
	 * 
	 * @param name
	 * @param text
	 * @param value
	 */
	public void setColumn(String title, String field, String width, String rowspan, String colspan, String align,
			boolean sortable, boolean checkbox, String formatter, boolean hidden, String replace, String treefield,
			boolean image, String imageSize, boolean query, String url, String funname, String arg, String queryMode,
			String dictCode, boolean frozenColumn, String extend, String style, String downloadName, boolean isAuto,
			String extendParams, String editType, String data, String queryInputType, String comboUrl,
			String queryExParams, String editorParams, String sql, String myFormatter, String compareSign,
			String groupName, Integer cascadeIndex, boolean aysn, String precision,String halign) {
		DataGridColumn dataGridColumn = new DataGridColumn();
		dataGridColumn.setAlign(align);
		dataGridColumn.setCheckbox(checkbox);
		dataGridColumn.setColspan(colspan);
		dataGridColumn.setField(field);
		dataGridColumn.setFormatter(formatter);
		dataGridColumn.setHidden(hidden);
		dataGridColumn.setRowspan(rowspan);
		dataGridColumn.setSortable(sortable);
		//将操作那一列的标题去掉
		title = "opt".equals(field) ? "" : title;
		dataGridColumn.setTitle(title);
		dataGridColumn.setWidth(width);
		dataGridColumn.setTreefield(treefield);
		dataGridColumn.setImage(image);
		dataGridColumn.setImageSize(imageSize);
		dataGridColumn.setReplace(replace);
		dataGridColumn.setQuery(query);
		dataGridColumn.setUrl(url);
		dataGridColumn.setFunname(funname);
		dataGridColumn.setArg(arg);
		dataGridColumn.setQueryMode(queryMode);
		dataGridColumn.setDictCode(dictCode);
		dataGridColumn.setFrozenColumn(frozenColumn);
		dataGridColumn.setExtend(extend);
		dataGridColumn.setStyle(style);
		dataGridColumn.setDownloadName(downloadName);
		dataGridColumn.setAutocomplete(isAuto);
		dataGridColumn.setExtendParams(extendParams);
		dataGridColumn.setEditType(editType);
		dataGridColumn.setData(data);
		dataGridColumn.setComboUrl(comboUrl);
		dataGridColumn.setQueryInputType(InputComponentType.valueOf(queryInputType));
		dataGridColumn.setQueryExParams(queryExParams);
		dataGridColumn.setEditorParams(editorParams);
		dataGridColumn.setMyFormatter(myFormatter);
		dataGridColumn.setSql(sql);
		dataGridColumn.setCompareSign(compareSign);
		dataGridColumn.setGroupName(groupName);
		dataGridColumn.setCascadeIndex(cascadeIndex);
		dataGridColumn.setAysn(aysn);
		dataGridColumn.setPrecision(precision);
		dataGridColumn.setHalign(halign);
		columnList.add(dataGridColumn);

		if (groupName != null) {
			setCascadeGroup(groupName, cascadeIndex, field);
		}

		if (query) {
			queryColumnList.add(dataGridColumn);
			hasQueryColum = true;
		}

		if (field != "opt") {
			fields += field + ",";
			if ("group".equals(queryMode)) {
				searchFields += field + "," + field + "_begin," + field + "_end,";
			} else {
				searchFields += field + ",";
			}
		}
		if (replace != null) {
			String[] test = replace.split(",");
			String text = "";
			String value = "";
			for (String string : test) {
				text += string.substring(0, string.indexOf("_")) + ",";
				value += string.substring(string.indexOf("_") + 1) + ",";
			}
			setColumn(field, text, value);

		}
		if (!StringUtils.isBlank(dictCode)) {
			DictTypeService dictTypeService = ApplicationContextUtil.getBean("dictTypeService");
			String dictType = dictTypeService.findDictTypeByCode(dictCode);
			List<ComboBox> valueList = new ArrayList<ComboBox>();
			if ("tree".equals(dictType)) {
				valueList = dictTypeService.transformToComboBox(dictCode);
			} else if ("selected".equals(dictType)) {
				valueList = dictTypeService.findCacheByCode(dictCode);
			}

			if (valueList != null && valueList.size() > 0) {
				String text = "";
				String value = "";
				for (ComboBox combo : valueList) {
					text += combo.getText() + ",";
					value += combo.getId() + ",";

				}
				setColumn(field, text, value);
			}
		}
		if (!StringUtils.isBlank(sql)) {
			CommonService systemService = ApplicationContextUtil.getContext().getBean(CommonService.class);
			List<Map<String, Object>> list = systemService.findForJdbc(sql);
			if (BeanUtils.isNotEmpty(list)) {
				String text = "";
				String value = "";
				for (Map<String, Object> map : list) {
					text += map.get("code") + ",";
					value += map.get("name") + ",";
				}
				setColumn(field, text, value);
			}
		}
		/*if (!StringUtils.isBlank(dictCode)) {
			if (dictCode.contains(",")) {
				String[] dic = dictCode.split(",");
				String text = "";
				String value = "";
				String sql = "select " + dic[1] + " as field," + dic[2]
						+ " as text from " + dic[0];
				systemService = ApplicationContextUtil.getContext().getBean(
						SystemService.class);
				List<Map<String, Object>> list = systemService.findForJdbc(sql);
				for (Map<String, Object> map : list) {
					text += map.get("text") + ",";
					value += map.get("field") + ",";
				}
				if (list.size() > 0)
					setColumn(field, text, value);
			} else {
				String text = "";
				String value = "";
				List<TSType> typeList = TSTypegroup.allTypes.get(dictCode
						.toLowerCase());
				if (typeList != null && !typeList.isEmpty()) {
					for (TSType type : typeList) {
						text += type.getTypename() + ",";
						value += type.getTypecode() + ",";
					}
					setColumn(field, text, value);
				}
			}
		}*/
		if (StringUtil.isNotEmpty(style)) {
			String[] temp = style.split(",");
			String text = "";
			String value = "";
			if (temp.length == 1 && temp[0].indexOf("_") == -1) {
				text = temp[0];
			} else {
				for (String string : temp) {
					text += string.substring(0, string.indexOf("_")) + ",";
					value += string.substring(string.indexOf("_") + 1) + ",";
				}
			}
			setStyleColumn(field, text, value);
		}
	}
	
	/**
	 * 设置 颜色替换值
	 * @param field
	 * @param text
	 * @param value
	 */
	private void setStyleColumn(String field, String text, String value) {
		ColumnValue columnValue = new ColumnValue();
		columnValue.setName(field);
		columnValue.setText(text);
		columnValue.setValue(value);
		columnStyleList.add(columnValue);
	}

	/**
	 * 
	 * <b>Summary: </b> setColumn(设置字段替换值)
	 * 
	 * @param name
	 * @param text
	 * @param value
	 */
	public void setColumn(String name, String text, String value) {
		ColumnValue columnValue = new ColumnValue();
		columnValue.setName(name);
		columnValue.setText(text);
		columnValue.setValue(value);
		columnValueList.add(columnValue);
	}

	public int doStartTag() throws JspTagException {
		// 清空资源
		urlList.clear();
		toolBarList.clear();
		columnValueList.clear();
		columnStyleList.clear();
		queryColumnList.clear();
		columnList.clear();
		cascadeMap.clear();
		fields = "";
		searchFields = "";
		return EVAL_PAGE;
	}

	
	public int doEndTag() throws JspException {
		try {
			JspWriter out = this.pageContext.getOut();
			if (style.equals("easyui")) {
				/*String searchString="";
				String scriptString="";
				if(hasQueryColum && StringUtil.equals("separate", queryMode)){
					String searchDiv=getQuerySeparateString();
					searchString="<div id=\"page_search\" style=\"overflow:visible;\">"
				           	 +"<div class=\"row\">"
				             +"<div class=\"col-xs-12 col-md-12\">"
				                 +"<div class=\"widget collapsed\">"
				                     +"<div class=\"widget-header \">"
				                         +"<span class=\"widget-caption\">"+title+"搜索</span>"
				                         +"<div class=\"widget-buttons\">"
				                             +"<a href=\"#\" data-toggle=\"maximize\">"
				                                 +"<i class=\"fa fa-expand icon-color\"></i>"
				                             +"</a>"
				                             +"<a href=\"#\" data-toggle=\"collapse\">"
				                                 +"<i class=\"fa fa-plus icon-color\"></i>"
				                             +"</a>"
				                             +"<a href=\"#\" data-toggle=\"dispose\">"
				                                 +"<i class=\"fa fa-times icon-color\"></i>"
				                             +"</a>"
				                         +"</div>"
				                     +"</div>"
				                     +"<div class=\"widget-body\"><div style=\"padding:12px;\">"
				                        +searchDiv
				                     +"</div></div>"
				                 +"</div>"
				             +"</div>"
				         +"</div> "
				     +"</div>";
					scriptString="<script type=\"text/javascript\">$(function() {InitiateWidgets();});</script>";
				}*/
				out.print(end().toString());
			} else {
				out.print(datatables().toString());
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//重置属性,防止生命周期内重用
		showButtonDiv = true;
		showOptColumn = true;
		showButtonDivResult = true;
		hasOptButtonResult = false;
		hasQueryColum = false;
		return EVAL_PAGE;
	}

	/**
	 * datatables构造方法
	 * 
	 * @return
	 */
	public StringBuffer datatables() {
		StringBuffer sb = new StringBuffer();
		sb.append("<script type=\"text/javascript\">");
		sb.append("$(document).ready(function() {");
		sb.append("var oTable = $(\'#userList\').dataTable({");
		// sb.append(
		// "\"sDom\" : \"<\'row\'<\'span6\'l><\'span6\'f>r>t<\'row\'<\'span6\'i><\'span6\'p>>\",");
		sb.append("\"bProcessing\" : true,");// 当datatable获取数据时候是否显示正在处理提示信息"
		sb.append("\"bPaginate\" : true,"); // 是否分页"
		sb.append("\"sPaginationType\" : \"full_numbers\",");// 分页样式full_numbers,"
		sb.append("\"bFilter\" : true,");// 是否使用内置的过滤功能"
		sb.append("\"bSort\" : true, ");// 排序功能"
		sb.append("\"bAutoWidth\" : true,");// 自动宽度"
		sb.append("\"bLengthChange\" : true,");// 是否允许用户自定义每页显示条数"
		sb.append("\"bInfo\" : true,");// 页脚信息"
		sb.append("\"sAjaxSource\" : \"userController.do?test\",");
		sb.append("\"bServerSide\" : true,");// 指定从服务器端获取数据
		sb.append("\"oLanguage\" : {" + "\"sLengthMenu\" : \" _MENU_ 条记录\"," + "\"sZeroRecords\" : \"没有检索到数据\"," + "\"sInfo\" : \"第 _START_ 至 _END_ 条数据 共 _TOTAL_ 条\"," + "\"sInfoEmtpy\" : \"没有数据\"," + "\"sProcessing\" : \"正在加载数据...\"," + "\"sSearch\" : \"搜索\"," + "\"oPaginate\" : {" + "\"sFirst\" : \"首页\"," + "\"sPrevious\" : \"前页\", " + "\"sNext\" : \"后页\"," + "\"sLast\" : \"尾页\"" + "}" + "},"); // 汉化
		// 获取数据的处理函数 \"data\" : {_dt_json : JSON.stringify(aoData)},
		sb.append("\"fnServerData\" : function(sSource, aoData, fnCallback, oSettings) {");
		// + "\"data\" : {_dt_json : JSON.stringify(aoData)},"
		sb.append("oSettings.jqXHR = $.ajax({" + "\"dataType\" : \'json\'," + "\"type\" : \"POST\"," + "\"url\" : sSource," + "\"data\" : aoData," + "\"success\" : fnCallback" + "});},");
		sb.append("\"aoColumns\" : [ ");
		int i = 0;
		for (DataGridColumn column : columnList) {
			i++;
			sb.append("{");
			sb.append("\"sTitle\":\"" + column.getTitle() + "\"");
			if (column.getField().equals("opt")) {
				sb.append(",\"mData\":\"" + idField + "\"");
				sb.append(",\"sWidth\":\"20%\"");
				sb.append(",\"bSortable\":false");
				sb.append(",\"bSearchable\":false");
				sb.append(",\"mRender\" : function(data, type, rec) {");
				this.getOptUrl(sb);
				sb.append("}");
			} else {
				int colwidth = (column.getWidth() == null) ? column.getTitle().length() * 15 : Integer.parseInt(column.getWidth());
				sb.append(",\"sName\":\"" + column.getField() + "\"");
				sb.append(",\"mDataProp\":\"" + column.getField() + "\"");
				sb.append(",\"mData\":\"" + column.getField() + "\"");
				sb.append(",\"sWidth\":\"" + colwidth + "\"");
				sb.append(",\"bSortable\":" + column.isSortable() + "");
				sb.append(",\"bVisible\":" + column.isHidden() + "");
				sb.append(",\"bSearchable\":" + column.isQuery() + "");
			}
			sb.append("}");
			if (i < columnList.size())
				sb.append(",");
		}

		sb.append("]" + "});" + "});" + "</script>");
		sb.append("<table width=\"100%\"  class=\"" + style + "\" id=\"" + name + "\" toolbar=\"#" + name + "tb\"></table>");
		return sb;

	}

	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * easyui构造方法
	 * 
	 * @return
	 */
	public StringBuffer end() {
		String grid = "";
		StringBuffer sb = new StringBuffer();
		
		
		optFlagHandler();
		
		if ((toolBarList.size() == 0 && !defaultOpt) || !showButtonDiv) {
			showButtonDivResult = false;
		}
		if (!showOptColumn) {
			hasOptButtonResult = false;
		}
		sb.append("<script type=\"text/javascript\">");
		sb.append("$(function(){");
		//sb.append(this.getNoAuthOperButton());
		if (treegrid) {
			grid = "treegrid";
			sb.append("$(\'#" + name + "\').treegrid({");
			sb.append("idField:'id',");
			sb.append("treeField:'text',");
			sb.append("onLoadSuccess:function(row,data){$(\"#"+name+"\")."+grid+"('options').queryParams.loadFlag=true;$(\"#"+name+"\")."+grid+"(\"clearSelections\");");
			if(openFirstNode){
				sb.append(" if(data==null){");
				sb.append(" var firstNode = $(\'#" + name + "\').treegrid('getRoots')[0];");
				sb.append(" $(\'#" + name + "\').treegrid('expand',firstNode.id)}");
			}
			if (StringUtil.isNotEmpty(onLoadSuccess)) {
				if (onLoadSuccess.indexOf("(") != -1) {
					sb.append(onLoadSuccess + ";");
				}else{
					sb.append(onLoadSuccess + "(row,data);");
				}
			}
			sb.append("},");
			if(BeanUtils.isNotEmpty(animate)){
				sb.append("animate: " +animate + ",");
			}
		} else {
			grid = "datagrid";
			sb.append("$(\'#" + name + "\').datagrid({");
			sb.append("idField: '" + idField + "',");
			sb.append("onLoadSuccess:function(data){$(\"#"+name+"\")."+grid+"('options').queryParams.loadFlag=true;$(\"#"+name+"\")."+grid+"(\"clearSelections\");");
			if (StringUtil.isNotEmpty(onLoadSuccess)) {
				if (onLoadSuccess.indexOf("(") != -1) {
					sb.append(onLoadSuccess + ";");
				} else {
					sb.append(onLoadSuccess + "(data);");
				}
			}
			sb.append("},");
		}
		if (BeanUtils.isNotEmpty(width)) {
			if (width.indexOf("%") > -1) {
				sb.append("width: \'" + width + "\',");
			} else {
				sb.append("width: " + width + ",");
			}
		}
		if (BeanUtils.isNotEmpty(height)) {
			if (height.indexOf("%") > -1) {
				sb.append("height: \'" + height + "\',");
			} else {
				sb.append("height: " + height + ",");
			}
		}
		if(BeanUtils.isNotEmpty(resizeHandle)){
			sb.append("resizeHandle: \'" + resizeHandle + "\',");
		}
		if(BeanUtils.isNotEmpty(resizeHandle)){
			sb.append("resizeHandle: \'" + resizeHandle + "\',");
		}
		if(BeanUtils.isNotEmpty(autoRowHeight)){
			sb.append("autoRowHeight: " + autoRowHeight + ",");
		}
		if(BeanUtils.isNotEmpty(striped)){
			sb.append("striped: " + striped + ",");
		}
		if(BeanUtils.isNotEmpty(method)){
			sb.append("method: \'" + method + "\',");
		}
		if(BeanUtils.isNotEmpty(nowrap)){
			sb.append("nowrap: " + nowrap + ",");
		}
		if(BeanUtils.isNotEmpty(loadMsg)){
			sb.append("loadMsg: \'" + loadMsg + "\',");
		}
		if(BeanUtils.isNotEmpty(rownumbers)){
			sb.append("rownumbers: " +rownumbers + ",");
		}
		if(BeanUtils.isNotEmpty(singleSelect)){
//			sb.append("singleSelect:" + !checkbox + ",");
			sb.append("singleSelect: " +singleSelect + ",");
		}
		if(BeanUtils.isNotEmpty(checkOnSelect)){
			sb.append("checkOnSelect: " +checkOnSelect + ",");
		}
		if(BeanUtils.isNotEmpty(selectOnCheck)){
			sb.append("selectOnCheck: " +selectOnCheck + ",");
		}
		if(BeanUtils.isNotEmpty(pagePosition)){
			sb.append("pagePosition: \'" + pagePosition + "\',");
		}
		if(BeanUtils.isNotEmpty(remoteSort)){
			sb.append("remoteSort: " +remoteSort + ",");
		}
		if(BeanUtils.isNotEmpty(showHeader)){
			sb.append("showHeader: " +showHeader + ",");
		}
		if(BeanUtils.isNotEmpty(showFooter)){
			sb.append("showFooter: " +showFooter + ",");
		}
		if(BeanUtils.isNotEmpty(scrollbarSize)){
			sb.append("scrollbarSize: " +scrollbarSize + ",");
		}
		if (BeanUtils.isNotEmpty(border)) {
			sb.append("border: " + border + ",");
		}
		
		if (title != null) {
			sb.append("title: \'" + title + "\',");
		}
		sb.append("queryParams:{loadFlag:false},"); //标识首次加载的变量,true为已加载过
		sb.append("url:\'" + actionUrl + "&field=" + fields + "&gridName=" + name + "\',");
		if(autoLoadData){
			sb.append("onBeforeLoad:function(param){$(\'#"+name+"\')."+grid+"('options').queryParams.loadFlag=true},");
		}
		else{
			//如果不让datagrid首次自己加载,就利用标识位loadFlag,过滤掉datagrid的首次加载
			sb.append("onBeforeLoad:function(param){if(!param.loadFlag){$(\'#"+name+"\')."+grid+"('options').queryParams.loadFlag=true;return false;}},");
		}
		if(StringUtils.isNotEmpty(rowStyler)){
			sb.append("rowStyler: function(index,row){ return "+rowStyler+"(index,row);},");
		}
		if(StringUtils.isNotEmpty(extendParams)){
			sb.append(extendParams);
		}
		if (fit) {
			sb.append("fit:true,");
		} else {
			sb.append("fit:false,");
		}
		
		sb.append("striped:true,");
		
		sb.append("loadMsg: \'数据加载中...\',");
		sb.append("pageSize: " + pageSize + ",");
		sb.append("pagination:" + pagination + ",");
		sb.append("pageList:[" + pageSize * 1 + "," + pageSize * 2 + "," + pageSize * 3 + "],");
		if(StringUtils.isNotBlank(sortName)){
			sb.append("sortName:'" +sortName +"',");
		}
		sb.append("sortOrder:'" + sortOrder + "',");
		
		if (fitColumns) {
			sb.append("fitColumns:true,");
		} else {
			sb.append("fitColumns:false,");
		}
		sb.append("frozenColumns:[[");
		this.getField(sb,0);
		sb.append("]],");
		
		sb.append("columns:[[");
		this.getField(sb);
		sb.append("]],");
		
		/**** 分组相关begin ****/
		if (StringUtil.isNotEmpty(view)) {
			sb.append("view:"+view+",");
		}
		if (StringUtil.isNotEmpty(groupField)) {
			sb.append("groupField:'"+groupField+"',");
		}
		if (StringUtil.isNotEmpty(groupFormatter)) {
			sb.append("groupFormatter:function(value,rows){return "+groupFormatter+";},");
		}
		/**** 分组相关end ****/
		if (StringUtil.isNotEmpty(onDblClickRow)) {
			sb.append("onDblClickRow:function(rowIndex,rowData){" + onDblClickRow + ";},");
		}
		if (treegrid) {
			sb.append("onClickRow:function(rowData){");
		}
		else{
			sb.append("onClickRow:function(rowIndex,rowData){");
			if(editable){
				sb.append("on"+name+"ClickRow(rowIndex,rowData);");
			}
		}
		/**行记录赋值*/
//		sb.append("rowid=rowData.id;");
//		sb.append("gridname=\'"+name+"\';");
		if (StringUtil.isNotEmpty(onClick)) {
			if (treegrid) {
				sb.append("" + onClick + ";");
			}else{
				sb.append("" + onClick + ";");
			}
		}
		sb.append("}");
		sb.append("});");
		this.setPager(sb, grid);
		sb.append("});");
		/*
		sb.append("function reloadTable(){");
		sb.append("try{");
		sb.append("	$(\'#"+name+"\').datagrid(\'reload\');" );
		sb.append("	$(\'#"+name+"\').treegrid(\'reload\');" );
		sb.append("}catch(ex){}");
		sb.append("}");
		*/
		sb.append("function reload" + name + "(){" + "$(\'#" + name + "\')." + grid + "(\'reload\');" + "}");
		/*sb.append("function get" + name + "Selected(field){return getSelected(field);}");*/
		sb.append("function get" + name + "Selected(field){var row = $(\'#" + name + "\')." + grid + "(\'getSelected\');" + "if(row!=null)" + "{" + "value= row[field];" + "}" + "else" + "{" + "value=\'\';" + "}" + "return value;" + "}");
		
		/*sb.append("function getSelected(field){" + "var row = $(\'#\'+gridname)." + grid + "(\'getSelected\');" + "if(row!=null)" + "{" + "value= row[field];" + "}" + "else" + "{" + "value=\'\';" + "}" + "return value;" + "}");*/
		sb.append("function get" + name + "Selections(field){" + "var ids = [];" + "var rows = $(\'#" + name + "\')." + grid + "(\'getSelections\');" + "for(var i=0;i<rows.length;i++){" + "ids.push(rows[i][field]);" + "}" + "ids.join(\',\');" + "return ids" + "};");
		/*
		sb.append("function getSelectRows(){");
		sb.append("	return $(\'#"+name+"\').datagrid('getChecked');");
		sb.append("}");
		*/
		if (columnList.size() > 0) {
			sb.append("function " + name + "search(){");
			sb.append("var queryParams=$(\'#" + name + "\')."+grid+"('options').queryParams;");
			sb.append("$(\'#" + name + "tb\').find(':input[name]').each(function(){queryParams[$(this).attr('name')]=$(this).val();});");
			//sb.append("$(\'#" + name + "\')." + grid + "({url:'" + actionUrl + "&field=" + searchFields + "',pageNumber:1});" + "}");
			if(treegrid){
				sb.append("$(\'#" + name + "\')." + grid + "('reload');" + "}");
			}else{
				sb.append("$(\'#" + name + "\')." + grid + "('load');" + "}");
			}
			/*高级查询执行方法
			sb.append("function dosearch(params){");
			sb.append("var jsonparams=$.parseJSON(params);");
			sb.append("$(\'#" + name + "\')." + grid + "({url:'" + actionUrl + "&field=" + searchFields + "',queryParams:jsonparams});" + "}");
			*/
			if(toolBarList.size()>0)
			{
			 //searchbox框执行方法
			  searchboxFun(sb,grid);
			}
			//生成重置按钮功能js
			sb.append("function " + name + "SearchReset(name){");
			sb.append(" $(\"#\"+name+\"tb\").find(\":input\").val(\"\");");
			String func = name.trim() + "search();";
			sb.append(func);
			sb.append("}");
			
			//生成表格编辑的 js
			if(editable){
				String editVar="var dg"+name+"=$(\'#" + name + "\');"
				+"var "+name+"editIndex = undefined;"
				+"function end"+name+"Editing() {"
					+"if ("+name+"editIndex == undefined) {"
						+"return true"
					+"}"
					+"if (dg"+name+".datagrid('validateRow', "+name+"editIndex)) {"
						+"dg"+name+".datagrid('endEdit', "+name+"editIndex);"
						+""+name+"editIndex = undefined;"
						+"return true;"
					+"} else {"
						+"tip('第' + ("+name+"editIndex + 1) + '行数据填写不正确,请填写正确后再新增/编辑其他行');"
						+"return false;"
					+"} "
				+"}"
				+"function on"+name+"ClickRow(rowIndex,rowData) {"
					 +"if (end"+name+"Editing()){"
						+" dg"+name+".datagrid('selectRow',rowIndex).datagrid('beginEdit',rowIndex);"
						+" dg"+name+".datagrid('unselectAll');"
						+name+"editIndex = rowIndex;"
					 +"}"
				+"}"
				+"function append"+name+"Row() {"
					+"if (end"+name+"Editing()){"
						+"dg"+name+".datagrid('uncheckAll');"
						+"dg"+name+".datagrid('appendRow', {});"
						+name+"editIndex=dg"+name+".datagrid('getRows').length -1;"
						+"dg"+name+".datagrid('beginEdit',"+name+"editIndex);"
					+"}"
				+"}"
				+"function remove"+name+"Row() {"
					+"var rows = dg"+name+".datagrid('getSelections'); "
					+"var length=rows.length;"
					+"if (length>0) {"
						+"$.messager.confirm('提示信息', '确认删除?', function(r) {"
							+"if(r){"
								+"for(var i=0;i<length;i++){"
									+"if("+name+"editIndex==i){"
										+name+"editIndex = undefined;"
									+"}"
									+"var rowIndex = dg"+name+".datagrid('getRowIndex', rows[i]);"
									+"dg"+name+".datagrid('deleteRow', rowIndex);"
								+"}"
							+"}"
						+"});"
					+"}else{"
						+"tip('您未选中任何行进行删除操作');"
					+"}"
				+"}"
				+"function reject"+name+"() {"
					+"dg"+name+".datagrid('unselectAll');"
					+"dg"+name+".datagrid('rejectChanges');"
					+name+"editIndex = undefined;"
				+"}";
				sb.append(editVar);
			}
		}
		/*sb.append("$(\".search_div_icon\",$(\"#"+name+"tb\")).toggle(function(){"
			+"$(\".search_div_i_icon\",$(\"#"+name+"tb\")).removeClass(\"awsm-icon-double-angle-down\").addClass(\"awsm-icon-double-angle-up\");"
			+"$(\".search_div\",$(\"#"+name+"tb\")).slideDown();"
			+"setTimeout('$(\"#"+name+"\").datagrid(\"resize\")',300);"
			+"},function(){"
			+"$(\".search_div_i_icon\",$(\"#"+name+"tb\")).removeClass(\"awsm-icon-double-angle-up\").addClass(\"awsm-icon-double-angle-down\");"
			+"$(\".search_div\",$(\"#"+name+"tb\")).slideUp();"
			+"setTimeout('$(\"#"+name+"\").datagrid(\"resize\")',300);"
			+"}).css(\"cursor\",\"pointer\").attr(\"title\",\"点击展开/收缩查询栏\");"
		);*/
		sb.append("</script>");
		
		sb.append("<table ");
		if(StringUtil.isNotEmpty(flag)){
			sb.append("flag="+flag);
		}
		sb.append("id=\"" + name + "\" toolbar=\"#" + name + "tb\"></table>");
		
		sb.append("<div class=\"grid_toolbar_div\" id=\"" + name + "tb\" >");
		
		if(!showButtonDivResult){
			sb.append("<div style=\"height:0px;\" >");
		}else{
			sb.append("<div class=\"datagrid-toolbar grid_button_div\">");
		}
		//update-end--Author:zhaojunfu  Date:20130807 for：解决新旧组合查询，按钮配置等的样式问题
		sb.append("<span class=\"grid_button_span\" >");
		if(defaultOpt){
			sb.append("<a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" plain=\"true\" icon=\"glyphicon glyphicon-plus icon-color\" onclick=\"append"+name+"Row()\">新增</a>");
			sb.append("<a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" plain=\"true\" icon=\"glyphicon glyphicon-remove icon-color\" onclick=\"remove"+name+"Row()\">批量删除</a>");
			sb.append("<a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" plain=\"true\" icon=\"glyphicon glyphicon-share-alt icon-color\" onclick=\"reject"+name+"()\">撤销</a>");
		}
		if (toolBarList.size() > 0 && showButtonDivResult) {
			for (DataGridUrl toolBar : toolBarList) {
				String width = toolBar.getWidth().contains("%") ? "'" + toolBar.getWidth() + "'" : toolBar.getWidth();
				String height = toolBar.getHeight().contains("%") ? "'" + toolBar.getHeight() + "'" : toolBar
						.getHeight();
				Integer preinstallWidth = toolBar.getPreinstallWidth();
				String exParams = toolBar.getExParams();
				String rowId="";
				//先看toolbar的各按钮是否配置rowId
				if (StringUtil.isNotEmpty(toolBar.getRowId())&&!StringUtil.equals(toolBar.getRowId(), "id")) {
					JSONObject exParamsObj = JSONHelper.mergeJson(exParams, "{rowId:'" + toolBar.getRowId() + "'}");
					exParams = exParamsObj.toString();
					exParams=exParams.replaceAll("\"", "\'");
				}else if(!StringUtil.equals(idField, "id")){
					//再看datagrid是否配置idField(不为默认"id"时)
					JSONObject exParamsObj = JSONHelper.mergeJson(exParams, "{rowId:'" + idField + "'}");
					exParams = exParamsObj.toString();
					exParams=exParams.replaceAll("\"", "\'");
				}
				sb.append("<a id=\"" + toolBar.getId()+ "\" class=\"easyui-linkbutton\" plain=\"true\" ");
				if (OptTypeDirection.GridAdd.equals(toolBar.getType())) {
					sb.append("href=\"javascript:void(0)\" ");
					if (StringUtil.isEmpty(toolBar.getIcon())) {
						sb.append("icon=\"glyphicon glyphicon-plus\"");
					} else {
						sb.append("icon=\"" + toolBar.getIcon() + "\"");
					}
					if (StringUtil.isNotEmpty(toolBar.getOnclick())) {
						sb.append("onclick=" + toolBar.getOnclick());
					} else {
						sb.append("onclick=\"" + toolBar.getFunname() + "(\'" + toolBar.getTitle() + "\',\'"
								+ toolBar.getUrl() + "\',\'" + name + "\'," + width + "," + height + ","
								+ preinstallWidth + "," + exParams + ")\"");
					}
				} else if (OptTypeDirection.GridUpdate.equals(toolBar.getType())) {
					sb.append("href=\"javascript:void(0)\" ");
					if (StringUtil.isEmpty(toolBar.getIcon())) {
						sb.append("icon=\"glyphicon glyphicon-pencil icon-color\" ");
					} else {
						sb.append("icon=\"" + toolBar.getIcon() + "\" ");
					}
					if (StringUtil.isNotEmpty(toolBar.getOnclick())) {
						sb.append("onclick=" + toolBar.getOnclick());
					} else {
						sb.append("onclick=\"" + toolBar.getFunname() + "(\'" + toolBar.getTitle() + "\',\'"
								+ toolBar.getUrl() + "\',\'" + name + "\'," + width + "," + height + ","
								+ preinstallWidth + "," + exParams + ")\"");
					}
				} else if (OptTypeDirection.GridDetail.equals(toolBar.getType())) {
					sb.append("href=\"javascript:void(0)\" ");
					if (StringUtil.isEmpty(toolBar.getIcon())) {
						sb.append("icon=\"glyphicon glyphicon-search icon-color\" ");
					} else {
						sb.append("icon=\"" + toolBar.getIcon() + "\" ");
					}
					if (StringUtil.isNotEmpty(toolBar.getOnclick())) {
						sb.append("onclick=" + toolBar.getOnclick());
					} else {
						sb.append("onclick=\"" + toolBar.getFunname() + "(\'" + toolBar.getTitle() + "\',\'"
								+ toolBar.getUrl() + "\',\'" + name + "\'," + width + "," + height + ","
								+ preinstallWidth + "," + exParams + ")\"");
					}
				} else if (OptTypeDirection.GridDelMul.equals(toolBar.getType())) {
					sb.append("href=\"javascript:void(0)\" ");
					if (StringUtil.isEmpty(toolBar.getIcon())) {
						sb.append("icon=\"glyphicon glyphicon-trash icon-color\" ");
					} else {
						sb.append("icon=\"" + toolBar.getIcon() + "\" ");
					}
					if (StringUtil.isNotEmpty(toolBar.getOnclick())) {
						sb.append("onclick=" + toolBar.getOnclick());
					} else {
						sb.append("onclick=\"" + toolBar.getFunname() + "(\'" + toolBar.getTitle() + "\'");
						String message = StringUtil.isNotEmpty(toolBar.getMessage()) ? "\'" + toolBar.getMessage()
								+ "\'" : null;
						sb.append("," + message + ",\'" + toolBar.getUrl() + "\',\'" + name + "\'");
						if (StringUtil.isNotEmpty(toolBar.getCallback())) {
							sb.append("," + toolBar.getCallback());
						} else {
							sb.append(",null");
						}
						if (StringUtil.isNotEmpty(toolBar.getRowId())) {
							sb.append(",\'" + toolBar.getRowId() + "\'");
						} else {
							sb.append(",null");
						}
						sb.append(")\"");
					}
				} else if (OptTypeDirection.EditGridAdd.equals(toolBar.getType())) {
					sb.append("href=\"javascript:void(0)\" ");
					if (StringUtil.isEmpty(toolBar.getIcon())) {
						sb.append("icon=\"glyphicon glyphicon-plus icon-color\" ");
					} else {
						sb.append("icon=\"" + toolBar.getIcon() + "\" ");
					}
					if (StringUtil.isNotEmpty(toolBar.getOnclick())) {
						sb.append("onclick=" + toolBar.getOnclick());
					} else {

					}
				} else if (OptTypeDirection.EditGridDelMul.equals(toolBar.getType())) {
					sb.append("href=\"javascript:void(0)\" ");
					if (StringUtil.isEmpty(toolBar.getIcon())) {
						sb.append("icon=\"glyphicon glyphicon-trash icon-color\" ");
					} else {
						sb.append("icon=\"" + toolBar.getIcon() + "\" ");
					}
					if (StringUtil.isNotEmpty(toolBar.getOnclick())) {
						sb.append("onclick=" + toolBar.getOnclick());
					} else {

					}
				} else if (OptTypeDirection.EditGridReject.equals(toolBar.getType())) {
					sb.append("href=\"javascript:void(0)\" ");
					if (StringUtil.isEmpty(toolBar.getIcon())) {
						sb.append("icon=\"glyphicon glyphicon-share-alt icon=color\" ");
					} else {
						sb.append("icon=\"" + toolBar.getIcon() + "\" ");
					}
					if (StringUtil.isNotEmpty(toolBar.getOnclick())) {
						sb.append("onclick=" + toolBar.getOnclick());
					} else {

					}
				} else if (OptTypeDirection.GridExcelExport.equals(toolBar.getType())) {
					if (StringUtil.isNotEmpty(toolBar.getUrl())) {
						sb.append("href=\"" + toolBar.getUrl() + "\" ");
					} else {
						sb.append("href=\"commonController.do?commonExport&gridName=" + name + exportParams + "\" ");
					}
					if (StringUtil.isEmpty(toolBar.getIcon())) {
						sb.append("icon=\"glyphicon glyphicon-cloud-download icon-color\" ");
					} else {
						sb.append("icon=\"" + toolBar.getIcon() + "\" ");
					}
					if (StringUtil.isNotEmpty(toolBar.getOnclick())) {
						sb.append("onclick=" + toolBar.getOnclick());
					} else {

					}
				} else if (OptTypeDirection.GridExcelImport.equals(toolBar.getType())) {
					sb.append("href=\"javascript:void(0)\" ");
					if (StringUtil.isEmpty(toolBar.getIcon())) {
						sb.append("icon=\"glyphicon glyphicon-cloud-upload icon-color\" ");
					} else {
						sb.append("icon=\"" + toolBar.getIcon() + "\" ");
					}
					if (StringUtil.isNotEmpty(toolBar.getOnclick())) {
						sb.append("onclick=" + toolBar.getOnclick());
					} else {
						sb.append("onclick=\"commonImportExcel(&quot;" + submitUrl + "&quot;,\'" + importParams + "\')\"");
					}
				} else {
					if (StringUtil.isEmpty(toolBar.getIcon())) {
						sb.append("icon=\"glyphicon glyphicon-share-alt icon-color\" ");
					} else {
						sb.append("icon=\"" + toolBar.getIcon() + "\" ");
					}
					if (StringUtil.isNotEmpty(toolBar.getUrl())) {
						sb.append("href=\"" + toolBar.getUrl() + "\" ");
					} else {
						sb.append("href=\"javascript:void(0)\" ");
					}
					if (StringUtil.isNotEmpty(toolBar.getOnclick())) {
						sb.append("onclick=" + toolBar.getOnclick());
					} else if (StringUtil.isNotEmpty(toolBar.getFunname())) {
						sb.append("onclick=" + toolBar.getFunname() + "()");
					}
				}
				sb.append(">" + toolBar.getTitle() + "</a>");
			}
		}
		sb.append("</span>");
		
		/* 产生ftl数据 */
		Map<String,Object> search_map=new HashMap<String,Object>();
		String queryMode= this.getQueryMode();
		String name=this.name;
		StringBuffer divs_str=new StringBuffer();
		/* 一些在freemarker里不好处理的Java方法放到java中转化为字符串直接传到ftl里 begin */
		for (DataGridColumn col : columnList) {
			if (col.isQuery()) {
				//原本的在searchbox加extendAttribute(col.getExtend())有Bug,故暂时不启用该扩展属性
				//divs_str.append("<div data-options=\"name:\'"+col.getField().replaceAll("_","\\.")+"\',iconCls:\'icon-ok\' "+extendAttribute(col.getExtend())+" \">"+col.getTitle()+"</div>");
				divs_str.append("<div data-options=\"name:\'"+col.getField().replaceAll("_","\\.")+"\',iconCls:\'icon-ok\'\">"+col.getTitle()+"</div>");
			}
		}
		/* 一些在freemarker里不好处理的Java方法放到java中转化为字符串直接传到ftl里 end */
		search_map.put("queryMode",queryMode);
		search_map.put("hasQueryColum", hasQueryColum);
		search_map.put("name", name);
		search_map.put("divs_str", divs_str.toString());
		
		if(!StringUtil.equals("separate", queryMode)){
			//sb.append(getQueryString());
			
			/* 隐藏式快速查询（右上方显示快速查询按钮，鼠标放上，下滑出查询框） */
			sb.append("<span class=\"grid_button_right\" >");
			sb.append("<a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" plain=\"true\" icon=\"glyphicon glyphicon-search icon-color\">");
			sb.append("<span class=\"l-btn-left l-btn-icon-left\"><span class=\"l-btn-text\">查询</span></span>");
			sb.append("</a>");
			sb.append(getQueryString());
			/*
			sb.append("<div class=\"drop-container search_div\">");
			sb.append("<span class=\"search_input_area\"><span class=\"search_title_span\" title=\"员工姓名\">员工姓名：</span><span class=\"search_content_span\"><input class=\"search_input\" type=\"text\" name=\"name\"></span></span>");
			sb.append("<span class=\"search_input_area\"><span class=\"search_title_span\" title=\"员工姓名\">员工姓名：</span><span class=\"search_content_span\"><input class=\"search_input\" type=\"text\" name=\"name\"><input class=\"search_input\" type=\"text\" name=\"name\"></span></span>");
			sb.append("<span class=\"search_button_area\"><a href=\"#\" class=\"easyui-linkbutton icon-color l-btn l-btn-small\" iconcls=\"glyphicon glyphicon-search\" onclick=\"userListsearch()\" group=\"\" id=\"\"><span class=\"l-btn-left l-btn-icon-left\"><span class=\"l-btn-text\">查询</span></span></a>&nbsp;<a href=\"#\" class=\"easyui-linkbutton icon-color l-btn l-btn-small\" iconcls=\"glyphicon glyphicon-refresh\" onclick=\"userListSearchReset('userList')\" group=\"\" id=\"\"><span class=\"l-btn-left l-btn-icon-left\"><span class=\"l-btn-text\">重置</span></span></a></span>");
			sb.append("</div>");
			 */
			sb.append("</span>");
		}
		
		String datagrid_search_html = viewEngine.parseTemplate(
				"/com/xplatform/base/framework/tag/core/ftl/datagrid_search.ftl", search_map);
		sb.append(datagrid_search_html);
		
		sb.append("</div>");
		
		return sb;
	}
	
	private String getQueryString(){
		//如果是group模式就产生查询div
		StringBuffer sb=new StringBuffer();
	   /* if("group".equals(getQueryMode())){*/
			
			//如果有查询字段,再循环产生各种查询输入框
			if(hasQueryColum){
//				sb.append("<div class=\"search_div_icon\" style=\"text-align:center\"><i class=\"search_div_i_icon awsm-icon-double-angle-down\"></i></div>");
				sb.append("<div name=\"searchColums\" class=\"drop-container search_div\">");
				for (DataGridColumn col : queryColumnList) {
					String exParams = col.getQueryExParams();
					Map<String, Object> input_map = new HashMap<String, Object>();
					String input_html = "";
					String fieldName = col.getField();
					if (this.isStatistics() && col.getCompareSign() != null) {
						fieldName = "filter_" + col.getCompareSign() + "_" + fieldName;
					} else {
						fieldName = fieldName.replaceAll("_", "\\.");
					}
					String inputDictCode = col.getDictCode();
					String comboUrl = col.getComboUrl();
					String data = col.getData();
					input_map.put("id", fieldName);
					input_map.put("name", fieldName);
					input_map.put("width", 148);
					input_map.put("height", 24);
					input_map.put("panelWidth", 150);
					input_map.put("editable", false);
					input_map.put("disabled", false);
					input_map.put("multiple", false);
					input_map.put("fit", false);

					sb.append("<span class=\"search_input_area\" >");
					//sb.append("<span style=\"display:-moz-inline-box;display:inline-block;width: 65px;text-align:right;text-overflow:ellipsis;-o-text-overflow:ellipsis;overflow: hidden;white-space:nowrap; \" title=\""+col.getTitle()+"\">"+col.getTitle()+"：</span>");
					sb.append("<span  class=\"search_title_span\" title=\"" + col.getTitle() + "\">" + col.getTitle()
							+ "：</span>");
					if ("single".equals(col.getQueryMode())) {
						if (InputComponentType.datetimebox.equals(col.getQueryInputType())) {
							input_map.put("showSeconds", true);
							input_map.put("type", "datetime");
							input_map.put("panelWidth", 180);
							input_map.put("format", col.getFormatter());
							addExParamsToMap(input_map, exParams);

							input_html = viewEngine.parseTemplate(
									"/com/xplatform/base/framework/tag/core/ftl/datetimebox.ftl", input_map);
						} else if (InputComponentType.datebox.equals(col.getQueryInputType())) {
							input_map.put("showSeconds", true);
							input_map.put("type", "date");
							input_map.put("panelWidth", 180);
							input_map.put("format", col.getFormatter());
							addExParamsToMap(input_map, exParams);

							input_html = viewEngine.parseTemplate(
									"/com/xplatform/base/framework/tag/core/ftl/datetimebox.ftl", input_map);
						} else if (InputComponentType.combobox.equals(col.getQueryInputType())) {
							input_map.put("textField", "text");
							input_map.put("valueField", "id");
							input_map.put("group", false);
							if (StringUtil.isNotEmpty(comboUrl)) {
								input_map.put("url", comboUrl);
							} else if (StringUtil.isNotEmpty(data)) {
								input_map.put("data", data);
							}
							if (StringUtil.isNotEmpty(inputDictCode)) {
								DictTypeService dictTypeService = ApplicationContextUtil.getBean("dictTypeService");
								List<ComboBox> valueList = dictTypeService.findCacheByCode(inputDictCode);
								String jsonList = JSONHelper.toJSONString(valueList);
								input_map.put("data", jsonList);
							}

							addExParamsToMap(input_map, exParams);

							input_html = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/combobox.ftl",
									input_map);
						} else if (InputComponentType.combogrid.equals(col.getQueryInputType())) {

							addExParamsToMap(input_map, exParams);

						} else if (InputComponentType.combotree.equals(col.getQueryInputType())) {
							if(col.isAysn()){
								if (StringUtil.isNotEmpty(inputDictCode)){
									input_map.put("url", "commonController.do?getDictValueTree&dictCode="+inputDictCode);
								}else{
									input_map.put("url", comboUrl);
								}
							}else{
								if (StringUtil.isNotEmpty(comboUrl)) {
									input_map.put("url", comboUrl);
								} else if (StringUtil.isNotEmpty(data)) {
									input_map.put("data", data);
								} else if (StringUtil.isNotEmpty(inputDictCode)) {
									DictTypeService dictTypeService = ApplicationContextUtil.getBean("dictTypeService");
									String treeJson = dictTypeService.findCacheByCode(inputDictCode);
									input_map.put("data", treeJson);
								}
							}
							input_map.put("animate", true);
							input_map.put("cascadeCheck", false);
							input_map.put("onlyLeafCheck", false);
							input_map.put("lines", false);

							addExParamsToMap(input_map, exParams);

							input_html = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/combotree.ftl",
									input_map);
						} else if (InputComponentType.numberbox.equals(col.getQueryInputType())) {

							input_map.put("groupSeparator", ",");
							input_map.put("decimalSeparator", ".");
							input_map.put("editable", true);
							input_map.put("spinner", false);

							addExParamsToMap(input_map, exParams);

							//如果是numberbox比较特殊,需要套一个span来完成垂直居中
							input_html = "<span class=\"search_content_span\">"
									+ viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/number.ftl",
											input_map) + "</span>";
						} else {
							sb.append("<span class=\"search_content_span\"><input class=\"search_input\" type=\"text\" name=\""
									+ fieldName + "\"  " + extendAttribute(col.getExtend()) + " /></span>");
						}
					} else if ("group".equals(col.getQueryMode())) {
						if (InputComponentType.datetimebox.equals(col.getQueryInputType())) {
							input_map.put("showSeconds", true);
							input_map.put("type", "datetime");
							input_map.put("panelWidth", 180);
							input_map.put("format", col.getFormatter());
							addExParamsToMap(input_map, exParams);
							if (this.isStatistics() && col.getCompareSign() != null) {
								//如果是between类型的统计查询,输入框的name取名要符合前者操作符是BTS 后者是BTE的规则
								if ("BT".equals(col.getCompareSign())) {
									fieldName = "filter_BTS_" + col.getField();
								}
								input_map.put("name", fieldName);
								input_map.put("id", fieldName);
							} else {
								input_map.put("name", col.getField() + "_begin");
								input_map.put("id", col.getField() + "_begin");
							}
							input_html += viewEngine.parseTemplate(
									"/com/xplatform/base/framework/tag/core/ftl/datetimebox.ftl", input_map);
							input_html += "<span class=\"search_input_scopelink\" >~</span>";
							if (this.isStatistics() && col.getCompareSign() != null) {
								//如果是between类型的统计查询,输入框的name取名要符合前者操作符是BTS 后者是BTE的规则
								if ("BT".equals(col.getCompareSign())) {
									fieldName = "filter_BTE_" + col.getField();
								}
								input_map.put("name", fieldName);
								input_map.put("id", fieldName);
							} else {
								input_map.put("name", col.getField() + "_end");
								input_map.put("id", col.getField() + "_end");
							}
							input_html += viewEngine.parseTemplate(
									"/com/xplatform/base/framework/tag/core/ftl/datetimebox.ftl", input_map);
						} else if (InputComponentType.datebox.equals(col.getQueryInputType())) {
							input_map.put("showSeconds", true);
							input_map.put("type", "date");
							input_map.put("panelWidth", 180);
							input_map.put("format", col.getFormatter());
							addExParamsToMap(input_map, exParams);
							if (this.isStatistics() && col.getCompareSign() != null) {
								//如果是between类型的统计查询,输入框的name取名要符合前者操作符是BTS 后者是BTE的规则
								if ("BT".equals(col.getCompareSign())) {
									fieldName = "filter_BTS_" + col.getField();
								}
								input_map.put("name", fieldName);
								input_map.put("id", fieldName);
							} else {
								input_map.put("name", col.getField() + "_begin");
								input_map.put("id", col.getField() + "_begin");
							}
							input_html += viewEngine.parseTemplate(
									"/com/xplatform/base/framework/tag/core/ftl/datetimebox.ftl", input_map);
							input_html += "<span class=\"search_input_scopelink\" >~</span>";
							if (this.isStatistics() && col.getCompareSign() != null) {
								//如果是between类型的统计查询,输入框的name取名要符合前者操作符是BTS 后者是BTE的规则
								if ("BT".equals(col.getCompareSign())) {
									fieldName = "filter_BTE_" + col.getField();
								}
								input_map.put("name", fieldName);
								input_map.put("id", fieldName);
							} else {
								input_map.put("name", col.getField() + "_end");
								input_map.put("id", col.getField() + "_end");
							}
							input_html += viewEngine.parseTemplate(
									"/com/xplatform/base/framework/tag/core/ftl/datetimebox.ftl", input_map);
						} else if (InputComponentType.numberbox.equals(col.getQueryInputType())) {

							input_map.put("groupSeparator", ",");
							input_map.put("decimalSeparator", ".");
							input_map.put("editable", true);
							input_map.put("spinner", false);

							addExParamsToMap(input_map, exParams);

							//如果是numberbox比较特殊,需要套一个span来完成垂直居中
							if (this.isStatistics() && col.getCompareSign() != null) {
								//如果是between类型的统计查询,输入框的name取名要符合前者操作符是BTS 后者是BTE的规则
								if ("BT".equals(col.getCompareSign())) {
									fieldName = "filter_BTS_" + col.getField();
								}
								input_map.put("name", fieldName);
								input_map.put("id", fieldName);
							} else {
								input_map.put("name", col.getField() + "_begin");
								input_map.put("id", col.getField() + "_begin");
							}
							input_html += "<span class=\"search_content_span\">"
									+ viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/number.ftl",
											input_map) + "</span>";
							input_html += "<span class=\"search_input_scopelink\" >~</span>";
							if (this.isStatistics() && col.getCompareSign() != null) {
								//如果是between类型的统计查询,输入框的name取名要符合前者操作符是BTS 后者是BTE的规则
								if ("BT".equals(col.getCompareSign())) {
									fieldName = "filter_BTE_" + col.getField();
								}
								input_map.put("name", fieldName);
								input_map.put("id", fieldName);
							} else {
								input_map.put("name", col.getField() + "_end");
								input_map.put("id", col.getField() + "_end");
							}
							input_html += "<span class=\"search_content_span\">"
									+ viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/number.ftl",
											input_map) + "</span>";
						} else {
							String beginName = "";
							String endName = "";
							if (this.isStatistics() && col.getCompareSign() != null) {
								//如果是between类型的统计查询,输入框的name取名要符合前者操作符是BTS 后者是BTE的规则
								if ("BT".equals(col.getCompareSign())) {
									beginName = "filter_BTS_" + col.getField();
									endName = "filter_BTE_" + col.getField();
								}
								beginName = fieldName;
								endName = fieldName;
							} else {
								beginName = col.getField() + "_begin";
								endName = col.getField() + "_end";
							}
							input_html += ("<input class=\"search_input\" type=\"text\" name=\"" + beginName + "\" "
									+ extendAttribute(col.getExtend()) + "/>");
							input_html += ("<span class=\"search_input_scopelink\" >~</span>");
							input_html += ("<input class=\"search_input\" type=\"text\" name=\"" + endName + "\" "
									+ extendAttribute(col.getExtend()) + "/>");
						}
					}
					sb.append(input_html);
					sb.append("</span>");
				}
				//生成查询、重置按钮
				sb.append("<span class=\"search_button_area\">");
				sb.append("<a href=\"#\" class=\"easyui-linkbutton icon-color\" iconCls=\"glyphicon glyphicon-search\" onclick=\""+name+"search()\">查询</a>&nbsp;");
				sb.append("<a href=\"#\" class=\"easyui-linkbutton icon-color\" iconCls=\"glyphicon glyphicon-refresh\" onclick=\"" + name + "SearchReset('"+name+"')\">重置</a>");
				sb.append("</span>");
				sb.append("</div>");
			}
			
		/*}*/
	    return sb.toString();
	}
	
	private String getQuerySeparateString(){
		//如果是group模式就产生查询div
		StringBuffer sb=new StringBuffer();
	    if("separate".equals(getQueryMode())){
			
			//如果有查询字段,再循环产生各种查询输入框
			if(hasQueryColum){
//				sb.append("<div class=\"search_div_icon\" style=\"text-align:center\"><i class=\"search_div_i_icon awsm-icon-double-angle-down\"></i></div>");
				sb.append("<div name=\"searchColums\" class=\"search_div\">");
				for (DataGridColumn col : queryColumnList) {
					String exParams = col.getQueryExParams();
					Map<String, Object> input_map = new HashMap<String, Object>();
					String input_html = "";
					String fieldName = col.getField();
					if (this.isStatistics() && col.getCompareSign() != null) {
						fieldName = "filter_" + col.getCompareSign() + "_" + fieldName;
					} else {
						fieldName = fieldName.replaceAll("_", "\\.");
					}
					String inputDictCode = col.getDictCode();
					String comboUrl = col.getComboUrl();
					String data = col.getData();
					input_map.put("id", fieldName);
					input_map.put("name", fieldName);
					input_map.put("width", 148);
					input_map.put("height", 24);
					input_map.put("panelWidth", 150);
					input_map.put("editable", false);
					input_map.put("disabled", false);
					input_map.put("multiple", false);
					input_map.put("fit", false);

					sb.append("<span class=\"search_input_area\" >");
					//sb.append("<span style=\"display:-moz-inline-box;display:inline-block;width: 65px;text-align:right;text-overflow:ellipsis;-o-text-overflow:ellipsis;overflow: hidden;white-space:nowrap; \" title=\""+col.getTitle()+"\">"+col.getTitle()+"：</span>");
					sb.append("<span  class=\"search_title_span\" title=\"" + col.getTitle() + "\">" + col.getTitle()
							+ "：</span>");
					if ("single".equals(col.getQueryMode())) {
						if (InputComponentType.datetimebox.equals(col.getQueryInputType())) {
							input_map.put("showSeconds", true);
							input_map.put("type", "datetime");
							input_map.put("panelWidth", 180);
							input_map.put("format", col.getFormatter());
							addExParamsToMap(input_map, exParams);

							input_html = viewEngine.parseTemplate(
									"/com/xplatform/base/framework/tag/core/ftl/datetimebox.ftl", input_map);
						} else if (InputComponentType.datebox.equals(col.getQueryInputType())) {
							input_map.put("showSeconds", true);
							input_map.put("type", "date");
							input_map.put("panelWidth", 180);
							input_map.put("format", col.getFormatter());
							addExParamsToMap(input_map, exParams);

							input_html = viewEngine.parseTemplate(
									"/com/xplatform/base/framework/tag/core/ftl/datetimebox.ftl", input_map);
						} else if (InputComponentType.combobox.equals(col.getQueryInputType())) {
							input_map.put("textField", "text");
							input_map.put("valueField", "id");
							input_map.put("group", false);
							if (StringUtil.isNotEmpty(comboUrl)) {
								input_map.put("url", comboUrl);
							} else if (StringUtil.isNotEmpty(data)) {
								input_map.put("data", data);
							}
							if (StringUtil.isNotEmpty(inputDictCode)) {
								DictTypeService dictTypeService = ApplicationContextUtil.getBean("dictTypeService");
								List<ComboBox> valueList = dictTypeService.findCacheByCode(inputDictCode);
								String jsonList = JSONHelper.toJSONString(valueList);
								input_map.put("data", jsonList);
							}

							addExParamsToMap(input_map, exParams);

							input_html = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/combobox.ftl",
									input_map);
						} else if (InputComponentType.combogrid.equals(col.getQueryInputType())) {

							addExParamsToMap(input_map, exParams);

						} else if (InputComponentType.combotree.equals(col.getQueryInputType())) {
							if(col.isAysn()){
								if (StringUtil.isNotEmpty(inputDictCode)){
									input_map.put("url", "commonController.do?getDictValueTree&dictCode="+inputDictCode);
								}else{
									input_map.put("url", comboUrl);
								}
							}else{
								if (StringUtil.isNotEmpty(comboUrl)) {
									input_map.put("url", comboUrl);
								} else if (StringUtil.isNotEmpty(data)) {
									input_map.put("data", data);
								} else if (StringUtil.isNotEmpty(inputDictCode)) {
									DictTypeService dictTypeService = ApplicationContextUtil.getBean("dictTypeService");
									String treeJson = dictTypeService.findCacheByCode(inputDictCode);
									input_map.put("data", treeJson);
								}
							}
							input_map.put("animate", true);
							input_map.put("cascadeCheck", false);
							input_map.put("onlyLeafCheck", false);
							input_map.put("lines", false);

							addExParamsToMap(input_map, exParams);

							input_html = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/combotree.ftl",
									input_map);
						} else if (InputComponentType.numberbox.equals(col.getQueryInputType())) {

							input_map.put("groupSeparator", ",");
							input_map.put("decimalSeparator", ".");
							input_map.put("editable", true);
							input_map.put("spinner", false);

							addExParamsToMap(input_map, exParams);

							//如果是numberbox比较特殊,需要套一个span来完成垂直居中
							input_html = "<span class=\"search_content_span\">"
									+ viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/number.ftl",
											input_map) + "</span>";
						} else {
							sb.append("<span class=\"search_content_span\"><input class=\"search_input\" type=\"text\" name=\""
									+ fieldName + "\"  " + extendAttribute(col.getExtend()) + " /></span>");
						}
					} else if ("group".equals(col.getQueryMode())) {
						if (InputComponentType.datetimebox.equals(col.getQueryInputType())) {
							input_map.put("showSeconds", true);
							input_map.put("type", "datetime");
							input_map.put("panelWidth", 180);
							input_map.put("format", col.getFormatter());
							addExParamsToMap(input_map, exParams);
							if (this.isStatistics() && col.getCompareSign() != null) {
								//如果是between类型的统计查询,输入框的name取名要符合前者操作符是BTS 后者是BTE的规则
								if ("BT".equals(col.getCompareSign())) {
									fieldName = "filter_BTS_" + col.getField();
								}
								input_map.put("name", fieldName);
								input_map.put("id", fieldName);
							} else {
								input_map.put("name", col.getField() + "_begin");
								input_map.put("id", col.getField() + "_begin");
							}
							input_html += viewEngine.parseTemplate(
									"/com/xplatform/base/framework/tag/core/ftl/datetimebox.ftl", input_map);
							input_html += "<span class=\"search_input_scopelink\" >~</span>";
							if (this.isStatistics() && col.getCompareSign() != null) {
								//如果是between类型的统计查询,输入框的name取名要符合前者操作符是BTS 后者是BTE的规则
								if ("BT".equals(col.getCompareSign())) {
									fieldName = "filter_BTE_" + col.getField();
								}
								input_map.put("name", fieldName);
								input_map.put("id", fieldName);
							} else {
								input_map.put("name", col.getField() + "_end");
								input_map.put("id", col.getField() + "_end");
							}
							input_html += viewEngine.parseTemplate(
									"/com/xplatform/base/framework/tag/core/ftl/datetimebox.ftl", input_map);
						} else if (InputComponentType.datebox.equals(col.getQueryInputType())) {
							input_map.put("showSeconds", true);
							input_map.put("type", "date");
							input_map.put("panelWidth", 180);
							input_map.put("format", col.getFormatter());
							addExParamsToMap(input_map, exParams);
							if (this.isStatistics() && col.getCompareSign() != null) {
								//如果是between类型的统计查询,输入框的name取名要符合前者操作符是BTS 后者是BTE的规则
								if ("BT".equals(col.getCompareSign())) {
									fieldName = "filter_BTS_" + col.getField();
								}
								input_map.put("name", fieldName);
								input_map.put("id", fieldName);
							} else {
								input_map.put("name", col.getField() + "_begin");
								input_map.put("id", col.getField() + "_begin");
							}
							input_html += viewEngine.parseTemplate(
									"/com/xplatform/base/framework/tag/core/ftl/datetimebox.ftl", input_map);
							input_html += "<span class=\"search_input_scopelink\" >~</span>";
							if (this.isStatistics() && col.getCompareSign() != null) {
								//如果是between类型的统计查询,输入框的name取名要符合前者操作符是BTS 后者是BTE的规则
								if ("BT".equals(col.getCompareSign())) {
									fieldName = "filter_BTE_" + col.getField();
								}
								input_map.put("name", fieldName);
								input_map.put("id", fieldName);
							} else {
								input_map.put("name", col.getField() + "_end");
								input_map.put("id", col.getField() + "_end");
							}
							input_html += viewEngine.parseTemplate(
									"/com/xplatform/base/framework/tag/core/ftl/datetimebox.ftl", input_map);
						} else if (InputComponentType.numberbox.equals(col.getQueryInputType())) {

							input_map.put("groupSeparator", ",");
							input_map.put("decimalSeparator", ".");
							input_map.put("editable", true);
							input_map.put("spinner", false);

							addExParamsToMap(input_map, exParams);

							//如果是numberbox比较特殊,需要套一个span来完成垂直居中
							if (this.isStatistics() && col.getCompareSign() != null) {
								//如果是between类型的统计查询,输入框的name取名要符合前者操作符是BTS 后者是BTE的规则
								if ("BT".equals(col.getCompareSign())) {
									fieldName = "filter_BTS_" + col.getField();
								}
								input_map.put("name", fieldName);
								input_map.put("id", fieldName);
							} else {
								input_map.put("name", col.getField() + "_begin");
								input_map.put("id", col.getField() + "_begin");
							}
							input_html += "<span class=\"search_content_span\">"
									+ viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/number.ftl",
											input_map) + "</span>";
							input_html += "<span class=\"search_input_scopelink\" >~</span>";
							if (this.isStatistics() && col.getCompareSign() != null) {
								//如果是between类型的统计查询,输入框的name取名要符合前者操作符是BTS 后者是BTE的规则
								if ("BT".equals(col.getCompareSign())) {
									fieldName = "filter_BTE_" + col.getField();
								}
								input_map.put("name", fieldName);
								input_map.put("id", fieldName);
							} else {
								input_map.put("name", col.getField() + "_end");
								input_map.put("id", col.getField() + "_end");
							}
							input_html += "<span class=\"search_content_span\">"
									+ viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/number.ftl",
											input_map) + "</span>";
						} else {
							String beginName = "";
							String endName = "";
							if (this.isStatistics() && col.getCompareSign() != null) {
								//如果是between类型的统计查询,输入框的name取名要符合前者操作符是BTS 后者是BTE的规则
								if ("BT".equals(col.getCompareSign())) {
									beginName = "filter_BTS_" + col.getField();
									endName = "filter_BTE_" + col.getField();
								}
								beginName = fieldName;
								endName = fieldName;
							} else {
								beginName = col.getField() + "_begin";
								endName = col.getField() + "_end";
							}
							input_html += ("<input class=\"search_input\" type=\"text\" name=\"" + beginName + "\" "
									+ extendAttribute(col.getExtend()) + "/>");
							input_html += ("<span class=\"search_input_scopelink\" >~</span>");
							input_html += ("<input class=\"search_input\" type=\"text\" name=\"" + endName + "\" "
									+ extendAttribute(col.getExtend()) + "/>");
						}
					}
					sb.append(input_html);
					sb.append("</span>");
				}
				//生成查询、重置按钮
				sb.append("<span class=\"search_button_area\">");
				sb.append("<a href=\"#\" class=\"easyui-linkbutton icon-color\" iconCls=\"glyphicon glyphicon-search\" onclick=\""+name+"search()\">查询</a>&nbsp;");
				sb.append("<a href=\"#\" class=\"easyui-linkbutton icon-color\" iconCls=\"glyphicon glyphicon-refresh\" onclick=\"" + name + "SearchReset('"+name+"')\">重置</a>");
				sb.append("</span>");
				sb.append("</div>");
			}
			
		}
	    return sb.toString();
	}
	
	/**
	 * 生成扩展属性
	 * @param field
	 * @return
	 */
	private String extendAttribute(String field) {
		if(StringUtil.isEmpty(field)){
			return "";
		}
		field = dealSyscode(field,1);
		StringBuilder re = new StringBuilder();
		try{
			JSONObject obj = JSONObject.fromObject(field);
			Iterator it = obj.keys();
			while(it.hasNext()){ //迭代所有key
				String key = String.valueOf(it.next());
				JSONObject nextObj =((JSONObject)obj.get(key)); //获得key指向的value,依旧是个json的java对象
				Iterator itvalue =nextObj.keys();
				re.append(key+"="+"\""); //开始准备,比如iconCls=\"
				if(nextObj.size()<=1){ //如果是单值 
					String onlykey = String.valueOf(itvalue.next());
					if("value".equals(onlykey)){ //这里要注意value的用法,比如传入的json是 extend="class:{value:'easyui-numberbox'}"
						re.append(nextObj.get(onlykey)+""); //会转变为class="easyui-numberbox"
					}else{	//比如传入的json是 extend="style:{width:'300px'}"
						re.append(onlykey+":"+nextObj.get(onlykey)+"");//会转变为style="width:300px"
					}
				}else{//复杂值 比如extend="{data-options:{required:false,groupSeparator:\"','\"},class:{value:'easyui-numberbox'}}"
					/** 在前台传的json要注意单引号和双引号的转义,因为会变成JsonObject对象,所以比如最后还要生成groupSeparator:','的话,在jsp中就要变成
				    data-options:{required:false,groupSeparator:\"','\"
					 **/
					while(itvalue.hasNext()){
						String multkey = String.valueOf(itvalue.next());
						String multvalue = nextObj.getString(multkey);
						re.append(multkey+":"+multvalue+",");
					}
					re.deleteCharAt(re.length()-1);
				}
				re.append("\" ");
			}
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return dealSyscode(re.toString(), 2);
	}
	/**
	 * 处理否含有json转换中的保留字
	 * @param field
	 * @param flag 1:转换 2:还原
	 * @return
	 */
	private String dealSyscode(String field,int flag) {
		String change = field;
		Iterator it = syscode.keySet().iterator();
		while(it.hasNext()){
			String key = String.valueOf(it.next());
			String value = String.valueOf(syscode.get(key));
			if(flag==1){
				change = field.replaceAll(key, value);
			}else if(flag==2){
				change = field.replaceAll(value, key);
			}
		}
		return change;
	}
	/**
	 * 判断是否存在查询字段
	 * @return hasQuery true表示有查询字段,false表示没有
	 
	protected boolean hasQueryColum(List<DataGridColumn> queryColumnList ) {
		boolean hasQuery = false;
		for (DataGridColumn col : columnList) {
			if(col.isQuery()){
				hasQuery =  true;
			}
		}
		return hasQuery;
	}*/
	/**
	 * 拼装操作地址
	 * 
	 * @param sb
	 */
	protected void getOptUrl(StringBuffer sb) {
		//注：操作列表会带入合计列中去，故加此判断
		sb.append("if(!rec."+idField+"){return '';}");
		List<DataGridUrl> list = urlList;
		sb.append("var href='';");
		//处理DataGrid“操作”列的按钮样式
		sb.append("href+=\"<div class='action-buttons'>\";");
		//		String btn_str = "<i class='##iconname## bigger-130'></i>";

		for (DataGridUrl dataGridUrl : list) {
			//			String btn_str = "<button type='button' class='btn btn-minier btn-white'><i></i>" + dataGridUrl.getTitle() + "</button>";
			String btn_str = "<i class='"
					+ dataGridUrl.getIcon()
					+ "' onmouseover=main.showTooltip({obj:this,msg:'"
					+ dataGridUrl.getTitle() + "',trg:'hover',placement:'top'})></i>";
			String url = dataGridUrl.getUrl();
			Integer preinstallWidth = dataGridUrl.getPreinstallWidth();
			String exParams = dataGridUrl.getExParams();
			String defaultParams = null;
			String optWidth=null;
			String optHeight=null;
			if(StringUtil.isNotEmpty(dataGridUrl.getWidth())){
				optWidth=dataGridUrl.getWidth().contains("%")?"'"+dataGridUrl.getWidth()+"'":dataGridUrl.getWidth();
			}
			if(StringUtil.isNotEmpty(dataGridUrl.getHeight())){
				optHeight=dataGridUrl.getHeight().contains("%")?"'"+dataGridUrl.getHeight()+"'":dataGridUrl.getHeight();
			}
			
			MessageFormat formatter = new MessageFormat("");
			if (dataGridUrl.getValue() != null) {
				String[] testvalue = dataGridUrl.getValue().split(",");
				List value = new ArrayList<Object>();
				for (String string : testvalue) {
					value.add("\"+rec." + string + " +\"");
				}
				url = formatter.format(url, value.toArray());
			}
			if (url != null && dataGridUrl.getValue() == null) {

				url = formatUrl(url);
			}
			String exp = dataGridUrl.getExp();// 判断显示表达式
			if (StringUtil.isNotEmpty(exp)) {
				String[] ShowbyFields = exp.split("&&");
				for (String ShowbyField : ShowbyFields) {
					int beginIndex = ShowbyField.indexOf("#");
					int endIndex = ShowbyField.lastIndexOf("#");
					String exptype = ShowbyField.substring(beginIndex + 1,
							endIndex);// 表达式类型
					String field = ShowbyField.substring(0, beginIndex);// 判断显示依据字段
					String[] values = ShowbyField.substring(endIndex + 1,
							ShowbyField.length()).split(",");// 传入字段值
					String value = "";
					for (int i = 0; i < values.length; i++) {
						value += "'" + "" + values[i] + "" + "'";
						if (i < values.length - 1) {
							value += ",";
						}
					}
					if ("eq".equals(exptype)) {
						sb.append("if($.inArray(rec." + field + ",[" + value
								+ "])>=0){");
					}
					if ("ne".equals(exptype)) {
						sb.append("if($.inArray(rec." + field + ",[" + value
								+ "])<0){");
					}
					if ("empty".equals(exptype) && value.equals("'true'")) {
						sb.append("if(rec." + field + "==''){");
					}
					if ("empty".equals(exptype) && value.equals("'false'")) {
						sb.append("if(rec." + field + "!=''){");
					}
				}
			}

			if (OptTypeDirection.Confirm.equals(dataGridUrl.getType())) {
				sb.append("href+=\"<a class=\'green\' href=\'#\' onclick=confirm(\'"
						+ url
						+ "\',\'"
						+ dataGridUrl.getMessage()
						+ "\',\'"
						+ name + "\')>" + btn_str + "</a>\";");
			}
			if (OptTypeDirection.Del.equals(dataGridUrl.getType())) {
				exParams = JSONHelper.extendJson(defaultParams, exParams);
				sb.append("href+=\"<a class=\'red\' href=\'#\' onclick=delObj(\'"
						+ url + "\',\'" + name + "\'");
				if (StringUtil.isNotEmpty(dataGridUrl.getCallback())) {
					sb.append("," + dataGridUrl.getCallback());
				}
				sb.append("," + exParams + ")>" + btn_str + "</a>\";");
			}
			if (OptTypeDirection.Fun.equals(dataGridUrl.getType())) {
				String name = TagUtil.getFunction(dataGridUrl.getFunname());
				String parmars = TagUtil.getFunParams(dataGridUrl.getFunname());
				sb.append("href+=\"<a href=\'#\' onclick=" + name + "("
						+ parmars + ")>" + btn_str + "</a>\";");
			}
			if (OptTypeDirection.OpenWin.equals(dataGridUrl.getType())) {
				exParams = JSONHelper.extendJson(defaultParams, exParams);
				sb.append("href+=\"<a href=\'#\' onclick=createwindow('"
						+ dataGridUrl.getTitle() + "','" + url + "',"
						+ optWidth + ","
						+ optHeight + "," + preinstallWidth + ","
						+ exParams + ")>" + btn_str + "</a>\";");
			}
			if (OptTypeDirection.Deff.equals(dataGridUrl.getType())) {
				sb.append("href+=\"<a href=\'" + url + "' title=\'"
						+ dataGridUrl.getTitle() + "\'>" + btn_str + "</a>\";");
			}
			if (OptTypeDirection.OpenTab.equals(dataGridUrl.getType())) {
				sb.append("href+=\"<a href=\'#\' onclick=addOneTab('"
						+ dataGridUrl.getTitle() + "','" + url + "')>"
						+ btn_str + "</a>\";");
			}
			sb.append("href+=\"\";");

			if (StringUtil.isNotEmpty(exp)) {
				for (int i = 0; i < exp.split("&&").length; i++) {
					sb.append("}");
				}

			}
		}

		sb.insert(sb.lastIndexOf("</a>") + 4, "</div>");
		//		sb.append("href+=\"</div>\"");
		sb.append("return href;");
	}

	/**
	 * 列自定义函数
	 * 
	 * @param sb
	 * @param column
	 */
	protected void getFun(StringBuffer sb, DataGridColumn column) {
		String url = column.getUrl();
		url = formatUrl(url);
		sb.append("var href=\"<a style=\'color:red\' href=\'#\' onclick=" + column.getFunname() + "('" + column.getTitle() + "','" + url + "')>\";");
		sb.append("return href+value+\'</a>\';");

	}

	/**
	 * 格式化URL
	 * 
	 * @return
	 */
	protected String formatUrl(String url) {
		MessageFormat formatter = new MessageFormat("");
		String parurlvalue = "";
		if (url.indexOf("&") >= 0) {
			String beforeurl = url.substring(0, url.indexOf("&"));// 截取请求地址
			String parurl = url.substring(url.indexOf("&") + 1, url.length());// 截取参数
			String[] pras = parurl.split("&");
			List value = new ArrayList<Object>();
			int j = 0;
			for (int i = 0; i < pras.length; i++) {
				if (pras[i].indexOf("{") >= 0 || pras[i].indexOf("#") >= 0) {
					String field = pras[i].substring(pras[i].indexOf("{") + 1, pras[i].lastIndexOf("}"));
					parurlvalue += "&" + pras[i].replace("{" + field + "}", "{" + j + "}");
					value.add("\"+rec." + field + " +\"");
					j++;
				} else {
					parurlvalue += "&" + pras[i];
				}
			}
			url = formatter.format(beforeurl + parurlvalue, value.toArray());
		}
		return url;

	}
	
	
	/**
	 * 拼接字段  普通列
	 * 
	 * @param sb
	 * 
	 */
	 protected void getField(StringBuffer sb){
		 getField(  sb,1);
	 }
	/**
	 * 拼接字段
	 * 
	 * @param sb
	 * @frozen 0 冰冻列    1 普通列
	 */
	protected void getField(StringBuffer sb, int frozen) {
		// 复选框
		if (checkbox && frozen == 0) {
			sb.append("{field:\'ck\',checkbox:\'true\'},");
		}
		int i = 0;
		labelName: for (DataGridColumn column : columnList) {
			String defaultParams = null;
			String dictCode = column.getDictCode();
			String comboUrl = column.getComboUrl();
			String formatter = column.getFormatter();
			String data = column.getData();
			if (column.getField().equals("opt")&&frozen!=0) {
				if (!hasOptButtonResult) {
					//如果是行操作按钮都被过滤了并且操作栏在最后一列,就去掉最后一个','
					if (i == columnList.size() - 1) {
						sb.deleteCharAt(sb.length() - 1);
					}
					continue labelName;
				}
			}
			i++;
			if ((column.isFrozenColumn() && frozen == 0) || (!column.isFrozenColumn() && frozen == 1)) {
				String field;
				if (treegrid) {
					field = column.getTreefield();
				} else {
					field = column.getField();
				}
				sb.append("{field:\'" + field + "\',title:\'" + column.getTitle() + "\'");
				if (column.getWidth() != null) {
					if (column.getWidth().indexOf("%") > -1) {
						sb.append(",width:\'" + column.getWidth() + "\'");
					} else {
						sb.append(",width:" + column.getWidth());
					}
				}
				if (column.getAlign() != null) {
					sb.append(",align:\'" + column.getAlign() + "\'");
				}
				if (column.getHalign() != null) {
					sb.append(",halign:\'" + column.getHalign() + "\'");
				}
				if (StringUtils.isNotEmpty(column.getExtendParams())) {
					sb.append("," + column.getExtendParams().substring(0, column.getExtendParams().length() - 1));
				}
				// 隐藏字段
				if (!column.isHidden()) {
					sb.append(",hidden:true");
				}
				if (!treegrid) {
					// 字段排序
					if ((column.isSortable()) && (field.indexOf("_") <= 0 && field != "opt")) {
						sb.append(",sortable:" + column.isSortable() + "");
					}
				}
				if (column.getMyFormatter() != null) {
					sb.append(",formatter:function(value,rec,index){");
					sb.append(column.getMyFormatter());
					sb.append("}");
				}
				// 显示图片
				if (column.isImage()) {
					sb.append(",formatter:function(value,rec,index){");
					sb.append(" return '<img border=\"0\" src=\"'+value+'\"/>';}");
				}
				// 自定义显示图片
				if (column.getImageSize() != null) {
					String[] tld = column.getImageSize().split(",");
					sb.append(",formatter:function(value,rec,index){");
					sb.append(" return '<img width=\"" + tld[0] + "\" height=\"" + tld[1]
							+ "\" border=\"0\" src=\"'+value+'\"/>';}");
					tld = null;
				}
				if (column.getDownloadName() != null) {
					sb.append(",formatter:function(value,rec,index){");
					sb.append(" return '<a target=\"_blank\" href=\"'+value+'\">" + column.getDownloadName()
							+ "</a>';}");
				}
				// 自定义链接
				if (column.getUrl() != null) {
					sb.append(",formatter:function(value,rec,index){");
					this.getFun(sb, column);
					sb.append("}");
				}
				if (column.getFormatter() != null) {
					sb.append(",formatter:function(value,rec,index){");
					sb.append("if(value==\"\"||value==null||value==undefined){return null}else{return new Date().format('" + column.getFormatter() + "',value);}}");
				}
				if (column.getPrecision() != null) {
					sb.append(",formatter:function(value,rec,index){");
					sb.append("if(value==\"\"||value==null||value==undefined){return null}else{return Number(value).toFixed(" + column.getPrecision() + ");}}");
				}
				// 加入操作
				if (column.getField().equals("opt") && hasOptButtonResult) {
					sb.append(",formatter:function(value,rec,index){");
					// sb.append("return \"");
					this.getOptUrl(sb);
					sb.append("}");
				}
				// 值替換
				if (columnValueList.size() > 0 && !column.getField().equals("opt")) {
					String testString = "";
					for (ColumnValue columnValue : columnValueList) {
						if (columnValue.getName().equals(column.getField())) {
							String[] value = columnValue.getValue().split(",");
							String[] text = columnValue.getText().split(",");
							sb.append(",formatter:function(value,rec,index){");
							for (int j = 0; j < value.length; j++) {
								testString += "if(value=='" + value[j] + "'){return \'" + text[j] + "\'}";
							}
							sb.append(testString);
							sb.append("else{return value}");
							sb.append("}");
						}
					}

				}
				// 背景设置
				if (columnStyleList.size() > 0 && !column.getField().equals("opt")) {
					String testString = "";
					for (ColumnValue columnValue : columnStyleList) {
						if (columnValue.getName().equals(column.getField())) {
							String[] value = columnValue.getValue().split(",");
							String[] text = columnValue.getText().split(",");
							sb.append(",styler:function(value,rec,index){");
							if ((value.length == 0 || StringUtils.isEmpty(value[0])) && text.length == 1) {
								if (text[0].indexOf("(") > -1) {
									testString = " return " + text[0].replace("(", "(value,rec,index");
								} else {
									testString = " return \'" + text[0] + "\'";
								}
							} else {
								for (int j = 0; j < value.length; j++) {
									testString += "if(value=='" + value[j] + "'){return \'" + text[j] + "\'}";
								}
							}
							sb.append(testString);
							sb.append("}");
						}
					}

				}
				//设置可编辑属性
				if (editable) {
					String editType = "validatebox";
					String dHeight = "22";
					if (StringUtil.equals(column.getEditType(), "text")) {
						editType = "text";
					} else if (StringUtil.equals(column.getEditType(), "textarea")) {
						editType = "textarea";
					} else if (StringUtil.equals(column.getEditType(), "checkbox")) {
						editType = "checkbox";
					} else if (StringUtil.equals(column.getEditType(), "numberbox")) {
						editType = "numberbox";
					} else if (StringUtil.equals(column.getEditType(), "validatebox")) {
						editType = "validatebox";
					} else if (StringUtil.equals(column.getEditType(), "combobox")) {
						editType = "combobox";
						defaultParams = "{valueField:'id',textField:'text',panelHeight:'auto',editable:false}";
						//如果是级联的字段,则在options中增加级联的combobox配置
						if (StringUtil.isNotEmpty(column.getGroupName())) {
							String groupFields = cascadeMap.get(column.getGroupName());
							String[] groupFieldsArray = groupFields.split(",");
							int fieldIndex = StringUtil.getIndexOfStrArray(groupFieldsArray, column.getField());
							String onBeforeLoadOptions = "{onBeforeLoad:function(param){var self=this;return editorCascadeBeforeLoad(param,self,"
									+ column.getCascadeIndex() + ",'" + groupFields + "');}}";
							String onSelectOptions = "{onSelect:function(record){var self=this;return editorCascadeSelect(record,self,"
									+ column.getCascadeIndex() + ",'" + groupFields + "');}}";

							if (fieldIndex == 0) { //首个级联只有onSelect事件
								defaultParams = JSONHelper.extendJson(defaultParams, onSelectOptions);
							} else if (fieldIndex == groupFieldsArray.length - 1) { //最后级联只有onBeforeLoad事件
								defaultParams = JSONHelper.extendJson(defaultParams, onBeforeLoadOptions);
							} else { //中间的级联同时有onSelect和onBeforeLoad事件
								defaultParams = JSONHelper.extendJson(defaultParams, onBeforeLoadOptions);
								defaultParams = JSONHelper.extendJson(defaultParams, onSelectOptions);
							}
						}
						if (StringUtil.isNotEmpty(comboUrl)) {
							String tempUrl = "{url:'" + comboUrl + "'}";
							defaultParams = JSONHelper.extendJson(defaultParams, tempUrl);
						}
						if (StringUtil.isNotEmpty(data)) {
							String tempData = "{data:" + data + "}";
							defaultParams = JSONHelper.extendJson(defaultParams, tempData);
						}
						if (StringUtil.isNotEmpty(dictCode)) {
							DictTypeService dictTypeService = ApplicationContextUtil.getBean("dictTypeService");
							List<ComboBox> valueList = dictTypeService.findCacheByCode(dictCode);
							String jsonList = JSONHelper.toJSONString(valueList);
							String dataJson = "{data:" + jsonList + "}";
							defaultParams = JSONHelper.extendJson(defaultParams, dataJson);
						}

					} else if (StringUtil.equals(column.getEditType(), "combotree")) {
						editType = "combotree";
						defaultParams = "{height:" + dHeight + ",panelHeight:'auto',editable:false}";
						if (column.isAysn()) {
							if (StringUtil.isNotEmpty(dictCode)) {
								String tempUrl = "{url:'" + "commonController.do?getDictValueTree&dictCode=" + dictCode
										+ "'}";
								defaultParams = JSONHelper.extendJson(defaultParams, tempUrl);
							} else {
								String tempUrl = "{url:'" + comboUrl + "'}";
								defaultParams = JSONHelper.extendJson(defaultParams, tempUrl);
							}
						} else {
							if (StringUtil.isNotEmpty(comboUrl)) {
								String tempUrl = "{url:'" + comboUrl + "'}";
								defaultParams = JSONHelper.extendJson(defaultParams, tempUrl);
							} else if (StringUtil.isNotEmpty(data)) {
								String tempData = "{data:" + data + "}";
								defaultParams = JSONHelper.extendJson(defaultParams, tempData);
							} else if (StringUtil.isNotEmpty(dictCode)) {
								DictTypeService dictTypeService = ApplicationContextUtil.getBean("dictTypeService");
								String treeJson = dictTypeService.findCacheByCode(dictCode);
								String dataJson = "{data:" + treeJson + "}";
								defaultParams = JSONHelper.extendJson(defaultParams, dataJson);
							}
						}
					} else if (StringUtil.equals(column.getEditType(), "datebox")) {
						editType = "datebox";
						defaultParams = "{editable:false,parser:dateParser,formatter:function(date){";
						if (StringUtil.isEmpty(formatter)) {
							formatter = "yyyy-MM-dd";
						}
						defaultParams += "return datetimeFormatters(date,\"" + formatter + "\");}}";
					} else if (StringUtil.equals(column.getEditType(), "datetimebox")) {
						editType = "datetimebox";
						defaultParams = "{editable:false,parser:dateParser,formatter:function(date){";
						if (StringUtil.isEmpty(formatter)) {
							formatter = "yyyy-MM-dd HH:mm:ss";
						}
						defaultParams += "return datetimeFormatters(date,\"" + formatter + "\");}}";
					} else if (StringUtil.equals(column.getEditType(), "commonselect")) {
						editType = "commonselect";
						defaultParams = "{editable:false}";
					}
					if (column.getField().equals("opt")) {

					} else {
						String editor = ",editor : {" + "type : '" + editType + "'," + "options :"
								+ JSONHelper.extendJson(defaultParams, column.getEditorParams()) + "}";
						sb.append(editor);
					}

				}

				sb.append("}");
				// 给field之间添加','隔开
				if (i < columnList.size()) {
					sb.append(",");
				}

			}
		}
	}
	/**
	 * 设置分页条信息
	 * 
	 * @param sb
	 */
	protected void setPager(StringBuffer sb, String grid) {
		sb.append("$(\'#" + name + "\')." + grid + "(\'getPager\').pagination({");
		sb.append("beforePageText:\'\'," + "afterPageText:\'/{pages}\',");
		if (showText) {
			sb.append("displayMsg:\'{from}-{to}共{total}条\',");
		} else {
			sb.append("displayMsg:\'\',");
		}
		if (BeanUtils.isNotEmpty(pageNumber)) {
			sb.append("pageNumber:" + pageNumber + ",");
		}
		if (showPageList == true) {
			sb.append("showPageList:true,");
		} else {
			sb.append("showPageList:false,");
		}
		sb.append("showRefresh:" + showRefresh + "");
		sb.append("});");// end getPager
		sb.append("$(\'#" + name + "\')." + grid + "(\'getPager\').pagination({");
		sb.append("onBeforeRefresh:function(pageNumber, pageSize){ $(this).pagination(\'loading\');$(this).pagination(\'loaded\'); }");
		sb.append("});");
	}
	//列表查询框函数
	protected void searchboxFun(StringBuffer sb,String grid)
	{
		sb.append("function "+name+"searchbox(value,name){");
		sb.append("var queryParams=$(\'#" + name + "\')."+grid+"('options').queryParams;");
		sb.append("queryParams[name]=value;queryParams.searchfield=name;$(\'#" + name + "\')." + grid + "(\'reload\');}");
		sb.append("$(\'#"+name+"searchbox\').searchbox({");
		sb.append("searcher:function(value,name){");
		sb.append(""+name+"searchbox(value,name);");
		sb.append("},");
		sb.append("menu:\'#"+name+"mm\',");
		sb.append("prompt:\'请输入查询关键字\'");
		sb.append("});");
	}
  
	/*public String getNoAuthOperButton(){
		List<String> nolist = (List<String>) super.pageContext.getRequest().getAttribute("noauto_operationCodes");
		StringBuffer sb = new StringBuffer();
		if(ClientUtil.getSessionUserName().getUserName().equals("admin")|| !Globals.BUTTON_AUTHORITY_CHECK){
		}else{
			if(nolist!=null&&nolist.size()>0){
				for(String s:nolist){
					sb.append("$('#" + name + "tb\').find(\""+s.replaceAll(" ", "")+"\").hide();");
				}
			}
		}
		com.xplatform.base.framework.core.util.LogUtil.info("----getNoAuthOperButton-------"+sb.toString());
		return sb.toString(); 
	}*/
	
	
	/**
	 * 描述：组装菜单按钮操作权限
	 * dateGridUrl：url
	 * operationCode：操作码
	 * optList： 操作列表
	 * buttonFlag:1为"操作"列按钮,2为toolbar的按钮
	 * @version 1.0
	 */
	private void installOperationCode(DataGridUrl dataGridUrl, String operationCode, List optList, int buttonFlag) {
		if ((StringUtil.isNotEmpty(ClientUtil.getUserEntity().getUserName()) && ClientUtil.getUserEntity().getUserName().equals("admin"))
				|| !Globals.BUTTON_AUTHORITY_CHECK) {
			optList.add(dataGridUrl);
			if (buttonFlag == 1) {
				if (!showOptColumn) {
					hasOptButtonResult = false;
				} else {
					hasOptButtonResult = true;
				}
			}
		} else if (StringUtil.isNotEmpty(operationCode)) {
			Set<String> operationCodes = (Set<String>) super.pageContext.getRequest().getAttribute("operationCodes");
			if (null != operationCodes) {
				for (String MyoperationCode : operationCodes) {
					if (MyoperationCode.equals(operationCode)) {
						// 如果在操作按钮字符集合中中存在,说明操作栏有按钮,将hasOptButton置为true
						if (allOptButtonTypes.indexOf(dataGridUrl.getType().toString()) != -1) {
							if (buttonFlag == 1) {
								if (!showOptColumn) {
									hasOptButtonResult = false;
								} else {
									hasOptButtonResult = true;
								}
							}
						}
						optList.add(dataGridUrl);
					}
				}
			}
		} else {
			optList.add(dataGridUrl);
			if (buttonFlag == 1) {
				if (!showOptColumn) {
					hasOptButtonResult = false;
				} else {
					hasOptButtonResult = true;
				}
			}
		}
	}
	
	/**
	 * 获取自动补全的panel
	 * @param filed
	 * @author JueYue
	 * @return
	 */
	private String getAutoSpan(String filed,String extend){
		String id = filed.replaceAll("\\.","_");
		StringBuffer nsb = new StringBuffer();
		nsb.append("<script type=\"text/javascript\">");
		nsb.append("$(document).ready(function() {") 
		.append("$(\"#"+getEntityName()+"_"+id+"\").autocomplete(\"commonController.do?getAutoList\",{")
		.append("max: 5,minChars: 2,width: 200,scrollHeight: 100,matchContains: true,autoFill: false,extraParams:{")
        .append("featureClass : \"P\",style : \"full\",	maxRows : 10,labelField : \""+filed+"\",valueField : \""+filed+"\",")
		.append("searchField : \""+filed+"\",entityName : \""+getEntityName()+"\",trem: function(){return $(\"#"+getEntityName()+"_"+id+"\").val();}}");
		nsb.append(",parse:function(data){return jeecgAutoParse.call(this,data);}");
		nsb.append(",formatItem:function(row, i, max){return row['"+filed+"'];} ");
		nsb.append("}).result(function (event, row, formatted) {");
		nsb.append("$(\"#"+getEntityName()+"_"+id+"\").val(row['"+filed+"']);}); });")
        .append("</script>")
        .append("<input type=\"text\" id=\""+getEntityName()+"_"+id+"\" name=\""+filed+"\" datatype=\"*\" "+extend+" nullmsg=\"\" errormsg=\"输入错误\"/>");
		return nsb.toString();
	}
	/**
	 * 获取实体类名称,没有这根据规则设置
	 * @return
	 */
	private String getEntityName() {
		if(StringUtils.isEmpty(entityName)){
			entityName = actionUrl.substring(0,actionUrl.indexOf("Controller"));
			entityName = (entityName.charAt(0)+"").toUpperCase()+entityName.substring(1)+"Entity";
		}
		return entityName;
	}
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年6月19日 上午11:30:23
	 * @Decription 添加自己扩展的参数到map中
	 *
	 * @param input_map
	 * @param exParams
	 */
		
	private void addExParamsToMap(Map<String,Object> input_map,String exParams){
		//用@@符号分割不同的参数
		if(StringUtil.isNotEmpty(exParams)){
			String[] exParamsArray=exParams.split("@@");
			for(String oneExParam:exParamsArray){
				//只分割第一个冒号(处理值中还有json对象的情况)
				String[] exParam=oneExParam.split("\\:",2);
				//如果是boolean类型的值
				if("true".equals(exParam[1])||"false".equals(exParam[1])){
					input_map.put(exParam[0], new Boolean(exParam[1]));
				}else{
					input_map.put(exParam[0], exParam[1]);
				}
				
			}
		}
	}
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年9月12日 下午4:15:43
	 * @Decription 根据页面类型标识 处理datagrid,比如只读查看页面时,datagrid各种编辑,按钮都要禁用/隐藏
	 *
	 */
	private void optFlagHandler() {
		//获得request
		ServletRequest request = this.pageContext.getRequest();
		String optFlag = request.getAttribute("optFlag") == null ? "" : request.getAttribute("optFlag").toString();
		if (StringUtil.isNotEmpty(optFlag)) {
			if ("detail".equals(optFlag)) {
				showButtonDiv = false;
				editable = false;
				checkbox = false;
				for (int i = 0; i < urlList.size(); i++) {
					DataGridUrl url = urlList.get(i);
					Boolean flag = url.getIsShowInViewPage();
					if (!flag) {
						urlList.remove(i);
					}
				}
				if (urlList.size() > 0) {
					hasOptButtonResult = true;
				} else {
					hasOptButtonResult = false;
				}
			}
		}
	}
	
	public boolean isFitColumns() {
		return fitColumns;
	}

	public void setFitColumns(boolean fitColumns) {
		this.fitColumns = fitColumns;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getQueryMode() {
		return queryMode;
	}

	public void setQueryMode(String queryMode) {
		this.queryMode = queryMode;
	}

	public boolean isAutoLoadData() {
		return autoLoadData;
	}

	public void setAutoLoadData(boolean autoLoadData) {
		this.autoLoadData = autoLoadData;
	}

	public void setOpenFirstNode(boolean openFirstNode) {
		this.openFirstNode = openFirstNode;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setRowStyler(String rowStyler) {
		this.rowStyler = rowStyler;
	}

	public void setExtendParams(String extendParams) {
		this.extendParams = extendParams;
	}

	public boolean getEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isDefaultOpt() {
		return defaultOpt;
	}

	public void setDefaultOpt(boolean defaultOpt) {
		this.defaultOpt = defaultOpt;
	}

	public boolean isStatistics() {
		return statistics;
	}

	public void setStatistics(boolean statistics) {
		this.statistics = statistics;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getGroupField() {
		return groupField;
	}

	public void setGroupField(String groupField) {
		this.groupField = groupField;
	}

	public String getGroupFormatter() {
		return groupFormatter;
	}

	public void setGroupFormatter(String groupFormatter) {
		this.groupFormatter = groupFormatter;
	}


	public Boolean getShowButtonDiv() {
		return showButtonDiv;
	}

	public void setShowButtonDiv(Boolean showButtonDiv) {
		this.showButtonDiv = showButtonDiv;
	}

	public Boolean getShowOptColumn() {
		return showOptColumn;
	}

	public void setShowOptColumn(Boolean showOptColumn) {
		this.showOptColumn = showOptColumn;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getResizeHandle() {
		return resizeHandle;
	}

	public void setResizeHandle(String resizeHandle) {
		this.resizeHandle = resizeHandle;
	}

	public Boolean getAutoRowHeight() {
		return autoRowHeight;
	}

	public void setAutoRowHeight(Boolean autoRowHeight) {
		this.autoRowHeight = autoRowHeight;
	}

	public Boolean getStriped() {
		return striped;
	}

	public void setStriped(Boolean striped) {
		this.striped = striped;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Boolean getNowrap() {
		return nowrap;
	}

	public void setNowrap(Boolean nowrap) {
		this.nowrap = nowrap;
	}

	public String getLoadMsg() {
		return loadMsg;
	}

	public void setLoadMsg(String loadMsg) {
		this.loadMsg = loadMsg;
	}

	public Boolean getRownumbers() {
		return rownumbers;
	}

	public void setRownumbers(Boolean rownumbers) {
		this.rownumbers = rownumbers;
	}

	public Boolean getSingleSelect() {
		return singleSelect;
	}

	public void setSingleSelect(Boolean singleSelect) {
		this.singleSelect = singleSelect;
	}

	public Boolean getCheckOnSelect() {
		return checkOnSelect;
	}

	public void setCheckOnSelect(Boolean checkOnSelect) {
		this.checkOnSelect = checkOnSelect;
	}

	public Boolean getSelectOnCheck() {
		return selectOnCheck;
	}

	public void setSelectOnCheck(Boolean selectOnCheck) {
		this.selectOnCheck = selectOnCheck;
	}

	public Boolean getRemoteSort() {
		return remoteSort;
	}

	public void setRemoteSort(Boolean remoteSort) {
		this.remoteSort = remoteSort;
	}

	public Boolean getShowHeader() {
		return showHeader;
	}

	public void setShowHeader(Boolean showHeader) {
		this.showHeader = showHeader;
	}

	public Boolean getShowFooter() {
		return showFooter;
	}

	public void setShowFooter(Boolean showFooter) {
		this.showFooter = showFooter;
	}

	public Integer getScrollbarSize() {
		return scrollbarSize;
	}

	public void setScrollbarSize(Integer scrollbarSize) {
		this.scrollbarSize = scrollbarSize;
	}

	public Boolean getAnimate() {
		return animate;
	}

	public void setAnimate(Boolean animate) {
		this.animate = animate;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getPagePosition() {
		return pagePosition;
	}

	public void setPagePosition(String pagePosition) {
		this.pagePosition = pagePosition;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public boolean isTreegrid() {
		return treegrid;
	}

	public int getPageSize() {
		return pageSize;
	}

	public boolean isPagination() {
		return pagination;
	}

	public boolean isShowRefresh() {
		return showRefresh;
	}

	public boolean isShowText() {
		return showText;
	}

	public boolean isShowPageList() {
		return showPageList;
	}

	public String getWidth() {
		return width;
	}

	public String getHeight() {
		return height;
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public boolean isOpenFirstNode() {
		return openFirstNode;
	}

	public boolean isFit() {
		return fit;
	}

	public String getStyle() {
		return style;
	}

	public String getOnLoadSuccess() {
		return onLoadSuccess;
	}

	public String getOnClick() {
		return onClick;
	}

	public String getRowStyler() {
		return rowStyler;
	}

	public String getExtendParams() {
		return extendParams;
	}

	public String getExportParams() {
		return exportParams;
	}

	public void setExportParams(String exportParams) {
		this.exportParams = exportParams;
	}

	public String getImportParams() {
		return importParams;
	}

	public void setImportParams(String importParams) {
		this.importParams = importParams;
	}

	public Boolean getBorder() {
		return border;
	}

	public void setBorder(Boolean border) {
		this.border = border;
	}

	public String getOnDblClickRow() {
		return onDblClickRow;
	}

	public void setOnDblClickRow(String onDblClickRow) {
		this.onDblClickRow = onDblClickRow;
	}

}
