package com.xplatform.base.system.message.jms.service;

import org.apache.activemq.web.MessageQuery;
import org.apache.activemq.web.QueueBrowseQuery;

/**
 * 
 * description :消息监控管理service
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月20日 下午5:07:41
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年6月20日 下午5:07:41
 *
 */
public interface QueuesService {

	/**
	 * 增加消息发送优先级
	 * @author xiehs
	 * @createtime 2014年6月20日 下午5:05:15
	 * @Decription 
	 *
	 * @param JMSDestination
	 * @throws Exception
	 */
	public void purgeDestination(String JMSDestination) throws Exception ;

	/**
	 * 移除消息
	 * @author xiehs
	 * @createtime 2014年6月20日 下午5:05:37
	 * @Decription
	 *
	 * @param JMSDestination
	 * @param messageId
	 * @throws Exception
	 */
	public void removeMessage(String JMSDestination, String messageId)throws Exception ;

	/**
	 * 查看队列分页列表
	 * @author xiehs
	 * @createtime 2014年6月20日 下午5:05:52
	 * @Decription
	 *
	 * @param JMSDestination
	 * @return
	 * @throws Exception
	 */
	public QueueBrowseQuery getQueueBrowseQuery(String JMSDestination)throws Exception ;

	/**
	 * 获取消息分页列表
	 * @author xiehs
	 * @createtime 2014年6月20日 下午5:05:57
	 * @Decription
	 *
	 * @param id
	 * @param JMSDestination
	 * @return
	 * @throws Exception
	 */
	public MessageQuery getMessageQuery(String id, String JMSDestination)throws Exception ;

	/**
	 * 消除监控
	 * @author xiehs
	 * @createtime 2014年6月20日 下午5:06:02
	 * @Decription
	 *
	 * @param JMSDestination
	 * @throws Exception
	 */
	public void removeDestinationQueue(String JMSDestination) throws Exception;
}
