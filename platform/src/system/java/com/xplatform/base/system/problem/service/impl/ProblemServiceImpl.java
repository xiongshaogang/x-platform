package com.xplatform.base.system.problem.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.user.service.impl.UserServiceImpl;
import com.xplatform.base.system.problem.dao.ProblemDao;
import com.xplatform.base.system.problem.entity.ProblemEntity;
import com.xplatform.base.system.problem.service.ProblemService;

/**
 * 
 * description : 问题反馈service实现
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年10月20日 下午3:15:59
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * hexj        2014年10月20日 下午3:15:59
 *
 */

@Service("problemService")
public class ProblemServiceImpl implements ProblemService {
	
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Resource
	private ProblemDao problemDao;

	@Override
	public String save(ProblemEntity Problem) throws BusinessException {
		String pk = "";
		try {
			pk = problemDao.addProblem(Problem);
		} catch (Exception e) {
			logger.error("问题反馈保存失败");
			throw new BusinessException("问题反馈保存失败");
		}
		logger.info("问题反馈保存成功");
		return pk;
	}
	
	@Override
	public ProblemEntity get(String id) {
		ProblemEntity problem = null;
		problem = problemDao.getProblem(id);
		return problem;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean offSet){
		problemDao.getDataGridReturn(cq, offSet);
	}
	
	@Override
	public void updateState(String id, String state) throws BusinessException {
		try {
			problemDao.updateProblemState(id, state);
		} catch (Exception e) {
			logger.error("问题反馈状态更新失败");
			throw new BusinessException("问题反馈状态更新失败");
		}
		logger.info("问题反馈状态更新成功");
	}


	/**
	 * 
	 * 重写方法: saveResolveProblem|描述: 保存问题解决方案
	 * 
	 * @param Problem
	 * @throws BusinessException
	 * @see com.xplatform.base.system.problem.service.ProblemService#saveResolveProblem(com.xplatform.base.system.problem.entity.ProblemEntity)
	 */
	@Override
	public void saveResolveProblem(ProblemEntity Problem) throws BusinessException {
		try {
			problemDao.saveResolveProblem(Problem);
		} catch (Exception e) {
			logger.info("问题解决方案保存失败");
			throw new BusinessException("问题解决方案保存失败");
		}
		logger.info("问题解决方案保存成功");
	}

	
	
	public ProblemDao getProblemDao() {
		return problemDao;
	}

	public void setProblemDao(ProblemDao problemDao) {
		this.problemDao = problemDao;
	}


}
