package com.xplatform.base.framework.tag.core.easyui;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

import jodd.util.StringUtil;

import com.xplatform.base.framework.core.common.model.json.ComboBox;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.tag.vo.easyui.ComboModel;
import com.xplatform.base.system.dict.service.DictTypeService;
import com.xplatform.base.system.dict.service.DictValueService;

/**
 * description :下拉树选择封装标签
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年6月5日 下午4:43:52
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年6月5日 下午4:43:52
 *
*/

public class ComboTreeTag extends ComboModel {
	private static final long serialVersionUID = 1L;

	/** 来源于TreeModel的树的通用属性,但java不能多继承,或者很方便的与自定义标签结合起来,所以这里直接复制属性过来 **/
	private boolean animate = true; //展开是否有动画效果
	private boolean cascadeCheck = false; //是否级联选择
	private boolean onlyLeafCheck = false;//是否只能子节点被复选
	private boolean lines = false; //是否显示树形虚线
	private String dictCode;//数据字典code
	private boolean aysn = false;//combotree数据是否异步加载(true为异步加载,false为一次性加载)
	private boolean autoExpand = true; //在异步获取情况下,是否展开所有节点(通过onLoadSuccess实现)

	private String onClick;//在点击树节点时触发
	private String onCheck;//在用户点击勾选复选框的时候触发
	private String onSelect;//在用户选择节点的时候触发
	private String onLoadSuccess;//在数据加载成功以后触发

	public int doEndTag() throws JspTagException {
		Map<String, Object> root = new HashMap<String, Object>();
		try {
			JspWriter out = this.pageContext.getOut();
			root.put("id", StringUtil.isEmpty(id) ? name : id);
			root.put("name", name);
			root.put("textname", textname);
			root.put("animate", animate);
			root.put("cascadeCheck", cascadeCheck);
			root.put("onlyLeafCheck", onlyLeafCheck);
			root.put("lines", lines);
			root.put("width", width);
			root.put("height", height);
			root.put("panelWidth", panelWidth);
			root.put("panelHeight", panelHeight);
			root.put("editable", editable);
			root.put("multiple", multiple);
			root.put("value", value);
			root.put("fit", fit);
			root.put("autoExpand", autoExpand);
			root.put("disabled", disabled);
			root.put("errormsg", errormsg);
			root.put("nullmsg", nullmsg);
			root.put("sucmsg", sucmsg);
			root.put("ajaxurl", ajaxurl);
			root.put("datatype", datatype);
			root.put("uniquemsg", uniquemsg);
			root.put("entityName", entityName);
			root.put("oldValue", oldValue);
			root.put("ignore", ignore);

			root.put("onClick", onClick);
			root.put("onCheck", onCheck);
			root.put("onChange", onChange);
			root.put("onSelect", onSelect);
			root.put("onLoadSuccess", onLoadSuccess);
			root.put("onShowPanel", onShowPanel);
			root.put("onHidePanel", onHidePanel);

			if (aysn) {
				if (StringUtil.isNotEmpty(dictCode)) {
					root.put("url", "commonController.do?getDictValueTree&dictCode=" + dictCode);
				} else {
					root.put("url", url);
				}
			} else {
				if (StringUtil.isNotEmpty(url)) {
					root.put("url", url);
				} else if (StringUtil.isNotEmpty(data)) {
					root.put("data", data);
				} else if (StringUtil.isNotEmpty(dictCode)) {
					DictTypeService dictTypeService = ApplicationContextUtil.getBean("dictTypeService");
					String treeJson = dictTypeService.findCacheByCode(dictCode);
					root.put("data", treeJson);
				}
			}
			FreemarkerHelper viewEngine = new FreemarkerHelper();
			String html = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/combotree.ftl", root);
			out.print(html);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public boolean isAnimate() {
		return animate;
	}

	public void setAnimate(boolean animate) {
		this.animate = animate;
	}

	public boolean isCascadeCheck() {
		return cascadeCheck;
	}

	public void setCascadeCheck(boolean cascadeCheck) {
		this.cascadeCheck = cascadeCheck;
	}

	public boolean isOnlyLeafCheck() {
		return onlyLeafCheck;
	}

	public void setOnlyLeafCheck(boolean onlyLeafCheck) {
		this.onlyLeafCheck = onlyLeafCheck;
	}

	public boolean isLines() {
		return lines;
	}

	public void setLines(boolean lines) {
		this.lines = lines;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public boolean isAysn() {
		return aysn;
	}

	public void setAysn(boolean aysn) {
		this.aysn = aysn;
	}

	public String getOnClick() {
		return onClick;
	}

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	public String getOnCheck() {
		return onCheck;
	}

	public void setOnCheck(String onCheck) {
		this.onCheck = onCheck;
	}

	public String getOnSelect() {
		return onSelect;
	}

	public void setOnSelect(String onSelect) {
		this.onSelect = onSelect;
	}

	public String getOnLoadSuccess() {
		return onLoadSuccess;
	}

	public void setOnLoadSuccess(String onLoadSuccess) {
		this.onLoadSuccess = onLoadSuccess;
	}

	public boolean isAutoExpand() {
		return autoExpand;
	}

	public void setAutoExpand(boolean autoExpand) {
		this.autoExpand = autoExpand;
	}

}
