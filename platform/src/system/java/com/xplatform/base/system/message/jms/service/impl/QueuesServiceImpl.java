package com.xplatform.base.system.message.jms.service.impl;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.broker.jmx.DestinationViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.pool.PooledSession;
import org.apache.activemq.web.MessageQuery;
import org.apache.activemq.web.QueueBrowseQuery;
import org.apache.activemq.web.RemoteJMXBrokerFacade;
import org.apache.activemq.web.SessionPool;
import org.apache.activemq.web.config.SystemPropertiesConfiguration;
import org.springframework.stereotype.Service;

import com.xplatform.base.system.message.jms.producer.MessageProducer;
import com.xplatform.base.system.message.jms.service.QueuesService;

/**
 * 消息监控实现service
 * description :
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月20日 下午5:09:03
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年6月20日 下午5:09:03
 *
 */
@Service("queuesServiceImpl")
public class QueuesServiceImpl implements QueuesService {
	@Resource
	protected MessageProducer messageProducer;

	@Resource
	protected ConnectionFactory simpleJmsConnectionFactory;
	private RemoteJMXBrokerFacade brokerFacade;
	private String jmxHostIp = "127.0.0.1";

	private RemoteJMXBrokerFacade getBrokerFacade() {
		if (this.brokerFacade == null) {
			this.brokerFacade = new RemoteJMXBrokerFacade();
			//this.jmxHostIp = AppConfigUtil.get("jms.ip");
			System.setProperty("webconsole.jmx.url",
					"service:jmx:rmi:///jndi/rmi://" + this.jmxHostIp
							+ ":1099/jmxrmi");

			SystemPropertiesConfiguration configuration = new SystemPropertiesConfiguration();

			this.brokerFacade.setConfiguration(configuration);
		}
		return this.brokerFacade;
	}

	private QueueViewMBean getQueue(String name) throws Exception {
		QueueViewMBean qvb = null;
		qvb = (QueueViewMBean) getDestinationByName(getQueues(), name);
		return qvb;
	}

	private DestinationViewMBean getDestinationByName(
			Collection<? extends DestinationViewMBean> collection, String name) {
		Iterator iter = collection.iterator();
		while (iter.hasNext()) {
			DestinationViewMBean destinationViewMBean = (DestinationViewMBean) iter
					.next();
			if (name.equals(destinationViewMBean.getName())) {
				return destinationViewMBean;
			}
		}
		return null;
	}

	private SessionPool getSessionPool() throws Exception {
		SessionPool sp = new SessionPool();
		sp.setConnectionFactory(this.simpleJmsConnectionFactory);
		sp.setConnection(sp.getConnection());
		Session session = sp.borrowSession();
		ActiveMQSession am = null;
		if ((session instanceof ActiveMQSession)) {
			am = (ActiveMQSession) session;
		}
		if ((session instanceof PooledSession)) {
			PooledSession pooledSession = (PooledSession) session;
			am = pooledSession.getInternalSession();
		}
		sp.returnSession(am);
		return sp;
	}

	public Collection<QueueViewMBean> getQueues() throws Exception {
		return getBrokerFacade().getQueues();
	}

	public void purgeDestination(String JMSDestination) throws Exception {
		getBrokerFacade().purgeQueue(ActiveMQDestination.createDestination(JMSDestination, (byte) 1));
	}

	public void removeMessage(String JMSDestination, String messageId)
			throws Exception {
		QueueViewMBean queueView = getQueue(JMSDestination);
		queueView.removeMessage(messageId);
	}

	public QueueBrowseQuery getQueueBrowseQuery(String JMSDestination)
			throws Exception {
		QueueBrowseQuery qbq = new QueueBrowseQuery(getBrokerFacade(),
				getSessionPool());
		qbq.setJMSDestination(JMSDestination);
		return qbq;
	}

	public MessageQuery getMessageQuery(String id, String JMSDestination)
			throws Exception {
		MessageQuery mq = new MessageQuery(getBrokerFacade(), getSessionPool());
		mq.setJMSDestination(JMSDestination);
		mq.setId(id);
		return mq;
	}

	public void removeDestinationQueue(String JMSDestination) throws Exception {
		getBrokerFacade().getBrokerAdmin().removeQueue(JMSDestination);
	}
}
