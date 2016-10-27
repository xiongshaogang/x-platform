package com.xplatform.base.framework.tag.core.easyui;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.json.JSONObject;

import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.tag.vo.easyui.GridFieldJsonModel;

/**
 * 
 * 类描述：列表字段处理项目
 * 
 * 张代浩
 * @date： 日期：2012-12-7 时间：上午10:17:45
 * @version 1.0
 */
public class DataGridColumnTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	protected String title;
	protected String field;
	protected String width;
	protected String rowspan;
	protected String colspan;
	protected String align="left";
	protected String halign = "left";
	protected boolean sortable = true;
	protected boolean checkbox;
	protected String formatter;
	protected String precision;//如果该列是小数类型(比如金额),此字段表示格式化到小数点后几位,含四舍五入逻辑(比如原值,32.4566,t:dgCol不指定该值,依然是32.4566,如果precision="2",则显示值为32.46)
	protected boolean hidden = true;
	protected String replace;//用于替换列中值的规则,比如1变成男,0变成女,那就是"男_1,女_0"
	protected String treefield;
	protected boolean image; 
	protected String sql;

	private boolean frozenColumn = false; // 是否是冰冻列    默认不是
	protected boolean bSearchable = true;
	protected String url;//自定义链接
	protected String arg;//自定义链接传入参数字段
	protected String funname = "openwindow";//自定义函数名称

	protected String style; //Td的CSS
	protected String imageSize;//自定义图片显示大小
	protected String downloadName;//附件下载
	protected String myFormatter;//自定义的formatter内容
	private boolean autocomplete = false;//自动完成
	private String extendParams;//扩展easyui有的columns参数(在.datagrid()时的columns参数上的参数)
	/**
	 * 可编辑类型
	 * ( validatebox默认,text, datetimebox, datebox, combobox, combotree, combogrid, numberbox,checkbox,textarea )
	 */
	private String editType;

	/** 查询相关 **/
	protected boolean query;//是否作为查询字段
	protected String queryMode = "single";//字段查询模式：single单字段查询；group范围查询
	/** 
	 * 查询框输入类型
	 * ( text默认, datetimebox, datebox, combobox, combotree, combogrid, numberbox )
	 **/
	protected String queryInputType = "text";

	/****************** 数据字典查询框相关 ******************/
	protected boolean aysn = false;//combotree数据是否异步加载(true为异步加载,false为一次性加载)
	protected String dictCode;//数据字典code
	protected String data;//combobox或combotree使用的临时数据
	protected String comboUrl;//combobox或combotree使用url
	protected String extend; //扩展查询时input、select等元素上的属性

	/** 扩展查询时input、select等元素上的data-options属性,用@@分割不同参数,冒号后面的值,比如字符串,符号,布尔值不需要加单引号或双引号
	 * 形如<t:dgCol ... queryExParams="groupSeparator:,@@decimalSeparator:.@@precision:2" queryInputType="Number"/>
	**/
	protected String queryExParams;
	protected String editorParams; //可编辑列中的editor的具体参数options
	protected String compareSign; //比较符(用于给查询框的name 改造成 filter_比较符_字段名 的形式)

	/****************** 行编辑级联相关(仅限editType为combobox类型) ******************/
	protected String groupName;//级联组名(同行可能存在多组级联),同一组设置同样的值
	protected Integer cascadeIndex;//级联索引位置(从1开始)
	private List<GridFieldJsonModel> columnList = new ArrayList<GridFieldJsonModel>();

	public void setColumn(String title, String field, String width, String hidden, String query, String queryMode,
			String queryInputType, String queryExParams, String backField, String dictCode,String halign,String align) {
		GridFieldJsonModel model = new GridFieldJsonModel();
		model.setTitle(title);
		model.setField(field);
		model.setWidth(width);
		model.setHidden(hidden);
		model.setQuery(query);
		model.setQueryMode(queryMode);
		model.setQueryInputType(queryInputType);
		model.setQueryExParams(queryExParams);
		model.setBackField(backField);
		model.setDictCode(dictCode);
		model.setHalign(halign);
		model.setAlign(align);
		columnList.add(model);
	}

	public int doStartTag() throws JspTagException {
		columnList.clear();
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {

		//如果dgCol还有子标签deField的话,说明是编辑类型为自定义的选择页,在此合并传递的各类参数
		if (columnList.size() > 0) {
			JSONObject gridFieldsJsonObj = new JSONObject();
			gridFieldsJsonObj.accumulate("gridFieldsJson", columnList);

			JSONObject editorParamsObj = JSONHelper.toJSONObject(editorParams);

			editorParams = JSONHelper.mergeJson(gridFieldsJsonObj, editorParamsObj).toString();
		}

		Tag t = findAncestorWithClass(this, DataGridTag.class);
		DataGridTag parent = (DataGridTag) t;
		parent.setColumn(title, field, width, rowspan, colspan, align, sortable, checkbox, formatter, hidden, replace,
				treefield, image, imageSize, query, url, funname, arg, queryMode, dictCode, frozenColumn, extend,
				style, downloadName, autocomplete, extendParams, editType, data, queryInputType, comboUrl,
				queryExParams, editorParams, sql, myFormatter, compareSign, groupName, cascadeIndex,aysn,precision,halign);
		return EVAL_PAGE;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getRowspan() {
		return rowspan;
	}

	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}

	public String getColspan() {
		return colspan;
	}

	public void setColspan(String colspan) {
		this.colspan = colspan;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public String getReplace() {
		return replace;
	}

	public void setReplace(String replace) {
		this.replace = replace;
	}

	public String getTreefield() {
		return treefield;
	}

	public void setTreefield(String treefield) {
		this.treefield = treefield;
	}

	public boolean isImage() {
		return image;
	}

	public void setImage(boolean image) {
		this.image = image;
	}

	public boolean isFrozenColumn() {
		return frozenColumn;
	}

	public void setFrozenColumn(boolean frozenColumn) {
		this.frozenColumn = frozenColumn;
	}

	public boolean isbSearchable() {
		return bSearchable;
	}

	public void setbSearchable(boolean bSearchable) {
		this.bSearchable = bSearchable;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getArg() {
		return arg;
	}

	public void setArg(String arg) {
		this.arg = arg;
	}

	public String getFunname() {
		return funname;
	}

	public void setFunname(String funname) {
		this.funname = funname;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getImageSize() {
		return imageSize;
	}

	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}

	public String getDownloadName() {
		return downloadName;
	}

	public void setDownloadName(String downloadName) {
		this.downloadName = downloadName;
	}

	public boolean isAutocomplete() {
		return autocomplete;
	}

	public void setAutocomplete(boolean autocomplete) {
		this.autocomplete = autocomplete;
	}

	public String getExtendParams() {
		return extendParams;
	}

	public void setExtendParams(String extendParams) {
		this.extendParams = extendParams;
	}

	public String getEditType() {
		return editType;
	}

	public void setEditType(String editType) {
		this.editType = editType;
	}

	public boolean isQuery() {
		return query;
	}

	public void setQuery(boolean query) {
		this.query = query;
	}

	public String getQueryMode() {
		return queryMode;
	}

	public void setQueryMode(String queryMode) {
		this.queryMode = queryMode;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getQueryInputType() {
		return queryInputType;
	}

	public void setQueryInputType(String queryInputType) {
		this.queryInputType = queryInputType;
	}

	public String getComboUrl() {
		return comboUrl;
	}

	public void setComboUrl(String comboUrl) {
		this.comboUrl = comboUrl;
	}

	public String getQueryExParams() {
		return queryExParams;
	}

	public void setQueryExParams(String queryExParams) {
		this.queryExParams = queryExParams;
	}

	public String getEditorParams() {
		return editorParams;
	}

	public void setEditorParams(String editorParams) {
		this.editorParams = editorParams;
	}

	public String getMyFormatter() {
		return myFormatter;
	}

	public void setMyFormatter(String myFormatter) {
		this.myFormatter = myFormatter;
	}

	public String getCompareSign() {
		return compareSign;
	}

	public void setCompareSign(String compareSign) {
		this.compareSign = compareSign;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getCascadeIndex() {
		return cascadeIndex;
	}

	public void setCascadeIndex(Integer cascadeIndex) {
		this.cascadeIndex = cascadeIndex;
	}

	public boolean isAysn() {
		return aysn;
	}

	public void setAysn(boolean aysn) {
		this.aysn = aysn;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getHalign() {
		return halign;
	}

	public void setHalign(String halign) {
		this.halign = halign;
	}

}
