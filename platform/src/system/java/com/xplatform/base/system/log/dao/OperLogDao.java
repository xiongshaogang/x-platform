package com.xplatform.base.system.log.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.log.entity.OperLogEntity;

public interface OperLogDao extends ICommonDao {
/**
	 * @Decription 新增
	 */
	public String addOperLog(OperLogEntity operLog);
	
	/**
	 * @Decription 删除
	 */
	public void deleteOperLog(String id);
	
	/**
	 * 
	 * @Decription 修改
	 */
	public void updateOperLog (OperLogEntity operLog);
	
	
	/**
	 * 
	 * @Decription 通过id查询单条记录
	 */
	public OperLogEntity getOperLog(String id);
	
	/**
	 * 
	 * @Decription 查询所有的记录
	 */
	public List<OperLogEntity> queryOperLogList();
	
	/**
	 * 
	 * @Decription hibernate分页查询
	 */
	public void DataGrid(CriteriaQuery cq, boolean b);
}
