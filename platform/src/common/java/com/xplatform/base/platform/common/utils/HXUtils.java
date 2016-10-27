package com.xplatform.base.platform.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.system.im.utils.IMHTTPClientUtils;
import com.xplatform.base.system.im.vo.ClientSecretCredential;
import com.xplatform.base.system.im.vo.Credential;

public class HXUtils {
	private static final Logger logger = Logger.getLogger(HXUtils.class);

	public static final String target_users = "users";

	public static final String target_chatgroups = "chatgroups";

	public static final String msgtype_txt = "txt";// 文本消息
	public static final String msgtype_img = "img";// 图片消息
	public static final String msgtype_audio = "audio";// 音频消息
	public static final String msgtype_video = "video";// 视频消息
	public static final String msgtype_cmd = "cmd";// 透传消息(自定义消息)

	private static final JsonNodeFactory factory = new JsonNodeFactory(false);

	public static final String preURL = ConfigConst.API_HTTP_SCHEMA + "://" + ConfigConst.API_SERVER_HOST;

	public static final String preAppKeyURL = preURL + "/" + ConfigConst.APPKEY.replace("#", "/");

	public static final String TOKEN_APP_URL = preAppKeyURL + "/token";

	public static final String USERS_URL = preAppKeyURL + "/users";

	public static final String MESSAGES_URL = preAppKeyURL + "/messages";

	public static final String CHATGROUPS_URL = preAppKeyURL + "/chatgroups";

	public static final String CHATFILES_URL = preAppKeyURL + "/chatfiles";

	public static final String CHATMESSAGES_URL = preAppKeyURL + "/chatmessages";

	// 通过app的client_id和client_secret来获取app管理员token
	private static Credential credential = new ClientSecretCredential(ConfigConst.APP_CLIENT_ID, ConfigConst.APP_CLIENT_SECRET, ConfigConst.USER_ROLE_APPADMIN);

	/**
	 * 注册IM用户[单个]
	 * 
	 * @param dataNode
	 * @return
	 */
	public static ObjectNode createNewIMUserSingle(ObjectNode dataNode) throws Exception {
		ObjectNode resultNode = factory.objectNode();
		resultNode = IMHTTPClientUtils.sendHTTPRequest(USERS_URL, credential, dataNode, IMHTTPClientUtils.METHOD_POST);
		return resultNode;
	}

	/**
	 * 修改用户nickname
	 * 
	 * @param dataNode
	 * @return
	 */
	public static ObjectNode updateUserNickname(String userId, ObjectNode dataNode) throws Exception {
		ObjectNode resultNode = factory.objectNode();
		resultNode = IMHTTPClientUtils.sendHTTPRequest(USERS_URL + "/" + userId, credential, dataNode, IMHTTPClientUtils.METHOD_PUT);
		return resultNode;
	}

	/**
	 * 修改用户密码
	 * 
	 * @param dataNode
	 * @return
	 */
	public static ObjectNode updateUserPassword(String userId, ObjectNode dataNode) throws Exception {
		ObjectNode resultNode = factory.objectNode();
		resultNode = IMHTTPClientUtils.sendHTTPRequest(USERS_URL + "/" + userId + "/password", credential, dataNode, IMHTTPClientUtils.METHOD_PUT);
		return resultNode;
	}

	/**
	 * 添加好友[单个]
	 * 
	 * @param ownerUserId
	 *            添加者Id
	 * @param friendUserId
	 *            好友Id
	 * 
	 * @return
	 * @throws Exception
	 */
	public static ObjectNode addFriendSingle(String ownerUserId, String friendUserId) throws Exception {
		ObjectNode resultNode = factory.objectNode();
		resultNode = IMHTTPClientUtils.sendHTTPRequest(USERS_URL + "/" + ownerUserId + "/contacts/users/" + friendUserId, credential, null,
				IMHTTPClientUtils.METHOD_POST);
		return resultNode;
	}

	/**
	 * 解除好友关系
	 * 
	 * @param dataNode
	 * @return
	 */
	public static ObjectNode deleteFriendSingle(String ownerUserId, String friendUserId) throws Exception {
		ObjectNode resultNode = factory.objectNode();
		resultNode = IMHTTPClientUtils.sendHTTPRequest(USERS_URL + "/" + ownerUserId + "/contacts/users/" + friendUserId, credential, null,
				IMHTTPClientUtils.METHOD_DELETE);
		return resultNode;
	}

	/**
	 * 创建群组 ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();
	 * dataObjectNode.put("groupname", "测试群组"); dataObjectNode.put("desc",
	 * "测试群组"); dataObjectNode.put("approval", true);
	 * dataObjectNode.put("public", true); dataObjectNode.put("maxusers", 333);
	 * dataObjectNode.put("owner", "xiaojianguo001"); ArrayNode arrayNode =
	 * JsonNodeFactory.instance.arrayNode(); arrayNode.add("xiaojianguo002");
	 * arrayNode.add("xiaojianguo003"); dataObjectNode.put("members",
	 * arrayNode);
	 */
	public static ObjectNode createChatGroups(ObjectNode dataObjectNode) throws Exception {
		ObjectNode resultNode = factory.objectNode();
		resultNode = IMHTTPClientUtils.sendHTTPRequest(CHATGROUPS_URL, credential, dataObjectNode, IMHTTPClientUtils.METHOD_POST);
		return resultNode;
	}

	/**
	 * 修改群组信息 groupname description maxusers
	 * 
	 * @param dataObjectNode
	 * @return
	 * @throws Exception
	 */
	public static ObjectNode updateChatGroups(ObjectNode dataObjectNode) throws Exception {
		ObjectNode resultNode = factory.objectNode();
		resultNode = IMHTTPClientUtils.sendHTTPRequest(CHATGROUPS_URL + "/" + dataObjectNode.get("groupid").asText(), credential, dataObjectNode,
				IMHTTPClientUtils.METHOD_PUT);
		return resultNode;
	}

	/**
	 * 删除群组
	 * 
	 * @param chatgroupid
	 * @return
	 * @throws Exception
	 */
	public static ObjectNode deleteChatGroups(String chatgroupid) throws Exception {
		ObjectNode resultNode = factory.objectNode();
		resultNode = IMHTTPClientUtils.sendHTTPRequest(CHATGROUPS_URL + "/" + chatgroupid, credential, null, IMHTTPClientUtils.METHOD_DELETE);
		return resultNode;
	}

	/**
	 * 获取群组中成员
	 * 
	 * @param chatgroupid
	 * @return
	 * @throws Exception
	 */
	public static ObjectNode getAllMemberssByGroupId(String chatgroupid) throws Exception {
		ObjectNode resultNode = factory.objectNode();
		resultNode = IMHTTPClientUtils.sendHTTPRequest(CHATGROUPS_URL + "/" + chatgroupid + "/users", credential, null, IMHTTPClientUtils.METHOD_GET);
		return resultNode;
	}

	/**
	 * 在群组中添加一个人
	 * 
	 * @param chatgroupid
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static ObjectNode addUserToGroup(String chatgroupid, String userId) throws Exception {
		ObjectNode resultNode = factory.objectNode();
		resultNode = IMHTTPClientUtils
				.sendHTTPRequest(CHATGROUPS_URL + "/" + chatgroupid + "/users/" + userId, credential, null, IMHTTPClientUtils.METHOD_POST);
		return resultNode;
	}

	/**
	 * 在群组中减少一个人
	 * 
	 * @param chatgroupid
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static ObjectNode deleteUserFromGroup(String chatgroupid, String userId) throws Exception {
		ObjectNode resultNode = factory.objectNode();
		resultNode = IMHTTPClientUtils.sendHTTPRequest(CHATGROUPS_URL + "/" + chatgroupid + "/users/" + userId, credential, null,
				IMHTTPClientUtils.METHOD_DELETE);
		return resultNode;
	}

	/**
	 * 群组批量添加成员
	 * 
	 * @param toAddBacthChatgroupid
	 * @param userIds
	 * @return
	 * @throws Exception
	 */
	public static ObjectNode addUsersToGroupBatch(String toAddBacthChatgroupid, ObjectNode userIds) throws Exception {
		ObjectNode resultNode = factory.objectNode();
		resultNode = IMHTTPClientUtils.sendHTTPRequest(CHATGROUPS_URL + "/" + toAddBacthChatgroupid + "/users", credential, userIds,
				IMHTTPClientUtils.METHOD_POST);
		return resultNode;
	}

	/**
	 * 获取聊天消息
	 * 
	 * @param queryStrNode
	 * @throws Exception
	 *
	 */
	public static ObjectNode getChatMessages(ObjectNode queryStrNode) throws Exception {
		ObjectNode resultNode = factory.objectNode();
		String rest = "";
		if (null != queryStrNode && queryStrNode.get("ql") != null && !StringUtil.isEmpty(queryStrNode.get("ql").asText())) {
			rest = "ql=" + java.net.URLEncoder.encode(queryStrNode.get("ql").asText(), "utf-8");
		}
		if (null != queryStrNode && queryStrNode.get("limit") != null && !StringUtil.isEmpty(queryStrNode.get("limit").asText())) {
			rest = rest + "&limit=" + queryStrNode.get("limit").asText();
		}
		if (null != queryStrNode && queryStrNode.get("cursor") != null && !StringUtil.isEmpty(queryStrNode.get("cursor").asText())) {
			rest = rest + "&cursor=" + queryStrNode.get("cursor").asText();
		}
		resultNode = IMHTTPClientUtils.sendHTTPRequest(CHATMESSAGES_URL + "?" + rest, credential, null, IMHTTPClientUtils.METHOD_GET);
		return resultNode;
	}

	/**
	 * 图片语音文件下载
	 * 
	 * 
	 * @param fileUUID
	 *            文件在DB的UUID
	 * @param shareSecret
	 *            文件在DB中保存的shareSecret
	 * @param localPath
	 *            下载后文件存放地址
	 * @param isThumbnail
	 *            是否下载缩略图 true:缩略图 false:非缩略图
	 * @return
	 */
	public static ObjectNode mediaDownload(String fileUUID, String shareSecret, File localPath, Boolean isThumbnail) {
		ObjectNode resultNode = factory.objectNode();
		File downLoadedFile = null;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		if (!StringUtil.isEmpty(shareSecret)) {
			headers.add(new BasicNameValuePair("share-secret", shareSecret));
		}
		headers.add(new BasicNameValuePair("Accept", "application/octet-stream"));
		if (isThumbnail != null && isThumbnail) {
			headers.add(new BasicNameValuePair("thumbnail", String.valueOf(isThumbnail)));
		}
		downLoadedFile = IMHTTPClientUtils.downLoadFile(CHATFILES_URL + "/" + fileUUID, credential, headers, localPath);
		logger.error("File download successfully，file path : " + downLoadedFile.getAbsolutePath() + ".");

		resultNode.put("message", "File download successfully .");

		return resultNode;
	}

	/**
	 * 发送消息
	 * 
	 * @param targetType
	 *            消息投递者类型：users 用户, chatgroups 群组
	 * @param target
	 *            接收者ID 必须是数组,数组元素为用户ID或者群组ID
	 * @param msg
	 *            消息内容
	 * @param from
	 *            发送者
	 * @param ext
	 *            扩展字段
	 * 
	 * @return 请求响应
	 * @throws Exception
	 */
	public static ObjectNode sendMessages(String targetType, ArrayNode target, ObjectNode msg, String from, ObjectNode ext) throws Exception {
		ObjectNode objectNode = factory.objectNode();
		ObjectNode dataNode = factory.objectNode();
		// 构造消息体
		dataNode.put("target_type", targetType);
		dataNode.put("target", target);
		dataNode.put("msg", msg);
		if (StringUtil.isNotEmpty(from)) {
			dataNode.put("from", from);
		} else {
			dataNode.put("from", BusinessConst.SysParam_user);
		}
		if (ext != null) {
			dataNode.put("ext", ext);
		}
		objectNode = IMHTTPClientUtils.sendHTTPRequest(MESSAGES_URL, credential, dataNode, IMHTTPClientUtils.METHOD_POST);
		objectNode = (ObjectNode) objectNode.get("data");
		try {
			for (int i = 0; i < target.size(); i++) {
				String resultStr = objectNode.path(target.path(i).asText()).asText();
				if ("success".equals(resultStr)) {
					System.out.println(String.format("Message has been send to user[%s] successfully .", target.path(i).asText()));
				} else if (!"success".equals(resultStr)) {
					System.out.println(String.format("Message has been send to user[%s] failed .", target.path(i).asText()));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return objectNode;
	}

	/**
	 * 发送用户消息
	 * 
	 * @param userIds
	 * @param msg
	 * @param from
	 * @param ext
	 * @return
	 * @throws Exception
	 */
	public static ObjectNode sendUserMessages(String userIds, ObjectNode msg, String from, ObjectNode ext) throws Exception {
		ArrayNode target = factory.arrayNode();
		for (String userId : StringUtil.parseString2ListByPattern(userIds)) {
			if (StringUtil.isNotEmpty(userId)) {
				target.add(userId);
			}
		}
		return sendMessages(HXUtils.target_users, target, msg, from, ext);
	}

	/**
	 * 发送文本消息
	 * 
	 * @param userIds
	 * @param content
	 * @param from
	 * @param ext
	 * @return
	 * @throws Exception
	 */
	public static ObjectNode sendUserText(String userIds, String content, String from, ObjectNode ext) throws Exception {
		ArrayNode target = factory.arrayNode();
		ObjectNode msg = factory.objectNode();
		msg.put("msg", content);
		msg.put("type", "txt");
		for (String userId : StringUtil.parseString2ListByPattern(userIds)) {
			if (StringUtil.isNotEmpty(userId)) {
				target.add(userId);
			}
		}
		ObjectNode result = sendMessages(HXUtils.target_users, target, msg, from, ext);
		return result;
	}

	/**
	 * 发送文本消息
	 * 
	 * @param userIds
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static ObjectNode sendUserText(String userIds, String content) throws Exception {
		return sendUserText(userIds, content, null, null);
	}

	/**
	 * 发送透析消息
	 * 
	 * @param userIds
	 * @param content
	 * @param from
	 * @param ext
	 * @return
	 * @throws Exception
	 */
	public static ObjectNode sendUserCMD(String userIds, ObjectNode cmd, String from, ObjectNode ext) throws Exception {
		ArrayNode target = factory.arrayNode();
		ObjectNode msg = factory.objectNode();
		msg.put("type", "cmd");
		for (String userId : StringUtil.parseString2ListByPattern(userIds)) {
			if (StringUtil.isNotEmpty(userId)) {
				target.add(userId);
			}
		}
		ObjectNode result = sendMessages(HXUtils.target_users, target, msg, from, ext);
		return result;
	}

	/**
	 * 发送透析消息
	 * 
	 * @param userIds
	 * @param content
	 * @param from
	 * @param ext
	 * @return
	 * @throws Exception
	 */
	public static ObjectNode sendUserCMD(String userIds, ObjectNode ext) throws Exception {
		ArrayNode target = factory.arrayNode();
		ObjectNode msg = factory.objectNode();
		msg.put("type", "cmd");
		for (String userId : StringUtil.parseString2ListByPattern(userIds)) {
			if (StringUtil.isNotEmpty(userId)) {
				target.add(userId);
			}
		}
		ObjectNode result = sendMessages(HXUtils.target_users, target, msg, null, ext);
		return result;
	}
}
