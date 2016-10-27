package com.xplatform.base.workflow.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.PropertiesUtil;
import com.xplatform.base.framework.core.util.StringUtil;

public class FlowUtil {
	public static String replaceTitleTag(String template, String receiver, String sender, String subject, String cause,Map<String,Object> vars) {
		if (StringUtil.isEmpty(template))
			return "";
		template = template.replace("${收件人}", receiver).replace("${发件人}", sender).replace("${转办人}", sender)
				.replace("${代理人}", sender);
		if (StringUtil.isEmpty(cause))
			cause = "无";
		template = template.replace("${原因}", cause).replace("${逾期级别}", cause);

		if(StringUtil.isNotEmpty(subject)){
			template = template.replace("${事项名称}", subject);
		}
		
		if(vars!=null){
			Pattern p = Pattern.compile("\\$\\{(.*?)\\}");
			Matcher m = p.matcher(template);
			while (m.find()) {
				if(vars.get(m.group(1))!=null){
					template = template.replace(m.group(0), vars.get(m.group(1)).toString());
				}
			}
		}
		template = template.replaceAll("&nuot;", "\n");
		return template;
	}

	public static String replaceTemplateTag(String template, String receiver, String sender, String subject,
			String url, String cause, boolean isSms,Map<String,Object> vars) {
		if (StringUtil.isEmpty(template))
			return "";
		template = template.replace("${收件人}", receiver).replace("${发件人}", sender).replace("${转办人}", sender)
				.replace("${代理人}", sender);
		if (StringUtil.isNotEmpty(cause)) {
			template = template.replace("${原因}", cause).replace("${逾期级别}", cause).replace("${事项意见}", cause);
		}
		if (StringUtil.isNotEmpty(subject)) {
			if ((isSms) || (StringUtil.isEmpty(url)))
				template = template.replace("${事项名称}", subject);
			else {
				template = template.replace("${事项名称}", "<a href='#' onclick='showTask(\"" + url + "\")'>" + subject + "</a>");
			}
		}
		if(vars!=null){
			Pattern p = Pattern.compile("\\$\\{(.*?)\\}");
			Matcher m = p.matcher(template);
			while (m.find()) {
				if(vars.get(m.group(1))!=null){
					template = template.replace(m.group(0), vars.get(m.group(1)).toString());
				}
			}
		}
		template = template.replaceAll("&quot;", "\n");
		return template;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月29日 下午5:21:12
	 * @Decription 站内信,邮件等通知中替换url的方法
	 *
	 * @param id
	 * @param isTask
	 * @return
	 */
	public static String getUrl(String id, boolean isTask) {
		String url = "";
		if (BeanUtils.isEmpty(id))
			return "";
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		if (StringUtils.isNotEmpty(p.readProperty("serverUrl"))) {
			url = p.readProperty("serverUrl");
		}
		if (isTask) {
			url = url + "/taskController.do?toStart&taskId=" + id;
		} else {
			url = url + "/processInstance/info.ht?runId=" + id;
		}
		return url;
	}

	public static String getProcessModuleType(int type) {
		String info = "";
		switch (type) {
		case 1:
			info = "待办";
			break;
		case 2:
			info = "驳回";
			break;
		case 3:
			info = "撤销";
			break;
		case 4:
			info = "催办";
			break;
		case 5:
			info = "驳回发起人";
			break;
		case 6:
			info = "沟通反馈";
			break;
		case 7:
			info = "会话通知";
			break;
		case 8:
			info = " 转办";
			break;
		case 9:
			info = "代理";
			break;
		case 10:
			info = "取消转办";
			break;
		case 11:
			info = "取消代理";
			break;
		case 12:
			info = "归档";
			break;
		case 13:
			info = "抄送";
			break;
		case 14:
			info = "终止";
			break;
		case 15:
			info = "转发";
			break;
		case 17:
			info = "消息节点";
			break;
		case 16:
		}

		return info;
	}

	public static boolean isAssigneeEmpty(String assignee) {
		return (StringUtil.isEmpty(assignee)) || ("0".equals(assignee));
	}

	public static boolean isAssigneeNotEmpty(String assignee) {
		return !isAssigneeEmpty(assignee);
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月29日 下午4:23:38
	 * @Decription 替换groovyScript中可以toString的参数
	 *
	 * @param content
	 * @param vars
	 * @return
	 */
	public static String replaceVars(String content, Map<String, Object> vars) {
		for (Map.Entry entry : vars.entrySet()) {
			String hold = "${" + (String) entry.getKey() + "}";
			content = content.replace(hold, entry.getValue() == null ? "" : entry.getValue().toString());
		}
		return content;
	}
}
