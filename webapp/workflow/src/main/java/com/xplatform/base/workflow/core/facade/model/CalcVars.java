package com.xplatform.base.workflow.core.facade.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * description :计算参数变量
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年9月11日 下午5:40:34
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年9月11日 下午5:40:34
 *
 */
public class CalcVars {
	private String startUserId;//流程发起人id
	private String prevExecUserId;//上一个任务执行人id
	private String actInstId;//流程实例id
	private Map<String, Object> vars = new HashMap<String, Object>();//其他变量

	public CalcVars() {
	}

	public CalcVars(String startUserId, String preExecUserId, String actInstId,
			Map<String, Object> vars) {
		this.startUserId = startUserId;
		this.prevExecUserId = preExecUserId;
		this.actInstId = actInstId;
		this.vars = vars;
	}

	public String getStartUserId() {
		return this.startUserId;
	}

	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}

	public String getPrevExecUserId() {
		return this.prevExecUserId;
	}

	public void setPrevExecUserId(String prevExecUserId) {
		this.prevExecUserId = prevExecUserId;
	}

	public String getActInstId() {
		return this.actInstId;
	}

	public void setActInstId(String actInstId) {
		this.actInstId = actInstId;
	}

	public Map<String, Object> getVars() {
		return this.vars;
	}

	public void setVars(Map<String, Object> vars) {
		this.vars = vars;
	}
}
