package com.xplatform.base.system.log.aop;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.context.ApplicationContext;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.framework.core.util.IpUtil;
import com.xplatform.base.framework.core.util.ServletUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.module.entity.ModuleEntity;
import com.xplatform.base.orgnaization.module.service.ModuleService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.log.entity.OperLogEntity;
import com.xplatform.base.system.log.service.OperLogService;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * description :日志拦截
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月6日 下午5:49:11
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年6月6日 下午5:49:11
 *
 */
public class LogAspect {
	
	private static WorkQueue wq = new WorkQueue(10);
	
	@Resource
	private OperLogService operLogService;
	
	@Resource
	private ModuleService moduleService;
	
	private static boolean isCommonServicesInited = false;
	private static Map<String, Object> commonServices = new HashMap();

	static Map<String, TemplateHashModel> STATIC_CLASSES = new HashMap();
	
	public void setOperLogService(OperLogService operLogService) {
		this.operLogService = operLogService;
	}

	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}
	
	public void doThrowing(JoinPoint point, Throwable ex) { 
		System.out.println("method " + point.getTarget().getClass().getName() + "." + point.getSignature().getName() + " throw exception"); 
		System.out.println(ex.getMessage()); 
		String methodName = point.getSignature().getName();
	    Class targetClass = point.getTarget().getClass();
	    Method[] methods = targetClass.getMethods();
	    Method method = null;
	    for (int i = 0; i < methods.length; i++) {
	      if (StringUtil.equals(methods[i].getName(), methodName)) {
	        method = methods[i];
	        break;
	      }
	    }
	    if (method == null) {
	      return ;
	    }
	    Action annotation = (Action) method.getAnnotation(Action.class);
	    if (annotation == null) {
	      return ;
	    }
	}
	
	public Object doAudit(ProceedingJoinPoint point) throws Throwable{
		Object returnVal = null;
		System.out.println("日志拦截");
		String methodName = point.getSignature().getName();

	    Class targetClass = point.getTarget().getClass();

	    Method[] methods = targetClass.getMethods();
	    Method method = null;
	    for (int i = 0; i < methods.length; i++) {
	      if (methods[i].getName() == methodName) {
	        method = methods[i];
	        break;
	      }
	    }

	    if (method == null) {
	      return point.proceed();
	    }
	    Action annotation = (Action) method.getAnnotation(Action.class);

		if (annotation == null) {
			return point.proceed();
		}

		if (ActionExecOrder.BEFORE.equals(annotation.execOrder())) {
			doLog(point, false);
			returnVal = point.proceed();
		} else if (ActionExecOrder.AFTER.equals(annotation.execOrder())) {
			returnVal = point.proceed();
			doLog(point, true);
		} else {
			returnVal = point.proceed();
			doLog(point, true);
		}
		return returnVal;
	}
	
	private void doLog(ProceedingJoinPoint point, boolean async) throws Exception {
		String methodName = point.getSignature().getName();
		if (StringUtils.isEmpty(methodName)) {
			return;
		}
		Class targetClass = point.getTarget().getClass();

		Method[] methods = targetClass.getMethods();
		Method method = null;
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName() == methodName) {
				method = methods[i];
				break;
			}
		}

		if (method == null) {
			return;
		}
		Action annotation = (Action) method.getAnnotation(Action.class);

		if (annotation == null) {
			return;
		}
		
		String methodDescp = annotation.description();
		String moduleCode = annotation.moduleCode();
		if(StringUtils.isEmpty(moduleCode)){
			return;
		}
		List<ModuleEntity> list = moduleService.findByProperty(ModuleEntity.class, "code", moduleCode);
		//如果如果模板code未找到，则利用类名进行查找
		if(list.size() == 0){
			String calssName = targetClass.getName().substring(targetClass.getName().lastIndexOf(".")+1, targetClass.getName().length()).replace("ServiceImpl", "").toLowerCase()+"Manager";
			list = moduleService.findByProperty(ModuleEntity.class, "code", calssName);
		}
		if(list.size() == 0 || !StringUtils.equals("1", list.get(0).getStatus())){
			return;
		}
		HttpServletRequest request = ContextHolderUtils.getRequest();
		OperLogEntity logs = new OperLogEntity();
		logs.setModuleName(list.get(0).getName());
		logs.setOpname(methodDescp);
		UserEntity user = ClientUtil.getUserEntity();
		if(user != null){
			logs.setUserId(user.getId());
			logs.setUserName(user.getUserName());
		}
		logs.setTime(new Date());
		logs.setIp(IpUtil.getIpAddr(request));
		logs.setMethod(targetClass.getName() + "."
				+ method.getName());
		logs.setRequesturi(request.getRequestURI());
		String params="";
		for(int i=0;i<point.getArgs().length;i++){
			params += point.getArgs()[i].toString()+",";
		}
		if(StringUtils.isNotEmpty(params)){
		params = params.substring(0, params.lastIndexOf(","));
		}
		logs.setParams(method.getName()+"("+params+")");
		
		String detail = annotation.detail();
		if(async){
			LogHolder logHolder = new LogHolder();
				if (StringUtils.isNotEmpty(detail)) {
					Map map = new HashMap();

					if (request != null) {
						map.putAll(ServletUtil.getFilterMap(request));
					}

					//map.putAll(SysLogThreadLocalHolder.getParamerters());

					initCommonServices();
					map.putAll(commonServices);

					map.putAll(STATIC_CLASSES);
					map.put("params", point.getArgs());

					logHolder.setParseDataModel(map);
					logHolder.setNeedParse(true);
				}
			logs.setDetail(detail);
			logHolder.setOperLogEntity(logs);
			doLogAsync(logHolder);
		
		}else{
			if(StringUtils.isNotEmpty(detail)){
				try {
					detail = parseDetail(detail, request,point);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					detail = null;
				} 
		}
			logs.setDetail(detail);
			operLogService.save(logs);
		}
	}
	
	private void doLogAsync(LogHolder holder) {
		LogExecutor logExecutor = new LogExecutor();
		logExecutor.setLogHolders(holder);
		wq.execute(logExecutor);
	}
	
	private String parseDetail(String detail, HttpServletRequest request, ProceedingJoinPoint point) throws TemplateException, IOException{
		Map map = new HashMap();

		if (request != null) {
			map.putAll(ServletUtil.getFilterMap(request));
		}
		map.put("request", request);

		/*map.putAll(SysAuditThreadLocalHolder.getParamerters());

		map.put("SysAuditLinkService", this.SysAuditLinkService);*/
		initCommonServices();
		map.putAll(commonServices);

		map.putAll(STATIC_CLASSES);
		
		map.put("params", point.getArgs());

		return FreemarkerHelper.parseByStringTemplate(map, detail);
	}
	
	private void initCommonServices() {
		if (isCommonServicesInited) {
			return;
		}
		ApplicationContext context = ApplicationContextUtil.getContext().getParent() == null ? ApplicationContextUtil.getContext() : ApplicationContextUtil.getContext().getParent();
		String[] beanNames = context.getBeanDefinitionNames();
		for (String beanName : beanNames) {
			try {
				Object bean = ApplicationContextUtil.getBean(beanName);
				if (StringUtils.contains(beanName.toLowerCase(), "service")){
					commonServices.put(beanName, bean);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		isCommonServicesInited = true;
	}

	static {
		try {
			BeansWrapper beansWrapper = BeansWrapper.getDefaultInstance();
			TemplateHashModel staticModel = beansWrapper.getStaticModels();
			STATIC_CLASSES.put(Long.class.getSimpleName(),
					(TemplateHashModel) staticModel.get(Long.class.getName()));
			STATIC_CLASSES.put(Integer.class.getSimpleName(),
					(TemplateHashModel) staticModel
							.get(Integer.class.getName()));
			STATIC_CLASSES
					.put(String.class.getSimpleName(),
							(TemplateHashModel) staticModel.get(String.class
									.getName()));
			STATIC_CLASSES.put(Short.class.getSimpleName(),
					(TemplateHashModel) staticModel.get(Short.class.getName()));
			STATIC_CLASSES.put(Boolean.class.getSimpleName(),
					(TemplateHashModel) staticModel
							.get(Boolean.class.getName()));
			STATIC_CLASSES.put(StringUtil.class.getSimpleName(),
					(TemplateHashModel) staticModel.get(StringUtil.class
							.getName()));
			STATIC_CLASSES.put(StringUtils.class.getSimpleName(),
					(TemplateHashModel) staticModel.get(StringUtils.class
							.getName()));
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}
	}
}
