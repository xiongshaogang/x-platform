package com.xplatform.base.orgnaization.user.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xplatform.base.framework.core.cache.manager.UcgCacheManager;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.common.UploadFile;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.common.model.json.TreeNode;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.FileUtils;
import com.xplatform.base.framework.core.util.MD5Util;
import com.xplatform.base.framework.core.util.MybatisTreeMapper;
import com.xplatform.base.framework.core.util.PasswordUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.TreeMapper;
import com.xplatform.base.framework.core.util.pdf.HttpUtils;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.module.mybatis.vo.ModuleTreeVo;
import com.xplatform.base.orgnaization.module.service.ModuleService;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupMemberService;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupService;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.orgnaization.service.OrgnaizationService;
import com.xplatform.base.orgnaization.resouce.entity.ResourceEntity;
import com.xplatform.base.orgnaization.resouce.service.ResourceService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.role.service.RoleService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.entity.UserOrgEntity;
import com.xplatform.base.orgnaization.user.entity.UserPasswordEntity;
import com.xplatform.base.orgnaization.user.entity.UserVerifycodeEntity;
import com.xplatform.base.orgnaization.user.service.UserOrgService;
import com.xplatform.base.orgnaization.user.service.UserPasswordService;
import com.xplatform.base.orgnaization.user.service.UserService;
import com.xplatform.base.orgnaization.user.service.UserVerifycodeService;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.platform.common.utils.HXUtils;
import com.xplatform.base.platform.common.utils.uploadFileUtil;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.poi.excel.entity.result.ExcelImportResult;
import com.xplatform.base.system.attachment.entity.AttachEntity;
import com.xplatform.base.system.attachment.entity.FTPAttachEntity;
import com.xplatform.base.system.attachment.service.AttachService;
import com.xplatform.base.system.attachment.service.FTPAttachService;
import com.xplatform.base.system.sysseting.service.SysParameterService;
import com.xplatform.base.system.type.entity.FileTypeUserAuthorityEntity;

/**
 * 
 * description : 用户管理controller
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:32:17
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiehs 2014年5月24日 下午12:32:17
 *
 */
@Controller
@RequestMapping("/userController")
public class UserController extends BaseController {

	@Resource(name = "userService")
	private UserService userService;
	@Resource
	private ModuleService moduleService;
	@Resource
	private ResourceService resourceService;
	@Resource
	private AuthorityService authorityService;
	@Resource
	private UserPasswordService userPasswordService;
	@Resource
	private SysParameterService sysParameterService;
	@Resource
	private UcgCacheManager ucgCacheManager;
	@Resource
	private OrgnaizationService orgnaizationService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private UserOrgService userOrgService;
	@Resource
	private RoleService roleService;
	@Resource
	private AttachService attachService;
	@Resource
	private FTPAttachService ftpAttachService;
	@Resource
	private OrgGroupService orgGroupservice;
	@Resource
	private OrgGroupMemberService orgGroupMemberService;
	@Resource
	private UserVerifycodeService userVerifycodeService;

	public void setSysParameterService(SysParameterService sysParameterService) {
		this.sysParameterService = sysParameterService;
	}

	public void setAuthorityService(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}

	private Page<UserEntity> page = new Page<UserEntity>(PAGESIZE);

	private AjaxJson result = new AjaxJson();

	private String message;

	/**
	 * 用户管理列表页跳转
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "user")
	public ModelAndView user(HttpServletRequest request) {
		return new ModelAndView("platform/organization/user/userList");
	}

	@RequestMapping(params = "changePass")
	public ModelAndView changePass(HttpServletRequest request) {
		return new ModelAndView("main/changePassword");
	}

	@RequestMapping(params = "changePass1")
	public ModelAndView changePass1(HttpServletRequest request) {
		return new ModelAndView("main/home/setting/changePass");
	}

	/**
	 * 单表hibernate组装表格数据
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param user
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {

		String orgId = request.getParameter("orgId");
		if (StringUtil.isEmpty(orgId)) {
			orgId = "NULL";
		}
		this.buildFilter(page, request);
		Map<String, Object> param = (Map<String, Object>) page.getParameter();
		param.put("orgIds", new String[] { "'" + orgId + "'" });
		page.setParameter(param);
		page = sysUserService.getUserByCurrentOrgIdsByPage(page); // 点击的是部门，获取部门下的所有员工

		dataGrid.setResults(page.getResult());
		dataGrid.setTotal((int) page.getTotalCount());
		TagUtil.datagrid(response, dataGrid);
	}

	@RequestMapping(params = "getDeptUserList")
	@ResponseBody
	public AjaxJson getDeptUserList(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		AjaxJson result = new AjaxJson();
		String orgId = request.getParameter("orgId");
		if (StringUtil.isEmpty(orgId)) {
			orgId = "NULL";
		}
		this.buildFilter(page, request);
		Map<String, Object> param = (Map<String, Object>) page.getParameter();
		param.put("orgIds", new String[] { "'" + orgId + "'" });
		page.setParameter(param);
		page = sysUserService.getUserByCurrentOrgIdsByPage(page); // 点击的是部门，获取部门下的所有员工

		/*
		 * Map<String,Object> map=new HashMap<String,Object>();
		 * map.put("userList", page.getResult()); map.put("totalCount",
		 * page.getTotalCount());
		 */
		result.setObj(page.getResult());
		return result;
	}

	/**
	 * 批量删除员工
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:27:15
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		try {
			if (StringUtil.isNotEmpty(ids)) {
				String[] idArr = StringUtil.split(ids, ",");
				for (String id : idArr) {
					/*
					 * UserOrgEntity userOrg = userOrgService.get(id);
					 * List<UserOrgEntity> userOrgList =
					 * userOrgService.findByUserId(userOrg.getUser().getId());
					 * if (userOrgList != null && userOrgList.size() == 1) {
					 * userService.delete(userOrg.getUser().getId());// 删除用户 }
					 */
					userOrgService.delete(id);// 删除关联的机构
				}
			}
			message = "删除成功";
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}

	@RequestMapping(params = "deleteUserFromDept")
	@ResponseBody
	public AjaxJson deleteUserFromDept(HttpServletRequest request) {
		String userIds = request.getParameter("userIds");
		String deptId = request.getParameter("deptId");
		try {
			if (StringUtil.isNotEmpty(userIds) && StringUtil.isNotEmpty(deptId)) {
				this.userOrgService.deleteUserFromDept(userIds, deptId);
				message = "删除成功";
			} else {
				message = "没有选择用户或者部门";
			}
		} catch (Exception e) {
			message = "删除失败";
		}
		result.setMsg(message);
		return result;
	}

	@RequestMapping(params = "getUserInfo")
	@ResponseBody
	public AjaxJson getUserInfo(HttpServletRequest request) {
		String userId = StringUtil.isEmpty(request.getParameter("id"), ClientUtil.getUserId());
		UserEntity user = this.userService.get(userId);
		if (user != null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", user.getId());
			map.put("name", user.getName());
			map.put("portrait", user.getPortrait());
			map.put("password", user.getPassword());
			map.put("phone", user.getPhone());
			result.setObj(map);
		}
		return result;
	}

	@RequestMapping(params = "getUsersInfo")
	@ResponseBody
	public AjaxJson getUsersInfo(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		if (StringUtil.isNotEmpty(ids)) {
			List<Map<String, Object>> list = userService.getUsersInfo(ids);
			result.setObj(list);
		}
		return result;
	}

	/**
	 * 进入新增或者修改查看页面
	 * 
	 * @param organization
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "userEdit")
	public ModelAndView userEdit(String id, Model model) throws BusinessException {
		if (StringUtil.isNotEmpty(id)) {
			UserOrgEntity userOrg = userOrgService.get(id);
			model.addAttribute("user", userOrg.getUser());
			model.addAttribute("userOrg", userOrg);
		}
		return new ModelAndView("platform/organization/user/userEdit");
	}

	/**
	 * 新增或修改员工
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param employee
	 * @return
	 */
	@RequestMapping(params = "saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(UserEntity user, OrgnaizationEntity org, HttpServletRequest request) {
		try {
			// 测试
			/*
			 * user.setName("李1晓1坛1"); user.setPhone("18814123063");
			 * user.setPassword("123456");
			 */

			// 上传头像
			String portrait = request.getParameter("portrait");
			if (StringUtil.isNotEmpty(portrait)) {
				UploadFile uploadFile = uploadFileUtil.uploadFile(portrait);
				userService.uploadFile(uploadFile);
			}
			// 设置主岗位 及主岗位所在的直接部门与机构
			String orgId = request.getParameter("orgId");
			String orgName = request.getParameter("orgName");
			if (StringUtil.isNotEmpty(user.getId())) {
				List<UserOrgEntity> userOrgList = userOrgService.findByUserId(user.getId());
				if (userOrgList != null && userOrgList.size() > 0) {
					for (UserOrgEntity userOrg : userOrgList) {
						if (!orgId.contains(userOrg.getOrg().getId())) {
							orgId += ("," + userOrg.getOrg().getId());
							orgName += ("," + userOrg.getOrg().getName());
						}
					}
				}
			}
			user.setOrgIds(orgId); // 设置组织的id集合
			user.setOrgNames(orgName); // 设置组织的名称集合

			if (StringUtil.isNotEmpty(user.getId())) {
				userService.update(user);
				message = "员工更新成功";
			} else {
				// 0.请求环信账号添加
				ObjectNode datanode = JsonNodeFactory.instance.objectNode();
				datanode.put("username", user.getId());
				datanode.put("password", user.getPassword());
				HXUtils.createNewIMUserSingle(datanode);
				// 1.添加员工信息
				message = "员工新增成功";
				String pk = userService.save(user);
				user.setId(pk);
				// 2.添加员工所属部门信息
				UserOrgEntity userJob = new UserOrgEntity();
				OrgnaizationEntity belongJob = new OrgnaizationEntity();
				belongJob.setId(orgId);
				userJob.setOrg(belongJob);
				userJob.setUser(user);
				userOrgService.save(userJob);
				// 3.设置员工的密码信息
				user.setPassword(PasswordUtil.encrypt(user.getUserName(), user.getPassword(), PasswordUtil.getStaticSalt()));
				UserEntity userEntity = new UserEntity();
				userEntity.setId(pk);
				UserPasswordEntity userPassword = new UserPasswordEntity();
				userPassword.setUser(userEntity);
				userPassword.setPassword(user.getPassword());
				userPasswordService.save(userPassword);

			}
		} catch (Exception e) {
			message = e.getMessage();
			e.printStackTrace();
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月17日 下午6:50:35
	 * @Decription 获取该岗位所在的直接部门 或机构
	 *
	 * @param
	 * @return
	 */
	public OrgnaizationEntity getFirstOrg(String orgId, String orgType) {
		OrgnaizationEntity org = orgnaizationService.get(orgId);
		while (org != null && !orgType.equals(org.getType())) {
			org = org.getParent();
		}
		return org;
	}

	/**
	 * 用户激活或者冻结
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doActive")
	@ResponseBody
	public AjaxJson doActive(UserEntity user) {
		String active = user.getFlag();
		try {
			if (StringUtil.isNotEmpty(active) && active.equals("3")) {
				message = "用户锁定成功";
			} else if (StringUtil.isNotEmpty(active) && active.equals("1")) {
				message = "用户激活成功";
			}
			this.userService.update(user);
		} catch (BusinessException e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 用户密码初始化
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "initPwd")
	@ResponseBody
	public AjaxJson initPwd(UserEntity user) {
		String userId = user.getId();
		message = "用户密码初始化成功";
		try {
			this.userService.initPwd(userId);
		} catch (BusinessException e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 用户选择
	 * 
	 * @author xiehs
	 * @createtime 2014年6月7日 下午3:12:36
	 * @param request
	 * @param moduleId
	 * @param userId
	 * @return
	 */
	@RequestMapping(params = "empSelect")
	public ModelAndView empSelect(HttpServletRequest request, String moduleId, String userId) {
		return new ModelAndView("platform/organization/user/empSelect");
	}

	/*
	 * @RequestMapping(params = "empDatagrid") public void
	 * empDatagrid(HttpServletRequest request, HttpServletResponse response,
	 * DataGrid dataGrid) { //自动封装查询条件 this.buildFilter(empPage, request);
	 * //手动添加查询条件 Map<String, String> param1 = (Map<String, String>)
	 * empPage.getParameter(); empPage.setParameter(param1); //添加排序字段
	 * this.buildOrder(empPage, request, "id", Page.ASC); empPage =
	 * userService.queryEmpDeptList(empPage);
	 * dataGrid.setResults(empPage.getResult()); dataGrid.setTotal((int)
	 * empPage.getTotalCount()); TagUtil.datagrid(response, dataGrid); }
	 */

	/**
	 * 模块选择列表页面跳转
	 * 
	 * @author xiehs
	 * @createtime 2014年5月27日 下午7:32:45
	 * @Decription
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "moduleSelect")
	public ModelAndView moduleSelect(HttpServletRequest request) {
		String userId = request.getParameter("userId");
		request.setAttribute("userId", userId);
		return new ModelAndView("platform/organization/user/moduleSelect");
	}

	/**
	 * 系统类型选择树页面跳转
	 * 
	 * @author luoheng
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "sysTypeSelect")
	public ModelAndView sysTypeSelect(HttpServletRequest request) {
		String userId = request.getParameter("userId");
		request.setAttribute("userId", userId);
		return new ModelAndView("platform/organization/user/sysTypeSelect");
	}

	/**
	 * 设置模块权限
	 * 
	 * @author xiehs
	 * @createtime 2014年5月27日 下午7:59:59
	 * @Decription
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "setModuleAuthority")
	public void setModuleAuthority(HttpServletRequest request, HttpServletResponse response) {
		String parentId = "";
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			parentId = request.getParameter("id");
		} else {
			parentId = "-1";
		}
		// 第一步：查询所有的模块
		List<ModuleTreeVo> moduleAllList = moduleService.queryModuleAuthorityByUser(request.getParameter("userId"), parentId);
		// 第二步：得到登陆用户的模块权限
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		Map<String, ModuleTreeVo> clientModuleList = client.getModules();
		// 第三步：构造模块的权限
		List<ModuleTreeVo> moduleList = new ArrayList<ModuleTreeVo>();
		if (moduleAllList != null && moduleAllList.size() > 0) {
			for (ModuleTreeVo module : moduleAllList) {
				if (clientModuleList.containsKey(module.getId())) {
					moduleList.add(module);
				}
			}
		}
		// 第四步：树的转换
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		propertyMapping.put(TreeMapper.PropertyType.CHECKED.getValue(), "checked");
		List<TreeNode> treeList = MybatisTreeMapper.buildJsonTree(moduleList, propertyMapping);
		TagUtil.tree(response, treeList);
	}

	/**
	 * 更新模块权限
	 * 
	 * @author xiehs
	 * @createtime 2014年5月27日 下午8:20:54
	 * @Decription
	 *
	 * @param request
	 * @param response
	 */
	/*
	 * @RequestMapping(params = "updateModuleAuthority")
	 * 
	 * @ResponseBody
	 */
	/*
	 * public AjaxJson updateModuleAuthority(HttpServletRequest request,
	 * HttpServletResponse response) { String moduleIds =
	 * request.getParameter("userModuleIds"); String userId =
	 * request.getParameter("userId"); result.setMsg("更新用户模块权限成功"); try {
	 * moduleService.updateUserModuleAuthority(moduleIds, userId); } catch
	 * (BusinessException e) { // TODO: handle exception
	 * result.setMsg(e.getMessage()); } return result; }
	 */

	/**
	 * 按钮权限展示
	 * 
	 * @param request
	 * @param functionId
	 * @param roleId
	 * @return
	 */
	/*
	 * @RequestMapping(params = "resourceSelect") public ModelAndView
	 * resourceSelect(HttpServletRequest request, String moduleId, String
	 * userId) { //模块所有的资源 List<ResourceEntity> resourceList =
	 * this.resourceService.queryListByModuleId(moduleId); //角色模块所有的资源权限
	 * List<UserResourceEntity> resourceAuthorityList =
	 * this.resourceService.queryResourceAuthorityByUser(userId, moduleId);
	 * Set<String> resourceAuthorityIds = new HashSet<String>(); for
	 * (UserResourceEntity userResourceEntity : resourceAuthorityList) {
	 * resourceAuthorityIds.add(userResourceEntity.getResource().getId()); }
	 * //设置参数 request.setAttribute("resourceList", resourceList);//模块资源
	 * request.setAttribute("resourceAuthorityIds",
	 * resourceAuthorityIds);//角色模块资源权限 request.setAttribute("moduleId",
	 * moduleId); return new
	 * ModelAndView("platform/organization/user/resourceSelect"); }
	 */

	/**
	 * 更新资源权限
	 * 
	 * @author xiehs
	 * @createtime 2014年5月27日 下午8:20:54
	 * @Decription
	 *
	 * @param request
	 * @param response
	 */
	/*
	 * @RequestMapping(params = "updateResourceAuthority")
	 * 
	 * @ResponseBody public AjaxJson updateResourceAuthority(HttpServletRequest
	 * request, HttpServletResponse response) { String moduleId =
	 * request.getParameter("moduleId"); String userId =
	 * request.getParameter("userId"); String resourceIds =
	 * request.getParameter("resourceIds"); result.setMsg("更新用户资源权限成功"); try {
	 * this.resourceService.updateUserResourceAuthority(moduleId, userId,
	 * resourceIds); } catch (Exception e) { result.setMsg(e.getMessage()); }
	 * return result;
	 * 
	 * }
	 */

	/**
	 * 进入修改密码界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "passwordEdit")
	public ModelAndView passwordEdit(HttpServletRequest request) {
		String userName = request.getParameter("userName");
		String oldPwd = request.getParameter("oldPwd");
		request.setAttribute("userName", userName);
		request.setAttribute("oldPwd", oldPwd);
		return new ModelAndView("main/changePassword");
	}

	/**
	 * 更改密码
	 * 
	 * @author binyong
	 * @createtime 2014年6月25日
	 * @Decription
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "changePassword")
	@ResponseBody
	public AjaxJson changePassword(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		AjaxJson result = new AjaxJson();
		String old_psd_val = request.getParameter("old_psd_val");
		String new_psd_val = request.getParameter("new_psd_val");
		UserEntity user = null;
		if (StringUtil.isNotEmpty(request.getParameter("userId"))) {
			// TODO 此方法需要修改
			// user = userService.findByProperty("businessUserId",
			// request.getParameter("userId"));
		} else {
			user = ClientUtil.getUserEntity();
		}
		if (user == null) {
			UserEntity u = new UserEntity();
			u.setUserName(request.getParameter("userName"));
			u.setPassword(old_psd_val);
			// String userName = request.getParameter("userName");
			// user = userService.getUserByName(userName);
			user = userService.getUserByNameAndPwd(u);
		}
		String old_password = PasswordUtil.encrypt(user.getId(), old_psd_val, PasswordUtil.getStaticSalt());
		String new_password = PasswordUtil.encrypt(user.getId(), new_psd_val, PasswordUtil.getStaticSalt());

		UserPasswordEntity userPasswordEntity = userPasswordService.getUserPasswordByUserId(user.getId());
		if (!StringUtils.equals(userPasswordEntity.getPassword(), old_password)) {
			result.setSuccess(false);
			result.setMsg("原密码填写错误");
		} else {
			List<UserPasswordEntity> userPasswordList = userPasswordService.queryList(user.getId());
			int size = userPasswordList.size() > 5 ? 5 : userPasswordList.size();

			int quite = 0;
			for (int i = 0; i < size; i++) {
				if (userPasswordList.get(i).getPassword().equals(new_password)) {
					quite = quite + 1;
				}
			}

			if (quite != 0) {
				result.setSuccess(false);
				result.setMsg("不能与近5次密码相同，请重新修改");
			} else {
				UserPasswordEntity userPassword = new UserPasswordEntity();
				userPassword.setUser(user);
				userPassword.setPassword(new_password);
				user.setPassword(new_password);
				try {
					userPasswordService.update(userPassword);
					userService.update(user);
					// 调用环信修改密码接口
					ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();
					dataObjectNode.put("newpassword", new_password);
					ObjectNode resultObject = HXUtils.updateUserPassword(user.getId(), dataObjectNode);
					if (resultObject.get("statusCode").intValue() == 200) {
					} else {
						ExceptionUtil.throwException("IM服务器请求失败");
					}
				} catch (Exception e) {
					result.setMsg(ExceptionUtil.printStackTraceAndLogger(e));
				}
				// user.setPassword(new_password);

				// 设置usertoken
				Map<String, Object> info = new HashMap<String, Object>();
				// info
				if (HttpUtils.isMoblie(request)) {
					info.put("pcypassword", user.getPassword());
					info.put("usertoken", StringUtil.encodeToDes3(user.getId()) + "_" + StringUtil.encodeToDes3(user.getPassword()));
				}
				info.put("userId", user.getId());

				// info.put("sessionId", session.getId());
				// info.put("userInfo",
				// sysUserService.getUserInfoById(ue.getId()));
				result.setSuccess(true);
				result.setObj(info);
				result.setMsg("密码修改成功！");
			}
		}
		return result;

	}

	/**
	 * 验证密码
	 * 
	 * @author binyong
	 * @createtime 2014年6月25日
	 * @Decription
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "checkPassword")
	@ResponseBody
	public AjaxJson checkPassword(HttpServletRequest request, HttpServletResponse response) {
		String password = request.getParameter("param");
		UserEntity user = ClientUtil.getUserEntity();
		if (user == null) {
			/*
			 * String userName = request.getParameter("userName"); user =
			 * userService.getUserByName(userName);
			 */
			UserEntity u = new UserEntity();
			u.setUserName(request.getParameter("userName"));
			u.setPassword(password);
			user = userService.getUserByNameAndPwd(u);
		}
		String old_password = PasswordUtil.encrypt(user.getId(), password, PasswordUtil.getStaticSalt());

		UserPasswordEntity userPasswordEntity = userPasswordService.getUserPasswordByUserId(user.getId());
		if (StringUtils.equals(userPasswordEntity.getPassword(), old_password)) {
			result.setSuccess(true);
			result.setStatus("y");
		} else {
			result.setSuccess(false);
			result.setStatus("n");
			result.setInfo("原密码填写错误");
		}
		return result;

	}

	/**
	 * 验证密码强度
	 * 
	 * @author binyong
	 * @createtime 2014年9月29日
	 * @Decription
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "checkPasswordStrength")
	@ResponseBody
	public AjaxJson checkPasswordStrength(HttpServletRequest request, HttpServletResponse response) {
		String message = "";
		// String password = request.getParameter("oldValue");// 密码等级
		String password = request.getParameter("param");
		/*
		 * SysParameterEntity sp =
		 * sysParameterService.findByCode("passwordStrength"); if
		 * ("low".equals(sp.getValue())) { if (Integer.valueOf(password) < 1) {
		 * message = "密码强度过低，密码强度至少达到弱"; } } else if
		 * ("medium".equals(sp.getValue())) { if (Integer.valueOf(password) < 2)
		 * { message = "密码强度过低，密码强度至少达到中"; } } else if
		 * ("high".equals(sp.getValue())) { if (Integer.valueOf(password) < 3) {
		 * message = "密码强度过低，密码强度必须达到强"; } } else { if
		 * (Integer.valueOf(password) < 1) { message = "密码强度过低，密码强度至少达到弱"; } }
		 */
		if (StringUtils.isEmpty(message)) {
			String psd = request.getParameter("param").toLowerCase();
			UserEntity user = ClientUtil.getUserEntity();
			if (user == null) {
				/*
				 * String userName = request.getParameter("userName"); user =
				 * userService.getUserByName(userName);
				 */
				UserEntity u = new UserEntity();
				u.setUserName(request.getParameter("userName"));
				u.setPassword(password);
				user = userService.getUserByNameAndPwd(u);

			}
			// update by lxt
			if (user.getName() != null && user.getEmail() != null) {
				if (psd.indexOf(user.getName().toLowerCase()) != -1 || psd.indexOf(user.getEmail().toLowerCase()) != -1) {
					message = "密码不能包含用户名或邮箱";
				}
			}
		}
		if (StringUtils.isNotEmpty(message)) {
			result.setStatus("n");
			result.setInfo(message);
		} else {
			result.setStatus("y");
		}
		return result;

	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月4日 下午3:57:17
	 * @Decription 进入资料分类权限维护页
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "dataDirBtnByUserSelect")
	public ModelAndView dataDirBtnByUserSelect(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("userId", request.getParameter("userId"));
		return new ModelAndView("platform/organization/user/dataDirBtnByUserSelect");
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月4日 下午3:57:17
	 * @Decription 进入资料分类操作权限维护页
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "resourceSelectByUser")
	public ModelAndView resourceSelectByUser(HttpServletRequest request, HttpServletResponse response) {
		String typeId = request.getParameter("typeId");
		String userId = request.getParameter("userId");
		// 查找公司资料管理下所有权限
		List<ResourceEntity> resourceList = this.resourceService.queryResourceListByModuleCode("dataManager");
		// 查找用户分类下所有的资源权限
		List<FileTypeUserAuthorityEntity> userAuthorityList = this.resourceService.queryUserFileTypeAuthority(typeId, userId);
		Set<String> resourceAuthorityIds = new HashSet<String>();
		for (FileTypeUserAuthorityEntity entity : userAuthorityList) {
			resourceAuthorityIds.add(entity.getResourceEntity().getId());
		}
		// 设置参数
		request.setAttribute("resourceList", resourceList);// 模块资源
		request.setAttribute("resourceAuthorityIds", resourceAuthorityIds);// 角色模块资源权限
		request.setAttribute("typeId", typeId);
		request.setAttribute("userId", userId);
		return new ModelAndView("platform/organization/user/resourceSelectByUser");
	}

	/**
	 * ============================ 重构用户权限 start
	 * =======================================
	 **/

	/**
	 * 
	 * @author hexj
	 * @createtime 2014年11月13日 下午5:07:29
	 * @Decription 重构用户权限设置
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(params = "userPrivillageSetting")
	public ModelAndView userPrivillageSetting(HttpServletRequest request) {
		String userId = request.getParameter("userId");
		request.setAttribute("userId", userId);
		return new ModelAndView("platform/organization/user/userPrivillageSetting");
	}

	@RequestMapping(params = "moduleAndResourceSelect")
	public ModelAndView moduleAndResourceSelect(HttpServletRequest request) {
		String userId = request.getParameter("userId");
		request.setAttribute("userId", userId);
		return new ModelAndView("platform/organization/user/moduleAndResourceSelect");
	}

	@RequestMapping(params = "getUserModuleTreegrid")
	@ResponseBody
	/*
	 * public List<TreeGrid> getUserModuleTreegrid(HttpServletRequest request,
	 * HttpServletResponse response, TreeGrid treegrid) { String parentId = "";
	 * if (StringUtil.isNotEmpty(request.getParameter("id"))) { parentId =
	 * request.getParameter("id"); } else { parentId = "-1"; } String userId =
	 * request.getParameter("userId");//给用户分配资源 //第一步：查询所有的模块 List<ModuleTreeVo>
	 * moduleAllList =
	 * moduleService.queryModuleAuthorityByUser(request.getParameter("userId"),
	 * parentId); //第二步：得到登陆用户的模块权限 HttpSession session =
	 * ContextHolderUtils.getSession(); Client client =
	 * ClientManager.getInstance().getClient(session.getId()); Map<String,
	 * ModuleTreeVo> clientModuleList = client.getModules(); //第三步：构造模块的权限
	 * List<ModuleTreeVo> moduleList = new ArrayList<ModuleTreeVo>(); if
	 * (moduleAllList != null && moduleAllList.size() > 0) { for (ModuleTreeVo
	 * module : moduleAllList) { if
	 * (clientModuleList.containsKey(module.getId())) { StringBuffer sb = new
	 * StringBuffer(); //查询模块下的资源 1为叶子模块 if ("1".equals(module.getIsLeaf())) {
	 * //模块下所有资源 List<ResourceEntity> resourceList =
	 * resourceService.queryListByModuleId(module.getId()); //该模块下 用户拥有的资源
	 * List<UserResourceEntity> userResources =
	 * resourceService.queryResourceAuthorityByUser(userId, module.getId());
	 * Set<String> userResourcesSet = new HashSet<String>(); for
	 * (UserResourceEntity ur : userResources) { if (ur.getResource() != null) {
	 * userResourcesSet.add(ur.getResource().getId()); } }
	 * 
	 * if (resourceList != null && resourceList.size() > 0) { //构造资源权限选择框
	 * sb.append(getCheckbox("selectAll_" + module.getId(), false,
	 * "全选&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", false)); for (ResourceEntity
	 * resource : resourceList) { sb.append(getCheckbox("resource_" +
	 * module.getId() + "_" + resource.getId(),
	 * userResourcesSet.contains(resource.getId()), resource.getName() +
	 * "&nbsp;&nbsp;", false)); } userResourcesSet.clear(); } else { //叶子模块
	 * 下没有资源权限 ; (用户资源表中有资源权限 ，但资源表中已删除 ,需删除用户资源表中记录。)
	 * sb.append(getCheckbox("selectAll_" + module.getId(), false, "", true)); }
	 * } module.setResourceIds(sb.toString()); //构造模块权限选择框 String checkbox =
	 * getCheckbox("module_" + module.getId(), "1".equals(module.getChecked()),
	 * "", false); module.setName(checkbox + module.getName());
	 * moduleList.add(module); } } } //第四步：树的转换 TreeGridModel dto = new
	 * TreeGridModel(); dto.setIdField("id"); dto.setTextField("name");
	 * dto.setSrc("url"); dto.setIsLeaf("isLeaf");
	 * dto.setResourceIds("resourceIds");
	 * 
	 * return systemService.treegrid(moduleList, dto, null); }
	 */
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年11月15日 上午10:47:22
	 * @Decription 更新用户模块及模块资源权限
	 *
	 * @param request
	 * @return
	 */
	/*
	 * @RequestMapping(params = "updateUserModuleAndResource")
	 * 
	 * @ResponseBody public AjaxJson
	 * updateUserModuleAndResource(HttpServletRequest request) { String userId =
	 * request.getParameter("userId"); String moduleIds =
	 * request.getParameter("moduleIds"); try { result.setMsg("更新模块及资源权限成功");
	 * moduleService.updateUserModuleAuthority(moduleIds, userId); } catch
	 * (Exception e) { result.setMsg("更新模块权限出错"); return result; }
	 * 
	 * String moduleResourceIds = request.getParameter("moduleResourceIds");//
	 * "{moduleId_resourceId},{moduleId_resourceId}" if
	 * (StringUtil.isNotEmpty(moduleResourceIds)) { //将模块对应的资源放入map中 Map<String,
	 * String> moduleResourceMap = new HashMap<String, String>(); for (String mr
	 * : moduleResourceIds.split(",")) { String[] moduleResource =
	 * mr.split("_"); String moduleId = moduleResource[0]; if
	 * (moduleIds.indexOf(moduleId) == -1) { continue; //勾选了操作按钮权限
	 * ，没有勾选模块。不保存勾了的操作按钮权限 } String resourceId = ""; if (moduleResource.length
	 * > 1) { resourceId = moduleResource[1]; } String resourceIdStr =
	 * moduleResourceMap.get(moduleId); if (resourceIdStr != null) {
	 * moduleResourceMap.put(moduleId, resourceIdStr + "," + resourceId); } else
	 * { moduleResourceMap.put(moduleId, resourceId); } }
	 * 
	 * Set<String> keyset = moduleResourceMap.keySet(); try { for (String
	 * moduleId : keyset) {
	 * resourceService.updateUserResourceAuthority(moduleId, userId,
	 * moduleResourceMap.get(moduleId)); } } catch (Exception e) {
	 * result.setMsg("更新资源权限失败"); } } return result; }
	 */
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年11月14日 上午9:34:31
	 * @Decription
	 *
	 * @param id
	 * @param value
	 * @param checked
	 * @param htmlValue
	 * @param displayNone  true 为不显示
	 * @return
	 */
	public String getCheckbox(String id, boolean checked, String htmlValue, boolean isDisplayNone) {
		String display = isDisplayNone == true ? " style='display:none' " : " ";
		String ck = checked == true ? " checked " : " ";
		return "<input type=\"checkbox\" id=\"" + id + "\" " + ck + display + ">" + htmlValue;
	}

	/**
	 * ========================== 重构用户权限 end
	 * =======================================
	 **/

	/**
	 * @author xiaqiang
	 * @createtime 2014年12月1日 上午10:07:07
	 * @Decription 用户excel导入处理
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "userImportSave")
	@ResponseBody
	public AjaxJson userImportSave(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		ExcelImportResult data = (ExcelImportResult) getExcelImportData(request, ClientUtil.getSessionId());
		List list = data.getAllList();
		try {
			result.setMsg("用户数据保存成功");
		} catch (Exception e) {
			result.setMsg("用户数据保存失败");
		}
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2015年4月20日 下午3:31:16
	 * @Decription t:orgMulSelect/t:empSelect 自定义标签Datagrid 用于获取员工的方法
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid4OrgMulSelectTag")
	public void datagrid4OrgMulSelectTag(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		Page<UserEntity> myPage = new Page<UserEntity>(PAGESIZE);
		// 查询条件组装器
		String orgId = request.getParameter("orgId");
		String roleId = request.getParameter("roleId");
		Boolean onlyAuthority = Boolean.parseBoolean(request.getParameter("onlyAuthority"));

		// 自动封装查询条件
		this.buildFilter(myPage, request);
		// 手动添加查询条件
		// 添加排序字段
		// this.buildOrder(page, request, "id", Page.ASC);
		Map<String, Object> map = (Map<String, Object>) myPage.getParameter();
		if (StringUtil.isNotEmpty(orgId)) {
			map.put("orgIds", StringUtil.arrayToListWithQuote(orgId.split(",")));
			myPage.setParameter(map);

			myPage = sysUserService.getUserByCurrentOrgIdsByPage(myPage); // 点击的是部门，获取是点击部门的用户
			// myPage = sysUserService.getUserByOrgIdsByPage(myPage); //
			// 点击的是部门或部门，获取下面的所有员工
		} else if (StringUtil.isNotEmpty(roleId)) {
			map.put("roleIds", roleId.split(","));
			myPage.setParameter(map);
			myPage = sysUserService.getUserByRolesByPage(myPage);
		} /*else {
			UserEntity user = ClientUtil.getUserEntity();
			if (onlyAuthority) {
				// TODO 下面逻辑不可用
				if (this.authorityService.isAdmin(user.getId())) {
					myPage = sysUserService.getAllUserByPage(myPage);
				} else {
					// map.put("empId", user.getUserTypeId());
					// page =
					// sysUserService.getAllEmpUserInAuthorityByPage(page);
					// TODO 这里还要加上用户有权限看到的用户集合
					map.put("orgIds", ClientUtil.getManagerOrgIds());
					myPage.setParameter(map);
					myPage = sysUserService.getUserByOrgIdsByPage(myPage);
				}
			} else {
				// myPage = sysUserService.getAllUserByPage(myPage);
			}
		}*/
		dataGrid.setResults(myPage.getResult());
		dataGrid.setTotal((int) myPage.getTotalCount());
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月28日 上午9:41:03
	 * @Decription 员工选择标签使用
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "userSelect")
	public ModelAndView userSelect(HttpServletRequest request) {
		String userIds = request.getParameter("userIds");
		String displayId = request.getParameter("displayId");
		String displayName = request.getParameter("displayName");
		String hiddenId = request.getParameter("hiddenId");
		String hiddenName = request.getParameter("hiddenName");
		Boolean multiples = Boolean.parseBoolean(request.getParameter("multiples"));

		String treeUrl = request.getParameter("treeUrl");
		String gridUrl = request.getParameter("gridUrl");
		Boolean aysn = Boolean.parseBoolean(request.getParameter("aysn"));
		Boolean onlyAuthority = Boolean.parseBoolean(request.getParameter("onlyAuthority"));
		String orgCode = request.getParameter("orgCode");
		Boolean containSelf = Boolean.parseBoolean(request.getParameter("containSelf"));
		Boolean expandAll = Boolean.parseBoolean(request.getParameter("expandAll"));
		Boolean needBtnSelected = Boolean.parseBoolean(request.getParameter("needBtnSelected"));
		Boolean needBtnSave = Boolean.parseBoolean(request.getParameter("needBtnSave"));
		Boolean afterSaveClose = Boolean.parseBoolean(request.getParameter("afterSaveClose"));
		String saveUrl = request.getParameter("saveUrl");
		String empOrUser = request.getParameter("empOrUser");
		String callback = request.getParameter("callback");
		String showOrgTypes = request.getParameter("showOrgTypes");

		/* 对url进行处理 */
		treeUrl += "&aysn=" + aysn + "&onlyAuthority=" + onlyAuthority + "&orgCode=" + orgCode + "&containSelf=" + containSelf + "&expandAll=" + expandAll;

		List<UserEntity> userList = new ArrayList<UserEntity>();
		if (StringUtil.isNotEmpty(userIds)) {
			String[] array = userIds.split(",");
			for (String id : array) {
				UserEntity entity = userService.get(id);
				userList.add(entity);
			}
		}
		request.setAttribute("userList", userList);
		request.setAttribute("displayId", displayId);
		request.setAttribute("displayName", displayName);
		request.setAttribute("hiddenId", hiddenId);
		request.setAttribute("hiddenName", hiddenName);
		request.setAttribute("multiples", multiples);

		request.setAttribute("treeUrl", treeUrl);
		request.setAttribute("gridUrl", gridUrl + "&onlyAuthority=" + onlyAuthority);
		request.setAttribute("aysn", aysn);
		request.setAttribute("onlyAuthority", onlyAuthority);
		request.setAttribute("orgCode", orgCode);
		request.setAttribute("containSelf", containSelf);
		request.setAttribute("expandAll", expandAll);
		request.setAttribute("needBtnSelected", needBtnSelected);
		request.setAttribute("needBtnSave", needBtnSave);
		request.setAttribute("afterSaveClose", afterSaveClose);
		request.setAttribute("saveUrl", saveUrl);
		request.setAttribute("empOrUser", empOrUser);
		request.setAttribute("callback", callback);
		request.setAttribute("showOrgTypes", showOrgTypes);
		return new ModelAndView("common/tag/org/userSelect");
	}

	/**
	 * 发送验证码
	 * 
	 * @author xiaqiang
	 * @createtime 2015年5月20日 上午10:16:55
	 *
	 */
	@RequestMapping(params = "sendVerifyCode")
	@ResponseBody
	public AjaxJson sendVerifyCode(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		String message = "";
		String phone = request.getParameter("phone");
		boolean isMobile = isMobile(phone);
		if (isMobile) {
			Boolean isExsits = userService.checkUserExsits(phone);
			if (isExsits) {
				message = "该号码已注册";
				result.setSuccess(false);
			} else {
				userService.sendVerifyCode(phone);
				message = "验证码已发送,请查看短信";
			}
		} else {
			message = "请输入有效的手机号码";
			result.setSuccess(false);
		}
		result.setMsg(message);
		return result;
	}

	@RequestMapping(params = "register")
	@ResponseBody
	public AjaxJson register(HttpServletRequest request) throws BusinessException {
		AjaxJson result = new AjaxJson();
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		String verifyCode = request.getParameter("verifyCode");
		Boolean passed = userService.checkVerifyCode(phone, verifyCode);
		if (passed) {
			userService.doRegisterUser(phone, password);
			result.setMsg("注册成功");
		} else {
			result.setMsg("验证码有误");
			result.setSuccess(false);
		}
		return result;
	}

	@RequestMapping(params = "deleteTest")
	@ResponseBody
	public AjaxJson deleteTest(HttpServletRequest request) {
		// String phone = request.getParameter("phone");
		// try {
		// if (jodd.util.StringUtil.isNotBlank(phone)) {
		// UserEntity user = userService.findByProperty("phone", phone);
		// if (user != null) {
		// userService.deleteEntity(user.getId());
		// message = "用户删除成功";
		// }
		// }
		// } catch (Exception e) {
		// message = e.getMessage();
		// }
		// result.setMsg(message);
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月28日 上午9:41:03
	 * @Decription 用户选择标签使用
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "orgMulSelect")
	public ModelAndView orgMulSelect(HttpServletRequest request) {
		String hiddenValue = request.getParameter("hiddenValue");
		String displayId = request.getParameter("displayId");
		String displayName = request.getParameter("displayName");
		String hiddenId = request.getParameter("hiddenId");
		String hiddenName = request.getParameter("hiddenName");
		Boolean multiples = Boolean.parseBoolean(request.getParameter("multiples"));

		String treeUrl = StringUtil.isEmpty(request.getParameter("treeUrl"), "orgnaizationController.do?orgSelectTagTree");
		String gridUrl = StringUtil.isEmpty(request.getParameter("gridUrl"), "userController.do?datagrid4OrgMulSelectTag");
		Boolean aysn = Boolean.parseBoolean(request.getParameter("aysn"));
		Boolean onlyAuthority = Boolean.parseBoolean(request.getParameter("onlyAuthority"));
		String orgId = request.getParameter("orgId");
		String orgCode = request.getParameter("orgCode");
		Boolean containSelf = Boolean.parseBoolean(request.getParameter("containSelf"));
		Boolean expandAll = Boolean.parseBoolean(request.getParameter("expandAll"));
		Boolean needOrg = Boolean.parseBoolean(request.getParameter("needOrg"));
		Boolean needRole = Boolean.parseBoolean(request.getParameter("needRole"));
		Boolean needJob = Boolean.parseBoolean(request.getParameter("needJob"));
		String roleListUrl = StringUtil.isEmpty(request.getParameter("roleListUrl"), "userController.do?getRoleList");
		String jobListUrl = request.getParameter("jobListUrl");
		Boolean needBtnSelected = Boolean.parseBoolean(request.getParameter("needBtnSelected"));
		Boolean needBtnSave = Boolean.parseBoolean(request.getParameter("needBtnSave"));
		Boolean afterSaveClose = Boolean.parseBoolean(request.getParameter("afterSaveClose"));
		String saveUrl = request.getParameter("saveUrl");
		String empOrUser = StringUtil.isEmpty(request.getParameter("empOrUser"), "user");
		String callback = request.getParameter("callback");

		/* 对url进行处理 */
		treeUrl += "&aysn=" + aysn + "&onlyAuthority=" + onlyAuthority + "&orgId=" + orgId + "&orgId=" + orgId + "&orgCode=" + orgCode + "&containSelf="
				+ containSelf + "&expandAll=" + expandAll;

		List<String[]> orgMulList = new ArrayList<String[]>();
		// if (StringUtil.isNotEmpty(hiddenValue)) {
		// String[] array = hiddenValue.split(",");
		// for (String temp : array) {
		// // 值范例:1111^^xx角色^^role
		// String[] tempArray = temp.split("\\^\\^");
		// orgMulList.add(tempArray);
		// }
		// }

		request.setAttribute("hiddenValue", hiddenValue);
		request.setAttribute("orgMulList", orgMulList);
		request.setAttribute("displayId", displayId);
		request.setAttribute("displayName", displayName);
		request.setAttribute("hiddenId", hiddenId);
		request.setAttribute("hiddenName", hiddenName);
		request.setAttribute("multiples", multiples);

		request.setAttribute("treeUrl", treeUrl);
		request.setAttribute("gridUrl", gridUrl + "&onlyAuthority=" + onlyAuthority);
		request.setAttribute("aysn", aysn);
		request.setAttribute("onlyAuthority", onlyAuthority);
		request.setAttribute("orgCode", orgCode);
		request.setAttribute("orgId", orgId);
		request.setAttribute("containSelf", containSelf);
		request.setAttribute("expandAll", expandAll);

		request.setAttribute("needOrg", needOrg);
		request.setAttribute("needRole", needRole);
		request.setAttribute("needJob", needJob);
		request.setAttribute("roleListUrl", roleListUrl);
		request.setAttribute("jobListUrl", jobListUrl);
		request.setAttribute("needBtnSelected", needBtnSelected);
		request.setAttribute("needBtnSave", needBtnSave);
		request.setAttribute("afterSaveClose", afterSaveClose);
		request.setAttribute("saveUrl", saveUrl);
		request.setAttribute("empOrUser", empOrUser);
		request.setAttribute("callback", callback);
		return new ModelAndView("common/tag/org/orgMulSelect");
	}

	@RequestMapping(params = "goSelectMulType")
	public ModelAndView goSelectMulType(HttpServletRequest request) {
		String options = request.getParameter("options");
		request.setAttribute("options", options);
		return new ModelAndView("common/org/selectMulType");
	}

	@RequestMapping(params = "getRoleList")
	@ResponseBody
	public List<RoleEntity> getRoleList(HttpServletRequest request) throws UnsupportedEncodingException {
		String roleName = StringUtil.isEmpty(request.getParameter("name"), "");
		roleName = URLDecoder.decode(roleName, "UTF-8");
		// 查询所有的角色
		List<RoleEntity> roleList = roleService.queryListByName(roleName);
		return roleList;
	}

	/**
	 * 进入头像上传页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "portraitUpload")
	public ModelAndView portraitUpload(HttpServletRequest request) {
		UserEntity user = ClientUtil.getUserEntity();
		request.setAttribute("user", user);
		return new ModelAndView("main/portraitUpload");
	}

	@RequestMapping(params = "uploadPortraitFile")
	@ResponseBody
	public AjaxJson uploadPortraitFile(MultipartHttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();

		String storageType = request.getParameter("storageType");

		String userId = ClientUtil.getUserId();
		String businessKey = StringUtil.isEmpty(request.getParameter("businessKey"), userId);
		String businessType = StringUtil.isEmpty(request.getParameter("businessType"), "UserEntity");
		String businessExtra = StringUtil.isEmpty(request.getParameter("businessExtra"), null);
		String otherKey = StringUtil.isEmpty(request.getParameter("otherKey"), null);
		String otherKeyType = StringUtil.isEmpty(request.getParameter("otherKeyType"), null);

		String x = request.getParameter("portaitUpload_x");
		String y = request.getParameter("portaitUpload_y");
		String w = request.getParameter("portaitUpload_w");
		String h = request.getParameter("portaitUpload_h");
		// 如果没有传入储存的位置,则从配置文件中读取
		if (StringUtils.isEmpty(storageType)) {
			storageType = attachService.getStorageType();
		}
		MultipartFile mpf = null;
		Iterator<String> itr = request.getFileNames();

		Boolean getPathFlag = false; // 是否已经获得各path路径(避免重复获得)
		String datePath = "";
		String afterDocBasePath = "";
		String funPath = "";
		String typePath = "";
		String uploadBasePath = "";
		String thumbnailDirPath = "";
		String relativeUploadPath = "";
		String absoluteUploadPath = "";
		String absoluteFilePath = "";
		String relativeFilePath = "";
		String thumbnailAbPath = "";
		String thumbnailRePath = "";
		String thumbnailAbFilePath = "";
		String thumbnailReFilePath = "";
		String originalFilename = "";
		String remotePath = "";
		String remoteFilePath = "";
		String remoteThumbnailPath = "";
		String remoteThumbnailFilePath = "";
		Map<String, Object> param = new HashMap<String, Object>();
		try {
			while (itr.hasNext()) {
				/* 1.上传时数据参数预处理 */
				mpf = request.getFile(itr.next());
				String md5 = MD5Util.getMD5(mpf.getInputStream()); // 获得md5码判断是否已存在该物理文件

				Boolean isMD5Exsits = attachService.isMD5FileExsits(md5, false, "1");
				originalFilename = mpf.getOriginalFilename();// 原始文件名称
				Long size = mpf.getSize();// 文件大小(字节byte)
				String contentType = mpf.getContentType();// 文件类型
				String ext = null;
				String onlyName = ""; // 纯文件名(不带后缀名)
				// 文件后缀名处理
				if (originalFilename.lastIndexOf(".") >= 0) {
					ext = FileUtils.getExtendWithDot(originalFilename);
					onlyName = FileUtils.getFilePrefix2(originalFilename);
				}
				String attachType = FileUtils.getAttachTypeByExt(ext);
				String attachSizeStr = FileUtils.convertFileSize(size);
				String realUUIDFileName = UUID.randomUUID().toString() + ext;

				// 没有实例化并且相同MD5的物理文件不存在,则需要进行物理文件的创建和拷贝了
				if (!getPathFlag && !isMD5Exsits) {
					afterDocBasePath = attachService.getAfterDocBaseDir();
					datePath = attachService.getDatePath();
					funPath = attachService.getFunPath(null);
					typePath = "";
					uploadBasePath = attachService.getUploadBasePath();
					thumbnailDirPath = attachService.getThumbnailDirName();
					relativeUploadPath = afterDocBasePath + File.separator + funPath + File.separator + typePath + File.separator + datePath + File.separator;
					absoluteUploadPath = uploadBasePath + File.separator + relativeUploadPath;
					thumbnailRePath = afterDocBasePath + File.separator + thumbnailDirPath + File.separator + typePath + File.separator + datePath
							+ File.separator;
					thumbnailAbPath = uploadBasePath + File.separator + thumbnailRePath;

					absoluteFilePath = absoluteUploadPath + realUUIDFileName;
					relativeFilePath = relativeUploadPath + realUUIDFileName;
					thumbnailAbFilePath = thumbnailAbPath + realUUIDFileName;
					thumbnailReFilePath = thumbnailRePath + realUUIDFileName;

					remotePath = File.separator + funPath + File.separator + typePath + File.separator + datePath + File.separator;
					remoteFilePath = remotePath + realUUIDFileName;
					remoteThumbnailPath = File.separator + thumbnailDirPath + File.separator + typePath + File.separator + datePath + File.separator;
					remoteThumbnailFilePath = remoteThumbnailPath + realUUIDFileName;
					getPathFlag = true;
				}

				param.put("mpf", mpf);
				param.put("realUUIDFileName", realUUIDFileName);
				param.put("attachType", attachType);
				param.put("absoluteUploadPath", absoluteUploadPath);
				param.put("thumbnailAbPath", thumbnailAbPath);
				param.put("remotePath", remotePath);
				param.put("remoteThumbnailPath", remoteThumbnailPath);
				param.put("x", x);
				param.put("y", y);
				param.put("w", w);
				param.put("h", h);

				/* 附件通用属性的事先设置(无论用何种存储方式,都需要记录的属性) */
				AttachEntity newAttachEntity = new AttachEntity();
				newAttachEntity.setSrcName(originalFilename);
				newAttachEntity.setAttachName(originalFilename);
				newAttachEntity.setOnlyName(onlyName);
				newAttachEntity.setAttachSize(size);
				newAttachEntity.setExt(ext);
				newAttachEntity.setAttachRemark(null);
				newAttachEntity.setAttachContentType(contentType);
				newAttachEntity.setAttachSizeStr(attachSizeStr);
				newAttachEntity.setAttachType(attachType);
				newAttachEntity.setMD5(md5);

				/* 2.保存物理文件 */
				if ("0".equals(storageType)) {// 如果存储位置是本地
					if (!isMD5Exsits) {
						if (HttpUtils.isMoblie(request)) {
							// 如果是移动端上传头像
							attachService.storeLocalFile(param);
						} else {
							// 如果是网页端上传头像
							attachService.storeLocalPortraitFile(param);
						}

					}
				} else if ("1".equals(storageType)) {// 如果存储位置是FTP服务器
					// 判断相同MD5码文件是否存在,存在就不进行重复存储,不存在才进入存储逻辑
					if (!isMD5Exsits) {
						attachService.storeFTPPortraitFile(param);
					}
				}

				/* 3.保存AttachEntity数据 */
				if ("0".equals(storageType)) {
					newAttachEntity.setStorageType("0");
					// 如果相同MD5码物理文件存在,非首次上传,并且用相同MD5码的附件的各类路径
					if (isMD5Exsits) {
						newAttachEntity.setIsFirstUpload("N");
						List<AttachEntity> attachEntityList = attachService.queryAttachEntityByMD5(md5);
						newAttachEntity.setRelativePath(attachEntityList.get(0).getRelativePath());
						newAttachEntity.setAbsolutePath(attachEntityList.get(0).getAbsolutePath());
						newAttachEntity.setThumbnailAbPath(attachEntityList.get(0).getThumbnailAbPath());
						newAttachEntity.setThumbnailRePath(attachEntityList.get(0).getThumbnailRePath());
					} else {
						newAttachEntity.setIsFirstUpload("Y");
						newAttachEntity.setRelativePath(relativeFilePath.replaceAll("\\\\", "/"));
						newAttachEntity.setAbsolutePath(absoluteFilePath.replaceAll("\\\\", "/"));
						if ("img".equals(attachType) && attachService.isNeedThumbnail()) {
							newAttachEntity.setThumbnailAbPath(thumbnailAbFilePath.replaceAll("\\\\", "/"));
							newAttachEntity.setThumbnailRePath(thumbnailReFilePath.replaceAll("\\\\", "/"));
						}
					}
				} else if ("1".equals(storageType)) {// 如果存储位置是FTP服务器
					/* 3.1 保存FTP附件关联表记录 */
					FTPAttachEntity ftp = new FTPAttachEntity();

					// 如果相同MD5码物理文件存在,非首次上传,并且用相同MD5码的附件的各类路径
					if (isMD5Exsits) {
						newAttachEntity.setIsFirstUpload("N");
						FTPAttachEntity ftpAttachEntity = attachService.queryFTPAttachEntityByMD5(md5);
						ftp.setRemoteFilePath(ftpAttachEntity.getRemoteFilePath());
						ftp.setRemotePath(ftpAttachEntity.getRemotePath());
						ftp.setFileName(ftpAttachEntity.getFileName());
						ftp.setRemoteThumbnailFilePath(ftpAttachEntity.getRemoteThumbnailFilePath());
						ftp.setRemoteThumbnailPath(ftpAttachEntity.getRemoteThumbnailPath());
					} else {
						newAttachEntity.setIsFirstUpload("Y");
						ftp.setRemoteFilePath(remoteFilePath.replaceAll("\\\\", "/"));
						ftp.setRemotePath(remotePath.replaceAll("\\\\", "/"));
						ftp.setFileName(realUUIDFileName);
						if ("img".equals(attachType) && attachService.isNeedThumbnail()) {
							ftp.setRemoteThumbnailFilePath(remoteThumbnailFilePath.replaceAll("\\\\", "/"));
							ftp.setRemoteThumbnailPath(remoteThumbnailPath.replaceAll("\\\\", "/"));
						}
					}
					String storageId = (String) ftpAttachService.save(ftp);

					/* 3.2 设置本方式附件属性 */
					newAttachEntity.setStorageType("1");
					newAttachEntity.setStorageId(storageId);
				}
				newAttachEntity.setTypeEntity(null);
				newAttachEntity.setBusinessKey(businessKey);
				newAttachEntity.setBusinessType(businessType);
				newAttachEntity.setBusinessExtra(businessExtra);
				newAttachEntity.setOtherKey(otherKey);
				newAttachEntity.setOtherKeyType(otherKeyType);
				attachService.save(newAttachEntity);

				/* 5.更新员工表头像字段 */
				UserEntity userEntity = userService.get(userId);
				userEntity.setPortrait(newAttachEntity.getId());
				userService.update(userEntity);
				String foreServerReq = ConfigConst.serverUrl + ConfigConst.attachRequest;
				String portraitUrl = foreServerReq + userEntity.getPortrait();
				j.setMsg("头像上传成功");
				j.setObj(portraitUrl);
			}
		} catch (Exception e) {
			j.setMsg("头像上传失败");
			j.setSuccess(false);
			e.printStackTrace();
		}
		return j;
	}

	// /**
	// * 查询所有的团队
	// * @param request
	// * @return
	// */
	// @RequestMapping(params = "queryAllCompany")
	// @ResponseBody
	// public AjaxJson queryAllCompany(HttpServletRequest request) {
	// try {
	// message="获取团队成功";
	// List<OrgnaizationEntity> orgList =
	// this.sysUserService.getAllOrganization();
	// result.setObj(orgList);
	// } catch (Exception e) {
	// message = e.getMessage();
	// e.printStackTrace();
	// }
	// result.setMsg(message);
	// return result;
	// }

	/**
	 * 加入到团队
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "addToCompany")
	@ResponseBody
	public AjaxJson addToCompany(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		String orgId = request.getParameter("orgId");
		String orgName = request.getParameter("orgName");
		if (StringUtil.isEmpty(orgId)) {
			result.setSuccess(false);
			result.setMsg("没有选择团队");
			return result;
		}
		try {
			this.userOrgService.updateJobUsers(orgId, ClientUtil.getUserId());
			// 如何没有设置那么就设置为默认当前的团队
			UserEntity user = ClientUtil.getUserEntity();
			if (StringUtil.isEmpty(user.getOrgIds())) {
				user.setOrgIds(orgId);
				user.setOrgNames(orgName);
				this.userService.update(user);
				ClientManager.getInstance().getClient().setUser(user);
			}
			result.setMsg("加入成功");
			result.setSuccess(true);
		} catch (Exception e) {
			message = e.getMessage();
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("加入失败");
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 退出团队,或者从部门那里删除成员 删除团队成员
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "drawbackToCompany")
	@ResponseBody
	public AjaxJson drawbackToCompany(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		String orgId = request.getParameter("orgId");
		String deptId = request.getParameter("deptId");
		String userId = request.getParameter("userId");
		if (StringUtil.isEmpty(userId)) {
			userId = ClientUtil.getUserId();
		}
		if (StringUtil.isNotEmpty(deptId)) {
			OrgnaizationEntity dept = this.orgnaizationService.get(deptId);
			if (dept != null) {
				String index = dept.getTreeIndex();
				if (StringUtil.isNotEmpty(index)) {
					String[] ids = index.split(",");
					for (int i = ids.length; i >= 0; i--) {
						OrgnaizationEntity org = this.orgnaizationService.get(ids[i - 1]);
						if (org != null && StringUtil.equals("org", org.getType())) {
							orgId = ids[i - 1];
							break;
						}
					}
				}
			}
		}

		if (StringUtil.isEmpty(orgId)) {
			result.setSuccess(false);
			result.setMsg("没有选择团队");
			return result;
		}
		try {
			boolean flag = this.userService.deleteToCompany(orgId, userId);
			if (flag == true) {
				result.setStatus("2");
			}
			result.setMsg("退出成功");
			result.setSuccess(true);
		} catch (Exception e) {
			message = e.getMessage();
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("退出失败");
		}
		return result;
	}

	/**
	 * 切换团队
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "changeCompany")
	@ResponseBody
	public AjaxJson changeCompany(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		try {
			String orgId = request.getParameter("orgId");
			String orgName = request.getParameter("orgName");
			UserEntity user = ClientUtil.getUserEntity();
			user.setOrgIds(orgId);
			user.setOrgNames(orgName);
			this.userService.update(user);
			ClientManager.getInstance().getClient().setUser(user);
			result.setObj(null);
			result.setMsg("切换团队成功");
			result.setSuccess(true);
		} catch (Exception e) {
			message = e.getMessage();
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("切换失败");
		}
		return result;
	}

	/**
	 * 获取所有自己所在的所有的组织
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "queryCompany")
	@ResponseBody
	public AjaxJson queryCompany(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		try {
			message = "查询团队成功";
			List<OrgnaizationEntity> list = userService.queryCompany(ClientUtil.getUserId());
			result.setObj(list);
		} catch (Exception e) {
			message = e.getMessage();
			e.printStackTrace();
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 获取一个机构下自己所在的部门
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "queryDepts")
	@ResponseBody
	public AjaxJson queryDepts(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		try {
			String orgId = request.getParameter("orgId");
			String userId = request.getParameter("userId");
			List<OrgnaizationEntity> orgList = this.userOrgService.getDeptsByOrg(userId, orgId);
			// String orgIds="";
			// String orgNames="";
			// if(userOrgList!=null && userOrgList.size()>0){
			// for(OrgnaizationEntity org:userOrgList){
			// orgIds+=orgIds+org.getId()+",";
			// orgNames+=orgNames+org.getName()+",";
			// }
			// orgIds=StringUtil.removeDot(orgIds);
			// orgNames=StringUtil.removeDot(orgNames);
			// }
			// Map<String,String> map=new HashMap<String,String>();
			// map.put("orgIds", orgIds);
			// map.put("orgNames", orgNames);
			result.setObj(orgList);
			result.setMsg("获取成功");
		} catch (Exception e) {
			message = e.getMessage();
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取所有自己所在的所有的组织
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "queryHomeCompany")
	@ResponseBody
	public AjaxJson queryHomeCompany(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		try {
			message = "获取团队成功";
			List<OrgnaizationEntity> list = userService.queryHomeCompany(ClientUtil.getUserId());
			result.setObj(list);
		} catch (Exception e) {
			message = e.getMessage();
			e.printStackTrace();
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 通过用户名模糊查询用户集合
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "searchUser")
	@ResponseBody
	public AjaxJson searchUser(HttpServletRequest request) {
		String name = request.getParameter("name");
		List<UserEntity> userList = userService.queryUsersByLikeName(name);
		result.setObj(userList);
		return result;
	}

	/**
	 * 通过用户手机查询用户是否注册用户
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "isRegister")
	@ResponseBody
	public AjaxJson isRegister(HttpServletRequest request) throws BusinessException {
		String phones = request.getParameter("phones");
		List<Map<Object, Object>> userList = new ArrayList<Map<Object, Object>>();

		if (StringUtil.isNotEmpty(phones)) {
			userList = this.userService.isRegister(phones);
			result.setObj(userList);
			result.setMsg("获取注册用户成功");
			result.setSuccess(true);
		} else {
			result.setObj(null);
			result.setMsg("请传递phones参数");
			result.setSuccess(false);
		}

		return result;
	}

	/**
	 * 修改昵称
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "changeNickname")
	@ResponseBody
	public AjaxJson changeNickname(HttpServletRequest request) throws Exception {
		String name = request.getParameter("name");
		String userId = ClientUtil.getUserId();
		userService.doChangeNickname(userId, name);
		result.setMsg("修改昵称成功");
		result.setSuccess(true);
		return result;
	}

	/**
	 * 获取用户列表
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryUserList")
	@ResponseBody
	public AjaxJson queryUserList(HttpServletRequest request) throws Exception {
		result.setObj(this.userService.queryList());
		return result;
	}

	/**
	 * 修改手机
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "changePhone")
	@ResponseBody
	public AjaxJson changePhone(HttpServletRequest request) throws Exception {
		// todo
		return result;
	}

	/**
	 * 进入客户版用户选择页
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "userSelectPage")
	public ModelAndView userSelectPage(HttpServletRequest request) throws Exception {
		String userId = ClientUtil.getUserId();
		List<Map<String, Object>> list = this.orgGroupservice.queryUsers(userId);
		request.setAttribute("users", list);
		return new ModelAndView("main/user/userSelectPage");
	}

	/**
	 * 找回密码发送验证码
	 * 
	 * @author lixt
	 * @createtime 2015年11月11日 上午10:16:55
	 *
	 */
	@RequestMapping(params = "sendSmsChangPassword")
	@ResponseBody
	public AjaxJson sendSmsChangPassword(UserVerifycodeEntity verifycode, HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		String message = "";
		String phone = request.getParameter("phone");
		boolean isMobile = isMobile(phone);
		if (isMobile) {
			Boolean isExsits = userService.checkUserExsits(phone);
			if (isExsits) {
				// userService.sendVerifyCode(phone);
				// 第二步：获取验证码
				if (StringUtil.isEmpty(verifycode.getModuleFlag())) {
					verifycode.setModuleFlag("findPassword");
				}
				userVerifycodeService.sendVerifyCode(verifycode);
				message = "验证码已发送,请查看短信";
			} else {
				message = "该用户不存在";
				result.setSuccess(false);
			}
		} else {
			message = "请输入有效的手机号码";
			result.setSuccess(false);
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 根据key模糊查询用户
	 * 
	 * @author lixt
	 * @throws UnsupportedEncodingException
	 * @createtime 2015年11月11日 上午10:16:55
	 *
	 */
	@RequestMapping(params = "queryUsersByLikeKey")
	@ResponseBody
	public AjaxJson queryUsersByLikeKey(HttpServletRequest request) throws UnsupportedEncodingException {
		AjaxJson result = new AjaxJson();
		String userId = ClientUtil.getUserId();
		String key = URLDecoder.decode(request.getParameter("key"), "utf-8");
		try {
			List<Map<String, Object>> userMap = this.userService.queryUsersByLikeKey(key, userId);
			result.setObj(userMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String message = "获取成功";
		result.setMsg(message);
		result.setSuccess(true);
		return result;
	}

	/**
	 * 找回密码设置新密码
	 * 
	 * @author lixt
	 * @createtime 2015年11月11日 上午10:16:55
	 *
	 */
	@RequestMapping(params = "forgetChangePassword")
	@ResponseBody
	public AjaxJson forgetChangePassword(HttpServletRequest request) throws Exception {
		AjaxJson result = new AjaxJson();
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		String verifyCode = request.getParameter("verifyCode");
		if (StringUtil.isNotEmpty(phone) && StringUtil.isNotEmpty(password) && StringUtil.isNotEmpty(verifyCode)) {
			result = this.userService.doForgetChangePassword(phone, password, verifyCode);
			// 设置usertoken
			if (result.isSuccess()) {
				// info
				if (HttpUtils.isMoblie(request)) {
				} else {
					// 如果不是移动端请求，则不返回token
					result.setObj(null);
				}

			}
		} else {
			result.setMsg("参数传入有误");
			result.setSuccess(true);
		}

		return result;
	}

	/**
	 * 手机号码正则校验
	 * */
	public boolean isMobile(String mobile) {
		return Pattern.matches(BusinessConst.REGEX_MOBILE, mobile);
	}

	@RequestMapping(params = "testApp")
	public ModelAndView testApp(HttpServletRequest request) {
		return new ModelAndView("main/changePassword");
	}

}
