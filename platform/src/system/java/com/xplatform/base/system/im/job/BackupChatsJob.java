package com.xplatform.base.system.im.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.platform.common.utils.HXUtils;
import com.xplatform.base.system.im.entity.BackupChatsEntity;
import com.xplatform.base.system.im.entity.ChatMessageEntity;
import com.xplatform.base.system.im.service.BackupChatsService;
import com.xplatform.base.system.im.service.ChatMessageService;
import com.xplatform.base.system.timer.job.BaseJob;

public class BackupChatsJob extends BaseJob {
	private static final JsonNodeFactory factory = new JsonNodeFactory(false);
	private BackupChatsService backupChatsService = ApplicationContextUtil.getBean("backupChatsService");
	private ChatMessageService chatMessageService = ApplicationContextUtil.getBean("chatMessageService");

	// 通过app的client_id和client_secret来获取app管理员token

	@Override
	public void executeJob(JobExecutionContext paramJobExecutionContext) throws Exception {
		// TODO Auto-generated method stub
		System.err.println("***************************开始执行备份了");
		String queryDateSQL = "";
		String cursor = "";// 当前的cursor
		Integer retryCount = 0;
		// 1.获得当前备份任务中 [备份消息时间最接近当前时间的endTimestamp] 那条记录
		BackupChatsEntity closestTask = backupChatsService.findClosestTask();
		// 2.本次任务从环信拉取timestamp>endTimestamp并且<当前时间的所有记录,每页100条

		// 插入本次备份任务记录
		BackupChatsEntity curTask = new BackupChatsEntity();
		// 2.1 首次备份
		long currentTimestamp = System.currentTimeMillis();
		if (closestTask == null) {
			queryDateSQL += " timestamp<=" + currentTimestamp;
			curTask.setStartTimestamp(null);
			curTask.setEndTimestamp(new Date(currentTimestamp));
		} else {
			String lastEndTimestamp = String.valueOf(closestTask.getEndTimestamp().getTime());
			queryDateSQL += " timestamp>=" + lastEndTimestamp + " and timestamp<=" + currentTimestamp;
			curTask.setStartTimestamp(new Date(closestTask.getEndTimestamp().getTime() + 1000));
			curTask.setEndTimestamp(new Date(closestTask.getEndTimestamp().getTime() + 1000));
		}
		curTask.setStatus(0);
		backupChatsService.save(curTask);

		ObjectNode queryStrNode = factory.objectNode();
		queryStrNode.put("ql", queryDateSQL);
		queryStrNode.put("limit", "3");
		queryStrNode.put("cursor", cursor);

		// 3.中途出错则重试5次
		while (retryCount < 5 && cursor != null) {
			singleBackupProcess(queryStrNode, curTask, retryCount);
			cursor = queryStrNode.path("cursor").asText(null);
			retryCount++;
		}

		// 4.运行到此,说明本次任务成功完成,更新任务表的结束时间
		curTask.setFinishTime(new Date());
		curTask.setStatus(1);
		backupChatsService.update(curTask);
		System.err.println("***************************备份正常结束了");

	}

	private String singleBackupProcess(ObjectNode queryStrNode, BackupChatsEntity curTask, Integer retryCount) {
		String cursor = queryStrNode.path("cursor").asText();
		try {
			while (cursor != null) {
				JsonNode node = null;
				if (!"".equals(cursor)) {
					// 并非第一次进入循环的话,加入游标
					queryStrNode.put("cursor", cursor);
				}
				ObjectNode messages = factory.objectNode();
				List<ChatMessageEntity> list = new ArrayList<ChatMessageEntity>();

				messages = HXUtils.getChatMessages(queryStrNode);
				System.out.println(messages);
				// 插入聊天记录表
				list = messagesToList(messages);
				for (ChatMessageEntity entity : list) {
					String msgType = entity.getType();
					// 如果消息是文件、视频、音频、图片类型的,则需要把文件从IM服务器上下载下来
					if ("file".equals(msgType)) {

					} else if ("vedio".equals(msgType)) {

					} else if ("audio".equals(msgType)) {

					} else if ("img".equals(msgType)) {

					}
				}
				chatMessageService.batchSave(list);

				if (list.size() > 0) {
					// 运行到此,说明本批次成功备份,更新任务表的最末记录备份时间
					curTask.setEndTimestamp(list.get(list.size() - 1).getSendTimestamp());
					backupChatsService.update(curTask);
				}
				node = messages.get("cursor");
				if (node != null) {
					// 依然有数据,则指定游标
					cursor = node.asText();
				} else {
					// 没有数据,终止最外层while
					cursor = null;
					queryStrNode.put("cursor", cursor);
				}
			}
		} catch (Exception e) {
			System.err.println("***************************中途遇到错误");
			curTask.setStatus(2);
			try {
				backupChatsService.update(curTask);
			} catch (BusinessException e1) {
			}
			return cursor;
		}
		return cursor;
	}

	/**
	 * 将返回的json数据转为实体集合
	 * 
	 * @author xiaqiang
	 * @createtime 2015年6月4日 下午9:32:24
	 *
	 * @param messages
	 * @return
	 */
	private static List<ChatMessageEntity> messagesToList(ObjectNode messages) {
		Iterator<JsonNode> iterator = messages.path("entities").iterator();
		List<ChatMessageEntity> list = new ArrayList<ChatMessageEntity>();
		while (iterator.hasNext()) {
			JsonNode node = iterator.next();
			ChatMessageEntity chat = new ChatMessageEntity();
			chat.setId(node.path("uuid").asText(null));
			chat.setMainType(node.path("type").asText(null));
			chat.setCreated(new Date(node.path("created").asLong()));
			chat.setModified(new Date(node.path("modified").asLong()));
			chat.setSendTimestamp(new Date(node.path("timestamp").asLong()));
			chat.setFromId(node.path("from").asText(null));
			chat.setMsgId(node.path("msg_id").asText(null));
			chat.setToId(node.path("to").asText(null));
			chat.setGroupId(node.path("groupId").asText(null));
			chat.setChatType(node.path("chat_type").asText(null));

			JsonNode payload = node.path("payload");
			chat.setBodies(payload.path("bodies").toString());
			chat.setExt(payload.path("ext").toString());

			JsonNode bodies = payload.path("bodies").path(0);
			chat.setType(bodies.path("type").asText(null));
			chat.setMsg(bodies.path("msg").asText(null));
			chat.setUrl(bodies.path("url").asText(null));
			chat.setFilename(bodies.path("filename").asText(null));
			chat.setLength(bodies.path("length").asInt(0));
			chat.setSecret(bodies.path("secret").asText(null));
			chat.setThumb(bodies.path("thumb").asText(null));
			chat.setFileLength(bodies.path("file_length").asLong());
			chat.setThumbSecret(bodies.path("thumb_secret").asText(null));
			chat.setWidth(bodies.path("size").path("width").asInt(0));
			chat.setHeight(bodies.path("size").path("height").asInt(0));
			chat.setAddr(bodies.path("addr").asText(null));
			chat.setLat(bodies.path("lat").asDouble(0));
			chat.setLng(bodies.path("lng").asDouble(0));

			list.add(chat);
		}

		return list;
	}
}
