package com.xplatform.base.system.type.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.system.type.dao.TypeRoleDao;
import com.xplatform.base.system.type.entity.TypeRoleEntity;

@Repository("typeRoleDao")
public class TypeRoleDaoImpl extends CommonDao implements TypeRoleDao {

	/**
	 * @author luoheng
	 * 保存系统分类角色
	 * @param typeRole
	 * @return
	 */
	@Override
	public String saveTypeRole(TypeRoleEntity typeRole){
		return (String) this.save(typeRole);
	}
	
	/**
	 * 修改系统分类角色
	 * @author luoheng
	 * @param typeRole
	 */
	@Override
	public void updateTypeRole(TypeRoleEntity typeRole){
		this.updateEntitie(typeRole);
	}
	
	/**
	 * 删除系统分类角色
	 * @author luoheng
	 * @param id
	 */
	@Override
	public void deleteTypeRole(String id){
		this.deleteEntityById(TypeRoleEntity.class, id);
	}
	
	/**
	 * 根据ID获取系统分类角色信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	@Override
	public TypeRoleEntity getTypeRole(String id){
		return (TypeRoleEntity) this.get(TypeRoleEntity.class, id);
	}
	
	/**
	 * 查询所有系统分类角色信息
	 * @author luoheng
	 * @return
	 */
	@Override
	public List<TypeRoleEntity> queryTypeRoleList(){
		return this.findByQueryString("from TypeEntity");
	}
	
	/**
	 * 根据系统分类类型ID获取系统分类角色
	 * @author luoheng
	 * @param typeId
	 * @return
	 */
	@Override
	public List<TypeRoleEntity> queryTypeRoleByTypeIdList(String typeId) {
		String hql = " from TypeRoleEntity where type_id = ? ";
		return this.findHql(hql, new Object[]{typeId});
	}
	
}
