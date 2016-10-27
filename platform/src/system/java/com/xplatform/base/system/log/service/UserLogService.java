package com.xplatform.base.system.log.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.log.entity.UserLogEntity;

public interface UserLogService{
	
 	/**
	 * 新增用户日志
	 */
	public String save(UserLogEntity logUser) throws Exception;
	
	/**
	 * 删除用户日志
	 */
	public void delete(String id) throws Exception;
	
	/**
	 * 批量删除用户日志
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新用户日志
	 */
	public void update(UserLogEntity userLog) throws Exception;
	
	/**
	 * 查询一条用户日志记录
	 */
	public UserLogEntity get(String id);
	
	/**
	 * 查询用户日志列表
	 */
	public List<UserLogEntity> queryList();
	
	/**
	 * hibernate用户日志分页列表
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b);
	
	/**
	 * 判断字段记录是否唯一
	 */
	public boolean isUnique(Map<String,String> param,String propertyName);
 	
}
