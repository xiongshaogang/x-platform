package com.xplatform.base.system.category.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.orgnaization.resouce.entity.ResourceEntity;
import com.xplatform.base.orgnaization.resouce.service.ResourceService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.system.category.dao.CategoryDao;
import com.xplatform.base.system.category.entity.CategoryEntity;
import com.xplatform.base.system.category.mybatis.dao.CategoryMybatisDao;
import com.xplatform.base.system.category.mybatis.vo.CategoryTreeVo;
import com.xplatform.base.system.category.service.CategoryService;
import com.xplatform.base.system.type.entity.FileTypeRoleAuthorityEntity;
import com.xplatform.base.system.type.entity.FileTypeRoleEntity;
import com.xplatform.base.system.type.entity.FileTypeUserAuthorityEntity;
import com.xplatform.base.system.type.entity.FileTypeUserEntity;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService{

	private static final Logger logger = Logger.getLogger(CategoryServiceImpl.class);
	@Resource
	private CategoryDao categoryDao;

	@Resource
	private CategoryMybatisDao categoryMybatisDao;

	@Resource
	private AuthorityService authorityService;

	private ResourceService resourceService;
	
	@Override
	public String saveCategory(CategoryEntity category) throws BusinessException {
		String pk = "";
		try {
			if (category.getParent() == null || StringUtil.isEmpty(category.getParent().getId())) {
				CategoryEntity parent = new CategoryEntity();
				parent.setId("-1");
				category.setParent(parent);
			}
			CategoryEntity categoryParent = categoryDao.getCategory(category.getParent().getId());
			// 第一步，保存信息
			pk = this.categoryDao.saveCategory(category);
			if (categoryParent != null) {
				// 第二步，修改树的详细信息
				category.setId(pk);
				category.setTreeIndex(categoryParent.getTreeIndex() + "," + pk);
				int level = categoryParent.getLevel() == null ? 0 : categoryParent.getLevel();
				category.setLevel(level + 1);
				category.setIsLeaf("1");
				this.categoryDao.merge(category);
				// 第三步，更新父节点的信息
				if (categoryParent.getIsLeaf() == null || categoryParent.getIsLeaf().equals("1")) {// 父节点是叶子节点
					if (categoryParent.getParent() == null) {
						CategoryEntity parent = new CategoryEntity();
						parent.setId("-1");
						categoryParent.setParent(parent);
					}
					categoryParent.setIsLeaf("0");
					this.categoryDao.merge(categoryParent);
				}
			} else {
				category.setId(pk);
				category.setTreeIndex(pk);
				category.setLevel(1);
				category.setIsLeaf("1");
				this.categoryDao.merge(category);
			}
			logger.info("系统分类保存成功");
		} catch (Exception e) {
			logger.error("系统分类保存失败");
			throw new BusinessException("系统分类保存失败");
		}
		return pk;
	}

	@Override
	public void updateCategory(CategoryEntity category) throws BusinessException {
		try {
			CategoryEntity oldEntity = getCategory(category.getId());
			MyBeanUtils.copyBeanNotNull2Bean(category, oldEntity);
			if (oldEntity.getParent() == null || StringUtil.isEmpty(oldEntity.getParent().getId())) {
				CategoryEntity parent = new CategoryEntity();
				parent.setId("-1");
				oldEntity.setParent(parent);
			}
			this.categoryDao.updateCategory(oldEntity);
			logger.info("系统分类更新成功");
		} catch (Exception e) {
			logger.error("系统分类更新失败");
			throw new BusinessException("系统分类更新失败");
		}
		
	}

	@Override
	public void delete(String id) throws BusinessException {

		try {
			CategoryEntity category = getCategory(id);
			CategoryEntity parent = null;
			// 获取父节点
			if (category != null && category.getParent() != null) {
				parent = category.getParent();
			}
			// 设置父节点是否叶子节点
			if (parent != null) {
				if (parent.getChildren() != null) {
					if (parent.getChildren().size() <= 1) {
						parent.setIsLeaf("1");
					}
				}
			}
			this.categoryDao.deleteCategory(id);
			// 修改节点
			if (parent != null)
				this.updateCategory(parent);
		} catch (Exception e) {
			logger.error("系统分类删除失败");
			throw new BusinessException("系统分类删除失败");
		}
		logger.info("系统分类删除成功");
	
		
	}

	@Override
	public void batchDelete(String ids) throws BusinessException {

		try {
			if (StringUtil.isNotEmpty(ids)) {
				String[] idArr = StringUtil.split(ids, ",");
				for (String id : idArr) {
					this.delete(id);
				}
			}
			logger.info("系统分类批量删除成功");
		} catch (Exception e) {
			logger.error("系统分类批量删除失败");
			throw new BusinessException("系统分类批量删除失败");
		}
	
		
	}

	@Override
	public CategoryEntity getCategory(String id) {
		CategoryEntity category = null;
		if (StringUtil.isNotEmpty(id)) {
			category = categoryDao.getCategory(id);
			logger.info("获取系统分类信息成功");
		}
		return category;
	}

	@Override
	public List<CategoryEntity> queryCategoryList() throws BusinessException {
		List<CategoryEntity> categoryList = new ArrayList<CategoryEntity>();
		try {
			categoryList = categoryDao.queryCategoryList();
			logger.error("获取所有系统分类信息成功");
		} catch (Exception e) {
			logger.error("获取所有系统分类信息失败");
			throw new BusinessException("获取所有系统分类信息失败");
		}
		return categoryList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
		this.categoryDao.getDataGridReturn(cq, true);
		logger.info("系统分类分页列表成功");
	}

	@Override
	public List<CategoryEntity> queryListByPorperty(String propertyName, String value) {
		List<CategoryEntity> categoryList = new ArrayList<CategoryEntity>();
		categoryList = this.categoryDao.findByProperty(CategoryEntity.class, propertyName, value);
		return categoryList;
	}

	@Override
	public List<CategoryTreeVo> queryCategoryTree(String userId, String parentId) throws BusinessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", userId);
		param.put("parentId", parentId);
		List<CategoryTreeVo> categoryTreeVoTreeList = new ArrayList<CategoryTreeVo>();
		try {
			categoryTreeVoTreeList = this.categoryMybatisDao.queryCategoryTree(param);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("根据权限查询系统类型树失败");
		}
		return categoryTreeVoTreeList;
	}

	@Override
	public List<CategoryTreeVo> queryUserCategoryTree(String userId, String parentId) throws BusinessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", userId);
		param.put("parentId", parentId);
		List<CategoryTreeVo> categoryTreeList = new ArrayList<CategoryTreeVo>();
		try {
			categoryTreeList = this.categoryMybatisDao.queryUserCategoryTree(param);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("根据用户ID查询该用户可操作的系统类型失败");
		}
		return categoryTreeList;
	}

	@Override
	public List<CategoryTreeVo> queryTypeRoleTreeBySysType(Map<String, String> param) throws BusinessException {
		List<CategoryTreeVo> categoryTreeList = new ArrayList<CategoryTreeVo>();
		if (this.authorityService.isAdmin(param)) {// 不是管理员，进行权限过滤
			List<CategoryEntity> categoryList = this.queryListByPorperty("sysType", param.get("sysType"));
			for (CategoryEntity category : categoryList) {
				CategoryTreeVo categoryTreeVo = new CategoryTreeVo();
				categoryTreeVo.setId(category.getId());
				categoryTreeVo.setCode(category.getCode());
				categoryTreeVo.setName(category.getName());
				categoryTreeVo.setSysType(category.getSysType());
				categoryTreeVo.setRemark(category.getRemark());
				if (category.getParent() != null) {
					categoryTreeVo.setParentId(category.getParent().getId());
				} else {
					categoryTreeVo.setParentId("-1");
				}
				categoryTreeVo.setIsLeaf(category.getIsLeaf());
				categoryTreeVo.setLevel(category.getLevel());
				categoryTreeVo.setIconCls(category.getIconCls());
				categoryTreeList.add(categoryTreeVo);
			}
		} else {
			try {
				categoryTreeList = this.categoryMybatisDao.queryTypeRoleTreeBySysTypeList(param);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException("根据类型、用户权限查询系统类型失败");
			}
		}
		return categoryTreeList;
	}

	@Override
	public List<CategoryTreeVo> queryTypeRoleTreeBySysTypeTree(Map<String, String> param) throws BusinessException {
		if (param.get("parentId").isEmpty()) {
			param.put("parentId", "-1");
		}
		List<CategoryTreeVo> categoryTreeList = new ArrayList<CategoryTreeVo>();
		if (this.authorityService.isAdmin(param)) {// 不是管理员，进行权限过滤
			List<CategoryEntity> categoryList = this.queryListByPorperty("parent.id", param.get("parentId"));
			for (CategoryEntity category : categoryList) {
				if (category.getSysType().equals(param.get("sysType"))) {
					CategoryTreeVo categoryTreeVo = new CategoryTreeVo();
					categoryTreeVo.setId(category.getId());
					categoryTreeVo.setCode(category.getCode());
					categoryTreeVo.setName(category.getName());
					categoryTreeVo.setSysType(category.getSysType());
					categoryTreeVo.setRemark(category.getRemark());
					if (category.getParent() != null) {
						categoryTreeVo.setParentId(category.getParent().getId());
					} else {
						categoryTreeVo.setParentId("-1");
					}
					categoryTreeVo.setIsLeaf(category.getIsLeaf());
					categoryTreeVo.setLevel(category.getLevel());
					categoryTreeVo.setIconCls(category.getIconCls());
					categoryTreeList.add(categoryTreeVo);
				}
			}
		} else {
			try {
				categoryTreeList = this.categoryMybatisDao.queryTypeRoleTreeBySysTypeTree(param);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException("根据类型、用户权限查询系统类型失败");
			}
		}
		return categoryTreeList;
	}

	@Override
	public Page<CategoryTreeVo> findByPage(Map<String, String> param, DataGrid dataGrid) {
		Page<CategoryTreeVo> page = new Page<CategoryTreeVo>();
		List<CategoryTreeVo> result = categoryMybatisDao.findByPage(param);
		page.setResult(result);
		page.setPageSize(dataGrid.getRows());
		page.setPageNo(dataGrid.getPage());
		return page;
	}

	@Override
	public String queryCategoryIdByCode(String code) {
		List<CategoryEntity> categoryEntity = categoryDao.findByProperty(CategoryEntity.class, "code", code);
		if (categoryEntity.size() > 0) {
			return categoryEntity.get(0).getId();
		}
		return null;
	}

	@Override
	public List<CategoryTreeVo> queryUserFileCategoryTree(String userId, String parentId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", userId);
		param.put("parentId", parentId);
		List<CategoryTreeVo> categoryTreeList = new ArrayList<CategoryTreeVo>();
		categoryTreeList = this.categoryMybatisDao.queryUserFileCategoryTree(param);
		return categoryTreeList;
	}

	@Override
	public void updateUserFileCategory(String checkedCategoryIds, String pIds, String userId) throws BusinessException {
		Map<String, FileTypeUserEntity> map = new HashMap<String, FileTypeUserEntity>();
		// key存放本次选中以及未选中的父id(混在一起),value存放是否选中
		Map<String, String> idMap = new HashMap<String, String>();
		UserEntity userEntity = this.categoryDao.get(UserEntity.class, userId);

		List<FileTypeUserEntity> fileTypeUserEntityList = this.categoryDao.findByProperty(FileTypeUserEntity.class, "userEntity.id", userId);
		for (FileTypeUserEntity fileTypeUserEntity : fileTypeUserEntityList) {
			map.put(fileTypeUserEntity.getTypeEntity().getId(), fileTypeUserEntity);
		}
		if (StringUtil.isNotEmpty(checkedCategoryIds)) {
			// 选中的ids
			String[] checkedTypeIdsArray = checkedCategoryIds.split(",");
			for (String checkedTypeId : checkedTypeIdsArray) {
				idMap.put(checkedTypeId, "1");
			}
		}
		if (StringUtil.isNotEmpty(pIds)) {
			// 未选中的父ids
			String[] pIdsArray = pIds.split(",");
			for (String pId : pIdsArray) {
				idMap.put(pId, "0");
			}
		}
		updateUserFileCategoryCompare(idMap, userEntity, map);
	}
	
	private void updateUserFileCategoryCompare(Map<String, String> idMap, UserEntity userEntity, Map<String, FileTypeUserEntity> map) throws BusinessException {
		List<FileTypeUserEntity> saveEntitys = new ArrayList<FileTypeUserEntity>();
		List<FileTypeUserEntity> deleteEntitys = new ArrayList<FileTypeUserEntity>();
		for (Entry<String, String> entry : idMap.entrySet()) {
			String id = entry.getKey();
			String isManage = entry.getValue();
			if (map.containsKey(id)) {
				// 判断需要更新的记录
				if (!map.get(id).getIsManage().equals(isManage)) {
					FileTypeUserEntity updateEntity = map.get(id);
					updateEntity.setIsManage(isManage);
					saveEntitys.add(updateEntity);
				}
				// 过滤记录集map中原来有,但是后来没出现的,即是需要删除的
				map.remove(id);
			} else {
				// map中没有的,就是新增的
				FileTypeUserEntity saveEntity = new FileTypeUserEntity();
				CategoryEntity typeEntity = new CategoryEntity();
				typeEntity.setId(id);
				saveEntity.setUserEntity(userEntity);
				// TODO Auto-generated method stub
				//saveEntity.setTypeEntity(typeEntity);
				saveEntity.setIsManage(isManage);
				saveEntitys.add(saveEntity);
			}
		}
		for (Entry<String, FileTypeUserEntity> entry : map.entrySet()) {
			deleteEntitys.add(entry.getValue());
		}
		try {
			this.categoryDao.batchSaveOrUpdate(saveEntitys);
			this.categoryDao.deleteAllEntitie(deleteEntitys);
			// deleteEntitys 删除资料文件夹权限，同时删除操作按钮权限
			String hql = "delete from t_sys_filetype_user_authority where user_entity=? and type_entity=?";
			for (FileTypeUserEntity deleteEntity : deleteEntitys) {
				categoryDao.executeSql(hql, new Object[] { deleteEntity.getUserEntity().getId(), deleteEntity.getTypeEntity().getId() });
			}
		} catch (Exception e) {
			throw new BusinessException("保存资料文件夹权限失败");
		}
	}

	@Override
	public void updateUserFileCategoryAuthority(String resourceIds, String categoryId, String userId) throws BusinessException {
		Map<String, FileTypeUserAuthorityEntity> map = new HashMap<String, FileTypeUserAuthorityEntity>();
		UserEntity userEntity = this.categoryDao.get(UserEntity.class, userId);
		CategoryEntity categoryEntity = this.categoryDao.get(CategoryEntity.class, categoryId);
		// 查找之前的权限记录
		// TODO Auto-generated method stub
		//待交流
		List<FileTypeUserAuthorityEntity> fileTypeUserAuthorityEntityList = this.resourceService.queryUserFileTypeAuthority(categoryId, userId);
		for (FileTypeUserAuthorityEntity fileTypeUserAuthorityEntity : fileTypeUserAuthorityEntityList) {
			map.put(fileTypeUserAuthorityEntity.getResourceEntity().getId(), fileTypeUserAuthorityEntity);
		}
		if (resourceIds == null) {
			resourceIds = "";
		}
		String[] resourceIdArray = resourceIds.split(",");
		updateUserAuthorityFileTypeCompare(resourceIdArray, categoryEntity, userEntity, map);
	}
	

	public void updateUserAuthorityFileTypeCompare(String[] resourceIdArray, CategoryEntity typeEntity, UserEntity userEntity,
			Map<String, FileTypeUserAuthorityEntity> map) throws BusinessException {
		List<FileTypeUserAuthorityEntity> saveEntitys = new ArrayList<FileTypeUserAuthorityEntity>();
		List<FileTypeUserAuthorityEntity> deleteEntitys = new ArrayList<FileTypeUserAuthorityEntity>();
		for (String s : resourceIdArray) {
			if ("".equals(s.trim())) {
				continue;
			}
			if (map.containsKey(s)) {
				map.remove(s);
			} else {
				FileTypeUserAuthorityEntity saveEntity = new FileTypeUserAuthorityEntity();
				ResourceEntity resourceEntity = new ResourceEntity();
				resourceEntity.setId(s);
				// TODO Auto-generated method stub
				//saveEntity.setTypeEntity(typeEntity);
				saveEntity.setUserEntity(userEntity);
				saveEntity.setResourceEntity(resourceEntity);
				saveEntitys.add(saveEntity);
			}
		}
		for (Entry<String, FileTypeUserAuthorityEntity> entry : map.entrySet()) {
			deleteEntitys.add(entry.getValue());
		}
		this.categoryDao.deleteAllEntitie(deleteEntitys);
		this.categoryDao.batchSaveOrUpdate(saveEntitys);
	}

	@Override
	public List<CategoryTreeVo> queryRoleFileCategoryTree(String roleId, String parentId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("roleId", roleId);
		param.put("parentId", parentId);
		List<CategoryTreeVo> CategoryTreeList = new ArrayList<CategoryTreeVo>();
		CategoryTreeList = this.categoryMybatisDao.queryRoleFileCategoryTree(param);
		return CategoryTreeList;
	}

	@Override
	public void updateRoleFileCategory(String checkedCategoryIds, String pIds, String roleId) throws BusinessException {
		Map<String, FileTypeRoleEntity> map = new HashMap<String, FileTypeRoleEntity>();
		// key存放本次选中以及未选中的父id(混在一起),value存放是否选中
		Map<String, String> idMap = new HashMap<String, String>();
		RoleEntity roleEntity = this.categoryDao.get(RoleEntity.class, roleId);

		List<FileTypeRoleEntity> fileTypeRoleEntityList = this.categoryDao.findByProperty(FileTypeRoleEntity.class, "roleEntity.id", roleId);
		for (FileTypeRoleEntity fileTypeRoleEntity : fileTypeRoleEntityList) {
			map.put(fileTypeRoleEntity.getTypeEntity().getId(), fileTypeRoleEntity);
		}
		if (StringUtil.isNotEmpty(checkedCategoryIds)) {
			// 选中的ids
			String[] checkedTypeIdsArray = checkedCategoryIds.split(",");
			for (String checkedTypeId : checkedTypeIdsArray) {
				idMap.put(checkedTypeId, "1");
			}
		}
		if (StringUtil.isNotEmpty(pIds)) {
			// 未选中的父ids
			String[] pIdsArray = pIds.split(",");
			for (String pId : pIdsArray) {
				idMap.put(pId, "0");
			}
		}
		updateRoleFileTypeCompare(idMap, roleEntity, map);
	}
	
	private void updateRoleFileTypeCompare(Map<String, String> idMap, RoleEntity roleEntity, Map<String, FileTypeRoleEntity> map) throws BusinessException {
		List<FileTypeRoleEntity> saveEntitys = new ArrayList<FileTypeRoleEntity>();
		List<FileTypeRoleEntity> deleteEntitys = new ArrayList<FileTypeRoleEntity>();
		for (Entry<String, String> entry : idMap.entrySet()) {
			String id = entry.getKey();
			String isManage = entry.getValue();
			if (map.containsKey(id)) {
				// 判断需要更新的记录
				if (!map.get(id).getIsManage().equals(isManage)) {
					FileTypeRoleEntity updateEntity = map.get(id);
					updateEntity.setIsManage(isManage);
					saveEntitys.add(updateEntity);
				}
				// 过滤记录集map中原来有,但是后来没出现的,即是需要删除的
				map.remove(id);
			} else {
				// map中没有的,就是新增的
				FileTypeRoleEntity saveEntity = new FileTypeRoleEntity();
				CategoryEntity typeEntity = new CategoryEntity();
				typeEntity.setId(id);
				saveEntity.setRoleEntity(roleEntity);
				// TODO Auto-generated method stub
				//saveEntity.setTypeEntity(typeEntity);
				saveEntity.setIsManage(isManage);
				saveEntitys.add(saveEntity);
			}
		}
		for (Entry<String, FileTypeRoleEntity> entry : map.entrySet()) {
			deleteEntitys.add(entry.getValue());
		}
		try {
			this.categoryDao.batchSaveOrUpdate(saveEntitys);
			this.categoryDao.deleteAllEntitie(deleteEntitys);
			// deleteEntitys 删除资料文件夹权限，同时删除操作按钮权限
			String sql = "delete from t_sys_filetype_role_authority where role_entity=? and type_entity=?";
			for (FileTypeRoleEntity deleteEntity : deleteEntitys) {
				categoryDao.executeSql(sql, new Object[] { deleteEntity.getRoleEntity().getId(), deleteEntity.getTypeEntity().getId() });
			}
		} catch (Exception e) {
			throw new BusinessException("保存资料文件夹权限失败");
		}
	}

	@Override
	public void updateRoleFileCategoryAuthority(String resourceIds, String categoryId, String roleId) throws BusinessException {
		Map<String, FileTypeRoleAuthorityEntity> map = new HashMap<String, FileTypeRoleAuthorityEntity>();
		RoleEntity roleEntity = this.categoryDao.get(RoleEntity.class, roleId);
		CategoryEntity categoryEntity = this.categoryDao.get(CategoryEntity.class, categoryId);
		// 查找之前的权限记录
		// TODO Auto-generated method stub
		//categoryId原来是typeId，需改变queryRoleFileTypeAuthority方法
		List<FileTypeRoleAuthorityEntity> fileTypeRoleAuthorityEntityList = this.resourceService.queryRoleFileTypeAuthority(categoryId, roleId);
		for (FileTypeRoleAuthorityEntity fileTypeRoleAuthorityEntity : fileTypeRoleAuthorityEntityList) {
			map.put(fileTypeRoleAuthorityEntity.getResourceEntity().getId(), fileTypeRoleAuthorityEntity);
		}
		if (resourceIds == null) {
			resourceIds = "";
		}
		String[] resourceIdArray = resourceIds.split(",");
		updateRoleAuthorityFileTypeCompare(resourceIdArray, categoryEntity, roleEntity, map);
	}
	
	public void updateRoleAuthorityFileTypeCompare(String[] resourceIdArray, CategoryEntity typeEntity, RoleEntity roleEntity,
			Map<String, FileTypeRoleAuthorityEntity> map) throws BusinessException {
		List<FileTypeRoleAuthorityEntity> saveEntitys = new ArrayList<FileTypeRoleAuthorityEntity>();
		List<FileTypeRoleAuthorityEntity> deleteEntitys = new ArrayList<FileTypeRoleAuthorityEntity>();
		for (String s : resourceIdArray) {
			if ("".equals(s.trim())) {
				continue;
			}
			if (map.containsKey(s)) {
				map.remove(s);
			} else {
				FileTypeRoleAuthorityEntity saveEntity = new FileTypeRoleAuthorityEntity();
				ResourceEntity resourceEntity = new ResourceEntity();
				resourceEntity.setId(s);
				// TODO Auto-generated method stub
				//有问题
				//saveEntity.setTypeEntity(typeEntity);
				saveEntity.setRoleEntity(roleEntity);
				saveEntity.setResourceEntity(resourceEntity);
				saveEntitys.add(saveEntity);
			}
		}
		for (Entry<String, FileTypeRoleAuthorityEntity> entry : map.entrySet()) {
			deleteEntitys.add(entry.getValue());
		}
		this.categoryDao.deleteAllEntitie(deleteEntitys);
		this.categoryDao.batchSaveOrUpdate(saveEntitys);
	}

	@Override
	public List<CategoryTreeVo> queryRoleFileCategoryRoot(String roleId, String categoryId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("roleId", roleId);
		// TODO Auto-generated method stub
		param.put("typeId", categoryId);
		List<CategoryTreeVo> typeTreeList = new ArrayList<CategoryTreeVo>();
		typeTreeList = this.categoryMybatisDao.queryRoleFileCategoryRoot(param);
		return typeTreeList;
	}

	@Override
	public List<CategoryTreeVo> queryUserFileCategoryRoot(String userId, String categoryId) {
		Map<String, String> param = new HashMap<String, String>();
		// TODO Auto-generated method stub
		param.put("userId", userId);
		param.put("typeId", categoryId);
		List<CategoryTreeVo> categoryTreeList = new ArrayList<CategoryTreeVo>();
		categoryTreeList = this.categoryMybatisDao.queryUserFileCategoryRoot(param);
		return categoryTreeList;
	}

	@Override
	public String queryCategoryIdByCondition(String parentId, String sysType) {
		String hql = "FROM CategoryEntity t WHERE t.parent.id=? and t.sysType=?";
		CategoryEntity categoryEntity = categoryDao.findUniqueByHql(hql, parentId, sysType);
		if (categoryEntity != null) {
			return categoryEntity.getId();
		}
		return null;
	}

	@Override
	public String parseTreeIndexToCodePath(String treeIndex, String separator) throws BusinessException {
		String codePath = "";
		String[] array = treeIndex.split(",");
		Integer i = 0;
		try {
			for (String typeId : array) {
				if (StringUtil.isNotEmpty(typeId)) {
					CategoryEntity entity = categoryDao.getCategory(typeId);
					String code = entity.getCode();
					if (i == array.length - 1) {
						codePath += code;
					} else {
						codePath += code + separator;
					}
				}
				i++;
			}
		} catch (Exception e) {
			throw new BusinessException("转换treeIndex发生错误");
		}
		return codePath;
	}

	@Override
	public CategoryEntity getRootById(String categoryId) {
		CategoryEntity entity = categoryDao.getCategory(categoryId);
		String treeIndex = entity.getTreeIndex();
		String rootId = "";
		// 获得第一次出现分割符的地方,获得根节点Id
		if (treeIndex.indexOf(",") != -1) {
			rootId = treeIndex.substring(0, treeIndex.indexOf(","));
		} else {
			rootId = categoryId;
		}
		CategoryEntity rootEntity = categoryDao.getCategory(rootId);
		return rootEntity;
	}

	@Override
	public CategoryEntity queryCategoryEntityByCode(String code) {
		List<CategoryEntity> categoryEntity = categoryDao.findByProperty(CategoryEntity.class, "code", code);
		if (categoryEntity.size() > 0) {
			return categoryEntity.get(0);
		}
		return null;
	}

	@Override
	public List<CategoryTreeVo> queryViewableCategoryTree(Map<String, String> param) {
		return categoryMybatisDao.queryViewableCategoryTree(param);
		}

	@Override
	public void deleteFileRolesByDataId(String dataId) throws BusinessException {
		String hql = "DELETE FROM FileRoleEntity fr WHERE fr.dataEntity.id=?";
		categoryDao.executeQuery(hql, new Object[] { dataId });
	}

	@Override
	public void deleteFileRolesByDataIds(String dataIds) throws BusinessException {
		String[] dataIdArray = dataIds.split(",");
		for (String dataId : dataIdArray) {
			deleteFileRolesByDataId(dataId);
		}
	}

	
	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public void setCategoryMybatisDao(CategoryMybatisDao categoryMybatisDao) {
		this.categoryMybatisDao = categoryMybatisDao;
	}

	public void setAuthorityService(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

	@Resource
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

}
