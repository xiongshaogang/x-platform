package com.xplatform.base.workflow.agent.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.agent.dao.AgentSettingDao;
import com.xplatform.base.workflow.agent.entity.AgentSettingEntity;
/**
 * 
 * description :岗位dao实现
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
@Repository("agentSettingDao")
public class AgentSettingDaoImpl extends CommonDao implements AgentSettingDao {

	@Override
	public String addAgentSetting(AgentSettingEntity AgentSetting) {
		// TODO Auto-generated method stub
		return (String) this.save(AgentSetting);
	}

	@Override
	public void deleteAgentSetting(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(AgentSettingEntity.class, id);
	}

	@Override
	public void updateAgentSetting(AgentSettingEntity AgentSetting) {
		// TODO Auto-generated method stub
		this.updateEntitie(AgentSetting);
	}

	@Override
	public AgentSettingEntity getAgentSetting(String id) {
		// TODO Auto-generated method stub
		return (AgentSettingEntity) this.get(AgentSettingEntity.class, id);
	}

	@Override
	public List<AgentSettingEntity> queryAgentSettingList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from AgentSettingEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
