package com.xplatform.base.workflow.core.bpm.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang.StringUtils;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.workflow.core.bpm.graph.activiti.ProcessDiagramGenerator;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstanceService;
import com.xplatform.base.workflow.task.service.TaskNodeStatusService;

public class BpmImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private FlowService flowService = (FlowService) ApplicationContextUtil.getBean("flowService");

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String deployId = request.getParameter("deployId");
		String taskId = request.getParameter("taskId");
		String processInstanceId = request.getParameter("processInstanceId");
		String definitionId = request.getParameter("definitionId");
		String runId = request.getParameter("runId");

		InputStream is = null;

		if (StringUtil.isNotEmpty(deployId)) {
			String bpmnXml = this.flowService.getDefXmlByDeployId(deployId);
			is = ProcessDiagramGenerator.generatePngDiagram(bpmnXml);
		} else if (StringUtils.isNotEmpty(definitionId)) {
			String bpmnXml = this.flowService
					.getDefXmlByProcessDefinitionId(definitionId);
			is = ProcessDiagramGenerator.generatePngDiagram(bpmnXml);
		} else if (StringUtil.isNotEmpty(taskId)) {
			String bpmnXml = this.flowService.getDefXmlByProcessTaskId(taskId);
			TaskEntity taskEntity = this.flowService.getTask(taskId);
			TaskNodeStatusService taskNodeStatusService =  ApplicationContextUtil.getBean("taskNodeStatusService");
			Map<String,String> highLightMap = taskNodeStatusService.getStatusByInstanceId(processInstanceId);
			is = ProcessDiagramGenerator.generateDiagram(bpmnXml,highLightMap, "png");
		} else if (StringUtils.isNotEmpty(processInstanceId)) {
			String bpmnXml = this.flowService.getDefXmlByProcessProcessInanceId(processInstanceId);
			if (bpmnXml == null) {
				ProcessInstanceService processInstanceService =  ApplicationContextUtil.getBean("processInstanceService");
				ProcessInstanceEntity processRun = processInstanceService.getByActInstanceId(processInstanceId);
				bpmnXml = this.flowService.getDefXmlByDeployId(processRun.getActDefId());
			}
			TaskNodeStatusService taskNodeStatusService =  ApplicationContextUtil.getBean("taskNodeStatusService");
			Map<String,String> highLightMap = taskNodeStatusService.getStatusByInstanceId(processInstanceId);
			is = ProcessDiagramGenerator.generateDiagram(bpmnXml, highLightMap,"png");
		} else if (StringUtils.isNotEmpty(runId)) {
			ProcessInstanceService processInstanceService =  ApplicationContextUtil.getBean("processInstanceService");
			ProcessInstanceEntity processRun = null;
			try {
				processRun = processInstanceService.get(runId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			processInstanceId = processRun.getActInstId();
			String bpmnXml = this.flowService.getDefXmlByProcessProcessInanceId(processRun.getActInstId());
			if (bpmnXml == null) {
				bpmnXml = this.flowService.getDefXmlByDeployId(processRun.getActDefId());
			}
			TaskNodeStatusService taskNodeStatusService =  ApplicationContextUtil.getBean("taskNodeStatusService");
			Map<String,String> highLightMap = taskNodeStatusService.getStatusByInstanceId(processInstanceId);
			is = ProcessDiagramGenerator.generateDiagram(bpmnXml, highLightMap,"png");
		}

		if (is != null) {
			response.setContentType("image/png");
			OutputStream out = response.getOutputStream();
			try {
				byte[] bs = new byte[1024];
				int n = 0;
				while ((n = is.read(bs)) != -1) {
					out.write(bs, 0, n);
				}
				out.flush();
			} catch (Exception localException) {
			} finally {
				is.close();
				out.close();
			}
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}