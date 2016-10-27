package com.xplatform.base.framework.core.common.model.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description : EasyUI所使用的树节点类
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年5月12日 下午9:05:32
 *
 */

public class TreeNode implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String id; // 节点ID
	private String text; // 节点名
	private Map<String, Object> attributes = new HashMap<String, Object>();// 其他参数
	private String state = "open";// 是否展开(open,closed)
	private String iconCls; // 按钮图标
	private Boolean checked; // 是否被选中
	private String level; // 层级
	private List<TreeNode> children;// 子节点

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}
