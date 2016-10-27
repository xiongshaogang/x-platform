package com.xplatform.base.framework.core.common.model.tree;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.Serializable;

import jodd.util.StringUtil;

/**
 * 
 * description :多叉树节点数据结构
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月10日 下午1:41:56
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年6月10日 下午1:41:56
 *
 */
public class MutiTreeNode implements Serializable {
	
	private String id;
	protected String text;
	private Map<String, Object> attributes = new HashMap<String, Object>();// 其他参数
	private String state = "open";// 是否展开(open,closed)
	private String iconCls; // 按钮图标
	private Boolean checked; // 是否被选中
	
	private String parentId;
	protected MutiTreeNode parent;
	protected List<MutiTreeNode> childList;

	public MutiTreeNode() {
		initChildList();
	}

	public MutiTreeNode(MutiTreeNode parentNode) {
		this.getParent();
		initChildList();
	}

	public boolean isLeaf() {
		if (childList == null) {
			return true;
		} else {
			if (childList.isEmpty()) {
				return true;
			} else {
				return false;
			}
		}
	}

	/** 插入一个child节点到当前节点中 */
	public void addChildNode(MutiTreeNode treeNode) {
		initChildList();
		childList.add(treeNode);
	}

	public void initChildList() {
		if (childList == null)
			childList = new ArrayList<MutiTreeNode>();
	}

	public boolean isValidTree() {
		return true;
	}

	/** 返回当前节点的父辈节点集合 */
	public List<MutiTreeNode> getElders() {
		List<MutiTreeNode> elderList = new ArrayList<MutiTreeNode>();
		MutiTreeNode parentNode = this.getParent();
		//id值为0代表根节点  ,本多叉树默认为 根节点是无效节点
		if (parentNode == null|| StringUtil.equals(parentNode.getId(), "-1")) {
			return elderList;
		} else {
			elderList.add(parentNode);
			elderList.addAll(parentNode.getElders());
			return elderList;
		}
	}

	/** 返回当前节点的晚辈集合 */
	public List<MutiTreeNode> getJuniors() {
		List<MutiTreeNode> juniorList = new ArrayList<MutiTreeNode>();
		List<MutiTreeNode> childList = this.getChildList();
		if (childList == null) {
			return juniorList;
		} else {
			int childNumber = childList.size();
			for (int i = 0; i < childNumber; i++) {
				MutiTreeNode junior = childList.get(i);
				juniorList.add(junior);
				juniorList.addAll(junior.getJuniors());
			}
			return juniorList;
		}
	}

	/** 返回当前节点的孩子集合 */
	public List<MutiTreeNode> getChildList() {
		return childList;
	}

	/** 删除节点和它下面的晚辈 */
	public void deleteNode() {
		MutiTreeNode parentNode = this.getParent();
		String id = this.getId();

		if (parentNode != null) {
			parentNode.deleteChildNode(id);
		}
	}

	/** 删除当前节点的某个子节点 */
	public void deleteChildNode(String childId) {
		List<MutiTreeNode> childList = this.getChildList();
		int childNumber = childList.size();
		for (int i = 0; i < childNumber; i++) {
			MutiTreeNode child = childList.get(i);
			if (StringUtil.equals(child.getId(), childId)) {
				childList.remove(i);
				return;
			}
		}
	}

	/** 动态的插入一个新的节点到当前树中 */
	public boolean insertJuniorNode(MutiTreeNode treeNode) {
		String juniorParentId = treeNode.getParentId();
		if (this.parentId == juniorParentId) {
			addChildNode(treeNode);
			return true;
		} else {
			List<MutiTreeNode> childList = this.getChildList();
			int childNumber = childList.size();
			boolean insertFlag;

			for (int i = 0; i < childNumber; i++) {
				MutiTreeNode childNode = childList.get(i);
				insertFlag = childNode.insertJuniorNode(treeNode);
				if (insertFlag == true)
					return true;
			}
			return false;
		}
	}

	/** 找到一颗树中某个节点 */
	public MutiTreeNode findTreeNodeById(String id) {
		if (StringUtil.equals(this.id, id))
			return this;
		if (childList.isEmpty() || childList == null) {
			return null;
		} else {
			for (int i = 0; i < childList.size(); i++) {
				try {
					MutiTreeNode child = childList.get(i);
					MutiTreeNode resultNode = child.findTreeNodeById(id);
					if (resultNode != null) {
						return resultNode;
					}
				} catch (Exception e) {
				}
			}
			return null;
		}
	}

	/** 遍历一棵树，层次遍历 */
	public void traverse() {
		if (StringUtil.equals(id, "-1"))
			return;
		print(this.id);
		if (childList == null || childList.isEmpty())
			return;
		int childNumber = childList.size();
		for (int i = 0; i < childNumber; i++) {
			MutiTreeNode child = childList.get(i);
			child.traverse();
		}
	}

	public void print(String content) {
		System.out.println(content);
	}

	public void print(int content) {
		System.out.println(String.valueOf(content));
	}

	public void setChildList(List<MutiTreeNode> childList) {
		this.childList = childList;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public MutiTreeNode getParent() {
		return parent;
	}

	public void setParent(MutiTreeNode parent) {
		this.parent = parent;
	}

	
}