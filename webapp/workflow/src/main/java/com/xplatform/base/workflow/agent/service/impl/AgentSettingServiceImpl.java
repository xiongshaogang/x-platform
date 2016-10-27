package com.xplatform.base.workflow.agent.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.activiti.engine.delegate.DelegateTask;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.groovy.GroovyScriptEngine;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.workflow.agent.dao.AgentSettingDao;
import com.xplatform.base.workflow.agent.entity.AgentFlowEntity;
import com.xplatform.base.workflow.agent.entity.AgentSettingEntity;
import com.xplatform.base.workflow.agent.service.AgentSettingService;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;

/**
 * 
 * description :流程代理设置service实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:30:12
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 下午12:30:12
 *
 */
@Service("agentSettingService")
public class AgentSettingServiceImpl implements AgentSettingService {
	private static final Logger logger = Logger.getLogger(AgentSettingServiceImpl.class);
	@Resource
	private AgentSettingDao agentSettingDao;
	@Resource
	private BaseService baseService;
	@Resource
	private DefinitionService definitionService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private GroovyScriptEngine scriptEngine;
	
	@Override
	@Action(moduleCode="AgentSettingManager",description="流程代理设置新增",detail="流程代理设置${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(AgentSettingEntity AgentSetting) throws BusinessException {
		String pk="";
		try {
			pk=this.agentSettingDao.addAgentSetting(AgentSetting);
		} catch (Exception e) {
			logger.error("流程代理设置保存失败");
			throw new BusinessException("流程代理设置保存失败");
		}
		logger.info("流程代理设置保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="AgentSettingManager",description="流程代理设置删除",detail="流程代理设置${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.agentSettingDao.deleteAgentSetting(id);
		} catch (Exception e) {
			logger.error("流程代理设置删除失败");
			throw new BusinessException("流程代理设置删除失败");
		}
		logger.info("流程代理设置删除成功");
	}

	@Override
	@Action(moduleCode="AgentSettingManager",description="流程代理设置批量删除",detail="流程代理设置${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("流程代理设置批量删除成功");
	}

	@Override
	@Action(moduleCode="AgentSettingManager",description="流程代理设置修改",detail="流程代理设置${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(AgentSettingEntity AgentSetting) throws BusinessException {
		try {
			this.agentSettingDao.updateAgentSetting(AgentSetting);
		} catch (Exception e) {
			logger.error("流程代理设置更新失败");
			throw new BusinessException("流程代理设置更新失败");
		}
		logger.info("流程代理设置更新成功");
	}

	@Override
	public AgentSettingEntity get(String id){
		AgentSettingEntity AgentSetting=null;
		try {
			AgentSetting=this.agentSettingDao.getAgentSetting(id);
		} catch (Exception e) {
			logger.error("流程代理设置获取失败");
			//throw new BusinessException("流程代理设置获取失败");
		}
		logger.info("流程代理设置获取成功");
		return AgentSetting;
	}

	@Override
	public List<AgentSettingEntity> queryList() throws BusinessException {
		List<AgentSettingEntity> AgentSettingList=new ArrayList<AgentSettingEntity>();
		try {
			AgentSettingList=this.agentSettingDao.queryAgentSettingList();
		} catch (Exception e) {
			logger.error("流程代理设置获取列表失败");
			throw new BusinessException("流程代理设置获取列表失败");
		}
		logger.info("流程代理设置获取列表成功");
		return AgentSettingList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.agentSettingDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("流程代理设置获取分页列表失败");
			throw new BusinessException("流程代理设置获取分页列表失败");
		}
		logger.info("流程代理设置获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(AgentSettingEntity.class, param, propertyName);
	}
	
	@Override
	public void deleteAgentFlowEntity(AgentSettingEntity agentSetting) {
		// TODO Auto-generated method stub
		this.agentSettingDao.executeSql("delete from t_flow_agent_flow where setting_id=?", new Object[]{agentSetting.getId()});
	}

	@Override
	public void saveFlow(AgentFlowEntity agentFlowEntity) {
		// TODO Auto-generated method stub
		this.agentSettingDao.save(agentFlowEntity);
	}
	
	@Override
	public List<AgentFlowEntity> queryFlowList(AgentSettingEntity agentSetting) {
		// TODO Auto-generated method stub
		return this.agentSettingDao.findByProperty(AgentFlowEntity.class, "setting.id", agentSetting.getId());
	}
	
	@Override
	public AgentSettingEntity getValidAgentSetting(String flowId,String userId,Date currentDate){
		AgentSettingEntity agentSetting=null;
		UserEntity emp= this.sysUserService.getUserById(userId);
		String authId=(emp!=null?emp.getId():"");
		List<AgentSettingEntity> list=this.agentSettingDao.findHql("select setting from AgentFlowEntity flow left join flow.setting setting  where setting.status='Y' and flow.flowId=? and setting.authId=? and setting.startTime<? and setting.endTime>?", flowId,authId,currentDate,currentDate);
		if(list!=null && list.size()>0){
			agentSetting=list.get(0);
		}
		return agentSetting;
	}
	@Override
	public Map<String,Object> getAgent(DelegateTask delegateTask, String userId) {
		// TODO Auto-generated method stub
		String actDefId = delegateTask.getProcessDefinitionId();
		DefinitionEntity bpmDefinition = this.definitionService.getByActDefId(actDefId);
		Map<String,Object> result=new HashMap<String,Object>();
		String flowKey = bpmDefinition.getActKey();
		Map<String,Object> formVars = delegateTask.getVariables();
		Date currentDate = new Date();
		UserEntity sysUser=null;
		AgentSettingEntity agentSetting = getValidAgentSetting(bpmDefinition.getId(), userId,currentDate);
		if(agentSetting!=null){
			if (StringUtil.equals(AgentSettingEntity.AUTHTYPE_GENERAL , agentSetting.getType())) {//全部代理
				sysUser = this.sysUserService.getUserById(agentSetting.getAgentId());
			} else if (StringUtil.equals(AgentSettingEntity.AUTHTYPE_PARTIAL, agentSetting.getType())) {
				sysUser = this.sysUserService.getUserById(agentSetting.getAgentId());
			} else if (StringUtil.equals(AgentSettingEntity.AUTHTYPE_CONDITION , agentSetting.getType())) {
				Boolean rtn = Boolean.valueOf(this.scriptEngine.executeBoolean(agentSetting.getConExp(), formVars));
				if(rtn.booleanValue()){
					sysUser = this.sysUserService.getUserById(agentSetting.getAgentId());
				}
			}
			if(sysUser!=null){
				result.put("sysUser", sysUser);
				result.put("selfReceive", agentSetting.getSelfReceive());
			}
		}
		return result;
	}
	
	public void setAgentSettingDao(AgentSettingDao agentSettingDao) {
		this.agentSettingDao = agentSettingDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
}
