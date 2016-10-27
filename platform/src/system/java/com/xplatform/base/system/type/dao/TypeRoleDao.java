package com.xplatform.base.system.type.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.system.type.entity.TypeRoleEntity;

public interface TypeRoleDao extends ICommonDao {

	/**
	 * @author luoheng
	 * 保存系统分类角色
	 * @param typeRole
	 * @return
	 */
	public String saveTypeRole(TypeRoleEntity typeRole);
	
	/**
	 * 修改系统分类角色
	 * @author luoheng
	 * @param typeRole
	 */
	public void updateTypeRole(TypeRoleEntity typeRole);
	
	/**
	 * 删除系统分类角色
	 * @author luoheng
	 * @param id
	 */
	public void deleteTypeRole(String id);
	
	/**
	 * 根据ID获取系统分类角色信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	public TypeRoleEntity getTypeRole(String id);
	
	/**
	 * 查询所有系统分类角色信息
	 * @author luoheng
	 * @return
	 */
	public List<TypeRoleEntity> queryTypeRoleList();
	
	/**
	 * 根据系统分类类型ID获取系统分类角色
	 * @author luoheng
	 * @param typeId
	 * @return
	 */
	public List<TypeRoleEntity> queryTypeRoleByTypeIdList(String typeId);
}
