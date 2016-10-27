package com.xplatform.base.platform.common.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.module.dao.ModuleDao;
import com.xplatform.base.orgnaization.module.entity.ModuleEntity;
import com.xplatform.base.orgnaization.module.mybatis.dao.ModuleMybatisDao;
import com.xplatform.base.orgnaization.module.mybatis.vo.ModuleTreeVo;
import com.xplatform.base.orgnaization.resouce.dao.ResourceDao;
import com.xplatform.base.orgnaization.resouce.entity.ResourceEntity;
import com.xplatform.base.orgnaization.resouce.mybatis.dao.ResourceMybatisDao;
import com.xplatform.base.orgnaization.resouce.mybatis.vo.ResourceVo;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.role.service.RoleService;
import com.xplatform.base.orgnaization.user.dao.UserDao;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.service.UserService;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Resource
	private UserDao userDao;

	@Resource
	private ResourceMybatisDao resourceMybatisDao;

	@Resource
	private ResourceDao resourceDao;

	@Resource
	private ModuleMybatisDao moduleMybatisDao;

	@Resource
	private ModuleDao moduleDao;
	
	@Resource
	private SysUserService sysUserService;
	@Resource
	private UserService userService;
	
	@Resource
	private RoleService roleService;

	@Override
	public boolean isAdmin(Map<String, String> param) {
		return isAdmin(param.get("userId"));
	}

	@Override
	public boolean isAdmin(String userId) {
		UserEntity user = userDao.getUser(userId);
		List<RoleEntity> roleList = roleService.querySuperAdminRole(userId);
		// if (user != null && StringUtil.equals(user.getId(), "1")) {
		// return true;
		// }
		// List<RoleEntity> roleList =
		// sysUserService.getRoleListByUserId(userId);
		for (RoleEntity role : roleList) {
			if (StringUtil.equals(RoleEntity.SUPER_ADMIN, role.getCode())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isOrgAdmin(String userId,String orgId) {
		List<UserEntity> userList=this.userDao.findHql("select ur.user from UserRoleEntity ur where ur.role.id=? and ur.orgId=? and ur.user.id=?",RoleEntity.COMPANY_ADMIN,orgId,userId);
		if(userList!=null && userList.size()>0){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isOrgAdmin(String orgId) {
		return isOrgAdmin(ClientUtil.getUserId(), orgId);
	}

	@Override
	public List<ModuleTreeVo> getUserModule(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return moduleMybatisDao.queryModuleAuthority(param);
	}

	/**
	 * 获取所有模块权限值
	 * <pre>
	 * </pre>
	 * @param map
	 * @return
	 */
	public List<ModuleTreeVo> getAllModule(Map<String, Object> param) {
		return moduleMybatisDao.queryAllModuleAuthority(param);
	}

	@Override
	public List<ResourceVo> getUserModuleResources(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return resourceMybatisDao.queryResourceAuthority(param);
	}

	@Override
	public boolean checkUrlResource(String url) {
		// TODO Auto-generated method stub
		List<ResourceEntity> ResourceList = resourceDao.findByProperty(ResourceEntity.class, "url", url);
		if (ResourceList != null && ResourceList.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public ModuleEntity getUrlModule(String url) {
		// TODO Auto-generated method stub
		List<ModuleEntity> moduleList = moduleDao.findByProperty(ModuleEntity.class, "url", url);
		if (moduleList != null && moduleList.size() > 0) {
			ModuleEntity module = moduleList.get(0);
			return module;
		}
		return null;
	}

	@Override
	public ModuleEntity getResourceModule(String url, HttpServletRequest request) {
		// TODO Auto-generated method stubrequest
		//模块中配置了url,直接查询到所在的模块
		List<ResourceEntity> ResourceList = resourceDao.findByProperty(ResourceEntity.class, "url", url);
		if (ResourceList != null && ResourceList.size() > 0) {
			ResourceEntity resource = ResourceList.get(0);
			return resource.getModule();
		}
		//模块中没有url,参数带了moduleCode，查询这个模块下的权限集合
		String moduleCode = request.getParameter("moduleCode");
		List<ModuleEntity> list = moduleDao.findByProperty(ModuleEntity.class, "code", moduleCode);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		//返回控制
		return null;
	}
	
	@Override
	public boolean currentUserIsAdmin() {
		if (StringUtil.equals(ClientUtil.getUserId(), "1")) {
			return true;
		} else {
			List<RoleEntity> roleList = this.sysUserService.getRoleListByUserId(ClientUtil.getUserId());
			for (RoleEntity role : roleList) {
				if (StringUtil.equals("admin", role.getCode())) {
					return true;
				}
			}
		}
		return false;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setResourceMybatisDao(ResourceMybatisDao resourceMybatisDao) {
		this.resourceMybatisDao = resourceMybatisDao;
	}

	public void setModuleMybatisDao(ModuleMybatisDao moduleMybatisDao) {
		this.moduleMybatisDao = moduleMybatisDao;
	}

	public void setResourceDao(ResourceDao resourceDao) {
		this.resourceDao = resourceDao;
	}

	public void setModuleDao(ModuleDao moduleDao) {
		this.moduleDao = moduleDao;
	}

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}
	
	
}
