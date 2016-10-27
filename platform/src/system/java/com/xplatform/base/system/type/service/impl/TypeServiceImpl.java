package com.xplatform.base.system.type.service.impl;

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
import com.xplatform.base.framework.core.common.service.CommonService;
import com.xplatform.base.framework.core.util.FileUtils;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.resouce.entity.ResourceEntity;
import com.xplatform.base.orgnaization.resouce.service.ResourceService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.attachment.dao.AttachDao;
import com.xplatform.base.system.attachment.entity.AttachEntity;
import com.xplatform.base.system.type.dao.TypeDao;
import com.xplatform.base.system.type.entity.FileRoleEntity;
import com.xplatform.base.system.type.entity.FileTypeOrgEntity;
import com.xplatform.base.system.type.entity.FileTypeRoleAuthorityEntity;
import com.xplatform.base.system.type.entity.FileTypeRoleEntity;
import com.xplatform.base.system.type.entity.FileTypeUserAuthorityEntity;
import com.xplatform.base.system.type.entity.FileTypeUserEntity;
import com.xplatform.base.system.type.entity.FileUserEntity;
import com.xplatform.base.system.type.entity.TypeEntity;
import com.xplatform.base.system.type.mybatis.dao.TypeMybatisDao;
import com.xplatform.base.system.type.mybatis.vo.TypeTreeVo;
import com.xplatform.base.system.type.service.TypeService;

@Service("typeService")
public class TypeServiceImpl implements TypeService {

	private static final Logger logger = Logger.getLogger(TypeServiceImpl.class);
	@Resource
	private TypeDao typeDao;
	@Resource
	private TypeMybatisDao typeMybatisDao;
	@Resource
	private AuthorityService authorityService;
	@Resource
	private ResourceService resourceService;
	@Resource
	private AttachDao attachDao;
	@Resource
	private CommonService commonService;

	/**
	 * 保存系统分类
	 * 
	 * @author luoheng
	 * @param Type
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public String saveType(TypeEntity type) throws BusinessException {
		String pk = "";
//		if (type.getParent() == null || StringUtil.isEmpty(type.getParent().getId())) {
//			TypeEntity parent = new TypeEntity();
//			parent.setId("-1");
//			type.setParent(parent);
//		}
		TypeEntity typeParent = null;
		if(type.getParent()!=null){
			typeParent=typeDao.getType(type.getParent().getId());
		}
		
		// 第一步，保存信息
		pk = this.typeDao.saveType(type);
		if (typeParent != null) {
			// 第二步，修改树的详细信息
			type.setTreeIndex(typeParent.getTreeIndex() + "," + pk);
			int level = typeParent.getLevel() == null ? 0 : typeParent.getLevel();
			type.setLevel(level + 1);
			type.setIsLeaf("1");
			this.typeDao.merge(type);
			// 第三步，更新父节点的信息
			if (typeParent.getIsLeaf() == null || typeParent.getIsLeaf().equals("1")) {// 父节点是叶子节点
//				if (typeParent.getParent() == null) {
//					TypeEntity parent = new TypeEntity();
//					parent.setId("-1");
//					typeParent.setParent(parent);
//				}
				typeParent.setIsLeaf("0");
				this.typeDao.merge(typeParent);
			}
		} else {
			type.setTreeIndex(pk);
			type.setLevel(1);
			type.setIsLeaf("1");
			this.typeDao.merge(type);
		}
		return pk;
	}

	/**
	 * 修改系统分类
	 * 
	 * @author luoheng
	 * @param Type
	 * @throws BusinessException
	 */
	@Override
	public void updateType(TypeEntity type) throws BusinessException {
		try {
			TypeEntity oldEntity = getType(type.getId());
			MyBeanUtils.copyBeanNotNull2Bean(type, oldEntity);
			if (oldEntity.getParent() == null || StringUtil.isEmpty(oldEntity.getParent().getId())) {
				TypeEntity parent = new TypeEntity();
				parent.setId("-1");
				oldEntity.setParent(parent);
			}
			this.typeDao.updateType(oldEntity);
			logger.info("系统分类更新成功");
		} catch (Exception e) {
			logger.error("系统分类更新失败");
			throw new BusinessException("系统分类更新失败");
		}
	}

	/**
	 * 删除目录(TODO:考虑删除后子目录、附件、引用处的各种问题)
	 * 
	 * @author xiaqiang
	 * @param id
	 * @throws BusinessException
	 */
	@Override
	public void delete(String id) throws BusinessException {
		try {
			TypeEntity type = getType(id);
			TypeEntity parent = null;
			// 获取父节点
			if (type != null && type.getParent() != null) {
				parent = type.getParent();
			}
			// 设置父节点是否叶子节点
			if (parent != null) {
				if (parent.getChildren() != null) {
					if (parent.getChildren().size() <= 1) {
						parent.setIsLeaf("1");
					}
				}
			}
			this.typeDao.deleteType(id);
			// 修改节点
			if (parent != null)
				this.updateType(parent);
		} catch (Exception e) {
			throw new BusinessException("文件夹删除失败");
		}
	}

	/**
	 * 批量删除系统分类
	 * 
	 * @author luoheng
	 * @param id
	 * @throws BusinessException
	 */
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

	/**
	 * 根据ID获取系统分类信息
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public TypeEntity getType(String id) {
		TypeEntity Type = null;
		if (StringUtil.isNotEmpty(id)) {
			Type = typeDao.getType(id);
			logger.info("获取系统分类信息成功");
		}
		return Type;
	}

	/**
	 * 获取所有系统分类信息
	 * 
	 * @author luoheng
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<TypeEntity> queryTypeList() throws BusinessException {
		List<TypeEntity> typeList = new ArrayList<TypeEntity>();
		try {
			typeList = typeDao.queryTypeList();
			logger.error("获取所有系统分类信息成功");
		} catch (Exception e) {
			logger.error("获取所有系统分类信息失败");
			throw new BusinessException("获取所有系统分类信息失败");
		}
		return typeList;
	}

	/**
	 * 获取系统分类分页列表
	 * 
	 * @author luoheng
	 * @param cq
	 * @param b
	 * @throws BusinessException
	 */
	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
		this.typeDao.getDataGridReturn(cq, true);
		logger.info("系统分类分页列表成功");
	}

	/**
	 * 根据属性、值查询实体
	 * 
	 * @author luoheng
	 * @param propertyName
	 * @param value
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<TypeEntity> queryListByPorperty(String propertyName, String value){
		List<TypeEntity> typeList = new ArrayList<TypeEntity>();
		typeList = this.typeDao.findByProperty(TypeEntity.class, propertyName, value);
		return typeList;
	}

	/**
	 * 根据权限查询系统类型树
	 * 
	 * @author luoheng
	 * @param propertyName
	 * @param value
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<TypeTreeVo> queryTypeTree(String userId, String parentId) throws BusinessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", userId);
		param.put("parentId", parentId);
		List<TypeTreeVo> typeTreeList = new ArrayList<TypeTreeVo>();
		try {
			typeTreeList = this.typeMybatisDao.queryTypeTree(param);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("根据权限查询系统类型树失败");
		}
		return typeTreeList;
	}

	/**
	 * 根据用户ID查询该用户可操作的系统类型
	 * 
	 * @param userId
	 * @param parentId
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<TypeTreeVo> queryUserTypeTree(String userId, String parentId) throws BusinessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", userId);
		param.put("parentId", parentId);
		List<TypeTreeVo> typeTreeList = new ArrayList<TypeTreeVo>();
		try {
			typeTreeList = this.typeMybatisDao.queryUserTypeTree(param);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("根据用户ID查询该用户可操作的系统类型失败");
		}
		return typeTreeList;
	}

	/**
	 * 根据类型、用户权限查询系统类型
	 * 
	 * @author luoheng
	 * @param param
	 *            (userId、sysType)
	 * @see com/xplatform/base/system/type/maybatis/mapper/Type.xml
	 *      queryTypeRoleTreeBySysType
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<TypeTreeVo> queryTypeRoleTreeBySysType(Map<String, String> param) throws BusinessException {
		List<TypeTreeVo> typeTreeList = new ArrayList<TypeTreeVo>();
		if (this.authorityService.isAdmin(param)) {// 不是管理员，进行权限过滤
			List<TypeEntity> typeList = this.queryListByPorperty("sysType", param.get("sysType"));
			for (TypeEntity type : typeList) {
				TypeTreeVo typeTreeVo = new TypeTreeVo();
				typeTreeVo.setId(type.getId());
				typeTreeVo.setCode(type.getCode());
				typeTreeVo.setName(type.getName());
				typeTreeVo.setSysType(type.getType());
				typeTreeVo.setRemark(type.getRemark());
				if (type.getParent() != null) {
					typeTreeVo.setParentId(type.getParent().getId());
				} else {
					typeTreeVo.setParentId("-1");
				}
				typeTreeVo.setIsLeaf(type.getIsLeaf());
				typeTreeVo.setLevel(type.getLevel());
				typeTreeVo.setIconCls(type.getIconCls());
				typeTreeList.add(typeTreeVo);
			}
		} else {
			try {
				typeTreeList = this.typeMybatisDao.queryTypeRoleTreeBySysTypeList(param);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException("根据类型、用户权限查询系统类型失败");
			}
		}
		return typeTreeList;
	}

	/**
	 * 根据类型、用户权限查询系统类型（数据返回Tree List）
	 * 
	 * @author luoheng
	 * @param param
	 *            (userId、sysType、parentId)
	 * @see com/xplatform/base/system/type/maybatis/mapper/Type.xml
	 *      queryTypeRoleTreeBySysType
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<TypeTreeVo> queryTypeRoleTreeBySysTypeTree(Map<String, String> param) throws BusinessException {
		if (param.get("parentId").isEmpty()) {
			param.put("parentId", "-1");
		}
		List<TypeTreeVo> typeTreeList = new ArrayList<TypeTreeVo>();
		if (this.authorityService.isAdmin(param)) {// 不是管理员，进行权限过滤
			List<TypeEntity> typeList = this.queryListByPorperty("parent.id", param.get("parentId"));
			for (TypeEntity type : typeList) {
				if (type.getType().equals(param.get("sysType"))) {
					TypeTreeVo typeTreeVo = new TypeTreeVo();
					typeTreeVo.setId(type.getId());
					typeTreeVo.setCode(type.getCode());
					typeTreeVo.setName(type.getName());
					typeTreeVo.setSysType(type.getType());
					typeTreeVo.setRemark(type.getRemark());
					if (type.getParent() != null) {
						typeTreeVo.setParentId(type.getParent().getId());
					} else {
						typeTreeVo.setParentId("-1");
					}
					typeTreeVo.setIsLeaf(type.getIsLeaf());
					typeTreeVo.setLevel(type.getLevel());
					typeTreeVo.setIconCls(type.getIconCls());
					typeTreeList.add(typeTreeVo);
				}
			}
		} else {
			try {
				typeTreeList = this.typeMybatisDao.queryTypeRoleTreeBySysTypeTree(param);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException("根据类型、用户权限查询系统类型失败");
			}
		}
		return typeTreeList;
	}

	/**
	 * 根据用户权限分页查询系统类型
	 * 
	 * @author luoheng
	 * @param userId
	 * @param parentId
	 * @param dataGrid
	 * @return
	 */
	public Page<TypeTreeVo> findByPage(Map<String, String> param, DataGrid dataGrid) {
		Page<TypeTreeVo> page = new Page<TypeTreeVo>();
		List<TypeTreeVo> result = typeMybatisDao.findByPage(param);
		page.setResult(result);
		page.setPageSize(dataGrid.getRows());
		page.setPageNo(dataGrid.getPage());
		return page;
	}

	@Override
	public String queryTypeIdByCode(String code) {
		List<TypeEntity> typeEntity = typeDao.findByProperty(TypeEntity.class, "code", code);
		if (typeEntity.size() > 0) {
			return typeEntity.get(0).getId();
		}
		return null;
	}

	@Override
	public String queryTypeIdByCondition(String parentId, String sysType) {
		String hql = "FROM TypeEntity t WHERE t.parent.id=? and t.sysType=?";
		TypeEntity typeEntity = typeDao.findUniqueByHql(hql, parentId, sysType);
		if (typeEntity != null) {
			return typeEntity.getId();
		}
		return null;
	}

	@Override
	public List<TypeTreeVo> queryUserFileTypeTree(String userId, String parentId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", userId);
		param.put("parentId", parentId);
		List<TypeTreeVo> typeTreeList = new ArrayList<TypeTreeVo>();
		typeTreeList = this.typeMybatisDao.queryUserFileTypeTree(param);
		return typeTreeList;
	}

	@Override
	public void updateUserFileType(String checkedTypeIds, String pIds, String userId) throws BusinessException {
		Map<String, FileTypeUserEntity> map = new HashMap<String, FileTypeUserEntity>();
		// key存放本次选中以及未选中的父id(混在一起),value存放是否选中
		Map<String, String> idMap = new HashMap<String, String>();
		UserEntity userEntity = this.typeDao.get(UserEntity.class, userId);

		List<FileTypeUserEntity> fileTypeUserEntityList = this.typeDao.findByProperty(FileTypeUserEntity.class, "userEntity.id", userId);
		for (FileTypeUserEntity fileTypeUserEntity : fileTypeUserEntityList) {
			map.put(fileTypeUserEntity.getTypeEntity().getId(), fileTypeUserEntity);
		}
		if (StringUtil.isNotEmpty(checkedTypeIds)) {
			// 选中的ids
			String[] checkedTypeIdsArray = checkedTypeIds.split(",");
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
		updateUserFileTypeCompare(idMap, userEntity, map);
	}

	private void updateUserFileTypeCompare(Map<String, String> idMap, UserEntity userEntity, Map<String, FileTypeUserEntity> map) throws BusinessException {
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
				TypeEntity typeEntity = new TypeEntity();
				typeEntity.setId(id);
				saveEntity.setUserEntity(userEntity);
				saveEntity.setTypeEntity(typeEntity);
				saveEntity.setIsManage(isManage);
				saveEntitys.add(saveEntity);
			}
		}
		for (Entry<String, FileTypeUserEntity> entry : map.entrySet()) {
			deleteEntitys.add(entry.getValue());
		}
		try {
			this.typeDao.batchSaveOrUpdate(saveEntitys);
			this.typeDao.deleteAllEntitie(deleteEntitys);
			// deleteEntitys 删除资料文件夹权限，同时删除操作按钮权限
			String hql = "delete from t_sys_filetype_user_authority where user_entity=? and type_entity=?";
			for (FileTypeUserEntity deleteEntity : deleteEntitys) {
				typeDao.executeSql(hql, new Object[] { deleteEntity.getUserEntity().getId(), deleteEntity.getTypeEntity().getId() });
			}
		} catch (Exception e) {
			throw new BusinessException("保存资料文件夹权限失败");
		}
	}

	@Override
	public void updateUserFileTypeAuthority(String resourceIds, String typeId, String userId) throws BusinessException {
		Map<String, FileTypeUserAuthorityEntity> map = new HashMap<String, FileTypeUserAuthorityEntity>();
		UserEntity userEntity = this.typeDao.get(UserEntity.class, userId);
		TypeEntity typeEntity = this.typeDao.get(TypeEntity.class, typeId);
		// 查找之前的权限记录
		List<FileTypeUserAuthorityEntity> fileTypeUserAuthorityEntityList = this.resourceService.queryUserFileTypeAuthority(typeId, userId);
		for (FileTypeUserAuthorityEntity fileTypeUserAuthorityEntity : fileTypeUserAuthorityEntityList) {
			map.put(fileTypeUserAuthorityEntity.getResourceEntity().getId(), fileTypeUserAuthorityEntity);
		}
		if (resourceIds == null) {
			resourceIds = "";
		}
		String[] resourceIdArray = resourceIds.split(",");
		updateUserAuthorityFileTypeCompare(resourceIdArray, typeEntity, userEntity, map);
	}

	public void updateUserAuthorityFileTypeCompare(String[] resourceIdArray, TypeEntity typeEntity, UserEntity userEntity,
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
				saveEntity.setTypeEntity(typeEntity);
				saveEntity.setUserEntity(userEntity);
				saveEntity.setResourceEntity(resourceEntity);
				saveEntitys.add(saveEntity);
			}
		}
		for (Entry<String, FileTypeUserAuthorityEntity> entry : map.entrySet()) {
			deleteEntitys.add(entry.getValue());
		}
		this.typeDao.deleteAllEntitie(deleteEntitys);
		this.typeDao.batchSaveOrUpdate(saveEntitys);
	}

	@Override
	public List<TypeTreeVo> queryRoleFileTypeTree(String roleId, String parentId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("roleId", roleId);
		param.put("parentId", parentId);
		List<TypeTreeVo> typeTreeList = new ArrayList<TypeTreeVo>();
		typeTreeList = this.typeMybatisDao.queryRoleFileTypeTree(param);
		return typeTreeList;
	}

	@Override
	public List<TypeTreeVo> queryUserFileTypeRoot(String userId, String typeId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", userId);
		param.put("typeId", typeId);
		List<TypeTreeVo> typeTreeList = new ArrayList<TypeTreeVo>();
		typeTreeList = this.typeMybatisDao.queryUserFileTypeRoot(param);
		return typeTreeList;
	}

	@Override
	public List<TypeTreeVo> queryRoleFileTypeRoot(String roleId, String typeId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("roleId", roleId);
		param.put("typeId", typeId);
		List<TypeTreeVo> typeTreeList = new ArrayList<TypeTreeVo>();
		typeTreeList = this.typeMybatisDao.queryRoleFileTypeRoot(param);
		return typeTreeList;
	}

	@Override
	public void updateRoleFileType(String checkedTypeIds, String pIds, String roleId) throws BusinessException {
		Map<String, FileTypeRoleEntity> map = new HashMap<String, FileTypeRoleEntity>();
		// key存放本次选中以及未选中的父id(混在一起),value存放是否选中
		Map<String, String> idMap = new HashMap<String, String>();
		RoleEntity roleEntity = this.typeDao.get(RoleEntity.class, roleId);

		List<FileTypeRoleEntity> fileTypeRoleEntityList = this.typeDao.findByProperty(FileTypeRoleEntity.class, "roleEntity.id", roleId);
		for (FileTypeRoleEntity fileTypeRoleEntity : fileTypeRoleEntityList) {
			map.put(fileTypeRoleEntity.getTypeEntity().getId(), fileTypeRoleEntity);
		}
		if (StringUtil.isNotEmpty(checkedTypeIds)) {
			// 选中的ids
			String[] checkedTypeIdsArray = checkedTypeIds.split(",");
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
				TypeEntity typeEntity = new TypeEntity();
				typeEntity.setId(id);
				saveEntity.setRoleEntity(roleEntity);
				saveEntity.setTypeEntity(typeEntity);
				saveEntity.setIsManage(isManage);
				saveEntitys.add(saveEntity);
			}
		}
		for (Entry<String, FileTypeRoleEntity> entry : map.entrySet()) {
			deleteEntitys.add(entry.getValue());
		}
		try {
			this.typeDao.batchSaveOrUpdate(saveEntitys);
			this.typeDao.deleteAllEntitie(deleteEntitys);
			// deleteEntitys 删除资料文件夹权限，同时删除操作按钮权限
			String sql = "delete from t_sys_filetype_role_authority where role_entity=? and type_entity=?";
			for (FileTypeRoleEntity deleteEntity : deleteEntitys) {
				typeDao.executeSql(sql, new Object[] { deleteEntity.getRoleEntity().getId(), deleteEntity.getTypeEntity().getId() });
			}
		} catch (Exception e) {
			throw new BusinessException("保存资料文件夹权限失败");
		}
	}

	@Override
	public void updateRoleFileTypeAuthority(String resourceIds, String typeId, String roleId) throws BusinessException {
		Map<String, FileTypeRoleAuthorityEntity> map = new HashMap<String, FileTypeRoleAuthorityEntity>();
		RoleEntity roleEntity = this.typeDao.get(RoleEntity.class, roleId);
		TypeEntity typeEntity = this.typeDao.get(TypeEntity.class, typeId);
		// 查找之前的权限记录
		List<FileTypeRoleAuthorityEntity> fileTypeRoleAuthorityEntityList = this.resourceService.queryRoleFileTypeAuthority(typeId, roleId);
		for (FileTypeRoleAuthorityEntity fileTypeRoleAuthorityEntity : fileTypeRoleAuthorityEntityList) {
			map.put(fileTypeRoleAuthorityEntity.getResourceEntity().getId(), fileTypeRoleAuthorityEntity);
		}
		if (resourceIds == null) {
			resourceIds = "";
		}
		String[] resourceIdArray = resourceIds.split(",");
		updateRoleAuthorityFileTypeCompare(resourceIdArray, typeEntity, roleEntity, map);
	}

	public void updateRoleAuthorityFileTypeCompare(String[] resourceIdArray, TypeEntity typeEntity, RoleEntity roleEntity,
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
				saveEntity.setTypeEntity(typeEntity);
				saveEntity.setRoleEntity(roleEntity);
				saveEntity.setResourceEntity(resourceEntity);
				saveEntitys.add(saveEntity);
			}
		}
		for (Entry<String, FileTypeRoleAuthorityEntity> entry : map.entrySet()) {
			deleteEntitys.add(entry.getValue());
		}
		this.typeDao.deleteAllEntitie(deleteEntitys);
		this.typeDao.batchSaveOrUpdate(saveEntitys);
	}

	@Override
	public String parseTreeIndexToCodePath(String treeIndex, String separator) throws BusinessException {
		String codePath = "";
		String[] array = treeIndex.split(",");
		Integer i = 0;
		try {
			for (String typeId : array) {
				if (StringUtil.isNotEmpty(typeId)) {
					TypeEntity entity = typeDao.getType(typeId);
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
	public TypeEntity getRootById(String typeId) {
		TypeEntity entity = typeDao.getType(typeId);
		String treeIndex = entity.getTreeIndex();
		String rootId = "";
		// 获得第一次出现分割符的地方,获得根节点Id
		if (treeIndex.indexOf(",") != -1) {
			rootId = treeIndex.substring(0, treeIndex.indexOf(","));
		} else {
			rootId = typeId;
		}
		TypeEntity rootEntity = typeDao.getType(rootId);
		return rootEntity;
	}

	@Override
	public TypeEntity queryTypeEntityByCode(String code) {
		List<TypeEntity> typeEntity = typeDao.findByProperty(TypeEntity.class, "code", code);
		if (typeEntity.size() > 0) {
			return typeEntity.get(0);
		}
		return null;
	}

	@Override
	public List<TypeTreeVo> queryViewableTypeTree(Map<String, String> param) {
		return typeMybatisDao.queryViewableTypeTree(param);
	}

	@Override
	public void deleteFileRolesByAttachId(String attachId) throws BusinessException {
		String hql = "DELETE FROM FileRoleEntity fr WHERE fr.attachEntity.id=?";
		typeDao.executeQuery(hql, new Object[] { attachId });
	}

	@Override
	public void deleteFileRolesByAttachIds(String attachIds) throws BusinessException {
		String[] attachIdArray = attachIds.split(",");
		for (String attachId : attachIdArray) {
			deleteFileRolesByAttachId(attachId);
		}
	}

	@Override
	public void batchUpdateFileRoles(String attachIds, String roleIds) throws BusinessException {
		String[] attachArray = attachIds.split(",");
		for (String attachId : attachArray) {
			updateFileRoles(attachId, roleIds);
		}
	}

	private void updateFileRoles(String attachId, String roleIds) throws BusinessException {
		Map<String, FileRoleEntity> map = new HashMap<String, FileRoleEntity>();
		AttachEntity attach = attachDao.get(attachId);
		// 1.得到文件角色权限
		List<FileRoleEntity> frList = typeDao.findByProperty(FileRoleEntity.class, "attachEntity.id", attachId);
		// 2.转化为map储存
		for (FileRoleEntity fr : frList) {
			map.put(fr.getRoleEntity().getId(), fr);
		}
		// 3.执行对比方法,得出增改的记录
		String[] roleIdsArray = roleIds.split(",");
		updateFileRoleCompare(roleIdsArray, attach, map);
	}

	private void updateFileRoleCompare(String[] roleIdsArray, AttachEntity attach, Map<String, FileRoleEntity> map) throws BusinessException {
		List<FileRoleEntity> saveEntitys = new ArrayList<FileRoleEntity>();
		List<FileRoleEntity> deleteEntitys = new ArrayList<FileRoleEntity>();
		for (String s : roleIdsArray) {
			if ("".equals(s.trim())) {
				continue;
			}
			if (map.containsKey(s)) {
				map.remove(s);
			} else {
				FileRoleEntity saveEntity = new FileRoleEntity();
				RoleEntity roleEntity = new RoleEntity();
				roleEntity.setId(s);
				saveEntity.setRoleEntity(roleEntity);
				saveEntity.setAttachEntity(attach);
				saveEntitys.add(saveEntity);
			}
		}
		for (Entry<String, FileRoleEntity> entry : map.entrySet()) {
			deleteEntitys.add(entry.getValue());
		}
		this.typeDao.deleteAllEntitie(deleteEntitys);
		this.typeDao.batchSaveOrUpdate(saveEntitys);
	}
	
	public void saveFileUsers(String attachIds, String userIds) throws BusinessException {
		List<FileUserEntity> fuList = new ArrayList<FileUserEntity>();
		for (String attachId : attachIds.split(",")) {
			AttachEntity attach = new AttachEntity();
			attach.setId(attachId);
			for (String userId : userIds.split(",")) {
				UserEntity user = new UserEntity();
				user.setId(userId);
				FileUserEntity fu = new FileUserEntity();
				fu.setUserEntity(user);
				fu.setAttachEntity(attach);
				fuList.add(fu);
			}
		}
		this.typeDao.batchSave(fuList);
	}
	
	public void deleteFileUsers(String attachId, String userId) throws BusinessException {
		String hql = "DELETE FROM FileUserEntity fu WHERE fu.attachEntity.id=? AND fu.userEntity.id=?";
		typeDao.executeQuery(hql, new Object[] { attachId, userId });
	}

	@Override
	public TypeEntity queryCurrentOrgRootType(String orgId) {
		String hql = "FROM TypeEntity t WHERE t.type=? AND t.code=? AND t.level=?";
		TypeEntity typeEntity = typeDao.findUniqueByHql(hql, BusinessConst.menuType_CODE_org, orgId, 1);
		return typeEntity;
	}
	
	@Override
	public List<TypeEntity> queryTypeListByLikeCode(String code) {
		String hql = "FROM TypeEntity t WHERE t.code like ?";
		List<TypeEntity> list = typeDao.findHql(hql, code + "%");
		return list;
	}
	
	@Override
	public String getUnRepeatCode(String code) {
		List<TypeEntity> typeList = queryTypeListByLikeCode(code);
		if (typeList.size() > 0) {
			// 说明有重复的数据
			int n = typeList.size();
			code = code + n + 1;
		}
		return code;
	}
	
	@Override
	public void updateName(String name, String id) throws BusinessException {
		String hql = "UPDATE FROM TypeEntity SET name=? WHERE id=?";
		attachDao.executeHql(hql,name,id);
	}
	
	@Override
	
	public void updateFileTypeAuthority(String typeIds, String finalValue) throws BusinessException {
		typeIds = StringUtil.removeDot(typeIds);// 文件夹Ids
		List<Map> finalValueList=JSONHelper.toList(finalValue, Map.class);
		
		String userIds = "";
		String orgIds = "";
		for (Map item : finalValueList) {
			String type = item.get("type").toString();
			String id = item.get("id").toString();
			String name = item.get("name").toString();
			if ("user".equals(type)) {
				userIds += id + ",";
			} else if ("org".equals(type)) {
				orgIds += id + ",";
			}
		}
		batchUpdateFileTypeUsers(typeIds, StringUtil.removeDot(userIds));
		batchUpdateFileTypeOrgs(typeIds, StringUtil.removeDot(orgIds));
	}
	
	@Override
	public void batchUpdateFileTypeOrgs(String typeIds, String orgIds) throws BusinessException {
		String[] typeArray = typeIds.split(",");
		for (String typeId : typeArray) {
			updateFileTypeOrgs(typeId, orgIds);
		}
	}

	private void updateFileTypeOrgs(String typeId, String orgIds) throws BusinessException {
		Map<String, FileTypeOrgEntity> map = new HashMap<String, FileTypeOrgEntity>();
		TypeEntity type = typeDao.get(TypeEntity.class,typeId);
		// 1.得到目录机构权限
		List<FileTypeOrgEntity> foList = typeDao.findByProperty(FileTypeOrgEntity.class, "typeEntity.id", typeId);
		// 2.转化为map储存
		for (FileTypeOrgEntity fo : foList) {
			map.put(fo.getOrgnaizationEntity().getId(), fo);
		}
		// 3.执行对比方法,得出增改的记录
		String[] orgIdsArray = orgIds.split(",");
		updateFileTypeOrgCompare(orgIdsArray, type, map);
	}

	private void updateFileTypeOrgCompare(String[] orgIdsArray, TypeEntity type, Map<String, FileTypeOrgEntity> map) throws BusinessException {
		List<FileTypeOrgEntity> saveEntitys = new ArrayList<FileTypeOrgEntity>();
		List<FileTypeOrgEntity> deleteEntitys = new ArrayList<FileTypeOrgEntity>();
		for (String s : orgIdsArray) {
			if ("".equals(s.trim())) {
				continue;
			}
			if (map.containsKey(s)) {
				map.remove(s);
			} else {
				FileTypeOrgEntity saveEntity = new FileTypeOrgEntity();
				OrgnaizationEntity orgnaizationEntity = new OrgnaizationEntity();
				orgnaizationEntity.setId(s);
				saveEntity.setOrgnaizationEntity(orgnaizationEntity);
				saveEntity.setTypeEntity(type);
				saveEntitys.add(saveEntity);
			}
		}
		for (Entry<String, FileTypeOrgEntity> entry : map.entrySet()) {
			deleteEntitys.add(entry.getValue());
		}
		this.typeDao.deleteAllEntitie(deleteEntitys);
		this.typeDao.batchSaveOrUpdate(saveEntitys);
	}
	
	@Override
	public void batchUpdateFileTypeUsers(String typeIds, String userIds) throws BusinessException {
		String[] typeArray = typeIds.split(",");
		for (String typeId : typeArray) {
			updateFileTypeUsers(typeId, userIds);
		}
	}

	private void updateFileTypeUsers(String typeId, String userIds) throws BusinessException {
		Map<String, FileTypeUserEntity> map = new HashMap<String, FileTypeUserEntity>();
		TypeEntity type = typeDao.get(TypeEntity.class,typeId);
		// 1.得到目录机构权限
		List<FileTypeUserEntity> foList = typeDao.findByProperty(FileTypeUserEntity.class, "typeEntity.id", typeId);
		// 2.转化为map储存
		for (FileTypeUserEntity fo : foList) {
			map.put(fo.getUserEntity().getId(), fo);
		}
		// 3.执行对比方法,得出增改的记录
		String[] userIdsArray = userIds.split(",");
		updateFileTypeUserCompare(userIdsArray, type, map);
	}

	private void updateFileTypeUserCompare(String[] userIdsArray, TypeEntity type, Map<String, FileTypeUserEntity> map) throws BusinessException {
		List<FileTypeUserEntity> saveEntitys = new ArrayList<FileTypeUserEntity>();
		List<FileTypeUserEntity> deleteEntitys = new ArrayList<FileTypeUserEntity>();
		for (String s : userIdsArray) {
			if ("".equals(s.trim())) {
				continue;
			}
			if (map.containsKey(s)) {
				map.remove(s);
			} else {
				FileTypeUserEntity saveEntity = new FileTypeUserEntity();
				UserEntity userEntity = new UserEntity();
				userEntity.setId(s);
				saveEntity.setUserEntity(userEntity);
				saveEntity.setTypeEntity(type);
				saveEntitys.add(saveEntity);
			}
		}
		for (Entry<String, FileTypeUserEntity> entry : map.entrySet()) {
			deleteEntitys.add(entry.getValue());
		}
		this.typeDao.deleteAllEntitie(deleteEntitys);
		this.typeDao.batchSaveOrUpdate(saveEntitys);
	}
	
	@Override
	public List<Map<String, Object>> queryOrgTypeAuthority(String typeId) throws BusinessException {
		List<FileTypeUserEntity> userAuthoritys = commonService.findByProperty(FileTypeUserEntity.class, "typeEntity.id", typeId);
		List<FileTypeOrgEntity> orgAuthoritys = commonService.findByProperty(FileTypeOrgEntity.class, "typeEntity.id", typeId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (FileTypeOrgEntity orgAuthority : orgAuthoritys) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", orgAuthority.getOrgnaizationEntity().getId());
			map.put("name", orgAuthority.getOrgnaizationEntity().getName());
			map.put("type", BusinessConst.mulSelect_org);
			list.add(map);
		}
		for (FileTypeUserEntity userAuthority : userAuthoritys) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", userAuthority.getUserEntity().getId());
			map.put("name", userAuthority.getUserEntity().getName());
			map.put("type", BusinessConst.mulSelect_user);
			list.add(map);
		}
		return list;
	}
	
	@Override
	public String initPersonalType(String userId) throws BusinessException {
		TypeEntity rootType = new TypeEntity();
		rootType.setParent(null);
		rootType.setName("个人");
		rootType.setCode(userId);
		rootType.setType(BusinessConst.menuType_CODE_personal);
		rootType.setIsPublic(0);
		rootType.setCreateUserId(BusinessConst.SysParam_user);
		rootType.setCreateUserName(BusinessConst.SysParam_user);
		rootType.setUpdateUserId(BusinessConst.SysParam_user);
		rootType.setUpdateUserName(BusinessConst.SysParam_user);
		return saveType(rootType);
	}
	
	@Override
	public String initOrgType(String orgId) throws BusinessException {
		TypeEntity rootType = new TypeEntity();
		rootType.setParent(null);
		rootType.setName(orgId);
		rootType.setCode(orgId);
		rootType.setOrgId(orgId);
		rootType.setType(BusinessConst.menuType_CODE_org);
		rootType.setIsPublic(0);
		rootType.setCreateUserId(BusinessConst.SysParam_user);
		rootType.setCreateUserName(BusinessConst.SysParam_user);
		rootType.setUpdateUserId(BusinessConst.SysParam_user);
		rootType.setUpdateUserName(BusinessConst.SysParam_user);
		saveType(rootType);

		TypeEntity publicType = new TypeEntity();
		publicType.setParent(rootType);
		publicType.setName("公共文件夹");
		publicType.setCode(orgId);
		publicType.setOrgId(orgId);
		publicType.setType(BusinessConst.menuType_CODE_org);
		publicType.setIsPublic(1);
		publicType.setCreateUserId(BusinessConst.SysParam_user);
		publicType.setCreateUserName(BusinessConst.SysParam_user);
		publicType.setUpdateUserId(BusinessConst.SysParam_user);
		publicType.setUpdateUserName(BusinessConst.SysParam_user);
		saveType(publicType);
		return rootType.getId();
	}
	
	@Override
	public String doQueryPersonalRootType(String userId) throws BusinessException {
		String hql = "FROM TypeEntity WHERE code=? AND type=?";
		TypeEntity type = typeDao.findUniqueByHql(hql, userId, BusinessConst.menuType_CODE_personal);
		String typeId = null;
		// 如果当前用户无个人文件夹 ,则自动创建个人根文件夹(为了兼容老用户逻辑)
		if (type == null) {
			typeId = initPersonalType(userId);
		} else {
			typeId = type.getId();
		}
		return typeId;
	}
	
	@Override
	public String queryOrgRootType(String orgId) {
		String hql = "FROM TypeEntity WHERE level=? AND code=? AND type=?";
		TypeEntity type = typeDao.findUniqueByHql(hql, 1, orgId, BusinessConst.menuType_CODE_org);
		if (type != null) {
			return type.getId();
		} else {
			return null;
		}
	}
	
	@Override
	public void deleteByIds(String ids) throws BusinessException {
		commonService.deleteByIds(TypeEntity.class, ids);
	}
	
	@Override
	public Long querySameNameType(String typeId, String name) {
		String sql = "select COUNT(*) from t_sys_type where parentId='" + typeId + "'  AND name = '" + name + "'";
		return attachDao.getCountForJdbc(sql);
	}
	
	@Override
	public Long querySerialNameType(String typeId, String name) {
		String sql = "select COUNT(*) from t_sys_type where parentId='" + typeId + "'  AND name REGEXP '^" + name
				+ "(\\\\([0-9]+\\\\))?$'";
		return attachDao.getCountForJdbc(sql);
	}
	
	@Override
	public String getUnrepeatName(String typeId, String name) {
		Long count1 = querySameNameType(typeId, name);
		if (count1 > 0) {
			Long count2 = querySerialNameType(typeId, name);
			return new StringBuffer(name).append("(").append(count2).append(")").toString();
		}
		return name;
	}
	
	@Override
	public void updateTypeName(String name, String id) throws BusinessException {
		String hql = "UPDATE FROM TypeEntity SET name=? WHERE id=?";
		attachDao.executeHql(hql, name , id);
//		String sql = "UPDATE FROM t_sys_type SET name=? WHERE id=?";
//		typeDao.executeSql(sql, name, id);
	}
}
