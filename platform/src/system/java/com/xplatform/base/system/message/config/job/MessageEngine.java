package com.xplatform.base.system.message.config.job;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSender;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.orgnaization.member.tools.EmailUtil;
import com.xplatform.base.system.attachment.mybatis.vo.AttachVo;
import com.xplatform.base.system.message.config.entity.MessageFromEntity;
import com.xplatform.base.system.message.config.entity.MessageReceiveEntity;
import com.xplatform.base.system.message.config.entity.MessageGroupSendEntity;
import com.xplatform.base.system.message.config.entity.MessageToEntity;
import com.xplatform.base.system.message.config.service.MessageService;
import com.xplatform.base.system.message.jms.model.InnerModel;
import com.xplatform.base.system.message.jms.model.MailModel;
import com.xplatform.base.system.message.jms.model.SmsModel;

/**
 * 
 * description :消息管理引擎
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月20日 下午4:42:53
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年6月20日 下午4:42:53
 *
 */
public class MessageEngine {
//	private final Log logger = LogFactory.getLog(MessageEngine.class);
//	private JavaMailSender mailSender;
//	private String fromUser = "";
//	private static final ResourceBundle bundle = ResourceBundle.getBundle("sysConfig");
//	private MessageService messageService =ApplicationContextUtil.getBean("messageService");
//
//	public void setFromUser(String fromUser) {
//		this.fromUser = fromUser;
//	}
//
//	public void setMailSender(JavaMailSender mailSender) {
//		this.mailSender = mailSender;
//	}
//
//	/**
//	 * 发送短信息
//	 * @author xiehs
//	 * @createtime 2014年6月20日 下午4:43:26
//	 * @Decription
//	 *
//	 * @param mobiles
//	 * @param content
//	 */
//	public void sendSms(SmsModel smsModel) {
//
//		if ("common".equals(smsModel.getType())) {
//			MessageGroupSendEntity msgSend = this.messageService.getMessageSend(smsModel.getFromId());
//			List<String> mobiles = smsModel.getMobile();
//			String content = bundle.getString("companyName") + smsModel.getContent();
//			String[] strs = new String[mobiles.size()];
//			for (int i = 0; i < mobiles.size(); i++) {
//				strs[i] = mobiles.get(i);
//			}
//			try {
//				int i = SingletonClient.getClient().sendSMS(strs, content, 3);
//				if(msgSend!=null){
//					msgSend.setSmsStatus("2");
//					msgSend.setSmsLog("SendSMS=====" + i);
//				}
//				this.messageService.updateMsgSend(msgSend);
//			} catch (Exception e) {
//				msgSend.setSmsStatus("3");
//				StringWriter sw = new StringWriter();
//				e.printStackTrace(new PrintWriter(sw, true));
//				String str = sw.toString();
//				msgSend.setSmsLog(str.substring(0, 1000));
//				this.messageService.updateMsgSend(msgSend);
//				e.printStackTrace();
//			}
//		} else {
//			MessageFromEntity smsFrom = this.messageService.getMessageFrom(smsModel.getFromId());
//			List<String> mobiles = smsModel.getMobile();
//			String content = smsModel.getContent();
//			String[] strs = new String[mobiles.size()];
//			for (int i = 0; i < mobiles.size(); i++) {
//				strs[i] = mobiles.get(i);
//			}
//			try {
//				int i = SingletonClient.getClient().sendSMS(strs, content, 3);
//				smsFrom.setSmsStatus("2");
//				smsFrom.setSmsErrorLog("SendSMS=====" + i);
//				this.messageService.updateMsgFrom(smsFrom);
//			} catch (Exception e) {
//				smsFrom.setSmsStatus("3");
//				StringWriter sw = new StringWriter();
//				e.printStackTrace(new PrintWriter(sw, true));
//				String str = sw.toString();
//				smsFrom.setSmsErrorLog(str.substring(0, 1000));
//				this.messageService.updateMsgFrom(smsFrom);
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * 发送站内信
//	 * @author xiehs
//	 * @createtime 2014年6月20日 下午4:43:35
//	 * @Decription
//	 *
//	 * @param messageSend
//	 */
//	public void sendInnerMessage(InnerModel innerModel) {
//		if("common".equals(innerModel.getType())){
//			/*List<UserVo> list = innerModel.getMsgTo();
//			for(UserVo emp : list){
//				MessageReceiveEntity msgReceive = new MessageReceiveEntity();
//				msgReceive.setReceiveId(emp.getId());
//				msgReceive.setReceiveName(emp.getName());
//				msgReceive.setIsRead("0");
//				msgReceive.setStatus("1");
//				msgReceive.setSendId(innerModel.getFromId());
//				this.messageService.saveMsgReveive(msgReceive);
//			}
//			MessageSendEntity inner = this.messageService.getMessageSend(innerModel.getFromId());
//			if(inner!=null){
//				inner.setInnerStatus("1");
//				this.messageService.updateMsgSend(inner);
//			}*/
//		}else{
//			/*List<UserVo> list = innerModel.getMsgTo();
//			for(UserVo emp : list){
//				MessageToEntity msgTo = new MessageToEntity();
//				msgTo.setReceiveId(emp.getId());
//				msgTo.setReceiveName(emp.getName());
//				msgTo.setIsRead("0");
//				msgTo.setNeedReply(innerModel.getNeedReply());
//				msgTo.setStatus("1");
//				msgTo.setFromId(innerModel.getFromId());
//				this.messageService.saveMsgTo(msgTo);
//			}
//			MessageFromEntity inner = this.messageService.getMessageFrom(innerModel.getFromId());
//			if(inner!=null){
//				inner.setInnerStatus("1");
//				this.messageService.updateMsgFrom(inner);
//			}*/
//		}
//	}
}
