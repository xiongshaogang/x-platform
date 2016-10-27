package com.xplatform.base.workflow.support.msgtemplate.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.workflow.support.msgtemplate.dao.MsgTemplateDao;
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;
import com.xplatform.base.workflow.support.msgtemplate.service.MsgTemplateService;

/**
 * 
 * description :信息模版service实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:30:12
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 下午12:30:12
 *
 */
@Service("msgTemplateService")
public class MsgTemplateServiceImpl implements MsgTemplateService {
	private static final Logger logger = Logger.getLogger(MsgTemplateServiceImpl.class);
	@Resource
	private MsgTemplateDao msgTemplateDao;
	
	@Resource
	private BaseService baseService;

	@Override
	@Action(moduleCode="MsgTemplateManager",description="信息模版新增",detail="信息模版${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(MsgTemplateEntity MsgTemplate) throws BusinessException {
		String pk="";
		try {
			pk=this.msgTemplateDao.addMsgTemplate(MsgTemplate);
		} catch (Exception e) {
			logger.error("信息模版保存失败");
			throw new BusinessException("信息模版保存失败");
		}
		logger.info("信息模版保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="MsgTemplateManager",description="信息模版删除",detail="信息模版${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.msgTemplateDao.deleteMsgTemplate(id);
		} catch (Exception e) {
			logger.error("信息模版删除失败");
			throw new BusinessException("信息模版删除失败");
		}
		logger.info("信息模版删除成功");
	}

	@Override
	@Action(moduleCode="MsgTemplateManager",description="信息模版批量删除",detail="信息模版${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("信息模版批量删除成功");
	}
	
	public void updateDefault(String useType){
		this.msgTemplateDao.executeSql("update t_flow_msg_template t set t.is_default='Y' where t.use_type=?", useType);
	}

	@Override
	@Action(moduleCode="MsgTemplateManager",description="信息模版修改",detail="信息模版${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(MsgTemplateEntity MsgTemplate) throws BusinessException {
		try {
			MsgTemplateEntity oldEntity = get(MsgTemplate.getId());
			MyBeanUtils.copyBeanNotNull2Bean(MsgTemplate, oldEntity);
			this.msgTemplateDao.updateMsgTemplate(oldEntity);
		} catch (Exception e) {
			logger.error("信息模版更新失败");
			throw new BusinessException("信息模版更新失败");
		}
		logger.info("信息模版更新成功");
	}

	@Override
	public MsgTemplateEntity get(String id) {
		MsgTemplateEntity MsgTemplate=null;
		try {
			MsgTemplate=this.msgTemplateDao.getMsgTemplate(id);
		} catch (Exception e) {
			logger.error("信息模版获取失败");
			//throw new BusinessException("信息模版获取失败");
		}
		logger.info("信息模版获取成功");
		return MsgTemplate;
	}

	@Override
	public List<MsgTemplateEntity> queryList() {
		List<MsgTemplateEntity> MsgTemplateList=new ArrayList<MsgTemplateEntity>();
		try {
			MsgTemplateList=this.msgTemplateDao.queryMsgTemplateList();
		} catch (Exception e) {
			logger.error("信息模版获取列表失败");
			//throw new BusinessException("信息模版获取列表失败");
		}
		logger.info("信息模版获取列表成功");
		return MsgTemplateList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.msgTemplateDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("信息模版获取分页列表失败");
			throw new BusinessException("信息模版获取分页列表失败");
		}
		logger.info("信息模版获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(MsgTemplateEntity.class, param, propertyName);
	}
	
	public void setMsgTemplateDao(MsgTemplateDao msgTemplateDao) {
		this.msgTemplateDao = msgTemplateDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public MsgTemplateEntity getDefaultByUseType(String useType) {
		// TODO Auto-generated method stub
		Map<String,String> param=new HashMap<String,String>();
		param.put("useType", useType);
		param.put("isDefault", "Y");
		List<MsgTemplateEntity> list=this.msgTemplateDao.findByPropertys(MsgTemplateEntity.class, param);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, String> getTempByFun(String useType){
		MsgTemplateEntity sysTemplate = getDefaultByUseType(useType);
	    String title = "";
	    String inner = "";
	    String mail = "";
	    String sms = "";
	    if (BeanUtils.isNotEmpty(sysTemplate)) {
	        title = sysTemplate.getTitle();
	        inner = sysTemplate.getInnerContent();
	        mail = sysTemplate.getMailContent();
	        sms = sysTemplate.getSmsContent();
	    }
	    Map<String,String> map = new HashMap<String,String>();
	    map.put(MsgTemplateEntity.TEMPLATE_TITLE, title);
	    map.put(MsgTemplateEntity.TEMPLATE_TYPE_INNER, inner);
	    map.put(MsgTemplateEntity.TEMPLATE_TYPE_MAIL, mail);
	    map.put(MsgTemplateEntity.TEMPLATE_TYPE_SMS, sms);
	    return map;
	}
}
