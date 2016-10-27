package com.xplatform.base.orgnaization.resouce.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.orgnaization.module.entity.ModuleEntity;
import com.xplatform.base.orgnaization.resouce.dao.ResourceDao;
import com.xplatform.base.orgnaization.resouce.entity.ResourceEntity;
import com.xplatform.base.orgnaization.resouce.service.ResourceService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.role.entity.RoleResourceEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.system.type.entity.FileTypeRoleAuthorityEntity;
import com.xplatform.base.system.type.entity.FileTypeUserAuthorityEntity;

/**
 * 
 * description :资源service实现
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
@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {
	private static final Logger logger = Logger.getLogger(ResourceServiceImpl.class);
	@Resource
	private ResourceDao resourceDao;

	@Resource
	private BaseService baseService;

	@Override
	public String save(ResourceEntity resource) throws BusinessException {
		// TODO Auto-generated method stub
		String pk = "";
		try {
			pk = this.resourceDao.addResource(resource);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("资源保存失败");
			throw new BusinessException("资源保存失败");

		}
		logger.info("资源保存成功");
		return pk;
	}

	@Override
	public void delete(String id) throws BusinessException {
		// TODO Auto-generated method stub
		try {
			this.resourceDao.deleteResource(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("资源删除失败");
			throw new BusinessException("资源删除失败");
		}
		logger.info("资源删除成功");
	}

	@Override
	public void batchDelete(String ids) throws BusinessException {
		// TODO Auto-generated method stub
		if (StringUtil.isNotBlank(ids)) {
			String[] idArr = StringUtil.split(ids, ",");
			for (String id : idArr) {
				this.delete(id);
			}
		}
		logger.info("资源批量删除成功");
	}

	@Override
	public void update(ResourceEntity resource) throws BusinessException {
		// TODO Auto-generated method stub
		try {
			ResourceEntity oldEntity = get(resource.getId());
			MyBeanUtils.copyBeanNotNull2Bean(resource, oldEntity);
			this.resourceDao.merge(oldEntity);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("资源更新失败");
			throw new BusinessException("资源更新失败");

		}
		logger.info("资源更新成功");
	}

	@Override
	public ResourceEntity get(String id) {
		// TODO Auto-generated method stub
		ResourceEntity Resource = null;
		try {
			Resource = this.resourceDao.getResource(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("资源获取失败");
		}
		logger.info("资源获取成功");
		return Resource;
	}
	
	@Override
	public List<ResourceEntity> findByPropertys(Map<String,String> param) {
		// TODO Auto-generated method stub
		List<ResourceEntity> list= null;
		try {
			list = this.resourceDao.findByPropertys(ResourceEntity.class, param);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("资源获取失败");
		}
		logger.info("资源获取成功");
		return list;
	}

	@Override
	public List<ResourceEntity> queryList() {
		// TODO Auto-generated method stub
		List<ResourceEntity> ResourceList = new ArrayList<ResourceEntity>();
		try {
			ResourceList = this.resourceDao.queryResourceList();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("资源获取列表失败");
		}
		logger.info("资源获取列表成功");
		return ResourceList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		try {
			this.resourceDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("资源获取分页列表失败");
		}
		logger.info("资源获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param, String propertyName) {
		logger.info(propertyName + "字段唯一校验");
		String newValue = param.get("newValue");
		String oldValue = param.get("oldValue");
		if (newValue == null || StringUtil.equals(newValue, oldValue)) {//修改同一条记录
			return true;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("filterType", "common");
		map.put("url", newValue);
		List<ResourceEntity> list = this.resourceDao.findByPropertys(ResourceEntity.class, map);
		if (list != null && list.size() > 0) {//数据库记录不唯一，返回false
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<ResourceEntity> queryListByModuleId(String moduleId) {
		// TODO Auto-generated method stub
		List<ResourceEntity> resourceList = this.resourceDao.findHql(
				"from ResourceEntity r where r.module.id=? and r.isInterceptor=? and r.filterType=?", moduleId, "Y",
				"module");
		return resourceList;
	}

	@Override
	public List<RoleResourceEntity> queryResourceAuthorityByRole(String roleId, String moduleId) {
		// TODO Auto-generated method stub
		List<RoleResourceEntity> resourceAuthorityList = this.resourceDao.findHql(
				"from RoleResourceEntity rr where rr.role.id=? and rr.module.id=?", roleId, moduleId);
		return resourceAuthorityList;
	}

	/*@Override
	public List<UserResourceEntity> queryResourceAuthorityByUser(String userId, String moduleId) {
		// TODO Auto-generated method stub
		List<UserResourceEntity> resourceAuthorityList = this.resourceDao.findHql(
				"from UserResourceEntity rr where rr.user.id=? and rr.module.id=? ", userId, moduleId);
		return resourceAuthorityList;
	}*/

	@Override
	public void updateRoleResourceAuthority(String moduleId, String roleId, String resourceIds) {
		// TODO Auto-generated method stub
		RoleEntity role = this.resourceDao.get(RoleEntity.class, roleId);
		ModuleEntity module = this.resourceDao.get(ModuleEntity.class, moduleId);
		//构造当前角色下的资源权限
		List<RoleResourceEntity> list = this.resourceDao.findHql(
				"from RoleResourceEntity rm where rm.role.id=? and rm.module.id=?", roleId, moduleId);
		Map<String, RoleResourceEntity> map = new HashMap<String, RoleResourceEntity>();
		if (list != null) {
			for (RoleResourceEntity roleResourceEntity : list) {
				map.put(roleResourceEntity.getResource().getId(), roleResourceEntity);
			}
		}
		//选择的模块权限
		Set<String> set = new HashSet<String>();
		if (StringUtils.isNotEmpty(resourceIds)) {
			String[] resourceArr = resourceIds.split(",");
			for (String s : resourceArr) {
				set.add(s);
			}
		}
		//模块权限比较更新
		updateRoleResourceCompare(set, role, module, map);
	}

	/**
	 * 权限比较
	 * @param set 最新的权限列表
	 * @param role 当前角色
	 * @param map 旧的权限列表
	 * @param entitys 最后保存的权限列表
	 */
	private void updateRoleResourceCompare(Set<String> set, RoleEntity role, ModuleEntity module,
			Map<String, RoleResourceEntity> map) {
		List<RoleResourceEntity> saveEntitys = new ArrayList<RoleResourceEntity>();
		List<RoleResourceEntity> deleteEntitys = new ArrayList<RoleResourceEntity>();
		//构造插入数据，排除删除数据
		for (String s : set) {
			if (map.containsKey(s)) {
				map.remove(s);
			} else {
				RoleResourceEntity rm = new RoleResourceEntity();
				ResourceEntity resource = this.resourceDao.get(ResourceEntity.class, s);
				rm.setResource(resource);
				rm.setModule(module);
				rm.setRole(role);
				saveEntitys.add(rm);
			}
		}
		//构造删除数据
		for (Entry<String, RoleResourceEntity> entry : map.entrySet()) {
			deleteEntitys.add(entry.getValue());
		}
		this.resourceDao.batchSave(saveEntitys);
		this.resourceDao.deleteAllEntitie(deleteEntitys);
	}

	/*@Override
	public void updateUserResourceAuthority(String moduleId, String userId, String resourceIds) {
		// TODO Auto-generated method stub
		UserEntity user = this.resourceDao.get(UserEntity.class, userId);
		ModuleEntity module = this.resourceDao.get(ModuleEntity.class, moduleId);
		//构造当前角色下的资源权限
		List<UserResourceEntity> list = this.resourceDao.findHql(
				"from UserResourceEntity rm where rm.user.id=? and rm.module.id=?", userId, moduleId);
		Map<String, UserResourceEntity> map = new HashMap<String, UserResourceEntity>();
		if (list != null) {
			for (UserResourceEntity userResourceEntity : list) {
				map.put(userResourceEntity.getResource().getId(), userResourceEntity);
			}
		}
		//选择的模块权限
		Set<String> set = new HashSet<String>();
		if (StringUtils.isNotEmpty(resourceIds)) {
			String[] resourceArr = resourceIds.split(",");
			for (String s : resourceArr) {
				set.add(s);
			}
		}
		//模块权限比较更新
		updateUserResourceCompare(set, user, module, map);
	}

	*//**
	 * 权限比较
	 * @param set 最新的权限列表
	 * @param role 当前角色
	 * @param map 旧的权限列表
	 * @param entitys 最后保存的权限列表
	 *//*
	private void updateUserResourceCompare(Set<String> set, UserEntity user, ModuleEntity module,
			Map<String, UserResourceEntity> map) {
		List<UserResourceEntity> saveEntitys = new ArrayList<UserResourceEntity>();
		List<UserResourceEntity> deleteEntitys = new ArrayList<UserResourceEntity>();
		//构造插入数据，排除删除数据
		for (String s : set) {
			if (map.containsKey(s)) {
				map.remove(s);
			} else {
				UserResourceEntity rm = new UserResourceEntity();
				ResourceEntity resource = this.resourceDao.get(ResourceEntity.class, s);
				rm.setResource(resource);
				rm.setModule(module);
				rm.setUser(user);
				saveEntitys.add(rm);
			}
		}
		//构造删除数据
		for (Entry<String, UserResourceEntity> entry : map.entrySet()) {
			List<UserResourceEntity> list = this.resourceDao.findHql(
					"from UserResourceEntity rm where rm.user.id=? and rm.resource.id=?", user.getId(), entry.getKey());
			deleteEntitys.addAll(list);
		}
		this.resourceDao.batchSave(saveEntitys);
		this.resourceDao.deleteAllEntitie(deleteEntitys);
	}*/

	@Override
	public String queryFilterTypeByCode(String optCode) {
		// TODO Auto-generated method stub
		ResourceEntity resourceEntity = this.resourceDao
				.findUniqueByHql("FROM ResourceEntity WHERE optCode=?", optCode);
		if (resourceEntity != null) {
			return resourceEntity.getFilterType();
		}
		return null;
	}

	@Override
	public List<ResourceEntity> queryResourceListByModuleCode(String moduleCode){
		return this.resourceDao.findHql("SELECT r FROM ResourceEntity r LEFT JOIN r.module m WHERE m.code=? and r.description=?",
				moduleCode,"useFlag");
	}
	

	@Override
	public List<FileTypeUserAuthorityEntity> queryUserFileTypeAuthority(String typeId, String userId) {
		// TODO Auto-generated method stub
		String hql = "FROM FileTypeUserAuthorityEntity f WHERE f.typeEntity.id=? AND f.userEntity.id=?";
		return this.resourceDao.findHql(hql, typeId, userId);
	}

	@Override
	public List<FileTypeRoleAuthorityEntity> queryRoleFileTypeAuthority(String typeId, String roleId) {
		String hql = "FROM FileTypeRoleAuthorityEntity f WHERE f.typeEntity.id=? AND f.roleEntity.id=?";
		return this.resourceDao.findHql(hql, typeId, roleId);
	}

	public void setResourceDao(ResourceDao resourceDao) {
		this.resourceDao = resourceDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

}
