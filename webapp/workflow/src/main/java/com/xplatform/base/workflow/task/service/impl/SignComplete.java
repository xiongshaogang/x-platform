package com.xplatform.base.workflow.task.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.core.bpm.model.ProcessCmd;
import com.xplatform.base.workflow.core.bpm.util.BpmConst;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.node.entity.NodeSignEntity;
import com.xplatform.base.workflow.node.entity.NodeSignPrivilegeEntity;
import com.xplatform.base.workflow.node.service.NodeSignService;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.service.ISignComplete;
import com.xplatform.base.workflow.task.service.TaskNodeStatusService;
import com.xplatform.base.workflow.task.service.TaskOpinionService;
import com.xplatform.base.workflow.task.service.TaskSignDataService;
import com.xplatform.base.workflow.threadlocal.TaskThreadService;

/**
 * 
 * description :完成会签逻辑
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年9月24日 上午10:03:27
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年9月24日 上午10:03:27
 *
 */
@Service("signComplete")
public class SignComplete implements ISignComplete {
	@Resource
	private FlowService flowService;
	@Resource
	private NodeSignService nodeSignService;
	@Resource
	private TaskSignDataService taskSignDataService;
	@Resource
	private TaskOpinionService taskOpinionService;
	@Resource
	private TaskNodeStatusService taskNodeStatusService;
	
	@Override
	public boolean isComplete(ActivityExecution execution) {
		String nodeId = execution.getActivity().getId();
		String actInstId = execution.getProcessInstanceId();
		boolean isCompleted = false;
		String signResult = "";
		//获取流程定义
		ProcessDefinition processDefinition = this.flowService.getProcessDefinitionByProcessInanceId(actInstId);
		//获取会签配置信息,如果没有设置会签的规则，那么一个人审批完成后，就直接结束会签节点
		NodeSignEntity nodeSign = this.nodeSignService.getByDefIdAndNodeId(processDefinition.getId(), nodeId);
		//完成的人数
		Integer completeCounter = (Integer) execution.getVariable("nrOfCompletedInstances");
		//实例总人数
		Integer instanceOfNumbers = (Integer) execution.getVariable("nrOfInstances");
		//一个人投票的结果，同意还是反对
		Short approvalStatus = TaskThreadService.getProcessCmd().getVoteAgree();
		
		ProcessCmd processCmd = TaskThreadService.getProcessCmd();
		
		if ((BpmConst.TASK_BACK_TOSTART.equals(processCmd.isBack()))|| (BpmConst.TASK_BACK.equals(processCmd.isBack()))) {
			isCompleted = true;
		} else if ((approvalStatus.shortValue() == 5)|| (approvalStatus.shortValue() == 6)|| (approvalStatus.shortValue() == 7)) {
			isCompleted = true;
			if (approvalStatus.shortValue() == 5){
				signResult = "pass";
			}else if(approvalStatus.shortValue() == 6){
				signResult = "refuse";
			}else if(approvalStatus.shortValue() == 7){
				signResult = "reconside";
			}
		} else {
			//后台配置允许一票否决跟所有特权的
			boolean isOneVote = this.nodeSignService.checkNodeSignPrivilege(processDefinition.getId(), nodeId,NodeSignPrivilegeEntity.ALLOW_ONE_VOTE,ClientUtil.getUserId(),actInstId);
			if ((isOneVote)&& (!execution.hasVariable("resultOfSign_" + nodeId)) && (approvalStatus.shortValue()==2)) {
				execution.setVariable("resultOfSign_" + nodeId, approvalStatus);
			}
			//获取一票否决的结果（如果执行一票否决）
			Short oneVoteResult = null;
			if (execution.hasVariable("resultOfSign_" + nodeId)) {
				oneVoteResult = (Short) execution.getVariable("resultOfSign_"+ nodeId);
			}

			VoteResult voteResult = calcResult(nodeSign, actInstId, nodeId,completeCounter, instanceOfNumbers, oneVoteResult,approvalStatus);

			signResult = voteResult.getSignResult();
			isCompleted = voteResult.getIsComplete();
		}
		
		/** 
	    * 会签完成做的动作。 
	    * 1.删除会签的流程变量。 
	    * 2.将会签数据更新为完成。 
	    * 3.设置会签结果变量。 
	    * 4.更新会签节点结果。 
	    * 5.清除会签用户。 
	    */  
		if (isCompleted) {
			//1.删除会签的流程变量
			this.flowService.delLoopAssigneeVars(execution.getId());
			//更新会签数据状态
			//this.taskSignDataService.batchUpdateCompleted(actInstId, nodeId);
			Short status = null;
			if ((BpmConst.TASK_BACK_TOSTART.equals(processCmd.isBack())) || (BpmConst.TASK_BACK.equals(processCmd.isBack()))) {
				status = processCmd.getVoteAgree();
				if ((TaskOpinionEntity.STATUS_RECOVER_TOSTART.equals(status))
						|| (TaskOpinionEntity.STATUS_RECOVER.equals(status)))
					signResult = "recover";
				else if (TaskOpinionEntity.STATUS_REJECT_TOSTART.equals(status))
					signResult = "rejectToStart";
				else if (TaskOpinionEntity.STATUS_REJECT.equals(status))
					signResult = "reject";
				else
					signResult = "UNKNOW_ACTION";
			} else {
				status = TaskOpinionEntity.STATUS_PASSED;
				if (signResult.equals("refuse")) {
					status = TaskOpinionEntity.STATUS_NOT_PASSED;
				}else if(signResult.equals("reconside")){
					status = TaskOpinionEntity.STATUS_RE_PASSED;
				}
			}
			execution.setVariable("signResult_" + nodeId, signResult);
			String resultSign = "resultOfSign_" + nodeId;
			if (execution.hasVariable(resultSign)) {
				execution.removeVariable(resultSign);
			}
			//2.修改任务节点的状态
			this.taskNodeStatusService.saveOrUpdte(execution.getProcessDefinitionId(),actInstId, nodeId, status.toString());
			//3.更新审批意见
			updOption(execution, status);
			//4.更新投票结果
			updOptionVoteStatus(execution, status);
			String multiInstance = (String) execution.getActivity().getProperty("multiInstance");
			//5.移除变量
			if ("sequential".equals(multiInstance)) {
				String varName = nodeId + "_" + "signUsers";
				execution.removeVariable(varName);
			}

		}

		return isCompleted;
	}
	
	private void updOption(ActivityExecution execution, Short signStatus) {
		String multiInstance = (String) execution.getActivity().getProperty("multiInstance");
		String nodeId = execution.getCurrentActivityId();
		String actInstId = execution.getProcessInstanceId();
		if (!"parallel".equals(multiInstance))
			return;

		Short status = getStatus(signStatus);

		List<TaskOpinionEntity> list = this.taskOpinionService.getByActInstIdTaskKeyStatus(actInstId, nodeId,TaskOpinionEntity.STATUS_CHECKING.intValue());
		for (TaskOpinionEntity taskOpinion : list) {
			taskOpinion.setCheckStatus(status.intValue());
			taskOpinion.setEndTime(new Date());
			String duration =DateUtils.getTime(DateUtils.getTime(taskOpinion.getCreateTime(), taskOpinion.getEndTime()));
			taskOpinion.setDurTime(duration);
			try {
				this.taskOpinionService.update(taskOpinion);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void updOptionVoteStatus(ActivityExecution execution, Short signStatus) {
		String nodeId = execution.getCurrentActivityId();
		String actInstId = execution.getProcessInstanceId();
		List<TaskOpinionEntity> list = this.taskOpinionService.getByActInstIdTaskKey(actInstId, nodeId);
		for (TaskOpinionEntity taskOpinion : list) {
			taskOpinion.setVoteStatus(signStatus.toString());
			try {
				this.taskOpinionService.update(taskOpinion);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Short getStatus(Short signResult) {
		ProcessCmd cmd = TaskThreadService.getProcessCmd();
		Short status = TaskOpinionEntity.STATUS_PASS_CANCEL;

		int isBack = cmd.isBack().intValue();
		boolean isRevover = cmd.isRecover();
		switch (isBack) {
		case 0://正常流转
			/*if (TaskOpinionEntity.STATUS_PASSED.equals(signResult)) {
				status = TaskOpinionEntity.STATUS_PASS_CANCEL;
			} else {
				status = TaskOpinionEntity.STATUS_REFUSE_CANCEL;
			}*/
			break;
		case 1://驳回
			break;
		case 2://驳回到发起人
			if (isRevover) {
				status = TaskOpinionEntity.STATUS_REVOKED_CANCEL;
			} else {
				status = TaskOpinionEntity.STATUS_BACK_CANCEL;
			}
		}

		return status;
	}
	
	/**  
	 * 根据会签规则计算投票结果。  
	 * <pre>  
	 * 1.如果会签规则为空，那么需要所有的人同意通过会签，否则不通过。  
	 * 2.否则按照规则计算投票结果。  
	 * </pre>  
	 * @param bpmNodeSign       会签规则  
	 * @param actInstId         流程实例ID  
	 * @param nodeId            节点id名称  
	 * @param completeCounter       循环次数  
	 * @param instanceOfNumbers     总的会签次数。  
	 * @return  
	 */  
	private VoteResult calcResult(NodeSignEntity bpmNodeSign, String actInstId,
			String nodeId, Integer completeCounter, Integer instanceOfNumbers,
			Short oneVoteResult,Short approvalStatus) {
		VoteResult voteResult = new VoteResult();
		//同意的票数
		Integer agreeAmount = this.taskSignDataService.getAgreeVoteCount(actInstId, nodeId);
		//拒绝的票数
		Integer refuseAmount = this.taskSignDataService.getRefuseVoteCount(actInstId, nodeId);
		//再议的票数
		Integer reconsideAmount=this.taskSignDataService.getReconsideVoteCount(actInstId, nodeId);
		
		/*if(approvalStatus.toString().equals(TaskOpinionEntity.STATUS_AGREE.toString()) || approvalStatus==TaskOpinionEntity.STATUS_PASSED){
			agreeAmount++;
		}else if(approvalStatus.toString().equals(TaskOpinionEntity.STATUS_REFUSE.toString()) || approvalStatus==TaskOpinionEntity.STATUS_NOT_PASSED){
			refuseAmount++;
		}else if(approvalStatus.toString().equals(TaskOpinionEntity.STATUS_ABANDON.toString()) || approvalStatus==TaskOpinionEntity.STATUS_NOT_PASSED){
			reconsideAmount++;
		}*/
		if (bpmNodeSign == null) {//没有设置会签规则
			voteResult = getResultNoRule(oneVoteResult,agreeAmount,refuseAmount,reconsideAmount, instanceOfNumbers,approvalStatus);
			return voteResult;
		}
		//设置了会签规则
		voteResult = getResultByRule(bpmNodeSign, oneVoteResult, agreeAmount,refuseAmount,reconsideAmount, completeCounter, instanceOfNumbers,approvalStatus);
		return voteResult;
	}
	
	/**
	 * 根据设定的规则计算结果
	 * @param bpmNodeSign
	 * @param oneVoteResult
	 * @param agreeAmount
	 * @param refuseAmount
	 * @param completeCounter
	 * @param instanceOfNumbers
	 * @return
	 */
	private VoteResult getResultByRule(NodeSignEntity bpmNodeSign,
			Short oneVoteResult, Integer agreeAmount, Integer refuseAmount,Integer reconsideAmount,
			Integer completeCounter, Integer instanceOfNumbers,short approvalStatus) {
		VoteResult voteResult = new VoteResult();
		
		String result="";
		if(approvalStatus == (short)1){
			result="pass";
		}else if(approvalStatus == (short)2){
			result="refuse";
			if(oneVoteResult != null){//如果是一票否决
				voteResult = new VoteResult(result, true);
				return voteResult;
			}
		}else{
			result="reconside";
		}

		if (NodeSignEntity.VOTE_TYPE_PERCENT.equals(bpmNodeSign.getVoteType())) {
			voteResult = getResultByPercent(bpmNodeSign, agreeAmount,refuseAmount,reconsideAmount, instanceOfNumbers, completeCounter,result);
		} else {
			voteResult = getResultByVotes(bpmNodeSign, agreeAmount,refuseAmount,reconsideAmount, instanceOfNumbers, completeCounter,result);
		}

		return voteResult;
	}

	/**
	 * 没有设定规则的计算
	 * @param oneVoteResult
	 * @param refuseAmount
	 * @param agreeAmount
	 * @param instanceOfNumbers
	 * @return
	 */
	private VoteResult getResultNoRule(Short oneVoteResult, Integer agreeAmount, Integer refuseAmount,Integer reconsideAmount,Integer instanceOfNumbers,short approvalStatus) {
		VoteResult voteResult = new VoteResult();

		String result="";
		if(approvalStatus == (short)1){
			result="pass";
		}else if(approvalStatus == (short)2){
			result="refuse";
			if(oneVoteResult != null){//如果是一票否决
				voteResult = new VoteResult(result, true);
				return voteResult;
			}
		}else{
			result="reconside";
		}
		voteResult.setSignResult(result);
		voteResult.setIsComplete(true);
		return voteResult;
	}
	
	/**
	 * 绝对的票数
	 * @param bpmNodeSign
	 * @param agree
	 * @param refuse
	 * @param instanceOfNumbers
	 * @param completeCounter
	 * @return
	 */
	private VoteResult getResultByVotes(NodeSignEntity bpmNodeSign, Integer agree,
			Integer refuse, Integer instanceOfNumbers,Integer reconside, Integer completeCounter,String resutltStr) {
		boolean isComplete = instanceOfNumbers.equals(completeCounter);
		VoteResult voteResult = new VoteResult();
		String result = "";
		boolean isDirect = StringUtil.equals(bpmNodeSign.getCompleteType(), NodeSignEntity.FLOW_MODE_DIRECT);
		boolean isPass = false;

		
		if (bpmNodeSign.getVoteAgreeAmount()!=null && agree.intValue() >= bpmNodeSign.getVoteAgreeAmount().longValue()) {
			result = "pass";
			isPass = true;
		} else if(bpmNodeSign.getVoteRefuseAmount()!=null && refuse.intValue() >= bpmNodeSign.getVoteRefuseAmount().longValue()){
			result = "refuse";
			isPass = true;
		}else if(bpmNodeSign.getVoteReconsideAmount()!=null && reconside.intValue() >= bpmNodeSign.getVoteReconsideAmount().longValue()){
			result = "reconside";
			isPass = true;
		}else {
			if (isComplete) {//完成所有的投票
				result = "reconside";
			}else{
				result = resutltStr;
			}
		}

		if ((isDirect) && (isPass)) {
			voteResult = new VoteResult(result, true);
		} else if (isComplete) {
			voteResult = new VoteResult(result, true);
		}
		return voteResult;
	}

	/**
	 * 百分比计算
	 * @param bpmNodeSign
	 * @param agree
	 * @param refuse
	 * @param instanceOfNumbers
	 * @param completeCounter
	 * @return
	 */
	private VoteResult getResultByPercent(NodeSignEntity bpmNodeSign,
			Integer agree, Integer refuse,Integer reconside, Integer instanceOfNumbers,
			Integer completeCounter,String resutltStr) {
		boolean isComplete = instanceOfNumbers.equals(completeCounter);
		VoteResult voteResult = new VoteResult();
		String result = "";
		boolean isPass = false;
		boolean isDirect = StringUtil.equals(bpmNodeSign.getCompleteType(), NodeSignEntity.FLOW_MODE_DIRECT);
		float agreePercents = (float)agree.intValue() / (float)instanceOfNumbers.intValue();
		float refusePercents =(float)refuse.intValue() / (float)instanceOfNumbers.intValue();
		float reconsidePercents =(float)reconside.intValue() / (float)instanceOfNumbers.intValue();
		
		if (bpmNodeSign.getVoteAgreeAmount()!=null && agreePercents * 100.0F >= (float) bpmNodeSign.getVoteAgreeAmount().floatValue()) {
			result = "pass";
			isPass = true;
		} else if(bpmNodeSign.getVoteAgreeAmount()!=null && refusePercents * 100.0F >= (float) bpmNodeSign.getVoteAgreeAmount().floatValue()){
			result = "refuse";
			isPass = true;
		}else if(bpmNodeSign.getVoteAgreeAmount()!=null && reconsidePercents * 100.0F >= (float) bpmNodeSign.getVoteAgreeAmount().floatValue()){
			result = "reconside";
			isPass = true;
		}else {
			if (isComplete) {//完成所有的投票
				result = "reconside";
			}else{
				result = resutltStr;
			}
		}

		if ((isDirect) && (isPass)) {//达到就结束投票
			voteResult = new VoteResult(result, true);
		} else if (isComplete) {//完成所有的投票
			voteResult = new VoteResult(result, true);
		}
		return voteResult;
	}

}
/**
 * 
 * description :投票结果
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年9月23日 下午3:14:44
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年9月23日 下午3:14:44
 *
 */
class VoteResult {
	private String signResult = "";//投票结果（pass，refuse）

	private boolean isComplete = false;//是否完成会签

	public VoteResult() {
	}

	public VoteResult(String signResult, boolean isComplate) {
		this.signResult = signResult;
		this.isComplete = isComplate;
	}

	public String getSignResult() {
		return this.signResult;
	}

	public void setSignResult(String signResult) {
		this.signResult = signResult;
	}

	public boolean getIsComplete() {
		return this.isComplete;
	}

	public void setIsComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}
}
