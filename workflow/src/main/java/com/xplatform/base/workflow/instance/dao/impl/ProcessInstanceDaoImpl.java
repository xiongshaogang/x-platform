package com.xplatform.base.workflow.instance.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.instance.dao.ProcessInstanceDao;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
/**
 * 
 * description :流程实例dao实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:23:11
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:23:11
 *
 */
@Repository("processInstanceDao")
public class ProcessInstanceDaoImpl extends CommonDao implements ProcessInstanceDao {

	@Override
	public String addProcessInstance(ProcessInstanceEntity ProcessInstance) {
		// TODO Auto-generated method stub
		return (String) this.save(ProcessInstance);
	}

	@Override
	public void deleteProcessInstance(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(ProcessInstanceEntity.class, id);
	}

	@Override
	public void updateProcessInstance(ProcessInstanceEntity ProcessInstance) {
		// TODO Auto-generated method stub
		this.updateEntitie(ProcessInstance);
	}

	@Override
	public ProcessInstanceEntity getProcessInstance(String id) {
		// TODO Auto-generated method stub
		return (ProcessInstanceEntity) this.get(ProcessInstanceEntity.class, id);
	}

	@Override
	public List<ProcessInstanceEntity> queryProcessInstanceList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from ProcessInstanceEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

	@Override
	public ProcessInstanceEntity getgetByActInstanceId(String actInstId) {
		// TODO Auto-generated method stub
		List<ProcessInstanceEntity> list=this.findByProperty(ProcessInstanceEntity.class, "actInstId", actInstId);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
