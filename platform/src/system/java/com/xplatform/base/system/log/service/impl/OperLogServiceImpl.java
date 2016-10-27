package com.xplatform.base.system.log.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.framework.core.util.IpUtil;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.ServletUtil;
import com.xplatform.base.orgnaization.module.entity.ModuleEntity;
import com.xplatform.base.orgnaization.module.service.ModuleService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.log.dao.OperLogDao;
import com.xplatform.base.system.log.entity.OperLogEntity;
import com.xplatform.base.system.log.service.OperLogService;

import freemarker.template.TemplateException;


@Service("operLogService")
public class OperLogServiceImpl implements OperLogService {

    private static final Logger logger = Logger.getLogger(OperLogServiceImpl.class);

    @Resource
	private OperLogDao operLogDao;
	
	@Resource
	private ModuleService moduleService;
	
	private static boolean isCommonServicesInited = false;
	private static Map<String, Object> commonServices = new HashMap();

	public void setOperLogDao(OperLogDao operLogDao) {
		this.operLogDao = operLogDao;
	}

 	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	@Override
	public String save(OperLogEntity operLog) throws Exception {
		// TODO Auto-generated method stub
		String pk="";
		try {
			pk=this.operLogDao.addOperLog(operLog);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("系统日志保存失败");
		}
		logger.info("系统日志保存成功");
		return pk;
	}

	@Override
	public void delete(String id) throws Exception {
		// TODO Auto-generated method stub
		try {
			this.operLogDao.deleteOperLog(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("系统日志删除失败");
		}
		logger.info("系统日志删除成功");
	}

	@Override
	public void batchDelete(String ids) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("系统日志批量删除成功");
	}

	@Override
	public void update(OperLogEntity operLog) throws Exception {
		// TODO Auto-generated method stub
		try {
			this.operLogDao.updateOperLog(operLog);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("系统日志更新失败");
		}
		logger.info("系统日志更新成功");
	}

	@Override
	public OperLogEntity get(String id){
		// TODO Auto-generated method stub
		OperLogEntity operLog=null;
		try {
			operLog=this.operLogDao.getOperLog(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("系统日志获取失败");
		}
		logger.info("系统日志获取成功");
		return operLog;
	}

	@Override
	public List<OperLogEntity> queryList(){
		// TODO Auto-generated method stub
		List<OperLogEntity> operLogList=new ArrayList<OperLogEntity>();
		try {
			operLogList=this.operLogDao.queryOperLogList();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("系统日志获取列表失败");
		}
		logger.info("系统日志获取列表成功");
		return operLogList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b){
		// TODO Auto-generated method stub
		try {
			this.operLogDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("系统日志获取分页列表失败");
		}
		logger.info("系统日志获取分页列表成功");
	}

	@Override
	public void saveOperLogHand(OperLogEntity operLog) throws Exception {
		// TODO Auto-generated method stub
		List<ModuleEntity> list = moduleService.findByProperty(ModuleEntity.class, "code", operLog.getModuleName());
		HttpServletRequest request = ContextHolderUtils.getRequest();
		OperLogEntity newLog = new OperLogEntity();
		if(list.size() > 0 && "1".equals(list.get(0).getStatus())){
			newLog.setModuleName(list.get(0).getName());
			newLog.setDetail(parseDetail(operLog.getDetail(),request));
			newLog.setIp(IpUtil.getIpAddr(request));
			UserEntity user = ClientUtil.getUserEntity();
			if(user != null){
				newLog.setUserId(user.getId());
				newLog.setUserName(user.getUserName());
			}
			newLog.setTime(new Date());
			newLog.setRequesturi(request.getRequestURI());
			MyBeanUtils.copyBeanNotNull2Bean(newLog, operLog);
			this.save(operLog);
		}
	}
	private String parseDetail(String detail, HttpServletRequest request) throws TemplateException, IOException{
		Map map = new HashMap();

		if (request != null) {
			map.putAll(ServletUtil.getFilterMap(request));
		}
		map.put("request", request);
		initCommonServices();
		map.putAll(commonServices);

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
}