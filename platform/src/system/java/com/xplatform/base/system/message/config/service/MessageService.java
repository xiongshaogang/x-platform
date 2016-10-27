package com.xplatform.base.system.message.config.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.system.message.config.entity.ApproveEntity;
import com.xplatform.base.system.message.config.entity.BusinessEntity;
import com.xplatform.base.system.message.config.entity.MailConfigEntity;
import com.xplatform.base.system.message.config.entity.MessageFromEntity;
import com.xplatform.base.system.message.config.entity.MessageReceiveEntity;
import com.xplatform.base.system.message.config.entity.MessageGroupSendEntity;
import com.xplatform.base.system.message.config.entity.MessageSend;
import com.xplatform.base.system.message.config.entity.MessageToEntity;
import com.xplatform.base.system.message.config.entity.SendUnionEntity;
import com.xplatform.base.system.message.config.mybatis.vo.InnerMessageVo;

public interface MessageService {

	void getDataGridReturn(CriteriaQuery cq, boolean b);

	MailConfigEntity getMailConfig(String id);

	void updateMailConfig(MailConfigEntity mailConfig);

	void saveMailConfig(MailConfigEntity mailConfig);

	public void batchSaveMessageFrom(List<MessageFromEntity> messageFromList);

	void deleteMailConfig(String id);

	List<MailConfigEntity> getMailConfigListByUser(String id);

	void testConnection(MailConfigEntity mail) throws Exception;

	MessageFromEntity getMessageFrom(String id);

	BusinessEntity getBusinessByFromId(String id);

	void saveMsg(MessageFromEntity messageFrom, BusinessEntity business);

	List<MessageToEntity> getMessageToByFromId(String id);

	void saveMsgTo(MessageToEntity msgTo);

	public void saveMsg(MessageFromEntity messageFrom);

	List<MessageFromEntity> queryMessageFromList();

	void updateMsgFrom(MessageFromEntity msg);

	List<MessageFromEntity> querySmsFromList();

	MessageToEntity getMessageToById(String id);

	void updateMsgTo(MessageToEntity to);

	void deleteMsgTo(String id);

	void deleteMsgFrom(String id);

	void saveApprove(ApproveEntity app);

	ApproveEntity queryApproveEntity(Map<String, String> param);

	String getReference(MessageFromEntity messageFrom, BusinessEntity business);

	List<ApproveEntity> queryApproveEntityList(Map<String, String> param);

	void updateMsgFrom(MessageFromEntity messageFrom, BusinessEntity business);

	/* 以下方法为 通用消息管理方法 */
	/**
	 * 查找待发送的消息列表
	 * 
	 * @author binyong
	 */
	List<MessageGroupSendEntity> queryMsgSendList();

	/**
	 * 更新消息发送表
	 * 
	 * @author binyong
	 */
	void updateMsgSend(MessageGroupSendEntity msgSend);

	/**
	 * 根据id获取消息发送表记录
	 * 
	 * @author binyong
	 */
	MessageGroupSendEntity getMessageSend(String id);

	/**
	 * 根据发送表Id获取消息接收列表数据
	 * 
	 * @author binyong
	 */
	List<MessageReceiveEntity> getMessageReceiveList(String id);

	/**
	 * 查询该条日志最新的提醒记录
	 * 
	 * @author binyong
	 */
	Date queryMsgSendMaxDate(Map<String, String> map);

	/**
	 * 根据消息接收表id查找数据
	 * 
	 * @author binyong
	 */
	MessageReceiveEntity getMsgReceiveEntity(String id);

	/**
	 * 更新消息接收表
	 * 
	 * @author binyong
	 */
	void updateMsgReceive(MessageReceiveEntity msgReceive);

	/**
	 * @author xiaqiang
	 * @createtime 2014年11月13日 下午3:41:44
	 * @Decription 查找登录人所接收到的站内信
	 *
	 * @param param
	 * @return
	 */
	public List<InnerMessageVo> queryInnerMessageByUser(Map<String, Object> param);

	/**
	 * @author xiaqiang
	 * @createtime 2014年11月16日 下午6:51:28
	 * @Decription 查询系统消息分页列表数据
	 *
	 * @param page
	 * @return
	 */
	public Page<InnerMessageVo> queryInnerMessageByPage(Page<InnerMessageVo> page);

	/**
	 * @author xiaqiang
	 * @createtime 2014年12月5日 上午9:21:03
	 * @Decription 更新站内信状态为已读
	 *
	 * @param id
	 * @throws BusinessException
	 */
	public void updateInnerMessageToRead(String id) throws BusinessException;

	/**
	 * 更新发送表状态和日志
	 * 
	 * @param sendType
	 * @param sendChannel
	 * @param sendId
	 * @param status
	 * @throws BusinessException
	 */
	public void updateSendStatus(String sendType, String sendChannel, String sendId, Integer status, String log) throws BusinessException;

	/**
	 * 保存多类型接受方记录
	 * 
	 * @author xiaqiang
	 * @createtime 2014年12月5日 上午9:21:03
	 */
	public void saveSendUnion(SendUnionEntity sendUnion);

	/**
	 * 保存消息发送表数据
	 */
	public String saveMsgSend(MessageSend send);

	/**
	 * 保存send表记录,并设置各类默认值
	 * 
	 * @author xiaqiang
	 * @createtime 2014年12月5日 上午9:21:03
	 */
	public String saveAndProcessMsgSend(MessageSend messageSend) throws BusinessException;

	/**
	 * 设置接受表消息为已读
	 */
	public Integer doReadMessageReceive(String sendId, String userId) throws BusinessException;

	/**
	 * 假删除接收表消息为已读
	 */
	public Integer deleteMessageReceive(String id) throws BusinessException;

	/**
	 * 保存消息接收表
	 */
	public MessageReceiveEntity saveMessageReceive(String sendId, String userId, String funcType) throws BusinessException;

	/**
	 * 保存消息接收表
	 */
	public String saveMessageReceive(MessageReceiveEntity receive) throws BusinessException;
	
	/**
	 * 获得Receive记录
	 * @param userId
	 * @param status
	 * @return
	 */
	public List<MessageReceiveEntity> queryMessageReceives(String userId, String status);
	
	/**
	 * 根据来源相关参数获得Receive记录
	 * @param sourceType
	 * @param sourceId
	 * @return
	 */
	public List<MessageReceiveEntity> queryMessageReceivesBySource(String sourceType, String sourceId) ;
	
	/**
	 * 通过来源设置为消息已读
	 * @param sourceId
	 * @param sourceBusinessType
	 * @return
	 * @throws BusinessException
	 */
	public Integer doReadMessageReceiveBySource(String sourceId, String sourceBusinessType) throws BusinessException ;
}
