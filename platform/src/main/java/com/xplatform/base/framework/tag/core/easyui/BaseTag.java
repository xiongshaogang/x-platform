package com.xplatform.base.framework.tag.core.easyui;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.xplatform.base.framework.core.util.StringUtil;

import com.xplatform.base.framework.core.util.CookieUtil;
import com.xplatform.base.framework.core.util.oConvertUtils;

/**
 * 
 * @author 张代浩
 *
 */
public class BaseTag extends TagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String type = "default";// 加载类型

	public void setType(String type) {
		this.type = type;
	}

	public int doStartTag() throws JspException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		try {
			JspWriter out = this.pageContext.getOut();
			HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
			StringBuffer sb = new StringBuffer();

			String types[] = type.split(",");
			if (oConvertUtils.isIn("base_css", types)) {
				/** Bootstrap基本样式 **/
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/bootstrap/css/bootstrap.min.css\" />");
				/** font-awesome系统图标库 **/
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/assets/css/font-awesome.min.css\" />");
				/** ace基本样式 **/
				// sb.append("<link rel=\"stylesheet\" href=\"plug-in/assets/css/ace.min.css\" />");
				// sb.append("<link rel=\"stylesheet\" href=\"plug-in/assets/css/ace-rtl.min.css\" />");
				// sb.append("<link rel=\"stylesheet\" href=\"plug-in/assets/css/ace-skins.min.css\" />");
				/** 系统自定义按钮样式 **/
				// sb.append("<link rel=\"stylesheet\" href=\"plug-in/assets/css/bootstrap-btn.css\" />");
				// sb.append("<link rel=\"stylesheet\" href=\"plug-in/assets/css/ace-btn.css\" />");
				/** Easy UI **/
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/easyui-1.4.2/themes/icon.css\" />");
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/easyui-1.4.2/themes/gray/easyui.css\" />");
				/** yitip **/
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/tools/css/common.css\" />");
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/yitip/css/jquery.yitip.css\" />");
				/** Validform **/
				// sb.append("<link rel=\"stylesheet\" href=\"plug-in/Validform/css/divfrom.css\" />");
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/Validform/css/style.css\" />");
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/Validform/css/tablefrom.css\" />");
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/Validform/plugin/jqtransform/jqtransform.css\" />");
				/** 系统全局样式 **/
//				sb.append("<link rel=\"stylesheet\" href=\"plug-in/bootstrap/css/non-responsive.css\" />");
				
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/tools/css/easyui_rewrite.css\" />");
			}
			if (oConvertUtils.isIn("base_js", types)) {
				/** jquery基本库 **/
				sb.append("<script type=\"text/javascript\" src=\"plug-in/jquery/jquery-1.11.2.min.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/jquery/jquery-migrate-1.2.1.min.js\"></script>");
				/** Bootstrap **/
				sb.append("<script type=\"text/javascript\" src=\"plug-in/bootstrap/js/bootstrap.min.js\"></script>");
				/** ace基础脚本 **/
//				sb.append("<script type=\"text/javascript\" src=\"plug-in/assets/js/ace-elements.min.js\"></script>");
//				sb.append("<script type=\"text/javascript\" src=\"plug-in/assets/js/ace.min.js\"></script>");
				/** Tools工具脚本 **/
				sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/syUtil.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/uuid.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/yitip/js/jquery.yitip.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/Map.js\"></script>");
				/** 表单验证相关JS **/
				sb.append("<script type=\"text/javascript\" src=\"plug-in/Validform/js/form.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/Validform/js/Validform_v5.3.2_min.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/Validform/js/Validform_Datatype.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/Validform/js/datatype.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/Validform/plugin/jqtransform/jquery.jqtransform.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/Validform/plugin/passwordStrength/passwordStrength-min.js\"></script>");
				/** Easy UI **/
				sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui-1.4.2/jquery.easyui.all.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui-1.4.2/extends/easyuiextend.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui-1.4.2/locale/easyui-lang-zh_CN.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui-1.4.2/extends/datagrid-scrollview.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui-1.4.2/extends/datagrid-groupview.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui-1.4.2/extends/editTableExtends.js\"></script>");
				/** 平台功能脚本 **/
				sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/curdtools.js\"></script>");
			}
			if (oConvertUtils.isIn("codemirror", types)) {
				sb.append("<script type=\"text/javascript\" src=\"plug-in/javacode/codemirror.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/javacode/InitMirror.js\"></script>");
			}
			if (oConvertUtils.isIn("ckeditor", types)) {
				sb.append("<script type=\"text/javascript\" src=\"plug-in/ckeditor/ckeditor.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/ckeditorTool.js\"></script>");
			}
			if (oConvertUtils.isIn("ckfinder", types)) {
				sb.append("<script type=\"text/javascript\" src=\"plug-in/ckfinder/ckfinder.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/ckfinderTool.js\"></script>");
			}
			if (oConvertUtils.isIn("easyui", types)) {
				/*
				 * if(StringUtil.isNotEmpty(CookieUtil.getValueByName(request,
				 * "easyuiThemeName"))){ sb.append(
				 * "<link id=\"easyuiTheme\" rel=\"stylesheet\" href=\"plug-in/easyui/themes/"
				 * +CookieUtil.getValueByName(request,
				 * "easyuiThemeName")+"/easyui.css\" type=\"text/css\"></link>"
				 * ); }else{ sb.append(
				 * "<link id=\"easyuiTheme\" rel=\"stylesheet\" href=\"plug-in/easyui/themes/gray/easyui.css\" type=\"text/css\"></link>"
				 * ); }
				 */
				// sb.append("<link id=\"easyuiTheme\" rel=\"stylesheet\" href=\"plug-in/easyui/themes/<c:out value='${cookie.easyuiThemeName.value}' default='default'/>/easyui.css\" type=\"text/css\"></link>");
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/easyui/themes/gray/easyui.css\" type=\"text/css\"></link>");
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/easyui/themes/icon.css\" type=\"text/css\"></link>");
				sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"plug-in/accordion/css/accordion.css\">");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui/jquery.easyui.min.1.3.2.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui/locale/easyui-lang-zh_CN.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/syUtil.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui/extends/datagrid-scrollview.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/jquery.cookie.js\"></script>");

				// sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui/extends/changeEasyuiTheme.js\"></script>");
			}
			if (oConvertUtils.isIn("DatePicker", types)) {
				sb.append("<script type=\"text/javascript\" src=\"plug-in/My97DatePicker/WdatePicker.js\"></script>");
			}
			if (oConvertUtils.isIn("jqueryui", types)) {
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/jquery-ui/css/ui-lightness/jquery-ui-1.9.2.custom.min.css\" type=\"text/css\"></link>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/jquery-ui/js/jquery-ui-1.9.2.custom.min.js\"></script>");
			}
			if (oConvertUtils.isIn("jqueryui-sortable", types)) {
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/jquery-ui/css/ui-lightness/jquery-ui-1.9.2.custom.min.css\" type=\"text/css\"></link>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/jquery-ui/js/ui/jquery.ui.core.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/jquery-ui/js/ui/jquery.ui.widget.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/jquery-ui/js/ui/jquery.ui.mouse.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/jquery-ui/js/ui/jquery.ui.sortable.js\"></script>");
			}
			if (oConvertUtils.isIn("prohibit", types)) {
				sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/prohibitutil.js\"></script>");
			}
			if (oConvertUtils.isIn("designer", types)) {
				sb.append("<script type=\"text/javascript\" src=\"plug-in/designer/easyui/jquery-1.7.2.min.js\"></script>");
				sb.append("<link id=\"easyuiTheme\" rel=\"stylesheet\" href=\"plug-in/designer/easyui/easyui.css\" type=\"text/css\"></link>");
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/designer/easyui/icon.css\" type=\"text/css\"></link>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/designer/easyui/jquery.easyui.min.1.3.0.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/designer/easyui/locale/easyui-lang-zh_CN.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/syUtil.js\"></script>");

				sb.append("<script type=\'text/javascript\' src=\'plug-in/jquery/jquery-autocomplete/lib/jquery.bgiframe.min.js\'></script>");
				sb.append("<script type=\'text/javascript\' src=\'plug-in/jquery/jquery-autocomplete/lib/jquery.ajaxQueue.js\'></script>");
				sb.append("<script type=\'text/javascript\' src=\'plug-in/jquery/jquery-autocomplete/jquery.autocomplete.min.js\'></script>");
				sb.append("<link href=\"plug-in/designer/designer.css\" type=\"text/css\" rel=\"stylesheet\" />");
				sb.append("<script src=\"plug-in/designer/draw2d/wz_jsgraphics.js\"></script>");
				sb.append("<script src=\'plug-in/designer/draw2d/mootools.js\'></script>");
				sb.append("<script src=\'plug-in/designer/draw2d/moocanvas.js\'></script>");
				sb.append("<script src=\'plug-in/designer/draw2d/draw2d.js\'></script>");
				sb.append("<script src=\"plug-in/designer/MyCanvas.js\"></script>");
				sb.append("<script src=\"plug-in/designer/ResizeImage.js\"></script>");
				sb.append("<script src=\"plug-in/designer/event/Start.js\"></script>");
				sb.append("<script src=\"plug-in/designer/event/End.js\"></script>");
				sb.append("<script src=\"plug-in/designer/connection/MyInputPort.js\"></script>");
				sb.append("<script src=\"plug-in/designer/connection/MyOutputPort.js\"></script>");
				sb.append("<script src=\"plug-in/designer/connection/DecoratedConnection.js\"></script>");
				sb.append("<script src=\"plug-in/designer/task/Task.js\"></script>");
				sb.append("<script src=\"plug-in/designer/task/UserTask.js\"></script>");
				sb.append("<script src=\"plug-in/designer/task/ManualTask.js\"></script>");
				sb.append("<script src=\"plug-in/designer/task/ServiceTask.js\"></script>");
				sb.append("<script src=\"plug-in/designer/gateway/ExclusiveGateway.js\"></script>");
				sb.append("<script src=\"plug-in/designer/gateway/ParallelGateway.js\"></script>");
				sb.append("<script src=\"plug-in/designer/boundaryevent/TimerBoundary.js\"></script>");
				sb.append("<script src=\"plug-in/designer/boundaryevent/ErrorBoundary.js\"></script>");
				sb.append("<script src=\"plug-in/designer/subprocess/CallActivity.js\"></script>");
				sb.append("<script src=\"plug-in/designer/task/ScriptTask.js\"></script>");
				sb.append("<script src=\"plug-in/designer/task/MailTask.js\"></script>");
				sb.append("<script src=\"plug-in/designer/task/ReceiveTask.js\"></script>");
				sb.append("<script src=\"plug-in/designer/task/BusinessRuleTask.js\"></script>");
				sb.append("<script src=\"plug-in/designer/designer.js\"></script>");
				sb.append("<script src=\"plug-in/designer/mydesigner.js\"></script>");

			}
			if (oConvertUtils.isIn("tools", types)) {
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/tools/css/common.css\" type=\"text/css\"></link>");
				// 2014.5.7 取消使用 lhgdialog 为了easyui能统一换肤 统一使用easyui.dialog
				// 配合改动的还有curdtools.js
				// sb.append("<script type=\"text/javascript\" src=\"plug-in/lhgDialog/lhgdialog.min.js\"></script>");
				// 2014.5.7 引入用于产生js的无重复uuid的js
				sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/uuid.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/curdtools.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui/extends/easyuiextend.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/yitip/js/jquery.yitip.js\"></script>");
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/yitip/css/jquery.yitip.css\" type=\"text/css\"></link>");
				sb.append("<script src=\"plug-in/tools/main.js\"></script>");
			}
			if (oConvertUtils.isIn("toptip", types)) {
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/toptip/css/css.css\" type=\"text/css\"></link>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/toptip/manhua_msgTips.js\"></script>");
			}
			if (oConvertUtils.isIn("autocomplete", types)) {
				sb.append("<link rel=\"stylesheet\" href=\"plug-in/jquery/jquery-autocomplete/jquery.autocomplete.css\" type=\"text/css\"></link>");
				sb.append("<script type=\"text/javascript\" src=\"plug-in/jquery/jquery-autocomplete/jquery.autocomplete.min.js\"></script>");
			}
			out.print(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

}
