package com.xplatform.base.framework.core.common.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.JSONHelper;

/**
 * spring mvc异常捕获类
 * 
 */
@Component
public class CustomSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {

	private static final Logger logger = Logger.getLogger(CustomSimpleMappingExceptionResolver.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		String viewName = determineViewName(ex, request);
		if (viewName != null) {
			// if (!(request.getHeader("accept").indexOf("application/json") >
			// -1 || (request.getHeader("X-Requested-With") != null &&
			// request.getHeader(
			// "X-Requested-With").indexOf("XMLHttpRequest") > -1))) {

			// 只要request的header的accept,包含了application/json的才是ajax异步请求,否则都是跳转页面请求
			if ((request.getHeader("accept") == null || !(request.getHeader("accept") != null && request.getHeader("accept").indexOf("application/json") > -1))) {
				// 如果是非异步请求(按照springmvc原逻辑处理)
				Integer statusCode = determineStatusCode(request, viewName);
				if (statusCode != null) {
					applyStatusCodeIfPossible(request, response, statusCode);
				}
				ExceptionUtil.printStackTraceAndLogger(ex);
				return getModelAndView(viewName, ex, request);
			} else {
				// 异步请求,返回json
				try {
					AjaxJson result = new AjaxJson();
					PrintWriter writer = response.getWriter();
					result.setMsg(ex.getMessage());
					result.setSuccess(false);
					result.setInfo(null);
					writer.write(JSONHelper.bean2json(result));
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		} else {
			return null;
		}
	}
}
