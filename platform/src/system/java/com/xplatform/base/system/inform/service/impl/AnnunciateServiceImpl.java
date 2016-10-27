package com.xplatform.base.system.inform.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.platform.common.script.IScript;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.inform.dao.AnnunciateDao;
import com.xplatform.base.system.inform.entity.AnnunciateEntity;
import com.xplatform.base.system.inform.service.AnnunciateService;
import com.xplatform.base.system.message.config.entity.MessageGroupSendEntity;
import com.xplatform.base.system.message.config.service.MessageService;
import com.xplatform.base.system.message.config.util.MsgUtil;
import com.xplatform.base.work.schedule.mybatis.dao.ReminderMybatisDao;
import com.xplatform.base.workflow.task.service.TaskOpinionService;

@Service("annunciateService")
public class AnnunciateServiceImpl implements AnnunciateService,IScript {
	
private static final Logger logger = Logger.getLogger(AnnunciateServiceImpl.class);
	
	@Resource
	private AnnunciateDao annunciateDao;
	
	@Resource
	private TaskOpinionService taskOpinionService;
	
	@Resource
	private ReminderMybatisDao reminderMybatisDao;
	
	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private MessageService messageService;

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		try {
			this.annunciateDao.getDataGridReturn(cq, b);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("查询内部通告列表失败");
		}
	}

	@Override
	public void deleteByAnnunciateId(String id) {
		// TODO Auto-generated method stub
		AnnunciateEntity ae = this.annunciateDao.get(AnnunciateEntity.class, id);
		ae.setStatus("2");
		this.annunciateDao.merge(ae);
	}

	@Override
	public AnnunciateEntity getAnnunciateById(String id) {
		// TODO Auto-generated method stub
		return this.annunciateDao.get(AnnunciateEntity.class, id);
	}

	@Override
	public void updateAnnunciateEntity(AnnunciateEntity annunciate) {
		// TODO Auto-generated method stub
		AnnunciateEntity me = this.getAnnunciateById(annunciate.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(annunciate, me);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("实体更新出错AnnunciateEntity id="+annunciate.getId());
		}
		this.annunciateDao.merge(me);
	}

	@Override
	public void saveAnnunciateEntity(AnnunciateEntity annunciate) {
		// TODO Auto-generated method stub
		this.annunciateDao.save(annunciate);
	}

	@Override
	public String getReference(AnnunciateEntity me) {
		// TODO Auto-generated method stub
		String reference = "";
		if(StringUtils.isNotEmpty(me.getReference())){
			reference = me.getReference();
		}else{
			reference += me.getCompanyId()+"-";
			//EmpUserVo user = ClientUtil.getSessionUserInfo();
			//EmployeeEntity emp = this.employeeService.get(ClientUtil.getUserId());
			/*OrgnaizationEntity  dept = this.deptService.get(emp.getPrimaryOrgId());
			reference += dept != null ? dept.getCode()+"-" : "";*/
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String date = df.format(new Date());
			reference += "("+date.substring(0, 4)+")";
			
			List<Map<String,Object>> list = this.annunciateDao.findForJdbc("select max(right(t.reference, 3)) as idx from t_sys_message_annunciate t where (t.id is null or t.id <> ?)",new Object[]{me.getId()});
			String index = (String) list.get(0).get("idx");
			if("null".equals(index) || StringUtil.isEmpty(index)){
					reference += "001";
			}else{
				StringBuilder sb = new StringBuilder();
				index = String.valueOf(Integer.valueOf(index) + 1);
				for(int i=index.length();i<3;i++){
					sb.append("0");
				}
				reference += sb.toString() + index;
			}
		}
		
		return reference;
	}

	@Override
	public Map<String, String> getApproveResult(String id) {
		// TODO Auto-generated method stub
		Map<String,String> result = new HashMap<String,String>();
		Map<String, Object> param = new HashMap<String,Object>();
		param.put("id", id);
		param.put("taskId", "UserTask2");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list = this.reminderMybatisDao.queryAnnApprove(param);
		if(list.size()>0){
			result.put("pm", list.get(0).get("opinion")!=null ? list.get(0).get("opinion").toString() :"" );
			result.put("pmt", list.get(0).get("update_time")!=null ? list.get(0).get("update_time").toString() :"");
		}
		param.put("taskId", "UserTask3");
		list = this.reminderMybatisDao.queryAnnApprove(param);
		if(list.size()>0){
			result.put("gm", list.get(0).get("opinion")!=null ? list.get(0).get("opinion").toString() :"" );
			result.put("gmt", list.get(0).get("update_time")!=null ? list.get(0).get("update_time").toString() :"" );
		}
		return result;
	}
	
	
	public void sendMsg(String id){
//		AnnunciateEntity annunciateEntity = getAnnunciateById(id);
//		//发送消息 往send表里面插入记录
//		MessageSendEntity ms = new MessageSendEntity();
//		ms.setTitle(annunciateEntity.getTitle());
//		ms.setMessageType(annunciateEntity.getMessageType());
//		ms.setSendType(annunciateEntity.getSendType());
//		ms.setSendTime(new Date());
//		ms.setMailConfigId(annunciateEntity.getMailConfigId());
//		/*if("1".equals(annunciateEntity.getAllUsers())){
//			ms.setReceiveIds(MsgUtil.getEmpId(sysUserService.getQueryListAll()));
//		}else{
//			String emps = "";
//			//如果员工不为空
//			if(StringUtils.isNotEmpty(annunciateEntity.getEmpIds())){
//				emps += annunciateEntity.getEmpIds();
//			}
//			//如果发送至岗位不为空
//			if(StringUtils.isNotEmpty(annunciateEntity.getJobIds())){
//				emps +=","+MsgUtil.getEmpId(sysUserService.getUserByJobs(annunciateEntity.getJobIds()));
//			}
//			//如果发送至机构不为空
//			if(StringUtils.isNotEmpty(annunciateEntity.getOrgIds())){
//				emps +=","+MsgUtil.getEmpId(sysUserService.getUserByOrgIds(annunciateEntity.getOrgIds()));
//			}
//		    ms.setReceiveIds(emps);
//		}*/
//		ms.setMailCC(annunciateEntity.getCopiedIds());
//		if(annunciateEntity.getSendType() !=null && annunciateEntity.getSendType().contains("innerMessage")){
//			ms.setInnerContent(annunciateEntity.getContent());
//			ms.setInnerStatus("0");
//		}
//		if(annunciateEntity.getSendType() !=null && annunciateEntity.getSendType().contains("sms")){
//			//ms.setSmsContent(MsgUtil.getAnnunciateSmsContent(annunciateEntity));
//			ms.setSmsStatus("0");
//		}
//		if(annunciateEntity.getSendType() !=null && annunciateEntity.getSendType().contains("email")){
//            ms.setMailContent(annunciateEntity.getContent().replaceAll("src=\"(.*?)/([\\w.]+)\"", "src=\"#\""));
//            ms.setMailStatus("0");
//		}
//		ms.setTypeId(annunciateEntity.getId());
//	    this.messageService.saveMsgSend(ms);
//	    annunciateEntity.setStatus("3");
//	    this.updateAnnunciateEntity(annunciateEntity);
	}
	
	
	public void setAnnunciateDao(AnnunciateDao annunciateDao) {
		this.annunciateDao = annunciateDao;
	}


	public void setReminderMybatisDao(ReminderMybatisDao reminderMybatisDao) {
		this.reminderMybatisDao = reminderMybatisDao;
	}

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

}
