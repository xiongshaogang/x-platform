package com.xplatform.base.system.problem.service;

import java.util.List;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.problem.entity.ProblemEntity;

/**
 * 
 * description : 问题反馈service
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年10月20日 下午3:04:48
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * hexj        2014年10月20日 下午3:04:48
 *
 */
public interface ProblemService{
	
	public String save(ProblemEntity Problem) throws BusinessException;
	
	public ProblemEntity get(String id) ;
	
	public void getDataGridReturn(CriteriaQuery cq, boolean offSet);
	
	public void updateState(String id, String state) throws BusinessException;
	
	public void saveResolveProblem(ProblemEntity problem) throws BusinessException;

}
