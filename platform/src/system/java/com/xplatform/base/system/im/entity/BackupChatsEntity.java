package com.xplatform.base.system.im.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
@Table(name = "t_sys_backupchats")
public class BackupChatsEntity extends OperationEntity {
	private Date finishTime; // 备份完成时间(中途终止则为空)
	private Date startTimestamp; // 备份的聊天记录开始时间
	private Date endTimestamp; // 备份的聊天记录结束时间
	private Integer counts; // 备份数据量
	private Integer status; // 执行状态(0.正在运行/异常终止 1.正常完成 2.重试后停止)

	@Column(name = "finishTime")
	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@Column(name = "startTimestamp")
	public Date getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(Date startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	@Column(name = "endTimestamp")
	public Date getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(Date endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	@Column(name = "counts")
	public Integer getCounts() {
		return counts;
	}

	public void setCounts(Integer counts) {
		this.counts = counts;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
