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
import com.xplatform.base.system.dict.service.DictValueService;

/**
 * 
 * description :表单中数字、金额输入封装标签 
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年5月30日 下午1:39:18
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                         修改内容
 * --------------- -------------------   -----------------------------------
 *
 */
public class NumberTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private String id;// 控件ID
	private String name;//控件名称
	private String min;// 最小值
	private String max;//最大值
	private Integer precision;//小数点位数
	private String groupSeparator = ",";//千分位使用的分隔符
	private String decimalSeparator = ".";//小数点与整数位使用的分隔符
	private String prefix;//输入数字时的前缀,比如人民币符号'￥'
	private String suffix;//输入数字时的后缀,一般用于金额显示单位('元')
	private boolean disabled = false; //是否禁用
	private boolean editable = true; //是否可编辑
	private Integer width = 198;//宽度(有微调器spinner=true时起效,否则直接根据元素style/class属性定义高宽)
	private Integer height = 28;//高度(有微调器spinner=true时起效,否则直接根据元素style/class属性定义高宽)
	private boolean spinner = false;//是否出现微调器
	private String value;//控件值

	protected String datatype;//验证类型
	protected String sucmsg;//验证成功提示
	protected String nullmsg;//为空提示
	protected String errormsg;//验证错误提示
	protected String ajaxurl;//远程验证url
	protected String uniquemsg;//远程验证出现错误时的提示
	protected String entityName;//远程验证的实体名(带包路径,如com.xplatform.base.system.type.entity.TypeEntity)
	protected String oldValue;//远程验证-处理更新时旧值保留的问题
	protected String ignore;//验证框架中,设置ignore="ignore"则不输入不进行验证

	public String onChange;//字段值更改事件
	public String onfocus;//控件获取焦点事件
	public String onblur;//控件移除焦点事件
	public String onclick;//点击事件
	public String extend;//扩展在input框上的属性,意味着它也可以加事件(如:<input name="aa" [myAttribute="5" onmouseover=...])

	Map<String, Object> root = new HashMap<String, Object>();

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		root.clear();
		root.put("id", StringUtil.isEmpty(id) ? name : id);
		root.put("name", name);
		root.put("width", width);
		root.put("height", height);
		root.put("min", min);
		root.put("max", max);
		root.put("precision", precision);
		root.put("groupSeparator", groupSeparator);
		root.put("decimalSeparator", decimalSeparator);
		root.put("prefix", prefix);
		root.put("suffix", suffix);
		root.put("disabled", disabled);
		root.put("editable", editable);
		root.put("spinner", spinner);
		root.put("value", value);

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
		root.put("onfocus", onfocus);
		root.put("onblur", onblur);
		root.put("onclick", onclick);
		root.put("extend", extend);
		JspWriter out = this.pageContext.getOut();

		FreemarkerHelper viewEngine = new FreemarkerHelper();
		String html = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/number.ftl", root);
		try {
			out.print(html);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public String getGroupSeparator() {
		return groupSeparator;
	}

	public void setGroupSeparator(String groupSeparator) {
		this.groupSeparator = groupSeparator;
	}

	public String getDecimalSeparator() {
		return decimalSeparator;
	}

	public void setDecimalSeparator(String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public boolean isSpinner() {
		return spinner;
	}

	public void setSpinner(boolean spinner) {
		this.spinner = spinner;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOnChange() {
		return onChange;
	}

	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}

	public String getOnfocus() {
		return onfocus;
	}

	public void setOnfocus(String onfocus) {
		this.onfocus = onfocus;
	}

	public String getOnblur() {
		return onblur;
	}

	public void setOnblur(String onblur) {
		this.onblur = onblur;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public String getUniquemsg() {
		return uniquemsg;
	}

	public void setUniquemsg(String uniquemsg) {
		this.uniquemsg = uniquemsg;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getIgnore() {
		return ignore;
	}

	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}

}
