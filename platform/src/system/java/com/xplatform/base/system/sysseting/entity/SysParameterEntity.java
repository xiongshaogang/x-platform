package com.xplatform.base.system.sysseting.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;

import javax.xml.soap.Text;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

import javax.persistence.SequenceGenerator;

/**   
 * @Title: Entity
 * @Description: 系统参数
 * @author onlineGenerator
 * @date 2014-05-19 17:01:32
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_sys_parameter", schema = "")
@SuppressWarnings("serial")
public class SysParameterEntity extends OperationEntity implements java.io.Serializable {
	
	/**参数代码*/
	private java.lang.String code;
	/**参数名称*/
	private java.lang.String name;
	/**参数值*/
	private java.lang.String value;
	/**有效标识*/
	private java.lang.String flag;
	/**是否可修改*/
	private java.lang.String updateFlag;
	/**描述*/
	private java.lang.String description;
	/**参数设置类型，来自数据字典*/
	private java.lang.String type;
	/**设置类别中文名称，来自数据字典*/
	private java.lang.String typename;
	/**备用字段1，当设置类别为表单验证时，存放的是备注信息
	 * 
	 * */
	private java.lang.String reserve1;
	/** 备用字段2*/
	private java.lang.String reserve2;
	/**备用字段3*/
	private java.lang.String reserve3;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  参数代码
	 */
	@Column(name ="CODE",nullable=true,length=64)
	public java.lang.String getCode(){
		return this.code;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  参数代码
	 */
	public void setCode(java.lang.String code){
		this.code = code;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  参数名称
	 */
	@Column(name ="NAME",nullable=true,length=100)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  参数名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  参数值
	 */
	@Column(name ="VALUE",nullable=true,length=1000)
	public java.lang.String getValue(){
		return this.value;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  参数值
	 */
	public void setValue(java.lang.String value){
		this.value = value;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  有效标识
	 */
	@Column(name ="FLAG",nullable=true,length=10)
	public java.lang.String getFlag(){
		return this.flag;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  有效标识
	 */
	public void setFlag(java.lang.String flag){
		this.flag = flag;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否可修改
	 */
	@Column(name ="UPDATE_FLAG",nullable=true,length=10)
	public java.lang.String getUpdateFlag(){
		return this.updateFlag;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否可修改
	 */
	public void setUpdateFlag(java.lang.String updateFlag){
		this.updateFlag = updateFlag;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  描述
	 */
	@Column(name ="DESCRIPTION",nullable=true,length=200)
	public java.lang.String getDescription(){
		return this.description;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  描述
	 */
	public void setDescription(java.lang.String description){
		this.description = description;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  参数类型
	 */
	@Column(name ="TYPE",nullable=true,length=64)
	public java.lang.String getType(){
		return this.type;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  参数类型
	 */
	public void setType(java.lang.String type){
		this.type = type;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  描述
	 */
	@Column(name ="TYPENAME",nullable=true,length=100)
	public java.lang.String getTypename() {
		return typename;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  参数类型
	 */
	public void setTypename(java.lang.String settingtypename) {
		this.typename = settingtypename;
	}


	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  描述
	 */
	@Column(name ="RESERVE1",nullable=true,length=100)
	public java.lang.String getReserve1() {
		return reserve1;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  参数类型
	 */
	public void setReserve1(java.lang.String reserve1) {
		this.reserve1 = reserve1;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  描述
	 */
	@Column(name ="RESERVE2",nullable=true,length=100)
	public java.lang.String getReserve2() {
		return reserve2;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  参数类型
	 */
	public void setReserve2(java.lang.String reserve2) {
		this.reserve2 = reserve2;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  描述
	 */
	@Column(name ="RESERVE3",nullable=true,length=100)
	public java.lang.String getReserve3() {
		return reserve3;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  参数类型
	 */
	public void setReserve3(java.lang.String reserve3) {
		this.reserve3 = reserve3;
	}
	
	
}
