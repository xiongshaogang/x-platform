package com.xplatform.base.orgnaization.orgnaization.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.common.model.json.TreeNode;
import com.xplatform.base.framework.core.common.model.tree.MutiTreeNode;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.FileUtils;
import com.xplatform.base.framework.core.util.MD5Util;
import com.xplatform.base.framework.core.util.MybatisTreeMapper;
import com.xplatform.base.framework.core.util.PinyinUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.TreeMapper;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgRoleEntity;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.orgnaization.mybatis.vo.OrgTreeVo;
import com.xplatform.base.orgnaization.orgnaization.service.OrgRoleService;
import com.xplatform.base.orgnaization.orgnaization.service.OrgnaizationService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.role.service.RoleService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.entity.UserOrgEntity;
import com.xplatform.base.orgnaization.user.entity.UserRoleEntity;
import com.xplatform.base.orgnaization.user.service.UserOrgService;
import com.xplatform.base.orgnaization.user.service.UserRoleService;
import com.xplatform.base.orgnaization.user.service.UserService;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.system.attachment.model.AttachJsonModel;
import com.xplatform.base.system.attachment.service.AttachService;
import com.xplatform.base.system.type.service.TypeService;

/**
 * 
 * description : 组织机构管理(机构、部门、岗位)
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年12月10日 上午9:35:35
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- hexj 2014年12月10日 上午9:35:35
 *
 */
@Controller
@RequestMapping("/orgnaizationController")
public class OrgnaizationController extends BaseController {
	@Resource
	private OrgnaizationService orgnaizationService;
	@Resource
	private AuthorityService authorityService;
	@Resource
	private OrgRoleService orgRoleService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private RoleService roleService;
	@Resource
	private UserOrgService userOrgService;
	@Resource
	private UserService userService;
	@Resource
	private UserRoleService userRoleService;
	@Resource
	AttachService attachService;
	
	private AjaxJson result = new AjaxJson();

	private String message;

	/**
	 * 组织机构管理列表页跳转
	 * 
	 * @author xiehs/hexj
	 * @createtime 2014年12月9日 下午12:59:50
	 * @param request
	 * @return
	 */
	@Resource
	private TypeService typeService;
	
	@RequestMapping(params = "orgnaization")
	public ModelAndView orgnaization(HttpServletRequest request) throws BusinessException {
		// 获取用户 判断是否管理员 如果不是 则控制tree的菜单显示
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		UserEntity user = client.getUser();
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", user.getId());
		if (this.authorityService.isAdmin(map)) {
			request.setAttribute("isAdmin", "true");
		} else {
			request.setAttribute("isAdmin", "false");
		}
//		String typeId = typeService.doQueryPersonalRootType(ClientUtil.getUserId());
//		request.setAttribute("typeId", typeId);
//		return new ModelAndView("platform/system/data/dataFrame");
		return new ModelAndView("platform/organization/orgnaization/orgnaizationList");
	}

	/**
	 * 机构树
	 * 
	 * @author xiehs/hexj
	 * @createtime 2014年12月9日 下午6:13:48
	 * @param dept
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "tree")
	@ResponseBody
	public AjaxJson tree(OrgTreeVo org, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		UserEntity user = client.getUser();
		String id = request.getParameter("id");
		if (StringUtil.isEmpty(id)) {
			id = "-1";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("parentId", id);
		map.put("userId", user.getId());
		List<TreeNode> treeList = new ArrayList<TreeNode>();
		List<OrgnaizationEntity> orgList=null;
		if(StringUtil.equals(id, "-1")){
			try {
				orgList=this.userService.queryCompany(user.getId());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			orgList = orgnaizationService.queryListByPorperty("parent.id", id);
		}
		// 树的转换
		if(StringUtil.equals("1", request.getParameter("isApp"))){
		    Page<UserEntity> page = new Page<UserEntity>(1000);
			this.buildFilter(page, request);
			Map<String, Object> param = (Map<String, Object>) page.getParameter();
			param.put("orgIds", new String[] { "'"+id+"'" });
			page.setParameter(param);
			page = sysUserService.getUserByCurrentOrgIdsByPage(page);
			
			Map<String,Object> result1=new HashMap<String,Object>();
			result1.put("dept", orgList);
			result1.put("employee", page.getResult());
			if(StringUtil.equals("org", request.getParameter("type"))){
				result1.put("allowDelete", true);
				List<UserEntity> userList=this.userService.queryOrgManager(id);
				if(userList!=null && userList.size()==1){
					if(StringUtil.equals(ClientUtil.getUserId(), userList.get(0).getId())){
						result1.put("allowDelete", false);
					}
				}
			}
			result.setObj(result1);
			return result;
			
		}else if(StringUtil.equals("2", request.getParameter("isApp"))){
			Map<String,Object> result1=new HashMap<String,Object>();
			result1.put("dept", orgList);
			result1.put("employee", null);
			result.setObj(result1);
			return result;
		}else{
			Map<String, String> propertyMapping = new HashMap<String, String>();
			propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
			propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
			propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
			propertyMapping.put(TreeMapper.PropertyType.ICONCLS.getValue(), "iconCls");
			propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(), "code,name,type,available");
			treeList = MybatisTreeMapper.buildJsonTree(orgList, propertyMapping);
			TagUtil.tree(response, treeList);
			return null;
		}
		
	}

	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月24日 上午11:17:03
	 * @Decription 角色分配机构 机构树显示
	 *
	 * @param org
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "roleOrgstree")
	public void roleOrgstree(OrgTreeVo org, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		UserEntity user = client.getUser();
		String id = request.getParameter("id");
		if (StringUtil.isEmpty(id)) {
			id = "-1";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("parentId", id);
		map.put("userId", user.getId());
		// map.put("userTypeId", user.getUserTypeId());
		List<TreeNode> treeList = new ArrayList<TreeNode>();
		List<OrgnaizationEntity> orgList;
		Map<String, String> propertyMapping = new HashMap<String, String>();

		if (this.authorityService.isAdmin(map)) {
			orgList = orgnaizationService.queryListByPorperty("parent.id", id);
			propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(), "code,name,type");
		} else { // 不是管理员，进行权限过滤
			List<OrgnaizationEntity> allOrgList = null;
			orgList = new ArrayList<OrgnaizationEntity>();
			for (OrgnaizationEntity orgnaization : allOrgList) {// 异步结果集
				if (orgnaization.getParent() == null && ("-1".equals(id)) || (orgnaization.getParent() != null && id.equals(orgnaization.getParent().getId()))) {
					orgList.add(orgnaization);
				}
			}
			propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(), "code,name,type,available");
		}

		String roleId = request.getParameter("roleId");
		List<OrgRoleEntity> roleOrgs = orgRoleService.queryOrgRolesByRoleId(roleId);
		Set<String> ownOrgSet = new HashSet<String>(); // role owns orgs
		for (OrgRoleEntity roleOrg : roleOrgs) {
			ownOrgSet.add(roleOrg.getOrg().getId());
		}
		for (OrgnaizationEntity o : orgList) {
			if (ownOrgSet.contains(o.getId())) {
				o.setChecked("1");
			}
		}
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		propertyMapping.put(TreeMapper.PropertyType.ICONCLS.getValue(), "iconCls");
		propertyMapping.put(TreeMapper.PropertyType.CHECKED.getValue(), "checked");
		treeList = MybatisTreeMapper.buildJsonTree(orgList, propertyMapping);

		TagUtil.tree(response, treeList);
	}

	/*@RequestMapping(params = "setGradeAuthority")
	public void setGradeAuthority(HttpServletRequest request, HttpServletResponse response) {
		String parentId = "";
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			parentId = request.getParameter("id");
		} else {
			parentId = "-1";
		}
		List<OrgTreeVo> deptTreeList = orgnaizationService.queryOrgTreeByGrade(request.getParameter("userId"), parentId);
		// 树的转换
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		propertyMapping.put(TreeMapper.PropertyType.ICONCLS.getValue(), "iconCls");
		propertyMapping.put(TreeMapper.PropertyType.CHECKED.getValue(), "checked");
		propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(), "code,name,type");
		List<TreeNode> treeList = MybatisTreeMapper.buildJsonTree(deptTreeList, propertyMapping);
		TagUtil.tree(response, treeList);
	}*/

	@RequestMapping(params = "setEmpOrgTree")
	public void setEmpOrgTree(HttpServletRequest request, HttpServletResponse response) {
		String parentId = "";
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			parentId = request.getParameter("id");
		} else {
			parentId = "-1";
		}
		List<OrgTreeVo> deptTreeList = orgnaizationService.queryEmpOrgTree(request.getParameter("empId"), parentId);
		// 树的转换
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		propertyMapping.put(TreeMapper.PropertyType.ICONCLS.getValue(), "iconCls");
		propertyMapping.put(TreeMapper.PropertyType.CHECKED.getValue(), "checked");
		propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(), "code,name,type");
		List<TreeNode> treeList = MybatisTreeMapper.buildJsonTree(deptTreeList, propertyMapping);
		TagUtil.tree(response, treeList);
	}

	/**
	 * 单表hibernate组装表格数据
	 * 
	 * @author xiehs/hexj
	 * @createtime 2014年12月9日 下午12:57:00
	 * @param Dept
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "datagrid")
	public void datagrid(OrgnaizationEntity org, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(OrgnaizationEntity.class, dataGrid);
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, org, request.getParameterMap());
		cq.add();
		this.orgnaizationService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(params = "datagridSel")
	public void datagridSel(OrgnaizationEntity dept, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(OrgnaizationEntity.class, dataGrid);
		// 查询条件组装器
		dept.setType("dept");
		HqlGenerateUtil.installHql(cq, dept, request.getParameterMap());
		cq.add();
		this.orgnaizationService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 批量删除部门
	 * 
	 * @author xiehs/hexj
	 * @createtime 2014年12月9日 下午1:27:15
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request) {
		AjaxJson result=new AjaxJson();
		String deptId = request.getParameter("deptId");
		String orgId = request.getParameter("orgId");
		try {
			if(StringUtil.isNotEmpty(orgId)){
				orgnaizationService.deleteCascade(orgId);
				result.setSuccess(true);
				message = "解散成功";
				updateSesseionCache();
			}else if(StringUtil.isNotEmpty(deptId)){
				List<OrgnaizationEntity> orgList = orgnaizationService.queryListByPorperty("parent.id", deptId);
				if (orgList != null && orgList.size() > 0) {
					message = "请先删除子部门";
					result.setSuccess(false);
					return result;
				} else {
					//查询部门下的所有人
					List<UserOrgEntity> uoList=this.userOrgService.findByJobId(deptId);
					Set<String> set=new HashSet<String>();
					if(uoList!=null && uoList.size()>0){
						for(UserOrgEntity uo:uoList){
							set.add(uo.getUser().getId());
						}
					}
					//删除部门下的人
					this.userOrgService.deleteUserFromDept(StringUtil.asString(set, ","), deptId);
					//删除部门
					orgnaizationService.delete(deptId);
					message = "部门删除成功";
					updateSesseionCache();
					result.setSuccess(true);
				}
			}
			
		} catch (Exception e) {
			message = e.getMessage();
			result.setSuccess(false);
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
	@RequestMapping(params = "orgnaizationEdit")
	public ModelAndView orgnaizationEdit(HttpServletRequest request, OrgnaizationEntity org, Model model) {
		String id = request.getParameter("id");
		if (StringUtil.isNotEmpty(id)) {
			org = orgnaizationService.get(id);
			if (org.getParent() == null) {
				OrgnaizationEntity parentOrg = new OrgnaizationEntity();
				parentOrg.setId("-1");
				parentOrg.setName("根节点");
				org.setParent(parentOrg);
			}
			model.addAttribute("org", org);
			model.addAttribute("orgType", org.getType());
		} else {
			String parentId = request.getParameter("parent.id");
			if (StringUtil.isNotEmpty(parentId)) {
				OrgnaizationEntity parentOrg = orgnaizationService.get(parentId);
				model.addAttribute("parentOrg", parentOrg);
				model.addAttribute("orgType", parentOrg.getType());
			}
		}

		return new ModelAndView("platform/organization/orgnaization/orgnaizationEdit");
	}

	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月16日 上午9:31:12
	 * @Decription : 新增或修改组织机构
	 *
	 * @param org
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(OrgnaizationEntity org, HttpServletRequest request) {
		try {
			//设置图标
			if (org.getType().equals("org")) {
				org.setIconCls("fa-sitemap"); // 机构
			} else if (org.getType().equals("dept")) {
				org.setIconCls("fa-home"); // 部门
			} else {
				org.setIconCls("icon-job"); // 岗位
			}
			//设置团队或部门默认的code
			if(!StringUtil.isNotEmpty(org.getCode())){
				String code = PinyinUtil.getPinYinHeadChar(org.getName());
				int count = 0;
				Map<String, String> param = new HashMap<String, String>();
				do {
					code = code + (count == 0 ? "" : Integer.valueOf(count));
					count++;
					param.put("newValue", code);
				} while (!this.orgnaizationService.isUnique(param, "code"));
				org.setCode(code);
			}
			//设置parentId
			String parentId= request.getParameter("parent.id");
			if (StringUtil.isNotEmpty(org.getId())) {
				//修改部门的时候同级的名称不能相通
				if (org.getType().equals("dept")) {
					OrgnaizationEntity dept=this.orgnaizationService.get(org.getId());
					if(dept!=null && dept.getParent()!=null){
						parentId=dept.getParent().getId();	
					}
				}
			}
			//判断同级部门的名称是否相同
			List<OrgnaizationEntity> orgList = orgnaizationService.queryListByPorperty("parent.id", parentId);
			if(orgList!=null && orgList.size()>0){
				for(OrgnaizationEntity orgCheck:orgList){
					if(StringUtil.equals(org.getName(), orgCheck.getName()) && !StringUtil.equals(org.getId(), orgCheck.getId())){
						result.setSuccess(false);
						result.setMsg("部门已存在,请重新添加");
						return result;
					}
				}
			}
			//新增或者修改逻辑
			if (StringUtil.isNotEmpty(org.getId())) {
				message = "修改成功";
				orgnaizationService.update(org);
			} else {
				message = "添加成功";
				OrgnaizationEntity orgEntity = new OrgnaizationEntity();
				if (StringUtil.isEmpty(parentId)) {
					orgEntity.setId("-1");
				} else {
					orgEntity.setId(parentId);
				}
				org.setParent(orgEntity);
				String pk=orgnaizationService.save(org);
				//添加员工
				String orgMember=request.getParameter("orgMember");
				if(StringUtil.isNotEmpty(orgMember)){
					userOrgService.updateJobUsers(pk, orgMember);
				}
				//添加机构管理员
				if (org.getType().equals("org")) {
					UserEntity user = new UserEntity();
					user.setId(ClientUtil.getUserId());
					UserRoleEntity ur = new UserRoleEntity();
					ur.setOrgId(pk);
					ur.setUser(user);
					RoleEntity role = new RoleEntity();
					role.setId(RoleEntity.COMPANY_ADMIN);
					ur.setRole(role);
					this.userRoleService.save(ur);

					// 添加团队根文件夹和公共文件夹
					String typeId=typeService.initOrgType(org.getId());
					org.setRootTypeId(typeId);
//					orgnaizationService.update(org);
				}
				//更新缓存
				updateSesseionCache();
			}
			result.setSuccess(true);
		} catch (Exception e) {
			message = e.getMessage();
			result.setSuccess(false);
		}
		result.setMsg(message);
		return result;
	}
	
	
	@RequestMapping(params = "addManager")
	@ResponseBody
	public AjaxJson addManager(OrgnaizationEntity org, HttpServletRequest request) {
		String userIds=request.getParameter("userIds");
		//org=orgnaizationService.get(org.getId());
		if(StringUtil.isEmpty(userIds)){
			message = "没有选择用户";
			result.setSuccess(false);
		}else{
			String[] userIdArr=userIds.split(",");
			String managerUserName="";
			for(String userId:userIdArr){
				UserEntity user=sysUserService.getUserById(userId);
				managerUserName=managerUserName+user.getName()+",";
			}
			
			managerUserName=StringUtil.removeDot(managerUserName);
			org.setManagerUserId(userIds);
			org.setManagerUserName(managerUserName);
			try {
				if (StringUtil.isNotEmpty(org.getId())) {
					message = "设置成功";
					orgnaizationService.update(org);
				}
			} catch (Exception e) {
				message = e.getMessage();
			}
			result.setSuccess(true);
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 添加团队管理员
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "queryOrgManager")
	@ResponseBody
	public AjaxJson queryOrgManager(HttpServletRequest request) {
		AjaxJson result=new AjaxJson();
		String orgId=request.getParameter("orgId");
		try {
			List<UserEntity> userList=this.userService.queryOrgManager(orgId);
			message = "查询管理员成功";
			result.setObj(userList);
			result.setSuccess(true);
		} catch (Exception e) {
			message = e.getMessage();
			message = "管理员查询失败";
			result.setSuccess(false);
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "getOrgInfo")
	@ResponseBody
	public AjaxJson getOrgInfo(HttpServletRequest request) {
		AjaxJson result=new AjaxJson();
		String orgId=request.getParameter("id");
		try {
			message = "获取成功";
			Map<String,Object> info=new HashMap<String,Object>();
			info.put("orgManagerList", this.userService.queryOrgManager(orgId));
			OrgnaizationEntity org=orgnaizationService.get(orgId);
			info.put("org", org);
			String managerUserId=org.getManagerUserId();
			List<UserEntity> userList=new ArrayList<UserEntity>();
			if(StringUtil.isNotEmpty(managerUserId)){
				message = "部门经理获取成功";
				String[] userArr=managerUserId.split(",");
				for(int i=0;i<userArr.length;i++){
					UserEntity user=this.sysUserService.getUserById(userArr[i]);
					userList.add(user);
				}
			}
			info.put("orgLeaderList", userList);
			result.setObj(info);
			result.setSuccess(true);
		} catch (Exception e) {
			message = e.getMessage();
			message = "获取失败";
			result.setSuccess(false);
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 添加团队管理员
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "addOrgManager")
	@ResponseBody
	public AjaxJson addOrgManager(HttpServletRequest request) {
		AjaxJson result=new AjaxJson();
		String userId=request.getParameter("userId");
		String orgId=request.getParameter("orgId");
		if(StringUtil.isEmpty(userId) ||StringUtil.isEmpty(orgId)){
			message = "没有选择团队或者用户";
			result.setSuccess(false);
			return result;
		}
		List<UserEntity> userList=this.userService.queryOrgManager(orgId);
		if(userList!=null && userList.size()>0){
			for(UserEntity user:userList){
				if(StringUtil.equals(userId, user.getId())){
					message = user.getName()+"已经是管理员";
					result.setSuccess(false);
					result.setMsg(message);
					return result;
				}
			}
		}
		try {
			UserEntity user=new UserEntity();
			user.setId(userId);
			RoleEntity role=new RoleEntity();
			role.setId(RoleEntity.COMPANY_ADMIN);
			UserRoleEntity ur=new UserRoleEntity();
			ur.setOrgId(orgId);
			ur.setUser(user);
			ur.setRole(role);
			this.userRoleService.save(ur);
			message = "管理员添加成功";
			result.setSuccess(true);
		} catch (Exception e) {
			message = e.getMessage();
			message = "管理员添加失败";
			result.setSuccess(false);
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 删除团队管理员
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "removeOrgManager")
	@ResponseBody
	public AjaxJson removeOrgManager(HttpServletRequest request) {
		AjaxJson result=new AjaxJson();
		String userId=request.getParameter("userId");
		String orgId=request.getParameter("orgId");
		if(StringUtil.isEmpty(userId) ||StringUtil.isEmpty(orgId)){
			message = "没有选择团队或者用户";
			result.setSuccess(false);
		}else{
			try {
				this.userRoleService.deleteOrgManager(userId, orgId);
				message = "管理员删除成功";
				result.setSuccess(true);
			} catch (Exception e) {
				message = "管理员删除失败";
				result.setSuccess(false);
			}
			
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "queryManager")
	@ResponseBody
	public AjaxJson queryManager(OrgnaizationEntity org, HttpServletRequest request) {
		AjaxJson result=new AjaxJson();
		org=orgnaizationService.get(org.getId());
		String managerUserId=org.getManagerUserId();
		//String managerUserName=org.getManagerUserName();
		if(StringUtil.isNotEmpty(managerUserId)){
			message = "部门经理获取成功";
			List<UserEntity> userList=new ArrayList<UserEntity>();
			String[] userArr=managerUserId.split(",");
			//String[] userNameArr=managerUserName.split(",");
			for(int i=0;i<userArr.length;i++){
				UserEntity user=this.sysUserService.getUserById(userArr[i]);
				userList.add(user);
			}
			result.setObj(userList);
		}else{
			message = "未设置部门经理";
		}
		result.setSuccess(true);
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "updateOrg")
	@ResponseBody
	public AjaxJson updateOrg(OrgnaizationEntity org, HttpServletRequest request) {
		AjaxJson result=new AjaxJson();
		String parentId= request.getParameter("parent.id");
		//修改部门的时候同级的名称不能相通
		OrgnaizationEntity dept=this.orgnaizationService.get(org.getId());
		if (dept.getType().equals("dept")) {
			if(dept!=null && dept.getParent()!=null){
				parentId=dept.getParent().getId();	
			}
		}
		//判断同级部门的名称是否相同
		List<OrgnaizationEntity> orgList = orgnaizationService.queryListByPorperty("parent.id", parentId);
		if(orgList!=null && orgList.size()>0){
			for(OrgnaizationEntity orgCheck:orgList){
				if(StringUtil.equals(org.getName(), orgCheck.getName()) && !StringUtil.equals(org.getId(), orgCheck.getId())){
					result.setSuccess(false);
					result.setMsg("部门已存在,请重新添加");
					return result;
				}
			}
		}
		
		try {
			if (StringUtil.isNotEmpty(org.getId())) {
				message = "修改成功";
				orgnaizationService.update(org);
			}
			updateSesseionCache();
			result.setSuccess(true);
		} catch (Exception e) {
			message = e.getMessage();
			result.setSuccess(false);
		}
		result.setMsg(message);
		return result;
	}
	
	
	@RequestMapping(params = "updateOrgLogo")
	@ResponseBody
	public AjaxJson updateOrgLogo(MultipartHttpServletRequest request) {
		AjaxJson result=new AjaxJson();
		String orgId=request.getParameter("id");
		MultipartFile mpf = null;
		try {
			if (StringUtil.isNotEmpty(orgId)) {
				Iterator<String> itr =request.getFileNames() ;
				while (itr.hasNext()) {
					/* 1.上传时数据参数预处理 */
					 mpf = request.getFile(itr.next());
				}
				if(mpf!=null){
					Map<String,Object> params=new HashMap<String,Object>();
					params.put("in", mpf.getInputStream());
					params.put("originalFilename", mpf.getOriginalFilename());
					params.put("size", mpf.getSize());
					params.put("contentType",  mpf.getContentType());
					AjaxJson attach=attachService.doUploadFiles(params);
					LinkedList<AttachJsonModel> list=(LinkedList<AttachJsonModel>)attach.getObj();
					if(list!=null && list.size()>0){
						String logo=list.getFirst().getId();
						message = "上传成功";
						OrgnaizationEntity org=new OrgnaizationEntity();
						org.setId(orgId);
						org.setLogo(logo);
						orgnaizationService.update(org);
					}
				}
				
			}
			result.setSuccess(true);
		} catch (Exception e) {
			message = e.getMessage();
			result.setSuccess(false);
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
	@RequestMapping(params = "getOrg")
	@ResponseBody
	public AjaxJson getOrg(HttpServletRequest request,OrgnaizationEntity org,Model model) {
		AjaxJson result=new AjaxJson();
		if (StringUtil.isNotEmpty(org.getId())) {
			message = "获取机构组织信息成功";
			org = orgnaizationService.get(org.getId());
		}else{
			message = "获取机构组织信息失败";
		}
		result.setObj(org);
		result.setSuccess(true);
		result.setMsg(message);
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月11日 上午10:59:30
	 * @Decription 机构树数据请求,一般给自定义标签中的机构左树使用
	 *
	 * @param dept
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "orgSelectTagTree")
	public void orgSelectTagTree(OrgTreeVo dept, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String orgCode = request.getParameter("orgCode");
		String orgId = request.getParameter("orgId");
		Boolean onlyAuthority = Boolean.parseBoolean(request.getParameter("onlyAuthority"));
		Boolean containSelf = Boolean.parseBoolean(request.getParameter("containSelf"));
		List<OrgnaizationEntity> orgEntityList = new ArrayList<OrgnaizationEntity>();
		List<TreeNode> treeList = new ArrayList<TreeNode>();
		// 获得user对象
		UserEntity user = ClientUtil.getUserEntity();

		// 转换类设定
		if(StringUtil.isEmpty(id)){
			// id为空说明是首次请求
			id = "-1";
			
			//是否有默认加载的节点
			if (StringUtil.isNotEmpty(orgCode) || StringUtil.isNotEmpty(orgId)) {
				List<OrgnaizationEntity> orgList = new ArrayList<OrgnaizationEntity>();
				if (StringUtil.isNotEmpty(orgCode)) {
					orgList = orgnaizationService.queryListByPorperty("code", orgCode);
				} else if (StringUtil.isNotEmpty(orgId)) {
					orgList = orgnaizationService.queryListByPorperty("id", orgId);
				}

				// 如果containSelf为真,那么首次加载的就是单个orgCode节点,而不是orgCode的子节点
				if (containSelf) {
					treeList = TreeMapper.buildJsonTree(orgList, null);
					TagUtil.tree(response, treeList);
					return;
				} else {
					if (orgList.size() > 0) {
						id = orgList.get(0).getId();
					}
				}
			}
		}
		
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("parentId", id);
		map.put("userId", user.getId());
		// 判断是否进行权限过滤
		/*if (onlyAuthority) {
			// if (this.authorityService.isAdmin(map)) {
			// orgEntityList = orgnaizationService.queryListByPorperty(
			// "parent.id", id);
			// treeList = TreeMapper.buildJsonTree(orgEntityList,
			// propertyMapping);
			// } else { // 不是管理员，进行权限过滤
			// orgEntityList=ClientUtil.getOrgListByPId(id);
			// treeList = TreeMapper.buildJsonTree(orgEntityList,
			// propertyMapping);
			// }
			orgEntityList = ClientUtil.getOrgListByPId(id);
			treeList = TreeMapper.buildJsonTree(orgEntityList, null);
		} else {*/
			if(StringUtil.equals(id, "-1")){
				try {
					orgEntityList=this.userService.queryCompany(user.getId());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				orgEntityList = orgnaizationService.queryListByPorperty("parent.id", id);
			}
			treeList = TreeMapper.buildJsonTree(orgEntityList, null);
		//}

		TagUtil.tree(response, treeList);
	}

	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月17日 下午3:19:40
	 * @Decription 分配机构
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "orgToRoleSelect")
	public ModelAndView orgToRoleSelect(HttpServletRequest request) {
		String roleId = request.getParameter("roleId");
		request.setAttribute("roleId", roleId);
		return new ModelAndView("platform/organization/role/orgToRoleSelect");
	}

	@RequestMapping(params = "saveRoleOrg")
	@ResponseBody
	public AjaxJson saveRoleOrg(HttpServletRequest request) {
		String roleId = request.getParameter("roleId");
		String orgIds = request.getParameter("orgIds");
		try {
			orgRoleService.updateRoleOrg(roleId, orgIds);
			message = "保存角色机构成功";
		} catch (Exception e) {
			message = "保存角色机构失败";
		}
		result.setMsg(message);
		return result;
	}
	
	private void updateSesseionCache() throws Exception{
		//更新缓存
		/*Client client=ClientManager.getInstance().getClient();
		UserEntity user=client.getUser();
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", user.getId());
		boolean isAdmin = authorityService.isAdmin(param);
		List<OrgnaizationEntity> orgList = null;
		if (isAdmin) {
			orgList = this.orgnaizationService.queryList();
			List<OrgnaizationEntity> copyOrgList = BeanUtils.deepCopy(orgList);
			for (OrgnaizationEntity org1 : copyOrgList) {
				org1.setAvailable("1");
			}
			client.setManagerOrgList(copyOrgList);
		} else {
			orgList = this.orgnaizationService.queryControllerOrg(user.getId());
			client.setManagerOrgList(orgList);// 获取员工具有的可操作的组织结构列表（用于展示个人的组织机构树）
		}
		client.setOrgList(sysUserService.getAllOrganizationsByUserId(user.getId()));*/
	}
	
	@RequestMapping(params = "getTree")
	@ResponseBody
	public AjaxJson testTree(OrgTreeVo org, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		UserEntity user = client.getUser();
		String id = request.getParameter("id");
		if (StringUtil.isEmpty(id)) {
			id = "-1";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("parentId", id);
		map.put("userId", user.getId());
		List<TreeNode> treeList = new ArrayList<TreeNode>();
		List<OrgnaizationEntity> orgList=null;
		if(StringUtil.equals(id, "-1")){
			try {
				orgList=this.userService.queryCompany(user.getId());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			orgList = orgnaizationService.queryListByPorperty("parent.id", id);
		}
		// 树的转换
		if(StringUtil.equals("1", request.getParameter("isApp"))){
		    Page<UserEntity> page = new Page<UserEntity>(1000);
			this.buildFilter(page, request);
			Map<String, Object> param = (Map<String, Object>) page.getParameter();
			param.put("orgIds", new String[] { "'"+id+"'" });
			page.setParameter(param);
			page = sysUserService.getUserByCurrentOrgIdsByPage(page);
			
			Map<String,Object> result1=new HashMap<String,Object>();
			result1.put("dept", orgList);
			result1.put("employee", page.getResult());
			if(StringUtil.equals("org", request.getParameter("type"))){
				result1.put("allowDelete", true);
				List<UserEntity> userList=this.userService.queryOrgManager(id);
				if(userList!=null && userList.size()==1){
					if(StringUtil.equals(ClientUtil.getUserId(), userList.get(0).getId())){
						result1.put("allowDelete", false);
					}
				}
			}
			result.setObj(result1);
			return result;
			
		}else if(StringUtil.equals("2", request.getParameter("isApp"))){
			Map<String,Object> result1=new HashMap<String,Object>();
			result1.put("dept", orgList);
			result1.put("employee", null);
			result.setObj(result1);
			return result;
		}else{
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(OrgnaizationEntity org1 : orgList){
				Map<String,Object> mapData = new HashMap<String,Object>();
				mapData.put("id", org1.getId());
				mapData.put("name", org1.getName());
				/*if(org1.getIsLeaf() == "0"){department
					mapData.put("isParent", true);organization
				}*/
				mapData.put("isParent", true);
				mapData.put("icon", "images/organization/organization.png");
				mapData.put("portrait", "organization");
				//mapData.put("icon", "attachController.do?downloadFile&aId=");
				if(org1.getParent() != null){
					mapData.put("pId", org1.getParent().getId());
				}else{
					mapData.put("pId", "0");
				}
				list.add(mapData);
			}
			result.setObj(list);
			return result;
		}
		
	}
	
	@RequestMapping(params = "getTreeData")
	@ResponseBody
	public AjaxJson getTreeData(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson result = new AjaxJson();
		String id = request.getParameter("id");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<OrgnaizationEntity> orgList = orgnaizationService.queryListByPorperty("parent.id", id);
		if(orgList != null){
			for(OrgnaizationEntity org1 : orgList){
				Map<String,Object> mapData = new HashMap<String,Object>();
				mapData.put("id", org1.getId());
				mapData.put("name", org1.getName());
				/*if(org1.getIsLeaf() == "0"){
					mapData.put("isParent", true);
				}*/
				mapData.put("isParent", true);
				if(org1.getParent() != null){
					mapData.put("pId", org1.getParent().getId());
				}else{
					mapData.put("pId", "0");
				}
				mapData.put("icon", "images/organization/department.png");
				mapData.put("portrait", "department");
				list.add(mapData);
				
			}
		}
		Page<UserEntity> page = new Page<UserEntity>(1000);
		this.buildFilter(page, request);
		Map<String, Object> param = (Map<String, Object>) page.getParameter();
		param.put("orgIds", new String[] { "'"+id+"'" });
		page.setParameter(param);
		page = sysUserService.getUserByCurrentOrgIdsByPage(page);
		List<UserEntity> userList = page.getResult();
		if(userList != null){
			for(UserEntity user : userList){
				Map<String,Object> mapData = new HashMap<String,Object>();
				mapData.put("id", user.getId());
				mapData.put("name", user.getName());
				mapData.put("isParent", false);
				mapData.put("pId", id);
				mapData.put("portrait", user.getPortrait());
				if(StringUtil.isNotEmpty(user.getPortrait())){
					mapData.put("icon", ConfigConst.LOCALHOST_URL+user.getPortrait());
				}else{
					mapData.put("icon", "basic/img/avatars/avatar_80.png");
				}
				list.add(mapData);
			}
		}

/*		for(int i=0;i<3;i++){
			Map<String,Object> mapData = new HashMap<String,Object>();
			mapData.put("id", "1234567");
			mapData.put("isParent", false);
			if(i == 0){
				mapData.put("name", "张三");
			}else if(i == 1){
				mapData.put("name", "黄四郎");
			}else if(i == 2){
				mapData.put("name", "王五");
			}
			
			if(id != null){
				mapData.put("pId", id);
			}else{
				mapData.put("pId", "0");
			}
			list.add(mapData);
		}*/
		result.setObj(list);
		return result;
		
		
	}

	
}
