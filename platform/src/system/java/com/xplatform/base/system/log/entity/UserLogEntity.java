package com.xplatform.base.system.log.entity;

import java.util.Date;
import java.lang.String;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**   
 * @Title: Entity
 * @Description: 用户日志
 * @author onlineGenerator
 * @date 2014-06-17 10:05:54
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_sys_user_log", schema = "")
@SuppressWarnings("serial")
public class UserLogEntity extends OperationEntity implements java.io.Serializable {
	private String userId;//用户ID
	private String userName;//用户名
	private Date time;//操作时间
	private String ip;//ip地址
	private String content;//日志内容
	private String status;//状态
	
	@Column(name ="USER_ID",nullable=true,length=32)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name ="USER_NAME",nullable=true,length=100)
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
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name ="CONTENT",nullable=true,length=100)
	public String getContent(){
		return this.content;
	}

	public void setContent(String content){
		this.content = content;
	}
	@Column(name ="STATUS",nullable=true,length=1)
	public String getStatus(){
		return this.status;
	}

	public void setStatus(String status){
		this.status = status;
	}
	
}
