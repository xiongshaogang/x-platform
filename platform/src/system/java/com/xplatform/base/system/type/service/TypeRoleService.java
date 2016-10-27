package com.xplatform.base.system.type.service;

import java.util.List;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.system.type.entity.TypeRoleEntity;

public interface TypeRoleService {

	/**
	 * 保存系统分类角色
	 * @author luoheng
	 * @param typeRole
	 * @return
	 * @throws BusinessException
	 */
	public String saveTypeRole(TypeRoleEntity typeRole) throws BusinessException;
	
	/**
	 * 修改系统分类角色
	 * @author luoheng
	 * @param typeRole
	 * @throws BusinessException
	 */
	public void updateTypeRole(TypeRoleEntity typeRole) throws BusinessException;
	
	/**
	 * 删除系统分类角色
	 * @author luoheng
	 * @param id
	 * @throws BusinessException
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除系统分类角色
	 * @author luoheng
	 * @param ids
	 * @throws BusinessException
	 */
	public void batchDelete(String ids) throws BusinessException;
	
	/**
	 * 根据ID获取系统分类角色信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	public TypeRoleEntity getTypeRole(String id);
	
	/**
	 * 获取所有系统分类角色信息
	 * @author luoheng
	 * @return
	 */
	public List<TypeRoleEntity> queryTypeRoleList();
	
	/**
	 * 根据分类类型ID获取系统分类角色信息
	 * @author luoheng
	 * @param typeId
	 * @return
	 */
	public List<TypeRoleEntity> queryTypeRoleByTypeIdList(String typeId);
	
	/**
	 * 系统分类分配角色
	 * @author luoheng
	 * @param roleIds
	 * @param typeId
	 * @throws BusinessException
	 */
	public void updateTypeRole(String roleIds, String typeId) throws BusinessException;
}
