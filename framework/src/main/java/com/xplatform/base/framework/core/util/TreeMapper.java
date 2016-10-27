package com.xplatform.base.framework.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.entity.IdEntity;
import com.xplatform.base.framework.core.common.model.json.TreeNode;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;

/**
 * description :构造树形节点
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月13日 上午9:11:22
 * 
 */
public class TreeMapper {
	/** 属性类型. */
	public static enum PropertyType {
		ID("id"),

		TEXT("text"),

		LEAF("isLeaf"),

		CHECKED("checked"),

		ICONCLS("iconCls"),

		CHILDREN("children"),

		LEVEL("level"),

		ATTRIBUTES("attributes");// 该属性指代树节点带上的额外数据,使用时对象中需要作为额外数据的属性用","隔开

		/**
		 * 使用方法: Map<String, String> propertyMapping = new HashMap<String,
		 * String>(); propertyMapping.put(TreeMapper.PropertyType.ID.getValue(),
		 * "id"); propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(),
		 * "name"); propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(),
		 * "isLeaf");
		 * propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(),
		 * "stuName,stuAge"); List<TreeNode> treeList =
		 * TreeMapper.buildJsonTree(moduleList, propertyMapping);
		 * TagUtil.tree(response, treeList);
		 **/

		private String property;

		private PropertyType(String property) {
			this.property = property;
		}

		public String getValue() {
			return property;
		}

	}

	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月13日 上午9:48:03
	 * @Decription 构造普通树
	 * @param srcs
	 * @param propertyMapping
	 * @param parentId
	 * @return
	 */
	public static <T> List<TreeNode> buildJsonTree(List<T> srcs, final Map<String, String> propertyMapping) {
		return buildJsonTree(srcs, propertyMapping, null);
	}

	/**
	 * @author xiehs
	 * @createtime 2014年5月13日 上午9:48:10
	 * @Decription 构造带复选框的树
	 * @param srcs
	 * @param propertyMapping
	 * @param parentId
	 * @param checkedIds
	 * @return
	 */
	public static <T> List<TreeNode> buildJsonTree(List<T> srcs, final Map<String, String> paramPropertyMapping, final List<String> checkedIds) {
		// 默认转换参数
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		propertyMapping.put(TreeMapper.PropertyType.ICONCLS.getValue(), "iconCls");
		propertyMapping.put(TreeMapper.PropertyType.LEVEL.getValue(), "level");
		// 合并传入的参数,一般为attribute
		if (paramPropertyMapping != null && paramPropertyMapping.size() > 0) {
			propertyMapping.putAll(paramPropertyMapping);
		}

		if (srcs == null || srcs.size() == 0) {
			return null;
		}
		List<TreeNode> trees = new ArrayList<TreeNode>();
		TreeNode treeNode = null;
		for (T src : srcs) {
			String isLeaf = "open";
			boolean checked = false;
			Map<String, Object> attributes = new HashMap<String, Object>();
			try {
				treeNode = new TreeNode();
				// 遍历转换类中的属性
				for (String treeProperty : propertyMapping.keySet()) {
					// 获得对象中属性名
					String srcProperty = propertyMapping.get(treeProperty);

					Object srcValue = null;

					// 针对ATTRIBUTES额外数据额外处理
					if (PropertyType.ATTRIBUTES.getValue().equals(treeProperty)) {
						String[] srcPropertyArray = srcProperty.split(",");
						for (String srcPropertyItem : srcPropertyArray) {
							Object srcValueTemp = ReflectHelper.getFieldValue(src, srcPropertyItem);
							attributes.put(srcPropertyItem, srcValueTemp);
						}
						treeNode.setAttributes(attributes);
					} else {
						// 获得对象中属性值
						srcValue = ReflectHelper.getFieldValue(src, srcProperty);
						// 构造叶子其他固有属性
						if (srcValue != null) {
							if (PropertyType.LEAF.getValue().equals(treeProperty)) {
								if ("0".equals(srcValue.toString())) {
									isLeaf = "closed";
								} else {
									isLeaf = "open";
								}
								treeNode.setState(isLeaf);
							} else if (PropertyType.ICONCLS.getValue().equals(treeProperty)) {// 节点样式
								treeNode.setIconCls(srcValue.toString());
							} else if (PropertyType.LEVEL.getValue().equals(treeProperty)) {// 节点层级
								treeNode.setLevel(srcValue.toString());
							} else if (PropertyType.CHECKED.getValue().equals(treeProperty)) {// 是否选中
								if ("1".equals(srcValue)) {
									checked = true;
								}
								treeNode.setChecked(checked);
							} else if (PropertyType.CHILDREN.getValue().equals(// 构造孩子节点
									treeProperty)) {
								List<T> entityList = (List<T>) srcValue;
								if (entityList != null && !entityList.isEmpty()) {
									List<TreeNode> childTrees = new ArrayList<TreeNode>();
									String pid = (String) ReflectHelper.getFieldValue(src, PropertyType.ID.getValue());
									childTrees = buildJsonTree(entityList, propertyMapping, checkedIds);
									treeNode.setChildren(childTrees);
								}
							} else {
								ReflectHelper.setFieldValue(treeNode, treeProperty, srcValue);
							}
						}
					}

				}
				// 构造选中树节点
				if (checkedIds != null && checkedIds.size() > 0) {
					for (String checkedId : checkedIds) {
						if (treeNode.getId().equals(checkedId)) {
							checked = true;
						}
					}
					treeNode.setChecked(checked);
					trees.add(treeNode);
				}
				if (!trees.contains(treeNode)) {
					trees.add(treeNode);
				}
			} catch (Exception e) {
				throw ReflectHelper.convertReflectionExceptionToUnchecked(e);
			}
		}
		return trees;
	}
}
