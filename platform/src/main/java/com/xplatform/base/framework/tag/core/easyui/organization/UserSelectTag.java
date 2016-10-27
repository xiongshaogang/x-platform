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
 * description : 部门员工选择标签
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月16日 下午2:46:36
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年7月16日 下午2:46:361
 *
*/

public class UserSelectTag extends TagSupport {
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
	private String icon = "fa fa-user";//选择按钮图标
	private String text;//按钮文字
	private String width="960";//选择框宽度
	private String height="560";//选择框高度
	/* 部门左树相关配置 */
	private String treeUrl = "orgnaizationController.do?orgSelectTagTree"; //部门树加载数据url
	private boolean aysn = true; //部门树是异步获取还是同步一次性全部获取数据(true为异步获取)
	private boolean onlyAuthority = false; //是否只显示有权管理的机构部门(默认不进行权限过滤)
	private String orgCode = "rootOrg"; //用于过滤显示特定节点下的部门(默认code为"rootOrg",不存在,仅为判断)
	private boolean containSelf = true; //是否包含传入的orgCode节点自身
	private boolean expandAll = false; //是否展开所有节点(展示完整的机构部门树)
	private String showOrgTypes;//需要显示的机构树类型,比如全部显示就是"org,dept,job"

	private boolean needBtnSelected = true;//是否出现完成选择按钮
	private boolean needBtnSave = false;//是否出现保存按钮
	private boolean afterSaveClose = true;//保存后是否关闭页面
	private String saveUrl;//直接保存的url
	private String empOrUser = "user";//员工列表选择出的是用户id还是员工id(可设值分别为'user'和'emp',默认'user')
	private String callback; //保存或完成选择后的js回调函数
	/* 员工grid相关配置 */
	private String gridUrl = "userController.do?datagrid4OrgMulSelectTag"; //grid加载数据url
	/* 验证相关配置 */
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
		root.put("extendDisplay", extendDisplay);
		root.put("extendHidden", extendHidden);
		root.put("extendButton", extendButton);
		root.put("icon", icon);
		root.put("text", text);
		root.put("width", width);
		root.put("height", height);

		root.put("errormsg", errormsg);
		root.put("nullmsg", nullmsg);
		root.put("sucmsg", sucmsg);
		root.put("ajaxurl", ajaxurl);
		root.put("datatype", datatype);
		root.put("uniquemsg", uniquemsg);
		root.put("entityName", entityName);
		root.put("oldValue", oldValue);
		root.put("ignore", ignore);

		root.put("treeUrl", treeUrl);
		root.put("gridUrl", gridUrl);
		root.put("aysn", aysn);
		root.put("onlyAuthority", onlyAuthority);
		root.put("orgCode", orgCode);
		root.put("containSelf", containSelf);
		root.put("expandAll", expandAll);

		root.put("needBtnSelected", needBtnSelected);
		root.put("needBtnSave", needBtnSave);
		root.put("afterSaveClose", afterSaveClose);
		root.put("saveUrl", saveUrl);
		root.put("empOrUser", empOrUser);
		root.put("callback", callback);
		root.put("showOrgTypes", showOrgTypes);

		JspWriter out = this.pageContext.getOut();

		FreemarkerHelper viewEngine = new FreemarkerHelper();
		String html = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/userSelect.ftl", root);
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

	public boolean isAysn() {
		return aysn;
	}

	public void setAysn(boolean aysn) {
		this.aysn = aysn;
	}

	public boolean isOnlyAuthority() {
		return onlyAuthority;
	}

	public void setOnlyAuthority(boolean onlyAuthority) {
		this.onlyAuthority = onlyAuthority;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public boolean isContainSelf() {
		return containSelf;
	}

	public void setContainSelf(boolean containSelf) {
		this.containSelf = containSelf;
	}

	public String getTreeUrl() {
		return treeUrl;
	}

	public void setTreeUrl(String treeUrl) {
		this.treeUrl = treeUrl;
	}

	public boolean isExpandAll() {
		return expandAll;
	}

	public void setExpandAll(boolean expandAll) {
		this.expandAll = expandAll;
	}

	public Boolean getCustomInput() {
		return customInput;
	}

	public void setCustomInput(Boolean customInput) {
		this.customInput = customInput;
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

	public String getGridUrl() {
		return gridUrl;
	}

	public void setGridUrl(String gridUrl) {
		this.gridUrl = gridUrl;
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

	public String getEmpOrUser() {
		return empOrUser;
	}

	public void setEmpOrUser(String empOrUser) {
		this.empOrUser = empOrUser;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
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

	public String getShowOrgTypes() {
		return showOrgTypes;
	}

	public void setShowOrgTypes(String showOrgTypes) {
		this.showOrgTypes = showOrgTypes;
	}

}
