package com.xplatform.base.framework.core.common.model.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import jodd.util.StringUtil;


/**
 * 
 * description :实现多叉树
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月10日 下午1:46:41
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年6月10日 下午1:46:41
 *
 */
public class TreeHelper implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private MutiTreeNode root;
	private List<MutiTreeNode> tempNodeList;
	private LinkedHashMap g_nodeMap = new LinkedHashMap();
	private boolean isValidTree = true;

	public TreeHelper() {
	}

	public TreeHelper(List<MutiTreeNode> treeNodeList) {
		tempNodeList = treeNodeList;
		generateTree();
	}

	public static MutiTreeNode getTreeNodeById(MutiTreeNode tree, String id) {
		if (tree == null)
			return null;
		MutiTreeNode treeNode;
		treeNode = tree.findTreeNodeById(id);
		return treeNode;
	}
	
	public MutiTreeNode getTreeNodeById(int id) {
		MutiTreeNode treeNode;
		treeNode = (MutiTreeNode) g_nodeMap.get(String.valueOf(id));
		return treeNode;
	}

	/**
	 * generate a tree from the given treeNode or entity list
	 * 生成树或实体由给定的treeNode名单
	 * */
	public void generateTree() {
		LinkedHashMap nodeMap = putNodesIntoMap();
		g_nodeMap = nodeMap;
		putChildIntoParent(nodeMap);
	}

	/**
	 * put all the treeNodes into a hash table by its id as the key
	 * 把所有的treeNodes成一张哈希表由其id作为关键
	 * 
	 * @return hashmap that contains the treenodes
	 */
	protected LinkedHashMap putNodesIntoMap() {
		int maxId = Integer.MAX_VALUE;
		LinkedHashMap nodeMap = new LinkedHashMap<String, MutiTreeNode>();
		Iterator it = tempNodeList.iterator();
		while (it.hasNext()) {
			MutiTreeNode treeNode = (MutiTreeNode) it.next();
			/*int id = treeNode.getId();
			if (id < maxId) {
				maxId = id;*/
			if(StringUtil.equals(treeNode.getId(), "-1")){
				this.root = treeNode;
			}
			
			//}
			//String keyId = String.valueOf(id);

			nodeMap.put(treeNode.getId(), treeNode);
			// System.out.println("keyId: " +keyId);
		}
		return nodeMap;
	}

	/**
	 * set the parent nodes point to the child nodes 设置子节点集合添加到父节点上。
	 * 
	 * @param nodeMap
	 *            a hashmap that contains all the treenodes by its id as the key
	 *            一个hashmap,包括所有treenodes由其id作为关键
	 */
	protected void putChildIntoParent(LinkedHashMap nodeMap) {
		Iterator it = nodeMap.values().iterator();
		while (it.hasNext()) {
			MutiTreeNode treeNode = (MutiTreeNode) it.next();
			String parentId = treeNode.getParentId();
			String parentKeyId = String.valueOf(parentId);
			if (nodeMap.containsKey(parentKeyId)) {
				MutiTreeNode parentNode = (MutiTreeNode) nodeMap.get(parentKeyId);
				treeNode.setParent(parentNode);
				if (parentNode == null) {
					this.isValidTree = false;
					return;
				} else {
					parentNode.addChildNode(treeNode);
				}
			}
		}
	}

	/**
	 * initialize the tempNodeList property 初始化tempNodeList属性
	 * */
	protected void initTempNodeList() {
		if (this.tempNodeList == null) {
			this.tempNodeList = new ArrayList<MutiTreeNode>();
		}
	}

	/**
	 * add a tree node to the tempNodeList 添加一个树节点的tempNodeList
	 * */
	public void addTreeNode(MutiTreeNode treeNode) {
		initTempNodeList();
		this.tempNodeList.add(treeNode);
	}

	/**
	 * insert a tree node to the tree generated already 将已经产生的树节点插入到树
	 * 
	 * @return show the insert operation is ok or not
	 */
	public boolean insertTreeNode(MutiTreeNode treeNode) {
		boolean insertFlag = root.insertJuniorNode(treeNode);
		return insertFlag;
	}

	public boolean isValidTree() {
		return this.isValidTree;
	}

	public MutiTreeNode getRoot() {
		return root;
	}

	public void setRoot(MutiTreeNode root) {
		this.root = root;
	}

	public List<MutiTreeNode> getTempNodeList() {
		return tempNodeList;
	}

	public void setTempNodeList(List<MutiTreeNode> tempNodeList) {
		this.tempNodeList = tempNodeList;
	}

}
