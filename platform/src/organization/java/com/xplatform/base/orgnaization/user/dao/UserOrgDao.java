package com.xplatform.base.orgnaization.user.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.user.entity.UserOrgEntity;

/**
 * 
 * description :   员工岗位管理
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年12月20日 下午9:47:17
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                        修改内容
 * -----------  ------------------- -----------------------------------
 * hexj         2014年12月20日 下午9:47:17
 *
 */
public interface UserOrgDao extends ICommonDao{

	public String addUserJob(UserOrgEntity userJobEntity);
	
	public void deleteUserJob(String id);
	
	public void updateUserJob(UserOrgEntity userJobEntity);
	
	public UserOrgEntity getUserJob(String id);

	public List<UserOrgEntity> queryUserJobList();
	
	public void DataGrid(CriteriaQuery cq, boolean b);

	public List<UserOrgEntity> queryUserJobListByUserId(String UserId);
	
	public List<UserOrgEntity> queryUserJobListByJobId(String jobId);
	
}
