package com.xplatform.base.framework.tag.core.easyui;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import jodd.util.StringUtil;

import com.xplatform.base.framework.core.cache.UcgCache;
import com.xplatform.base.framework.core.cache.manager.UcgCacheManager;
import com.xplatform.base.framework.core.cache.manager.impl.UcgCacheManagerImpl;
import com.xplatform.base.framework.core.common.model.json.ComboBox;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.CacheUtil;
import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.tag.vo.easyui.ComboModel;
import com.xplatform.base.system.dict.service.DictValueService;

/**
 * 
 * description :日期选择框标签
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年5月28日 下午7:39:18
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                         修改内容
 * --------------- -------------------   -----------------------------------
 * xiaqiang        2014年5月28日 下午7:39:18
 *
 */
public class DateTimeBoxTag extends ComboModel {
	private static final long serialVersionUID = 1L;
	private boolean showSeconds = true; //时分秒是否显示到秒
	private String format;//日期时间格式(标准格式之外希望使用如"yyyy-MM-dd HH:mm"等格式的时候使用)
	private String type = "date";//共有date和datetime两种值,后者会显示时分秒

	private String onSelect;//在用户选择了一个日期的时候触发

	Map<String, Object> root = new HashMap<String, Object>();

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		root.clear();
		root.put("id", StringUtil.isEmpty(id) ? name : id);
		root.put("name", name);
		root.put("value", value);
		root.put("width", width);
		root.put("height", height);
		root.put("panelWidth", panelWidth);
		root.put("panelHeight", panelHeight);
		root.put("editable", editable);
		root.put("disabled", disabled);
		root.put("showSeconds", showSeconds);
		root.put("type", type);
		root.put("fit", fit);
		root.put("format", format);
		root.put("errormsg", errormsg);
		root.put("nullmsg", nullmsg);
		root.put("sucmsg", sucmsg);
		root.put("ajaxurl", ajaxurl);
		root.put("datatype", datatype);
		root.put("uniquemsg", uniquemsg);
		root.put("entityName", entityName);
		root.put("oldValue", oldValue);
		root.put("ignore", ignore);
		root.put("onChange", onChange);
		root.put("onShowPanel", onShowPanel);
		root.put("onHidePanel", onHidePanel);
		root.put("onSelect", onSelect);

		JspWriter out = this.pageContext.getOut();

		FreemarkerHelper viewEngine = new FreemarkerHelper();
		String html = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/datetimebox.ftl", root);
		try {
			out.print(html);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public boolean isShowSeconds() {
		return showSeconds;
	}

	public void setShowSeconds(boolean showSeconds) {
		this.showSeconds = showSeconds;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOnSelect() {
		return onSelect;
	}

	public void setOnSelect(String onSelect) {
		this.onSelect = onSelect;
	}

}
