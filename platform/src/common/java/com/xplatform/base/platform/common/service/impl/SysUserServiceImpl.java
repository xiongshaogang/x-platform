package com.xplatform.base.platform.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Service;

import com.xplatform.base.form.entity.FlowInstanceUserEntity;
import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.orgnaization.service.OrgnaizationService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.role.service.RoleService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.service.UserRoleService;
import com.xplatform.base.orgnaization.user.service.UserService;
import com.xplatform.base.orgnaization.user.vo.UserTypeVo;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.mybatis.dao.SysUserMybatisDao;
import com.xplatform.base.platform.common.script.IScript;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.platform.common.vo.Client;

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService, IScript {

	@Resource
	private UserService userService;
	@Resource
	private SysUserMybatisDao sysUserMybatisDao;
	@Resource
	private CommonDao commonDao;
	@Resource
	private UserRoleService userRoleService;
	@Resource
	private OrgnaizationService orgnaizationService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setSysUserMybatisDao(SysUserMybatisDao sysUserMybatisDao) {
		this.sysUserMybatisDao = sysUserMybatisDao;
	}

	@Override
	public List<UserEntity> getUserByOrgIds(String orgIds) {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orgIds", orgIds.split(","));
		return this.sysUserMybatisDao.getUserByOrgIds(param);
	}

	@Override
	public List<UserEntity> getUserByCurrentOrgIds(String orgIds) {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orgIds", orgIds.split(","));
		return this.sysUserMybatisDao.getUserByCurrentOrgIds(param);
	}

	@Override
	public List<UserEntity> getDistinctUserByOrgIds(String orgIds) {
		List<UserEntity> userEntityList = getUserByOrgIds(orgIds);
		return getDistinctUsers(userEntityList);
	}

	@Override
	public List<UserEntity> getDistinctUserByCurrentOrgIds(String orgIds) {
		List<UserEntity> userEntityList = getUserByCurrentOrgIds(orgIds);
		return getDistinctUsers(userEntityList);
	}

	@Override
	public Page<UserEntity> getUserByOrgIdsByPage(Page<UserEntity> page) {
		page.setResult(sysUserMybatisDao.getUserByOrgIdsByPage(page));
		return page;
	}

	@Override
	public Page<UserEntity> getUserByCurrentOrgIdsByPage(Page<UserEntity> page) {
		// TODO Auto-generated method stub
		page.setResult(sysUserMybatisDao.getUserByCurrentOrgIdsByPage(page));
		return page;
	}

	@Override
	public List<UserEntity> getDistinctUserByRoles(String roles) {
		// 获得去重后的组织集合
		List<OrgnaizationEntity> orgs = getDistinctOrgsByRoles(roles);
		String orgIds = parseOrgListToStr(orgs);
		// 通过组织集合获得去重下属
		List<UserEntity> userList = getDistinctUserByOrgIds(orgIds);
		return userList;
	}

	@Override
	public List<OrgnaizationEntity> getOrgsByRoles(String roleIds) {
		String hql = "SELECT ro.org FROM OrgRoleEntity ro WHERE ro.role.id in(:ids)";
		Query q = this.commonDao.getSession().createQuery(hql);
		q.setParameterList("ids", roleIds.split(","));
		return q.list();
	}

	@Override
	public List<OrgnaizationEntity> getDistinctOrgsByRoles(String roleIds) {
		List<OrgnaizationEntity> orgs = this.getOrgsByRoles(roleIds);
		return getDistinctOrgs(orgs);
	}

	@Override
	public Page<UserEntity> getUserByRolesByPage(Page<UserEntity> page) {
		Map map = (Map) page.getParameter();
		String roleIds = map.get("roleIds").toString();
		List<OrgnaizationEntity> orgs = this.getOrgsByRoles(roleIds);
		if (orgs.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (OrgnaizationEntity org : orgs) {
				String orgId = org.getId();
				sb.append(orgId + ",");
			}
			map.put("orgIds", sb.toString());
			return this.getUserByOrgIdsByPage(page);
		} else {
			return page;
		}
	}

	@Override
	public Page<UserEntity> getAllUserByPage(Page<UserEntity> page) {
		page.setResult(sysUserMybatisDao.getAllUserByPage(page));
		return page;
	}

	@Override
	public List<UserEntity> getQueryListAll() {
		return this.sysUserMybatisDao.getQueryListAll();
	}

	@Override
	public UserEntity getUserById(String userId) {
		// TODO Auto-generated method stub
		return this.userService.get(userId);
	}

	@Override
	public String getOrgNameByIds(String orgIds) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		// 如果机构不为空，设置好所选择的机构的name
		if (StringUtils.isNotEmpty(orgIds)) {
			String[] orgs = orgIds.split(",");
			for (String org : orgs) {
				OrgnaizationEntity d = this.orgnaizationService.get(org);
				sb.append(d.getName()).append(",");
			}
			return sb.toString().substring(0, sb.toString().length() - 1);
		} else {
			return "";
		}
	}

	/*@Override
	public List<UserEntity> getUserByRoleId(String roleId) {
		// TODO Auto-generated method stub
		List<UserEntity> voList = getDistinctUserByRoles(roleId);
		List<UserEntity> userEntityList = new ArrayList<UserEntity>();
		for (UserEntity vo : voList) {
			UserEntity userEntity = getUserById(vo.getId());
			if (userEntity != null) {
				userEntityList.add(userEntity);
			}
		}
		return userEntityList;
	}*/

	@Override
	public List<RoleEntity> getRoleListByUserId(String userId) {
		// TODO Auto-generated method stub
		String hql = "select DISTINCT ro.role FROM UserRoleEntity ro WHERE ro.user.id ='"+userId+"'";
		Query q = this.commonDao.getSession().createQuery(hql);
		return q.list();
		// 一个用户获得所有机构类型
		/*List<OrgnaizationEntity> allOrgs = getAllOrganizationsByUserId(userId);
		List<String> list = new ArrayList<String>();
		for (OrgnaizationEntity o : allOrgs) {
			list.add(o.getId());
		}
		StringUtil.addQuotes(list);
		// 获得所有机构类型的ids
		String allOrgIds = StringUtil.asString(list, ",");
		return getRolesByAllOrgIds(allOrgIds);*/
	}

	/*@Override
	public List<RoleEntity> getRolesByAllOrgIds(String allOrgIds) {
		// TODO Auto-generated method stub
		String hql = "select DISTINCT ro.role FROM OrgRoleEntity ro WHERE ro.org.id in(:ids)";
		Query q = this.commonDao.getSession().createQuery(hql);
		q.setParameterList("ids", allOrgIds.split(","));
		return q.list();
	}*/

	/*
	 * @Override public List<UserEntity> getUserByJobId(String jobId) { // TODO
	 * Auto-generated method stub String hql =
	 * "SELECT e FROM UserEntity e,UserOrgEntity ej WHERE e.id=ej.user.id and ej.org.id=?"
	 * ; return commonDao.findHql(hql, new Object[] { jobId }); }
	 * 
	 * @Override public List<UserEntity> getUserByDeptId(String deptId) { //
	 * TODO Auto-generated method stub List<UserEntity> voList =
	 * getDistinctUserByOrgIds(deptId); List<UserEntity> employeeList = new
	 * ArrayList<UserEntity>(); for (UserEntity vo : voList) { UserEntity e =
	 * getUserById(vo.getId()); employeeList.add(e); } return employeeList; }
	 */

	// @Override
	// public List<UserEntity> getUnderUserByUserId(String userId) {
	// // TODO Auto-generated method stub
	// /**
	// * 1.获取所在的岗位， 2.如果所在的岗位是总经理，那么获取上一个机构，查出所有的人员
	// * 3.如果所在的岗位是分管领导，那么查询出所有可管理的部门，查询出这些部门的人 4.如果是部门负责人岗位，那么查询出该部门所有的人员
	// * 5.如果是普通岗位，那么查询出下级岗位及后代岗位的所有人员
	// */
	// Set<UserEntity> userSet = new HashSet<UserEntity>();
	// List<OrgnaizationEntity> jobs = getJobsByUserId(userId);
	// for (OrgnaizationEntity job : jobs) {
	// List<UserEntity> users = getUserByDeptId(job.getId());
	// userSet.addAll(users);
	// }
	// // 下属员工不包括自己
	// UserEntity self = getUserById(userId);
	// if (userSet.contains(self)) {
	// userSet.remove(self);
	// }
	// return new ArrayList<UserEntity>(userSet);
	// }

	// @Override
	// public List<UserEntity> getDirectLeaderByUserId(String userId) {
	// // TODO Auto-generated method stub
	// /**
	// * 1.获取主岗位 2.获取主岗位 的parentId 3.parent如果是岗位，那么查询出所有的用户，就是他的直接领导
	// * 4.parent如果是部门，那么查出部门负责人，就是他的直接领导
	// */
	// OrgnaizationEntity mainJob = getMainJobByUserId(userId);
	// OrgnaizationEntity parent = mainJob.getParent();
	// if ("job".equals(parent.getType())) { // 主岗位上一级为岗位
	// return getUserByJobId(parent.getId());
	// }
	// PositionEntity mainPosition = mainJob.getPosition();
	// if (mainPosition != null && "deptManager".equals(mainPosition.getCode()))
	// {
	// // 员工主岗位部门 经理,领导为上级部门的部门经理
	// OrgnaizationEntity superOrg = parent.getParent(); // 所在部门的上级部门
	// if (superOrg == null) {
	// return null;
	// }
	// for (OrgnaizationEntity org : superOrg.getChildren()) {
	// if (!org.getType().equals("job")) {
	// continue;
	// }
	// PositionEntity position = org.getPosition();
	// if (position != null && position.getCode().equals("deptManager")) {
	// return getUserByJobId(org.getId());
	// }
	// }
	// } else {// 员工主岗位为普通岗位 , 领导为所在部门 经理
	// UserEntity user = getDeptManagerUserByUserId(userId);
	// if (user != null) {
	// List<UserEntity> list = new ArrayList<UserEntity>();
	// list.add(user);
	// return list;
	// }
	// }
	// return null;
	// }

	/*
	 * @Override public List<OrgnaizationEntity>
	 * getDeptManagersJobByUserId(String userId) { // TODO Auto-generated method
	 * stub Set<OrgnaizationEntity> leaderJobs = new
	 * HashSet<OrgnaizationEntity>(); List<OrgnaizationEntity> depts =
	 * getDeptsByUserId(userId); for (OrgnaizationEntity dept : depts) {
	 * List<OrgnaizationEntity> jobs = dept.getChildren(); for
	 * (OrgnaizationEntity job : jobs) { PositionEntity position =
	 * job.getPosition(); // 找到部门负责人岗位并加入集合 if (position != null &&
	 * "deptManager".equals(position.getCode())) { if
	 * (!leaderJobs.contains(job)) { leaderJobs.add(job); } } } String
	 * managerUserId=dept.getManagerUserId(); String
	 * leaderUserId=dept.getLeaderUserId();
	 * if(StringUtil.isNotEmpty(leaderUserId)){
	 * this.getOrganizationsByUserId(leaderUserId); }
	 * this.getOrganizationsByUserId(userId); } return new
	 * ArrayList<OrgnaizationEntity>(leaderJobs); }
	 */

	// @Override
	/*
	 * public List<OrgnaizationEntity> getDeptLeadersJobByUserId(String userId)
	 * { // TODO Auto-generated method stub Set<OrgnaizationEntity> leaderJobs =
	 * new HashSet<OrgnaizationEntity>(); //获取员工所有的部门 List<OrgnaizationEntity>
	 * depts = getDeptsByUserId(userId); for (OrgnaizationEntity dept : depts) {
	 * List<OrgnaizationEntity> jobs = dept.getChildren(); for
	 * (OrgnaizationEntity job : jobs) { // 找到分管领导岗位并加入集合 PositionEntity
	 * position = job.getPosition(); if (position != null &&
	 * "deptLeader".equals(position.getCode())) { if (!leaderJobs.contains(job))
	 * {
	 * 
	 * } } } if(StringUtil.equals(userId, dept.getLeaderUserId())){//部门的
	 * leaderJobs.add(dept); } } return new
	 * ArrayList<OrgnaizationEntity>(leaderJobs); }
	 */

	@Override
	public List<OrgnaizationEntity> getAllOrganizationsByUserId(String userId) {
		// TODO Auto-generated method stub
		String hql = "SELECT distinct j FROM OrgnaizationEntity j,UserOrgEntity uj WHERE  j.id=uj.org.id AND uj.user.id=?";
		return this.commonDao.findHql(hql, new Object[] { userId });
	}
	
	/**
	 * 获取所有机构
	 * 
	 * @param userId
	 * @return
	 */
	public List<OrgnaizationEntity> getAllOrganization(){
		String hql = "SELECT j FROM OrgnaizationEntity j WHERE  j.type='org' AND j.id not in('-1','-2')";
		return this.commonDao.findHql(hql);
	}

	@Override
	public List<OrgnaizationEntity> getOrganizationsByUserId(String userId) {
		// TODO Auto-generated method stub
		String hql = "SELECT j FROM OrgnaizationEntity j,UserOrgEntity uj WHERE  j.type='org' and j.id=uj.org.id AND uj.user.id=?";
		return this.commonDao.findHql(hql, new Object[] { userId });
	}

	@Override
	public List<OrgnaizationEntity> getDeptsByUserId(String userId) {
		String hql = "SELECT j FROM OrgnaizationEntity j,UserOrgEntity uj WHERE  j.type='dept' and j.id=uj.org.id AND uj.user.id=?";
		return this.commonDao.findHql(hql, new Object[] { userId });
	}

	@Override
	public List<OrgnaizationEntity> getJobsByUserId(String userId) {
		// TODO Auto-generated method stub
		String hql = "SELECT j FROM OrgnaizationEntity j,UserOrgEntity uj WHERE  j.type='job' and j.id=uj.org.id AND uj.user.id=?";
		return this.commonDao.findHql(hql, new Object[] { userId });
	}

	@Override
	public List<UserEntity> getBranchLeadersByUserId(String userId) {
		// TODO Auto-generated method stub
		Map<String, UserEntity> map = new HashMap<String, UserEntity>();
		List<OrgnaizationEntity> depts = this.getDeptsByUserId(userId);
		for (OrgnaizationEntity dept : depts) {
			if (StringUtil.isNotEmpty(dept.getLeaderUserId())) {
				UserEntity user = this.getUserById(dept.getLeaderUserId());
				if (user != null && !map.containsKey(user.getId())) {
					map.put(user.getId(), user);
				}
			}
		}
		return new ArrayList<UserEntity>(map.values());
	}

	@Override
	public List<UserEntity> getDeptManagersByUserId(String userId) {
		// TODO Auto-generated method stub
		Map<String, UserEntity> map = new HashMap<String, UserEntity>();
		List<OrgnaizationEntity> depts = this.getDeptsByUserId(userId);
		for (OrgnaizationEntity dept : depts) {
			if (StringUtil.isNotEmpty(dept.getManagerUserId())) {
				UserEntity user = this.getUserById(dept.getManagerUserId());
				if (user != null && !map.containsKey(user.getId())) {
					map.put(user.getId(), user);
				}
			}
		}
		return new ArrayList<UserEntity>(map.values());
	}

	// @Override
	// public List<OrgnaizationEntity> getUnderJobsByUserId(String userId) {
	// // TODO Auto-generated method stub
	// Set<OrgnaizationEntity> result = new HashSet<OrgnaizationEntity>();
	// List<OrgnaizationEntity> orgList = this.getJobsByUserId(userId);
	// result.addAll(orgList);
	// for (OrgnaizationEntity org : orgList) {
	// PositionEntity position = org.getPosition();
	// List<OrgnaizationEntity> lowList = new ArrayList<OrgnaizationEntity>();
	// if (position != null) {
	// if ("manager".equals(position.getCode())) { // 总经理职位
	// OrgnaizationEntity firstOrg = getFirstOrg(org.getId(), "org");
	// lowList = orgnaizationService.getLowJob(firstOrg);// 查找该机构下的所有子岗位
	// } else if ("deptManager".equals(position.getCode()) ||
	// "deptLeader".equals(position.getCode())) { // 部门经理或分管领导职位
	// lowList = orgnaizationService.getLowJob(org.getParent());// 查找该岗位下的所有的子岗位
	// } else {
	// lowList = orgnaizationService.getLowJob(org);// 查找该岗位的子岗位
	// }
	// }
	// for (OrgnaizationEntity low : lowList) {
	// if (!result.contains(low)) {
	// result.add(low);
	// }
	// }
	// }
	// return new ArrayList<OrgnaizationEntity>(result);
	// }

	@Override
	public List<String> getUserOrgList(List<OrgnaizationEntity> orgList) {
		// TODO Auto-generated method stub
		List<String> idList = new ArrayList<String>();
		for (OrgnaizationEntity org : orgList) {
			idList.add(org.getId());
		}
		return StringUtil.addQuotes(idList);
	}

	/*
	 * @Override public List<OrgnaizationEntity> getOrgnaizationsByUserId(String
	 * userId) { // TODO Auto-generated method stub Set<OrgnaizationEntity> orgs
	 * = new HashSet<OrgnaizationEntity>(); // 1.找到员工所有的岗位
	 * List<OrgnaizationEntity> jobs = getOrgsByUserId(userId);
	 * 
	 * return new ArrayList<OrgnaizationEntity>(orgs); }
	 */

	/**
	 * 获取第一个机构
	 * 
	 * @param orgId
	 * @param orgType
	 * @return
	 */
	public OrgnaizationEntity getFirstOrg(String orgId, String orgType) {
		OrgnaizationEntity org = orgnaizationService.get(orgId);
		while (org != null && !orgType.equals(org.getType())) {
			org = org.getParent();
		}
		return org;
	}

	/**
	 * 通过userId获得用户所管理的所有机构、部门
	 * 
	 * @author xiaqiang
	 * @createtime 2015年4月21日 下午2:46:25
	 *
	 * @param userId
	 * @return
	 */
	public List<OrgnaizationEntity> getManageOrgsAndDepts(String userId) {
		String hql = "SELECT o FROM OrgnaizationEntity o WHERE o.managerUserId=? or o.leaderUserId=?";
		return this.commonDao.findHql(hql, new Object[] { userId, userId });
	}

	/**
	 * 获得当前登录用户所管理的所有机构、部门
	 * 
	 * @author xiaqiang
	 * @createtime 2015年4月21日 下午2:46:25
	 *
	 */
	public List<OrgnaizationEntity> getCurrentManageAllOrgsAndDepts() {
		String userId = ClientUtil.getUserId();
		return getManageOrgsAndDepts(userId);
	}

	/**
	 * 通过userId获得所有可管理的组织机构
	 * 
	 * @author xiaqiang
	 * @createtime 2015年4月21日 下午2:46:25
	 *
	 * @param userId
	 * @return
	 */
	public List<OrgnaizationEntity> getUnderJobsByUserId(String userId) {
		Set<OrgnaizationEntity> underJobs = new HashSet<OrgnaizationEntity>();
		// 1.获得用户所有机构
		List<OrgnaizationEntity> jobs = getAllOrganizationsByUserId(userId);
		// 2.获得用户所有管理的机构、部门
		List<OrgnaizationEntity> orgs = getManageOrgsAndDepts(userId);
		// 3.合并
		List<OrgnaizationEntity> allOrgs = new ArrayList<OrgnaizationEntity>();
		allOrgs.addAll(jobs);
		allOrgs.addAll(orgs);
		for (OrgnaizationEntity job : allOrgs) {
			List<OrgnaizationEntity> lowList = orgnaizationService.getLowOrg(job);// 查找该机构下的所有子岗位
			// 去重复岗位
			for (OrgnaizationEntity low : lowList) {
				if (!underJobs.contains(low)) {
					underJobs.add(low);
				}
			}
		}

		return new ArrayList<OrgnaizationEntity>(underJobs);
	}

	/**
	 * 通过userId获得直接下属
	 * 
	 * @author xiaqiang
	 */
	public List<UserEntity> getUnderUserByUserId(String userId) {
		List<OrgnaizationEntity> orgs = getManageOrgsAndDepts(userId);
		Map<String, OrgnaizationEntity> map = new HashMap<String, OrgnaizationEntity>();
		// 用全类型方法获可管理的下属(包含自己)
		StringBuffer sb = new StringBuffer();
		String jobIds = null;
		for (OrgnaizationEntity job : orgs) {
			if (!map.containsKey(job.getId())) {
				map.put(job.getId(), job);
				sb.append(job.getId() + ",");
			}

		}
		jobIds = StringUtil.removeDot(sb.toString());
		return getUserByCurrentOrgIds(jobIds);
	}

	/**
	 * 通过userId获得直接领导
	 * 
	 * @author xiaqiang
	 */
	@Override
	public List<UserEntity> getDirectLeaderByUserId(String userId) {

		List<OrgnaizationEntity> orgs = getAllOrganizationsByUserId(userId);
		List<UserEntity> userList = new ArrayList<UserEntity>();
		Set<String> set = new HashSet<String>();
		if (orgs != null && orgs.size() > 0) {
			for (OrgnaizationEntity org : orgs) {
				// 机构管理员
				if (StringUtil.isNotEmpty(org.getManagerUserId()) && !StringUtil.equals(userId, org.getManagerUserId())) {
					if (!set.contains(org.getManagerUserId())) {
						set.add(org.getManagerUserId());
						userList.add(this.getUserById(org.getManagerUserId()));
					}
				}
				// 分管领导
				if (StringUtil.isNotEmpty(org.getLeaderUserId()) && !StringUtil.equals(userId, org.getLeaderUserId())) {
					if (!set.contains(org.getLeaderUserId())) {
						set.add(org.getLeaderUserId());
						userList.add(this.getUserById(org.getLeaderUserId()));
					}
				}
			}
		}
		return userList;
	}

	/*@Override
	public UserEntity getUserInfoById(String userId) {
		// TODO Auto-generated method stub
		return this.sysUserMybatisDao.getUserInfoById(userId);
	}*/

	@Override
	public List<UserEntity> getUserByIds(String userIds) {
		List<UserEntity> userList = new ArrayList<UserEntity>();
		if (StringUtil.isNotEmpty(userIds)) {
			String hql = "FROM UserEntity WHERE id in(:ids)";
			Query q = this.commonDao.getSession().createQuery(hql);
			q.setParameterList("ids", userIds.split(","));
			userList = q.list();
		}
		return userList;
	}

	@Override
	public List<UserEntity> getDistinctUsers(List<UserEntity> users) {
		Map<String, UserEntity> map = new HashMap<String, UserEntity>();
		for (UserEntity user : users) {
			if (!map.containsKey(user.getId())) {
				map.put(user.getId(), user);
			}
		}
		return new ArrayList<UserEntity>(map.values());
	}

	@Override
	public List<OrgnaizationEntity> getDistinctOrgs(List<OrgnaizationEntity> orgs) {
		Map<String, OrgnaizationEntity> map = new HashMap<String, OrgnaizationEntity>();
		for (OrgnaizationEntity org : orgs) {
			if (!map.containsKey(org.getId())) {
				map.put(org.getId(), org);
			}
		}
		return new ArrayList<OrgnaizationEntity>(map.values());
	}

	@Override
	public String parseOrgListToStr(List<OrgnaizationEntity> orgList) {
		StringBuffer sb = new StringBuffer();
		for (OrgnaizationEntity org : orgList) {
			sb.append(org.getId() + ",");
		}
		return StringUtil.removeDot(sb.toString());
	}

	@Override
	public List<UserEntity> getDistinctUsersByMulType(String receive) {
		List<UserEntity> userList = new ArrayList<UserEntity>();
		if (StringUtil.isNotEmpty(receive)) {
			String[] receiveArray = receive.split(",");
			for (String receiveStr : receiveArray) {
				String[] mulType = receiveStr.split("^^");
				String id = mulType[0];
				String type = mulType[2];
				if ("org".equals(type)) {
					List<UserEntity> orgAddList = getDistinctUserByOrgIds(id);
					userList.addAll(orgAddList);
				} else if ("role".equals(type)) {
					List<UserEntity> roleAddList = getDistinctUserByRoles(id);
					userList.addAll(roleAddList);
				}
			}
		}
		return getDistinctUsers(userList);
	}
	
	@Override
	public List<String> getDistinctUpOrgIds(String userId) {
		List<OrgnaizationEntity> orgList = getAllOrganizationsByUserId(userId);
		Map<String, String> result = new HashMap<String, String>();// 不可操作的
		for (OrgnaizationEntity org : orgList) {
			String[] treeArray = org.getTreeIndex().split(",");
			for (String orgId : treeArray) {
				result.put(orgId, orgId);
			}
		}
		return new ArrayList<String>(result.values());
	}
	
	@Override
	public List<UserEntity> getDistinctMulUsers(String finalValue) throws BusinessException {
		List<Map> finalValueList = JSONHelper.toList(finalValue, Map.class);
		Map<String, UserEntity> result = new HashMap<String, UserEntity>();
		for (int i = 0; i < finalValueList.size(); i++) {
			Map item = finalValueList.get(i);
			String type = item.get("type").toString();
			String id = item.get("id").toString();
			String name = item.get("name").toString();

			if (BusinessConst.mulSelect_user.equals(type)) {
				if (!result.containsKey(id)) {
					UserEntity user = new UserEntity();
					user.setId(id);
					user.setName(name);
					result.put(id, user);
				}
			} else if (BusinessConst.mulSelect_org.equals(type)) {
				List<UserEntity> orgUsers = getUserByOrgIds(id);
				for (UserEntity user : orgUsers) {
					if (!result.containsKey(user.getId())) {
						result.put(user.getId(), user);
					}
				}
			} else if (BusinessConst.mulSelect_role.equals(type)) {
				List<Map<String, Object>> roleUsers = userRoleService.queryRoleUsers(id);
				for (Map<String, Object> map : roleUsers) {
					if (!result.containsKey(map.get("id").toString())) {
						UserEntity user = new UserEntity();
						user.setId(map.get("id").toString());
						user.setName(map.get("name").toString());
						result.put(map.get("id").toString(), user);
					}
				}
			}
		}
		return new ArrayList<UserEntity>(result.values());
	}
	
	@Override
	public List<UserTypeVo> getMulUsers(String finalValue) {
		List<UserTypeVo> user=new ArrayList<UserTypeVo>();
		List<Map> finalValueList = JSONHelper.toList(finalValue, Map.class);
		if(finalValueList!=null && finalValueList.size()>0){
			for (int i = 0; i < finalValueList.size(); i++) {
				Map item = finalValueList.get(i);
				String type = item.get("type").toString();
				String id = item.get("id").toString();
				String name = item.get("name").toString();
				UserTypeVo v=new UserTypeVo();
				v.setId(id);
				v.setName(name);
				v.setType(type);
				user.add(v);
			}
		}
		return user;
	}
	
	@Override
	public List<String> getDistinctMulUserIds(String finalValue) throws BusinessException {
		List<UserEntity> list = getDistinctMulUsers(finalValue);
		List<String> userIdsList = new ArrayList<String>();
		for (UserEntity user : list) {
			userIdsList.add(user.getId());
		}
		return userIdsList;
	}
	
	
	@Override
	public List<UserEntity> getUserListByRoleId(String roleId) {
		String hql = "SELECT distinct u FROM UserEntity u,UserRoleEntity ur WHERE ur.user.id=u.id and ur.role.id=?";
		return this.commonDao.findHql(hql, new Object[] { roleId });
	}

	@Override
	public String getOrgIdsByUserId(String userId) {
		// TODO Auto-generated method stub
		//获取用户所在的机构或者部门
		String orgIds="";
		String hql = "SELECT distinct uo.org FROM UserOrgEntity uo WHERE uo.user.id=?";
		List<OrgnaizationEntity> orgList= this.commonDao.findHql(hql, new Object[] { userId });
		//查询上一级
		if(orgList!=null && orgList.size()>0){
			for(OrgnaizationEntity org:orgList){
				String[] treeIndex=org.getTreeIndex().split(",");
				for(String o:treeIndex){
					if(!(StringUtil.indexOf(orgIds, o)>-1)){
						orgIds+="'"+o+"',";
					}
				}
			}
			return StringUtil.removeDot(orgIds);
		}
		return orgIds;
	}
	
	@Override
	public String getUserIds(List<UserEntity> list) {
		String userIds = "";
		for (UserEntity user : list) {
			userIds += user.getId() + ",";
		}
		return StringUtil.removeDot(userIds);
	}
}
