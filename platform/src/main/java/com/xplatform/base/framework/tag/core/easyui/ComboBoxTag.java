package com.xplatform.base.framework.tag.core.easyui;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;

import jodd.util.StringUtil;

import com.xplatform.base.framework.core.common.model.json.ComboBox;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.tag.vo.easyui.ComboModel;
import com.xplatform.base.system.dict.service.DictTypeService;
import com.xplatform.base.system.dict.service.DictValueService;

/**
 * 
 * description :下拉选择框标签
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月19日 下午7:39:18
 * 
 * 修改历史:
 * 修改人                                              修改时间                                                    修改内容
 * ---------------  -------------------  -----------------------------------
 * xiehs        	2014年5月19日 下午7:39:18
 * xiaqiang			2014年6月4日 下午7:39:18
 *
 */
public class ComboBoxTag extends ComboModel {
	private static final long serialVersionUID = 1L;
	private String textField = "text";// 显示文本
	private String valueField = "id";// 显示文本值
	private String dictCode;
	private String groupName;//所在组名(如果需要级联过滤的话)
	private Integer index;//所在组中顺序(从1开始)

	private String onBeforeLoad;//在请求加载数据之前触发，返回false取消该加载动作
	private String onLoadSuccess;//在加载远程数据成功的时候触发
	private String onLoadError;//在加载远程数据失败的时候触发
	private String onSelect;//在用户选择列表项的时候触发
	private String onUnselect;//在用户取消选择列表项的时候触发

	public int doEndTag() throws JspTagException {
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("id", StringUtil.isEmpty(id) ? name : id);
		root.put("textField", textField);
		root.put("valueField", valueField);
		root.put("name", name);
		root.put("textname", textname);
		root.put("value", value);
		root.put("width", width);
		root.put("height", height);
		root.put("panelWidth", panelWidth);
		root.put("panelHeight", panelHeight);
		root.put("editable", editable);
		root.put("multiple", multiple);
		root.put("groupName", groupName);
		root.put("index", index);
		root.put("fit", fit);
		root.put("disabled", disabled);
		root.put("errormsg", errormsg);
		root.put("nullmsg", nullmsg);
		root.put("sucmsg", sucmsg);
		root.put("ajaxurl", ajaxurl);
		root.put("datatype", datatype);
		root.put("onBeforeLoad", onBeforeLoad);
		root.put("onLoadSuccess", onLoadSuccess);
		root.put("onLoadError", onLoadError);
		root.put("onSelect", onSelect);
		root.put("onUnselect", onUnselect);
		root.put("onShowPanel", onShowPanel);
		root.put("onHidePanel", onHidePanel);
		root.put("onChange", onChange);
		if (StringUtil.isNotEmpty(url)) {
			root.put("url", url);
		} else if (StringUtil.isNotEmpty(data)) {
			root.put("data", data);
		} else if (StringUtil.isNotEmpty(dictCode)) {
			DictTypeService dictTypeService = ApplicationContextUtil.getBean("dictTypeService");
			List<ComboBox> valueList = dictTypeService.findCacheByCode(dictCode);
			String data = JSONHelper.toJSONString(valueList);
			root.put("data", data);
		}
		try {
			JspWriter out = this.pageContext.getOut();
			out.print(end(root));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public String end(Map<String, Object> root) {
		FreemarkerHelper viewEngine = new FreemarkerHelper();
		String html = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/combobox.ftl", root);
		return html;
	}

	public String getTextField() {
		return textField;
	}

	public void setTextField(String textField) {
		this.textField = textField;
	}

	public String getValueField() {
		return valueField;
	}

	public void setValueField(String valueField) {
		this.valueField = valueField;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getOnBeforeLoad() {
		return onBeforeLoad;
	}

	public void setOnBeforeLoad(String onBeforeLoad) {
		this.onBeforeLoad = onBeforeLoad;
	}

	public String getOnLoadSuccess() {
		return onLoadSuccess;
	}

	public void setOnLoadSuccess(String onLoadSuccess) {
		this.onLoadSuccess = onLoadSuccess;
	}

	public String getOnLoadError() {
		return onLoadError;
	}

	public void setOnLoadError(String onLoadError) {
		this.onLoadError = onLoadError;
	}

	public String getOnSelect() {
		return onSelect;
	}

	public void setOnSelect(String onSelect) {
		this.onSelect = onSelect;
	}

	public String getOnUnselect() {
		return onUnselect;
	}

	public void setOnUnselect(String onUnselect) {
		this.onUnselect = onUnselect;
	}

}
