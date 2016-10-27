package com.xplatform.base.orgnaization.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.common.UploadFile;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.MapKit;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.PasswordUtil;
import com.xplatform.base.framework.core.util.PinyinUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.pdf.HttpUtils;
import com.xplatform.base.framework.core.util.sms.JXTSmsClient;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.orgnaization.service.OrgnaizationService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.role.service.RoleService;
import com.xplatform.base.orgnaization.user.dao.UserDao;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.entity.UserOrgEntity;
import com.xplatform.base.orgnaization.user.entity.UserPasswordEntity;
import com.xplatform.base.orgnaization.user.entity.UserRoleEntity;
import com.xplatform.base.orgnaization.user.entity.UserVerifycodeEntity;
import com.xplatform.base.orgnaization.user.mybatis.dao.UserMybatisDao;
import com.xplatform.base.orgnaization.user.service.UserOrgService;
import com.xplatform.base.orgnaization.user.service.UserPasswordService;
import com.xplatform.base.orgnaization.user.service.UserService;
import com.xplatform.base.orgnaization.user.service.UserVerifycodeService;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.platform.common.utils.HXUtils;

/**
 * 
 * description :用户service实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:30:12
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiehs 2014年5月24日 下午12:30:12
 *
 */
@Service("userService")
public class UserServiceImpl implements UserService {
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Resource
	private UserDao userDao;

	@Resource
	private BaseService baseService;
	@Resource
	private UserMybatisDao userMybatisDao;
	@Resource
	private UserPasswordService userPasswordService;
	@Resource
	private OrgnaizationService orgnaizationService;
	@Resource
	private UserVerifycodeService userVerifycodeService;
	@Resource
	private RoleService roleService;
	@Resource
	private AuthorityService authorityService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private UserOrgService userOrgService;
	
	@Override
	public String save(UserEntity user) throws BusinessException {
		String pk = "";
		try {
			user.setFlag("1");// 设置激活
			user.setLoginErrorTimes(0);
			if (StringUtil.isEmpty(user.getName())) {
				user.setName(user.getPhone());
				user.setSearchKey(user.getPhone());
				user.setSortKey("#");
			} else {
				String nameKeyWord = PinyinUtil.converterToSpell(user.getName()) + "," + PinyinUtil.converterToFirstSpell(user.getName());
				user.setSearchKey(user.getName() + "," + user.getPhone() + "," + nameKeyWord);
				if ((nameKeyWord.charAt(0) >= 'A' && nameKeyWord.charAt(0) <= 'Z') || (nameKeyWord.charAt(0) >= 'a' && nameKeyWord.charAt(0) <= 'z')) {
					user.setSortKey(nameKeyWord.substring(0, 1).toUpperCase());
				} else {
					user.setSortKey("#");
				}

			}

			pk = this.userDao.addUser(user);
		} catch (Exception e) {
			logger.error("用户保存失败");
			throw new BusinessException("用户保存失败");
		}
		logger.info("用户保存成功");
		return pk;
	}

	@Override
	public void delete(String id) throws BusinessException {
		try {
			UserEntity user = userDao.getUser(id);
			user.setFlag("N");
			userDao.updateUser(user);
		} catch (Exception e) {
			logger.error("用户删除失败");
			throw new BusinessException("用户删除失败");
		}
		logger.info("用户删除成功");
	}

	@Override
	public void batchDelete(String ids) throws BusinessException {
		if (StringUtil.isNotEmpty(ids)) {
			String[] idArr = StringUtil.split(ids, ",");
			for (String id : idArr) {
				UserEntity user = userDao.getUser(id);
				user.setFlag("N");
				userDao.updateUser(user);
				// this.delete(id);
			}
		}
		logger.info("用户批量删除成功");
	}

	@Override
	public void update(UserEntity user) throws BusinessException {
		try {
			UserEntity oldEntity = get(user.getId());
			MyBeanUtils.copyBeanNotNull2Bean(user, oldEntity);
			this.userDao.updateUser(oldEntity);
		} catch (Exception e) {
			logger.error("用户更新失败");
			throw new BusinessException("用户更新失败");
		}
		logger.info("用户更新成功");
	}

	@Override
	public UserEntity get(String id) {
		UserEntity User = null;
		try {
			User = this.userDao.getUser(id);
		} catch (Exception e) {
			logger.error("用户获取失败");
		}
		logger.info("用户获取成功");
		return User;
	}

	@Override
	public List<UserEntity> queryList() {
		List<UserEntity> UserList = new ArrayList<UserEntity>();
		try {
			UserList = this.userDao.queryUserList();
		} catch (Exception e) {
			logger.error("用户获取列表失败");
		}
		logger.info("用户获取列表成功");
		return UserList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
		try {
			this.userDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("用户获取分页列表失败");
		}
		logger.info("用户获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param, String propertyName) {
		logger.info(propertyName + "字段唯一校验");
		return this.baseService.isUnique(UserEntity.class, param, propertyName);
	}

	@Override
	public void initPwd(String userId) throws BusinessException {
		UserEntity user = this.get(userId);
		if (user == null) {
			throw new BusinessException("用户不存在");
		}
		try {
			// user.setLoginNum(0);
			user.setLoginErrorTimes(0);
			this.update(user);// 初始化密码时，登录错误次数清零

			String pwd = PasswordUtil.encrypt(user.getUserName(), "123456", PasswordUtil.getStaticSalt());
			UserPasswordEntity userPassword = new UserPasswordEntity();
			userPassword.setUser(user);
			userPassword.setPassword(pwd);
			userPasswordService.update(userPassword);

			// user.setPassword(pwd);
			// this.userDao.updateUser(user);
		} catch (BusinessException e) {
			throw new BusinessException("用户密码初始化失败");
		}
	}

	/**
	 * 检查用户是否存在，不验证密码
	 * 
	 * @author xiehs
	 * @createtime 2014年6月5日 上午10:32:01
	 * @Decription
	 *
	 * @param user
	 * @return
	 */
	public UserEntity checkUserExits(String param) {
		String hql = "from UserEntity u where (u.userName = ? or u.email = ? or u.phone=? or id=?) ";
		UserEntity user = userDao.findUniqueByHql(hql, new Object[] { param, param, param, param });
		return user;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public void setUserMybatisDao(UserMybatisDao userMybatisDao) {
		this.userMybatisDao = userMybatisDao;
	}

	@Override
	public UserEntity getUserByName(String userName) {
		List<UserEntity> list = userDao.findByProperty(UserEntity.class, "userName", userName);
		if (list.size() > 0) {
			UserEntity user = list.get(0);
			if (user.getFlag().equals("Y")) {
				return user;
			}

		} else {
			list = userDao.findByProperty(UserEntity.class, "email", userName);
			if (list.size() > 0) {
				UserEntity user = list.get(0);
				if (user.getFlag().equals("Y")) {
					return user;
				}
			}
		}
		return null;
	}

	public void setUserPasswordService(UserPasswordService userPasswordService) {
		this.userPasswordService = userPasswordService;
	}

	@Override
	public UserEntity getUserByNameAndPwd(UserEntity user) {
		List<UserEntity> list = new ArrayList<UserEntity>();
		List<UserEntity> userNamelist = userDao.findByProperty(UserEntity.class, "userName", user.getUserName());// 根据用户名查找用户
		list.addAll(userNamelist);
		List<UserEntity> emaillist = userDao.findByProperty(UserEntity.class, "email", user.getUserName());// 根据邮箱查找用户
		list.addAll(emaillist);
		/*
		 * List<EmployeeEntity> empList =
		 * userDao.findByProperty(EmployeeEntity.class, "name",
		 * user.getUserName());//根据中文名和手机查出用户
		 */
		/*
		 * List<EmployeeEntity> empList =
		 * userDao.findByProperty(EmployeeEntity.class, "phone",
		 * user.getUserName()); for(EmployeeEntity e : empList){
		 * list.addAll(this.userDao.findByProperty(UserEntity.class,
		 * "userTypeId", e.getId())); }
		 */
		if (list.size() == 1) {
			return list.get(0);// 当只有一条记录时直接返回
		} else if (list.size() > 1) {
			for (UserEntity u : list) {
				String password = PasswordUtil.encrypt(u.getUserName(), user.getPassword(), PasswordUtil.getStaticSalt());
				String hql = "from UserEntity u " + "where (u.userName = ? or u.email = ?) "
						+ "and (select up.password from UserPasswordEntity up where up.user.id = u.id and up.flag = ? ) = ? ";

				List<UserEntity> users = userDao.findHql(hql, new Object[] { u.getUserName(), u.getUserName(), "Y", password });
				if (users != null && users.size() > 0) {
					return users.get(0);// 多条记录时，根据用户名和密码正确返回
				}
			}
			return list.get(0);// 当用户名匹配多条记录，密码又匹配不上时取第一条记录（用户用户名锁定等）（有bug待解决）
		}
		return null;
	}

	@Override
	public void branchSave(List list) throws BusinessException {
		userDao.batchSave(list);
	}

	@Override
	public List<String> getUserOrgTreeIndex(Map<String, String> param) {
		// TODO Auto-generated method stub
		List<String> ls = new ArrayList<String>();
		List<Map<String, String>> list = userMybatisDao.getUserOrgTreeIndex(param);
		for (Map<String, String> l : list) {
			ls.add(l.get("treeIndex"));
		}
		return ls;
	}

	@Override
	public List<String> getManagerOrgList(Map<String, String> param) {
		// TODO Auto-generated method stub
		List<String> ls = new ArrayList<String>();
		List<Map<String, String>> list = userMybatisDao.getUserOrgTreeIndex(param);
		for (Map<String, String> l : list) {
			String orgIndex = l.get("treeIndex");
			if (StringUtil.isNotEmpty(orgIndex)) {
				String[] arr = orgIndex.split(",");
				for (String orgStr : arr) {
					if (!ls.contains(orgStr)) {
						ls.add("'" + orgStr + "'");
					}
				}
			}
		}
		return ls;
	}

	@Override
	public Object uploadFile(UploadFile uploadFile) {
		return userDao.uploadFile(uploadFile);
	}

	public List<UserEntity> findUserByPhone(String phone) {
		String hql = "FROM UserEntity WHERE phone=?";
		List<UserEntity> user = userDao.findHql(hql, new Object[] { phone });
		return user;
	}

	@Override
	public Boolean checkUserExsits(String phone) {
		List<UserEntity> users = findUserByPhone(phone);
		if (users.size() > 0) {
			// 未激活,则可继续注册使用
			if ("2".equals(users.get(0).getFlag())) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public void sendVerifyCode(String phone) {
		Random random = new Random();
		int k = random.nextInt(9000) + 1000;
		String num = String.valueOf(k);
		List<UserEntity> users = findUserByPhone(phone);
		UserVerifycodeEntity userVerifycodeEntity = new UserVerifycodeEntity();
		if (users.size() > 0) {
			UserEntity user = users.get(0);
			user.setVerifyCode(num);
			user.setVerifySendTime(new Date());
			user.setPhone(phone);
			userDao.updateEntitie(user);
		} else {
			UserEntity user = new UserEntity();
			user.setPhone(phone);
			user.setVerifyCode(num);
			user.setVerifySendTime(new Date());
			user.setFlag("2");
			userDao.save(user);
		}
		JXTSmsClient.sendSms("spreadingwind", "abc123", phone, "您的注册验证码是" + num, "");
	}

	@Override
	public Boolean checkVerifyCode(String phone, String verifyCode) {
		String hql = "FROM UserEntity WHERE phone=? and verifyCode=?";
		UserEntity user = userDao.findUniqueByHql(hql, new Object[] { phone, verifyCode });
		if (user != null) {
			Date verifySendTime = user.getVerifySendTime();
			Date nowTime = new Date();
			long distanceTime = nowTime.getTime() - verifySendTime.getTime();
			// TODO 验证码一分钟超时(可以设成可配)
			if (distanceTime / (60 * 1000) > 1) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public void doRegisterUser(String phone, String password) throws BusinessException {
		List<UserEntity> users = findUserByPhone(phone);
		if (users.size() > 0) {
			// 1. 保存用户表信息
			UserEntity user = users.get(0);
			password = PasswordUtil.encrypt(phone, password, PasswordUtil.getStaticSalt());
			user.setPassword(password);
			user.setFlag("1");
			user.setVerifySendTime(null);
			user.setVerifyCode(null);
			user.setRegisterTime(new Date());
			user.setLoginErrorTimes(0);
			userDao.updateEntitie(user);
			// 2. 保存用户密码表信息
			UserPasswordEntity userPassword = new UserPasswordEntity();
			userPassword.setUser(user);
			userPassword.setPassword(password);
			userPasswordService.save(userPassword);

			// 3. 分配用户普通会员角色
			RoleEntity role = roleService.findUniqueByProperty(RoleEntity.class, "code", RoleEntity.MEMBER);
			roleService.saveUserRole(user.getId(), role.getId(), null);
			// 4. 注册环信

		}
	}

	@Override
	public void deleteEntity(String id) {
		userDao.deleteUser(id);
	}

	@Override
	public void updateUserLastInstitution(String userId, String institutionId) {
		String hql = "UPDATE UserEntity u SET u.lastInstitution='" + institutionId + "' WHERE u.id='" + userId + "'";
		userDao.executeHql(hql);
	}

	@Override
	public List<UserEntity> queryUsersByRole(String roleId) {
		String hql = "SELECT ur.user FROM UserRoleEntity ur WHERE ur.role.id=?";
		return userDao.findHql(hql, new Object[] { roleId });
	}

	@Override
	public boolean getExistPhone(String phone) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("phone", phone);
		List<UserEntity> users = this.userDao.findByObjectPropertys(UserEntity.class, param);
		if (users != null && users.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<UserEntity> queryUsersByLikeName(String name) {
		return userDao.findHql("FROM UserEntity WHERE name like ?", "%" + name + "%");
	}

	@Override
	public String queryName(String userId) {
		Map<String, Object> result = userDao.findOneForJdbc("SELECT name FROM t_org_user WHERE id=?", userId);
		if (result != null) {
			Object obj = result.get("name");
			if (obj != null) {
				return obj.toString();
			}
		}
		return null;
	}

	@Override
	public UserEntity getUser(String phone, boolean flag) {
		// TODO Auto-generated method stub
		return this.userDao.getUser(phone, flag);
	}

	@Override
	public List<Map<Object, Object>> isRegister(String phones) throws BusinessException {
		// TODO Auto-generated method stub
		List<Map<Object, Object>> userList = new ArrayList<Map<Object, Object>>();
		if (StringUtil.isNotEmpty(phones)) {
			String[] phoneArr = StringUtil.split(phones, ",");
			for (String phone : phoneArr) {
				Map<Object, Object> map = new HashMap<Object, Object>();
				UserEntity user = this.getUser(phone, true);
				if (user != null) {
					map.put("id", user.getId());
					map.put("phone", user.getPhone());
					map.put("portrait", user.getPortrait());
					userList.add(map);
				} else {
					map.put("id", "");
					map.put("phone", phone);
					map.put("portrait", "");
					userList.add(map);
				}
			}
		}
		return userList;
	}

	@Override
	public void doChangeNickname(String userId, String name) throws Exception {
		UserEntity user = this.get(userId);
		user.setName(name);
		user.setSearchKey(user.getName() + "," + user.getPhone() + "," + PinyinUtil.converterToSpell(user.getName()) + ","
				+ PinyinUtil.converterToFirstSpell(user.getName()));
		String nameKeyWord = PinyinUtil.converterToFirstSpell(user.getName());
		if ((nameKeyWord.charAt(0) >= 'A' && nameKeyWord.charAt(0) <= 'Z') || (nameKeyWord.charAt(0) >= 'a' && nameKeyWord.charAt(0) <= 'z')) {
			user.setSortKey(nameKeyWord.substring(0, 1).toUpperCase());
		} else {
			user.setSortKey("#");
		}
		userDao.executeHql("UPDATE UserEntity SET name=?,nameKeyWord=? WHERE id=?", name, user.getSearchKey(), userId);
		// this.update(user);
		ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();
		dataObjectNode.put("nickname", name);
		ObjectNode result = HXUtils.updateUserNickname(userId, dataObjectNode);
		if (result.get("statusCode").intValue() == 200) {
		} else {
			ExceptionUtil.throwException("IM服务器请求失败");
		}
	}

	@Override
	public List<Map<String, Object>> getUsersInfo(String ids) {
		return userDao.findForNamedJdbc("SELECT id,phone,portrait,name FROM t_org_user WHERE id in (:ids)",
				MapKit.create("ids", StringUtil.parseString2ListByPattern(ids)).getMap());
	}

	@Override
	public AjaxJson doForgetChangePassword(String phone, String password, String verifyCode) throws Exception {
		AjaxJson result = new AjaxJson();
		//Boolean passed = this.checkVerifyCode(phone, verifyCode);
		boolean flag = userVerifycodeService.compareVerifyCode(phone, verifyCode, "findPassword");
		if (flag) {
			UserEntity user = this.getUser(phone, true);
			if(user != null){
			String new_password = PasswordUtil.encrypt(user.getId(), password, PasswordUtil.getStaticSalt());
			
			//获取旧密码
			UserPasswordEntity lastPass = this.userPasswordService.getLastPass(user.getId());
			String old_password = lastPass.getPassword();
			
			UserPasswordEntity userPassword = new UserPasswordEntity();
			userPassword.setUser(user);
			userPassword.setPassword(new_password);
			user.setPassword(new_password);
			try {
				userPasswordService.update(userPassword);
				this.update(user);
				//调用环信修改密码接口
				ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();
				dataObjectNode.put("newpassword", new_password);
				ObjectNode resultObject = HXUtils.updateUserPassword(user.getId(),dataObjectNode);
				if (resultObject.get("statusCode").intValue() == 200) {
				} else {
					ExceptionUtil.throwException("IM服务器请求失败");
				}
			} catch (Exception e) {
				result.setMsg(ExceptionUtil.printStackTraceAndLogger(e));
			}
			// user.setPassword(new_password);
			
			//设置usertoken
			Map<String, Object> info = new HashMap<String, Object>();
			// info
			info.put("pcypassword", user.getPassword());
			info.put("usertoken", StringUtil.encodeToDes3(user.getId()) + "_" + StringUtil.encodeToDes3(user.getPassword()));
			info.put("userId", user.getId());

			// info.put("sessionId", session.getId());
			// info.put("userInfo",
			// sysUserService.getUserInfoById(ue.getId()));
			result.setSuccess(true);
			result.setObj(info);
			result.setMsg("密码修改成功！");
			}else{
				result.setMsg("用户不存在");
				result.setSuccess(false);
			}
		} else {
			result.setMsg("验证码有误");
			result.setSuccess(false);
		}
		
		return result;
	}

	@Override
	public List<Map<String,Object>> queryUsersByLikeKey(String key,String userId) {
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		try {
			//第二步：查询自己所在的团队
			List<OrgnaizationEntity> companyList=this.queryCompany(userId);
			//第三步：查询团队所在的用户
			//通过团队查寻员工
			if(companyList!=null && companyList.size()>0){
				for(OrgnaizationEntity org:companyList){
					List<UserEntity> userList=userDao.findHql("select distinct u FROM UserEntity u,UserOrgEntity uo WHERE uo.org.treeIndex like ? and u.searchKey like ?","%" + org.getId() + "%", "%" + key + "%");
					if(userList!=null && userList.size()>0){
						Map<String,Object> map=new HashMap<String,Object>();
						map.put("companyName", org.getName());
						map.put("userList", userList);
						result.add(map);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	@Override
	public List<OrgnaizationEntity> queryCompany(String userId) throws BusinessException {
		//获取所有的机构或者部门
		List<OrgnaizationEntity> orgList = sysUserService.getAllOrganizationsByUserId(ClientUtil.getUserId());
		//获取自己所有可管理的公司
		List<UserRoleEntity> orgManagerList=this.userDao.findHql("from UserRoleEntity ur where ur.role.id=? and ur.user.id=?",RoleEntity.COMPANY_ADMIN,userId);
		Set<String> orgManagerSet=new HashSet<String>();
		if(orgManagerList!=null && orgManagerList.size()>0){
			for(UserRoleEntity orgManager:orgManagerList){
				orgManagerSet.add(orgManager.getOrgId());
			}
		}
		Map<String, OrgnaizationEntity> resultObj = new HashMap<String, OrgnaizationEntity>();
		if (orgList != null && orgList.size() > 0) {
			for (OrgnaizationEntity org : orgList) {
				if (StringUtil.isNotEmpty(org.getTreeIndex())) {
					String[] idArr = StringUtil.split(org.getTreeIndex(), ",");
					for (String id : idArr) {
						org = this.orgnaizationService.get(id);
						if (org != null && !StringUtil.equals("-2", org.getId()) && !StringUtil.equals("-1", org.getId()) && StringUtil.equals("org", org.getType())) {
							if (!resultObj.containsKey(org.getId())) {
								if(orgManagerSet.contains(org.getId())){
									org.setIsManage(true);
								}
								resultObj.put(org.getId(), org);
							}
						}
					}
				}
			}
		}
		return new ArrayList<OrgnaizationEntity>(resultObj.values());
	}
	
	@Override
	public List<OrgnaizationEntity> queryHomeCompany(String userId) throws BusinessException {
		//获取所有的机构或者部门
		List<OrgnaizationEntity> orgList = sysUserService.getAllOrganizationsByUserId(ClientUtil.getUserId());
		UserEntity user=this.sysUserService.getUserById(userId);
		boolean flag=false;
		Map<String, OrgnaizationEntity> resultObj = new HashMap<String, OrgnaizationEntity>();
		if (orgList != null && orgList.size() > 0) {
			for (OrgnaizationEntity org : orgList) {
				if (StringUtil.isNotEmpty(org.getTreeIndex())) {
					String[] idArr = StringUtil.split(org.getTreeIndex(), ",");
					for (String id : idArr) {
						org = this.orgnaizationService.get(id);
						if (org != null && !StringUtil.equals("-1", org.getId()) && StringUtil.equals("org", org.getType())) {
							if (!resultObj.containsKey(org.getId())) {
								if(StringUtil.equals(user.getOrgIds(), org.getId())){
									org.setIsManage(true);
									flag=true;
								}
								resultObj.put(org.getId(), org);
							}
						}
					}
				}
			}
		}
		if(!resultObj.containsKey("-2")){
			OrgnaizationEntity org=this.orgnaizationService.get("-2");
			resultObj.put(org.getId(), org);
		}
		if(!flag){//设置默认的当前所在团队
			OrgnaizationEntity org=resultObj.get("-2");
			org.setIsManage(true);
			resultObj.put(org.getId(), org);
		}
		return new ArrayList<OrgnaizationEntity>(resultObj.values());
	}
	
	@Override
	public boolean deleteToCompany(String orgId,String userId) throws BusinessException {
		try {			
			UserEntity user=this.get(userId);			//第一步：查询到该机构下所有的组织机构
			List<UserOrgEntity> userOrgList=this.userDao.findHql("from UserOrgEntity uo where uo.user.id=? and uo.org.treeIndex like ?",userId,"%"+orgId+"%");
			//第二步：删除所有的关联记录
			if(userOrgList!=null && userOrgList.size()>0){
				//删除所在的部门
				for(UserOrgEntity userOrg:userOrgList){
					this.userOrgService.delete(userOrg.getId());
					//删除负责人
					this.userOrgService.deleteManagerUserFromDept(userOrg.getOrg().getId(),userId);
				}
				//删除管理员
				boolean isOrgAdmin=this.authorityService.isOrgAdmin(userId, orgId);
				if(isOrgAdmin){
					this.userDao.executeSql("delete from t_org_user_role where userId=? and orgId=? and roleId=?", userId,orgId,RoleEntity.COMPANY_ADMIN);
				}
				//第四步，修改默认组织
				if(StringUtil.equals(orgId, user.getOrgIds())){//如果删除的是当前的组织，那么必须进行组织切换
					user.setOrgIds(OrgnaizationEntity.DEFAULT_ID);
					user.setOrgNames(OrgnaizationEntity.DEFAULT_NAME);
				    this.update(user);
					ClientManager.getInstance().getClient().setUser(user);
					return true;
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("失败");
		}
		
		return false;
	}

	@Override
	public List<UserEntity> queryOrgManager(String orgId) {
		// TODO Auto-generated method stub
		List<UserEntity> userList=this.userDao.findHql("select ur.user from UserRoleEntity ur where ur.role.id=? and ur.orgId=?",RoleEntity.COMPANY_ADMIN,orgId);
		return userList;
	}

	@Override
	public void updateRootTypeId(String typeId,String userId) {
		String hql="UPDATE UserEntity SET rootTypeId=? WHERE id=?";
		userDao.executeHql(hql,typeId,userId);
	}
}
