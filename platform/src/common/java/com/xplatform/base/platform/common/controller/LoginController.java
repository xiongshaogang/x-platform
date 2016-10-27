package com.xplatform.base.platform.common.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.constant.Globals;
import com.xplatform.base.framework.core.extend.datasource.DataSourceContextHolder;
import com.xplatform.base.framework.core.extend.datasource.DataSourceType;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.IpUtil;
import com.xplatform.base.framework.core.util.PasswordUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.pdf.HttpUtils;
import com.xplatform.base.framework.core.util.validcode.GeetestLib;
import com.xplatform.base.orgnaization.module.mybatis.vo.ModuleTreeVo;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.orgnaization.service.OrgnaizationService;
import com.xplatform.base.orgnaization.resouce.mybatis.vo.ResourceVo;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.role.service.RoleService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.entity.UserOrgEntity;
import com.xplatform.base.orgnaization.user.entity.UserPasswordEntity;
import com.xplatform.base.orgnaization.user.entity.UserVerifycodeEntity;
import com.xplatform.base.orgnaization.user.service.UserOrgService;
import com.xplatform.base.orgnaization.user.service.UserPasswordService;
import com.xplatform.base.orgnaization.user.service.UserRoleService;
import com.xplatform.base.orgnaization.user.service.UserService;
import com.xplatform.base.orgnaization.user.service.UserVerifycodeService;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.mybatis.dao.SysUserMybatisDao;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.platform.common.utils.HXUtils;
import com.xplatform.base.platform.common.utils.NumberComparator;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.system.log.entity.UserLogEntity;
import com.xplatform.base.system.log.service.UserLogService;
import com.xplatform.base.system.sysseting.entity.SysParameterEntity;
import com.xplatform.base.system.sysseting.service.SysParameterService;
import com.xplatform.base.system.type.service.TypeService;

/**
 * 
 * description :用户登陆
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月4日 上午9:59:03
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiehs 2014年6月4日 上午9:59:03
 *
 */
@Controller
@RequestMapping("/loginController")
public class LoginController {
	@Resource
	private UserService userService;
	@Resource
	private UserLogService userLogService;
	@Resource
	private UserPasswordService userPasswordService;
	@Resource
	private UserRoleService userRoleService;
	@Resource
	private RoleService roleService;
	@Resource
	private SysParameterService sysParameterService;
	@Resource
	private SysUserMybatisDao sysUserMybatisDao;
	@Resource
	private OrgnaizationService orgnaizationService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private AuthorityService authorityService;
	@Resource
	private UserOrgService userOrgService;
	@Resource
	private UserVerifycodeService userVerifycodeService;
	@Resource
	private TypeService typeService;
	private String message;

	/**
	 * 检查是否显示验证码
	 * 
	 * @param response
	 */
	@RequestMapping(params = "isVerificationCode")
	@ResponseBody
	public AjaxJson isVerificationCode(HttpServletRequest request) {
		SysParameterEntity parameter = sysParameterService.findByCode("isVerificationCode");
		AjaxJson j = new AjaxJson();
		if (parameter != null) {
			j.setObj(parameter.getValue());
		} else {
			j.setObj("N");
		}
		return j;
	}

	@RequestMapping(params = "findSysParameterCode")
	@ResponseBody
	public AjaxJson findSysParameterCode(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String code = request.getParameter("code");
		if (StringUtil.isNotEmpty(code)) {
			SysParameterEntity parameter = sysParameterService.findByCode(code);
			if (parameter != null) {
				j.setObj(parameter.getValue());
			} else {
				j.setObj("N");
			}
		}
		return j;
	}
	
	@RequestMapping(params = "getVerifyCode")
	@ResponseBody
	public AjaxJson getVerifyCode(UserVerifycodeEntity verifycode,HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String phone = request.getParameter("phone");
		j.setMsg("验证码已发送请接收");
		try {
			//第一步，先判断手机号码是否有效
			boolean isMobile = isMobile(phone);
			if (isMobile) {
				// 第二步：判断手机是否注册过，
				boolean flag = userService.getExistPhone(phone);
				if (flag) {
					j.setMsg("该手机已注册过");
					j.setSuccess(false);
				} else {
					// 第二步：获取验证码
					userVerifycodeService.sendVerifyCode(verifycode);
					j.setMsg("验证码已发送请接收");
					j.setSuccess(true);
				}
			}else{
				j.setMsg("请输入正确的手机号码");
				j.setSuccess(false);
			}
		} catch (Exception e) {
			j.setMsg("获取验证码失败");
			j.setSuccess(false);
		}
		return j;
	}
	@RequestMapping(params = "register")
	@ResponseBody
	public AjaxJson register(UserEntity user, OrgnaizationEntity org, HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		String verifyCode=request.getParameter("verifyCode");
		try {
			String moduleFlag = "register";
			if (StringUtil.isEmpty(user.getPhone()) || StringUtil.isEmpty(user.getPassword()) || StringUtil.isEmpty(verifyCode)) {
				result.setMsg("注册信息有误,注册失败");
				result.setSuccess(false);
				return result;
			}
			boolean flag = userVerifycodeService.compareVerifyCode(user.getPhone(), verifyCode, moduleFlag);
			if(flag){
				UserEntity exsitsUser = userService.checkUserExits( user.getPhone());
				String userId=null;
				if (exsitsUser == null) {//用户没有注册过
					// 设置主岗位 及主岗位所在的直接部门与机构
					String orgId = request.getParameter("orgId");
					String orgName = request.getParameter("orgName");
					if(StringUtil.isNotEmpty(orgId)){
						user.setOrgIds(orgId); // 设置组织的id集合
						user.setOrgNames(orgName); // 设置组织的名称集合
					}else{
						orgId=OrgnaizationEntity.DEFAULT_ID;
						orgName=OrgnaizationEntity.DEFAULT_NAME;
						user.setOrgId(orgId);
						user.setOrgName(orgName);
						user.setOrgIds(orgId);
						user.setOrgNames(orgName);
					}
					// 1.添加员工信息
					user.setFlag("1");//激活状态
					String pk = userService.save(user);
					user.setPassword(PasswordUtil.encrypt(user.getId(), user.getPassword(), PasswordUtil.getStaticSalt()));
					user.setId(pk);
					//默认昵称为手机号
					user.setName(user.getPhone());
					userService.update(user);
					// 2.设置员工的密码信息
					UserPasswordEntity userPassword = new UserPasswordEntity();
					userPassword.setUser(user);
					userPassword.setPassword(user.getPassword());
					userPasswordService.save(userPassword);
					// 3.添加员工所属部门信息
					UserOrgEntity userJob = new UserOrgEntity();
					OrgnaizationEntity belongJob = new OrgnaizationEntity();
					belongJob.setId(orgId);
					userJob.setOrg(belongJob);
					userJob.setUser(user);
					userOrgService.save(userJob);
					// 4.请求环信账号添加
					userId=user.getId();
					ObjectNode datanode = JsonNodeFactory.instance.objectNode();
					datanode.put("username", user.getId());
					datanode.put("password", user.getPassword());
					HXUtils.createNewIMUserSingle(datanode);
				} else {
					// 1.添加员工信息
					exsitsUser.setFlag("1");// 激活状态
					exsitsUser.setPassword(PasswordUtil.encrypt(exsitsUser.getId(), user.getPassword(), PasswordUtil.getStaticSalt()));
					userService.update(exsitsUser);
					// 2.设置员工的密码信息
					UserPasswordEntity userPassword = new UserPasswordEntity();
					userPassword.setUser(exsitsUser);
					userPassword.setPassword(exsitsUser.getPassword());
					userPasswordService.save(userPassword);
					// 3.请求环信账号添加
					userId=exsitsUser.getId();
					ObjectNode datanode = JsonNodeFactory.instance.objectNode();
					datanode.put("username", exsitsUser.getId());
					datanode.put("password", exsitsUser.getPassword());
					datanode.put("nickname", exsitsUser.getName());
					HXUtils.createNewIMUserSingle(datanode);
				}
				//生成个人根目录
				String rootTypeId=typeService.doQueryPersonalRootType(userId);
				userService.updateRootTypeId(rootTypeId,userId);
				
				result.setMsg("注册成功");
			}else{
				result.setMsg("验证码失效");
				result.setSuccess(false);
			}
		} catch (Exception e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 检查用户名称
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(params = "checkuser")
	@ResponseBody
	public AjaxJson checkuser(HttpServletRequest req, HttpServletResponse rep) {
		SysParameterEntity sysPasswordIsInitPwd = sysParameterService.findByCode("passwordIsUpdate");// 是否启用初始密码检测
		DataSourceContextHolder.setDataSourceType(DataSourceType.dataSource_pool);
		HttpSession session = ContextHolderUtils.getSession();
		String userName = req.getParameter("userName");			
		String password = req.getParameter("password");
		String token = req.getParameter("usertoken");
		String wcUnionId = req.getParameter("wcUnionId");
		AjaxJson j = new AjaxJson();
		UserEntity user = null;
		String pcyPassword = null;
		try {
			if (HttpUtils.isMoblie(req)) {
				// 移动端请求
				if (StringUtil.isNotEmpty(userName)) {
					// 登录名登陆方式
					user = userService.checkUserExits(userName);
					if (user != null) {
						pcyPassword = PasswordUtil.encrypt(user.getId(), password, PasswordUtil.getStaticSalt());
					}
				} else if (StringUtil.isNotEmpty(wcUnionId)) {
					// wcUnionId登陆
					// user = userService.checkUserExitsByWcUnionId(wcUnionId);
				} else if (StringUtil.isNotEmpty(token)) {
					String[] str = token.split("_");
					String memberId = StringUtil.decodeFromDes3(str[0]);
					user = userService.checkUserExits(memberId);
					pcyPassword = StringUtil.decodeFromDes3(str[1]);
				}
			} else {
				// web端请求
				SysParameterEntity parameter = sysParameterService.findByCode("isVerificationCode");
				if (StringUtil.isNotEmpty(parameter.getValue()) && "Y".equals(parameter.getValue())) {
					// 先进行极验验证码的验证
					GeetestLib geetest = GeetestLib.getGtSession(req);
					int gt_server_status_code = GeetestLib.getGtServerStatusSession(req);

					String gtResult = "fail";

					if (gt_server_status_code == 1) {
						gtResult = geetest.enhencedValidateRequest(req);
					} else {
						// TODO use you own system when geetest-server is
						// down:failback,这里表明极验服务器down机了,需要启用本地校验方式
						gtResult = "fail";
						gtResult = geetest.failbackValidateRequest(req);
						j.setSuccess(false);
						j.setMsg("校验服务器发生错误,请稍后再试");
						j.setStatus("validError");
					}

					if (gtResult.equals(GeetestLib.success_res)) {
						// 验证成功,继续方法
					} else if (gtResult.equals(GeetestLib.forbidden_res)) {
						// 验证中断
						j.setSuccess(false);
						j.setMsg("校验服务器发生错误,请稍后再试");
						j.setStatus("validError");
						return j;
					} else {
						j.setSuccess(false);
						j.setMsg("验证信息有误,请重试");
						j.setStatus("validError");
						return j;
					}
				}

				if (StringUtil.isNotEmpty(userName)) {
					// 登录名登陆方式
					user = userService.checkUserExits(userName);
					if (user != null) {
						pcyPassword = PasswordUtil.encrypt(user.getId(), password, PasswordUtil.getStaticSalt());
					}
				}
			}

			if (user != null) {
				// 意外处理过程
				if ("0".equals(user.getFlag())) {
					j.setSuccess(false);
					j.setMsg("该用户已停用");
					j.setStatus("userDisabled");
					return j;
				} else if ("2".equals(user.getFlag())) {
					j.setSuccess(false);
					j.setMsg("该用户还未激活");
					j.setStatus("userNotActived");
					return j;
				} else if ("3".equals(user.getFlag())) {
					j.setSuccess(false);
					j.setMsg("该用户已锁定");
					j.setStatus("userLocked");
					return j;
				} else if (StringUtil.isNotEmpty(pcyPassword)) {
					if (StringUtil.equals(pcyPassword, user.getPassword())) {
						// 密码校验正确
						if (sysPasswordIsInitPwd != null && "Y".equals(sysPasswordIsInitPwd.getValue())) {
							SysParameterEntity initPassword = sysParameterService.findByCode("initPassword");// 系统初始密码
							String pcyInitPassword = PasswordUtil.encrypt(user.getId(), initPassword.getValue(), PasswordUtil.getStaticSalt());// 加密后的初始密码
							if (StringUtil.equals(pcyInitPassword, pcyPassword)) {
								j.setSuccess(false);
								j.setMsg("当前为初始密码,请修改为个人密码");
								j.setStatus("initPassword");
								return j;
							}
						} else {
							SysParameterEntity sysPasswordEffectiveDay = sysParameterService.findByCode("passwordEffectiveDay");
							int passwordEffectiveDay = Integer.parseInt(sysPasswordEffectiveDay.getValue());
							if (passwordEffectiveDay > 0) {
								UserPasswordEntity userPassword = userPasswordService.getUserPasswordByUserId(user.getId());
								int l = StringUtil.getBetweenDay(userPassword.getUpdatePasswordTime(), new Date(System.currentTimeMillis()));
								// 设置为0表示不启用密码过期策略,否则表示启用
								if (l >= passwordEffectiveDay) {
									j.setSuccess(false);
									j.setMsg("密码已过期,请重新设置");
									j.setStatus("passwordTooOld");
									return j;
								}
							}
						}
					} else {
						// 密码校验不正确
						j.setSuccess(false);
						Integer loginErrorTimes = user.getLoginErrorTimes() + 1;
						user.setLoginErrorTimes(loginErrorTimes);
						// 输入密码超过配置次数则锁定
						SysParameterEntity sysPasswordSameNumber = sysParameterService.findByCode("passwordSameNumber");
						int passwordSameNumber = Integer.parseInt(sysPasswordSameNumber.getValue());
						if (user.getLoginErrorTimes() >= passwordSameNumber) {
							j.setMsg("密码输入错误超过" + passwordSameNumber + "次,已锁定用户");
							j.setStatus("userLocked");
							//user.setFlag("3");
						} else {
							j.setMsg("密码错误");
							j.setStatus("passwordError");
						}
						userService.update(user);
						return j;
					}
				}

				j = loginSuccessProcess(j, user, req);
				j.setSuccess(true);
				j.setMsg("用户登录成功");
				return j;
			} else {
				j.setSuccess(false);
				j.setMsg("该用户不存在");
				j.setStatus("noUser");
				return j;
			}
		} catch (Exception e) {
			j.setMsg("登陆验证过程发生错误,请联系管理员");
			j.setSuccess(false);
			j.setStatus("unknowError");
			return j;
		}
	}

	/**
	 * 登录成功方法
	 * 
	 * @param j
	 * @param user
	 * @param req
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws BusinessException
	 */
	private AjaxJson loginSuccessProcess(AjaxJson j, UserEntity user, HttpServletRequest req) throws ClassNotFoundException, IOException, BusinessException {
		// 重置登录错误次数
		user.setLoginErrorTimes(0);
		userService.update(user);
		Client client = new Client();
		Map<String, Object> info = new HashMap<String, Object>();
		// info
		if (HttpUtils.isMoblie(req)) {
			info.put("pcypassword", user.getPassword());
			info.put("usertoken", StringUtil.encodeToDes3(user.getId()) + "_" + StringUtil.encodeToDes3(user.getPassword()));
		}
		info.put("userId", user.getId());

		// info.put("sessionId", session.getId());
		// info.put("userInfo",
		// sysUserService.getUserInfoById(ue.getId()));
		j.setObj(info);

		// 构造用户所在机构,拥有角色等信息
		/*Map<String, String> param = new HashMap<String, String>();
		param.put("userId", user.getId());
		boolean isAdmin = authorityService.isAdmin(param);
		List<OrgnaizationEntity> orgList = null;
		if (isAdmin) {
			orgList = this.orgnaizationService.queryList();
			List<OrgnaizationEntity> copyOrgList = BeanUtils.deepCopy(orgList);
			for (OrgnaizationEntity org : copyOrgList) {
				org.setAvailable("1");
			}
			client.setManagerOrgList(copyOrgList);
		} else {
			orgList = this.orgnaizationService.queryControllerOrg(user.getId());
			client.setManagerOrgList(orgList);// 获取员工具有的可操作的组织结构列表（用于展示个人的组织机构树）
		}
		List<RoleEntity> roleList = roleService.getRoleListByOrgids(sysUserService.getUserOrgList(sysUserService.getAllOrganizationsByUserId(user.getId())));// 获取员工角色列表
		client.setRoleList(roleList);
		client.setOrgList(sysUserService.getAllOrganizationsByUserId(user.getId()));*/
		client.setIp(IpUtil.getIpAddr(req));
		client.setLogindatetime(new Date());
		client.setUser(user);
		ClientManager.getInstance().addClient(req.getSession().getId(), client);
		return j;
	}

	/**
	 * 用户登录
	 * 
	 * @param user
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(params = "index")
	public String index(HttpServletRequest request) {
		DataSourceContextHolder.setDataSourceType(DataSourceType.dataSource_pool);
		UserEntity user = ClientUtil.getUserEntity();
		if (user != null) {
			String foreServerReq = ConfigConst.serverUrl + ConfigConst.attachRequest;
			String portraitUrl = foreServerReq + user.getPortrait();
			request.setAttribute("user", user);
			request.setAttribute("portraitUrl", portraitUrl);

			UserLogEntity userLog = new UserLogEntity();
			userLog.setUserId(user.getId());
			userLog.setUserName(user.getUserName());
			userLog.setTime(new Date());
			userLog.setIp(IpUtil.getIpAddr(request));
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			userLog.setContent(user.getUserName() + "在" + sdf.format(date) + "登录系统");
			userLog.setStatus("0");
			try {
				userLogService.save(userLog);
			} catch (Exception e) {
				e.printStackTrace();
			}

			SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd");
			Date todayDate = new Date();
			Date tomorrowDate = com.xplatform.base.framework.core.util.StringUtil.getNextNDay(new Date(), 1, "after");
			Date dayAfterTomorrowDate = com.xplatform.base.framework.core.util.StringUtil.getNextNDay(new Date(), 2, "after");
			Date thrdate = com.xplatform.base.framework.core.util.StringUtil.getNextNDay(new Date(), 3, "after");
			Date fordate = com.xplatform.base.framework.core.util.StringUtil.getNextNDay(new Date(), 4, "after");

			request.setAttribute("editTodayDate", sdf.format(todayDate));
			request.setAttribute("editTomorrowDate", sdf.format(tomorrowDate));
			request.setAttribute("editDayAfterTomorrowDate", sdf.format(dayAfterTomorrowDate));
			request.setAttribute("editThrdate", sdf.format(thrdate));
			request.setAttribute("editFordate", sdf.format(fordate));

			request.setAttribute("thrdate", sdf1.format(thrdate));
			request.setAttribute("fordate", sdf1.format(fordate));

			return "main/index";

		} else {
			return "login/login";
		}
	}

	@RequestMapping(params = "login")
	public String login(HttpServletRequest request) {
		if (ClientManager.getInstance().getClient() != null) {
			return "redirect:loginController.do?home ";
		} else {
			return "login/login";
		}

	}

	/**
	 * 退出系统(web端)
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "logout")
	public ModelAndView logout(HttpServletRequest request) {
		HttpSession session = ContextHolderUtils.getSession();

		UserLogEntity userLog = new UserLogEntity();
		UserEntity user = ClientUtil.getUserEntity();
		if (user != null) {
			userLog.setUserId(user.getId());
			userLog.setUserName(user.getUserName());
			userLog.setTime(new Date());
			userLog.setIp(IpUtil.getIpAddr(request));
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			userLog.setContent(user.getUserName() + "在" + sdf.format(date) + "退出系统");
			userLog.setStatus("1");
			try {
				userLogService.save(userLog);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ClientManager.getInstance().removeClient(session.getId());
		ModelAndView modelAndView = new ModelAndView(new RedirectView("loginController.do?login"));

		return modelAndView;
	}

	/**
	 * 退出系统(移动端)
	 * 
	 * @param user
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "logout4App")
	@ResponseBody
	public AjaxJson logout4App(HttpServletRequest request) throws BusinessException {
		AjaxJson result = new AjaxJson();
		try {
			HttpSession session = ContextHolderUtils.getSession();

			UserLogEntity userLog = new UserLogEntity();
			UserEntity user = ClientUtil.getUserEntity();
			if (user != null) {
				userLog.setUserId(user.getId());
				userLog.setUserName(user.getUserName());
				userLog.setTime(new Date());
				userLog.setIp(IpUtil.getIpAddr(request));
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				userLog.setContent(user.getUserName() + "在" + sdf.format(date) + "退出系统");
				userLog.setStatus("1");
				userLogService.save(userLog);
			}

			ClientManager.getInstance().removeClient(session.getId());
			result.setMsg("用户退出成功");
		} catch (Exception e) {
			ExceptionUtil.throwBusinessException("用户退出失败");
		}
		return result;
	}

	/**
	 * 菜单跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "left")
	public ModelAndView left(HttpServletRequest request, HttpServletResponse response) {
		UserEntity user = ClientUtil.getUserEntity();
		HttpSession session = ContextHolderUtils.getSession();
		// 登陆者的权限
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
			return new ModelAndView(new RedirectView("loginController.do?login"));
		}

		Map<Integer, List<ModuleTreeVo>> map = getFunctionMap(user);
		// 构造左侧菜单数据
		StringBuffer menuMapSbf = new StringBuffer();
		menuMapSbf.append("{\"data\":[");
		for (Entry<Integer, List<ModuleTreeVo>> entry : map.entrySet()) {
			Integer key = entry.getKey();

			List<ModuleTreeVo> funList = (List<ModuleTreeVo>) entry.getValue();
			for (ModuleTreeVo fun : funList) {
				menuMapSbf.append("{\"funId\":\"").append(fun.getId()).append("\",");
				menuMapSbf.append("\"funPid\":\"").append(fun.getParentId() == null ? "" : fun.getParentId()).append("\",");
				menuMapSbf.append("\"funName\":\"").append(fun.getName() == null ? "" : fun.getName()).append("\",");
				menuMapSbf.append("\"funLevel\":\"").append(fun.getLevel() == 0 ? "" : fun.getLevel() - 1).append("\",");
				menuMapSbf.append("\"funUrl\":\"").append(fun.getUrl() == null ? "" : fun.getUrl()).append("\",");
				menuMapSbf.append("\"funIframe\":\"").append(fun.getIsIframe() == null ? "" : fun.getIsIframe()).append("\",");
				menuMapSbf.append("\"funOrder\":\"").append(fun.getOrderby() == 0 ? "" : fun.getOrderby()).append("\",");
				menuMapSbf.append("\"funIcon\":\"").append(fun.getIconCls() == null ? "" : fun.getIconCls()).append("\",");
				menuMapSbf.append("\"funSubsystem\":\"").append(fun.getSubsystem() == null ? "" : fun.getSubsystem()).append("\",");

				List<ModuleTreeVo> moduleList = map.get(key + 1);
				int childSize = 0;
				if (moduleList != null && moduleList.size() > 0) {
					for (ModuleTreeVo module : moduleList) {
						if (StringUtil.equals(fun.getId(), module.getParentId())) {
							childSize++;
						}
					}
				}
				menuMapSbf.append("\"funCsize\":\"").append(childSize).append("\"},");
			}
		}
		menuMapSbf = menuMapSbf.append("]}");
		if(menuMapSbf.lastIndexOf(",")>=0){
			menuMapSbf = menuMapSbf.replace(menuMapSbf.lastIndexOf(","), menuMapSbf.length() - 2, "");
		}
		try {
			PrintWriter pw = response.getWriter();
			pw.write(menuMapSbf.toString());
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 菜单跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "appModules")
	@ResponseBody
	public AjaxJson appModules(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson result = new AjaxJson();
		UserEntity user = ClientUtil.getUserEntity();
		HttpSession session = ContextHolderUtils.getSession();
		// 登陆者的权限
		if (user.getId() == null) {
			result.setSuccess(false);
			session.removeAttribute(Globals.USER_SESSION);
			result.setMsg("模块列表获取失败");
			return result;
		}
		Map<String, ModuleTreeVo> userFunctionList = getUserFunction(user);
		List<ModuleTreeVo> moduleList = new ArrayList<ModuleTreeVo>();
		if (userFunctionList != null && userFunctionList.keySet().size() > 0) {
			for (Map.Entry<String, ModuleTreeVo> entry : userFunctionList.entrySet()) {
				if (entry != null && entry.getValue() != null) {
					if (StringUtil.equals(entry.getValue().getPhoneShow(), "Y") && StringUtil.equals(entry.getValue().getIsLeaf(), "1")) {
						moduleList.add(entry.getValue());
					}
				}
			}
		}
		result.setObj(moduleList);
		return result;
	}

	/**
	 * 构造用户的模块权限层次
	 * 
	 * @param user
	 * @return
	 */
	private Map<Integer, List<ModuleTreeVo>> getFunctionMap(UserEntity user) {
		Map<Integer, List<ModuleTreeVo>> functionMap = new HashMap<Integer, List<ModuleTreeVo>>();
		Map<String, ModuleTreeVo> loginActionlist = getUserFunction(user);
		if (loginActionlist.size() > 0) {
			Collection<ModuleTreeVo> allFunctions = loginActionlist.values();
			// 同一级的放在一个list里面，最后map就是级别里面放入list
			for (ModuleTreeVo function : allFunctions) {
				// 按树的层级level重新构造一个key为层级,value为层级下的模块集合的Map
				if (!functionMap.containsKey(function.getLevel() + 0)) {
					functionMap.put(function.getLevel() + 0, new ArrayList<ModuleTreeVo>());
				}
				functionMap.get(function.getLevel() + 0).add(function);
			}
			// 菜单栏级别排序
			Collection<List<ModuleTreeVo>> c = functionMap.values();
			for (List<ModuleTreeVo> list : c) {
				Collections.sort(list, new NumberComparator());
			}
		}
		return functionMap;
	}

	/**
	 * 获取用户菜单列表
	 * 
	 * @param user
	 * @return
	 */
	private Map<String, ModuleTreeVo> getUserFunction(UserEntity user) {
		Client client = ClientUtil.getClient();
		Map<String, ModuleTreeVo> loginActionlist = new HashMap<String, ModuleTreeVo>();
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", ClientUtil.getUserId());
		boolean isAdmin = authorityService.isAdmin(param);
		List<ModuleTreeVo> moduleList = new ArrayList<ModuleTreeVo>();
		List<RoleEntity> roleList=this.sysUserService.getRoleListByUserId(ClientUtil.getUserId());
		String roleIds=null;
		if(roleList !=null && roleList.size()>0){
			for (RoleEntity role : roleList) {
				roleIds+=("'" + role.getId() + "',");
			}
			roleIds=StringUtil.removeDot(roleIds);
		}
		// 获取模块权限
		if (client.getModules() == null) {
			if (isAdmin) {// 管理员可以获得某组织所有模块权限
				Map<String, Object> param1 = new HashMap<String, Object>();
				moduleList = authorityService.getAllModule(param1);
			} else {// 不是管理员，得查询出该用户应有的权限
				Map<String, Object> param2 = new HashMap<String, Object>();
				param2.put("userId", ClientUtil.getUserId());
				param2.put("roleIds",roleIds);
				moduleList = authorityService.getUserModule(param2);
			}
			for (ModuleTreeVo module : moduleList) {
				loginActionlist.put(module.getId(), module);
			}
			client.setModules(loginActionlist);
		}
		// 模块资源权限
		if (client.getResources() == null) {
			Map<String, List<ResourceVo>> moduleResourceList = new HashMap<String, List<ResourceVo>>();
			if (!isAdmin && moduleList != null && moduleList.size() > 0) {
				for (ModuleTreeVo module : moduleList) {
					Map<String, Object> resourceParam = new HashMap<String, Object>();
					resourceParam.put("moduleId", module.getId());
					resourceParam.put("roleIds", roleIds);
					// 获取用户所在模块的所有资源权限
					List<ResourceVo> resourceList = authorityService.getUserModuleResources(resourceParam);
					moduleResourceList.put(module.getCode(), resourceList);
				}
				client.setResources(moduleResourceList);
			}
		}

		return client.getModules();
	}

	/**
	 * 首页跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "home")
	public ModelAndView home(HttpServletRequest request) {
		UserEntity user = this.userService.get(ClientUtil.getUserId());
		if (user != null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", user.getId());
			map.put("name", user.getName());
			map.put("portrait", user.getPortrait());
			map.put("password", user.getPassword());
			request.setAttribute("user", map);
		}
		return new ModelAndView("main/home");
	}
	
	@RequestMapping(params = "appSetting")
	public ModelAndView appSetting(HttpServletRequest request) {
		request.setAttribute("load", request.getParameter("load"));
		String userId = ClientUtil.getUserId();
		if(StringUtil.isNotEmpty(userId)){
			UserEntity user = this.userService.get(userId);
			if(user != null){
				request.setAttribute("userName", user.getName());
			}
		}
		return new ModelAndView("main/home/setting/appSettingHome");
	}

	@RequestMapping(params = "changePass")
	public ModelAndView changePass(HttpServletRequest request) {
		return new ModelAndView("main/changePassword");
	}
	
	/**
	 * 无权限页面提示跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "noAuth")
	public ModelAndView noAuth(HttpServletRequest request) {
		return new ModelAndView("common/noAuth");
	}

	/**
	 * 向极验验证服务器请求challenge方法
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(params = "queryChallenge")
	@ResponseBody
	public AjaxJson queryChallenge(HttpServletRequest request, HttpServletResponse response) throws IOException {
		AjaxJson j = new AjaxJson();
		GeetestLib gtSdk = new GeetestLib();
		gtSdk.setCaptchaId(ConfigConst.geetest_captcha_id);
		gtSdk.setPrivateKey(ConfigConst.geetest_private_key);

		gtSdk.setGtSession(request);

		String resStr = "{}";

		if (gtSdk.preProcess() == 1) {
			// 请求成功
			resStr = gtSdk.getSuccessPreProcessRes();
			gtSdk.setGtServerStatusSession(request, 1);
			j.setSuccess(true);
		} else {
			// 请求失败
			resStr = gtSdk.getFailPreProcessRes();
			gtSdk.setGtServerStatusSession(request, 0);
			j.setSuccess(false);
		}
		j.setObj(resStr);
		return j;
	}
	
	/**
	 * 手机号码正则校验
	 * */
	public boolean isMobile(String mobile) {
		return Pattern.matches(BusinessConst.REGEX_MOBILE, mobile);
	}
	
	@RequestMapping(params = "downloadApp")
	public ModelAndView downloadApp(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("login/downloadApp");
	}
	
	@RequestMapping(params = "toRegister")
	public ModelAndView toRegister(HttpServletRequest request) {
		return new ModelAndView("main/home/register");
	}
	
	/**
	 * 表单引擎页面临时过渡页
	 * 
	 * @return
	 */
	@RequestMapping(params = "homePage")
	public ModelAndView homePage(HttpServletRequest request) {
		UserEntity user = this.userService.get(ClientUtil.getUserId());
		if (user != null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", user.getId());
			map.put("name", user.getName());
			map.put("portrait", user.getPortrait());
			map.put("password", user.getPassword());
			request.setAttribute("user", map);
		}
		return new ModelAndView("main/homePage");
	}
}
