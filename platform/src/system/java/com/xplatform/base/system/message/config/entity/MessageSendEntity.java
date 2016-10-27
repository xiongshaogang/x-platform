package com.xplatform.base.system.message.config.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_sys_message_send")
public class MessageSendEntity extends MessageSend implements Serializable {

}
