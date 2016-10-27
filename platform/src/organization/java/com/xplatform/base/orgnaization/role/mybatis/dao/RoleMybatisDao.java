package com.xplatform.base.orgnaization.role.mybatis.dao;

import java.util.List;

import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.orgnaization.role.mybatis.vo.RoleVo;

public interface RoleMybatisDao {
	public List<RoleVo> queryAuthorityRoleByPage(Page<RoleVo> page);
}
