package com.xplatform.base.framework.core.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
/**
* @ClassName: ContextHolderUtils 
* @Description: TODO(上下文工具类) 
* @author  张代浩 
* @date 2012-12-15 下午11:27:39 
*
 */
public class ContextHolderUtils {
	/**
	 * SpringMvc下获取request
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;

	}
	/**
	 * SpringMvc下获取session
	 * 
	 * @return
	 */
	public static HttpSession getSession() {
		HttpSession session = getRequest().getSession();
		return session;

	}

	/**
	 * 只读查看的逻辑(用途很简单,如果请求参数中包含optFlag/viewType,则根据其对应的值,再用request set一个新属性给页面)
	 * @param request
	 */
	public static void viewPageHandle(ServletRequest request) {
		// 用于后台系统页面
		String optFlag = request.getParameter("optFlag");
		if (StringUtil.isNotEmpty(optFlag)) {
			request.setAttribute("optFlag", optFlag);
			if ("detail".equals(optFlag)) {
				request.setAttribute("isEdit", false);
			}
		}
		// 用于前台系统页面
		String viewType = request.getParameter("viewType");
		if (StringUtil.isNotEmpty(viewType)) {
			request.setAttribute("viewType", viewType);
		}
	}
}
