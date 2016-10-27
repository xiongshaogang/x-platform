package com.xplatform.base.system.type.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.xplatform.base.framework.core.util.MybatisTreeMapper;
import com.xplatform.base.framework.core.util.PinyinUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.TreeMapper;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.framework.tag.vo.easyui.TreeGridModel;
import com.xplatform.base.orgnaization.resouce.entity.ResourceEntity;
import com.xplatform.base.orgnaization.resouce.service.ResourceService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.system.type.entity.FileTypeRoleAuthorityEntity;
import com.xplatform.base.system.type.entity.FileTypeUserAuthorityEntity;
import com.xplatform.base.system.type.entity.TypeEntity;
import com.xplatform.base.system.type.entity.TypeUserEntity;
import com.xplatform.base.system.type.mybatis.vo.TypeTreeVo;
import com.xplatform.base.system.type.service.TypeService;
import com.xplatform.base.system.type.service.TypeUserService;

@Controller
@RequestMapping("/typeController")
public class TypeController extends BaseController {
	@Resource
	private TypeService typeService;
	@Resource
	private AuthorityService authorityService;
	@Resource
	private TypeUserService typeUserService;
	@Resource
	private ResourceService resourceService;
	@Resource
	private CommonService commonService;
	
	private AjaxJson result = new AjaxJson();

	private String message;

	/**
	 * 跳转系统类型管理界面
	 * @author luoheng
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "type")
	public ModelAndView type(HttpServletRequest request) {
		return new ModelAndView("platform/system/type/typeList");
	}

	@RequestMapping(params = "tree")
	public void tree(TypeTreeVo type, HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		UserEntity user = ClientUtil.getUserEntity();
		String id = "";
		List<TreeNode> treeList = new ArrayList<TreeNode>();
		//需要转换的额外属性
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(), "code,name");
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			id = request.getParameter("id");
			Map<String, String> map = new HashMap<String, String>();
			map.put("parentId", id);
			map.put("userId", user.getId());
			
			//map.put("userTypeId", user.getUserTypeId());
			if (this.authorityService.isAdmin(map)) {
				List<TypeEntity> typeList = typeService.queryListByPorperty("parent.id", id);
				treeList = TreeMapper.buildJsonTree(typeList, propertyMapping);
			} else {
				//不是管理员，进行权限过滤
				List<TypeTreeVo> typeList = new ArrayList<TypeTreeVo>();
				typeList = typeService.queryTypeTree(user.getId(), id);
				treeList = MybatisTreeMapper.buildJsonTree(typeList, propertyMapping);
			}
		} else {
			//TODO 第一次进入时
			id="11";
			TypeEntity rootType = typeService.queryCurrentOrgRootType(id);
			List<TypeEntity> typeList = new ArrayList<TypeEntity>();
			typeList.add(rootType);
			treeList = TreeMapper.buildJsonTree(typeList, propertyMapping);
		}

		TagUtil.tree(response, treeList);
	}

	/**
	 * 根据类型、用户权限查询系统类型（同步树）
	 * @author luoheng
	 * @param sysType
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "typeRoleTree")
	public void typeRoleTree(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		UserEntity user = client.getUser();

		/**
		String id="";
		if(StringUtil.isNotEmpty(request.getParameter("id"))){
			id= request.getParameter("id");
		}else{
			id="-1";
		}**/

		String sysType = request.getParameter("sysType");
		Map<String, String> map = new HashMap<String, String>();
		//map.put("parentId", id);
		map.put("userId", user.getId());
		map.put("sysType", sysType);

		List<TypeTreeVo> typeList=new ArrayList<TypeTreeVo>();
		try {
			typeList = typeService.queryTypeRoleTreeBySysType(map);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//树的转换
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		propertyMapping.put(TreeMapper.PropertyType.ICONCLS.getValue(), "iconCls");
		propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(), "code,name,sysType");
		List<TreeNode> treeList = MybatisTreeMapper.buildJsonTree(typeList, propertyMapping);

		TagUtil.tree(response, treeList);
	}

	/**
	 * 根据类型、用户权限查询系统类型(异步树)
	 * @author luoheng
	 * @param sysType
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "typeRoleTreeBySysTypeTree")
	public void typeRoleTreeBySysTypeTree(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		UserEntity user = client.getUser();

		String id = "";
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			id = request.getParameter("id");
		} else {
			id = "-1";
		}

		String sysType = request.getParameter("sysType");
		Map<String, String> map = new HashMap<String, String>();
		map.put("parentId", id);
		map.put("userId", user.getId());
		map.put("sysType", sysType);

		List<TypeTreeVo> typeList=new ArrayList<TypeTreeVo>();
		try {
			typeList = typeService.queryTypeRoleTreeBySysTypeTree(map);
		} catch (BusinessException e) {
			ExceptionUtil.printStackTraceAndLogger(e);
		}
		//树的转换
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		propertyMapping.put(TreeMapper.PropertyType.ICONCLS.getValue(), "iconCls");
		propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(), "code,name,sysType");
		List<TreeNode> treeList = MybatisTreeMapper.buildJsonTree(typeList, propertyMapping);

		TagUtil.tree(response, treeList);
	}

	/**
	 * 根据用户ID查询该用户分配可操作的系统类型（分配树展示）
	 * @author luoheng
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "queryUserTypeTree")
	public void queryUserTypeTree(HttpServletRequest request, HttpServletResponse response) {
		String parentId = "";
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			parentId = request.getParameter("id");
		} else {
			parentId = "-1";
		}
		List<TypeTreeVo> typeList=new ArrayList<TypeTreeVo>();
		try {
			typeList = typeService.queryUserTypeTree(request.getParameter("userId"), parentId);
		} catch (BusinessException e) {
			ExceptionUtil.printStackTraceAndLogger(e);
		}
		//树的转换
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		propertyMapping.put(TreeMapper.PropertyType.ICONCLS.getValue(), "iconCls");
		propertyMapping.put(TreeMapper.PropertyType.CHECKED.getValue(), "checked");
		propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(), "code,name,sysType");
		List<TreeNode> treeList = MybatisTreeMapper.buildJsonTree(typeList, propertyMapping);
		TagUtil.tree(response, treeList);
	}

	/**
	 * 更新用户系统类型权限成功
	 * @author luoheng    update by hexj
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "updateUserType")
	@ResponseBody
	public AjaxJson updateUserType(HttpServletRequest request, HttpServletResponse response) {
		/*String typeIds = request.getParameter("userTypeIds");
		String userId = request.getParameter("userId");
		try {
			typeUserService.updateTypeUser(typeIds, userId);
			result.setMsg("更新用户系统类型权限成功");
		} catch (BusinessException e) {
			result.setMsg(e.getMessage());
		}
		return result;*/
		String userId = request.getParameter("userId");
		String checkedNode = request.getParameter("checkedNodes");  //选中的节点
		String pNode = request.getParameter("pNodes"); //未勾选的父节点
		Map<String, String> selectNodeMap = new HashMap<String, String>();
		for(String cnode : StringUtil.split(checkedNode, ",")){
			selectNodeMap.put(cnode, "1");
		}
		for(String cnode : StringUtil.split(pNode, ",")){
			selectNodeMap.put(cnode, "0");
		}
		
		List<TypeUserEntity> typeUsers = typeUserService.queryTypeUserByUserIdList(userId);
		Map<String, TypeUserEntity> ownsNodeMap = new HashMap<String, TypeUserEntity>();
		for(TypeUserEntity typeUser : typeUsers){
			ownsNodeMap.put(typeUser.getType().getId(), typeUser);
		}
		
		try {
			typeUserService.updateTypeUser(selectNodeMap, ownsNodeMap, userId);
			result.setMsg("更新用户系统类型权限成功");
		} catch (BusinessException e) {
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author luoheng
	 * @param type
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(TypeEntity type, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		UserEntity user = client.getUser();
		String parentId = request.getParameter("parentId");

		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", user.getId());
		if (this.authorityService.isAdmin(map)) {//不是管理员，进行权限过滤
			CriteriaQuery cq = new CriteriaQuery(TypeEntity.class, dataGrid);
			//查询条件组装器
			if (StringUtil.isNotEmpty(request.getParameter("parentId"))) {
				cq.eq("parent.id", request.getParameter("parentId"));
			} else {
				cq.eq("parent.id", "-1");
			}
			HqlGenerateUtil.installHql(cq, type, request.getParameterMap());
			cq.add();
			this.typeService.getDataGridReturn(cq, true);
			TagUtil.datagrid(response, dataGrid);
		} else {
			if (!StringUtil.isNotEmpty(parentId)) {
				parentId = "-1";
			}
			map.put("parentId", parentId);
			Page<TypeTreeVo> page = typeService.findByPage(map, dataGrid);
			dataGrid.setResults(page.getResult());
			TagUtil.datagrid(response, dataGrid);
		}

	}

	/**
	 * 新增、修改系统分类
	 * @param type
	 * @return
	 */
	@RequestMapping(params = "saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(TypeEntity type) throws BusinessException {
		if (StringUtil.isNotEmpty(type.getId())) {
			result.setMsg("文件夹更新成功");
			result.setSuccess(false);
			typeService.updateType(type);
		} else {
			result.setMsg("文件夹新增成功");
			result.setSuccess(false);
			typeService.saveType(type);
		}
		return result;
	}

	/**
	 * 删除系统分类
	 * @author luoheng
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request) {
		String id = request.getParameter("id");
		try {
			if (StringUtil.isNotEmpty(id)) {
				typeService.delete(id);
				message = "系统分类删除成功";
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 批量删除系统分类
	 * @author luoheng
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		try {
			typeService.batchDelete(ids);
			message = "系统分类批量删除成功";
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 跳转系统类型编辑界面
	 * @author luoheng
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(params = "typeEdit")
	public ModelAndView typeEdit(TypeEntity type, Model model) {
		if (StringUtil.isNotEmpty(type.getId())) {
			type = typeService.getType(type.getId());
			model.addAttribute("typePage", type);
		}
		return new ModelAndView("platform/system/type/typeEdit");
	}

	/**
	 * 提取汉字首字母拼音
	 * @author luoheng
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(params = "queryUnRepeatCode")
	@ResponseBody
	public AjaxJson queryUnRepeatCode(HttpServletRequest request) throws UnsupportedEncodingException {
		String name = request.getParameter("name");
		String code = PinyinUtil.converterToFirstSpell(name);
		code = typeService.getUnRepeatCode(code);
		result.setObj(code);
		return result;
	}
	

//	public void findByCode(String code) {
//		List<TypeEntity> list = typeService.queryListByPorperty("code", code);
//		if (list.size() > 0) {
//			int count = list.size();
//			wirteCode = code + String.valueOf(count);
//			findByCode(wirteCode);
//		} else {
//			wirteCode = code;
//		}
//	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月4日 下午5:07:34
	 * @Decription 根据用户ID查询该用户分配可操作的文件夹类型树
	 *
	 * @param request
	 * @param response
	 * @return 
	 */
	@RequestMapping(params = "queryUserFileTypeCheckedTree")
	public void queryUserFileTypeCheckedTree(HttpServletRequest request, HttpServletResponse response) {
		String parentId = "";
		List<TypeTreeVo> typeVoList = new ArrayList<TypeTreeVo>();
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			parentId = request.getParameter("id");
			typeVoList = typeService.queryUserFileTypeTree(request.getParameter("userId"), parentId);
		} else {
			//如果首次进入,则只加载'资料分类'单个节点(因为它来自另一颗树,有很多同级节点)
			parentId = typeService.queryTypeIdByCondition("-1", "file");
			typeVoList = typeService.queryUserFileTypeRoot(request.getParameter("userId"), parentId);
		}
		//树的转换
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		propertyMapping.put(TreeMapper.PropertyType.ICONCLS.getValue(), "iconCls");
		propertyMapping.put(TreeMapper.PropertyType.CHECKED.getValue(), "checked");

		List<TreeNode> treeList = TreeMapper.buildJsonTree(typeVoList, propertyMapping);
		TagUtil.tree(response, treeList);
	}
	
	
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年7月7日 下午4:28:02
	 * @Decription 用户资料分类权限
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "updateUserFileType")
	@ResponseBody
	public AjaxJson updateUserFileType(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson result = new AjaxJson();
		String checkedTypeIds = request.getParameter("checkedTypeIds");
		String pIds = request.getParameter("pIds");
		String userId = request.getParameter("userId");
		try {
			this.typeService.updateUserFileType(checkedTypeIds, pIds, userId);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			result.setMsg("用户资料分类权限更新失败");
			e.printStackTrace();
		}
		result.setMsg("用户资料分类权限更新成功");
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月8日 下午12:57:29
	 * @Decription 更新用户资料分类操作权限
	 *
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(params = "updateUserFileTypeAuthority")
	@ResponseBody
	public AjaxJson updateUserFileTypeAuthority(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson result = new AjaxJson();

		String resourceIds = request.getParameter("resourceIds");
		String typeId = request.getParameter("typeId");
		String userId = request.getParameter("userId");
		try {
			this.typeService.updateUserFileTypeAuthority(resourceIds, typeId, userId);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			result.setMsg("用户资料分类操作权限更新失败");
			e.printStackTrace();
		}
		result.setMsg("用户资料分类操作权限更新成功");
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月4日 下午5:07:34
	 * @Decription 根据角色ID查询该用户分配可操作的文件夹类型树
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "queryRoleFileTypeCheckedTree")
	public void queryRoleFileTypeCheckedTree(HttpServletRequest request, HttpServletResponse response) {
		String parentId = "";
		List<TypeTreeVo> typeVoList = new ArrayList<TypeTreeVo>();
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			parentId = request.getParameter("id");
			typeVoList = typeService.queryRoleFileTypeTree(request.getParameter("roleId"), parentId);
		} else {
			//如果首次进入,则只加载'资料分类'单个节点(因为它来自另一颗树,有很多同级节点)
			parentId = typeService.queryTypeIdByCondition("-1", "file");
			typeVoList = typeService.queryRoleFileTypeRoot(request.getParameter("roleId"), parentId);
		}
		//树的转换
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		propertyMapping.put(TreeMapper.PropertyType.ICONCLS.getValue(), "iconCls");
		propertyMapping.put(TreeMapper.PropertyType.CHECKED.getValue(), "checked");

		List<TreeNode> treeList = TreeMapper.buildJsonTree(typeVoList, propertyMapping);
		TagUtil.tree(response, treeList);
	}
	
	

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月7日 下午4:28:02
	 * @Decription 用户资料分类权限
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "updateRoleFileType")
	@ResponseBody
	public AjaxJson updateRoleFileType(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson result = new AjaxJson();
		String checkedTypeIds = request.getParameter("checkedTypeIds");
		String pIds = request.getParameter("pIds");
		String roleId = request.getParameter("roleId");
		try {
			this.typeService.updateRoleFileType(checkedTypeIds, pIds, roleId);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			result.setMsg("角色资料分类权限更新失败");
			e.printStackTrace();
		}
		result.setMsg("角色资料分类权限更新成功");
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月8日 下午12:57:29
	 * @Decription 更新角色资料分类操作权限
	 *
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(params = "updateRoleFileTypeAuthority")
	@ResponseBody
	public AjaxJson updateRoleFileTypeAuthority(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson result = new AjaxJson();

		String resourceIds = request.getParameter("resourceIds");
		String typeId = request.getParameter("typeId");
		String roleId = request.getParameter("roleId");
		try {
			this.typeService.updateRoleFileTypeAuthority(resourceIds, typeId, roleId);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			result.setMsg("角色资料分类操作权限更新失败");
			e.printStackTrace();
		}
		result.setMsg("角色资料分类操作权限更新成功");
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月16日 下午5:28:41
	 * @Decription 通用上传标签选择分类树所请求的公共方法
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "queryCommonAttachUploadTree")
	public void queryCommonAttachUploadTree(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String defaultTypeCode = request.getParameter("defaultTypeCode");
		String rootTreeCode = request.getParameter("rootTreeCode");
		Boolean onlyAuthority = Boolean.parseBoolean(request.getParameter("onlyAuthority"));
		Boolean containSelf = Boolean.parseBoolean(request.getParameter("containSelf"));
		List<TypeEntity> typeEntityList = new ArrayList<TypeEntity>();
		List<TreeNode> treeList = new ArrayList<TreeNode>();
		List<TypeTreeVo> typeVOList = new ArrayList<TypeTreeVo>();
		//转换MAP设定
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		propertyMapping.put(TreeMapper.PropertyType.ICONCLS.getValue(), "iconCls");
		propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(), "code,name,sysType");

		if (StringUtil.isEmpty(id) && StringUtil.isNotEmpty(rootTreeCode)) {
			if ("rootType".equals(rootTreeCode)) {
				//不填写defaultTypeCode的话,默认赋值defaultTypeCode为rootType,默认加载'资料分类'单个节点(因为它来自另一颗树,有很多同级节点)
				id = typeService.queryTypeIdByCondition("-1", "file");
			} else {
				List<TypeEntity> typeList1 = typeService.queryListByPorperty("code", rootTreeCode);
				//比如如果containSelf为真,那么首次加载的就是单个defaultTypeCode节点,而不是defaultTypeCode的子节点
				if (containSelf) {
					treeList = TreeMapper.buildJsonTree(typeList1, propertyMapping);
					TagUtil.tree(response, treeList);
					return;
				} else {
					if (typeList1.size() > 0) {
						id = typeList1.get(0).getId();
					}
				}
			}
		}

		//设置查询MAP
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("typeId", id);
		queryMap.put("userId", ClientUtil.getUserId());
		//判断是否进行权限过滤
		if (onlyAuthority) {
			if (this.authorityService.isAdmin(queryMap)) {
				typeEntityList = typeService.queryListByPorperty("parent.id", id);
				treeList = TreeMapper.buildJsonTree(typeEntityList, propertyMapping);
			} else { //不是管理员，进行权限过滤
				typeVOList = typeService.queryViewableTypeTree(queryMap);
				treeList = TreeMapper.buildJsonTree(typeVOList, propertyMapping);
			}
		} else {
			typeEntityList = typeService.queryListByPorperty("parent.id", id);
			treeList = TreeMapper.buildJsonTree(typeEntityList, propertyMapping);
		}

		TagUtil.tree(response, treeList);
	}

	
	
	
	
	/**  ============================    重构资料文件夹权限 start    ================================    **/
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年11月18日 下午4:05:21
	 * @Decription  用户资料文件夹权限树
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "getUserDataDirTreegrid")
	@ResponseBody
	public List<TreeGrid> getUserDataDirTreegrid(HttpServletRequest request, HttpServletResponse response) {
		String parentId = "";
		List<TypeTreeVo> typeVoList = new ArrayList<TypeTreeVo>();
		String userId = request.getParameter("userId");
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			parentId = request.getParameter("id");
			typeVoList = typeService.queryUserFileTypeTree(userId, parentId);
		} else {
			//如果首次进入,则只加载'资料分类'单个节点(因为它来自另一颗树,有很多同级节点)
			parentId = typeService.queryTypeIdByCondition("-1", "file");
			typeVoList = typeService.queryUserFileTypeRoot(userId, parentId);
		}
		//查找公司资料管理下所有权限
		List<ResourceEntity> resourceList = resourceService.queryResourceListByModuleCode("dataManager");
		for(TypeTreeVo typeTreeVo : typeVoList){
			//查找用户分类下所有的资源权限
			List<FileTypeUserAuthorityEntity> userAuthorityList = resourceService
					                                         .queryUserFileTypeAuthority(typeTreeVo.getId(), userId);
			Set<String> userAuthoritySet = new HashSet<String>();
			for(FileTypeUserAuthorityEntity userAuthority : userAuthorityList){
				userAuthoritySet.add(userAuthority.getResourceEntity().getId());
			}
			StringBuffer resourceIds = new StringBuffer("");
			if(resourceList != null && resourceList.size()>0){
				resourceIds.append(getCheckbox("selectAll_"+ typeTreeVo.getId(), false, "全选&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", false));
				for(ResourceEntity resource : resourceList){
					resourceIds.append(getCheckbox("resource_" + typeTreeVo.getId() + "_" + resource.getId() + "", 
							userAuthoritySet.contains(resource.getId()), resource.getName() + "&nbsp;&nbsp;&nbsp;", false));
				}
			}else{
				resourceIds.append(getCheckbox("selectAll_"+ typeTreeVo.getId(), false, "全选&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", false));
			}
			typeTreeVo.setResourceIds(resourceIds.toString());
			typeTreeVo.setName(getCheckbox("dataDir_" + typeTreeVo.getId(), "1".equals(typeTreeVo.getChecked()),
					                                                   "", false) + typeTreeVo.getName());
			userAuthoritySet.clear();
		}
		TreeGridModel dto = new TreeGridModel();
		dto.setIdField("id");
		dto.setTextField("name");
		dto.setSrc("url");
		dto.setIsLeaf("isLeaf");
		dto.setResourceIds("resourceIds");
		return commonService.treegrid(typeVoList, dto, null);
	}
	
	
	public String getCheckbox(String id, boolean checked, String htmlValue, boolean isDisplayNone){
		String display = isDisplayNone==true?" style='display:none' ":" ";
		String ck = checked==true?" checked ":" ";
		return "<input type=\"checkbox\" id=\"" + id + "\" " + ck + display + ">" + htmlValue;
	}
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年11月20日 上午11:41:52
	 * @Decription 更新用户资料文件夹及操作按钮权限
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "updateUserDataDirAndResource")
	@ResponseBody
	public AjaxJson updateUserDataDirAndResource(HttpServletRequest request){
		String userId = request.getParameter("userId");
		String dataDirIds = request.getParameter("dataDirIds");
		String unSelParentIds = request.getParameter("unSelParentIds");
		String dataDirResourceIds = request.getParameter("dataDirResourceIds");
		try{
			typeService.updateUserFileType(dataDirIds, unSelParentIds, userId);
			result.setMsg("更新资料文件夹权限成功");
		}catch(Exception e){
			result.setMsg("更新资料文件夹权限失败");
			return result;
		}
		String[] dataDirResourceIdArr = dataDirResourceIds.split(",");
		Map<String, String> dataDirResourceMap = new HashMap<String, String>();
		String resourceIdStr = "";
		for(String dataDirResourceId : dataDirResourceIdArr){
			String[] dataDirResource = dataDirResourceId.split("_"); 
			String dataDirId = dataDirResource[0];
			if(dataDirIds.indexOf(dataDirId) == -1){
				continue; //勾选了操作按钮权限 ，没有勾选模块。不保存勾了的操作按钮权限
			}
			String resourceId = "";
			if(dataDirResource.length>1){
				resourceId = dataDirResource[1];
			}
			resourceIdStr = dataDirResourceMap.get(dataDirId);
			if(StringUtil.isNotEmpty(resourceIdStr)){
				dataDirResourceMap.put(dataDirId, resourceIdStr + "," +resourceId);
			}else{
				dataDirResourceMap.put(dataDirId, resourceId);
			}
		}
		Set<Entry<String, String>> entrySet = dataDirResourceMap.entrySet();
		try{
			for(Entry<String, String> entry:entrySet){
				String dataDirId = entry.getKey();
				String resourceIds = entry.getValue();
				typeService.updateUserFileTypeAuthority(resourceIds, dataDirId, userId);
			}
			result.setMsg("更新资料文件夹及操作按钮权限成功");
		}catch(Exception e){
			result.setMsg("更新资料文件夹及操作按钮权限失败");
		}
		
		return result;
	}
	

	/**
	 * 
	 * @author hexj
	 * @createtime 2014年11月20日 下午2:47:23
	 * @Decription    获取角色 资料文件夹权限treegrid
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "getRoleDataDirTreegrid")
	@ResponseBody
	public List<TreeGrid> getRoleDataDirTreegrid(HttpServletRequest request){
		String parentId = "";
		List<TypeTreeVo> typeVoList = new ArrayList<TypeTreeVo>();
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			parentId = request.getParameter("id");
			typeVoList = typeService.queryRoleFileTypeTree(request.getParameter("roleId"), parentId);
		} else {
			//如果首次进入,则只加载'资料分类'单个节点(因为它来自另一颗树,有很多同级节点)
			parentId = typeService.queryTypeIdByCondition("-1", "file");
			typeVoList = typeService.queryRoleFileTypeRoot(request.getParameter("roleId"), parentId);
		}
		String roleId = request.getParameter("roleId");
		
		//所有按钮操作类型
		List<ResourceEntity> allResources = resourceService.queryResourceListByModuleCode("dataManager");
		
		Set<String> roleResourcesSet = new HashSet<String>(); //保存角色拥有某个模块拥有的操作按钮权限
		for(TypeTreeVo typeTreeVo:typeVoList){
			StringBuffer resourceIds = new StringBuffer("");
			if(allResources != null){
				resourceIds.append(getCheckbox("selectAll_"+typeTreeVo.getId(), false, "全选&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", false));
				for(ResourceEntity resource : allResources){
					//角色拥有资料文件夹的操作按钮权限
					List<FileTypeRoleAuthorityEntity> roleResources = resourceService
							                                           .queryRoleFileTypeAuthority(typeTreeVo.getId(), roleId);
					
					for(FileTypeRoleAuthorityEntity r : roleResources){
						if(r.getResourceEntity() != null){
							roleResourcesSet.add(r.getResourceEntity().getId());
						}
					}
					String r = getCheckbox("resource_" + typeTreeVo.getId() + "_" + resource.getId(),
							roleResourcesSet.contains(resource.getId()), resource.getName() + "&nbsp;&nbsp;&nbsp;", false);
					resourceIds.append(r);
					roleResourcesSet.clear();
				}
			}else{
				resourceIds.append(getCheckbox("selectAll_"+typeTreeVo.getId(), false, "", true));
			}
			typeTreeVo.setResourceIds(resourceIds.toString());
			typeTreeVo.setName(getCheckbox("dataDir_" + typeTreeVo.getId(), "1".equals(typeTreeVo.getChecked()),
					                                                                "", false) + typeTreeVo.getName());
		}
		TreeGridModel dto = new TreeGridModel();
		dto.setIdField("id");
		dto.setTextField("name");
		dto.setSrc("url");
		dto.setIsLeaf("isLeaf");
		dto.setResourceIds("resourceIds");
		return commonService.treegrid(typeVoList, dto, null);
	}
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年11月20日 下午5:10:33
	 * @Decription  更新角色资料文件夹权限
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "updateRoleDataDirAndResource")
	@ResponseBody
	public AjaxJson updateRoleDataDirAndResource(HttpServletRequest request){
		String roleId = request.getParameter("roleId");
		String dataDirIds = request.getParameter("dataDirIds");
		String unSelParentIds = request.getParameter("unSelParentIds");
		String dataDirResourceIds = request.getParameter("dataDirResourceIds");
		try{
			typeService.updateRoleFileType(dataDirIds, unSelParentIds, roleId);
			result.setMsg("更新资料文件夹权限成功");
		}catch(Exception e){
			result.setMsg("更新资料文件夹权限失败");
			return result;
		}
		String[] dataDirResourceIdArr = dataDirResourceIds.split(",");
		Map<String, String> dataDirResourceMap = new HashMap<String, String>();
		String resourceIdStr = "";
		for(String dataDirResourceId : dataDirResourceIdArr){
			String[] dataDirResource = dataDirResourceId.split("_"); 
			String dataDirId = dataDirResource[0];
			if(dataDirIds.indexOf(dataDirId) == -1){
				continue;   //勾选了操作按钮权限 ，没有勾选模块。不保存勾了的操作按钮权限
			}
			String resourceId = "";
			if(dataDirResource.length>1){
				resourceId = dataDirResource[1];
			}
			resourceIdStr = dataDirResourceMap.get(dataDirId);
			if(StringUtil.isNotEmpty(resourceIdStr)){
				dataDirResourceMap.put(dataDirId, resourceIdStr + "," +resourceId);
			}else{
				dataDirResourceMap.put(dataDirId, resourceId);
			}
		}
		Set<Entry<String, String>> entrySet = dataDirResourceMap.entrySet();
		try{
			for(Entry<String, String> entry : entrySet){
				String dataDirId = entry.getKey();
				String resourceIds = entry.getValue();
				typeService.updateRoleFileTypeAuthority(resourceIds, dataDirId, roleId);
			}
			result.setMsg("更新资料文件夹及操作按钮权限成功");
		}catch(Exception e){
			result.setMsg("更新资料文件夹及操作按钮权限失败");
		}
		
		return result;
	}
	
	
	/**  ============================    重构资料文件夹权限 end    ================================    
	
	/**
	 * 查询个人根文件夹(若不存在则创建)
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "queryPersonalRootType")
	@ResponseBody
	public AjaxJson queryPersonalRootType(HttpServletRequest request) throws BusinessException {
		AjaxJson j = new AjaxJson();

		// 如果当前用户无个人文件夹 ,则自动创建个人根文件夹(为了兼容老用户逻辑)
		String typeId = typeService.doQueryPersonalRootType(ClientUtil.getUserId());
		j.setObj(typeId);
		return j;
	}
}
