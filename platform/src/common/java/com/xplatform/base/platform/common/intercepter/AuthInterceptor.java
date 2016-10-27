package com.xplatform.base.platform.common.intercepter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.ResourceUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.pdf.HttpUtils;
import com.xplatform.base.orgnaization.module.entity.ModuleEntity;
import com.xplatform.base.orgnaization.resouce.entity.ResourceEntity;
import com.xplatform.base.orgnaization.resouce.mybatis.vo.ResourceVo;
import com.xplatform.base.orgnaization.resouce.service.ResourceService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.vo.Client;

/**
 * 
 * description :权限拦截
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月4日 上午11:12:02
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiehs 2014年6月4日 上午11:12:02
 *
 */
public class AuthInterceptor implements HandlerInterceptor {

	private List<String> excludeUrls;

	@Resource
	private AuthorityService authorityService;
	@Resource
	private ResourceService resourceService;
	@Resource
	private SysUserService sysUserService;

	/**
	 * 在controller后拦截
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 在controller前拦截
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		// 只读查看的逻辑(用途很简单,如果请求参数中包含optFlag,则根据其对应的值,再用request set一个新属性给页面)
		ContextHolderUtils.viewPageHandle(request);
		this.globalParamHandle(request);
		
		String requestPath = ResourceUtil.getRequestPath(request);// 用户访问的资源地址
		if (excludeUrls.contains(requestPath)) {
			return true;
		}
		
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		if (client == null) {
			client = ClientManager.getInstance().getClient(request.getParameter("sessionId"));
		}
		// 已经登陆用户
		if (client != null && client.getUser() != null) {
			UserEntity user = ClientManager.getInstance().getClient(ContextHolderUtils.getSession().getId()).getUser();
			String userId = user.getId();
			// String empId=user.getUserTypeId();
			Map<String, String> param = new HashMap<String, String>();
			param.put("userId", userId);
			boolean isAdmin = authorityService.isAdmin(param);
			// 第一步：如果是管理员，不拦截，直接返回true
			if (isAdmin) {
				return true;
			}
			// 第二步：获取权限的信息
			// 如果是查询不到获取资源所在的模块
			ModuleEntity module = authorityService.getResourceModule(requestPath, request);
			// 获取用户所在模块的所有资源权限
			List<ResourceVo> resourceList = null;
			if (module != null) {
				// request.getRequestDispatcher("webpage/common/noAuth.jsp").forward(request,
				// response);
				// return false;
				if (client.getResources() == null) {
					Map<String, Object> resourceParam = new HashMap<String, Object>();
					resourceParam.put("userId", userId);
					// resourceParam.put("empId", empId);
					resourceParam.put("moduleId", module.getId());
					List<String> roleIds = new ArrayList<String>();
					List<RoleEntity> roleList=this.sysUserService.getRoleListByUserId(userId);
					if (roleList != null && roleList.size() != 0) {
						for (RoleEntity r :roleList) {
							roleIds.add(r.getId());
						}
					}
					resourceParam.put("roleIds", StringUtil.addQuotes(roleIds));
					resourceList = authorityService.getUserModuleResources(resourceParam);
				} else {
					resourceList = client.getResources().get(module.getCode());
				}
			}

			// 第三步：设置operationCodes
			boolean flag = false;
			Set<String> operationCodes = new HashSet<String>();
			if (resourceList != null && resourceList.size() > 0) {
				for (ResourceVo resource : resourceList) {
					if (StringUtil.equals(resource.getUrl(), requestPath)) {
						flag = true;
					}
					operationCodes.add(resource.getOptCode());
				}
			}
			request.setAttribute("operationCodes", operationCodes);

			/*
			 * Map<String,String> map = new HashMap<String,String>();
			 * map.put("url", requestPath); List<ResourceEntity> list =
			 * this.resourceService.findByPropertys(map); for(ResourceEntity r :
			 * list){ if(StringUtils.equals("common", r.getFilterType()) &&
			 * StringUtils.equals("N", r.getIsInterceptor())){ flag=true; } }
			 */

			// 第四步：如果这个url没有配置拦截，那么默认是不拦截的
			// 获取拦截这个url的resource,如果查询不到，那么不拦截，直接返回true
			Map<String, String> resourceMap = new HashMap<String, String>();
			resourceMap.put("isInterceptor", "Y");
			resourceMap.put("url", requestPath);
			List<ResourceEntity> interceptorList = this.resourceService.findByPropertys(resourceMap);
			if (interceptorList == null || interceptorList.size() == 0) {
				return true;
			}

			// 第五步：如果是模块的url，则不进行url的拦截
			ModuleEntity moduleAuthority = authorityService.getUrlModule(requestPath);// 获取资源所在的模块
			if (moduleAuthority != null) {
				return true;
			}
			// 第六步：没有资源权限
			if (!flag) {
				// response.sendRedirect("loginController.do?noAuth");
				// request.getRequestDispatcher("webpage/common/noAuth.jsp").forward(request,
				// response);
				return false;
			}
			return flag;
			// return true;
		} else {
			if (HttpUtils.isMoblie(request)) {
				// 移动端返回session失效信息
				/*AjaxJson result = new AjaxJson();
				result.setSuccess(false);
				result.setStatus("sessionTimeOut");
				result.setMsg("session失效");*/
				//response.getWriter().print(JSONHelper.toJSONString(result));
				response.sendRedirect("timeout.jsp");
				return false;
			} else {
				// web端 则跳转 到登陆页面
				request.getRequestDispatcher("index.jsp").forward(request, response);
				return false;
			}
		}
	}
	
	/**
	 * 视图端,全局参数的通用传递
	 * @param request
	 */
	public void globalParamHandle(ServletRequest request) {
		request.setAttribute("attachForeRequest", ConfigConst.serverUrl + ConfigConst.attachRequest);
		request.setAttribute("attachImgRequest", ConfigConst.serverUrl + ConfigConst.attachImgRequest);
		request.setAttribute("attachThumbnailRequest", ConfigConst.serverUrl + ConfigConst.attachThumbnailRequest);
	}

	/**
	 * 转发
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
//	@RequestMapping(params = "forword")
//	public ModelAndView forword(HttpServletRequest request) {
//		return new ModelAndView(new RedirectView("loginController.do?login"));
//	}

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

}
