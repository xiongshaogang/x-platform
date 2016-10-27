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
import com.xplatform.base.framework.core.common.model.tree.MutiTreeNode;
import com.xplatform.base.framework.core.common.model.tree.TreeHelper;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.system.dict.dao.DictTypeDao;
import com.xplatform.base.system.dict.dao.DictValueDao;
import com.xplatform.base.system.dict.entity.DictTypeEntity;
import com.xplatform.base.system.dict.entity.DictValueEntity;
import com.xplatform.base.system.dict.service.DictTypeService;
import com.xplatform.base.system.dict.service.DictValueService;

/**
 * 
 * description :字典值service实现
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
@Service("dictValueService")
public class DictValueServiceImpl extends BaseServiceImpl<DictValueEntity> implements DictValueService {
	private static final Logger logger = Logger.getLogger(DictValueServiceImpl.class);
	@Resource
	private DictValueDao dictValueDao;
	@Resource
	private DictTypeDao dictTypeDao;
	@Resource
	private DictTypeService dictTypeService;
	@Resource
	private UcgCacheManager ucgCacheManager;
	private UcgCache ucgCache;
	
	@Resource
	public void setBaseDao(DictValueDao dictValueDao) {
		super.setBaseDao(dictValueDao);
	}

	@Override
	@Action(moduleCode = "dictManager", description = "字典值新增", detail = "字典值${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(DictValueEntity DictValue) throws BusinessException {
		String pk = "";
		try {
			DictValueEntity parent = this.get(DictValue.getParent().getId());
			//第一步，保存信息
			pk = (String) this.dictValueDao.save(DictValue);
			if (parent != null) {
				//第二步，修改树的详细信息
				DictValue.setId(pk);
				DictValue.setTreeIndex(parent.getTreeIndex() + "," + pk);
				DictValue.setLevel(parent.getLevel() + 1);
				DictValue.setIsLeaf("1");
				this.dictValueDao.merge(DictValue);
				//第三步，更新父节点的信息
				if (StringUtil.equals(parent.getIsLeaf(), "1")) {//父节点是叶子节点
					parent.setIsLeaf("0");
					if (parent.getParent() == null) {
						DictValueEntity parent1 = new DictValueEntity();
						parent1.setId("-1");
						parent.setParent(parent1);
					}
					this.dictValueDao.merge(parent);
				}
			} else {
				DictValue.setId(pk);
				DictValue.setTreeIndex(pk);
				DictValue.setLevel(1);
				DictValue.setIsLeaf("1");
				this.dictValueDao.merge(DictValue);
			}
			this.updateCache(DictValue.getTypeCode());
		} catch (Exception e) {
			logger.error("字典值保存失败");
			throw new BusinessException("字典值保存失败");
		}
		logger.info("字典值保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode = "dictManager", description = "删除字典值", detail = "id为${id}的字典值删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		String msg = "字典值删除失败";
		try {
			DictValueEntity dictValue = get(id);

			//设置父节点是否叶子节点
			if (dictValue != null && dictValue.getParent() != null && dictValue.getParent().getChildren() != null
					&& dictValue.getParent().getChildren().size() <= 1) {
				DictValueEntity parent = null;
				parent = dictValue.getParent();
				parent.setIsLeaf("1");
				//修改节点
				this.dictValueDao.merge(parent);
			}

			String dictCode = dictValue.getDictType().getCode();
			if (dictValue.getChildren() != null && dictValue.getChildren().size() > 0) {
				msg = "字典值" + dictValue.getName() + "有子节点，不允许删除";
				throw new BusinessException(msg);
			}
			//删除节点
			this.dictValueDao.deleteEntityById(id);

			//更新缓存
			DictTypeEntity dictType = dictTypeDao.findUniqueByProperty("code", dictCode);
			if (dictType != null && dictType.getDictValueList() != null && dictType.getDictValueList().size() > 0) {
				this.updateCache(dictCode);
			} else {
				this.deleteCache(dictCode);
			}
		} catch (Exception e) {
			logger.error(msg);
			throw new BusinessException(msg);
		}
		logger.info("字典值删除成功");
	}

	@Override
	@Action(moduleCode = "dictManager", description = "字典值修改", detail = "字典值${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(DictValueEntity DictValue) throws BusinessException {
		try {
			DictValueEntity oldEntity = get(DictValue.getId());
			if (oldEntity.getParent() == null) {
				DictValueEntity parent = new DictValueEntity();
				parent.setId("-1");
				oldEntity.setParent(parent);
			}

			MyBeanUtils.copyBeanNotNull2Bean(DictValue, oldEntity);
			this.dictValueDao.merge(oldEntity);
			this.updateCache(oldEntity.getTypeCode());
		} catch (Exception e) {
			logger.error("字典值更新失败");
			throw new BusinessException("字典值更新失败");
		}

		logger.info("字典值更新成功");
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

	public void updateCache(String code) {
		DictTypeEntity dictType = dictTypeDao.findUniqueByProperty("code", code);
		ucgCache = ucgCacheManager.getDictCacheBean();

		if (StringUtil.equals("selected", dictType.getValueType())) {
			List<ComboBox> list = new ArrayList<ComboBox>();
			for (DictValueEntity valueEntity : dictType.getDictValueList()) {
				ComboBox comboBox = new ComboBox();
				comboBox.setId(valueEntity.getValue());
				comboBox.setText(valueEntity.getName());
				list.add(comboBox);
			}
			deleteCache(dictType.getCode());
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

			deleteCache(dictType.getCode());
			ucgCache.put("dict_" + dictType.getCode(), data);
		}

	}

	public void deleteCache(String code) {
		DictTypeEntity dictType = dictTypeDao.findUniqueByProperty("code", code);
		ucgCache = ucgCacheManager.getDictCacheBean();
		Object obj = ucgCache.get("dict_" + dictType.getCode());
		if (obj != null) {
			ucgCache.remove("dict_" + dictType.getCode());
		}
	}

	@Override
	public List<DictValueEntity> queryListByPorpertys(Map<String, String> param){
		String hql = "SELECT v FROM DictValueEntity v LEFT JOIN v.dictType t WHERE t.code=? and v.parent.id=?";
		List<DictValueEntity> list = this.dictValueDao.findHql(hql, param.get("dictTypeCode"), param.get("parentId"));
		return list;
	}
	
	@Override
	public List<DictValueEntity> queryListByTypeidAndParentid(Map<String, String> param) {
		String hql = "SELECT v FROM DictValueEntity v LEFT JOIN v.dictType t WHERE t.id=? and v.parent.id=?";
		List<DictValueEntity> list = this.dictValueDao.findHql(hql, param.get("dictType.id"), param.get("parent.id"));
		return list;
	}

	@Override
	public List<DictValueEntity> findValuesByType(String code) {
		List<DictValueEntity> list = new ArrayList<DictValueEntity>();
		DictTypeEntity dictType = dictTypeDao.findUniqueByProperty("code", code);
		if (dictType != null) {
			list = dictType.getDictValueList();
		}
		return list;
	}
	
	@Override
	public Boolean isValueUnique(String typeId, String value) {
		String hql = "FROM DictValueEntity v where v.dictType.id=? and v.value=?";
		List<DictValueEntity> list = dictValueDao.findHql(hql, new Object[] { typeId, value });
		return list.size() > 0 ? false : true;
	}
	
	@Override
	public List<DictValueEntity> findValuesByValue(String value) throws BusinessException {
		String hql = "FROM DictValueEntity t WHERE t.value=?";
		return dictValueDao.findHql(hql, new Object[] { value });
	}
}
