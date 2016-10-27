package com.xplatform.base.system.log.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.log.entity.UserLogEntity;

public interface UserLogDao extends ICommonDao {
/**
	 * @Decription 新增
	 */
	public String addUserLog(UserLogEntity userLog);
	
	/**
	 * @Decription 删除
	 */
	public void deleteUserLog(String id);
	
	/**
	 * 
	 * @Decription 修改
	 */
	public void updateUserLog (UserLogEntity userLog);
	
	
	/**
	 * 
	 * @Decription 通过id查询单条记录
	 */
	public UserLogEntity getUserLog(String id);
	
	/**
	 * 
	 * @Decription 查询所有的记录
	 */
	public List<UserLogEntity> queryUserLogList();
	
	/**
	 * 
	 * @Decription hibernate分页查询
	 */
	public void DataGrid(CriteriaQuery cq, boolean b);
}
