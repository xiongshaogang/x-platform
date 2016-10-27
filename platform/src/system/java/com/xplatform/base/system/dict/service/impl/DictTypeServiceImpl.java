package com.xplatform.base.system.dict.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.cache.UcgCache;
import com.xplatform.base.framework.core.cache.manager.UcgCacheManager;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.ComboBox;
import com.xplatform.base.framework.core.common.model.json.TreeNode;
import com.xplatform.base.framework.core.common.model.tree.TreeHelper;
import com.xplatform.base.framework.core.common.model.tree.MutiTreeNode;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.system.dict.dao.DictTypeDao;
import com.xplatform.base.system.dict.entity.DictTypeEntity;
import com.xplatform.base.system.dict.entity.DictValueEntity;
import com.xplatform.base.system.dict.service.DictTypeService;

/**
 * 
 * description :字典类型service实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:30:12
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 下午12:30:12
 *
 */
@Service("dictTypeService")
public class DictTypeServiceImpl extends BaseServiceImpl<DictTypeEntity> implements DictTypeService {
	private static final Logger logger = Logger.getLogger(DictTypeServiceImpl.class);
	@Resource
	private DictTypeDao dictTypeDao;
	@Resource
	private UcgCacheManager ucgCacheManager;
	private UcgCache ucgCache;
	
	@Resource
	public void setBaseDao(DictTypeDao dictTypeDao) {
		super.setBaseDao(dictTypeDao);
	}
	
	public void createExtTree(MutiTreeNode rootTree, TreeNode extTree) {
		if (rootTree.getChildList() == null) {
			return;
		} else {
			if (StringUtil.equals(rootTree.getParentId(), "-1")) {
				extTree.setId(rootTree.getId());
				extTree.setText(rootTree.getText());
				extTree.setState(rootTree.getState());
			}
			for (MutiTreeNode node : rootTree.getChildList()) {
				TreeNode extNode = new TreeNode();
				extNode.setId(node.getId());
				extNode.setText(node.getText());
				if (extTree.getChildren() == null) {
					extTree.setChildren(new ArrayList<TreeNode>());
				}
				extTree.getChildren().add(extNode);
				createExtTree(node, extNode);
			}
		}
	}

	@Override
	public void initCache() {
		// TODO Auto-generated method stub
		String dictTypeSql = " from DictTypeEntity";
		List<DictTypeEntity> dictTypeList = dictTypeDao.findHql(dictTypeSql,
				new Object[] {});
		ucgCache = ucgCacheManager.getDictCacheBean();

		for (DictTypeEntity dictType : dictTypeList) {
			List<ComboBox> list = new ArrayList<ComboBox>();
			if (StringUtil.equals("selected", dictType.getValueType())) {
				for (DictValueEntity valueEntity : dictType.getDictValueList()) {
					ComboBox comboBox = new ComboBox();
					comboBox.setId(valueEntity.getValue());
					comboBox.setText(valueEntity.getName());
					list.add(comboBox);
				}
				ucgCache.put("dict_" + dictType.getCode(), list);
			} else if (StringUtil.equals("tree", dictType.getValueType())) {
				TreeHelper pageTree = new TreeHelper();

				MutiTreeNode rootNode = new MutiTreeNode();
				rootNode.setParentId("-2");
				rootNode.setId("-1");
				rootNode.setText("根节点");
				pageTree.addTreeNode(rootNode);

				for (DictValueEntity valueEntity : dictType.getDictValueList()) {
					MutiTreeNode pageNode = new MutiTreeNode();
					// 父节点ID
					pageNode.setId(valueEntity.getId());
					if (valueEntity.getParent() != null) {
						pageNode.setParentId(valueEntity.getParent().getId());
					} else {
						pageNode.setParentId("-1");
					}
					pageNode.setText(valueEntity.getName());
					if (StringUtil.equals(valueEntity.getIsLeaf(), "0")) {
						pageNode.setState("closed");
					} else {
						pageNode.setState("open");
					}
					pageTree.addTreeNode(pageNode);

				}
				pageTree.generateTree();

				MutiTreeNode rootTree = pageTree.getRoot();

				TreeNode extTree = new TreeNode();
				createExtTree(rootTree, extTree);
				String data = JSONHelper.array2json(extTree.getChildren());
				ucgCache.put("dict_" + dictType.getCode(), data);
			}

		}
	}

	@Override
	public <T> T findCacheByCode(String code) {
		// TODO Auto-generated method stub
		ucgCache = ucgCacheManager.getDictCacheBean();
		Object obj = ucgCache.get("dict_" + code);
		if (obj == null) {
			DictTypeEntity dictType = dictTypeDao.findUniqueByProperty("code", code);
			if (dictType != null) {
				List<DictValueEntity> valueList = dictType.getDictValueList();
				if (valueList != null && valueList.size() > 0) {
					if (StringUtil.equals("tree", dictType.getValueType())) {
						TreeHelper pageTree = new TreeHelper();

						MutiTreeNode rootNode = new MutiTreeNode();
						rootNode.setParentId("-2");
						rootNode.setId("-1");
						rootNode.setText("根节点");
						pageTree.addTreeNode(rootNode);

						for (DictValueEntity valueEntity : valueList) {
							MutiTreeNode pageNode = new MutiTreeNode();
							// 父节点ID
							pageNode.setId(valueEntity.getId());
							if (valueEntity.getParent() != null) {
								pageNode.setParentId(valueEntity.getParent()
										.getId());
							} else {
								pageNode.setParentId("-1");
							}
							pageNode.setText(valueEntity.getName());
							if (StringUtil.equals(valueEntity.getIsLeaf(), "0")) {
								pageNode.setState("closed");
							} else {
								pageNode.setState("open");
							}
							pageTree.addTreeNode(pageNode);

						}
						pageTree.generateTree();

						MutiTreeNode rootTree = pageTree.getRoot();

						TreeNode extTree = new TreeNode();
						createExtTree(rootTree, extTree);
						String data = JSONHelper.bean2json(extTree);
						ucgCache.put("dict_" + dictType.getCode(), data);
						return (T) data;
					} else if (StringUtil.equals("selected",
							dictType.getValueType())) {
						List<ComboBox> list = new ArrayList<ComboBox>();
						for (DictValueEntity value : valueList) {
							ComboBox comboBox = new ComboBox();
							comboBox.setId(value.getValue());
							comboBox.setText(value.getName());
							list.add(comboBox);
						}
						ucgCache.put("dict_" + code, list);
						return (T) list;
					}
				}
			} else {
				return null;
			}
		} else {
			return (T) obj;
		}
		return null;
	}

	@Override
	public List<DictValueEntity> findDictValueEntityListByCode(String dictCode){
		return dictTypeDao.findByProperty(DictValueEntity.class, "typeCode", dictCode);
	}

	@Override
	public List<ComboBox> transformToComboBox(String dictCode) {
		// TODO Auto-generated method stub
		List<ComboBox> list = new ArrayList<ComboBox>();
		List<DictValueEntity> dictValueEntityList = this
				.findDictValueEntityListByCode(dictCode);
		for (DictValueEntity dictValueEntity : dictValueEntityList) {
			ComboBox comboBox = new ComboBox();
			comboBox.setId(dictValueEntity.getValue());
			comboBox.setText(dictValueEntity.getName());
			list.add(comboBox);
		}

		return list;
	}

	@Override
	public String findDictTypeByCode(String dictCode) {
		// TODO Auto-generated method stub
//		String hql = "SELECT valueType FROM DictTypeEntity WHERE code='"
//				+ dictCode + "'";
		List<DictTypeEntity> objList = dictTypeDao.findByProperty(DictTypeEntity.class, "code", dictCode);
		if (objList != null && objList.size() > 0) {
			return objList.get(0).getValueType();
		}
		return null;
	}

}
