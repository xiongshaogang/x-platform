package com.xplatform.base.workflow.core.facade.listener;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.platform.common.groovy.GroovyScriptEngine;
import com.xplatform.base.workflow.core.bpm.util.BpmConst;
import com.xplatform.base.workflow.node.entity.NodeScriptEntity;
import com.xplatform.base.workflow.node.service.NodeScriptService;

public class SubProcessStartListener implements ExecutionListener {
	 /** 
	  * serialVersionUID
	  */
	private static final long serialVersionUID = 1L;

	public void notify(DelegateExecution execution) throws Exception {
		Integer loopCounter = (Integer) execution.getVariable("loopCounter");

		if ((loopCounter == null) || (loopCounter.intValue() == 0)) {
			String actDefId = execution.getProcessDefinitionId();
			String nodeId = execution.getCurrentActivityId();
			exeEventScript(execution, BpmConst.StartScript,
					actDefId, nodeId);
		}
	}

	private void exeEventScript(DelegateExecution execution, String scriptType,
			String actDefId, String nodeId) {
		NodeScriptService nodeScriptService =ApplicationContextUtil.getBean("nodeScriptService");
		NodeScriptEntity model = nodeScriptService.getScriptByType(nodeId,actDefId, scriptType);
		if (model == null){
			return;
		}
		String script = model.getScript();
		if (StringUtil.isEmpty(script)){
			return;
		}
		GroovyScriptEngine scriptEngine = (GroovyScriptEngine) ApplicationContextUtil.getBean("scriptEngine");
		Map<String,Object> vars = execution.getVariables();
		vars.put("execution", execution);
		scriptEngine.execute(script, vars);
	}
}