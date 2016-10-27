package com.xplatform.base.orgnaization.module.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.orgnaization.module.mybatis.vo.ModuleTreeVo;

public interface ModuleMybatisDao{
	
	public List<ModuleTreeVo> queryModuleAuthorityByRole(Map<String,String> param);
	
	public List<ModuleTreeVo> queryModuleAuthorityByUser(Map<String,String> param);

	public List<ModuleTreeVo> queryModuleAuthority(Map<String,Object> param);
	
	public List<ModuleTreeVo> queryAllModuleAuthority(Map<String,Object> param);
	
}
