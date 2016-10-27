package com.xplatform.base.form.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.xplatform.base.form.dao.AppFormApproveUserDao;
import com.xplatform.base.form.entity.AppFormApproveUser;
import com.xplatform.base.form.service.AppFormApproveUserService;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;

@Service("appFormApproveUserService")
public class AppFormApproveUserServiceImpl extends BaseServiceImpl<AppFormApproveUser> implements AppFormApproveUserService {

	@Resource
	private AppFormApproveUserDao appFormApproveUserDao;
	private AjaxJson result = new AjaxJson();
	
	@Resource
	public void setBaseDao(AppFormApproveUserDao appFormApproveUserDao) {
		super.setBaseDao(appFormApproveUserDao);
	}

	@Override
	public AjaxJson saveOrDeleteAFAU(String formId, JSONObject jsonObject) throws BusinessException {
		String sql = "";
		
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("userList"));
		
		// 不为空则修改，为空则全部删除
		if (jsonArray.size()>0) {
			//String[] userArr = userIds.split(",");
			//String[] userNameArr = userNames.split(",");
			//String[] orderbyArr = orderbys.split(",");
				sql = "delete from AppFormApproveUser where formId=?";
				this.appFormApproveUserDao.executeHql(sql,formId);

				List<AppFormApproveUser> list = new ArrayList<AppFormApproveUser>();
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject afauJson = (JSONObject)jsonArray.get(i);
					AppFormApproveUser appFormApproveUser = new AppFormApproveUser();
					appFormApproveUser.setUserId(afauJson.getString("id"));
					appFormApproveUser.setFormId(formId);
					appFormApproveUser.setOrderby(afauJson.getInt("orderby"));
					appFormApproveUser.setType(afauJson.getString("type"));
					appFormApproveUser.setUserName(afauJson.getString("name"));
					list.add(appFormApproveUser);
				}
				this.appFormApproveUserDao.batchSave(list);
				result.setMsg("保存成功");
				result.setSuccess(true);

		} else {
			if(StringUtil.isNotEmpty(formId)){
				sql = "delete from AppFormApproveUser where formId=?";
				this.appFormApproveUserDao.executeHql(sql,formId);
	
				result.setMsg("保存成功");
				result.setSuccess(true);
			}else{
				result.setMsg("保存失败");
				result.setSuccess(false);
			}
		}

		return result;
	}

	@Override
	public List<AppFormApproveUser> getAFAUList(String formId) throws BusinessException {
		String sql = "from AppFormApproveUser where formId=?";
		return this.appFormApproveUserDao.findHql(sql, formId);
	}

	@Override
	public List<Map<String,Object>> queryAFAUList(String formId) throws BusinessException {
		String sql = "select * from "
					+"( SELECT a.formId,a.userId as id,a.orderby,a.type,b.portrait,b.name"
					+" FROM "
					+" t_app_form_approve_user a "
					+" LEFT JOIN t_org_user b ON a.userId = b.id "
					+" WHERE "
					+" a.formId ='"+formId+"' "
					+" and a.type = 'user' "
					+" UNION ALL "
					+" SELECT a.formId,a.userId as id,a.orderby,a.type,b.name as portrait,b.name "
					+" FROM "
					+" t_app_form_approve_user a "
					+" LEFT JOIN t_org_role b ON a.userId = b.id "
					+" WHERE "
					+" a.formId ='"+formId+"' " 
					+" and a.type = 'role' "
					+" ) n "
					+" ORDER BY "
					+" n.orderby ASC";
		//String sql = "select a.id,a.formId,a.userId,a.orderby,a.type,b.portrait,b.name from t_app_form_approve_user a left join t_org_user b on a.userId=b.id where a.formId=? ORDER BY a.orderby ASC";
		return this.appFormApproveUserDao.findForJdbc(sql);
	}
}
