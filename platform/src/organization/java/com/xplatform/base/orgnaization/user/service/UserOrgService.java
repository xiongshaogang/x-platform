package com.xplatform.base.orgnaization.user.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.user.entity.UserOrgEntity;

/**
 * 
 * description :   员工岗位管理
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年12月20日 下午9:38:52
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                        修改内容
 * -----------  ------------------- -----------------------------------
 * hexj         2014年12月20日 下午9:38:52
 *
 */
public interface UserOrgService {
	
	public String save(UserOrgEntity userJobEntity) throws BusinessException ;
	
	public void delete(String ids) throws BusinessException ;
	
	public void batchDelete(String ids) throws Exception;

	public void update(UserOrgEntity userJobEntity) throws BusinessException ;
	
	public UserOrgEntity get(String id) ;
	
	public List<UserOrgEntity> queryList() throws BusinessException ;
	
	/**
	 * 通过属性查询部门列表
	 * @author xiehs/hexj
	 * @createtime 2014年12月9日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<UserOrgEntity> queryListByPorperty(String propertyName,String value);
	
	public boolean isUnique(Map<String,String> param,String propertyName);
	
	public UserOrgEntity getUserJobByUserId(String userId) throws BusinessException ;
	
	public UserOrgEntity findByUserJob(String userId, String jobId);
	
	public List<UserOrgEntity> findByUserId(String userId);
	
	public List<UserOrgEntity> findByJobId(String jobId);
	
	
	/**
	 * hibernate部门分页列表
	 * @author xiehs/hexj
	 * @createtime 2014年12月9日 上午11:34:16
	 * @Decription   
	 * @param cq
	 * @param b
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException ;
	
	
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月20日 下午9:23:02
	 * @Decription  员工分配岗位
	 *
	 * @param UserId
	 * @param jobIds
	 * @throws BusinessException
	 */
	public void updateUserJobs(String userId, String jobIds,String orgId) throws BusinessException;
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月20日 下午9:23:13
	 * @Decription  岗位分配员工
	 *
	 * @param UserIds
	 * @param id
	 * @throws BusinessException
	 */
	public void updateJobUsers(String jobId, String userIds) throws BusinessException;
	
	/**
	 * 将用户添加到某个机构
	 * @param orgId
	 * @param phone
	 * @param name
	 * @param type
	 * @throws Exception
	 */
	public void addUserToOrg(String orgId, String phone, String name, String type) throws Exception;
	
	public List<OrgnaizationEntity> getDeptsByOrg(String userId,String orgId);
	
	public void addUserJobs(String userId, String jobIds) throws BusinessException;
	public void addJobUsers(String userIds, String jobId) throws BusinessException;
	public boolean isExistCompany(String userId,String orgId);
	
	public void deleteUserFromDept(String userIds, String orgId) throws BusinessException;
	/**
	 * 删除团队中的负责人
	 * @param orgId
	 * @param userId
	 */
	public void deleteManagerUserFromDept(String orgId,String userId);
	
}
