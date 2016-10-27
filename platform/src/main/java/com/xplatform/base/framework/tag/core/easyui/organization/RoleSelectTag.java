package com.xplatform.base.framework.tag.core.easyui.organization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.json.JSONArray;

import com.xplatform.base.framework.core.common.model.json.TreeNode;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.MybatisTreeMapper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.TreeMapper;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.framework.tag.vo.easyui.ComboModel;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.platform.common.utils.ClientUtil;

/**
 * description : 角色选择标签
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

public class RoleSelectTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private String displayId; //显示的input框的id(不写的话,则默认为和name一样)
	private String displayName;//显示的input框的name
	private String hiddenId; //隐藏的input框的id(不写的话,则默认为和name一样)
	private String hiddenName;//隐藏的input框的name
	private String displayValue = ""; //显示的input框的value
	private String hiddenValue = "";//隐藏的input框的value
	private Boolean multiples = true;//是否多选
	private Boolean customInput = false;//是否自定义选择框(标签只产生选择按钮)
	private String extendDisplay;//扩展在显示input框上的属性,意味着它也可以加事件,style等(如:<input name="aa" [myAttribute="5" onmouseover=...])
	private String extendHidden;//扩展在隐藏input框上的属性,意味着它也可以加事件,style等(如:<input name="aa" [myAttribute="5" onmouseover=...])
	private String extendButton;//扩展在按钮上的属性,意味着它也可以加事件,style等(如:<input name="aa" [myAttribute="5" onmouseover=...])
	private String icon = "fa fa-suitcase";//选择按钮图标
	private String text;//按钮文字
	private String width = "790";//选择框宽度
	private String height = "480";//选择框高度

	private boolean needBtnSelected = true;//是否出现完成选择按钮
	private boolean needBtnSave = false;//是否出现保存按钮
	private boolean afterSaveClose = true;//保存后是否关闭页面
	private String saveUrl;//直接保存的url

	protected String datatype;//验证类型
	protected String sucmsg;//验证成功提示
	protected String nullmsg;//为空提示
	protected String errormsg;//验证错误提示
	protected String ajaxurl;//远程验证url
	protected String uniquemsg;//远程验证出现错误时的提示
	protected String entityName;//远程验证的实体名(带包路径,如com.xplatform.base.system.type.entity.TypeEntity)
	protected String oldValue;//远程验证-处理更新时旧值保留的问题
	protected String ignore;//验证框架中,设置ignore="ignore"则不输入不进行验证

	private Map<String, Object> root = new HashMap<String, Object>();

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		root.clear();

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
		root.put("multiples", multiples);
		root.put("customInput", customInput);
		root.put("errormsg", errormsg);
		root.put("nullmsg", nullmsg);
		root.put("sucmsg", sucmsg);
		root.put("ajaxurl", ajaxurl);
		root.put("datatype", datatype);
		root.put("uniquemsg", uniquemsg);
		root.put("entityName", entityName);
		root.put("oldValue", oldValue);
		root.put("ignore", ignore);
		root.put("extendDisplay", extendDisplay);
		root.put("extendHidden", extendHidden);
		root.put("extendButton", extendButton);
		root.put("needBtnSelected", needBtnSelected);
		root.put("needBtnSave", needBtnSave);
		root.put("afterSaveClose", afterSaveClose);
		root.put("saveUrl", saveUrl);
		root.put("icon", icon);
		root.put("text", text);
		root.put("width", width);
		root.put("height", height);
		JspWriter out = this.pageContext.getOut();

		FreemarkerHelper viewEngine = new FreemarkerHelper();
		String html = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/roleSelect.ftl", root);
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

	public String getExtendDisplay() {
		return extendDisplay;
	}

	public void setExtendDisplay(String extendDisplay) {
		this.extendDisplay = extendDisplay;
	}

	public String getExtendHidden() {
		return extendHidden;
	}

	public void setExtendHidden(String extendHidden) {
		this.extendHidden = extendHidden;
	}

	public String getExtendButton() {
		return extendButton;
	}

	public void setExtendButton(String extendButton) {
		this.extendButton = extendButton;
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

	public boolean isNeedBtnSelected() {
		return needBtnSelected;
	}

	public void setNeedBtnSelected(boolean needBtnSelected) {
		this.needBtnSelected = needBtnSelected;
	}

	public boolean isNeedBtnSave() {
		return needBtnSave;
	}

	public void setNeedBtnSave(boolean needBtnSave) {
		this.needBtnSave = needBtnSave;
	}

	public boolean isAfterSaveClose() {
		return afterSaveClose;
	}

	public void setAfterSaveClose(boolean afterSaveClose) {
		this.afterSaveClose = afterSaveClose;
	}

	public String getSaveUrl() {
		return saveUrl;
	}

	public void setSaveUrl(String saveUrl) {
		this.saveUrl = saveUrl;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

}
