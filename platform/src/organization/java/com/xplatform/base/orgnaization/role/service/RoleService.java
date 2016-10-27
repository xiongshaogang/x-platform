package com.xplatform.base.orgnaization.role.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.role.mybatis.vo.RoleVo;
import com.xplatform.base.orgnaization.user.entity.UserRoleEntity;

/**
 * 
 * description : 角色管理service
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:34:52
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiehs 2014年5月24日 上午11:34:52
 *
 */
public interface RoleService {

	/**
	 * 新增角色
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription
	 * @param Role
	 * @return
	 */
	public String save(RoleEntity role,String userIds) throws Exception;

	/**
	 * 删除角色
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws Exception;
	
	/**
	 * 仅仅删除角色
	 * 
	 * @author lixt
	 * @createtime 2016年01月14日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void singleDelete(String id) throws Exception;

	/**
	 * 批量删除角色
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception;

	/**
	 * 更新角色
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param Role
	 * @return
	 */
	public void update(RoleEntity role) throws RuntimeException;

	/**
	 * 查询一条角色记录
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public RoleEntity get(String id);

	/**
	 * 查询角色列表
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<RoleEntity> queryRoleListByAuthority(String userId);
	
	/**
	 * 根据code查询角色
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public RoleEntity queryRoleByCode(String code);

	/**
	 * hibernate角色分页列表
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:34:16
	 * @Decription
	 * @param cq
	 * @param b
	 */
	public void getDataGridReturn(CriteriaQuery cq, boolean b);

	/**
	 * 判断字段记录是否唯一
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:25:48
	 * @Decription
	 * @param param
	 * @return
	 */
	public boolean isUnique(Map<String, String> param, String propertyName);

	/**
	 * 分页获取角色权限
	 * 
	 * @author xiehs
	 * @createtime 2014年6月8日 上午11:08:04
	 * @Decription
	 *
	 * @param page
	 * @return
	 */
	public Page<RoleVo> queryAuthorityRoleList(Page<RoleVo> page);

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月17日 上午11:03:41
	 * @Decription 根据角色名模糊查询所有角色列表
	 *
	 * @return
	 */
	public List<RoleEntity> queryListByName(String name);

	/**
	 * @author biny
	 * @Decription 根据用户的组织机构id获取该用户拥有的角色
	 * @return
	 */
	public List<RoleEntity> getRoleListByOrgids(List<String> empOrgList);

	/**
	 * 通过用户id和组织id查找在某组织下拥有的角色
	 * 
	 * @author xiaqiang
	 * @createtime 2015年5月22日 上午10:14:58
	 *
	 * @param userId
	 * @param instId
	 * @return
	 */
	public List<RoleEntity> queryRoleListByUserAndInst(String userId, String instId);

	/**
	 * 通过角色Id获取用户Ids(逗号隔开字符串)
	 * 
	 * @author xiaqiang
	 * @createtime 2015年5月24日 上午10:49:52
	 *
	 * @param roleId
	 * @return
	 */
	public String queryUsersByRole(String roleId);

	/**
	 * 批量更新用户角色中间表记录
	 * 
	 * @author xiaqiang
	 * @createtime 2015年5月24日 下午1:23:37
	 *
	 * @param roleId
	 * @param userIds
	 */
	public void batchUpdateUserRole(String roleId, String userIds) throws BusinessException;

	/**
	 * 查询默认的角色集合
	 * 
	 * @author xiaqiang
	 * @createtime 2015年5月28日 上午11:03:43
	 *
	 * @return
	 */
	public List<RoleEntity> queryDefaultRole();

	/**
	 * 保存用户角色记录(单条)
	 * 
	 * @author xiaqiang
	 * @createtime 2015年5月29日 上午10:52:45
	 *
	 * @param userId
	 * @param roleId
	 */
	public void saveUserRole(String userId, String roleId, String institutionId);

	/**
	 * 为一个用户指定"组织管理员"角色
	 * 
	 * @author xiaqiang
	 * @createtime 2015年5月29日 上午10:53:11
	 *
	 * @param userId
	 */
	public void saveAdminRole(String userId, String institutionId);

	public RoleEntity findUniqueByProperty(Class<RoleEntity> entityClass, String propertyName, String value);
	
	/**
	 * 查询平台管理员集合
	 * @author xiaqiang
	 * @createtime 2015年6月2日 下午5:20:22
	 *
	 * @param userId
	 * @return
	 */
	public List<RoleEntity> querySuperAdminRole(String userId);
	
	/**
	 * 查询某个用户的角色
	 * @author lixt
	 * @createtime 2016年1月19日 下午5:20:22
	 *
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> queryMyRole(String userId) throws Exception;
	

}
