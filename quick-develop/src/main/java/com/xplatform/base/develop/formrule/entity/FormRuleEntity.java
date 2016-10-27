package com.xplatform.base.develop.formrule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**   
 * @Title: Entity
 * @Description: 表单校验规则
 * @author onlineGenerator
 * @date 2014-05-15 17:19:29
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_dev_form_rule", schema = "")
public class FormRuleEntity extends OperationEntity {
	/**名称*/
	private java.lang.String name;
	/**规则*/
	private java.lang.String regulation;
	/**提示信息*/
	private java.lang.String tipInfo;
	/**备注*/
	private java.lang.String memo;
	

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  名称
	 */
	@Column(name ="NAME",nullable=true,length=100)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  规则
	 */
	@Column(name ="REGULATION",nullable=true,length=200)
	public java.lang.String getRegulation(){
		return this.regulation;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  规则
	 */
	public void setRegulation(java.lang.String regulation){
		this.regulation = regulation;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  提示信息
	 */
	@Column(name ="TIP_INFO",nullable=true,length=300)
	public java.lang.String getTipInfo(){
		return this.tipInfo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  提示信息
	 */
	public void setTipInfo(java.lang.String tipInfo){
		this.tipInfo = tipInfo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="MEMO",nullable=true,length=300)
	public java.lang.String getMemo(){
		return this.memo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setMemo(java.lang.String memo){
		this.memo = memo;
	}
}
