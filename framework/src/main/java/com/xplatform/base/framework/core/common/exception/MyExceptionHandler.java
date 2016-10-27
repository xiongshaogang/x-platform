package com.xplatform.base.framework.core.common.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.ExceptionUtil;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * spring mvc异常捕获类
 * 
 */
@Component
public class MyExceptionHandler implements HandlerExceptionResolver {

	private static final Logger logger = Logger.getLogger(MyExceptionHandler.class);

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		Map<String, Object> model = new HashMap<String, Object>();
		// String exceptionMessage = ExceptionUtil.getExceptionMessage(ex);
		// logger.error(exceptionMessage);
		// Map<String, Object> model = new HashMap<String, Object>();
		// model.put("exceptionMessage", exceptionMessage);
		// model.put("ex", ex);
		// return new ModelAndView("common/error", model);

		if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request.getHeader("X-Requested-With") != null && request.getHeader(
				"X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
			// 如果是非异步请求,也就是跳转类,加载页面类请求(按照springmvc原逻辑处理)
			return new ModelAndView("common/error/404");
		} else {
			// 异步请求,返回json
			try {
				PrintWriter writer = response.getWriter();
				writer.write(ex.getMessage());
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			model.put("ex", ex);
			return new ModelAndView("common/error/404", model);
		}
	}
}
