package com.xplatform.base.orgnaization.member.service.impl;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.common.service.MailService;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.member.dao.MemberDao;
import com.xplatform.base.orgnaization.member.entity.MemberEntity;
import com.xplatform.base.orgnaization.member.entity.TOrgUser;
import com.xplatform.base.orgnaization.member.service.MemberService;
import com.xplatform.base.orgnaization.member.tools.ServiceException;


@Service("memberService")
public class MemberServiceImpl implements MemberService {

    @Resource
	private MemberDao memberDao;
	
	@Resource
	private MailService mailService;

	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}


 	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

 	@Action(moduleCode="memberManager",description="会员删除",detail="会员${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public <T> void delete(T entity) {
		memberDao.delete(entity);
 	}
 	
	@Action(moduleCode="memberManager",description="会员新增",detail="会员${name}新增成功", execOrder = ActionExecOrder.BEFORE)
 	public <T> Serializable save(T entity) {
 		return memberDao.save(entity);
 	}
 	
	@Action(moduleCode="memberManager",description="会员修改",detail="会员${name}修改成功", execOrder = ActionExecOrder.BEFORE)
 	public <T> void saveOrUpdate(T entity) {
 		//super.saveOrUpdate(entity);
 		//执行更新操作配置的sql增强
 		//this.doUpdateSql((MemberEntity)entity);
 		
 		memberDao.saveOrUpdate(entity);		
 	}
 	
 	@Override
	public DataGridReturn getDataGridReturn(CriteriaQuery cq, boolean b) {
 		return memberDao.getDataGridReturn(cq, b);
	}

	@Override
	public MemberEntity getEntity(Class<MemberEntity> class1, String id) {
		return memberDao.getEntity(class1, id);
	}

	@Override
	public MemberEntity get(Class<MemberEntity> class1, String id) {
		return memberDao.get(class1, id);
	}
	
	/**
	 * 根据用户名查询会员信息
	 * @param code
	 * @return
	 */
	public MemberEntity findByLoginName(String loginName){
		String sql = "from MemberEntity where login_name = ?";
		List<MemberEntity> parameterEntitieList = memberDao.findHql(sql, new Object[]{loginName});
		MemberEntity memberEntity = null;
		if(parameterEntitieList.size() > 0)
			memberEntity = parameterEntitieList.get(0);
		return memberEntity;
	}
	
	/**
     * 处理注册
     */
  
    public void processregister(TOrgUser tou,String url){
    	///邮件的内容
        StringBuffer sb=new StringBuffer("点击下面链接激活账号，48小时生效，否则重新注册账号，请尽快激活！<br>");
        sb.append(url); 
        sb.append("?activate&sid=");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sid = tou.getId()+","+tou.getEmail()+","+sdf.format(date);
        sb.append(StringUtil.encodeToDes3(sid));
        //发送邮件
        mailService.sendMailOffLine(tou.getEmail(), "激活邮件", sb.toString(), null, 1);
    }
    
    /**
     * 处理激活
     * @throws ParseException 
     */
      ///传递激活码和email过来
    public void processActivate(MemberEntity memberEntity)throws ServiceException, ParseException{
    	MemberEntity member = this.findByLoginName(memberEntity.getLoginName());
    	//验证用户是否存在
    	if(member != null){
    		//验证用户激活状态
    		if(member.getIsActived().equals("0")){
    			//没激活
                //Date currentTime = new Date();//获取当前时间
              //验证链接是否过期
                
    		}else {
                throw new ServiceException("邮箱已激活，请登录！");  
            } 
    	}
    }

	@Override
	public TOrgUser getTOrgUserEntity(Class<TOrgUser> class1, String id) {
		return memberDao.getEntity(class1, id);
	}
 	
}