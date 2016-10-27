package com.xplatform.base.orgnaization.module.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.module.entity.ModuleEntity;
import com.xplatform.base.orgnaization.module.mybatis.vo.ModuleTreeVo;

/**
 * 
 * description : 模块管理service
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:34:52
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:34:52
 *
 */
public interface ModuleService {
	
	/**
	 * 新增模块
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param job
	 * @return
	 */
	public String save(ModuleEntity module) throws BusinessException;
	
	/**
	 * 删除模块
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除模块
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws BusinessException;
	
	/**
	 * 更新模块
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param job
	 * @return
	 */
	public void update(ModuleEntity module) throws BusinessException;
	
	/**
	 * 查询一条模块记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public ModuleEntity get(String id) ;
	
	public String getName(String id);
	
	/**
	 * 查询模块列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<ModuleEntity> queryList() throws BusinessException;
	
	
	/**
	 * 通过父Id与组织Id获得下属节点
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<ModuleEntity> queryModulesByCondition(String parentId, String institutionId);
	
	/**
	 * hibernate模块分页列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:34:16
	 * @Decription   
	 * @param cq
	 * @param b
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b);
	
	
	/**
	 * 更新角色的模块权限
	 * @author xiehs
	 * @createtime 2014年5月27日 下午8:31:54
	 * @Decription
	 *
	 * @param moduleIds
	 */
	public void updateRoleModuleAuthority(String moduleIds,String roleId) throws BusinessException;
	
	/**
	 * 根据用户ID删除用户所属权限
	 * @author luoheng
	 * @param userId
	 */
	public void deleteUserModuleByUserId(String userId) throws BusinessException;
	
	/**
	 * 更新用户的模块权限
	 * @author xiehs
	 * @createtime 2014年5月27日 下午8:31:54
	 * @Decription
	 *
	 * @param moduleIds
	 */
	//public void updateUserModuleAuthority(String moduleIds,String userId) throws BusinessException;
	
	/**
	 * 查询角色的模块权限
	 * @author xiehs
	 * @createtime 2014年5月27日 下午8:31:54
	 * @Decription
	 *
	 * @param moduleIds
	 */
	public List<ModuleTreeVo> queryModuleAuthorityByRole(String roleId,String parentId);
	
	/**
	 * 查询用户的模块权限
	 * @author xiehs
	 * @createtime 2014年5月27日 下午8:31:54
	 * @Decription
	 *
	 * @param moduleIds
	 */
	public List<ModuleTreeVo> queryModuleAuthorityByUser(String userId,String parentId);
	
	
	public List<ModuleEntity> findByProperty(Class t,String propertyName,Object value);
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年7月21日 下午4:53:03
	 * @Decription 
	 *
	 * @param parentId
	 * @return
	 * @throws BusinessException
	 */
	public ModuleEntity getRoot(String parentId);
}
