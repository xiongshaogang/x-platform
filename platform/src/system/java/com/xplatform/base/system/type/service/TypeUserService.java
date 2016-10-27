package com.xplatform.base.system.type.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.system.type.entity.TypeUserEntity;

public interface TypeUserService {

	/**
	 * 保存系统分类权限
	 * @author luoheng
	 * @param typeUser
	 * @return
	 * @throws BusinessException
	 */
	public String saveTypeUser(TypeUserEntity typeUser) throws BusinessException;
	
	public void batchSave(List<TypeUserEntity> saveList) throws BusinessException;
	
	/**
	 * 修改系统分类权限
	 * @author luoheng
	 * @param typeUser
	 * @throws BusinessException
	 */
	public void updateTypeUser(TypeUserEntity typeUser) throws BusinessException;
	
	/**
	 * 删除系统分类权限
	 * @author luoheng
	 * @param id
	 * @throws BusinessException
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除系统分类权限
	 * @author luoheng
	 * @param ids
	 * @throws BusinessException
	 */
	public void batchDelete(String ids) throws BusinessException;
	
	/**
	 * 根据ID获取系统分类权限信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	public TypeUserEntity getTypeUser(String id);
	
	/**
	 * 获取所有系统分类权限信息
	 * @author luoheng
	 * @return
	 */
	public List<TypeUserEntity> queryTypeUserList();
	
	/**
	 * 根据分类类型ID获取系统分类权限信息
	 * @author luoheng
	 * @param typeId
	 * @return
	 */
	public List<TypeUserEntity> queryTypeUserByUserIdList(String userId);
	
	/**
	 * 系统分类分配权限
	 * @author luoheng
	 * @param UserIds
	 * @param typeId
	 * @throws BusinessException
	 */
	public void updateTypeUser(String typeIds, String userId) throws BusinessException;
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年11月19日 下午2:53:59
	 * @Decription 更新系统分类权限
	 *
	 * @param selectNodeMap  勾选的
	 * @param ownsNodeMap   数据库已有的
	 * @param userId 
	 * @throws BusinessException
	 */
	public void updateTypeUser(Map<String, String> selectNodeMap, Map<String, TypeUserEntity> ownsNodeMap, String userId) throws BusinessException;
}
