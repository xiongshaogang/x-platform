package com.xplatform.base.form.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.form.dao.FlowInstanceUserDao;
import com.xplatform.base.form.entity.FlowInstanceUserEntity;
import com.xplatform.base.form.service.FlowInstanceUserService;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.framework.core.util.JSONHelper;

@Service("flowInstanceUserService")
public class FlowInstanceUserServiceImpl extends BaseServiceImpl<FlowInstanceUserEntity> implements FlowInstanceUserService {

	@Resource
	private FlowInstanceUserDao flowInstanceUserDao;
	
	@Override
	public AjaxJson saveOrDeleteFIU(String assignResult, String businessKey) throws BusinessException {
		AjaxJson result = new AjaxJson();
		List<Map> finalValueList = JSONHelper.toList(assignResult, Map.class);
		List<FlowInstanceUserEntity> list = new ArrayList<FlowInstanceUserEntity>();

		for (int i = 0; i < finalValueList.size(); i++) {
			Map item = finalValueList.get(i);
			String type = item.get("type").toString();
			String id = item.get("id").toString();
			String name = item.get("name").toString();

			FlowInstanceUserEntity flowInstanceUser = new FlowInstanceUserEntity();
			flowInstanceUser.setStatus(0);
			flowInstanceUser.setUserId(id);
			flowInstanceUser.setUserName(name);
			flowInstanceUser.setType(type);
			flowInstanceUser.setTaskNodeId("task" + (i + 1));
			flowInstanceUser.setBusinessKey(businessKey);
			flowInstanceUser.setOrderby(i + 1);
			list.add(flowInstanceUser);
		}

		// 删除BusinessKey相同的表记录
		String sql = "delete from FlowInstanceUserEntity where businessKey=?";
		this.flowInstanceUserDao.executeHql(sql, businessKey);

		// 保存新加入的FlowInstanceUserEntity
		if (list.size() > 0) {
			this.flowInstanceUserDao.batchSave(list);
		}
		result.setMsg("保存成功");
		result.setSuccess(true);
		return result;
	}

	@Override
	public List<FlowInstanceUserEntity> queryFIUListByBus(String businessKey) throws BusinessException {
		String hql = "from FlowInstanceUserEntity where businessKey=?";
		return this.flowInstanceUserDao.findHql(hql, businessKey);
	}

	@Override
	public FlowInstanceUserEntity queryFIUListByStatus(String businessKey,String taskNodeId) throws BusinessException {
		String hql = "from FlowInstanceUserEntity t where status=0 and businessKey = ? and taskNodeId=? ORDER BY t.orderby ASC";
		FlowInstanceUserEntity flowInstanceUser = this.flowInstanceUserDao.findUniqueByHql(hql,businessKey,taskNodeId);
		
		return flowInstanceUser;
	}
	
}
