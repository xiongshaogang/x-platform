package com.xplatform.base.orgnaization.orggroup.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.xalan.lib.sql.ObjectArray;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xplatform.base.form.entity.FlowFormEntity;
import com.xplatform.base.framework.core.common.dao.jdbc.JdbcDao;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.HqlQuery;
import com.xplatform.base.framework.core.common.hibernate.qbc.PageList;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.PinyinUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.orgnaization.orggroup.dao.OrgGroupDao;
import com.xplatform.base.orgnaization.orggroup.entity.OrgGroupEntity;
import com.xplatform.base.orgnaization.orggroup.entity.OrgGroupMemberEntity;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupMemberService;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.role.service.RoleService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.entity.UserRoleEntity;
import com.xplatform.base.orgnaization.user.service.UserRoleService;
import com.xplatform.base.orgnaization.user.service.UserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.platform.common.utils.HXUtils;

@Service("orgGroupService")
public class OrgGroupServiceImpl extends BaseServiceImpl<OrgGroupEntity> implements OrgGroupService {

	@Resource
	private OrgGroupDao orgGroupdao;
	@Resource
	private OrgGroupMemberService orgGroupMemberService;
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	@Resource
	private UserRoleService userRoleService;

	private AjaxJson j = new AjaxJson();

	@Resource
	public void setBaseDao(OrgGroupDao orgGroupdao) {
		super.setBaseDao(orgGroupdao);
	}

	@Override
	public AjaxJson saveAndProcessHX(OrgGroupEntity orgGroup,int workGroup) throws Exception {
		AjaxJson j = new AjaxJson();
		String groupid = "";// 环信id
		//设置群组类别
		orgGroup.setWorkGroup(workGroup);
		if (orgGroup.getOwner() == null) {
			orgGroup.setOwner(ClientUtil.getUserId());
		}
		if (orgGroup.getApproval() == null) {
			orgGroup.setApproval(1);
		}
		if (orgGroup.getIspublic() == null) {
			orgGroup.setIspublic(1);
		}
		if (orgGroup.getMaxusers() == null) {
			orgGroup.setMaxusers(100);
		}
		if (orgGroup.getAllowinvites() == null) {
			orgGroup.setAllowinvites(0);
		}

		// this.orgGroupdao.save(orgGroup);

		// 添加群组群主
		ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
		arrayNode.add(ClientUtil.getUserId());
		UserEntity user = this.userService.get(orgGroup.getOwner());
		OrgGroupMemberEntity orgGroupMemberEntity = new OrgGroupMemberEntity();
		orgGroupMemberEntity.setId(UUIDGenerator.generate());
		orgGroupMemberEntity.setUserId(user.getId());
		// orgGroupMemberEntity.setGroupId(orgGroup.getId());
		orgGroupMemberEntity.setIsOwner(1);
		// 设置群名称
		if (orgGroup.getName() == null) {
			if (StringUtil.isEmpty(user.getName())) {
				orgGroup.setName(user.getPhone());
			} else {
				orgGroup.setName(user.getName());
			}
		}

/*		//WorkGroup为2则为角色群
		if(orgGroup.getWorkGroup() == 2){
			String orgId = ClientUtil.getUserEntity().getOrgIds();
			//1、先生成角色插入角色表
			RoleEntity role = new RoleEntity();
			role.setOrgId(orgId);
			role.setDefinedFlag(2);
			role.setName(orgGroup.getName().replace(" ", ""));
			String name = role.getName().replaceAll("\\pP|\\pS", "");
			String code = PinyinUtil.converterToFirstSpell(name);
			role.setCode(this.getCode(code));
			String roleId = this.roleService.save(role);
			
			//2、往员工角色表插入数据
			
			UserRoleEntity userRole = new UserRoleEntity();
			userRole.setUser(user);
			userRole.setRole(role);
			userRole.setOrgId(orgId);
			this.userRoleService.save(userRole);
			
			//3、设置群组的机构id和角色id
			orgGroup.setOrgId(orgId);
			orgGroup.setRoleId(roleId);
			
		}*/
		
		ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();
		dataObjectNode.put("groupname", orgGroup.getName());
		if (StringUtil.isNotEmpty(orgGroup.getDescription())) {
			dataObjectNode.put("desc", orgGroup.getDescription());
		}
		dataObjectNode.put("approval", orgGroup.getApproval() == null ? true : StringUtil.parseToBoolean(orgGroup.getApproval()));
		dataObjectNode.put("public", orgGroup.getIspublic() == null ? true : StringUtil.parseToBoolean(orgGroup.getIspublic()));
		dataObjectNode.put("allowinvites", orgGroup.getAllowinvites() == null ? false : StringUtil.parseToBoolean(orgGroup.getAllowinvites()));
		dataObjectNode.put("maxusers", orgGroup.getMaxusers());
		dataObjectNode.put("owner", orgGroup.getOwner());

		// this.orgGroupMemberService.save(orgGroupMemberEntity);
		dataObjectNode.put("members", arrayNode);
		// arrayNode.add("xiaojianguo002");
		// arrayNode.add("xiaojianguo003");
		// dataObjectNode.put("members", arrayNode);
		try {

			ObjectNode result = HXUtils.createChatGroups(dataObjectNode);
			if (result.get("statusCode").intValue() == 200) {
				groupid = result.get("data").get("groupid").asText();
				// orgGroup.setEmGroupId(groupid);
				orgGroup.setId(groupid);
				this.orgGroupdao.save(orgGroup);
				orgGroupMemberEntity.setGroupId(groupid);
				this.orgGroupMemberService.save(orgGroupMemberEntity);
				// this.orgGroupdao.updateEntitie(orgGroup);
				j.setObj(orgGroup);
				j.setSuccess(true);
				j.setMsg("工作组创建成功！");
			} else {
				ExceptionUtil.throwException("IM服务器请求失败");
			}
		} catch (Exception e) {
			// TODO: handle exception
			// 删除环信表记录
			ObjectNode result = HXUtils.deleteChatGroups(groupid);
			if (result.get("statusCode").intValue() == 200) {
			} else {
				ExceptionUtil.throwException("IM服务器请求失败,操作:删除环信group");
			}
		}
		return j;
	}

	@Override
	public AjaxJson saveAndProcessHX(OrgGroupEntity orgGroup, String phones, String ids,int workGroup) throws Exception {
		AjaxJson j = new AjaxJson();
		String NotRigPhone = "";
		String NotRigId = "";
		String groupid = ""; // 环信id
		List<UserEntity> userList = new ArrayList<UserEntity>();
		Map<String,String> isRepeat = new HashMap<String,String>();
		List<OrgGroupMemberEntity> OrgGroupMemberList = new ArrayList<OrgGroupMemberEntity>();
		// 添加群组群主
		UserEntity Owner = this.userService.get(ClientUtil.getUserId());
		if(!(isRepeat.containsKey(Owner.getId()))){
			OrgGroupMemberEntity orgGroupMemberEntity = new OrgGroupMemberEntity();
			orgGroupMemberEntity.setId(UUIDGenerator.generate());
			orgGroupMemberEntity.setUserId(Owner.getId());
			orgGroupMemberEntity.setIsOwner(1);
			OrgGroupMemberList.add(orgGroupMemberEntity);
			if(StringUtil.isNotEmpty(phones)){
				//phones = Owner.getPhone()+","+phones;
				isRepeat.put(Owner.getPhone(), Owner.getPhone());
			}else{
				//ids = Owner.getId()+","+ids;
				isRepeat.put(Owner.getId(), Owner.getId());
			}
		}
		if (StringUtil.isNotEmpty(phones) || StringUtil.isNotEmpty(ids)) {
			String[] phoneArr = StringUtil.split(phones, ",");
			String[] idArr = StringUtil.split(ids, ",");
			if (StringUtil.isNotEmpty(phones)) {
				for (String phone : phoneArr) {
					UserEntity user = this.userService.getUser(phone, true);
					if (user != null) {
						//不可重复添加
						if(!(isRepeat.containsKey(user.getId()))){
							isRepeat.put(user.getId(), user.getId());
							userList.add(user);
						}
						
					} else {
						if (NotRigPhone == "") {
							NotRigPhone = phone;
						} else {
							NotRigPhone = NotRigPhone + "," + phone;
						}
					}
				}
			}
			if (StringUtil.isNotEmpty(ids)) {
				for (String id : idArr) {
					UserEntity user = this.userService.get(id);
					if (user != null) {
						//不可重复添加
						if(!(isRepeat.containsKey(user.getId()))){
							isRepeat.put(user.getId(), user.getId());
							userList.add(user);
						}
					} else {
						if (NotRigId == "") {
							NotRigId = id;
						} else {
							NotRigId = NotRigId + "," + id;
						}
					}
				}
			}
			if (StringUtil.isEmpty(NotRigPhone) && StringUtil.isEmpty(NotRigId)) {
				String tempName = "";
				if (StringUtil.isEmpty(orgGroup.getName())) {
					for (int i = 0; i < userList.size(); i++) {
						if (StringUtil.isEmpty(userList.get(i).getName())) {
							if (tempName == "") {
								tempName = userList.get(i).getPhone();
							} else {
								tempName = tempName + "," + userList.get(i).getPhone();
							}
						} else {
							if (tempName == "") {
								tempName = userList.get(i).getName();
							} else {
								tempName = tempName + "," + userList.get(i).getName();
							}
						}
					}
					if (tempName.length() > 50 && tempName != "") {
						orgGroup.setName(tempName.substring(0, 47) + "...");
					} else {
						orgGroup.setName(tempName);
					}
				}
				//设置群组类别
				orgGroup.setWorkGroup(workGroup);
				// 創建群組
				if (orgGroup.getOwner() == null) {
					orgGroup.setOwner(ClientUtil.getUserId());
				}
				if (orgGroup.getApproval() == null) {
					orgGroup.setApproval(1);
				}
				if (orgGroup.getIspublic() == null) {
					orgGroup.setIspublic(1);
				}
				if (orgGroup.getMaxusers() == null) {
					orgGroup.setMaxusers(100);
				}
				if (orgGroup.getAllowinvites() == null) {
					orgGroup.setAllowinvites(0);
				}

				// this.orgGroupdao.save(orgGroup);
				
				ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();
				dataObjectNode.put("groupname", orgGroup.getName());
				if (StringUtil.isNotEmpty(orgGroup.getDescription())) {
					dataObjectNode.put("desc", orgGroup.getDescription());
				}
				dataObjectNode.put("owner", orgGroup.getOwner());
				dataObjectNode.put("approval", orgGroup.getApproval() == null ? true : StringUtil.parseToBoolean(orgGroup.getApproval()));
				dataObjectNode.put("public", orgGroup.getIspublic() == null ? true : StringUtil.parseToBoolean(orgGroup.getIspublic()));
				dataObjectNode.put("allowinvites", orgGroup.getAllowinvites() == null ? false : StringUtil.parseToBoolean(orgGroup.getAllowinvites()));
				dataObjectNode.put("maxusers", orgGroup.getMaxusers() == null ? 100 : orgGroup.getMaxusers());
				ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
				// 添加群组成员
				for (int i = 0; i < userList.size(); i++) {
					OrgGroupMemberEntity orgGroupMemberEntity = new OrgGroupMemberEntity();
					orgGroupMemberEntity.setId(UUIDGenerator.generate());
					orgGroupMemberEntity.setUserId(userList.get(i).getId());
					// orgGroupMemberEntity.setGroupId(orgGroup.getId());
					orgGroupMemberEntity.setIsOwner(0);
					OrgGroupMemberList.add(orgGroupMemberEntity);
					// this.orgGroupMemberService.save(orgGroupMemberEntity);
					arrayNode.add(userList.get(i).getId());
				}
				
				//WorkGroup为2则为角色群
/*				if(orgGroup.getWorkGroup() == 2){
					String orgId = ClientUtil.getUserEntity().getOrgIds();
					//1、先生成角色插入角色表
					RoleEntity role = new RoleEntity();
					role.setOrgId(orgId);
					role.setDefinedFlag(2);
					role.setName(orgGroup.getName().replace(" ", ""));
					String name = role.getName().replaceAll("\\pP|\\pS", "");
					String code = PinyinUtil.converterToFirstSpell(name);
					role.setCode(this.getCode(code));
					String roleId = this.roleService.save(role);
					
					//2、往员工角色表插入数据

					List<UserRoleEntity> userRoleList = new ArrayList<UserRoleEntity>();
					//添加群主
					UserRoleEntity ownerRole = new UserRoleEntity();
					ownerRole.setUser(Owner);
					ownerRole.setRole(role);
					ownerRole.setOrgId(orgId);
					userRoleList.add(ownerRole);
					//添加群组成员
					for(UserEntity roleUser : userList){
						UserRoleEntity userRole = new UserRoleEntity();
						userRole.setUser(roleUser);
						userRole.setRole(role);
						userRole.setOrgId(orgId);
						userRoleList.add(userRole);
					}
					//保存员工角色表数据
					this.userRoleService.batchSave(userRoleList);
					
					//3、设置群组的机构id和角色id
					orgGroup.setOrgId(orgId);
					orgGroup.setRoleId(roleId);
					
				}*/
/*				// 添加群组群主
				UserEntity user = this.userService.get(ClientUtil.getUserId());
				if(!(isRepeat.containsKey(user.getId()))){
					OrgGroupMemberEntity orgGroupMemberEntity = new OrgGroupMemberEntity();
					orgGroupMemberEntity.setId(UUIDGenerator.generate());
					orgGroupMemberEntity.setUserId(user.getId());
					// orgGroupMemberEntity.setGroupId(orgGroup.getId());
					orgGroupMemberEntity.setIsOwner(1);
					// this.orgGroupMemberService.save(orgGroupMemberEntity);
					OrgGroupMemberList.add(orgGroupMemberEntity);
					//arrayNode.add(ClientUtil.getUserId());
				}*/
				
				//不能小于3人的群组
//				if(OrgGroupMemberList.size()<3){
//					return null;
//				}
				
				dataObjectNode.put("members", arrayNode);
				try {
					ObjectNode result = HXUtils.createChatGroups(dataObjectNode);
					if (result.get("statusCode").intValue() == 200) {
						groupid = result.get("data").get("groupid").asText();
						orgGroup.setId(groupid);
						this.orgGroupdao.save(orgGroup);
						// orgGroup.setEmGroupId(groupid);
						// this.orgGroupdao.updateEntitie(orgGroup);
						for (int i = 0; i < OrgGroupMemberList.size(); i++) {
							OrgGroupMemberList.get(i).setGroupId(groupid);
							this.orgGroupMemberService.save(OrgGroupMemberList.get(i));
						}
						j.setObj(orgGroup);

					} else {
						this.orgGroupdao.delete(orgGroup);
						ExceptionUtil.throwException("IM服务器请求失败");
					}

					j.setMsg("工作组创建成功！");
					j.setSuccess(true);

				} catch (Exception e) {
					// 删除环信表记录
					ObjectNode result = HXUtils.deleteChatGroups(groupid);
					if (result.get("statusCode").intValue() == 200) {
					} else {
						ExceptionUtil.throwException("IM服务器请求失败,操作:删除环信group");
					}
				}
			} else {
				j.setMsg("创建工作组組失败,原因:有下列用户未注册" + NotRigPhone + "," + NotRigId);
				j.setSuccess(false);
			}
		} else {
			j.setMsg("创建工作组組失败,原因:未添加群员");
			j.setSuccess(false);
		}
		return j;
	}

	@Override
	public AjaxJson addUser(String groupId, String userIds) throws Exception {
		AjaxJson ajaxResult = new AjaxJson();
		String register = "";  //判断是否已是群成员
		String msg = "添加用户成功";
		String[] userIdsArray = userIds.split(",");
		ObjectNode result = JsonNodeFactory.instance.objectNode();
		if (userIdsArray.length > 1) {
			// 添加人数大于1个
			ArrayNode usernames = JsonNodeFactory.instance.arrayNode();
			ObjectNode usernamesNode = JsonNodeFactory.instance.objectNode();
			List<OrgGroupMemberEntity> list = new ArrayList<OrgGroupMemberEntity>();
			List<UserEntity> userList = new ArrayList<UserEntity>();
			for (String userId : userIdsArray) {
				UserEntity user = userService.get(userId);
				//判断用户是否存在
				if (user != null) {
					//判断所添加用户是否已经是群成员
					OrgGroupMemberEntity groupMem = this.orgGroupMemberService.getOrgGroMem(groupId, userId);
					if (groupMem == null) {
						OrgGroupMemberEntity groupMember = new OrgGroupMemberEntity();
						groupMember.setGroupId(groupId);
						groupMember.setUserId(userId);
						groupMember.setIsOwner(0);
						list.add(groupMember);
						userList.add(user);
						usernames.add(userId);
					} else {
						register = register + userId + ".";
					}
				}
			}

			if (list.size() == 0) {
				msg = "添加用户失败，有下列用户在群组中已存在，无法重复添加:" + register;
			}
			if (list.size() > 0) {
				usernamesNode.put("usernames", usernames);
				result = HXUtils.addUsersToGroupBatch(groupId, usernamesNode);

				if (result.get("statusCode").intValue() == 200) {
					//判断群组是否是角色群组,是则添加成员到成员角色表中
					OrgGroupEntity orgGroup = this.get(groupId);
/*					if(orgGroup.getWorkGroup() == 2){
						RoleEntity role = this.roleService.get(orgGroup.getRoleId());
						for(UserEntity user : userList){
							UserRoleEntity userRole = new UserRoleEntity();
							userRole.setUser(user);
							userRole.setRole(role);
							userRole.setOrgId(groupId);
							this.userRoleService.save(userRole);
						}
					}*/
					//判断是否有无法添加的成员
					if(register != null){
						msg = "添加群组成员成功，以下成员已是群组成员，无法重复添加"+register;
					}
					ajaxResult.setSuccess(true);
					orgGroupMemberService.batchSave(list);
				} else {
					ExceptionUtil.throwException("IM服务器请求失败");
				}
				if (register != "") {
					msg = "添加用户成功，但有下列用户在群组中已存在，无法重复添加:" + register;
				}
			}
		} else {
			// 添加人数为1
			UserEntity user = userService.get(userIds);
			if (user != null) {
				OrgGroupMemberEntity groupMember = this.orgGroupMemberService.getOrgGroMem(groupId, userIds);
				if (groupMember == null) {
					OrgGroupMemberEntity groupMb = new OrgGroupMemberEntity();
					groupMb.setGroupId(groupId);
					groupMb.setUserId(userIds);
					groupMb.setIsOwner(0);
					
					result = HXUtils.addUserToGroup(groupId, userIds);

					if (result.get("statusCode").intValue() == 200) {
						//判断群组是否是角色群组,是则添加成员到成员角色表中
						OrgGroupEntity orgGroup = this.get(groupId);
/*						if(orgGroup.getWorkGroup() == 2){
							RoleEntity role = this.roleService.get(orgGroup.getRoleId());
							UserRoleEntity userRole = new UserRoleEntity();
							userRole.setUser(user);
							userRole.setRole(role);
							userRole.setOrgId(groupId);
							this.userRoleService.save(userRole);
						}*/
						orgGroupMemberService.save(groupMb);
						ajaxResult.setSuccess(true);
					} else {
						ExceptionUtil.throwException("IM服务器请求失败");
					}
				} else {
					ajaxResult.setSuccess(false);
					msg = "添加用户失败，用户已是该群组成员，无法重复添加";
				}
			}else{
				ajaxResult.setSuccess(false);
				msg = "添加用户失败，用户不存在";
			}

		}
		ajaxResult.setMsg(msg);
		return ajaxResult;
	}

	@Override
	public AjaxJson addUser(String groupId, String phones, boolean flag) throws Exception {
		AjaxJson ajaxResult = new AjaxJson();
		String register = "";
		String msg = "工作组成员添加成功！";
		String[] phonesArray = phones.split(",");
		ObjectNode result = JsonNodeFactory.instance.objectNode();
		if (phonesArray.length > 1) {
			// 添加人数大于1个
			ArrayNode usernames = JsonNodeFactory.instance.arrayNode();
			ObjectNode usernamesNode = JsonNodeFactory.instance.objectNode();
			List<OrgGroupMemberEntity> list = new ArrayList<OrgGroupMemberEntity>();
			List<UserEntity> userList = new ArrayList<UserEntity>();
			for (String phone : phonesArray) {
				UserEntity user = this.userService.getUser(phone, true);
				//判断用户是否存在
				if (user != null) {
					OrgGroupMemberEntity groupMem = this.orgGroupMemberService.getOrgGroMem(groupId, user.getId());
					if (groupMem == null) {
						OrgGroupMemberEntity groupMember = new OrgGroupMemberEntity();
						groupMember.setGroupId(groupId);
						groupMember.setUserId(user.getId());
						groupMember.setIsOwner(0);
						list.add(groupMember);
						userList.add(user);
						usernames.add(user.getId());
					} else {
						register = register + phone + ".";
					}
				}
			}

			if (list.size() == 0) {
				ajaxResult.setSuccess(false);
				msg = "工作组成员添加失败，有下列用户在群组中已存在，无法重复添加:" + register;
			}
			if (list.size() > 0) {
				
				usernamesNode.put("usernames", usernames);
				result = HXUtils.addUsersToGroupBatch(groupId, usernamesNode);

				if (result.get("statusCode").intValue() == 200) {
					//判断群组是否是角色群组,是则添加成员到成员角色表中
					OrgGroupEntity orgGroup = this.get(groupId);
					/*if(orgGroup.getWorkGroup() == 2){
						RoleEntity role = this.roleService.get(orgGroup.getRoleId());
						for(UserEntity user : userList){
							UserRoleEntity userRole = new UserRoleEntity();
							userRole.setUser(user);
							userRole.setRole(role);
							userRole.setOrgId(groupId);
							this.userRoleService.save(userRole);
						}
					}*/
					ajaxResult.setSuccess(true);
					//判断是否有无法添加的成员
					if(register != ""){
						msg = "工作组成员添加成功！，以下成员已是群组成员，无法重复添加"+register;
					}
					orgGroupMemberService.batchSave(list);
				} else {
					ExceptionUtil.throwException("IM服务器请求失败");
				}
			}
		} else {
			// 添加人数为1
			UserEntity user = this.userService.getUser(phones, true);
			//判断用户是否存在
			if (user != null) {
				OrgGroupMemberEntity groupMem = this.orgGroupMemberService.getOrgGroMem(groupId, user.getId());
				if (groupMem == null) {
					OrgGroupMemberEntity groupMember = new OrgGroupMemberEntity();
					groupMember.setGroupId(groupId);
					groupMember.setUserId(user.getId());
					groupMember.setIsOwner(0);

					
					result = HXUtils.addUserToGroup(groupId, user.getId());
					if (result.get("statusCode").intValue() == 200) {
						//判断群组是否是角色群组,是则添加成员到成员角色表中
						OrgGroupEntity orgGroup = this.get(groupId);
	/*					if(orgGroup.getWorkGroup() == 2){
							RoleEntity role = this.roleService.get(orgGroup.getRoleId());
							UserRoleEntity userRole = new UserRoleEntity();
							userRole.setUser(user);
							userRole.setRole(role);
							userRole.setOrgId(groupId);
							this.userRoleService.save(userRole);
						}*/
						orgGroupMemberService.save(groupMember);
						ajaxResult.setSuccess(true);
					} else {
						ExceptionUtil.throwException("IM服务器请求失败");
					}
				} else {
					ajaxResult.setSuccess(false);
					msg = "工作组成员添加失败，用户已是该群组成员，无法重复添加";
				}
			}else{
				ajaxResult.setSuccess(false);
				msg = "工作组成员添加失败，用户不存在";
			}
		}

		ajaxResult.setMsg(msg);
		return ajaxResult;
	}

	@Override
	public AjaxJson deleteUser(String groupId, String userIds) throws Exception {
		AjaxJson AjaxResult = new AjaxJson();
		// orgGroupMemberService.executeSql("DELETE FROM t_org_group_member WHERE groupId=? AND userId=? ",
		// groupId, userIds);
		String[] userIdsArray = userIds.split(",");
		OrgGroupEntity OrgGroup = this.orgGroupdao.get(groupId);
		List<Map<String, Object>>  orgMemberList = this.orgGroupMemberService.queryGroupUsers(groupId);
		if (orgMemberList.size() - userIdsArray.length >= 1) {
			if (OrgGroup != null) {
				ObjectNode result = HXUtils.deleteUserFromGroup(OrgGroup.getId(), userIds);
				if (result.get("statusCode").intValue() == 200) {

					for (String userId : userIdsArray) {
						orgGroupMemberService.executeSql("DELETE FROM t_org_group_member WHERE groupId=? AND userId=? ", groupId, userId);
						//如果WorkGroup为2则删除员工角色表
/*						if(OrgGroup.getWorkGroup() == 2){
							userRoleService.deleteByUseAndRole(userId, OrgGroup.getRoleId());
						}*/
					}
					AjaxResult.setSuccess(true);
					AjaxResult.setMsg("工作组成员移除成功！");
				} else {
					ExceptionUtil.throwException("IM服务器请求失败");
				}
			}
		}else{
			AjaxResult.setSuccess(false);
			AjaxResult.setMsg("群组人数不能少于2人");
		}
		return AjaxResult;
	}

	@Override
	public void updateAndProcessHX(OrgGroupEntity orgGroup) throws Exception {
		this.update(orgGroup, orgGroup.getId());
		ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();
		dataObjectNode.put("groupid", orgGroup.getId());
		if (StringUtil.isNotEmpty(orgGroup.getName())) {
			dataObjectNode.put("groupname", orgGroup.getName());
		}
		if (StringUtil.isNotEmpty(orgGroup.getDescription())) {
			dataObjectNode.put("description", orgGroup.getDescription());
		}
		if (StringUtil.isNotEmpty(orgGroup.getMaxusers())) {
			dataObjectNode.put("maxusers", orgGroup.getMaxusers());
		}
		/*
		 * if (StringUtil.isNotEmpty(orgGroup.getAllowinvites())) {
		 * dataObjectNode.put("allowinvites", orgGroup.getAllowinvites()); }
		 */

		ObjectNode result = HXUtils.updateChatGroups(dataObjectNode);
		if (result.get("statusCode").intValue() == 200) {
		} else {
			ExceptionUtil.throwException("IM服务器请求失败");
		}
	}

	@Override
	public void deleteAndProcessHX(String groupId) throws Exception {
		// orgGroupdao.findForJdbc(sql, objs)
		OrgGroupEntity orgGroup = this.orgGroupdao.get(groupId);
		//判断WorkGroup为2的情况
/*		if(orgGroup.getWorkGroup() == 2){
			//删除员工角色表
			this.userRoleService.deleteByRoleId(orgGroup.getRoleId());
			//删除角色表
			this.roleService.singleDelete(orgGroup.getRoleId());
		}*/
		this.orgGroupdao.deleteEntityById(groupId);
		this.orgGroupMemberService.deleteOrgGroMem(groupId);
		
		ObjectNode result = HXUtils.deleteChatGroups(groupId);
		if (result.get("statusCode").intValue() == 200) {
		} else {
			ExceptionUtil.throwException("IM服务器请求失败");
		}
	}

	@Override
	public List<Map<String, Object>> queryUserGroupsByPage(String userId,int row,int page) {
	/*	HqlQuery q = new HqlQuery();
		q.setQueryString("SELECT a.*,c.name as ownerName FROM t_org_group a,t_org_group_member b,t_org_user c WHERE a.id=b.groupId AND b.userId='"+userId+"' and workGroup = 1 and c.id=a.owner ORDER BY a.createTime DESC");
		q.setCurPage(page);
		q.setPageSize(row);
		PageList p = orgGroupdao.getPageList(q, false);*/
		String sql = "SELECT DISTINCT a.*,c.name as ownerName FROM t_org_group a,t_org_group_member b,t_org_user c WHERE a.id=b.groupId AND b.userId='"+userId+"' and workGroup = 1 and c.id=a.owner ORDER BY a.createTime DESC";
		
		//String sql = "SELECT * FROM t_org_group a,t_org_group_member b WHERE a.id=b.groupId AND b.userId='"+userId+"' ";
		//sql = JdbcDao.jeecgCreatePageSql(sql, page, row);
		return this.orgGroupdao.findForJdbc(sql, page, row);
	}

	@Override
	public List<Map<String, Object>> queryUsers(String userId) {
		// TODO Auto-generated method stub
		// String hql =
		// "SELECT id,name,phone,portrait,sortKey,searchKey from t_org_user t WHERE id in (SELECT DISTINCT(c.userId) from t_org_group_member c where c.groupId in (SELECT a.id FROM t_org_group a,t_org_group_member b WHERE a.id=b.groupId AND b.userId=?)) GROUP BY sortKey";
		String sql = "SELECT DISTINCT u.id,u.name,u.phone,u.portrait,u.sortKey,u.searchKey FROM t_org_group_member gm,(SELECT groupId FROM t_org_group_member WHERE userId=?) a,t_org_user u WHERE gm.groupId=a.groupId AND gm.userId=u.id ORDER BY sortKey";
		return this.orgGroupdao.findForJdbc(sql, userId);
	}
	
	@Override
	public List<Map<String, Object>> queryUsersByWork(String userId) {
		String sql ="SELECT DISTINCT u.id,u.name,u.phone,u.portrait,u.sortKey,u.searchKey FROM t_org_group_member gm,(SELECT groupId FROM t_org_group_member WHERE userId='"+userId+"') a,t_org_user u,t_org_group t WHERE gm.groupId=a.groupId AND gm.userId=u.id and t.id = gm.groupId and t.workGroup = 1  ORDER BY sortKey";
		return this.orgGroupdao.findForJdbc(sql);
	}

	@Override
	public List<Map<String, Object>> queryUserFromGroup(String searchKey, String groupId) throws Exception {
		// TODO Auto-generated method stub
		String hql = "select b.id,b.name,b.phone,b.portrait,b.searchKey,b.sortKey "
				+ "from t_org_group_member a,(SELECT id,name,phone,portrait,sortKey,searchKey FROM `t_org_user` t where t.searchKey like '%" + searchKey
				+ "%') b" + " where a.userId = b.id and a.groupId = ?";
		return this.orgGroupdao.findForJdbc(hql, groupId);
	}

	@Override
	public Map<String, Object> getUserOrg(String groupId) {
		String sql = "from OrgGroupEntity where id=?";
		List<Map<String, Object>> OrgGroupMember = this.orgGroupMemberService.queryGroupUsers(groupId);
		OrgGroupEntity OrgGroup = this.orgGroupdao.findUniqueByHql(sql, groupId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("OrgGroup", OrgGroup);
		map.put("OrgGroupMember", OrgGroupMember);
		return map;
	}

	@Override
	public List<Map<String, Object>> queryUserGroups(String userId) throws Exception {
		String sql = "SELECT DISTINCT a.*,c.name as ownerName FROM t_org_group a,t_org_group_member b,t_org_user c WHERE a.id=b.groupId AND b.userId=? and workGroup = 1 and c.id=a.owner ORDER BY a.createTime DESC";
		return this.orgGroupdao.findForJdbc(sql, userId);
	}

	@Override
	public List<Map<String, Object>> queryUsersByLikeKey(String userId,String key) {
		String sql ="SELECT DISTINCT u.id,u.name,u.phone,u.portrait,u.sortKey,u.searchKey FROM t_org_group_member gm,(SELECT groupId FROM t_org_group_member WHERE userId='"+userId+"') a,t_org_user u,t_org_group t WHERE gm.groupId=a.groupId AND gm.userId=u.id and t.id = gm.groupId and t.workGroup = 1 and u.id != '"+userId+"' and u.searchKey like '%"+key+"%' ORDER BY sortKey";
		return this.orgGroupdao.findForJdbc(sql);
	}
	
	//判断当前code是否与数据库role表中code一样，一样则改，不一样则返回code
	public String getCode(String code) throws BusinessException {

		for (int i = 0; i < 1; i++) {
			RoleEntity isExist = this.roleService.queryRoleByCode(code);
			if (isExist != null) {
				if (isExist.getCode().equals(code)) {
					String number = isExist.getCode().replaceAll("\\D+", "");
					if (StringUtil.isNotEmpty(number)) {
						int newNumner = Integer.parseInt(number) + 1;
						code = code.replaceAll("\\d+", "");
						code = code + newNumner;
						i = -1;
					} else {
						code = code + "1";
						i = -1;
					}
				}
			}
		}
		return code;
	}

	@Override
	public void deleteAndProcessHXByRoleId(String roleId) throws Exception {
		String hql = "from OrgGroupEntity where roleId=?";
		OrgGroupEntity orgGroup = this.orgGroupdao.findUniqueByHql(hql, roleId);
		if(orgGroup != null){
			this.orgGroupdao.delete(orgGroup);
			this.orgGroupMemberService.deleteOrgGroMem(orgGroup.getId());
			
			ObjectNode result = HXUtils.deleteChatGroups(orgGroup.getId());
			if (result.get("statusCode").intValue() == 200) {
			} else {
				ExceptionUtil.throwException("IM服务器请求失败");
			}
		}
	}

}
