package com.xplatform.base.system.log.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.log.entity.OperLogEntity;

import freemarker.template.TemplateException;

public interface OperLogService{
	
 	/**
	 * 新增系统日志
	 */
	public String save(OperLogEntity operLog) throws Exception;
	
	/**
	 * 删除系统日志
	 */
	public void delete(String id) throws Exception;
	
	/**
	 * 批量删除系统日志
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新系统日志
	 */
	public void update(OperLogEntity operLog) throws Exception;
	
	/**
	 * 查询一条系统日志记录
	 */
	public OperLogEntity get(String id);
	
	/**
	 * 查询系统日志列表
	 */
	public List<OperLogEntity> queryList();
	
	/**
	 * hibernate系统日志分页列表
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b);
	
	/**
	 *  手动保存日志信息
	 * */
 	public void saveOperLogHand(OperLogEntity operLog) throws Exception;
}
