package com.xplatform.base.system.message.config.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.ComboBox;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupService;
import com.xplatform.base.orgnaization.orgnaization.service.OrgnaizationService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.service.UserService;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.mybatis.dao.SysUserMybatisDao;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.system.attachment.mybatis.vo.AttachVo;
import com.xplatform.base.system.attachment.service.AttachService;
import com.xplatform.base.system.dict.entity.DictValueEntity;
import com.xplatform.base.system.dict.service.DictTypeService;
import com.xplatform.base.system.message.config.entity.ApproveEntity;
import com.xplatform.base.system.message.config.entity.BusinessEntity;
import com.xplatform.base.system.message.config.entity.MailConfigEntity;
import com.xplatform.base.system.message.config.entity.MessageFromEntity;
import com.xplatform.base.system.message.config.entity.MessageSendEntity;
import com.xplatform.base.system.message.config.entity.MessageToEntity;
import com.xplatform.base.system.message.config.mybatis.vo.InnerMessageVo;
import com.xplatform.base.system.message.config.service.MessageService;
import com.xplatform.base.system.message.config.util.MsgUtil;
import com.xplatform.base.system.ntko.entity.OfficeFileEntity;
import com.xplatform.base.system.ntko.service.NtkoService;

/**
 * @Title: Controller
 * @Description: 邮件配置
 * @author binyong
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/messageController")
public class MessageController extends BaseController {

	@Resource
	private MessageService messageService;

	@Resource
	private SysUserMybatisDao sysUserMybatisDao;

	@Resource
	private DictTypeService dictTypeService;

	@Resource
	private UserService userService;

	@Resource
	private SysUserService sysUserService;

	@Resource
	private AttachService attachService;

	@Resource
	private NtkoService ntkoService;

	@Resource
	private OrgnaizationService orgnaizationService;
	@Resource
	private OrgGroupService orgGroupService;

	private AjaxJson result = new AjaxJson();
	private String message;
	private Page<InnerMessageVo> page = new Page<InnerMessageVo>(PAGESIZE);

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/********************************** 邮件配置 ***************************************************************/
	@RequestMapping(params = "mailConfig")
	public ModelAndView mailConfig(HttpServletRequest request) {
		return new ModelAndView("platform/system/message/mailConfigList");
	}

	@RequestMapping(params = "datagrid")
	public void datagrid(MailConfigEntity mailConfig, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		UserEntity user = ClientUtil.getUserEntity();
		CriteriaQuery cq = new CriteriaQuery(MailConfigEntity.class, dataGrid);
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, mailConfig, request.getParameterMap());
		HttpSession session = ContextHolderUtils.getSession();
		// 获取client对象，得到当前用户的Institution的id
		Client client = ClientManager.getInstance().getClient(session.getId());
		// 自定义追加查询条件
		cq.eq("userId", user.getId());
		cq.add();
		this.messageService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	@RequestMapping(params = "mailConfigEdit")
	private ModelAndView mailConfigEdit(MailConfigEntity mailConfig, HttpServletRequest request) {
		if (StringUtils.isNotBlank(mailConfig.getId())) {
			mailConfig = this.messageService.getMailConfig(mailConfig.getId());
		}
		UserEntity user = ClientUtil.getUserEntity();
		return new ModelAndView("platform/system/message/mailConfigEdit").addObject("mailConfig", mailConfig).addObject("user", user);
	}

	@RequestMapping(params = "saveMailConfig")
	@ResponseBody
	public AjaxJson saveMailConfig(MailConfigEntity mailConfig) {
		if (StringUtils.isNotEmpty(mailConfig.getId())) {
			message = "邮件配置更新成功";
			this.messageService.updateMailConfig(mailConfig);
		} else {
			message = "邮件配置新增成功";
			mailConfig.setIsdefault("0");
			this.messageService.saveMailConfig(mailConfig);
		}
		result.setMsg(message);
		return result;
	}

	@RequestMapping(params = "deleteMailCofig")
	@ResponseBody
	public AjaxJson deleteMailCofig(MailConfigEntity mailConfig) {
		this.messageService.deleteMailConfig(mailConfig.getId());
		message = "邮件配置删除成功";
		result.setMsg(message);
		return result;
	}

	@RequestMapping(params = "batchDeleteMailCofig")
	@ResponseBody
	public AjaxJson batchDeleteMailCofig(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		for (String id : ids.split(",")) {
			this.messageService.deleteMailConfig(id);
		}
		message = "邮件配置批量删除成功";
		result.setMsg(message);
		return result;
	}

	@RequestMapping(params = "setDefault")
	@ResponseBody
	public AjaxJson setDefault(HttpServletRequest request) {
		String id = request.getParameter("parameters");
		UserEntity user = ClientUtil.getUserEntity();
		List<MailConfigEntity> list = this.messageService.getMailConfigListByUser(user.getId());
		for (MailConfigEntity m : list) {
			if (m.getId().equals(id)) {
				m.setIsdefault("1");
				this.messageService.updateMailConfig(m);
			} else {
				m.setIsdefault("0");
				this.messageService.updateMailConfig(m);
			}
		}
		message = "邮件默认值设置成功";
		result.setMsg(message);
		return result;
	}

	@RequestMapping(params = "getEmail")
	@ResponseBody
	public List<ComboBox> getEmail(HttpServletRequest request) {
		String value = request.getParameter("value");
		UserEntity user = ClientUtil.getUserEntity();
		List<MailConfigEntity> list = this.messageService.getMailConfigListByUser(user.getId());
		List<ComboBox> boxList = new ArrayList<ComboBox>();
		for (MailConfigEntity mailConfig : list) {
			ComboBox c = new ComboBox();
			c.setId(mailConfig.getId());
			c.setText(mailConfig.getMailAddress());
			if (StringUtil.isNotEmpty(value) && value.equals(mailConfig.getId())) {
				c.setSelected(true);
			} else if ("1".equals(mailConfig.getIsdefault()) && StringUtil.isEmpty(value)) {
				c.setSelected(true);
			}
			boxList.add(c);
		}
		return boxList;
	}

	@RequestMapping(params = "testConnect")
	@ResponseBody
	public AjaxJson testConnect(HttpServletRequest request) {
		String id = StringUtils.isNotEmpty(request.getParameter("id")) ? request.getParameter("id").toString() : request.getParameter("parameters").toString();
		String data = request.getParameter("data");
		Map<String, Object> map = JSONHelper.parseJSON2Map(data);
		MailConfigEntity mail = null;
		if (StringUtils.isNotEmpty(id)) {
			mail = this.messageService.getMailConfig(id);
		} else {
			mail = getForm(map);
		}
		try {
			this.messageService.testConnection(mail);
			message = "测试通过";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			message = "测试失败";
			e.printStackTrace();
		}
		result.setMsg(message);
		return result;
	}

	public MailConfigEntity getForm(Map<String, Object> map) {
		MailConfigEntity mail = new MailConfigEntity();
		mail.setUserName(map.get("userName") != null ? map.get("userName").toString() : "");
		mail.setMailType(map.get("mailType") != null ? map.get("mailType").toString() : "");
		mail.setMailAddress(map.get("mailAddress") != null ? map.get("mailAddress").toString() : "");
		mail.setPassWord(map.get("passWord") != null ? map.get("passWord").toString() : "");
		mail.setSmtpHost(map.get("smtpHost") != null ? map.get("smtpHost").toString() : "");
		mail.setSmtpPort(map.get("smtpPort") != null ? map.get("smtpPort").toString() : "");
		mail.setPopHost(map.get("popHost") != null ? map.get("popHost").toString() : "");
		mail.setPopPort(map.get("popPort") != null ? map.get("popPort").toString() : "");
		mail.setImapHost(map.get("imapHost") != null ? map.get("imapHost").toString() : "");
		mail.setImapPort(map.get("imapPort") != null ? map.get("imapPort").toString() : "");
		return mail;
	}

	/********************************** 会议通知 ***************************************************************/
	@RequestMapping(params = "message")
	public ModelAndView message(HttpServletRequest request) {
		String type = request.getParameter("type");
		if ("annunciate".equals(type)) {
			return new ModelAndView("platform/system/message/annunciateList");
		} else {
			return new ModelAndView("platform/system/message/meetingList");
		}
		/*
		 * if("meeting".equals(type)){ return new
		 * ModelAndView("platform/system/message/meetingList"); }else
		 * if("train".equals(type)){ return new
		 * ModelAndView("platform/system/message/trainList"); }else{ return new
		 * ModelAndView("platform/system/message/annunciateList"); }
		 */
		// return new ModelAndView("platform/system/message/mailConfigList");
	}

	// 会议通知列表
	@RequestMapping(params = "msgDatagrid")
	public void meetingDatagrid(MessageFromEntity messageFrom, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String type = request.getParameter("type");
		UserEntity user = ClientUtil.getUserEntity();
		CriteriaQuery cq = new CriteriaQuery(MessageFromEntity.class, dataGrid);
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, messageFrom, request.getParameterMap());
		// 自定义追加查询条件
		cq.eq("createUserId", user.getId());
		cq.eq("messageType", type);
		cq.notEq("status", "1");
		cq.add();
		this.messageService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}


	// @RequestMapping(params = "updateMsg")
	// @ResponseBody
	// public AjaxJson updateMsg(HttpServletRequest request) throws
	// ParseException, UnsupportedEncodingException {
	// String flag = "";
	// String data = request.getParameter("data");
	// Map<String, Object> map = JSONHelper.parseJSON2Map(data);
	// String id = map.get("id") != null ? map.get("id").toString() : "";
	// MessageFromEntity from = this.messageService.getMessageFrom(id);
	// BusinessEntity bus = new BusinessEntity();
	// if (from == null) {
	// from = new MessageFromEntity();
	// from.setId(id);
	// flag = "add";
	// } else {
	// bus = this.messageService.getBusinessByFromId(id);
	// flag = "update";
	// }
	// from.setStatus("0");//保存
	// from.setMessageType(map.get("messageType") != null ?
	// map.get("messageType").toString() : "");
	// from.setAllUsers(map.get("allUsers") != null ?
	// map.get("allUsers").toString() : "");
	// from.setSendType(map.get("sendType") != null ?
	// map.get("sendType").toString() : "");
	// from.setName(map.get("name") != null ? map.get("name").toString() : "");
	// from.setEmpIds(map.get("empIds") != null ? map.get("empIds").toString() :
	// "");
	// from.setEmpNames(map.get("empNames") != null ?
	// map.get("empNames").toString() : "");
	// from.setJobIds(map.get("jobIds") != null ? map.get("jobIds").toString() :
	// "");
	// from.setJobNames(map.get("jobNames") != null ?
	// map.get("jobNames").toString() : "");
	// from.setCopiedIds(map.get("copiedIds") != null ?
	// map.get("copiedIds").toString() : "");
	// from.setCopiedNames(map.get("copiedNames") != null ?
	// map.get("copiedNames").toString() : "");
	// from.setNeedReply(map.get("needReply") != null ?
	// map.get("needReply").toString() : "");
	// from.setPmApprove(map.get("pmApprove") != null ?
	// map.get("pmApprove").toString() : "");
	// String orgIds = map.get("orgIds") != null ? map.get("orgIds").toString()
	// : "";
	// from.setOrgIds(orgIds);
	// String str = "";
	// if (StringUtils.isNotEmpty(orgIds)) {
	// String[] orgs = orgIds.split(",");
	// for (String org : orgs) {
	// OrgnaizationEntity d = this.orgnaizationService.get(org);
	// str += d.getName() + ",";
	// }
	// from.setOrgNames(str.substring(0, str.length() - 1));
	// }
	// from.setTitle(map.get("title") != null ? map.get("title").toString() :
	// "");
	// from.setContent(map.get("content") != null ?
	// map.get("content").toString() : "");
	// String businesstime = map.get("businesstime") != null ?
	// map.get("businesstime").toString() : "";
	// if (StringUtils.isNotEmpty(businesstime)) {
	// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// bus.setBusinesstime(df.parse(businesstime));
	// }
	// bus.setBusinessPerson(map.get("businessPerson") != null ?
	// map.get("businessPerson").toString() : "");
	// bus.setBusinessPlace(map.get("businessPlace") != null ?
	// map.get("businessPlace").toString() : "");
	// bus.setBusinessType(map.get("messageType") != null ?
	// map.get("messageType").toString() : "");
	// bus.setBusinessObj(map.get("bobj") != null ? map.get("bobj").toString() :
	// "");
	//
	// String companyId = map.get("company") != null ?
	// map.get("company").toString() : "";
	// bus.setCompanyId(companyId);
	// bus.setCompanyName(MsgUtil.getCompanyName(companyId));
	// if ("update".equals(flag)) {
	// this.messageService.updateMsgFrom(from, bus);
	// } else {
	// this.messageService.saveMsg(from, bus);
	// }
	// message = "保存成功";
	// result.setMsg(message);
	// return result;
	// }

	// @RequestMapping(params = "sendMsg")
	// @ResponseBody
	// public AjaxJson saveMsg(MessageFromEntity messageFrom, HttpServletRequest
	// request) throws ParseException,
	// UnsupportedEncodingException {
	// BusinessEntity business = null;
	// String flag = "";
	// MessageFromEntity from =
	// this.messageService.getMessageFrom(messageFrom.getId());
	// if (from == null) {
	// flag = "add";
	// business = new BusinessEntity();
	// } else {
	// flag = "update";
	// business = this.messageService.getBusinessByFromId(messageFrom.getId());
	// }
	// /*String optFlag =
	// request.getParameter("optFlag");//由于ID是事先生成，所以需要该参数来判断是新增还是编辑
	// //设置业务实体属性
	// if(StringUtil.isNotEmpty(messageFrom.getId()) && !"add".equals(optFlag)){
	// business = this.messageService.getBusinessByFromId(messageFrom.getId());
	// }else{
	// business = new BusinessEntity();
	// }*/
	// /*messageFrom.setOrgNames(this.sysUserService.getOrgNameByIds(messageFrom.getOrgIds()));*/
	// //设置短信，邮件，站内信的发送状态
	// String sendType = messageFrom.getSendType();
	// if (sendType.contains("sms")) {
	// messageFrom.setSmsStatus("0");
	// }
	// if (sendType.contains("email")) {
	// messageFrom.setEmailStatus("0");
	// }
	// if (sendType.contains("innerMessage")) {
	// messageFrom.setInnerStatus("0");
	// }
	// messageFrom.setStatus("3");//发送
	//
	// business.setBusinessType(messageFrom.getMessageType());
	// String businesstime = request.getParameter("businesstime");
	// if (StringUtils.isNotEmpty(businesstime)) {
	// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// business.setBusinesstime(df.parse(businesstime));
	// }
	// business.setBusinessPlace(request.getParameter("businessPlace"));
	// business.setBusinessPerson(request.getParameter("businessPerson"));
	//
	// business.setBusinessObj(request.getParameter("businessObj"));
	// String companyId = request.getParameter("company");
	// business.setCompanyId(companyId);
	// business.setCompanyName(MsgUtil.getCompanyName(companyId));
	// if ("annunciate".equals(messageFrom.getMessageType())) {//如果为新增则设置新的文号
	// business.setReference(this.messageService.getReference(messageFrom,
	// business));//设置文号
	// }
	// if (StringUtil.isNotEmpty(messageFrom.getId()) && "update".equals(flag))
	// {
	// this.messageService.updateMsgFrom(messageFrom, business);
	// } else {
	// this.messageService.saveMsg(messageFrom, business);
	// }
	// message = "发送成功";
	// result.setMsg(message);
	// return result;
	// }

	// @RequestMapping(params = "delete")
	// @ResponseBody
	// public AjaxJson delete(HttpServletRequest request) {
	// this.messageService.deleteMsgFrom(request.getParameter("id"));
	// message = "邮件配置删除成功";
	// result.setMsg(message);
	// return result;
	// }

	/********************************** 站内信管理 ***************************************************************/
	// @RequestMapping(params = "innerMsgList")
	// private ModelAndView innerMsgList(HttpServletRequest request) {
	// return new ModelAndView("platform/system/message/inner/innerList");
	// }
	//
	// @RequestMapping(params = "getPringHtml")
	// public ModelAndView getPringHtml(HttpServletRequest request) {
	// String id = request.getParameter("id");
	// MessageFromEntity from = this.messageService.getMessageFrom(id);
	// if (from == null) {
	// MessageToEntity to = this.messageService.getMessageToById(id);
	// from = this.messageService.getMessageFrom(to.getFromId());
	// }
	// BusinessEntity bus =
	// this.messageService.getBusinessByFromId(from.getId());
	// return new
	// ModelAndView("platform/system/message/print/msgPrint").addObject("content",
	// MsgUtil.getMailContent(from, bus));
	// }

	// @RequestMapping(params = "innerMsgView")
	// private ModelAndView meetingEdit(HttpServletRequest request) throws
	// ParseException {
	// String id = request.getParameter("id");
	// String type = request.getParameter("type");
	// MessageToEntity to = null;
	// MessageFromEntity from = null;
	// BusinessEntity bus = null;
	// if (StringUtil.isNotEmpty(id)) {
	// if ("view".equals(type)) { //查看
	// to = this.messageService.getMessageToById(id);
	// from = this.messageService.getMessageFrom(to.getFromId());
	// bus = this.messageService.getBusinessByFromId(to.getFromId());
	// to.setIsRead("1");
	// this.messageService.updateMsgTo(to);
	// } else if ("approve".equals(type)) {
	// from = this.messageService.getMessageFrom(id);
	// bus = this.messageService.getBusinessByFromId(id);
	// }
	// } else {//预览
	// String data = request.getParameter("data");
	// Map<String, Object> map = JSONHelper.parseJSON2Map(data);
	// from = new MessageFromEntity();
	// bus = new BusinessEntity();
	// from.setId(map.get("id") != null ? map.get("id").toString() : "");
	// from.setMessageType(map.get("messageType") != null ?
	// map.get("messageType").toString() : "");
	// from.setAllUsers(map.get("allUsers") != null ?
	// map.get("allUsers").toString() : "");
	// from.setName(map.get("name") != null ? map.get("name").toString() : "");
	// from.setEmpIds(map.get("empIds") != null ? map.get("empIds").toString() :
	// "");
	// from.setEmpNames(map.get("empNames") != null ?
	// map.get("empNames").toString() : "");
	// from.setJobIds(map.get("jobIds") != null ? map.get("jobIds").toString() :
	// "");
	// from.setJobNames(map.get("jobNames") != null ?
	// map.get("jobNames").toString() : "");
	// String orgIds = map.get("orgIds") != null ? map.get("orgIds").toString()
	// : "";
	// from.setOrgIds(orgIds);
	// String str = "";
	// if (StringUtils.isNotEmpty(orgIds)) {
	// String[] orgs = orgIds.split(",");
	// for (String org : orgs) {
	// OrgnaizationEntity d = this.orgnaizationService.get(org);
	// str += d.getName() + ",";
	// }
	// from.setOrgNames(str.substring(0, str.length() - 1));
	// }
	// from.setTitle(map.get("title") != null ? map.get("title").toString() :
	// "");
	// from.setContent(map.get("content") != null ?
	// map.get("content").toString() : "");
	// String businesstime = map.get("businesstime") != null ?
	// map.get("businesstime").toString() : "";
	// if (StringUtils.isNotEmpty(businesstime)) {
	// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// bus.setBusinesstime(df.parse(businesstime));
	// }
	// bus.setBusinessPerson(map.get("businessPerson") != null ?
	// map.get("businessPerson").toString() : "");
	// bus.setBusinessPlace(map.get("businessPlace") != null ?
	// map.get("businessPlace").toString() : "");
	// bus.setBusinessType(map.get("messageType") != null ?
	// map.get("messageType").toString() : "");
	// bus.setBusinessObj(map.get("bobj") != null ? map.get("bobj").toString() :
	// "");
	//
	// String companyId = map.get("company") != null ?
	// map.get("company").toString() : "";
	// bus.setCompanyId(companyId);
	// bus.setCompanyName(MsgUtil.getCompanyName(companyId));
	// if ("annunciate".equals(from.getMessageType())) {
	// bus.setReference(this.messageService.getReference(from, bus));//设置文号
	// }
	// }
	//
	// //获取上传附件信息
	// Map<String, String> param = new HashMap<String, String>();
	// param.put("businessKey", from.getId());
	// List<DataVo> dataVoList = dataService.queryUploadData(param);
	// return new ModelAndView("platform/system/message/inner/innerView")
	// .addObject("content", MsgUtil.getMailContent(from,
	// bus)).addObject("msgTo", to).addObject("from", from)
	// .addObject("dataVoList", dataVoList);
	// }

	// @RequestMapping(params = "innerDatagrid")
	// public void innerDatagrid(HttpServletRequest request, HttpServletResponse
	// response, DataGrid dataGrid) {
	// Map<String, Object> param = new HashMap<String, Object>();
	// UserEntity user = ClientUtil.getUserEntity();
	// List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	// param.put("userId", user.getId());
	// param.put("type", "all");
	// param.put("messageType", request.getParameter("message.type"));
	// param.put("name", request.getParameter("name"));
	// // List<Map<String, Object>> list =
	// this.sysUserMybatisDao.queryInnerMessageByUser(param);
	// dataGrid.setResults(list);
	// dataGrid.setTotal(list.size());
	// TagUtil.datagrid(response, dataGrid);
	// }

	@RequestMapping(params = "deleteMsgTo")
	@ResponseBody
	public AjaxJson deleteMsgTo(HttpServletRequest request) {
		String id = request.getParameter("id");
		this.messageService.deleteMsgTo(id);
		message = "站内信删除成功";
		result.setMsg(message);
		return result;
	}

	// 回复
	@RequestMapping(params = "reply")
	private ModelAndView reply(HttpServletRequest request) {
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		MessageToEntity to = this.messageService.getMessageToById(id);
		return new ModelAndView("platform/system/message/reply/messageReply").addObject("msgTo", to);
	}

	// 保存回复
	@RequestMapping(params = "saveReply")
	@ResponseBody
	public AjaxJson saveReply(HttpServletRequest request) {
		String id = request.getParameter("id");
		String btn_type = request.getParameter("btn_type");
		if ("reply".equals(btn_type)) {
			String content = request.getParameter("content");
			MessageToEntity to = this.messageService.getMessageToById(id);
			to.setReplyContent(content);
			to.setNeedReply("2");
			this.messageService.saveMsgTo(to);
			message = "消息回复成功";
		} else if ("transpond".equals(btn_type)) {
			MessageToEntity to = this.messageService.getMessageToById(id);
			MessageFromEntity from = this.messageService.getMessageFrom(to.getFromId());
			BusinessEntity bus = this.messageService.getBusinessByFromId(to.getFromId());

			MessageFromEntity f = new MessageFromEntity();
			BusinessEntity b = new BusinessEntity();
			f.setId(UUIDGenerator.generate());
			f.setName(from.getName());
			f.setMessageType(from.getMessageType());
			f.setTitle(from.getTitle());
			f.setContent(from.getContent());
			f.setAttach(from.getAttach());
			// 设置短信，邮件，站内信的发送状态
			String sendType = request.getParameter("sendType");
			f.setSendType(sendType);
			if (sendType.contains("sms")) {
				f.setSmsStatus("0");
			}
			if (sendType.contains("email")) {
				f.setEmailStatus("0");
				f.setMailConfigId(request.getParameter("mailConfigId"));
			}
			if (sendType.contains("innerMessage")) {
				f.setInnerStatus("0");
			}

			f.setStatus("2");
			// 设置转发接收人
			f.setEmpIds(request.getParameter("empIds"));
			f.setEmpNames(request.getParameter("empNames"));
			f.setJobIds(request.getParameter("jobIds"));
			f.setJobNames(request.getParameter("jobNames"));
			f.setOrgIds(request.getParameter("orgIds"));
			/*
			 * f.setOrgNames(this.sysUserService.getOrgNameByIds(request.
			 * getParameter("orgIds")));
			 */
			f.setAllUsers("0");
			f.setNeedReply("0");
			if ("annunciate".equals(f.getMessageType())) {
				f.setPmApprove(from.getPmApprove());
				f.setGmStatus(from.getGmStatus());
				f.setPmStatus(from.getPmStatus());
			}

			b.setBusinessObj(bus.getBusinessObj());
			b.setBusinessPerson(bus.getBusinessPerson());
			b.setBusinessPlace(bus.getBusinessPlace());
			b.setBusinesstime(bus.getBusinesstime());
			b.setBusinessType(bus.getBusinessType());
			b.setReference(bus.getReference());
			b.setCompanyId(bus.getCompanyId());
			b.setCompanyName(bus.getCompanyName());

			this.messageService.saveMsg(f, b);

			/*
			 * //转发保存审核信息 Map<String,String> param = new
			 * HashMap<String,String>(); param.put("fromId", from.getId());
			 * //param.put("psnType", roleCode); List<ApproveEntity> appList =
			 * this.messageService.queryApproveEntityList(param);
			 * for(ApproveEntity app : appList){ ApproveEntity approve = new
			 * ApproveEntity(); approve.setFromId(f.getId());
			 * approve.setPsnType(app.getPsnType());
			 * approve.setType(app.getType());
			 * approve.setStatus(app.getStatus());
			 * approve.setContent(app.getContent()); }
			 */

			message = "消息转发成功";
		}

		result.setMsg(message);
		return result;
	}

	/*
	 * //查看回复列表
	 * 
	 * @RequestMapping(params = "replyView") private ModelAndView
	 * replyView(HttpServletRequest request) { String id =
	 * request.getParameter("id"); Map<String, Object> param = new
	 * HashMap<String, Object>(); param.put("id", id); List<Map<String, Object>>
	 * list = this.sysUserMybatisDao.queryReplyViewList(param); return new
	 * ModelAndView("platform/system/message/inner/replyList").addObject("list",
	 * list); }
	 * 
	 * //查看回复列表
	 * 
	 * @RequestMapping(params="replyView2") private ModelAndView
	 * replyView2(HttpServletRequest request){ String id =
	 * request.getParameter("id"); Map<String,Object> param = new
	 * HashMap<String,Object>(); param.put("id", id); List<Map<String,Object>>
	 * list = this.sysUserMybatisDao.queryReplyViewList2(param); return new
	 * ModelAndView("platform/system/inform/replyList").addObject("list", list);
	 * }
	 */

	// 查看审核列表
	@RequestMapping(params = "approveView")
	private ModelAndView approveView(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map<String, String> param = new HashMap<String, String>();
		param.put("fromId", id);
		List<ApproveEntity> approveList = this.messageService.queryApproveEntityList(param);
		return new ModelAndView("platform/system/message/approve/ApproveView").addObject("approveList", approveList);
	}


	// 内部通告审核列表
	/*
	 * @RequestMapping(params = "annunciateApproveDatagrid") public void
	 * annunciateApproveDatagrid(MessageFromEntity messageFrom,
	 * HttpServletRequest request, HttpServletResponse response, DataGrid
	 * dataGrid) { String sort = request.getParameter("sort"); String order =
	 * request.getParameter("order"); String roleCode =
	 * request.getParameter("roleCode"); Map<String, String> param = new
	 * HashMap<String, String>(); UserEntity user = ClientUtil.getUserEntity();
	 * param.put("userId", user.getId()); param.put("roleCode", roleCode);
	 * param.put("empId", user.getUserTypeId()); EmpUserVo userVo =
	 * this.userService.getUserInfo(param); param.put("orgId",
	 * userVo.getJobId()); param.put("sort", sort); param.put("order", order);
	 * List<Map<String, Object>> list =
	 * this.sysUserMybatisDao.queryAnnunciateApprove(param);
	 * dataGrid.setResults(list); dataGrid.setTotal(list.size());
	 * TagUtil.datagrid(response, dataGrid); }
	 */

	// 进入审核界面
	@RequestMapping(params = "annApprove")
	private ModelAndView annApprove(HttpServletRequest request) {
		String id = request.getParameter("id");
		String roleCode = request.getParameter("roleCode");
		Map<String, String> param = new HashMap<String, String>();
		param.put("fromId", id);
		// param.put("psnType", roleCode);
		List<ApproveEntity> approveList = this.messageService.queryApproveEntityList(param);
		return new ModelAndView("platform/system/message/approve/annunciateApproveEdit").addObject("approveList", approveList);
	}

	// 保存审核结果
	@RequestMapping(params = "saveAnnApprove")
	@ResponseBody
	public AjaxJson saveAnnApprove(HttpServletRequest request) {
		String id = request.getParameter("id");
		String roleCode = request.getParameter("roleCode");
		MessageFromEntity msgf = this.messageService.getMessageFrom(id);
		// 设置审核状态
		if ("pm".equals(roleCode)) {
			msgf.setPmStatus(request.getParameter("status"));
		}
		if ("gm".equals(roleCode)) {
			msgf.setGmStatus(request.getParameter("status"));
		}
		// 保存审核信息
		ApproveEntity app = new ApproveEntity();
		app.setFromId(id);
		app.setStatus(request.getParameter("status"));
		app.setContent(request.getParameter("content"));
		app.setType(msgf.getMessageType());
		app.setPsnType(roleCode);
		this.messageService.updateMsgFrom(msgf);
		this.messageService.saveApprove(app);
		message = "审核成功";
		result.setMsg(message);
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年11月13日 上午10:55:53
	 * @Decription 请求首页站内信数据
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "queryInnerMsgData")
	@ResponseBody
	public JSONObject queryInnerMsgData(HttpServletRequest request) {
		List<InnerMessageVo> list = new ArrayList<InnerMessageVo>();

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", ClientUtil.getUserId());
		param.put("isRead", "0");
		param.put("status", "1");
		// 获得所有未读站内信
		list = messageService.queryInnerMessageByUser(param);
		int total = 0;
		Map<String, DictValueEntity> mapperMap = new HashMap<String, DictValueEntity>();
		List<DictValueEntity> mapperList = dictTypeService.findDictValueEntityListByCode("messageCategory");
		for (DictValueEntity value : mapperList) {
			// 将大、小分类对应关系放到map中,key为大分类code,value为DictValueEntity(value为小分类code,用","分割多个;extend2为url)
			if (!mapperMap.containsKey(value.getExtend1())) {
				mapperMap.put(value.getExtend1(), value);
			}
		}

		Map<String, JSONArray> map = new HashMap<String, JSONArray>();

		for (InnerMessageVo vo : list) {
			JSONObject sObj = new JSONObject();
			sObj.accumulate("title", vo.getTitle());
			// sObj.accumulate("receiveTime",
			// DateUtils.formatDateWZTime(vo.getReceiveTime()));
			// sObj.accumulate("receiveName", vo.getReceiveName());
			sObj.accumulate("receiveId", vo.getReceiveId());

			String messageCategory = "";
			String url = "";
			String icon = "";
			// 判断本消息属于哪个大分类,以及使用哪个url跳转
			for (Map.Entry<String, DictValueEntity> entry : mapperMap.entrySet()) {
				DictValueEntity value = entry.getValue();
				// if (value.getValue().contains(vo.getMessageType())) {
				// messageCategory = entry.getKey();
				// url = value.getExtend2();
				// icon = value.getExtend3();
				// break;
				// }
			}
			sObj.accumulate("messageCategory", messageCategory);
			// sObj.accumulate("url", url + "&id=" + vo.getId());
			sObj.accumulate("icon", icon);

			// 构造大分类下的JSONArray
			JSONArray temp = map.get(messageCategory);
			if (BeanUtils.isEmpty(temp)) {
				temp = new JSONArray();
			}
			temp.add(sObj);
			map.put(messageCategory, temp);
		}

		// 此时的map不包含0条消息的大类,在此处理加入
		for (String key : mapperMap.keySet()) {
			if (!map.containsKey(key)) {
				map.put(key, new JSONArray());
			}
		}

		JSONArray fArray = new JSONArray();
		for (Map.Entry<String, JSONArray> entry : map.entrySet()) {
			JSONObject fObj = new JSONObject();
			String title = ""; // 分类标题
			Integer count = entry.getValue().size(); // 剩余数
			String url = "";
			title = mapperMap.get(entry.getKey()).getName();

			fObj.accumulate("title", title);
			fObj.accumulate("count", count);
			fObj.accumulate("url", url);
			fObj.accumulate("msgs", entry.getValue());
			total += count;
			fArray.add(fObj);
		}

		JSONObject mainObj = new JSONObject();
		mainObj.accumulate("total", total).accumulate("category", fArray);
		return mainObj;
	}

	// office word 打印界面
	@RequestMapping(params = "printFile")
	public ModelAndView printFile(HttpServletRequest request) {
		String id = request.getParameter("id");
		String name = "ntko/templateFile/" + request.getParameter("name");
		OfficeFileEntity of = this.ntkoService.getOfficeEntityByBusId(id);
		if (of != null) {
			name = of.getFileNameDisk();
		}
		return new ModelAndView("platform/system/inform/printOffice").addObject("name", name);
	}

	// 进入消息展示界面
	@RequestMapping(params = "sysMsgView")
	private ModelAndView sysMsgView(HttpServletRequest request) throws BusinessException {
		String id = request.getParameter("id");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		List<InnerMessageVo> msgList = this.messageService.queryInnerMessageByUser(param);
		this.messageService.updateInnerMessageToRead(id);
		request.setAttribute("message", msgList.get(0));
		return new ModelAndView("platform/system/message/inner/sysMsgView");
	}

	// 进入消息列表
	@RequestMapping(params = "sysMsgList")
	private ModelAndView sysMsgList(HttpServletRequest request) {
		return new ModelAndView("platform/system/message/inner/sysMsgList");
	}

	@RequestMapping(params = "sysMsgDatagrid")
	public void sysMsgDatagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {

		// 自动封装查询条件
		this.buildFilter(page, request);
		// 添加排序字段
		// this.buildOrder(page, request, "receiveTime", Page.DESC);
		Map<String, String[]> msgTypeMap = new HashMap<String, String[]>();
		List<DictValueEntity> mapperList = dictTypeService.findDictValueEntityListByCode("messageCategory");
		for (DictValueEntity value : mapperList) {
			// 将大、小分类对应关系放到map中,key为大分类code,value为DictValueEntity(value为小分类code,用","分割多个;extend2为url)
			if (!msgTypeMap.containsKey(value.getExtend1())) {
				msgTypeMap.put(value.getExtend1(), value.getValue().split(","));// put("notice","[meeting,train,annunciate]");
			}
		}

		Map<String, Object> map = (Map<String, Object>) page.getParameter();
		map.put("userId", ClientUtil.getUserId());
		map.put("status", "1");
		String msgType = request.getParameter("messageType");
		if (StringUtil.isNotEmpty(msgTypeMap.get(msgType))) {
			map.put("messageType", StringUtil.arrayToListWithQuote(msgTypeMap.get(msgType)));
		} else {
			map.put("messageType", new String[] { "-999" });// 该消息数据库中没有类型配置("notice","[meeting,train,annunciate]"),查出空数据
		}
		String isRead = request.getParameter("isRead");
		if (StringUtil.isNotEmpty(isRead)) {
			map.put("isRead", isRead);
		}
		page.setParameter(map);
		page = messageService.queryInnerMessageByPage(page);
		dataGrid.setResults(page.getResult());
		dataGrid.setTotal((int) page.getTotalCount());
		TagUtil.datagrid(response, dataGrid);
	}

	@RequestMapping(params = "validEmail")
	@ResponseBody
	public AjaxJson validEmail(HttpServletRequest request, HttpServletResponse response) {
		String email = request.getParameter("email");
		String status = request.getParameter("status");
		result.setSuccess(true);
		result.setStatus("y");
		result.setSuccess(false);
		result.setStatus("n");
		result.setInfo("原密码填写错误");
		return result;

	}

	@RequestMapping(params = "testSend")
	@ResponseBody
	public AjaxJson testSend(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String receive = request.getParameter("receive");
		String content = request.getParameter("content");
		String extraParam = request.getParameter("extra");
		MessageSendEntity send = new MessageSendEntity();
		send.setSendChannel("IM,msg");
		send.setReceive(receive);
		send.setContent(content);
		
		List<Map<String,Object>> extraData=new ArrayList<Map<String,Object>>();
		Map<String,Object> data=new HashMap<String,Object>();
		data.put("name", "项目名称");
		data.put("value", "河南担保项目");
		extraData.add(data);
		data=new HashMap<String,Object>();
		data.put("name", "开始时间");
		data.put("value", "2015-5-28");
		extraData.add(data);
		data=new HashMap<String,Object>();
		data.put("name", "评论意见");
		data.put("value", "我已经完成了所有的待审批任务，准备接受 下一个审批任务");
		extraData.add(data);
		String groupId="129265796705681920";
		Map<String,Object> groupUserList=orgGroupService.getUserOrg(groupId);
		Map<String,Object> extra=new HashMap<String,Object>();
		extra.put("groupId", groupId);
		extra.put("currentUserId", "1");
		extra.put("title", "我的项目");
		extra.put("header", "张三的出差需要您的审批");
		extra.put("description", "张三的出差需要您的审批");
		extra.put("content", "张三的出差需要您的审批");
		extra.put("url", "taskController.do?toStart&taskId=");
		extra.put("img", "4028810350c618e20150c621c2940002");
		extra.put("createTime", "2016-4-23");
		extra.put("groupUserList", groupUserList.get("OrgGroupMember"));
		extra.put("approveStatus", "1");
		extra.put("footer", "帮帮邦团队 10月15日");
		extra.put("extraData", extraData);
		send.setSourceType("flow");
		

		
//		ObjectNode obj = JsonNodeFactory.instance.objectNode();
//		obj.put("action", "t1");
//		obj.put("text", "2222");
		ObjectNode IMMsg = JsonNodeFactory.instance.objectNode();
		IMMsg.put("type", "cmd");
		IMMsg.put("action", "");
		send.setIMMsg(IMMsg.toString());
		if(StringUtil.isNotEmpty(extraParam)){
			send.setExtra(extraParam);
		}else{
			send.setExtra(JSONHelper.toJSONString(extra));
		}
		
		MsgUtil.SendMulMeassage(send, null);
		return result;
	}

	/**
	 * 获取个人系统消息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "loadInnerMessage")
	@ResponseBody
	public AjaxJson loadInnerMessage(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		//List<String> orgIds = ClientUtil.getManagerOrgQuoteIds();
		this.buildFilter(page, request);
		Map<String, Object> param = (Map<String, Object>) page.getParameter();
		//param.put("orgIds", new String[] { "'"+orgId+"'" });
		param.put("orgIds", null);
		param.put("userId", ClientUtil.getUserId());
		page.setParameter(param);
		Page<InnerMessageVo> list = this.messageService.queryInnerMessageByPage(page);
		j.setObj(list.getResult());
		return j;
	}

	/**
	 * 删除系统消息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "deleteMessage")
	@ResponseBody
	public AjaxJson deleteMessage(HttpServletRequest request) throws BusinessException {
		AjaxJson j = new AjaxJson();
		String sendId = request.getParameter("sendId");
		this.messageService.deleteMessageReceive(sendId);
		j.setMsg("删除成功");
		return j;
	}

	/**
	 * 设置系统消息为已读
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "readMessage")
	@ResponseBody
	public AjaxJson readMessage(HttpServletRequest request) throws BusinessException {
		AjaxJson j = new AjaxJson();
		String userId = ClientUtil.getUserId();
		String sendId = request.getParameter("sendId");
		String funcType = request.getParameter("funcType");
		if (BusinessConst.FUNCTYPE_CODE_private.equals(funcType)) {
			// 说明是private-个人消息(1对1/1对少量n)
			this.messageService.doReadMessageReceive(sendId, userId);
		} else {
			// 说明是global-公告/group-分组消息(选择部门/角色/某个用户范围)
			this.messageService.saveMessageReceive(sendId, userId, funcType);
		}
		return j;
	}

	@RequestMapping(params = "home")
	private ModelAndView home(HttpServletRequest request) {
		return new ModelAndView("main/home/message/messageHome");
	}
	
	@RequestMapping(params = "sysMessageHome")
	private ModelAndView IMHome(HttpServletRequest request) {
		return new ModelAndView("main/home/message/sysMessageHome");
	}
}
