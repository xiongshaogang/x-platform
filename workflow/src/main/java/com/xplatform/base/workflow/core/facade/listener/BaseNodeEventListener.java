package com.xplatform.base.workflow.core.facade.listener;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.platform.common.groovy.GroovyScriptEngine;
import com.xplatform.base.workflow.node.entity.NodeScriptEntity;
import com.xplatform.base.workflow.node.service.NodeScriptService;

public abstract class BaseNodeEventListener implements ExecutionListener {
	private static final long serialVersionUID = 1L;
	private Log logger = LogFactory.getLog(BaseNodeEventListener.class);

	public void notify(DelegateExecution execution) throws Exception {
		this.logger.debug("enter the node event listener.." + execution.getId());

		ExecutionEntity ent = (ExecutionEntity) execution;

		String actDefId = ent.getProcessDefinitionId();
		String nodeId = ent.getActivityId();

		execute(execution, actDefId, nodeId);

		if (nodeId != null) {
			String scriptType = getScriptType();
			//执行脚本
			exeEventScript(execution, scriptType, actDefId, nodeId);
		}
	}

	protected abstract void execute(DelegateExecution paramDelegateExecution,String paramString1, String paramString2);

	protected abstract String getScriptType();

	private void exeEventScript(DelegateExecution execution, String scriptType,
			String actDefId, String nodeId) {
		NodeScriptService bpmNodeScriptService = ApplicationContextUtil.getBean("nodeScriptService");
		//通过脚本类型查询脚本
		NodeScriptEntity model = bpmNodeScriptService.getScriptByType(nodeId,actDefId, scriptType);
		if (model == null){return;}
		String script = model.getScript();
		if (StringUtil.isEmpty(script)){return;}
		GroovyScriptEngine scriptEngine = (GroovyScriptEngine) ApplicationContextUtil.getBean("scriptEngine");
		Map<String,Object> vars = execution.getVariables();
		vars.put("execution", execution);

		scriptEngine.execute(script, vars);//执行脚本

		this.logger.debug("execution script :" + script);
	}
}