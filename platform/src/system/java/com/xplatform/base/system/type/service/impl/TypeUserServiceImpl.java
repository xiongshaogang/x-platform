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
import com.xplatform.base.orgnaization.user.dao.UserDao;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.system.type.dao.TypeDao;
import com.xplatform.base.system.type.dao.TypeUserDao;
import com.xplatform.base.system.type.entity.TypeEntity;
import com.xplatform.base.system.type.entity.TypeUserEntity;
import com.xplatform.base.system.type.service.TypeUserService;

@Service("typeUserService")
public class TypeUserServiceImpl implements TypeUserService {

	private static final Logger logger = Logger.getLogger(TypeUserServiceImpl.class);
	
	@Resource
	private TypeUserDao typeUserDao;
	
	@Resource
	private TypeDao typeDao;
	
	@Resource
	private UserDao userDao;
	
	/**
	 * 保存系统分类权限
	 * @author luoheng
	 * @param typeUser
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public String saveTypeUser(TypeUserEntity typeUser) throws BusinessException {
		String pk = "";
		try {
			pk = typeUserDao.saveTypeUser(typeUser);
			logger.error("系统分类权限保存成功");
		} catch (Exception e) {
			logger.error("系统分类权限保存失败");
			throw new BusinessException("系统分类权限保存失败");
		}
		return pk;
	}
	
	/**
	 * 批量保存
	 * @author luoheng
	 * @param saveList
	 */
	@Override
	public void batchSave(List<TypeUserEntity> saveList) throws BusinessException{
		try {
			this.typeUserDao.batchSave(saveList);
		} catch (Exception e) {
			logger.error("系统分类权限保存失败");
			throw new BusinessException("系统分类权限保存失败");
		}
		logger.info("系统分类权限保存成功");
	}
	
	/**
	 * 修改系统分类权限
	 * @author luoheng
	 * @param typeUser
	 * @throws BusinessException
	 */
	@Override
	public void updateTypeUser(TypeUserEntity typeUser) throws BusinessException {
		try {
			typeUserDao.updateTypeUser(typeUser);
			logger.error("系统分类权限修改成功");
		} catch (Exception e) {
			logger.error("系统分类权限修改失败");
			throw new BusinessException("系统分类权限修改失败");
		}
	}
	
	/**
	 * 删除系统分类权限
	 * @author luoheng
	 * @param id
	 * @throws BusinessException
	 */
	@Override
	public void delete(String id) throws BusinessException {
		try {
			typeUserDao.deleteTypeUser(id);
			logger.error("系统分类权限删除成功");
		} catch (Exception e) {
			logger.error("系统分类权限删除失败");
			throw new BusinessException("系统分类权限删除失败");
		}
	}
	
	/**
	 * 批量删除系统分类权限
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
			logger.error("系统分类权限批量删除成功");
		} catch (Exception e) {
			logger.error("系统分类权限批量删除失败");
			throw new BusinessException("系统分类权限批量删除失败");
		}
	}
	
	/**
	 * 根据ID获取系统分类权限信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	@Override
	public TypeUserEntity getTypeUser(String id){
		TypeUserEntity typeUser = null;
		typeUser = typeUserDao.getTypeUser(id);
		return typeUser;
	}
	
	/**
	 * 获取所有系统分类权限信息
	 * @author luoheng
	 * @return
	 */
	@Override
	public List<TypeUserEntity> queryTypeUserList(){
		List<TypeUserEntity> typeUserList = new ArrayList<TypeUserEntity>();
		typeUserList = typeUserDao.queryTypeUserList();
		return typeUserList;
	}
	
	/**
	 * 根据分类类型ID获取系统分类权限信息
	 * @author luoheng
	 * @param typeId
	 * @return
	 */
	@Override
	public List<TypeUserEntity> queryTypeUserByUserIdList(String userId){
		List<TypeUserEntity> typeUserList = new ArrayList<TypeUserEntity>();
		typeUserList = typeUserDao.queryTypeUserByUserIdList(userId);
		return typeUserList;
	}
	
	/**
	 * 系统分类分配权限
	 * @author luoheng
	 * @param UserIds
	 * @param typeId
	 * @throws BusinessException
	 */
	@Override
	public void updateTypeUser(String typeIds, String userId) throws BusinessException {
		try {
			UserEntity user = userDao.getUser(userId);
			
			List<TypeUserEntity> typeUserList = typeUserDao.queryTypeUserByUserIdList(userId);
			Map<String, TypeUserEntity> typeUserMap = new HashMap<String, TypeUserEntity>();
			if(!typeUserList.isEmpty()){
				for(TypeUserEntity typeUser : typeUserList){
					typeUserMap.put(typeUser.getType().getId() + "|" + typeUser.getIsManage(), typeUser);
				}
			}
			
			Set<String> set = new HashSet<String>();
			if(StringUtils.isNotEmpty(typeIds)){
				String[] typeArr = typeIds.split(",");
				for(String typeId : typeArr){
					set.add(typeId);
				}
			}
			
			updateTypeUserCompare(set, user, typeUserMap);
		} catch (Exception e) {
			logger.error("系统分类权限操作失败");
			throw new BusinessException("系统分类权限操作失败");
		}
		
	}
	
	/**
	 * 
	 * 重写方法: updateTypeUser|描述: 更新用户系统分类权限
	 * @author hexj
	 * @param selectNodeMap
	 * @param ownsNodeMap
	 * @param userId
	 * @throws BusinessException
	 * @see com.xplatform.base.system.type.service.TypeUserService#updateTypeUser(java.util.Map, java.util.Map, java.lang.String)
	 */
	@Override
	public void updateTypeUser(Map<String, String> selectNodeMap, Map<String, TypeUserEntity> ownsNodeMap, String userId)
			throws BusinessException{
		List<TypeUserEntity> saveOrUpdateList = new ArrayList<TypeUserEntity>();
		for(Entry<String, String> entry : selectNodeMap.entrySet()){
			String id = entry.getKey();
			String value = entry.getValue();
			if(ownsNodeMap.containsKey(id)){
				if(!ownsNodeMap.get(id).getIsManage().equals(value)){
					TypeUserEntity typeUser = ownsNodeMap.get(id);
					typeUser.setIsManage(value);
					saveOrUpdateList.add(typeUser);
				}
				ownsNodeMap.remove(id);
			}else{
				TypeUserEntity typeUser = new TypeUserEntity();
				UserEntity user = new UserEntity();
				user.setId(userId);
				TypeEntity type = new TypeEntity();
				type.setId(id);
				typeUser.setUser(user);
				typeUser.setType(type);
				typeUser.setIsManage(value);
				saveOrUpdateList.add(typeUser);
			}
		}
		
		List<TypeUserEntity> deleteList = new ArrayList<TypeUserEntity>();
		for(Entry<String, TypeUserEntity> entry : ownsNodeMap.entrySet()){
			deleteList.add(entry.getValue());
		}
		try{
			typeUserDao.batchSaveOrUpdate(saveOrUpdateList);
			typeUserDao.deleteAllEntitie(deleteList);
		}catch(Exception e){
			logger.error("系统分类权限保存失败");
			throw new BusinessException("系统分类权限保存失败");
		}
		
	}
	
	private void updateTypeUserCompare(Set<String> set,UserEntity user, Map<String, TypeUserEntity> map) {
		List<TypeUserEntity> saveList = new ArrayList<TypeUserEntity>();
		List<TypeUserEntity> deleteList = new ArrayList<TypeUserEntity>();
		
		//添加的数据
		for(String typeId : set){
			String containsKey = typeId;
			String isManage = typeId.substring(typeId.indexOf("|") + 1, typeId.length());
			typeId = typeId.substring(0, typeId.indexOf("|"));
			if(map.containsKey(containsKey)){
				map.remove(containsKey);
			}else{
				TypeUserEntity typeUser = new TypeUserEntity();
				TypeEntity type = new TypeEntity();
				type.setId(typeId);
				typeUser.setType(type);
				typeUser.setUser(user);
				typeUser.setIsManage(isManage);
				saveList.add(typeUser);
			}
		}
		
		//构造删除数据
		for(Entry<String, TypeUserEntity> entry:map.entrySet()){
			deleteList.add(entry.getValue());
		}
		
		this.typeUserDao.batchSave(saveList);
		this.typeUserDao.deleteAllEntitie(deleteList);
	}

	public void setTypeUserDao(TypeUserDao typeUserDao) {
		this.typeUserDao = typeUserDao;
	}

	public void setTypeDao(TypeDao typeDao) {
		this.typeDao = typeDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
