package com.xplatform.base.system.message.jms.converter;

import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import org.springframework.jms.support.converter.MessageConverter;

/**
 * 
 * description :active消息转换
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月20日 下午4:44:07
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年6月20日 下午4:44:07
 *
 */
public class ActiveMqMessageConverter implements MessageConverter {
	public Message toMessage(Object object, Session session)
			throws JMSException {
		if ((object != null) && (object.getClass() != null)) {
			ObjectMessage objMsg = session.createObjectMessage();
			objMsg.setObject((Serializable) object);
			return objMsg;
		}
		throw new JMSException("Object:[" + object + "] is not legal message");
	}
	
	public Object fromMessage(Message msg) throws JMSException {
		if ((msg instanceof ObjectMessage)) {
			ObjectMessage objMsg = (ObjectMessage) msg;
			Object object = objMsg.getObject();
			if ((object != null) && (object.getClass() != null)) {
				return object;
			}
			throw new JMSException("Object:[" + msg + "] is not legal message");
		}

		throw new JMSException("Msg:[" + msg + "] is not ObjectMessage");
	}
}