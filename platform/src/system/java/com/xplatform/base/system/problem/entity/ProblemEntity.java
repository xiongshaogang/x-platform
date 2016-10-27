package com.xplatform.base.system.problem.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.AssignedOperationEntity;

/**
 * 
 * description : 问题反馈表
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年10月20日 下午3:06:01
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * hexj        2014年10月20日 下午3:06:01
 *
 */

@Entity
@Table(name = "t_sys_problem")
@SuppressWarnings("serial")
public class ProblemEntity extends AssignedOperationEntity implements Serializable{
	private String problemType;        //问题类型   bug,用户体验 experience两种 
	private String content;            //反馈内容
	private String contactInformation; //联系方式
	private String problemState;       //状态 : 0未读, 1已读 ,2已解决
	private String resolveSolution;    //解决方案
	
	@Column(name = "problem_type", length = 32)
	public String getProblemType() {
		return problemType;
	}
	
	public void setProblemType(String problemType) {
		this.problemType = problemType;
	}
	
	@Column(name = "content", length = 1000)
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name = "contact_information", length = 100)
	public String getContactInformation() {
		return contactInformation;
	}
	
	public void setContactInformation(String contactInformation) {
		this.contactInformation = contactInformation;
	}

	@Column(name = "problem_state", length = 2)
	public String getProblemState() {
		return problemState;
	}

	public void setProblemState(String problemState) {
		this.problemState = problemState;
	}

	@Column(name = "resolve_solution", length = 1000)
	public String getResolveSolution() {
		return resolveSolution;
	}

	public void setResolveSolution(String resolveSolution) {
		this.resolveSolution = resolveSolution;
	}
	
}
