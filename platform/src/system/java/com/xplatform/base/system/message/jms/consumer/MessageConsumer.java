package com.xplatform.base.system.message.jms.consumer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.system.message.jms.handler.JmsHandler;

/**
 * 
 * description : 消费者根据继承JmsHandler的种类进行消费
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月20日 下午4:36:42
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年6月20日 下午4:36:42
 *
 */
public class MessageConsumer {
	private Map<String, JmsHandler> handlers = new HashMap();

	protected Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

	public void setHandlers(Map<String, JmsHandler> handlers) {
		this.handlers = handlers;
	}

	/**
	 * 消费者发送消息
	 * @author xiehs
	 * @createtime 2014年6月20日 下午4:38:04
	 * @Decription
	 *
	 * @param model
	 * @throws Exception
	 */
	public void sendMessage(Object model) throws Exception {
		if ((BeanUtils.isNotEmpty(this.handlers)) && (BeanUtils.isNotEmpty(model))) {
			JmsHandler jmsHandler = (JmsHandler) this.handlers.get(model.getClass().getName());
			if (jmsHandler != null) {
				jmsHandler.handMessage(model);
			} else {
				this.logger.info(model.toString());
			}
		} else {
			throw new Exception("Object:[" + model + "] is not  entity Object ");
		}
	}
}
