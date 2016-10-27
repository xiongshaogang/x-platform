package com.xplatform.base.system.problem.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.system.problem.entity.ProblemEntity;

/**
 * 
 * description : 问题反馈管理dao
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年10月20日 下午3:08:48
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * hexj        2014年10月20日 下午3:08:48
 *
 */
public interface ProblemDao extends ICommonDao{
	
	public String addProblem(ProblemEntity problem);
	
	public ProblemEntity getProblem(String id);
	
	public void updateProblemState(String id,String state);
	
	public void saveResolveProblem(ProblemEntity problem);
	

}
