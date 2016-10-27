package com.xplatform.base.framework.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.Logger;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;

/**
 * Exception工具类
 * 
 * @author 张代浩
 */
public class ExceptionUtil {

	private ExceptionUtil() {
		// 保证其无法用new实例化
	}

	/**
	 * 返回错误信息字符串
	 * 
	 * @param ex
	 *            Exception
	 * @return 错误信息字符串
	 */
	public static String getExceptionMessage(Exception ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		return sw.toString();
	}

	/**
	 * 如果目标为空则抛出异常
	 * 
	 * @author 苍鹰 2009-11-3
	 * @param target
	 * @param errorMessage
	 */
	public static void throwIfNull(Object target, String errorMessage) {
		if (target == null) {
			throw new BusinessRuntimeException(errorMessage);
		}
	}

	/**
	 * 如果目标为空则抛出异常 本方法空指针安全 2009-11-3
	 * 
	 * @param target
	 * @param errorMessage
	 */
	public static void throwIfEmpty(String target, String errorMessage) {
		if (target == null || target.equals("")) {
			throw new BusinessRuntimeException(errorMessage);
		}
	}

	/**
	 * 打印错误堆信息以及message
	 * 
	 * @param ex
	 *            Exception
	 * @return 错误信息字符串
	 */
	public static String printStackTraceAndLogger(Exception e) {
		// if (StringUtil.isNotEmpty(e.getMessage()))
		// System.err.println("系统报错出错错误原因:" + e.getMessage());
		e.printStackTrace();
		return e.getMessage();
	}

	/**
	 * 打印错误堆信息以及logger
	 * 
	 * @param ex
	 *            Exception
	 * @param Logger
	 *            logger对象
	 * @return 错误信息字符串
	 */
	public static String printStackTraceAndLogger(Exception e, Logger logger) {
		e.printStackTrace();
		logger.error(e.getMessage());
		return e.getMessage();
	}

	public static void throwBusinessException(String message) throws BusinessException {
		throw new BusinessException(message);
	}
	
	public static void throwBusinessException(String message, Logger logger) throws BusinessException {
		logger.error(message);
		throw new BusinessException(message);
	}
	
	public static void throwException(String message) throws Exception {
		throw new Exception(message);
	}
	
	public static void throwException(String message, Logger logger) throws Exception {
		logger.error(message);
		throw new Exception(message);
	}
	
}
