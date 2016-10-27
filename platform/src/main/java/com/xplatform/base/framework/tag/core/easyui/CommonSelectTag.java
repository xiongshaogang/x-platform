package com.xplatform.base.framework.tag.core.easyui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.vo.easyui.GridFieldJsonModel;

/**
 * description : 通用弹出页选择标签
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月16日 下午2:46:36
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年7月16日 下午2:46:36
 *
*/

public class CommonSelectTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private String displayId; //显示的input框的id(不写的话,则默认为和name一样)
	private String displayName;//显示的input框的name
	private String hiddenId; //隐藏的input框的id(不写的话,则默认为和name一样)
	private String hiddenName;//隐藏的input框的name
	private String displayValue = ""; //显示的input框的value
	private String hiddenValue = "";//隐藏的input框的value
	private String title;//弹出框标题

	private Boolean multiples = true;//是否多选
	private Boolean customInput = false;//是否自定义选择框(标签只产生选择按钮),要注意使用textarea的时候,结束符要与内容齐平,不要换行,例如 xxxx,yyyyy,aaaa</textarea>
	private String url; //弹出页datagrid加载数据的url(参数连接需使用@@,而不是&,比如jobController.do?datagrid@@t1=1@@t2=2),记得给url配资源权限

	/* grid中显示的列,以及其配置(比如哪些需要隐藏,哪些需要作为查询项,宽度等)用json形式存储
	 * 有field、hidden、width、title、query、queryMode、queryInputType、queryExParams、backField,dictCode几个字段
	 * 含义就与<t:dgCol>标签的字段一样,backField特殊些,标识该列的数据返回到父页面哪个input中(input的name属性去匹配),不填写就表示该列不返回
	 * 
	 * 使用范例:
	 * gridFieldsJson='[{field:"id",hidden:"false",title:"主键",backField:"id"},
	   {field:"code",width:"300",title:"岗位编码",queryExParams:"groupSeparator:,@@decimalSeparator:.@@precision:2",queryMode:"group",query:"true",queryInputType:"numberbox"},
	   {field:"name",width:"200",title:"岗位名称",backField:"name"},
	   {field:"shortName",width:"200",title:"岗位简称",backField:"shortName_input"}]' 
	 *  
	 *  后续版本则是通过<t:csField>子标签来生成这个gridFieldsJson的内容,无需自己书写
	*/
	private String gridFieldsJson;
	private String width = "700"; //弹出页宽度
	private String height = "460"; //弹出页高度
	private String buttonColorClass = "btn-purple"; //选择按钮颜色的bootstrap class(btn-xxx 的形式,不是#FFF这颜色代码)
	private String icon = "awsm-icon-chevron-sign-down";//选择按钮图标
	private String text;//按钮文字
	private String callback; //保存或完成选择后的js回调函数
	private String idOrName = "name";//返回数据时,通过name还是id匹配

	/* 左树过滤相关 */
	private Boolean hasTree = false;//是否有过滤树
	private Boolean expandAll = true;//是否展开所有节点(展示完整的过滤树)
	private String treeUrl;//树加载数据url(参数连接需使用@@,而不是&,比如jobController.do?datagrid@@t1=1@@t2=2),记得给url配资源权限
	private String gridTreeFilter;//列表datagrid中被左树过滤的字段

	/* 验证相关 */
	protected String datatype;//验证类型
	protected String sucmsg;//验证成功提示
	protected String nullmsg;//为空提示
	protected String errormsg;//验证错误提示
	protected String ajaxurl;//远程验证url

	private Map<String, Object> root = new HashMap<String, Object>();
	private List<GridFieldJsonModel> columnList = new ArrayList<GridFieldJsonModel>();

	public void setColumn(String title, String field, String width, String hidden, String query, String queryMode,
			String queryInputType, String queryExParams, String backField, String dictCode) {
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
		columnList.add(model);
	}

	public int doStartTag() throws JspTagException {
		root.clear();
		columnList.clear();
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {

		root.put("displayId", StringUtil.isEmpty(displayId) ? displayName : displayId);
		root.put("displayName", displayName);

		if (StringUtil.isEmpty(hiddenId)) {
			root.put("hiddenId", hiddenName);
		} else {
			root.put("hiddenId", hiddenId);
		}
		root.put("hiddenName", hiddenName);

		root.put("displayValue", displayValue);
		root.put("hiddenValue", hiddenValue);
		root.put("title", title);

		root.put("multiples", multiples);
		root.put("customInput", customInput);
		root.put("url", url);
		gridFieldsJson = JSONHelper.toJSONString(columnList);
		root.put("gridFieldsJson", gridFieldsJson);
		root.put("width", width);
		root.put("height", height);
		root.put("buttonColorClass", buttonColorClass);
		root.put("icon", icon);
		root.put("text", text);

		root.put("hasTree", hasTree);
		root.put("expandAll", expandAll);
		root.put("treeUrl", treeUrl);
		root.put("gridTreeFilter", gridTreeFilter);
		root.put("idOrName", idOrName);

		root.put("errormsg", errormsg);
		root.put("nullmsg", nullmsg);
		root.put("sucmsg", sucmsg);
		root.put("ajaxurl", ajaxurl);
		root.put("callback", callback);

		JspWriter out = this.pageContext.getOut();

		FreemarkerHelper viewEngine = new FreemarkerHelper();
		String html = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/commonSelect.ftl", root);
		try {
			out.print(html);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public String getDisplayId() {
		return displayId;
	}

	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getHiddenId() {
		return hiddenId;
	}

	public void setHiddenId(String hiddenId) {
		this.hiddenId = hiddenId;
	}

	public String getHiddenName() {
		return hiddenName;
	}

	public void setHiddenName(String hiddenName) {
		this.hiddenName = hiddenName;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getHiddenValue() {
		return hiddenValue;
	}

	public void setHiddenValue(String hiddenValue) {
		this.hiddenValue = hiddenValue;
	}

	public Boolean getMultiples() {
		return multiples;
	}

	public void setMultiples(Boolean multiples) {
		this.multiples = multiples;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getGridFieldsJson() {
		return gridFieldsJson;
	}

	public void setGridFieldsJson(String gridFieldsJson) {
		this.gridFieldsJson = gridFieldsJson;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getButtonColorClass() {
		return buttonColorClass;
	}

	public void setButtonColorClass(String buttonColorClass) {
		this.buttonColorClass = buttonColorClass;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getSucmsg() {
		return sucmsg;
	}

	public void setSucmsg(String sucmsg) {
		this.sucmsg = sucmsg;
	}

	public String getNullmsg() {
		return nullmsg;
	}

	public void setNullmsg(String nullmsg) {
		this.nullmsg = nullmsg;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getAjaxurl() {
		return ajaxurl;
	}

	public void setAjaxurl(String ajaxurl) {
		this.ajaxurl = ajaxurl;
	}

	public Boolean getCustomInput() {
		return customInput;
	}

	public void setCustomInput(Boolean customInput) {
		this.customInput = customInput;
	}

	public Boolean getHasTree() {
		return hasTree;
	}

	public void setHasTree(Boolean hasTree) {
		this.hasTree = hasTree;
	}

	public Boolean getExpandAll() {
		return expandAll;
	}

	public void setExpandAll(Boolean expandAll) {
		this.expandAll = expandAll;
	}

	public String getTreeUrl() {
		return treeUrl;
	}

	public void setTreeUrl(String treeUrl) {
		this.treeUrl = treeUrl;
	}

	public String getGridTreeFilter() {
		return gridTreeFilter;
	}

	public void setGridTreeFilter(String gridTreeFilter) {
		this.gridTreeFilter = gridTreeFilter;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getIdOrName() {
		return idOrName;
	}

	public void setIdOrName(String idOrName) {
		this.idOrName = idOrName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
