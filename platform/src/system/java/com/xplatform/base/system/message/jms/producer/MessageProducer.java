package com.xplatform.base.system.message.jms.producer;

import javax.annotation.Resource;
import javax.jms.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.platform.common.service.SysUserService;

/**
 * 
 * description :消息生产者
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2015年9月15日23:09:28
 *
 */
public class MessageProducer {
	private static final Log logger = LogFactory.getLog(MessageProducer.class);

	@Resource
	private SysUserService sysUserService;

	/**
	 * 加入消息队列
	 * 
	 * @author xiehs
	 * @createtime 2014年6月20日 下午4:41:22
	 * @Decription
	 *
	 * @param model
	 */
	public void send(Object model) {
		JmsTemplate jmsTemplate = ApplicationContextUtil.getBean("jmsTemplate");
		Queue messageQueue = ApplicationContextUtil.getBean("messageQueue");
		logger.debug("procduce the message");
		jmsTemplate.convertAndSend(messageQueue, model);
	}
}
