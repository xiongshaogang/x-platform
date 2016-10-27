package com.xplatform.base.system.areas.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.AssignedIdEntity;
import com.xplatform.base.framework.core.common.entity.AssignedOperationEntity;
import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 即时聊天消息备份表
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2015年6月3日 下午5:09:29
 * 
 *
 */

@Entity
@Table(name = "t_sys_areas")
public class AreasEntity extends AssignedIdEntity{
	
	private String parentId;// 父区域Id
	private String areaName;// 区域名称
	private String zipCode;// 邮编

	@Column(name = "parentId", length = 32)
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Column(name = "areaName", length = 100)
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Column(name = "zipCode", length = 10)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}
