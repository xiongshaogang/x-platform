package com.xplatform.base.workflow.core.bpm.task;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.platform.common.groovy.GroovyScriptEngine;
import com.xplatform.base.workflow.core.bpm.util.BpmConst;
import com.xplatform.base.workflow.node.entity.NodeScriptEntity;
import com.xplatform.base.workflow.node.service.NodeScriptService;

/**
 * 
 * description :脚本任务执行
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月20日 下午3:00:33
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年8月20日 下午3:00:33
 *
 */
public class ScriptTask implements JavaDelegate {
	private Log logger = LogFactory.getLog(GroovyScriptEngine.class);
	//执行
	public void execute(DelegateExecution execution) throws Exception {
		ExecutionEntity ent = (ExecutionEntity) execution;
		String nodeId = ent.getActivityId();
		String actDefId = ent.getProcessDefinitionId();
		NodeScriptService nodeScriptService =ApplicationContextUtil.getBean("bpmNodeScriptService");
		//第一步：获取脚本实体
		NodeScriptEntity model = nodeScriptService.getScriptByType(nodeId,actDefId, BpmConst.ScriptNodeScript);
		if (model == null){
			return;
		}
		//第二步：获取脚本
		String script = model.getScript();
		if (StringUtil.isEmpty(script)){
			return;
		}
		//第三步：执行脚本
		GroovyScriptEngine scriptEngine = (GroovyScriptEngine) ApplicationContextUtil.getBean("scriptEngine");
		Map<String,Object> vars = execution.getVariables();//脚本变量
		vars.put("execution", execution);
		scriptEngine.execute(script, vars);
		
		this.logger.debug("execution script :" + script);
	}

}
