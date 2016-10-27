package com.xplatform.base.orgnaization.resouce.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.resouce.entity.ResourceEntity;
import com.xplatform.base.orgnaization.role.entity.RoleResourceEntity;
import com.xplatform.base.system.type.entity.FileTypeRoleAuthorityEntity;
import com.xplatform.base.system.type.entity.FileTypeUserAuthorityEntity;

/**
 * 
 * description : 资源管理service
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
public interface ResourceService {

	/**
	 * 新增资源
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param resource
	 * @return
	 */
	public String save(ResourceEntity resource) throws BusinessException;

	/**
	 * 删除资源
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;

	/**
	 * 批量删除资源
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws BusinessException;

	/**
	 * 更新资源
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param resource
	 * @return
	 */
	public void update(ResourceEntity resource) throws BusinessException;

	/**
	 * 查询一条资源记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public ResourceEntity get(String id);

	/**
	 * 查询资源列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<ResourceEntity> queryList();

	/**
	 * hibernate资源分页列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:34:16
	 * @Decription   
	 * @param cq
	 * @param b
	 */
	public void getDataGridReturn(CriteriaQuery cq, boolean b);

	/**
	 * 判断字段记录是否唯一
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:25:48
	 * @Decription 
	 * @param param
	 * @return
	 */
	public boolean isUnique(Map<String, String> param, String propertyName);

	/**
	 * 通过模块查询所有的资源
	 * @author xiehs
	 * @createtime 2014年5月28日 上午10:49:41
	 * @Decription
	 *
	 * @param moduleId
	 * @return
	 */
	public List<ResourceEntity> queryListByModuleId(String moduleId);

	/**
	 * 通过角色和模块查询所有的资源权限
	 * @author xiehs
	 * @createtime 2014年5月28日 上午10:49:55
	 * @Decription
	 *
	 * @param roleId
	 * @param moduleId
	 * @return
	 */
	public List<RoleResourceEntity> queryResourceAuthorityByRole(String roleId, String moduleId);

	/**
	 * 通过用户和模块查询所有的资源权限
	 * @author xiehs
	 * @createtime 2014年5月28日 上午10:50:21
	 * @Decription
	 *
	 * @param userId
	 * @param moduleId
	 * @return
	 */
	//public List<UserResourceEntity> queryResourceAuthorityByUser(String userId, String moduleId);

	/**
	 * 更新角色资源权限
	 * @author xiehs
	 * @createtime 2014年5月28日 上午11:32:30
	 * @Decription
	 *
	 * @param moduleId
	 * @param roleId
	 * @param resourceIds
	 */
	public void updateRoleResourceAuthority(String moduleId, String roleId, String resourceIds);

	/**
	 * 更新用户资源权限
	 * @author xiehs
	 * @createtime 2014年5月28日 上午11:32:30
	 * @Decription
	 *
	 * @param moduleId
	 * @param roleId
	 * @param resourceIds
	 */
	//public void updateUserResourceAuthority(String moduleId, String userId, String resourceIds);

	/**
	 * @author xiaqiang
	 * @createtime 2014年6月21日 上午10:28:47
	 * @Decription 通过操作code查询对应的type(公共/模块)
	 *
	 * @param optCode
	 * @return
	 */

	public String queryFilterTypeByCode(String optCode);

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月8日 上午11:04:39
	 * @Decription 通过模块code查找模块下的资源权限
	 *
	 * @param moduleCode
	 * @return
	 */
	public List<ResourceEntity> queryResourceListByModuleCode(String moduleCode);

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月8日 上午11:46:55
	 * @Decription 查找资料文件用户操作权限
	 *
	 * @param typeId 资料分类Id
	 * @param userId 用户Id
	 * @return
	 * @throws BusinessException
	 */
	public List<FileTypeUserAuthorityEntity> queryUserFileTypeAuthority(String typeId, String userId);

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月8日 上午11:46:55
	 * @Decription 查找资料文件角色操作权限
	 *
	 * @param typeId 资料分类Id
	 * @param roleId 角色Id
	 * @return
	 * @throws BusinessException
	 */
	public List<FileTypeRoleAuthorityEntity> queryRoleFileTypeAuthority(String typeId, String roleId);
	
	/**
	 * @author binyong
	 * @Decription 按属性查找数据
	 *
	 * @return
	 * @throws BusinessException
	 */
	public List<ResourceEntity> findByPropertys(Map<String,String> param);

}
