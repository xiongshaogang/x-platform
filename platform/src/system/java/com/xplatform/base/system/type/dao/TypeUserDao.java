package com.xplatform.base.system.type.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.system.type.entity.TypeUserEntity;

public interface TypeUserDao extends ICommonDao {

	/**
	 * @author luoheng
	 * 保存系统分类权限
	 * @param typeUser
	 * @return
	 */
	public String saveTypeUser(TypeUserEntity typeUser);
	
	/**
	 * 修改系统分类权限
	 * @author luoheng
	 * @param typeUser
	 */
	public void updateTypeUser(TypeUserEntity typeUser);
	
	/**
	 * 删除系统分类权限
	 * @author luoheng
	 * @param id
	 */
	public void deleteTypeUser(String id);
	
	/**
	 * 根据ID获取系统分类权限信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	public TypeUserEntity getTypeUser(String id);
	
	/**
	 * 查询所有系统分类权限信息
	 * @author luoheng
	 * @return
	 */
	public List<TypeUserEntity> queryTypeUserList();
	
	/**
	 * 根据系统分类类型ID获取系统分类权限
	 * @author luoheng
	 * @param typeId
	 * @return
	 */
	public List<TypeUserEntity> queryTypeUserByUserIdList(String userId);
}
