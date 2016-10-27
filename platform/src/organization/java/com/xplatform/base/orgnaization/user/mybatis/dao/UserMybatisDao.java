package com.xplatform.base.orgnaization.user.mybatis.dao;

import java.util.List;
import java.util.Map;

public interface UserMybatisDao {
	/**
	 * 得到用户的详细信息
	 * @author xiehs
	 * @createtime 2014年6月8日 下午4:10:50
	 * @Decription
	 *
	 * @param param
	 * @return
	 *//*
	public EmpUserVo getUserInfo(Map<String,String> param);
	
	*//**
	 * 查询可管理的用户列表
	 * @author xiehs
	 * @createtime 2014年6月8日 下午4:11:06
	 * @Decription
	 *
	 * @param page
	 * @return
	 *//*
	public List<EmpUserVo> queryAuthorityUserByPage(Page<EmpUserVo> page);

	
	*//**
	 * 查询可选择的员工，已存在账号的员工排除
	 * @author binyong
	 * @createtime 2014年6月28日 
	 * @Decription
	 *
	 * @param page
	 * @return
	 *//*
	public List<EmpDeptVo> queryEmpDeptByPage(Page<EmpDeptVo> page);*/

	public List<Map<String,String>> getUserOrgTreeIndex(
			Map<String, String> param);
}
