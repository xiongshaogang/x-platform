package com.xplatform.base.framework.tag.core.easyui.organization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

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
 * description : 组织机构选择标签
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

public class OrgSelectTag extends ComboModel {
	private static final long serialVersionUID = 1L;
	private boolean aysn = true; //是异步获取还是同步一次性全部获取数据(true为异步获取)
	private boolean autoExpand = true; //在异步获取情况下,是否展开所有节点(通过onLoadSuccess实现)
	private boolean onlyAuthority = false; //是否只显示有权管理的机构部门(默认不进行权限过滤)
	private String orgCode = "rootOrg"; //用于过滤显示特定节点下的部门(默认code为"rootOrg",不存在,仅为判断)
	private boolean containSelf = true; //是否包含传入的orgCode节点自身

	private boolean checkbox = true; //展开是否有动画效果
	private boolean animate = true; //展开是否有动画效果
	private boolean cascadeCheck = false; //是否级联选择
	private boolean onlyLeafCheck = false;//是否只能子节点被复选
	private boolean lines = false; //是否显示树形虚线

	private boolean needText = false;//(新版本弃用) 是否需要提交所选项的text值
	private String textId;//(新版本弃用,使用textname实现)
	private String textName;//(新版本弃用,使用textname实现)
	private String textValue;//(新版本弃用,使用textname实现)

	private String onClick;//在点击树节点时触发
	private String onCheck;//在用户点击勾选复选框的时候触发
	private String onSelect;//在用户选择节点的时候触发
	private String onLoadSuccess;//在数据加载成功以后触发
	Map<String, Object> root = new HashMap<String, Object>();

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		url = "orgnaizationController.do?orgSelectTagTree";
		root.clear();

		//异步获取,去请求远程url
		if (aysn) {
			url += "&orgCode=" + orgCode;
			url += "&containSelf=" + containSelf;
			url += "&onlyAuthority=" + onlyAuthority;
		} else {
			/*UserEntity user = ClientUtil.getUser();
			DeptEntity dept = null;
			String deptId = "";
			DeptService deptService = ApplicationContextUtil.getBean("deptService");
			AuthorityService authorityService = ApplicationContextUtil.getBean("authorityService");

			Map<String, String> map = new HashMap<String, String>();
			map.put("parentId", deptId);
			map.put("userId", user.getId());

			List<TreeNode> treeList = new ArrayList<TreeNode>();
			Map<String, String> propertyMapping = new HashMap<String, String>();
			propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
			propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
			propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
			propertyMapping.put(TreeMapper.PropertyType.ICONCLS.getValue(), "iconCls");
			propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(), "code,name,type");

			if (authorityService.isAdmin(map)) {//不是管理员，进行权限过滤
				List<DeptEntity> list = deptService.queryListByPorperty("parent.id", id);
				treeList = TreeMapper.buildJsonTree(list, propertyMapping);
			} else {
				List<DeptTreeVo> list = deptService.queryDeptTree(user.getUserTypeId(), id);
				treeList = TreeMapper.buildJsonTree(list, propertyMapping);
			}
			JSONArray object = JSONHelper.toJSONArray(treeList);
			object.toString();*/
		}

		root.put("id", StringUtil.isEmpty(id) ? name : id);
		root.put("name", name);
		root.put("textname", textname);
		root.put("url", url);
		root.put("data", data);
		root.put("animate", animate);
		root.put("checkbox", checkbox);
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
		root.put("needText", needText);
		root.put("textId", StringUtil.isEmpty(textId) ? textName : textId);
		root.put("textName", textName);
		root.put("textValue", textValue);

		root.put("errormsg", errormsg);
		root.put("nullmsg", nullmsg);
		root.put("sucmsg", sucmsg);
		root.put("ajaxurl", ajaxurl);
		root.put("datatype", datatype);
		root.put("uniquemsg", uniquemsg);
		root.put("entityName", entityName);
		root.put("oldValue", oldValue);
		root.put("ignore", ignore);

		root.put("fit", fit);
		root.put("autoExpand", autoExpand);
		root.put("onClick", onClick);
		root.put("onCheck", onCheck);
		root.put("onChange", onChange);
		root.put("onShowPanel", onShowPanel);
		root.put("onHidePanel", onHidePanel);
		root.put("onSelect", onSelect);
		root.put("onLoadSuccess", onLoadSuccess);

		JspWriter out = this.pageContext.getOut();

		FreemarkerHelper viewEngine = new FreemarkerHelper();
		String html = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/orgSelect.ftl", root);
		try {
			out.print(html);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
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

	public boolean isAysn() {
		return aysn;
	}

	public void setAysn(boolean aysn) {
		this.aysn = aysn;
	}

	public boolean isContainSelf() {
		return containSelf;
	}

	public void setContainSelf(boolean containSelf) {
		this.containSelf = containSelf;
	}

	public boolean isAutoExpand() {
		return autoExpand;
	}

	public void setAutoExpand(boolean autoExpand) {
		this.autoExpand = autoExpand;
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

	public boolean isNeedText() {
		return needText;
	}

	public void setNeedText(boolean needText) {
		this.needText = needText;
	}

	public String getTextId() {
		return textId;
	}

	public void setTextId(String textId) {
		this.textId = textId;
	}

	public String getTextName() {
		return textName;
	}

	public void setTextName(String textName) {
		this.textName = textName;
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

}
