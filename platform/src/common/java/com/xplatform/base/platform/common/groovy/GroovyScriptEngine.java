package com.xplatform.base.platform.common.groovy;

import groovy.lang.GroovyShell;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.platform.common.script.IScript;

/**
 * 
 * description :脚本引擎
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年7月3日 下午4:39:28
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月3日 下午4:39:28
 *
 */
public class GroovyScriptEngine implements BeanPostProcessor {
	private Log logger = LogFactory.getLog(GroovyScriptEngine.class);
	public static GroovyBinding binding = new GroovyBinding();

	/**
	 * 执行所有的类型
	 */
	public void execute(String script, Map<String, Object> vars) {
		executeObject(script, vars);
	}

	/**
	 * 设置参数
	 * @author xiehs
	 * @createtime 2014年7月3日 下午4:40:00
	 * @Decription
	 *
	 * @param shell
	 * @param vars
	 */
	private void setParameters(GroovyShell shell, Map<String, Object> vars) {
		if (vars == null)
			return;
		Set set = vars.entrySet();
		for (Iterator it = set.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			shell.setVariable((String) entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 执行boolean的类型
	 */
	public boolean executeBoolean(String script, Map<String, Object> vars) {
		Boolean rtn = (Boolean) executeObject(script, vars);
		return rtn.booleanValue();
	}

	/**
	 * 执行string的类型
	 */
	public String executeString(String script, Map<String, Object> vars) {
		String str = (String) executeObject(script, vars);
		return str;
	}

	/**
	 * 执行int的类型
	 */
	public int executeInt(String script, Map<String, Object> vars) {
		Integer rtn = (Integer) executeObject(script, vars);
		return rtn.intValue();
	}

	/**
	 * 执行float的类型
	 */
	public float executeFloat(String script, Map<String, Object> vars) {
		Float rtn = (Float) executeObject(script, vars);
		return rtn.floatValue();
	}

	/**
	 * 最终执行方法
	 */
	public Object executeObject(String script, Map<String, Object> vars) {
		this.logger.debug("执行:" + script);
		GroovyShell shell = new GroovyShell(this.binding);
		setParameters(shell, vars);

		script = script.replace("&apos;", "'").replace("&quot;", "\"").replace(
				"&gt;", ">").replace("&lt;", "<").replace("&nuot;", "\n")
				.replace("&amp;", "&");

		Object rtn = shell.evaluate(script);
		this.binding.clearVariables();
		return rtn;
	}

	//ioc初始化完成后,将所有继承了IScript的bean交给groovy管理
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		//boolean rtn = BeanUtils.isInherit(bean.getClass(), BaseService.class);
		boolean isImplScript = BeanUtils.isInherit(bean.getClass(),IScript.class);
		if (isImplScript) {//如果继承了IScript,那么就会将对象放在脚本引擎的变量中，在脚本引擎就可以通过对象调用方法
			this.binding.setProperty(beanName, bean);
		}
		return bean;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}
}