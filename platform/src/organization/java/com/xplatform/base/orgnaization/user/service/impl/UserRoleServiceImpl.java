package com.xplatform.base.orgnaization.user.service.impl;

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
import com.xplatform.base.orgnaization.role.dao.RoleDao;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.user.dao.UserDao;
import com.xplatform.base.orgnaization.user.dao.UserRoleDao;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.entity.UserRoleEntity;
import com.xplatform.base.orgnaization.user.service.UserRoleService;

/**
 * 
 * description :分组service实现
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
@Service("userRoleService")
public class UserRoleServiceImpl implements UserRoleService {
	private static final Logger logger = Logger.getLogger(UserRoleServiceImpl.class);
	@Resource
	private UserRoleDao userRoleDao;
	
	@Resource
	private UserDao userDao;
	
	@Resource
	private RoleDao roleDao;
	
	@Resource
	private BaseService baseService;
	

	@Override
	public String save(UserRoleEntity userRoleEntity) throws BusinessException {
		String pk="";
		try {
			pk=this.userRoleDao.addUserRole(userRoleEntity);
		} catch (Exception e) {
			logger.error("用户角色保存失败");
			throw new BusinessException("用户角色保存失败");
		}
		logger.info("用户角色保存成功");
		return pk;
	}
	
	@Override
	public void updateUserRole(String roleIds, String userId) throws BusinessException{
		try {
			UserEntity user = userDao.get(UserEntity.class, userId);
			
			List<UserRoleEntity> userRoleList = userRoleDao.queryUserRoleByUserIdList(userId);
			Map<String, UserRoleEntity> userRoleMap = new HashMap<String, UserRoleEntity>();
			if(!userRoleList.isEmpty()){
				for(UserRoleEntity userRole : userRoleList){
					userRoleMap.put(userRole.getRole().getId(), userRole);
				}
			}
			
			Set<String> set = new HashSet<String>();
			if(StringUtils.isNotEmpty(roleIds)){
				String[] roleArr=roleIds.split(",");
				for(String roleId : roleArr){
					set.add(roleId);
				}
			}
			
			updateUserRoleCompare(set,user,userRoleMap);
		} catch (Exception e) {
			logger.error("用户角色分配失败");
			throw new BusinessException("用户角色分配失败");
		}
		
	}
	
	private void updateUserRoleCompare(Set<String> set,UserEntity user, Map<String, UserRoleEntity> map) {
		List<UserRoleEntity> saveList = new ArrayList<UserRoleEntity>();
		List<UserRoleEntity> deleteList = new ArrayList<UserRoleEntity>();
		
		//添加的数据
		for(String roleId : set){
			if(map.containsKey(roleId)){
				map.remove(roleId);
			}else{
				UserRoleEntity userRole = new UserRoleEntity();
				RoleEntity role = new RoleEntity();
				role.setId(roleId);
				userRole.setRole(role);
				userRole.setUser(user);
				saveList.add(userRole);
			}
		}
		
		//构造删除数据
		for(Entry<String, UserRoleEntity> entry:map.entrySet()){
			deleteList.add(entry.getValue());
		}
		
		this.userRoleDao.batchSave(saveList);
		this.userRoleDao.deleteAllEntitie(deleteList);
	}
	
	
	
	@Override
	public void updateRoleUser(String userIds, String roleId) throws BusinessException{
		try {
			RoleEntity role = roleDao.get(RoleEntity.class, roleId);
			
			List<UserRoleEntity> userRoleList = userRoleDao.queryUserRoleByRoleIdList(roleId);
			Map<String, UserRoleEntity> userRoleMap = new HashMap<String, UserRoleEntity>();
			if(!userRoleList.isEmpty()){
				for(UserRoleEntity userRole : userRoleList){
					userRoleMap.put(userRole.getUser().getId(), userRole);
				}
			}
			
			Set<String> set = new HashSet<String>();
			if(StringUtils.isNotEmpty(userIds)){
				String[] userArr=userIds.split(",");
				for(String userId : userArr){
					set.add(userId);
				}
			}
			
			updateRoleUserCompare(set,role,userRoleMap);
		} catch (Exception e) {
			logger.error("角色分配用户失败");
			throw new BusinessException("角色分配用户失败");
		}
		
	}
	
	private void updateRoleUserCompare(Set<String> set,RoleEntity role, Map<String, UserRoleEntity> map) {
		List<UserRoleEntity> saveList = new ArrayList<UserRoleEntity>();
		List<UserRoleEntity> deleteList = new ArrayList<UserRoleEntity>();
		
		//添加的数据
		for(String userId : set){
			if(map.containsKey(userId)){
				map.remove(userId);
			}else{
				UserRoleEntity userRole = new UserRoleEntity();
				UserEntity user = new UserEntity();
				user.setId(userId);
				userRole.setRole(role);
				userRole.setUser(user);
				saveList.add(userRole);
			}
		}
		
		//构造删除数据
		for(Entry<String, UserRoleEntity> entry:map.entrySet()){
			deleteList.add(entry.getValue());
		}
		
		this.userRoleDao.batchSave(saveList);
		this.userRoleDao.deleteAllEntitie(deleteList);
	}
	

	@Override
	public void delete(String id) throws BusinessException{
		try {
			this.userRoleDao.deleteUserRole(id);
		} catch (Exception e) {
			logger.error("用户角色删除失败");
			throw new BusinessException("用户角色删除失败");
		}
		logger.info("用户角色删除成功");
	}

	@Override
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("用户角色批量删除成功");
	}

	@Override
	public void update(UserRoleEntity userRoleEntity) throws BusinessException {
		try {
			this.userRoleDao.updateUserRole(userRoleEntity);
		} catch (Exception e) {
			logger.error("用户角色更新失败");
			throw new BusinessException("用户角色更新失败");
		}
		logger.info("用户角色更新成功");
	}

	@Override
	public UserRoleEntity get(String id) throws BusinessException {
		UserRoleEntity userRoleEntity=null;
		try {
			userRoleEntity=this.userRoleDao.getUserRole(id);
		} catch (Exception e) {
			logger.error("用户角色获取失败");
			throw new BusinessException("用户角色获取失败");
		}
		logger.info("用户角色获取成功");
		return userRoleEntity;
	}
	
	@Override
	public UserRoleEntity getUserRole(String userId, String roleId) throws BusinessException {
		UserRoleEntity userRoleEntity=null;
		try {
			userRoleEntity=this.userRoleDao.getUserRole(userId, roleId);
		} catch (Exception e) {
			logger.error("用户角色获取失败");
			throw new BusinessException("用户角色获取失败");
		}
		logger.info("用户角色获取成功");
		return userRoleEntity;
	}

	@Override
	public List<UserRoleEntity> queryList() throws BusinessException {
		List<UserRoleEntity> userRoleEntityList=new ArrayList<UserRoleEntity>();
		try {
			userRoleEntityList=this.userRoleDao.queryUserRoleList();
		} catch (Exception e) {
			logger.error("用户角色获取列表失败");
			throw new BusinessException("用户角色获取列表失败");
		}
		logger.info("用户角色获取列表成功");
		return userRoleEntityList;
	}
	
	public List<UserRoleEntity> queryUserRoleByUserIdList(String userId) {
		List<UserRoleEntity> userRoleList=new ArrayList<UserRoleEntity>();
		userRoleList=this.userRoleDao.queryUserRoleByUserIdList(userId);
		return userRoleList;
	}
	
	@Override
	public List<UserRoleEntity> queryUserRoleByRoleIdList(String RoleId) {
		List<UserRoleEntity> userRoleList=new ArrayList<UserRoleEntity>();
		String hql = " from UserRoleEntity where role_id = ? ";
		return this.userRoleDao.findHql(hql, new Object[]{RoleId});
	}
	
	/**
	 * 根据用户ID删除用户所属角色
	 * @author luoheng
	 * @param userId
	 * @throws BusinessException
	 */
	@Override
	public void deleteUserRoleByUserId(String userId) throws BusinessException {
		try {
			userRoleDao.deleteUserRoleByUserId(userId);
			logger.error("用户角色删除成功");
		} catch (Exception e) {
			logger.error("用户角色删除失败");
			throw new BusinessException("用户角色删除失败");
		}
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.userRoleDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("用户角色获取分页列表失败");
			throw new BusinessException("用户角色获取分页列表失败");
		}
		logger.info("用户角色获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(UserRoleEntity.class, param, propertyName);
	}
	
	public void deleteOrgManager(String userId,String orgId) throws BusinessException{
		this.userRoleDao.executeSql("delete from t_org_user_role where userId=? and orgId=? and roleId=?", userId,orgId,RoleEntity.COMPANY_ADMIN);
	}
	
	public void setUserRoleDao(UserRoleDao userRoleDao) {
		this.userRoleDao = userRoleDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void batchSave(List<UserRoleEntity> UserRoleList) throws BusinessException {
		for(UserRoleEntity userRole : UserRoleList){
			this.save(userRole);
		}
	}

	@Override
	public void deleteByRoleId(String roleId) throws BusinessException {
		String sql = "delete from t_org_user_role where roleId=?";
		this.userRoleDao.executeSql(sql, roleId);
	}

	@Override
	public void deleteByUseAndRole(String userId, String roleId) throws BusinessException {
		String sql = "delete from t_org_user_role where userId=? and roleId=?";
		this.userRoleDao.executeSql(sql, userId,roleId);
	}
	
	@Override
	public List<Map<String, Object>> queryRoleUsers(String roleId) throws BusinessException {
		String sql = "SELECT DISTINCT u.id,u.name,u.phone,u.portrait,u.sortKey,u.searchKey FROM t_org_user_role ur left join t_org_user u ON ur.userId=u.id WHERE ur.roleId=? ORDER BY ur.createTime DESC";
		return this.roleDao.findForJdbc(sql, roleId);
	}

	@Override
	public String save(UserRoleEntity UserRoleEntity, String userIds) throws BusinessException {
		String idArr[] = userIds.split(",");
		for(String userId : idArr){
			UserEntity user = this.userDao.getUser(userId);
			if(user != null){
				UserRoleEntity isExist = this.getUserRole(userId, UserRoleEntity.getRole().getId());
				if(isExist == null){
					UserRoleEntity userRole = new UserRoleEntity();
					userRole.setOrgId(UserRoleEntity.getOrgId());
					userRole.setRole(UserRoleEntity.getRole());
					userRole.setUser(user);
					this.userRoleDao.save(userRole);
				}
			}

		}
		return "新增成功";
	}

	@Override
	public List<Map<String, Object>> queryRolePortrait(String roleId) throws BusinessException {
		String sql = "SELECT DISTINCT u.portrait,u.name FROM t_org_user_role ur left join t_org_user u ON ur.userId=u.id WHERE ur.roleId=?  ORDER BY ur.createTime DESC";
		List<Map<String, Object>> list = this.userRoleDao.findForJdbcParam(sql, 1, 4, roleId);
		return list;
	}

}
