package com.xplatform.base.form.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.xplatform.base.form.dao.AppFormUserDataDao;
import com.xplatform.base.form.entity.AppFormUser;
import com.xplatform.base.form.entity.AppFormUserData;
import com.xplatform.base.form.service.AppFormUserDataService;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;

@Service("appFormUserDataService")
public class AppFormUserDataServiceImpl extends BaseServiceImpl<AppFormUserData> implements AppFormUserDataService {

	@Resource
	private AppFormUserDataDao appFormUserDataDao;
	private AjaxJson result = new AjaxJson();
	
	@Resource
	public void setBaseDao(AppFormUserDataDao appFormUserDataDao) {
		super.setBaseDao(appFormUserDataDao);
	}

	@Override
	public AjaxJson saveOrDeleteAppFormData(String formId,JSONObject jsonObject) throws BusinessException {
		String sql = "";
		
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("userList"));
		
		// 不为空则修改，为空则全部删除
		if (jsonArray.size()>0) {
			List<AppFormUserData> isExistedList = this.getFormDataList(formId);
			List<AppFormUserData> list = new ArrayList<AppFormUserData>();

/*			for (String userId : userArr) {
				AppFormUserData AppFormUserDataEntity = new AppFormUserData();
				AppFormUserDataEntity.setFormId(formId);
				AppFormUserDataEntity.setUserId(userId);
				list.add(AppFormUserDataEntity);
			}*/
			
			for (int i=0;i<jsonArray.size();i++) {
				JSONObject apudJson = (JSONObject)jsonArray.get(i);
				AppFormUserData AppFormUserDataEntity = new AppFormUserData();
				AppFormUserDataEntity.setFormId(formId);
				AppFormUserDataEntity.setUserId(apudJson.getString("userId"));
				list.add(AppFormUserDataEntity);
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
						this.appFormUserDataDao.delete(isExistedList.get(i));
					}

				}

			}
			if (list.size() > 0) {
				this.batchSave(list);
				result.setMsg("保存成功");
				result.setSuccess(true);
			}

		} else {
			if(StringUtil.isNotEmpty(formId)){
				sql = "delete from AppFormUserData where formId=?";
				this.appFormUserDataDao.executeHql(sql,formId);
	
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
	public List<AppFormUserData> getFormDataList(String formId) throws BusinessException {
		String sql = "from AppFormUserData where formId=?";
		return this.appFormUserDataDao.findHql(sql, formId);
	}

	@Override
	public List<Map<String,Object>> queryFormDataList(String formId) throws BusinessException {
		String sql = "select a.id,a.formId,a.userId,b.portrait,b.name from t_app_form_user_data a left join t_org_user b on a.userId=b.id where a.formId=?";
		return this.appFormUserDataDao.findForJdbc(sql, formId);
	}

	@Override
	public AppFormUserData getUserData(String formId, String userId) throws BusinessException {
		String sql = "from AppFormUserData where formId=? and userId=?";
		return this.appFormUserDataDao.findUniqueByHql(sql, formId,userId);
	}	
}
