package com.xplatform.base.workflow.threadlocal;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.xplatform.base.workflow.core.bpm.cache.DeploymentCache;
import com.xplatform.base.workflow.core.bpm.model.ProcessCmd;

/**
 * 
 * description :任务流转的各个记录，通过threadloacal来记录
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月12日 下午1:54:06
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年8月12日 下午1:54:06
 *
 */
public class TaskThreadService {
	//流程分发汇总的threadlocal
	private static ThreadLocal<String> forkTaskTokenLocal = new ThreadLocal<String>();
	//新任务节点的threadlocal
	private static ThreadLocal<List<Task>> newTasksLocal = new ThreadLocal<List<Task>>();
	//上一个节点用户threadlocal
	private static ThreadLocal<String> preUserLocal = new ThreadLocal<String>();
	//流程变量threadlocal的集合
	private static ThreadLocal<ProcessCmd> processCmdLocal = new ThreadLocal<ProcessCmd>();
	//外部子节点threadlocal
	private static ThreadLocal<List<String>> extSubProcess = new ThreadLocal<List<String>>();

	private static ThreadLocal<Object> objLocal = new ThreadLocal<Object>();
	//回到第一个节点threadlocal
	private static ThreadLocal<Short> toFirstNode = new ThreadLocal<Short>();
	//变量threadlocal
	private static ThreadLocal<Map<String, Object>> varsLocal = new ThreadLocal<Map<String, Object>>();
	//所有threadlocal的集合
	private static ThreadLocal<Map<String, Object>> tempLocal = new ThreadLocal<Map<String, Object>>();

	public static void addTask(Task taskEntity) {
		List<Task> taskList = (List<Task>) newTasksLocal.get();
		if (taskList == null) {
			taskList = new ArrayList<Task>();
			newTasksLocal.set(taskList);
		}
		taskList.add(taskEntity);
	}

	public static List<Task> getNewTasks() {
		List<Task> list = (List<Task>) newTasksLocal.get();
		return list;
	}
	
	public static void clearNewTasks() {
		newTasksLocal.remove();
	}

	public static String getToken() {
		return (String) forkTaskTokenLocal.get();
	}

	public static void setToken(String token) {
		forkTaskTokenLocal.set(token);
	}

	public static void clearToken() {
		forkTaskTokenLocal.remove();
	}

	

	public static void cleanTaskUser() {
		preUserLocal.remove();
	}

	public static ProcessCmd getProcessCmd() {
		return (ProcessCmd) processCmdLocal.get();
	}

	public static void setProcessCmd(ProcessCmd processCmd) {
		processCmdLocal.set(processCmd);
	}

	public static void cleanProcessCmd() {
		processCmdLocal.remove();
	}

	public static List<String> getExtSubProcess() {
		return (List<String>) extSubProcess.get();
	}

	public static void setExtSubProcess(List<String> extSubProcessList) {
		extSubProcess.set(extSubProcessList);
	}

	public static void addExtSubProcess(String instanceId) {
		List<String> list = null;
		if (extSubProcess.get() == null) {
			list = new ArrayList<String>();
			list.add(instanceId);
			extSubProcess.set(list);
		} else {
			((List<String>) extSubProcess.get()).add(instanceId);
		}
	}

	public static void cleanExtSubProcess() {
		extSubProcess.remove();
	}

	public static void setObject(Object obj) {
		objLocal.set(obj);
	}

	public static Object getObject() {
		return objLocal.get();
	}

	public static void setToFirstNode(Short obj) {
		toFirstNode.set(obj);
	}

	public static Object getToFirstNode() {
		return toFirstNode.get();
	}

	public static void removeToFirstNode() {
		toFirstNode.remove();
	}

	public static void removeObject() {
		objLocal.remove();
	}

	public static void setVariables(Map<String, Object> vars_) {
		varsLocal.set(vars_);
	}

	public static Map<String, Object> getVariables() {
		return (Map<String, Object>) varsLocal.get();
	}

	public static void clearVariables() {
		varsLocal.remove();
	}

	public static void setTempLocal(String actInstId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("forkTaskTokenLocal" + actInstId, forkTaskTokenLocal.get());
		map.put("newTasksLocal" + actInstId, newTasksLocal.get());
		map.put("preUserLocal" + actInstId, preUserLocal.get());
		map.put("processCmdLocal" + actInstId, processCmdLocal.get());
		map.put("extSubProcess" + actInstId, extSubProcess.get());
		map.put("objLocal" + actInstId, objLocal.get());
		map.put("varsLocal" + actInstId, varsLocal.get());
		tempLocal.set(map);
	}

	@SuppressWarnings("unchecked")
	public static void resetTempLocal(String actInstId) {
		Map<String, Object> map = (Map<String, Object>) tempLocal.get();
		forkTaskTokenLocal.set((String) map.get("forkTaskTokenLocal"+ actInstId));
		newTasksLocal.set((List<Task>) map.get("newTasksLocal" + actInstId));
		preUserLocal.set((String) map.get("preUserLocal" + actInstId));
		processCmdLocal.set((ProcessCmd) map.get("processCmdLocal" + actInstId));
		extSubProcess.set((List<String>) map.get("extSubProcess" + actInstId));
		objLocal.set(map.get("objLocal" + actInstId));
		varsLocal.set((Map<String,Object>) map.get("varsLocal" + actInstId));
		tempLocal.remove();
	}

	public static void clearAll() {
		forkTaskTokenLocal.remove();
		newTasksLocal.remove();
		preUserLocal.remove();
		processCmdLocal.remove();
		extSubProcess.remove();
		objLocal.remove();
		varsLocal.remove();
		DeploymentCache.clearProcessDefinitionEntity();
	}
}