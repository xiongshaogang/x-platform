package com.xplatform.base.system.problem.dao.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.system.problem.dao.ProblemDao;
import com.xplatform.base.system.problem.entity.ProblemEntity;


/**
 * 
 * description : 问题反馈dao实现
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年10月20日 下午3:12:49
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * hexj        2014年10月20日 下午3:12:49
 *
 */

@Repository("problemDao")
@SuppressWarnings("unchecked")
public class ProblemDaoImpl extends CommonDao implements ProblemDao{

	@Override
	public String addProblem(ProblemEntity Problem) {
		return (String)this.save(Problem);
	}

	@Override
	public ProblemEntity getProblem(String id) {
		return (ProblemEntity)this.get(ProblemEntity.class, id);
	}

	@Override
	public void updateProblemState(String id, String state) {
		String hql = "update ProblemEntity pe set pe.problemState=:state where pe.id=:id";
		Query queryObject = this.getSession().createQuery(hql);
		queryObject.setParameter("state", state);
		queryObject.setParameter("id", id);
		queryObject.executeUpdate();
	}
	
	@Override
	public void saveResolveProblem(ProblemEntity problem) {
		String hql = "update ProblemEntity pe set pe.resolveSolution=:solution, pe.problemState=:state,"
				    + "pe.updateTime=:updateTime, pe.updateUserId=:userId, pe.updateUserName=:userName where pe.id=:id";
		Query q = this.getSession().createQuery(hql);
		q.setParameter("solution", problem.getResolveSolution());
		q.setParameter("state", problem.getProblemState());
		q.setParameter("updateTime", new Date());
		q.setParameter("userId", problem.getUpdateUserId());
		q.setParameter("userName", problem.getUpdateUserName());
		q.setParameter("id", problem.getId());
		q.executeUpdate();
	}

}
