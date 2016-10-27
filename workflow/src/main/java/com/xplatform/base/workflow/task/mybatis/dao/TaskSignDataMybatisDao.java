package com.xplatform.base.workflow.task.mybatis.dao;

import java.util.List;
import java.util.Map;

public interface TaskSignDataMybatisDao {
	//
	public Integer getMaxSignNums(Map<String,String> param);

	public Integer getAgreeVoteCount(Map<String,String> param);

	public Integer getRefuseVoteCount(Map<String,String> param);

	public Integer getAbortVoteCount(Map<String,String> param);

	public void updateCompleted(Map<String,String> param);

	public Integer getMaxBatch(Map<String,Object> param);
	
	
	


}
