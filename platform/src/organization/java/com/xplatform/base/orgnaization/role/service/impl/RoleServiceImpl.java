package com.xplatform.base.orgnaization.role.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.PinyinUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupService;
import com.xplatform.base.orgnaization.role.dao.RoleDao;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.role.mybatis.dao.RoleMybatisDao;
import com.xplatform.base.orgnaization.role.mybatis.vo.RoleVo;
import com.xplatform.base.orgnaization.role.service.RoleService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.entity.UserRoleEntity;
import com.xplatform.base.orgnaization.user.service.UserRoleService;
import com.xplatform.base.orgnaization.user.service.UserService;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.platform.common.utils.ClientUtil;

/**
 * 
 * description :角色service实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:30:12
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiehs 2014年5月24日 下午12:30:12
 *
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {
	private static final Logger logger = Logger.getLogger(RoleServiceImpl.class);
	@Resource
	private RoleDao roleDao;

	@Resource
	private RoleMybatisDao roleMybatisDao;

	@Resource
	private BaseService baseService;

	@Resource
	private UserService userService;
	
	@Resource
	private UserRoleService userRoleService;
	
	@Resource
	private OrgGroupService orgGroupservice;
	
	@Resource
	private AuthorityService authorityService;

	@Override
	public String save(RoleEntity role,String userIds) throws BusinessException {

		String pk = "";
		try {
			//设置角色的机构id
			role.setOrgId(ClientUtil.getUserEntity().getOrgIds());
			if(StringUtil.isEmpty(role.getCode())){
				String name = role.getName().replace(" ", "");
				name = name.replaceAll("\\pP|\\pS", "");
				String code = PinyinUtil.converterToFirstSpell(name);
				role.setCode(this.getCode(code));
			}
			pk = this.roleDao.addRole(role);
			//role.setDefinedFlag(2);
			//往员工角色表里添加数据
			String[] idArr = StringUtil.split(userIds, ",");
			for(String userId : idArr){
				UserEntity user = this.userService.get(userId);
				if(user != null){
					UserRoleEntity userRole = new UserRoleEntity();
					userRole.setRole(role);
					userRole.setUser(user);
					userRole.setOrgId(ClientUtil.getUserEntity().getOrgIds());
					this.userRoleService.save(userRole);
				}
			}
		} catch (Exception e) {
			ExceptionUtil.throwBusinessException("添加失败", logger);
		}
		logger.info("角色保存成功");
		return pk;
	}

	@Override
	public void delete(String id) throws BusinessException {
		try {
			/*//删除员工角色表
			this.userRoleService.deleteByRoleId(id);
			//删除群组角色表
			this.orgGroupservice.deleteAndProcessHXByRoleId(id);*/
			this.roleDao.deleteRole(id);
			//删除员工角色表
			this.userRoleService.deleteByRoleId(id);
		} catch (Exception e) {
			// TODO: handle exception
			ExceptionUtil.throwBusinessException("角色删除失败", logger);
		}
		logger.info("角色删除成功");
	}

	@Override
	public void batchDelete(String ids) throws Exception {
		// TODO Auto-generated method stub
		if (StringUtil.isNotEmpty(ids)) {
			String[] idArr = StringUtil.split(ids, ",");
			for (String id : idArr) {
				this.delete(id);
			}
		}
		logger.info("角色批量删除成功");
	}

	@Override
	public void update(RoleEntity role) throws RuntimeException {
		try {
			RoleEntity oldEntity = get(role.getId());
			MyBeanUtils.copyBeanNotNull2Bean(role, oldEntity);
			this.roleDao.updateRole(oldEntity);
			throw new RuntimeException("角色更新失败");
		} catch (Exception e) {
			throw new RuntimeException("角色更新失败");
			//ExceptionUtil.throwBusinessException("角色更新失败", logger);
		}
		//logger.info("角色更新成功");
	}

	@Override
	public RoleEntity get(String id) {
		// TODO Auto-generated method stub
		RoleEntity Role = null;
		try {
			Role = this.roleDao.getRole(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("角色获取失败");
		}
		logger.info("角色获取成功");
		return Role;
	}

	@Override
	public List<RoleEntity> queryRoleListByAuthority(String userId) {
		// TODO Auto-generated method stub
		List<RoleEntity> RoleList = new ArrayList<RoleEntity>();
		try {
			RoleList = this.roleDao.queryRoleListByAuthority(userId);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("角色获取列表失败");
		}
		logger.info("角色获取列表成功");
		return RoleList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		try {
			this.roleDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("角色获取分页列表失败");
		}
		logger.info("角色获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param, String propertyName) {
		logger.info(propertyName + "字段唯一校验");
		return this.baseService.isUnique(RoleEntity.class, param, propertyName);
	}

	@Override
	public Page<RoleVo> queryAuthorityRoleList(Page<RoleVo> page) {
		// TODO Auto-generated method stub
		page.setResult(this.roleMybatisDao.queryAuthorityRoleByPage(page));
		return page;
	}

	@Override
	public List<RoleEntity> queryListByName(String name) {
		List<RoleEntity> roleList = new ArrayList<RoleEntity>();
		String hql = "FROM RoleEntity WHERE name like ?";
		roleList = this.roleDao.findHql(hql, "%" + name + "%");
		return roleList;
	}

	public void setroleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public List<RoleEntity> getRoleListByOrgids(List<String> empOrgList) {
		// TODO Auto-generated method stub
		String orgStr = "";
		if (empOrgList != null) {
			for (String str : empOrgList) {
				orgStr += str + ",";
			}
			orgStr = orgStr.substring(0, orgStr.length() - 1);
			String hql = "SELECT DISTINCT rr.role From OrgRoleEntity rr,RoleEntity r where r.id = rr.role.id and rr.org.id in(" + orgStr + ")";
			return this.roleDao.findHql(hql, new Object[] {});
		} else {
			return null;
		}

	}

	@Override
	public List<RoleEntity> queryRoleListByUserAndInst(String userId, String instId) {
		String hql = "SELECT a.role FROM UserRoleEntity a WHERE a.user.id=? and a.institutionId=?";
		Object[] obj = new Object[] { userId, instId };
		if (StringUtil.isEmpty(instId)) {
			hql = "SELECT a.role FROM UserRoleEntity a WHERE a.user.id=? and a.institutionId is null";
			obj = new Object[] { userId };
		}
		List<RoleEntity> roles = this.roleDao.findHql(hql, obj);
		return roles;
	}
	
	@Override
	public List<RoleEntity> querySuperAdminRole(String userId) {
		String hql = "SELECT a.role FROM UserRoleEntity a WHERE a.user.id=?";
		List<RoleEntity> roles = this.roleDao.findHql(hql, new Object[] { userId });
		return roles;
	}

	@Override
	public String queryUsersByRole(String roleId) {
		List<UserEntity> users = userService.queryUsersByRole(roleId);
		StringBuffer userIds = new StringBuffer();
		for (UserEntity user : users) {
			String userId = user.getId();
			userIds.append(userId).append(",");
		}
		return StringUtil.removeDot(userIds.toString());
	}

	@Override
	public void batchUpdateUserRole(String roleId, String userIds) throws BusinessException {
		Map<String, UserRoleEntity> map = new HashMap<String, UserRoleEntity>();
		try {
			RoleEntity role = get(roleId);
			// 1.得到文件角色权限
			List<UserRoleEntity> list = roleDao.findByProperty(UserRoleEntity.class, "role.id", roleId);
			// 2.转化为map储存
			for (UserRoleEntity ur : list) {
				map.put(ur.getUser().getId(), ur);
			}
			// 3.执行对比方法,得出增改的记录
			String[] userIdsArray = userIds.split(",");
			updateUserRoleCompare(userIdsArray, role, map);
		} catch (Exception e) {
			ExceptionUtil.throwBusinessException("分配角色出错", logger);
		}
	}

	private void updateUserRoleCompare(String[] userIdsArray, RoleEntity role, Map<String, UserRoleEntity> map) throws BusinessException {
		List<UserRoleEntity> saveEntitys = new ArrayList<UserRoleEntity>();
		List<UserRoleEntity> deleteEntitys = new ArrayList<UserRoleEntity>();
		for (String s : userIdsArray) {
			if ("".equals(s.trim())) {
				continue;
			}
			if (map.containsKey(s)) {
				map.remove(s);
			} else {
				UserRoleEntity saveEntity = new UserRoleEntity();
				UserEntity userEntity = new UserEntity();
				userEntity.setId(s);
				saveEntity.setUser(userEntity);
				saveEntity.setRole(role);
				saveEntitys.add(saveEntity);
			}
		}
		for (Entry<String, UserRoleEntity> entry : map.entrySet()) {
			deleteEntitys.add(entry.getValue());
		}
		roleDao.deleteAllEntitie(deleteEntitys);
		roleDao.batchSaveOrUpdate(saveEntitys);
	}

	@Override
	public List<RoleEntity> queryDefaultRole() {
		return roleDao.findByProperty(RoleEntity.class, "definedFlag", 1);
	}

	@Override
	public void saveUserRole(String userId, String roleId, String institutionId) {
		RoleEntity role = new RoleEntity();
		UserEntity user = new UserEntity();
		role.setId(roleId);
		user.setId(userId);
		UserRoleEntity ur = new UserRoleEntity();
		ur.setRole(role);
		ur.setUser(user);
		roleDao.save(ur);
	}

	@Override
	public void saveAdminRole(String userId, String institutionId) {
		RoleEntity admin = roleDao.findUniqueByProperty(RoleEntity.class, "code", RoleEntity.ADMIN);
		this.saveUserRole(userId, admin.getId(), institutionId);
	}

	@Override
	public RoleEntity findUniqueByProperty(Class<RoleEntity> entityClass, String propertyName, String value) {
		return roleDao.findUniqueByProperty(entityClass, propertyName, value);
	}

	@Override
	public RoleEntity queryRoleByCode(String code) {
		String hql = "from RoleEntity where code = ?";
		return this.roleDao.findUniqueByHql(hql, code);
	}

	@Override
	public void singleDelete(String id) throws Exception {
		this.roleDao.deleteRole(id);
	}
	//判断当前code是否与数据库role表中code一样，一样则改，不一样则返回code
	public String getCode(String code) throws BusinessException {

		for (int i = 0; i < 1; i++) {
			RoleEntity isExist = this.queryRoleByCode(code);
			if (isExist != null) {
				if (isExist.getCode().equals(code)) {
					String number = isExist.getCode().replaceAll("\\D+", "");
					if (StringUtil.isNotEmpty(number)) {
						int newNumner = Integer.parseInt(number) + 1;
						code = code.replaceAll("\\d+", "");
						code = code + newNumner;
						i = -1;
					} else {
						code = code + "1";
						i = -1;
					}
				}
			}
		}
		return code;
	}

	@Override
	public List<Map<String, Object>> queryMyRole(String userId) throws Exception {
		String sql = "SELECT DISTINCT a.*,c.name as ownerName FROM t_org_role a,t_org_user c where a.createUserId=? and a.createUserId=c.id ORDER BY a.createTime DESC";
		return this.roleDao.findForJdbc(sql, userId);
	}



}
