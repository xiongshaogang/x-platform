package com.xplatform.base.system.message.config.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name = "t_sys_message_send_group")
public class MessageGroupSendEntity extends MessageSend implements Serializable {

}
