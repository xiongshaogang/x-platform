package com.xplatform.base.form.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.xplatform.base.form.dao.AppFormUserDao;
import com.xplatform.base.form.dao.AppFormUserDataDao;
import com.xplatform.base.form.entity.AppFormUser;
import com.xplatform.base.form.service.AppFormUserService;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.orgnaization.service.OrgnaizationService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.role.service.RoleService;
import com.xplatform.base.orgnaization.user.dao.UserDao;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.utils.ClientUtil;

@Service("appFormUserService")
public class AppFormUserServiceImpl extends BaseServiceImpl<AppFormUser> implements AppFormUserService {

	@Resource
	private AppFormUserDao appFormUserDao;
	@Resource
	private RoleService roleService;
	@Resource
	private OrgnaizationService orgnaizationService;
	@Resource
	private UserDao userDao;
	private AjaxJson result = new AjaxJson();
	
	@Resource
	public void setBaseDao(AppFormUserDao appFormUserDao) {
		super.setBaseDao(appFormUserDao);
	}

	@Override
	public AjaxJson saveOrDeleteAppForm(String formId,JSONObject jsonObject) throws BusinessException {
		String sql = "";
		
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("userList"));

		// 不为空则修改，为空则全部删除
		if (jsonArray.size()>0) {
			//String[] userArr = userIds.split(",");
			List<AppFormUser> isExistedList = this.getAppFormList(formId);
			List<AppFormUser> list = new ArrayList<AppFormUser>();

		/*	for (String userId : userArr) {
				AppFormUser appFormUserEntity = new AppFormUser();
				appFormUserEntity.setFormId(formId);
				appFormUserEntity.setUserId(userId);
				list.add(appFormUserEntity);
			}*/
			for(int i=0;i<jsonArray.size();i++){
				JSONObject apuJson = (JSONObject)jsonArray.get(i);

				if(StringUtil.isNotEmpty(apuJson.optString("id"))){
					AppFormUser appFormUserEntity = new AppFormUser();
					if(StringUtil.isNotEmpty(apuJson.optString("type"))){
						if("user".equals(apuJson.getString("type"))){
							//用户
							UserEntity user = this.userDao.getUser(apuJson.getString("id"));
							if(user != null){
								appFormUserEntity.setUserId(apuJson.getString("id"));
								appFormUserEntity.setType(apuJson.getString("type"));
							}
						}else if("org".equals(apuJson.getString("type"))){
							//机构部门类型
							OrgnaizationEntity org = this.orgnaizationService.get(apuJson.getString("id"));
							if(org != null){
								appFormUserEntity.setUserId(apuJson.getString("id"));
								appFormUserEntity.setType(apuJson.getString("type"));
							}
						}else{
							//为role类型
							RoleEntity role = this.roleService.get(apuJson.getString("id"));
							if(role != null){
								appFormUserEntity.setUserId(apuJson.getString("id"));
								appFormUserEntity.setType(apuJson.getString("type"));
							}
						}
						appFormUserEntity.setFormId(formId);
						appFormUserEntity.setViewStatus(1);
						list.add(appFormUserEntity);
					}else{
						appFormUserEntity.setUserId(apuJson.getString("id"));
						OrgnaizationEntity org = this.orgnaizationService.get(apuJson.getString("id"));
						if(org != null){
							appFormUserEntity.setType("org");
						}else{
							RoleEntity role = this.roleService.get(apuJson.getString("id"));
							if(role != null){
								appFormUserEntity.setType("role");
							}else{
								appFormUserEntity.setType("user");
							}
						}
						list.add(appFormUserEntity);
					}
					

				}
				
			}
			// 如果当前数据库中存在数据，则进行比对添加
			if (isExistedList != null) {

				for (int i = 0; i < isExistedList.size(); i++) {
					boolean flag = false;  //判断user是否再数据库中存在
					for (int j = 0; j < list.size(); j++) {
						if (list.get(j).getUserId().equals(isExistedList.get(i).getUserId())) {
							// userId当前存在，则删除掉isExistedList里边的元素
							flag = true;
							list.remove(j);
							if(list.size() == 0){
								break;
							}
						}
					}
					//当前数据库对象在userids中不存在，执行删除
					if(!flag){
						this.appFormUserDao.delete(isExistedList.get(i));
					}

				}

			}
			if (list.size() > 0) {
				//判断list里是否含有用户本身的id，没有则加上
				String userId = ClientUtil.getUserId();
				boolean isExist = false;
				for(AppFormUser afu : list){
					if(afu.getUserId().equals(userId)){
						isExist = true;
						break;
					}
				}
				if(!isExist){
					AppFormUser appFormUserEntity = new AppFormUser();
					appFormUserEntity.setFormId(formId);
					appFormUserEntity.setUserId(userId);
					appFormUserEntity.setViewStatus(1);
					appFormUserEntity.setType("user");
					list.add(appFormUserEntity);
				}
				this.batchSave(list);
				result.setMsg("保存成功");
				result.setSuccess(true);
			}

		} else {
			if(StringUtil.isNotEmpty(formId)){
				sql = "delete from AppFormUser where formId = ?";
				this.appFormUserDao.executeHql(sql,formId);
				AppFormUser appFormUser = new AppFormUser();
				appFormUser.setFormId(formId);
				appFormUser.setUserId(ClientUtil.getUserId());
				appFormUser.setViewStatus(1);
				appFormUser.setType("user");
				AppFormUser isExist = this.getAppFormUser(formId, ClientUtil.getUserId());
				if(isExist == null){
					this.save(appFormUser);
				}
				result.setMsg("保存成功");
				result.setSuccess(true);
			}else{
				result.setMsg("保存失败，参数有误");
				result.setSuccess(false);
			}
			
		}
		return result;
	}

	@Override
	public List<Map<String,Object>> queryAppFormList(String formId) throws BusinessException {
		String sql = "select * from ("
					+"select a.formId,a.userId as id,a.type,b.portrait,a.createTime,b.name " 
					+"from t_app_form_user a left join t_org_user b on a.userId=b.id "
					+"where a.formId='"+formId+"' "
					+" and a.type = 'user' "
					+" UNION ALL "
					+" SELECT a.formId,a.userId as id,a.type,b.name as portrait,a.createTime,b.name "
					+" FROM "
					+" t_app_form_user a "
					+" LEFT JOIN t_org_role b ON a.userId = b.id "
					+" WHERE "
					+" a.formId = '"+formId+"' "
					+" and a.type = 'role' "
					+" UNION ALL "
					+" SELECT a.formId,a.userId as id,a.type,b.name as portrait,a.createTime,b.name "
					+" FROM "
					+" t_app_form_user a "
					+" LEFT JOIN t_org_orgnaization b ON a.userId = b.id "
					+" WHERE "
					+" a.formId = '"+formId+"' "
					+" and a.type = 'org'"
					+") t"
					+" ORDER BY t.createTime ASC ";
		//String sql = "select a.id,a.formId,a.userId,a.type,b.portrait,b.name from t_app_form_user a left join t_org_user b on a.userId=b.id where a.formId=?";
		return this.appFormUserDao.findForJdbc(sql);
	}

	@Override
	public List<AppFormUser> getAppFormList(String formId) throws BusinessException {
		String hql = "from AppFormUser where formId=?";
		return this.appFormUserDao.findHql(hql, formId);
	}

	@Override
	public AppFormUser getAppFormUser(String formId, String userId) throws BusinessException {
		String hql = "from AppFormUser where formId=? and userId=?";
		return this.userDao.findUniqueByHql(hql, formId,userId);
	}	
}
