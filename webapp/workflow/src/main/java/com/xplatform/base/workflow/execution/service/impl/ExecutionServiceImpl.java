package com.xplatform.base.workflow.execution.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Service;

import com.xplatform.base.workflow.execution.mybatis.dao.ExecutionDao;
import com.xplatform.base.workflow.execution.service.ExecutionService;

@Service("executionService")
public class ExecutionServiceImpl implements ExecutionService {
	@Resource
	private ExecutionDao executionDao;

	@Override
	public void insert(ExecutionEntity execution) {
		// TODO Auto-generated method stub
		this.executionDao.save(execution);
	}

	@Override
	public void update(ExecutionEntity execution) {
		// TODO Auto-generated method stub
		this.executionDao.update(execution);
	}

	@Override
	public ExecutionEntity get(String id) {
		// TODO Auto-generated method stub
		return this.executionDao.get(id);
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		this.executionDao.delete(id);
	}

	
	
	

}
