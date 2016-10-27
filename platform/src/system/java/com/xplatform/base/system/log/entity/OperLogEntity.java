package com.xplatform.base.system.log.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationDeptEntity;
import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**   
 * @Title: Entity
 * @Description: 系统日志
 * @author onlineGenerator
 * @date 2014-06-17 19:16:27
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_sys_oper_log", schema = "")
@SuppressWarnings("serial")
public class OperLogEntity extends OperationEntity implements java.io.Serializable {
	private String opname;//opname
	private String userId;//user_id
	private String userName;//user_name
	private Date time;//time
	private String ip;//ip
	private String method;//method
	private String requesturi;//requesturi
	private String params;//params
	private String moduleName;//module_name
	private String detail;//detail
	
	@Column(name ="OPNAME",nullable=true,length=100)
	public String getOpname(){
		return this.opname;
	}

	public void setOpname(String opname){
		this.opname = opname;
	}
	@Column(name ="USER_ID",nullable=true,length=32)
	public String getUserId(){
		return this.userId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}
	@Column(name ="USER_NAME",nullable=true,length=50)
	public String getUserName(){
		return this.userName;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}
	@Column(name ="TIME",nullable=true,length=50)
	public Date getTime(){
		return this.time;
	}

	public void setTime(Date time){
		this.time = time;
	}
	@Column(name ="IP",nullable=true,length=20)
	public String getIp(){
		return this.ip;
	}

	public void setIp(String ip){
		this.ip = ip;
	}
	@Column(name ="METHOD",nullable=true,length=1000)
	public String getMethod(){
		return this.method;
	}

	public void setMethod(String method){
		this.method = method;
	}
	@Column(name ="REQUESTURI",nullable=true,length=256)
	public String getRequesturi(){
		return this.requesturi;
	}

	public void setRequesturi(String requesturi){
		this.requesturi = requesturi;
	}
	@Column(name ="PARAMS",nullable=true,length=1000)
	public String getParams(){
		return this.params;
	}

	public void setParams(String params){
		this.params = params;
	}
	@Column(name ="MODULE_NAME",nullable=true,length=50)
	public String getModuleName(){
		return this.moduleName;
	}

	public void setModuleName(String moduleName){
		this.moduleName = moduleName;
	}
	@Column(name ="DETAIL",nullable=true,length=1000)
	public String getDetail(){
		return this.detail;
	}

	public void setDetail(String detail){
		this.detail = detail;
	}


	
	
}
