package com.xplatform.base.form.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;




import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.form.dao.AppFormUserShareDao;
import com.xplatform.base.form.entity.AppFormUserShareEntity;
import com.xplatform.base.form.service.AppFormUserShareService;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.user.vo.UserTypeVo;
import com.xplatform.base.platform.common.service.SysUserService;

import jodd.util.StringUtil;

@Service("appFormUserShareService")
public class AppFormUserShareServiceImpl extends BaseServiceImpl<AppFormUserShareEntity> implements AppFormUserShareService {
	private static final Logger logger = Logger.getLogger(AppFormUserShareServiceImpl.class);
	@Resource
	private AppFormUserShareDao appFormUserShareDao;
	@Resource
	private SysUserService sysUserService;
	
	@Resource
	public void setBaseDao(AppFormUserShareDao appFormUserShareDao) {
		super.setBaseDao(appFormUserShareDao);
	}
	
	public List<Map<String,Object>> queryShareFormList(String userId){
		String orgIds = this.sysUserService.getOrgIdsByUserId(userId);
		String orgSql = "";
		if(StringUtil.isNotEmpty(orgIds) && !StringUtil.equals("'"+OrgnaizationEntity.DEFAULT_ID+"'", userId)){
			orgSql = " UNION ALL "
				+" SELECT a.id,a.name,a.code,a.parentId,a.createTime,a.description,a.url,a.content,a.isApp,a.isEdit,a.type_id,a.fieldJson,a.version,a.logo,a.`status`,a.isFlow,a.notifyType,a.viewType "
				+" FROM "
				+" t_flow_form a,t_app_form_user_share b "
				+" WHERE "
				+" a.ID = b.formId AND b.userId IN ( "
				+orgIds
				+" ) "
				+" AND b.type = 'org' ";
		}
		String sql = "SELECT DISTINCT * "
				+" FROM "
				+" (SELECT a.id,a.name,a.code,a.parentId,a.createTime,a.description,a.url,a.content,a.isApp,a.isEdit,a.type_id,a.fieldJson,a.version,a.logo,a.`status`,a.isFlow,a.notifyType,a.viewType "
				+" FROM t_flow_form a,t_app_form_user_share b "
				+" WHERE "
				+" a.ID = b.formId AND b.userId = '"+userId+"' AND b.type = 'user' "
				+orgSql
				+" UNION ALL "
				+" SELECT t.id,t.name,t.code,t.parentId,t.createTime,t.description,t.url,t.content,t.isApp,t.isEdit,t.type_id,t.fieldJson,t.version,t.logo,t.`status`,t.isFlow,t.notifyType,t.viewType "
				+" FROM "
				+" t_flow_form t,t_app_form_user_share b "
				+" WHERE "
				+" b.userId IN ( "
				+" SELECT roleId "
				+" FROM "
				+" t_org_user_role "
				+" WHERE "
				+" userId = '"+userId+"' "
				+" ) "
				+" AND t.id = b.formId AND b.type = 'role' "
				+" ) n "
				+" WHERE "
				+" n.parentId = '-1' "
				+" AND n.version = ( "
				+" SELECT "
				+" max(m.version) "
				+" FROM "
				+" t_flow_form m "
				+" WHERE "
				+" m.code = n.code "
				+" ) "
				+" ORDER BY "
				+"n.createTime DESC";
		
		return this.appFormUserShareDao.findForJdbc(sql);
	}
	
	public List<AppFormUserShareEntity> queryShareUser(String userId,String formId){
		Map<String,Object> p=new HashMap<String,Object>();
		p.put("createUserId", userId);
		p.put("formId", formId);
		return appFormUserShareDao.findByPropertys(p);
	}
	
	@Override
	public void updateUsers(String formId,String userId,List<UserTypeVo> users) throws BusinessException{
		try {
			//所有的分享的模版
			Map<String,Object> p=new HashMap<String,Object>();
			//p.put("userId", userId);
			p.put("formId", formId);
			p.put("createUserId", userId);
			List<AppFormUserShareEntity> userShareList = appFormUserShareDao.findByPropertys(p);
			Map<String,AppFormUserShareEntity> set = new HashMap<String,AppFormUserShareEntity>();
			if(!userShareList.isEmpty()){
				for(AppFormUserShareEntity userShare : userShareList){
					set.put(userShare.getId(), userShare);
				}
			}
			
			updateUserShareCompare(userId,set, formId, users,userShareList);
		} catch (Exception e) {
			logger.error("员工岗位分配失败");
			throw new BusinessException("员工岗位分配失败");
		}
	}
	
	private void updateUserShareCompare(String userId,Map<String,AppFormUserShareEntity> set, String formId, List<UserTypeVo> users,List<AppFormUserShareEntity> userShareList) {
		List<AppFormUserShareEntity> saveList = new ArrayList<AppFormUserShareEntity>();
		List<AppFormUserShareEntity> deleteList = new ArrayList<AppFormUserShareEntity>();
		for(UserTypeVo userType:users){
			boolean flag=true;
			for(AppFormUserShareEntity userShare:userShareList){
				if(StringUtil.equals(userType.getId(), userShare.getUserId()) && StringUtil.equals(userType.getType(), userShare.getType())
						&& StringUtil.equals(userId, userShare.getCreateUserId())){
					set.remove(userShare.getId());
					flag=false;
					break;
				}
			}
			if(flag){
				AppFormUserShareEntity userShare = new AppFormUserShareEntity();
				userShare.setUserId(userType.getId());
				userShare.setFormId(formId);
				userShare.setType(userType.getType());
				userShare.setName(userType.getName());
				saveList.add(userShare);
			}
		}
		
			
			/*for(AppFormUserShareEntity userShare:userShareList){
				boolean flag=true;
				UserTypeVo user=null;
				for(UserTypeVo userType:users){
					if(StringUtil.equals(userType.getId(), userShare.getUserId()) && StringUtil.equals(userType.getType(), userShare.getType())
							&& StringUtil.equals(userId, userShare.getCreateUserId())){
						set.remove(userShare.getId());
						flag=false;
						break;
					}
				}
				if(flag){
					AppFormUserShareEntity userShare = new AppFormUserShareEntity();
					userShare.setUserId(userType.getId());
					userShare.setFormId(formId);
					userShare.setType(userType.getType());
					userShare.setName(userType.getName());
					saveList.add(userShare);
				}
			}*/
			
		// 构造删除数据
		deleteList=new ArrayList<AppFormUserShareEntity>(set.values());
		this.appFormUserShareDao.batchSave(saveList);
		this.appFormUserShareDao.deleteAllEntitie(deleteList);
	}
}
