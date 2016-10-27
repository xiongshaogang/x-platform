package com.xplatform.base.orgnaization.orgnaization.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.orgnaization.orgnaization.mybatis.vo.OrgTreeVo;

public interface OrgMybatisDao {

	/**
	 * 查询员工分配可控制的机构树
	 * @param param
	 * @return
	 */
	public List<OrgTreeVo> queryOrgTreeByGrade(Map<String,String> param);
	
	/**
	 * 查询可控制机构树
	 * @param param
	 * @return
	 */
	public List<OrgTreeVo> queryOrgTree(Map<String,String> param);
	
	/**
	 * 查询员工分配部门机构树
	 * @param param
	 * @return
	 */
	public List<OrgTreeVo> queryEmpOrgTree(Map<String,String> param);
	
}
