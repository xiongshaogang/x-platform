package com.xplatform.base.system.message.config.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.platform.common.mybatis.dao.SysUserMybatisDao;
import com.xplatform.base.system.message.config.dao.MessageDao;
import com.xplatform.base.system.message.config.entity.ApproveEntity;
import com.xplatform.base.system.message.config.entity.BusinessEntity;
import com.xplatform.base.system.message.config.entity.MailConfigEntity;
import com.xplatform.base.system.message.config.entity.MessageFromEntity;
import com.xplatform.base.system.message.config.entity.MessageGroupSendEntity;
import com.xplatform.base.system.message.config.entity.MessageReceiveEntity;
import com.xplatform.base.system.message.config.entity.MessageSend;
import com.xplatform.base.system.message.config.entity.MessageSendEntity;
import com.xplatform.base.system.message.config.entity.MessageToEntity;
import com.xplatform.base.system.message.config.entity.SendUnionEntity;
import com.xplatform.base.system.message.config.mybatis.dao.MessageMybatisDao;
import com.xplatform.base.system.message.config.mybatis.vo.InnerMessageVo;
import com.xplatform.base.system.message.config.push.PushUtils;
import com.xplatform.base.system.message.config.push.android.AndroidCustomizedcast;
import com.xplatform.base.system.message.config.push.ios.IOSCustomizedcast;
import com.xplatform.base.system.message.config.service.MessageService;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

	private static final Logger logger = Logger.getLogger(MessageServiceImpl.class);

	private static final ResourceBundle bundle = ResourceBundle.getBundle("sysConfig");

	@Resource
	private MessageDao messageDao;

	@Resource
	private SysUserMybatisDao sysUserMybatisDao;

	@Resource
	private MessageMybatisDao messageMybatisDao;

	@Resource
	private BaseService baseService;

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public void setSysUserMybatisDao(SysUserMybatisDao sysUserMybatisDao) {
		this.sysUserMybatisDao = sysUserMybatisDao;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		try {
			this.messageDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("邮件配置分页列表失败");
		}
		logger.info("邮件配置分页列表成功");
	}

	@Override
	public MailConfigEntity getMailConfig(String id) {
		// TODO Auto-generated method stub
		return this.messageDao.get(MailConfigEntity.class, id);
	}

	@Override
	public void updateMailConfig(MailConfigEntity mailConfig) {
		// TODO Auto-generated method stub
		MailConfigEntity old = this.getMailConfig(mailConfig.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(mailConfig, old);
			this.messageDao.updateMailConfig(old);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void saveMailConfig(MailConfigEntity mailConfig) {
		// TODO Auto-generated method stub
		this.messageDao.save(mailConfig);
	}

	public void batchSaveMessageFrom(List<MessageFromEntity> messageFromList) {
		this.messageDao.batchSave(messageFromList);
	}

	@Override
	public void deleteMailConfig(String id) {
		// TODO Auto-generated method stub
		this.messageDao.deleteEntityById(MailConfigEntity.class, id);
	}

	@Override
	public List<MailConfigEntity> getMailConfigListByUser(String id) {
		// TODO Auto-generated method stub
		return this.messageDao.findByProperty(MailConfigEntity.class, "userId", id);
	}

	@Override
	public void testConnection(MailConfigEntity mail) throws Exception {
		// TODO Auto-generated method stub

		this.messageDao.connectSmtp(mail);

		this.messageDao.connectReciever(mail);
	}

	@Override
	public MessageFromEntity getMessageFrom(String id) {
		// TODO Auto-generated method stub
		return this.messageDao.get(MessageFromEntity.class, id);
	}

	@Override
	public BusinessEntity getBusinessByFromId(String id) {
		// TODO Auto-generated method stub
		List<BusinessEntity> list = this.messageDao.findByProperty(BusinessEntity.class, "fromId", id);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void saveMsg(MessageFromEntity messageFrom, BusinessEntity business) {
		// TODO Auto-generated method stub
		this.messageDao.save(messageFrom);// 保存发送相关信息
		business.setFromId(messageFrom.getId());
		this.messageDao.save(business);// 保存业务相关信息
	}

	@Override
	public void saveMsg(MessageFromEntity messageFrom) {
		// TODO Auto-generated method stub
		this.messageDao.save(messageFrom);// 保存发送相关信息
	}

	@Override
	public List<MessageToEntity> getMessageToByFromId(String id) {
		// TODO Auto-generated method stub
		return this.messageDao.findByProperty(MessageToEntity.class, "fromId", id);
	}

	@Override
	public void saveMsgTo(MessageToEntity msgTo) {
		// TODO Auto-generated method stub
		this.messageDao.save(msgTo);
	}

	@Override
	public List<MessageFromEntity> queryMessageFromList() {
		// TODO Auto-generated method stub
		return this.messageDao
				.findByQueryString("from MessageFromEntity where (smsStatus = '0' or emailStatus = '0' or innerStatus = '0') and status in ('2','3') and((messageType='annunciate' and pmApprove ='1' and pmStatus ='1' and gmStatus ='1') or (messageType='annunciate' and pmApprove ='0' and gmStatus ='1') or (messageType<>'annunciate') )");
	}

	@Override
	public void updateMsgFrom(MessageFromEntity msg) {
		// TODO Auto-generated method stub
		this.messageDao.merge(msg);
	}

	/**
	 * 查询待发送短信
	 * */
	@Override
	public List<MessageFromEntity> querySmsFromList() {
		// TODO Auto-generated method stub
		return this.messageDao.findByQueryString("from MessageFromEntity where smsStatus = '0' and smsStatus is not null");
	}

	@Override
	public MessageToEntity getMessageToById(String id) {
		// TODO Auto-generated method stub
		return this.messageDao.get(MessageToEntity.class, id);
	}

	@Override
	public void updateMsgTo(MessageToEntity to) {
		// TODO Auto-generated method stub
		this.messageDao.merge(to);
	}

	@Override
	public void deleteMsgTo(String id) {
		// TODO Auto-generated method stub
		MessageToEntity to = this.getMessageToById(id);
		to.setStatus("3");
		this.messageDao.merge(to);
	}

	@Override
	public void deleteMsgFrom(String id) {
		// TODO Auto-generated method stub
		MessageFromEntity msgFrom = getMessageFrom(id);
		msgFrom.setStatus("1");
		this.messageDao.merge(msgFrom);
	}

	@Override
	public void saveApprove(ApproveEntity app) {
		// TODO Auto-generated method stub
		this.messageDao.save(app);
	}

	@Override
	public ApproveEntity queryApproveEntity(Map<String, String> param) {
		// TODO Auto-generated method stub
		List<ApproveEntity> list = this.messageDao.findByPropertys(ApproveEntity.class, param);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public String getReference(MessageFromEntity messageFrom, BusinessEntity business) {
		// TODO Auto-generated method stub
		String reference = "";
		/*
		 * if ("annunciate".equals(messageFrom.getMessageType())) { reference +=
		 * business.getCompanyId() + "-"; UserVo user =
		 * ClientUtil.getSessionUserInfo(); EmployeeEntity emp =
		 * this.employeeService.get(user.getEmpId()); DeptEntity dept =
		 * this.deptService.get(emp.getPrimaryOrgId()); reference += dept !=
		 * null ? dept.getCode() + "-" : "";
		 * 
		 * SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); String date
		 * = df.format(new Date()); reference += "(" + date.substring(0, 4) +
		 * ")";
		 * 
		 * List<Map<String, Object>> list = this.messageDao .findForJdbc(
		 * "select max(right(t.reference, 3)) as idx from t_sys_message_business t where (t.id is null or t.id <> ?)"
		 * , new Object[] { business.getId() }); String index = (String)
		 * list.get(0).get("idx"); if ("null".equals(index) ||
		 * StringUtil.isEmpty(index)) { reference += "001"; } else {
		 * StringBuilder sb = new StringBuilder(); index =
		 * String.valueOf(Integer.valueOf(index) + 1); for (int i =
		 * index.length(); i < 3; i++) { sb.append("0"); } reference +=
		 * sb.toString() + index; } }
		 */
		return reference;
	}

	@Override
	public List<ApproveEntity> queryApproveEntityList(Map<String, String> param) {
		// TODO Auto-generated method stub
		return this.messageDao.findByPropertys(ApproveEntity.class, param);
	}

	@Override
	public void updateMsgFrom(MessageFromEntity messageFrom, BusinessEntity business) {
		// TODO Auto-generated method stub
		MessageFromEntity f = this.getMessageFrom(messageFrom.getId());
		BusinessEntity b = this.getBusinessByFromId(messageFrom.getId());
		messageFrom.setCreateUserId(f.getCreateUserId());
		messageFrom.setCreateUserName(f.getCreateUserName());
		messageFrom.setCreateTime(f.getCreateTime());
		this.updateMsgFrom(messageFrom);
		this.messageDao.merge(business);
	}

	/* 以下方法为 通用消息管理方法 */
	@Override
	public List<MessageGroupSendEntity> queryMsgSendList() {
		// TODO Auto-generated method stub
		return this.messageDao
				.findByQueryString("from MessageSendEntity where (smsStatus = '0' or mailStatus = '0' or innerStatus = '0') and (send_time<NOW() or send_time is null)");
	}

	@Override
	public void updateMsgSend(MessageGroupSendEntity msgSend) {
		// TODO Auto-generated method stub
		MessageGroupSendEntity ms = this.messageDao.get(MessageGroupSendEntity.class, msgSend.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(msgSend, ms);
			this.messageDao.merge(ms);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public MessageGroupSendEntity getMessageSend(String id) {
		// TODO Auto-generated method stub
		return this.messageDao.get(MessageGroupSendEntity.class, id);
	}

	@Override
	public List<MessageReceiveEntity> getMessageReceiveList(String id) {
		// TODO Auto-generated method stub
		return this.messageDao.findByProperty(MessageReceiveEntity.class, "sendId", id);
	}

	@Override
	public Date queryMsgSendMaxDate(Map<String, String> map) {
		// TODO Auto-generated method stub
		List<MessageGroupSendEntity> list = this.messageDao.findByPropertys(MessageGroupSendEntity.class, map);
		Date date = null;
		for (MessageGroupSendEntity msgSend : list) {
			if (DateUtils.getMillis(msgSend.getCreateTime()) > DateUtils.getMillis(date)) {
				date = msgSend.getCreateTime();
			}
		}
		return date;
	}

	@Override
	public MessageReceiveEntity getMsgReceiveEntity(String id) {
		// TODO Auto-generated method stub
		return this.messageDao.get(MessageReceiveEntity.class, id);
	}

	@Override
	public void updateMsgReceive(MessageReceiveEntity msgReceive) {
		// TODO Auto-generated method stub
		MessageReceiveEntity mr = this.messageDao.get(MessageReceiveEntity.class, msgReceive.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(msgReceive, mr);
			this.messageDao.merge(mr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<InnerMessageVo> queryInnerMessageByUser(Map<String, Object> param) {
		return messageMybatisDao.queryInnerMessageByUser(param);
	}

	@Override
	public Page<InnerMessageVo> queryInnerMessageByPage(Page<InnerMessageVo> page) {
		page.setResult(messageMybatisDao.queryInnerMessageByPage(page));
		return page;
	}

	@Override
	public void updateInnerMessageToRead(String id) throws BusinessException {
		String hql = "UPDATE t_sys_message_receive SET is_read=1 WHERE id=?";
		try {
			messageDao.executeSql(hql, new Object[] { id });
		} catch (Exception e) {
			throw new BusinessException("站内信设置已读操作失败");
		}
	}

	@Override
	public void updateSendStatus(String funcType, String sendChannel, String sendId, Integer status, String log) throws BusinessException {
		String tableName = null;
		String statusName = null;
		String logName = null;
		if (BusinessConst.sendChannel_CODE_email.equals(sendChannel)) {
			statusName = "mailStatus";
			logName = "mailLog";
		} else if (BusinessConst.sendChannel_CODE_sms.equals(sendChannel)) {
			statusName = "smsStatus";
			logName = "smsLog";
		} else if (BusinessConst.sendChannel_CODE_msg.equals(sendChannel)) {
			statusName = "msgStatus";
		} else if (BusinessConst.sendChannel_CODE_IM.equals(sendChannel)) {
			statusName = "IMStatus";
		} else if (BusinessConst.sendChannel_CODE_push_android.equals(sendChannel)) {
			statusName = "pushAndroidStatus";
			logName = "androidPushLog";
		} else if (BusinessConst.sendChannel_CODE_push_ios.equals(sendChannel)) {
			statusName = "pushIOSStatus";
			logName = "IOSPushLog";
		}

		if (BusinessConst.FUNCTYPE_CODE_private.equals(funcType)) {
			tableName = "t_sys_message_send";
		} else {
			tableName = "t_sys_message_send_group";
		}
		String sql = "UPDATE " + tableName + " SET " + statusName + "=" + status + (StringUtil.isEmpty(log) ? "" : "," + logName + "='" + log + "'")
				+ "  WHERE id='" + sendId + "'";

		messageDao.executeSql(sql);
	}

	@Override
	public void saveSendUnion(SendUnionEntity sendUnion) {
		this.messageDao.save(sendUnion);
	}

	@Override
	public String saveMsgSend(MessageSend send) {
		return this.messageDao.save(send).toString();
	}

	@Override
	public String saveAndProcessMsgSend(MessageSend send) throws BusinessException {
		if (send instanceof MessageSendEntity) {
			send.setFuncType(BusinessConst.FUNCTYPE_CODE_private);
		}

		// 设置默认状态
		String sendChannel = send.getSendChannel();
		if (StringUtil.indexOf(sendChannel, BusinessConst.sendChannel_CODE_msg) != -1 && send.getMsgStatus() == null) {
			send.setMsgStatus(1);
		}
		if (StringUtil.indexOf(sendChannel, BusinessConst.sendChannel_CODE_email) != -1 && send.getMailStatus() == null) {
			send.setMailStatus(1);
			if (StringUtils.isEmpty(send.getMailConfigId())) {
				MailConfigEntity mailConfigEntity = this.messageDao.findUniqueByProperty(MailConfigEntity.class, "mailAddress", ConfigConst.mail_from);
				if (mailConfigEntity != null) {
					send.setMailConfigId(mailConfigEntity.getId());
				} else {
					logger.error("没有找到系统邮箱信息");
				}
			}
		}
		if (StringUtil.indexOf(sendChannel, BusinessConst.sendChannel_CODE_sms) != -1 && send.getSmsStatus() == null) {
			send.setSmsStatus(1);
		}
		if (StringUtil.indexOf(sendChannel, BusinessConst.sendChannel_CODE_IM) != -1 && send.getIMStatus() == null) {
			send.setIMStatus(1);
		}
		
		// TODO 还有推送的默认设置
		if (StringUtil.indexOf(sendChannel, BusinessConst.sendChannel_CODE_push) != -1) {
			if (send.getPushAndroidStatus() == null) {
				send.setPushAndroidStatus(1);
			}
			if (send.getPushIOSStatus() == null) {
				send.setPushIOSStatus(1);
			}
			if (send.getPushType() == null) {
				send.setPushType(BusinessConst.PUSHTYPE_CODE_umeng);
			}
		}
		if (send.getSendTime() == null) {
			send.setSendTime(new Date());
		}
		
		return this.saveMsgSend(send);
	}
	@Override
	public List<MessageReceiveEntity> queryMessageReceives(String userId, String status) {
		String hql = "FROM MessageReceiveEntity WHERE receiveId=? AND status=?";
		return messageDao.findHql(hql, userId, status);
	}
	
	@Override
	public List<MessageReceiveEntity> queryMessageReceivesBySource(String sourceType, String sourceId) {
		String hql = "FROM MessageReceiveEntity WHERE sourceType=? AND sourceId=?";
		return messageDao.findHql(hql, sourceType, sourceId);
	}

	@Override
	public Integer deleteMessageReceive(String id) throws BusinessException {
		return this.messageDao.executeHql("UPDATE MessageReceiveEntity SET status=2 WHERE sendId=?", id);
	}

	@Override
	public Integer doReadMessageReceive(String sendId, String userId) throws BusinessException {
		return this.messageDao.executeHql("UPDATE MessageReceiveEntity SET status=1 WHERE sendId=? AND receiveId=?", sendId, userId);
	}
	
	@Override
	public Integer doReadMessageReceiveBySource(String sourceId, String sourceBusinessType) throws BusinessException {
		return this.messageDao.executeHql("UPDATE MessageReceiveEntity SET status=1 WHERE sourceType='circulate' AND sourceId=? AND sourceBusinessType=?", sourceId, sourceBusinessType);
	}

	@Override
	public MessageReceiveEntity saveMessageReceive(String sendId, String userId, String funcType) throws BusinessException {
		String tableName = "";
		if (BusinessConst.FUNCTYPE_CODE_private.equals(funcType)) {
			tableName = "t_sys_message_send";
		} else {
			tableName = "t_sys_message_send_group";
		}
		String sql = "SELECT title,funcType,id,title,sourceType,extra FROM " + tableName + " WHERE id=?";
		Map<String, Object> map = this.messageDao.findOneForJdbc(sql, sendId);
		MessageReceiveEntity receive = new MessageReceiveEntity();
		receive.setReceiveId(userId);
		receive.setReadTime(new Date());
		receive.setStatus(1);
		receive.setFuncType(funcType);
		receive.setSendId(sendId);
		receive.setTitle(map.get("title").toString());
		receive.setSourceType(map.get("sourceType").toString());
		receive.setExtra(map.get("extra").toString());

		this.messageDao.save(receive);
		return receive;
	}

	@Override
	public String saveMessageReceive(MessageReceiveEntity receive) throws BusinessException {
		return this.messageDao.save(receive).toString();
	}
	
//	@Override
//	public void saveMemberUnicast(MessageSendEntity send) throws BusinessException {
//		// 构造发送表
//		send.setActivity("com.huike.bang.activity.MineActivity");
//		Map<String, Object> extraMap = StringUtil.isEmpty(send.getExtra()) ? new HashMap<String, Object>() : JSONHelper.parseJSON2Map(send.getExtra());
//		extraMap.put("after_open", "go_activity");
//		extraMap.put("iViewName", "AnnouncementMsg");
//		extraMap.put("source", "notification");
//		send.setOpenType(BusinessConst.OPENTYPE_CODE_text);
//		send.setExtra(JSONHelper.map2json(extraMap));
//		send.setFuncType(BusinessConst.FUNCTYPE_CODE_pmessage);
//		send.setCastType(BusinessConst.CASTTYPE_CODE_unicast);
//		send.setPushType(BusinessConst.PUSHTYPE_CODE_umeng);
//	}
}
