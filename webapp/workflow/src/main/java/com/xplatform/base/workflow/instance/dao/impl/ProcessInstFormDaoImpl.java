package com.xplatform.base.workflow.instance.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.instance.dao.ProcessInstFormDao;
import com.xplatform.base.workflow.instance.entity.ProcessInstFormEntity;
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
@Repository("processInsFormDao")
public class ProcessInstFormDaoImpl extends CommonDao implements ProcessInstFormDao {

	@Override
	public String addProcessInsForm(ProcessInstFormEntity ProcessInsForm) {
		// TODO Auto-generated method stub
		return (String) this.save(ProcessInsForm);
	}

	@Override
	public void deleteProcessInsForm(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(ProcessInstFormEntity.class, id);
	}

	@Override
	public void updateProcessInsForm(ProcessInstFormEntity ProcessInsForm) {
		// TODO Auto-generated method stub
		this.updateEntitie(ProcessInsForm);
	}

	@Override
	public ProcessInstFormEntity getProcessInsForm(String id) {
		// TODO Auto-generated method stub
		return (ProcessInstFormEntity) this.get(ProcessInstFormEntity.class, id);
	}

	@Override
	public List<ProcessInstFormEntity> queryProcessInsFormList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from ProcessInsFormEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
