package com.xplatform.base.system.message.config.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.system.attachment.mybatis.vo.AttachVo;
import com.xplatform.base.system.attachment.service.AttachService;
import com.xplatform.base.system.message.config.entity.MailConfigEntity;
import com.xplatform.base.system.message.config.entity.MessageGroupSendEntity;
import com.xplatform.base.system.message.config.entity.MessageSend;
import com.xplatform.base.system.message.config.entity.MessageSendEntity;
import com.xplatform.base.system.message.config.service.MessageService;
import com.xplatform.base.system.message.jms.model.IMModel;
import com.xplatform.base.system.message.jms.model.InnerModel;
import com.xplatform.base.system.message.jms.model.MailModel;
import com.xplatform.base.system.message.jms.model.SmsModel;
import com.xplatform.base.system.message.jms.producer.MessageProducer;

public class MsgUtil {
	private static final ResourceBundle bundle = ResourceBundle.getBundle("sysConfig");
	private static SysUserService sysUserService = ApplicationContextUtil.getBean("sysUserService");
	private static AttachService attachService = ApplicationContextUtil.getBean("attachService");
	private static MessageService messageService = ApplicationContextUtil.getBean("messageService");

	// public static String getSmsContent(MessageFromEntity messageFrom,
	// BusinessEntity business) {
	// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// String result = "";
	// if ("meeting".equals(messageFrom.getMessageType())) {
	// result = "会议通知：请您于" + df.format(business.getBusinesstime()) + "到" +
	// business.getBusinessPlace() + "参加主题为“" + messageFrom.getTitle() +
	// "”的会议。";
	// } else if ("annunciate".equals(messageFrom.getMessageType())) {
	// result = "内部通告通知，文号：" + business.getReference() + "，主题：" +
	// messageFrom.getTitle();
	// } else if ("train".equals(messageFrom.getMessageType())) {
	// result = "培训通知：请您于" + df.format(business.getBusinesstime()) + "到" +
	// business.getBusinessPlace() + "参加主题为“" + messageFrom.getTitle() +
	// "”的培训。";
	// } else {
	// result = messageFrom.getContent();
	// }
	//
	// return "【广东中盛创投电子商务有限公司】" + result;
	// }

	// 获取会议邮件内容
	// public static String getMailContent(MessageFromEntity messageFrom,
	// BusinessEntity business) {
	// BufferedReader br = null;
	// StringBuilder sb = null;
	// String str = "";
	// String path = "";
	// /*
	// * System.out.println(SystemPath.getSysPath());
	// * System.out.println(System.getProperty("java.io.tmpdir"));
	// * System.out.println(System.getProperty("user.dir"));
	// * System.out.println(SystemPath.getSeparator());
	// * System.out.println(SystemPath.getClassPath()); String path =
	// * ContextHolderUtils.getSession().getServletContext().getRealPath("/");
	// */
	//
	// if ("meeting".equals(messageFrom.getMessageType()) ||
	// "annunciate".equals(messageFrom.getMessageType()) ||
	// "train".equals(messageFrom.getMessageType())) {
	// // String path = SystemPath.getSysPath().replaceAll("%20", " ");
	// try {
	// path =
	// ApplicationContextUtil.getServletContext().getRealPath("/").replaceAll("%20",
	// " ");
	// } catch (Exception e2) {
	// // TODO Auto-generated catch block
	// e2.printStackTrace();
	// }
	//
	// String fileName = messageFrom.getMessageType().trim() + ".html";
	// path = path.replaceAll("/", File.separator + File.separator) +
	// File.separator + "userfiles" + File.separator + "files" + File.separator
	// + fileName;
	//
	// File file = new File(path);
	// try {
	// InputStreamReader isr = new InputStreamReader(new FileInputStream(file),
	// "UTF-8");
	// br = new BufferedReader(isr);
	// String tempString = null;
	// sb = new StringBuilder();
	// while ((tempString = br.readLine()) != null) {
	// sb.append(tempString);
	// }
	// br.close();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } finally {
	// if (br != null) {
	// try {
	// br.close();
	// } catch (IOException e1) {
	// }
	// }
	// }
	// Map<String, String> map = getMessageData(messageFrom, business);
	// if (sb == null || StringUtils.isBlank(sb.toString())) {
	// str = "读取查看界面文件出错";
	// } else {
	// str = replaceValue(sb.toString(), map);
	// }
	// } else {
	// str = messageFrom.getContent();
	// }
	// return str;
	// }

	// public static String getFileContent(File file) {
	// BufferedReader br = null;
	// StringBuilder sb = new StringBuilder();
	// String tempString = null;
	// String code = "gb2312";
	// if (file.exists()) {
	// try {
	// // String result = new
	// // String(tempStr.getBytes("GB2312"),"UTF-8");
	// br = new BufferedReader(new InputStreamReader(new FileInputStream(file),
	// code));
	// while (!(tempString = br.readLine()).contains("charset")) {
	//
	// }
	// tempString = tempString.replaceAll("\\s*", "").split("charset=")[1];
	// code = tempString.substring(0, tempString.indexOf("\">")).toLowerCase();
	// br.close();
	// if (code.contains("utf-8")) {
	// code = "utf-8";
	// } else if (code.contains("gbk")) {
	// code = "gbk";
	// } else {
	// code = "gb2312";
	// }
	// /*
	// * if(!"utf-8".equals(code) && !"gbk".equals(code)){ code =
	// * "gb2312"; }
	// */
	// br = new BufferedReader(new InputStreamReader(new FileInputStream(file),
	// code));
	//
	// while ((tempString = br.readLine()) != null) {
	// sb.append(tempString).append("\n");
	// }
	// br.close();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } finally {
	// if (br != null) {
	// try {
	// br.close();
	// } catch (IOException e1) {
	// }
	// }
	// }
	// } else {
	// sb.append("file not found");
	// }
	//
	// return sb.toString();
	// }

	// public static Map<String, String> getMessageData(MessageFromEntity
	// messageFrom, BusinessEntity business) {
	// Map<String, String> map = new HashMap<String, String>();
	// SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
	// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// map.put("from", getFrom(messageFrom));
	// map.put("name", messageFrom.getName());
	// map.put("now_time", s.format(new Date()).trim());
	// if (business.getBusinesstime() != null) {
	// String btime = df.format(business.getBusinesstime());
	// map.put("mdate", btime.substring(0, 10).trim());
	// map.put("mtime", btime.substring(11, btime.length()).trim());
	// }
	// map.put("place", business.getBusinessPlace());
	// map.put("person", business.getBusinessPerson());
	// map.put("title", messageFrom.getTitle());
	// map.put("content", messageFrom.getContent());
	// map.put("reference", business.getReference());
	// map.put("obj", business.getBusinessObj());
	// map.put("company", business.getCompanyName() == null ? "" :
	// business.getCompanyName().replaceAll(",", "<br/>"));
	// // map.put("company", business.getCompanyName() ==null ? "" :
	// // business.getCompanyName());
	// return map;
	// }

	// public static String replaceValue(String sb, Map<String, String> map) {
	// Pattern p = Pattern.compile("\\[@\\S[^@]*@\\]", 2);
	// Matcher m = p.matcher(sb);
	// String key;
	// String paramKey;
	//
	// while (m.find()) {
	// key = m.group().toLowerCase();
	// paramKey = key.substring(2, key.length() - 2);// 去掉[@ @]
	// sb = sb.replace("[@" + paramKey + "@]", map.get(paramKey) != null ?
	// map.get(paramKey) : "");
	// }
	// return sb;
	// }

	// 信息发送时获取发致人信息
	// public static String getFrom(MessageFromEntity messageFrom) {
	// StringBuilder sb = new StringBuilder();
	// if ("1".equals(messageFrom.getAllUsers())) {
	// sb.append("全体员工");
	// } else {
	// if (StringUtils.isNotEmpty(messageFrom.getEmpNames())) {
	// sb.append(messageFrom.getEmpNames()).append(",");
	// }
	// if (StringUtils.isNotEmpty(messageFrom.getJobNames())) {
	// sb.append(messageFrom.getJobNames()).append(",");
	// }
	// if (StringUtils.isNotEmpty(messageFrom.getOrgNames())) {
	// sb.append(messageFrom.getOrgNames()).append(",");
	// }
	// }
	// if (sb.toString().length() == 0) {
	// return "";
	// } else {
	// return "1".equals(messageFrom.getAllUsers()) ? sb.toString() :
	// sb.toString().substring(0, sb.toString().length() - 1);
	// }
	// }

	// 信息发送时获取发致人信息
	// public static String getFrom(String allUsers, String empNames, String
	// jobNames, String orgNames) {
	// StringBuilder sb = new StringBuilder();
	// if ("1".equals(allUsers)) {
	// sb.append("全体员工");
	// } else {
	// if (StringUtils.isNotEmpty(empNames)) {
	// sb.append(empNames).append(",");
	// }
	// if (StringUtils.isNotEmpty(jobNames)) {
	// sb.append(jobNames).append(",");
	// }
	// if (StringUtils.isNotEmpty(orgNames)) {
	// sb.append(orgNames).append(",");
	// }
	// }
	// if (sb.toString().length() == 0) {
	// return "";
	// } else {
	// return "1".equals(allUsers) ? sb.toString() : sb.toString().substring(0,
	// sb.toString().length() - 1);
	// }
	// }

	public static String toStr(String str) {
		if (StringUtils.isEmpty(str)) {
			str = "";
		}
		return str;
	}

	/*
	 * 根据公司ID获取公司name
	 */
	// public static String getCompanyName(String companyId) {
	// DictTypeService dictTypeService =
	// ApplicationContextUtil.getBean("dictTypeService");
	// List<DictValueEntity> dictValueList =
	// dictTypeService.findDictValueEntityListByCode("companyType");
	// String companyName = "";
	// if ("A".equals(companyId)) {
	// for (DictValueEntity dictValue : dictValueList) {
	// if (!"A".equals(dictValue.getValue())) {
	// companyName += dictValue.getName() + ",";
	// }
	// }
	// companyName = companyName.substring(0, companyName.length() - 1);
	// } else {
	// for (DictValueEntity dictValue : dictValueList) {
	// if (StringUtil.isNotEmpty(companyId) &&
	// companyId.equals(dictValue.getValue())) {
	// companyName += dictValue.getName();
	// }
	// }
	// }
	// return companyName;
	// }

	/**
	 * 根据部门id获取部门名
	 * **/
	// public static String getOrgNameById(String ids) {
	// OrgnaizationService deptService =
	// ApplicationContextUtil.getBean("orgnaizationService");
	// // 获取部门名字
	// String str = "";
	// if (StringUtils.isNotEmpty(ids)) {
	// String[] orgs = ids.split(",");
	// for (String org : orgs) {
	// OrgnaizationEntity d = deptService.get(org);
	// str += d.getName() + ",";
	// }
	// str.substring(0, str.length() - 1);
	// }
	// return str;
	// }

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月31日 下午6:03:29
	 * @Decription 将MessageSendEntity 转化为 邮件发送模型MailModel
	 * @return
	 */
	public static MailModel covertToMailModel(String funcType, String sendId, String title, String mailContent, MailConfigEntity mailConfig, String[] list,
			String[] listCC, String[] listBCC, List<AttachVo> dataVoList) {
		MailModel mailModel = new MailModel();
		mailModel.setFuncType(funcType);
		mailModel.setSendId(sendId);
		mailModel.setTitle(title);
		mailModel.setMailContent(mailContent);
		mailModel.setMailConfig(mailConfig);
		mailModel.setDataVoList(dataVoList);
		mailModel.setMailMain(list);
		mailModel.setMailCC(listCC);
		mailModel.setMailBCC(listBCC);
		return mailModel;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月31日 下午6:12:53
	 * @Decription 将MessageSendEntity 转化为短信发送模型SmsModel
	 *
	 * @return
	 */
	public static SmsModel covertToSmsModel(String funcType, String sendId, String smsContent, String[] mobiles) {
		SmsModel smsModel = new SmsModel();
		smsModel.setFuncType(funcType);
		smsModel.setSendId(sendId);
		smsModel.setSmsContent(smsContent);
		smsModel.setMobile(mobiles);
		return smsModel;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月31日 下午6:12:53
	 * @Decription 将MessageSendEntity 转化为短信发送模型IMModel
	 *
	 * @param msgSend
	 *            信息send表
	 * @param mobiles
	 *            信息send表
	 * @return
	 */
	public static IMModel covertToIMModel(String funcType, String sendId, String receive, String content, String fromId, String IMMsg, String extra) {
		IMModel IMModel = new IMModel();
		IMModel.setFuncType(funcType);
		IMModel.setSendId(sendId);
		IMModel.setReceive(receive);
		IMModel.setContent(content);
		IMModel.setFromId(fromId);
		IMModel.setIMMsg(IMMsg);
		IMModel.setExtra(extra);
		return IMModel;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月31日 下午6:12:53
	 * @Decription 将MessageSendEntity 转化为站内信发送模型InnerModel
	 *
	 * @param msgSend
	 *            信息send表
	 * @return
	 */
	public static InnerModel covertToInnerModel(String sendId, String funcType, String receive, String cc, String bcc, String title, String sourceType,
			String sourceId, String extra,String sourceBusinessType) {
		InnerModel innerModel = new InnerModel();
		innerModel.setSendId(sendId);
		innerModel.setFuncType(funcType);
		innerModel.setReceive(receive);
		innerModel.setCc(cc);
		innerModel.setBcc(bcc);
		innerModel.setTitle(title);
		innerModel.setSourceType(sourceType);
		innerModel.setSourceId(sourceId);
		innerModel.setSourceBusinessType(sourceBusinessType);
		innerModel.setExtra(extra);
		return innerModel;
	}

	/**
	 * 默认IM发送透传消息的调用方法
	 * 
	 * @param send
	 * @throws BusinessException
	 */
	public static void SendMulMeassage(MessageSend send) throws BusinessException {
		ObjectNode IMMsg = JsonNodeFactory.instance.objectNode();
		IMMsg.put("type", "cmd");
		IMMsg.put("action", "hxcmd");
		send.setIMMsg(IMMsg.toString());
		String extra = send.getExtra();
		if (StringUtil.isNotEmpty(extra)) {
			ObjectNode extraObj = null;
			extraObj = JSONHelper.toObjectNode(extra);
			if (StringUtil.isNotEmpty(send.getSourceType()))
				extraObj.put("sourceType", send.getSourceType());
			if (StringUtil.isNotEmpty(send.getSourceId()))
				extraObj.put("sourceId", send.getSourceId());
			send.setExtra(extraObj.toString());
		}
		SendMulMeassage(send, null);
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月31日 下午11:01:16
	 * @Decription
	 *
	 * @param msgSend
	 * @param params
	 *            发送信息额外的参数,可用参数如下:
	 *            1.onlySendType/Boolean类型/是否无视状态位,只看sendType的值
	 *            ,有就一定发送(适用于非轮询,自己先插入了状态为1的send表,然后希望调用本方法做真正的发消息动作)
	 * @throws BusinessException
	 * 
	 * 
	 */
	public static void SendMulMeassage(MessageSend send, Map<String, Object> params) throws BusinessException {
		String sendId = "";
		if (StringUtil.isEmpty(send.getId())) {
			sendId = messageService.saveAndProcessMsgSend(send);
		}

		List<UserEntity> receive = new ArrayList<UserEntity>();
		List<UserEntity> ccList = new ArrayList<UserEntity>();
		List<UserEntity> bccList = new ArrayList<UserEntity>();

		if (send instanceof MessageGroupSendEntity) {
			send = (MessageGroupSendEntity) send;
			receive = sysUserService.getDistinctUsersByMulType(send.getReceive());
			ccList = sysUserService.getDistinctUsersByMulType(send.getMailCC());
			bccList = sysUserService.getDistinctUsersByMulType(send.getMailBCC());
		} else if (send instanceof MessageSendEntity) {
			send = (MessageSendEntity) send;
			receive = sysUserService.getUserByIds(send.getReceive());
			ccList = sysUserService.getUserByIds(send.getMailCC());
			bccList = sysUserService.getUserByIds(send.getMailBCC());
		}

		MessageProducer producer = new MessageProducer();
		Boolean onlySendTypeObj = false;
		if (BeanUtils.isNotEmpty(params)) {
			onlySendTypeObj = params.get("onlySendType") == null ? false : Boolean.parseBoolean(params.get("onlySendType").toString());
		}

		// 邮件发送
		if ((send.getMailStatus() != null && send.getMailStatus() == 1) || (onlySendTypeObj && send.getSendChannel().contains("email"))) {
			MailModel mailModel = sendMail(send, receive, ccList, bccList);
			if (mailModel != null) {
				producer.send(mailModel);
			}
		}

		List<UserEntity> allReceive = new ArrayList<UserEntity>();
		allReceive.addAll(receive);
		allReceive.addAll(ccList);
		allReceive.addAll(bccList);
		// 短信发送
		if ((send.getSmsStatus() != null && send.getSmsStatus() == 1) || (onlySendTypeObj && send.getSendChannel().contains("sms"))) {
			if (ConfigConst.smsSwitch) {

				SmsModel smsModel = sendSms(send, allReceive);
				if (smsModel != null) {
					// producer.send(smsModel);
				}
			}
		}

		// 推送发送
		// if ((send.getSmsStatus() == 0) || (onlySendTypeObj &&
		// send.getSendChannel().contains("push"))) {
		// if (ConfigConst.pushSwitch) {
		// SmsModel smsModel = sendSms(send, receive);
		// p.send(smsModel);
		// }
		// }

		// 站内信发送
		if ((send.getMsgStatus() != null && send.getMsgStatus() == 1) || (onlySendTypeObj && send.getSendChannel().contains("msg"))) {
			InnerModel inner = sendInner(send);
			producer.send(inner);
		}

		// IM发送
		if ((send.getIMStatus() != null && send.getIMStatus() == 1) || (onlySendTypeObj && send.getSendChannel().contains("IM"))) {
			IMModel IMModel = sendIM(send, allReceive);
			producer.send(IMModel);
		}
	}

	/**
	 * 发送站内消息方法
	 * 
	 * @param send
	 * @param receive
	 * @param ccList
	 * @param bccList
	 * @return
	 * @throws BusinessException
	 */
	public static InnerModel sendInner(MessageSend send) throws BusinessException {
		// 设置send表为发送中
		messageService.updateSendStatus(send.getFuncType(), BusinessConst.sendChannel_CODE_msg, send.getId(), 2, null);
		InnerModel innerModel = covertToInnerModel(send.getId(), send.getFuncType(), send.getReceive(), send.getMailCC(), send.getMailBCC(), send.getTitle(),
				send.getSourceType(),send.getSourceId(), send.getExtra(),send.getSourceBusinessType());
		return innerModel;
	}

	/**
	 * 发送IM即时通信消息方法
	 * 
	 * @param send
	 * @param receive
	 * @param ccList
	 * @param bccList
	 * @return
	 * @throws BusinessException
	 */
	public static IMModel sendIM(MessageSend send, List<UserEntity> receive) throws BusinessException {
		// IM消息的extra数据要加上sendId和funcType
		String extra = send.getExtra();
		if (StringUtil.isNotEmpty(extra)) {
			ObjectNode extraObj = null;
			extraObj = JSONHelper.toObjectNode(extra);
			extraObj.put("sendId", send.getId());
			extraObj.put("funcType", send.getFuncType());
			send.setExtra(extraObj.toString());
		}
		IMModel IMModel = covertToIMModel(send.getFuncType(), send.getId(), listToIds(receive), send.getContent(), send.getFromId(), send.getIMMsg(),
				send.getExtra());
		if (IMModel.getReceive() != null) {
			// 设置send表为发送中
			messageService.updateSendStatus(send.getFuncType(), BusinessConst.sendChannel_CODE_IM, send.getId(), 2, null);
			return IMModel;
		} else {
			return null;
		}

	}

	/**
	 * 发送短信方法
	 * 
	 * @param send
	 * @param receive
	 * @param ccList
	 * @param bccList
	 * @return
	 * @throws BusinessException
	 */
	public static SmsModel sendSms(MessageSend send, List<UserEntity> receive) throws BusinessException {
		SmsModel smsModel = covertToSmsModel(send.getFuncType(), send.getId(), StringUtil.isEmpty(send.getSmsContent(), send.getContent()),
				listToMobileArray(receive));
		if (smsModel.getMobile() != null) {
			// 设置send表为发送中
			messageService.updateSendStatus(send.getFuncType(), BusinessConst.sendChannel_CODE_sms, send.getId(), 2, null);
			return smsModel;
		} else {
			return null;
		}

	}

	/**
	 * 发送邮件方法
	 * 
	 * @param send
	 * @param receive
	 * @param ccList
	 * @param bccList
	 * @return
	 * @throws BusinessException
	 */
	public static MailModel sendMail(MessageSend send, List<UserEntity> receive, List<UserEntity> ccList, List<UserEntity> bccList) throws BusinessException {
		MailConfigEntity mailConfig = (send.getMailConfigId() == null ? null : messageService.getMailConfig(send.getMailConfigId()));
		if (mailConfig == null) {
			messageService.updateSendStatus(send.getFuncType(), BusinessConst.sendChannel_CODE_email, send.getId(), 4, "找不到对应邮件配置记录");
		} else {
			List<AttachVo> dataVoList = null;
			if (StringUtil.isNotEmpty(send.getSourceId())) {
				// 获取邮件附件
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("businessKey", send.getSourceId());
				dataVoList = attachService.queryUploadAttach(param);
			}

			MailModel mailModel = covertToMailModel(send.getFuncType(), send.getId(), send.getTitle(),
					StringUtil.isEmpty(send.getMailContent(), send.getContent()), mailConfig, listToMailArray(receive), listToMailArray(ccList),
					listToMailArray(bccList), dataVoList);
			if (mailModel.getMailMain() != null) {
				// 设置send表为发送中
				messageService.updateSendStatus(send.getFuncType(), BusinessConst.sendChannel_CODE_email, send.getId(), 2, null);
				return mailModel;
			}
		}
		return null;
	}

	/**
	 * 将用户集合转换为去重邮箱集合
	 * 
	 * @param receive
	 * @return
	 */
	public static String[] listToMailArray(List<UserEntity> receive) {
		// 获取邮箱集合
		if (receive != null) {
			List<String> receiveStr = new ArrayList<String>();
			for (int i = 0; i < receive.size(); i++) {
				UserEntity user = receive.get(i);
				if (StringUtil.isNotEmpty(user.getEmail()) && !receiveStr.contains(user.getEmail())) {
					// 接收者有邮箱的情况下
					receiveStr.add(user.getEmail());
				}
			}
			return receiveStr.toArray().length > 0 ? StringUtil.toArray(receiveStr.toArray()) : null;
		}
		return null;
	}

	/**
	 * 将用户集合转换为去重手机集合
	 * 
	 * @param receive
	 * @return
	 */
	public static String[] listToMobileArray(List<UserEntity> receive) {
		// 获取邮箱集合
		List<String> receiveStr = new ArrayList<String>();
		for (int i = 0; i < receive.size(); i++) {
			UserEntity user = receive.get(i);
			if (StringUtil.isNotEmpty(user.getPhone()) && !receiveStr.contains(user.getPhone())) {
				// 接收者有邮箱的情况下
				receiveStr.add(user.getPhone());
			}
		}
		return receiveStr.toArray().length > 0 ? StringUtil.toArray(receiveStr.toArray()) : null;
	}

	/**
	 * 将用户集合转换为id集合
	 * 
	 * @param receive
	 * @return
	 */
	public static String listToIds(List<UserEntity> receive) {
		// 获取邮箱集合
		String receiveStr = "";
		for (int i = 0; i < receive.size(); i++) {
			UserEntity user = receive.get(i);
			if (StringUtil.isNotEmpty(user.getId()) && receiveStr.indexOf(user.getId()) == -1) {
				// 接收者有邮箱的情况下
				receiveStr += user.getId() + ",";
			}
		}
		receiveStr = StringUtil.removeDot(receiveStr);
		return receiveStr.length() > 0 ? receiveStr : null;
	}
}
