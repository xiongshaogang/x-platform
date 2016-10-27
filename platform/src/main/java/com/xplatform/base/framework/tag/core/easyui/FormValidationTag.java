package com.xplatform.base.framework.tag.core.easyui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.framework.core.util.StringUtil;

/**
 * 
 * @author 张代浩
 *
 */
public class FormValidationTag extends TagSupport {
	protected String formid = "formobj";// 表单FORM ID
	protected Boolean refresh = true;
	/**
	 * 提交前处理函数,注意:无参js要写成beforeSubmit="test"而不是beforeSubmit="test()",
	 * 在funtion test()或者function test(a,b)中会在末尾会自动带上一个参数获得当前表单,
	 * 比如在t:formvalid指定一个无参beforeSubmit <t:formvalid beforeSubmit="test()" ....
	 * 在test定义中可以这样 function test(curform){alert(curform)} 
	 * add by xiaqiang 2014.5.20
	 */
	protected String beforeSubmit;
	protected String btnsub = "btn_sub";// 以ID为标记触发提交事件
	protected String btnreset = "btn_reset";// 以ID为标记触发提交事件
	protected String layout = "table";// 表单布局
	protected String usePlugin;// 外调插件
	protected boolean dialog = true;// 是否是弹出窗口模式
	protected String action;// 表单提交路径
	protected String method = "post";// 表单提交方式
	protected String enctype;// 表单提交类型
	protected String tabtitle;// 表单选项卡
	/**
	 * 校验方式,1,2,3,4是validform原生方式,5是后来扩展的 
	 * add by xiaqiang 2014.5.20
	 */
	protected String tiptype = "5";
	/** 
	 * 表单美化插件jqtransformSelector的作用范围,支持jquery所有的选择符
	 * add by xiaqiang 2014.5.20
	 */
	protected String jqtransformSelector = "select";
	protected String callback;// 本页面回调函数,固定第一个参数是ajax返回的数据data,可以传入自己的参数例如callback="abc(data,x,y)"
	protected String parentCallback; //父页面回调函数(2014.6.7后已停用)
	protected boolean ajaxPost = true; //validform的提交方式是否为ajax提交
	protected String gridId; //打开弹出窗口的父页面grid的Id,用于自动关闭刷新
	protected boolean afterSaveClose = true; //保存后是否关闭页面
	protected String gridType = "datagrid"; //打开弹出窗口的父页面grid的类型,有三种"datagrid"、"treegrid"、"tree"

	Map<String, Object> root = new HashMap<String, Object>();

	public int doStartTag() throws JspException {
		try {
			root.clear();
			root.put("formid", formid);
			root.put("refresh", refresh);
			root.put("beforeSubmit", beforeSubmit);
			root.put("btnsub", btnsub);
			root.put("btnreset", btnreset);
			root.put("layout", layout);
			root.put("usePlugin", usePlugin);
			root.put("dialog", dialog);
			root.put("action", action);
			root.put("tabtitle", tabtitle);
			root.put("tiptype", tiptype);
			root.put("jqtransformSelector", jqtransformSelector);
			root.put("callback", callback);
			root.put("parentCallback", parentCallback);
			root.put("ajaxPost", ajaxPost);
			root.put("afterSaveClose", afterSaveClose);
			root.put("gridId", gridId);
			root.put("gridType", gridType);
			root.put("method", method);
			root.put("enctype", enctype);

			JspWriter out = this.pageContext.getOut();
			ServletRequest request = this.pageContext.getRequest();
			root.put("request", request);
			FreemarkerHelper viewEngine = new FreemarkerHelper();
			String start_str = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/formvalid_start.ftl",
					root);
			out.print(start_str);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		try {
			JspWriter out = this.pageContext.getOut();
			FreemarkerHelper viewEngine = new FreemarkerHelper();
			String end_str = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/formvalid_end.ftl", root);
			out.print(end_str);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getEnctype() {
		return enctype;
	}

	public void setEnctype(String enctype) {
		this.enctype = enctype;
	}

	public void setTabtitle(String tabtitle) {
		this.tabtitle = tabtitle;
	}

	public void setDialog(boolean dialog) {
		this.dialog = dialog;
	}

	public void setBtnsub(String btnsub) {
		this.btnsub = btnsub;
	}

	public void setRefresh(Boolean refresh) {
		this.refresh = refresh;
	}

	public void setBtnreset(String btnreset) {
		this.btnreset = btnreset;
	}

	public void setFormid(String formid) {
		this.formid = formid;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setUsePlugin(String usePlugin) {
		this.usePlugin = usePlugin;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public void setBeforeSubmit(String beforeSubmit) {
		this.beforeSubmit = beforeSubmit;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getTiptype() {
		return tiptype;
	}

	public void setTiptype(String tiptype) {
		this.tiptype = tiptype;
	}

	public String getJqtransformSelector() {
		return jqtransformSelector;
	}

	public void setJqtransformSelector(String jqtransformSelector) {
		this.jqtransformSelector = jqtransformSelector;
	}

	public String getParentCallback() {
		return parentCallback;
	}

	public void setParentCallback(String parentCallback) {
		this.parentCallback = parentCallback;
	}

	public String getFormid() {
		return formid;
	}

	public Boolean getRefresh() {
		return refresh;
	}

	public String getBeforeSubmit() {
		return beforeSubmit;
	}

	public String getBtnsub() {
		return btnsub;
	}

	public String getBtnreset() {
		return btnreset;
	}

	public String getLayout() {
		return layout;
	}

	public String getUsePlugin() {
		return usePlugin;
	}

	public boolean isDialog() {
		return dialog;
	}

	public String getAction() {
		return action;
	}

	public String getTabtitle() {
		return tabtitle;
	}

	public String getCallback() {
		return callback;
	}

	public boolean isAjaxPost() {
		return ajaxPost;
	}

	public void setAjaxPost(boolean ajaxPost) {
		this.ajaxPost = ajaxPost;
	}

	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	public boolean isAfterSaveClose() {
		return afterSaveClose;
	}

	public void setAfterSaveClose(boolean afterSaveClose) {
		this.afterSaveClose = afterSaveClose;
	}

	public String getGridType() {
		return gridType;
	}

	public void setGridType(String gridType) {
		this.gridType = gridType;
	}

}
