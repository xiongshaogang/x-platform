package com.xplatform.base.system.message.config.job;

import java.util.List;
import java.util.ResourceBundle;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.platform.common.mybatis.dao.SysUserMybatisDao;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.system.message.config.entity.MessageFromEntity;
import com.xplatform.base.system.message.config.service.MessageService;
import com.xplatform.base.system.timer.job.BaseJob;

public class MessageJob extends BaseJob{

	protected Logger logger = LoggerFactory.getLogger(MessageJob.class);
	private MessageService messageService=ApplicationContextUtil.getBean("messageService");
	
	private SysUserService sysUserService=ApplicationContextUtil.getBean("sysUserService");
	
	private SysUserMybatisDao sysUserMybatisDao=ApplicationContextUtil.getBean("sysUserMybatisDao");
	
	private static final ResourceBundle bundle = ResourceBundle.getBundle("sysConfig");
	
	@Override
	public void executeJob(JobExecutionContext paramJobExecutionContext)
			throws Exception {
		// TODO Auto-generated method stub
		List<MessageFromEntity> msgFromList = this.messageService.queryMessageFromList();
		
		
		/*for(MessageFromEntity msg : msgFromList){
			String[] sendTypes = msg.getSendType().split(",");
			
			BusinessEntity bus = this.messageService.getBusinessByFromId(msg.getId());
			//保存接收人相关信息
			List<EmpUserVo> list = new ArrayList<EmpUserVo>();
			List<EmpUserVo> listcc = new ArrayList<EmpUserVo>();
			//如果发送至全体员工
			if("1".equals(msg.getAllUsers())){
				 list = sysUserService.getQueryListAll();
			}else{
				//如果员工不为空
				if(StringUtils.isNotEmpty(msg.getEmpIds())){
					List<EmpUserVo> empList = sysUserService.getEmpUserByEmpIds(msg.getEmpIds());
					list.addAll(empList);
				}
				//如果发送至岗位不为空
				if(StringUtils.isNotEmpty(msg.getJobIds())){
					List<EmpUserVo> empList = sysUserService.getEmpUserByJobs(msg.getJobIds());
					list.addAll(empList);
				}
				//如果发送至机构不为空
				if(StringUtils.isNotEmpty(msg.getOrgIds())){
					List<EmpUserVo> empList = sysUserService.getEmpUserByOrgIds(msg.getOrgIds());
					list.addAll(empList);
				}
				
				//抄送不为空
				if(StringUtils.isNotEmpty(msg.getCopiedIds())){
					 listcc = sysUserService.getEmpUserByEmpIds(msg.getCopiedIds());
				}
			}
			
			
			MessageProducer p = new MessageProducer();
			
			for(String sendType : sendTypes){
				
				//组装邮件model
				if("email".equals(sendType) && "0".equals(msg.getEmailStatus())){
					msg.setEmailStatus("1");
					this.messageService.updateMsgFrom(msg);
					
					List<String> strs = new ArrayList<String>();
					MailModel mailModel = new MailModel();
					mailModel.setFromId(msg.getId());
					mailModel.setSubject(msg.getTitle());
					mailModel.setMc(this.messageService.getMailConfig(msg.getMailConfigId()));
					//接收地址
					List<String> emailTo = new ArrayList<String>();
					for(EmpUserVo to : list){
						if(Collections.frequency(strs, to.getEmpEmail()) < 1){
						strs.add(to.getEmpEmail());
						emailTo.add(to.getEmpEmail());
						}
					}
					String[] sto = new String[emailTo.size()];
					for(int i=0;i<emailTo.size();i++){
						sto[i] = emailTo.get(i);
					}
					mailModel.setTo(sto);
					
					//抄送地址
					List<String> emailCc = new ArrayList<String>();
					for(EmpUserVo cc : listcc){
						if(Collections.frequency(strs, cc.getEmpEmail()) < 1){//去除重复数据（如果发送邮件中已含有抄送地址则不发送）
							strs.add(cc.getEmpEmail());
							emailCc.add(cc.getEmpEmail());
						}
					}
					String[] scc = new String[emailCc.size()];
					for(int i=0;i<emailCc.size();i++){
						scc[i] = emailCc.get(i);
					}
					mailModel.setCc(scc);
					//邮件附件
					Map<String, String> param = new HashMap<String, String>();
					param.put("businessKey", msg.getId());
					List<DataVo> dataVoList = dataService.queryUploadData(param);
					mailModel.setDataVoList(dataVoList);
					mailModel.setContent(MsgUtil.getMailContent(msg, bus));
					p.send(mailModel);
				}
				//只有在短信开关开启的时候才发送短信
				if("sms".equals(sendType) && "0".equals(msg.getSmsStatus()) && "1".equals(bundle.getString("smsSwitch").trim())){
					msg.setSmsStatus("1");
					this.messageService.updateMsgFrom(msg);
					SmsMobile smsMobile = new SmsMobile();
					List<String> sms = new ArrayList<String>();
					for(EmpUserVo to : list){
						if(Collections.frequency(sms, to.getEmpPhone()) < 1){
						sms.add(to.getEmpPhone());
						}
					}
					for(EmpUserVo cc : listcc){
						if(Collections.frequency(sms, cc.getEmpPhone()) < 1){//去除重复数据（如果发送中已含有抄送地址则不发送）
							sms.add(cc.getEmpPhone());
						}
					}
					smsMobile.setFromId(msg.getId());
					smsMobile.setMobile(sms);
					smsMobile.setContent(MsgUtil.getSmsContent(msg, bus));
					p.send(smsMobile);
				}
				
				if("innerMessage".equals(sendType)){
					//判断该消息是否已经成功发送
					Map<String,String> param = new HashMap<String,String>();
					param.put("formId", msg.getId());
					param.put("type", sendType);
					List<Map<String,Object>> innerList = sysUserMybatisDao.getMsgTo(param);
					List<String> empIds = new ArrayList<String>();
					for(int i=0;i<innerList.size();i++){
						empIds.add(innerList.get(i).get("receive_id").toString());
					}
					
					InnerMessage inner = new InnerMessage();
					List<EmpUserVo> empUser = new ArrayList<EmpUserVo>();
					for(EmpUserVo to : list){
						if(Collections.frequency(empIds, to.getEmpId()) < 1){
							empIds.add(to.getEmpId());
							empUser.add(to);
						}
					}
					
					
					for(EmpUserVo cc : listcc){
						if(Collections.frequency(empIds, cc.getEmpId()) < 1){//去除重复数据（如果发送中已含有抄送地址则不发送）
							empIds.add(cc.getEmpId());
							empUser.add(cc);
						}
					}
					if(empUser.size() > 0){
						inner.setNeedReply(msg.getNeedReply());
						inner.setMsgTo(empUser);
						inner.setFromId(msg.getId());
						p.send(inner);
					}
				}
			}
		}*/
		
	}

}
