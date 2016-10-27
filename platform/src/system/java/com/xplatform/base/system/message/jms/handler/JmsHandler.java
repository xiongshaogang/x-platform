package com.xplatform.base.system.message.jms.handler;

/**
 * 
 * description :消息处理接口
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月20日 下午4:41:46
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年6月20日 下午4:41:46
 *
 */
public interface JmsHandler {
	/**
	 * 发送消息
	 * @author xiehs
	 * @createtime 2014年6月20日 下午4:42:10
	 * @Decription
	 *
	 * @param paramObject
	 */
	public abstract void handMessage(Object paramObject);
}
