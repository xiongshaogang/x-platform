package com.xplatform.base.system.type.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.system.type.dao.TypeUserDao;
import com.xplatform.base.system.type.entity.TypeUserEntity;

@Repository("typeUserDao")
public class TypeUserDaoImpl extends CommonDao implements TypeUserDao {

	/**
	 * @author luoheng
	 * 保存系统分类权限
	 * @param typeUser
	 * @return
	 */
	@Override
	public String saveTypeUser(TypeUserEntity typeUser){
		return (String) this.save(typeUser);
	}
	
	/**
	 * 修改系统分类权限
	 * @author luoheng
	 * @param typeUser
	 */
	@Override
	public void updateTypeUser(TypeUserEntity typeUser){
		this.updateEntitie(typeUser);
	}
	
	/**
	 * 删除系统分类权限
	 * @author luoheng
	 * @param id
	 */
	@Override
	public void deleteTypeUser(String id){
		this.deleteEntityById(TypeUserEntity.class, id);
	}
	
	/**
	 * 根据ID获取系统分类权限信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	@Override
	public TypeUserEntity getTypeUser(String id){
		return (TypeUserEntity) this.get(TypeUserEntity.class, id);
	}
	
	/**
	 * 查询所有系统分类权限信息
	 * @author luoheng
	 * @return
	 */
	@Override
	public List<TypeUserEntity> queryTypeUserList(){
		return this.findByQueryString("from TypeUserEntity");
	}
	
	/**
	 * 根据系统分类类型ID获取系统分类权限
	 * @author luoheng
	 * @param typeId
	 * @return
	 */
	@Override
	public List<TypeUserEntity> queryTypeUserByUserIdList(String userId) {
		String hql = " from TypeUserEntity where user_id = ? ";
		return this.findHql(hql, new Object[]{userId});
	}
}
