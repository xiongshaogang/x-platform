package com.xplatform.base.system.type.service.impl;

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
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.system.type.dao.TypeDao;
import com.xplatform.base.system.type.dao.TypeRoleDao;
import com.xplatform.base.system.type.entity.TypeEntity;
import com.xplatform.base.system.type.entity.TypeRoleEntity;
import com.xplatform.base.system.type.service.TypeRoleService;

@Service("typeRoleService")
public class TypeRoleServiceImpl implements TypeRoleService {
	
	private static final Logger logger = Logger.getLogger(TypeRoleServiceImpl.class);

	@Resource
	private TypeRoleDao typeRoleDao;
	
	@Resource
	private TypeDao typeDao;
	
	/**
	 * 保存系统分类角色
	 * @author luoheng
	 * @param typeRole
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public String saveTypeRole(TypeRoleEntity typeRole) throws BusinessException {
		String pk = "";
		try {
			pk = typeRoleDao.saveTypeRole(typeRole);
			logger.error("系统分类角色保存成功");
		} catch (Exception e) {
			logger.error("系统分类角色保存失败");
			throw new BusinessException("系统分类角色保存失败");
		}
		return pk;
	}
	
	/**
	 * 修改系统分类角色
	 * @author luoheng
	 * @param typeRole
	 * @throws BusinessException
	 */
	@Override
	public void updateTypeRole(TypeRoleEntity typeRole) throws BusinessException {
		try {
			typeRoleDao.updateTypeRole(typeRole);
			logger.error("系统分类角色修改成功");
		} catch (Exception e) {
			logger.error("系统分类角色修改失败");
			throw new BusinessException("系统分类角色修改失败");
		}
	}
	
	/**
	 * 删除系统分类角色
	 * @author luoheng
	 * @param id
	 * @throws BusinessException
	 */
	@Override
	public void delete(String id) throws BusinessException {
		try {
			typeRoleDao.deleteTypeRole(id);
			logger.error("系统分类角色删除成功");
		} catch (Exception e) {
			logger.error("系统分类角色删除失败");
			throw new BusinessException("系统分类角色删除失败");
		}
	}
	
	/**
	 * 批量删除系统分类角色
	 * @author luoheng
	 * @param ids
	 * @throws BusinessException
	 */
	@Override
	public void batchDelete(String ids) throws BusinessException {
		try {
			if(StringUtil.isNotBlank(ids)){
				String[] idArr=StringUtil.split(ids, ",");
				for(String id:idArr){
					this.delete(id);
				}
			}
			logger.error("系统分类角色批量删除成功");
		} catch (Exception e) {
			logger.error("系统分类角色批量删除失败");
			throw new BusinessException("系统分类角色批量删除失败");
		}
	}
	
	/**
	 * 根据ID获取系统分类角色信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	@Override
	public TypeRoleEntity getTypeRole(String id){
		TypeRoleEntity typeRole = null;
		typeRole = typeRoleDao.getTypeRole(id);
		return typeRole;
	}
	
	/**
	 * 获取所有系统分类角色信息
	 * @author luoheng
	 * @return
	 */
	@Override
	public List<TypeRoleEntity> queryTypeRoleList(){
		List<TypeRoleEntity> typeRoleList = new ArrayList<TypeRoleEntity>();
		typeRoleList = typeRoleDao.queryTypeRoleList();
		return typeRoleList;
	}
	
	/**
	 * 根据分类类型ID获取系统分类角色信息
	 * @author luoheng
	 * @param typeId
	 * @return
	 */
	@Override
	public List<TypeRoleEntity> queryTypeRoleByTypeIdList(String typeId){
		List<TypeRoleEntity> typeRoleList = new ArrayList<TypeRoleEntity>();
		typeRoleList = typeRoleDao.queryTypeRoleByTypeIdList(typeId);
		return typeRoleList;
	}
	
	/**
	 * 系统分类分配角色
	 * @author luoheng
	 * @param roleIds
	 * @param typeId
	 * @throws BusinessException
	 */
	@Override
	public void updateTypeRole(String roleIds, String typeId) throws BusinessException {
		try {
			TypeEntity type = typeDao.getType(typeId);
			
			List<TypeRoleEntity> typeRoleList = typeRoleDao.queryTypeRoleByTypeIdList(typeId);
			Map<String, TypeRoleEntity> typeRoleMap = new HashMap<String, TypeRoleEntity>();
			if(!typeRoleList.isEmpty()){
				for(TypeRoleEntity typeRole : typeRoleList){
					typeRoleMap.put(typeRole.getRole().getId(), typeRole);
				}
			}
			
			Set<String> set = new HashSet<String>();
			if(StringUtils.isNotEmpty(roleIds)){
				String[] roleArr=roleIds.split(",");
				for(String roleId : roleArr){
					set.add(roleId);
				}
			}
			
			updateTypeRoleCompare(set,type,typeRoleMap);
		} catch (Exception e) {
			logger.error("系统分类角色操作失败");
			throw new BusinessException("系统分类角色操作失败");
		}
		
	}
	
	private void updateTypeRoleCompare(Set<String> set,TypeEntity type, Map<String, TypeRoleEntity> map) {
		List<TypeRoleEntity> saveList = new ArrayList<TypeRoleEntity>();
		List<TypeRoleEntity> deleteList = new ArrayList<TypeRoleEntity>();
		
		//添加的数据
		for(String roleId : set){
			if(map.containsKey(roleId)){
				map.remove(roleId);
			}else{
				TypeRoleEntity typeRole = new TypeRoleEntity();
				RoleEntity role = new RoleEntity();
				role.setId(roleId);
				typeRole.setRole(role);
				typeRole.setType(type);
				saveList.add(typeRole);
			}
		}
		
		//构造删除数据
		for(Entry<String, TypeRoleEntity> entry:map.entrySet()){
			deleteList.add(entry.getValue());
		}
		
		this.typeRoleDao.batchSave(saveList);
		this.typeRoleDao.deleteAllEntitie(deleteList);
	}

	public void setTypeRoleDao(TypeRoleDao typeRoleDao) {
		this.typeRoleDao = typeRoleDao;
	}

	public void setTypeDao(TypeDao typeDao) {
		this.typeDao = typeDao;
	} 
}
