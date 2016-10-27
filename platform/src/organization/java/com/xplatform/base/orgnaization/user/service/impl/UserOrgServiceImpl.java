package com.xplatform.base.orgnaization.user.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;



import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.orgnaization.dao.OrgnaizationDao;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.orgnaization.service.OrgnaizationService;
import com.xplatform.base.orgnaization.user.dao.UserOrgDao;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.entity.UserOrgEntity;
import com.xplatform.base.orgnaization.user.service.UserOrgService;
import com.xplatform.base.orgnaization.user.service.UserService;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.platform.common.utils.HXUtils;

@Service("userOrgService")
public class UserOrgServiceImpl implements UserOrgService {
	private static final Logger logger = Logger.getLogger(UserOrgServiceImpl.class);
	@Resource
	private UserOrgDao userOrgDao;
	@Resource
	private BaseService baseService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private UserService userService;
	@Resource
	private OrgnaizationService orgnaizationService;
	
	@Override
	public String save(UserOrgEntity userJobEntity) throws BusinessException {
		String pk="";
		try {
			pk=this.userOrgDao.addUserJob(userJobEntity);
		} catch (Exception e) {
			logger.error("保存失败");
			throw new BusinessException("保存失败");
		}
		logger.info("岗位员工保存成功");
		return pk;
	}
	
	@Override
	public void delete(String id) throws BusinessException {
		try {
			this.userOrgDao.deleteUserJob(id);
		} catch (Exception e) {
			logger.error("岗位员工删除失败");
			throw new BusinessException("岗位员工删除失败");
		}
		logger.info("岗位员工删除成功");
	}
	
	@Override
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotEmpty(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("岗位员工批量删除成功");
	}

	/**
	 * 修改员工信息
	 */
	@Override 
	public void update(UserOrgEntity userJobEntity) throws BusinessException {
		try {
			this.userOrgDao.updateUserJob(userJobEntity);
		} catch (Exception e) {
			logger.error("岗位员工更新失败");
			throw new BusinessException("岗位员工更新失败");
		}
		logger.info("岗位员工更新成功");
	}

	@Override
	public UserOrgEntity get(String id) {
		UserOrgEntity userJobEntity=null;
		userJobEntity = this.userOrgDao.getUserJob(id);
		return userJobEntity;
	}
	
	@Override
	public UserOrgEntity getUserJobByUserId(String userId){
		List<UserOrgEntity> UserJobEntitieList = userOrgDao.queryUserJobListByUserId(userId);
		UserOrgEntity UserJobEntity = null;
		if (UserJobEntitieList.size() > 0)
			UserJobEntity = UserJobEntitieList.get(0);
		return UserJobEntity;
	}
	
	@Override
	public List<UserOrgEntity> findByUserId(String userId){
		return userOrgDao.queryUserJobListByUserId(userId);
	}
	
	@Override
	public List<UserOrgEntity> findByJobId(String jobId){
		List<UserOrgEntity> userJobList = userOrgDao.queryUserJobListByJobId(jobId);
		return userJobList;
	}
	
	@Override
	public UserOrgEntity findByUserJob(String userId, String jobId){
		String sql = "from UserJobEntity where job_id = ? and user_id=?";
		List<UserOrgEntity> userJobList = userOrgDao.findHql(sql, new Object[]{jobId, userId});
		UserOrgEntity userOrg = new UserOrgEntity();
		if(userJobList != null && userJobList.size() > 0){
			userOrg = userJobList.get(0);
		}
		return userOrg;
	}

	@Override
	public List<UserOrgEntity> queryList() throws BusinessException {
		List<UserOrgEntity> userOrgEntitieList=new ArrayList<UserOrgEntity>();
		try {
			userOrgEntitieList=this.userOrgDao.queryUserJobList();
		} catch (Exception e) {
			logger.error("岗位员工获取列表失败");
			throw new BusinessException("岗位员工获取列表失败");
		}
		logger.info("岗位员工获取列表成功");
		return userOrgEntitieList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.userOrgDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("岗位员工获取分页列表失败");
			throw new BusinessException("岗位员工获取列表失败");
		}
		logger.info("岗位员工获取分页列表成功");
	}
	
	/**
	 * 
	 * 重写方法: updateUserJobs|描述:  员工分配岗位
	 * 
	 * @param jobIds
	 * @param id
	 * @throws BusinessException
	 * @see com.xplatform.base.orgnaization.orgnaization.service.UserOrgService#updateUserJobs(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateUserJobs(String userId, String jobIds, String orgId) throws BusinessException{
		try {
			UserEntity user = this.userService.get(userId);
			List<UserOrgEntity> UserJobList = userOrgDao.findHql("from UserOrgEntity uo where uo.user.id=? and uo.org.treeIndex like ?", userId,"%"+orgId+"%");
			Map<String, UserOrgEntity> UserJobMap = new HashMap<String, UserOrgEntity>();
			if(!UserJobList.isEmpty()){
				for(UserOrgEntity UserOrg : UserJobList){
					UserJobMap.put(UserOrg.getOrg().getId(), UserOrg);
				}
			}
			
			Set<String> set = new HashSet<String>();
			if(StringUtil.isNotEmpty(jobIds)){
				String[] deptArr = jobIds.split(",");
				for(String deptId : deptArr){
					set.add(deptId);
				}
			}
			updateUserJobsCompare(set, user, UserJobMap);
		} catch (Exception e) {
			logger.error("员工岗位分配失败");
			throw new BusinessException("员工岗位分配失败");
		}
		
	}
	
	public void addUserJobs(String userId, String jobIds) throws BusinessException{
		try {
			List<UserOrgEntity> saveList = new ArrayList<UserOrgEntity>();
			UserEntity user = this.userService.get(userId);
			String[] jobArr=jobIds.split(",");
			//添加的数据
			for(String jobId : jobArr){
				List<UserOrgEntity> exist=this.userOrgDao.findHql("from UserOrgEntity uo where uo.user.id=? and uo.org.id=?", userId,jobId);
				if(!(exist!=null && exist.size()>0)){
					UserOrgEntity UserJob = new UserOrgEntity();
					OrgnaizationEntity job = new OrgnaizationEntity();
					job.setId(jobId);
					UserJob.setOrg(job);
					UserJob.setUser(user);
					saveList.add(UserJob);
				}
			}
			if(saveList!=null && saveList.size()>0){
				this.userOrgDao.batchSave(saveList);
			}
		} catch (Exception e) {
			logger.error("员工岗位分配失败");
			throw new BusinessException("员工岗位分配失败");
		}
		
	}
	
	public void addJobUsers(String userIds, String jobId) throws BusinessException{
		try {
			List<UserOrgEntity> saveList = new ArrayList<UserOrgEntity>();
			OrgnaizationEntity job=this.orgnaizationService.get(jobId);
			String[] userArr=userIds.split(",");
			//添加的数据
			for(String userId : userArr){
				List<UserOrgEntity> exist=this.userOrgDao.findHql("from UserOrgEntity uo where uo.user.id=? and uo.org.id=?", userId,jobId);
				if(!(exist!=null && exist.size()>0)){
					UserOrgEntity UserJob = new UserOrgEntity();
					UserEntity user = new UserEntity();
					user.setId(userId);
					UserJob.setOrg(job);
					UserJob.setUser(user);
					saveList.add(UserJob);
				}
			}
			if(saveList!=null && saveList.size()>0){
				this.userOrgDao.batchSave(saveList);
			}
		} catch (Exception e) {
			logger.error("员工岗位分配失败");
			throw new BusinessException("员工岗位分配失败");
		}
		
	}
	
	private void updateUserJobsCompare(Set<String> set,UserEntity user, Map<String, UserOrgEntity> map) {
		List<UserOrgEntity> saveList = new ArrayList<UserOrgEntity>();
		List<UserOrgEntity> deleteList = new ArrayList<UserOrgEntity>();
		
		//添加的数据
		for(String jobId : set){
			if(map.containsKey(jobId)){
				map.remove(jobId);
			}else{
				UserOrgEntity UserJob = new UserOrgEntity();
				OrgnaizationEntity job = new OrgnaizationEntity();
				job.setId(jobId);
				UserJob.setOrg(job);
				UserJob.setUser(user);
				saveList.add(UserJob);
			}
		}
		//构造删除数据
		for(Entry<String, UserOrgEntity> entry:map.entrySet()){
			deleteList.add(entry.getValue());
		}
		//删除部门负责人
		this.userOrgDao.batchSave(saveList);
		if(deleteList!=null && deleteList.size()>0){
			for(UserOrgEntity uo:deleteList){
				deleteManagerUserFromDept(uo.getOrg().getId(),user.getId());
			}
		}
		//删除部门
		this.userOrgDao.deleteAllEntitie(deleteList);
	}
	
	/**
	 * 
	 * 重写方法: updateJobUsers|描述:  岗位分配员工
	 * 
	 * @param UserIds
	 * @param id
	 */
	@Override
	public void updateJobUsers(String jobId, String userIds) throws BusinessException{
		try {
			OrgnaizationEntity job = orgnaizationService.get(jobId);
			
			List<UserOrgEntity> userJobList = userOrgDao.queryUserJobListByJobId(job.getId());
			Map<String, UserOrgEntity> UserJobMap = new HashMap<String, UserOrgEntity>();
			if(!userJobList.isEmpty()){
				for(UserOrgEntity UserJob : userJobList){
					UserJobMap.put(UserJob.getUser().getId(), UserJob);
				}
			}
			
			Set<String> set = new HashSet<String>();
			if(StringUtil.isNotEmpty(userIds)){
				String[] userArr=userIds.split(",");
				for(String userId : userArr){
					set.add(userId);
					/*//更新用户的组织机构
					String orgId = job.getId();
					String orgName =job.getName();
					List<UserOrgEntity> userOrgList=this.findByUserId(userId);
					if(userOrgList!=null && userOrgList.size()>0){
						for(UserOrgEntity userOrg:userOrgList){
							if(!orgId.contains(userOrg.getOrg().getId())){
								orgId+=(","+userOrg.getOrg().getId());
								orgName+=(","+userOrg.getOrg().getName());
							}
						}
					}
					UserEntity user=userService.get(userId);
					user.setOrgId(orgId);   //设置组织的id集合
					user.setOrgName(orgName);   //设置组织的名称集合
					userService.update(user);*/
				}
			}
			
			updateJobUsersCompare(set, job, UserJobMap);
		} catch (Exception e) {
			logger.error("员工岗位分配失败");
			throw new BusinessException("员工岗位分配失败");
		}
		
	}
	
	private void updateJobUsersCompare(Set<String> set, OrgnaizationEntity job, Map<String, UserOrgEntity> map) {
		List<UserOrgEntity> saveList = new ArrayList<UserOrgEntity>();
		List<UserOrgEntity> deleteList = new ArrayList<UserOrgEntity>();

		// 添加的数据
		for (String userId : set) {
			if (map.containsKey(userId)) {
				map.remove(userId);
			} else {
				UserOrgEntity userJob = new UserOrgEntity();
				UserEntity user = new UserEntity();
				user.setId(userId);
				userJob.setOrg(job);
				userJob.setUser(user);
				saveList.add(userJob);
			}
		}
		// 构造删除数据
		deleteList=new ArrayList<UserOrgEntity>(map.values());
		this.userOrgDao.batchSave(saveList);
		this.userOrgDao.deleteAllEntitie(deleteList);
	}

	@Override
	public List<UserOrgEntity> queryListByPorperty(String propertyName, String value) {
		return this.userOrgDao.findByProperty(UserOrgEntity.class, propertyName, value);
	}
	
	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(UserOrgEntity.class, param, propertyName);
	}
	
	@Override
	public void addUserToOrg(String orgId, String phone, String name, String type) throws Exception {
		UserEntity user = userService.checkUserExits(phone);
		if (user == null) {// 添加用户
			user = new UserEntity();
			user.setOrgId(orgId);
			user.setPhone(phone);
			user.setName(name);
			user.setOrgName(orgnaizationService.get(orgId).getName());
			// 1.添加员工信息
			user.setFlag("0");// 激活状态
			String pk = userService.save(user);
			user.setId(pk);
			// 2.添加员工所属部门信息
			UserOrgEntity userJob = new UserOrgEntity();
			OrgnaizationEntity belongJob = new OrgnaizationEntity();
			belongJob.setId(orgId);
			userJob.setOrg(belongJob);
			userJob.setUser(user);
			this.save(userJob);
		} else {
			// 2.判断这个人是否已经加入了orgId这个组织，加入了那么不分配了，否则，加入这个组织
			UserOrgEntity userOrgExist = this.getUserJobByUserId(user.getId());
			if (userOrgExist == null) {
				UserOrgEntity userOrg = new UserOrgEntity();
				userOrg.setUser(user);
				OrgnaizationEntity org = new OrgnaizationEntity();
				org.setId(orgId);
				userOrg.setOrg(org);
				this.save(userOrg);
				if (StringUtil.equals(type, "org")) {// 加入的是公司，环信对接好友
					List<UserEntity> userList = sysUserService.getUserByOrgIds(orgId);
					for (UserEntity u : userList) {
						// 让新加入公司的用户与公司内部已有成员加为好友,调用环信
						ObjectNode result = HXUtils.addFriendSingle(user.getId(), u.getId());
						if (result.get("statusCode").intValue() == 200) {
						} else {
							ExceptionUtil.throwException("IM服务器请求失败");
						}
					}
				}
			}
		}
	}
	
	
	public List<OrgnaizationEntity> getDeptsByOrg(String userId,String orgId){
		List<OrgnaizationEntity> orgList= this.userOrgDao.findHql("select uo.org from UserOrgEntity uo where uo.user.id=? and uo.org.treeIndex like ?",userId,"%"+orgId+"%");
		Map<String,OrgnaizationEntity> map=new HashMap<String,OrgnaizationEntity>();
		if(orgList!=null && orgList.size()>0){
			for(OrgnaizationEntity org:orgList){
				if(!StringUtil.equals(org.getId(), "-1")){
					map.put(org.getId(), org);
				}
			}
		}
		return new ArrayList<OrgnaizationEntity>(map.values());
	}
	
	public boolean isExistCompany(String userId,String orgId){
		List<UserOrgEntity> userOrgList= this.userOrgDao.findHql("from UserOrgEntity uo where uo.user.id=? and uo.org.treeIndex like ?",userId,"%"+orgId+"%");
		if(userOrgList!=null && userOrgList.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	public void deleteUserFromDept(String userIds, String deptId) throws BusinessException{
		try {
			if (StringUtil.isNotEmpty(userIds) && StringUtil.isNotEmpty(deptId)) {
				String[] idArr = StringUtil.split(userIds, ",");
				for (String id : idArr) {
					//通过部门查询到公司
					OrgnaizationEntity org=getOrgFromDeptId(deptId);
					if(org!=null && StringUtil.isNotEmpty(org.getId())){
						//通过部门查询到公司
						List<UserOrgEntity> userOrgList= this.userOrgDao.findHql("from UserOrgEntity uo where uo.user.id=? and uo.org.treeIndex like ?",id,"%"+org.getId()+"%");
						if(!(userOrgList!=null && userOrgList.size()>=2)){//只在一个部门下面，那么先挪到公司下面
							UserOrgEntity uo=new UserOrgEntity();
							UserEntity u=new UserEntity();
							u.setId(id);
							OrgnaizationEntity o=new OrgnaizationEntity();
							o.setId(org.getId());
							uo.setUser(u);
							uo.setOrg(o);
							this.save(uo);
						}
						deleteManagerUserFromDept(deptId,id);
						this.userOrgDao.executeSql("delete from t_org_user_org where userId=? and orgId=?", id,deptId);// 删除关联的机构
					}
					
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("删除失败");
		}
		
	}
	
	public void deleteManagerUserFromDept(String orgId,String userId){
		OrgnaizationEntity org=this.orgnaizationService.get(orgId);
		String ids="";
		String names="";
		if(StringUtil.isNotEmpty(org.getManagerUserId()) && org.getManagerUserId().contains(userId)){
			String[] idArr=org.getManagerUserId().split(",");
			String[] nameArr=org.getManagerUserName().split(",");
			for(int i=0;i<idArr.length;i++){
				if(!StringUtil.equals(userId, idArr[i])){
					ids=ids+idArr[i]+",";
					names=names+nameArr[i]+",";
				}
			}
			org.setManagerUserId(StringUtil.removeDot(ids));
			org.setManagerUserName(StringUtil.removeDot(names));
			try {
				this.orgnaizationService.update(org);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
	}
	
	private OrgnaizationEntity getOrgFromDeptId(String deptId){
		OrgnaizationEntity dept=this.orgnaizationService.get(deptId);
		if(dept!=null){
			String index=dept.getTreeIndex();
			if(StringUtil.isNotEmpty(index)){
				String[] ids=index.split(",");
				for(int i=ids.length;i>=0;i--){
					OrgnaizationEntity org=this.orgnaizationService.get(ids[i-1]);
					if(org!=null && StringUtil.equals("org", org.getType())){
						return org;
					}
				}
			}
		}
		return null;
	}
	
}
