package com.xplatform.base.orgnaization.orgnaization.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgRoleEntity;

/**
 * 
 * description : 部门/岗位  角色管理
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年12月15日 下午3:58:04
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                        修改内容
 * -----------  ------------------- -----------------------------------
 * hexj         2014年12月15日 下午3:58:04
 *
 */
public interface OrgRoleDao extends ICommonDao{


	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月15日 下午4:06:59
	 * @Decription  add
	 *
	 * @param orgRole
	 * @return
	 */
	public String addOrgRole(OrgRoleEntity orgRole);
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月15日 下午4:07:08
	 * @Decription    delete
	 *
	 * @param id
	 */
	public void deleteOrgRole(String id);
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月15日 下午4:07:20
	 * @Decription  update
	 *
	 * @param orgRole
	 */
	public void updateOrgRole (OrgRoleEntity orgRole);
	
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月15日 下午4:07:35
	 * @Decription   获取 部门/岗位  角色
	 *
	 * @param id
	 * @return
	 */
	public OrgRoleEntity getOrgRole(String id);
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月15日 下午4:07:35
	 * @Decription   获取 部门/岗位  角色
	 *
	 * @param id
	 * @return
	 */
	public List<OrgRoleEntity> queryOrgRoleList();
	
	public OrgRoleEntity getOrgRole(String roleId, String orgId);
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月15日 下午4:04:48
	 * @Decription  通过 部门/岗位 ID  获取  部门/岗位  角色
	 *
	 * @param orgId
	 * @return
	 * @throws BusinessException
	 */
	public List<OrgRoleEntity> queryOrgRoleByOrgIdList(String orgId);
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月15日 下午4:04:48
	 * @Decription  通过 角色ID获取 机构 角色
	 *
	 * @param orgId
	 * @return
	 * @throws BusinessException
	 */
	public List<OrgRoleEntity> queryOrgRolesByRoleId(String roleId);
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午9:58:12
	 * @Decription hibernate分页查询
	 * @param cq
	 * @param b
	 */
	public void DataGrid(CriteriaQuery cq, boolean b);
	
}
