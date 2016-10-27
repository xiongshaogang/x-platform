package com.xplatform.base.workflow.task.service;

import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

/**
 * 
 * description :完成会签接口
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月11日 下午6:20:11
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年8月11日 下午6:20:11
 *
 */
public interface ISignComplete {
	 public static final String SIGN_RESULT_PASS = "pass";//通过
	 public static final String SIGN_RESULT_REFUSE = "refuse";//拒绝
	 public static final String SIGN_RESULT_RECOVER = "recover";//
	 public static final String SIGN_RESULT_BACK = "reject";
	 public static final String SIGN_RESULT_TOSTART = "rejectToStart";//
	 //完成会签
	 public abstract boolean isComplete(ActivityExecution paramActivityExecution);
}
