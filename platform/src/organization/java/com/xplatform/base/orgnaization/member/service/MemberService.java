package com.xplatform.base.orgnaization.member.service;

import java.io.Serializable;
import java.text.ParseException;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.orgnaization.member.entity.MemberEntity;
import com.xplatform.base.orgnaization.member.entity.TOrgUser;
import com.xplatform.base.orgnaization.member.tools.ServiceException;

public interface MemberService{
	
 	public <T> void delete(T entity);
 	
 	public <T> Serializable save(T entity);
 	
 	public <T> void saveOrUpdate(T entity);
 	
 	MemberEntity getEntity(Class<MemberEntity> class1, String id);
 	
 	MemberEntity get(Class<MemberEntity> class1, String id);
 	
 	DataGridReturn getDataGridReturn(CriteriaQuery cq, boolean b);
 	
 	/**
	 * 根据用户名查询会员信息
	 * @param code
	 * @return
	 */
	public MemberEntity findByLoginName(String loginName);
	
	/**
     * 处理注册
     */
  
    public void processregister(TOrgUser tou,String url);
    
    /**
     * 取得TOrgUser实体
     */
    TOrgUser getTOrgUserEntity(Class<TOrgUser> class1, String id);
    
    /**
     * 处理激活
     * @throws ParseException 
     */
      ///传递激活码和email过来
    public void processActivate(MemberEntity memberEntity)throws ServiceException, ParseException;
}
