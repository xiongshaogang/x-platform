package com.xplatform.base.framework.core.util;

import java.io.File;
import java.util.Map;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * description :获取bean的信息
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月17日 下午1:45:05
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月17日 下午1:45:05
 *
 */
public class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	private static ServletContext servletContext;
	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(ApplicationContextUtil.class);
	
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		ApplicationContextUtil.applicationContext = context;
	}

	public static ApplicationContext getContext() {
		return applicationContext;
	}
	
	public static void init(ServletContext _servletContext) {
		servletContext = _servletContext;
	}



	public static ServletContext getServletContext() throws Exception {
		return servletContext;
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 * 
	 * @param requiredType 转换的类型
	 * @param <T> 泛型类型
	 * @return Spring中的bean对象
	 */
	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}
	
	/**
	 * 从静态变量applicationContext中取得Beans, 自动转型为所赋值对象的类型.
	 * 
	 * @param requiredType 请求类型
	 * @param <T> 泛型
	 * @return 该类型及其子类的集合
	 */
	public static <T> Map<String,T> getBeans(Class<T> requiredType){
		return applicationContext.getBeansOfType(requiredType);
	}

	public static String getAppAbsolutePath() {
		return servletContext.getRealPath("/");
	}

	public static String getRealPath(String path) {
		return servletContext.getRealPath(path);
	}


	public static String getClasspath() {
		String classPath = Thread.currentThread().getContextClassLoader()
				.getResource("").getPath();
		String rootPath = "";

		if ("\\".equals(File.separator)) {
			rootPath = classPath.substring(1);
			rootPath = rootPath.replace("/", "\\");
		}

		if ("/".equals(File.separator)) {
			rootPath = classPath.substring(1);
			rootPath = rootPath.replace("\\", "/");
		}
		return rootPath;
	}
	
	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 */
	public static void clear() {
		logger.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
		applicationContext = null;
	}

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(ApplicationContextUtil.class);
		logger.info("path:" + getClasspath());
	}
}
