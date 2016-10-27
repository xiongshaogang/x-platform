package com.xplatform.base.orgnaization.orgnaization.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgRoleEntity;

/**
 * 
 * description :  部门/岗位  角色管理
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年12月15日 下午3:59:50
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                        修改内容
 * -----------  ------------------- -----------------------------------
 * hexj         2014年12月15日 下午3:59:50
 *
 */
public interface OrgRoleService {
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月15日 下午4:02:23
	 * @Decription  新增部门/岗位  角色
	 *
	 * @param orgRole
	 * @return
	 * @throws BusinessException
	 */
	public String save(OrgRoleEntity orgRole) throws BusinessException ;
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月15日 下午4:02:58
	 * @Decription   删除部门/岗位  角色
	 *
	 * @param id
	 * @throws BusinessException
	 */
	public void delete(String id) throws BusinessException ;
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月15日 下午4:03:15
	 * @Decription 批量删除 部门/岗位  角色
	 *
	 * @param ids
	 * @throws Exception
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月15日 下午4:03:32
	 * @Decription 更新 部门/岗位  角色
	 *
	 * @param orgRole
	 * @throws BusinessException
	 */
	public void update(OrgRoleEntity orgRole) throws BusinessException ;
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月17日 下午3:26:32
	 * @Decription   保存机构角色， 机构分配角色
	 *
	 * @param roleIds
	 * @param orgId
	 * @throws BusinessException
	 */
	public void updateOrgRole(String roleIds, String orgId) throws BusinessException ;
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月17日 下午3:26:32
	 * @Decription   保存角色机构，为角色分配机构
	 *
	 * @param roleIds
	 * @param orgId
	 * @throws BusinessException
	 */
	public void updateRoleOrg(String roleId, String orgIds) throws BusinessException ;
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月15日 下午4:03:54
	 * @Decription 获取 部门/岗位  角色
	 *
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public OrgRoleEntity get(String id) throws BusinessException ;
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月15日 下午4:03:54
	 * @Decription 获取 部门/岗位  角色
	 *
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public List<OrgRoleEntity> queryList() throws BusinessException ;
	
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
	 * @Decription  通过 部门/岗位 ID  获取  部门/岗位  角色
	 *
	 * @param orgId
	 * @return
	 * @throws BusinessException
	 */
	public List<OrgRoleEntity> queryOrgRolesByRoleId(String roleId) ;
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月15日 下午4:06:01
	 * @Decription
	 *
	 * @param cq
	 * @param b
	 * @throws BusinessException
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException ;
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月15日 下午4:06:10
	 * @Decription
	 *
	 * @param param
	 * @param propertyName
	 * @return
	 */
	public boolean isUnique(Map<String,String> param,String propertyName);
}
