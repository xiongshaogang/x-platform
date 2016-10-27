package com.xplatform.base.orgnaization.role.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.common.model.json.TreeGrid;
import com.xplatform.base.framework.core.common.model.json.TreeNode;
import com.xplatform.base.framework.core.common.service.CommonService;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.MapKit;
import com.xplatform.base.framework.core.util.MybatisTreeMapper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.TreeMapper;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.framework.tag.vo.easyui.TreeGridModel;
import com.xplatform.base.orgnaization.module.mybatis.vo.ModuleTreeVo;
import com.xplatform.base.orgnaization.module.service.ModuleService;
import com.xplatform.base.orgnaization.orggroup.entity.OrgGroupEntity;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgRoleEntity;
import com.xplatform.base.orgnaization.orgnaization.service.OrgRoleService;
import com.xplatform.base.orgnaization.resouce.entity.ResourceEntity;
import com.xplatform.base.orgnaization.resouce.service.ResourceService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.role.entity.RoleResourceEntity;
import com.xplatform.base.orgnaization.role.mybatis.vo.RoleVo;
import com.xplatform.base.orgnaization.role.service.RoleService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.entity.UserRoleEntity;
import com.xplatform.base.orgnaization.user.service.UserRoleService;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.system.type.entity.FileTypeRoleAuthorityEntity;
import com.xplatform.base.system.type.entity.TypeRoleEntity;
import com.xplatform.base.system.type.service.TypeRoleService;

/**
 * 
 * description : 角色管理controller
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
@RequestMapping("/roleController")
public class RoleController extends BaseController {

	@Resource
	private RoleService roleService;

	@Resource
	private ModuleService moduleService;

	@Resource
	private ResourceService resourceService;

	@Resource
	private UserRoleService userRoleService;

	@Resource
	private AuthorityService authorityService;

	@Resource
	private TypeRoleService typeRoleService;

	@Resource
	private OrgRoleService orgRoleService;

	@Resource
	private SysUserService sysUserService;
	@Resource
	private CommonService commonService;

	private Page<RoleVo> page = new Page<RoleVo>(PAGESIZE);

	public void setAuthorityService(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}

	private AjaxJson result = new AjaxJson();

	private String message;

	/**
	 * 角色管理列表页跳转
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "role")
	public ModelAndView Role(HttpServletRequest request) {
		return new ModelAndView("platform/organization/role/roleList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param Role
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(RoleEntity role, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		// 自定义追加查询条件
		UserEntity user = ClientUtil.getUserEntity();
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", user.getId());
		CriteriaQuery cq = new CriteriaQuery(RoleEntity.class, dataGrid);
		if (this.authorityService.isAdmin(param)) {// 不是管理员，进行权限过滤
			
			// 查询条件组装器
			HqlGenerateUtil.installHql(cq, role, request.getParameterMap());
			this.roleService.getDataGridReturn(cq, true);
		} else {
			// 查询条件组装器
			HqlGenerateUtil.installHql(cq, role, request.getParameterMap());
			this.roleService.getDataGridReturn(cq, true);
			// // 自动封装查询条件
			// this.buildFilter(page, request);
			// // 手动添加查询条件
			// Map<String, Object> param1 = (Map<String, Object>)
			// page.getParameter();
			// param1.put("userId", user.getId());
			// // param1.put("empId", user.getUserTypeId());
			// List<String> roleIds = new ArrayList<String>();
			// if (client.getRoleList() != null && client.getRoleList().size()
			// != 0) {
			// for (RoleEntity r : client.getRoleList()) {
			// roleIds.add(r.getId());
			// }
			// }
			// param1.put("roleIds", StringUtil.addQuotes(roleIds));
			//
			// page.setParameter(param1);
			// // 添加排序字段
			// this.buildOrder(page, request, "createTime", Page.DESC);
			// page = roleService.queryAuthorityRoleList(page);
			// dataGrid.setResults(page.getResult());
			// dataGrid.setTotal((int) page.getTotalCount());
		}
		
		List<RoleEntity> queryList=dataGrid.getResults();
		List<RoleEntity> defaultList=roleService.queryDefaultRole();
		queryList.addAll(defaultList);
		dataGrid.setResults(queryList);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 角色删除
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param Role
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(RoleEntity role, HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", ClientUtil.getUserId());
		boolean isAdmin = authorityService.isAdmin(param);
		RoleEntity roleEntity = this.roleService.get(role.getId());
		if(roleEntity.getDefinedFlag() == 3){
			if(!isAdmin){
				result.setMsg("非管理员不能操作公共角色");
				return result;
			}
		}
		message = "角色删除成功";
		try {
			roleService.delete(role.getId());
		} catch (Exception e) {
			message = "角色删除失败";
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 批量删除角色
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:27:15
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		String ids = request.getParameter("ids");
		try {
			roleService.batchDelete(ids);
			message = "删除成功";
		} catch (Exception e) {
			message = "删除失败";
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 进入新增或者修改查看页面
	 * 
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "roleEdit")
	public ModelAndView RoleEdit(RoleEntity role, Model model) {
		if (StringUtil.isNotEmpty(role.getId())) {
			role = roleService.get(role.getId());
			model.addAttribute("role", role);
		}
		return new ModelAndView("platform/organization/role/roleEdit");
	}

	/**
	 * 新增或修改角色
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param Role
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "saveOrUpdateRole")
	@ResponseBody
	public AjaxJson saveOrUpdateRole(RoleEntity Role,HttpServletRequest request) throws BusinessException {
		AjaxJson result = new AjaxJson();
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", ClientUtil.getUserId());
		boolean isAdmin = authorityService.isAdmin(param);
		String userIds = request.getParameter("userIds");
		if(StringUtil.isNotEmpty(userIds)){
			String[] idArr = StringUtil.split(userIds, ",");
			if(idArr.length <= 1){
				result.setMsg("userId不能为空");
				return result;
			}
		}
		try {
			if(Role.getDefinedFlag() == 3){
				if(!isAdmin){
					result.setMsg("非管理员不能操作公共角色");
					return result;
				}
			}
			if (StringUtil.isNotEmpty(Role.getId())) {
				message = "角色更新成功";
				roleService.update(Role);
			} else {
				message = "角色新增成功";
				roleService.save(Role,userIds);
			}
		} catch (Exception e) {
			message = "机构操作失败";
		}
		result.setMsg(message);
		return result;
	}

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
		String roleId = request.getParameter("roleId");
		request.setAttribute("roleId", roleId);
		return new ModelAndView("platform/organization/role/moduleSelect");
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
		String roleId = request.getParameter("roleId");
		String parentId = "";
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			parentId = request.getParameter("id");
		} else {
			parentId = "-1";
		}
		// 第一步：查询所有的模块
		List<ModuleTreeVo> moduleAllList = moduleService.queryModuleAuthorityByRole(roleId, parentId);
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
	@RequestMapping(params = "updateModuleAuthority")
	@ResponseBody
	public AjaxJson updateModuleAuthority(HttpServletRequest request, HttpServletResponse response) {
		String moduleIds = request.getParameter("roleModules");
		String roleId = request.getParameter("roleId");
		result.setMsg("更新角色模块权限成功");
		try {
			moduleService.updateRoleModuleAuthority(moduleIds, roleId);
		} catch (BusinessException e) {
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 按钮权限展示
	 * 
	 * @param request
	 * @param functionId
	 * @param roleId
	 * @return
	 */
	@RequestMapping(params = "resourceSelect")
	public ModelAndView resourceSelect(HttpServletRequest request, String moduleId, String roleId) {
		// 模块所有的资源
		List<ResourceEntity> resourceList = this.resourceService.queryListByModuleId(moduleId);
		// 角色模块所有的资源权限
		List<RoleResourceEntity> resourceAuthorityList = this.resourceService.queryResourceAuthorityByRole(roleId, moduleId);
		Set<String> resourceAuthorityIds = new HashSet<String>();
		for (RoleResourceEntity roleResourceEntity : resourceAuthorityList) {
			resourceAuthorityIds.add(roleResourceEntity.getResource().getId());
		}
		// 设置参数
		request.setAttribute("resourceList", resourceList);// 模块资源
		request.setAttribute("resourceAuthorityIds", resourceAuthorityIds);// 角色模块资源权限
		request.setAttribute("moduleId", moduleId);
		return new ModelAndView("platform/organization/role/resourceSelect");
	}

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
	@RequestMapping(params = "updateResourceAuthority")
	@ResponseBody
	public AjaxJson updateResourceAuthority(HttpServletRequest request, HttpServletResponse response) {
		String moduleId = request.getParameter("moduleId");
		String roleId = request.getParameter("roleId");
		String resourceIds = request.getParameter("resourceIds");
		result.setMsg("更新角色资源权限成功");
		try {
			this.resourceService.updateRoleResourceAuthority(moduleId, roleId, resourceIds);
		} catch (Exception e) {
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 分配用户
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "distributionUser")
	public ModelAndView distributionUser(HttpServletRequest request) {
		String distributionid = request.getParameter("id");
		String distributionFlag = request.getParameter("distributionFlag");
		if (StringUtil.isNotEmpty(distributionid)) {
			// 角色分配用户
			if (StringUtil.isNotEmpty(distributionFlag) && distributionFlag.equals("roleDistribution")) {
				List<UserRoleEntity> userList = userRoleService.queryUserRoleByRoleIdList(distributionid);
				request.setAttribute("distributionid", distributionid);
				request.setAttribute("userList", userList);
			}
		}
		request.setAttribute("distributionFlag", distributionFlag);
		return new ModelAndView("common/distributionUserList");
	}

	/**
	 * 保存分配用户
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "saveDistributionUser")
	@ResponseBody
	public AjaxJson saveDistributionUser(HttpServletRequest request) {
		String distributionid = request.getParameter("distributionid");
		String distributionFlag = request.getParameter("distributionFlag");
		String userIds = request.getParameter("userIds");
		try {
			// 角色分配用户
			if (StringUtil.isNotEmpty(distributionFlag) && distributionFlag.equals("roleDistribution")) {
				userRoleService.updateRoleUser(userIds, distributionid);
			}
			message = "添加成功";
		} catch (Exception e) {
			e.printStackTrace();
			message = "添加失败";
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 机构、部门、岗位通过组织id获得已分配角色ids
	 * 
	 * @author xiaqiang
	 * @createtime 2015年4月22日 下午4:40:47
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "getRoleIds4Orgs")
	@ResponseBody
	public String getRoleIds4Orgs(HttpServletRequest request) {
		String orgId = request.getParameter("orgId");
		String roleIds = "";
		if (StringUtil.isNotEmpty(orgId)) {
			StringBuffer sb = new StringBuffer();
			List<OrgRoleEntity> orgRoleList = orgRoleService.queryOrgRoleByOrgIdList(orgId);
			for (OrgRoleEntity or : orgRoleList) {
				String roleId = or.getRole().getId();
				sb.append(roleId).append(",");
			}
			roleIds = StringUtil.removeDot(sb.toString());
		}
		return roleIds;
	}

	/**
	 * 用户组分配角色
	 * 
	 * @author luoheng
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "distributionRole")
	public ModelAndView distributionRole(HttpServletRequest request) {
		String distributionid = request.getParameter("id");
		String distributionFlag = request.getParameter("distributionFlag");
		if (StringUtil.isNotEmpty(distributionid)) {
			/*
			 * //用户组分配角色 if(StringUtil.isNotEmpty(distributionFlag) &&
			 * distributionFlag.equals("groupDistribution")){
			 * List<GroupRoleEntity> groupRoleList =
			 * groupRoleService.queryGroupRoleByGroupIdList(distributionid);
			 * request.setAttribute("distributionid", distributionid);
			 * request.setAttribute("roleList", groupRoleList); }
			 * 
			 * //岗位分配角色 if(StringUtil.isNotEmpty(distributionFlag) &&
			 * distributionFlag.equals("jobDistribution")){ List<JobRoleEntity>
			 * jobRoleList =
			 * jobRoleService.queryJobRoleByJobIdList(distributionid);
			 * request.setAttribute("distributionid", distributionid);
			 * request.setAttribute("roleList", jobRoleList); }
			 * 
			 * //机构、部门分配角色 if(StringUtil.isNotEmpty(distributionFlag) &&
			 * distributionFlag.equals("deptDistribution")){
			 * List<DeptRoleEntity> deptRoleList =
			 * deptRoleService.queryDeptRoleByDeptIdList(distributionid);
			 * request.setAttribute("distributionid", distributionid);
			 * request.setAttribute("roleList", deptRoleList); }
			 */

			// 部门、岗位分配角色
			if (StringUtil.isNotEmpty(distributionFlag) && distributionFlag.equals("orgDistribution")) {
				List<OrgRoleEntity> orgRoleList = orgRoleService.queryOrgRoleByOrgIdList(distributionid);
				request.setAttribute("distributionid", distributionid);
				request.setAttribute("roleList", orgRoleList);
			}

			// 用户配角色
			if (StringUtil.isNotEmpty(distributionFlag) && distributionFlag.equals("userDistribution")) {
				// EmpDeptEntity empDept = empDeptService.get(distributionid);
				List<UserRoleEntity> userRoleList = userRoleService.queryUserRoleByUserIdList(distributionid);
				request.setAttribute("distributionid", distributionid);
				request.setAttribute("roleList", userRoleList);
			}

			// 系统类型分配角色
			if (StringUtil.isNotEmpty(distributionFlag) && distributionFlag.equals("typeDistribution")) {
				List<TypeRoleEntity> typeRoleList = typeRoleService.queryTypeRoleByTypeIdList(distributionid);
				request.setAttribute("distributionid", distributionid);
				request.setAttribute("roleList", typeRoleList);
			}
		}
		request.setAttribute("distributionFlag", distributionFlag);
		return new ModelAndView("common/distributionRoleList");
	}

	/**
	 * 保存用户组角色信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "saveDistributionOrgRole")
	@ResponseBody
	public AjaxJson saveDistributionOrgRole(HttpServletRequest request) {
		String roleIds = request.getParameter("ids");
		String orgId = request.getParameter("orgId");

		try {
			orgRoleService.updateOrgRole(roleIds, orgId);
			result.setMsg("分配成功");
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setMsg("分配失败");
		}
		return result;
	}

	/**
	 * 保存用户组角色信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "saveDistributionRole")
	@ResponseBody
	public AjaxJson saveDistributionRole(HttpServletRequest request) {
		String roleIds = request.getParameter("roleIds");
		String distributionid = request.getParameter("distributionid");
		String distributionFlag = request.getParameter("distributionFlag");
		try {
			/*
			 * //用户组分配角色 if(StringUtil.isNotEmpty(distributionFlag) &&
			 * distributionFlag.equals("groupDistribution")){
			 * groupRoleService.updateUserRole(roleIds, distributionid); }
			 * //岗位分配角色 if(StringUtil.isNotEmpty(distributionFlag) &&
			 * distributionFlag.equals("jobDistribution")){
			 * jobRoleService.updateJobRole(roleIds, distributionid); }
			 * 
			 * //部门角色分配 if(StringUtil.isNotEmpty(distributionFlag) &&
			 * distributionFlag.equals("deptDistribution")){
			 * deptRoleService.updateDeptRole(roleIds, distributionid); }
			 */

			// 部门、岗位角色分配
			if (StringUtil.isNotEmpty(distributionFlag) && distributionFlag.equals("orgDistribution")) {
				orgRoleService.updateOrgRole(roleIds, distributionid);
			}

			// 用户角色分配
			if (StringUtil.isNotEmpty(distributionFlag) && distributionFlag.equals("userDistribution")) {
				userRoleService.updateUserRole(roleIds, distributionid);
			}

			// 系统类型角色分配
			if (StringUtil.isNotEmpty(distributionFlag) && distributionFlag.equals("typeDistribution")) {
				typeRoleService.updateTypeRole(roleIds, distributionid);
			}
			message = "添加成功";
		} catch (Exception e) {
			e.printStackTrace();
			message = "添加失败";
		}
		result.setMsg(message);
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
	@RequestMapping(params = "dataDirBtnByRoleSelect")
	public ModelAndView dataDirBtnByRoleSelect(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("roleId", request.getParameter("roleId"));
		return new ModelAndView("platform/organization/role/dataDirBtnByRoleSelect");
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
	@RequestMapping(params = "resourceSelectByRole")
	public ModelAndView resourceSelectByRole(HttpServletRequest request, HttpServletResponse response) {
		String typeId = request.getParameter("typeId");
		String roleId = request.getParameter("roleId");
		// 查找公司资料管理下所有权限
		List<ResourceEntity> resourceList = this.resourceService.queryResourceListByModuleCode("dataManager");
		// 查找用户分类下所有的资源权限
		List<FileTypeRoleAuthorityEntity> roleAuthorityList = this.resourceService.queryRoleFileTypeAuthority(typeId, roleId);
		Set<String> resourceAuthorityIds = new HashSet<String>();
		for (FileTypeRoleAuthorityEntity entity : roleAuthorityList) {
			resourceAuthorityIds.add(entity.getResourceEntity().getId());
		}
		// 设置参数
		request.setAttribute("resourceList", resourceList);// 模块资源
		request.setAttribute("resourceAuthorityIds", resourceAuthorityIds);// 角色模块资源权限
		request.setAttribute("typeId", typeId);
		request.setAttribute("roleId", roleId);
		return new ModelAndView("platform/organization/role/resourceSelectByRole");
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月24日 上午10:46:18
	 * @Decription 角色选择标签使用
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "roleSelect")
	public ModelAndView roleSelect(HttpServletRequest request) {
		// 获得","隔开的当前选中值
		String roleIds = request.getParameter("roleIds");
		String displayId = request.getParameter("displayId");
		String displayName = request.getParameter("displayName");
		String hiddenId = request.getParameter("hiddenId");
		String hiddenName = request.getParameter("hiddenName");
		Boolean multiples = Boolean.parseBoolean(request.getParameter("multiples"));
		Boolean needBtnSelected = Boolean.parseBoolean(request.getParameter("needBtnSelected"));
		Boolean needBtnSave = Boolean.parseBoolean(request.getParameter("needBtnSave"));
		Boolean afterSaveClose = Boolean.parseBoolean(request.getParameter("afterSaveClose"));
		String saveUrl = request.getParameter("saveUrl");

		List<RoleEntity> roleList = new ArrayList<RoleEntity>();
		if (StringUtil.isNotEmpty(roleIds)) {
			String[] array = roleIds.split(",");
			for (String id : array) {
				RoleEntity entity = roleService.get(id);
				roleList.add(entity);
			}
		}
		request.setAttribute("roleList", roleList);
		request.setAttribute("displayId", displayId);
		request.setAttribute("displayName", displayName);
		request.setAttribute("hiddenId", hiddenId);
		request.setAttribute("hiddenName", hiddenName);
		request.setAttribute("multiples", multiples);
		request.setAttribute("needBtnSelected", needBtnSelected);
		request.setAttribute("needBtnSave", needBtnSave);
		request.setAttribute("afterSaveClose", afterSaveClose);
		request.setAttribute("saveUrl", saveUrl);
		return new ModelAndView("common/tag/org/roleSelect");
	}


	@RequestMapping(params = "rolePrivillageSetting")
	public ModelAndView rolePrivillageSetting(HttpServletRequest request) {
		String roleId = request.getParameter("roleId");
		request.setAttribute("roleId", roleId);
		return new ModelAndView("platform/organization/role/rolePrivillageSetting");
	}

	@RequestMapping(params = "moduleAndResourceSelect")
	public ModelAndView moduleAndResourceSelect(HttpServletRequest request) {
		String roleId = request.getParameter("roleId");
		request.setAttribute("roleId", roleId);
		return new ModelAndView("platform/organization/role/moduleAndResourceSelect");
	}

	/**
	 * 
	 * @author hexj
	 * @createtime 2014年11月20日 上午11:21:01
	 * @Decription 获取角色模块 及模块资源权限treegrid
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "getRoleModuleTreegrid")
	@ResponseBody
	public List<TreeGrid> getRoleModuleTreegrid(HttpServletRequest request) {
		String roleId = request.getParameter("roleId");
		String parentId = request.getParameter("id");
		if (StringUtil.isEmpty(parentId)) {
			parentId = "-1";
		}
		// 第一步：查询所有的模块
		List<ModuleTreeVo> moduleAllList = moduleService.queryModuleAuthorityByRole(roleId, parentId);
		// 第二步：得到登陆用户的模块权限
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		Map<String, ModuleTreeVo> clientModuleList = client.getModules();
		// 第三步：构造模块的权限
		List<ModuleTreeVo> moduleList = new ArrayList<ModuleTreeVo>();
		if (moduleAllList != null && moduleAllList.size() > 0) {
			for (ModuleTreeVo module : moduleAllList) {
				if (clientModuleList.containsKey(module.getId())) {
					if ("1".equals(module.getIsLeaf())) { // 1 叶子节点
						// 获取模块下的资源
						List<ResourceEntity> allResources = resourceService.queryListByModuleId(module.getId());
						// 角色拥有该模块下的资源
						List<RoleResourceEntity> roleResources = resourceService.queryResourceAuthorityByRole(roleId, module.getId());
						Set<String> roleResourcesSet = new HashSet<String>();
						for (RoleResourceEntity roleResource : roleResources) {
							if (roleResource.getResource() != null) {
								roleResourcesSet.add(roleResource.getResource().getId());
							}
						}

						StringBuffer resourceIds = new StringBuffer("");
						if (allResources != null && allResources.size() > 0) {
							resourceIds.append(getCheckbox("selectAll_" + module.getId(), false, "全选&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", false));
							for (ResourceEntity resource : allResources) {
								resourceIds.append(getCheckbox("resource_" + module.getId() + "_" + resource.getId(),
										roleResourcesSet.contains(resource.getId()), resource.getName() + "&nbsp;&nbsp;&nbsp;", false));
							}
						} else {
							resourceIds.append(getCheckbox("selectAll_" + module.getId(), false, "", true));
						}
						module.setResourceIds(resourceIds.toString());
						roleResourcesSet.clear();
					}
					module.setName(getCheckbox("module_" + module.getId(), "1".equals(module.getChecked()), module.getName(), false));
					moduleList.add(module);
				}
			}
		}

		// 第四步：树的转换
		TreeGridModel dto = new TreeGridModel();
		dto.setIdField("id");
		dto.setTextField("name");
		dto.setSrc("url");
		dto.setIsLeaf("isLeaf");
		dto.setResourceIds("resourceIds");

		return commonService.treegrid(moduleList, dto, null);
	}

	/**
	 * 
	 * @author hexj
	 * @createtime 2014年11月20日 上午11:19:23
	 * @Decription 更新角色模块 及资源权限
	 *
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "updateRoleModuleAndResource")
	@ResponseBody
	public AjaxJson updateRoleModuleAndResource(HttpServletRequest request) throws BusinessException {
		String roleId = request.getParameter("roleId");
		String moduleIds = request.getParameter("moduleIds");
		try {
			// 1.更新模块相关数据
			moduleService.updateRoleModuleAuthority(moduleIds, roleId);
			// 2.更新资源相关数据
			String moduleResourceIds = request.getParameter("moduleResourceIds");// "{moduleId_resourceId},{moduleId_resourceId}"
			if (StringUtil.isNotEmpty(moduleResourceIds)) {
				// 将模块对应的资源放入map中
				Map<String, String> moduleResourceMap = new HashMap<String, String>();
				for (String mr : moduleResourceIds.split(",")) {
					String[] moduleResource = mr.split("_");
					String moduleId = moduleResource[0];
					if (moduleIds.indexOf(moduleId) == -1) {
						continue; // 勾选了操作按钮权限 ，没有勾选模块。不保存勾了的操作按钮权限
					}
					String resourceId = "";
					if (moduleResource.length > 1) {
						resourceId = moduleResource[1];
					}
					String resourceIdStr = moduleResourceMap.get(moduleId);
					if (resourceIdStr != null) {
						moduleResourceMap.put(moduleId, resourceIdStr + "," + resourceId);
					} else {
						moduleResourceMap.put(moduleId, resourceId);
					}
				}

				Set<String> keyset = moduleResourceMap.keySet();
				for (String moduleId : keyset) {
					resourceService.updateRoleResourceAuthority(moduleId, roleId, moduleResourceMap.get(moduleId));
				}
				moduleResourceMap.clear();
			}
		} catch (Exception e) {
			ExceptionUtil.throwBusinessException("更新模块权限出错");
		}
		result.setMsg("更新模块及资源权限成功");
		return result;
	}

	public String getCheckbox(String id, boolean checked, String htmlValue, boolean isDisplayNone) {
		String display = isDisplayNone == true ? " style='display:none' " : " ";
		String ck = checked == true ? " checked " : " ";
		return "<label><input type=\"checkbox\" id=\"" + id + "\" " + ck + display + ">" + "<span class=\"text\">"+htmlValue+"</span></label>";
	}
	
	@RequestMapping(params = "queryUsersByRole")
	@ResponseBody
	public AjaxJson queryUsersByRole(HttpServletRequest request) throws BusinessException {
		AjaxJson result=new AjaxJson();
		String roleId=request.getParameter("roleId");
		if(StringUtil.isNotEmpty(roleId)){
			String userIds=roleService.queryUsersByRole(roleId);
			result.setObj(userIds);
			result.setMsg(null);
		}
		return result;
	}

	@RequestMapping(params = "userToRoleSelect")
	public ModelAndView userToRoleSelect(HttpServletRequest request) {
		String roleId = request.getParameter("roleId");
		request.setAttribute("roleId", roleId);
		return new ModelAndView("platform/organization/role/userToRoleSelect");
	}
	
	/**
	 * 添加角色
	 * 
	 * @param request
	 */
	@RequestMapping(params = "saveUserRole")
	@ResponseBody
	public AjaxJson saveUserRole(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		String message = "";
		try {
			String roleId = request.getParameter("roleId");// 角色Id
			String userIds = request.getParameter("ids");// 用户Ids
			roleService.batchUpdateUserRole(roleId, userIds);
			message = "文件角色权限更新成功";
		} catch (Exception e) {
			message = "文件角色权限更新失败";
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 查询某个用户的所有私人角色
	 * 
	 * @param request
	 */
	@RequestMapping(params = "queryMyRole")
	@ResponseBody
	public AjaxJson queryMyRole(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		String message = "";
		String userId = ClientUtil.getUserId();
		try {
			result.setObj(roleService.queryMyRole(userId));
			message = "获取成功";
		} catch (Exception e) {
			message = "获取失败";
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 查询一个角色里的所有用户
	 * 
	 * @param request
	 */
	@RequestMapping(params = "queryRoleUsers")
	@ResponseBody
	public AjaxJson queryRoleUsers(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		String message = "";
		String roleId = request.getParameter("roleId");
		try {
			result.setObj(this.userRoleService.queryRoleUsers(roleId));
			message = "获取成功";
		} catch (Exception e) {
			message = "获取失败";
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 获得角色头像(最多4个,附带群名称)
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(params = "queryRolesPortrait")
	@ResponseBody
	public AjaxJson queryGroupsPortrait(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		AjaxJson j = new AjaxJson();
		String roleIds = request.getParameter("roleIds");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (String roleId : roleIds.split(",")) {
			List<Map<String, Object>> portraitList = this.userRoleService.queryRolePortrait(roleId);
			RoleEntity role = this.roleService.get(roleId);
			if(role != null){
				Map<String, Object> item = MapKit.create("id", role.getId()).put("name", role.getName()).put("portraits", portraitList).getMap();
				list.add(item);
			}	
		}
		j.setObj(list);
		return j;
	}
	
	/**
	 * 添加员工角色
	 * 
	 * @author lixt
	 * @createtime 2016年01月20日 下午1:27:15
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "saveUserRoles")
	@ResponseBody
	public AjaxJson saveUserRoles(UserRoleEntity userRole,HttpServletRequest request) throws BusinessException{
		AjaxJson result = new AjaxJson();
		String message = "新增成功";
		String userIds = request.getParameter("userIds");
		String roleId = request.getParameter("roleId");
		if(StringUtil.isEmpty(roleId)){
			result.setMsg("roleId不能为空");
			result.setSuccess(false);
			return result;
		}
		if(StringUtil.isNotEmpty(userIds)){
			RoleEntity role = this.roleService.get(roleId);
			if(role != null){
				userRole.setOrgId(ClientUtil.getUserEntity().getOrgIds());
				userRole.setRole(role);
				this.userRoleService.save(userRole,userIds);
				result.setMsg(message);
			}
		}else{
			result.setMsg("userId不能为空");
			result.setSuccess(false);
		}
		return result;
	}
	
	/**
	 * 删除员工角色
	 * 
	 * @author lixt
	 * @createtime 2016年01月20日 下午1:27:15
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "deleteUserRole")
	@ResponseBody
	public AjaxJson deleteUserRole(UserRoleEntity userRole,HttpServletRequest request) throws BusinessException{
		AjaxJson result = new AjaxJson();
		String message = "删除成功";
		String userId = request.getParameter("userId");
		String roleId = request.getParameter("roleId");
		if(StringUtil.isEmpty(roleId)){
			result.setMsg("roleId不能为空");
			result.setSuccess(false);
			return result;
		}
		if(StringUtil.isNotEmpty(userId)){
			this.userRoleService.deleteByUseAndRole(userId, roleId);
			result.setMsg(message);
		}else{
			result.setMsg("userId不能为空");
			result.setSuccess(false);
		}
		return result;
	}
	
	/**
	 * 批量删除员工角色
	 * 
	 * @author lixt
	 * @createtime 2016年01月20日 下午1:27:15
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "batchDeleteUserRole")
	@ResponseBody
	public AjaxJson batchDeleteUserRole(HttpServletRequest request) throws BusinessException{
		AjaxJson result = new AjaxJson();
		String userIds = request.getParameter("userIds");
		String roleId = request.getParameter("roleId");
		if(StringUtil.isEmpty(roleId)){
			result.setMsg("roleId不能为空");
			result.setSuccess(false);
			return result;
		}
		if(StringUtil.isNotEmpty(userIds)){
			String[] userIdArr = userIds.split(",");
			for(String userId : userIdArr){
				this.userRoleService.deleteByUseAndRole(userId, roleId);
			}
			message = "删除成功";
		}
		
		result.setMsg(message);
		return result;
	}
	
	
	/**
	 * 新增或修改角色
	 * 
	 * @author lixt
	 * @createtime 2016年01月14日 上午10:19:16
	 * @param Role
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "saveOrUpdateOrgRole")
	@ResponseBody
	public AjaxJson saveOrUpdateOrgRole(RoleEntity Role,HttpServletRequest request) throws Exception {
		AjaxJson result = new AjaxJson();
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", ClientUtil.getUserId());
		boolean isAdmin = authorityService.isAdmin(param);
		
		if(Role.getDefinedFlag() == 3){
			if(!isAdmin){
				result.setMsg("非管理员不能操作公共角色");
				return result;
			}
		}
		if (StringUtil.isNotEmpty(Role.getId())) {
			message = "角色更新成功";
			roleService.update(Role);
		} else {
			message = "角色新增成功";
			String userIds = request.getParameter("userIds");
			if(StringUtil.isEmpty(userIds)){
				result.setMsg("userId不能为空");
				return result;
			}
			roleService.save(Role,userIds);
		}
		result.setMsg(message);
		return result;
	}
	
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	public void setUserRoleService(UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}

	public void setTypeRoleService(TypeRoleService typeRoleService) {
		this.typeRoleService = typeRoleService;
	}

	public void setOrgRoleService(OrgRoleService orgRoleService) {
		this.orgRoleService = orgRoleService;
	}

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

}
